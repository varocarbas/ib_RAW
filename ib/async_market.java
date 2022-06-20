package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.config;
import accessory_ib.types;
import accessory_ib.logs;
import external_ib.calls;
import external_ib.contracts;
import db_ib.market;

public class async_market extends parent_static 
{
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

	public static final int DEFAULT_DATA_TYPE = external_ib.market.DATA_LIVE;
	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final boolean DEFAULT_SCREEN_LOGS = true;
	public static final boolean DEFAULT_DB_QUICK = true;
	public static final int DEFAULT_NONSTOP_PAUSE = 0;
	
	private static volatile boolean _stop_all = false;
	private static boolean _screen_logs = DEFAULT_SCREEN_LOGS;
	private static boolean _db_quick = DEFAULT_DB_QUICK;  
	private static int _nonstop_pause = DEFAULT_NONSTOP_PAUSE; 
	
	private static volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();
	private static volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	private static volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC_MARKET); }

	public static void __wrapper_tickPrice(int id_, int field_ib_, double price_)
	{
		String field = get_field(get_all_prices(), field_ib_);
		if (!strings.is_ok(field)) return;

		__update(id_, field, price_);
	}
	
	public static void __wrapper_tickSize(int id_, int field_ib_, int size_)
	{
		String field = get_field(get_all_sizes(), field_ib_);
		if (!strings.is_ok(field)) return;
		
		boolean is_snapshot = is_snapshot(id_);
		
		__update(id_, field, size_, is_snapshot);

		if (is_snapshot && field_ib_ == VOLUME && snapshot_is_quick()) __stop_snapshot(id_); 
	}
	
	public static void __wrapper_tickGeneric(int id_, int tick_, double value_)
	{
		String field = get_field(get_all_generics(), tick_);
		if (!strings.is_ok(field)) return;

		__update(id_, field, value_);
	}

	public static boolean get_screen_logs() { return _screen_logs; }

	public static void update_screen_logs() { _screen_logs = DEFAULT_SCREEN_LOGS; }

	public static void update_screen_logs(boolean screen_logs_) { _screen_logs = screen_logs_; }

	public static boolean get_db_quick() { return _db_quick; }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	public static void update_db_quick() { update_db_quick(DEFAULT_DB_QUICK); }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	public static void update_db_quick(boolean db_quick_) { _db_quick = db_quick_; }

	public static int get_nonstop_pause() { return _nonstop_pause; }

	public static void update_nonstop_pause() { update_nonstop_pause(DEFAULT_NONSTOP_PAUSE); }

	public static void update_nonstop_pause(int nonstop_pause_) { _nonstop_pause = (nonstop_pause_ >= 0 ? nonstop_pause_ : DEFAULT_NONSTOP_PAUSE); }
	
	public static void __stop_all()
	{	
		__lock();	
	
		_stop_all = true;
		
		HashMap<Integer, String> ids = async.get_ids();
		
		__unlock();
		
		if (!arrays.is_ok(ids)) return;
		
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			if (is_snapshot(id)) __stop_snapshot(id);
			else __stop_stream(id);	
		}
	}
	
	public static boolean is_snapshot(int id_) { return async.is_ok(id_, TYPE_SNAPSHOT); }

	public static boolean snapshot_is_quick() { return config.get_async_boolean(types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP); }

	public static boolean __start_snapshot(String symbol_) { return __start_snapshot(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean __start_snapshot(String symbol_, int data_type_) { return __start(symbol_, data_type_, true); }

	public static boolean __start_stream(String symbol_) { return __start_stream(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean __start_stream(String symbol_, int data_type_) { return __start(symbol_, data_type_, false); }

	public static void __stop_snapshot(int id_) 
	{	
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
			
			__start_snapshot(symbol, data_type);
		}
	}
	
	public static void __stop_stream(String symbol_) { __stop_stream(__get_id(symbol_)); }

	public static void __stop_stream(int id_) 
	{
		__lock();
		
		if (stop_common(id_, _get_symbol(id_, false))) calls.cancelMktData(id_);
		
		__unlock();
	}
	
	public static String __get_symbol(int id_) { return _get_symbol(id_, true); }

	public static int __get_data_type(int id_) { return _get_data_type(id_, true); }
	
	public static int __get_id(String symbol_) { return _get_id(symbol_, true); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, market.PRICE);
		all.put(OPEN, market.OPEN);
		all.put(CLOSE, market.CLOSE);
		all.put(LOW, market.LOW);
		all.put(HIGH, market.HIGH);
		all.put(ASK, market.ASK);
		all.put(BID, market.BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME, market.VOLUME);
		all.put(SIZE, market.SIZE);
		all.put(ASK_SIZE, market.ASK_SIZE);
		all.put(BID_SIZE, market.BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, market.HALTED);
		
		return all;
	}

	private static String _get_symbol(int id_, boolean lock_) { return (String)(lock_ ? arrays.__get_value_async(_symbols, id_) : arrays.get_value(_symbols, id_)); }

	private static int _get_id(String symbol_, boolean lock_) { return (int)(lock_ ? arrays.__get_key_async(_symbols, symbol_) : arrays.get_key(_symbols, symbol_)); }

	private static int _get_data_type(int id_, boolean lock_) { return (int)(lock_ ? arrays.__get_value_async(_data_types, id_) : arrays.get_value(_data_types, id_)); }
	
	private static HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_MARKET_PRICES; }
	
	private static HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_MARKET_SIZES; }
	
	private static HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_MARKET_GENERICS; }

	private static String get_field(HashMap<Integer, String> all_, int field_ib_)
	{
		for (Entry<Integer, String> item: all_.entrySet())
		{
			if (field_ib_ == item.getKey()) return item.getValue();
		}
		
		return strings.DEFAULT;
	}
	
	private static boolean __start(String symbol_, int data_type_, boolean is_snapshot_)
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

		if (!market.exists(symbol)) 
		{
			if (_db_quick) market.insert_quick(symbol);
			else market.insert(symbol);
		}
		else if (!market.is_enabled(symbol)) 
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

	private static int add(String symbol_, int data_type_, boolean is_snapshot_)
	{
		int id = async.add_id((is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM));

		_symbols.put(id, symbol_);
		_data_types.put(id, data_type_);
		
		if (_db_quick) _vals_quick.put(id, new HashMap<String, String>());
		else _vals.put(id, new HashMap<String, Object>());
		
		return id;
	}

	@SuppressWarnings("unchecked")
	private static boolean remove(int id_) 
	{ 
		if (!async.is_ok(id_)) return false;
		
		async.remove_id(id_);
		
		_symbols = (HashMap<Integer, String>)arrays.remove_key(_symbols, id_);
		_data_types = (HashMap<Integer, Integer>)arrays.remove_key(_data_types, id_);
	
		if (_db_quick) _vals_quick = (HashMap<Integer, HashMap<String, String>>)arrays.remove_key(_vals_quick, id_);
		else _vals = (HashMap<Integer, HashMap<String, Object>>)arrays.remove_key(_vals, id_);
		
		return true;
	}
	
	private static boolean stop_common(int id_, String symbol_) 
	{ 
		if (!remove(id_)) return false;
		
		to_screen(id_, symbol_, "stopped");
		
		return true;
	}

	private static void __update(int id_, String field_, double val_) { __update(id_, field_, val_, is_snapshot(id_)); }

	private static void __update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_) || val_ <= 0.0) return;  
		
		if (is_snapshot_) __update_vals(id_, field_, val_);
		else update_db(id_, __get_symbol(id_), field_, val_);
	}
	
	private static void __update_vals(int id_, String field_, double val_)
	{
		if (_db_quick && arrays.__key_exists_async(_vals_quick, id_)) _vals_quick.get(id_).put(market.get_col(field_), Double.toString(val_));
		else if (!_db_quick && arrays.__key_exists_async(_vals, id_)) _vals.get(id_).put(field_, val_);
		
		__to_screen_update(id_, false);
	}
	
	private static void update_db(int id_, String symbol_, String field_, double val_)
	{
		if (_db_quick) market.update_quick(symbol_, market.get_col(field_), Double.toString(val_));
		else market.update(symbol_, field_, val_);
		
		to_screen_update(id_, symbol_, true);
	}
	
	@SuppressWarnings("unchecked")
	private static void update_db(int id_, String symbol_)
	{
		if (_db_quick)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_value(_vals_quick, id_);
			if (arrays.is_ok(vals)) market.update_quick(symbol_, vals);
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_value(_vals, id_);
			if (arrays.is_ok(vals)) market.update(symbol_, vals);
		}
		
		to_screen_update(id_, symbol_, true);
	}
	
	private static void __to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, __get_symbol(id_), is_db_); }

	private static void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
	
	private static void to_screen(int id_, String symbol_, String message_) { if (_screen_logs) logs.update_screen(id_, symbol_, message_); }
}