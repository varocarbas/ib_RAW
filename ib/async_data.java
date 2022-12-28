package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.logs;
import accessory_ib.types;
import external_ib.calls;
import external_ib.data;

public abstract class async_data extends parent_static
{
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
	
	public static final String TYPE_SNAPSHOT = types.ASYNC_DATA_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_DATA_STREAM;

	public static final double FACTOR_VOLUME = 1.0 / 1000.0;

	public static final int SIZE_GLOBALS = common_xsync.MAX_REQ_ID_ASYNC - common_xsync.MIN_REQ_ID_ASYNC + 1;
	public static final int MAX_I = SIZE_GLOBALS - 1;

	public static final int WRONG_ID = common.WRONG_ID;
	public static final int WRONG_DATA = data.WRONG_DATA;
	public static final int WRONG_HALTED = data.WRONG_HALTED;
	public static final int WRONG_I = common.WRONG_I;
	
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final boolean DEFAULT_ENABLED = false;
	public static final int DEFAULT_MAX_MINS_INACTIVE = 1;
	public static final boolean DEFAULT_DISABLE_ASAP = true;
	public static final boolean DEFAULT_ONLY_ESSENTIAL = false;
	
	private static volatile int _last_i = -1;
	
	private static volatile String[] _symbols = new String[SIZE_GLOBALS];
	private static volatile String[] _apps = new String[SIZE_GLOBALS];
	private static volatile String[] _types = new String[SIZE_GLOBALS];
	private static volatile Integer[] _data_types = new Integer[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile HashMap<String, Object>[] _vals = (HashMap<String, Object>[])new HashMap[SIZE_GLOBALS];
	@SuppressWarnings("unchecked")
	private static volatile HashMap<String, String>[] _vals_quick = (HashMap<String, String>[])new HashMap[SIZE_GLOBALS];

	private static HashMap<Integer, String> _fields = new HashMap<Integer, String>();

	public static String get_symbol(int id_) { return get_string(id_, _symbols); }
	
	static boolean start(String app_, String symbol_, String type_, int data_type_)
	{
		perform_initial_actions(app_, symbol_);
		
		boolean output = start_internal(app_, symbol_, type_, data_type_, false);
	
		if (output) async_data_quicker.disable();

		return output;
	}
	
	static void tick_price(String app_, int id_, int field_ib_, double price_)
	{
		String app = get_app(id_);
		if (!app.equals(app_) || (field_ib_ != PRICE_IB && !field_is_ok(app, field_ib_))) return;
		
		String symbol = get_symbol(id_);
		String type = get_type(id_);
		
		double price = adapt_val(price_, field_ib_);
		String field = get_field(field_ib_);	

		if (!strings.are_ok(new String[] { symbol, type, field }) || price <= common.WRONG_PRICE) return;
		
		if (field_ib_ == PRICE_IB) async_data_apps.tick_price(app_, id_, price, symbol);

		update(app, id_, symbol, field, price, type.equals(TYPE_SNAPSHOT));
	}

	static void tick_size(String app_, int id_, int field_ib_, int size_)
	{
		String app = get_app(id_);
		if (!app.equals(app_)) return;
		
		String symbol = get_symbol(id_);
		String type = get_type(id_);
		
		if (!strings.are_ok(new String[] { symbol, type })) return; 

		boolean is_snapshot = type.equals(TYPE_SNAPSHOT);
		
		double size = adapt_val(size_, field_ib_);
		String field = get_field(field_ib_);	

		if (size > common.WRONG_SIZE && strings.is_ok(field) && field_is_ok(app, field_ib_))
		{
			if (field_ib_ == VOLUME_IB) async_data_apps.tick_volume(app, id_, size, symbol);			

			update(app, id_, symbol, field, size, is_snapshot);
		}
		
		if (field_ib_ == VOLUME_IB && is_snapshot) precomplete_snapshot(app, id_, symbol);
	}
	
	static void tick_generic(String app_, int id_, int field_ib_, double value_)
	{
		String app = get_app(id_);
		if (!app.equals(app_) || !field_is_ok(app, field_ib_)) return;
		
		String symbol = get_symbol(id_);
		String type = get_type(id_);
		
		double value = value_;
		String field = get_field(field_ib_);	

		if (!strings.are_ok(new String[] { symbol, type, field }) || value_ <= 0.0) return;

		boolean update = true;
		
		if (field_ib_ == HALTED_IB) 
		{
			value = adapt_halted(app, id_, value, symbol);
			update = (value != WRONG_HALTED);
		}
		
		if (update) update(app, id_, symbol, field, value, type.equals(TYPE_SNAPSHOT));
	}

	static void tick_snapshot_end(String app_, int id_) 
	{
		String app = get_app(id_);
		if (!app.equals(app_)) return;

		String symbol = get_symbol(id_);
		String type = get_type(id_);

		if (!strings.are_ok(new String[] { symbol, type }) || !type.equals(TYPE_SNAPSHOT)) return;
		
		if (!async_data_apps.only_essential(app_)) store_snapshot(app, id_, symbol);
		
		end_common(app, id_, symbol, true, true);
	}

	static void stop_all(String app_, boolean delete_from_db_) { for (String symbol: _symbols) { stop(app_, symbol, delete_from_db_); } }
	
	static void stop(String app_, String symbol_, boolean delete_from_db_)
	{
		int id = get_id(app_, symbol_);
		if (id == WRONG_ID) return; 
		
		String type = get_type(id);
		if (!strings.is_ok(type)) return;
	
		if (type.equals(TYPE_SNAPSHOT)) stop_snapshot(app_, symbol_);
		else stop_stream(app_, symbol_);
		
		if (delete_from_db_ && !symbol_is_running(app_, symbol_)) db_ib.async_data.delete(async_data_apps.get_source(app_), symbol_);
	}
	
	static void to_screen(String app_, int id_, String symbol_, String message_) 
	{ 
		if (async_data_apps.logs_to_screen(app_)) logs.update_screen(id_, symbol_, (app_ + misc.SEPARATOR_CONTENT + message_)); 
	}

	static void perform_initial_actions(String app_, String symbol_) 
	{
		async_data_apps.enable(app_);
		
		async_data_apps.remove_from_stopping(app_, symbol_);
		
		if (async_data_apps.includes_time_elapsed(app_))
		{
			String source = async_data_apps.get_source(app_);
			boolean is_quick = async_data_apps.is_quick(app_);
			
			if (db_ib.async_data.exists(source, symbol_)) async_data.update_elapsed_ini(source, symbol_, is_quick);
		}
	}

	static int get_i(String[] array_, int last_i_, int max_i_, String target_) { return get_i(array_, last_i_, max_i_, target_, null, null); }
	
	static boolean field_is_ok(String app_, int field_ib_) { return async_data_apps.populate_fields(app_).contains(field_ib_); }
	
	static ArrayList<String> get_all_symbols(String app_, boolean is_quick_) { return get_all_symbols(app_, accessory.db.DEFAULT_WHERE, is_quick_); }
	
	static ArrayList<String> get_all_symbols(String app_, String where_, boolean is_quick_) { return db_ib.async_data.get_all_symbols(async_data_apps.get_source(app_), where_, is_quick_); }
	
	static ArrayList<String> get_active_symbols(String app_, boolean is_quick_) { return get_active_symbols(app_, accessory.db.DEFAULT_WHERE, is_quick_); }
	
	static ArrayList<String> get_active_symbols(String app_, String where_, boolean is_quick_) { return db_ib.async_data.get_active_symbols(async_data_apps.get_source(app_), async_data_apps.get_max_mins_inactive(app_), where_, is_quick_); }
	
	static boolean symbol_is_running(String app_, String symbol_) { return (!async_data_apps.is_stopping(app_, symbol_) && db_ib.async_data.symbol_is_active(async_data_apps.get_source(app_), symbol_, async_data_apps.get_max_mins_inactive(app_))); }

	private static int get_i(String[] array_, int last_i_, int max_i_, String target_, String[] array2_, String target2_) 
	{
		if (last_i_ <= WRONG_I) return WRONG_I;
		
		int i = last_i_;

		while (true)
		{
			if (strings.are_equal(array_[i], target_) && (array2_ == null || strings.are_equal(array2_[i], target2_))) return i;
			
			i--;
			if (i <= WRONG_I) i = max_i_; 
			if (i == last_i_) return WRONG_I;
		}		
	}

	private static void stop_snapshot(String app_, String symbol_) 
	{
		int id = get_id(app_, symbol_);
		if (id == WRONG_ID) return;
		
		async_data_apps.add_to_stopping(app_, symbol_);

		end_common(app_, id, symbol_, true, false);
		
		to_screen(app_, id, symbol_, "snapshots stopped");
	}	
	
	private static void stop_stream(String app_, String symbol_) 
	{
		int id = get_id(app_, symbol_);
		if (id == WRONG_ID) return;
		
		calls.cancelMktData(id);
		
		end_common(app_, id, symbol_, false, true);
	}
	
	private static void precomplete_snapshot(String app_, int id_, String symbol_)
	{				
		store_snapshot(app_, id_, symbol_);
		
		to_screen(app_, id_, symbol_, "snapshot precompleted");
		
		if (async_data_apps.snapshot_nonstop(app_) && symbol_is_running(app_, symbol_)) 
		{
			int data_type = get_data_type(id_);
			if (data_type <= WRONG_DATA) return;
			
			start_internal(app_, symbol_, TYPE_SNAPSHOT, data_type, true);
		}
	}

	private static void store_snapshot(String app_, int id_, String symbol_)
	{
		if (!symbol_is_running(app_, symbol_)) return;
	
		boolean is_quick = async_data_apps.is_quick(app_);
		Object vals = get_vals(id_, is_quick);
		
		if (arrays.is_ok(vals)) update_db(app_, id_, symbol_, vals);
	}
	
	private static int get_id(String app_, String symbol_) 
	{ 
		int i = get_i(_symbols, _last_i, MAX_I, symbol_, _apps, app_);
		
		return (i == WRONG_I ? WRONG_ID : get_id(i));
	}

	private static String get_app(int id_) { return get_string(id_, _apps); }

	private static String get_type(int id_) { return get_string(id_, _types); }
	
	private static String get_string(int id_, String[] array_) 
	{ 
		String output = array_[get_i(id_)];
		
		return (output == null ? strings.DEFAULT : output); 
	}
	
	private static int get_data_type(int id_) 
	{ 
		int output = _data_types[get_i(id_)];
		
		return (output < WRONG_DATA ? WRONG_DATA : output); 
	}
	
	private static Object get_vals(int id_, boolean is_quick_) 
	{ 
		Object output = null;
		
		int i = get_i(id_);
		
		if (is_quick_ && _vals_quick[i] != null) output = new HashMap<String, String>(_vals_quick[i]);
		else if (!is_quick_ && _vals[i] != null) output = new HashMap<String, Object>(_vals[i]);		
		
		return output; 
	}
	
	private static boolean start_internal(String app_, String symbol_, String type_, int data_type_, boolean is_restart_)
	{
		boolean started = false;

		int id = start_get_id(app_, symbol_, type_, data_type_);
		if (id <= WRONG_ID) return started; 
		
		boolean is_snapshot = type_.equals(TYPE_SNAPSHOT);
		started = calls.reqMktData(id, symbol_, is_snapshot);
		
		if (started) 
		{
			String message = null;
			
			if (is_snapshot) message = "snapshot" + ((is_restart_ || !async_data_apps.snapshot_nonstop(app_)) ? "" : "s") + " started";
			else message = "stream started";
			
			to_screen(app_, id, symbol_, message);
		}
		
		return started;
	}

	private static int start_get_id(String app_, String symbol_, String type_, int data_type_)
	{
		int id = WRONG_ID;
		
		String source = async_data_apps.get_source(app_);
		boolean is_quick = async_data_apps.is_quick(app_);
		
		if (!db_ib.async_data.exists(source, symbol_)) db_ib.async_data.insert_new(source, symbol_);
		else if (!db_ib.async_data.is_enabled(source, symbol_)) return id;
		else db_ib.async_data.update_timestamp(source, symbol_, is_quick);

		id = async.get_req_id();
		
		int i = get_i(id);
		_last_i = i;
		
		populate_globals(i, app_, symbol_, type_, data_type_, is_quick);
		
		return id;
	}

	private static void populate_globals(int i_, String app_, String symbol_, String type_, int data_type_, boolean is_quick_)
	{
		_apps[i_] = app_;
		_symbols[i_] = symbol_;
		_types[i_] = type_;
		_data_types[i_] = data_type_;	
		
		if (is_quick_) _vals_quick[i_] = new HashMap<String, String>();
		else _vals[i_] = new HashMap<String, Object>();
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

	private static int adapt_halted(String app_, int id_, double val_, String symbol_)
	{
		int output = WRONG_HALTED;
		if (!async_data_apps.includes_halted(app_)) return output;
		
		int val = (int)val_;

		String source = async_data_apps.get_source(app_);
		boolean is_quick = async_data_apps.is_quick(app_);
		
		boolean halted = data.is_halted(val);
		boolean halted_db = db_ib.async_data.is_halted(source, symbol_);
	
		if (halted == halted_db) return output;		
		output = val;
		
		if (async_data_apps.includes_halted_tot(app_) && halted && !halted_db) db_ib.async_data.update_halted_tot(source, symbol_, is_quick);		

		return output;
	}
	
	private static void update(String app_, int id_, String symbol_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_)) return;  
		
		boolean is_quick = async_data_apps.is_quick(app_);
		
		if (is_snapshot_) update_vals(app_, id_, field_, val_, is_quick);
		else update_db(app_, id_, symbol_, field_, val_, is_quick); 
	}

