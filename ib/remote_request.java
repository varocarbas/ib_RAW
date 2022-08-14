package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

abstract class remote_request extends parent_static 
{
	public static final long TIMEOUT = 30l;

	public static final String ERROR = types.ERROR_IB_REMOTE_REQUEST; 
	public static final String ERROR_REQUEST = types.ERROR_IB_REMOTE_REQUEST_REQUEST; 
	public static final String ERROR_PLACE = types.ERROR_IB_REMOTE_REQUEST_PLACE; 
	public static final String ERROR_CANCEL = types.ERROR_IB_REMOTE_REQUEST_CANCEL;
	public static final String ERROR_UPDATE = types.ERROR_IB_REMOTE_REQUEST_UPDATE;
	public static final String ERROR_WAIT = types.ERROR_IB_REMOTE_REQUEST_WAIT;
	
	public static int __place(String type_place_, String symbol_, double stop_, double start_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, quantity_, wait_for_execution_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, type_place_, quantity_, stop_, start_, common.WRONG_PRICE);
		else if (wait_for_execution_)
		{
			if (!wait_for_execution(request)) 
			{
				remote.update_error(request, ERROR_WAIT, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
	
	public static int __place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, start_, start2_, quantity_, common.WRONG_MONEY, common.WRONG_PRICE); 

		if (request == common.WRONG_REQUEST) update_error_place(request, type_place_, quantity_, stop_, start_, start2_);
		else if (wait_for_execution_)
		{
			if (!wait_for_execution(request)) 
			{
				remote.update_error(request, ERROR_WAIT, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}

	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_perc(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, perc_money_, price_, wait_for_execution_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, type_place_, price_, perc_money_, stop_, start_, common.WRONG_PRICE);
		else if (wait_for_execution_)
		{
			if (!wait_for_execution(request)) 
			{
				remote.update_error(request, ERROR_WAIT, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
	
	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, start_, start2_, common.WRONG_QUANTITY, perc_money_, price_); 

		if (request == common.WRONG_REQUEST) update_error_place(request, type_place_, price_, perc_money_, stop_, start_, start2_);
		else if (wait_for_execution_)
		{
			if (!wait_for_execution(request)) 
			{
				remote.update_error(request, ERROR_WAIT, type_place_);
				
				request = common.WRONG_REQUEST;
			}
		}
		
		return request;
	}
	
	public static boolean cancel(int request_, boolean wait_for_execution_) 
	{ 
		boolean output = false;
		
		if (!is_ok(request_, true))
		{
			remote.update_error(request_, ERROR_REQUEST, remote.CANCEL);
			
			return output;
		}
		
		output = db_ib.remote.request_update_type_order(request_, remote.CANCEL, remote.is_quick()); 
		
		if (!output) remote.update_error(request_, ERROR_CANCEL, null);
		else if (wait_for_execution_) 
		{
			output = wait_for_execution(request_);
			
			if (!output) remote.update_error(request_, ERROR_WAIT, remote.CANCEL);
		}
		
		return output;
	}

	public static boolean update(int request_, String type_update_, double val_, boolean wait_for_execution_)
	{
		boolean output = false;
		
		if (!is_ok(request_, false))
		{
			remote.update_error(request_, ERROR_REQUEST, type_update_);
			
			return output;
		}
		
		double val = db_ib.common.adapt_price(val_);

		String type_update = orders.check_update(type_update_);
		if (!strings.is_ok(type_update) || !(orders.is_update_market(type_update) || common.price_is_ok(val))) return output;

		output = db_ib.remote.request_update_type_order_values(request_, type_update, db_ib.orders.get_field_update(type_update), val, remote.is_quick());
		
		if (!output) update_error_update(request_, type_update, val_);
		else if (wait_for_execution_) 
		{
			output = wait_for_execution(request_);
			
			if (!output) remote.update_error(request_, ERROR_WAIT, type_update);
		}
		
		return output;
	}

	public static boolean wait_for_execution(int request_) { return is_executed(request_, false, true); }

	static String get_error_message(String type_)
	{	
		String message = strings.DEFAULT;
		
		if (type_.equals(ERROR_PLACE)) message = "place request failed";
		else if (type_.equals(ERROR_CANCEL)) message = "cancel request failed";
		else if (type_.equals(ERROR_UPDATE)) message = "update request failed";
		else if (type_.equals(ERROR_WAIT)) message = "tired of waiting for request execution";
		else message = remote.get_error_message_default(type_, true);
		
		return message;
	}
	
	private static int __place_common(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, double perc_money_, double price_)
	{
		double quantity = quantity_;
		double perc_money = common.WRONG_MONEY; 
		double price = common.WRONG_PRICE;
		
		if (common.percent_is_ok(perc_money_, false))
		{
			perc_money = perc_money_;
			price = price_;
			quantity = 1;
		}
		
		order order = new order(type_place_, symbol_, quantity, stop_, start_, start2_);

		return (order.is_ok() ? db_ib.remote.__request_start(order, perc_money, price, remote.is_quick()) : common.WRONG_REQUEST);
	}
	
	private static boolean is_ok(int request_, boolean is_cancel_) { return (db_ib.remote.is_active(request_) && remote.order_id_is_ok(remote.get_order_id_main(request_), is_cancel_) && is_executed(request_, true, false)); }
	
	private static boolean is_executed(int request_, boolean ignore_error_, boolean inactive_ok_)
	{
		HashMap<String, String> vals = remote.get_vals(request_);
		if (!arrays.is_ok(vals)) return inactive_ok_;
		
		String status = remote.get_status(vals);
		if (!strings.are_equal(status, remote.STATUS_ACTIVE)) return inactive_ok_; 
		
		String status2 = remote.get_status2(vals);
		if (strings.are_equal(status2, remote.STATUS2_EXECUTED)) return true;		
		else if (strings.are_equal(status2, remote.STATUS2_ERROR)) return ignore_error_;

		boolean is_quick = remote.is_quick();
		
		int order_id_main = remote.get_order_id_main(vals);
		
		long start = dates.start_elapsed();
			
		while (true)
		{
			vals = remote.get_vals(request_);
			if (!arrays.is_ok(vals)) return inactive_ok_;
			
			status = remote.get_status(vals);
			if (!strings.are_equal(status, remote.STATUS_ACTIVE)) return inactive_ok_; 
			
			status2 = remote.get_status2(vals);
			
			if (strings.are_equal(status2, remote.STATUS2_EXECUTED)) return true;		
			else if (strings.are_equal(status2, remote.STATUS2_ERROR)) return ignore_error_;

			if (dates.get_elapsed(start) >= remote_request.TIMEOUT) 
			{				
				db_ib.remote.deactivate_order_id(order_id_main);
				
				misc.pause_secs(2);
				
				if (remote.order_was_updated(vals))	
				{
					Object vals2 = db_ib.remote.add_to_vals(db_ib.remote.STATUS, db_ib.remote.get_key_from_status(remote.STATUS_ACTIVE), null, is_quick);
					vals2 = db_ib.remote.add_to_vals(db_ib.remote.STATUS2, db_ib.remote.get_status2_key_execute(true), vals2, is_quick);

					db_ib.remote.update(request_, vals2, is_quick);
					
					return true;
				}
				
				break;
			}
		}

		return false;
	}

	private static void update_error_place(int request_, String type_, double quantity_, double stop_, double start_, double start2_) { remote.update_error_place(request_, type_, quantity_, stop_, start_, start2_, true); }

	private static void update_error_place(int request_, String type_, double price_, double perc_, double stop_, double start_, double start2_) { remote.update_error_place(request_, type_, price_, perc_, stop_, start_, start2_, true); }

	private static void update_error_update(int request_, String type_, double val_) { remote.update_error_update(request_, type_, common.WRONG_ORDER_ID, val_, true); }
}