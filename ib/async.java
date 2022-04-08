package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.logs;
import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.ini;
import accessory_ib.db;
import accessory_ib.types;
import external_ib.constants;

public class async 
{	
	public static volatile HashMap<Integer, Boolean> _market_retrieving = new HashMap<Integer, Boolean>();
	public static volatile HashMap<Integer, Boolean> _market_retrieved = new HashMap<Integer, Boolean>();
	public static String _market_type = strings.DEFAULT;
	
	private static int _market_id = 0;
	private static HashMap<String, ArrayList<HashMap<String, String>>> _market = new HashMap<String, ArrayList<HashMap<String, String>>>();
	private static HashMap<Integer, String> _market_symbols = new HashMap<Integer, String>();
	private static HashMap<String, Integer> _market_i = new HashMap<String, Integer>();

	private static int MIN_ID = 1;
	private static int MAX_ID = 2500;

	static { ini.load(); }

	public static boolean snapshot_start(String symbol_, int data_)
	{
		return market_start(symbol_, data_, true);
	}

	public static boolean stream_start(String symbol_, int data_)
	{
		return market_start(symbol_, data_, false);
	}

	public static void stream_end(int id_)
	{
		conn._client.cancelMktData(id_);
	}

	public static void stream_end(String symbol_)
	{
		int id = get_id(symbol_);

		stream_end(id);
	}

	public static String get_symbol(int id_)
	{
		return arrays.get_value(_market_symbols, id_);
	}

	public static int get_id(String symbol_)
	{
		int id = MIN_ID - 1;
		if (!strings.is_ok(symbol_)) return id;

		for (Entry<Integer, String> item: _market_symbols.entrySet())
		{
			if (strings.are_equivalent(symbol_, item.getValue())) return item.getKey();
		}

		return id;
	}

	//To manage information retrieved via wrapper.tickPrice().
	public static void tick_price(int id_, int field_, double price_)
	{
		String symbol = arrays.get_value(_market_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;
		if (field_ == constants.TICK_BID) key = db.FIELD_BID;
		else if (field_ == constants.TICK_ASK) key = db.FIELD_ASK;
		else if (field_ == constants.TICK_LAST) key = db.FIELD_PRICE;
		else if (field_ == constants.TICK_HIGH) key = db.FIELD_HIGH;
		else if (field_ == constants.TICK_LOW) key = db.FIELD_LOW;
		else if (field_ == constants.TICK_CLOSE) key = db.FIELD_CLOSE;		
		else if (field_ == constants.TICK_OPEN) key = db.FIELD_OPEN;	

		if (!strings.is_ok(key)) return;

		market_update(symbol, key, price_);
	}

	//To manage information retrieved via wrapper.tickSize().
	public static void tick_size(int id_, int field_, int size_)
	{
		String symbol = arrays.get_value(_market_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;
		if (field_ == constants.TICK_BID_SIZE) key = db.FIELD_BID_SIZE;
		else if (field_ == constants.TICK_ASK_SIZE) key = db.FIELD_ASK_SIZE;
		else if (field_ == constants.TICK_LAST_SIZE) key = db.FIELD_SIZE;
		else if (field_ == constants.TICK_VOLUME) 
		{
			key = db.FIELD_VOLUME;

			if 
			(
				_market_retrieving.get(id_) && strings.are_equal(_market_type, types.ASYNC_MARKET_SNAPSHOT) && 
				strings.to_boolean(config.get_async(types.CONFIG_IB_ASYNC_SNAPSHOT_QUICK))
			)
			{ _market_retrieved.put(id_, true); }
		}

		if (!strings.is_ok(key)) return;

		market_update(symbol, key, size_);
	}

	//To manage information retrieved via wrapper.tickGeneric().
	public static void tick_generic(int id_, int tick_, double value_)
	{
		String symbol = arrays.get_value(_market_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;
		if (tick_ == constants.TICK_HALTED) key = db.FIELD_HALTED;

		if (!strings.is_ok(key)) return;

		market_update(symbol, key, value_);
	}

	//To manage information retrieved via wrapper.error().
	public static void error(int id_, int error_code_, String error_msg_)
	{
		if (constants.is_warning(error_code_)) logs.update_screen(error_msg_);
		else
		{
			HashMap<String, String> items = new HashMap<String, String>();
			items.put("id", strings.from_number_int(id_));
			items.put("error_code", strings.from_number_int(error_code_));
			items.put("error_msg", error_msg_);

			String message = arrays.to_string(items, null, null, null);
			message = "IB " + misc.SEPARATOR_CONTENT + types.ERROR_IB_ASYNC + misc.SEPARATOR_CONTENT + message;

			logs.update(message, null, true);	
		}
	}	

	private static boolean market_start(String symbol_, int data_, boolean is_snapshot_)
	{
		boolean is_ok = false;

		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return is_ok;

		is_ok = true;
		if (!arrays.is_ok(db.get_market_info(symbol)))

			_market_type = (is_snapshot_ ? types.ASYNC_MARKET_SNAPSHOT : types.ASYNC_MARKET_STREAM);

		if (!numbers.is_ok(_market_id, MIN_ID, MAX_ID)) _market_id = MIN_ID - 1;
		_market_id++;

		if (strings.are_equal(_market_type, types.ASYNC_MARKET_STREAM)) stream_end(_market_id);

		_market_symbols.put(_market_id, symbol);
		_market_retrieving.put(_market_id, true);
		_market_retrieved.put(_market_id, false);

		String storage = config.get_async(types.CONFIG_IB_ASYNC_STORAGE);
		if (storage.equals(types.CONFIG_IB_ASYNC_STORAGE_MEMORY))
		{
			market_check_memory(symbol);
		}
		else if (storage.equals(types.CONFIG_IB_ASYNC_STORAGE_DB))
		{
			if (!arrays.is_ok(db.get_market_info(symbol_))) db.insert_market(db.get_default_vals(), symbol_);
		}

		if (constants.data_is_ok(data_)) conn._client.reqMarketDataType(data_);
		conn._client.reqMktData(_market_id, common.get_contract(symbol), "", is_snapshot_, false, null);

		return is_ok;		
	}

	private static void market_check_memory(String symbol_)
	{
		if (!arrays.is_ok(_market)) _market = new HashMap<String, ArrayList<HashMap<String, String>>>();
		if (!_market.containsKey(symbol_)) _market.put(symbol_, new ArrayList<HashMap<String, String>>());
		if (!_market_i.containsKey(symbol_)) _market_i.put(symbol_, 0);

		int max = _market.get(symbol_).size() - 1;
		if (_market_i.get(symbol_) > max) 
		{
			if (max > 0) _market_i.put(symbol_, max);
			else _market.get(symbol_).add(new HashMap<String, String>());
		}
	}

	private static boolean market_update(String symbol_, String key_, double val_)
	{
		boolean is_ok = false;
		if (!strings.is_ok(symbol_) || !strings.is_ok(key_) || val_ <= 0.0) return is_ok;

		String storage = config.get_async(types.CONFIG_IB_ASYNC_STORAGE);
		if (!strings.is_ok(storage)) return is_ok;

		if (storage.equals(types.CONFIG_IB_ASYNC_STORAGE_DB)) 
		{	
			is_ok = db.update_market_val(key_, val_, symbol_);
		}
		else if (storage.equals(types.CONFIG_IB_ASYNC_STORAGE_MEMORY)) 
		{
			String val = strings.to_string(val_);
			
			market_check_memory(symbol_);

			_market.get(symbol_).get(_market_i.get(symbol_)).put(key_, val);
			is_ok = true;
		}

		return is_ok;
	}
}