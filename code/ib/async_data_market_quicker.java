package ib;

import java.util.ArrayList;

import accessory.parent_static;

abstract class async_data_market_quicker extends parent_static
{
	public static final String _APP = "market";
	
	public static final String SOURCE = db_ib.market.SOURCE;
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 5000;
	
	static final int SIZE_GLOBALS = 2500;
	static final boolean INCLUDES_TIME = true;
	static final boolean INCLUDES_TIME_ELAPSED = false;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = true;
	static final boolean RECENTLY_HALTED_ENABLED = false;
	static final boolean IS_SNAPSHOT = true;
	
	static final boolean DEFAULT_ONLY_DB = true;
	
	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	static volatile double[][] _vals = new double[SIZE_GLOBALS][];
	static volatile int[] _fields_ib = null;
	
	static int _min_id = async_data_quicker.WRONG_ID;
	static int _max_id = async_data_quicker.WRONG_ID;
	
	static volatile int _last_id = get_min_id() - 1;
	static volatile boolean _only_db = DEFAULT_ONLY_DB;
	static volatile boolean _check_enabled = true;
	static volatile boolean _only_essential = false;
	static volatile boolean _only_halts = false;
	
	private static final int DEFAULT_MIN_ID = 2013;
	
	private static boolean _log = async_data_quicker.DEFAULT_LOG;
	
	public static int get_max_id() 
	{ 
		if (_max_id <= async_data_quicker.WRONG_ID) _max_id = async_data_quicker.get_max_id(get_min_id(), SIZE_GLOBALS);
	
		return _max_id; 
	}
	
	public static int get_min_id() 
	{ 
		if (_min_id <= async_data_quicker.WRONG_ID) _min_id = DEFAULT_MIN_ID;
		
		return _min_id; 
	}
	
	public static boolean __update_min_id(int min_id_) { return async_data_apps_quicker.__update_min_id(_APP, min_id_); }

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
	
	public static boolean is_only_halts() { return _only_halts; }
	
	public static void __only_halts(boolean only_halts_) 
	{ 
		__lock();
		
		_only_halts = only_halts_; 

		__unlock();
	}
	
	public static boolean __start(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __stop(String symbol_) { __stop(symbol_, async_data_quicker.DEFAULT_STOP_REMOVE_SYMBOL); }
	
	public static void __stop(String symbol_, boolean remove_symbol_) { async_data_quicker.__stop(_APP, symbol_, remove_symbol_); }

	public static void __stop_all() { __stop_all(async_data_quicker.DEFAULT_STOP_REMOVE_SYMBOL); }
	
	public static void __stop_all(boolean remove_symbols_) { async_data_quicker.__stop_all(_APP, _symbols, remove_symbols_); }

	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static boolean tick_price(int id_, int field_ib_, double price_, String symbol_) { return true; }

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_) { }
	
	public static void start(String symbol_, boolean is_restart_) { }
	
	public static void stop(String symbol_, boolean remove_symbol_) { }
}