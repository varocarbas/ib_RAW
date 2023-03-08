package ib;

import java.util.Map.Entry;

import accessory.arrays;
import accessory.arrays_quick;
import accessory.db_quick;
import accessory.parent_static;
import accessory.strings;

abstract class async_data_apps_quicker extends parent_static
{
	private static String[] COLS = null;

	private static String APP = strings.DEFAULT;
	private static String SOURCE = strings.DEFAULT;
	private static String COL_HALTED = null;
	private static int VALS_SIZE = 0;
	
	public static boolean update_app(String app_)
	{
		boolean output = strings.are_equal(APP, app_);
		
		APP = app_;
		
		if (app_.equals(async_data_watchlist_quicker._APP)) SOURCE = async_data_watchlist_quicker.SOURCE; 
		else if (app_.equals(async_data_trades_quicker._APP)) SOURCE = async_data_trades_quicker.SOURCE; 
		else if (app_.equals(async_data_market_quicker._APP)) SOURCE = async_data_market_quicker.SOURCE; 
	
		return output;
	}

	public static String get_app() { return APP; }

	public static String get_source() { return SOURCE; }
	
	public static void tick_price(int id_, int field_ib_, double price_, String symbol_)
	{
		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker.tick_price(id_, field_ib_, price_, symbol_);
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_price(id_, field_ib_, price_, symbol_);
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_price(id_, field_ib_, price_, symbol_);
	}

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_)
	{
		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker.tick_size(id_, field_ib_, size_, symbol_);
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker.tick_size(id_, field_ib_, size_, symbol_);
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker.tick_size(id_, field_ib_, size_, symbol_);
	}
	
	public static boolean __field_is_ok(int field_ib_) { return _field_is_ok(field_ib_, true); }
	
	public static boolean _field_is_ok(int field_ib_, boolean lock_) 
	{
		boolean is_market = APP.equals(async_data_market_quicker._APP);
		
		if (is_market && lock_) __lock();
		
		boolean output = false;

		if (APP.equals(async_data_watchlist_quicker._APP)) output = arrays_quick.value_exists(async_data_watchlist_quicker.FIELDS_IB, field_ib_);
		else if (APP.equals(async_data_trades_quicker._APP)) output = arrays_quick.value_exists(async_data_trades_quicker.FIELDS_IB, field_ib_);
		else if (is_market) output = arrays_quick.value_exists(async_data_market_quicker._fields_ib, field_ib_);

		if (is_market && lock_) __unlock();
		
		return output;
	}

	public static String __get_symbol(int id_) { return _get_symbol(id_, true); }
	
	public static String _get_symbol(int id_, boolean lock_)
	{
		if (lock_) __lock();

		String symbol = (async_data_quicker.id_is_ok(id_) ? get_symbols()[async_data_quicker.get_i(id_, true)] : null);

		if (lock_) __unlock();

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

	public static boolean __is_only_essential() { return _is_only_essential(true); }
	
	public static boolean _is_only_essential(boolean lock_)
	{
		boolean is_market = APP.equals(async_data_market_quicker._APP);
		
		if (is_market && lock_) __lock();

		boolean output = false;

		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.ONLY_ESSENTIAL;
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.ONLY_ESSENTIAL;
		else if (is_market) output = async_data_market_quicker._only_essential;

		if (is_market && lock_) __unlock();		

		return output;
	}

	public static boolean __is_only_db() { return _is_only_db(true); }
	
	public static boolean _is_only_db(boolean lock_)
	{
		if (lock_) __lock();
		
		boolean output = false;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) output = async_data_watchlist_quicker.is_only_db();
		else if (APP.equals(async_data_trades_quicker._APP)) output = async_data_trades_quicker.is_only_db();
		else if (APP.equals(async_data_market_quicker._APP)) output = async_data_market_quicker.is_only_db();

		if (lock_) __unlock();
		
		return output;
	}
	
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
		boolean is_restart = async_data_quicker.id_is_ok(async_data_quicker._get_id(symbol_, false));
		boolean start_vals = !_is_only_db(false);

		int i = async_data_quicker.get_i(id_, true);
		
		if (APP.equals(async_data_watchlist_quicker._APP)) 
		{
			async_data_watchlist_quicker._last_id = id_;

			async_data_watchlist_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_watchlist_quicker._vals[i] = new double[VALS_SIZE];
			
			async_data_watchlist_quicker.start(symbol_, is_restart);
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{
			async_data_trades_quicker._last_id = id_;

			async_data_trades_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_trades_quicker._vals[i] = new double[VALS_SIZE];
			
			async_data_trades_quicker.start(symbol_, is_restart);
		}
		else if (APP.equals(async_data_market_quicker._APP)) 
		{
			async_data_market_quicker._last_id = id_;

			async_data_market_quicker._symbols[i] = symbol_;
			if (start_vals) async_data_market_quicker._vals[i] = new double[VALS_SIZE];
			
			async_data_market_quicker.start(symbol_, is_restart);
		}		
	}

	public static void __stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_) { _stop(id_, symbol_, snapshot_completed_, remove_symbol_, true); }

	public static void stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_) { _stop(id_, symbol_, snapshot_completed_, remove_symbol_, false); }
	
	public static void _update_vals(int id_, int field_ib_, double val_, boolean lock_)
	{
		if (lock_) __lock();

		if (!async_data_quicker.id_is_ok(id_)) 
		{
			if (lock_) __unlock();

			return;
		}

		int i = async_data_quicker.get_i(id_, true);

		if (APP.equals(async_data_watchlist_quicker._APP)) async_data_watchlist_quicker._vals[i][field_ib_] = val_;
		else if (APP.equals(async_data_trades_quicker._APP)) async_data_trades_quicker._vals[i][field_ib_] = val_;
		else if (APP.equals(async_data_market_quicker._APP)) async_data_market_quicker._vals[i][field_ib_] = val_; 

		if (lock_) __unlock();		
	}

	public static double[] __get_vals(int id_)
	{			
		__lock();

		double[] vals = null;

		if (!async_data_quicker.id_is_ok(id_)) 
		{
			__unlock();

			return vals;
		}

		int i = async_data_quicker.get_i(id_, true);

		if (APP.equals(async_data_watchlist_quicker._APP) && (async_data_watchlist_quicker._vals[i] != null)) vals = arrays_quick.get_1d(async_data_watchlist_quicker._vals, i);
		else if (APP.equals(async_data_trades_quicker._APP) && (async_data_trades_quicker._vals[i] != null)) vals = arrays_quick.get_1d(async_data_trades_quicker._vals, i);
		else if (APP.equals(async_data_market_quicker._APP) && (async_data_market_quicker._vals[i] != null)) vals = arrays_quick.get_1d(async_data_market_quicker._vals, i);

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
			
			if (!async_data_quicker.id_is_ok(output)) 
			{
				output = async_data_quicker.MIN_ID;
				async_data_watchlist_quicker._last_id = output;
			}
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{
			output = async_data_trades_quicker._last_id;
			
			if (!async_data_quicker.id_is_ok(output)) 
			{
				output = async_data_quicker.MIN_ID;
				async_data_trades_quicker._last_id = output;			
			}			
		}	
		else if (APP.equals(async_data_market_quicker._APP)) 
		{
			output = async_data_market_quicker._last_id;
			
			if (!async_data_quicker.id_is_ok(output)) 
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
	
	public static String get_col(int field_ib_) { return (field_ib_ == async_data_quicker.HALTED_IB ? COL_HALTED : COLS[field_ib_]); }

	public static void __populate_fields_cols_cache(boolean force_population_) { _populate_fields_cols_cache(true, force_population_); }
	
	public static void _populate_fields_cols_cache(boolean lock_, boolean force_population_)
	{
		if (COLS != null && !force_population_) return;

		async_data_cache_quicker.COLS = new String[0];
		
		int[] fields_ib = null;
		
		if (APP.equals(async_data_watchlist_quicker._APP)) 
		{	
			async_data_watchlist_quicker.COL_FLU = db_quick.get_col(SOURCE, db_ib.watchlist.FLU);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_FLU);
			
			async_data_watchlist_quicker.COL_FLUS_PRICE = db_quick.get_col(SOURCE, db_ib.watchlist.FLUS_PRICE);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_FLUS_PRICE);

			async_data_watchlist_quicker.COL_FLU2 = db_quick.get_col(SOURCE, db_ib.watchlist.FLU2);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_FLU2);
			
			async_data_watchlist_quicker.COL_FLU2_MIN = db_quick.get_col(SOURCE, db_ib.watchlist.FLU2_MIN);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_FLU2_MIN);

			async_data_watchlist_quicker.COL_FLU2_MAX = db_quick.get_col(SOURCE, db_ib.watchlist.FLU2_MAX);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_FLU2_MAX);
			
			async_data_watchlist_quicker.COL_PRICE_INI = db_quick.get_col(SOURCE, db_ib.watchlist.PRICE_INI);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_PRICE_INI);
			
			async_data_watchlist_quicker.COL_PRICE_MIN = db_quick.get_col(SOURCE, db_ib.watchlist.PRICE_MIN);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_PRICE_MIN);
			
			async_data_watchlist_quicker.COL_PRICE_MAX = db_quick.get_col(SOURCE, db_ib.watchlist.PRICE_MAX);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_PRICE_MAX);
			
			async_data_watchlist_quicker.COL_VOLUME_INI = db_quick.get_col(SOURCE, db_ib.watchlist.VOLUME_INI);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_VOLUME_INI);
			
			async_data_watchlist_quicker.COL_VOLUME_MIN = db_quick.get_col(SOURCE, db_ib.watchlist.VOLUME_MIN);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_VOLUME_MIN);
			
			async_data_watchlist_quicker.COL_VOLUME_MAX = db_quick.get_col(SOURCE, db_ib.watchlist.VOLUME_MAX);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_VOLUME_MAX);

			async_data_watchlist_quicker.COL_VAR_TOT = db_quick.get_col(SOURCE, db_ib.watchlist.VAR_TOT);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_watchlist_quicker.COL_VAR_TOT);
			
			fields_ib = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.VOLUME_IB };
			
			async_data_watchlist_quicker.FIELDS_IB = arrays_quick.get_new(fields_ib);
		}
		else if (APP.equals(async_data_trades_quicker._APP)) 
		{ 
			fields_ib = new int[] { async_data_quicker.PRICE_IB }; 
		
			async_data_trades_quicker.FIELDS_IB = arrays_quick.get_new(fields_ib);
		}
		else if (APP.equals(async_data_market_quicker._APP))
		{	
			if (lock_) __lock();
			
			if (_is_only_essential(false)) fields_ib = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.VOLUME_IB, async_data_quicker.HALTED_IB };
			else
			{				
				fields_ib = new int[] 
				{
					async_data_quicker.PRICE_IB, async_data_quicker.VOLUME_IB, async_data_quicker.OPEN_IB,
					async_data_quicker.CLOSE_IB, async_data_quicker.LOW_IB, async_data_quicker.HIGH_IB,
					async_data_quicker.ASK_IB, async_data_quicker.BID_IB, async_data_quicker.SIZE_IB,
					async_data_quicker.ASK_SIZE_IB, async_data_quicker.BID_SIZE_IB, async_data_quicker.HALTED_IB
				};
			}
			
			async_data_market_quicker._fields_ib = arrays_quick.get_new(fields_ib);
		
			if (lock_) __unlock();
		}

		async_data_quicker.COL_SYMBOL = db_quick.get_col(SOURCE, db_ib.async_data.SYMBOL);
		async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_quicker.COL_SYMBOL);

		async_data_quicker.COL_TIMESTAMP = db_quick.get_col(SOURCE, db_ib.async_data.TIMESTAMP);
		async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_quicker.COL_TIMESTAMP);
		
		if (includes_time_elapsed())
		{
			async_data_quicker.COL_TIME_ELAPSED = db_quick.get_col(SOURCE, db_ib.async_data.TIME_ELAPSED);			
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_quicker.COL_TIME_ELAPSED);
			
			async_data_quicker.COL_ELAPSED_INI = db_quick.get_col(SOURCE, db_ib.async_data.ELAPSED_INI);			
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_quicker.COL_ELAPSED_INI);
		}

		if (includes_time()) 
		{
			async_data_quicker.COL_TIME = db_quick.get_col(SOURCE, db_ib.async_data.TIME);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, async_data_quicker.COL_TIME);
		}
		
		if (includes_halted()) 
		{
			COL_HALTED = db_quick.get_col(SOURCE, db_ib.async_data.HALTED);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, COL_HALTED);
		}
		
		VALS_SIZE = 0;
		COLS = new String[0];

		for (Entry<Integer, String> item: async_data_quicker.get_field_equivalents(fields_ib).entrySet())
		{
			int field_ib = item.getKey();
			String field = item.getValue();

			String col = db_quick.get_col(SOURCE, field);
			
			COLS = arrays_quick.add(COLS, field_ib, col);
			async_data_cache_quicker.COLS = arrays_quick.add(async_data_cache_quicker.COLS, col);
			
			if (field_ib >= VALS_SIZE) VALS_SIZE = field_ib + 1;
		}
		
		async_data_cache_quicker.populate(SOURCE);
		
		if (lock_) __unlock();
	}	
	
	private static void _stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_, boolean lock_)
	{
		if (lock_) __lock();

		int id = id_;
		
		boolean id_is_ok = async_data_quicker.id_is_ok(id);
		boolean symbol_ok = strings.is_ok(symbol_);

		if (!id_is_ok && symbol_ok) 
		{
			id = async_data_quicker._get_id(symbol_, false);
			
			id_is_ok = async_data_quicker.id_is_ok(id);
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