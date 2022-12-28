package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;

abstract class async_data_watchlist extends parent_static
{
	public static final String _APP = "watchlist";

	public static final String SOURCE = db_ib.watchlist.SOURCE;
	public static final int MAX_MINS_INACTIVE = async_data.DEFAULT_MAX_MINS_INACTIVE;

	public static final int MAX_SIMULTANEOUS_SYMBOLS = 10;
	public static final int SIZE_GLOBALS = MAX_SIMULTANEOUS_SYMBOLS;
	public static final int MAX_I = SIZE_GLOBALS - 1;
	
	public static final int MIN_FLUS_TOT = 3;
	public static final int MAX_FLUS_TOT = 10;
	public static final int MAX_FLU2_MIN_MAX_TOT = 50;

	public static final double FACTOR_FLU2_ZERO = 1.5;
	
	public static final String DEFAULT_TYPE = async_data.TYPE_SNAPSHOT;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;

	public static volatile String[] _stopping = new String[SIZE_GLOBALS];

	public static volatile int _last_i_stopping = -1;
	public static volatile boolean _enabled = async_data.DEFAULT_ENABLED;
	public static volatile boolean _logs_to_screen = async_data.DEFAULT_LOGS_TO_SCREEN;
	public static volatile boolean _snapshot_nonstop = async_data.DEFAULT_SNAPSHOT_NONSTOP;

	public static ArrayList<Integer> _fields = new ArrayList<Integer>();

	public static boolean _includes_time = false;
	public static boolean _includes_time_elapsed = true;
	public static boolean _includes_halted = true;
	public static boolean _includes_halted_tot = true;
	public static boolean _disable_asap = true;
	public static boolean _only_essential = true;
	
