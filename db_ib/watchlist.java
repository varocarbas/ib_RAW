package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;

public abstract class watchlist 
{
	public static final String SOURCE = common.SOURCE_WATCHLIST;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String PRICE_INI = common.FIELD_PRICE_INI;
	public static final String PRICE_MIN = common.FIELD_PRICE_MIN;
	public static final String PRICE_MAX = common.FIELD_PRICE_MAX;
	public static final String VOLUME = common.FIELD_VOLUME;
	public static final String VOLUME_INI = common.FIELD_VOLUME_INI;
	public static final String VOLUME_MIN = common.FIELD_VOLUME_MIN;
	public static final String VOLUME_MAX = common.FIELD_VOLUME_MAX;
	public static final String TIME_ELAPSED = common.FIELD_TIME_ELAPSED;
	public static final String ELAPSED_INI = common.FIELD_ELAPSED_INI;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;	
	public static final String FLU = common.FIELD_FLU;
	public static final String FLU2 = common.FIELD_FLU2;
	public static final String FLU2_MIN = common.FIELD_FLU2_MIN;
	public static final String FLU2_MAX = common.FIELD_FLU2_MAX;
	public static final String FLUS_PRICE = common.FIELD_FLUS_PRICE;
	public static final String VAR_TOT = common.FIELD_VAR_TOT;
	
	private static HashMap<String, String> _cols = new HashMap<String, String>();
	private static String[] _cols2 = null;

	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static String[] get_fields() { return new String[] { SYMBOL, PRICE, PRICE_INI, PRICE_MIN, PRICE_MAX, VOLUME, VOLUME_INI, VOLUME_MIN, VOLUME_MAX, TIME_ELAPSED, ELAPSED_INI, HALTED, HALTED_TOT, FLU, FLU2, FLU2_MIN, FLU2_MAX, FLUS_PRICE, VAR_TOT }; }

	public static String[] get_cols() 
	{ 
		if (_cols2 == null) populate_cols();
		
		return _cols2; 
	}
	
	public static boolean update(Object vals_, String symbol_, boolean is_quick_) { return async_data.update(SOURCE, vals_, symbol_, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_vals(boolean is_quick_) { return (is_quick_ ? common.get_all_vals_quick(SOURCE, get_cols(), accessory.db.DEFAULT_WHERE) : common.get_all_vals(SOURCE, get_fields(), accessory.db.DEFAULT_WHERE)); }
	
	public static HashMap<String, String> get_vals(String symbol_, boolean is_quick_) { return (is_quick_ ? common.get_vals_quick(SOURCE, get_cols(), common.get_where_symbol(SOURCE, symbol_)) : common.get_vals(SOURCE, get_fields(), common.get_where_symbol(SOURCE, symbol_))); }
	
	public static boolean delete(String symbol_) { return common.delete(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }

	public static String get_col(String field_) 
	{ 
		if (_cols.size() == 0) populate_cols();
		
		return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT);
	}
	
	private static void populate_cols() 
	{ 
		_cols = db_ib.common.populate_cols(SOURCE, get_fields()); 
		
		_cols2 = arrays.to_array(arrays.get_values_hashmap(_cols));	
	}
}