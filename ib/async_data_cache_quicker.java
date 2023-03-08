package ib;

import accessory.arrays_quick;
import accessory.db_cache;
import accessory.db_cache_mysql;
import db_ib.cache;

class async_data_cache_quicker 
{
	public static String[] COLS = null;
	
	public static int EXISTS = db_cache.WRONG_ID;
	
	public static boolean exists(String symbol_) { return db_cache_mysql.exists_simple(EXISTS, cache.add_changing_val(get_col_id(async_data_quicker.COL_SYMBOL), symbol_)); }
	
	public static void populate(String source_) { add_exists(source_); }
	
	private static void add_exists(String source_)
	{
		String col = async_data_quicker.COL_SYMBOL;
		
		String where = cache.get_variable(source_, col) + "=" + cache.get_value(source_);
		
		String query = cache.get_query_exists(source_, where);
		
		EXISTS = cache.add_query(source_, query, col, get_col_id(col), true);
	}
	
	private static int get_col_id(String col_) { return arrays_quick.get_i(COLS, col_); }
}