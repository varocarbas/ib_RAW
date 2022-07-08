package db_ib;

import java.util.HashMap;

import accessory.arrays;

public abstract class async_data 
{
	public static final String SYMBOL = common.FIELD_SYMBOL;
	
	public static boolean exists(String source_, String symbol_) { return common.exists(source_, common.get_where_symbol(source_, symbol_)); }

	public static boolean is_enabled(String source_, String symbol_) { return common.is_enabled(source_, common.get_where_symbol(source_, symbol_)); }

	public static boolean insert(String source_, String symbol_) { return common.insert(source_, get_default_vals(symbol_)); }

	public static boolean insert_quick(String source_, String symbol_) { return common.insert_quick(source_, get_default_vals_quick(source_, symbol_)); }
	
	public static boolean update(String source_, String symbol_, String field_, Object val_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(source_, symbol_, vals); 
	}

	public static <x> boolean update(String source_, String symbol_, HashMap<String, x> vals_) { return common.update(source_, vals_, common.get_where_symbol(source_, symbol_)); }

	public static boolean update_quick(String source_, String symbol_, String col_, String val_) 
	{ 
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(col_, val_);
		
		return update_quick(source_, symbol_, vals); 
	}
	
	public static boolean update_quick(String source_, String symbol_, HashMap<String, String> vals_) { return common.update_quick(source_, vals_, common.get_where_symbol_quick(source_, symbol_)); }

	public static String get_col(String source_, String field_) { return common.get_col(source_, field_); }
	
	public static HashMap<String, Object> get_default_vals(String symbol_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>(); 
		vals.put(SYMBOL, symbol_);
		
		return vals;
	}
	
	public static HashMap<String, String> get_default_vals_quick(String source_, String symbol_) 
	{ 
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(get_col(source_, SYMBOL), symbol_);
		
		return vals;
	}

	public static HashMap<String, String> populate_cols(String source_, String[] fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>();
		if (!arrays.is_ok(fields_)) return output;
		
		for (String field: fields_) { output.put(field, get_col(source_, field)); }
		
		return output;
	}
}