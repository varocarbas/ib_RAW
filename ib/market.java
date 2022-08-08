package ib;

import java.util.ArrayList;

import accessory.strings;
import db_ib.async_data;

public abstract class market 
{	
	public static void pause_nonstop(int pause_nonstop_) { async_data_market.pause_nonstop(pause_nonstop_); }

	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_market.logs_to_screen(logs_to_screen_); }
			
	public static boolean start(String symbol_) { return async_data_market.start(symbol_); }

	public static boolean start_snapshot(String symbol_) { return async_data_market.start_snapshot(symbol_); }

	public static boolean start_stream(String symbol_) { return async_data_market.start_stream(symbol_); }

	public static void stop(String symbol_) { async_data_market.stop(symbol_); }

	public static void stop_all() { async_data_market.stop_all(); }
	
	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? async_data.get_price(db_ib.market.SOURCE, symbol) : common.WRONG_PRICE); 
	}
	
	public static ArrayList<String> get_active_symbols() { return async_data_market.get_active_symbols(); }
}