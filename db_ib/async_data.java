package db_ib;

import java.util.HashMap;

import external_ib.data;

public abstract class async_data 
{
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;
	
	public static boolean exists(String source_, String symbol_) { return common.exists(source_, common.get_where_symbol(source_, symbol_)); }

	public static boolean is_enabled(String source_, String symbol_) { return common.is_enabled(source_, common.get_where_symbol(source_, symbol_)); }

	public static boolean is_halted(String source_, String symbol_, boolean is_quick_)
	{
		int temp = common.get_int(source_, (is_quick_ ? get_col(source_, HALTED) : HALTED), common.get_where_symbol(source_, symbol_, is_quick_), is_quick_);
	
		return data.is_halted(temp);
	}
	
	public static int get_halted_tot(String source_, String symbol_, boolean is_quick_) { return common.get_int(source_, (is_quick_ ? get_col(source_, HALTED_TOT) : HALTED_TOT), common.get_where_symbol(source_, symbol_, is_quick_), is_quick_); }
	
	public static boolean update_halted_tot(String source_, String symbol_, boolean is_quick_) 
	{ 
		boolean output = false;
		
		int val = get_halted_tot(source_, symbol_, is_quick_);
		
		if (val < 0) val = 0;
		val++;
		
		if (is_quick_) output = async_data.update_quick(source_, symbol_, get_col(source_, HALTED_TOT), Integer.toString(val));
		else output = async_data.update(source_, symbol_, HALTED_TOT, val);
		
		return output;
	}

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
	
	public static double get_val_number(String source_, String field_, HashMap<String, String> vals_, boolean is_quick_) 
	{ 
		String key = (is_quick_ ? get_col(source_, field_) : field_);
		
		return (vals_.containsKey(key) ? Double.parseDouble(vals_.get(key)) : ib.common.WRONG_VALUE);
	}
}