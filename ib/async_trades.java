package ib;

import accessory.parent_static;
import accessory.strings;
import db_ib.trades;

abstract class async_trades extends parent_static
{	
	public static final String STATUS_FILLED = order.STATUS_FILLED;
	public static final String STATUS_INACTIVE = order.STATUS_INACTIVE;
	
	static void __order_status(int order_id_, String status_ib_) 
	{ 
		__lock();
		
		if (order_id_is_ok(order_id_))
		{
			if (status_is_ok(status_ib_, true))
			{
				if (!order_id_exists(order_id_)) start(order_id_);
			}
			else if (status_is_ok(status_ib_, false))
			{
				if (order_id_exists(order_id_)) end(order_id_);				
			}
		}
		
		__unlock();
	}

	static void _start(int order_id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		if (order_id_is_ok(order_id_) && !order_id_exists(order_id_)) start(order_id_); 
		
		if (lock_) __unlock();
	}

	static void _end(int order_id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		if (order_id_exists(order_id_)) end(order_id_); 
		
		if (lock_) __unlock();
	}
	
	private static String get_symbol(int order_id_) { return trades.get_symbol(order_id_); }

	private static boolean order_id_is_ok(int order_id_) { return trades.is_ok(order_id_); }
	
	private static boolean order_id_exists(int order_id_) { return trades.exists(order_id_); }

	private static boolean status_is_ok(String status_ib_, boolean is_add_) { return order.is_status(status_ib_, (is_add_ ? STATUS_FILLED : STATUS_INACTIVE)); }

	private static void start(int order_id_) 
	{
		String symbol = get_symbol(order_id_);
		if (!strings.is_ok(symbol)) return;
		
		trades.insert(order_id_, symbol);
		
		async_data_trades._start(order_id_, symbol, false);
	}
	
	private static void end(int order_id_) 
	{
		trades.end(order_id_);
		
		async_data_trades._stop(order_id_, false);
	}
}