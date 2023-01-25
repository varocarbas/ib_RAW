package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;

abstract class async_data_apps_quicker extends parent_static
{
	private static String APP = strings.DEFAULT;
	private static String SOURCE = strings.DEFAULT;

	public static void update_app(String app_)
	{
		APP = app_;
		SOURCE = strings.DEFAULT;
		
		if (app_.equals(async_data_watchlist_quicker._APP)) SOURCE = async_data_watchlist_quicker.SOURCE; 
		else if (app_.equals(async_data_trades_quicker._APP)) SOURCE = async_data_trades_quicker.SOURCE; 
		else if (app_.equals(async_data_market_quicker._APP)) SOURCE = async_data_market_quicker.SOURCE; 
	}

	public static String get_app() { return APP; }

	public static String get_source() { return SOURCE; }
	
	public static void _tick_price(int id_, int field_ib_, double price_, String symbol_)
	{
		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._tick_price(id_, field_ib_, price_, symbol_);
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_price(id_, field_ib_, price_, symbol_);
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_price(id_, field_ib_, price_, symbol_);
	}

	public static void _tick_size(int id_, int field_ib_, double size_, String symbol_)
	{
		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._tick_size(id_, field_ib_, size_, symbol_);
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_size(id_, field_ib_, size_, symbol_);
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_size(id_, field_ib_, size_, symbol_);
	}

	public static boolean id_is_ok(int id_) { return (id_ > async_data_quicker.WRONG_ID && id_ <= get_max_id()); }
	
	public static boolean _field_is_ok(int field_ib_) 
	{
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._get_fields().contains(field_ib_);
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._get_fields().contains(field_ib_);
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._get_fields().contains(field_ib_);
		
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
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_TIME;
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_TIME;
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_TIME;

		return output;
	}

	public static boolean includes_time_elapsed()
	{
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_TIME_ELAPSED;
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_TIME_ELAPSED;
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_TIME_ELAPSED;

		return output;
	}

	public static boolean includes_halted()
	{
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_HALTED;
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_HALTED;
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_HALTED;

		return output;
	}

	public static boolean includes_halted_tot()
	{
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.INCLUDES_HALTED_TOT;
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.INCLUDES_HALTED_TOT;
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.INCLUDES_HALTED_TOT;

		return output;
	}

	public static boolean __is_only_essential()
	{
		__lock();
		
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.is_only_essential();
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.is_only_essential();
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.is_only_essential();

		__unlock();		
		
		return output;
	}

	public static boolean __is_only_db() { return _is_only_db(true); }

	public static boolean __checks_enabled()
	{
		__lock();
		
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.checks_enabled();
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.checks_enabled();
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.checks_enabled();

		__unlock();

		return output;
	}

	public static void start(String symbol_, int id_)
	{
		boolean is_restart = id_is_ok(async_data_quicker._get_id(symbol_, false));
		boolean start_vals = !_is_only_db(false);

		int i = async_data_quicker.get_i(id_, true);
		
		if (APP.equals(async_data_watchlist_quicker._APP)) 
		{
			async_data_watchlist_quicker._last_id = id_;

			async_data_watchlist_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_watchlist_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_watchlist_quicker.start(symbol_, is_restart);
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{
			async_data_trades_quicker._last_id = id_;

			async_data_trades_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_trades_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_trades_quicker.start(symbol_, is_restart);
		}
		else if (APP.equals(async_data_market_quicker._APP)) 
		{
			async_data_market_quicker._last_id = id_;

			async_data_market_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_market_quicker._vals[i] = new HashMap<String, String>();
			
			async_data_market_quicker.start(symbol_, is_restart);
		}		
	}

	public static void __stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_) { _stop(id_, symbol_, snapshot_completed_, remove_symbol_, true); }

