package ib;

import java.util.ArrayList;
import java.util.HashMap;

abstract class async_data_market_quicker 
{
	public static final String _APP = "market";
	
	public static final String SOURCE = db_ib.market.SOURCE;

	public static final int MAX_SIMULTANEOUS_SYMBOLS = 250;

	static final int SIZE_GLOBALS = 500;
	static final int MAX_ID = SIZE_GLOBALS - 1;
	static final boolean INCLUDES_TIME = true;
	static final boolean INCLUDES_TIME_ELAPSED = false;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = false;
	static final boolean ONLY_ESSENTIAL = false;
	static final boolean ONLY_DB = false;

	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	static volatile HashMap<String, String>[] _vals = (HashMap<String, String>[])new HashMap[SIZE_GLOBALS];

	static volatile int _last_id = -1;

	static int[] _fields = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.OPEN_IB, async_data_quicker.CLOSE_IB, async_data_quicker.LOW_IB, async_data_quicker.HIGH_IB, async_data_quicker.ASK_IB, async_data_quicker.BID_IB, async_data_quicker.VOLUME_IB, async_data_quicker.SIZE_IB, async_data_quicker.ASK_SIZE_IB, async_data_quicker.BID_SIZE_IB, async_data_quicker.HALTED_IB };

	private static boolean _log = async_data_quicker.DEFAULT_LOG;
		
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	
	
	public static boolean __start(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __stop(String symbol_) { async_data_quicker.__stop(_APP, symbol_); }

	public static void __stop_all() { async_data_quicker.__stop_all(_APP, _symbols); }

	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static void tick_price(int id_, int field_ib_, double price_, String symbol_) { }

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_) { }
	
	public static void start_globals(String symbol_, boolean is_restart_) { }
	
	public static void stop_globals(String symbol_, boolean remove_symbol_) { }
}