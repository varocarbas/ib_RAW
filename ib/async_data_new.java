package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.logs;
import accessory_ib.types;
import external_ib.calls;
import external_ib.data;

public class async_data_new extends parent_static
{
	public static final String CONFIG_SNAPSHOT_NONSTOP = types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP;

	public static final int PRICE_IB = external_ib.data.TICK_LAST;
	public static final int OPEN_IB = external_ib.data.TICK_OPEN;
	public static final int CLOSE_IB = external_ib.data.TICK_CLOSE;
	public static final int LOW_IB = external_ib.data.TICK_LOW;
	public static final int HIGH_IB = external_ib.data.TICK_HIGH;
	public static final int ASK_IB = external_ib.data.TICK_ASK;
	public static final int BID_IB = external_ib.data.TICK_BID;
	public static final int HALTED_IB = external_ib.data.TICK_HALTED;
	public static final int VOLUME_IB = external_ib.data.TICK_VOLUME;
	public static final int SIZE_IB = external_ib.data.TICK_LAST_SIZE;
	public static final int ASK_SIZE_IB = external_ib.data.TICK_ASK_SIZE;
	public static final int BID_SIZE_IB = external_ib.data.TICK_BID_SIZE;
	
	public static final String PRICE = db_ib.common.FIELD_PRICE;
	public static final String OPEN = db_ib.common.FIELD_OPEN;
	public static final String CLOSE = db_ib.common.FIELD_CLOSE;
	public static final String LOW = db_ib.common.FIELD_LOW;
	public static final String HIGH = db_ib.common.FIELD_HIGH;
	public static final String ASK = db_ib.common.FIELD_ASK;
	public static final String BID = db_ib.common.FIELD_BID;
	public static final String HALTED = db_ib.common.FIELD_HALTED;
	public static final String VOLUME = db_ib.common.FIELD_VOLUME;
	public static final String SIZE = db_ib.common.FIELD_SIZE;
	public static final String ASK_SIZE = db_ib.common.FIELD_ASK_SIZE;
	public static final String BID_SIZE = db_ib.common.FIELD_BID_SIZE;

	public static final String HALTED_TOT = db_ib.common.FIELD_HALTED_TOT;
	public static final String TIME = db_ib.common.FIELD_TIME;
	public static final String TIME_ELAPSED = db_ib.common.FIELD_TIME_ELAPSED;
	public static final String ELAPSED_INI = db_ib.common.FIELD_ELAPSED_INI;

	public static final String PRICE_INI = async_data_watchlist_new.PRICE_INI;
	public static final String PRICE_MIN = async_data_watchlist_new.PRICE_MIN;
	public static final String PRICE_MAX = async_data_watchlist_new.PRICE_MAX;
	public static final String VOLUME_INI = async_data_watchlist_new.VOLUME_INI;
	public static final String VOLUME_MIN = async_data_watchlist_new.VOLUME_MIN;
	public static final String VOLUME_MAX = async_data_watchlist_new.VOLUME_MAX;
	
	public static final String FLU = async_data_watchlist_new.FLU;
	public static final String FLU_PRICE = async_data_watchlist_new.FLU_PRICE;
	public static final String FLU2 = async_data_watchlist_new.FLU2;
	public static final String FLU2_MIN = async_data_watchlist_new.FLU2_MIN;
	public static final String FLU2_MAX = async_data_watchlist_new.FLU2_MAX;
	public static final String VAR_TOT = async_data_watchlist_new.VAR_TOT;
	
	public static final String TYPE_SNAPSHOT = parent_async_data.TYPE_SNAPSHOT;
	public static final String TYPE_STREAM = parent_async_data.TYPE_STREAM;

	public static final double MULTIPLIER_VOLUME = 1.0 / 1000.0;
	
	public static final int WRONG_ID = common.WRONG_ID;
	public static final int WRONG_DATA = data.WRONG_DATA;
	public static final int WRONG_HALTED = data.WRONG_HALTED;
	
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final int DEFAULT_PAUSE_NONSTOP = 0;
	public static final boolean DEFAULT_ENABLED = false;
	public static final int DEFAULT_MAX_MINS_INACTIVE = 1;
	public static final boolean DEFAULT_DISABLE_ASAP = true;
	
