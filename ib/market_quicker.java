package ib;

import java.util.ArrayList;

import accessory.arrays;

public abstract class market_quicker 
{
	public static boolean log() { return async_data_market_quicker.log(); }
	
	public static void log(boolean log_) { async_data_market_quicker.log(log_); }	

	public static void __start(ArrayList<String> symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return;
		
		for (String symbol: symbols_) { __start(symbol); }  
	}

	public static boolean __start(String symbol_) { return async_data_market_quicker.__start(symbol_); }
	
	public static void __stop(String symbol_) { async_data_market_quicker.__stop(symbol_); }

	public static void __stop_all() { async_data_market_quicker.__stop_all(); }

	public static ArrayList<String> get_all_symbols() { return async_data_market_quicker.get_all_symbols(); }
}