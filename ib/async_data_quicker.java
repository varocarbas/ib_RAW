package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db_common;
import accessory.generic;
import accessory.parent_static;
import accessory.strings;
import external_ib.calls;
import external_ib.data;

public class async_data_quicker extends parent_static
{	
	public static final double FACTOR_VOLUME = 1.0 / 1000.0;
	
	public static final double MAX_VAR = market.MAX_VAR;
	
	public static final int MIN_ID = common_xsync.MIN_REQ_ID_ASYNC;	
	public static final long MIN_SECS_HALT_BASIC = 300l;
	
	public static final int WRONG_ID = MIN_ID - 1;
	public static final int WRONG_I = common.WRONG_I;
	public static final int WRONG_HALTED = data.WRONG_HALTED;

	public static final boolean DEFAULT_LOG = true;	
	
	static final int PRICE_IB = external_ib.data.TICK_LAST;
	static final int OPEN_IB = external_ib.data.TICK_OPEN;
	static final int CLOSE_IB = external_ib.data.TICK_CLOSE;
	static final int LOW_IB = external_ib.data.TICK_LOW;
	static final int HIGH_IB = external_ib.data.TICK_HIGH;
	static final int ASK_IB = external_ib.data.TICK_ASK;
	static final int BID_IB = external_ib.data.TICK_BID;
	static final int HALTED_IB = external_ib.data.TICK_HALTED;
	static final int VOLUME_IB = external_ib.data.TICK_VOLUME;
	static final int SIZE_IB = external_ib.data.TICK_LAST_SIZE;
	static final int ASK_SIZE_IB = external_ib.data.TICK_ASK_SIZE;
	static final int BID_SIZE_IB = external_ib.data.TICK_BID_SIZE;

	static String _col_symbol = null;
	static String _col_time = null;
	static String _col_time_elapsed = null;
	static String _col_elapsed_ini = null;
	static String _col_timestamp = null;
	
	private static final long MIN_SECS_HALT = MIN_SECS_HALT_BASIC + 60l;
	
	private static HashMap<String, Long> _halts = new HashMap<String, Long>();
	
	private static boolean _enabled = false;
	
	public static boolean enabled() { return _enabled; }
	
	public static void enable() { _enabled = true; }
	
	public static void disable() { _enabled = false; }
	
	static boolean __start(String app_, String symbol_) 
	{
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
		
		output = __start_internal(app_, symbol, !async_data_apps_quicker.update_app(app_));
		
		if (!_enabled && output) _enabled = true;
		
		return output;
	}

	static void __stop(String app_, String symbol_) 
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return;

		async_data_apps_quicker.update_app(app_);

		__stop(WRONG_ID, symbol_, true, true); 
		
