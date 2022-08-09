package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;

abstract class remote_request extends parent_static 
{
	public static final long TIMEOUT = 20l;

	public static int __place(String type_place_, String symbol_, double stop_, double start_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, quantity_, wait_for_execution_); 
	
		if (request != common.WRONG_REQUEST && wait_for_execution_) 
		{
			if (!wait_for_execution(request)) request = common.WRONG_REQUEST;
		}
		
		return request;
	}
	
	public static int __place(String type_place_, String symbol_, double stop_, double start_, double start2_, double quantity_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, start_, start2_, quantity_, common.WRONG_MONEY, common.WRONG_PRICE); 
		
		if (request != common.WRONG_REQUEST && wait_for_execution_) 
		{
			if (!wait_for_execution(request)) request = common.WRONG_REQUEST;
		}
		
		return request;
	}

	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_perc(type_place_, symbol_, stop_, start_, common.WRONG_PRICE, perc_money_, price_, wait_for_execution_); 

		if (request != common.WRONG_REQUEST && wait_for_execution_) 
		{
			if (!wait_for_execution(request)) request = common.WRONG_REQUEST;
		}
		
		return request;
	}
	
	public static int __place_perc(String type_place_, String symbol_, double stop_, double start_, double start2_, double perc_money_, double price_, boolean wait_for_execution_) 
	{ 
		int request = __place_common(type_place_, symbol_, stop_, start_, start2_, common.WRONG_QUANTITY, perc_money_, price_); 

		if (request != common.WRONG_REQUEST && wait_for_execution_) 
		{
			if (!wait_for_execution(request)) request = common.WRONG_REQUEST;
		}
		
		return request;
	}
	
	public static boolean cancel(int request_, boolean wait_for_execution_) 
	{ 
		boolean output = (is_ok(request_, true) ? db_ib.remote.request_update_type_order(request_, remote.CANCEL, remote.is_quick()) : false); 
	
		if (output && wait_for_execution_) output = wait_for_execution(request_);
		
		return output;
	}

	public static boolean update(int request_, String type_update_, double val_, boolean wait_for_execution_)
	{
		boolean is_ok = false;
		if (!is_ok(request_, false)) return is_ok;
		
		double val = db_ib.common.adapt_price(val_);

		String type_update = orders.check_update(type_update_);
		if (!strings.is_ok(type_update) || !(orders.is_update_market(type_update) || common.price_is_ok(val))) return is_ok;

		is_ok = db_ib.remote.request_update_type_order_values(request_, type_update, db_ib.orders.get_field_update(type_update), val, remote.is_quick());
		if (is_ok && wait_for_execution_) is_ok = wait_for_execution(request_);
		
		return is_ok;
	}

	public static boolean wait_for_execution(int request_) { return is_executed(request_, false, true); }
	
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
}