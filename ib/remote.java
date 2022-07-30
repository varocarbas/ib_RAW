package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import accessory_ib.types;

public abstract class remote 
{
	public static final String FIELD_SYMBOL = db_ib.remote.SYMBOL;
	public static final String FIELD_ORDER_ID_MAIN = db_ib.remote.ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = db_ib.remote.ORDER_ID_SEC;
	public static final String FIELD_STATUS = db_ib.remote.STATUS;
	public static final String FIELD_STATUS2 = db_ib.remote.STATUS2;
	public static final String FIELD_START = db_ib.remote.START;
	public static final String FIELD_START2 = db_ib.remote.START2;
	public static final String FIELD_STOP = db_ib.remote.STOP;
	public static final String FIELD_IS_MARKET = db_ib.remote.IS_MARKET;
	public static final String FIELD_QUANTITY = db_ib.remote.QUANTITY;
	public static final String FIELD_PERC_MONEY = db_ib.remote.PERC_MONEY;
	public static final String FIELD_PRICE = db_ib.remote.PRICE;
	public static final String FIELD_ERROR = db_ib.remote.ERROR;
	public static final String FIELD_REQUEST = db_ib.remote.REQUEST;
	public static final String FIELD_TYPE_ORDER = db_ib.remote.TYPE_ORDER;
	public static final String FIELD_TIME2 = db_ib.remote.TIME2;
	
	public static final String STATUS = types.REMOTE_STATUS;
	public static final String STATUS_ACTIVE = types.REMOTE_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.REMOTE_STATUS_INACTIVE;
	public static final String STATUS_ERROR = types.REMOTE_STATUS_ERROR;

	public static final String STATUS2 = types.REMOTE_STATUS2;
	public static final String STATUS2_PENDING = types.REMOTE_STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = types.REMOTE_STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = types.REMOTE_STATUS2_ERROR;

	public static final String PLACE_MARKET = orders.PLACE_MARKET;
	public static final String PLACE_STOP = orders.PLACE_STOP;
	public static final String PLACE_LIMIT = orders.PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = orders.PLACE_STOP_LIMIT;

	public static final String CANCEL = orders.CANCEL;

	public static final String UPDATE_START_VALUE = orders.UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = orders.UPDATE_START_MARKET;
	public static final String UPDATE_START2_VALUE = orders.UPDATE_START2_VALUE;
	public static final String UPDATE_STOP_VALUE = orders.UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = orders.UPDATE_STOP_MARKET;
	
	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	public static final String DEFAULT_STATUS2 = STATUS2_PENDING;
	public static final boolean DEFAULT_WAIT_FOR_EXECUTION = true;

	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double quantity_) { return __request_place(type_place_, symbol_, stop_, start_, quantity_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double quantity_, boolean wait_for_execution_) { return remote_request.__place(type_place_, symbol_, stop_, start_, quantity_, wait_for_execution_); }

	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_) { return __request_place(type_place_, symbol_, stop_, start_, start2_, quantity_, DEFAULT_WAIT_FOR_EXECUTION); }
	
	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, boolean wait_for_execution_) { return remote_request.__place(type_place_, symbol_, stop_, start_, start2_, quantity_, wait_for_execution_); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_) { return __request_place_perc(type_place_, symbol_, stop_, start_, perc_money_, price_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_, boolean wait_for_execution_) { return remote_request.__place_perc(type_place_, symbol_, stop_, start_, perc_money_, price_, wait_for_execution_); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_) { return __request_place_perc(type_place_, symbol_, stop_, start_, start2_, perc_money_, price_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) { return remote_request.__place_perc(type_place_, symbol_, stop_, start_, start2_, perc_money_, price_, wait_for_execution_); }

	public static boolean request_cancel(int request_) { return request_cancel(request_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static boolean request_cancel(int request_, boolean wait_for_execution_) { return remote_request.cancel(request_, wait_for_execution_); }

	public static boolean request_update(int request_, String type_update_, double val_) { return request_update(request_, type_update_, val_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static boolean request_update(int request_, String type_update_, double val_, boolean wait_for_execution_) { return remote_request.update(request_, type_update_, val_, wait_for_execution_); }
		
	public static void __execute_all() { remote_execute.__execute_all(); }

	public static void wait_for_execution(int request_) { remote_request.wait_for_execution(request_); }

	public static boolean status_is_ok(String type_) { return strings.is_ok(check_status(type_)); }

	public static String check_status(String type_) { return accessory.types.check_type(type_, STATUS); }

	public static boolean status2_is_ok(String type_) { return strings.is_ok(check_status2(type_)); }

	public static String check_status2(String type_) { return accessory.types.check_type(type_, STATUS2); }

	public static boolean is_active(int request_) { return db_ib.remote.is_active(request_); }

	public static boolean is_active(String symbol_) { return db_ib.remote.is_active(symbol_); }
	
	static HashMap<String, String> get_vals(int request_) { return db_ib.remote.get_vals(request_); }

	static int get_request(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(FIELD_REQUEST, vals_); }

	static int get_order_id_main(int request_) { return db_ib.remote.get_order_id(request_); }

	static int get_order_id_main(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(FIELD_ORDER_ID_MAIN, vals_); }

	static String get_status(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(FIELD_STATUS, vals_); }

	static String get_status2(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(FIELD_STATUS2, vals_); }

	static String get_type(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(FIELD_TYPE_ORDER, vals_); }

	static String get_symbol(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(FIELD_SYMBOL, vals_); }

	static boolean get_is_market(HashMap<String, String> vals_) { return (boolean)db_ib.remote.get_val(FIELD_IS_MARKET, vals_); }

	static double get_stop(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_STOP, vals_); }

	static double get_start(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_START, vals_); }

	static double get_start2(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_START2, vals_); }

	static double get_quantity(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_QUANTITY, vals_); }

	static double get_perc_money(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_PERC_MONEY, vals_); }

	static double get_price(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(FIELD_PRICE, vals_); }
	
	static boolean order_id_is_ok(int order_id_main_, boolean is_cancel_) 
	{ 
		boolean is_ok = ((order_id_main_ > common.WRONG_ORDER_ID) && !orders.order_is_inactive(order_id_main_));
		
		if (is_ok) 
		{
			if (is_cancel_) is_ok = (ib.orders.order_is_active(order_id_main_) && !ib.orders.order_is_filled(order_id_main_));
		}
		else db_ib.remote.deactivate_order_id(order_id_main_);
		
		return is_ok;
	}
	
	static boolean order_was_updated(HashMap<String, String> vals_)
	{
		boolean output = false;
		if (!arrays.is_ok(vals_)) return output;
		
		int order_id = (int)get_order_id_main(vals_);
		String type = (String)get_type(vals_);
		
		double stop = (double)get_stop(vals_);
		double start = (double)get_start(vals_);
		double start2 = (double)get_start2(vals_);
		
		if (ib.orders.is_update(type)) output = db_ib.orders.order_was_updated(order_id, type, stop, start, start2);
		else if (ib.orders.is_cancel(type)) output = ib.orders.order_is_inactive(order_id);
		else if (ib.orders.is_place(type)) output = db_ib.orders.is_active(order_id, stop, start, start2);
		
		return output;
	}
}