package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.db_common;
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
	public static final String DB_FLU3 = db_ib.watchlist.FLU3;
	public static final String DB_FLUS_PRICE = db_ib.watchlist.FLUS_PRICE;
	public static final String DB_VAR_TOT = db_ib.watchlist.VAR_TOT;
	
	public static boolean is_quick() { return db_common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(DB_SOURCE, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_vals() { return db_ib.watchlist.get_all_vals(); }	

	public static HashMap<String, String> get_vals(String symbol_) { return db_ib.watchlist.get_vals(symbol_); }	

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.async_data.get_price(DB_SOURCE, symbol) : common.WRONG_PRICE); 
	}
}