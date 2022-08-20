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
import accessory_ib.errors;
import accessory_ib.types;
import external_ib.calls;

public abstract class sync extends parent_static 
{
	public static final String GET_ORDER_ID = types.SYNC_GET_ORDER_ID;
	public static final String GET_ORDERS = types.SYNC_GET_ORDERS;
	public static final String GET_FUNDS = types.SYNC_GET_FUNDS;
	public static final String GET_ERROR = types.SYNC_GET_ERROR;
	
	public static final String ORDER_PLACE = sync_orders.PLACE;
	public static final String ORDER_UPDATE = sync_orders.UPDATE;
	public static final String ORDER_CANCEL = sync_orders.CANCEL;
	
	public static final String OUT_DECIMAL = types.SYNC_OUT_DECIMAL;
	public static final String OUT_INT = types.SYNC_OUT_INT;
	public static final String OUT_INTS = types.SYNC_OUT_INTS;
	public static final String OUT_STRINGS = types.SYNC_OUT_STRINGS;
	public static final String OUT_ORDERS = types.SYNC_OUT_ORDERS;
	public static final String OUT_FUNDS = types.SYNC_OUT_FUNDS;
		
	public static final long TIMEOUT_GET_ORDERS = 3l;
	public static final long TIMEOUT_GET_ERROR = 3l;

	public static final String ERROR_TIMEOUT = types.ERROR_IB_SYNC_TIMEOUT;
	
	public static final int WRONG_REQ_ID = common.WRONG_ID;
	public static final int WRONG_ORDER_ID = common.WRONG_ORDER_ID;
	public static final double WRONG_MONEY = common.WRONG_MONEY;

	public static final long DEFAULT_TIMEOUT = 10l;
	
	private static volatile boolean _getting = false;
	private static volatile double _out_decimal = numbers.DEFAULT_DECIMAL;
	private static volatile int _out_int = numbers.DEFAULT_INT;
	private static volatile boolean _error_triggered = false;
	private static volatile ArrayList<Integer> _out_ints = new ArrayList<Integer>();
	private static volatile ArrayList<String> _out_strings = new ArrayList<String>();
	private static volatile ArrayList<Double> _out_decimals = new ArrayList<Double>();

	private static int _req_id = WRONG_REQ_ID;
	private static int _order_id = WRONG_ORDER_ID;
	private static String _get = strings.DEFAULT;
	private static String _out = strings.DEFAULT;

