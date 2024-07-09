package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db_common;
import accessory.generic;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._types;
import accessory_ib.config;
import db_ib.temp_price;
import external_ib.calls;
import external_ib.data;

public class async_data_quicker extends parent_static
{	
	public static final double FACTOR_VOLUME = 1.0 / 1000.0;
	
	public static final double MAX_VAR = market.MAX_VAR;
	public static final double MAX_VAR_ASK_BID = 50.0;
	
	public static final int RETRIEVE_ID = common_xsync.MIN_REQ_ID_ASYNC;	
	public static final int MIN_ID = RETRIEVE_ID + 1;	
	public static final int MAX_ID = common_xsync.MAX_REQ_ID_ASYNC;	
	
	public static final long MIN_SECS_HALT_BASIC = 300l;

	public static final String CONFIG_ASK_BID_AS_PRICE = _types.CONFIG_ASYNC_DATA_ASK_BID_AS_PRICE;
	
	public static final int WRONG_ID = RETRIEVE_ID - 1;
	public static final int WRONG_I = common.WRONG_I;
	public static final int WRONG_HALTED = data.WRONG_HALTED;
	public static final int WRONG_SIZE = 0;
	
	public static final boolean DEFAULT_LOG = true;	
	public static final boolean DEFAULT_STOP_REMOVE_SYMBOL = true;	
	public static final boolean DEFAULT_ASK_BID_AS_PRICE = false;
	
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

	static volatile String _retrieve_symbol = null;
	
	static String _col_symbol = null;
	static String _col_time = null;
	static String _col_time_elapsed = null;
	static String _col_elapsed_ini = null;
	static String _col_timestamp = null;
	
	private static final long MIN_SECS_HALT = MIN_SECS_HALT_BASIC + 60l;
	
	private static volatile HashMap<String, Long> _halts = new HashMap<String, Long>();
	
	private static volatile boolean _retrieving = false;
	private static volatile boolean _retrieved = false;
	
	private static boolean _enabled = false;
	
	public static boolean enabled() { return _enabled; }
	
	public static void enable() { _enabled = true; }
	
	public static void disable() { _enabled = false; }
	
	public static boolean ask_bid_as_price() { return config.get_async_data_boolean(CONFIG_ASK_BID_AS_PRICE); }
	
	public static boolean ask_bid_as_price(boolean ask_bid_as_price_) { return config.update_async_data(CONFIG_ASK_BID_AS_PRICE, ask_bid_as_price_); }
	
	public static String __get_symbol(int id_, boolean ignore_retrieve_) { return ((!ignore_retrieve_ || id_ != RETRIEVE_ID) ? async_data_apps_quicker.__get_symbol(id_) : strings.DEFAULT); }
	
	public static boolean __start_retrieve(String app_, String symbol_) 
	{
		__lock();
		
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) 
		{
			__unlock();
			
			return output;
		}
		
		if (calls.reqMktData(RETRIEVE_ID, symbol, true)) 
		{
			_retrieving = true;
			_retrieved = false;
			
			_retrieve_symbol = symbol;
			
			temp_price.add(symbol);
		
			output = true;
			
			__unlock();
		}
		else 
		{
			__unlock();
			
			__stop_retrieve(app_, symbol, false);
		}
		
