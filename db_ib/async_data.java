package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.db_where;
import accessory.strings;
import external_ib.data;

public abstract class async_data 
{
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;
	public static final String ELAPSED_INI = common.FIELD_ELAPSED_INI;

	public static boolean contains_active(String source_, int max_mins_inactive_) { return (common.get_count(source_, get_where_active(source_, max_mins_inactive_)) > 0); }

	public static boolean exists(String source_, String symbol_) { return common.exists(source_, common.get_where_symbol(source_, symbol_)); }

	public static double get_price(String source_, String symbol_) { return common.get_decimal(source_, PRICE, common.get_where_symbol(source_, symbol_)); }
	
	public static boolean is_enabled(String source_, String symbol_) { return common.is_enabled(source_, common.get_where_symbol(source_, symbol_)); }

	public static ArrayList<String> get_active_symbols(String source_, int max_mins_inactive_, String where_) 
	{ 
		String where = get_where_active(source_, max_mins_inactive_);
		if (strings.is_ok(where_)) where = db_where.join(where, where_, db_where.LINK_AND);
		
		return common.get_all_strings(source_, SYMBOL, where); 
	}
	
	public static String get_where_active(String source_, int max_mins_inactive_) { return common.get_where_timestamp(source_, max_mins_inactive_); }

	public static boolean is_halted(String source_, String symbol_, boolean is_quick_)
	{
		int temp = common.get_int(source_, (is_quick_ ? get_col(source_, HALTED) : HALTED), common.get_where_symbol(source_, symbol_, is_quick_), is_quick_);
	
		return data.is_halted(temp);
	}
	
	public static int get_halted_tot(String source_, String symbol_, boolean is_quick_) { return common.get_int(source_, (is_quick_ ? get_col(source_, HALTED_TOT) : HALTED_TOT), common.get_where_symbol(source_, symbol_, is_quick_), is_quick_); }
	
	public static long get_elapsed_ini(String source_, String symbol_, boolean is_quick_) { return common.get_long(source_, (is_quick_ ? get_col(source_, ELAPSED_INI) : ELAPSED_INI), common.get_where_symbol(source_, symbol_, is_quick_), is_quick_); }

	public static boolean update(String source_, Object vals_, String symbol_, boolean is_quick_) { return common.update(source_, vals_, symbol_, is_quick_); }

	public static boolean update_halted_tot(String source_, String symbol_, boolean is_quick_) 
	{ 
		boolean output = false;
		
		int val = get_halted_tot(source_, symbol_, is_quick_);
		
		if (val < 0) val = 0;
		val++;
		
		if (is_quick_) output = update_quick(source_, symbol_, get_col(source_, HALTED_TOT), Integer.toString(val));
		else output = update(source_, symbol_, HALTED_TOT, val);
		
		return output;
	}

	public static boolean update(String source_, String symbol_, String field_col_, Object val_, boolean is_quick_) { return (is_quick_ ? update_quick(source_, symbol_, field_col_, db.adapt_input(val_)) : update(source_, symbol_, field_col_, val_)); } 

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

	public static boolean insert(String source_, String symbol_) { return common.insert(source_, get_default_vals(source_, symbol_)); }

	public static boolean insert_quick(String source_, String symbol_) { return common.insert_quick(source_, get_default_vals_quick(source_, symbol_)); }

	public static boolean delete(String source_, String symbol_) { return common.delete(source_, common.get_where_symbol(source_, symbol_)); }

	public static String get_col(String source_, String field_) { return common.get_col(source_, field_); }

	public static HashMap<String, Object> get_default_vals(String source_, String symbol_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>(); 
		vals.put(SYMBOL, symbol_);
		
		if (arrays.value_exists(common.get_all_sources_elapsed(), source_)) vals.put(ELAPSED_INI, dates.start_elapsed());
		
		return vals;
	}
	
	public static HashMap<String, String> get_default_vals_quick(String source_, String symbol_) 
	{ 
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(get_col(source_, SYMBOL), symbol_);

		if (arrays.value_exists(common.get_all_sources_elapsed(), source_)) vals.put(get_col(source_, ELAPSED_INI), Long.toString(dates.start_elapsed()));

		return vals;
	}
	
	public static double get_vals_number(String source_, String field_, HashMap<String, String> vals_, boolean is_quick_) 
	{ 
		if (!arrays.is_ok(vals_)) return ib.common.WRONG_VALUE;
		
		String key = (is_quick_ ? get_col(source_, field_) : field_);
		
		return (vals_.containsKey(key) ? Double.parseDouble(vals_.get(key)) : ib.common.WRONG_VALUE);
	}
	
	public static Object add_to_vals(String source_, String field_, Object val_, Object vals_, boolean is_quick_) { return common.add_to_vals(source_, field_, val_, vals_, is_quick_); }
}