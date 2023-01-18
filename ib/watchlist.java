package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.misc;
import accessory.parent_static;
import accessory.strings;

public abstract class watchlist extends parent_static
{
	public static final String DB_SOURCE = db_ib.watchlist.SOURCE;

	public static final String DB_SYMBOL = db_ib.watchlist.SYMBOL;
	public static final String DB_PRICE = db_ib.watchlist.PRICE;
	public static final String DB_PRICE_INI = db_ib.watchlist.PRICE_INI;
	public static final String DB_PRICE_MIN = db_ib.watchlist.PRICE_MIN;
	public static final String DB_PRICE_MAX = db_ib.watchlist.PRICE_MAX;
	public static final String DB_VOLUME = db_ib.watchlist.VOLUME;
	public static final String DB_VOLUME_INI = db_ib.watchlist.VOLUME_INI;
	public static final String DB_VOLUME_MIN = db_ib.watchlist.VOLUME_MIN;
	public static final String DB_VOLUME_MAX = db_ib.watchlist.VOLUME_MAX;
	public static final String DB_TIME_ELAPSED = db_ib.watchlist.TIME_ELAPSED;
	public static final String DB_ELAPSED_INI = db_ib.watchlist.ELAPSED_INI;
	public static final String DB_FLU = db_ib.watchlist.FLU;
	public static final String DB_FLU2 = db_ib.watchlist.FLU2;
	public static final String DB_FLU2_MIN = db_ib.watchlist.FLU2_MIN;
	public static final String DB_FLU2_MAX = db_ib.watchlist.FLU2_MAX;
	public static final String DB_FLUS_PRICE = db_ib.watchlist.FLUS_PRICE;
	public static final String DB_VAR_TOT = db_ib.watchlist.VAR_TOT;
	
	public static boolean is_quick() { return db_ib.common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_ib.common.is_quick(DB_SOURCE, is_quick_); }

	public static boolean logs_to_screen() { return async_data_watchlist.logs_to_screen(); }

	public static void logs_to_screen(boolean logs_to_screen_) { async_data_watchlist.logs_to_screen(logs_to_screen_); }

	public static int max_simultaneous_symbols() { return async_data_watchlist.MAX_SIMULTANEOUS_SYMBOLS; }
	
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

	public static ArrayList<HashMap<String, String>> get_all_vals() { return db_ib.watchlist.get_all_vals(); }	

	public static HashMap<String, String> get_vals(String symbol_) { return db_ib.watchlist.get_vals(symbol_); }	
	
	public static double _get_price(String symbol_, boolean check_ib_) 
	{ 
		double output = get_price(symbol_);
		
		if (check_ib_)
		{
			double temp = __get_price(symbol_);
			
			if (ib.common.price_is_ok(temp)) output = temp;
		}
		
		return output; 
	}

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.async_data.get_price(DB_SOURCE, symbol) : common.WRONG_PRICE); 
	}

	public static double __get_price(String symbol_) 
	{ 
		double output = common.WRONG_PRICE;
		
		String symbol = ib.common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
		
		boolean is_quick = is_quick();

		if (is_quick) watchlist_quicker.__add(symbol_);
		else add(symbol_);
		
		misc.pause_secs(5);
		
		if (is_quick) watchlist_quicker.__remove(symbol_);
		else remove(symbol_);
		
		return get_price(symbol_);
	}
}