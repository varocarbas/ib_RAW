package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db;
import accessory.db_where;
import accessory.strings;
import ib.order;

public abstract class execs 
{
	public static final String SOURCE = common.SOURCE_EXECS;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String EXEC_ID = common.FIELD_EXEC_ID;
	public static final String ORDER_ID = common.FIELD_ORDER_ID;
	public static final String SIDE = common.FIELD_SIDE;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String FEES = common.FIELD_FEES;

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String exec_id_) { return common.exists(SOURCE, get_where(exec_id_)); }

	public static boolean order_id_exists(int order_id_, boolean is_main_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_main_)); }

	public static ArrayList<Integer> get_order_ids_filled(String symbol_) { return get_order_ids_common(symbol_, true); }

	public static ArrayList<Integer> get_order_ids_completed(String symbol_) { return get_order_ids_common(symbol_, false); }

	public static boolean is_filled(int order_id_main_) { return (order_id_exists(order_id_main_, true) && !order_id_exists(order.get_id_sec(order_id_main_), false)); }

	public static boolean is_completed(int order_id_main_) { return (order_id_exists(order_id_main_, true) && order_id_exists(order.get_id_sec(order_id_main_), false)); }

	public static ArrayList<Integer> get_all_order_ids_main() { return get_all_order_ids_main(null); }

	public static ArrayList<Integer> get_all_order_ids_main(String symbol_) 
	{ 
		String where = get_where_side(true);
		if (strings.is_ok(symbol_)) where = common.join_wheres(where, get_where_symbol(symbol_));
		
		return common.get_all_ints(SOURCE, ORDER_ID, where); 
	}

	public static boolean contains_active() 
	{
		if (common.contains_active(SOURCE)) return true;
		
		ArrayList<Integer> ids_main = get_all_order_ids_main();
		if (!arrays.is_ok(ids_main)) return false;
		
		for (int id_main: ids_main)
		{
			if (!execs.order_id_exists(order.get_id_sec(id_main), false)) return true;
		}	
		
		return false; 
	}

	public static boolean update(String exec_id_, HashMap<String, Object> vals_) { return common.insert_update(SOURCE, vals_, get_where(exec_id_)); }
	
	public static double get_fees(int order_id_) { return common.get_decimal(SOURCE, FEES, get_where_order_id(order_id_)); }

	public static double get_start_price(int order_id_buy_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_buy_), get_where_side(true), db_where.LINK_AND)); }
	
	public static double get_end_price(int order_id_sell_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_sell_), get_where_side(false), db_where.LINK_AND)); }

	public static ArrayList<HashMap<String, String>> get_money_info(int order_id_) { return common.get_all_vals(SOURCE, new String[] { PRICE, QUANTITY, FEES }, get_where_order_id(order_id_)); }

	public static ArrayList<HashMap<String, String>> get_order_ids(String symbol_) { return common.get_all_vals(SOURCE, new String[] { db.FIELD_TIMESTAMP, ORDER_ID, SIDE }, get_where_symbol(symbol_)); }

	private static ArrayList<Integer> get_order_ids_common(String symbol_, boolean is_filled_) 
	{ 
		ArrayList<Integer> output = new ArrayList<Integer>();

		ArrayList<Integer> all = get_all_order_ids_main(symbol_);
		if (!arrays.is_ok(all)) return output;
		
		for (int id_main: all)
		{
			boolean sec_exists = order_id_exists(order.get_id_sec(id_main), false); 
			if ((is_filled_ && !sec_exists) || (!is_filled_ && sec_exists)) output.add(id_main);
		}
		
		return output; 
	}

	private static String get_where(String exec_id_) { return common.get_where(SOURCE, EXEC_ID, exec_id_, false); }

	private static String get_where_order_id(int order_id_, boolean is_main_) { return db_where.join(get_where_order_id(order_id_), get_where_side(is_main_), db_where.LINK_AND); }
	
	private static String get_where_order_id(int order_id_) { return common.get_where(SOURCE, ORDER_ID, Integer.toString(order_id_), false); }
	
	private static String get_where_side(boolean is_main_) { return get_where_side(ib.execs.get_side(is_main_)); }

	private static String get_where_side(String side_) { return common.get_where(SOURCE, SIDE, side_, false); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }
}