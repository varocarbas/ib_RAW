package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.generic;
import accessory.parent_static;
import accessory.strings;
import external_ib.calls;
import external_ib.data;

public class async_data_quicker extends parent_static
{	
	public static final double FACTOR_VOLUME = 1.0 / 1000.0;

	public static final int MIN_ID = common_xsync.MIN_REQ_ID_ASYNC;	

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

	static final int[] FIELDS_ESSENTIAL = new int[] { PRICE_IB, VOLUME_IB, HALTED_IB };

	private static HashMap<Integer, String> _cols = new HashMap<Integer, String>();

	private static boolean _enabled = false;
	
	public static boolean enabled() { return _enabled; }
	
	public static void enable() { _enabled = true; }
	
	public static void disable() { _enabled = false; }
	
	static boolean __start(String app_, String symbol_) 
	{
		boolean output = false;
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return output;
		
		async_data_apps_quicker.update_app(app_);

		output = __start_internal(app_, symbol);
		
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
	
	static ArrayList<String> get_all_symbols(String source_) { return db_ib.async_data.get_all_symbols(source_, true); }
	
	static void __tick_price(int id_, int field_ib_, double price_)
	{
		String symbol = async_data_apps_quicker.__get_symbol(id_);		
		if ((symbol == null) || !async_data_apps_quicker._field_is_ok(field_ib_)) return;		
		
		double price = adapt_val(price_, field_ib_);
		if (!ib.common.price_is_ok(price)) return;
		
		async_data_apps_quicker.tick_price(id_, field_ib_, price, symbol);	

		_update(id_, symbol, field_ib_, price);
	}

	static void __tick_size(int id_, int field_ib_, int size_)
	{
		String symbol = async_data_apps_quicker.__get_symbol(id_);		
		if (symbol == null) return;		

		if (async_data_apps_quicker._field_is_ok(field_ib_))
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
		String symbol = async_data_apps_quicker.__get_symbol(id_);		
		if (symbol == null) return;		
		
		double value = value_;
		boolean update = async_data_apps_quicker._field_is_ok(field_ib_);
		
		if (field_ib_ == HALTED_IB) 
		{
			value = __adapt_halted(id_, value, symbol);
			if (value == WRONG_HALTED) return;
			
			if (!update && async_data_apps_quicker.includes_halted()) update = true;
		}		
		
		if (update) _update(id_, symbol, field_ib_, value, true);
	}
	
	static void __tick_snapshot_end(int id_) 
	{
		String symbol = async_data_apps_quicker.__get_symbol(id_);
		if (symbol == null) return;
		
		__complete_snapshot(id_, symbol);
	}	
	
	static String get_col(int field_ib_) 
	{
		if (_cols.size() == 0) populate_cols();
		
		return (_cols.containsKey(field_ib_) ? _cols.get(field_ib_) : strings.DEFAULT);
	}
	
	static void _update(int id_, String symbol_, HashMap<String, String> vals_) 
	{ 
		if (async_data_apps_quicker.__is_only_db()) update_db(id_, symbol_, vals_); 
		else async_data_apps_quicker.__update_vals(id_, vals_);
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
		
		if (strings.is_ok(col_ini)) 
		{
			long temp = db_ib.common.get_long_quick(source_, col_ini, db_ib.common.get_where_symbol(source_, symbol_));
			
			ini = (temp == db.WRONG_LONG ? dates.ELAPSED_START : temp);
		}
		else
		{			
			col_ini = db_ib.async_data.get_col(db_ib.async_data.ELAPSED_INI);

			ini = db_ib.async_data.get_elapsed_ini(source_, symbol_, true);
		}

		if (ini <= dates.ELAPSED_START) reset_elapsed_ini(source_, symbol_, col_ini);
		else output = dates.get_elapsed(ini);
		
		return output;
	}

	static void reset_elapsed_ini(String source_, String symbol_, String col_ini_) { update_elapsed_ini(source_, symbol_, col_ini_, dates.ELAPSED_START); }
	
	static void update_elapsed_ini(String source_, String symbol_, String col_ini_, long ini_) { db_ib.async_data.update_quick(source_, symbol_, col_ini_, Long.toString(ini_ <= dates.ELAPSED_START ? dates.start_elapsed() : ini_)); }

	static ArrayList<Integer> __get_fields(int[] fields_, boolean only_essential_) { return _get_fields(fields_, only_essential_, true); }
	
	static ArrayList<Integer> _get_fields(int[] fields_, boolean only_essential_, boolean lock_) 
	{
		if (lock_) __lock();
		
		ArrayList<Integer> output = arrays.to_arraylist(only_essential_ ? FIELDS_ESSENTIAL : fields_); 
	
		if (lock_) __unlock();
		
		return output;
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

	private static boolean __start_internal(String app_, String symbol_)
	{
		boolean output = false;

		String source = async_data_apps_quicker.get_source();

		if (!db_ib.async_data.exists(source, symbol_)) db_ib.async_data.insert_quick(source, symbol_);
		else if (!async_data_apps_quicker.__checks_enabled() || db_ib.async_data.is_enabled(source, symbol_)) db_ib.async_data.update_timestamp(source, symbol_, true);
		else return output;			

		int id = __get_new_id(symbol_); 
		if (!async_data_apps_quicker.id_is_ok(id)) return output;

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
		
		if (async_data_apps_quicker.id_is_ok(id)) async_data_apps_quicker.start(symbol_, id);
		
		__unlock();
		
		return id;
	}

	private static void __store_vals(int id_, String symbol_)
	{
		HashMap<String, String> vals = async_data_apps_quicker.__get_vals(id_);
		if (vals == null) return;
		
		update_db(id_, symbol_, start_db_vals(symbol_, vals));
	}

	private static void _update(int id_, String symbol_, int field_ib_, double val_) { _update(id_, symbol_, field_ib_, val_, false); }
	
	private static void _update(int id_, String symbol_, int field_ib_, double val_, boolean force_db_) 
	{
		String col = get_col(field_ib_);

		if (force_db_ || async_data_apps_quicker.__is_only_db()) update_db(id_, symbol_, col, val_); 
		else async_data_apps_quicker.__update_vals(id_, col, val_);
	
		log(id_, symbol_, col, val_);
	}
	
	private static void update_db(int id_, String symbol_, String col_, double val_)
	{
		HashMap<String, String> vals = start_db_vals(symbol_, null);
		
		vals.put(col_, Double.toString(val_));

		update_db(id_, symbol_, vals);
	}
		
	private static void update_db(int id_, String symbol_, HashMap<String, String> vals_) { db_ib.async_data.update_quick(async_data_apps_quicker.get_source(), symbol_, vals_); }
	
	private static HashMap<String, String> start_db_vals(String symbol_, HashMap<String, String> vals_)
	{
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_); 
		
		if (async_data_apps_quicker.includes_time_elapsed())
		{
			String val = dates.seconds_to_time((int)get_elapsed(async_data_apps_quicker.get_source(), symbol_, null));
			if (strings.is_ok(val)) vals.put(db_ib.async_data.get_col(db_ib.async_data.TIME_ELAPSED), val);				
		}
				
		if (async_data_apps_quicker.includes_time()) vals.put(db_ib.async_data.get_col(db_ib.async_data.TIME), ib.common.get_current_time());
	
		return vals;
	}
	
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
		
		String source = async_data_apps_quicker.get_source();

		int val = (int)val_;			
		
		boolean halted = data.is_halted(val);
		boolean halted_db = db_ib.async_data.is_halted(source, symbol_, true);
			
		if (async_data_apps_quicker.includes_halted_tot() && (halted && !halted_db)) db_ib.async_data.update_halted_tot(source, symbol_, true);		
		
		val = ((async_data_apps_quicker.includes_halted() && (halted != halted_db)) ? val : WRONG_HALTED);
	
		__unlock();
	
		return val;
	}

