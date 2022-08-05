package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.parent_static;
import accessory.strings;
import db_ib.async_data;

public abstract class watchlist extends parent_static
{
	public static final String SOURCE = db_ib.watchlist.SOURCE;
	
	public static final String PRICE = db_ib.watchlist.PRICE;
	public static final String VOLUME = db_ib.watchlist.VOLUME;
	public static final String HALTED = db_ib.watchlist.HALTED;	

	public static final int PRICE_IB = parent_async_data.PRICE_IB;
	public static final int VOLUME_IB = parent_async_data.VOLUME_IB;
	public static final int HALTED_IB = parent_async_data.HALTED_IB;
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_watchlist_new.logs_to_screen(logs_to_screen_); }
	
	public static boolean __add(String symbol_) 
	{ 
		__lock();
		
		boolean added = false;
		
		String symbol = common.normalise_symbol(symbol_);

		if (strings.is_ok(symbol)) added = async_data_watchlist_new._start(symbol, true); 
		
		__unlock();
		
		return added;
	}

	public static void __remove(String symbol_) 
	{ 
		__lock();
		
		String symbol = common.normalise_symbol(symbol_);

		if (strings.is_ok(symbol)) 
		{
			async_data_watchlist_new._stop(symbol, false);
			
			db_ib.watchlist.delete(symbol_);
		}
		
		__unlock();
	}

	public static ArrayList<String> get_active_symbols() { return async_data_watchlist_new.get_active_symbols(); }

	public static ArrayList<HashMap<String, String>> get_all_vals(boolean is_quick_) { return db_ib.watchlist.get_all_vals(is_quick_); }
	
	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(parent_async_data.PRICE_IB, PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(parent_async_data.VOLUME_IB, VOLUME);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(parent_async_data.HALTED_IB, HALTED);
		
		return all;
	}

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? async_data.get_price(db_ib.watchlist.SOURCE, symbol) : common.WRONG_PRICE); 
	}
}