		return output;
	}
	
	public static void __stop_retrieve(String app_, String symbol_) { __stop_retrieve(app_, symbol_, true); }

	public static boolean retrieved() { return _retrieved; }

	static boolean __start(String app_, String symbol_) 
	{
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
		
		output = __start_internal(app_, symbol, !async_data_apps_quicker.update_app(app_));
		
		if (!_enabled && output) _enabled = true;
		
		return output;
	}

	static void __stop(String app_, String symbol_) { __stop(app_, symbol_, DEFAULT_STOP_REMOVE_SYMBOL); }
	
	static void __stop(String app_, String symbol_, boolean remove_symbol_) 
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return;

		async_data_apps_quicker.update_app(app_);

		__stop(WRONG_ID, symbol_, true, remove_symbol_); 
		
		_enabled = true;
	}

	static void __stop_all(String app_, String[] symbols_) { __stop_all(app_, symbols_, DEFAULT_STOP_REMOVE_SYMBOL); }
	
	static void __stop_all(String app_, String[] symbols_, boolean remove_symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return;
		
		for (String symbol: symbols_) 
		{
			if (!strings.is_ok(symbol)) continue;
			
			__stop(app_, symbol, remove_symbols_);
		}
	}
	
	static ArrayList<String> get_all_symbols(String source_) { return db_ib.async_data.get_all_symbols(source_); }
	
	static void __tick_price(int id_, int field_ib_, double price_)
	{
		if (_retrieving && field_ib_ != PRICE_IB) return;
		
		__lock();

		if (async_data_apps_quicker.is_only_halts())
		{
			__unlock();
			
			return;
		}
		
		String symbol = null;
		
		if (_retrieving)
		{
			if (strings.is_ok(_retrieve_symbol)) symbol = _retrieve_symbol;
		}
		else symbol = async_data_apps_quicker._get_symbol(id_, false);
		
		if ((symbol == null) || (!_retrieving && !async_data_apps_quicker._field_is_ok(field_ib_, false))) 
		{
			__unlock();
			
			return;
		}
		
		__unlock();
		
		double price = adapt_val(price_, field_ib_);
		if (!ib.common.price_is_ok(price)) return;

		if (_retrieving) update_retrieve(id_, symbol, price_);
		else
		{
			async_data_apps_quicker.tick_price(id_, field_ib_, price, symbol);	

			_update(id_, symbol, field_ib_, price);			
		}
	}
	
	static void __tick_size(int id_, int field_ib_, int size_)
	{
		if (_retrieving) return;
		
		__lock();
		
		if (async_data_apps_quicker.is_only_halts())
		{
			__unlock();
			
			return;
		}
		
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
		if (_retrieving) return;
		
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
	
	static int get_max_id(int min_id_, int size_globals_) { return (min_id_ + size_globals_ - 1); }

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
			min = async_data_apps_quicker.get_min_id();	
			wrong = WRONG_ID;	
		}
		
		int output = wrong;
		
		if (!strings.is_ok(symbol_) || last_ <= wrong) return output;
		
		output = last_;

		int first = wrong;
		
		while (true)
		{	
			output++;
			if (output > max_) output = min; 

			boolean is_first = false;
			
			if (first == wrong) first = output;
			else if (output == first) is_first = true;
			
			int i = get_i(output, is_id_);
			if (i < 0) continue;
			
			if (is_first || (symbols_[i] != null && symbols_[i].equals(symbol_))) 
			{
				if (is_first) output = wrong;
				
				break;
			}
		}

		return output;
	}

	static int get_i(int id_i_, boolean is_id_) { return (is_id_ ? (id_i_ - async_data_apps_quicker.get_min_id()) : id_i_); }
	
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
	
	private static void __stop_retrieve(String app_, String symbol_, boolean cancel_data_) 
	{	
		__lock();
	
		if (cancel_data_) calls.cancelMktData(RETRIEVE_ID);
		
		temp_price.delete(symbol_);
		
		_retrieving = false; 
		_retrieved = false;
	
		__unlock();
	}
	
	private static boolean __start_internal(String app_, String symbol_, boolean first_run_)
	{
		boolean output = false;

		async_data_apps_quicker.__populate_fields_cols_cache(first_run_);

		int id = __get_new_id(symbol_);
		
		String source = async_data_apps_quicker.get_source();
	
		if (!async_data_cache_quicker.exists(symbol_)) db_ib.async_data.insert_new(source, symbol_);
		else if (!async_data_apps_quicker.__checks_enabled() || db_ib.async_data.is_enabled(source, symbol_)) db_ib.async_data.update_timestamp(source, symbol_);
		else return output;	

		return (id_is_ok(id) ? calls.reqMktData(id, symbol_, true) : output);
	}
	
	private static int __get_new_id(String symbol_)
	{
		__lock();
		
		String[] symbols = async_data_apps_quicker.get_symbols();
		
		int min_id = async_data_apps_quicker.get_min_id();
		int max_id = async_data_apps_quicker.get_max_id();

		int last_id = async_data_apps_quicker.get_last_id();
		
		int id = last_id;

		int first_id = WRONG_ID;
		
		while (true)
		{
			id++;
			if (id > max_id) id = min_id;
			
			boolean is_first = false;
			
			if (first_id == WRONG_ID) first_id = id;
			else if (id == first_id) is_first = true;
			
			int i = get_i(id, true);
			if (i < 0) continue;
			
			if (is_first || symbols[i] == null) 
			{
				if (is_first && symbols[i] != null) 
				{
					async_data_apps_quicker.stop(id, symbols[i], true, false);
					
					calls.cancelMktData(id);
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
	
	private static void update_retrieve(int id_, String symbol_, double price_) 
	{ 
		temp_price.update(symbol_, price_);
		
		_retrieved = true;
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
				if (async_data_apps_quicker.__field_is_ok(i)) vals = add_db_val(vals_, i, vals);
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
	
	private static HashMap<String, String> add_db_val(double[] in_, int i_, HashMap<String, String> out_)
	{
		HashMap<String, String> output = new HashMap<String, String>(out_);
	
		double val = in_[i_];
		
		if (i_ == async_data_quicker.PRICE_IB) val = add_db_val_last(in_, val);
		
		if (common.price_is_ok(val)) output.put(async_data_apps_quicker.get_col(i_), Double.toString(val));
		
		return output;
	}
	
	private static double add_db_val_last(double[] in_, double val_)
	{
		double output = val_;
		if (common.price_is_ok(output) || !ask_bid_as_price()) return output;
		
		double ask = in_[ASK_IB];
		double bid = in_[BID_IB];
		
		if 
		(
			common.price_is_ok(ask) && common.price_is_ok(bid) && 
			(accessory.numbers.get_perc(ask, bid, false, true) < MAX_VAR_ASK_BID)
		)
		{ output = ((ask > bid) ? bid : ask); }
		
		return output;
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