package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db;
import accessory.db_common;
import accessory.db_where;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory.types;
import ib._order;

public abstract class remote extends parent_static
{
	public static final String SOURCE = common.SOURCE_REMOTE;
	public static final String SOURCE_OLD = common.SOURCE_REMOTE_OLD;
	
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
		
	private static HashMap<String, String> _cols = new HashMap<String, String>();
	private static HashMap<String, String> _statuses = new HashMap<String, String>();
	private static HashMap<String, String> _statuses2 = new HashMap<String, String>();
	private static HashMap<String, String> _type_orders = new HashMap<String, String>();

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	
	
	public static ArrayList<HashMap<String, String>> get_all_pending(boolean is_quick_) { return common.get_all_vals(SOURCE, (is_quick_ ? get_cols() : get_fields()), get_where_pending(), is_quick_); }
	
	public static ArrayList<HashMap<String, String>> get_all_errors(boolean is_quick_) { return common.get_all_vals(SOURCE, (is_quick_ ? get_cols() : get_fields()), get_where_error(), is_quick_); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || contains_active_requests()); }
	
	public static boolean contains_active_requests() { return common.exists(SOURCE, get_where_active()); }

	public static boolean exists(int request_) { return common.exists(SOURCE, get_where_request(request_)); }

	public static boolean is_active(int request_) { return common.exists(SOURCE, common.join_wheres(get_where_request(request_), get_where_active())); }

	public static boolean is_active(String symbol_) { return common.exists(SOURCE, common.join_wheres(get_where_symbol(symbol_), get_where_active())); }

	public static boolean is_pending(int request_) { return common.exists(SOURCE, get_where_pending()); }

	public static boolean delete(int request_) { return common.delete(SOURCE, get_where_request(request_)); }

	public static HashMap<String, String> get_vals(int request_, boolean is_quick_) 
	{ 
		String source = SOURCE;
		String where = get_where_request(request_);
		
		return (is_quick_ ? common.get_vals_quick(source, get_cols(), where) : common.get_vals(SOURCE, get_fields(), where)); 
	}

	public static HashMap<String, String> get_vals_order_id(int order_id_main_, boolean is_quick_) 
	{ 
		String source = SOURCE;
		String where = get_where_order_id(order_id_main_);
		
		return (is_quick_ ? common.get_vals_quick(source, get_cols(), where) : common.get_vals(SOURCE, get_fields(), where)); 
	}

	public static String get_status(int request_, boolean is_quick_) { return get_status_from_key(common.get_string(SOURCE, get_field_col(STATUS, is_quick_), get_where_request(request_), is_quick_)); }

	public static String get_status2(int request_, boolean is_quick_) { return get_status_from_key(common.get_string(SOURCE, get_field_col(STATUS2, is_quick_), get_where_request(request_), is_quick_)); }

	public static String get_type_order(int request_, boolean is_quick_) { return get_type_order_from_key(common.get_string(SOURCE, get_field_col(TYPE_ORDER, is_quick_), get_where_request(request_), is_quick_)); }

	public static String get_error(int request_, boolean is_quick_) { return common.get_string(SOURCE, get_field_col(ERROR, is_quick_), get_where_request(request_)); }

	public static int get_order_id(int request_, boolean is_quick_) { return get_order_id(request_, true, is_quick_); }

	public static int get_order_id(int request_, boolean is_main_, boolean is_quick_) 
	{ 
		int output = common.get_int(SOURCE, get_field_col((is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), is_quick_), get_where_request(request_), is_quick_); 
	
		return (output == db.WRONG_INT ? ib.common.WRONG_ORDER_ID : output);
	}

	public static int get_request(String symbol_, boolean is_quick_) 
	{
		String where = common.join_wheres(get_where_symbol(symbol_), get_where_active());

		if (common.get_count(SOURCE, where) != 1) return ib.common.WRONG_REQUEST;
		
		int output = common.get_int(SOURCE, get_field_col(REQUEST, is_quick_), where, is_quick_);
	
		return (output == db.WRONG_INT ? ib.common.WRONG_REQUEST : output);
	}

	public static int get_request(int order_id_main_, boolean is_quick_) 
	{ 
		int output = common.get_int(SOURCE, get_field_col(REQUEST, is_quick_), get_where_order_id(order_id_main_), is_quick_); 
		
		return (output == db.WRONG_INT ? ib.common.WRONG_REQUEST : output);
	}

	public static String get_symbol(int request_, boolean is_quick_) 
	{ 
		String output = common.get_string(SOURCE, get_field_col(SYMBOL, is_quick_), get_where_request(request_), is_quick_); 
	
		return (strings.are_equal(output, db.WRONG_STRING) ? strings.DEFAULT : output);
	}

	public static String get_symbol_order_id(int order_id_main_, boolean is_quick_) 
	{ 
		String output = common.get_string(SOURCE, get_field_col(SYMBOL, is_quick_), get_where_order_id(order_id_main_), is_quick_); 
		
		return (strings.are_equal(output, db.WRONG_STRING) ? strings.DEFAULT : output);		
	}
	
	public static int __request_start(_order order_, double perc_money_, double price_, boolean is_quick_)
	{
		Object vals = to_hashmap(order_, is_quick_);	
		vals = get_vals_common(get_status2_key_request(), vals, is_quick_);
		
		if (perc_money_ > ib.common.WRONG_MONEY) vals = add_to_vals(PERC_MONEY, perc_money_, vals, is_quick_);
		if (price_ > ib.common.WRONG_PRICE) vals = add_to_vals(PRICE, price_, vals, is_quick_);

		return __insert_new(vals, is_quick_);
	}

	public static int __insert_new(Object vals_, boolean is_quick_)
	{
		__lock();

		int request = ib.common.WRONG_REQUEST;
		
		int max = 5;
		int count = 0;
		
		while (true)
		{
			request = get_new_request();
			
			if (request > ib.common.WRONG_REQUEST)
			{
				if (common.insert(SOURCE, add_to_vals(REQUEST, request, vals_, is_quick_), is_quick_)) break;	
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
	
	public static boolean request_update_type_order(int request_, String type_, boolean is_quick_)
	{
		Object vals = get_vals_common(get_status2_key_request(), is_quick_);
		
		vals = add_to_vals(TYPE_ORDER, get_key_from_type_order(type_), vals, is_quick_);
		
		return update(request_, vals, is_quick_);
	}

	public static boolean request_update_type_order_values(int request_, String type_, String field_, double val_, boolean is_quick_)
	{
		Object vals = get_vals_common(get_status2_key_request(), is_quick_);

		vals = add_to_vals(TYPE_ORDER, get_key_from_type_order(type_), vals, is_quick_);

		if (ib.remote.is_update_market(type_)) vals = add_to_vals(IS_MARKET, true, vals, is_quick_);
		else vals = add_to_vals(field_, common.adapt_price(val_), vals, is_quick_);
		
		return update(request_, vals, is_quick_);	
	}
	
	public static boolean update(int request_, Object vals_, boolean is_quick_) { return common.update_where(SOURCE, vals_, get_where_request(request_), is_quick_); }
	
	public static boolean update_status(int request_, String status_key_) { return update(request_, STATUS, status_key_); }

	public static boolean update_status2(int request_, String status2_key_) { return update(request_, STATUS2, status2_key_); }

	public static boolean update_error(int request_, String error_, String type_order_, boolean deactivate_, boolean is_quick_) 
	{
		String error = common.adapt_string(error_, ERROR);
		if (!strings.is_ok(error)) error = "ERROR";
		
		Object vals = get_vals_common(get_key_from_status2(ib.remote.STATUS2_ERROR), is_quick_);
		
		vals = add_to_vals(ERROR, error, vals, is_quick_);
		if (deactivate_) vals = add_to_vals(STATUS, get_key_from_status(ib.remote.STATUS_INACTIVE), vals, is_quick_);
		
		String type_order = get_key_from_type_order(type_order_);
		if (!strings.is_ok(type_order)) type_order = type_order_;
		
		vals = add_to_vals(TYPE_ORDER, type_order, vals, is_quick_);
		
		return update(request_, vals, is_quick_);
	}

	public static boolean update_order_id(int order_id_main_, Object vals_, boolean is_quick_) { return common.update_where(SOURCE, vals_, get_where_order_id(order_id_main_), is_quick_); }
	
	public static boolean deactivate_order_id(int order_id_main_) { return common.update(SOURCE, STATUS, get_key_from_status(ib.remote.STATUS_INACTIVE), get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate(int request_) { return update_status(request_, get_key_from_status(ib.remote.STATUS_INACTIVE)); }

	public static String get_key_from_status(String status_) 
	{ 
		if (_statuses.size() == 0) populate_statuses();

		return ((strings.is_ok(status_) && _statuses.containsKey(status_)) ? _statuses.get(status_) : strings.DEFAULT);
	}
	
	public static String get_status_from_key(String key_) 
	{ 
		if (_statuses.size() == 0) populate_statuses();

		return ((strings.is_ok(key_) && _statuses.containsValue(key_)) ? (String)arrays.get_key(_statuses, key_) : strings.DEFAULT);
	}
	
	public static String get_key_from_status2(String status2_) 
	{ 
		if (_statuses2.size() == 0) populate_statuses2();

		return ((strings.is_ok(status2_) && _statuses2.containsKey(status2_)) ? _statuses2.get(status2_) : strings.DEFAULT);
	}
	
	public static String get_status2_from_key(String key_) 
	{ 
		if (_statuses2.size() == 0) populate_statuses2();

		return ((strings.is_ok(key_) && _statuses2.containsValue(key_)) ? (String)arrays.get_key(_statuses2, key_) : strings.DEFAULT);
	}
	
	public static String get_key_from_type_order(String type_order_) 
	{ 
		if (_type_orders.size() == 0) populate_type_orders();

		return ((strings.is_ok(type_order_) && _type_orders.containsKey(type_order_)) ? _type_orders.get(type_order_) : strings.DEFAULT);
	}
	
	public static String get_type_order_from_key(String key_) 
	{ 
		if (_type_orders.size() == 0) populate_type_orders();

		return ((strings.is_ok(key_) && _type_orders.containsValue(key_)) ? (String)arrays.get_key(_type_orders, key_) : strings.DEFAULT);
	}

	public static double get_start(int request_, boolean is_quick_)
	{
		double output = common.get_decimal_quick(SOURCE, remote.get_field_col(START, is_quick_), get_where_request(request_));
		
		return (output == db.WRONG_DECIMAL ? ib.common.WRONG_PRICE : output);
	}
	
	public static double get_stop(int request_, boolean is_quick_)
	{
		double output = common.get_decimal_quick(SOURCE, remote.get_field_col(STOP, is_quick_), get_where_request(request_));
		
		return (output == db.WRONG_DECIMAL ? ib.common.WRONG_PRICE : output);
	}
	
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

	public static Object get_vals_common(String status2_key_, Object vals_, boolean is_quick_) 
	{ 
		Object vals = add_to_vals(TIME2, ib.common.get_current_time2(), vals_, is_quick_);
		
		vals = add_to_vals(STATUS2, status2_key_, vals, is_quick_);
		
		return vals; 
	}	
	
	public static String get_status2_key_request() { return get_key_from_status2(ib.remote.get_status2_request()); }

	public static String get_status2_key_execute(boolean is_ok_) { return get_key_from_status2(ib.remote.get_status2_execute(is_ok_)); }

	public static void update_status2_execute(int request_, boolean is_ok_) { update_status2(request_, get_status2_key_execute(is_ok_)); }

	public static Object to_hashmap(_order order_, boolean is_quick_)
	{
		Object vals = orders.to_hashmap(order_, true, is_quick_);
		
		String field_col = (is_quick_ ? db_common.get_col(orders.SOURCE, orders.TYPE_PLACE) : orders.TYPE_PLACE);
		
		String type_order = (ib.orders.PLACE + types.SEPARATOR + arrays.get_value(vals, field_col));
		vals = add_to_vals(TYPE_ORDER, get_key_from_type_order(type_order), vals, is_quick_);
		
		vals = arrays.remove_key(vals, field_col);
		
		return vals;
	}

	public static String get_col(String field_) 
	{
		if (_cols.size() == 0) populate_cols();
		
		return _cols.get(field_);
	}

	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String field_, Object val_, Object vals_, boolean is_quick_)
	{
		Object output = null;
		
		if (is_quick_)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(get_col(field_), accessory.db.adapt_input(val_));
		
			output = vals;
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_, val_);
			
			output = vals;
		}
		
		return output;
	}

	private static String get_field_col(String field_, boolean is_quick_) { return (is_quick_ ? get_col(field_) : field_); }

	private static String[] get_cols() 
	{ 
		if (_cols.size() == 0) populate_cols();

		return arrays.to_array(arrays.get_values_hashmap(_cols));
	}
	
	private static void populate_cols()
	{
		for (String field: get_fields()) { _cols.put(field, db_common.get_col(SOURCE, field)); }
	}	
	
	private static String[] get_fields() { return new String[] { SYMBOL, TIME2, ORDER_ID_MAIN, ORDER_ID_SEC, STATUS, STATUS2, START, START2, STOP, QUANTITY, PERC_MONEY, PRICE, REQUEST, TYPE_ORDER, ERROR, IS_MARKET }; }
		
	private static void populate_statuses()
	{
		_statuses = new HashMap<String, String>();
		
		String[] statuses = new String[] { ib.remote.STATUS_ACTIVE, ib.remote.STATUS_INACTIVE, ib.remote.STATUS_ERROR };

		for (String status: statuses) { _statuses.put(status, get_key_from_status_internal(status)); }
	}
	
	private static void populate_statuses2()
	{
		_statuses2 = new HashMap<String, String>();
		
		String[] statuses2 = new String[] { ib.remote.STATUS2_PENDING, ib.remote.STATUS2_EXECUTED, ib.remote.STATUS2_ERROR };

		for (String status2: statuses2) { _statuses2.put(status2, get_key_from_status2_internal(status2)); }
	}
	
	private static void populate_type_orders()
	{
		_type_orders = new HashMap<String, String>();
		
		String[] types = new String[] { ib.remote.PLACE_LIMIT, ib.remote.PLACE_MARKET, ib.remote.PLACE_STOP, ib.remote.PLACE_STOP_LIMIT, ib.remote.CANCEL, ib.remote.UPDATE_START_MARKET, ib.remote.UPDATE_START_VALUE, ib.remote.UPDATE_START2_VALUE, ib.remote.UPDATE_STOP_MARKET, ib.remote.UPDATE_STOP_VALUE };

		for (String type: types) { _type_orders.put(type, get_key_from_type_order_internal(type)); }
	}

	private static String get_key_from_status_internal(String status_) { return common.get_key_from_type(status_, ib.remote.STATUS); }

	private static String get_key_from_status2_internal(String status2_) { return common.get_key_from_type(status2_, ib.remote.STATUS2); }

	private static String get_key_from_type_order_internal(String type_) { return common.get_key_from_type(type_, ib.remote.ORDERS); }
	
	private static boolean field_is_boolean(String field_) { return field_.equals(IS_MARKET); }
	
	private static boolean field_is_int(String field_) { return (field_.equals(ORDER_ID_MAIN) || field_.equals(ORDER_ID_SEC) || field_.equals(REQUEST)); }
	
	private static boolean field_is_decimal(String field_) { return (field_.equals(START) || field_.equals(START2) || field_.equals(STOP) || field_.equals(QUANTITY) || field_.equals(PERC_MONEY) || field_.equals(PRICE)); }
	
	private static Object get_vals_common(String status2_key_, boolean is_quick_) { return get_vals_common(status2_key_, null, is_quick_); }

	private static int get_new_request() 
	{	
		int min = ib.common.WRONG_REQUEST + 1;
		int request = numbers.get_random_int(min, numbers.MAX_INT);
		
		while (exists(request)) { request = (request < numbers.MAX_INT ? request + 1 : min); }

		return request;
	}

	private static boolean update(int request_, String field_, Object val_) { return common.update(SOURCE, field_, val_, get_where_request(request_)); }

	private static String get_where_request(int request_) { return common.get_where(SOURCE, REQUEST, Integer.toString(request_)); }

	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }

	private static String get_where_active() { return (new db_where(SOURCE, STATUS, db_where.OPERAND_NOT_EQUAL, get_key_from_status(ib.remote.STATUS_INACTIVE))).toString(); }

	private static String get_where_pending() { return common.join_wheres(get_where_status2(get_key_from_status2(ib.remote.STATUS2_PENDING)), get_where_status(get_key_from_status(ib.remote.STATUS_ACTIVE))); }

	private static String get_where_error() { return common.join_wheres(get_where_status2(get_key_from_status2(ib.remote.STATUS2_ERROR)), get_where_status(get_key_from_status(ib.remote.STATUS_ACTIVE))); }

	private static String get_where_status(String status_key_) { return common.get_where(SOURCE, STATUS, status_key_); }

	private static String get_where_status2(String status2_key_) { return common.get_where(SOURCE, STATUS2, status2_key_); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }
}