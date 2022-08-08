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
	
	public static final String PRICE_INI = db_ib.watchlist.PRICE_INI;
	public static final String PRICE_MIN = db_ib.watchlist.PRICE_MIN;
	public static final String PRICE_MAX = db_ib.watchlist.PRICE_MAX;
	public static final String VOLUME_INI = db_ib.watchlist.VOLUME_INI;
	public static final String VOLUME_MIN = db_ib.watchlist.VOLUME_MIN;
	public static final String VOLUME_MAX = db_ib.watchlist.VOLUME_MAX;

	public static final String FLU = db_ib.watchlist.FLU;
	public static final String FLU_PRICE = db_ib.watchlist.FLU_PRICE;
	public static final String FLU2 = db_ib.watchlist.FLU2;
	public static final String FLU2_MIN = db_ib.watchlist.FLU2_MIN;
	public static final String FLU2_MAX = db_ib.watchlist.FLU2_MAX;
	public static final String VAR_TOT = db_ib.watchlist.VAR_TOT;

	public static final int MAX_SIMULTANEOUS_SYMBOLS = 50;
	public static final int SIZE_GLOBALS = 2 * MAX_SIMULTANEOUS_SYMBOLS;
	public static final int MAX_I = SIZE_GLOBALS - 1;
	
	public static final double MIN_FLUS_VAR = 0.005;
	public static final int MAX_FLU = 50;
	public static final int MIN_FLU = 5;
	public static final int MIN_FLU2_MAIN = 50;
	public static final int MAX_FLU2_MIN_MAX = 100;
	
	public static final String DEFAULT_TYPE = async_data.TYPE_SNAPSHOT;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;

	public static volatile String[] _stopping = new String[SIZE_GLOBALS];

	public static volatile int _last_i_stopping = -1;
	public static volatile boolean _enabled = async_data.DEFAULT_ENABLED;
	public static volatile boolean _is_quick = async_data.DEFAULT_IS_QUICK;
	public static volatile boolean _logs_to_screen = async_data.DEFAULT_LOGS_TO_SCREEN;
	public static volatile int _pause_nonstop = async_data.DEFAULT_PAUSE_NONSTOP;

	public static ArrayList<Integer> _fields = new ArrayList<Integer>();

	public static boolean _includes_time = false;
	public static boolean _includes_time_elapsed = true;
	public static boolean _includes_halted = true;
	public static boolean _includes_halted_tot = true;
	public static boolean _disable_asap = true;

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

	public static boolean is_ok() { return _enabled; }
	
	public static boolean logs_to_screen() { return _logs_to_screen; }

	public static void logs_to_screen(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }

	public static void stop_all() { async_data.stop_all(_APP, true); }
	
	public static void tick_price(int id_, int field_ib_, double price_) { async_data.tick_price(_APP, id_, field_ib_, price_); }
	
	public static void tick_size(int id_, int field_ib_, int size_) { async_data.tick_size(_APP, id_, field_ib_, size_); }
	
	public static void tick_generic(int id_, int tick_, double value_) { async_data.tick_generic(_APP, id_, tick_, value_); }

	public static void end_snapshot(int id_) { async_data.end_snapshot(_APP, id_); }

	public static void tick_price_specific(int id_, int field_ib_, double price_, String symbol_)
	{
		if (field_ib_ != async_data.PRICE_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, _is_quick);
		Object vals = (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>());

		vals = tick_price_specific_basic(symbol_, price_, db, vals);
		vals = tick_price_specific_flus(symbol_, price_, db, vals);

		db_ib.watchlist.update(vals, symbol_, _is_quick);
	}

	public static void tick_size_specific(int id_, int field_ib_, double size_, String symbol_)
	{
		if (field_ib_ != async_data.VOLUME_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, _is_quick);
		
		db_ib.watchlist.update(tick_size_specific_basic(symbol_, size_, db), symbol_, _is_quick);
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
	
	public static ArrayList<String> get_active_symbols() { return async_data.get_active_symbols(_APP); }

	private static boolean start(String symbol_, String type_, int data_type_) 
	{
		boolean started = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (async_data.symbol_is_running(_APP, symbol)) return started;

		started = async_data.start_common(_APP, symbol, type_, data_type_);
		if (started) start_globals(symbol, type_);
		
		return started;
	}

	private static Object tick_price_specific_basic(String symbol_, double price_, HashMap<String, String> db_, Object vals_) { return tick_specific_basic(symbol_, price_, db_, vals_, true); }

	private static Object tick_price_specific_flus(String symbol_, double price_, HashMap<String, String> db_, Object vals_)
	{
		Object vals = db_ib.watchlist.add_to_vals(FLU_PRICE, price_, arrays.get_new(vals_), _is_quick);
		
		double price_db = db_ib.watchlist.get_vals_number(FLU_PRICE, db_, _is_quick);
				
		double var = numbers.get_perc_hist(price_, price_db);
		if (Math.abs(var) < MIN_FLUS_VAR) return vals;

		vals = tick_price_specific_flus_flu(symbol_, db_, vals, var);

		vals = tick_price_specific_flus_flu2(symbol_, vals, var);

		return vals;
	}

	private static Object tick_price_specific_flus_flu(String symbol_, HashMap<String, String> db_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);
		
		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;

		double var = Math.abs(var_);
		
		int tot = _flus[i].size();
		if (tot >= MAX_FLU) 
		{
			_flus[i].remove(0);
			tot--;
		}
		
		_flus[i].add(var);
		tot++;
		
		double flu = 0.0;
		for (double val: _flus[i]) { flu += val; }

		flu = (tot < MIN_FLU ? 0.0 : flu / tot);

		return db_ib.watchlist.add_to_vals(FLU, flu, vals, _is_quick);
	}

	private static Object tick_price_specific_flus_flu2(String symbol_, Object vals_, double var_)
	{
		Object vals = tick_price_specific_flus_flu2_main(symbol_, vals_, var_);
		
		vals = tick_price_specific_flus_flu2_min_max(symbol_, vals, var_);

		return vals;
	}
	
	private static Object tick_price_specific_flus_flu2_main(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);

		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;

		int tot_minus = _flus2_minus[i].size();
		int tot_plus = _flus2_plus[i].size();
		int tot = (tot_minus + tot_plus);
		
		if (tot >= MIN_FLU2_MAIN)
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
		
		double flu2_plus = 0.0;
		for (double val: _flus2_plus[i]) { flu2_plus += val; }
		
		flu2_plus = (tot_plus == 0 ? 0 : flu2_plus / tot_plus);
		
		double flu2_minus = 0.0;
		for (double val: _flus2_minus[i]) { flu2_minus += val; }
		
		flu2_minus = (tot_minus == 0 ? 0 : flu2_minus / tot_minus);

		double flu2 = (flu2_minus == 0 ? 0.0 : (flu2_plus / Math.abs(flu2_minus)));

		vals = db_ib.watchlist.add_to_vals(FLU2, flu2, vals, _is_quick);
		
		return vals;
	}

	private static Object tick_price_specific_flus_flu2_min_max(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);

		int i = get_i(symbol_);
		if (i == async_data.WRONG_I) return vals;
		
		int tot = _flus2[i].size();
		if (tot >= MAX_FLU2_MIN_MAX) _flus2[i].remove(0);

		_flus2[i].add(var_);
		
		double min = 0;
		double max = 0;
		
		for (double val: _flus2[i])
		{
			if (val < min) min = val;
			else if (val > max) max = val;
		}

		vals = db_ib.watchlist.add_to_vals(FLU2_MIN, min, vals, _is_quick);
		vals = db_ib.watchlist.add_to_vals(FLU2_MAX, max, vals, _is_quick);
		
		return vals;
	}
	
	private static Object tick_size_specific_basic(String symbol_, double size_, HashMap<String, String> db_) { return tick_specific_basic(symbol_, size_, db_, (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>()), false); }
	
	private static Object tick_specific_basic(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
	{
		Object vals = tick_specific_basic_start(symbol_, val_, db_, vals_, is_price_);

		String field = (is_price_ ? PRICE_MIN : VOLUME_MIN);
		double val_db = db_ib.watchlist.get_vals_number(field, db_, _is_quick);
		
		boolean is_ok_db = tick_specific_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ < val_db) vals = db_ib.watchlist.add_to_vals(field, val_, vals, _is_quick);
		
		field = (is_price_ ? PRICE_MAX : VOLUME_MAX);
		val_db = db_ib.watchlist.get_vals_number(field, db_, _is_quick);
		
		is_ok_db = tick_specific_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ > val_db) vals = db_ib.watchlist.add_to_vals(field, val_, vals, _is_quick);
		
		return vals;
	}

	private static Object tick_specific_basic_start(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
	{
		Object vals = arrays.get_new(vals_);		
				
		HashMap<String, String> items = new HashMap<String, String>();
		
		items.put(async_data.PRICE, async_data.VOLUME);
		items.put(PRICE_INI, VOLUME_INI);
		items.put(FLU_PRICE, strings.DEFAULT);

		double price_ini = common.WRONG_PRICE;
		
		for (Entry<String, String> item: items.entrySet())
		{
			String field = (is_price_ ? item.getKey() : item.getValue());
			if (!strings.is_ok(field)) continue;
			
			boolean is_price_ini = (is_price_ && field.equals(PRICE_INI));			
			double val_db = db_ib.watchlist.get_vals_number(field, db_, _is_quick);
			
			if (!tick_specific_val_is_ok(val_db, is_price_)) 
			{
				vals = db_ib.watchlist.add_to_vals(field, val_, vals, _is_quick);
				if (is_price_ini) price_ini = val_;
			}
			else if (is_price_ini) price_ini = val_db;
		}

		if (is_price_) vals = db_ib.watchlist.add_to_vals(VAR_TOT, numbers.get_perc_hist(val_, price_ini), vals, _is_quick);
		
		return vals;
	}

	private static boolean tick_specific_val_is_ok(double val_, boolean is_price_) { return (is_price_ ? ib.common.price_is_ok(val_) : ib.common.size_is_ok(val_)); }

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