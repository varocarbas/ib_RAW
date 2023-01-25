package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.data;
import accessory.db;
import accessory.db_common;
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
	
	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String symbol_) { return db_common.exists(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }

	public static boolean exists_status(String status_) { return db_common.exists(SOURCE, get_where_status(status_)); }
	
	public static boolean exists(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, null); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || db_common.exists(SOURCE, get_where_active())); }
	
	public static boolean is_active(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, get_where_active()); }

	public static boolean is_active(int order_id_, double stop_, double start_) { return is_active(order_id_, stop_, start_, ib.common.WRONG_PRICE); }
	
	public static boolean is_active(int order_id_, double stop_, double start_, double start2_) 
	{
		String where = orders.get_where_active();
		
		if (ib.common.price_is_ok(stop_)) where = db_common.join_wheres(where, common.get_where(SOURCE, STOP, Double.toString(ib.common.adapt_price(stop_)), false));
		if (ib.common.price_is_ok(start_)) where = db_common.join_wheres(where, common.get_where(SOURCE, START, Double.toString(ib.common.adapt_price(start_)), false));
		if (ib.common.price_is_ok(start2_)) where = db_common.join_wheres(where, common.get_where(SOURCE, START2, Double.toString(ib.common.adapt_price(start2_)), false));
			
		return db_common.exists(SOURCE, where);
	}

	public static boolean is_inactive(int order_id_, boolean is_main_) { return exists_internal(order_id_, is_main_, get_where_inactive()); }

	public static _order get_to_order(int order_id_main_) { return to_order(get(order_id_main_)); }
	
	public static _order get_to_order(String symbol_) { return to_order(get(symbol_)); }

	public static String get_symbol(int order_id_main_) { return common.get_string(SOURCE, db_common.get_field_quick_col(SOURCE, SYMBOL), get_where_order_id(order_id_main_)); }

	public static int get_order_id(String symbol_, boolean is_main_) { return db_common.get_int(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), common.get_where_symbol(SOURCE, symbol_), ib.common.WRONG_ORDER_ID); }

	public static double get_quantity(int order_id_main_) { return db_common.get_decimal(SOURCE, QUANTITY, get_where_order_id(order_id_main_), ib.common.WRONG_PRICE); }

	public static int get_order_id_main(int order_id_sec_) { return db_common.get_int(SOURCE, ORDER_ID_MAIN, get_where_order_id(order_id_sec_, false), ib.common.WRONG_ORDER_ID); }

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

	public static HashMap<String, String> get(int order_id_main_) { return db_common.get_vals(SOURCE, db_common.get_fields(SOURCE), get_where_order_id(order_id_main_, true)); }

	public static HashMap<String, String> get(String symbol_) { return db_common.get_vals(SOURCE, db_common.get_fields(SOURCE), common.get_where_symbol(SOURCE, symbol_)); }

	public static String get_status(String symbol_) { return get_status_common(common.get_where_symbol(SOURCE, symbol_)); }
	
	public static String get_status(int order_id_main_) { return get_status_common(get_where_order_id(order_id_main_)); }
	
	public static boolean insert_update(_order order_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;
		
		output = common.insert_update(SOURCE, to_hashmap(order_, false), common.get_where_symbol(SOURCE, order_.get_symbol()));
		
		sync_tables(order_);
		
		return output;
	}

	public static boolean update(_order order_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;

		output = db_common.update(SOURCE, to_hashmap(order_, false), get_where_order_id(order_.get_id_main()));

		sync_tables(order_);
		
		return output;
	}
	
	public static boolean update_status(int order_id_main_, String status_) 
	{ 
		if (order_id_main_ <= ib.common.WRONG_ORDER_ID) return false;

		String key = store_status_type(status_);	
		if (!strings.is_ok(key)) return false;

		return update_status_internal(order_id_main_, key); 
	}
	
	public static boolean deactivate(int order_id_main_) { return update_status(order_id_main_, ib.orders.STATUS_INACTIVE); }

	public static _order to_order(HashMap<String, String> db_)
	{
		String type_place = get_place_type((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, TYPE_PLACE)));
		String symbol = (String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, SYMBOL));
		
		double quantity = strings.to_number_decimal((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, QUANTITY)));
		double stop = strings.to_number_decimal((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, STOP))); 

		double start = strings.to_number_decimal((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, START)));
		double start2 = strings.to_number_decimal((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, START2)));
		
		int id_main = strings.to_number_int((String)arrays.get_value(db_, db_common.get_field_quick_col(SOURCE, ORDER_ID_MAIN)));

		return new _order(type_place, symbol, quantity, stop, start, start2, id_main);
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> to_hashmap(_order order_) { return (HashMap<String, Object>)to_hashmap(order_, false); }
	
	public static Object to_hashmap(_order order_, boolean only_basic_)
	{
		if (order_ == null) return (db_common.is_quick(SOURCE) ? new HashMap<String, String>() : new HashMap<String, Object>());

		Object db = db_common.add_to_vals(SOURCE, ORDER_ID_MAIN, order_.get_id_main(), null);
		db = db_common.add_to_vals(SOURCE, ORDER_ID_SEC, order_.get_id_sec(), db);
		db = db_common.add_to_vals(SOURCE, TYPE_PLACE, store_place_type(order_.get_type_place()), db);
		db = db_common.add_to_vals(SOURCE, SYMBOL, order_.get_symbol(), db);
		db = db_common.add_to_vals(SOURCE, QUANTITY, order_.get_quantity(), db);
		db = db_common.add_to_vals(SOURCE, STOP, order_.get_stop(), db);
		
		boolean is_market = _order.is_market(order_);
		
		if (is_market) db = db_common.add_to_vals(SOURCE, IS_MARKET, is_market, db);
		else 
		{
			if (ib.common.price_is_ok(order_.get_start())) db = db_common.add_to_vals(SOURCE, START, order_.get_start(), db);
			if (ib.common.price_is_ok(order_.get_start2())) db = db_common.add_to_vals(SOURCE, START2, order_.get_start2(), db);
		}
		
		if (!only_basic_)
		{
			db = db_common.add_to_vals(SOURCE, TYPE_MAIN, order_.get_type_main(), db);
			db = db_common.add_to_vals(SOURCE, TYPE_SEC, order_.get_type_sec(), db);
		}
		
		return db;
	}

	public static String store_place_type(String place_type_) { return ib.orders.get_place_key(place_type_); }

	public static String get_place_type(String place_key_) { return ib.orders.get_place_type(place_key_); }

	public static String store_update_type(String update_type_) { return ib.orders.get_update_key(update_type_); }

	public static String get_update_type(String update_key_) { return ib.orders.get_update_type(update_key_); }
		
	public static String store_cancel_type(String cancel_type_) { return ib.orders.get_cancel_key(cancel_type_); }

	public static String get_cancel_type(String cancel_key_) { return ib.orders.get_cancel_type(cancel_key_); }
	
	public static String store_order_type(String order_type_) { return ib.orders.get_order_key(order_type_); }

	public static String get_order_type(String order_key_) { return ib.orders.get_order_type(order_key_); }
	
	public static String store_status_type(String status_type_) { return ib.orders.get_status_key(status_type_); }

	public static String get_status_type(String status_key_) { return ib.orders.get_status_type(status_key_); }
	
	public static boolean order_was_updated(int order_id_, String type_) { return order_was_updated(order_id_, type_, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE); }

	public static boolean order_was_updated(int order_id_, String type_, double stop_, double start_) { return order_was_updated(order_id_, type_, stop_, start_, ib.common.WRONG_PRICE); }
	
	public static boolean order_was_updated(int order_id_main_, String type_, double stop_, double start_, double start2_)
	{
		boolean output = false;
		if (!ib.orders.update_is_ok(type_, stop_, start_, start2_) || !is_active(order_id_main_, true)) return output;
		
		HashMap<String, String> vals = get(order_id_main_);
		if (!arrays.is_ok(vals)) return output;

		if (ib.orders.is_update_market(type_)) output = (boolean)db.adapt_output(vals.get(db_common.get_field_quick_col(SOURCE, db_ib.orders.IS_MARKET)), data.BOOLEAN);
		else
		{
			String field_col = strings.DEFAULT;
			
			if (ib.orders.is_update_stop(type_)) field_col = db_common.get_field_quick_col(SOURCE, STOP);
			else if (ib.orders.is_update_start(type_)) field_col = db_common.get_field_quick_col(SOURCE, START);
			else if (ib.orders.is_update_start2(type_)) field_col = db_common.get_field_quick_col(SOURCE, START2);

			output = (ib.orders.get_update_val(type_, stop_, start_, start2_) == common.adapt_price(Double.parseDouble(vals.get(field_col))));
		}
		
		return output;
	}

	public static ArrayList<HashMap<String, String>> get_all_active(String[] fields_) { return get_all_common(fields_, ib.orders.STATUS_ACTIVE); }

	public static ArrayList<HashMap<String, String>> get_all_filled(String[] fields_) { return get_all_common(fields_, ib.orders.STATUS_FILLED); }

	public static ArrayList<HashMap<String, String>> get_all_submitted(String[] fields_) { return get_all_common(fields_, ib.orders.STATUS_SUBMITTED); }

	public static ArrayList<HashMap<String, String>> get_all_common(String[] fields_, String status_) 
	{ 
		String status = ib.orders.check_status(status_);
		if (!strings.is_ok(status)) return null;
		
		String where = null;
		if (status.equals(ib.orders.STATUS_ACTIVE)) where = get_where_active();
		else where = get_where_status(status);
		
		return db_common.get_all_vals(SOURCE, fields_, where); 
	}

	public static int get_highest_order_id()
	{
		int output = ib.orders.MIN_ORDER_ID;
		
		String[] fields = new String[] { orders.SOURCE, ORDER_ID_SEC };
			
		ArrayList<HashMap<String, String>> all = db_common.get_all_vals(SOURCE, fields, null);
		if (!arrays.is_ok(all)) return output;
		
		for (HashMap<String, String> item: all)
		{
			for (String field: fields) 
			{ 
				int order_id = Integer.parseInt(item.get(db_common.get_field_quick_col(SOURCE, field)));
				if (order_id > output) output = order_id; 
			}
		}
		
		return output;
	}
	
	public static Object get_val(String field_, HashMap<String, String> vals_)
	{
		Object val = null;
		if (!arrays.is_ok(vals_)) return val;
		
		String field_col = db_common.get_field_quick_col(SOURCE, field_);
		
		String val0 = vals_.get(field_col);
		
		if (field_is_boolean(field_)) val = strings.to_boolean(val0);
		else if (field_is_int(field_)) val = Integer.parseInt(val0);
		else if (field_is_decimal(field_)) val = Double.parseDouble(val0);
		else 
		{
			if (field_.equals(STATUS)) val = get_status_type(val0);
			else val = val0;
		}
		
		return val;
	}
	
	private static boolean field_is_decimal(String field_) { return (field_.equals(START) || field_.equals(START2) || field_.equals(STOP) || field_.equals(QUANTITY)); }
	
	private static boolean field_is_int(String field_) { return (field_.equals(ORDER_ID_MAIN) || field_.equals(ORDER_ID_SEC)); }
	
	private static boolean field_is_boolean(String field_) { return (field_.equals(IS_MARKET)); }

	private static boolean update_status_internal(int order_id_main_, String status_key_) { return db_common.update(SOURCE, STATUS, status_key_, get_where_order_id(order_id_main_)); }

	private static String get_status_common(String where_) { return common.get_type(SOURCE, STATUS, ib.orders.STATUS, where_); }

	private static void sync_tables(_order order_)
	{
		sync_tables_remote(order_);
		sync_tables_trades(order_);		
	}

	private static void sync_tables_remote(_order order_)
	{	
		Object vals = db_common.add_to_vals(SOURCE, remote.IS_MARKET, _order.is_market(order_), null);
		
		HashMap<String, Double> items = new HashMap<String, Double>();
		
		items.put(remote.START, order_.get_start());
		items.put(remote.START2, order_.get_start2());
		items.put(remote.STOP, order_.get_stop());
		
		for (Entry<String, Double> item: items.entrySet())
		{			
			double val = item.getValue();
			if (!ib.common.price_is_ok(val)) continue;
		
			vals = db_common.add_to_vals(SOURCE, item.getKey(), val, vals);
		}
		
		remote.update_order_id(order_.get_id_main(), vals);
	}

	private static void sync_tables_trades(_order order_)
	{
		if (ib.common.price_is_ok(order_.get_stop())) trades.update_stop(order_.get_id_main(), order_.get_stop());	
	}
	
	private static boolean exists_internal(int order_id_, boolean is_main_, String where_) 
	{ 
		String where = get_where_order_id(order_id_, is_main_);
		
		if (strings.is_ok(where_)) where = db_common.join_wheres(where, where_);
		
		return db_common.exists(SOURCE, where_); 
	}

	private static String get_where_order_id(int order_id_main_) { return get_where_order_id(order_id_main_, true); }

	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(order_id_)); }

	private static String get_where_active() { return (new db_where(SOURCE, STATUS, db_where.OPERAND_NOT_EQUAL, store_status_type(ib.orders.STATUS_INACTIVE))).toString(); }

	private static String get_where_inactive() { return get_where_status(ib.orders.STATUS_INACTIVE); }

	private static String get_where_status(String status_) { return common.get_where(SOURCE, STATUS, store_status_type(status_)); }
}