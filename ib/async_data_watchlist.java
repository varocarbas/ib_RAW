package ib;

import java.util.HashMap;

import accessory.strings;
import accessory_ib._alls;
import db_ib.watchlist;

public class async_data_watchlist extends parent_async_data 
{
	public static String _ID = "watchlist";
	
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	public static async_data_watchlist _instance = instantiate();
	
	private async_data_watchlist() { }
 	
	private static async_data_watchlist instantiate()
	{
		async_data_watchlist instance = new async_data_watchlist();
		
		instance._source = watchlist.SOURCE;
		instance._id = _ID;
		
		return instance;
	}

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }
	
	public void __add(String symbol_) { __add_internal(symbol_); }
	
	public static void __stop_all() { _instance.__stop_all_internal(); }
	
	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE_IB, watchlist.PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME_IB, watchlist.VOLUME);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED_IB, watchlist.HALTED);
		
		return all;
	}
	
	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }

	protected HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_WATCHLIST_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_WATCHLIST_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_WATCHLIST_GENERICS; }
	
	private void __add_internal(String symbol_) 
	{
		__lock();
		
		String symbol = normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) 
		{
			__unlock();
			
			return;
		}
	
		_start_snapshot_internal(symbol_, DATA, false);
		
		__unlock();
	}
}