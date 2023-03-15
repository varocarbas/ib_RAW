package ib;

import accessory.arrays_quick;
import accessory.db;
import accessory.db_cache;
import accessory.db_cache_mysql;

class async_data_cache_quicker 
{	
	public static int _exists = db_cache.WRONG_ID;

	private static String[] _cols = null;
	
	public static boolean exists(String symbol_) { return db_cache_mysql.exists_simple(_exists, db_cache.add_changing_val(get_col_id(async_data_quicker._col_symbol), symbol_)); }
	
	public static void populate(String source_) 
	{ 
		_cols = db.get_cols(source_);
		
		add_exists(source_); 
	}
	
	private static void add_exists(String source_)
	{
		String col = async_data_quicker._col_symbol;
		
		String where = db_cache.get_variable(source_, col) + "=" + db_cache.get_value(source_);
		
		String query = db_cache.get_query_select_count(source_, where);
		
		_exists = db_cache.add_query(source_, query, col, get_col_id(col), true);
	}
	
	private static int get_col_id(String col_) { return arrays_quick.get_i(_cols, col_); }
}