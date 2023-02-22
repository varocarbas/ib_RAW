package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.arrays_quick;
import accessory.dates;
import accessory.db;
import accessory.db_cache;
import accessory.db_common;
import accessory.db_quicker_mysql;
import accessory.generic;
import accessory.parent_static;
import accessory.strings;
import external_ib.calls;
import external_ib.data;

public class async_data_quicker extends parent_static
{	
	public static final double FACTOR_VOLUME = 1.0 / 1000.0;
	
	public static final int MIN_ID = common_xsync.MIN_REQ_ID_ASYNC;	
	public static final double MAX_VAR = 30.0;
	
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

	static String COL_SYMBOL = null;
	static String COL_TIME = null;
	static String COL_TIME_ELAPSED = null;
	static String COL_ELAPSED_INI = null;

	static String[] DB_CACHE_COLS = null;
	
	private static int DB_CACHE_ID_UPDATE_MAIN = db_cache.WRONG_ID;
	
	private static boolean _enabled = false;
	
	public static boolean enabled() { return _enabled; }
	
	public static void enable() { _enabled = true; }
	
	public static void disable() { _enabled = false; }

	public static boolean db_cache_is_enabled() { return (DB_CACHE_COLS != null); }
	
	public static void enable_db_cache() { async_data_apps_quicker._populate_fields_cols_cache(false, true); }

	public static void disable_db_cache()
	{
		DB_CACHE_COLS = null;
		
		DB_CACHE_ID_UPDATE_MAIN = db_cache.WRONG_ID;
	}
	
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
		
		async_data_apps_quicker._tick_price(id_, field_ib_, price, symbol);	

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
				async_data_apps_quicker._tick_size(id_, field_ib_, size, symbol);
				
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

		__unlock();
		
		double value = value_;
		
		if (field_ib_ == HALTED_IB) 
		{
			value = __adapt_halted(id_, value, symbol);
			if (value == WRONG_HALTED) return;
		}		
		
		_update(id_, symbol, field_ib_, value, true);
	}
	
	static void __tick_snapshot_end(int id_) 
	{
		String symbol = async_data_apps_quicker.__get_symbol(id_);
		if (symbol == null) return;
		
		__complete_snapshot(id_, symbol);
	}	
	
	static void update_db(String symbol_, HashMap<String, String> vals_) { db_ib.async_data.update(async_data_apps_quicker.get_source(), symbol_, vals_, true); }

	static boolean execute_db_cache(int id_, String symbol_, HashMap<String, String> vals_)
	{
		boolean output = false;
		
		if (db_cache_is_enabled() && id_ > db_cache.WRONG_ID)
		{
			output = true;

			db_cache.execute(id_, get_db_cache_cols(id_), get_db_cache_vals(symbol_, vals_));
		} 
	
		return output;
	}

	private static String[] get_db_cache_vals(String symbol_, HashMap<String, String> vals_)
	{
		int tot = DB_CACHE_COLS.length;
		
		String[] output = new String[tot];
		
		for (int i = 0; i < tot; i++)
		{
			String col = DB_CACHE_COLS[i];
			if (!strings.is_ok(col)) continue;
			
			String val = "0";
			if (col.equals(COL_SYMBOL)) val = symbol_;
			else if (vals_.containsKey(col)) val = vals_.get(col);
			else if (col.equals(COL_TIME)) val = get_time();
			else if (col.equals(COL_TIME_ELAPSED)) val = get_time_elapsed(async_data_apps_quicker.get_source(), symbol_);
		
			output[i] = val;
		}
		
		return output;
	}
	
	private static int get_db_cache_id(String col_)
	{
		int output = arrays_quick.get_i(DB_CACHE_COLS, col_);
		
		return (output > arrays.WRONG_I ? output : db_cache.WRONG_ID);
	}
	
	private static String[] get_db_cache_cols(int id_)
	{
		String[] cols = null;
	
		return cols;
	}
	
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
			col_ini = COL_ELAPSED_INI;

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

	static void populate_db_cache() { populate_db_cache_update(); }
	
	private static void populate_db_cache_update()
	{
		String source = async_data_apps_quicker.get_source();
		
		String query = "UPDATE " + db.get_variable(source, db.get_table(source)) + " SET ";
		
		int i = 0;
		boolean first_time = true;
		
		for (i = 0; i < async_data_quicker.DB_CACHE_COLS.length; i++)
		{
			String col = async_data_quicker.DB_CACHE_COLS[i];
			if (!strings.is_ok(col) || col.equals(COL_SYMBOL)) continue;
			
			if (first_time) first_time = false;
			else query += ", ";
			
			query = update_db_cache_query(source, query, async_data_quicker.DB_CACHE_COLS[i]);
		}
		
		query = update_db_cache_query(source, query + " WHERE ", COL_SYMBOL);

		DB_CACHE_ID_UPDATE_MAIN = db_cache.add(source, query, db_quicker_mysql.TYPE, false, true);
	}
	
	private static String update_db_cache_query(String source_, String query_, String col_)
	{
		String query = query_;

		query += db.get_variable(col_) + "=''";
		query = db_cache.add_placeholders(source_, query, col_, get_db_cache_id(col_));	

		return query;
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
		
		if (!db_ib.async_data.exists(source, symbol_)) db_ib.async_data.insert_new(source, symbol_);
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

	private static void _update(int id_, String symbol_, int field_ib_, double val_) { _update(id_, symbol_, field_ib_, val_, false); }
	
	private static void _update(int id_, String symbol_, int field_ib_, double val_, boolean force_db_) 
	{
		String col = async_data_apps_quicker.get_col(field_ib_);

		if (force_db_ || async_data_apps_quicker.__is_only_db()) _update_db(symbol_, col, val_); 
		else async_data_apps_quicker.__update_vals(id_, field_ib_, val_);
	
		log(id_, symbol_, col, val_);
	}
	
	private static void _update_db(String symbol_, String col_, double val_)
	{
		HashMap<String, String> vals = _start_db_vals(symbol_, null);
		
		vals.put(col_, Double.toString(val_));

		update_db(symbol_, vals);
	}
	
	private static HashMap<String, String> _start_db_vals(String symbol_, double[] vals_)
	{
		HashMap<String, String> vals = new HashMap<String, String>(); 
		
		if (vals != null)
		{
			for (int i = 0; i < vals_.length; i++) 
			{
				if (!async_data_apps_quicker.__field_is_ok(i)) continue;
				
				vals.put(async_data_apps_quicker.get_col(i), Double.toString(vals_[i]));
			}
		}
		
		String source = async_data_apps_quicker.get_source();
		
		if (async_data_apps_quicker.includes_time_elapsed())
		{
			String val = get_time_elapsed(source, symbol_);
			if (strings.is_ok(val)) vals.put(COL_TIME_ELAPSED, val);				
		}
				
		if (async_data_apps_quicker.includes_time()) vals.put(COL_TIME, get_time());
	
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

	private static int __adapt_halted(int id_, double val_, String symbol_)
	{
		__lock();
		
		int val = (int)val_;			
		
		boolean halted = data.is_halted(val);
		boolean halted_db = db_ib.async_data.is_halted(symbol_);
			
		if (async_data_apps_quicker.includes_halted_tot() && (halted && !halted_db)) db_ib.async_data.update_halted_tot(symbol_);		
		
		val = ((async_data_apps_quicker.includes_halted() && (halted != halted_db)) ? val : WRONG_HALTED);
	
		__unlock();
	
		return val;
	}
}