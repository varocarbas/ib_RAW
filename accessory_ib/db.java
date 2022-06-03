package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.data;
import accessory.db_field;
import accessory.db_where;
import accessory.strings;

public class db 
{	
	public static final String FIELD_SYMBOL = types.CONFIG_DB_FIELD_SYMBOL;
	public static final String FIELD_PRICE = types.CONFIG_DB_FIELD_PRICE;
	public static final String FIELD_SIZE = types.CONFIG_DB_FIELD_SIZE;
	public static final String FIELD_TIME = types.CONFIG_DB_FIELD_TIME;
	public static final String FIELD_OPEN = types.CONFIG_DB_FIELD_OPEN;
	public static final String FIELD_CLOSE = types.CONFIG_DB_FIELD_CLOSE;
	public static final String FIELD_LOW = types.CONFIG_DB_FIELD_LOW;
	public static final String FIELD_HIGH = types.CONFIG_DB_FIELD_HIGH;
	public static final String FIELD_VOLUME = types.CONFIG_DB_FIELD_VOLUME;		
	public static final String FIELD_ASK = types.CONFIG_DB_FIELD_ASK;
	public static final String FIELD_ASK_SIZE = types.CONFIG_DB_FIELD_ASK_SIZE;
	public static final String FIELD_BID = types.CONFIG_DB_FIELD_BID;
	public static final String FIELD_BID_SIZE = types.CONFIG_DB_FIELD_BID_SIZE;
	public static final String FIELD_HALTED = types.CONFIG_DB_FIELD_HALTED;
	public static final String FIELD_HALTED_TOT = types.CONFIG_DB_FIELD_HALTED_TOT;
	public static final String FIELD_ENABLED = types.CONFIG_DB_FIELD_ENABLED;
	
	public static final String SOURCE_MARKET = types.CONFIG_DB_IB_MARKET_SOURCE;

	public static final String DEFAULT_SOURCE = SOURCE_MARKET;

	public static HashMap<String, String> get_vals(String symbol_) { return get_vals(DEFAULT_SOURCE, symbol_); }
	
	public static HashMap<String, String> get_vals(String source_, String symbol_) { return accessory.db.select_one(source_, null, get_where_symbol(symbol_), null); }

	public static boolean exists(String symbol_) { return exists(DEFAULT_SOURCE, symbol_); }
	
	public static boolean exists(String source_, String symbol_) { return strings.is_ok(accessory.db.select_one_string(source_, FIELD_SYMBOL, get_where_symbol(symbol_), null)); }

	public static boolean is_enabled(String symbol_) { return is_enabled(DEFAULT_SOURCE, symbol_); }
	
	public static boolean is_enabled(String source_, String symbol_) { return accessory.db.select_one_boolean(source_, FIELD_ENABLED, get_where_symbol(symbol_), null); }
	
	public static boolean insert_all(ArrayList<String> symbols_) { return insert_all(DEFAULT_SOURCE, symbols_); }

	public static boolean insert_all(String source_, ArrayList<String> symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return false;
		
		for (String symbol: symbols_)
		{
			if (exists(symbol)) continue;
			if (!insert(source_, get_default_vals(source_, symbol))) return false;
			
			logs.update_screen(symbol, true);
		}
		
		return true; 
	}

	public static boolean insert(String symbol_) { return insert(DEFAULT_SOURCE, symbol_); }

	public static boolean insert(String source_, String symbol_) { return insert(source_, get_default_vals(source_, symbol_)); }
	
	public static boolean insert(HashMap<String, Object> vals_) { return update(DEFAULT_SOURCE, vals_); }

	public static boolean insert(String source_, HashMap<String, Object> vals_) 
	{ 
		accessory.db.insert(source_, vals_);

		return accessory.db.is_ok(source_);
	}

	public static boolean update(String symbol_) { return update(DEFAULT_SOURCE, symbol_, get_default_vals()); }

	public static boolean update(String source_, String symbol_) { return update(source_, symbol_, get_default_vals(source_, symbol_)); }

	public static boolean update_number(String symbol_, String field_, double val_) { return update(DEFAULT_SOURCE, symbol_, field_, val_); }
	
	public static boolean update(String source_, String symbol_, String field_, Object val_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(source_, symbol_, vals);
	}

	public static boolean update(HashMap<String, Object> vals_) { return update(DEFAULT_SOURCE, vals_); }

	public static boolean update(String source_, HashMap<String, Object> vals_) { return update(source_, null, vals_); }
	
	public static boolean update(String source_, String symbol_, HashMap<String, Object> vals_)
	{
		String where = get_where_symbol(source_, (strings.is_ok(symbol_) ? symbol_ : get_symbol(vals_)));
		
		accessory.db.update(source_, vals_, where);
		
		return accessory.db.is_ok(source_);
	}

	public static ArrayList<String> get_all_symbols() { return get_all_symbols(DEFAULT_SOURCE); }

	public static ArrayList<String> get_all_symbols(String source_) { return accessory.db.select_some_strings(source_, FIELD_SYMBOL, (new db_where(source_, FIELD_ENABLED, true)).toString(), 0, null); }

	public static String get_where_symbol(String symbol_) { return get_where_symbol(DEFAULT_SOURCE, symbol_); }

	public static String get_where_symbol(String source_, String symbol_) { return (new db_where(source_, FIELD_SYMBOL, symbol_)).toString(); }

	public static HashMap<String, Object> get_default_vals() { return get_default_vals(DEFAULT_SOURCE, null); }

	public static HashMap<String, Object> get_default_vals(String symbol_) { return get_default_vals(DEFAULT_SOURCE, symbol_); }
	
	public static HashMap<String, Object> get_default_vals(String source_, String symbol_)
	{
		HashMap<String, db_field> fields = accessory.db.get_source_fields(source_);
		if (!arrays.is_ok(fields)) return null;
	
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		for (Entry<String, db_field> item: fields.entrySet())
		{
			String key = item.getKey();
			Object val = null; 
			
			if (key.equals(FIELD_SYMBOL))
			{
				if (strings.is_ok(symbol_)) val = symbol_;
				else continue;
			}
			else if (key.equals(FIELD_ENABLED)) val = accessory.db.adapt_input(source_, key, true);
			else val = get_default_val(source_, key, item.getValue().get_type());
			
			output.put(key, val);
		}

		return output;
	}

	private static Object get_default_val(String source_, String field_, String type_)
	{
		Object output = null;
		
		if (strings.are_equal(source_, SOURCE_MARKET))
		{
			if (strings.are_equal(field_, FIELD_TIME)) output = "00:00";
		}
		if (output != null || !strings.is_ok(type_)) return output;
		
		if (data.is_number(type_)) output = 0;
		
		return (output == null ? strings.DEFAULT : output);
	}

	private static String get_symbol(HashMap<String, Object> vals_) 
	{ 
		Object output = arrays.get_value(vals_, FIELD_SYMBOL); 
		
		return (output == null ? strings.DEFAULT : (String)output);
	}
}