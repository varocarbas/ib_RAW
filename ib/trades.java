package ib;

import java.util.ArrayList;

import accessory.parent_static;
import accessory.strings;
import db_ib.async_data;

public abstract class trades extends parent_static
{
	public static final boolean DEFAULT_SYNCED_WITH_EXECS = true;
	
	private static boolean _synced_with_execs = DEFAULT_SYNCED_WITH_EXECS; 
	
	public static boolean logs_to_screen() { return async_data_trades.logs_to_screen(); }

	public static void logs_to_screen(boolean logs_to_screen_) { async_data_trades.logs_to_screen(logs_to_screen_); }
	
	public static boolean synced_with_execs() { return _synced_with_execs; }
	
	public static void synced_with_execs(boolean synced_with_execs_) 
	{ 
		_synced_with_execs = synced_with_execs_; 
	
		if (synced_with_execs_) execs.enabled(is_ok(), false);
		else execs.disable(false);
	}
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_trades.logs_to_screen(logs_to_screen_); }
	
	public static void __order_status(int order_id_, String status_ib_) 
	{ 
		if (!synced_with_execs()) return;
		
		async_trades.__start(order_id_, status_ib_);
	}
	
	public static void __start(String symbol_, int order_id_, double start_) { async_trades.__start(symbol_, order_id_, start_); }
	
	public static void __end(String symbol_, int order_id_, double end_) { async_trades.__end(symbol_, order_id_, end_); }

	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? async_data.get_price(db_ib.trades.SOURCE, symbol) : common.WRONG_PRICE); 
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