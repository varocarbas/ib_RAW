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
	public static final String GET_ORDERS = types.SYNC_GET_ORDERS;
	public static final String GET_FUNDS = types.SYNC_GET_FUNDS;
	
	public static final String OUT_DECIMAL = types.SYNC_OUT_DECIMAL;
	public static final String OUT_INT = types.SYNC_OUT_INT;
	public static final String OUT_INTS = types.SYNC_OUT_INTS;
	public static final String OUT_STRINGS = types.SYNC_OUT_STRINGS;
	public static final String OUT_ORDERS = types.SYNC_OUT_ORDERS;

	public static final int WRONG_ID = common.MIN_REQ_ID_SYNC - 1;

	public static final long DEFAULT_TIMEOUT = _defaults.SYNC_TIMEOUT;

	public static final String ERROR_GET = types.ERROR_IB_SYNC_GET;
	public static final String ERROR_TIME = types.ERROR_IB_SYNC_TIME;

	static int _id = common.MIN_REQ_ID_SYNC;

	private static volatile boolean _getting = false;
	private static volatile double _out_decimal = numbers.DEFAULT_DECIMAL;
	private static volatile int _out_int = numbers.DEFAULT_INT;
	private static volatile ArrayList<Integer> _out_ints = new ArrayList<Integer>();
	private static volatile ArrayList<String> _out_strings = new ArrayList<String>();

	private static String _get = strings.DEFAULT;
	private static String _out = strings.DEFAULT;

	public static String get_class_id() { return accessory.types.get_id(types.ID_SYNC); }

	public static int get_next_id() { return (int)get(GET_ID); }
	
	public static double get_funds() { return (double)get(GET_FUNDS); }
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, String> get_orders() 
	{ 	
		HashMap<Integer, String> orders = (HashMap<Integer, String>)get(GET_ORDERS); 
			
		sync_orders.sync_global(orders);
		
		return orders; 
	}
	
	public static boolean order_is_submitted(int id_) { return order_is_common(id_, sync_orders.STATUS_SUBMITTED); }
	
	public static boolean order_is_filled(int id_) { return order_is_common(id_, sync_orders.STATUS_FILLED); }

	public static boolean order_is_inactive(int id_) { return order_is_common(id_, sync_orders.STATUS_INACTIVE); }
	
	public static boolean update(String val_) 
	{ 
		boolean is_ok = true;

		if (strings.are_equal(_out, OUT_ORDERS) || strings.are_equal(_out, OUT_STRINGS)) _out_strings.add(val_);
		else if (strings.is_number(val_)) is_ok = update(strings.to_number_decimal(val_));
		else is_ok = false;
		
		return is_ok;
	}
	
	public static boolean update(double val_)
	{
		if (!strings.are_equal(_out, OUT_DECIMAL)) return false;

		_out_decimal = val_;

		return true;
	}

	public static boolean update(int val_)
	{
		boolean is_ok = true;

		if (strings.are_equal(_out, OUT_ORDERS) || strings.are_equal(_out, OUT_INTS)) _out_ints.add(val_);
		else if (strings.are_equal(_out, OUT_INT)) _out_int = val_;
		else is_ok = false;

		return is_ok;
	}

	public static String check_get(String type_) { return accessory.types.check_type(type_, types.SYNC_GET); }

	public static String check_out(String type_) { return accessory.types.check_type(type_, types.SYNC_OUT); }
	
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
		all.put(GET_ORDERS, OUT_ORDERS);

		return all;
	}
	
	public static boolean is_ok() { return (_getting); }

	public static boolean is_ok(int id_) { return (_getting && (_id == id_)); }
	
	public static void end() { if (_getting) _getting = false; }
	
	static boolean cancel_order(int id_) { return execute_order(sync_orders.CANCEL, id_, null, null); }

	static boolean place_order(int id_, Contract contract_, Order order_) { return execute_order(sync_orders.PLACE, id_, contract_, order_); }

	static boolean update_order(int id_, Contract contract_, Order order_) { return execute_order(sync_orders.UPDATE, id_, contract_, order_); }
	
	private static boolean order_is_common(int id_, String target_)
	{
		String status = (String)arrays.get_value(get_orders(), id_);
		if (!strings.is_ok(status)) return strings.are_equal(target_, sync_orders.STATUS_INACTIVE);
		
		return sync_orders.is_status(status, target_);
	}
	
	private static HashMap<String, String> get_all_get_outs() { return _alls.SYNC_GET_OUTS; }

	private static boolean execute_order(String type_, int id_, Contract contract_, Order order_)
	{
		if (!common.req_id_is_ok_sync(id_)) return false;
	
		_id = id_;
		
		boolean is_cancel = sync_orders.is_cancel(type_);
		
		if (is_cancel) conn._client.cancelOrder(id_);
		else 
		{
			if (contract_ == null || order_ == null) return false;
			
			conn._client.placeOrder(id_, contract_, order_);
		}
		
		return ((is_cancel || sync_orders.is_place(type_)) ? wait_orders(type_) : true); 
	}
	
	private static Object get(String type_)
	{
		common.get_req_id(true);
		if (!get_ini(type_)) return null;
		
		get(); 

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
		else if (_out.equals(OUT_ORDERS)) 
		{
			if (is_ini_) 
			{
				_out_ints = new ArrayList<Integer>();
				_out_strings = new ArrayList<String>();
			}
			else 
			{
				//Java's peculiar behavior when dealing with HashMaps or similar collections in
				//multithreading scenarios is the main reason explaining this unusual setup.
				//!!!
				HashMap<Integer, String> orders = new HashMap<Integer, String>();

				for (int i = 0; i < _out_ints.size(); i++)
				{
					int id = _out_ints.get(i);
					String status = _out_strings.get(i);
					
					orders.put(id, status);
				}

				output = orders;
			}
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

	private static boolean get()
	{
		long timeout = DEFAULT_TIMEOUT;
		boolean cannot_fail = true;

		_getting = true;

		if (_get.equals(GET_FUNDS))
		{	
			//accountSummary, accountSummaryEnd
			conn._client.reqAccountSummary(_id, "All", "AvailableFunds"); 
		}
		else if (_get.equals(GET_ORDERS))
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

		return wait_get(timeout, cannot_fail);
	}

	private static boolean wait_get(long timeout_, boolean cannot_fail_) { return wait(timeout_, cannot_fail_, null); }	

	private static boolean wait_orders(String type_) { return wait(DEFAULT_TIMEOUT, true, type_); }
	
	private static boolean wait(long timeout_, boolean cannot_fail_, String type_)
	{
		boolean is_ok = true;
		
		boolean is_get = false;
		boolean is_place = false;
		boolean is_cancel = false;
		
		if (sync_orders.is_place(type_)) is_place = true;
		else if (sync_orders.is_cancel(type_)) is_cancel = true;
		else if (!strings.is_ok(type_)) is_get = true;
		else return false;

		long start = dates.start_elapsed();
		
		while (true)
		{
			if (is_get)
			{
				if (!_getting) 
				{
					if (!(_get.equals(GET_ORDERS) && (_out_ints.size() != _out_strings.size()))) break;
				}
			}
			else if (is_place || is_cancel)
			{
				boolean is_inactive = order_is_inactive(_id);
				
				if ((is_inactive && is_cancel) || (!is_inactive && is_place)) break;			
			}
			
			if (dates.get_elapsed(start) >= timeout_) 
			{
				if (cannot_fail_) errors.manage(ERROR_TIME);
				is_ok = false;

				break;
			}

			misc.pause_loop();
		}

		if (is_get) _getting = false;
		
		return is_ok;
	}
}