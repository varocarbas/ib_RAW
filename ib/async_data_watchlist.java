package ib;

import java.util.HashMap;

import accessory_ib._alls;

class async_data_watchlist extends parent_async_data 
{
	public static String _ID = "watchlist";
	
	public static final String SOURCE = watchlist.SOURCE;
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	public static async_data_watchlist _instance = instantiate();
	
	private async_data_watchlist() { }
 	
	private static async_data_watchlist instantiate()
	{
		async_data_watchlist instance = new async_data_watchlist();
		
		instance._source = SOURCE;
		instance._id = _ID;
		instance._includes_halted = true;
		instance._includes_halted_tot = true;
		
		return instance;
	}

	public static boolean _start(String symbol_, boolean lock_) 
	{ 
		if (lock_) __lock();
	
		boolean output = _instance.start(symbol_); 

		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean _stop(String symbol_, boolean lock_) 
	{ 
		if (lock_) __lock();

		int id = _instance._get_id(symbol_, false);
		
		boolean output = (id == WRONG_ID ? true : _instance.stop(id)); 

		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean __stop_snapshot(int id_) 
	{ 
		__lock();

		boolean output = (_instance.id_is_ok(id_) ? _instance.stop_id(id_, true) : true); 

		__unlock();
		
		return output;
	}
	
	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }

	protected HashMap<Integer, String> get_all_prices() { return _alls.WATCHLIST_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.WATCHLIST_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.WATCHLIST_GENERICS; }
	
	protected String[] get_fields() { return db_ib.watchlist.get_fields(); }

	private boolean id_is_ok(int id_) { return (get_id(_get_symbol(id_, false)) == id_); }

	private int get_id(String symbol_) { return _get_id(symbol_, false); }

	private boolean start(String symbol_) { return (_start_snapshot_internal(symbol_, DATA, false) != WRONG_ID); }
		
	private boolean stop(int id_) { return stop_id(id_, false); }
	
	private boolean stop_id(int id_, boolean restart_) { return _stop_snapshot_internal(id_, restart_, false); }
}