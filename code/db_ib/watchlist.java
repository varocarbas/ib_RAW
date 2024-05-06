package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.db;
import accessory.db_common;

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
	public static final String FLU = common.FIELD_FLU;
	public static final String FLU2 = common.FIELD_FLU2;
	public static final String FLU2_MIN = common.FIELD_FLU2_MIN;
	public static final String FLU2_MAX = common.FIELD_FLU2_MAX;
	public static final String FLU3 = common.FIELD_FLU3;
	public static final String FLUS_PRICE = common.FIELD_FLUS_PRICE;
	public static final String VAR_TOT = common.FIELD_VAR_TOT;

	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	
	
	public static boolean update(Object vals_, String symbol_) { return async_data.update(SOURCE, symbol_, vals_); }

	public static ArrayList<HashMap<String, String>> get_all_vals() { return db_common.get_all_vals(SOURCE, db_common.get_fields(SOURCE), db.DEFAULT_WHERE); }
	
	public static HashMap<String, String> get_vals(String symbol_) { return db_common.get_vals(SOURCE, db_common.get_fields(SOURCE), common.get_where_symbol(SOURCE, symbol_)); }
	
	public static boolean delete(String symbol_) { return db_common.delete(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }
}