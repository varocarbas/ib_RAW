package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.data;
import accessory.db;
import accessory.db_where;

import accessory.strings;
import accessory_ib.types;
import ib.order;

public abstract class orders 
{
	public static final String SOURCE = common.SOURCE_ORDERS;
	
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

	public static boolean exists(String symbol_) { return common.exists(SOURCE, get_where_symbol(symbol_)); }
	
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

	public static order get_to_order(int order_id_main_) { return to_order(get(order_id_main_)); }
	
	public static order get_to_order(String symbol_) { return to_order(get(symbol_)); }

	public static String get_symbol(int order_id_main_) { return common.get_string(SOURCE, SYMBOL, get_where_order_id(order_id_main_)); }

	public static int get_id_main(int order_id_sec_) 
	{ 
		int id_main = common.get_int(SOURCE, ORDER_ID_MAIN, get_where_order_id(order_id_sec_, false), false); 
	
		return (id_main == db.WRONG_INT ? ib.common.WRONG_ORDER_ID : id_main);
	}

	public static int get_id_sec(int order_id_main_) 
	{
		int id_sec = common.get_int(SOURCE, ORDER_ID_SEC, get_where_order_id(order_id_main_), false); 
		
		return (id_sec == db.WRONG_INT ? ib.common.WRONG_ORDER_ID : id_sec);
	}

	public static double get_quantity(int order_id_main_) { return common.get_decimal(SOURCE, QUANTITY, get_where_order_id(order_id_main_)); }

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
	
	public static HashMap<String, String> get(int order_id_main_) { return common.get_vals(SOURCE, get_where_order_id(order_id_main_)); }

	public static HashMap<String, String> get(String symbol_) { return common.get_vals(SOURCE, get_where_symbol(symbol_)); }

	public static String get_status(String symbol_) { return get_status_common(get_where_symbol(symbol_)); }
	
	public static String get_status(int order_id_main_) { return get_status_common(get_where_order_id(order_id_main_)); }
	
	public static boolean insert_update(order order_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;
		
		output = common.insert_update(SOURCE, to_hashmap(order_), get_where_symbol(order_.get_symbol()));
		
		sync_tables(order_);
		
		return output;
	}

	public static boolean update(order order_) 
	{ 
		boolean output = false;
		if (order_ == null) return output;

		output = common.update(SOURCE, to_hashmap(order_), get_where_order_id(order_.get_id_main()));

		sync_tables(order_);
		
		return output;
	}
	
