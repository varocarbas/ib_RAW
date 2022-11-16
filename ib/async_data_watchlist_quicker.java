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
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 10;
	
	static final int[] FIELDS = new int[] { async_data_quicker.PRICE_IB, async_data_quicker.VOLUME_IB, async_data_quicker.HALTED_IB };

	static final int SIZE_GLOBALS = 1000;
	static final int MAX_ID = SIZE_GLOBALS + async_data_quicker.MIN_ID - 1;	
	static final boolean INCLUDES_TIME = false;
	static final boolean INCLUDES_TIME_ELAPSED = true;
	static final boolean INCLUDES_HALTED = true;
	static final boolean INCLUDES_HALTED_TOT = false;

	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	static volatile HashMap<String, String>[] _vals = (HashMap<String, String>[])new HashMap[SIZE_GLOBALS];

	static volatile int _last_id = async_data_quicker.MIN_ID;
	static volatile boolean _only_db = false;
	static volatile boolean _check_enabled = false;
	static volatile boolean _only_essential = true;

	private static final double MAX_VAR = 30.0;
	private static final int SIZE_GLOBALS_FLUS = MAX_SIMULTANEOUS_SYMBOLS;
	private static final int MAX_I_FLUS = SIZE_GLOBALS_FLUS - 1;
	private static final int MIN_FLUS_TOT = 3;
	private static final int MAX_FLUS_TOT = 10;
	private static final double FACTOR_FLU2_ZERO = 1.5;

	private static volatile String[] _symbols_flus = new String[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus = new ArrayList[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_minus = new ArrayList[SIZE_GLOBALS_FLUS];
	@SuppressWarnings("unchecked")
	private static volatile ArrayList<Double>[] _flus2_plus = new ArrayList[SIZE_GLOBALS_FLUS];
	private static volatile boolean[] _flus2_remove = new boolean[SIZE_GLOBALS_FLUS];
	private static volatile int _last_i_flus = -1;

	private static boolean _log = async_data_quicker.DEFAULT_LOG;
	
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	
	
	public static boolean only_db() { return _only_db; }
	
	public static void only_db(boolean only_db_) { _only_db = only_db_; }	
	
	public static boolean check_enabled() { return _check_enabled; }
	
	public static void check_enabled(boolean check_enabled_) { _check_enabled = check_enabled_; }	
	
	public static boolean only_essential() { return _only_essential; }
	
	public static void only_essential(boolean only_essential_) { _only_essential = only_essential_; }	

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

	public static void _tick_size(int id_, int field_ib_, double size_, String symbol_)
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

	private static int __get_i_flus(String symbol_) { return _get_i_flus(symbol_, true); }
	
	private static int _get_i_flus(String symbol_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		int i = async_data_quicker.get_id_i(symbol_, _symbols_flus, _last_i_flus, MAX_I_FLUS, false); 

		if (lock_) __unlock();
		
		return i;
	}

	private static void start_globals_flus(String symbol_)
	{
		int i = _last_i_flus + 1;
		if (i > MAX_I_FLUS) i = 0;
		
		_symbols_flus[i] = symbol_;
		
		_flus[i] = new ArrayList<Double>();
		_flus2_minus[i] = new ArrayList<Double>();
		_flus2_plus[i] = new ArrayList<Double>();
		_flus2_remove[i] = true;		
	
		_last_i_flus = i;
	}

	private static void stop_globals_flus(String symbol_)
	{
		int i = _get_i_flus(symbol_, false);
		if (i <= async_data_quicker.WRONG_I) return;

		_symbols_flus[i] = null;
		
		_flus[i] = null;
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

		double price_old = Double.parseDouble(db_.get(col_price));	
		double var = (common.price_is_ok(price_old) ? numbers.get_perc_hist(price_, price_old) : 0.0);
		int i = __get_i_flus(symbol_);

		boolean is_wrong = (var == 0.0 || i <= async_data_quicker.WRONG_I);
		if (is_wrong || Math.abs(var) > MAX_VAR) return (is_wrong ? vals : null);		

		vals = __tick_price_flus_flu(symbol_, i, vals, var);
		vals = __tick_price_flus_flu2(symbol_, i, vals, var);
		
		return vals;
	}

	private static HashMap<String, String> __tick_price_flus_flu(String symbol_, int i_, HashMap<String, String> vals_, double var_)
	{		
		__lock();		

		ArrayList<Double> flus_i = new ArrayList<Double>(_flus[i_]);

		__unlock();
		
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);

		int tot = flus_i.size();
		if (tot > MAX_FLUS_TOT) 
		{
			flus_i.remove(0);

			tot--;
		}
		
		flus_i.add(Math.abs(var_));		
		
		tot++;
		if (tot < MIN_FLUS_TOT) 
		{
			__lock();		

			_flus[i_] = new ArrayList<Double>(flus_i);

			__unlock();
			
			return vals;
		}
		
		double flu = 0.0;
		for (double val: flus_i) { flu += val; }
		
		flu /= (double)tot;

		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU), Double.toString(numbers.round(flu)));

		__lock();		
		
		_flus[i_] = new ArrayList<Double>(flus_i);
		
		__unlock();

		return vals;
	}

	private static HashMap<String, String> __tick_price_flus_flu2(String symbol_, int i_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = __tick_price_flus_flu2_main(symbol_, i_, vals_, var_);
		
		vals = tick_price_flus_flu2_min_max(symbol_, vals, var_);

		return vals;
	}
	
	private static HashMap<String, String> __tick_price_flus_flu2_main(String symbol_, int i_, HashMap<String, String> vals_, double var_)
	{
		__lock();

		ArrayList<Double> flus2_minus_i = new ArrayList<Double>(_flus2_minus[i_]);
		ArrayList<Double> flus2_plus_i = new ArrayList<Double>(_flus2_plus[i_]);
		boolean flus2_remove_i = _flus2_remove[i_];

		__unlock();
		
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		
		int tot_minus = flus2_minus_i.size();
		int tot_plus = flus2_plus_i.size();
		
		if ((tot_minus + tot_plus) > MAX_FLUS_TOT)
		{
			if (flus2_remove_i)
			{
				if (tot_plus > 0) 
				{
					flus2_remove_i = false;
					flus2_plus_i.remove(0);
					
					tot_plus--;
				}
				else if (tot_minus > 0)
				{
					flus2_minus_i.remove(0);
					
					tot_minus--;
				}
			}
			else
			{
				if (tot_minus > 0) 
				{
					flus2_remove_i = true;
					flus2_minus_i.remove(0);
					
					tot_minus--;
				}
				else if (tot_plus > 0)
				{
					flus2_plus_i.remove(0);
					
					tot_plus--;
				}
			}
		}

		if (var_ > 0) 
		{
			flus2_plus_i.add(var_);
			
			tot_plus++;
		}
		else 
		{
			flus2_minus_i.add(var_);
			
			tot_minus++;
		}
				
		if ((tot_plus + tot_minus) < MIN_FLUS_TOT) 
		{
			__lock();

			_flus2_minus[i_] = new ArrayList<Double>(flus2_minus_i);
			_flus2_plus[i_] = new ArrayList<Double>(flus2_plus_i);
			_flus2_remove[i_] = flus2_remove_i;

			__unlock();
			
			return vals;
		}

		double flu2 = 0.0;		
		double flu2_plus = 0.0;
		double flu2_minus = 0.0;
		
		for (double val: flus2_plus_i) { flu2_plus += val; }		
		flu2_plus = (tot_plus == 0 ? 0.0 : (flu2_plus / (double)tot_plus));
		
		for (double val: flus2_minus_i) { flu2_minus += val; }		
		flu2_minus = (tot_minus == 0 ? 0.0 : (flu2_minus / (double)tot_minus));
		
		if (tot_plus > 0 && tot_minus > 0) flu2 = flu2_plus / Math.abs(flu2_minus);
		else
		{
			if (tot_minus == 0) flu2 = (tot_plus == 1 ? 1.0 : ((double)tot_plus / FACTOR_FLU2_ZERO));
			else if (tot_plus == 0) flu2 = Math.pow(FACTOR_FLU2_ZERO, -1.0 * (double)tot_minus);
		}
		
		vals.put(db_ib.async_data.get_col(db_ib.async_data.FLU2), Double.toString(numbers.round(flu2)));

		__lock();

		_flus2_minus[i_] = new ArrayList<Double>(flus2_minus_i);
		_flus2_plus[i_] = new ArrayList<Double>(flus2_plus_i);
		_flus2_remove[i_] = flus2_remove_i;

		__unlock();
		
		return vals;
	}

	private static HashMap<String, String> tick_price_flus_flu2_min_max(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);

		String col_min = db_ib.async_data.get_col(db_ib.async_data.FLU2_MIN);
		String col_max = db_ib.async_data.get_col(db_ib.async_data.FLU2_MAX);

		HashMap<String, String> db = db_ib.common.get_vals_quick(SOURCE, new String[] { col_min, col_max }, db_ib.common.get_where_symbol(SOURCE, symbol_));
		
		if (var_ < 0.0 && var_ < Double.parseDouble(db.get(col_min))) vals.put(col_min, Double.toString(numbers.round(var_)));
		else if (var_ > 0.0 && var_ > Double.parseDouble(db.get(col_max))) vals.put(col_max, Double.toString(numbers.round(var_)));
				
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