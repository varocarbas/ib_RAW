package ib;

import java.util.HashMap;

import accessory_ib._alls;
import db_ib.trades;

class async_data_trades extends parent_async_data 
{
	public static String _ID = "trades";
	
	public static final String SOURCE = trades.SOURCE;
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	public static async_data_trades _instance = instantiate();
	
	private async_data_trades() { }
 	
	private static async_data_trades instantiate()
	{
		async_data_trades instance = new async_data_trades();
		
		instance._source = SOURCE;
		instance._id = _ID;
		instance._includes_halted = true;
		instance._includes_time_elapsed = true;
		instance._enabled = false;
		
		return instance;
	}

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static boolean _start(String symbol_, boolean lock_) { return (_instance._start_snapshot_internal(symbol_, DATA, lock_) != WRONG_ID); }
	
	public static void _stop(String symbol_, boolean lock_) { if (_instance.symbol_exists(symbol_)) _instance._stop_all_internal(symbol_, lock_); }
	
	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }

	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
	
	static void tick_price_specific(int id_, int field_ib_, double price_)
	{
		if (field_ib_ != PRICE_IB) return;

		trades.update_unrealised(_instance._get_symbol(id_, false));
	}
	
	protected HashMap<Integer, String> get_all_prices() { return _alls.TRADES_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return null; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.TRADES_GENERICS; }
	
	protected String[] get_fields() { return trades.get_fields(); }
}