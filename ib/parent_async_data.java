package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.logs;
import accessory_ib.types;
import db_ib.async_data;
import external_ib.calls;
import external_ib.contracts;
import external_ib.data;

public abstract class parent_async_data extends parent_static
{
	public static final String CONFIG_SNAPSHOT_QUICK = types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK;
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
	public static final String VOLUME = db_ib.common.FIELD_VOLUME;
	public static final String HALTED = db_ib.common.FIELD_HALTED;
	public static final String HALTED_TOT = db_ib.common.FIELD_HALTED_TOT;
	public static final String TIME = db_ib.common.FIELD_TIME;
	public static final String TIME_ELAPSED = db_ib.common.FIELD_TIME_ELAPSED;
	public static final String ELAPSED_INI = db_ib.common.FIELD_ELAPSED_INI;

	public static final String TYPE_SNAPSHOT = types.ASYNC_DATA_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_DATA_STREAM;
	
	public static final double MULTIPLIER_VOLUME = 1.0 / 1000.0;
	
	public static final int WRONG_ID = common.WRONG_ID;
	public static final int WRONG_DATA = data.WRONG_DATA;
	public static final int WRONG_HALTED = data.WRONG_HALTED;
	
	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final boolean DEFAULT_IS_QUICK = true;
	public static final int DEFAULT_PAUSE_NONSTOP = 0;
	public static final int DEFAULT_PAUSE_AFTER_STOP_ALL = 30;
	public static final boolean DEFAULT_ENABLED = true;
	public static final boolean DEFAULT_INCLUDES = false;
	
	protected volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	protected volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();
	protected volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();
	
	protected volatile boolean _enabled = DEFAULT_ENABLED;
	protected volatile boolean _stop_all = false;
	protected volatile boolean _logs_to_screen = DEFAULT_LOGS_TO_SCREEN;
	protected volatile boolean _is_quick = DEFAULT_IS_QUICK;  
	protected volatile int _nonstop_pause = DEFAULT_PAUSE_NONSTOP;

	protected String _id = strings.DEFAULT; 
	protected String _source = strings.DEFAULT; 
	protected boolean _includes_halted = DEFAULT_INCLUDES; 
	protected boolean _includes_halted_tot = DEFAULT_INCLUDES;
	protected boolean _includes_time = DEFAULT_INCLUDES; 
	protected boolean _includes_time_elapsed = DEFAULT_INCLUDES;
	
	protected abstract HashMap<Integer, String> get_all_prices();
	protected abstract HashMap<Integer, String> get_all_sizes();
	protected abstract HashMap<Integer, String> get_all_generics();
	protected abstract String[] get_fields();

	private HashMap<String, String> _cols = new HashMap<String, String>();
	
