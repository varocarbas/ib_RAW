package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.data;
import accessory.db;
import accessory.db_where;

import accessory.strings;
import ib._order;

public abstract class orders 
{
	public static final String SOURCE = common.SOURCE_ORDERS;
	public static final String SOURCE_OLD = common.SOURCE_ORDERS_OLD;

	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String START = common.FIELD_START;
	public static final String START2 = common.FIELD_START2;
	public static final String STOP = common.FIELD_STOP;
	public static final String IS_MARKET = common.FIELD_IS_MARKET;
	public static final String TYPE_PLACE = common.FIELD_TYPE_PLACE;
	public static final String TYPE_MAIN = common.FIELD_TYPE_MAIN;
	public static final String TYPE_SEC = common.FIELD_TYPE_SEC;
	public static final String QUANTITY = common.FIELD_QUANTITY;

	private static HashMap<String, String> _field_cols = new HashMap<String, String>();
	private static String[] _fields = null;
	private static String[] _cols = null;
	
	private static HashMap<String, String> _statuses = new HashMap<String, String>();
	private static HashMap<String, String> _type_orders = new HashMap<String, String>();
	private static HashMap<String, String> _type_places = new HashMap<String, String>();
	private static HashMap<String, String> _type_updates = new HashMap<String, String>();
	private static HashMap<String, String> _type_cancels = new HashMap<String, String>();
	
	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String symbol_) { return common.exists(SOURCE, get_where_symbol(symbol_)); }

	public static boolean exists_status(String status_) { return common.exists(SOURCE, get_where_status(status_)); }
	
	public static boolean exists(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, null); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || common.exists(SOURCE, get_where_active())); }
	
	public static boolean is_active(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, get_where_active()); }

	public static boolean is_active(int order_id_, double stop_, double start_) { return is_active(order_id_, stop_, start_, ib.common.WRONG_PRICE); }
	
	public static boolean is_active(int order_id_, double stop_, double start_, double start2_) 
	{
		String where = orders.get_where_active();
		
		if (ib.common.price_is_ok(stop_)) where = common.join_wheres(where, common.get_where(SOURCE, STOP, Double.toString(ib.common.adapt_price(stop_)), false));
		if (ib.common.price_is_ok(start_)) where = common.join_wheres(where, common.get_where(SOURCE, START, Double.toString(ib.common.adapt_price(start_)), false));
		if (ib.common.price_is_ok(start2_)) where = common.join_wheres(where, common.get_where(SOURCE, START2, Double.toString(ib.common.adapt_price(start2_)), false));
			
		return common.exists(SOURCE, where);
	}

	public static boolean is_inactive(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, get_where_inactive()); }

	public static _order get_to_order(int order_id_main_, boolean is_quick_) { return to_order(get(order_id_main_, is_quick_), is_quick_); }
	
	public static _order get_to_order(String symbol_, boolean is_quick_) { return to_order(get(symbol_, is_quick_), is_quick_); }

	public static String get_symbol(int order_id_main_, boolean is_quick_) 
	{ 
		String symbol = common.get_string(SOURCE, get_field_col(SYMBOL, is_quick_), get_where_order_id(order_id_main_), is_quick_); 
	
		return (strings.are_equal(symbol, db.WRONG_STRING) ? strings.DEFAULT : symbol);
	}

	public static int get_order_id(String symbol_, boolean is_main_, boolean is_quick_) 
	{ 
		int order_id = common.get_int(SOURCE, get_field_col((is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), is_quick_), get_where_symbol(symbol_), is_quick_); 
	
		return (order_id == db.WRONG_INT ? ib.common.WRONG_ORDER_ID : order_id);
	}

	public static double get_quantity(int order_id_main_, boolean is_quick_) 
	{ 
		double quantity = common.get_decimal(SOURCE, get_field_col(QUANTITY, is_quick_), get_where_order_id(order_id_main_), is_quick_); 
	
		return (quantity == db.WRONG_DECIMAL ? ib.common.WRONG_QUANTITY : quantity);
	}

	public static int get_order_id_main(int order_id_sec_, boolean is_quick_) 
	{ 
		int order_id_main = common.get_int(SOURCE, get_field_col(ORDER_ID_MAIN, is_quick_), get_where_order_id(order_id_sec_, false), is_quick_); 
		
		return (order_id_main == db.WRONG_INT ? ib.common.WRONG_ORDER_ID : order_id_main);
	}

	public static String get_field_update(String type_update_) 
	{
		String field = strings.DEFAULT;
		
		String type_update = ib.orders.check_update(type_update_);
		if (!strings.is_ok(type_update)) return field;
	
		if (ib.orders.is_update_market(type_update)) field = IS_MARKET;
		else if (ib.orders.is_update_stop(type_update)) field = STOP;
		else if (ib.orders.is_update_start(type_update)) field = START;
		else if (ib.orders.is_update_start2(type_update)) field = START2;

		return field; 
	}

	public static HashMap<String, String> get(int order_id_main_, boolean is_quick_) { return common.get_vals(SOURCE, (is_quick_ ? get_cols() : get_fields()), get_where_order_id(order_id_main_, true), is_quick_); }

	public static HashMap<String, String> get(String symbol_, boolean is_quick_) { return common.get_vals(SOURCE, (is_quick_ ? get_cols() : get_fields()), get_where_symbol(symbol_), is_quick_); }

	public static String get_status(String symbol_) { return get_status_common(get_where_symbol(symbol_)); }
	
	public static String get_status(int order_id_main_) { return get_status_common(get_where_order_id(order_id_main_)); }
	
	public static boolean insert_update(_order order_, boolean is_quick_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;
		
		output = common.insert_update(SOURCE, to_hashmap(order_, false, is_quick_), get_where_symbol(order_.get_symbol()), is_quick_);
		
		sync_tables(order_);
		
		return output;
	}

	public static boolean update(_order order_, boolean is_quick_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;

		output = common.update_where(SOURCE, to_hashmap(order_, false, is_quick_), get_where_order_id(order_.get_id_main()), is_quick_);

		sync_tables(order_);
		
		return output;
	}
	
	public static boolean update_status(int order_id_main_, String status_, boolean is_quick_) 
	{ 
		if (order_id_main_ <= ib.common.WRONG_ORDER_ID) return false;

		String key = orders.get_key_from_status(status_);	
		if (!strings.is_ok(key)) return false;

		return update_status_internal(order_id_main_, key, is_quick_); 
	}
	
	public static boolean deactivate(int order_id_main_, boolean is_quick_) { return update_status(order_id_main_, ib.orders.STATUS_INACTIVE, is_quick_); }

	public static _order to_order(HashMap<String, String> db_, boolean is_quick_)
	{
		String type_place = get_type_place_from_key((String)arrays.get_value(db_, get_field_col(TYPE_PLACE, is_quick_)));
		String symbol = (String)arrays.get_value(db_, get_field_col(SYMBOL, is_quick_));
		
		double quantity = strings.to_number_decimal((String)arrays.get_value(db_, get_field_col(QUANTITY, is_quick_)));
		double stop = strings.to_number_decimal((String)arrays.get_value(db_, get_field_col(STOP, is_quick_))); 

		double start = strings.to_number_decimal((String)arrays.get_value(db_, get_field_col(START, is_quick_)));
		double start2 = strings.to_number_decimal((String)arrays.get_value(db_, get_field_col(START2, is_quick_)));
		
		int id_main = strings.to_number_int((String)arrays.get_value(db_, get_field_col(ORDER_ID_MAIN, is_quick_)));

		return new _order(type_place, symbol, quantity, stop, start, start2, id_main);
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> to_hashmap(_order order_) { return (HashMap<String, Object>)to_hashmap(order_, false, false); }
	
	public static Object to_hashmap(_order order_, boolean only_basic_, boolean is_quick_)
	{
		if (order_ == null) return (is_quick_ ? new HashMap<String, String>() : new HashMap<String, Object>());

		Object db = add_to_vals(ORDER_ID_MAIN, order_.get_id_main(), null, is_quick_);
		db = add_to_vals(ORDER_ID_SEC, order_.get_id_sec(), db, is_quick_);
		db = add_to_vals(TYPE_PLACE, get_key_from_type_place(order_.get_type_place()), db, is_quick_);
		db = add_to_vals(SYMBOL, order_.get_symbol(), db, is_quick_);
		db = add_to_vals(QUANTITY, order_.get_quantity(), db, is_quick_);
		db = add_to_vals(STOP, order_.get_stop(), db, is_quick_);
		
		boolean is_market = _order.is_market(order_);

		if (is_market) db = add_to_vals(IS_MARKET, is_market, db, is_quick_);
		else 
		{
			if (ib.common.price_is_ok(order_.get_start())) db = add_to_vals(START, order_.get_start(), db, is_quick_);
			if (ib.common.price_is_ok(order_.get_start2())) db = add_to_vals(START2, order_.get_start2(), db, is_quick_);
		}

		if (!only_basic_)
		{
			db = add_to_vals(TYPE_MAIN, order_.get_type_main(), db, is_quick_);
			db = add_to_vals(TYPE_SEC, order_.get_type_sec(), db, is_quick_);
		}
		
		return db;
	}

	public static String get_key_from_type_place(String type_place_) 
	{ 
		if (_type_places.size() == 0) populate_type_places();

		return common.get_key_from_type(type_place_, _type_places);
	}
		
	public static String get_type_place_from_key(String key_) 
	{ 
		if (_type_places.size() == 0) populate_type_places();

		return common.get_type_from_key(key_, _type_places);
	}

	public static String get_key_from_type_update(String type_update_) 
	{ 
		if (_type_updates.size() == 0) populate_type_updates();

		return common.get_key_from_type(type_update_, _type_updates);
	}
		
	public static String get_type_update_from_key(String key_) 
	{ 
		if (_type_updates.size() == 0) populate_type_updates();

		return common.get_type_from_key(key_, _type_updates);
	}

	public static String get_key_from_type_cancel(String type_cancel_) 
	{ 
		if (_type_cancels.size() == 0) populate_type_cancels();

		return common.get_key_from_type(type_cancel_, _type_cancels);
	}
		
	public static String get_type_cancel_from_key(String key_) 
	{ 
		if (_type_cancels.size() == 0) populate_type_cancels();

		return common.get_type_from_key(key_, _type_cancels);
	}

	public static String get_key_from_type_order(String type_order_) 
	{ 
		if (_type_orders.size() == 0) populate_type_orders();

		return common.get_key_from_type(type_order_, _type_orders);
	}
		
	public static String get_type_order_from_key(String key_) 
	{ 
		if (_type_orders.size() == 0) populate_type_orders();

		return common.get_type_from_key(key_, _type_orders);
	}

	public static String get_key_from_status(String status_) 
	{ 
		if (_statuses.size() == 0) populate_statuses();

		return common.get_key_from_type(status_, _statuses);
	}
	
	public static String get_status_from_key(String key_) 
	{ 
		if (_statuses.size() == 0) populate_statuses();

		return common.get_type_from_key(key_, _statuses);
	}
	
	public static boolean order_was_updated(int order_id_, String type_, boolean is_quick_) { return order_was_updated(order_id_, type_, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE, is_quick_); }

	public static boolean order_was_updated(int order_id_, String type_, double stop_, double start_, boolean is_quick_) { return order_was_updated(order_id_, type_, stop_, start_, ib.common.WRONG_PRICE, is_quick_); }
	
	public static boolean order_was_updated(int order_id_main_, String type_, double stop_, double start_, double start2_, boolean is_quick_)
	{
		boolean output = false;
		if (!ib.orders.update_is_ok(type_, stop_, start_, start2_) || !is_active(order_id_main_, true)) return output;
		
		HashMap<String, String> vals = get(order_id_main_, is_quick_);
		if (!arrays.is_ok(vals)) return output;

		if (ib.orders.is_update_market(type_)) output = (boolean)db.adapt_output(vals.get(get_field_col(db_ib.orders.IS_MARKET, is_quick_)), data.BOOLEAN);
		else
		{
			String field_col = strings.DEFAULT;
			
			if (ib.orders.is_update_stop(type_)) field_col = get_field_col(STOP, is_quick_);
			else if (ib.orders.is_update_start(type_)) field_col = get_field_col(START, is_quick_);
			else if (ib.orders.is_update_start2(type_)) field_col = get_field_col(START2, is_quick_);

			output = (ib.orders.get_update_val(type_, stop_, start_, start2_) == common.adapt_price(Double.parseDouble(vals.get(field_col))));
		}
		
		return output;
	}

	public static ArrayList<HashMap<String, String>> get_all_active(String[] fields_cols_, boolean is_quick_) { return get_all_common(fields_cols_, ib.orders.STATUS_ACTIVE, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_filled(String[] fields_cols_, boolean is_quick_) { return get_all_common(fields_cols_, ib.orders.STATUS_FILLED, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_submitted(String[] fields_cols_, boolean is_quick_) { return get_all_common(fields_cols_, ib.orders.STATUS_SUBMITTED, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_common(String[] fields_cols_, String status_, boolean is_quick_) 
	{ 
		String status = ib.orders.check_status(status_);
		if (!strings.is_ok(status)) return null;
		
		String where = null;
		if (status.equals(ib.orders.STATUS_ACTIVE)) where = get_where_active();
		else where = get_where_status(status);
		
		return common.get_all_vals(SOURCE, fields_cols_, where, is_quick_); 
	}

	public static int get_highest_order_id(boolean is_quick_)
	{
		int output = ib.orders.MIN_ORDER_ID;
		
		String[] field_cols = new String[] { orders.get_field_col(ORDER_ID_MAIN, is_quick_), orders.get_field_col(ORDER_ID_SEC, is_quick_) };
			
		ArrayList<HashMap<String, String>> all = common.get_all_vals(SOURCE, field_cols, null, is_quick_);
		if (!arrays.is_ok(all)) return output;
		
		for (HashMap<String, String> item: all)
		{
			for (String field_col: field_cols) 
			{ 
				int order_id = Integer.parseInt(item.get(field_col));
				if (order_id > output) output = order_id; 
			}
		}
		
		return output;
	}

	public static String get_field_col(String field_, boolean is_quick_) { return (is_quick_ ? get_col(field_) : field_); }

	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String field_, Object val_, Object vals_, boolean is_quick_)
	{
		Object output = null;
		
		if (is_quick_)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(get_col(field_), accessory.db.adapt_input(val_));
		
			output = vals;
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_, val_);
			
			output = vals;
		}
		
		return output;
	}
	
	private static void populate_type_places()
	{
		String[] types = new String[] { ib.orders.PLACE_MARKET, ib.orders.PLACE_STOP, ib.orders.PLACE_LIMIT, ib.orders.PLACE_STOP_LIMIT };
	
		for (String type: types) { _type_places.put(type, get_key_from_type_place_internal(type)); }
	}
	
	private static void populate_type_updates()
	{
		String[] types = new String[] { ib.orders.UPDATE_START_VALUE, ib.orders.UPDATE_START_MARKET, ib.orders.UPDATE_START2_VALUE, ib.orders.UPDATE_STOP_VALUE, ib.orders.UPDATE_STOP_MARKET };
	
		for (String type: types) { _type_updates.put(type, get_key_from_type_update_internal(type)); }
	}
	
	private static void populate_type_cancels()
	{
		String[] types = new String[] { ib.orders.CANCEL };
	
		for (String type: types) { _type_cancels.put(type, get_key_from_type_cancel_internal(type)); }
	}
	
	private static void populate_type_orders()
	{
		String[] types = new String[] 
		{ 
			ib.orders.PLACE_MARKET, ib.orders.PLACE_STOP, ib.orders.PLACE_LIMIT, ib.orders.PLACE_STOP_LIMIT, ib.orders.CANCEL, ib.orders.UPDATE_START_VALUE, 
			ib.orders.UPDATE_START_MARKET, ib.orders.UPDATE_START2_VALUE, ib.orders.UPDATE_STOP_VALUE, ib.orders.UPDATE_STOP_MARKET
		};
	
		for (String type: types) { _type_orders.put(type, get_key_from_type_order_internal(type)); }
	}

	private static void populate_statuses()
	{
		String[] statuses = new String[] { ib.orders.STATUS_SUBMITTED, ib.orders.STATUS_FILLED, ib.orders.STATUS_ACTIVE, ib.orders.STATUS_INACTIVE, ib.orders.STATUS_IN_PROGRESS };
	
		for (String status: statuses) { _statuses.put(status, get_key_from_status_internal(status)); }
	}

	private static String get_key_from_type_place_internal(String type_) { return common.get_key_from_type(type_, ib.orders.PLACE); }

	private static String get_key_from_type_update_internal(String type_) { return common.get_key_from_type(type_, ib.orders.UPDATE); }

	private static String get_key_from_type_cancel_internal(String type_) { return common.get_key_from_type(type_, ib.orders.CANCEL); }

	private static String get_key_from_type_order_internal(String type_) { return common.get_key_from_type(type_, ib.orders.ORDERS); }

	private static String get_key_from_status_internal(String status_) { return common.get_key_from_type(status_, ib.orders.STATUS); }
	
	private static boolean update_status_internal(int order_id_main_, String status_key_, boolean is_quick_) { return common.update(SOURCE, get_field_col(STATUS, is_quick_), status_key_, get_where_order_id(order_id_main_), is_quick_); }

	private static String get_col(String field_) 
	{
		if (_field_cols.size() == 0) populate_cols();
		
		return _field_cols.get(field_);
	}
	
	private static String[] get_cols() 
	{ 
		if (_cols == null) populate_cols();

		return _cols;
	}
	
	private static void populate_cols()
	{
		String[] fields = get_fields();
	
		int tot = fields.length;
		_cols = new String[tot];
		
		for (int i = 0; i < tot; i++) 
		{	
			String field = fields[i];
			String col = common.get_col(SOURCE, field);
		
			_cols[i] = col;				
			_field_cols.put(field, col); 
		}
	}	
	
	private static String[] get_fields() 
	{ 
		if (_fields == null) populate_fields();

		return _fields;
	}
	
	private static void populate_fields() { _fields = new String[] { db.FIELD_ID, db.FIELD_TIMESTAMP, SYMBOL, ORDER_ID_MAIN, ORDER_ID_SEC, STATUS, START, START2, STOP, IS_MARKET, TYPE_PLACE, TYPE_MAIN, TYPE_SEC, QUANTITY }; }	

	private static String get_status_common(String where_) { return common.get_type(SOURCE, STATUS, ib.orders.STATUS, where_); }

	private static void sync_tables(_order order_)
	{
		sync_tables_remote(order_);
		sync_tables_trades(order_);		
	}

	private static void sync_tables_remote(_order order_)
	{	
		boolean is_quick = ib.remote.is_quick();
		
		Object vals = remote.add_to_vals(remote.IS_MARKET, _order.is_market(order_), null, is_quick);
		
		HashMap<String, Double> items = new HashMap<String, Double>();
		
		items.put(remote.START, order_.get_start());
		items.put(remote.START2, order_.get_start2());
		items.put(remote.STOP, order_.get_stop());
		
		for (Entry<String, Double> item: items.entrySet())
		{			
			double val = item.getValue();
			if (!ib.common.price_is_ok(val)) continue;
		
			vals = remote.add_to_vals(item.getKey(), val, vals, is_quick);
		}
		
		remote.update_order_id(order_.get_id_main(), vals, is_quick);
	}

	private static void sync_tables_trades(_order order_)
	{
		if (ib.common.price_is_ok(order_.get_stop())) trades.update_stop(order_.get_id_main(), order_.get_stop());	
	}
	
	private static boolean exists_internal(int order_id_, boolean is_main_, String where_) 
	{ 
		String where = get_where_order_id(order_id_, is_main_);
		
		if (strings.is_ok(where_)) where = common.join_wheres(where, where_);
		
		return common.exists(SOURCE, where_); 
	}

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }

	private static String get_where_order_id(int order_id_main_) { return get_where_order_id(order_id_main_, true); }

	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(order_id_)); }

	private static String get_where_active() { return (new db_where(SOURCE, STATUS, db_where.OPERAND_NOT_EQUAL, orders.get_key_from_status(ib.orders.STATUS_INACTIVE))).toString(); }

	private static String get_where_inactive() { return get_where_status(ib.orders.STATUS_INACTIVE); }

	private static String get_where_status(String status_) { return common.get_where(SOURCE, STATUS, get_key_from_status(status_)); }
}