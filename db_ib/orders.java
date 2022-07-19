package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.strings;
import ib.order;

public abstract class orders 
{
	public static final String SOURCE = common.SOURCE_ORDERS;
	
	public static final String USER = common.FIELD_USER;
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

	public static final String STATUS_INACTIVE = ib.order.STATUS_INACTIVE;
	
	public static boolean exists(int id_, boolean is_main_) { return exists_internal(id_, is_main_, null); }

	public static boolean exists_active(int id_, boolean is_main_) { return exists_internal(id_, is_main_, (new db_where(SOURCE, STATUS, db_where.OPERAND_NOT_EQUAL, STATUS_INACTIVE)).toString()); }

	public static boolean exists_inactive(int id_, boolean is_main_) { return exists_internal(id_, is_main_, (new db_where(SOURCE, STATUS, db_where.OPERAND_EQUAL, STATUS_INACTIVE)).toString()); }

	public static order get_to_order(int id_main_) { return to_order(get(id_main_)); }
	
	public static order get_to_order(String symbol_) { return to_order(get(symbol_)); }

	public static String get_symbol(int id_main_) { return common.get_string(SOURCE, SYMBOL, get_where_order_id(id_main_)); }

	public static int get_id_sec(int id_main_) { return common.get_int(SOURCE, ORDER_ID_SEC, get_where_order_id(id_main_), false); }
	
	public static HashMap<String, String> get(int id_main_) { return common.get_vals(SOURCE, get_where_order_id(id_main_)); }

	public static HashMap<String, String> get(String symbol_) { return common.get_vals(SOURCE, get_where_symbol(symbol_)); }

	public static boolean insert(order order_) { return (order_ == null ? false : common.insert(SOURCE, to_hashmap(order_))); }

	public static boolean update(order order_) 
	{ 
		if (order_ == null) return false;
		
		HashMap<String, Object> vals = to_hashmap(order_);
		
		return common.update(SOURCE, vals, get_where_order_id((int)vals.get(ORDER_ID_MAIN)));
	}

	public static boolean update_status(int id_main_, String status_) 
	{ 
		String status = get_key_from_status(status_);
		if (!strings.is_ok(status)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(STATUS, status);
		
		return common.update(SOURCE, vals, get_where_order_id(id_main_));
	}
	
	public static boolean delete() { return common.delete(SOURCE, get_where_user()); }
	
	public static boolean delete(int id_main_) { return common.delete(SOURCE, get_where_order_id(id_main_)); }
	
	public static boolean delete_except(Integer[] ids_main_) { return (arrays.is_ok(ids_main_) ? common.delete(SOURCE, common.get_where_order_id(SOURCE, ids_main_, false)) : delete()); }

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
	
	public static HashMap<String, Object> to_hashmap(order order_)
	{
		HashMap<String, Object> db = new HashMap<String, Object>();
		if (order_ == null) return db;

		db.put(USER, ib.basic.get_user());
		db.put(STATUS, get_key_from_status(ib.orders.DEFAULT_STATUS));
		db.put(ORDER_ID_MAIN, order_.get_id_main());
		db.put(ORDER_ID_SEC, order_.get_id_sec());
		db.put(TYPE_PLACE, get_key_from_type_place(order_.get_type_place()));
		db.put(TYPE_MAIN, order_.get_type_main());
		db.put(TYPE_SEC, order_.get_type_sec());
		db.put(SYMBOL, order_.get_symbol());
		db.put(QUANTITY, order_.get_quantity());
		db.put(STOP, order_.get_stop());
		db.put(START, order_.get_start());
		db.put(START2, order_.get_start2());
		db.put(IS_MARKET, (order_.is_market(true) || order_.is_market(false)));
		
		return db;
	}
	
	public static String get_type_place_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.PLACE); }

	public static String get_key_from_type_place(String type_) { return common.get_key_from_type(type_, ib.orders.PLACE); }
	
	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.orders.STATUS); }

	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.orders.STATUS); }

	private static boolean exists_internal(int id_, boolean is_main_, String where_) 
	{ 
		String where = get_where_order_id(id_, is_main_);
		
		if (strings.is_ok(where_)) where = db_where.join(where, where_, db_where.LINK_AND);
		
		return common.exists(SOURCE, where); 
	}
	
	private static String get_where_user() { return common.get_where_user(SOURCE); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }

	private static String get_where_order_id(int id_main_) { return get_where_order_id(id_main_, false); }

	private static String get_where_order_id(int id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(id_), false); }
}