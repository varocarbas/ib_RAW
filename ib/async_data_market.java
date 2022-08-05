package ib;

import java.util.HashMap;

import accessory_ib._alls;

class async_data_market extends parent_async_data 
{	
	public static final String SOURCE = market.SOURCE;
	public static final int MAX_MINS_INACTIVE = 30;
	
	public static async_data_market _instance = instantiate();
	
	private static boolean _instantiated = false;
	
	public static int __start_snapshot(String symbol_) 
	{ 
		_instance.enable();
		
		return _instance.__start_snapshot_internal(symbol_, parent_async_data.DEFAULT_DATA_TYPE); 
	}

	public static int __start_snapshot(String symbol_, int data_type_) 
	{ 
		_instance.enable();
		
		return _instance.__start_snapshot_internal(symbol_, data_type_); 
	}

	public static int __start_stream(String symbol_) 
	{ 
		_instance.enable();
		
		return _instance.__start_stream_internal(symbol_, parent_async_data.DEFAULT_DATA_TYPE); 
	}

	public static int __start_stream(String symbol_, int data_type_) 
	{
		_instance.enable();
		
		return _instance.__start_stream_internal(symbol_, data_type_); 
	}

	public static boolean __stop_snapshot(String symbol_) { return _instance.__stop_snapshot_internal(symbol_); }

	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }
	
	public static boolean __stop_stream(String symbol_) { return _instance.__stop_stream_internal(symbol_); }

	public static boolean __stop_stream(int id_) { return _instance.__stop_stream_internal(id_); }
	
	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
		
	protected HashMap<Integer, String> get_all_prices() { return _alls.MARKET_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.MARKET_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.MARKET_GENERICS; }
	
	protected String[] get_fields() { return db_ib.market.get_fields(); }

	private static async_data_market instantiate()
	{
		if (_instantiated) return _instance;
		
		_instantiated = true;
		
		return (async_data_market)parent_async_data.instantiate(new async_data_market(), SOURCE, ID_MARKET, MAX_MINS_INACTIVE, true, true, true, parent_async_data.DEFAULT_INCLUDES, false, false);		
	}
	
	private async_data_market() { }
}