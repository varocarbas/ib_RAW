package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;

class async_data_watchlist_new extends parent_static
{
	public static final String _APP = "watchlist";

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
	
	public static final double MIN_FLUS_VAR = 0.005;
	public static final int MAX_FLU = 50;
	public static final int MIN_FLU = 5;
	public static final int MIN_FLU2_MAIN = 50;
	public static final int MAX_FLU2_MIN_MAX = 100;
	
	public static final String DEFAULT_TYPE = async_data_new.TYPE_SNAPSHOT;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;
	
	private volatile static HashMap<String, ArrayList<Double>> _flus = new HashMap<String, ArrayList<Double>>();
	private volatile static HashMap<String, ArrayList<Double>> _flus2 = new HashMap<String, ArrayList<Double>>();
	private volatile static HashMap<String, ArrayList<Double>> _flus2_minus = new HashMap<String, ArrayList<Double>>();
	private volatile static HashMap<String, ArrayList<Double>> _flus2_plus = new HashMap<String, ArrayList<Double>>();
	private volatile static HashMap<String, Boolean> _flus2_remove = new HashMap<String, Boolean>();

	private volatile static HashMap<String, String> _symbols = new HashMap<String, String>();

	private static ArrayList<Integer> _fields = new ArrayList<Integer>();
	
	private volatile static boolean _enabled = async_data_new.DEFAULT_ENABLED;
	private volatile static boolean _is_quick = async_data_new.DEFAULT_IS_QUICK;
	private volatile static boolean _logs_to_screen = async_data_new.DEFAULT_LOGS_TO_SCREEN;
	
	private static boolean _includes_time = false;
	private static boolean _includes_time_elapsed = true;
	private static boolean _includes_halted = true;
	private static boolean _includes_halted_tot = true;
	private static boolean _disable_asap = true;

	public static boolean is_ok() { return _enabled; }
	
	public static void enable() { _enabled = true; }
	
	public static void disable() { _enabled = false; }
	
	public static boolean is_quick() { return _is_quick; }

	public static boolean includes_time() { return _includes_time; }

	public static boolean includes_time_elapsed() { return _includes_time_elapsed; }

	public static boolean includes_halted() { return _includes_halted; }

	public static boolean includes_halted_tot() { return _includes_halted_tot; }

	public static boolean disable_asap() { return _disable_asap; }
	
	public static boolean logs_to_screen() { return _logs_to_screen; }

	public static void logs_to_screen(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }

	public static int get_max_mins_inactive() { return async_data_new.DEFAULT_MAX_MINS_INACTIVE; }
	
	public static ArrayList<String> get_active_symbols() { return db_ib.async_data.get_active_symbols(get_source(), get_max_mins_inactive()); }
	
