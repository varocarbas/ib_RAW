package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory.types;
import ib.order;

public abstract class remote extends parent_static
{
	public static final String SOURCE = common.SOURCE_REMOTE;
	
	public static final String USER = common.FIELD_USER;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String STATUS2 = common.FIELD_STATUS2;
	public static final String START = common.FIELD_START;
	public static final String START2 = common.FIELD_START2;
	public static final String STOP = common.FIELD_STOP;
	public static final String IS_MARKET = common.FIELD_IS_MARKET;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String PERC_MONEY = common.FIELD_PERC_MONEY;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String ERROR = common.FIELD_ERROR;
	public static final String REQUEST = common.FIELD_REQUEST;
	public static final String TYPE_ORDER = common.FIELD_TYPE_ORDER;
	public static final String TIME2 = common.FIELD_TIME2;
	
	public static final String STATUS_ACTIVE_KEY = get_key_from_status(ib.remote.STATUS_ACTIVE);
	public static final String STATUS_INACTIVE_KEY = get_key_from_status(ib.remote.STATUS_INACTIVE);
	public static final String STATUS_ERROR_KEY = get_key_from_status(ib.remote.STATUS_ERROR);

	public static final String STATUS2_PENDING_KEY = get_key_from_status2(ib.remote.STATUS2_PENDING);
	public static final String STATUS2_EXECUTED_KEY = get_key_from_status2(ib.remote.STATUS2_EXECUTED);
	public static final String STATUS2_ERROR_KEY = get_key_from_status2(ib.remote.STATUS2_ERROR);
	
	private static HashMap<String, String> _cols = new HashMap<String, String>();
	
	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	
	
	public static ArrayList<HashMap<String, String>> get_all_pending(boolean any_user_) { return common.get_all_vals(SOURCE, get_fields(), get_where_pending(any_user_)); }

	public static boolean contains_active() { return common.exists(SOURCE, get_where_active(true)); }

	public static boolean exists(int request_) { return common.exists(SOURCE, get_where_request(request_)); }

	public static boolean is_active(int request_) { return common.exists(SOURCE, db_where.join(get_where_request(request_), get_where_active(), db_where.LINK_AND)); }

	public static boolean is_active(String symbol_) { return common.exists(SOURCE, db_where.join(get_where_symbol(symbol_), get_where_active(), db_where.LINK_AND)); }

	public static boolean is_pending(int request_) { return common.exists(SOURCE, get_where_pending()); }

	public static boolean delete(int request_) { return common.delete(SOURCE, get_where_request(request_)); }

	public static HashMap<String, String> get_vals(int request_, boolean is_quick_) 
	{ 
		String source = SOURCE;
		String where = get_where_request(request_, is_quick_);
		
		return (is_quick_ ? common.get_vals_quick(source, get_cols(), where) : common.get_vals(SOURCE, get_fields(), where)); 
	}

	public static String get_status(int request_) { return common.get_type(SOURCE, STATUS, ib.remote.STATUS, get_where_request(request_)); }

	public static String get_status2(int request_) { return common.get_type(SOURCE, STATUS2, ib.remote.STATUS2, get_where_request(request_)); }

	public static String get_error(int request_) { return common.get_string(SOURCE, ERROR, get_where_request(request_)); }

	public static int get_order_id(int request_) { return get_order_id(request_, true); }

