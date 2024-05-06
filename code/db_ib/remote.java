package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_common;
import accessory.db_where;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory._types;
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
	
	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	
	
	public static ArrayList<HashMap<String, String>> get_all_pending() { return db_common.get_all_vals(SOURCE, db_common.get_fields(SOURCE), get_where_pending()); }
	
	public static ArrayList<HashMap<String, String>> get_all_errors() { return db_common.get_all_vals(SOURCE, db_common.get_fields(SOURCE), get_where_error()); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || contains_active_requests(false)); }
	
	public static boolean contains_active_requests() { return contains_active_requests(false); }
	
	public static boolean contains_active_requests(boolean only_active_status_) { return db_common.exists(SOURCE, get_where_active(only_active_status_)); }

	public static boolean exists(int request_) { return db_common.exists(SOURCE, get_where_request(request_)); }

	public static boolean is_active(int request_) { return db_common.exists(SOURCE, db_common.join_wheres(get_where_request(request_), get_where_active())); }

	public static boolean is_active(String symbol_) { return db_common.exists(SOURCE, db_common.join_wheres(common.get_where_symbol(SOURCE, symbol_), get_where_active())); }

	public static boolean is_pending(int request_) { return db_common.exists(SOURCE, get_where_pending()); }

	public static boolean delete(int request_) { return db_common.delete(SOURCE, get_where_request(request_)); }

	public static HashMap<String, String> get_vals(int request_) { return get_vals(request_, null); }
	
	public static HashMap<String, String> get_vals(int request_, String[] fields_) 
	{ 
		String source = SOURCE;
		String where = get_where_request(request_);
		
		return db_common.get_vals(source, (arrays.is_ok(fields_) ? fields_ : db_common.get_fields(SOURCE)), where); 
	}

	public static HashMap<String, String> get_vals_order_id(int order_id_main_) 
	{ 
		String source = SOURCE;
		String where = get_where_order_id(order_id_main_);
		
		return db_common.get_vals(source, db_common.get_fields(SOURCE), where); 
	}

	public static String get_status(int request_) { return get_status_type(common.get_string(SOURCE, STATUS, get_where_request(request_))); }

	public static String get_status2(int request_) { return get_status2_type(common.get_string(SOURCE, STATUS2, get_where_request(request_))); }

	public static String get_type_order(int request_) { return get_order_type(common.get_string(SOURCE, TYPE_ORDER, get_where_request(request_))); }

	public static String get_error(int request_) { return common.get_string(SOURCE, ERROR, get_where_request(request_)); }

	public static int get_order_id(int request_) { return get_order_id(request_, true); }

	public static int get_order_id(int request_, boolean is_main_) { return db_common.get_int(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), get_where_request(request_), ib.common.WRONG_ORDER_ID); }

	public static int get_any_request(String symbol_) { return db_common.get_int(SOURCE, REQUEST, db_common.join_wheres(common.get_where_symbol(SOURCE, symbol_), get_where_active()), ib.common.WRONG_REQUEST); }

	public static int get_request(int order_id_main_) { return db_common.get_int(SOURCE, REQUEST, get_where_order_id(order_id_main_), ib.common.WRONG_REQUEST); }

	public static String get_symbol(int request_) { return common.get_string(SOURCE, SYMBOL, get_where_request(request_)); }

	public static String get_symbol_order_id(int order_id_main_) { return common.get_string(SOURCE, SYMBOL, get_where_order_id(order_id_main_)); }
	
	public static int __request_start(_order order_, double perc_money_, double price_)
	{
		Object vals = to_hashmap(order_);	
		vals = get_vals_common(get_status2_key_request(), vals);
		
		if (perc_money_ > ib.common.WRONG_MONEY) vals = db_common.add_to_vals(SOURCE, PERC_MONEY, perc_money_, vals);
		if (price_ > ib.common.WRONG_PRICE) vals = db_common.add_to_vals(SOURCE, PRICE, price_, vals);

		return __insert_new(vals);
	}

	public static int __insert_new(Object vals_)
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
				if (common.insert(SOURCE, db_common.add_to_vals(SOURCE, REQUEST, request, vals_))) break;	
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
		Object vals = get_vals_common(get_status2_key_request());
		
		vals = db_common.add_to_vals(SOURCE, TYPE_ORDER, store_order_type(type_), vals);
		
		return update(request_, vals);
	}

	public static boolean request_update_type_order_values(int request_, String type_, String field_, double val_)
	{
		Object vals = get_vals_common(get_status2_key_request());

		vals = db_common.add_to_vals(SOURCE, TYPE_ORDER, store_order_type(type_), vals);

		if (ib.remote.is_update_market(type_)) vals = db_common.add_to_vals(SOURCE, IS_MARKET, true, vals);
		else vals = db_common.add_to_vals(SOURCE, field_, common.adapt_price(val_), vals);
		
		return update(request_, vals);	
	}
	
	public static boolean update(int request_, Object vals_) { return db_common.update(SOURCE, vals_, get_where_request(request_)); }
	
	public static boolean update_status(int request_, String status_key_) { return update(request_, STATUS, status_key_); }

	public static boolean update_status2(int request_, String status2_key_) { return update(request_, STATUS2, status2_key_); }

	public static boolean update_error(int request_, String error_, String type_order_, boolean deactivate_) 
	{
		String error = common.adapt_string(error_, ERROR);
		if (!strings.is_ok(error)) error = "ERROR";
		
		Object vals = get_vals_common(store_status2_type(ib.remote.STATUS2_ERROR));
		
		vals = db_common.add_to_vals(SOURCE, ERROR, error, vals);
		if (deactivate_) vals = db_common.add_to_vals(SOURCE, STATUS, store_status_type(ib.remote.STATUS_INACTIVE), vals);
		
		String type_order = store_order_type(type_order_);
		if (!strings.is_ok(type_order)) type_order = type_order_;
		
		vals = db_common.add_to_vals(SOURCE, TYPE_ORDER, type_order, vals);
		
		return update(request_, vals);
	}

	public static boolean update_order_id(int order_id_main_, Object vals_) { return db_common.update(SOURCE, vals_, get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate_order_id(int order_id_main_) { return db_common.update(SOURCE, STATUS, store_status_type(ib.remote.STATUS_INACTIVE), get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate(int request_) { return update_status(request_, store_status_type(ib.remote.STATUS_INACTIVE)); }
		
	public static String store_status_type(String status_type_) { return ib.remote.get_status_key(status_type_); }

	public static String get_status_type(String status_key_) { return ib.remote.get_status_type(status_key_); }
	
	public static String store_status2_type(String status2_type_) { return ib.remote.get_status2_key(status2_type_); }

	public static String get_status2_type(String status2_key_) { return ib.remote.get_status2_type(status2_key_); }
	
	public static String store_order_type(String order_type_) { return ib.remote.get_order_key(order_type_); }

	public static String get_order_type(String order_key_) { return ib.remote.get_order_type(order_key_); }

	public static double get_start(int request_) { return db_common.get_decimal(SOURCE, START, get_where_request(request_), ib.common.WRONG_PRICE); }
	
	public static double get_stop(int request_) { return db_common.get_decimal(SOURCE, STOP, get_where_request(request_), ib.common.WRONG_PRICE); }
	
	public static Object get_val(String field_, HashMap<String, String> vals_)
	{
		Object val = null;
		if (!db_common.vals_are_ok(SOURCE, vals_)) return val;
		
		String field_col = db_common.get_field_quick_col(SOURCE, field_);
		
		String val0 = vals_.get(field_col);
		
		if (field_is_boolean(field_)) val = strings.to_boolean(val0);
		else if (field_is_int(field_)) val = Integer.parseInt(val0);
		else if (field_is_decimal(field_)) val = Double.parseDouble(val0);
		else 
		{
			if (field_.equals(STATUS)) val = get_status_type(val0);
			else if (field_.equals(STATUS2)) val = get_status2_type(val0);
			else if (field_.equals(TYPE_ORDER)) val = get_order_type(val0);
			else val = val0;
		}
		
		return val;
	}

	public static Object get_vals_common(String status2_key_, Object vals_) 
	{ 
		Object vals = db_common.add_to_vals(SOURCE, TIME2, ib.common.get_current_time2(), vals_);
		
		vals = db_common.add_to_vals(SOURCE, STATUS2, status2_key_, vals);
		
		return vals; 
	}	
	
	public static String get_status2_key_request() { return store_status2_type(ib.remote.get_status2_request()); }

	public static String get_status2_key_execute(boolean is_ok_) { return store_status2_type(ib.remote.get_status2_execute(is_ok_)); }

	public static void update_status2_execute(int request_, boolean is_ok_) { update_status2(request_, get_status2_key_execute(is_ok_)); }

	public static Object to_hashmap(_order order_)
	{
		if (order_ == null) return null;
		
		Object vals = orders.to_hashmap(order_, true);
		
		String field_col = db_common.get_field_quick_col(orders.SOURCE, orders.TYPE_PLACE);
		
		String type_order = (ib.orders.PLACE + _types.SEPARATOR + arrays.get_value(vals, field_col));
		vals = db_common.add_to_vals(SOURCE, TYPE_ORDER, store_order_type(type_order), vals);
		
		vals = arrays.remove_key(vals, field_col);
		
		return vals;
	}

	public static int get_new_request() 
	{	
		int min = ib.common.WRONG_REQUEST + 1;
		int request = numbers.get_random_int(min, numbers.MAX_INT);
		
		while (exists(request)) { request = (request < numbers.MAX_INT ? request + 1 : min); }

		return request;
	}
	
	private static boolean field_is_boolean(String field_) { return field_.equals(IS_MARKET); }
	
	private static boolean field_is_int(String field_) { return (field_.equals(ORDER_ID_MAIN) || field_.equals(ORDER_ID_SEC) || field_.equals(REQUEST)); }
	
	private static boolean field_is_decimal(String field_) { return (field_.equals(START) || field_.equals(START2) || field_.equals(STOP) || field_.equals(QUANTITY) || field_.equals(PERC_MONEY) || field_.equals(PRICE)); }
	
	private static Object get_vals_common(String status2_key_) { return get_vals_common(status2_key_, null); }

	private static boolean update(int request_, String field_, Object val_) { return db_common.update(SOURCE, field_, val_, get_where_request(request_)); }

	private static String get_where_request(int request_) { return common.get_where(SOURCE, REQUEST, Integer.toString(request_)); }

	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }

	private static String get_where_active() { return get_where_active(false); }

	private static String get_where_active(boolean only_active_status_) 
	{
		String operand = db_where.OPERAND_NOT_EQUAL;
		String status = ib.remote.STATUS_INACTIVE;
		
		if (only_active_status_)
		{
			operand = db_where.OPERAND_EQUAL;
			status = ib.remote.STATUS_ACTIVE;		
		}
		
		return (new db_where(SOURCE, STATUS, operand, store_status_type(status))).toString(); 
	}

	private static String get_where_pending() { return db_common.join_wheres(get_where_status2(store_status2_type(ib.remote.STATUS2_PENDING)), get_where_status(store_status_type(ib.remote.STATUS_ACTIVE))); }

	private static String get_where_error() { return db_common.join_wheres(get_where_status2(store_status2_type(ib.remote.STATUS2_ERROR)), get_where_status(store_status_type(ib.remote.STATUS_ACTIVE))); }

	private static String get_where_status(String status_key_) { return common.get_where(SOURCE, STATUS, status_key_); }

	private static String get_where_status2(String status2_key_) { return common.get_where(SOURCE, STATUS2, status2_key_); }
}