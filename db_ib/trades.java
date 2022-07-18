package db_ib;

import java.util.ArrayList;
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
	public static final String POSITION = common.FIELD_POSITION;
	public static final String INVESTMENT = common.FIELD_INVESTMENT;
	
	public static boolean exists(int order_id_) { return common.exists(SOURCE, common.get_where_order_id(SOURCE, order_id_)); }

	public static boolean is_ok(int order_id_) { return orders.exists_active(order_id_); }
	
	public static String get_symbol(int order_id_) { return order.get_symbol(order_id_); }

	public static ArrayList<Double> get_all_positions(String symbol_) { return common.get_all_decimals(SOURCE, POSITION, common.get_where_symbol(SOURCE, symbol_)); }
	
	public static boolean insert(int order_id_, String symbol_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(ORDER_ID_MAIN, order_id_);
		vals.put(SYMBOL, symbol_);
		vals.put(START, adapt_start(get_start(order_id_)));
		vals.put(POSITION, get_position(symbol_));
		
		return common.insert(SOURCE, vals);
	}
	
	public static boolean end(int order_id_) 
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(IS_ACTIVE, false);
		
		return common.update(SOURCE, vals, common.get_where_order_id(SOURCE, order_id_));
	}
	
	public static String[] get_fields() { return new String[] { USER, ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }

	private static double get_position(String symbol_) { return adapt_position(ib.trades.get_position(symbol_)); }

	private static double get_start(int order_id_) 
	{ 
		double start = 0.0;

		return start; 
	}

	private static double adapt_start(double start_) { return common.adapt_number(start_, common.FIELD_PRICE); }

	private static double adapt_position(double position_) { return common.adapt_number(position_, common.FIELD_POSITION); }
}