	public static int get_order_id(int request_, boolean is_main_) { return common.get_int(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), get_where_request(request_)); }
	
	public static int __request_start(order order_, double perc_money_, double price_)
	{
		__lock();

		HashMap<String, Object> vals = to_hashmap(order_);
			
		vals = get_vals_common(get_status2_key_request(), vals);
		
		if (perc_money_ > ib.common.WRONG_MONEY) vals.put(PERC_MONEY, perc_money_);
		if (price_ > ib.common.WRONG_PRICE) vals.put(PRICE, price_);
		
		int request = ib.common.WRONG_REQUEST;
		
		int max = 5;
		int count = 0;
		
		while (true)
		{
			request = get_new_request();
			
			if (request > ib.common.WRONG_REQUEST)
			{
				vals.put(REQUEST, request);			
				if (common.insert(SOURCE, vals)) break;	
				
			}
			
			count++;
			if (count > max) 
			{
				request = ib.common.WRONG_REQUEST;
				
				break;
			}
		}		
		
		__unlock();

		return request;
	}

	public static boolean request_update_type_order(int request_, String type_)
	{
		HashMap<String, Object> vals = get_vals_common(get_status2_key_request());
		
		vals.put(TYPE_ORDER, get_key_from_type_order(type_));
		
		return update(request_, vals);
	}

	public static boolean request_update_type_order_values(int request_, String type_, String field_, double val_)
	{
		HashMap<String, Object> vals = get_vals_common(get_status2_key_request());
		
		vals.put(TYPE_ORDER, get_key_from_type_order(type_));
		
		if (ib.orders.is_update_market(type_)) vals.put(IS_MARKET, true);
		else vals.put(field_, common.adapt_price(val_));
		
		return update(request_, vals);	
	}

	public static boolean update(int request_, HashMap<String, Object> vals_) { return common.update(SOURCE, vals_, get_where_request(request_)); }
	
	public static boolean update_status(int request_, String status_key_) { return update(request_, STATUS, status_key_); }

	public static boolean update_status2(int request_, String status2_key_) { return update(request_, STATUS2, status2_key_); }

	public static boolean update_error(int request_, String error_) 
	{
		String error = common.adapt_string(error_, ERROR);
		if (!strings.is_ok(error)) return false;
		
		HashMap<String, Object> vals = get_vals_common(STATUS2_ERROR_KEY);
		vals.put(ERROR, error);

		return remote.update(request_, vals);
	}

	public static boolean update_order_id(int order_id_main_, HashMap<String, Object> vals_) { return common.update(SOURCE, vals_, get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate_order_id(int order_id_main_) { return common.update(SOURCE, STATUS, STATUS_INACTIVE_KEY, get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate(int request_) { return update_status(request_, STATUS_INACTIVE_KEY); }

	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS); }

	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.remote.STATUS); }
	
	public static String get_status2_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS2); }

	public static String get_key_from_status2(String status2_) { return common.get_key_from_type(status2_, ib.remote.STATUS2); }
	
	public static String get_type_order_from_key(String key_) { return orders.get_type_order_from_key(key_); }

	public static String get_key_from_type_order(String type_) { return orders.get_key_from_type_order(type_); }

	public static Object get_val(String field_, HashMap<String, String> vals_, boolean is_quick_)
	{
		Object val = null;

		String field_col = (is_quick_ ? get_col(field_) : field_);
		
		String val0 = vals_.get(field_col);
		
		if (field_is_boolean(field_)) val = strings.to_boolean(val0);
		else if (field_is_int(field_)) val = Integer.parseInt(val0);
		else if (field_is_decimal(field_)) val = Double.parseDouble(val0);
		else 
		{
			if (field_.equals(STATUS)) val = get_status_from_key(val0);
			else if (field_.equals(STATUS2)) val = get_status2_from_key(val0);
			else if (field_.equals(TYPE_ORDER)) val = get_type_order_from_key(val0);
			else val = val0;
		}
		
		return val;
	}

	public static HashMap<String, Object> get_vals_common(String status2_key_, HashMap<String, Object> vals_) 
	{ 
		HashMap<String, Object> vals = (arrays.is_ok(vals_) ? new HashMap<String, Object>(vals_) : new HashMap<String, Object>());
		
		vals.put(TIME2, ib.common.get_current_time2());
		vals.put(STATUS2, status2_key_);
		
		return vals; 
	}	
	
	public static String get_status2_key_request() { return get_key_from_status2(ib.remote.get_status2_request()); }

	public static String get_status2_key_execute(boolean is_ok_) { return get_key_from_status2(ib.remote.get_status2_execute(is_ok_)); }

	public static void update_status2_execute(int request_, boolean is_ok_) { update_status2(request_, get_status2_key_execute(is_ok_)); }

	public static HashMap<String, Object> to_hashmap(order order_)
	{
		HashMap<String, Object> vals = orders.to_hashmap(order_, true);
		
		String type_order = (ib.orders.PLACE + types.SEPARATOR + (String)vals.get(orders.TYPE_PLACE));
		vals.put(TYPE_ORDER, get_key_from_type_order(type_order));
		
		vals.remove(orders.TYPE_PLACE);
		
		return vals;
	}

	public static String get_col(String field_) 
	{
		if (_cols.size() == 0) populate_cols();
		
		return _cols.get(field_);
	}
	
	private static String[] get_cols() 
	{ 
		if (_cols.size() == 0) populate_cols();

		return arrays.to_array(arrays.get_values_hashmap(_cols));
	}
	
	private static void populate_cols()
	{
		for (String field: get_fields()) { _cols.put(field, common.get_col(SOURCE, field)); }
	}
	
	private static boolean field_is_boolean(String field_) { return field_.equals(IS_MARKET); }
	
	private static boolean field_is_int(String field_) { return (field_.equals(ORDER_ID_MAIN) || field_.equals(ORDER_ID_SEC) || field_.equals(REQUEST)); }
	
	private static boolean field_is_decimal(String field_) { return (field_.equals(START) || field_.equals(START2) || field_.equals(STOP) || field_.equals(QUANTITY) || field_.equals(PERC_MONEY) || field_.equals(PRICE)); }
	
	private static HashMap<String, Object> get_vals_common(String status2_key_) { return get_vals_common(status2_key_, null); }

	private static int get_new_request() 
	{	
		int min = ib.common.WRONG_REQUEST + 1;
		int request = numbers.get_random_int(min, numbers.MAX_INT);
		
		while (exists(request)) { request = (request < numbers.MAX_INT ? request + 1 : min); }

		return request;
	}

	private static boolean update(int request_, String field_, Object val_) { return common.update(SOURCE, field_, val_, get_where_request(request_)); }
	
	private static String[] get_fields() { return new String[] { USER, SYMBOL, ORDER_ID_MAIN, ORDER_ID_SEC, STATUS, STATUS2, START, START2, STOP, QUANTITY, PERC_MONEY, PRICE, REQUEST, TYPE_ORDER }; }

	private static String get_where_request(int request_) { return get_where_request(request_, false); }

	private static String get_where_request(int request_, boolean is_quick_) { return common.get_where(SOURCE, REQUEST, Integer.toString(request_), is_quick_); }

	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }

	private static String get_where_active() { return get_where_active(false); }

	private static String get_where_active(boolean any_user_) { return get_where_status(STATUS_ACTIVE_KEY, any_user_); }

	private static String get_where_pending() { return get_where_pending(false); }

	private static String get_where_pending(boolean any_user_) { return db_where.join(get_where_status2(STATUS2_PENDING_KEY, any_user_), get_where_status(STATUS_ACTIVE_KEY, any_user_), db_where.LINK_AND); }

	private static String get_where_status(String status_key_, boolean any_user_) { return common.get_where(SOURCE, STATUS, status_key_, false, !any_user_); }

	private static String get_where_status2(String status2_key_, boolean any_user_) { return common.get_where(SOURCE, STATUS2, status2_key_, false, !any_user_); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }
}