	private static void populate_cols()
	{
		_cols.put(PRICE_IB, db_ib.async_data.get_col(db_ib.async_data.PRICE));
		_cols.put(OPEN_IB, db_ib.async_data.get_col(db_ib.async_data.OPEN));
		_cols.put(CLOSE_IB, db_ib.async_data.get_col(db_ib.async_data.CLOSE));
		_cols.put(LOW_IB, db_ib.async_data.get_col(db_ib.async_data.LOW));
		_cols.put(HIGH_IB, db_ib.async_data.get_col(db_ib.async_data.HIGH));
		_cols.put(ASK_IB, db_ib.async_data.get_col(db_ib.async_data.ASK));
		_cols.put(BID_IB, db_ib.async_data.get_col(db_ib.async_data.BID));
		_cols.put(HALTED_IB, db_ib.async_data.get_col(db_ib.async_data.HALTED));
		_cols.put(VOLUME_IB, db_ib.async_data.get_col(db_ib.async_data.VOLUME));
		_cols.put(SIZE_IB, db_ib.async_data.get_col(db_ib.async_data.SIZE));
		_cols.put(ASK_SIZE_IB, db_ib.async_data.get_col(db_ib.async_data.ASK_SIZE));
		_cols.put(BID_SIZE_IB, db_ib.async_data.get_col(db_ib.async_data.BID_SIZE));
	}
}