	private static volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, String> _apps = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, String> _types = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();
	private static volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	private static volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();

	private static HashMap<Integer, String> _fields = new HashMap<Integer, String>();
	private static HashMap<String, String> _cols = new HashMap<String, String>();

	static boolean _start(String app_, String symbol_, String type_, int data_type_, boolean lock_)
	{
		perform_initial_actions(app_, symbol_);
	
		return _start_internal(app_, symbol_, type_, data_type_, lock_);
	}
	
	static boolean __tick_price(String app_, int id_, int field_ib_, double price_)
	{
		__lock();
		
		boolean output = false;
		
		if (!_get_app(id_, false).equals(app_) || !field_is_ok(app_, field_ib_))
		{
			__unlock();
			
			return output;
		}
		
		String symbol = _get_symbol(id_, false);
		String type = _get_type(id_, false);
		
		double price = adapt_val(price_, field_ib_);
		String field = get_field(field_ib_);	

		if (!strings.are_ok(new String[] { symbol, type, field }) || price <= common.WRONG_PRICE)
		{
			__unlock();
			
			return output;			
		}
		
		update(app_, id_, symbol, field, price, type.equals(TYPE_SNAPSHOT));

		tick_price_specific(app_, id_, field_ib_, price_, symbol);
		
		__unlock();
		
		return output;
	}

	static boolean __tick_size(String app_, int id_, int field_ib_, int size_)
	{	
		__lock();
	
		boolean output = false;
		
		if (!_get_app(id_, false).equals(app_) || !field_is_ok(app_, field_ib_))
		{
			__unlock();
			
			return output;
		}
		
		String symbol = _get_symbol(id_, false);
		String type = _get_type(id_, false);
		
		double size = adapt_val(size_, field_ib_);
		String field = get_field(field_ib_);	

		if (!strings.are_ok(new String[] { symbol, type, field }) || size <= common.WRONG_SIZE)
		{
			__unlock();
			
			return output;			
		}
		
		update(app_, id_, symbol, field, size, type.equals(TYPE_SNAPSHOT));

		tick_size_specific(app_, id_, field_ib_, size_, symbol);
		
		__unlock();
		
		if (field_ib_ == parent_async_data.VOLUME_IB) __end_snapshot(app_, id_, symbol);
		
		return output;
	}
	
	static boolean __tick_generic(String app_, int id_, int field_ib_, double value_)
	{
		__lock();
	
		boolean output = false;
		
		if (!_get_app(id_, false).equals(app_) || !field_is_ok(app_, field_ib_))
		{
			__unlock();
			
			return output;
		}
		
		
		String symbol = _get_symbol(id_, false);
		String type = _get_type(id_, false);
		
		double value = value_;
		String field = get_field(field_ib_);	

		if (!strings.are_ok(new String[] { symbol, type, field }) || value_ <= 0.0)
		{
			__unlock();
			
			return output;			
		}

		boolean update = true;
		
		if (field_ib_ == HALTED_IB) 
		{
			value = adapt_halted(app_, id_, value);
			update = (value != WRONG_HALTED);
		}
		
		if (update) update(app_, id_, symbol, field, value, type.equals(TYPE_SNAPSHOT));

		__unlock();
		
		return output;
	}

	static void __end_snapshot(String app_, int id_) { __end_snapshot(app_, id_, _get_symbol(id_, false)); }

	static void __end_snapshot(String app_, int id_, String symbol_)
	{
		__lock();
		
		boolean restart = (snapshot_is_nonstop() && _symbol_is_running(app_, symbol_, false));
		int data_type = _get_data_type(id_, false);
		
		__unlock();
		
		if (data_type <= WRONG_DATA) return;
		
		__lock();		
		
		Object vals = arrays.get_new(is_quick(app_) ? _vals_quick : _vals);
		update_db(app_, id_, symbol_, vals);
		
		stop_common(app_, id_, symbol_);
		
		if (restart) _start_internal(app_, symbol_, TYPE_SNAPSHOT, data_type, false);
		
		__unlock();		
	}
	
