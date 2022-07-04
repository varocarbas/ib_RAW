package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;

import accessory.arrays;
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

	public static final String TYPE_SNAPSHOT = types.ASYNC_MARKET_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_MARKET_STREAM;
	
	public static final int PRICE = external_ib.data.TICK_LAST;
	public static final int OPEN = external_ib.data.TICK_OPEN;
	public static final int CLOSE = external_ib.data.TICK_CLOSE;
	public static final int LOW = external_ib.data.TICK_LOW;
	public static final int HIGH = external_ib.data.TICK_HIGH;
	public static final int ASK = external_ib.data.TICK_ASK;
	public static final int BID = external_ib.data.TICK_BID;
	public static final int HALTED = external_ib.data.TICK_HALTED;
	public static final int VOLUME = external_ib.data.TICK_VOLUME;
	public static final int SIZE = external_ib.data.TICK_LAST_SIZE;
	public static final int ASK_SIZE = external_ib.data.TICK_ASK_SIZE;
	public static final int BID_SIZE = external_ib.data.TICK_BID_SIZE;

	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.data.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final boolean DEFAULT_IS_DB_QUICK = true;
	public static final boolean DEFAULT_ENABLED = false;
	public static final int DEFAULT_PAUSE_NONSTOP = 0;
	public static final int DEFAULT_PAUSE_AFTER_STOP_ALL = 30;
	
	public static boolean snapshot_is_quick() { return config.get_async_boolean(CONFIG_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(CONFIG_SNAPSHOT_NONSTOP); }

	public static String get_type(boolean is_snapshot_) { return (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM); }
	
	protected volatile boolean _stop_all = false;	
	protected volatile boolean _logs_to_screen = DEFAULT_LOGS_TO_SCREEN;
	protected volatile boolean _is_db_quick = DEFAULT_IS_DB_QUICK;  
	protected volatile boolean _enabled = DEFAULT_ENABLED;
	protected volatile int _nonstop_pause = DEFAULT_PAUSE_NONSTOP;
	
	protected volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	protected volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();
	protected volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();

	protected String _id = strings.DEFAULT; 
	protected String _source = strings.DEFAULT; 
	
	protected abstract HashMap<Integer, String> get_all_prices();
	protected abstract HashMap<Integer, String> get_all_sizes();
	protected abstract HashMap<Integer, String> get_all_generics();
	
	protected void enable_internal() { _enabled = true; }

	protected void disable_internal() { _enabled = false; }

	protected boolean get_logs_to_screen_internal() { return _logs_to_screen; }

	protected void update_logs_to_screen_internal() { _logs_to_screen = DEFAULT_LOGS_TO_SCREEN; }

	protected void update_logs_to_screen_internal(boolean logs_to_screen_) { _logs_to_screen = logs_to_screen_; }

	protected boolean get_is_db_quick_internal() { return _is_db_quick; }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	protected void update_is_db_quick_internal() { update_is_db_quick_internal(DEFAULT_IS_DB_QUICK); }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	protected void update_is_db_quick_internal(boolean is_db_quick_) { _is_db_quick = is_db_quick_; }

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
		if (!__is_ok(id_)) return;
		
		String field = get_field(get_all_prices(), field_ib_);	
		if (field != null) __update(id_, field, price_);
	}
	
	protected void __tick_size_internal(int id_, int field_ib_, int size_)
	{
		if (!__is_ok(id_)) return;
		
		boolean is_snapshot = __is_snapshot(id_);
		
		String field = get_field(get_all_sizes(), field_ib_);
		if (field != null) __update(id_, field, size_, is_snapshot);
	
		if (is_snapshot && field_ib_ == VOLUME && snapshot_is_quick()) __stop_snapshot_internal(id_); 
	}
	
	protected void __tick_generic_internal(int id_, int tick_, double value_)
	{
		if (!__is_ok(id_)) return;
		
		String field = get_field(get_all_generics(), tick_);
		if (field != null) __update(id_, field, value_);	
	}

	protected boolean __start_snapshot_internal(String symbol_) { return __start_snapshot_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected boolean __start_snapshot_internal(String symbol_, int data_type_) { return __start(symbol_, data_type_, true); }

	protected boolean __start_stream_internal(String symbol_) { return __start_stream_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected boolean __start_stream_internal(String symbol_, int data_type_) { return __start(symbol_, data_type_, false); }
	
	protected boolean __stop_snapshot_internal(String symbol_) { return __stop_snapshot_internal(_get_id(symbol_, true)); }
	
	protected boolean __stop_snapshot_internal(int id_) 
	{	
		if (!__is_ok(id_)) return false;
		
		__lock();
		
		String symbol = _get_symbol(id_, false);
		
		if (!strings.is_ok(symbol)) 
		{
			__unlock();
			
			return false;
		}
		
		boolean restart = (snapshot_is_nonstop() && !_stop_all);
		int data_type = (restart ? _get_data_type(id_, false) : 0);
		
		update_db(id_, symbol);
		stop_common(id_, symbol);
	
		__unlock();
		
		if (restart) 
		{
			if (_nonstop_pause > 0) misc.pause_secs(_nonstop_pause);
			
			__start_snapshot_internal(symbol, data_type);
		}
		
		return true;
	}
	
	protected boolean __stop_stream_internal(String symbol_) { return __stop_stream_internal(_get_id(symbol_, true)); }

	protected boolean __stop_stream_internal(int id_) 
	{
		if (!__is_ok(id_)) return false;
			
		__lock();
		
		if (stop_common(id_, _get_symbol(id_, false))) calls.cancelMktData(id_);
		
		__unlock();
		
		return true;
	}

	protected boolean _id_exists(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		boolean output = _ids.containsKey(id_);
		
		if (lock_) __unlock();
		
		return output; 
	}
	
	protected int _get_id(String symbol_, boolean lock_) { return (int)(lock_ ? arrays.__get_key_async(_symbols, symbol_) : arrays.get_key(_symbols, symbol_)); }

	protected int _get_data_type(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		int output = (_data_types.containsKey(id_) ? _data_types.get(id_) : data.WRONG_DATA);
		
		if (lock_) __unlock();
		
		return output;
	}

	protected String _get_symbol(int id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		String output = (_symbols.containsKey(id_) ? _symbols.get(id_) : null);	
		
		if (lock_) __unlock();
		
		return output;
	}
	
	protected boolean __is_snapshot(int id_) 
	{
		__lock();
		
		boolean output = (_ids.containsKey(id_) ? _ids.get(id_).equals(TYPE_SNAPSHOT) : false);	
		
		__unlock();
		
		return output;
	}

	protected void __update(int id_, String field_, double val_) { __update(id_, field_, val_, __is_snapshot(id_)); }

	protected void __update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_) || val_ <= 0.0) return;  
		
		if (is_snapshot_) __update_vals(id_, field_, val_);
		else update_db(id_, _get_symbol(id_, true), field_, val_);
	}
	
	protected void update_db(int id_, String symbol_)
	{
		if (_is_db_quick)
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
		if (_is_db_quick) async_data.update_quick(_source, symbol_, async_data.get_col(_source, field_), Double.toString(val_));
		else async_data.update(_source, symbol_, field_, val_);	
		
		to_screen_update(id_, symbol_, true);
	}	
	
	protected void to_screen(int id_, String symbol_, String message_) { if (_logs_to_screen) logs.update_screen(id_, symbol_, (_id + misc.SEPARATOR_CONTENT + message_)); }

	protected void __stop_all_internal()
	{	
		__lock();	
	
		_stop_all = true;
		
		HashMap<Integer, String> ids = new HashMap<Integer, String>(_ids);
		
		__unlock();
		
		if (ids.size() == 0) return;
		
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			if (__is_snapshot(id)) __stop_snapshot_internal(id);
			else __stop_stream_internal(id);	
		}
	}
	
	protected boolean remove(int id_) 
	{ 
		if (!_id_exists(id_, false)) return false;
		
		_ids.remove(id_);	
		_symbols.remove(id_);
		_data_types.remove(id_);
	
		if (_is_db_quick) _vals_quick.remove(id_);
		else _vals.remove(id_);
		
		return true;
	}

	protected String normalise_symbol(String symbol_) { return common.check_symbol(symbol_); }
	
	private boolean __is_ok(int id_) { return (_enabled && _id_exists(id_, true)); }

	private boolean __start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		__lock();
		
		String symbol = normalise_symbol(symbol_);
		
		if (_stop_all || _symbols.containsValue(symbol_)) 
		{
			__unlock();
			
			return false;
		}
		
		int data_type = (external_ib.data.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		int id = add(symbol, data_type, is_snapshot_);

		if (!async_data.exists(_source, symbol)) 
		{
			if (_is_db_quick) async_data.insert_quick(_source, symbol);
			else async_data.insert(_source, symbol);
		}
		else if (!async_data.is_enabled(_source, symbol)) 
		{
			remove(id);	

			__unlock();
			
			return false;
		}
		
		Contract contract = contracts.get_contract(symbol);
		if (contract == null) 
		{
			remove(id);

			__unlock();
			
			return false;
		}

		calls.reqMarketDataType(data_type_);
		
		if (!calls.reqMktData(id, symbol_, is_snapshot_)) 
		{
			remove(id);			

			__unlock();
			
			return false;
		}
		
		to_screen(id, symbol, "started");

		__unlock();
		
		return true;		
	}

	private int add(String symbol_, int data_type_, boolean is_snapshot_)
	{
		int id = async.get_req_id();

		_ids.put(id, get_type(is_snapshot_));
		_symbols.put(id, symbol_);
		_data_types.put(id, data_type_);
		
		if (_is_db_quick) _vals_quick.put(id, new HashMap<String, String>());
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

	private void __update_vals(int id_, String field_, double val_)
	{
		__lock();
		
		if (_is_db_quick && arrays.key_exists(_vals_quick, id_)) _vals_quick.get(id_).put(async_data.get_col(_source, field_), Double.toString(val_));
		else if (!_is_db_quick && arrays.key_exists(_vals, id_)) _vals.get(id_).put(field_, val_);			

		__unlock();
		
		__to_screen_update(id_, false);
	}

	private void __to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, _get_symbol(id_, true), is_db_); }

	private void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
}