package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db_quick;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._types;

abstract class remote_request extends parent_static 
{
	public static final long TIMEOUT = 20l;

	public static final String REQUEST_OK = _types.REMOTE_REQUEST_OK;
	public static final String REQUEST_ERROR = _types.REMOTE_REQUEST_ERROR;
	public static final String REQUEST_IGNORED = _types.REMOTE_REQUEST_IGNORED;
	
	public static final String ERROR = _types.ERROR_IB_REMOTE_REQUEST; 
	public static final String ERROR_REQUEST = _types.ERROR_IB_REMOTE_REQUEST_REQUEST; 
	public static final String ERROR_PLACE = _types.ERROR_IB_REMOTE_REQUEST_PLACE; 
	public static final String ERROR_CANCEL = _types.ERROR_IB_REMOTE_REQUEST_CANCEL;
	public static final String ERROR_UPDATE = _types.ERROR_IB_REMOTE_REQUEST_UPDATE;
	public static final String ERROR_WAIT = _types.ERROR_IB_REMOTE_REQUEST_WAIT;
	public static final String ERROR_FAIL = _types.ERROR_IB_REMOTE_REQUEST_FAIL;
	
	public static int __place(String type_place_, String symbol_, double stop_, double start_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, quantity_, wait_for_execution_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, symbol_, type_place_, quantity_, stop_, common.WRONG_PRICE, start_, common.WRONG_PRICE);
		else if (wait_for_execution_)
		{
			if (!__wait_for_execution(request)) 
			{
				update_error_wait(request, symbol_, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
	
	public static int __place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, boolean wait_for_execution_) { return __place(type_place_, symbol_, stop_, ib.common.WRONG_PRICE, start_, start2_, quantity_, wait_for_execution_); }
		
	public static int __place(String type_place_, String symbol_, double stop_, double stop2_, double start_, double start2_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, stop2_, start_, start2_, quantity_, common.WRONG_MONEY, common.WRONG_PRICE); 

		if (request == common.WRONG_REQUEST) update_error_place(request, symbol_, type_place_, quantity_, stop_, stop2_, start_, start2_);
		else if (wait_for_execution_)
		{
			if (!__wait_for_execution(request)) 
			{
				update_error_wait(request, symbol_, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}

	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_perc(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, perc_money_, price_, wait_for_execution_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, symbol_, type_place_, price_, perc_money_, stop_, common.WRONG_PRICE, start_, common.WRONG_PRICE);
		else if (wait_for_execution_)
		{
			if (!__wait_for_execution(request)) 
			{
				update_error_wait(request, symbol_, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
	
	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) { return __place_perc(type_place_, symbol_, stop_, ib.common.WRONG_PRICE, start_, start2_, perc_money_, price_, wait_for_execution_); }
	
	public static int __place_perc(String type_place_, String symbol_, double stop_, double stop2_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, stop2_, start_, start2_, common.WRONG_QUANTITY, perc_money_, price_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, symbol_, type_place_, price_, perc_money_, stop_, stop2_, start_, start2_);
		else if (wait_for_execution_)
		{
			if (!__wait_for_execution(request)) 
			{
				update_error_wait(request, symbol_, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
		
	public static String __cancel(int request_, boolean wait_for_execution_) 
	{ 	
		if (!can_request(request_, true)) return REQUEST_IGNORED;
		
		if (!is_ok(request_))
		{
			update_error_request(request_, remote.CANCEL);
			
			return REQUEST_ERROR;
		}
		
		if (!db_ib.remote.request_update_type_order(request_, remote.CANCEL))
		{
			update_error(request_, null, ERROR_CANCEL, remote.CANCEL);
			
			return REQUEST_ERROR;
		}
				
		HashMap<String, String> temp = db_ib.remote.get_vals(request_, new String[] { db_ib.remote.SYMBOL, db_ib.remote.ORDER_ID_MAIN });
		
		String symbol = ib.remote.get_symbol(temp);
		int order_id = ib.remote.get_order_id_main(temp);
		
		remote.log(remote.get_ok_message_default(remote.CANCEL, request_, symbol, common.WRONG_ORDER_ID, true)); 
		
		String output = REQUEST_OK;

		if (wait_for_execution_ && !__wait_for_execution(request_, true)) 
		{
			if (!ib.orders._is_inactive_both(order_id))
			{
				update_error_wait(request_, symbol, remote.CANCEL);
				
				output = REQUEST_ERROR;
			}
		}
		
		return output;
	}

	public static String __update(int request_, String type_update_, double val_, boolean wait_for_execution_)
	{
		if (!can_request(request_, false)) return REQUEST_IGNORED;
		
		if (!is_ok(request_))
		{
			update_error_request(request_, type_update_);
			
			return REQUEST_ERROR;
		}
		
		double val = db_ib.common.adapt_price(val_);

		String type_update = remote.check_type_update(type_update_);
		if (!strings.is_ok(type_update) || !(remote.is_update_market(type_update) || common.price_is_ok(val))) return REQUEST_ERROR;
		
		String symbol = db_ib.remote.get_symbol(request_);

		if (!db_ib.remote.request_update_type_order_values(request_, type_update, db_ib.orders.get_field_update(type_update), val))
		{
			update_error_update(request_, symbol, type_update, val_);
			
			return REQUEST_ERROR;
		}

		remote.log(remote.get_ok_message_default(type_update, request_, symbol, common.WRONG_ORDER_ID, true));

		String output = REQUEST_OK;

		if (wait_for_execution_ && !__wait_for_execution(request_)) 
		{
			update_error_wait(request_, symbol, type_update);
			
			output = (ib.orders.is_filled(symbol) ? REQUEST_ERROR : REQUEST_IGNORED);
		}
		
		return output;
	}

	public static boolean __wait_for_execution(int request_) { return __wait_for_execution(request_, false); }

	public static boolean __wait_for_execution(int request_, boolean is_cancel_) { return _is_executed(request_, false, is_cancel_, true); }

	static String get_error_message(String type_)
	{	
		String message = strings.DEFAULT;
		
		if (type_.equals(ERROR_PLACE)) message = "place request failed";
		else if (type_.equals(ERROR_CANCEL)) message = "cancel request failed";
		else if (type_.equals(ERROR_UPDATE)) message = "update request failed";
		else if (type_.equals(ERROR_WAIT)) message = "tired of waiting for request execution";
		else if (type_.equals(ERROR_FAIL)) message = "request execution failed";
		else message = remote.get_error_message_default(type_, true);
		
		return message;
	}
	
	private static int __place_common(String type_place_, String symbol_, double stop_, double stop2_, double start_, double start2_, double quantity_, double perc_money_, double price_)
	{	
		int output = common.WRONG_REQUEST;
		
		if (!basic.multiple_trades_symbol())
		{
			output = db_ib.remote.get_any_request(symbol_);
			
			if (output != common.WRONG_REQUEST) return output;			
		}
		
		double quantity = quantity_;
		double perc_money = common.WRONG_MONEY; 
		double price = common.WRONG_PRICE;
		
		if (common.percent_is_ok(perc_money_, false))
		{
			perc_money = perc_money_;
			price = price_;
			quantity = 1;
		}
		
		_order order = new _order(type_place_, symbol_, quantity, stop_, start_, start2_);

		if (common.price_is_ok(stop2_)) order.update_stop2(stop2_);
		
		if (order.is_ok()) output = db_ib.remote.__request_start(order, perc_money, price);
	
		if (output != common.WRONG_REQUEST) remote.log(remote.get_ok_message_default(type_place_, output, symbol_, order.get_id_main(), true));
	
		return output;
	}
	
	private static boolean can_request(int request_, boolean is_cancel_) { return (db_ib.remote.is_active(request_) && (!is_cancel_ || !strings.are_equal(remote.get_type_order(request_), remote.CANCEL))); }

	private static boolean is_ok(int request_) 
	{ 
		boolean is_ok = _is_executed(request_, true, false, false); 
	
		if (is_ok) db_ib.remote.update_status2(request_, db_ib.remote.store_status2_type(ib.remote.STATUS2_EXECUTED));
		
		return is_ok;
	}
	
	private static boolean _is_executed(int request_, boolean ignore_error_, boolean inactive_ok_, boolean lock_)
	{
		if (lock_) __lock();
		
		int temp = is_executed_internal(request_, ignore_error_, inactive_ok_);
		if (temp != -1) 
		{
			if (lock_) __unlock();
			
			return numbers.to_boolean(temp);
		}

		long start = dates.start_elapsed();
			
		while (dates.get_elapsed(start) < TIMEOUT)
		{
			temp = is_executed_internal(request_, ignore_error_, inactive_ok_);
			
			if (temp != -1) 
			{
				if (lock_) __unlock();
				
				return numbers.to_boolean(temp);
			}
		}

		if (lock_) __unlock();
		
		return false;
	}

	private static int is_executed_internal(int request_, boolean ignore_error_, boolean inactive_ok_)
	{
		HashMap<String, String> vals = remote.get_vals(request_);
		if (!arrays.is_ok(vals)) return numbers.from_boolean(false);
		
		int output = -1;
				
		String status = remote.get_status(vals);
		String status2 = remote.get_status2(vals);

		if (strings.are_equal(status, remote.STATUS_ERROR) || strings.are_equal(status2, remote.STATUS2_ERROR)) 
		{
			if (!ignore_error_) remote._temp_type_error = ERROR_FAIL;
			
			output = numbers.from_boolean(ignore_error_);
		}
		else if (strings.are_equal(status, remote.STATUS_INACTIVE)) output = numbers.from_boolean(inactive_ok_); 
		else if (strings.are_equal(status2, remote.STATUS2_EXECUTED)) output = numbers.from_boolean(true);		

		return output;
	}
	
	private static void update_error_place(int request_, String symbol_, String type_, double quantity_, double stop_, double stop2_, double start_, double start2_) { remote.update_error_place(request_, symbol_, type_, quantity_, stop_, stop2_, start_, start2_, true); }

	private static void update_error_place(int request_, String symbol_, String type_, double price_, double perc_, double stop_, double stop2_, double start_, double start2_) { remote.update_error_place(request_, symbol_, type_, price_, perc_, stop_, stop2_, start_, start2_, true); }

	private static void update_error_update(int request_, String symbol_, String type_, double val_) { remote.update_error_update(request_, symbol_, type_, common.WRONG_ORDER_ID, val_, true); }

	private static void update_error_request(int request_, String type_order_) { update_error(request_, null, ERROR_REQUEST, type_order_); }
	
	private static void update_error_wait(int request_, String symbol_, String type_order_) { update_error(request_, symbol_, ERROR_WAIT, type_order_); }
	
	private static void update_error(int request_, String symbol_, String type_error_, String type_order_) 
	{ 
		Object vals = null;
		
		if (strings.is_ok(symbol_))
		{
			HashMap<String, Object> vals2 = new HashMap<String, Object>();
			vals2.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.SYMBOL), strings.to_string(symbol_));
			
			vals = vals2;
		}
		else vals = type_order_;
		
		remote.update_error(request_, type_error_, vals, type_order_);
	}
}