	static void to_screen(String app_, int id_, String symbol_, String message_) { if (logs_to_screen(app_)) logs.update_screen(id_, symbol_, (app_ + misc.SEPARATOR_CONTENT + message_)); }

	static void perform_initial_actions(String app_, String symbol_) 
	{
		if (includes_time_elapsed(app_))
		{
			String source = get_source(app_);
			boolean is_quick = is_quick(app_);
			
			if (db_ib.async_data.exists(source, symbol_)) async_data_new.update_elapsed_ini(source, symbol_, is_quick);
		}
	}
	
	private static String _get_app(int id_, boolean lock_)
	{
		if (lock_) __lock();
		
		String output = (_apps.containsKey(id_) ? _apps.get(id_) : strings.DEFAULT);
		
		if (lock_) __unlock();
		
		return output;
	}
	
	private static String _get_symbol(int id_, boolean lock_)
	{
		if (lock_) __lock();
		
		String output = (_symbols.containsKey(id_) ? _symbols.get(id_) : strings.DEFAULT);
		
		if (lock_) __unlock();
		
		return output;
	}

	private static String _get_type(int id_, boolean lock_)
	{
		if (lock_) __lock();
		
		String output = (_types.containsKey(id_) ? _types.get(id_) : strings.DEFAULT);
		
		if (lock_) __unlock();
		
		return output;
	}

	private static int _get_data_type(int id_, boolean lock_)
	{
		if (lock_) __lock();
		
		int output = (_data_types.containsKey(id_) ? _data_types.get(id_) : WRONG_DATA);
		
		if (lock_) __unlock();
		
		return output;
	}
	
	private static boolean _start_internal(String app_, String symbol_, String type_, int data_type_, boolean lock_)
	{
		boolean started = false;

		int id = _start_get_id(app_, symbol_, type_, data_type_, lock_);
		if (id <= WRONG_ID) return started; 
		
		started = calls.reqMktData(id, symbol_, type_.equals(TYPE_SNAPSHOT));
		
		if (started) to_screen(app_, id, symbol_, "started");
		
		return started;
	}

	private static int _start_get_id(String app_, String symbol_, String type_, int data_type_, boolean lock_)
	{
		int id = WRONG_ID;
		
		if (lock_) __lock();
	
		String source = get_source(app_);
		boolean is_quick = is_quick(app_);
		
		if (!db_ib.async_data.exists(source, symbol_)) 
		{
			if (is_quick) db_ib.async_data.insert_quick(source, symbol_);
			else db_ib.async_data.insert(source, symbol_);
		}
		else if (!db_ib.async_data.is_enabled(source, symbol_)) 
		{
			if (lock_) __unlock();
			
			return id;
		}

		id = async.get_req_id();
		
		_apps.put(id, app_);
		_symbols.put(id, symbol_);
		_types.put(id, type_);
		_data_types.put(id, data_type_);
		
		if (lock_) __unlock();
		
		return id;
	}

	private static boolean snapshot_is_nonstop() { return config.get_async_boolean(CONFIG_SNAPSHOT_NONSTOP); }

	private static double adapt_val(double val_, int field_ib_)
	{
		double output = val_;

		if (field_ib_ == VOLUME_IB) 
		{
			output *= MULTIPLIER_VOLUME;
			output = db_ib.common.adapt_number(output, VOLUME);
		}
		else if (field_ib_ == PRICE_IB) output = db_ib.common.adapt_number(output, PRICE);
		
		return output;
	}

	private static int adapt_halted(String app_, int id_, double val_)
	{
		int output = WRONG_HALTED;
		if (!includes_halted(app_)) return output;
		
		int val = (int)val_;
		String symbol = _get_symbol(id_, false);

		String source = get_source(app_);
		boolean is_quick = is_quick(app_);
		
		boolean halted = data.is_halted(val);
		boolean halted_db = db_ib.async_data.is_halted(source, symbol, is_quick);
	
		if (halted == halted_db) return output;		
		output = val;
		
		if (includes_halted_tot(app_) && halted && !halted_db) db_ib.async_data.update_halted_tot(source, symbol, is_quick);		

		return output;
	}
	
