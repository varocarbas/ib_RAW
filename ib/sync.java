package ib;

import java.util.ArrayList;
import java.util.HashMap;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory.dates;
import accessory_ib._alls;
import accessory_ib._defaults;
import accessory_ib.errors;
import accessory_ib.types;

public class sync extends parent_static 
{
	public static final String GET_ID = types.SYNC_GET_ID;
	public static final String GET_IDS = types.SYNC_GET_IDS;
	public static final String GET_FUNDS = types.SYNC_GET_FUNDS;

	public static final String ORDER_CANCEL = types.SYNC_ORDER_CANCEL;
	public static final String ORDER_PLACE_UPDATE = types.SYNC_ORDER_PLACE_UPDATE;
	
	public static final String OUT_DECIMAL = types.SYNC_OUT_DECIMAL;
	public static final String OUT_INT = types.SYNC_OUT_INT;
	public static final String OUT_INTS = types.SYNC_OUT_INTS;
	public static final String OUT_MISC = types.SYNC_OUT_MISC;

	public static final int WRONG_ID = common.MIN_REQ_ID_SYNC - 1;
	
	public static final String ERROR_GET = types.ERROR_IB_SYNC_GET;
	public static final String ERROR_TIME = types.ERROR_IB_SYNC_TIME;
	
	public static volatile boolean _retrieving = false;

	static int _id = common.MIN_REQ_ID_SYNC;
	
	private static final long DEFAULT_TIMEOUT = _defaults.SYNC_TIMEOUT;

	private static volatile double _out_decimal = numbers.DEFAULT_DECIMAL;
	private static volatile int _out_int = numbers.DEFAULT_INT;
	private static volatile ArrayList<Integer> _out_ints = new ArrayList<Integer>();
	private static volatile HashMap<String, String> _out_misc = new HashMap<String, String>();

	private static String _get = strings.DEFAULT;
	private static String _out = strings.DEFAULT;

	public static String get_class_id() { return accessory.types.get_id(types.ID_SYNC); }

	public static int get_next_id() { return (int)get(GET_ID); }
	
	public static double get_funds() { return (double)get(GET_FUNDS); }
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> get_open_ids() { return (ArrayList<Integer>)get(GET_IDS); }
	
	public static boolean update(String val_) { return update(strings.to_number_decimal(val_)); }

	public static boolean update(double val_)
	{
		if (!strings.are_equal(_out, OUT_DECIMAL)) return false;

		_out_decimal = val_;

		return true;
	}

	public static boolean update(int val_)
	{
		boolean is_ok = true;

		if (strings.are_equal(_out, OUT_INTS)) _out_ints.add(val_);
		else if (strings.are_equal(_out, OUT_INT)) _out_int = val_;
		else is_ok = false;

		return is_ok;
	}

	public static boolean update(String key_, String val_)
	{
		if (!strings.are_equal(_out, OUT_MISC) || !strings.is_ok(key_)) return false;

		_out_misc.put(key_, val_);

		return true;
	}

	public static String check_get(String type_) { return accessory.types.check_type(type_, types.SYNC_GET); }

	public static String check_out(String type_) { return accessory.types.check_type(type_, types.SYNC_OUT); }

	public static String check_order(String type_) { return accessory.types.check_type(type_, types.SYNC_ORDER); }
	
	public static String check_error(String type_) { return accessory.types.check_type(type_, types.ERROR_IB_SYNC); }

	public static String get_error_message(String type_)
	{
		String message = strings.DEFAULT;
		
		String type = check_error(type_);
		if (!strings.is_ok(type)) return message;
		
		if (type.equals(ERROR_GET)) message = "Wrong sync_get type";
		else if (type.equals(ERROR_TIME)) message = "Retrieval timed out";

		return message;	
	}

	public static HashMap<String, String> populate_all_get_outs()
	{		
		HashMap<String, String> all = new HashMap<String, String>();
		
		all.put(GET_ID, OUT_INT);
		all.put(GET_FUNDS, OUT_DECIMAL);
		all.put(GET_IDS, OUT_INTS);

		return all;
	}
		
