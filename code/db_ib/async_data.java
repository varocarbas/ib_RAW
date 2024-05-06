package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.db_common;
import accessory.strings;
import external_ib.data;

public abstract class async_data 
{
	public static final String MAIN_SOURCE_HALTED = market.SOURCE;
	
	public static final String TIMESTAMP = db.FIELD_TIMESTAMP;
	
	public static final String ENABLED = common.FIELD_ENABLED;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String OPEN = common.FIELD_OPEN;
	public static final String CLOSE = common.FIELD_CLOSE;
	public static final String LOW = common.FIELD_LOW;
	public static final String HIGH = common.FIELD_HIGH;
	
	public static final String ASK = common.FIELD_ASK;
	public static final String BID = common.FIELD_BID;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String VOLUME = common.FIELD_VOLUME;
	public static final String SIZE = common.FIELD_SIZE;
	public static final String ASK_SIZE = common.FIELD_ASK_SIZE;
	public static final String BID_SIZE = common.FIELD_BID_SIZE;

	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;
	public static final String TIME = common.FIELD_TIME;
	public static final String TIME_ELAPSED = common.FIELD_TIME_ELAPSED;
	public static final String ELAPSED_INI = common.FIELD_ELAPSED_INI;

	public static final String PRICE_INI = common.FIELD_PRICE_INI;
	public static final String PRICE_MIN = common.FIELD_PRICE_MIN;
	public static final String PRICE_MAX = common.FIELD_PRICE_MAX;
	public static final String VOLUME_INI = common.FIELD_VOLUME_INI;
	public static final String VOLUME_MIN = common.FIELD_VOLUME_MIN;
	public static final String VOLUME_MAX = common.FIELD_VOLUME_MAX;
	
	public static final String FLU = common.FIELD_FLU;
	public static final String FLU2 = common.FIELD_FLU2;
	public static final String FLU2_MIN = common.FIELD_FLU2_MIN;
	public static final String FLU2_MAX = common.FIELD_FLU2_MAX;
	public static final String FLU3 = common.FIELD_FLU3;
	public static final String VAR_TOT = common.FIELD_VAR_TOT;
	public static final String FLUS_PRICE = common.FIELD_FLUS_PRICE;	
	
	public static boolean contains_active(String source_, int max_mins_inactive_) { return (db_common.get_count(source_, get_where_active(source_, max_mins_inactive_)) > 0); }

	public static boolean exists(String source_, String symbol_) { return db_common.exists(source_, common.get_where_symbol(source_, symbol_)); }

	public static double get_price(String source_, String symbol_) { return common.get_decimal(source_, PRICE, common.get_where_symbol(source_, symbol_)); }
	
	public static boolean is_enabled(String source_, String symbol_) { return common.is_enabled(source_, common.get_where_symbol(source_, symbol_)); }

	public static ArrayList<String> get_all_symbols(String source_) { return get_all_symbols(source_, db.DEFAULT_WHERE); }

	public static ArrayList<String> get_all_symbols(String source_, String where_) { return get_active_symbols(source_, 0, where_); }
	
	public static ArrayList<String> get_active_symbols(String source_, int max_mins_inactive_, String where_) 
	{ 
		String where = null;
		
		if (max_mins_inactive_ > 0) where = get_where_active(source_, max_mins_inactive_);
		if (strings.is_ok(where_)) where = (strings.is_ok(where) ? db_common.join_wheres(where, where_) : where_);
		
		return db_common.get_all_strings(source_, SYMBOL, where); 
	}

	public static boolean symbol_is_active(String source_, String symbol_, int max_mins_inactive_) { return db_common.exists(source_, db_common.join_wheres(common.get_where_symbol(source_, symbol_), get_where_active(source_, max_mins_inactive_))); } 

	public static String get_where_active(String source_, int max_mins_inactive_) { return common.get_where_timestamp(source_, max_mins_inactive_); }

	public static boolean is_halted(String symbol_) { return data.is_halted(db_common.get_int(MAIN_SOURCE_HALTED, HALTED, common.get_where_symbol(MAIN_SOURCE_HALTED, symbol_), data.WRONG_HALTED)); }
	
	public static int get_halted_tot(String symbol_) { return common.get_int(MAIN_SOURCE_HALTED, HALTED_TOT, common.get_where_symbol(MAIN_SOURCE_HALTED, symbol_)); }
	
	public static long get_elapsed_ini(String source_, String symbol_) { return db_common.get_long(source_, ELAPSED_INI, common.get_where_symbol(source_, symbol_), dates.ELAPSED_START); }

	public static boolean update_halted_tot(String symbol_) 
	{ 
		int val = get_halted_tot(symbol_);
		
		if (val < 0) val = 0;
		val++;
		
		return update(MAIN_SOURCE_HALTED, symbol_, HALTED_TOT, val, true);
	}

	public static boolean update(String source_, String symbol_, String field_col_, Object val_, boolean is_field_) { return update(source_, symbol_, db_common.add_to_vals(source_, field_col_, db_common.get_input(source_, val_), null, is_field_, db_common.is_quick(source_))); }

	public static boolean update(String source_, String symbol_, Object vals_) { return db_common.update(source_, vals_, common.get_where_symbol(source_, symbol_)); }

	public static boolean update(String source_, String symbol_, Object vals_, boolean is_quick_) { return db_common.update(source_, vals_, common.get_where_symbol(source_, symbol_), is_quick_); }

	public static boolean update_timestamp(String source_, String symbol_) { return db_common.update(source_, accessory.db.FIELD_TIMESTAMP, dates.get_now_string(ib.common.FORMAT_TIMESTAMP, 0), common.get_where_symbol(source_, symbol_)); }
	
	public static boolean insert_new(String source_, String symbol_) { return common.insert(source_, get_default_vals_new(source_, symbol_)); }

	public static boolean delete(String source_, String symbol_) { return db_common.delete(source_, common.get_where_symbol(source_, symbol_)); }

	public static Object get_default_vals_new(String source_, String symbol_) 
	{ 
		Object vals = db_common.add_to_vals(source_, SYMBOL, symbol_, null); 
		
		if (arrays.value_exists(common.get_all_sources_elapsed(), source_)) vals = db_common.add_to_vals(source_, ELAPSED_INI, dates.start_elapsed(), vals);
		
		return vals;
	}

	public static double get_in_vals_number(String source_, String field_col_, Object vals_, boolean is_field_) { return get_vals_number(source_, field_col_, vals_, is_field_, false); } 

	public static double get_out_vals_number(String source_, String field_col_, HashMap<String, String> vals_, boolean is_field_) { return get_vals_number(source_, field_col_, vals_, is_field_, true); } 

	@SuppressWarnings("unchecked")
	private static double get_vals_number(String source_, String field_col_, Object vals_, boolean is_field_, boolean is_out_) 
	{ 
		double output = ib.common.WRONG_VALUE;
		if (!db_common.vals_are_ok(source_, vals_)) return output;
		
		boolean is_quick = db_common.is_quick(source_);
		
		String field_col = db_common.get_field_quick_col(source_, field_col_, is_field_, is_quick);
		
		if (is_out_ || is_quick) 
		{
			HashMap<String, String> vals = arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);			
			if (vals.containsKey(field_col)) output = Double.parseDouble(vals.get(field_col));
		}
		else
		{
			HashMap<String, Object> vals = arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);			
			if (vals.containsKey(field_col)) output = (double)vals.get(field_col);	
		}
		
		return output;
	}
}