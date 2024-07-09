package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.db_common;
import accessory.db_quick;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import db_ib.temp_price;

abstract class async_data_watchlist_quicker extends parent_static 
{
	public static final String _APP = "watchlist";
	
	public static final String SOURCE = db_ib.watchlist.SOURCE;
	public static final int MAX_SIMULTANEOUS_SYMBOLS = 10;

	static int[] FIELDS_IB = null;
	
	static final int SIZE_GLOBALS = 500;
	static final boolean INCLUDES_TIME = false;
	static final boolean INCLUDES_TIME_ELAPSED = true;
	static final boolean INCLUDES_HALTED = false;
	static final boolean INCLUDES_HALTED_TOT = false;
	static final boolean ONLY_ESSENTIAL = true;
	static final boolean ONLY_HALTS = false;
	
	static final boolean DEFAULT_ONLY_DB = false;
	
	static String COL_FLU = null;
	static String COL_FLUS_PRICE = null;
	static String COL_FLU2 = null;
	static String COL_FLU2_MIN = null;
	static String COL_FLU2_MAX = null;
	static String COL_FLU3 = null;
	static String COL_PRICE = null;
	static String COL_PRICE_INI = null;
	static String COL_PRICE_MIN = null;
	static String COL_PRICE_MAX = null;
	static String COL_VOLUME = null;
	static String COL_VOLUME_INI = null;
	static String COL_VOLUME_MIN = null;
	static String COL_VOLUME_MAX = null;
	static String COL_VAR_TOT = null;
	
	static volatile String[] _symbols = new String[SIZE_GLOBALS];
	static volatile double[][] _vals = new double[SIZE_GLOBALS][];	
	
	static int _min_id = async_data_quicker.WRONG_ID;
	static int _max_id = async_data_quicker.WRONG_ID;

	static volatile int _last_id = get_min_id() - 1;
	static volatile boolean _only_db = DEFAULT_ONLY_DB;
	static volatile boolean _check_enabled = false;
	
	private static final int SIZE_GLOBALS_FLUS = 2 * MAX_SIMULTANEOUS_SYMBOLS;
	private static final int MAX_I_FLUS = SIZE_GLOBALS_FLUS - 1;
	private static final int MIN_FLUS_TOT = 1;
	private static final int MAX_FLUS_TOT = 300;
	private static final int MAX_MIN_FLU3 = 100;
	private static final double MIN_VAR_FLU3 = 0.25;
	private static final double FACTOR_FLU2_ZERO = 1.5;
	
	private static final int DEFAULT_MIN_ID = async_data_quicker.MIN_ID;
	
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
	
	public static int get_max_id() 
	{ 
		if (_max_id <= async_data_quicker.WRONG_ID) _max_id = async_data_quicker.get_max_id(get_min_id(), SIZE_GLOBALS);
	
		return _max_id; 
	}
	
	public static int get_min_id() 
	{ 
		if (_min_id <= async_data_quicker.WRONG_ID) _min_id = DEFAULT_MIN_ID;
		
		return _min_id; 
	}
	
	public static boolean __update_min_id(int min_id_) { return async_data_apps_quicker.__update_min_id(_APP, min_id_); }
	
	public static boolean log() { return _log; }
	
	public static void log(boolean log_) { _log = log_; }	
	
	public static boolean is_only_db() { return _only_db; }
	
	public static void __only_db(boolean only_db_) 
	{ 
		__lock();
		
		_only_db = only_db_; 
	
		__unlock();
	}	
	
	public static boolean checks_enabled() { return _check_enabled; }
	
	public static void __check_enabled(boolean check_enabled_) 
	{ 
		__lock();
		
		_check_enabled = check_enabled_; 
	
		__unlock();
	}	

	public static boolean __add(String symbol_) { return async_data_quicker.__start(_APP, symbol_); }
	
	public static void __remove(String symbol_) { async_data_quicker.__stop(_APP, symbol_); }

	public static void __remove_all() { async_data_quicker.__stop_all(_APP, _symbols); }
	
