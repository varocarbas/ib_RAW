package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import external_ib.orders;
import ib._order;

public abstract class execs 
{
	public static final String SOURCE = common.SOURCE_EXECS;
	
	public static final String USER = common.FIELD_USER;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String EXEC_ID = common.FIELD_EXEC_ID;
	public static final String ORDER_ID = common.FIELD_ORDER_ID;
	public static final String SIDE = common.FIELD_SIDE;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String FEES = common.FIELD_FEES;
	
	public static final String SIDE_BOUGHT = orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = orders.EXEC_SIDE_SOLD;

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String exec_id_) { return common.exists(SOURCE, get_where(exec_id_)); }

	public static boolean trade_completed(int order_id_main_) { return trade_completed(order_id_main_, true); }

	public static boolean trade_completed(int order_id_main_, boolean any_user_) { return common.exists(SOURCE, db_where.join(get_where_order_id(_order.get_id_sec(order_id_main_), any_user_), get_where_side(SIDE_SOLD, any_user_), db_where.LINK_AND)); }

	public static ArrayList<Integer> get_all_order_ids_main() { return get_all_order_ids_main(true); }

	public static ArrayList<Integer> get_all_order_ids_main(boolean any_user_) { return common.get_all_ints(SOURCE, ORDER_ID, get_where_side(SIDE_BOUGHT, any_user_)); }

	public static boolean contains_active() 
	{
		ArrayList<Integer> ids_main = get_all_order_ids_main();
		if (!arrays.is_ok(ids_main)) return false;
		
		for (int id_main: ids_main)
		{
			if (!trade_completed(id_main)) return true;
		}	
		
		return false; 
	}

	public static boolean update(String exec_id_, HashMap<String, Object> vals_) { return common.insert_update(SOURCE, vals_, get_where(exec_id_)); }
	
	public static double get_fees(int order_id_) { return common.get_decimal(SOURCE, FEES, get_where_order_id(order_id_)); }

	public static double get_start_price(int order_id_buy_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_buy_), get_where_side(SIDE_BOUGHT), db_where.LINK_AND)); }
	
	public static double get_end_price(int order_id_sell_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_sell_), get_where_side(SIDE_SOLD), db_where.LINK_AND)); }

	public static ArrayList<HashMap<String, String>> get_money_info(int order_id_) { return common.get_all_vals(SOURCE, new String[] { PRICE, QUANTITY, FEES }, get_where_order_id(order_id_)); }

	private static String get_where(String exec_id_) { return common.get_where(SOURCE, EXEC_ID, exec_id_, false); }
	
	private static String get_where_order_id(int order_id_) { return get_where_order_id(order_id_, false); }

	private static String get_where_order_id(int order_id_, boolean any_user_) { return common.get_where(SOURCE, ORDER_ID, Integer.toString(order_id_), false, !any_user_); }

	private static String get_where_side(String side_) { return get_where_side(side_, false); }

	private static String get_where_side(String side_, boolean any_user_) { return common.get_where(SOURCE, SIDE, side_, false, !any_user_); }
}