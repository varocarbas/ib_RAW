package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.strings;
import ib.order;
import ib.sync_orders;

public class orders 
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
	
	public static order get_to_order(int id_) { return to_order(get(id_)); }
	
	public static order get_to_order(String symbol_) { return to_order(get(symbol_)); }

	public static HashMap<String, String> get(int id_) { return common.get_vals(SOURCE, get_where_order_id(id_)); }

	public static HashMap<String, String> get(String symbol_) { return common.get_vals(SOURCE, get_where_symbol(symbol_)); }

	public static boolean add(order order_) { return (order_ == null ? false : common.insert(SOURCE, to_hashmap(order_))); }

	public static boolean update(order order_) 
	{ 
		if (order_ == null) return false;
		
		HashMap<String, Object> vals = to_hashmap(order_);
		
		return common.update(SOURCE, vals, orders.get_where_order_id((int)vals.get(ORDER_ID_MAIN)));
	}
	
	public static boolean delete() { return common.delete(SOURCE, get_where_user()); }
	
	public static boolean delete(int id_) { return common.delete(SOURCE, get_where_order_id(id_)); }
	
	public static boolean delete_except(Integer[] ids_) { return (arrays.is_ok(ids_) ? common.delete(SOURCE, get_where_order_id(ids_, false)) : delete()); }

	public static order to_order(HashMap<String, String> db_)
	{
		String type_place = db_to_order((String)arrays.get_value(db_, TYPE_PLACE), false);
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

		db.put(USER, ib.common.USER);
		db.put(STATUS, order_to_db(sync_orders.DEFAULT_STATUS, true));
		db.put(ORDER_ID_MAIN, order_.get_id_main());
		db.put(ORDER_ID_SEC, order_.get_id_sec());
		db.put(TYPE_PLACE, order_to_db(order_.get_type_place(), false));
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

	public static String db_to_order(String input_, boolean is_status_) { return (strings.is_ok(input_) ? ((is_status_ ? sync_orders.STATUS : sync_orders.PLACE) + accessory.types.SEPARATOR + input_) : strings.DEFAULT); }

	public static String order_to_db(String input_, boolean is_status_) { return (strings.is_ok(input_) ? accessory._keys.get_key(input_, (is_status_ ? sync_orders.STATUS : sync_orders.PLACE)) : strings.DEFAULT); }
	
	private static String get_where_user() { return common.get_where_user(SOURCE); }

	private static String get_where_symbol(String symbol_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		wheres.add(new db_where(SOURCE, USER, db_where.OPERAND_EQUAL, ib.common.USER, db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, SYMBOL, db_where.OPERAND_EQUAL, symbol_, db_where.LINK_AND));

		return db_where.to_string(wheres); 
	}

	private static String get_where_order_id(int id_) { return get_where_order_id(new Integer[] { id_ }, true); }

	private static String get_where_order_id(Integer[] ids_, boolean equal_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		wheres.add(new db_where(SOURCE, USER, db_where.OPERAND_EQUAL, ib.common.USER, db_where.LINK_AND));
		
		for (int id: ids_) { wheres.add(new db_where(SOURCE, ORDER_ID_MAIN, (equal_ ? db_where.OPERAND_EQUAL : db_where.OPERAND_NOT_EQUAL), id, db_where.LINK_AND)); }
		
		return db_where.to_string(wheres); 
	}
}