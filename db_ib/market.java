package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory_ib.logs;

public abstract class market 
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

	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static ArrayList<String> get_all_symbols() { return common.get_all_strings(SOURCE, SYMBOL, get_where_enabled()); }

	public static boolean insert_all(ArrayList<String> symbols_) 
	{ 
		if (!arrays.is_ok(symbols_)) return false;
		
		for (String symbol: symbols_)
		{
			if (async_data.exists(SOURCE, symbol)) continue;
			if (!async_data.insert(SOURCE, symbol)) return false;
			
			logs.update_screen(symbol, true);
		}
		
		return true; 
	}
	
	public static HashMap<String, String> get_vals(String symbol_, String[] field_cols_, boolean is_quick_) { return common.get_vals(SOURCE, field_cols_, common.get_where_symbol(SOURCE, symbol_), is_quick_); }
	
	public static String[] get_fields() { return new String[] { SYMBOL, PRICE, SIZE, TIME, OPEN, CLOSE, LOW, HIGH, VOLUME, ASK, ASK_SIZE, BID, BID_SIZE, HALTED, HALTED_TOT, ENABLED }; }

	public static String get_where_enabled() { return common.get_where(SOURCE, ENABLED, "1"); }
}