	public static boolean snapshot_is_quick() { return config.get_async_boolean(CONFIG_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(CONFIG_SNAPSHOT_NONSTOP); }

	public static String get_type(boolean is_snapshot_) { return (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM); }

	public boolean get_enabled() { return _enabled; }
	
	public void update_enabled(boolean enabled_) { _enabled = enabled_; }
	
	protected boolean get_logs_to_screen_internal() { return _logs_to_screen; }

	protected void update_logs_to_screen_internal() { _logs_to_screen = DEFAULT_LOGS_TO_SCREEN; }

	protected void update_logs_to_screen_internal(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }

	protected boolean get_is_quick_internal() { return _is_quick; }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	protected void update_is_quick_internal() { update_is_quick_internal(DEFAULT_IS_QUICK); }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	protected void update_is_quick_internal(boolean is_quick_) { _is_quick = is_quick_; }

	protected int get_nonstop_pause_internal() { return _nonstop_pause; }

	protected void update_nonstop_pause_internal() { update_nonstop_pause_internal(DEFAULT_PAUSE_NONSTOP); }

	protected void update_nonstop_pause_internal(int nonstop_pause_) { _nonstop_pause = (nonstop_pause_ >= 0 ? nonstop_pause_ : DEFAULT_PAUSE_NONSTOP); }
	
	protected void restart_after_stop_all_internal() { restart_after_stop_all_internal(DEFAULT_PAUSE_AFTER_STOP_ALL); }
	
	protected void restart_after_stop_all_internal(int pause_secs_)
	{
		if (!_stop_all) return;
			
		if (pause_secs_ > 0) misc.pause_secs(pause_secs_);
		
		_stop_all = false;
	}
		
	protected void __tick_price_internal(int id_, int field_ib_, double price_)
	{
		if (price_ <= ib.common.WRONG_PRICE) return;

		__lock();
		
		if (!_is_ok(id_, false)) 
		{
			__unlock();
		
			return;
		}
		
		String field = get_field(get_all_prices(), field_ib_);	
		if (field != null) 
		{
			double price = adapt_val(price_, field_ib_);
			
			update(id_, field, price);
			tick_price_specific(id_, field_ib_, price);
		}
		
		__unlock();
	}
	
	protected void __tick_size_internal(int id_, int field_ib_, int size_)
	{
		if (size_ <= ib.common.WRONG_SIZE) return;

		__lock();
		
		if (!_is_ok(id_, false)) 
		{
			__unlock();
			
			return;
		}
		
		boolean is_snapshot = _is_snapshot(id_, false);
		
		String field = get_field(get_all_sizes(), field_ib_);

		if (field != null) 
		{
			double size = adapt_val(size_, field_ib_);
	
			update(id_, field, size);
			tick_size_specific(id_, field_ib_, size);
		}
	
		if (is_snapshot && field_ib_ == VOLUME_IB && snapshot_is_quick()) _stop_snapshot_internal(id_, false);
		
		__unlock();
	}
	
	protected void __tick_generic_internal(int id_, int tick_, double value_)
	{
		__lock();
		
		if (!_is_ok(id_, false)) 
		{
			__unlock();
			
			return;
		}
		
		String field = get_field(get_all_generics(), tick_);
		if (field != null) 
		{
			if (tick_ == HALTED_IB) 
			{
				int val = adapt_halted(id_, value_);
				if (val != WRONG_HALTED) update(id_, field, val);
			}
			else update(id_, field, value_);
		}
		
		__unlock();
	}

	protected int __start_snapshot_internal(String symbol_) { return __start_snapshot_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected int __start_snapshot_internal(String symbol_, int data_type_) { return _start_snapshot_internal(symbol_, data_type_, true); }

	protected int _start_snapshot_internal(String symbol_, int data_type_, boolean lock_) { return _start(symbol_, data_type_, true, lock_); }

	protected int __start_stream_internal(String symbol_) { return __start_stream_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected int __start_stream_internal(String symbol_, int data_type_) { return _start_stream_internal(symbol_, data_type_, false); }

	protected int _start_stream_internal(String symbol_, int data_type_, boolean lock_) { return _start(symbol_, data_type_, false, lock_); }

	protected boolean __stop_snapshot_internal(int id_) { return _stop_snapshot_internal(id_, true); }

	protected boolean __stop_snapshot_internal(String symbol_) { return _stop_snapshot_internal(_get_id(symbol_, true), true); }
	
	protected boolean _stop_snapshot_internal(int id_, boolean lock_) { return _stop_snapshot_internal(id_, true, lock_); }

	protected boolean _stop_snapshot_internal(int id_, boolean restart_, boolean lock_) 
	{	
		if (lock_) __lock();
		
		if (!_id_exists(id_, false)) 
		{
			if (lock_) __unlock();
			
			return false;
		}
		
		String symbol = _get_symbol(id_, false);
		
		if (!strings.is_ok(symbol)) 
		{
			if (lock_) __unlock();
			
			return false;
		}
		
		boolean restart = (restart_ && snapshot_is_nonstop() && !_stop_all);
		int data_type = (restart ? _get_data_type(id_, false) : 0);
		
		update_db(id_, symbol);
		stop_common(id_, symbol);
	
		if (lock_) __unlock();
		
		if (restart) 
		{
			if (_nonstop_pause > 0) misc.pause_secs(_nonstop_pause);
			
			_start_snapshot_internal(symbol, data_type, lock_);
		}
		
		return true;
	}
	
	protected boolean __stop_stream_internal(String symbol_) { return __stop_stream_internal(_get_id(symbol_, true)); }

	protected boolean __stop_stream_internal(int id_) { return _stop_stream_internal(id_, true); }
	
	protected boolean _stop_stream_internal(int id_, boolean lock_) 
	{
		if (lock_) __lock();
		
		if (!_id_exists(id_, false))
		{
			if (lock_) __unlock();
			
			return false;
		}
		
		if (stop_common(id_, _get_symbol(id_, false))) calls.cancelMktData(id_);
		
		if (lock_) __unlock();
		
		return true;
	}

	protected boolean _id_exists(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		boolean output = _ids.containsKey(id_);
		
		if (lock_) __unlock();
		
		return output; 
	}
	
	protected int _get_id(String symbol_, boolean lock_) 
	{
		if (lock_) __lock();

		String symbol = common.normalise_symbol(symbol_);
		
		int output = (_symbols.containsValue(symbol) ? (int)arrays.get_key(_symbols, symbol) : WRONG_ID);
		
		if (lock_) __unlock();
		
		return output; 
	}

	protected int _get_data_type(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		int output = (_data_types.containsKey(id_) ? _data_types.get(id_) : WRONG_DATA);
		
		if (lock_) __unlock();
		
		return output;
	}

	protected String _get_id_type(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();

		String output = (_ids.containsKey(id_) ? _ids.get(id_) : strings.DEFAULT);	

		if (lock_) __unlock();
		
		return output;
	}

	protected String _get_symbol(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		String output = (_symbols.containsKey(id_) ? _symbols.get(id_) : strings.DEFAULT);	
		
		if (lock_) __unlock();
		
		return output;
	}
	
	protected boolean _is_snapshot(int id_, boolean lock_) 
	{
		if (lock_) __lock();
		
		boolean output = _get_id_type(id_, false).equals(TYPE_SNAPSHOT);	
		
		if (lock_) __unlock();
		
		return output;
	}

	protected void update(int id_, String field_, double val_) { update(id_, field_, val_, _is_snapshot(id_, false)); }

	protected void update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_)) return;  
		
		if (is_snapshot_) update_vals(id_, field_, val_);
		else update_db(id_, _get_symbol(id_, false), field_, val_); 
	}
	
