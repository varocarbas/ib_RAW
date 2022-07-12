package ib;

import java.util.HashMap;

import accessory.parent_static;
import accessory.strings;

public abstract class watchlist extends parent_static
{
	public static final String SOURCE = db_ib.watchlist.SOURCE;
	
	public static final String PRICE = db_ib.watchlist.PRICE;
	public static final String VOLUME = db_ib.watchlist.VOLUME;
	public static final String HALTED = db_ib.watchlist.HALTED;	
	public static final String HALTED_TOT = db_ib.watchlist.HALTED_TOT;	
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_watchlist._instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static void __add(String symbol_) 
	{ 
		__lock();
		
		String symbol = async_watchlist.normalise_symbol(symbol_);
		if (strings.is_ok(symbol) && !async_watchlist.symbol_exists(symbol)) async_watchlist.add(symbol); 
		
		__unlock();
	}

	public static void __remove(String symbol_) 
	{ 
		__lock();
		
		String symbol = async_watchlist.normalise_symbol(symbol_);
		if (strings.is_ok(symbol) && async_watchlist.symbol_exists(symbol)) async_watchlist.remove(symbol);  
		
		__unlock();
	}
	
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
}