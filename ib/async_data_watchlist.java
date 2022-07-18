package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.strings;
import accessory_ib._alls;

class async_data_watchlist extends parent_async_data 
{
	public static String _ID = "watchlist";
	
	public static final String SOURCE = watchlist.SOURCE;
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	private volatile HashMap<String, ArrayList<Double>> _flu = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flu2_minus = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flu2_plus = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flu2 = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, Boolean> _flu2_remove_plus = new HashMap<String, Boolean>();
	
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
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
	
		if (lock_) __lock();
		
		output = _instance.start(symbol);
		if (output) _instance.add_global(symbol);
		
		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean _stop(String symbol_, boolean lock_) 
	{ 	
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
	
		if (lock_) __lock();
		
		output = true;
		
		int id = _instance._get_id(symbol, false);

		if (id != WRONG_ID)
		{
			output = _instance.stop(id);
			if (output) _instance.remove_global(symbol);
		}
		
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

	void tick_price_watchlist(int id_, int field_ib_, double price_)
	{
		if (field_ib_ != PRICE_IB) return;
		
		//String symbol = _get_symbol(id_, false);
		
		//HashMap<String, String> db = db_ib.watchlist.get_vals(symbol, _is_quick);

	}
	
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

	private void add_global(String symbol_)
	{
		_flu.put(symbol_, new ArrayList<Double>());
		_flu2_minus.put(symbol_, new ArrayList<Double>());
		_flu2_plus.put(symbol_, new ArrayList<Double>());
		_flu2.put(symbol_, new ArrayList<Double>());
		_flu2_remove_plus.put(symbol_, true);		
	}

	private void remove_global(String symbol_)
	{
		if (!_flu.containsKey(symbol_)) return;
		
		_flu.remove(symbol_);
		_flu2_minus.remove(symbol_);
		_flu2_plus.remove(symbol_);
		_flu2.remove(symbol_);
		_flu2_remove_plus.remove(symbol_);		
	}
}