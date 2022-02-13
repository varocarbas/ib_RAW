package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.keys;
import accessory.strings;
import accessory_ib._config;
import accessory_ib._ini;
import accessory_ib.types;

public class async 
{
	private static HashMap<String, ArrayList<HashMap<String, String>>> data = new HashMap<String, ArrayList<HashMap<String, String>>>();
	
	static { _ini.load(); }
	
	//To manage the information retrieved via wrapper.tickPrice().
	public static void tick_price(int ticker_id_, int field_, double price_)
	{
		String symbol = orders.get_symbol(ticker_id_);
		if (!strings.is_ok(symbol)) return;
		
		String key = strings.DEFAULT;
		
		update_data(symbol, key, price_);
	}

	//To manage the information retrieved via wrapper.tickSize().
	public static void tick_size(int ticker_id_, int field_, int size_)
	{
		String symbol = orders.get_symbol(ticker_id_);
		if (!strings.is_ok(symbol)) return;
		
		
	}
 
	//To manage the information retrieved via wrapper.tickGeneric().
	public static void tick_generic(int ticker_id_, int tick_type_, double value_)
	{
		String symbol = orders.get_symbol(ticker_id_);
		if (!strings.is_ok(symbol)) return;
		
		
	}

	//To manage the information retrieved via wrapper.error().
	public static void error(int id_, int error_code_, String error_msg_)
	{
		
	}
	
	private static <x> boolean update_data(String symbol_, String key_, double val_)
	{
		boolean is_ok = false;
		if (!strings.is_ok(symbol_) || !strings.is_ok(key_) || val_ <= 0.0) return is_ok;
		
		String storage = _config.get_async(types._CONFIG_ASYNC_STORAGE);
		if (!strings.is_ok(storage)) return is_ok;
		
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(key_, strings.from_number_decimal(val_));
		
		if (storage.equals(keys.DB)) 
		{
			String type = types._CONFIG_DB_TABLE_CONN;
			String where = strings.DEFAULT;
			
			is_ok = db.update(type, vals, where);
		}
		else if (storage.equals(keys.MEMORY)) 
		{
			if (!data.containsKey(symbol_)) data.put(symbol_, new ArrayList<HashMap<String, String>>());
			
			data.get(symbol_).add(new HashMap<String, String>(vals));
			is_ok = true;
		}
		
		return is_ok;
	}
}