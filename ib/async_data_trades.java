package ib;

import java.util.ArrayList;

import accessory.strings;
import db_ib.trades;

abstract class async_data_trades 
{
	public static final String _APP = "trades";

	public static final String SOURCE = trades.SOURCE;
	public static final int MAX_MINS_INACTIVE = async_data.DEFAULT_MAX_MINS_INACTIVE;
	
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 25;
	public static final int SIZE_GLOBALS = MAX_SIMULTANEOUS_SYMBOLS;
	public static final int MAX_I = SIZE_GLOBALS - 1;
	
	public static final String DEFAULT_TYPE = async_data.TYPE_SNAPSHOT;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;

	public static volatile String[] _stopping = new String[SIZE_GLOBALS];

	public static volatile int _last_i_stopping = -1;
	public static volatile boolean _enabled = async_data.DEFAULT_ENABLED;
	public static volatile boolean _is_quick = async_data.DEFAULT_IS_QUICK;
	public static volatile boolean _logs_to_screen = async_data.DEFAULT_LOGS_TO_SCREEN;
	public static volatile int _pause_nonstop = async_data.DEFAULT_PAUSE_NONSTOP;

	public static ArrayList<Integer> _fields = new ArrayList<Integer>();

	public static boolean _includes_time = false;
	public static boolean _includes_time_elapsed = true;
	public static boolean _includes_halted = true;
	public static boolean _includes_halted_tot = false;
	public static boolean _disable_asap = true;
	
	public static boolean is_ok() { return _enabled; }
	
	public static boolean logs_to_screen() { return _logs_to_screen; }

	public static void logs_to_screen(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }

	public static void stop_all() { async_data.stop_all(_APP, false); }
	
	public static void tick_price(int id_, int field_ib_, double price_) { async_data.tick_price(_APP, id_, field_ib_, price_); }
	
	public static void tick_size(int id_, int field_ib_, int size_) { async_data.tick_size(_APP, id_, field_ib_, size_); }
	
	public static void tick_generic(int id_, int tick_, double value_) { async_data.tick_generic(_APP, id_, tick_, value_); }

	public static void end_snapshot(int id_) { async_data.end_snapshot(_APP, id_); }

	public static void tick_price_specific(int id_, int field_ib_, double price_, String symbol_)
	{
		if (field_ib_ != async_data.PRICE_IB) return;

		trades.update_unrealised(symbol_);
	}
	
	public static void populate_fields()
	{
		if (_fields.size() > 0) return;
		
		_fields.add(async_data.PRICE_IB);
		_fields.add(async_data.HALTED_IB);
	}
	
	public static ArrayList<String> get_active_symbols() { return async_data.get_active_symbols(_APP); }
	
	public static boolean start(String symbol_) { return start(symbol_, DEFAULT_TYPE, DEFAULT_DATA_TYPE); }
	
	public static void stop(String symbol_) 
	{
		String symbol = common.normalise_symbol(symbol_);
		
		if (strings.is_ok(symbol) && async_data.symbol_is_running(_APP, symbol)) async_data.stop(_APP, symbol, false);
	}
	
	private static boolean start(String symbol_, String type_, int data_type_) 
	{
		boolean started = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (async_data.symbol_is_running(_APP, symbol)) return started;

		return async_data.start_common(_APP, symbol, type_, data_type_);
	}
}