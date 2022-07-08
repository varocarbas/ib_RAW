package ib;

import accessory.parent_static;
import accessory.strings;
import db_ib.watchlist;

public abstract class async_watchlist extends parent_static 
{
	static void __add(String symbol_) 
	{ 
		__lock();
		
		String symbol = normalise_symbol(symbol_);
		if (strings.is_ok(symbol) && !symbol_exists(symbol)) add(symbol); 
		
		__unlock();
	}

	static void __remove(String symbol_) 
	{ 
		__lock();
		
		String symbol = normalise_symbol(symbol_);
		if (strings.is_ok(symbol) && symbol_exists(symbol)) remove(symbol);  
		
		__unlock();
	}

	private static boolean symbol_exists(String symbol_) { return watchlist.exists(symbol_); }

	private static String normalise_symbol(String symbol_) { return parent_async_data.normalise_symbol(symbol_); }
	
	private static void add(String symbol_) { async_data_watchlist._start(symbol_, false); }
	
	private static void remove(String symbol_) 
	{ 
		watchlist.delete(symbol_);
		
		async_data_watchlist._stop(symbol_, false); 
	}
}