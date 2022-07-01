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
import external_ib.calls;
import external_ib.contracts;

public abstract class parent_async_data extends parent_static
{
	public static final String CONFIG_SNAPSHOT_QUICK = types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK;
	public static final String CONFIG_SNAPSHOT_NONSTOP = types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP;

	public static final String TYPE_SNAPSHOT = types.ASYNC_MARKET_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_MARKET_STREAM;
	
	public static final int PRICE = external_ib.market.TICK_LAST;
	public static final int OPEN = external_ib.market.TICK_OPEN;
	public static final int CLOSE = external_ib.market.TICK_CLOSE;
	public static final int LOW = external_ib.market.TICK_LOW;
	public static final int HIGH = external_ib.market.TICK_HIGH;
	public static final int ASK = external_ib.market.TICK_ASK;
	public static final int BID = external_ib.market.TICK_BID;
	public static final int HALTED = external_ib.market.TICK_HALTED;
	public static final int VOLUME = external_ib.market.TICK_VOLUME;
	public static final int SIZE = external_ib.market.TICK_LAST_SIZE;
	public static final int ASK_SIZE = external_ib.market.TICK_ASK_SIZE;
	public static final int BID_SIZE = external_ib.market.TICK_BID_SIZE;

	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.market.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final boolean DEFAULT_IS_DB_QUICK = true;
	public static final int DEFAULT_NONSTOP_PAUSE = 0;
	public static final boolean DEFAULT_ENABLED = false;
	
	public static boolean snapshot_is_quick() { return config.get_async_boolean(CONFIG_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(CONFIG_SNAPSHOT_NONSTOP); }

	public static String get_type(boolean is_snapshot_) { return (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM); }

	protected boolean _logs_to_screen = DEFAULT_LOGS_TO_SCREEN;
	protected boolean _is_db_quick = DEFAULT_IS_DB_QUICK; 
	protected int _nonstop_pause = DEFAULT_NONSTOP_PAUSE; 
	protected boolean _enabled = DEFAULT_ENABLED; 
	
	protected volatile boolean _stop_all = false;	
	
	protected volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	protected volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();
	protected volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	protected volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();

	protected abstract HashMap<Integer, String> get_all_prices();
	protected abstract HashMap<Integer, String> get_all_sizes();
	protected abstract HashMap<Integer, String> get_all_generics();
	protected abstract boolean is_enabled(String symbol_);
	protected abstract boolean exists(String symbol_);
	protected abstract void insert(String symbol_);
	protected abstract void insert_quick(String symbol_);
	protected abstract void update(String symbol_, HashMap<String, Object> vals_);
	protected abstract void update(String symbol_, String field_, Object val_);
	protected abstract void update_quick(String symbol_, HashMap<String, String> vals_);
	protected abstract void update_quick(String symbol_, String col_, String val_);
	protected abstract String get_col(String field_);
	
	protected void __tick_price_internal(int id_, int field_ib_, double price_)
	{
		if (!__is_ok(id_)) return;
		
		String field = get_field(get_all_prices(), field_ib_);
		if (!strings.is_ok(field)) return;
		
		__update(id_, field, price_);
	}
	
	protected void __tick_size_internal(int id_, int field_ib_, int size_)
	{
		if (!__is_ok(id_)) return;
		
		String field = get_field(get_all_sizes(), field_ib_);
		if (!strings.is_ok(field)) return;
		
		boolean is_snapshot = __is_snapshot(id_);
		
		__update(id_, field, size_, is_snapshot);
	
		if (is_snapshot && field_ib_ == VOLUME && snapshot_is_quick()) __stop_snapshot_internal(id_); 
	}
	
	protected void __tick_generic_internal(int id_, int tick_, double value_)
	{
		if (!__is_ok(id_)) return;
		
		String field = get_field(get_all_generics(), tick_);
		if (!strings.is_ok(field)) return;
	
		__update(id_, field, value_);
	}

	protected boolean __start_snapshot_internal(String symbol_) { return __start_snapshot_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected boolean __start_snapshot_internal(String symbol_, int data_type_) { return __start(symbol_, data_type_, true); }

	protected boolean __start_stream_internal(String symbol_) { return __start_stream_internal(symbol_, DEFAULT_DATA_TYPE); }

	protected boolean __start_stream_internal(String symbol_, int data_type_) { return __start(symbol_, data_type_, false); }
	
	protected void __stop_stream_internal(String symbol_) { __stop_stream_internal(_get_id(symbol_, true)); }

	protected void __stop_stream_internal(int id_) 
	{
		if (!__is_ok(id_)) return;
			
		__lock();
		
		if (stop_common(id_,_get_symbol(id_, false))) calls.cancelMktData(id_);
		
		__unlock();
	}
	
	protected void __stop_snapshot_internal(String symbol_) { __stop_snapshot_internal(_get_id(symbol_, true)); }
	
	protected void __stop_snapshot_internal(int id_) 
	{	
		if (!__is_ok(id_)) return;
		
		__lock();
		
		String symbol = _get_symbol(id_, false);
		
		if (!strings.is_ok(symbol)) 
		{
			__unlock();
			
			return;
		}
		
		boolean restart = (snapshot_is_nonstop() && !_stop_all);
		int data_type = (restart ? _get_data_type(id_, false) : 0);
		
		update_db(id_, symbol);
		stop_common(id_, symbol);
	
		__unlock();
		
		if (restart) 
		{
			misc.pause_secs(_nonstop_pause);
			
			__start_snapshot_internal(symbol, data_type);
		}
	}

	protected boolean _id_exists(int id_, boolean lock_) { return (lock_ ? arrays.__key_exists_async(_ids, id_) : arrays.key_exists(_ids, id_) ); }

	protected int _get_id(String symbol_, boolean lock_) { return (int)(lock_ ? arrays.__get_key_async(_symbols, symbol_) : arrays.get_key(_symbols, symbol_)); }

	protected int _get_data_type(int id_, boolean lock_) { return (int)(lock_ ? arrays.__get_value_async(_data_types, id_) : arrays.get_value(_data_types, id_)); }

	protected String _get_symbol(int id_, boolean lock_) 
	{ 
		String output = null;
		
		if (lock_) __lock();
			
		if (_symbols.containsKey(id_)) output = _symbols.get(id_);
		
		if (lock_) __unlock();
		
		return output;
	}
	
	protected boolean __is_snapshot(int id_) 
	{
		boolean output = false;
		
		__lock();
			
		if (_ids.containsKey(id_)) output = _ids.get(id_).equals(TYPE_SNAPSHOT);
		
		__unlock();
		
		return output;
	}

	protected void __update(int id_, String field_, double val_) { __update(id_, field_, val_, __is_snapshot(id_)); }
		
	protected void update_db(int id_, String symbol_, String field_, double val_)
	{
		if (_is_db_quick) update_quick(symbol_, get_col(field_), Double.toString(val_));
		else update(symbol_, field_, val_);	
		
		to_screen_update(id_, symbol_, true);
	}	
	
	protected void to_screen(int id_, String symbol_, String message_) { if (_logs_to_screen) logs.update_screen(id_, symbol_, message_); }

	protected void __update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_) || val_ <= 0.0) return;  
		
