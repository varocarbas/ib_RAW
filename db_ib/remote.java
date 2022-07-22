package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db;
import accessory.db_order;
import accessory.db_where;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import ib.order;

public abstract class remote extends parent_static
{
	public static final String SOURCE = common.SOURCE_REMOTE;
	
	public static final String USER = common.FIELD_USER;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
	public static final String TIME = common.FIELD_TIME;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String STATUS2 = common.FIELD_STATUS2;
	public static final String START = common.FIELD_START;
	public static final String START2 = common.FIELD_START2;
	public static final String STOP = common.FIELD_STOP;
	public static final String IS_MARKET = common.FIELD_IS_MARKET;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String TYPE_PLACE = common.FIELD_TYPE_PLACE;
	public static final String INVEST_PERC = common.FIELD_INVEST_PERC;
	public static final String ERROR = common.FIELD_ERROR;
	public static final String IS_ACTIVE = common.FIELD_IS_ACTIVE;
	public static final String REQUEST = common.FIELD_REQUEST;

	public static final String STATUS_ACTIVE = ib.remote.STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = ib.remote.STATUS_INACTIVE;
	public static final String STATUS_ERROR = ib.remote.STATUS_ERROR;

	public static final String STATUS2_PENDING = ib.remote.STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = ib.remote.STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = ib.remote.STATUS2_ERROR;
	
	public static boolean is_active(int request_) { return common.exists(SOURCE, db_where.join(get_where_request(request_), get_where_status(STATUS_ACTIVE), db_where.LINK_AND)); }
	
	public static ArrayList<HashMap<String, String>> get_all() { return common.get_all_vals(SOURCE, get_main_fields(), null); }

	public static ArrayList<HashMap<String, String>> get_all_pending() { return common.get_all_vals(SOURCE, get_main_fields(), get_where_status2(STATUS2_PENDING)); }

	public static ArrayList<HashMap<String, String>> get_all_executed() { return common.get_all_vals(SOURCE, get_main_fields(), get_where_status2(STATUS2_EXECUTED)); }

	public static HashMap<String, String> get(int request_) { return common.get_vals(SOURCE, get_main_fields(), get_where_request(request_)); }

	public static String get_status(int request_) { return common.get_type(SOURCE, STATUS, ib.remote.STATUS, get_where_request(request_)); }

	public static String get_status2(int request_) { return common.get_type(SOURCE, STATUS2, ib.remote.STATUS2, get_where_request(request_)); }

	public static String get_error(int request_) { return common.get_string(SOURCE, ERROR, get_where_request(request_)); }

	public static int get_order_id(int request_) { return get_order_id(request_, true); }

	public static int get_order_id(int request_, boolean is_main_) { return common.get_int(SOURCE, (is_main_ ? ORDER_ID_MAIN : ORDER_ID_SEC), get_where_request(request_)); }

	public static int __add_new(order order_)
	{
		HashMap<String, Object> vals = orders.to_hashmap(order_, true);
		if (!arrays.is_ok(vals)) return ib.remote.WRONG_REQUEST;
		
		int request = __get_new_request();

		vals.put(REQUEST, request);
		
		common.insert(SOURCE, vals);
		
		return request;
	}
	
	public static boolean update(int request_, order order_)
	{
		HashMap<String, Object> vals = orders.to_hashmap(order_, true);
		if (!arrays.is_ok(vals)) return false;
		
		return remote.update(request_, vals);
	}
	
	public static boolean update(int request_, HashMap<String, Object> vals_) { return common.update(SOURCE, vals_, get_where_request(request_)); }
	
	public static boolean update_status(int request_, String status_) { return common.update_type(SOURCE, STATUS, status_, ib.remote.STATUS, get_where_request(request_)); }

	public static boolean update_status2(int request_, String status2_) { return common.update_type(SOURCE, STATUS2, status2_, ib.remote.STATUS2, get_where_request(request_)); }

	public static boolean update_error(int request_, String val_) 
	{
		update_status(request_, STATUS_ERROR);
		
		String val = common.adapt_string(val_, ERROR);
		
		return (strings.is_ok(val) ? update(request_, ERROR, val) : false); 
	}

	public static boolean update_order_id(int order_id_main_, HashMap<String, Object> vals_) { return common.update(SOURCE, vals_, get_where_order_id(order_id_main_)); }
	
	public static boolean deactivate_order_id(int order_id_main_) { return common.update(SOURCE, IS_ACTIVE, false, get_where_order_id(order_id_main_)); }

	public static int __get_new_request() 
	{
		__lock();
		
		int last = db.select_one_int(SOURCE, REQUEST, db.DEFAULT_WHERE, (new db_order(REQUEST, db_order.ORDER_DESC)).toString());
		
		int request = ib.remote.WRONG_REQUEST;
		int temp = ib.remote.WRONG_REQUEST;
		
		while (request == ib.remote.WRONG_REQUEST)
		{
			if (last < numbers.MAX_INT) request = last + 1;
			else
			{
				temp++;
				if (!common.exists(SOURCE, get_where_request(temp))) request = temp;
			}
		}
		
		__unlock();
		
		return request;
	}
	
	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS); }

	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.remote.STATUS); }
	
	public static String get_status2_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS2); }

	public static String get_key_from_status2(String status2_) { return common.get_key_from_type(status2_, ib.remote.STATUS2); }
	
	private static boolean update(int request_, String field_, Object val_) { return common.update(SOURCE, field_, val_, get_where_request(request_)); }
	
	private static String[] get_main_fields() { return new String[] { SYMBOL, ORDER_ID_MAIN, ORDER_ID_SEC, STATUS, STATUS2, START, START2, STOP, QUANTITY, TYPE_PLACE, INVEST_PERC, REQUEST }; }

	private static String get_where_request(int request_) { return common.get_where(SOURCE, REQUEST, Integer.toString(request_), false); }

	private static String get_where_order_id(int order_id_main_) { return common.get_where_order_id(SOURCE, order_id_main_); }

	private static String get_where_status(String status_) { return common.get_where(SOURCE, STATUS, get_key_from_status(status_), false); }

	private static String get_where_status2(String status2_) { return get_where_status2(status2_, true); }

	private static String get_where_status2(String status2_, boolean only_active_) 
	{
		String where = common.get_where(SOURCE, STATUS2, get_key_from_status2(status2_), false); 
	
		if (only_active_) where = db_where.join(where, get_where_status(STATUS_ACTIVE), db_where.LINK_AND);
		
		return where;
	}
}