	private static void update(String app_, int id_, String symbol_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_)) return;  
		
		if (is_snapshot_) update_vals(app_, id_, field_, val_);
		else update_db(app_, id_, symbol_, field_, val_); 
	}

	private static void update_vals(String app_, int id_, String field_, double val_)
	{	
		boolean is_quick = is_quick(app_);

		if (is_quick && _vals_quick.containsKey(id_)) _vals_quick.get(id_).put(get_col(field_), Double.toString(val_));
		else if (!is_quick && _vals.containsKey(id_)) _vals.get(id_).put(field_, val_);			

		to_screen_update(app_, id_, app_, false);
	}
	
	private static void update_db(String app_, int id_, String symbol_, String field_, double val_)
	{
		String source = get_source(app_);
		boolean is_quick = is_quick(app_);
		
		Object vals = db_ib.async_data.add_to_vals(source, field_, val_, null, is_quick);	

		update_db_common(app_, id_, symbol_, vals, source, is_quick);
	}
	
	private static void update_db(String app_, int id_, String symbol_, Object vals_)
	{
		String source = get_source(app_);
		boolean is_quick = is_quick(app_);
		
		update_db_common(app_, id_, symbol_, vals_, source, is_quick);
	}

	private static void update_db_common(String app_, int id_, String symbol_, Object vals_, String source_, boolean is_quick_)
	{
		Object vals = add_time(app_, symbol_, vals_, source_, is_quick_);

		db_ib.async_data.update(source_, vals, symbol_, is_quick_);
		
		to_screen_update(app_, id_, symbol_, true);
	}

	private static Object add_time(String app_, String symbol_, Object vals_, String source_, boolean is_quick_)
	{
		Object vals = arrays.get_new(vals_); 
		
		if (includes_time_elapsed(app_))
		{
			long ini = db_ib.async_data.get_elapsed_ini(source_, symbol_, is_quick_);
			if (ini <= 0) update_elapsed_ini(source_, symbol_, is_quick_);
			
			vals = db_ib.async_data.add_to_vals(source_, TIME_ELAPSED, dates.seconds_to_time((int)dates.get_elapsed(ini)), vals, is_quick_);
		}
				
		if (includes_time(app_)) vals = db_ib.async_data.add_to_vals(source_, TIME,ib.common.get_current_time(), vals, is_quick_);
	
		return vals;
	}
	
	private static void update_elapsed_ini(String source_, String symbol_, boolean is_quick_) { db_ib.async_data.update(source_, symbol_, (is_quick_ ? get_col(ELAPSED_INI) : ELAPSED_INI), dates.start_elapsed(), is_quick_); }
	
	private static void end(String app_, int id_)
	{		
		_symbols.remove(id_);
		_apps.remove(id_);
		_types.remove(id_);
		_data_types.remove(id_);
		
		if (is_quick(app_)) _vals_quick.remove(id_); 
		else _vals.remove(id_); 
	}
	
	private static void stop_common(String app_, int id_, String symbol_)
	{
		end(app_, id_);	
		
		if (disable_asap(app_) && !db_ib.async_data.contains_active(get_source(app_), get_max_mins_inactive(app_))) disable(app_);
		
		to_screen(app_, id_, symbol_, "stopped");
	}

	private static boolean _symbol_is_running(String app_, String symbol_, boolean lock_) 
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new._symbol_is_running(symbol_, lock_);		
	
		return output;
	}

	private static int get_max_mins_inactive(String app_)
	{
		int output = 0;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.get_max_mins_inactive();		
	
		return output;
	}

	private static boolean disable_asap(String app_)
	{
		boolean output = DEFAULT_DISABLE_ASAP;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.disable_asap();		
	
		return output;
	}
	
	private static void disable(String app_)
	{
		if (app_.equals(async_data_watchlist_new._APP)) async_data_watchlist_new.disable();		
	}

	private static void tick_price_specific(String app_, int id_, int field_ib_, double price_, String symbol_)
	{
		if (app_.equals(async_data_watchlist_new._APP)) async_data_watchlist_new.tick_price_specific(id_, field_ib_, price_, symbol_);	
	}

	private static void tick_size_specific(String app_, int id_, int field_ib_, double size_, String symbol_)
	{
		if (app_.equals(async_data_watchlist_new._APP)) async_data_watchlist_new.tick_size_specific(id_, field_ib_, size_, symbol_);	
	}

	private static boolean logs_to_screen(String app_) 
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.logs_to_screen();

		return output;		
	}
	
	private static boolean field_is_ok(String app_, int field_ib_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.field_is_ok(field_ib_);

		return output;
	}
	
	private static boolean is_quick(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.is_quick();

		return output;
	}

	private static String get_source(String app_)
	{
		String output = strings.DEFAULT;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.get_source();

		return output;
	}	

	private static boolean includes_time(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.includes_time();

		return output;
	}

	private static boolean includes_time_elapsed(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.includes_time_elapsed();

		return output;
	}

	private static boolean includes_halted(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.includes_halted();

		return output;
	}

	private static boolean includes_halted_tot(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist_new._APP)) output = async_data_watchlist_new.includes_halted_tot();

		return output;
	}
	
	private static String get_field(int field_ib_)
	{
		if (_fields.size() == 0) populate_fields();
		
		return (_fields.containsKey(field_ib_) ? _fields.get(field_ib_) : strings.DEFAULT);
	}
	
	private static void populate_fields()
	{
		_fields.put(PRICE_IB, PRICE);
		_fields.put(OPEN_IB, OPEN);
		_fields.put(CLOSE_IB, CLOSE);
		_fields.put(LOW_IB, LOW);
		_fields.put(HIGH_IB, HIGH);
		_fields.put(ASK_IB, ASK);
		_fields.put(BID_IB, BID);
		_fields.put(HALTED_IB, HALTED);
		_fields.put(VOLUME_IB, VOLUME);
		_fields.put(SIZE_IB, SIZE);
		_fields.put(ASK_SIZE_IB, ASK_SIZE);
		_fields.put(BID_SIZE_IB, BID_SIZE);
		
		int whatevs = external_ib.data.MAX_TICK + 100;
		
		_fields.put(whatevs, HALTED_TOT);
		
		whatevs++;
		_fields.put(whatevs, TIME);
		
		whatevs++;
		_fields.put(whatevs, TIME_ELAPSED);
		
		whatevs++;
		_fields.put(whatevs, ELAPSED_INI);
		
		whatevs++;
		_fields.put(whatevs, PRICE_INI);
		
		whatevs++;
		_fields.put(whatevs, PRICE_MIN);
		
		whatevs++;
		_fields.put(whatevs, PRICE_MAX);
		
		whatevs++;
		_fields.put(whatevs, VOLUME_INI);
		
		whatevs++;
		_fields.put(whatevs, VOLUME_MIN);
		
		whatevs++;
		_fields.put(whatevs, VOLUME_MAX);
		
		whatevs++;
		_fields.put(whatevs, FLU);
		
		whatevs++;
		_fields.put(whatevs, FLU2);
		
		whatevs++;
		_fields.put(whatevs, FLU2_MIN);

		whatevs++;
		_fields.put(whatevs, FLU2_MAX);

		whatevs++;
		_fields.put(whatevs, VAR_TOT);
	}
	
	private static String get_col(String field_) 
	{ 
		if (_cols.size() == 0) populate_cols();
		
		return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT);
	}

	private static void populate_cols() 
	{ 
		if (_fields.size() == 0) populate_fields();

		String[] sources = new String[] { async_data_watchlist_new.get_source() };
		
		for (Entry<Integer, String> item: _fields.entrySet())
		{
			String field = item.getValue();
			
			for (String source: sources)
			{
				String col = db_ib.common.get_col(source, field);	
				if (!strings.is_ok(col)) continue;
				
				_cols.put(field, col);
				
				break;
			}
		}
	}

	private static void to_screen_update(String app_, int id_, String symbol_, boolean is_db_) { to_screen(app_, id_, symbol_, (is_db_ ? "stored" : "updated")); }
}