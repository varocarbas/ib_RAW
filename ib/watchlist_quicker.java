package ib;

import java.util.ArrayList;

import accessory.arrays;

public abstract class watchlist_quicker 
{
	public static boolean log() { return async_data_watchlist_quicker.log(); }
	
	public static void log(boolean log_) { async_data_watchlist_quicker.log(log_); }	

	public static void __add(ArrayList<String> symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return;
		
		for (String symbol: symbols_) { __add(symbol); }  
	}

	public static boolean __add(String symbol_) { return async_data_watchlist_quicker.__add(symbol_); }
	
	public static void __remove(String symbol_) { async_data_watchlist_quicker.__remove(symbol_); }

	public static void __remove_all() { async_data_watchlist_quicker.__remove_all(); }

	public static ArrayList<String> get_all_symbols() { return async_data_watchlist_quicker.get_all_symbols(); }
}