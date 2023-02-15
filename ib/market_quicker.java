package ib;

import java.util.ArrayList;

import accessory.arrays;
import accessory.misc;

public abstract class market_quicker 
{
	public static final double MAX_VAR = market.MAX_VAR;
	
	public static boolean log() { return async_data_market_quicker.log(); }
	
	public static void log(boolean log_) { async_data_market_quicker.log(log_); }	
	
	public static boolean is_only_db() { return async_data_market_quicker.is_only_db(); }
	
	public static void __only_db(boolean only_db_) { async_data_market_quicker.__only_db(only_db_); }	
	
	public static boolean checks_enabled() { return async_data_market_quicker.checks_enabled(); }
	
	public static void __check_enabled(boolean check_enabled_) { async_data_market_quicker.__check_enabled(check_enabled_); }	
	
	public static boolean is_only_essential() { return async_data_market_quicker.is_only_essential(); }
	
	public static void __only_essential(boolean only_essential_) { async_data_market_quicker.__only_essential(only_essential_); }	

	public static void __start(ArrayList<String> symbols_) 
	{ 
		int tot = arrays.get_size(symbols_);
		if (tot < 0) return;
		
		for (int i = 0; i < tot; i++) 
		{
			if (i > 0) misc.pause_loop();
			
			__start(symbols_.get(i)); 
		}  
	}

	public static boolean __start(String symbol_) { return async_data_market_quicker.__start(symbol_); }
	
	public static void __stop(String symbol_) { async_data_market_quicker.__stop(symbol_); }

	public static void __stop_all() { async_data_market_quicker.__stop_all(); }

	public static ArrayList<String> get_all_symbols() { return async_data_market_quicker.get_all_symbols(); }
}