package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.logs;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib.types;

public abstract class remote 
{
	public static final String _ID = "remote";
	
	public static final String REQUEST_OK = remote_request.REQUEST_OK;
	public static final String REQUEST_ERROR = remote_request.REQUEST_ERROR;
	public static final String REQUEST_IGNORED = remote_request.REQUEST_IGNORED;
	
	public static final String STATUS = types.REMOTE_STATUS;
	public static final String STATUS_ACTIVE = types.REMOTE_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.REMOTE_STATUS_INACTIVE;
	public static final String STATUS_ERROR = types.REMOTE_STATUS_ERROR;

	public static final String STATUS2 = types.REMOTE_STATUS2;
	public static final String STATUS2_PENDING = types.REMOTE_STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = types.REMOTE_STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = types.REMOTE_STATUS2_ERROR;

	public static final String ORDERS = types.ORDERS;

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

	public static final double MAX_PERC_MONEY = 90.0;
	public static final double MIN_PERC_FREE = (100.0 - MAX_PERC_MONEY) + 1.0;
	
	public static final double WRONG_MONEY2 = common.WRONG_MONEY2;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	public static final String DEFAULT_STATUS2 = STATUS2_PENDING;
	public static final boolean DEFAULT_WAIT_FOR_EXECUTION = true;
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final boolean DEFAULT_LOGS_TO_FILE = false;
	public static final String DEFAULT_PATH_LOGS = common.get_log_file_id(_ID);
	
	static String _temp_type_error = null;

	private static boolean _is_quick = DEFAULT_IS_QUICK;
	private static boolean _logs_to_file = DEFAULT_LOGS_TO_FILE;
	private static String _path_logs = DEFAULT_PATH_LOGS;
	
	public static boolean is_quick() { return _is_quick; }

	public static boolean logs_to_file() { return _logs_to_file; }
	
	public static String path_logs() { return _path_logs; }
	
	public static void path_logs(String path_logs_) { _path_logs = path_logs_; }
	
	public static void logs_to_file(boolean logs_to_file_) { _logs_to_file = logs_to_file_; }
	
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

	public static String check_status(String type_) { return accessory.types.check_type(type_, STATUS); }

	public static boolean status2_is_ok(String type_) { return strings.is_ok(check_status2(type_)); }

	public static String check_status2(String type_) { return accessory.types.check_type(type_, STATUS2); }

	public static String check_type_update(String type_) { return orders.check_update(type_); }

	public static boolean is_update_market(String type_) { return orders.is_update_market(type_); }

	public static boolean is_active(int request_) { return db_ib.remote.is_active(request_); }

	public static boolean is_active(String symbol_) { return db_ib.remote.is_active(symbol_); }

	public static String get_status2_request() { return STATUS2_PENDING; }

	public static String get_status2_execute(boolean is_ok_) { return (is_ok_ ? remote.STATUS2_EXECUTED : remote.STATUS2_ERROR); }

	public static HashMap<String, Object> get_quantity(String symbol_, double quantity_, double perc_money_, double price_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		double quantity = quantity_;
		double price = price_;
				
		if (common.percent_is_ok(perc_money_, false))
		{
			if (!common.price_is_ok(price)) price = common.get_price(symbol_);
			if (!common.price_is_ok(price)) return null;
			
			double investment = get_investment(perc_money_, true);
			if (investment <= WRONG_MONEY2) return null;
			
			quantity = investment / price;
			quantity = orders.adapt_quantity(quantity);
		}
		
		output.put(db_ib.remote.QUANTITY, quantity);
		output.put(db_ib.remote.PRICE, price);
		
		return output;
	}

	public static double get_investment(double perc_) { return get_investment(perc_, false); }

	public static int get_order_id_main(int request_) { return db_ib.remote.get_order_id(request_, _is_quick); }

	public static int get_request(int order_id_main_) { return db_ib.remote.get_request(order_id_main_, _is_quick); }

