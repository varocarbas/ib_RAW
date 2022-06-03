package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib._defaults;
import accessory_ib.config;
import accessory_ib.db;
import accessory_ib.types;
import accessory_ib.logs;
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

	public static final int DEFAULT_DATA_TYPE = _defaults.ASYNC_DATA_TYPE;
	
	public static volatile boolean _print_all = true;
	
	private static volatile boolean _stop_all = false;
	private static volatile HashMap<Integer, String> _symbols = new HashMap<Integer, String>();
	private static volatile HashMap<Integer, Integer> _data_types = new HashMap<Integer, Integer>();
		
	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC_MARKET); }

	public static void wrapper_tickPrice(int id_, int field_, double price_)
	{
		String field = (String)arrays.get_value(get_all_prices(), field_);
		if (!strings.is_ok(field)) return;

		update_db(id_, field, price_);
	}

	public static void wrapper_tickSize(int id_, int field_, int size_)
	{
		String field = (String)arrays.get_value(get_all_sizes(), field_);
		if (!strings.is_ok(field)) return;
		
		if (field_ == VOLUME)
		{
			if (is_snapshot(id_) && snapshot_is_quick()) stop_snapshot(id_);
		}

		update_db(id_, field, size_);
	}
	
	public static void wrapper_tickGeneric(int id_, int tick_, double value_)
	{
		String field = (String)arrays.get_value(get_all_generics(), tick_);
		if (!strings.is_ok(field)) return;

		update_db(id_, field, value_);
	}

	public static void stop_all()
	{		
		if (!arrays.is_ok(async._ids)) return;
	
		_stop_all = true;
		
		for (Entry<Integer, String> item: async._ids.entrySet())
		{
			int id = item.getKey();
			
			if (is_snapshot(id)) stop_snapshot(id);
			else stop_stream(id);	
		}
	}
	
	public static boolean is_snapshot(int id_) { return async.is_ok(id_, TYPE_SNAPSHOT); }

	public static boolean snapshot_is_quick() { return (boolean)config.get_async(types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return (boolean)config.get_async(types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP); }

	public static boolean start_snapshot(String symbol_) { return start_snapshot(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean start_snapshot(String symbol_, int data_type_) { return start(symbol_, data_type_, true); }

	public static boolean start_stream(String symbol_) { return start_stream(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean start_stream(String symbol_, int data_type_) { return start(symbol_, data_type_, false); }

	public static void stop_snapshot(int id_) 
	{ 	
		if (!is_snapshot(id_)) return;
		
		String symbol = null;
		int data_type = 0;
		
		boolean restart = (snapshot_is_nonstop() && !_stop_all);
		
		if (restart)
		{
			symbol = get_symbol(id_);
			data_type = get_data_type(id_);
		}
		
		stop_common(id_);
		
		if (restart) start_snapshot(symbol, data_type);
	}
	
	public static void stop_stream(String symbol_) { stop_stream(get_id(symbol_)); }

	public static void stop_stream(int id_) 
	{
		if (is_snapshot(id_)) return;
		
		stop_common(id_);
		
		conn._client.cancelMktData(id_); 
	}
	
	public static String get_symbol(int id_) { return (String)arrays.get_value(_symbols, id_); }
	
	public static int get_data_type(int id_) { return (int)arrays.get_value(_data_types, id_); }
	
	public static int get_id(String symbol_) { return (int)arrays.get_key(_symbols, symbol_); }

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
	
	private static HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_MARKET_PRICES; }
	
	private static HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_MARKET_SIZES; }
	
	private static HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_MARKET_GENERICS; }
	
	private static boolean start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol) || arrays.value_exists(_symbols, symbol)) return false;

		_stop_all = false;
		
		int data_type = (market.data_is_ok(data_type_) ? data_type_ : DEFAULT_DATA_TYPE);
		int id = add(symbol, data_type, is_snapshot_);

		if (!db.exists(symbol_)) db.insert(symbol); 
		else if (!db.is_enabled(symbol_)) return false;
		
		Contract contract = common.get_contract(symbol);
		if (contract == null) return false;

		conn._client.reqMarketDataType(data_type);
		conn._client.reqMktData(id, contract, "", is_snapshot_, false, null);

		return true;		
	}

	private static int add(String symbol_, int data_type_, boolean is_snapshot_)
	{
		int id = async.add((is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM));

		if (_print_all) logs.update_screen(id, symbol_, "added");
		
		_symbols.put(id, symbol_);
		_data_types.put(id, data_type_);

		return id;
	}
	
	@SuppressWarnings("unchecked")
	private static void stop_common(int id_) 
	{ 
		String symbol = get_symbol(id_);
		
		async.remove(id_);
		
		_symbols = (HashMap<Integer, String>)arrays.remove_key(_symbols, id_);
		_data_types = (HashMap<Integer, Integer>)arrays.remove_key(_data_types, id_);
		
		if (_print_all) logs.update_screen(id_, symbol, "removed");
	}
	
	private static boolean update_db(int id_, String field_, double val_)
	{
		String symbol = (String)arrays.get_value(_symbols, id_);
		if (!strings.is_ok(symbol) || !strings.is_ok(field_) || val_ <= 0.0) return false;

		if (_print_all) logs.update_screen(id_, symbol, "updated");

		return db.update_number(symbol, field_, val_);
	}
}