	protected void update_db(int id_, String symbol_)
	{
		if (_is_quick)
		{
			if (_vals_quick.containsKey(id_)) async_data.update_quick(_source, symbol_, _vals_quick.get(id_));
		}
		else
		{
			if (_vals.containsKey(id_)) async_data.update(_source, symbol_, _vals.get(id_));
		}
				
		to_screen_update(id_, symbol_, true);
	}
			
	protected void update_db(int id_, String symbol_, String field_, double val_)
	{
		Object vals = async_data.add_to_vals(_source, field_, val_, (_is_quick ? new HashMap<String, String>() : new HashMap<String, Object>()), _is_quick);	
		vals = add_time(symbol_, vals);
	
		async_data.update(_source, vals, symbol_, _is_quick);
		
		to_screen_update(id_, symbol_, true);
	}	
	
	protected void to_screen(int id_, String symbol_, String message_) { if (_logs_to_screen) logs.update_screen(id_, symbol_, (_id + misc.SEPARATOR_CONTENT + message_)); }

	protected void __stop_all_internal() { _stop_all_internal(strings.DEFAULT, true); }
	
	protected void _stop_all_internal(String symbol_, boolean lock_)
	{	
		_stop_all = true;

		misc.pause_secs(5);
		
		if (lock_) __lock();	
		
		HashMap<Integer, String> ids = new HashMap<Integer, String>(_ids);
		
		if (lock_) __unlock();
		
		if (ids.size() == 0) 
		{
			_stop_all = false;
			
			return;
		}	
		
		boolean check_symbol = strings.is_ok(symbol_);
		
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			if (check_symbol && !symbol_.equals(_get_symbol(id, lock_))) continue;
			
			if (_is_snapshot(id, lock_)) _stop_snapshot_internal(id, false, lock_);
			else _stop_stream_internal(id, lock_);	
		}
	}
	
	protected boolean remove(int id_) 
	{ 
		if (!_id_exists(id_, false)) return false;
		
		_ids.remove(id_);	
		_symbols.remove(id_);
		_data_types.remove(id_);
		
		if (_is_quick) _vals_quick.remove(id_); 
		else _vals.remove(id_); 
		
		return true;
	}

	protected boolean symbol_exists(String symbol_) { return _symbols.containsValue(symbol_); } 

	private Object add_time(String symbol_, Object vals_)
	{
		Object vals = arrays.get_new(vals_); 
		
		if (_includes_time_elapsed)
		{
			long ini = async_data.get_elapsed_ini(_source, symbol_, _is_quick);
			if (ini <= 0) async_data.update(_source, symbol_, (_is_quick ? get_col(ELAPSED_INI) : ELAPSED_INI), ini, _is_quick);
			
			vals = async_data.add_to_vals(_source, TIME_ELAPSED, dates.seconds_to_time((int)dates.get_elapsed(ini)), vals, _is_quick);
		}
				
		if (_includes_time) vals = async_data.add_to_vals(_source, TIME,ib.common.get_current_time(), vals, _is_quick);
	
		return vals;
	}

	private void tick_price_specific(int id_, int field_ib_, double price_)
	{
		if (_id.equals(async_data_watchlist._ID)) async_data_watchlist.tick_price_specific(id_, field_ib_, price_);	
		else if (_id.equals(async_data_trades._ID)) async_data_trades.tick_price_specific(id_, field_ib_, price_);	
	}
	
	private void tick_size_specific(int id_, int field_ib_, double volume_)
	{
		if (_id.equals(async_data_watchlist._ID)) async_data_watchlist.tick_size_specific(id_, field_ib_, volume_);	
	}

	private double adapt_val(double val_, int field_ib_)
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

	private int adapt_halted(int id_, double val_)
	{
		int output = WRONG_HALTED;
		if (!_includes_halted) return output;
		
		int val = (int)val_;
		String symbol = _get_symbol(id_, false);

		boolean halted = data.is_halted(val);
		boolean halted_db = db_ib.async_data.is_halted(_source, symbol, _is_quick);
	
		if (halted == halted_db) return output;		
		output = val;
		
		if (_includes_halted_tot && halted && !halted_db) async_data.update_halted_tot(_source, symbol, _is_quick);		

		return output;
	}
	
	private String get_col(String field_) 
	{ 
		if (_cols.size() == 0) populate_cols();
		
		return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT);
	}

	private void populate_cols() { _cols = db_ib.common.populate_cols(_source, get_fields()); }

	private boolean _is_ok(int id_, boolean lock_) { return (_enabled && _id_exists(id_, lock_)); }

	private int _start(String symbol_, int data_type_, boolean is_snapshot_, boolean lock_)
	{
		if (lock_) __lock();
	
		int id = WRONG_ID;
		
		String symbol = common.normalise_symbol(symbol_);
		
		if (_stop_all || !strings.is_ok(symbol) || _symbols.containsValue(symbol_)) 
		{
			if (lock_) __unlock();
			
			return id;
		}
		
		int data_type = (external_ib.data.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		id = add(symbol, data_type, is_snapshot_);

		if (!async_data.exists(_source, symbol)) 
		{
			if (_is_quick) async_data.insert_quick(_source, symbol);
			else async_data.insert(_source, symbol);
		}
		else if (!async_data.is_enabled(_source, symbol)) 
		{
			remove(id);	

			if (lock_) __unlock();
			
			return id;
		}
		
		Contract contract = contracts.get_contract(symbol);
		if (contract == null) 
		{
			remove(id);

			if (lock_) __unlock();
			
			return id;
		}

		calls.reqMarketDataType(data_type_);
		
		if (!calls.reqMktData(id, symbol_, is_snapshot_)) 
		{
			remove(id);			

			if (lock_) __unlock();
			
			return id;
		}
		
		to_screen(id, symbol, "started");

		if (lock_) __unlock();
		
		return id;		
	}

	private int add(String symbol_, int data_type_, boolean is_snapshot_)
	{
		int id = async.get_req_id();

		_ids.put(id, get_type(is_snapshot_));
		_symbols.put(id, symbol_);
		_data_types.put(id, data_type_);
		
		if (_is_quick) _vals_quick.put(id, new HashMap<String, String>());
		else _vals.put(id, new HashMap<String, Object>());
		
		return id;
	}

	private boolean stop_common(int id_, String symbol_) 
	{ 
		if (!remove(id_)) return false;
		
		to_screen(id_, symbol_, "stopped");
		
		return true;
	}
	
	private String get_field(HashMap<Integer, String> all_, int field_ib_)
	{
		String output = null;
		if (all_ == null) return output;
		
		for (Entry<Integer, String> item: all_.entrySet())
		{
			if (field_ib_ == item.getKey()) return item.getValue();
		}
		
		return output;
	}

	private void update_vals(int id_, String field_, double val_)
	{	
		if (_is_quick && arrays.key_exists(_vals_quick, id_)) _vals_quick.get(id_).put(get_col(field_), Double.toString(val_));
		else if (!_is_quick && arrays.key_exists(_vals, id_)) _vals.get(id_).put(field_, val_);			

		to_screen_update(id_, false);
	}

	private void to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, _get_symbol(id_, false), is_db_); }

	private void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
}