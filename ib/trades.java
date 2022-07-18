package ib;

import java.util.HashMap;

import accessory.parent_static;

public abstract class trades extends parent_static
{
	public static final String SOURCE = db_ib.trades.SOURCE;
	
	public static final String PRICE = db_ib.trades.PRICE;
	public static final String HALTED = db_ib.trades.HALTED;	

	public static final int PRICE_IB = parent_async_data.PRICE_IB;
	public static final int HALTED_IB = parent_async_data.HALTED_IB;
	
	public static final double WRONG_POSITION = 0.0;
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_trades._instance.update_logs_to_screen_internal(logs_to_screen_); }
	
	public static void __order_status(int order_id_, String status_ib_) 
	{ 
		__lock();
		
		if (async_trades.order_id_is_ok(order_id_))
		{
			if (async_trades.status_is_ok(status_ib_, true))
			{
				if (!async_trades.order_id_exists(order_id_)) async_trades.start(order_id_);
			}
			else if (async_trades.status_is_ok(status_ib_, false))
			{
				if (async_trades.order_id_exists(order_id_)) async_trades.end(order_id_);				
			}
		}
		
		__unlock();
	}
	
	public static void _start(int order_id_, double price_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		if (async_trades.order_id_is_ok(order_id_) && !async_trades.order_id_exists(order_id_)) async_trades.start(order_id_); 
		
		if (lock_) __unlock();
	}

	public static void _end(int order_id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		if (async_trades.order_id_exists(order_id_)) async_trades.end(order_id_); 
		
		if (lock_) __unlock();
	}
	
	public static double get_position(String symbol_) { return sync_trades.get_position(symbol_); }
	
	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE_IB, PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED_IB, HALTED);
		
		return all;
	}
}