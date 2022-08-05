package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.parent_static;
import accessory.strings;
import db_ib.async_data;

public abstract class trades extends parent_static
{
	public static final String PRICE = db_ib.trades.PRICE;
	public static final String HALTED = db_ib.trades.HALTED;	

	public static final int PRICE_IB = parent_async_data.PRICE_IB;
	public static final int HALTED_IB = parent_async_data.HALTED_IB;

	public static final boolean DEFAULT_SYNCED_WITH_EXECS = true;
	
	private static boolean _synced_with_execs = DEFAULT_SYNCED_WITH_EXECS; 
	
	public static boolean is_ok() { return enabled(); }

	public static void enable() { enabled(true); }

	public static void disable() { enabled(false); }
	
	public static boolean enabled() { return async_data_trades._instance._enabled; }
	
	public static void enabled(boolean enabled_) 
	{ 
		async_data_trades._instance._enabled = enabled_; 
	
		if (_synced_with_execs) execs.enabled(enabled_, false);
	}
	
	public static boolean synced_with_execs() { return _synced_with_execs; }
	
	public static void synced_with_execs(boolean synced_with_execs_) 
	{ 
		_synced_with_execs = synced_with_execs_; 
	
		if (synced_with_execs_) execs.enabled(is_ok(), false);
		else execs.disable(false);
	}
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_trades._instance.update_logs_to_screen_internal(logs_to_screen_); }
	
	public static void __order_status(int order_id_, String status_ib_) 
	{ 
		if (!synced_with_execs()) return;
		
		__lock();

		async_trades.start(order_id_, status_ib_);

		__unlock();
	}
	
	public static void start(int order_id_, double start_) { async_trades.start(order_id_, start_); }
	
	public static void end(int order_id_, double end_) { async_trades.end(order_id_, end_); }

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

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? async_data.get_price(db_ib.trades.SOURCE, symbol) : common.WRONG_PRICE); 
	}

	public static double get_start(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.get_start(symbol) : common.WRONG_PRICE); 
	}

	public static double get_end(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.get_end(symbol) : common.WRONG_PRICE); 
	}

	public static boolean is_trading(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.exists(symbol) : false); 
	}
	
	public static ArrayList<String> get_active_symbols() { return async_data_trades._instance.get_active_symbols(); }

	static boolean exists(int order_id_main_) { return db_ib.trades.order_id_exists(order_id_main_, true); }
}