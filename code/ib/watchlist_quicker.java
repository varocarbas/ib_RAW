package ib;

import java.util.ArrayList;

import accessory.arrays;
import accessory.misc;

public abstract class watchlist_quicker 
{
	public static boolean log() { return async_data_watchlist_quicker.log(); }
	
	public static void log(boolean log_) { async_data_watchlist_quicker.log(log_); }	
	
	public static boolean is_only_db() { return async_data_watchlist_quicker.is_only_db(); }
	
	public static void __only_db(boolean only_db_) { async_data_watchlist_quicker.__only_db(only_db_); }	
	
	public static boolean checks_enabled() { return async_data_watchlist_quicker.checks_enabled(); }
	
	public static void __check_enabled(boolean check_enabled_) { async_data_watchlist_quicker.__check_enabled(check_enabled_); }	

	public static boolean is_snapshot() { return async_data_watchlist_quicker.is_snapshot(); }
	
	public static void __is_snapshot(boolean is_snapshot_) { async_data_watchlist_quicker.__is_snapshot(is_snapshot_); }
	
	public static void __add(ArrayList<String> symbols_) 
	{ 
		int tot = arrays.get_size(symbols_);
		if (tot < 0) return;
		
		for (int i = 0; i < tot; i++) 
		{
			if (i > 0) misc.pause_loop();
			
			__add(symbols_.get(i)); 
		}  
	}

	public static boolean __add(String symbol_) { return async_data_watchlist_quicker.__add(symbol_); }
	
	public static void __remove(String symbol_) { __remove(symbol_, async_data_quicker.DEFAULT_STOP_REMOVE_SYMBOL); }

	public static void __remove(String symbol_, boolean remove_symbol_) { async_data_watchlist_quicker.__remove(symbol_, remove_symbol_); }

	public static void __remove_all() { __remove_all(async_data_quicker.DEFAULT_STOP_REMOVE_SYMBOL); }

	public static void __remove_all(boolean remove_symbols_) { async_data_watchlist_quicker.__remove_all(remove_symbols_); }

	public static ArrayList<String> get_all_symbols() { return async_data_watchlist_quicker.get_all_symbols(); }

	public static double __get_price(String symbol_) { return __get_price(symbol_, common.DEFAULT_WAIT_SECS_PRICE); }
	
	public static double __get_price(String symbol_, int pause_secs_) { return __get_price(symbol_, pause_secs_, true); }
	
	public static double __get_price(String symbol_, int pause_secs_, boolean from_db_too_) { return async_data_watchlist_quicker.__get_price(symbol_, pause_secs_, from_db_too_); }

	public static double get_price(String symbol_) { return async_data_watchlist_quicker.get_price(symbol_); }
	
	public static int get_data_request_min_id() { return async_data_watchlist_quicker.get_min_id(); }
	
	public static boolean __update_data_request_min_id(int min_id_) { return async_data_watchlist_quicker.__update_min_id(min_id_); }
}