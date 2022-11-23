package ib;

import java.util.ArrayList;
import java.util.HashMap;

import db_ib.trades;

abstract class async_data_trades_quicker 
{
	public static final String _APP = "trades";
	
	public static final String SOURCE = db_ib.trades.SOURCE;
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 5;

	static final int[] FIELDS = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.HALTED_IB };

	static final int SIZE_GLOBALS = 350;
	static final int MAX_ID = SIZE_GLOBALS + async_data_quicker.MIN_ID - 1;	
	static final boolean INCLUDES_TIME = false;
	static final boolean INCLUDES_TIME_ELAPSED = true;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = false;

	static volatile String[] _symbols = new String[SIZE_GLOBALS];	
	@SuppressWarnings("unchecked")
	static volatile HashMap<String, String>[] _vals = (HashMap<String, String>[])new HashMap[SIZE_GLOBALS];

	static volatile int _last_id = async_data_quicker.MIN_ID;
	static volatile boolean _only_db = false;
	static volatile boolean _check_enabled = false;
	static volatile boolean _only_essential = true;

	private static boolean _log = async_data_quicker.DEFAULT_LOG;
	
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	
	
	public static boolean only_db() { return _only_db; }
	
	public static void only_db(boolean only_db_) { _only_db = only_db_; }	
	
	public static boolean check_enabled() { return _check_enabled; }
	
	public static void check_enabled(boolean check_enabled_) { _check_enabled = check_enabled_; }	
	
	public static boolean only_essential() { return _only_essential; }
	
	public static void only_essential(boolean only_essential_) { _only_essential = only_essential_; }	

	public static boolean __start(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __stop(String symbol_) { async_data_quicker.__stop(_APP, symbol_); }
	
	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static void tick_price(int id_, int field_ib_, double price_, String symbol_) { trades.update_unrealised(symbol_); }

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_) { }
	
	public static void start(String symbol_, boolean is_restart_) { }
	
	public static void stop(String symbol_, boolean remove_symbol_) { }	
}