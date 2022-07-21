package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.db_where;
import external_ib.orders;

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
	
	public static boolean exists(String exec_id_) { return common.exists(SOURCE, get_where(exec_id_)); }

	public static boolean update(String exec_id_, HashMap<String, Object> vals_) { return common.insert_update(SOURCE, vals_, get_where(exec_id_)); }
	
	public static double get_fees(int order_id_) { return common.get_decimal(SOURCE, FEES, get_where_order_id(order_id_)); }

	public static double get_start_price(int order_id_buy_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_buy_), get_where_side(SIDE_BOUGHT), db_where.LINK_AND)); }
	
	public static double get_end_price(int order_id_sell_) { return common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id(order_id_sell_), get_where_side(SIDE_SOLD), db_where.LINK_AND)); }

	public static ArrayList<HashMap<String, String>> get_money_info(int order_id_) { return common.get_all_vals(SOURCE, new String[] { PRICE, QUANTITY, FEES }, get_where_order_id(order_id_)); }

	private static String get_where(String exec_id_) { return common.get_where(SOURCE, EXEC_ID, exec_id_, false); }
	
	private static String get_where_order_id(int order_id_) { return common.get_where(SOURCE, ORDER_ID, Integer.toString(order_id_), false); }

	private static String get_where_side(String side_) { return common.get_where(SOURCE, SIDE, side_, false); }
}