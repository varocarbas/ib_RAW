package ib;

import java.util.ArrayList;
import java.util.HashMap;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory.arrays;
import accessory.dates;
import accessory_ib._alls;
import accessory_ib.errors;
import accessory_ib.types;
import external_ib.calls;

public abstract class sync extends parent_static 
{
	public static final String GET_ORDER_ID = types.SYNC_GET_ORDER_ID;
	public static final String GET_ORDERS = types.SYNC_GET_ORDERS;
	public static final String GET_FUNDS = types.SYNC_GET_FUNDS;
	
	public static final String ORDER_PLACE = orders.PLACE;
	public static final String ORDER_UPDATE = orders.UPDATE;
	public static final String ORDER_CANCEL = orders.CANCEL;

	public static final String ORDER_STATUS_SUBMITTED = orders.STATUS_SUBMITTED;
	public static final String ORDER_STATUS_FILLED = orders.STATUS_FILLED;
	public static final String ORDER_STATUS_INACTIVE = orders.STATUS_INACTIVE;
	public static final String ORDER_STATUS_IN_PROGRESS = orders.STATUS_IN_PROGRESS;

	public static final String OUT_INT = types.SYNC_OUT_INT;
	public static final String OUT_ORDERS = types.SYNC_OUT_ORDERS;
	public static final String OUT_FUNDS = types.SYNC_OUT_FUNDS;
	
	public static final long TIMEOUT_GET_ORDERS = 3l;
	public static final long TIMEOUT_EXECUTE_ORDER = 3l;
	
	public static final String ERROR_TIMEOUT = types.ERROR_IB_SYNC_TIMEOUT;
	
	public static final int WRONG_REQ_ID = common.WRONG_ID;
	public static final int WRONG_ORDER_ID = common.WRONG_ORDER_ID;
	public static final double WRONG_MONEY = common.WRONG_MONEY;

	public static final long DEFAULT_TIMEOUT = 5l;
	
	private static volatile boolean _getting = false;
	private static volatile int _out_int = numbers.DEFAULT_INT;
	private static volatile boolean _error_triggered = false;
	private static volatile ArrayList<Integer> _out_ints = new ArrayList<Integer>();
	private static volatile ArrayList<String> _out_strings = new ArrayList<String>();
	private static volatile ArrayList<Double> _out_decimals = new ArrayList<Double>();
	
	private static int _req_id = WRONG_REQ_ID;
	private static int _order_id = WRONG_ORDER_ID;
	private static String _get = strings.DEFAULT;