		_enabled = true;
	}

	static void __stop_all(String app_, String[] symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return;
		
		for (String symbol: symbols_) 
		{
			if (!strings.is_ok(symbol)) continue;
			
			__stop(app_, symbol);
		}
	}
	
	static ArrayList<String> get_all_symbols(String source_) { return db_ib.async_data.get_all_symbols(source_); }
	
	static void __tick_price(int id_, int field_ib_, double price_)
	{
		__lock();
		
		String symbol = async_data_apps_quicker._get_symbol(id_, false);
		
		if (symbol == null || !async_data_apps_quicker._field_is_ok(field_ib_, false)) 
		{
			__unlock();
			
			return;
		}
		
		__unlock();
		
		double price = adapt_val(price_, field_ib_);
		if (!ib.common.price_is_ok(price)) return;
		
		async_data_apps_quicker.tick_price(id_, field_ib_, price, symbol);	

		_update(id_, symbol, field_ib_, price);
	}

	static void __tick_size(int id_, int field_ib_, int size_)
	{
		__lock();
		
		String symbol = async_data_apps_quicker._get_symbol(id_, false);		
		
		if (symbol == null) 
		{
			__unlock();
			
			return;
		}

		boolean is_ok = async_data_apps_quicker._field_is_ok(field_ib_, false);
		
		__unlock();
		
		if (is_ok)
		{
			double size = adapt_val(size_, field_ib_);

			if (ib.common.size_is_ok(size))
			{
				async_data_apps_quicker.tick_size(id_, field_ib_, size, symbol);
				
				_update(id_, symbol, field_ib_, size);			
			}
		}
		
		if (field_ib_ == VOLUME_IB) __precomplete_snapshot(id_, symbol);
	}
	
	static void __tick_generic(int id_, int field_ib_, double value_)
	{
		__lock();
		
		String symbol = async_data_apps_quicker._get_symbol(id_, false);
		
		if (symbol == null || !async_data_apps_quicker._field_is_ok(field_ib_, false)) 
		{
			__unlock();
			
			return;	
		}
		
		boolean update = true;
		
		double value = value_;
		
		if (field_ib_ == HALTED_IB) 
		{
			value = adapt_halted(id_, value, symbol);

			update = (value != WRONG_HALTED);
		}		
		
		if (update) _update(id_, symbol, field_ib_, value, true, false);
		
		__unlock();
	}
	
	static void __tick_snapshot_end(int id_) 
	{
		String symbol = async_data_apps_quicker.__get_symbol(id_);
		if (symbol == null) return;
		
		__complete_snapshot(id_, symbol);
	}	
	
	static void update_db(String symbol_, HashMap<String, String> vals_) { db_ib.async_data.update(async_data_apps_quicker.get_source(), symbol_, vals_, true); }
	
	static void __update_db(String symbol_, HashMap<String, String> vals_) { db_ib.async_data.update(async_data_apps_quicker.get_source(), symbol_, vals_, true); }
	
	static int _get_id(String symbol_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		int id = WRONG_ID;
		
		id = get_id_i(symbol_, async_data_apps_quicker.get_symbols(), async_data_apps_quicker.get_last_id(), async_data_apps_quicker.get_max_id(), true); 
	
		if (lock_) __unlock();
		
		return id;
	}

	static int get_id_i(String symbol_, String[] symbols_, int last_, int max_, boolean is_id_) 
	{
		int min = 0;
		int wrong = WRONG_I;	
		
		if (is_id_)
		{
			min = MIN_ID;	
			wrong = WRONG_ID;	
		}
		
		if (!strings.is_ok(symbol_) || last_ <= wrong) return wrong;
		
		int id = last_;

		while (true)
		{	
			id--;
			if (id < min) id = max_; 

			boolean is_last = (id == last_);
			
			int i = get_i(id, is_id_);
			if (is_last || (symbols_[i] != null && symbols_[i].equals(symbol_))) 
			{
				if (is_last) id = wrong;
				
				break;
			}
		}

		return id;
	}

	static int get_i(int id_i_, boolean is_id_) { return (is_id_ ? (id_i_ - MIN_ID) : id_i_); }
	
	static long get_elapsed(String source_, String symbol_, String col_ini_)
	{
		long output = 0l;	
		
		long ini = dates.ELAPSED_START;
		
		String col_ini = col_ini_;
		
		if (strings.is_ok(col_ini)) ini = db_common.get_long(source_, col_ini, db_ib.common.get_where_symbol(source_, symbol_), dates.ELAPSED_START, false, true);
		else
		{			
			col_ini = _col_elapsed_ini;

			ini = db_ib.async_data.get_elapsed_ini(source_, symbol_);
		}

		if (ini <= dates.ELAPSED_START) reset_elapsed_ini(source_, symbol_, col_ini);
		else output = dates.get_elapsed(ini);
		
		return output;
	}

	static void reset_elapsed_ini(String source_, String symbol_, String col_ini_) { update_elapsed_ini(source_, symbol_, col_ini_, dates.ELAPSED_START); }
	
	static void update_elapsed_ini(String source_, String symbol_, String col_ini_, long ini_) { db_ib.async_data.update(source_, symbol_, col_ini_, Long.toString(ini_ <= dates.ELAPSED_START ? dates.start_elapsed() : ini_), false); }

	static boolean id_is_ok(int id_) { return (id_ > WRONG_ID && id_ <= async_data_apps_quicker.get_max_id()); }
	
	static HashMap<Integer, String> get_field_equivalents(int[] fields_ib_)
	{
		HashMap<Integer, String> fields = new HashMap<Integer, String>();

		for (int field_ib: fields_ib_) fields = get_field_equivalent(field_ib, fields);
				
		return fields;
	}

	private static HashMap<Integer, String> get_field_equivalent(int field_ib_, HashMap<Integer, String> fields_)
	{
		String field = null;
		
		if (field_ib_ == async_data_quicker.PRICE_IB) field = db_ib.async_data.PRICE;
		else if (field_ib_ == async_data_quicker.OPEN_IB) field = db_ib.async_data.OPEN;
		else if (field_ib_ == async_data_quicker.CLOSE_IB) field = db_ib.async_data.CLOSE;
		else if (field_ib_ == async_data_quicker.LOW_IB) field = db_ib.async_data.LOW;
		else if (field_ib_ == async_data_quicker.HIGH_IB) field = db_ib.async_data.HIGH;
		else if (field_ib_ == async_data_quicker.ASK_IB) field = db_ib.async_data.ASK;
		else if (field_ib_ == async_data_quicker.BID_IB) field = db_ib.async_data.BID;
		else if (field_ib_ == async_data_quicker.VOLUME_IB) field = db_ib.async_data.VOLUME;
		else if (field_ib_ == async_data_quicker.SIZE_IB) field = db_ib.async_data.SIZE;
		else if (field_ib_ == async_data_quicker.ASK_SIZE_IB) field = db_ib.async_data.ASK_SIZE;
		else if (field_ib_ == async_data_quicker.BID_SIZE_IB) field = db_ib.async_data.BID_SIZE;
		
		if (field != null) fields_.put(field_ib_, field);
		
		return fields_;
	}	
	
	private static void __complete_snapshot(int id_, String symbol_) { __stop(id_, symbol_, true, false); }
	
	private static void __precomplete_snapshot(int id_, String symbol_) { __stop(id_, symbol_, async_data_apps_quicker.__is_only_essential(), false); }
	
	private static void __stop(int id_, String symbol_, boolean snapshot_completed_, boolean remove_symbol_)
	{
		int id = (id_ > WRONG_ID ? id_ : _get_id(symbol_, true));
		
		if (!async_data_apps_quicker.__is_only_db()) __store_vals(id, symbol_);

		async_data_apps_quicker.__stop(id, symbol_, snapshot_completed_, remove_symbol_);
		
		if (remove_symbol_) db_ib.async_data.delete(async_data_apps_quicker.get_source(), symbol_);
	}

	private static boolean __start_internal(String app_, String symbol_, boolean first_run_)
	{
		boolean output = false;

		async_data_apps_quicker.__populate_fields_cols_cache(first_run_);
		
		String source = async_data_apps_quicker.get_source();

		int id = __get_new_id(symbol_); 

		if (!async_data_cache_quicker.exists(symbol_)) db_ib.async_data.insert_new(source, symbol_);
		else if (!async_data_apps_quicker.__checks_enabled() || db_ib.async_data.is_enabled(source, symbol_)) db_ib.async_data.update_timestamp(source, symbol_);
		else return output;			

		if (!id_is_ok(id)) return output;
		
		return calls.reqMktData(id, symbol_, true);
	}
	
	private static int __get_new_id(String symbol_)
	{
		__lock();
		
		String[] symbols = async_data_apps_quicker.get_symbols();
		
		int max_id = async_data_apps_quicker.get_max_id();

		int first_id = WRONG_ID;
		int last_id = async_data_apps_quicker.get_last_id();
		
		int id = last_id;
		
		while (true)
		{
			id++;
			if (id > max_id) id = MIN_ID;
			
			boolean is_first = false;
			
			if (first_id == WRONG_ID) first_id = id;
			else if (id == first_id) is_first = true;
			
			int i = get_i(id, true);
			
			if (is_first || symbols[i] == null) 
			{
				if (is_first && symbols[i] != null) 
				{
					calls.cancelMktData(id);
					
					async_data_apps_quicker.stop(id, symbols[i], true, false);
				}

				break;
			}
		}
		
		if (id_is_ok(id)) async_data_apps_quicker.start(symbol_, id);
		
		__unlock();
		
		return id;
	}

	private static void __store_vals(int id_, String symbol_)
	{
		double[] vals = async_data_apps_quicker.__get_vals(id_);
		if (vals == null) return;
		
		update_db(symbol_, _start_db_vals(symbol_, vals));
	}

	private static void _update(int id_, String symbol_, int field_ib_, double val_) { _update(id_, symbol_, field_ib_, val_, false, true); }
	
	private static void _update(int id_, String symbol_, int field_ib_, double val_, boolean force_db_, boolean lock_calls_) 
	{
		String col = async_data_apps_quicker.get_col(field_ib_);

		if (force_db_ || async_data_apps_quicker._is_only_db(lock_calls_)) _update_db(symbol_, col, val_); 
		else async_data_apps_quicker._update_vals(id_, field_ib_, val_, lock_calls_);
	
		log(id_, symbol_, col, val_);
	}
	
	private static void _update_db(String symbol_, String col_, double val_)
	{
		HashMap<String, String> vals = _start_db_vals(symbol_, null);
		if (vals == null) return;
		
		vals.put(col_, Double.toString(val_));

		update_db(symbol_, vals);
	}
	
	private static HashMap<String, String> _start_db_vals(String symbol_, double[] vals_)
	{
		HashMap<String, String> vals = new HashMap<String, String>(); 
	
		String source = async_data_apps_quicker.get_source();
		
		if (vals_ != null)
		{
			for (int i = 0; i < vals_.length; i++) 
			{
				if (!async_data_apps_quicker.__field_is_ok(i)) continue;
				
				vals.put(async_data_apps_quicker.get_col(i), Double.toString(vals_[i]));
			}
		}
				
		if (async_data_apps_quicker.includes_time_elapsed())
		{
			String val = get_time_elapsed(source, symbol_);
			if (strings.is_ok(val)) vals.put(_col_time_elapsed, val);				
		}
						
		if (async_data_apps_quicker.includes_time()) vals.put(_col_time, get_time());
	
		return vals;
	}
	
	private static String get_time_elapsed(String source_, String symbol_) { return dates.seconds_to_time((int)get_elapsed(source_, symbol_, null)); }
	
	private static String get_time() { return ib.common.get_current_time(); }
	
	private static void log(int id_, String symbol_, String col_, double val_)
	{
		if (!async_data_apps_quicker.log()) return;
			
		String message = dates.get_now_string(dates.FORMAT_TIME_FULL) + accessory.misc.SEPARATOR_CONTENT;
		
		message += async_data_apps_quicker.get_app() + accessory.misc.SEPARATOR_CONTENT;		
		message += symbol_ + " (" + id_ + ")" + accessory.misc.SEPARATOR_CONTENT;
		message += col_ + ": " + Double.toString(val_);
		
		generic.to_screen(message);
	}

	private static double adapt_val(double val_, int field_ib_)
	{
		double output = val_;

		if (field_ib_ == VOLUME_IB) 
		{
			output *= FACTOR_VOLUME;
			output = db_ib.common.adapt_number(output, db_ib.async_data.VOLUME);
		}
		else if (field_ib_ == PRICE_IB) output = db_ib.common.adapt_number(output, db_ib.async_data.PRICE);
		
		return output;
	}

	private static double adapt_halted(int id_, double val_, String symbol_)
	{
		boolean halted = data.is_halted((int)val_);
		boolean halted_db = db_ib.async_data.is_halted(symbol_);
			
		if (async_data_apps_quicker.includes_halted_tot()) 
		{
			boolean update = false;
			
			if (halted && !halted_db)
			{
				update = true;
				
				if (_halts.containsKey(symbol_))
				{
					if (dates.get_elapsed(_halts.get(symbol_)) < MIN_SECS_HALT) update = false;
					else _halts.remove(symbol_);
				}
				else _halts.put(symbol_, dates.start_elapsed());
			}
			else if (!halted) _halts.remove(symbol_);
			
			if (update) db_ib.async_data.update_halted_tot(symbol_);
		}
		
		return ((async_data_apps_quicker.includes_halted() && (halted != halted_db)) ? val_ : WRONG_HALTED);
	}
}