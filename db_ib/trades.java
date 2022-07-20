package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;

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
	
	public static boolean started(int order_id_main_) { return common.exists(SOURCE, get_where_started(order_id_main_)); }

	public static boolean order_id_is_ok(int order_id_main_) { return orders.exists_active(order_id_main_, true); }

	public static boolean order_id_exists(int order_id_, boolean is_start_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_start_)); }
	
	public static String get_symbol(int order_id_, boolean is_start_) { return (is_start_ ? orders.get_symbol(order_id_) : get_string(SYMBOL, order_id_, false)); }
	
	public static double get_price(int order_id_main_) { return get_price(order_id_main_, true); }
	
	public static double get_price(int order_id_, boolean is_main_) { return common.get_decimal(SOURCE, PRICE, get_where_order_id(order_id_, is_main_)); }

	public static int get_order_id_main(int order_id_sec_) { return orders.get_id_main(order_id_sec_); }

	public static int get_order_id_sec(int order_id_main_) { return orders.get_id_sec(order_id_main_); }

	public static ArrayList<Double> get_all_positions(String symbol_) { return common.get_all_decimals(SOURCE, POSITION, common.get_where_symbol(SOURCE, symbol_)); }
	
	public static boolean start(int order_id_main_, String symbol_, double start_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (order_id_exists(order_id_main_, true)) vals = start_internal(order_id_main_, symbol_, start_, vals);
		else
		{
			vals.put(ORDER_ID_MAIN, order_id_main_);
			vals.put(ORDER_ID_SEC, get_order_id_sec(order_id_main_));
			vals.put(SYMBOL, symbol_);

			vals = start_internal(order_id_main_, symbol_, start_, vals);
		}				
			
		return (arrays.is_ok(vals) ? common.insert_update(SOURCE, vals, get_where_order_id(order_id_main_)) : false);
	}

	public static boolean end(int order_id_sec_, double price_) 
	{
		orders.update_status(get_order_id_main(order_id_sec_), orders.STATUS_INACTIVE);
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(IS_ACTIVE, false);
		vals.put(END, price_);
		
		return common.update(SOURCE, vals, get_where_order_id(order_id_sec_, false));
	}
	
	public static String[] get_fields() { return new String[] { USER, ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }

	public static double get_start(int order_id_main_) { return get_start(order_id_main_, true); }

	public static double get_end(int order_id_main_) { return get_end(order_id_main_, true); }

	public static double get_position(int order_id_main_) { return get_decimal(POSITION, order_id_main_, true); }

	public static boolean update_unrealised(double val_, double pos_) { return update(UNREALISED, common.adapt_money(val_), pos_); }

	private static HashMap<String, Object> start_internal(int order_id_main_, String symbol_, double start_, HashMap<String, Object> vals_) 
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);

		if (!started(order_id_main_))
		{
			double start = (ib.common.price_is_ok(start_) ? start_ : get_start_execs(order_id_main_)); 		

			if (ib.common.price_is_ok(start))
			{
				vals.put(START, common.adapt_price(start));
				vals.put(INVESTMENT, common.adapt_money(start * orders.get_quantity(order_id_main_)));			
			}			
		}

		double pos = get_position(order_id_main_);		
		if (!ib.common.position_is_ok(pos)) 
		{
			pos = common.adapt_position(ib.trades.get_position_start(symbol_));
			if (ib.common.position_is_ok(pos)) vals.put(POSITION, pos);
		}
		
		double unrealised = ib.common.WRONG_MONEY;
		if (ib.common.position_is_ok(pos)) unrealised = ib.trades.get_unrealised(pos);
		else unrealised = ib.execs.estimate_unrealised(order_id_main_, ib.common.WRONG_ORDER_ID); 

		if (unrealised != ib.common.WRONG_MONEY) vals.put(UNREALISED, common.adapt_money(unrealised));

		return vals;
	}

	private static double get_start(int order_id_main_, boolean update_) 
	{
		boolean exists = true;	
		double start = get_decimal(START, order_id_main_, true);
		
		if (!ib.common.price_is_ok(start)) 
		{
			exists = false;		
			start = get_start_execs(order_id_main_);
		}
		
		if (!exists && update_ && ib.common.price_is_ok(start)) update(START, start, order_id_main_, true);
	
		return start;
	}

	private static double get_start_execs(int order_id_main_) { return execs.get_start_price(order_id_main_); }
	
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

	private static String get_where_order_id(int order_id_main_) { return get_where_order_id(order_id_main_, true); }
	
	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), Integer.toString(order_id_), false); }

	private static String get_where_position(double pos_) { return common.get_where(SOURCE, POSITION, Double.toString(pos_), false); }

	private static String get_where_started(int order_id_main_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();

		wheres.add(new db_where(SOURCE, USER, db_where.OPERAND_EQUAL, basic.get_user(), db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, ORDER_ID_MAIN, db_where.OPERAND_EQUAL, order_id_main_, db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, START, db_where.OPERAND_GREATER, ib.common.WRONG_PRICE, db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, POSITION, db_where.OPERAND_GREATER, ib.common.WRONG_POSITION, db_where.DEFAULT_LINK));
			
		return db_where.to_string(wheres); 
	}
}