	public static int __get_order_id() 
	{ 
		__lock();
		
		Object temp = get(GET_ORDER_ID);
		
		int output = (temp == null ? WRONG_ORDER_ID : (int)temp);
		
		__unlock();
		
		return output;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Double> __get_funds() 
	{
		__lock();

		HashMap<String, Double> funds = (HashMap<String, Double>)get(GET_FUNDS);
		if (funds == null) funds = new HashMap<String, Double>();

		__unlock();
		
		return funds;
	}
	
	public static HashMap<Integer, String> __get_orders() { return _get_orders(true); } 

	public static int get_req_id() { return _req_id; }

	public static void update_req_id(int id_) { _req_id = id_; }
	
	public static boolean is_get(String type_) { return strings.is_ok(check_get(type_)); }
	
	public static String check_get(String type_) { return accessory.types.check_type(type_, types.SYNC_GET); }
	
	public static String check_out(String type_) { return accessory.types.check_type(type_, types.SYNC_OUT); }
	
	public static String check_error(String type_) { return accessory.types.check_type(type_, types.ERROR_IB_SYNC); }

	public static String get_error_message(String type_)
	{
		String message = strings.DEFAULT;
		
		String type = check_error(type_);
		if (!strings.is_ok(type)) return message;
		
		if (type.equals(ERROR_TIMEOUT)) 
		{
			message = "";
			
			if (_getting) 
			{
				message = "get method"; 
				if (strings.is_ok(_get)) message += " (" + _get + ")";
			}
			else if (_order_id > WRONG_ORDER_ID) message = "order id (" + _order_id + ")";
			else message = "request";
			
			message += " timed out";
		}

		return message;
	}

	public static HashMap<String, String> populate_all_get_outs()
	{		
		HashMap<String, String> all = new HashMap<String, String>();
		
		all.put(GET_ORDER_ID, OUT_INT);
		all.put(GET_FUNDS, OUT_FUNDS);
		all.put(GET_ORDERS, OUT_ORDERS);
		
		return all;
	}
	
	public static boolean is_ok() { return (_getting || _order_id > WRONG_ORDER_ID); }
	
	public static void end_get() { _getting = false; }

	public static boolean wait_orders(int order_id_main_, String type_) 
	{ 
		_order_id = order_id_main_;
		
		return wait(TIMEOUT_EXECUTE_ORDER, false, type_); 
	}
	
	static boolean next_valid_id(int id_) 
	{
		if (!is_ok()) return false;
		
		update_int(id_);
		
		return true;
	}
	
	static boolean is_ok(int id_) { return (_getting && (_req_id == id_)); }
		
	static boolean cancel_order(int id_) { return execute_order(ORDER_CANCEL, id_, null, null); }

	static boolean place_order(int id_, Contract contract_, Order order_) { return execute_order(ORDER_PLACE, id_, contract_, order_); }

	static boolean update_order(int id_, Contract contract_, Order order_) { return execute_order(ORDER_UPDATE, id_, contract_, order_); }

	static void update_int(int val_)
	{
		_out_int = val_;

		end_get();		
	}
	
	static void update_orders(int order_id_, String status_ib_) 
	{	
		_out_ints.add(order_id_);
		_out_strings.add(status_ib_);
	}
	
	static void update_funds(String key_, String value_) 
	{			
		_out_strings.add(key_);
		_out_decimals.add(Double.parseDouble(value_));
	}
	
	static void update_error_triggered(boolean triggered_) { _error_triggered = triggered_; }
	
	static void account_download_end(String account_ib_) 
	{
		if (!basic.account_ib_is_ok(account_ib_)) return;
			
		end_get(); 
	}

	static HashMap<String, Double> get_funds(ArrayList<String> keys_, ArrayList<Double> vals_)
	{
		HashMap<String, Double> funds = new HashMap<String, Double>();
		if (keys_.size() != vals_.size()) return funds;

		for (int i = 0; i < keys_.size(); i++)
		{
			ArrayList<String> fields = calls.get_funds_fields(keys_.get(i));
			if (!arrays.is_ok(fields)) continue;
			
			double val = vals_.get(i);
			
			for (String field: fields)
			{
				if (funds.containsKey(field)) val += funds.get(field);
				
				funds.put(field, val);				
			}
		}

		return funds;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<Integer, String> _get_orders(boolean lock_) 
	{
		if (lock_) __lock();
		
		HashMap<Integer, String> orders = (HashMap<Integer, String>)get(GET_ORDERS); 
		if (orders == null) orders = new HashMap<Integer, String>();
		
		if (lock_) __unlock();
		
		return orders;
	}

	private static boolean order_is_submitted(int order_id_) { return sync_orders.is_submitted(order_id_, _get_orders(false)); }
	
	private static boolean order_is_inactive(int order_id_) { return sync_orders.is_inactive(order_id_, _get_orders(false)); }
	
	private static HashMap<String, String> get_all_get_outs() { return _alls.SYNC_GET_OUTS; }
	
	private static boolean execute_order(String type_, int id_, Contract contract_, Order order_)
	{	
		_order_id = id_;
	
		if (orders.is_cancel(type_)) calls.cancelOrder(_order_id);
		else 
		{
			if (contract_ == null || order_ == null) return false;

			calls.placeOrder(_order_id, contract_, order_);
		}

		return true;
	}
	
	private static Object get(String get_)
	{
		common_xsync.get_req_id(true);
		
		get_ini(get_);
		
		return (get_call(get_) ? get_out(get_) : null);
	}

	private static void get_ini(String get_) { get_ini_out(get_, true); }

	private static Object get_out(String get_) { return get_ini_out(get_, false); }

	private static Object get_ini_out(String get_, boolean is_ini_)
	{
		Object output = null;
		
		if (is_ini_) _req_id = common_xsync.get_req_id(true);
		
		String out = get_out_type(get_);

		if (out.equals(OUT_INT)) 
		{
			if (is_ini_) _out_int = numbers.DEFAULT_INT;
			else output = _out_int;
		}
		else output = get_ini_out_hashmaps(out, is_ini_);
		
		return output;
	}

	//Java's peculiar behavior when dealing with HashMaps or similar collections in
	//multithreading scenarios is the main reason explaining the unusual setups below.
	//!!!
	private static Object get_ini_out_hashmaps(String out_, boolean is_ini_)
	{
		Object output = null;
		
		if (out_.equals(OUT_ORDERS)) 
		{
			if (is_ini_) 
			{
				_out_ints = new ArrayList<Integer>();
				_out_strings = new ArrayList<String>();
			}
			else 
			{
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
		else if (out_.equals(OUT_FUNDS)) 
		{
			if (is_ini_) 
			{
				_out_strings = new ArrayList<String>();
				_out_decimals = new ArrayList<Double>();
			}
			else output = get_funds(_out_strings, _out_decimals); 
		}
		
		return output;
	}
	
	private static String get_out_type(String get_) { return get_all_get_outs().get(get_); }
	
	private static boolean get_call(String get_)
	{
		_getting = true;
		_get = get_;
		
		long timeout = DEFAULT_TIMEOUT;
		boolean cannot_fail = true;

		if (get_.equals(GET_FUNDS))
		{	
			cannot_fail = false;
			
			//Methods called in external_ib.wrapper: accountSummary, accountSummaryEnd.		
			calls.reqAccountSummary(_req_id); 
		}
		else if (get_.equals(GET_ORDERS))
		{	
			timeout = TIMEOUT_GET_ORDERS;
			cannot_fail = false;

			//Methods called in external_ib.wrapper: openOrder, openOrderEnd, orderStatus.
			calls.reqAllOpenOrders(); 
		}
		else if (get_.equals(GET_ORDER_ID))
		{	
			//Methods called in external_ib.wrapper: nextValidId.
			calls.reqIds(); 
		}
		else return false;

		boolean is_ok = wait_get(timeout, cannot_fail, get_);

		_getting = false;
		_get = strings.DEFAULT;
		
		return is_ok;
	}

	private static boolean wait_get(long timeout_, boolean cannot_fail_, String get_) { return wait(timeout_, cannot_fail_, get_); }	
	
	private static boolean wait(long timeout_, boolean cannot_fail_, String type_)
	{	
		_error_triggered = false;

		boolean is_ok = true;	
		
		boolean is_place = false;
		boolean is_cancel = false;
		boolean is_get = false;
		
		if (is_get(type_)) is_get = true;
		else if (orders.is_place(type_)) is_place = true;
		else if (orders.is_cancel(type_)) is_cancel = true;			
		else return false;
		
		long start = dates.start_elapsed();

		while (true)
		{
			if (_error_triggered) 
			{
				_error_triggered = false;
				is_ok = false;
				
				break;
			}
			
			if (is_get && wait_exit_get(type_)) break;
			else if (is_place || is_cancel)
			{
				if ((is_place && order_is_submitted(_order_id)) || (is_cancel && order_is_inactive(_order_id))) break;
			}
			
			if (dates.get_elapsed(start) >= timeout_) 
			{
				if (cannot_fail_) errors.manage(ERROR_TIMEOUT);
				
				is_ok = false;	

				break;
			}

			misc.pause_loop();
		}
		
		return is_ok;
	}
	
	private static boolean wait_exit_get(String type_)
	{
		if (_getting) return false;
		
		boolean exit = true;
		
		if (type_.equals(GET_ORDERS))
		{
			if (_out_ints.size() != _out_strings.size()) exit = false;
		}
		else if (type_.equals(GET_FUNDS))
		{
			if (_out_strings.size() != _out_decimals.size()) exit = false;
		}
		
		return exit;
	}
}