	public static boolean is_ok(int id_) { return (_retrieving && (_id == id_)); }
	
	static boolean cancel_order(int id_) { return execute_order(ORDER_CANCEL, id_, null, null); }

	static boolean place_update_order(int id_, Contract contract_, Order order_) { return execute_order(ORDER_PLACE_UPDATE, id_, contract_, order_); }

	private static HashMap<String, String> get_all_get_outs() { return _alls.SYNC_GET_OUTS; }

	private static boolean execute_order(String type_, int id_, Contract contract_, Order order_)
	{
		if (!common.req_id_is_ok_sync(id_)) return false;

		if (type_.equals(ORDER_CANCEL)) 
		{
			conn._client.cancelOrder(id_);
			
			misc.pause_secs(1);
		}
		else 
		{
			if (contract_ == null || order_ == null) return false;
			
			conn._client.placeOrder(id_, contract_, order_);
		}
		
		return true;
	}
	
	private static Object get(String type_)
	{
		common.get_req_id(true);
		if (!get_ini(type_)) return null;
		
		retrieve(); 

		return get_out();
	}

	private static boolean get_ini(String type_) { return (boolean)get_ini_out(type_, true); }

	private static Object get_out() { return get_ini_out(null, false); }

	private static Object get_ini_out(String type_, boolean is_ini_)
	{
		Object output = null;
		if (is_ini_ && !ini_is_ok(type_)) return false;

		if (_out.equals(OUT_INT)) 
		{
			if (is_ini_) _out_int = numbers.DEFAULT_INT;
			else output = _out_int;
		}
		else if (_out.equals(OUT_DECIMAL)) 
		{
			if (is_ini_) _out_decimal = numbers.DEFAULT_DECIMAL;
			else output = _out_decimal;
		}
		else if (_out.equals(OUT_INTS)) 
		{
			if (is_ini_) _out_ints = new ArrayList<Integer>();
			else output = arrays.get_new(_out_ints);
		}
		else if (_out.equals(OUT_MISC)) 
		{
			if (is_ini_) _out_misc = new HashMap<String, String>();
			else output = arrays.get_new(_out_misc);
		}

		return (is_ini_ ? true : output);
	}

	private static boolean ini_is_ok(String get_)
	{
		String get = check_get(get_);
		
		String error = null;
		if (!strings.is_ok(get)) error = ERROR_GET;

		if (error != null)
		{
			accessory_ib.errors.manage(error);

			return false;
		}

		_get = get;
		_out = get_all_get_outs().get(_get);		
		_id = common.get_req_id(true);

		return true;
	}

	private static boolean retrieve()
	{
		long timeout = DEFAULT_TIMEOUT;
		boolean cannot_fail = true;

		_retrieving = true;

		if (_get.equals(GET_FUNDS))
		{	
			//accountSummary, accountSummaryEnd
			conn._client.reqAccountSummary(_id, "All", "AvailableFunds"); 
		}
		else if (_get.equals(GET_IDS))
		{	
			timeout = 3;
			cannot_fail = false;
			
			//openOrder, openOrderEnd, orderStatus
			conn._client.reqAllOpenOrders(); 
		}
		else if (_get.equals(GET_ID))
		{	
			//nextValidId
			conn._client.reqIds(-1); 
		}
		else return false;

		return wait(timeout, cannot_fail);
	}

	private static boolean wait(long timeout_, boolean cannot_fail_)
	{
		boolean is_ok = true;

		long start = dates.get_elapsed(0);

		while (_retrieving)
		{
			if (dates.get_elapsed(start) >= timeout_) 
			{
				if (cannot_fail_) errors.manage(ERROR_TIME);
				is_ok = false;

				break;
			}

			misc.pause_loop();
		}

		_retrieving = false;
		
		return is_ok;
	}
}