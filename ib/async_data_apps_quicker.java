package ib;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;

class async_data_apps_quicker extends parent_static
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
	
	public static void __tick_price(int field_ib_, double price_, String symbol_)
	{
		if (_app.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker.__tick_price(field_ib_, price_, symbol_);
		else if (_app.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_price(field_ib_, price_, symbol_);
		else if (_app.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_price(field_ib_, price_, symbol_);
	}

	public static void tick_size(int field_ib_, double size_, String symbol_)
	{
		if (_app.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker.tick_size(field_ib_, size_, symbol_);
		else if (_app.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_size(field_ib_, size_, symbol_);
		else if (_app.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_size(field_ib_, size_, symbol_);
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

		String symbol = (id_is_ok(id_) ? get_symbols()[id_] : null);
		
		__unlock();
		
		return symbol;
	}
	
	public static boolean includes_time()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._includes_time;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._includes_time;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._includes_time;

		return output;
	}

	public static boolean includes_time_elapsed()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._includes_time_elapsed;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._includes_time_elapsed;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._includes_time_elapsed;

		return output;
	}

	public static boolean includes_halted()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._includes_halted;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._includes_halted;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._includes_halted;

		return output;
	}

	public static boolean includes_halted_tot()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._includes_halted_tot;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._includes_halted_tot;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._includes_halted_tot;

		return output;
	}

	public static boolean only_essential()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._only_essential;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._only_essential;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._only_essential;

		return output;
	}

	public static boolean only_db()
	{
		boolean output = false;
		
		if (_app.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker._only_db;
		else if (_app.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker._only_db;
		else if (_app.equals(async_data_market_quicker._APP)) output = async_data_market_quicker._only_db;

		return output;
	}
	
	public static void start_globals(String symbol_, int id_)
	{
		boolean is_restart = id_is_ok(async_data_quicker.get_id(symbol_));

		if (_app.equals(async_data_watchlist_quicker._APP)) 
		{
			async_data_watchlist_quicker._last_id = id_;

			async_data_watchlist_quicker._symbols[id_] = symbol_;
			
			async_data_watchlist_quicker.start_globals(symbol_, is_restart);
		}
		else if (_app.equals(async_data_trades_quicker._APP)) 
		{
			async_data_trades_quicker._last_id = id_;

			async_data_trades_quicker._symbols[id_] = symbol_;
			
			async_data_trades_quicker.start_globals(symbol_, is_restart);
		}
		else if (_app.equals(async_data_market_quicker._APP)) 
		{
			async_data_market_quicker._last_id = id_;

			async_data_market_quicker._symbols[id_] = symbol_;
			
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
			id = async_data_quicker.get_id(symbol_);
			
			id_is_ok = id_is_ok(id);
		}

		boolean update_symbols = (id_is_ok && (snapshot_completed_ || remove_symbol_));		
				
		if (_app.equals(async_data_watchlist_quicker._APP)) 
		{	
			if (update_symbols) async_data_watchlist_quicker._symbols[id] = null;

			if (symbol_ok) async_data_watchlist_quicker.stop_globals(symbol_, remove_symbol_);
		}
		else if (_app.equals(async_data_trades_quicker._APP)) 
		{	
			if (update_symbols) async_data_trades_quicker._symbols[id] = null;

			if (symbol_ok) async_data_trades_quicker.stop_globals(symbol_, remove_symbol_);
		}
		else if (_app.equals(async_data_market_quicker._APP)) 
		{	
			if (update_symbols) async_data_market_quicker._symbols[id] = null;

			if (symbol_ok) async_data_market_quicker.stop_globals(symbol_, remove_symbol_);
		}
		
		__unlock();
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
}