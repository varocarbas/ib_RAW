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

	public static final int WRONG_ID = common.WRONG_ID;
	
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
		__lock();
		
		if (!_is_ok(id_, false)) 
		{
			__unlock();
		
			return;
		}
		
		String field = get_field(get_all_prices(), field_ib_);	
		if (field != null) update(id_, field, price_);
		
		__unlock();
	}
	
	protected void __tick_size_internal(int id_, int field_ib_, int size_)
	{
		__lock();
		
		if (!_is_ok(id_, false)) 
		{
			__unlock();
			
			return;
		}
		
		boolean is_snapshot = _is_snapshot(id_, false);
		
		String field = get_field(get_all_sizes(), field_ib_);
		if (field != null) update(id_, field, size_, is_snapshot);
	
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
		if (field != null) update(id_, field, value_);
		
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
	
	protected boolean _stop_snapshot_internal(int id_, boolean lock_) 
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
		
		boolean restart = (snapshot_is_nonstop() && !_stop_all);
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
	
	protected boolean _is_snapshot(int id_, boolean lock_) 
	{
		if (lock_) __lock();
		
		boolean output = (_ids.containsKey(id_) ? _ids.get(id_).equals(TYPE_SNAPSHOT) : false);	
		
		if (lock_) __unlock();
		
		return output;
	}

	protected void update(int id_, String field_, double val_) { update(id_, field_, val_, _is_snapshot(id_, false)); }

	protected void update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_) || val_ <= 0.0) return;  
		
		if (is_snapshot_) update_vals(id_, field_, val_);
		else update_db(id_, _get_symbol(id_, false), field_, val_);
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
	
		misc.pause_secs(5);
		
		_stop_all = true;
		
		HashMap<Integer, String> ids = new HashMap<Integer, String>(_ids);
		
		if (ids.size() == 0) 
		{
			_stop_all = false;
			
			__unlock();
			
			return;
		}
				
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			misc.pause_secs(1);
			
			if (_is_snapshot(id, false)) _stop_snapshot_internal(id, false);
			else _stop_stream_internal(id, false);	
		}
		
		__unlock();
	}
	
	protected boolean remove(int id_) 
	{ 
		if (!_id_exists(id_, false)) return false;
		
		_ids.remove(id_);	
		_symbols.remove(id_);
		_data_types.remove(id_);
	
		int tot = 0;
		
		if (_is_db_quick) 
		{
			_vals_quick.remove(id_);
			tot = _vals_quick.size();
		}
		else 
		{
			_vals.remove(id_);
			tot = _vals.size();
		}

		if (tot == 0) _enabled = false;
		
		return true;
	}

	protected String normalise_symbol(String symbol_) { return common.check_symbol(symbol_); }

	private boolean _is_ok(int id_, boolean lock_) { return (_enabled && _id_exists(id_, lock_)); }

	private int _start(String symbol_, int data_type_, boolean is_snapshot_, boolean lock_)
	{
		if (lock_) __lock();
	
		int id = WRONG_ID;
		
		String symbol = normalise_symbol(symbol_);
		
		if (_stop_all || !strings.is_ok(symbol) || _symbols.containsValue(symbol_)) 
		{
			if (lock_) __unlock();
			
			return id;
		}
		
		int data_type = (external_ib.data.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		id = add(symbol, data_type, is_snapshot_);

		if (!async_data.exists(_source, symbol)) 
		{
			if (_is_db_quick) async_data.insert_quick(_source, symbol);
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
		
		_enabled = true;
		
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

	private void update_vals(int id_, String field_, double val_)
	{	
		if (_is_db_quick && arrays.key_exists(_vals_quick, id_)) _vals_quick.get(id_).put(async_data.get_col(_source, field_), Double.toString(val_));
		else if (!_is_db_quick && arrays.key_exists(_vals, id_)) _vals.get(id_).put(field_, val_);			

		to_screen_update(id_, false);
	}

	private void to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, _get_symbol(id_, false), is_db_); }

	private void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
}