	private static volatile String[] _symbols = new String[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus = new ArrayList[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2 = new ArrayList[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_minus = new ArrayList[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_plus = new ArrayList[SIZE_GLOBALS];
	private static volatile Boolean[] _flus2_remove = new Boolean[SIZE_GLOBALS];

	private static volatile int _last_i = -1;
	
	public static boolean is_quick() { return db_ib.common.is_quick(SOURCE); }

	public static boolean is_ok() { return _enabled; }
	
	public static boolean logs_to_screen() { return _logs_to_screen; }

	public static void logs_to_screen(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }
	
	public static void stop_all() { async_data.stop_all(_APP, true); }
	
	public static void tick_price(int id_, int field_ib_, double price_) { async_data.tick_price(_APP, id_, field_ib_, price_); }
	
	public static void tick_size(int id_, int field_ib_, int size_) { async_data.tick_size(_APP, id_, field_ib_, size_); }
	
	public static void tick_generic(int id_, int tick_, double value_) { async_data.tick_generic(_APP, id_, tick_, value_); }

	public static void tick_snapshot_end(int id_) { async_data.tick_snapshot_end(_APP, id_); }

	public static void tick_price(int id_, double price_, String symbol_)
	{
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, is_quick());
		Object vals = (is_quick() ? new HashMap<String, String>() : new HashMap<String, Object>());

		vals = tick_price_basic(symbol_, price_, db, vals);
		vals = tick_price_flus(symbol_, price_, db, vals);

		db_ib.watchlist.update(vals, symbol_, is_quick());
	}

	public static void tick_volume(int id_, double volume_, String symbol_)
	{
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, is_quick());
		
		db_ib.watchlist.update(tick_volume_basic(symbol_, volume_, db), symbol_, is_quick());
	}
	
	public static void populate_fields()
	{
		if (_fields.size() > 0) return;
		
		_fields.add(async_data.PRICE_IB);
		_fields.add(async_data.VOLUME_IB);
		_fields.add(async_data.HALTED_IB);
	}

	public static boolean start(String symbol_) { return start(symbol_, DEFAULT_TYPE, DEFAULT_DATA_TYPE); }
	
	public static void stop(String symbol_) 
	{
		String symbol = common.normalise_symbol(symbol_);
		
		if (strings.is_ok(symbol) && async_data.symbol_is_running(_APP, symbol)) async_data.stop(_APP, symbol, true);
	}
	
	public static ArrayList<String> get_all_symbols() { return async_data.get_all_symbols(_APP, is_quick()); }
	
	public static ArrayList<String> get_active_symbols() { return async_data.get_active_symbols(_APP, is_quick()); }

	private static boolean start(String symbol_, String type_, int data_type_) 
	{
		boolean started = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (async_data.symbol_is_running(_APP, symbol)) return started;

		started = async_data.start(_APP, symbol, type_, data_type_);
		if (started) start_globals(symbol, type_);
		
		return started;
	}

	private static Object tick_price_basic(String symbol_, double price_, HashMap<String, String> db_, Object vals_) { return tick_basic(symbol_, price_, db_, vals_, true); }

	private static Object tick_price_flus(String symbol_, double price_, HashMap<String, String> db_, Object vals_)
	{
		Object vals = db_ib.common.add_to_vals(SOURCE, db_ib.async_data.FLUS_PRICE, price_, arrays.get_new(vals_));
		
		double price_db = db_ib.async_data.get_out_vals_number(SOURCE, db_ib.async_data.FLUS_PRICE, db_, true);			
		
		double var = numbers.get_perc_hist(price_, price_db);
		if (var == 0.0) return vals; 
			
		vals = tick_price_flus_flu(symbol_, vals, var);
		vals = tick_price_flus_flu2(symbol_, vals, var);

		return vals;
	}

	private static Object tick_price_flus_flu(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);
		
		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;

		int tot = _flus[i].size();
		if (tot > MAX_FLUS_TOT) 
		{
			_flus[i].remove(0);

			tot--;
		}
		
		_flus[i].add(Math.abs(var_));		
		tot++;
		
		if (tot < MIN_FLUS_TOT) return vals;
		
		double flu = 0.0;
		for (double val: _flus[i]) { flu += val; }

		flu /= (double)tot;

		return db_ib.common.add_to_vals(SOURCE, db_ib.async_data.FLU, numbers.round(flu), vals);
	}

	private static Object tick_price_flus_flu2(String symbol_, Object vals_, double var_)
	{
		Object vals = tick_price_flus_flu2_main(symbol_, vals_, var_);
		
		vals = tick_price_flus_flu2_min_max(symbol_, vals, var_);

		return vals;
	}
	
	private static Object tick_price_flus_flu2_main(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);

		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;

		int tot_minus = _flus2_minus[i].size();
		int tot_plus = _flus2_plus[i].size();
		
		if ((tot_minus + tot_plus) > MAX_FLUS_TOT)
		{
			if (_flus2_remove[i])
			{
				if (tot_plus > 0) 
				{
					_flus2_remove[i] = false;
					_flus2_plus[i].remove(0);
					
					tot_plus--;
				}
				else if (tot_minus > 0)
				{
					_flus2_minus[i].remove(0);
					
					tot_minus--;
				}
			}
			else
			{
				if (tot_minus > 0) 
				{
					_flus2_remove[i] = true;
					_flus2_minus[i].remove(0);
					
					tot_minus--;
				}
				else if (tot_plus > 0)
				{
					_flus2_plus[i].remove(0);
					
					tot_plus--;
				}
			}
		}

		if (var_ > 0) 
		{
			_flus2_plus[i].add(var_);
			
			tot_plus++;
		}
		else 
		{
			_flus2_minus[i].add(var_);
			
			tot_minus++;
		}

		if ((tot_plus + tot_minus) < MIN_FLUS_TOT) return vals;
		
		double flu2_plus = 0.0;
		for (double val: _flus2_plus[i]) { flu2_plus += val; }
		
		flu2_plus = (tot_plus == 0 ? 0.0 : flu2_plus / (double)tot_plus);
		
		double flu2_minus = 0.0;
		for (double val: _flus2_minus[i]) { flu2_minus += val; }
		
		flu2_minus = (tot_minus == 0 ? 0.0 : flu2_minus / (double)tot_minus);

		double flu2 = 0.0;		
		if (tot_plus > 0 && tot_minus > 0) flu2 = flu2_plus / Math.abs(flu2_minus);
		else
		{
			if (tot_minus == 0) 
			{
				flu2 = (double)tot_plus;
				
				if (Math.abs(flu2) > 1.0) flu2 /= FACTOR_FLU2_ZERO;
			}
			else if (tot_plus == 0) flu2 = Math.pow(FACTOR_FLU2_ZERO, -1.0 * (double)tot_minus);
		}
		
		return db_ib.common.add_to_vals(SOURCE, db_ib.async_data.FLU2, numbers.round(flu2), vals);
	}