	public static boolean update_status(int order_id_main_, String status_) { return common.update_type(SOURCE, STATUS, status_, ib.orders.STATUS, get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate(int order_id_main_) { return update_status(order_id_main_, ib.orders.STATUS_INACTIVE); }
	
	public static boolean delete(int order_id_main_) { return common.delete(SOURCE, get_where_order_id(order_id_main_)); }

	public static order to_order(HashMap<String, String> db_)
	{
		String type_place = get_type_place_from_key((String)arrays.get_value(db_, TYPE_PLACE));
		String symbol = (String)arrays.get_value(db_, SYMBOL);
		
		double quantity = strings.to_number_decimal((String)arrays.get_value(db_, QUANTITY));
		double stop = strings.to_number_decimal((String)arrays.get_value(db_, STOP)); 

		double start = strings.to_number_decimal((String)arrays.get_value(db_, START));
		double start2 = strings.to_number_decimal((String)arrays.get_value(db_, START2));
		
		int id_main = strings.to_number_int((String)arrays.get_value(db_, ORDER_ID_MAIN));

		return new order(type_place, symbol, quantity, stop, start, start2, id_main);
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> to_hashmap(order order_) { return (HashMap<String, Object>)to_hashmap(order_, false, false); }
	
	public static Object to_hashmap(order order_, boolean only_basic_, boolean is_quick_)
	{
		if (order_ == null) return (is_quick_ ? new HashMap<String, String>() : new HashMap<String, Object>());

		Object db = common.add_to_vals(SOURCE, ORDER_ID_MAIN, order_.get_id_main(), null, is_quick_);
		db = common.add_to_vals(SOURCE, ORDER_ID_SEC, order_.get_id_sec(), db, is_quick_);
		db = common.add_to_vals(SOURCE, TYPE_PLACE, get_key_from_type_place(order_.get_type_place()), db, is_quick_);
		db = common.add_to_vals(SOURCE, SYMBOL, order_.get_symbol(), db, is_quick_);
		db = common.add_to_vals(SOURCE, QUANTITY, order_.get_quantity(), db, is_quick_);
		db = common.add_to_vals(SOURCE, STOP, order_.get_stop(), db, is_quick_);
		
		if (is_market(order_)) db = common.add_to_vals(SOURCE, IS_MARKET, is_market(order_), db, is_quick_);
		else 
		{
			if (ib.common.price_is_ok(order_.get_start())) db = common.add_to_vals(SOURCE, START, order_.get_start(), db, is_quick_);
			if (ib.common.price_is_ok(order_.get_start2())) db = common.add_to_vals(SOURCE, START2, order_.get_start2(), db, is_quick_);
		}

		if (!only_basic_)
		{
			db = common.add_to_vals(SOURCE, STATUS, get_key_from_status(ib.orders.DEFAULT_STATUS), db, is_quick_);
			db = common.add_to_vals(SOURCE, TYPE_MAIN, order_.get_type_main(), db, is_quick_);
			db = common.add_to_vals(SOURCE, TYPE_SEC, order_.get_type_sec(), db, is_quick_);
		}
		
		return db;
	}
	
	public static boolean is_market(order order_) { return (order_ != null && (order_.is_market(true) || order_.is_market(false))); } 
	
	public static String get_type_place_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.PLACE); }

	public static String get_key_from_type_place(String type_) { return common.get_key_from_type(type_, ib.orders.PLACE); }
	
	public static String get_type_update_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.UPDATE); }

	public static String get_key_from_type_update(String type_) { return common.get_key_from_type(type_, ib.orders.UPDATE); }
	
	public static String get_type_cancel_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.CANCEL); }

	public static String get_key_from_type_cancel(String type_) { return common.get_key_from_type(type_, ib.orders.CANCEL); }
	
	public static String get_type_order_from_key(String key_) { return common.get_type_from_key(key_, types.ORDERS); }

	public static String get_key_from_type_order(String type_) { return common.get_key_from_type(type_, types.ORDERS); }
	
	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.STATUS); }

	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.orders.STATUS); }

	public static boolean order_was_updated(int order_id_, String type_) { return order_was_updated(order_id_, type_, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE, ib.common.WRONG_PRICE); }

	public static boolean order_was_updated(int order_id_, String type_, double stop_, double start_) { return order_was_updated(order_id_, type_, stop_, start_, ib.common.WRONG_PRICE); }
	
	public static boolean order_was_updated(int order_id_main_, String type_, double stop_, double start_, double start2_)
	{
		boolean output = false;
		if (!ib.orders.update_is_ok(type_, stop_, start_, start2_) || !orders.is_active(order_id_main_, true)) return output;
		
		HashMap<String, String> vals = db_ib.orders.get(order_id_main_);
		if (!arrays.is_ok(vals)) return output;

		if (ib.orders.is_update_market(type_)) output = (boolean)db.adapt_output(vals.get(db_ib.orders.IS_MARKET), data.BOOLEAN);
		else
		{
			String field = strings.DEFAULT;
			
			if (ib.orders.is_update_stop(type_)) field = db_ib.orders.STOP;
			else if (ib.orders.is_update_start(type_)) field = db_ib.orders.START;
			else if (ib.orders.is_update_start2(type_)) field = db_ib.orders.START2;

			output = (ib.orders.get_update_val(type_, stop_, start_, start2_) == common.adapt_price(Double.parseDouble(vals.get(field))));
		}
		
		return output;
	}

	private static String get_status_common(String where_) { return common.get_type(SOURCE, STATUS, ib.orders.STATUS, where_); }

	private static void sync_tables(order order_)
	{
		sync_tables_remote(order_);
		sync_tables_trades(order_);		
	}

	private static void sync_tables_remote(order order_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(remote.IS_MARKET, is_market(order_));
		
		if (ib.common.price_is_ok(order_.get_start())) vals.put(remote.START, order_.get_start());
		if (ib.common.price_is_ok(order_.get_start2())) vals.put(remote.START2, order_.get_start2());
		if (ib.common.price_is_ok(order_.get_stop())) vals.put(remote.STOP, order_.get_stop());
		
		remote.update_order_id(order_.get_id_main(), vals);
	}

	private static void sync_tables_trades(order order_)
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

	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(order_id_), false); }

	private static String get_where_active() { return (new db_where(SOURCE, STATUS, db_where.OPERAND_NOT_EQUAL, orders.get_key_from_status(ib.orders.STATUS_INACTIVE))).toString(); }

	private static String get_where_inactive() { return common.get_where(SOURCE, STATUS, orders.get_key_from_status(ib.orders.STATUS_INACTIVE), false); }
}