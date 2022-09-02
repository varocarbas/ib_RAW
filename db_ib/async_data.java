package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.strings;
import external_ib.data;

public abstract class async_data 
{
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
	public static final String FLU_PRICE = common.FIELD_FLU_PRICE;
	public static final String FLU2 = common.FIELD_FLU2;
	public static final String FLU2_MIN = common.FIELD_FLU2_MIN;
	public static final String FLU2_MAX = common.FIELD_FLU2_MAX;
	public static final String VAR_TOT = common.FIELD_VAR_TOT;

	private static HashMap<String, String> _cols = new HashMap<String, String>();

	public static boolean contains_active(String source_, int max_mins_inactive_) { return (common.get_count(source_, get_where_active(source_, max_mins_inactive_)) > 0); }

	public static boolean exists(String source_, String symbol_) { return common.exists(source_, common.get_where_symbol(source_, symbol_)); }

	public static double get_price(String source_, String symbol_) { return common.get_decimal(source_, PRICE, common.get_where_symbol(source_, symbol_)); }
	
	public static boolean is_enabled(String source_, String symbol_) { return common.is_enabled(source_, common.get_where_symbol(source_, symbol_)); }

	public static ArrayList<String> get_all_symbols(String source_) { return get_all_symbols(source_, db.DEFAULT_WHERE); }

	public static ArrayList<String> get_all_symbols(String source_, String where_) { return get_active_symbols(source_, 0, where_); }
	
	public static ArrayList<String> get_active_symbols(String source_, int max_mins_inactive_, String where_) 
	{ 
		String where = null;
		
		if (max_mins_inactive_ > 0) where = get_where_active(source_, max_mins_inactive_);
		if (strings.is_ok(where_)) where = (strings.is_ok(where) ? common.join_wheres(where, where_) : where_);
		
		return common.get_all_strings(source_, SYMBOL, where); 
	}

	public static boolean symbol_is_active(String source_, String symbol_, int max_mins_inactive_) { return common.exists(source_, common.join_wheres(common.get_where_symbol(source_, symbol_), get_where_active(source_, max_mins_inactive_))); } 

	public static String get_where_active(String source_, int max_mins_inactive_) { return common.get_where_timestamp(source_, max_mins_inactive_); }

	public static boolean is_halted(String source_, String symbol_, boolean is_quick_)
	{
		int temp = common.get_int(source_, (is_quick_ ? get_col(HALTED) : HALTED), common.get_where_symbol(source_, symbol_), is_quick_);
	
		return data.is_halted(temp);
	}
	
	public static int get_halted_tot(String source_, String symbol_, boolean is_quick_) { return common.get_int(source_, (is_quick_ ? get_col(HALTED_TOT) : HALTED_TOT), common.get_where_symbol(source_, symbol_), is_quick_); }
	
	public static long get_elapsed_ini(String source_, String symbol_, boolean is_quick_) { return common.get_long(source_, (is_quick_ ? get_col(ELAPSED_INI) : ELAPSED_INI), common.get_where_symbol(source_, symbol_), is_quick_); }

	public static boolean update(String source_, Object vals_, String symbol_, boolean is_quick_) { return common.update(source_, vals_, symbol_, is_quick_); }

	public static boolean update_halted_tot(String source_, String symbol_, boolean is_quick_) 
	{ 
		boolean output = false;
		
		int val = get_halted_tot(source_, symbol_, is_quick_);
		
		if (val < 0) val = 0;
		val++;
		
		if (is_quick_) output = update_quick(source_, symbol_, get_col(HALTED_TOT), Integer.toString(val));
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
	
	public static boolean update_quick(String source_, String symbol_, HashMap<String, String> vals_) { return common.update_quick(source_, vals_, common.get_where_symbol(source_, symbol_)); }

	public static boolean update_timestamp(String source_, String symbol_, boolean is_quick_) { return common.update(source_, (is_quick_ ? common.get_col(source_, accessory.db.FIELD_TIMESTAMP) : accessory.db.FIELD_TIMESTAMP), dates.get_now_string(dates.FORMAT_TIMESTAMP), common.get_where_symbol(source_, symbol_), is_quick_); }
	
	public static boolean insert(String source_, String symbol_) { return common.insert(source_, get_default_vals(source_, symbol_)); }

	public static boolean insert_quick(String source_, String symbol_) { return common.insert_quick(source_, get_default_vals_quick(source_, symbol_)); }

	public static boolean delete(String source_, String symbol_) { return common.delete(source_, common.get_where_symbol(source_, symbol_)); }

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
		vals.put(get_col(SYMBOL), symbol_);

		if (arrays.value_exists(common.get_all_sources_elapsed(), source_)) vals.put(get_col(ELAPSED_INI), Long.toString(dates.start_elapsed()));

		return vals;
	}

	public static double get_in_vals_number(String field_, Object vals_, boolean is_quick_) { return get_vals_number(field_, vals_, false, is_quick_); } 

	public static double get_out_vals_number(String field_, HashMap<String, String> vals_, boolean is_quick_) { return get_vals_number(field_, vals_, true, is_quick_); } 
	
	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String field_, Object val_, Object vals_, boolean is_quick_) 
	{ 
		Object output = null;
		
		if (is_quick_)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(get_col(field_), accessory.db.adapt_input(val_));
		
			output = vals;
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_, val_);
			
			output = vals;
		}
		
		return output;
	}

	public static String get_col(String field_) 
	{ 
		if (_cols.size() == 0) populate_cols();
		
		return (_cols.containsKey(field_) ? _cols.get(field_) : strings.DEFAULT);
	}

	@SuppressWarnings("unchecked")
	private static double get_vals_number(String field_, Object vals_, boolean is_out_, boolean is_quick_) 
	{ 
		double output = ib.common.WRONG_VALUE;
		if (!arrays.is_ok(vals_)) return output;
		
		String key = (is_quick_ ? get_col(field_) : field_);
		
		if (is_out_ || is_quick_) 
		{
			HashMap<String, String> vals = arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);			
			if (vals.containsKey(key)) output = Double.parseDouble(vals.get(key));
		}
		else
		{
			HashMap<String, Object> vals = arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);			
			if (vals.containsKey(key)) output = (double)vals.get(key);	
		}
		
		return output;
	}

	private static String[] get_all_fields() 
	{ 
		return new String[]
		{
			ENABLED, SYMBOL, PRICE, OPEN, CLOSE, LOW, HIGH, ASK, BID, HALTED, VOLUME,
			SIZE, ASK_SIZE, BID_SIZE, HALTED_TOT, TIME, TIME_ELAPSED, ELAPSED_INI,
			PRICE_INI, PRICE_MIN, PRICE_MAX, VOLUME_INI, VOLUME_MIN, VOLUME_MAX,
			FLU, FLU_PRICE, FLU2, FLU2_MIN, FLU2_MAX, VAR_TOT
		};
	}
	
	private static String[] get_all_sources() { return new String[] { watchlist.SOURCE, trades.SOURCE, market.SOURCE }; }

	private static void populate_cols() 
	{ 
		String[] sources = get_all_sources();
		
		for (String field: get_all_fields())
		{			
			for (String source: sources)
			{
				String col = db_ib.common.get_col(source, field);	
				if (!strings.is_ok(col)) continue;
				
				_cols.put(field, col);
				
				break;
			}
		}
	}
}