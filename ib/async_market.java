package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.config;
import accessory_ib.db;
import accessory_ib.types;
import accessory_ib.logs;
import external_ib.calls;
import external_ib.contracts;
import external_ib.market;

public class async_market extends parent_static 
{
	public static final String TYPE_SNAPSHOT = types.ASYNC_MARKET_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_MARKET_STREAM;
	
	public static final int PRICE = market.TICK_LAST;
	public static final int OPEN = market.TICK_OPEN;
	public static final int CLOSE = market.TICK_CLOSE;
	public static final int LOW = market.TICK_LOW;
	public static final int HIGH = market.TICK_HIGH;
	public static final int ASK = market.TICK_ASK;
	public static final int BID = market.TICK_BID;
	public static final int HALTED = market.TICK_HALTED;
	public static final int VOLUME = market.TICK_VOLUME;
	public static final int SIZE = market.TICK_LAST_SIZE;
	public static final int ASK_SIZE = market.TICK_ASK_SIZE;
	public static final int BID_SIZE = market.TICK_BID_SIZE;

	public static final int DEFAULT_DATA_TYPE = market.DATA_LIVE;
	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	
	public static boolean _print_all = true;
	public static boolean _enable_db_quick = true; //Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones). 
	
	private static volatile boolean _stop_all = false;
	private static volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();
	private static volatile HashMap<Integer, HashMap<String, Object>> _vals = new HashMap<Integer, HashMap<String, Object>>();
	private static volatile HashMap<Integer, HashMap<String, String>> _vals_quick = new HashMap<Integer, HashMap<String, String>>();

	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC_MARKET); }

	public static void wrapper_tickPrice(int id_, int field_ib_, double price_)
	{
		String field = get_field(get_all_prices(), field_ib_);
		if (!strings.is_ok(field)) return;

		update(id_, field, price_);
	}
	
	public static void wrapper_tickSize(int id_, int field_ib_, int size_)
	{
		String field = get_field(get_all_sizes(), field_ib_);
		if (!strings.is_ok(field)) return;
		
		boolean is_snapshot = is_snapshot(id_);
		
		update(id_, field, size_, is_snapshot);

		if (is_snapshot && field_ib_ == VOLUME && snapshot_is_quick()) stop_snapshot(id_); 
	}
	
	public static void wrapper_tickGeneric(int id_, int tick_, double value_)
	{
		String field = get_field(get_all_generics(), tick_);
		if (!strings.is_ok(field)) return;

		update(id_, field, value_);
	}

	public static void stop_all()
	{	
		lock();	
	
		_stop_all = true;
		
		HashMap<Integer, String> ids = new HashMap<Integer, String>(async._ids);
		
		unlock();
		
		if (!arrays.is_ok(ids)) return;
		
		for (Entry<Integer, String> item: ids.entrySet())
		{
			int id = item.getKey();
			
			if (is_snapshot(id)) stop_snapshot(id);
			else stop_stream(id);	
		}
	}
	
	public static boolean is_snapshot(int id_) { return async.is_ok(id_, TYPE_SNAPSHOT); }

	public static boolean snapshot_is_quick() { return config.get_async_boolean(types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP); }

	public static boolean start_snapshot(String symbol_) { return start_snapshot(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean start_snapshot(String symbol_, int data_type_) { return start(symbol_, data_type_, true); }

	public static boolean start_stream(String symbol_) { return start_stream(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean start_stream(String symbol_, int data_type_) { return start(symbol_, data_type_, false); }

	public static void stop_snapshot(int id_) 
	{	
		lock();
		
		String symbol = get_symbol(id_, false);
		
		if (!strings.is_ok(symbol)) 
		{
			unlock();
			
			return;
		}
		
		boolean restart = (snapshot_is_nonstop() && !_stop_all);
		int data_type = (restart ? get_data_type(id_, false) : 0);
		
		update_db(id_, symbol);
		stop_common(id_, symbol);
	
		unlock();
		
		if (restart) start_snapshot(symbol, data_type);
	}
	
	public static void stop_stream(String symbol_) { stop_stream(get_id(symbol_)); }

	public static void stop_stream(int id_) 
	{
		lock();
		
		if (stop_common(id_, get_symbol(id_, false))) calls.cancelMktData(id_);
		
		unlock();
	}
	
	public static String get_symbol(int id_) { return get_symbol(id_, true); }

	public static int get_data_type(int id_) { return get_data_type(id_, true); }
	
	public static int get_id(String symbol_) { return get_id(symbol_, true); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, db.FIELD_PRICE);
		all.put(OPEN, db.FIELD_OPEN);
		all.put(CLOSE, db.FIELD_CLOSE);
		all.put(LOW, db.FIELD_LOW);
		all.put(HIGH, db.FIELD_HIGH);
		all.put(ASK, db.FIELD_ASK);
		all.put(BID, db.FIELD_BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME, db.FIELD_VOLUME);
		all.put(SIZE, db.FIELD_SIZE);
		all.put(ASK_SIZE, db.FIELD_ASK_SIZE);
		all.put(BID_SIZE, db.FIELD_BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, db.FIELD_HALTED);
		
		return all;
	}

	private static String get_symbol(int id_, boolean lock_) { return (String)(lock_ ? arrays.get_value_async(_symbols, id_) : arrays.get_value(_symbols, id_)); }

	private static int get_id(String symbol_, boolean lock_) { return (int)(lock_ ? arrays.get_key_async(_symbols, symbol_) : arrays.get_key(_symbols, symbol_)); }

	private static int get_data_type(int id_, boolean lock_) { return (int)(lock_ ? arrays.get_value_async(_data_types, id_) : arrays.get_value(_data_types, id_)); }
	
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
	
	private static boolean start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		lock();
		
		String symbol = common.check_symbol(symbol_);
		
		if (_stop_all || !strings.is_ok(symbol) || arrays.value_exists(_symbols, symbol_)) 
		{
			unlock();
			
			return false;
		}
		
		int data_type = (market.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		int id = add(symbol, data_type, is_snapshot_);

		if (!db.exists(symbol)) 
		{
			if (_enable_db_quick) db.insert_market_quick(symbol);
			else db.insert_market(symbol);
		}
		else if (!db.is_enabled(symbol)) 
		{
			remove(id);
			
			unlock();
			
			return false;
		}
		
		Contract contract = contracts.get_contract(symbol);
		if (contract == null) 
		{
			unlock();
			
			return false;
		}

		calls.reqMarketDataType(data_type_);
		
		if (!calls.reqMktData(id, symbol_, is_snapshot_)) 
		{
			remove(id);
			
			unlock();
			
			return false;
		}
		
		to_screen(id, symbol, "started");

		unlock();
		
		return true;		
	}

	private static int add(String symbol_, int data_type_, boolean is_snapshot_)
	{
		int id = async.add_id((is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM));

		_symbols.put(id, symbol_);
		_data_types.put(id, data_type_);
		
		if (_enable_db_quick) _vals_quick.put(id, new HashMap<String, String>());
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
	
		if (_enable_db_quick) _vals_quick = (HashMap<Integer, HashMap<String, String>>)arrays.remove_key(_vals_quick, id_);
		else _vals = (HashMap<Integer, HashMap<String, Object>>)arrays.remove_key(_vals, id_);
		
		return true;
	}
	
	private static boolean stop_common(int id_, String symbol_) 
	{ 
		if (!remove(id_)) return false;
		
		to_screen(id_, symbol_, "stopped");
		
		return true;
	}

	private static void update(int id_, String field_, double val_) { update(id_, field_, val_, is_snapshot(id_)); }

	private static void update(int id_, String field_, double val_, boolean is_snapshot_)
	{
		if (!strings.is_ok(field_) || val_ <= 0.0) return;  
		
		if (is_snapshot_) update_vals(id_, field_, val_);
		else update_db(id_, get_symbol(id_), field_, val_);
	}
	
	private static void update_vals(int id_, String field_, double val_)
	{
		if (_enable_db_quick && arrays.key_exists_async(_vals_quick, id_)) _vals_quick.get(id_).put(db.get_col_market(field_), Double.toString(val_));
		else if (!_enable_db_quick && arrays.key_exists_async(_vals, id_)) _vals.get(id_).put(field_, val_);
		
		to_screen_update(id_, false);
	}
	
	private static void update_db(int id_, String symbol_, String field_, double val_)
	{
		if (_enable_db_quick) db.update_val_market_quick(symbol_, db.get_col_market(field_), Double.toString(val_));
		else db.update_val_market(symbol_, field_, val_);
		
		to_screen_update(id_, symbol_, true);
	}
	
	@SuppressWarnings("unchecked")
	private static void update_db(int id_, String symbol_)
	{
		if (_enable_db_quick)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_value(_vals_quick, id_);
			if (arrays.is_ok(vals)) db.update_vals_market_quick(symbol_, vals);
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_value(_vals, id_);
			if (arrays.is_ok(vals)) db.update_vals_market(symbol_, vals);
		}
		
		to_screen_update(id_, symbol_, true);
	}
	
	private static void to_screen_update(int id_, boolean is_db_) { to_screen_update(id_, get_symbol(id_), is_db_); }

	private static void to_screen_update(int id_, String symbol_, boolean is_db_) { to_screen(id_, symbol_, (is_db_ ? "stored" : "updated")); }
	
	private static void to_screen(int id_, String symbol_, String message_) { if (_print_all) logs.update_screen(id_, symbol_, message_); }
}