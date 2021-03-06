package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
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
	public static final String ELAPSED_INI = common.FIELD_ELAPSED_INI;
	public static final String START = common.FIELD_START;
	public static final String STOP = common.FIELD_STOP;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String UNREALISED = common.FIELD_UNREALISED;	
	public static final String IS_ACTIVE = common.FIELD_IS_ACTIVE;
	public static final String POSITION = common.FIELD_POSITION;
	public static final String INVESTMENT = common.FIELD_INVESTMENT;
	public static final String END = common.FIELD_END;
	public static final String REALISED = common.FIELD_REALISED;

	public static boolean started(int order_id_main_) { return common.exists(SOURCE, get_where_started(order_id_main_)); }

	public static boolean order_id_is_ok(int order_id_main_) { return orders.exists_active(order_id_main_, true); }

	public static boolean order_id_exists(int order_id_, boolean is_start_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_start_)); }
	
	public static String get_symbol(int order_id_, boolean is_start_) { return (is_start_ ? orders.get_symbol(order_id_) : get_string(SYMBOL, order_id_, false)); }
	
	public static double get_price(int order_id_main_) { return get_price(order_id_main_, true); }
	
	public static double get_price(int order_id_, boolean is_main_) { return common.get_decimal(SOURCE, PRICE, get_where_order_id(order_id_, is_main_)); }

	public static int get_order_id_main(int order_id_sec_) { return orders.get_id_main(order_id_sec_); }

	public static int get_order_id_sec(int order_id_main_) { return orders.get_id_sec(order_id_main_); }

	public static int get_order_id_no_position(String symbol_) { return common.get_int(SOURCE, ORDER_ID_MAIN, db_where.join(common.get_where_symbol(SOURCE, symbol_), (new db_where(SOURCE, POSITION, 0.0)).toString(), db_where.LINK_AND), false); }
	
	public static boolean start(int order_id_main_, String symbol_, double start_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (order_id_exists(order_id_main_, true)) vals = start_internal(order_id_main_, symbol_, start_, vals);
		else
		{
			vals.put(ORDER_ID_MAIN, order_id_main_);
			vals.put(ORDER_ID_SEC, get_order_id_sec(order_id_main_));
			vals.put(SYMBOL, symbol_);
			vals.put(ELAPSED_INI, dates.start_elapsed());
			
			vals = start_internal(order_id_main_, symbol_, start_, vals);
		}				
			
		return (arrays.is_ok(vals) ? common.insert_update(SOURCE, vals, get_where_order_id(order_id_main_)) : false);
	}

	public static boolean end(int order_id_sec_, double end_) 
	{			
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(IS_ACTIVE, false);
		vals.put(POSITION, 0.0);
		vals.put(UNREALISED, 0.0);
		vals.put(INVESTMENT, 0.0);
		
		if (end_ > ib.common.WRONG_PRICE) vals.put(END, db_ib.common.adapt_price(end_));

		double unrealised = get_decimal(UNREALISED, order_id_sec_, false);
		double investment = get_decimal(INVESTMENT, order_id_sec_, false);
		
		int order_id_main = get_order_id_main(order_id_sec_);
		
		double realised = ib.execs.get_realised(order_id_main, order_id_sec_);
		realised = (realised == 0.0 ? unrealised : db_ib.common.adapt_money(realised));
		if (realised != 0.0) vals.put(REALISED, realised);
		
		boolean output = common.update(SOURCE, vals, get_where_order_id(order_id_sec_, false));

		basic.update_money_free(common.adapt_money(basic.get_money_free() + investment + realised));

		orders.deactivate(order_id_main);
		remote.deactivate_order_id(order_id_main);
		
		ib.basic.get_money();

		return output;
	}
	
	public static String[] get_fields() { return new String[] { USER, ORDER_ID_MAIN, SYMBOL, PRICE, TIME_ELAPSED, START, STOP, HALTED, UNREALISED }; }

	public static double get_start(int order_id_main_) { return get_start(order_id_main_, true); }

	public static double get_end(int order_id_main_) { return get_end(order_id_main_, true); }

	public static double get_position(int order_id_main_) { return get_decimal(POSITION, order_id_main_, true); }

	public static boolean update_position(int order_id_main_, double pos_) { return update(POSITION, common.adapt_position(pos_), order_id_main_); }

	public static void update_unrealised(String symbol_) 
	{
		ArrayList<Integer> order_ids = common.get_all_ints(SOURCE, ORDER_ID_MAIN, common.get_where_symbol(SOURCE, symbol_));
		if (!arrays.is_ok(order_ids)) return;
		
		for (int order_id: order_ids)
		{
			double unrealised = ib.execs.get_unrealised(order_id);
			if (!ib.common.money_is_ok(unrealised)) continue;
			
			trades.update(UNREALISED, common.adapt_money(unrealised), order_id);
		}
	}

	public static boolean update_unrealised(double pos_, double unrealised_) { return update(UNREALISED, common.adapt_money(unrealised_), pos_); }

	static boolean update_stop(int order_id_main_, double stop_) { return update(STOP, common.adapt_price(stop_), order_id_main_); }

	private static HashMap<String, Object> start_internal(int order_id_main_, String symbol_, double start_, HashMap<String, Object> vals_) 
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);

		if (!started(order_id_main_))
		{
			double start = (start_ > ib.common.WRONG_PRICE ? start_ : get_start_execs(order_id_main_)); 		

			if (start > ib.common.WRONG_PRICE)
			{
				vals.put(START, common.adapt_price(start));
				
				double investment = ib.execs.get_investment(start, order_id_main_);
				vals.put(INVESTMENT, common.adapt_money(investment));	

				basic.update_money_free(common.adapt_money(basic.get_money_free() - investment));
			}			
		}

		vals.put(UNREALISED, common.adapt_money(ib.execs.get_unrealised(order_id_main_)));

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

	private static boolean update(String field_, Object val_, double pos_) { return common.update(SOURCE, field_, val_, get_where_position(pos_)); }

	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }
	
	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.get_where_order_id(SOURCE, order_id_, is_main_); }

	private static String get_where_position(double pos_) { return common.get_where(SOURCE, POSITION, Double.toString(pos_), false); }

	private static String get_where_started(int order_id_main_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();

		wheres.add(new db_where(SOURCE, USER, db_where.OPERAND_EQUAL, basic.get_user(), db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, ORDER_ID_MAIN, db_where.OPERAND_EQUAL, order_id_main_, db_where.LINK_AND));
		wheres.add(new db_where(SOURCE, START, db_where.OPERAND_GREATER, ib.common.WRONG_PRICE, db_where.LINK_AND));
			
		return db_where.to_string(wheres); 
	}
}