	public static boolean _symbol_is_running(String symbol_, boolean lock_) 
	{
		if (lock_) __lock();
		
		boolean output = _symbols.containsKey(symbol_);  
	
		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean _start(String symbol_, boolean lock_) 
	{
		if (lock_) __lock();
		
		boolean started = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (_symbol_is_running(symbol, false)) 
		{
			if (lock_) __unlock();
			
			return started;
		}

		enable();
		
		String type = async_data_new.TYPE_SNAPSHOT;
		int data_type = async_data_new.DEFAULT_DATA_TYPE;
		
		started = async_data_new._start(_APP, symbol, type, data_type, false);
		if (started) start_global(symbol, type);

		if (lock_) __unlock();
		
		return started;
	}

	public static void _stop(String symbol_, boolean lock_) 
	{
		if (lock_) __lock();
		
		String symbol = common.normalise_symbol(symbol_);		
		if (!_symbol_is_running(symbol, false)) end_global(symbol);
		
		if (lock_) __unlock();
	}
	
	public static void __tick_price(int id_, int field_ib_, double price_) { async_data_new.__tick_price(_APP, id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { async_data_new.__tick_size(_APP, id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { async_data_new.__tick_generic(_APP, id_, tick_, value_); }

	public static void __end_snapshot(int id_) { async_data_new.__end_snapshot(_APP, id_); }

	public static boolean field_is_ok(int field_ib_)
	{
		if (_fields.size() == 0) populate_fields();
		
		return _fields.contains(field_ib_);
	}

	public static String get_source() { return watchlist.SOURCE; }

	public static void tick_price_specific(int id_, int field_ib_, double price_, String symbol_)
	{
		if (field_ib_ != async_data_new.PRICE_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, _is_quick);
		Object vals = (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>());

		vals = tick_price_specific_basic(symbol_, price_, db, vals);
		vals = tick_price_specific_flus(symbol_, price_, db, vals);

		db_ib.watchlist.update(vals, symbol_, _is_quick);
	}

	public static void tick_size_specific(int id_, int field_ib_, double size_, String symbol_)
	{
		if (field_ib_ != async_data_new.VOLUME_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, _is_quick);
		
		db_ib.watchlist.update(tick_size_specific_basic(symbol_, size_, db), symbol_, _is_quick);
	}
	
	static void _end(String symbol_, boolean lock_)
	{
		if (lock_) __lock();
		
		String symbol = common.normalise_symbol(symbol_);		
		if (_symbol_is_running(symbol, false)) end_global(symbol);
		
		if (lock_) __unlock();
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
		if (!_flus.containsKey(symbol_)) return vals;

		double var = Math.abs(var_);
		
		int tot = _flus.get(symbol_).size();
		if (tot >= MAX_FLU) 
		{
			_flus.get(symbol_).remove(0);
			tot--;
		}
		
		_flus.get(symbol_).add(var);
		tot++;
		
		double flu = 0.0;
		for (double val: _flus.get(symbol_)) { flu += val; }

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
		if (!_flus2_minus.containsKey(symbol_)) return vals;

		int tot_minus = _flus2_minus.get(symbol_).size();
		int tot_plus = _flus2_plus.get(symbol_).size();
		int tot = (tot_minus + tot_plus);
		
		if (tot >= MIN_FLU2_MAIN)
		{
			if (_flus2_remove.get(symbol_))
			{
				if (tot_plus > 0) 
				{
					_flus2_remove.put(symbol_, false);
					_flus2_plus.get(symbol_).remove(0);
					
					tot_plus--;
				}
				else if (tot_minus > 0)
				{
					_flus2_minus.get(symbol_).remove(0);
					
					tot_minus--;
				}
			}
			else
			{
				if (tot_minus > 0) 
				{
					_flus2_remove.put(symbol_, true);
					_flus2_minus.get(symbol_).remove(0);
					
					tot_minus--;
				}
				else if (tot_plus > 0)
				{
					_flus2_plus.get(symbol_).remove(0);
					
					tot_plus--;
				}
			}
		}

		if (var_ > 0) 
		{
			_flus2_plus.get(symbol_).add(var_);
			
			tot_plus++;
		}
		else 
		{
			_flus2_minus.get(symbol_).add(var_);
			
			tot_minus++;
		}
		
		double flu2_plus = 0.0;
		for (double val: _flus2_plus.get(symbol_)) { flu2_plus += val; }
		
		flu2_plus = (tot_plus == 0 ? 0 : flu2_plus / tot_plus);
		
		double flu2_minus = 0.0;
		for (double val: _flus2_minus.get(symbol_)) { flu2_minus += val; }
		
		flu2_minus = (tot_minus == 0 ? 0 : flu2_minus / tot_minus);

		double flu2 = (flu2_minus == 0 ? 0.0 : (flu2_plus / Math.abs(flu2_minus)));

		vals = db_ib.watchlist.add_to_vals(FLU2, flu2, vals, _is_quick);
		
		return vals;
	}

	private static Object tick_price_specific_flus_flu2_min_max(String symbol_, Object vals_, double var_)
	{
		Object vals = arrays.get_new(vals_);
		if (!_flus2.containsKey(symbol_)) return vals;
		
		int tot = _flus2.get(symbol_).size();
		if (tot >= MAX_FLU2_MIN_MAX) _flus2.get(symbol_).remove(0);

		_flus2.get(symbol_).add(var_);
		
		double min = 0;
		double max = 0;
		
		for (double val: _flus2.get(symbol_))
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
		
		items.put(async_data_new.PRICE, async_data_new.VOLUME);
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
	
	private static void populate_fields()
	{
		_fields.add(async_data_new.PRICE_IB);
		_fields.add(async_data_new.VOLUME_IB);
		_fields.add(async_data_new.HALTED_IB);
	}

	private static void start_global(String symbol_, String type_)
	{
		_symbols.put(symbol_, type_);
		
		_flus.put(symbol_, new ArrayList<Double>());
		_flus2.put(symbol_, new ArrayList<Double>());
		_flus2_minus.put(symbol_, new ArrayList<Double>());
		_flus2_plus.put(symbol_, new ArrayList<Double>());
		_flus2_remove.put(symbol_, true);		
	}

	private static void end_global(String symbol_)
	{
		_symbols.remove(symbol_);
		
		_flus.remove(symbol_);
		_flus2.remove(symbol_);
		_flus2_minus.remove(symbol_);
		_flus2_plus.remove(symbol_);
		_flus2_remove.remove(symbol_);		
	}
}