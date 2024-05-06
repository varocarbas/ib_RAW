package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory._keys;
import accessory.arrays;
import accessory.db_common;
import accessory.db_quick;
import accessory.logs;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib._types;
import accessory_ib.config;

public abstract class remote 
{
	public static final String _ID = "remote";
	
	public static final String DB_SOURCE = db_ib.remote.SOURCE;

	public static final String DB_SYMBOL = db_ib.remote.SYMBOL;
	public static final String DB_ORDER_ID_MAIN = db_ib.remote.ORDER_ID_MAIN;
	public static final String DB_ORDER_ID_SEC = db_ib.remote.ORDER_ID_SEC;
	public static final String DB_STATUS = db_ib.remote.STATUS;
	public static final String DB_STATUS2 = db_ib.remote.STATUS2;
	public static final String DB_START = db_ib.remote.START;
	public static final String DB_START2 = db_ib.remote.START2;
	public static final String DB_STOP = db_ib.remote.STOP;
	public static final String DB_IS_MARKET = db_ib.remote.IS_MARKET;
	public static final String DB_QUANTITY = db_ib.remote.QUANTITY;
	public static final String DB_PERC_MONEY = db_ib.remote.PERC_MONEY;
	public static final String DB_PRICE = db_ib.remote.PRICE;
	public static final String DB_ERROR = db_ib.remote.ERROR;
	public static final String DB_REQUEST = db_ib.remote.REQUEST;
	public static final String DB_TYPE_ORDER = db_ib.remote.TYPE_ORDER;
	public static final String DB_TIME2 = db_ib.remote.TIME2;

	public static final String REQUEST_OK = remote_request.REQUEST_OK;
	public static final String REQUEST_ERROR = remote_request.REQUEST_ERROR;
	public static final String REQUEST_IGNORED = remote_request.REQUEST_IGNORED;
	
	public static final String STATUS = _types.REMOTE_STATUS;
	public static final String STATUS_ACTIVE = _types.REMOTE_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = _types.REMOTE_STATUS_INACTIVE;
	public static final String STATUS_ERROR = _types.REMOTE_STATUS_ERROR;

	public static final String STATUS2 = _types.REMOTE_STATUS2;
	public static final String STATUS2_PENDING = _types.REMOTE_STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = _types.REMOTE_STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = _types.REMOTE_STATUS2_ERROR;

	public static final String ORDERS = _types.REMOTE_ORDERS;

	public static final String PLACE_MARKET = _types.REMOTE_ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = _types.REMOTE_ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = _types.REMOTE_ORDERS_PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = _types.REMOTE_ORDERS_PLACE_STOP_LIMIT;

	public static final String CANCEL = _types.REMOTE_ORDERS_CANCEL;

	public static final String UPDATE_START_VALUE = _types.REMOTE_ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = _types.REMOTE_ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_START2_VALUE = _types.REMOTE_ORDERS_UPDATE_START2_VALUE;
	public static final String UPDATE_STOP_VALUE = _types.REMOTE_ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = _types.REMOTE_ORDERS_UPDATE_STOP_MARKET;
	
	public static final String CONFIG_UPDATE_WAIT_FOR_ERRORS = _types.CONFIG_REMOTE_UPDATE_WAIT_FOR_ERRORS;

	public static final int WRONG_REQUEST = common.WRONG_REQUEST;
	public static final double WRONG_MONEY2 = common.WRONG_MONEY2;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	public static final String DEFAULT_STATUS2 = STATUS2_PENDING;
	public static final boolean DEFAULT_WAIT_FOR_EXECUTION = true;
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final boolean DEFAULT_LOGS_TO_FILE = false;
	public static final String DEFAULT_PATH_LOGS = common.get_log_file_id(_ID);
	public static final boolean DEFAULT_MULTIPLE_TRADES_SYMBOL = true;
	public static final boolean DEFAULT_UPDATE_WAIT_FOR_ERRORS = true;

	static String _temp_type_error = null;

	private static boolean _logs_to_file = DEFAULT_LOGS_TO_FILE;
	private static String _path_logs = DEFAULT_PATH_LOGS;
	
	public static boolean is_quick() { return db_common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(DB_SOURCE, is_quick_); }

