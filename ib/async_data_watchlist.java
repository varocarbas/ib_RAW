package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.numbers;
import accessory.strings;
import accessory_ib._alls;

class async_data_watchlist extends parent_async_data 
{
	public static final String SOURCE = watchlist.SOURCE;
	public static final int MAX_MINS_INACTIVE = parent_async_data.DEFAULT_MAX_MINS_INACTIVE;

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
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	public static final double MIN_FLUS_VAR = 0.005;
	public static final int MAX_FLU = 50;
	public static final int MIN_FLU = 5;
	public static final int MIN_FLU2_MAIN = 50;
	public static final int MAX_FLU2_MIN_MAX = 100;
	
	private volatile HashMap<String, ArrayList<Double>> _flus = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flus2 = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flus2_minus = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, ArrayList<Double>> _flus2_plus = new HashMap<String, ArrayList<Double>>();
	private volatile HashMap<String, Boolean> _flus2_remove = new HashMap<String, Boolean>();
	
	public static async_data_watchlist _instance = instantiate();
	
	private static boolean _instantiated = false;
	
	public static boolean start(String symbol_) 
	{
		_instance.enable();
		
		boolean started = (_instance._start_snapshot_internal(symbol_, DATA, false) != WRONG_ID);
		if (started) _instance.add_global(symbol_);
		
		return started;
	}
	
	public static boolean stop(String symbol_) 
	{ 
		if (_instance.symbol_exists(symbol_)) _instance._stop_all_internal(symbol_, false); 
		else db_ib.watchlist.delete(symbol_);
		
		_instance.remove_global(symbol_);
		
		return true;
	}
	
	public static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }
	
	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
	
	void tick_price_specific(int id_, int field_ib_, double price_)
	{
		if (field_ib_ != PRICE_IB) return;
		
		String symbol = _get_symbol(id_, false);
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol, _is_quick);
		Object vals = (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>());

		vals = tick_price_specific_basic(symbol, price_, db, vals);
		vals = tick_price_specific_flus(symbol, price_, db, vals);

		db_ib.watchlist.update(vals, symbol, _instance._is_quick);
	}

	void tick_size_specific(int id_, int field_ib_, double size_)
	{
		if (field_ib_ != VOLUME_IB) return;
		
		String symbol = _get_symbol(id_, false);
		
		HashMap<String, String> db = db_ib.watchlist.get_vals(symbol, _is_quick);
		
		db_ib.watchlist.update(tick_size_specific_basic(symbol, size_, db), symbol, _is_quick);
	}
	
	protected HashMap<Integer, String> get_all_prices() { return _alls.WATCHLIST_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.WATCHLIST_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.WATCHLIST_GENERICS; }
	
	protected String[] get_fields() { return db_ib.watchlist.get_fields(); }

	private Object tick_price_specific_basic(String symbol_, double price_, HashMap<String, String> db_, Object vals_) { return tick_specific_basic(symbol_, price_, db_, vals_, true); }

	private Object tick_price_specific_flus(String symbol_, double price_, HashMap<String, String> db_, Object vals_)
	{
		Object vals = db_ib.watchlist.add_to_vals(FLU_PRICE, price_, arrays.get_new(vals_), _is_quick);
		
		double price_db = db_ib.watchlist.get_vals_number(FLU_PRICE, db_, _is_quick);
				
		double var = numbers.get_perc_hist(price_, price_db);
		if (Math.abs(var) < MIN_FLUS_VAR) return vals;

		vals = tick_price_specific_flus_flu(symbol_, db_, vals, var);

		vals = tick_price_specific_flus_flu2(symbol_, vals, var);

		return vals;
	}

	private Object tick_price_specific_flus_flu(String symbol_, HashMap<String, String> db_, Object vals_, double var_)
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

	private Object tick_price_specific_flus_flu2(String symbol_, Object vals_, double var_)
	{
		Object vals = tick_price_specific_flus_flu2_main(symbol_, vals_, var_);
		
		vals = tick_price_specific_flus_flu2_min_max(symbol_, vals, var_);

		return vals;
	}
	
	private Object tick_price_specific_flus_flu2_main(String symbol_, Object vals_, double var_)
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

	private Object tick_price_specific_flus_flu2_min_max(String symbol_, Object vals_, double var_)
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
	
	private Object tick_size_specific_basic(String symbol_, double size_, HashMap<String, String> db_) { return tick_specific_basic(symbol_, size_, db_, (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>()), false); }
	
	private Object tick_specific_basic(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
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

	private Object tick_specific_basic_start(String symbol_, double val_, HashMap<String, String> db_, Object vals_, boolean is_price_)
	{
		Object vals = arrays.get_new(vals_);		
				
		HashMap<String, String> items = new HashMap<String, String>();
		
		items.put(PRICE, VOLUME);
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

	private boolean tick_specific_val_is_ok(double val_, boolean is_price_) { return (is_price_ ? ib.common.price_is_ok(val_) : ib.common.size_is_ok(val_)); }

	private void add_global(String symbol_)
	{
		_flus.put(symbol_, new ArrayList<Double>());
		_flus2.put(symbol_, new ArrayList<Double>());
		_flus2_minus.put(symbol_, new ArrayList<Double>());
		_flus2_plus.put(symbol_, new ArrayList<Double>());
		_flus2_remove.put(symbol_, true);		
	}

	private void remove_global(String symbol_)
	{
		if (!_flus.containsKey(symbol_)) return;
		
		_flus.remove(symbol_);
		_flus2.remove(symbol_);
		_flus2_minus.remove(symbol_);
		_flus2_plus.remove(symbol_);
		_flus2_remove.remove(symbol_);		
	}
	
	private static async_data_watchlist instantiate()
	{
		if (_instantiated) return _instance;
		
		_instantiated = true;
		
		return (async_data_watchlist)parent_async_data.instantiate(new async_data_watchlist(), SOURCE, ID_WATCHLIST, MAX_MINS_INACTIVE, true, true, parent_async_data.DEFAULT_INCLUDES, true, true, parent_async_data.DEFAULT_DISABLE_ASAP);		
	}

	private async_data_watchlist() { }
}