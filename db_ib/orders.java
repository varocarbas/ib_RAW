package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.strings;
import ib.order;

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

	public static HashMap<String, String> get_order(int id_) { return common.get_vals(SOURCE, get_where_order_id(id_)); }

	public static HashMap<String, String> get_order(String symbol_) { return common.get_vals(SOURCE, get_where_symbol(symbol_)); }

	public static boolean add_order(order order_) { return (order_ == null ? false : common.insert(SOURCE, orders.order_to_db(order_))); }

	public static boolean update_order(order order_) 
	{ 
		if (order_ == null) return false;
		
		HashMap<String, Object> vals = orders.order_to_db(order_);
		
		return common.update(SOURCE, vals, orders.get_where_order_id((int)vals.get(ORDER_ID_MAIN)));
	}
	
	public static boolean delete() { return common.delete(SOURCE, get_where_user()); }
	
	public static boolean delete(int id_) { return common.delete(SOURCE, get_where_order_id(id_)); }
	
	public static boolean delete_except(Integer[] ids_) { return (arrays.is_ok(ids_) ? common.delete(SOURCE, get_where_order_id(ids_, false)) : delete()); }

	public static order db_to_order(HashMap<String, String> db_)
	{
		String type_place = (String)arrays.get_value(db_, db_ib.orders.TYPE_PLACE);
		String symbol = (String)arrays.get_value(db_, db_ib.orders.SYMBOL);
		double quantity = strings.to_number_decimal((String)arrays.get_value(db_, db_ib.orders.QUANTITY));
		double stop = strings.to_number_decimal((String)arrays.get_value(db_, db_ib.orders.STOP)); 
		double start = strings.to_number_decimal((String)arrays.get_value(db_, db_ib.orders.START));
		double start2 = strings.to_number_decimal((String)arrays.get_value(db_, db_ib.orders.START2));
		
		return new order(type_place, symbol, quantity, stop, start, start2);
	}
	
	public static HashMap<String, Object> order_to_db(order order_)
	{
		HashMap<String, Object> db = new HashMap<String, Object>();
		if (order_ == null) return db;

		db.put(db_ib.orders.ORDER_ID_MAIN, order_.get_id_main());
		db.put(db_ib.orders.ORDER_ID_SEC, order_.get_id_sec());
		db.put(db_ib.orders.TYPE_PLACE, order_.get_type_place());
		db.put(db_ib.orders.TYPE_MAIN, order_.get_type_main());
		db.put(db_ib.orders.TYPE_SEC, order_.get_type_sec());
		db.put(db_ib.orders.SYMBOL, order_.get_symbol());
		db.put(db_ib.orders.QUANTITY, order_.get_quantity());
		db.put(db_ib.orders.STOP, order_.get_stop());
		db.put(db_ib.orders.START, order_.get_start());
		db.put(db_ib.orders.START2, order_.get_start2());
		
		return db;
	}

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