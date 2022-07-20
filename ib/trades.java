package ib;

import java.util.HashMap;

import accessory.parent_static;

public abstract class trades extends parent_static
{
	public static final String PRICE = db_ib.trades.PRICE;
	public static final String HALTED = db_ib.trades.HALTED;	

	public static final int PRICE_IB = parent_async_data.PRICE_IB;
	public static final int HALTED_IB = parent_async_data.HALTED_IB;

	public static boolean is_ok() { return async_data_trades._instance._enabled; }
	
	public static void enable() 
	{ 
		async_data_trades._instance.update_enabled(true); 
	
		execs.enable(false);
	}
	
	public static void disable() 
	{ 
		async_data_trades._instance.update_enabled(false); 
		
		execs.disable(false);
	}
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_trades._instance.update_logs_to_screen_internal(logs_to_screen_); }
	
	public static void __order_status(int order_id_, String status_ib_) 
	{ 
		__lock();
		
		async_trades.start(order_id_, status_ib_);

		__unlock();
	}
	
	public static void start(int order_id_, double start_) { async_trades.start(order_id_, start_); }
	
	public static void end(int order_id_, double price_) { async_trades.end(order_id_, price_); }
	
	public static double get_position_start(String symbol_) { return sync_trades.get_position(symbol_); }
	
	public static double get_position(int order_id_main_) { return db_ib.trades.get_position(order_id_main_); }

	public static double get_unrealised(double position_) { return sync_trades.get_unrealised(position_); }

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

	static boolean exists(int order_id_main_) { return db_ib.trades.order_id_exists(order_id_main_, true); }
}