		if (is_snapshot_) __update_vals(id_, field_, val_);
		else update_db(id_, _get_symbol(id_, true), field_, val_);
	}
	
	@SuppressWarnings("unchecked")
	protected void update_db(int id_, String symbol_)
	{
		if (_is_db_quick)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_value(_vals_quick, id_);
			if (arrays.is_ok(vals)) update_quick(symbol_, vals);
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_value(_vals, id_);
			if (arrays.is_ok(vals)) update(symbol_, vals);
		}
				
		to_screen_update(id_, symbol_, true);
	}
	
	protected void __stop_all_internal()
	{	
		__lock();	
	
		_stop_all = true;
		
		HashMap<Integer, String> ids = new HashMap<Integer, String>(_ids);
		
		__unlock();
		
		if (!arrays.is_ok(ids)) return;
		
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			if (__is_snapshot(id)) __stop_snapshot_internal(id);
			else __stop_stream_internal(id);	
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean remove(int id_) 
	{ 
		if (!_id_exists(id_, false)) return false;
		
		_ids.remove(id_);
		
		_symbols = (HashMap<Integer, String>)arrays.remove_key(_symbols, id_);
		_data_types = (HashMap<Integer, Integer>)arrays.remove_key(_data_types, id_);
	
		if (_is_db_quick) _vals_quick = (HashMap<Integer, HashMap<String, String>>)arrays.remove_key(_vals_quick, id_);
		else _vals = (HashMap<Integer, HashMap<String, Object>>)arrays.remove_key(_vals, id_);
		
		return true;
	}

	private boolean __is_ok(int id_) { return (_enabled && _id_exists(id_, true)); }

	private boolean __start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		__lock();
		
		String symbol = common.check_symbol(symbol_);
		
		if (_stop_all || !strings.is_ok(symbol) || arrays.value_exists(_symbols, symbol_)) 
		{
			__unlock();
			
			return false;
		}
		
		int data_type = (external_ib.market.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		int id = add(symbol, data_type, is_snapshot_);

		if (!exists(symbol)) 
		{
			if (_is_db_quick) insert_quick(symbol);
			else insert(symbol);
		}
		else if (!is_enabled(symbol)) 
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
		String output = strings.DEFAULT;
		if (!arrays.is_ok(all_)) return output;
		
		for (Entry<Integer, String> item: all_.entrySet())
		{
			if (field_ib_ == item.getKey()) return item.getValue();
		}
		
		return output;
	}

	private void __update_vals(int id_, String field_, double val_)
	{
		__lock();
		
		if (_is_db_quick && arrays.key_exists(_vals_quick, id_)) _vals_quick.get(id_).put(get_col(field_), Double.toString(val_));
		else if (!_is_db_quick && arrays.key_exists(_vals, id_)) _vals.get(id_).put(field_, val_);			

		__unlock();
		
		__to_screen_update(id_, false);
	}

	private void __to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, _get_symbol(id_, true), is_db_); }

	private void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
}