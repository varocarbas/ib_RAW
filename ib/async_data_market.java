package ib;

import java.util.HashMap;

import accessory_ib._alls;
import db_ib.market;

public class async_data_market extends parent_async_data 
{	
	public static String _ID = "market";
	
	public static async_data_market _instance = instantiate();
	
	private async_data_market() { }
 	
	private static async_data_market instantiate()
	{
		async_data_market instance = new async_data_market();
		
		instance._source = market.SOURCE;
		instance._id = _ID;
		
		return instance;
	}

	public static void update_nonstop_pause(int nonstop_pause_) { _instance.update_nonstop_pause_internal(nonstop_pause_); }

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static void restart_after_stop_all(int pause_secs_) { _instance.restart_after_stop_all_internal(pause_secs_); }
	
	public static void __stop_all() { _instance.__stop_all_internal(); }
	
	public static int __start_snapshot(String symbol_) { return _instance.__start_snapshot_internal(symbol_, DEFAULT_DATA_TYPE); }

	public static int __start_snapshot(String symbol_, int data_type_) { return _instance.__start_snapshot_internal(symbol_, data_type_); }

	public static int __start_stream(String symbol_) { return _instance.__start_stream_internal(symbol_, DEFAULT_DATA_TYPE); }

	public static int __start_stream(String symbol_, int data_type_) { return _instance.__start_stream_internal(symbol_, data_type_); }

	public static boolean __stop_snapshot(String symbol_) { return _instance.__stop_snapshot_internal(symbol_); }

	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }
	
	public static boolean __stop_stream(String symbol_) { return _instance.__stop_stream_internal(symbol_); }

	public static boolean __stop_stream(int id_) { return _instance.__stop_stream_internal(id_); }
	
	public static String __get_symbol(int id_) { return _instance._get_symbol(id_, true); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE_IB, market.PRICE);
		all.put(OPEN_IB, market.OPEN);
		all.put(CLOSE_IB, market.CLOSE);
		all.put(LOW_IB, market.LOW);
		all.put(HIGH_IB, market.HIGH);
		all.put(ASK_IB, market.ASK);
		all.put(BID_IB, market.BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME_IB, market.VOLUME);
		all.put(SIZE_IB, market.SIZE);
		all.put(ASK_SIZE_IB, market.ASK_SIZE);
		all.put(BID_SIZE_IB, market.BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED_IB, market.HALTED);
		
		return all;
	}
	
	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
		
	protected HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_MARKET_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_MARKET_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_MARKET_GENERICS; }
}