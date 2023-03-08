package db_ib;

import java.util.HashMap;

import accessory.arrays_quick;
import accessory.db;
import accessory.db_cache;
import accessory.db_cache_mysql;
import accessory.strings;

public abstract class cache 
{	
	public static int add_query(String source_, String query_, boolean return_data_) { return add_query(source_, query_, null, db_cache.WRONG_ID, return_data_); }
	
	public static int add_query(String source_, String query_, String col_, int col_id_, boolean return_data_)
	{
		String query = (col_id_ != db_cache.WRONG_ID ? db_cache_mysql.add_placeholders(source_, query_, col_, col_id_) : query_);
	
		return db_cache_mysql.add(source_, query, return_data_, true);
	}

	public static String get_query_exists(String source_, String where_)
	{
		String output = "SELECT COUNT(*) FROM " + get_variable(source_, db.get_table(source_));
		
		if (strings.is_ok(where_)) output += " WHERE " + where_;

		return output;
	}
	
	public static String get_variable(String source_, String variable_) { return db.get_variable(source_, variable_); }
	
	public static String get_value(String source_) { return get_value(source_, null); }
	
	public static String get_value(String source_, String value_) { return db.get_value(source_, (value_ == null ? "placeholder" : value_)); }
	
	public static HashMap<Integer, String> add_changing_val(int col_id_, String val_) { return add_changing_val(col_id_, val_, null); }
	
	public static HashMap<Integer, String> add_changing_val(int col_id_, String val_, HashMap<Integer, String> changing_vals_) 
	{ 
		HashMap<Integer, String> output = (changing_vals_ != null ? new HashMap<Integer, String>(changing_vals_) : new HashMap<Integer, String>());
		
		output.put(col_id_, val_);
		
		return output; 
	}
	
	public static int get_col_id(String col_, String[] cols_) { return arrays_quick.get_i(cols_, col_); }
}