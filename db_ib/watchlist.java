package db_ib;

import java.util.HashMap;

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
	public static final String HALTED = common.FIELD_HALTED;
	public static final String HALTED_TOT = common.FIELD_HALTED_TOT;	
	public static final String FLU = common.FIELD_FLU;
	public static final String FLU2 = common.FIELD_FLU2;
	public static final String FLU2_MIN = common.FIELD_FLU2_MIN;
	public static final String FLU2_MAX = common.FIELD_FLU2_MAX;
	
	public static String[] get_fields() { return new String[] { SYMBOL, PRICE, PRICE_INI, PRICE_MIN, PRICE_MAX, VOLUME, VOLUME_INI, VOLUME_MIN, VOLUME_MAX, TIME_ELAPSED, HALTED, HALTED_TOT, FLU, FLU2, FLU2_MIN, FLU2_MAX }; }

	public static boolean exists(String symbol_) { return common.exists(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }
	
	public static boolean insert(String symbol_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(SYMBOL, symbol_);
		
		return common.insert(SOURCE, vals);
	}

	public static boolean delete(String symbol_) { return common.delete(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }
}