	public static int get_order_id() 
	{ 
		Object temp = get(GET_ORDER_ID);
		
		int output = (temp == null ? WRONG_ORDER_ID : (int)temp);
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Double> get_funds() 
	{
		HashMap<String, Double> funds = (HashMap<String, Double>)get(GET_FUNDS);
		if (funds == null) funds = new HashMap<String, Double>();
	
		return funds;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, String> get_orders() 
	{ 	
		HashMap<Integer, String> orders = (HashMap<Integer, String>)get(GET_ORDERS); 
		if (orders == null) orders = new HashMap<Integer, String>();
		
		return orders; 
	}

	public static int get_req_id() { return _req_id; }

	public static void update_req_id(int id_) { _req_id = id_; }
	
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
			
			if (_getting) message = (strings.is_ok(_get) ? _get : "get method");
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
	
	public static void end_get() { if (_getting) _getting = false; }

	public static boolean wait_orders(String type_) { return wait(DEFAULT_TIMEOUT, true, type_, false); }
	
	static boolean next_valid_id(int id_) 
	{
		if (!is_ok()) return false;
		
		update(id_);

		end_get();
		
		return true;
	}
	
	static boolean is_ok(int id_) { return (_getting && (_req_id == id_)); }

	static boolean is_ok(String get_) { return (_getting && (_get.equals(get_))); }
	
	static boolean is_ok(int id_, String key_) 
	{
		if (!is_ok(id_)) return false;
	
		boolean is_ok = true;
		
		if (is_ok(GET_FUNDS)) is_ok = calls.get_all_keys_funds().containsKey(key_); 
		
		return is_ok;
	}
		
	static boolean cancel_order(int id_) { return execute_order(ORDER_CANCEL, id_, null, null); }

	static boolean place_order(int id_, Contract contract_, Order order_) { return execute_order(ORDER_PLACE, id_, contract_, order_); }

	static boolean update_order(int id_, Contract contract_, Order order_) { return execute_order(ORDER_UPDATE, id_, contract_, order_); }
	
	static boolean update(String val_) 
	{ 
		boolean is_ok = true;

		if (strings.are_equal(_out, OUT_STRINGS)) _out_strings.add(val_); 
		else if (strings.is_number(val_)) is_ok = update(strings.to_number_decimal(val_));
		else is_ok = false;
		
		return is_ok;
	}
	
	static boolean update(double val_)
	{
		if (!strings.are_equal(_out, OUT_DECIMAL)) return false;

		_out_decimal = val_;

		return true;
	}

	static boolean update(int val_)
	{
		if (strings.are_equal(_out, OUT_INTS)) _out_ints.add(val_);
		else if (strings.are_equal(_out, OUT_INT)) _out_int = val_;
		else return false;

		return true;
	}
	
	static boolean update_orders(int order_id_, String status_ib_) 
	{	
		if (!is_ok(GET_ORDERS)) return false;
		
		_out_ints.add(order_id_);
		_out_strings.add(status_ib_);
		
		return true;
	}
	
	static boolean update_funds(String key_, String value_) 
	{	
		if (!is_ok(GET_FUNDS) || !strings.is_number(value_)) return false;
		
		_out_strings.add(key_);
		_out_decimals.add(Double.parseDouble(value_));
		
		return true;
	}
	
	static boolean update_error_triggered(boolean triggered_)
	{ 
		if (!is_ok()) return false;
		
		_error_triggered = triggered_;

		return true;
	}
	
	static void account_download_end(String account_ib_) 
	{
		if (!basic.account_ib_is_ok(account_ib_)) return;
			
		end_get(); 
	}

	private static boolean order_is_submitted(int order_id_) { return order_is_common(order_id_, sync_orders.STATUS_SUBMITTED); }
	
	private static boolean order_is_inactive(int order_id_) { return order_is_common(order_id_, sync_orders.STATUS_INACTIVE); }

	private static boolean order_is_common(int order_id_, String target_)
	{
		HashMap<Integer, String> orders = new HashMap<Integer, String>(get_orders());

		String status = (String)arrays.get_value(orders, order_id_);

		return (strings.is_ok(status) ? order.is_status(status, target_) : strings.are_equal(target_, sync_orders.STATUS_INACTIVE));
	}
	
	private static HashMap<String, String> get_all_get_outs() { return _alls.SYNC_GET_OUTS; }
	
	private static boolean execute_order(String type_, int id_, Contract contract_, Order order_)
	{	
		_order_id = id_;

		boolean is_cancel = orders.is_cancel(type_);

		boolean wait_default = false;
		boolean wait_error = false;
	
		if (is_cancel) 
		{
			wait_default = true;
			
			calls.cancelOrder(_order_id);
		}
		else 
		{
			if (contract_ == null || order_ == null) return false;
					
			//For ORDER_PLACE, the waiting occurs in sync_orders.place_update() after all the orders have been placed. 
			if (type_.equals(ORDER_UPDATE)) wait_error = true;

			calls.placeOrder(_order_id, contract_, order_);
		}

		boolean is_ok = true;

		if (wait_default) is_ok = wait_orders(type_);
		else if (wait_error) is_ok = wait_error();

		return is_ok; 
	}
	
	private static Object get(String type_)
	{
		common_xsync.get_req_id(true);
		
		return ((get_ini(type_) && get()) ? get_out() : null);
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
		else output = get_ini_out_hashmaps(is_ini_);
		
		return (is_ini_ ? true : output);
	}

	//Java's peculiar behavior when dealing with HashMaps or similar collections in
	//multithreading scenarios is the main reason explaining the unusual setups below.
	//!!!
	private static Object get_ini_out_hashmaps(boolean is_ini_)
	{
		Object output = null;
		
		if (_out.equals(OUT_ORDERS)) 
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
		else if (_out.equals(OUT_FUNDS)) 
		{
			if (is_ini_) 
			{
				_out_strings = new ArrayList<String>();
				_out_decimals = new ArrayList<Double>();
			}
			else 
			{
				HashMap<String, Double> funds = new HashMap<String, Double>();

				HashMap<String, String> keys = calls.get_all_keys_funds();
				
				for (int i = 0; i < _out_strings.size(); i++)
				{
					String key = _out_strings.get(i);
					if (!keys.containsKey(key)) continue;
					
					String field = keys.get(key);
					double val = _out_decimals.get(i);
					
					funds.put(field, val);
				}

				output = funds;
			}
		}
		
		return output;
	}

	private static boolean ini_is_ok(String get_)
	{
		_get = get_;
		_out = get_all_get_outs().get(_get);		
		_req_id = common_xsync.get_req_id(true);

		return true;
	}
	
	private static boolean get()
	{
		_getting = true;
		
		long timeout = DEFAULT_TIMEOUT;
		boolean cannot_fail = true;

		if (_get.equals(GET_FUNDS))
		{	
			//Methods called in external_ib.wrapper: accountSummary, accountSummaryEnd.		
			calls.reqAccountSummary(_req_id); 
		}
		else if (_get.equals(GET_ORDERS))
		{	
			timeout = TIMEOUT_GET_ORDERS;
			cannot_fail = false;

			//Methods called in external_ib.wrapper: openOrder, openOrderEnd, orderStatus.
			calls.reqAllOpenOrders(); 
		}
		else if (_get.equals(GET_ORDER_ID))
		{	
			//Methods called in external_ib.wrapper: nextValidId.
			calls.reqIds(); 
		}
		else return false;

		boolean is_ok = wait_get(timeout, cannot_fail);

		if (is_ok) get_after();
		
		_get = strings.DEFAULT;
		_getting = false;
	
		return is_ok;
	}

	private static void get_after()
	{
		if (_get.equals(GET_FUNDS)) calls.cancelAccountSummary(_req_id);
	}
	
	private static boolean wait_error()
	{
		_getting = true;
		_get = GET_ERROR;
		
		return wait_get(TIMEOUT_GET_ERROR, false);		
	}

	private static boolean wait_get(long timeout_, boolean cannot_fail_) { return wait(timeout_, cannot_fail_, null, true); }	
	
	private static boolean wait(long timeout_, boolean cannot_fail_, String type_, boolean is_getting_)
	{	
		boolean is_ok = true;		
		boolean is_place = false;
		boolean is_cancel = false;
		
		_error_triggered = false;
	
		if (orders.is_place(type_)) is_place = true;
		else if (orders.is_cancel(type_)) is_cancel = true;			

		long start = dates.start_elapsed();

		while (true)
		{
			if (_error_triggered) 
			{
				_error_triggered = false;
				is_ok = false;
				
				break;
			}
			
			if (is_place || is_cancel)
			{
				if ((is_place && order_is_submitted(_order_id)) || (is_cancel && order_is_inactive(_order_id))) break;
			}
			else if (is_getting_)
			{
				if (!_getting) 
				{
					boolean exit = true;
					
					if (_get.equals(GET_ORDERS))
					{
						if (_out_ints.size() != _out_strings.size()) exit = false;
					}
					else if (_get.equals(GET_FUNDS))
					{
						if (_out_strings.size() != _out_decimals.size()) exit = false;
					}
					
					if (exit) break;
				}
			}
			
			if (dates.get_elapsed(start) >= timeout_) 
			{
				if (!_get.equals(GET_ERROR))
				{
					if (cannot_fail_) errors.manage(ERROR_TIMEOUT);
					is_ok = false;					
				}

				break;
			}

			misc.pause_loop();
		}
		
		return is_ok;
	}
}