package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;

abstract class async_data_watchlist_quicker extends parent_static 
{
	public static final String _APP = "watchlist";
	
	public static final String SOURCE = db_ib.watchlist.SOURCE;

	public static final int MAX_SIMULTANEOUS_SYMBOLS = 5;
	public static final double MAX_VAR = 50.0;

	static final int SIZE_GLOBALS = 350;
	static final int MAX_ID = SIZE_GLOBALS - 1;	
	static final boolean INCLUDES_TIME = false;
	static final boolean INCLUDES_TIME_ELAPSED = true;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = false;
	static final boolean ONLY_ESSENTIAL = true;
	static final boolean ONLY_DB = true;

	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	static volatile HashMap<String, String>[] _vals = (HashMap<String, String>[])new HashMap[SIZE_GLOBALS];

	static volatile int _last_id = -1;
	
	static int[] _fields = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.VOLUME_IB, async_data_quicker.HALTED_IB };
	
	private static final int SIZE_GLOBALS_FLUS = MAX_SIMULTANEOUS_SYMBOLS;
	private static final int MAX_I_FLUS = SIZE_GLOBALS_FLUS - 1;

	private static final int MIN_FLUS_TOT = 3;
	private static final int MAX_FLUS_TOT = 10;
	private static final int MAX_FLU2_MIN_MAX_TOT = 100;
	private static final double FACTOR_FLU2_ZERO = 1.5;

	private static volatile String[] _symbols_flus = new String[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus = new ArrayList[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2 = new ArrayList[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_minus = new ArrayList[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_plus = new ArrayList[SIZE_GLOBALS_FLUS];
	private static volatile Boolean[] _flus2_remove = new Boolean[SIZE_GLOBALS_FLUS];
	private static volatile int _last_i_flus = -1;

	private static boolean _log = async_data_quicker.DEFAULT_LOG;
	
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	

	public static boolean __add(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __remove(String symbol_) { async_data_quicker.__stop(_APP, symbol_); }

	public static void __remove_all() { async_data_quicker.__stop_all(_APP, _symbols); }
	
	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static void __tick_price(int id_, int field_ib_, double price_, String symbol_)
	{
		if (field_ib_ != async_data_quicker.PRICE_IB) return;
			
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, true);
		HashMap<String, String> vals = new HashMap<String, String>();

		vals = tick_price_basic(symbol_, price_, db, vals);
		if (!arrays.is_ok(vals)) return;
		
		HashMap<String, String> temp = __tick_price_flus(symbol_, price_, db, vals);
		if (arrays.is_ok(temp)) vals = new HashMap<String, String>(temp);
		
		async_data_quicker._update(id_, symbol_, vals);
	}

	public static void tick_size(int id_, int field_ib_, double size_, String symbol_)
	{
		if (field_ib_ != async_data_quicker.VOLUME_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_, true);
		
		HashMap<String, String> vals = tick_size_basic(symbol_, size_, db);
		if (!arrays.is_ok(vals)) return;
		
		async_data_quicker._update(id_, symbol_, vals);
	}
	
	public static void start_globals(String symbol_, boolean is_restart_)
	{
		if (is_restart_) return;
		
		start_globals_flus(symbol_);
	}
	
	public static void stop_globals(String symbol_, boolean remove_symbol_)
	{
		if (!remove_symbol_) return;
		
		stop_globals_flus(symbol_);		
	}

	private static int get_i_flus(String symbol_) { return async_data_quicker.get_id_i(symbol_, (String[])arrays.get_new(_symbols_flus), _last_i_flus, MAX_I_FLUS, false); }

	private static void start_globals_flus(String symbol_)
	{
		int i = _last_i_flus + 1;
		if (i > MAX_I_FLUS) i = 0;
		
		_symbols_flus[i] = symbol_;
		
		_flus[i] = new ArrayList<Double>();
		_flus2[i] = new ArrayList<Double>();
		_flus2_minus[i] = new ArrayList<Double>();
		_flus2_plus[i] = new ArrayList<Double>();
		_flus2_remove[i] = true;		
	
		_last_i_flus = i;
	}

	private static void stop_globals_flus(String symbol_)
	{
		int i = get_i_flus(symbol_);
		if (i <= async_data_quicker.WRONG_I) return;

		_symbols_flus[i] = null;
		
		_flus[i] = null;
		_flus2[i] = null;
		_flus2_minus[i] = null;
		_flus2_plus[i] = null;
		_flus2_remove[i] = true;				
	}

	private static HashMap<String, String> tick_price_basic(String symbol_, double price_, HashMap<String, String> db_, HashMap<String, String> vals_) { return tick_basic(symbol_, price_, db_, vals_, true); }

	private static HashMap<String, String> __tick_price_flus(String symbol_, double price_, HashMap<String, String> db_, HashMap<String, String> vals_)
	{
		String col_price = db_ib.async_data.get_col(db_ib.async_data.FLUS_PRICE);
		
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		vals.put(col_price, Double.toString(price_));
		
		double var = numbers.get_perc_hist(price_, Double.parseDouble(db_.get(col_price)));
		
		if (Math.abs(var) > MAX_VAR) return null;
		if (var == 0.0) return vals; 

		__lock();
		
		vals = tick_price_flus_flu(symbol_, vals, var);
		vals = tick_price_flus_flu2(symbol_, vals, var);

		__unlock();
		
		return vals;
	}

	private static HashMap<String, String> tick_price_flus_flu(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		
		int i = get_i_flus(symbol_);
		if (i <= async_data_quicker.WRONG_I) return vals;

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

		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU), Double.toString(flu));

		return vals;
	}

	private static HashMap<String, String> tick_price_flus_flu2(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = tick_price_flus_flu2_main(symbol_, vals_, var_);
		
		vals = tick_price_flus_flu2_min_max(symbol_, vals, var_);

		return vals;
	}
	
	private static HashMap<String, String> tick_price_flus_flu2_main(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);

		int i = get_i_flus(symbol_);
		if (i <= async_data_quicker.WRONG_I) return vals;

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
		
		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU2), Double.toString(flu2));
		
		return vals;
	}

	private static HashMap<String, String> tick_price_flus_flu2_min_max(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);

		int i = get_i_flus(symbol_);
		if (i <= async_data_quicker.WRONG_I) return vals;
		
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

		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU2_MIN), Double.toString(min));
		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU2_MAX), Double.toString(max));
		
		return vals;
	}

	private static HashMap<String, String> tick_size_basic(String symbol_, double volume_, HashMap<String, String> db_) { return tick_basic(symbol_, volume_, db_, new HashMap<String, String>(), false); }
	
	private static HashMap<String, String> tick_basic(String symbol_, double val_, HashMap<String, String> db_, HashMap<String, String> vals_, boolean is_price_)
	{
		HashMap<String, String> vals = tick_basic_start(symbol_, val_, db_, vals_, is_price_);
		if (!arrays.is_ok(vals)) return null;
		
		String col = db_ib.async_data.get_col(is_price_ ? db_ib.async_data.PRICE_MIN : db_ib.async_data.VOLUME_MIN);
		double val_db = Double.parseDouble(db_.get(col));
		
		boolean is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ < val_db) vals.put(col, Double.toString(val_));
		
		col = db_ib.async_data.get_col(is_price_ ? db_ib.async_data.PRICE_MAX : db_ib.async_data.VOLUME_MAX);
		val_db = Double.parseDouble(db_.get(col));
		
		is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ > val_db) vals.put(col, Double.toString(val_));
		
		return vals;
	}

	private static HashMap<String, String> tick_basic_start(String symbol_, double val_, HashMap<String, String> db_, HashMap<String, String> vals_, boolean is_price_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);		
		
		String col_price_ini = db_ib.async_data.get_col(db_ib.async_data.PRICE_INI);
		String col_var_tot = db_ib.async_data.get_col(db_ib.async_data.VAR_TOT);
		
		HashMap<String, String> items = new HashMap<String, String>();
		
		items.put(db_ib.async_data.get_col(db_ib.async_data.PRICE), db_ib.async_data.get_col(db_ib.async_data.VOLUME));
		items.put(col_price_ini, db_ib.async_data.get_col(db_ib.async_data.VOLUME_INI));
		items.put(db_ib.async_data.get_col(db_ib.async_data.FLUS_PRICE), strings.DEFAULT);

		double price_ini = common.WRONG_PRICE;
		
		for (Entry<String, String> item: items.entrySet())
		{
			String col = (is_price_ ? item.getKey() : item.getValue());
			if (!strings.is_ok(col)) continue;
			
			boolean is_price_ini = (is_price_ && col.equals(col_price_ini));			
			double val_db = Double.parseDouble(db_.get(col));
			
			if (!tick_app_val_is_ok(val_db, is_price_)) 
			{
				vals.put(col, Double.toString(val_));
				
				if (is_price_ini) price_ini = val_;
			}
			else if (is_price_ini) price_ini = val_db;
		}

		if (is_price_) 
		{
			double var = numbers.get_perc_hist(val_, price_ini);
			if (Math.abs(var) > MAX_VAR) return null;
			
			vals.put(col_var_tot, Double.toString(var));
		}
		
		return vals;
	}

	private static boolean tick_app_val_is_ok(double val_, boolean is_price_) { return (is_price_ ? ib.common.price_is_ok(val_) : ib.common.size_is_ok(val_)); }
}