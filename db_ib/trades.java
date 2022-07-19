package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class trades 
{
	public static final String SOURCE = common.SOURCE_TRADES;
	
	public static final String USER = common.FIELD_USER;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
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
	public static final String END = common.FIELD_END;
	
	public static boolean is_ok(int order_id_main_) { return orders.exists_active(order_id_main_, true); }

	public static boolean order_id_exists(int order_id_, boolean is_start_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_start_)); }
	
	public static String get_symbol(int order_id_, boolean is_start_) { return (is_start_ ? orders.get_symbol(order_id_) : get_string(SYMBOL, order_id_, false)); }

	public static int get_order_id_sec(int order_id_main_) { return orders.get_id_sec(order_id_main_); }

	public static ArrayList<Double> get_all_positions(String symbol_) { return common.get_all_decimals(SOURCE, POSITION, common.get_where_symbol(SOURCE, symbol_)); }
	
	public static boolean start(int order_id_main_, String symbol_, double price_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(ORDER_ID_MAIN, order_id_main_);
		vals.put(ORDER_ID_SEC, get_order_id_sec(order_id_main_));
		vals.put(SYMBOL, symbol_);
		vals.put(START, (ib.common.price_is_ok(price_) ? price_ : get_start(order_id_main_, false)));
		
		double pos = get_position_start(symbol_);	
		vals.put(POSITION, pos);
		vals.put(UNREALISED, ib.trades.get_unrealised(pos));
		
		return common.insert_update(SOURCE, vals, get_where_order_id(order_id_main_));
	}
	
	public static boolean end(int order_id_sec_, double price_) 
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(IS_ACTIVE, false);
		vals.put(END, price_);
		
		return common.update(SOURCE, vals, get_where_order_id(order_id_sec_, false));
	}
	
	public static String[] get_fields() { return new String[] { USER, ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }

	public static double get_start(int order_id_main_) { return get_start(order_id_main_, true); }

	public static double get_end(int order_id_main_) { return get_end(order_id_main_, true); }

	public static double get_position(int order_id_main_) { return get_decimal(POSITION, order_id_main_, true); }

	public static boolean update_unrealised(double val_, double pos_) { return update(UNREALISED, val_, pos_); }

	private static double get_position_start(String symbol_) { return adapt_position(ib.trades.get_position_start(symbol_)); }
	
	private static double get_start(int order_id_main_, boolean update_) 
	{
		boolean exists = true;	
		double start = get_decimal(START, order_id_main_, true);
		
		if (!ib.common.price_is_ok(start)) 
		{
			exists = false;		
			start = execs.get_start_price(order_id_main_);
		}
		
		if (!exists && update_ && ib.common.price_is_ok(start)) update(START, start, order_id_main_, true);
	
		return start;
	}
	
	private static double get_end(int order_id_main_, boolean update_) 
	{
		boolean exists = true;	
		double end = get_decimal(END, order_id_main_, true);
		
		if (!ib.common.price_is_ok(end)) 
		{
			exists = false;		
			end = execs.get_end_price(order_id_main_);
		}
		
		if (!exists && update_ && ib.common.price_is_ok(end)) update(END, end, order_id_main_, true);
	
		return end;
	}

	private static double get_decimal(String field_, int order_id_, boolean is_main_) { return common.get_decimal(SOURCE, field_, get_where_order_id(order_id_, is_main_)); }

	private static String get_string(String field_, int order_id_, boolean is_main_) { return common.get_string(SOURCE, field_, get_where_order_id(order_id_, is_main_)); }

	private static boolean update(String field_, Object val_, int order_id_, boolean is_main_) { return common.update(SOURCE, field_, val_, get_where_order_id(order_id_, is_main_)); }

	private static boolean update(String field_, Object val_, double pos_) { return common.update(SOURCE, field_, val_, get_where_position(pos_)); }

	private static double adapt_position(double position_) { return common.adapt_number(position_, common.FIELD_POSITION); }

	private static String get_where_order_id(int order_id_main_) { return get_where_order_id(order_id_main_, true); }
	
	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(order_id_), false); }

	private static String get_where_position(double pos_) { return common.get_where(SOURCE, POSITION, Double.toString(pos_), false); }
}