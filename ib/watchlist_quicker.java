package ib;

import java.util.ArrayList;

import accessory.arrays;
import accessory.misc;

public abstract class watchlist_quicker 
{
	public static boolean log() { return async_data_watchlist_quicker.log(); }
	
	public static void log(boolean log_) { async_data_watchlist_quicker.log(log_); }	
	
	public static boolean only_db() { return async_data_watchlist_quicker.only_db(); }
	
	public static void only_db(boolean only_db_) { async_data_watchlist_quicker.only_db(only_db_); }	
	
	public static boolean check_enabled() { return async_data_watchlist_quicker.check_enabled(); }
	
	public static void check_enabled(boolean check_enabled_) { async_data_watchlist_quicker.check_enabled(check_enabled_); }	
	
	public static boolean only_essential() { return async_data_watchlist_quicker.only_essential(); }
	
	public static void only_essential(boolean only_essential_) { async_data_watchlist_quicker.only_essential(only_essential_); }	

	public static void __add(ArrayList<String> symbols_) 
	{ 
		int size = arrays.get_size(symbols_);
		if (size < 0) return;
		
		for (int i = 0; i < size; i++) 
		{
			if (i > 0) misc.pause_loop();
			
			__add(symbols_.get(i)); 
		}  
	}

	public static boolean __add(String symbol_) { return async_data_watchlist_quicker.__add(symbol_); }
	
	public static void __remove(String symbol_) { async_data_watchlist_quicker.__remove(symbol_); }

	public static void __remove_all() { async_data_watchlist_quicker.__remove_all(); }

	public static ArrayList<String> get_all_symbols() { return async_data_watchlist_quicker.get_all_symbols(); }
}