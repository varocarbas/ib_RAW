package ib;

import java.util.HashMap;

import accessory_ib._alls;
import db_ib.trades;

class async_data_trades extends parent_async_data 
{
	public static final String SOURCE = trades.SOURCE;
	public static final int MAX_MINS_INACTIVE = parent_async_data.DEFAULT_MAX_MINS_INACTIVE;
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	public static async_data_trades _instance = instantiate();
	
	private static boolean _instantiated = false;
	
	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static boolean _start(String symbol_, boolean lock_) 
	{
		_instance.enable();
		
		return (_instance._start_snapshot_internal(symbol_, DATA, lock_) != WRONG_ID); 
	}
	
	public static void _stop(String symbol_, boolean lock_) { if (_instance.symbol_exists(symbol_)) _instance._stop_all_internal(symbol_, lock_); }
	
	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }

	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
	
	void tick_price_specific(int id_, int field_ib_, double price_)
	{
		if (field_ib_ != PRICE_IB) return;

		trades.update_unrealised(_get_symbol(id_, false));
	}
	
	protected HashMap<Integer, String> get_all_prices() { return _alls.TRADES_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return null; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.TRADES_GENERICS; }
	
	protected String[] get_fields() { return trades.get_fields(); }
		
	private static async_data_trades instantiate()
	{
		if (_instantiated) return _instance;
		
		_instantiated = true;
		
		return (async_data_trades)parent_async_data.instantiate(new async_data_trades(), SOURCE, ID_TRADES, MAX_MINS_INACTIVE, true, parent_async_data.DEFAULT_INCLUDES, parent_async_data.DEFAULT_INCLUDES, true, true, parent_async_data.DEFAULT_DISABLE_ASAP);		
	}
	
	private async_data_trades() { }
}