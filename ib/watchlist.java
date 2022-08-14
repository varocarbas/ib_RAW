package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.parent_static;
import accessory.strings;

public abstract class watchlist extends parent_static
{
	public static boolean logs_to_screen() { return async_data_watchlist.logs_to_screen(); }

	public static void logs_to_screen(boolean logs_to_screen_) { async_data_watchlist.logs_to_screen(logs_to_screen_); }
	
	public static boolean add(String symbol_) { return async_data_watchlist.start(symbol_); }

	public static void remove(String symbol_) 
	{ 
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return;
		 
		async_data_watchlist.stop(symbol);
		
		if (!async_data.symbol_is_running(async_data_watchlist._APP, symbol)) db_ib.watchlist.delete(symbol);
	}

	public static void remove_all() { async_data_market.stop_all(); }

	public static ArrayList<String> get_active_symbols() { return async_data_watchlist.get_active_symbols(); }

	public static ArrayList<HashMap<String, String>> get_all_vals(boolean is_quick_) { return db_ib.watchlist.get_all_vals(is_quick_); }	

	public static HashMap<String, String> get_vals(String symbol_, boolean is_quick_) { return db_ib.watchlist.get_vals(symbol_, is_quick_); }	

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.async_data.get_price(db_ib.watchlist.SOURCE, symbol) : common.WRONG_PRICE); 
	}
}