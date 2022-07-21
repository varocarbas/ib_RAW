package ib;

import accessory.parent_static;
import db_ib.watchlist;

abstract class async_watchlist extends parent_static 
{
	public static boolean symbol_exists(String symbol_) { return watchlist.exists(symbol_); }

	public static void add(String symbol_) { async_data_watchlist.start(symbol_); }
	
	public static void remove(String symbol_) 
	{ 
		watchlist.delete(symbol_);
		
		async_data_watchlist.stop(symbol_); 
	}
}