	private static void update_vals(String app_, int id_, String field_, double val_, boolean is_quick_)
	{	
		int i = get_i(id_);
		
		if (is_quick_) _vals_quick[i].put(db_ib.common.get_col(async_data_apps.get_source(app_), field_), Double.toString(val_));
		else if (!is_quick_) _vals[i].put(field_, val_);			

		to_screen_update(app_, id_, app_, false);
	}
	
	private static void update_db(String app_, int id_, String symbol_, String field_, double val_, boolean is_quick_)
	{
		String source = async_data_apps.get_source(app_);
		
		Object vals = db_ib.common.add_to_vals(async_data_apps.get_source(app_), field_, val_, null);	

		update_db_common(app_, id_, symbol_, vals, source, is_quick_);
	}
	
	private static void update_db(String app_, int id_, String symbol_, Object vals_)
	{
		String source = async_data_apps.get_source(app_);
		boolean is_quick = async_data_apps.is_quick(app_);
		
		update_db_common(app_, id_, symbol_, vals_, source, is_quick);
	}

	private static void update_db_common(String app_, int id_, String symbol_, Object vals_, String source_, boolean is_quick_)
	{
		Object vals = add_time(app_, symbol_, vals_, source_, is_quick_);		
		
		if (symbol_is_running(app_, symbol_)) 
		{
			db_ib.async_data.update(source_, vals, symbol_, is_quick_);
			
			to_screen_update(app_, id_, symbol_, true);
		}
	}