	public static ArrayList<String> get_all_symbols() { return async_data_quicker.get_all_symbols(SOURCE); }
	
	public static double __get_price(String symbol_, int pause_secs_) 
	{ 
		double price = (strings.is_ok(symbol_) ? __get_price_internal(symbol_, pause_secs_) : common.WRONG_PRICE);

		return (common.price_is_ok(price) ? price : get_price_internal(symbol_, false));  
	}

	public static double get_price(String symbol_) { return (strings.is_ok(symbol_) ? get_price_internal(symbol_, false) : common.WRONG_PRICE); }

	public static void tick_price(int id_, int field_ib_, double price_, String symbol_)
	{
		if (field_ib_ != async_data_quicker.PRICE_IB) return;
	
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_);
		HashMap<String, String> vals = new HashMap<String, String>();

		vals = tick_price_basic(symbol_, price_, db, vals);
		if (!arrays.is_ok(vals)) return;
		
		HashMap<String, String> temp = tick_price_flus(symbol_, price_, db, vals);
		if (arrays.is_ok(temp)) vals = new HashMap<String, String>(temp);
		
		async_data_quicker.update_db(symbol_, vals);
	}
	
	public static void tick_size(int id_, int field_ib_, double size_, String symbol_)
	{
		if (field_ib_ != async_data_quicker.VOLUME_IB) return;
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol_);
		
		HashMap<String, String> vals = tick_size_basic(symbol_, size_, db);
		if (!arrays.is_ok(vals)) return;
		
		async_data_quicker.update_db(symbol_, vals);
	}
	
	public static void start(String symbol_, boolean is_restart_) { start_globals(symbol_, is_restart_); }
	
	public static void stop(String symbol_, boolean remove_symbol_) { stop_globals(symbol_, remove_symbol_); }
	
	private static void start_globals(String symbol_, boolean is_restart_)
	{
		if (is_restart_) return;
		
		start_globals_flus(symbol_);
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
	
	private static void stop_globals(String symbol_, boolean remove_symbol_)
	{
		if (!remove_symbol_) return;
		
		stop_globals_flus(symbol_);		
	}

	private static void stop_globals_flus(String symbol_)
	{
		int i = get_i_flus(symbol_);
		if (i <= async_data_quicker.WRONG_I) return;

		_symbols_flus[i] = null;
		
		_flus[i] = null;
		_flus2_minus[i] = null;
		_flus2_plus[i] = null;
		_flus2_remove[i] = true;
	}
	
	private static int get_i_flus(String symbol_) { return async_data_quicker.get_id_i(symbol_, _symbols_flus, _last_i_flus, MAX_I_FLUS, false); }
	
	private static double __get_price_internal(String symbol_, int pause_secs_) 
	{ 
		double output = common.WRONG_PRICE;
		
		boolean log = log();
		
		log(false);	
		
		if (!async_data_quicker.__start_retrieve(_APP, symbol_))
		{
			log(log);
			
			return output;
		}

		long start = dates.start_elapsed();
		
		while (!async_data_quicker.retrieved() && (dates.get_elapsed(start) < pause_secs_)) { }
		
		output = get_price_internal(symbol_, true);
		
		async_data_quicker.__stop_retrieve(_APP, symbol_);
		
		log(log);
		
		return output;
	}

	private static double get_price_internal(String symbol_, boolean is_retrieve_price_) 
	{ 
		double output = 0.0;
		
		if (is_retrieve_price_) output = temp_price.get(symbol_);
		else
		{
			if (COL_PRICE == null) COL_PRICE = db_quick.get_col(SOURCE, db_ib.watchlist.PRICE);

			output = db_common.get_decimal(SOURCE, COL_PRICE, db_ib.common.get_where_symbol(SOURCE, symbol_), common.WRONG_PRICE, false, true); 			
		}
	
		return output;
	}
	
	private static HashMap<String, String> tick_price_basic(String symbol_, double price_, HashMap<String, String> db_, HashMap<String, String> vals_) { return tick_basic(symbol_, price_, db_, vals_, true); }

	private static HashMap<String, String> tick_price_flus(String symbol_, double price_, HashMap<String, String> db_, HashMap<String, String> vals_)
	{	
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		vals.put(COL_FLUS_PRICE, Double.toString(price_));

		double price_old = Double.parseDouble(db_.get(COL_FLUS_PRICE));	
		
		double var = (common.price_is_ok(price_old) ? numbers.get_perc_hist(price_, price_old) : 0.0);				
		if (var == 0.0 || Math.abs(var) > async_data_quicker.MAX_VAR) return (var == 0.0 ? vals : null);		
		
		vals = tick_price_flus_flu(symbol_, vals, var);
		vals = tick_price_flus_flu2(symbol_, vals, var);
		vals = tick_price_flus_flu3(symbol_, vals, var);
		
		return vals;
	}
	
	private static HashMap<String, String> tick_price_flus_flu(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);

		int i = get_i_flus(symbol_);		
		if (i <= async_data_quicker.WRONG_I) return vals;				

		ArrayList<Double> flus = new ArrayList<Double>(_flus[i]);
		
		int tot = flus.size();
		if (tot > MAX_FLUS_TOT) 
		{
			flus.remove(0);

			tot--;
		}
		
		flus.add(Math.abs(var_));		
		
		tot++;
		
		if (tot >= MIN_FLUS_TOT) 
		{
			double flu = 0.0;
			for (double val: flus) { flu += val; }
			
			flu /= (double)tot;

			vals.put(COL_FLU, Double.toString(numbers.round(flu)));			
		}

		_flus[i] = new ArrayList<Double>(flus);
		
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

		ArrayList<Double> flus2_minus = new ArrayList<Double>(_flus2_minus[i]);
		ArrayList<Double> flus2_plus = new ArrayList<Double>(_flus2_plus[i]);
		boolean flus2_remove = _flus2_remove[i];
		
		int tot_minus = flus2_minus.size();
		int tot_plus = flus2_plus.size();
		
		if ((tot_minus + tot_plus) > MAX_FLUS_TOT)
		{
			if (flus2_remove)
			{
				if (tot_plus > 0) 
				{
					flus2_remove = false;
					flus2_plus.remove(0);
					
					tot_plus--;
				}
				else if (tot_minus > 0)
				{
					flus2_minus.remove(0);
					
					tot_minus--;
				}
			}
			else
			{
				if (tot_minus > 0) 
				{
					flus2_remove = true;
					flus2_minus.remove(0);
					
					tot_minus--;
				}
				else if (tot_plus > 0)
				{
					flus2_plus.remove(0);
					
					tot_plus--;
				}
			}
		}

		if (var_ > 0) 
		{
			flus2_plus.add(var_);
			
			tot_plus++;
		}
		else 
		{
			flus2_minus.add(var_);
			
			tot_minus++;
		}
		
		if ((tot_plus + tot_minus) >= MIN_FLUS_TOT)
		{
			double flu2 = 0.0;		
			double flu2_plus = 0.0;
			double flu2_minus = 0.0;
			
			for (double val: flus2_plus) { flu2_plus += val; }		
			flu2_plus = (tot_plus == 0 ? 0.0 : (flu2_plus / (double)tot_plus));
			
			for (double val: flus2_minus) { flu2_minus += val; }		
			flu2_minus = (tot_minus == 0 ? 0.0 : (flu2_minus / (double)tot_minus));
			
			if (tot_plus > 0 && tot_minus > 0) flu2 = flu2_plus / Math.abs(flu2_minus);
			else
			{
				if (tot_minus == 0) flu2 = (tot_plus == 1 ? 1.0 : ((double)tot_plus / FACTOR_FLU2_ZERO));
				else if (tot_plus == 0) flu2 = Math.pow(FACTOR_FLU2_ZERO, -1.0 * (double)tot_minus);
			}
				
			vals.put(COL_FLU2, Double.toString(numbers.round(flu2)));			
		}
		
		_flus2_minus[i] = new ArrayList<Double>(flus2_minus);
		_flus2_plus[i] = new ArrayList<Double>(flus2_plus);
		_flus2_remove[i] = flus2_remove;
		
		return vals;
	}

	private static HashMap<String, String> tick_price_flus_flu2_min_max(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		
		HashMap<String, String> db = db_common.get_vals(SOURCE, new String[] { COL_FLU2_MIN, COL_FLU2_MAX }, db_ib.common.get_where_symbol(SOURCE, symbol_), accessory.db.DEFAULT_ORDER, false, true);
		
		if (var_ < 0.0 && var_ < Double.parseDouble(db.get(COL_FLU2_MIN))) vals.put(COL_FLU2_MIN, Double.toString(numbers.round(var_)));
		else if (var_ > 0.0 && var_ > Double.parseDouble(db.get(COL_FLU2_MAX))) vals.put(COL_FLU2_MAX, Double.toString(numbers.round(var_)));
				
		return vals;
	}
	
	private static HashMap<String, String> tick_price_flus_flu3(String symbol_, HashMap<String, String> vals_, double var_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		if (Math.abs(var_) < MIN_VAR_FLU3) return vals;
		
		boolean update = false;
		
		int flu3 = db_common.get_int(SOURCE, COL_FLU3, db_ib.common.get_where_symbol(SOURCE, symbol_), db.WRONG_TINYINT, false, true);
		
		if (flu3 != db.WRONG_TINYINT)
		{
			if (var_ > 0.0) 
			{
				if (flu3 < MAX_MIN_FLU3) 
				{
					flu3++;
					
					update = true;
				}
			}
			else if (var_ < 0.0) 
			{
				if (flu3 > -1 * MAX_MIN_FLU3) 
				{
					flu3--;
					
					update = true;
				}
			}
		}
		
		if (update) vals.put(COL_FLU3, Integer.toString(flu3));

		return vals;
	}

	private static HashMap<String, String> tick_size_basic(String symbol_, double volume_, HashMap<String, String> db_) { return tick_basic(symbol_, volume_, db_, new HashMap<String, String>(), false); }
	
	private static HashMap<String, String> tick_basic(String symbol_, double val_, HashMap<String, String> db_, HashMap<String, String> vals_, boolean is_price_)
	{
		HashMap<String, String> vals = tick_basic_start(symbol_, val_, db_, vals_, is_price_);
		if (!arrays.is_ok(vals)) return null;
		
		String col = (is_price_ ? COL_PRICE_MIN : COL_VOLUME_MIN);
		double val_db = Double.parseDouble(db_.get(col));
		
		boolean is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ < val_db) vals.put(col, Double.toString(val_));
		
		col = (is_price_ ? COL_PRICE_MAX : COL_VOLUME_MAX);
		val_db = Double.parseDouble(db_.get(col));
		
		is_ok_db = tick_app_val_is_ok(val_db, is_price_);
		if (!is_ok_db || val_ > val_db) vals.put(col, Double.toString(val_));
		
		return vals;
	}

	private static HashMap<String, String> tick_basic_start(String symbol_, double val_, HashMap<String, String> db_, HashMap<String, String> vals_, boolean is_price_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);		
				
		HashMap<String, String> items = new HashMap<String, String>();
		
		items.put(COL_PRICE, COL_VOLUME);
		items.put(COL_PRICE_INI, COL_VOLUME_INI);
		items.put(COL_FLUS_PRICE, strings.DEFAULT);

		double price_ini = common.WRONG_PRICE;
		
		for (Entry<String, String> item: items.entrySet())
		{
			String col = (is_price_ ? item.getKey() : item.getValue());
			if (!strings.is_ok(col)) continue;
			
			boolean is_price_ini = (is_price_ && col.equals(COL_PRICE_INI));			
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
			if (Math.abs(var) > async_data_quicker.MAX_VAR) return null;
			
			vals.put(COL_VAR_TOT, Double.toString(var));
		}
		
		return vals;
	}

	private static boolean tick_app_val_is_ok(double val_, boolean is_price_) { return (is_price_ ? ib.common.price_is_ok(val_) : ib.common.size_is_ok(val_)); }
}