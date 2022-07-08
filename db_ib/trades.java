package db_ib;

import java.util.HashMap;

import ib.order;

public abstract class trades 
{
	public static final String SOURCE = common.SOURCE_TRADES;
	
	public static final String USER = common.FIELD_USER;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String TIME_ELAPSED = common.FIELD_TIME_ELAPSED;
	public static final String START = common.FIELD_START;
	public static final String STOP = common.FIELD_STOP;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String UNREALISED = common.FIELD_UNREALISED;	
	public static final String IS_ACTIVE = common.FIELD_IS_ACTIVE;

	public static boolean exists(int order_id_) { return common.exists(SOURCE, common.get_where_order_id(SOURCE, order_id_)); }

	public static boolean is_ok(int order_id_) { return orders.exists_active(order_id_); }
	
	public static String get_symbol(int order_id_) { return order.get_symbol(order_id_); }
	
	public static boolean insert(int order_id_, String symbol_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(ORDER_ID_MAIN, order_id_);
		vals.put(SYMBOL, symbol_);
		
		return common.insert(SOURCE, vals);
	}
	
	public static boolean end(int order_id_) 
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(IS_ACTIVE, false);
		
		return common.update(SOURCE, vals, common.get_where_order_id(SOURCE, order_id_));
	}
	
	public static String[] get_fields() { return new String[] { USER, ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }
}