	public static void stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_) { _stop(id_, symbol_, snapshot_completed_, remove_symbol_, false); }
	
	public static void __update_vals(int id_, HashMap<String, String> vals_)
	{		
		__lock();
	
		if (!id_is_ok(id_)) 
		{
			__unlock();
			
			return;
		}
		
		for (Entry<String, String> item: vals_.entrySet()) { _update_vals(id_, item.getKey(), item.getValue(), false); }
		
		__unlock();		
	}

	public static void __update_vals(int id_, String col_, double val_) { _update_vals(id_, col_, Double.toString(val_), true); }

	public static HashMap<String, String> __get_vals(int id_)
	{			
		__lock();

		HashMap<String, String> vals = null;
		
		if (!id_is_ok(id_)) 
		{
			__unlock();
			
			return vals;
		}
		
		int i = async_data_quicker.get_i(id_, true);
		
		if (APP.equals(async_data_watchlist_quicker._APP) && (async_data_watchlist_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_watchlist_quicker._vals[i]);
		else if (APP.equals(async_data_trades_quicker._APP) && (async_data_trades_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_trades_quicker._vals[i]);
		else if (APP.equals(async_data_market_quicker._APP) && (async_data_market_quicker._vals[i] != null)) vals = new HashMap<String, String>(async_data_market_quicker._vals[i]);
		
		__unlock();
		
		return vals;
	}
	
	public static String[] get_symbols()
	{
		String[] output = null;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = (String[])arrays.get_new(async_data_watchlist_quicker._symbols); 	
		else if (APP.equals(async_data_trades_quicker._APP)) output = (String[])arrays.get_new(async_data_trades_quicker._symbols); 	
		else if (APP.equals(async_data_market_quicker._APP)) output = (String[])arrays.get_new(async_data_market_quicker._symbols); 	
		
		return output;
	}

	public static int get_max_id()
	{
		int output = 0;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.MAX_ID; 	
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.MAX_ID; 	
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.MAX_ID; 	
	
		return output;
	}

	public static int get_last_id()
	{
		int output = async_data_quicker.MIN_ID;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) 
		{
			output = async_data_watchlist_quicker._last_id;
			
			if (!async_data_apps_quicker.id_is_ok(output)) 
			{
				output = async_data_quicker.MIN_ID;
				async_data_watchlist_quicker._last_id = output;
			}
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{
			output = async_data_trades_quicker._last_id;
			
			if (!async_data_apps_quicker.id_is_ok(output)) 
			{
				output = async_data_quicker.MIN_ID;
				async_data_trades_quicker._last_id = output;			
			}			
		}	
		else if (APP.equals(async_data_market_quicker._APP)) 
		{
			output = async_data_market_quicker._last_id;
			
			if (!async_data_apps_quicker.id_is_ok(output)) 
			{
				output = async_data_quicker.MIN_ID;
				async_data_market_quicker._last_id = output;
			}						
		}	
	
		return output;
	}

	public static boolean log()
	{
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.log();
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.log();
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.log();
		
		return output;
	}

	private static boolean _is_only_db(boolean lock_)
	{
		if (lock_) __lock();
		
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.is_only_db();
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.is_only_db();
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.is_only_db();

		if (lock_) __unlock();
		
		return output;
	}

	private static void _update_vals(int id_, String col_, String val_, boolean lock_)
	{
		if (lock_) __lock();
		
		if (!id_is_ok(id_)) 
		{
			if (lock_) __unlock();
			
			return;
		}
		
		int i = async_data_quicker.get_i(id_, true);
		
		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._vals[i].put(col_, val_);
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker._vals[i].put(col_, val_);
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker._vals[i].put(col_, val_); 
		
		if (lock_) __unlock();		
	}	
	
	private static void _stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_, boolean lock_)
	{
		if (lock_) __lock();

		int id = id_;
		
		boolean id_is_ok = id_is_ok(id);
		boolean symbol_ok = strings.is_ok(symbol_);

		if (!id_is_ok && symbol_ok) 
		{
			id = async_data_quicker._get_id(symbol_, false);
			
			id_is_ok = id_is_ok(id);
		}

		boolean delete_globals = (id_is_ok && (snapshot_completed_ || remove_symbol_));		
		boolean delete_vals = !_is_only_db(false);

		int i = async_data_quicker.get_i(id, true);
		
		if (APP.equals(async_data_watchlist_quicker._APP)) 
		{
			if (delete_globals) 
			{
				async_data_watchlist_quicker._symbols[i] = null;				
				if (delete_vals) async_data_watchlist_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_watchlist_quicker.stop(symbol_, remove_symbol_);
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{	
			if (delete_globals) 
			{
				async_data_trades_quicker._symbols[i] = null;				
				if (delete_vals) async_data_trades_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_trades_quicker.stop(symbol_, remove_symbol_);
		}
		else if (APP.equals(async_data_market_quicker._APP)) 
		{	
			if (delete_globals) 
			{
				async_data_market_quicker._symbols[i] = null;				
				if (delete_vals) async_data_market_quicker._vals[i] = null;
			}

			if (symbol_ok) async_data_market_quicker.stop(symbol_, remove_symbol_);
		}
		
		if (lock_) __unlock();
	}
}