	public static int get_request(String symbol_) { return db_ib.remote.get_request(symbol_, _is_quick); }

	public static String get_symbol(int request_) { return db_ib.remote.get_symbol(request_, _is_quick); }

	public static String get_symbol_order_id(int order_id_main_) { return db_ib.remote.get_symbol_order_id(order_id_main_, _is_quick); }
	
	public static HashMap<String, String> get_vals(int request_) { return db_ib.remote.get_vals(request_, _is_quick); }
	
	public static HashMap<String, String> get_vals_order_id(int order_id_main_) { return db_ib.remote.get_vals_order_id(order_id_main_, _is_quick); }

	public static int get_request(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(db_ib.remote.REQUEST, vals_, _is_quick); }

	public static int get_order_id_main(HashMap<String, String> vals_) { return (int)db_ib.remote.get_val(db_ib.remote.ORDER_ID_MAIN, vals_, _is_quick); }

	public static String get_status(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(db_ib.remote.STATUS, vals_, _is_quick); }
	
	public static String get_status2(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(db_ib.remote.STATUS2, vals_, _is_quick); }

	public static String get_type(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(db_ib.remote.TYPE_ORDER, vals_, _is_quick); }

	public static String get_symbol(HashMap<String, String> vals_) { return (String)db_ib.remote.get_val(db_ib.remote.SYMBOL, vals_, _is_quick); }

	public static String get_type_order(int request_) { return db_ib.remote.get_type_order(request_, _is_quick); }

	public static boolean get_is_market(HashMap<String, String> vals_) { return (boolean)db_ib.remote.get_val(db_ib.remote.IS_MARKET, vals_, _is_quick); }

