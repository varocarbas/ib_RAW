package ib;

import java.util.ArrayList;

import accessory.parent_static;

abstract class async_data_market_quicker extends parent_static
{
	public static final String _APP = "market";
	
	public static final String SOURCE = db_ib.market.SOURCE;
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 5000;
	
	static final int SIZE_GLOBALS = 2000;
	static final int MAX_ID = SIZE_GLOBALS + async_data_quicker.MIN_ID - 1;	
	static final boolean INCLUDES_TIME = true;
	static final boolean INCLUDES_TIME_ELAPSED = false;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = true;

	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	static volatile double[][] _vals = new double[SIZE_GLOBALS][];
	static volatile int[] _fields_ib = null;
	
	static volatile int _last_id = async_data_quicker.MIN_ID;
	static volatile boolean _only_db = false;
	static volatile boolean _check_enabled = true;
	static volatile boolean _only_essential = false;

	private static boolean _log = async_data_quicker.DEFAULT_LOG;
		
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	
	
	public static boolean is_only_db() { return _only_db; }
	
	public static void __only_db(boolean only_db_) 
	{
		__lock();
		
		_only_db = only_db_; 
	
		__unlock();
	}	
	
	public static boolean checks_enabled() { return _check_enabled; }
	
	public static void __check_enabled(boolean check_enabled_) 
	{ 
		__lock();
		
		_check_enabled = check_enabled_; 
	
		__unlock();
	}	
	
	public static boolean is_only_essential() { return _only_essential; }
	
	public static void __only_essential(boolean only_essential_) 
	{ 
		__lock();
		
		_only_essential = only_essential_; 
		
		async_data_apps_quicker.update_app(_APP);
		
		async_data_apps_quicker._populate_fields_cols_cache(false, true);

		__unlock();
	}	
	
	public static boolean __start(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __stop(String symbol_) { async_data_quicker.__stop(_APP, symbol_); }

	public static void __stop_all() { async_data_quicker.__stop_all(_APP, _symbols); }

	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static void tick_price(int id_, int field_ib_, double price_, String symbol_) { }

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_) { }
	
	public static void start(String symbol_, boolean is_restart_) { }
	
	public static void stop(String symbol_, boolean remove_symbol_) { }
}