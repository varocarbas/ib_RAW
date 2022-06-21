package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory_ib.logs;

public class market 
{
	public static final String SOURCE = common.SOURCE_MARKET;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String SIZE = common.FIELD_SIZE;
	public static final String TIME = common.FIELD_TIME;
	public static final String OPEN = common.FIELD_OPEN;
	public static final String CLOSE = common.FIELD_CLOSE;
	public static final String LOW = common.FIELD_LOW;
	public static final String HIGH = common.FIELD_HIGH;
	public static final String VOLUME = common.FIELD_VOLUME;		
	public static final String ASK = common.FIELD_ASK;
	public static final String ASK_SIZE = common.FIELD_ASK_SIZE;
	public static final String BID = common.FIELD_BID;
	public static final String BID_SIZE = common.FIELD_BID_SIZE;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;
	public static final String ENABLED = common.FIELD_ENABLED;

	public static boolean exists(String symbol_) { return common.exists(SOURCE, get_where_symbol(symbol_)); }

	public static boolean is_enabled(String symbol_) { return common.is_enabled(SOURCE, get_where_symbol(symbol_)); }

	public static ArrayList<String> get_all_symbols() { return accessory.db.select_some_strings(SOURCE, SYMBOL, (new db_where(SOURCE, ENABLED, true)).toString(), 0, null); }

	public static boolean insert(String symbol_) { return common.insert(SOURCE, get_default_vals(symbol_)); }

	public static boolean insert_quick(String symbol_) { return common.insert_quick(SOURCE, get_default_vals_quick(symbol_)); }

	public static boolean update(String symbol_, String field_, Object val_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(symbol_, vals); 
	}

	public static <x> boolean update(String symbol_, HashMap<String, x> vals_) { return common.update(SOURCE, vals_, get_where_symbol(symbol_)); }

	public static boolean update_quick(String symbol_, String col_, String val_) 
	{ 
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(col_, val_);
		
		return update_quick(symbol_, vals); 
	}
	
	public static boolean update_quick(String symbol_, HashMap<String, String> vals_) { return common.update_quick(SOURCE, vals_, get_where_symbol_quick(symbol_)); }
	
	public static boolean insert_all(ArrayList<String> symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return false;
		
		for (String symbol: symbols_)
		{
			if (exists(symbol)) continue;
			if (!common.insert(SOURCE, get_default_vals(symbol))) return false;
			
			logs.update_screen(symbol, true);
		}
		
		return true; 
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> get_default_vals(String symbol_) 
	{ 
		HashMap<String, Object> vals = (HashMap<String, Object>)common.get_default_vals(SOURCE, false); 
		vals.put(SYMBOL, symbol_);
		
		return vals;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> get_default_vals_quick(String symbol_) 
	{ 
		HashMap<String, String> vals = (HashMap<String, String>)common.get_default_vals(SOURCE, true);
		vals.put(get_col(SYMBOL), symbol_);
		
		return vals;
	}
	
	public static HashMap<String, String> get_val(String symbol_) { return common.get_vals(SOURCE, null, get_where_symbol(symbol_)); }
	
	public static String get_col(String field_) { return common.get_col(SOURCE, field_); }

	private static String get_where_symbol(String symbol_) { return common.get_where_symbol(SOURCE, symbol_); }

	private static String get_where_symbol_quick(String symbol_) { return common.get_where_symbol_quick(SOURCE, symbol_); }
}