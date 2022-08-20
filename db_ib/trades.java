package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db_where;
import ib.order;

public abstract class trades 
{
	public static final String SOURCE = common.SOURCE_TRADES;
	
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String TIME_ELAPSED = common.FIELD_TIME_ELAPSED;
	public static final String ELAPSED_INI = common.FIELD_ELAPSED_INI;
	public static final String START = common.FIELD_START;
	public static final String STOP = common.FIELD_STOP;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String UNREALISED = common.FIELD_UNREALISED;	
	public static final String IS_ACTIVE = common.FIELD_IS_ACTIVE;
	public static final String INVESTMENT = common.FIELD_INVESTMENT;
	public static final String END = common.FIELD_END;
	public static final String REALISED = common.FIELD_REALISED;

	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }

	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String symbol_) { return common.exists(SOURCE, get_where_symbol(symbol_)); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || common.exists(SOURCE, get_where_is_active())); }

	public static boolean started(int order_id_main_) { return common.exists(SOURCE, get_where_started(order_id_main_)); }

	public static boolean order_id_is_ok(int order_id_main_) { return orders.is_active(order_id_main_, true); }

	public static boolean order_id_exists(int order_id_, boolean is_start_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_start_)); }
	
	public static String get_symbol(int order_id_, boolean is_start_) { return (is_start_ ? ib.orders.get_symbol(order_id_) : get_string(SYMBOL, order_id_, false)); }
	
	public static double get_price(int order_id_main_) { return get_price(order_id_main_, true); }
	
	public static double get_price(int order_id_, boolean is_main_) { return common.get_decimal(SOURCE, PRICE, get_where_order_id(order_id_, is_main_)); }
	
	public static boolean start(String symbol_, int order_id_main_, double start_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (order_id_exists(order_id_main_, true)) vals = start_internal(symbol_, order_id_main_, start_, vals);
		else
		{
			vals.put(SYMBOL, symbol_);
			vals.put(ORDER_ID_MAIN, order_id_main_);
			vals.put(ORDER_ID_SEC, order.get_id_sec(order_id_main_));
			vals.put(ELAPSED_INI, dates.start_elapsed());
			
			vals = start_internal(symbol_, order_id_main_, start_, vals);
		}				
			
		return (arrays.is_ok(vals) ? common.insert_update(SOURCE, vals, get_where_order_id(order_id_main_)) : false);
	}

	public static boolean end(int order_id_sec_, double end_) 
	{			
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(IS_ACTIVE, false);
		vals.put(UNREALISED, 0.0);
		vals.put(INVESTMENT, 0.0);
		
		if (end_ > ib.common.WRONG_PRICE) vals.put(END, db_ib.common.adapt_price(end_));
		
		int order_id_main = order.get_id_main(order_id_sec_);

		double realised = ib.execs.get_realised(order_id_sec_);
		if (realised != ib.execs.WRONG_MONEY && realised != 0.0) vals.put(REALISED, db_ib.common.adapt_money(realised));
		
		boolean output = common.update(SOURCE, vals, get_where_order_id(order_id_sec_, false));

		ib.orders.deactivate(order_id_main);

		return output;
	}
	
	public static String[] get_fields() { return new String[] { ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }

	public static double get_start(int order_id_main_) { return get_start(order_id_main_, true); }

	public static double get_start(String symbol_) { return common.get_decimal(SOURCE, START, get_where_symbol(symbol_)); }

	public static double get_end(int order_id_main_) { return get_end(order_id_main_, true); }

	public static double get_end(String symbol_) { return common.get_decimal(SOURCE, END, get_where_symbol(symbol_)); }

	public static void update_unrealised(String symbol_) 
	{
		ArrayList<Integer> order_ids = common.get_all_ints(SOURCE, ORDER_ID_MAIN, common.get_where_symbol(SOURCE, symbol_));
		if (!arrays.is_ok(order_ids)) return;
		
		for (int order_id: order_ids)
		{
			double unrealised = ib.execs.get_unrealised(order_id);
			if (unrealised == ib.execs.WRONG_MONEY) continue;
			
			trades.update(UNREALISED, common.adapt_money(unrealised), order_id);
		}
	}

	public static void update_realised(int order_id_main_, double realised_) { update(REALISED, common.adapt_money(realised_), order_id_main_); }

	static boolean update_stop(int order_id_main_, double stop_) { return update(STOP, common.adapt_price(stop_), order_id_main_); }

	private static HashMap<String, Object> start_internal(String symbol_, int order_id_main_, double start_, HashMap<String, Object> vals_) 
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);

		if (!started(order_id_main_))
		{
			double start = (start_ > ib.common.WRONG_PRICE ? start_ : get_start_execs(order_id_main_)); 		

			if (start > ib.common.WRONG_PRICE)
			{
				vals.put(START, common.adapt_price(start));
				
				double investment = common.adapt_money(ib.execs.get_investment(order_id_main_, true));
				if (ib.common.money_is_ok(investment)) vals.put(INVESTMENT, investment);	
			}			
		}

		double unrealised = ib.execs.get_unrealised(order_id_main_);
		if (unrealised != ib.execs.WRONG_MONEY) vals.put(UNREALISED, common.adapt_money(unrealised));

		return vals;
	}

	private static double get_start(int order_id_main_, boolean update_) 
	{
		boolean exists = true;	
		double start = get_decimal(START, order_id_main_, true);
		
		if (start <= ib.common.WRONG_PRICE) 
		{
			exists = false;		
			start = get_start_execs(order_id_main_);
		}
		
		if (!exists && update_ && start > ib.common.WRONG_PRICE) update(START, common.adapt_price(start), order_id_main_);
	
		return start;
	}

	private static double get_start_execs(int order_id_main_) { return execs.get_start_price(order_id_main_); }
	
	private static double get_end(int order_id_main_, boolean update_) 
	{
		boolean exists = true;	
		double end = get_decimal(END, order_id_main_, true);
		
		if (end <= ib.common.WRONG_PRICE) 
		{
			exists = false;		
			end = execs.get_end_price(order_id_main_);
		}
		
		if (!exists && update_ && end > ib.common.WRONG_PRICE) update(END, common.adapt_price(end), order_id_main_);
	
		return end;
	}

	private static double get_decimal(String field_, int order_id_, boolean is_main_) { return common.get_decimal(SOURCE, field_, get_where_order_id(order_id_, is_main_)); }

	private static String get_string(String field_, int order_id_, boolean is_main_) { return common.get_string(SOURCE, field_, get_where_order_id(order_id_, is_main_)); }

	private static boolean update(String field_, Object val_, int order_id_) { return update(field_, val_, order_id_, true); }

	private static boolean update(String field_, Object val_, int order_id_, boolean is_main_) { return common.update(SOURCE, field_, val_, get_where_order_id(order_id_, is_main_)); }

	private static String get_where_is_active() { return common.get_where(SOURCE, IS_ACTIVE, accessory.db.adapt_input(true), false); }
	
	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }
	
	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where_order_id(SOURCE, order_id_, is_main_); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }

	private static String get_where_started(int order_id_main_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();

		wheres.add(new db_where(SOURCE, ORDER_ID_MAIN, db_where.OPERAND_EQUAL, order_id_main_, db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, START, db_where.OPERAND_GREATER, ib.common.WRONG_PRICE, db_where.LINK_AND));
			
		return db_where.to_string(wheres); 
	}
}