	private static Object tick_price_flus_flu2_min_max(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);

		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;
		
		int tot = _flus2[i].size();
		if (tot > MAX_FLU2_MIN_MAX_TOT) _flus2[i].remove(0);

		_flus2[i].add(var_);
		
		double min = 0;
		double max = 0;
		
		for (double val: _flus2[i])
		{
			if (val < min) min = val;
			else if (val > max) max = val;
		}

		vals = db_ib.common.add_to_vals(SOURCE, db_ib.async_data.FLU2_MIN, numbers.round(min), vals);
		vals = db_ib.common.add_to_vals(SOURCE, db_ib.async_data.FLU2_MAX, numbers.round(max), vals);
		
		return vals;
	}
	
	private static Object tick_volume_basic(String symbol_, double volume_, HashMap<String, String> db_) { return tick_basic(symbol_, volume_, db_, (is_quick() ? new HashMap<String, String>() : new HashMap<String, Object>()), false); }
	
	private static Object tick_basic(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
	{
		Object vals = tick_basic_start(symbol_, val_, db_, vals_, is_price_);

		String field = (is_price_ ? db_ib.async_data.PRICE_MIN : db_ib.async_data.VOLUME_MIN);
		double val_db = db_ib.async_data.get_out_vals_number(SOURCE, field, db_, false);
		
		boolean is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ < val_db) vals = db_ib.common.add_to_vals(SOURCE, field, val_, vals);
		
		field = (is_price_ ? db_ib.async_data.PRICE_MAX : db_ib.async_data.VOLUME_MAX);
		val_db = db_ib.async_data.get_out_vals_number(SOURCE, field, db_, false);
		
		is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ > val_db) vals = db_ib.common.add_to_vals(SOURCE, field, val_, vals);
		
		return vals;
	}

	private static Object tick_basic_start(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
	{
		Object vals = arrays.get_new(vals_);		
				
		HashMap<String, String> items = new HashMap<String, String>();
		
		items.put(db_ib.async_data.PRICE, db_ib.async_data.VOLUME);
		items.put(db_ib.async_data.PRICE_INI, db_ib.async_data.VOLUME_INI);
		items.put(db_ib.async_data.FLUS_PRICE, strings.DEFAULT);

		double price_ini = common.WRONG_PRICE;
		
		for (Entry<String, String> item: items.entrySet())
		{
			String field = (is_price_ ? item.getKey() : item.getValue());
			if (!strings.is_ok(field)) continue;
			
			boolean is_price_ini = (is_price_ && field.equals(db_ib.async_data.PRICE_INI));			
			double val_db = db_ib.async_data.get_out_vals_number(SOURCE, field, db_, false);
			
			if (!tick_app_val_is_ok(val_db, is_price_)) 
			{
				vals = db_ib.common.add_to_vals(SOURCE, field, val_, vals);
				if (is_price_ini) price_ini = val_;
			}
			else if (is_price_ini) price_ini = val_db;
		}

		if (is_price_) vals = db_ib.common.add_to_vals(SOURCE, db_ib.async_data.VAR_TOT, numbers.get_perc_hist(val_, price_ini), vals);
		
		return vals;
	}

	private static boolean tick_app_val_is_ok(double val_, boolean is_price_) { return (is_price_ ? ib.common.price_is_ok(val_) : ib.common.size_is_ok(val_)); }

	private static void start_globals(String symbol_, String type_)
	{
		int i = _last_i + 1;
		if (i > MAX_I) i = 0;
		
		_symbols[i] = symbol_;
		
		_flus[i] = new ArrayList<Double>();
		_flus2[i] = new ArrayList<Double>();
		_flus2_minus[i] = new ArrayList<Double>();
		_flus2_plus[i] = new ArrayList<Double>();
		_flus2_remove[i] = true;		
	
		_last_i = i;
	}
	
	private static int get_i(String symbol_) { return async_data.get_i(_symbols, _last_i, MAX_I, symbol_); }
}