	private static Object add_time(String app_, String symbol_, Object vals_, String source_, boolean is_quick_)
	{
		Object vals = arrays.get_new(vals_); 
		
		if (async_data_apps.includes_time_elapsed(app_))
		{
			long ini = db_ib.async_data.get_elapsed_ini(source_, symbol_, false);			
			
			if (ini <= dates.ELAPSED_START) db_ib.async_data.update(source_, symbol_, db_ib.async_data.ELAPSED_INI, dates.start_elapsed(), true);
			else
			{
				String val = dates.seconds_to_time((int)dates.get_elapsed(ini));
				if (strings.is_ok(val)) vals = db_ib.common.add_to_vals(source_, db_ib.async_data.TIME_ELAPSED, val, vals);				
			}
		}
				
		if (async_data_apps.includes_time(app_)) vals = db_ib.common.add_to_vals(source_, db_ib.async_data.TIME, ib.common.get_current_time(), vals);
	
		return vals;
	}

	private static int get_id(int i_) { return get_id_i(i_, true); }

	private static int get_i(int id_) { return get_id_i(id_, false); }

	private static int get_id_i(int i_id_, boolean is_id_) { return (i_id_ + (is_id_ ? 1 : -1) * common_xsync.MIN_REQ_ID_ASYNC); }
	
