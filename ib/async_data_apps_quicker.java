package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;

abstract class async_data_apps_quicker extends parent_static
{
	private static String _app = strings.DEFAULT;
	private static String _source = strings.DEFAULT;

	public static void update_app(String app_)
	{
		_app = app_;
		_source = strings.DEFAULT;
		
		if (app_.equals(async_data_watchlist_quicker._APP)) _source = async_data_watchlist_quicker.SOURCE; 
		else if (app_.equals(async_data_trades_quicker._APP)) _source = async_data_trades_quicker.SOURCE; 
		else if (app_.equals(async_data_market_quicker._APP)) _source = async_data_market_quicker.SOURCE; 
	}

	public static String get_app() { return _app; }

	public static String get_source() { return _source; }
	
	public static void __tick_price(int id_, int field_ib_, double price_, String symbol_)
	{
		if (_app.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker.__tick_price(id_, field_ib_, price_, symbol_);
		else if (_app.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_price(id_, field_ib_, price_, symbol_);
		else if (_app.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_price(id_, field_ib_, price_, symbol_);
	}

	public static void _tick_size(int id_, int field_ib_, double size_, String symbol_)
	{
		if (_app.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._tick_size(id_, field_ib_, size_, symbol_);
		else if (_app.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_size(id_, field_ib_, size_, symbol_);
		else if (_app.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_size(id_, field_ib_, size_, symbol_);
	}

	public static boolean id_is_ok(int id_) { return (id_ > async_data_quicker.WRONG_ID && id_ <= get_max_id()); }
	
	public static boolean field_is_ok(int field_ib_) 
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = arrays.value_exists(async_data_watchlist_quicker._fields, field_ib_);
		else if (_app.equals(async_data_trades_quicker._APP)) output = arrays.value_exists(async_data_trades_quicker._fields, field_ib_);
		else if (_app.equals(async_data_market_quicker._APP)) output = arrays.value_exists(async_data_market_quicker._fields, field_ib_);
		
		return output;
	}

	public static String __get_symbol(int id_)
	{
		__lock();

		String symbol = (id_is_ok(id_) ? get_symbols()[async_data_quicker.get_i(id_, true)] : null);
		
		__unlock();
		
		return symbol;
	}
	
	public static boolean includes_time()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_TIME;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_TIME;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_TIME;

		return output;
	}

	public static boolean includes_time_elapsed()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_TIME_ELAPSED;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_TIME_ELAPSED;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_TIME_ELAPSED;

		return output;
	}

	public static boolean includes_halted()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_HALTED;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_HALTED;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_HALTED;

		return output;
	}

	public static boolean includes_halted_tot()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_HALTED_TOT;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_HALTED_TOT;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_HALTED_TOT;

		return output;
	}

	public static boolean __only_essential()
	{
		__lock();
		
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._only_essential;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._only_essential;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._only_essential;

		__unlock();		
		
		return output;
	}

	public static boolean __only_db() { return _only_db(true); }

	public static boolean __check_enabled()
	{
		__lock();
		
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.check_enabled();
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.check_enabled();
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.check_enabled();

		__unlock();

		return output;
	}
	
	public static void start_globals(String symbol_, int id_)
	{
		boolean is_restart = id_is_ok(async_data_quicker._get_id(symbol_, false));
		boolean start_vals = !_only_db(false);

		int i = async_data_quicker.get_i(id_, true);
		
		if (_app.equals(async_data_watchlist_quicker._APP)) 
		{
			async_data_watchlist_quicker._last_id = id_;

			async_data_watchlist_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_watchlist_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_watchlist_quicker.start_globals(symbol_, is_restart);
		}
		else if (_app.equals(async_data_trades_quicker._APP)) 
		{
			async_data_trades_quicker._last_id = id_;

			async_data_trades_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_trades_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_trades_quicker.start_globals(symbol_, is_restart);
		}
		else if (_app.equals(async_data_market_quicker._APP)) 
		{
			async_data_market_quicker._last_id = id_;

			async_data_market_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_market_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_market_quicker.start_globals(symbol_, is_restart);
		}		
	}

	public static void __stop_globals(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_)
	{
		__lock();

		int id = id_;
		
		boolean id_is_ok = id_is_ok(id);
		boolean symbol_ok = strings.is_ok(symbol_);

		if (!id_is_ok && symbol_ok) 
		{
			id = async_data_quicker._get_id(symbol_, false);
			
			id_is_ok = id_is_ok(id);
		}

		boolean delete_globals = (id_is_ok && (snapshot_completed_ || remove_symbol_));		
		boolean delete_vals = !_only_db(false);

		int i = async_data_quicker.get_i(id, true);
		
		if (_app.equals(async_data_watchlist_quicker._APP)) 
		{
			if (delete_globals) 
			{
				async_data_watchlist_quicker._symbols[i] = null;				
				if (delete_vals) async_data_watchlist_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_watchlist_quicker.stop_globals(symbol_, remove_symbol_);
		}
		else if (_app.equals(async_data_trades_quicker._APP)) 
		{	
			if (delete_globals) 
			{
				async_data_trades_quicker._symbols[i] = null;				
				if (delete_vals) async_data_trades_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_trades_quicker.stop_globals(symbol_, remove_symbol_);
		}
		else if (_app.equals(async_data_market_quicker._APP)) 
		{	
			if (delete_globals) 
			{
				async_data_market_quicker._symbols[i] = null;				
				if (delete_vals) async_data_market_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_market_quicker.stop_globals(symbol_, remove_symbol_);
		}
		
		__unlock();
	}
	
	public static void __update_vals(int id_, HashMap<String, String> vals_)
	{
		if (!id_is_ok(id_)) return;
		
		__lock();
		
		for (Entry<String, String> item: vals_.entrySet()) { _update_vals(id_, item.getKey(), item.getValue(), false); }
		
		__unlock();		
	}

	public static void __update_vals(int id_, String col_, double val_) { _update_vals(id_, col_, Double.toString(val_), true); }

	public static HashMap<String, String> __get_vals(int id_)
	{
		HashMap<String, String> vals = null;
		if (!id_is_ok(id_)) return vals;
			
		__lock();

		int i = async_data_quicker.get_i(id_, true);
		
		if (_app.equals(async_data_watchlist_quicker._APP) && (async_data_watchlist_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_watchlist_quicker._vals[i]);
		else if (_app.equals(async_data_trades_quicker._APP) && (async_data_trades_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_trades_quicker._vals[i]);
		else if (_app.equals(async_data_market_quicker._APP) && (async_data_market_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_market_quicker._vals[i]);
		
		__unlock();
		
		return vals;
	}
	
	public static String[] get_symbols()
	{
		String[] output = null;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = (String[])arrays.get_new(async_data_watchlist_quicker._symbols); 	
		else if (_app.equals(async_data_trades_quicker._APP)) output = (String[])arrays.get_new(async_data_trades_quicker._symbols); 	
		else if (_app.equals(async_data_market_quicker._APP)) output = (String[])arrays.get_new(async_data_market_quicker._symbols); 	
		
		return output;
	}

	public static int get_max_id()
	{
		int output = 0;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.MAX_ID; 	
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.MAX_ID; 	
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.MAX_ID; 	
	
		return output;
	}

	public static int get_last_id()
	{
		int output = 0;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._last_id; 	
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._last_id; 	
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._last_id; 	
	
		return output;
	}

	public static boolean log()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.log();
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.log();
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.log();
		
		return output;
	}
	
	private static boolean _only_db(boolean lock_)
	{
		if (lock_) __lock();
		
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.only_db();
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.only_db();
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.only_db();

		if (lock_) __unlock();
		
		return output;
	}

	private static void _update_vals(int id_, String col_, String val_, boolean lock_)
	{
		if (!id_is_ok(id_)) return;
		
		if (lock_) __lock();
		
		int i = async_data_quicker.get_i(id_, true);
		
		if (_app.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._vals[i].put(col_, val_);
		else if (_app.equals(async_data_trades_quicker._APP)) async_data_trades_quicker._vals[i].put(col_, val_);
		else if (_app.equals(async_data_market_quicker._APP)) async_data_market_quicker._vals[i].put(col_, val_); 
		
		if (lock_) __unlock();		
	}
}