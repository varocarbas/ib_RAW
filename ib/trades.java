package ib;

import java.util.ArrayList;

import accessory.db_common;
import accessory.parent_static;
import accessory.strings;
import db_ib.async_data;

public abstract class trades extends parent_static
{	
	public static final String DB_SOURCE = db_ib.trades.SOURCE;

	public static final String DB_ORDER_ID_MAIN = db_ib.trades.ORDER_ID_MAIN;
	public static final String DB_ORDER_ID_SEC = db_ib.trades.ORDER_ID_SEC;
	public static final String DB_SYMBOL = db_ib.trades.SYMBOL;
	public static final String DB_PRICE = db_ib.trades.PRICE;
	public static final String DB_TIME_ELAPSED = db_ib.trades.TIME_ELAPSED;
	public static final String DB_ELAPSED_INI = db_ib.trades.ELAPSED_INI;
	public static final String DB_START = db_ib.trades.START;
	public static final String DB_STOP = db_ib.trades.STOP;
	public static final String DB_UNREALISED = db_ib.trades.UNREALISED;	
	public static final String DB_IS_ACTIVE = db_ib.trades.IS_ACTIVE;
	public static final String DB_INVESTMENT = db_ib.trades.INVESTMENT;
	public static final String DB_END = db_ib.trades.END;
	public static final String DB_REALISED = db_ib.trades.REALISED;
	
	public static final boolean DEFAULT_SYNCED_WITH_EXECS = true;
	
	private static boolean _synced_with_execs = DEFAULT_SYNCED_WITH_EXECS; 

	public static boolean is_quick() { return db_common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(DB_SOURCE, is_quick_); }

	public static boolean logs_to_screen() { return async_data_trades.logs_to_screen(); }

	public static void logs_to_screen(boolean logs_to_screen_) { async_data_trades.logs_to_screen(logs_to_screen_); }
	
	public static boolean synced_with_execs() { return _synced_with_execs; }
	
	public static void synced_with_execs(boolean synced_with_execs_) 
	{ 
		_synced_with_execs = synced_with_execs_; 
	
		if (synced_with_execs_) execs.enabled(true, false);
	}

	public static int max_simultaneous_symbols() { return async_data_trades.MAX_SIMULTANEOUS_SYMBOLS; }
	
	public static void start(String symbol_, int order_id_, double start_) { async_trades.start(symbol_, order_id_, start_); }
	
	public static void end(String symbol_, int order_id_, double end_) { async_trades.end(symbol_, order_id_, end_); }

	public static boolean start_data(String symbol_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return false;

		return async_data_trades.start(symbol);
	}

	public static boolean stop_data(String symbol_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return false;

		async_data_trades.stop(symbol);
	
		return true;
	}
	
	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? async_data.get_price(DB_SOURCE, symbol) : common.WRONG_PRICE); 
	}

	public static double get_start(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.get_start(symbol) : common.WRONG_PRICE); 
	}

	public static double get_end(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.get_end(symbol) : common.WRONG_PRICE); 
	}

	public static boolean is_trading(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.trades.exists(symbol) : false); 
	}

	public static ArrayList<String> get_active_symbols() { return async_data_trades.get_active_symbols(); }

	static void enabled(boolean enabled_) { async_data_trades._enabled = enabled_; }
	
	static boolean exists(int order_id_main_) { return db_ib.trades.order_id_exists(order_id_main_, true); }
}