	public static boolean logs_to_file() { return _logs_to_file; }
	
	public static String path_logs() { return _path_logs; }
	
	public static void path_logs(String path_logs_) { _path_logs = path_logs_; }
	
	public static void logs_to_file(boolean logs_to_file_) { _logs_to_file = logs_to_file_; }
	
	public static boolean update_wait_for_errors() { return config.get_remote_boolean(CONFIG_UPDATE_WAIT_FOR_ERRORS); }

	public static boolean update_wait_for_errors(boolean update_wait_for_errors_) { return config.update_remote(CONFIG_UPDATE_WAIT_FOR_ERRORS, update_wait_for_errors_); }
	
	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double quantity_) { return __request_place(type_place_, symbol_, stop_, start_, quantity_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double quantity_, boolean wait_for_execution_) { return remote_request.__place(type_place_, symbol_, stop_, start_, quantity_, wait_for_execution_); }

	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_) { return __request_place(type_place_, symbol_, stop_, start_, start2_, quantity_, DEFAULT_WAIT_FOR_EXECUTION); }
	
	public static int __request_place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, boolean wait_for_execution_) { return remote_request.__place(type_place_, symbol_, stop_, start_, start2_, quantity_, wait_for_execution_); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_) { return __request_place_perc(type_place_, symbol_, stop_, start_, perc_money_, price_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_, boolean wait_for_execution_) { return remote_request.__place_perc(type_place_, symbol_, stop_, start_, perc_money_, price_, wait_for_execution_); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_) { return __request_place_perc(type_place_, symbol_, stop_, start_, start2_, perc_money_, price_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static int __request_place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) { return remote_request.__place_perc(type_place_, symbol_, stop_, start_, start2_, perc_money_, price_, wait_for_execution_); }

	public static String __request_cancel(int request_) { return __request_cancel(request_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static String __request_cancel(int request_, boolean wait_for_execution_) { return remote_request.__cancel(request_, wait_for_execution_); }

	public static String __request_update(int request_, String type_update_, double val_) { return __request_update(request_, type_update_, val_, DEFAULT_WAIT_FOR_EXECUTION); }

	public static String __request_update(int request_, String type_update_, double val_, boolean wait_for_execution_) { return remote_request.__update(request_, type_update_, val_, wait_for_execution_); }

	public static void __execute_all() { remote_execute.__execute_all(); }

	public static void __wait_for_execution(int request_, boolean is_cancel_) { remote_request.__wait_for_execution(request_, is_cancel_); }

	public static boolean status_is_ok(String type_) { return strings.is_ok(check_status(type_)); }

	public static String check_status(String type_) { return accessory._types.check_type(type_, STATUS); }

	public static boolean status2_is_ok(String type_) { return strings.is_ok(check_status2(type_)); }

	public static String check_status2(String type_) { return accessory._types.check_type(type_, STATUS2); }

	public static String check_type_place(String type_) { return orders.check_place(type_); }

	public static String check_type_update(String type_) { return orders.check_update(type_); }

	public static boolean is_place(String type_) { return orders.is_place(type_); }

	public static boolean is_update(String type_) { return orders.is_update(type_); }

	public static boolean is_cancel(String type_) { return orders.is_cancel(type_); }

	public static boolean is_update_market(String type_) { return orders.is_update_market(type_); }

	public static boolean is_active(int request_) { return db_ib.remote.is_active(request_); }

	public static boolean is_active(String symbol_) { return db_ib.remote.is_active(symbol_); }

	public static String get_status2_request() { return STATUS2_PENDING; }

	public static String get_status2_execute(boolean is_ok_) { return (is_ok_ ? remote.STATUS2_EXECUTED : remote.STATUS2_ERROR); }

	public static HashMap<String, Object> __get_quantity(String symbol_, double quantity_, double perc_money_, double price_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		double quantity = quantity_;
		double price = price_;
				
		if (common.percent_is_ok(perc_money_, false))
		{
			if (!common.price_is_ok(price)) price = common.get_price(symbol_);
			if (!common.price_is_ok(price)) return null;
			
			double investment = __get_investment(perc_money_, true);
			if (investment <= WRONG_MONEY2) return null;
			
			quantity = investment / price;
			quantity = orders.adapt_quantity(quantity);
		}
		
		output.put(DB_QUANTITY, quantity);
		output.put(DB_PRICE, price);
		
		return output;
	}

	public static double __get_investment(double perc_) { return __get_investment(perc_, false); }

	public static int get_order_id_main(int request_) { return db_ib.remote.get_order_id(request_, is_quick()); }

	public static int get_request(int order_id_main_) { return db_ib.remote.get_request(order_id_main_); }

	public static int get_any_request(String symbol_) { return db_ib.remote.get_any_request(symbol_); }

	public static String get_symbol(int request_) { return db_ib.remote.get_symbol(request_); }

	public static String get_symbol_order_id(int order_id_main_) { return db_ib.remote.get_symbol_order_id(order_id_main_); }
	
	public static HashMap<String, String> get_vals(int request_) { return db_ib.remote.get_vals(request_); }
	
	public static HashMap<String, String> get_vals_order_id(int order_id_main_) { return db_ib.remote.get_vals_order_id(order_id_main_); }

	public static int get_request(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(DB_REQUEST, vals_); }

	public static int get_order_id_main(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(DB_ORDER_ID_MAIN, vals_); }

	public static String get_status(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(DB_STATUS, vals_); }
	
	public static String get_status2(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(DB_STATUS2, vals_); }

	public static String get_type(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(DB_TYPE_ORDER, vals_); }

	public static String get_symbol(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(DB_SYMBOL, vals_); }

	public static String get_type_order(int request_) { return db_ib.remote.get_type_order(request_); }

	public static boolean get_is_market(HashMap<String, String> vals_) { return (boolean)db_ib.remote.get_val(DB_IS_MARKET, vals_); }

	public static double get_stop(int request_) { return (double)db_ib.remote.get_stop(request_); }

	public static double get_start(int request_) { return (double)db_ib.remote.get_start(request_); }

	public static double get_stop(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_STOP, vals_); }

	public static double get_start(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_START, vals_); }

	public static double get_start2(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_START2, vals_); }

	public static double get_quantity(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_QUANTITY, vals_); }

	public static double get_perc_money(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_PERC_MONEY, vals_); }

	public static double get_price(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(DB_PRICE, vals_); }

	public static ArrayList<HashMap<String, String>> get_all_errors() { return db_ib.remote.get_all_errors(); }
	
	public static String get_status_key(String status_type_) { return _keys.get_startup_key(status_type_, STATUS); }

	public static String get_status_type(String status_key_) { return _keys.get_startup_type(status_key_, STATUS); }
	
	public static String get_status2_key(String status2_type_) { return _keys.get_startup_key(status2_type_, STATUS2); }

	public static String get_status2_type(String status2_key_) { return _keys.get_startup_type(status2_key_, STATUS2); }
	
	public static String get_order_key(String order_type_) { return _keys.get_startup_key(order_type_, ORDERS); }

	public static String get_order_type(String order_key_) { return _keys.get_startup_type(order_key_, ORDERS); }

	static boolean order_was_updated(HashMap<String, String> vals_)
	{
		boolean output = false;
		if (!arrays.is_ok(vals_)) return output;
		
		int order_id = (int)get_order_id_main(vals_);
		String type = (String)get_type(vals_);
		
		double stop = (double)get_stop(vals_);
		double start = (double)get_start(vals_);
		double start2 = (double)get_start2(vals_);
		
		if (is_update(type)) output = orders.order_was_updated(order_id, type, stop, start, start2);
		else if (is_cancel(type)) output = orders.is_inactive(order_id);
		else if (is_place(type)) output = db_ib.orders.is_active(order_id, stop, start, start2);
		
		return output;
	}
	
	static void update_error(int request_, String type_, Object vals_, String type_order_) 
	{ 
		String type = type_;
		
		if (strings.is_ok(_temp_type_error))
		{
			type = _temp_type_error;
			
			_temp_type_error = null;
		}
		
		String message = get_error_message(type, vals_);
		
		if (request_ > common.WRONG_REQUEST) db_ib.remote.update_error(request_, message, type_order_, is_place(type_order_)); 
	
		errors.manage(type, message);
	}

	static void update_error_place(int request_, String symbol_, String type_, double quantity_, double stop_, double start_, double start2_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_quick.get_col(DB_SOURCE, DB_QUANTITY), quantity_);
		
		update_error_place(request_, symbol_, type_, stop_, start_, start2_, is_request_, vals);
	}

	static void update_error_place(int request_, String symbol_, String type_, double price_, double perc_, double stop_, double start_, double start2_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_quick.get_col(DB_SOURCE, DB_PRICE), price_);
		vals.put(db_quick.get_col(DB_SOURCE, DB_PERC_MONEY), perc_);
		
		update_error_place(request_, symbol_, type_, stop_, start_, start2_, is_request_, vals);
	}

	static void update_error_update(int request_, String symbol_, String type_, int order_id_, double val_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		String type = strings.to_string(type_);
		
		vals.put(db_quick.get_col(DB_SOURCE, DB_SYMBOL), strings.to_string(symbol_));
		vals.put(db_quick.get_col(DB_SOURCE, DB_TYPE_ORDER), db_ib.orders.store_order_type(type));
		vals.put("val", val_);

		if (order_id_ > common.WRONG_ORDER_ID) vals.put(db_quick.get_col(DB_SOURCE, DB_ORDER_ID_MAIN), order_id_);
		
		update_error(request_, (is_request_ ? remote_request.ERROR_UPDATE : remote_execute.ERROR_UPDATE), vals, type);		
	}

	static String get_ok_message_default(String type_, int request_, String symbol_, int order_id_, boolean is_request_)
	{		
		String temp = "";
		if (strings.is_ok(symbol_)) temp = symbol_;
		if (order_id_ > common.WRONG_ORDER_ID) temp += (temp.equals("") ? "" : ", ") + Integer.toString(order_id_);
	
		String message = Integer.toString(request_) + " (";
		if (!temp.equals("")) message += temp + ", ";
		message += db_ib.orders.store_order_type(type_) + ") ";
		
		message += (is_request_ ? "requested" : "executed") + " successfully";
		
		return message;
	}
	
	static String get_error_message_default(String type_, boolean is_request_) 
	{ 
		String message = "";
		
		if (is_request_) message = (strings.are_equal(type_, remote_request.ERROR_REQUEST) ? "" : "request ");
		else message = "execution ";
		
		message = "wrong " + message + accessory._keys.get_key(type_, (is_request_ ? remote_request.ERROR : remote_execute.ERROR));
			
		return message; 
	}
	
	static void log(String message_)
	{
		logs.update_screen(message_, true);
		
		if (_logs_to_file) logs.update_file(message_, _path_logs, false, true); 
	}

	private static void update_error_place(int request_, String symbol_, String type_, double stop_, double start_, double start2_, boolean is_request_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(vals_);

		String type = strings.to_string(type_);
		
		vals.put(db_quick.get_col(DB_SOURCE, DB_SYMBOL), strings.to_string(symbol_));
		vals.put(db_quick.get_col(DB_SOURCE, DB_TYPE_ORDER), db_ib.orders.store_order_type(type));
		vals.put(db_quick.get_col(DB_SOURCE, DB_STOP), stop_);
		vals.put(db_quick.get_col(DB_SOURCE, DB_START), start_);
		vals.put(db_quick.get_col(DB_SOURCE, DB_START2), start2_);
		
		update_error(request_, (is_request_ ? remote_request.ERROR_PLACE : remote_execute.ERROR_PLACE), vals, type);		
	}
	
	private static double __get_investment(double perc_, boolean log_)
	{
		double output = basic.__get_investment(perc_);
		
		if (output <= basic.WRONG_MONEY2 && log_) log("not enough money");
		
		return output;
	}

	private static String get_error_message(String type_, Object vals_)
	{	
		String message = strings.DEFAULT;
	
		String type = accessory._types.check_type(type_, remote_execute.ERROR);
		
		if (strings.is_ok(type)) message = remote_execute.get_error_message(type);
		else
		{
			type = accessory._types.check_type(type_, remote_request.ERROR);

			if (strings.is_ok(type)) message = remote_request.get_error_message(type);
			else message = "ERROR";
		}
				
		if (vals_ != null) message += " (" + strings.to_string(vals_) + ")";

		return message;
	}
}