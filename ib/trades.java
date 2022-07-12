package ib;

import java.util.HashMap;

import accessory.parent_static;

public abstract class trades extends parent_static
{
	public static final String SOURCE = db_ib.trades.SOURCE;
	
	public static final String PRICE = db_ib.trades.PRICE;
	public static final String HALTED = db_ib.trades.HALTED;	

	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_trades._instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static void _start(int order_id_, boolean lock_) 
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
	
	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(parent_async_data.PRICE_IB, PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(parent_async_data.HALTED_IB, HALTED);
		
		return all;
	}
}