	public static double get_stop(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.STOP, vals_, _is_quick); }

	public static double get_start(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.START, vals_, _is_quick); }

	public static double get_start2(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.START2, vals_, _is_quick); }

	public static double get_quantity(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.QUANTITY, vals_, _is_quick); }

	public static double get_perc_money(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.PERC_MONEY, vals_, _is_quick); }

	public static double get_price(HashMap<String, String> vals_) { return (double)db_ib.remote.get_val(db_ib.remote.PRICE, vals_, _is_quick); }

	public static ArrayList<HashMap<String, String>> get_all_errors() { return db_ib.remote.get_all_errors(_is_quick); }

	public static void update_error_place(int request_, String symbol_, String type_, double quantity_, double stop_, double start_, double start2_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		String type = strings.to_string(type_);
		
		vals.put(db_ib.remote.get_col(db_ib.remote.SYMBOL), strings.to_string(symbol_));
		vals.put(db_ib.remote.get_col(db_ib.remote.TYPE_ORDER), db_ib.remote.get_key_from_type_order(type));
		vals.put(db_ib.remote.get_col(db_ib.remote.QUANTITY), quantity_);
		vals.put(db_ib.remote.get_col(db_ib.remote.STOP), stop_);
		vals.put(db_ib.remote.get_col(db_ib.remote.START), start_);
		vals.put(db_ib.remote.get_col(db_ib.remote.START2), start2_);
		
		update_error(request_, (is_request_ ? remote_request.ERROR_PLACE : remote_execute.ERROR_PLACE), vals, type);		
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
		
		if (orders.is_update(type)) output = orders.order_was_updated(order_id, type, stop, start, start2);
		else if (orders.is_cancel(type)) output = orders.is_inactive(order_id);
		else if (orders.is_place(type)) output = db_ib.orders.is_active(order_id, stop, start, start2);
		
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
		
		if (request_ > common.WRONG_REQUEST) db_ib.remote.update_error(request_, message, type_order_, _is_quick); 
	
		errors.manage(type, message);
	}

	static void update_error_place(int request_, String symbol_, String type_, double price_, double perc_, double stop_, double start_, double start2_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		String type = strings.to_string(type_);
		
		vals.put(db_ib.remote.get_col(db_ib.remote.SYMBOL), strings.to_string(symbol_));
		vals.put(db_ib.remote.get_col(db_ib.remote.TYPE_ORDER), db_ib.orders.get_key_from_type_order(type));
		vals.put(db_ib.remote.get_col(db_ib.remote.PRICE), price_);
		vals.put(db_ib.remote.get_col(db_ib.remote.PERC_MONEY), perc_);
		vals.put(db_ib.remote.get_col(db_ib.remote.STOP), stop_);
		vals.put(db_ib.remote.get_col(db_ib.remote.START), start_);
		vals.put(db_ib.remote.get_col(db_ib.remote.START2), start2_);
		
		update_error(request_, (is_request_ ? remote_request.ERROR_PLACE : remote_execute.ERROR_PLACE), vals, type);		
	}

	static void update_error_update(int request_, String symbol_, String type_, int order_id_, double val_, boolean is_request_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		String type = strings.to_string(type_);
		
		vals.put(db_ib.remote.get_col(db_ib.remote.SYMBOL), strings.to_string(symbol_));
		vals.put(db_ib.remote.get_col(db_ib.remote.TYPE_ORDER), db_ib.orders.get_key_from_type_order(type));
		vals.put("val", val_);

		if (order_id_ > common.WRONG_ORDER_ID) vals.put(db_ib.remote.get_col(db_ib.remote.ORDER_ID_MAIN), order_id_);
		
		update_error(request_, (is_request_ ? remote_request.ERROR_UPDATE : remote_execute.ERROR_UPDATE), vals, type);		
	}

	static String get_ok_message_default(String type_, int request_, String symbol_, int order_id_, boolean is_request_)
	{		
		String temp = "";
		if (strings.is_ok(symbol_)) temp = symbol_;
		if (order_id_ > common.WRONG_ORDER_ID) temp += (temp.equals("") ? "" : ", ") + Integer.toString(order_id_);
	
		String message = Integer.toString(request_) + " (";
		if (!temp.equals("")) message += temp + ", ";
		message += db_ib.orders.get_key_from_type_order(type_) + ") ";
		
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
	
	private static double get_investment(double perc_, boolean log_)
	{
		double investment = WRONG_MONEY2;
		if (!common.percent_is_ok(perc_, false)) return investment;
		
		HashMap<String, Double> money_all = ib.basic.get_money_and_free();
		if (!arrays.is_ok(money_all)) 
		{
			if (log_) log("not enough money");

			return investment;
		}
		
		double money = money_all.get(ib.basic.MONEY);	
		if (money <= basic.WRONG_MONEY2) 
		{
			if (log_) log("not enough money (" + strings.to_string(money_all) + ")");

			return investment;
		}

		double free = money_all.get(ib.basic.MONEY_FREE);
		double free_min = money * MIN_PERC_FREE / 100.0;

		if (free < free_min)
		{
			if (log_) log("not enough money (" + strings.to_string(money_all) + ")");
			
			return investment;
		}
		
		double perc = perc_;
		if (perc > MAX_PERC_MONEY) perc = MAX_PERC_MONEY;	
		
		investment = money * perc / 100.0;			
		if (investment > free) investment = MAX_PERC_MONEY * free / 100;
		
		return investment;
	}

	private static String get_error_message(String type_, Object vals_)
	{	
		String message = strings.DEFAULT;
	
		String type = accessory.types.check_type(type_, remote_execute.ERROR);
		
		if (strings.is_ok(type)) message = remote_execute.get_error_message(type);
		else
		{
			type = accessory.types.check_type(type_, remote_request.ERROR);

			if (strings.is_ok(type)) message = remote_request.get_error_message(type);
			else message = "ERROR";
		}
				
		if (vals_ != null) message += " (" + strings.to_string(vals_) + ")";

		return message;
	}
}