	private static void update_elapsed_ini(String source_, String symbol_, boolean is_quick_) { db_ib.async_data.update(source_, symbol_, db_ib.async_data.ELAPSED_INI, dates.start_elapsed(), true); }
		
	private static void end_common(String app_, int id_, String symbol_, boolean is_snapshot_, boolean to_screen_)
	{
		if (async_data_apps.disable_asap(app_) && !db_ib.async_data.contains_active(async_data_apps.get_source(app_), async_data_apps.get_max_mins_inactive(app_))) async_data_apps.disable(app_);
			
		if (to_screen_) to_screen(app_, id_, symbol_, (is_snapshot_ ? "snapshot completed" : "stream stopped"));
	}
	
	private static String get_field(int field_ib_)
	{
		if (_fields.size() == 0) populate_fields();
		
		return (_fields.containsKey(field_ib_) ? _fields.get(field_ib_) : strings.DEFAULT);
	}
	
	private static void populate_fields()
	{
		_fields.put(PRICE_IB, db_ib.async_data.PRICE);
		_fields.put(OPEN_IB, db_ib.async_data.OPEN);
		_fields.put(CLOSE_IB, db_ib.async_data.CLOSE);
		_fields.put(LOW_IB, db_ib.async_data.LOW);
		_fields.put(HIGH_IB, db_ib.async_data.HIGH);
		_fields.put(ASK_IB, db_ib.async_data.ASK);
		_fields.put(BID_IB, db_ib.async_data.BID);
		_fields.put(HALTED_IB, db_ib.async_data.HALTED);
		_fields.put(VOLUME_IB, db_ib.async_data.VOLUME);
		_fields.put(SIZE_IB, db_ib.async_data.SIZE);
		_fields.put(ASK_SIZE_IB, db_ib.async_data.ASK_SIZE);
		_fields.put(BID_SIZE_IB, db_ib.async_data.BID_SIZE);
	}
	
	private static void to_screen_update(String app_, int id_, String symbol_, boolean is_db_) { to_screen(app_, id_, symbol_, (is_db_ ? "stored" : "updated")); }
}