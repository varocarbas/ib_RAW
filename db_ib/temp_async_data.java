package db_ib;

import accessory.strings;
import accessory_ib.types;
import ib.parent_async_data;

public abstract class temp_async_data 
{
	public static final String SOURCE = common.SOURCE_TEMP_ASYNC_DATA;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String ID = common.FIELD_ID;
	public static final String TYPE = common.FIELD_TYPE;
	public static final String DATA_TYPE = common.FIELD_DATA_TYPE;
	
	public static final String COL_SYMBOL = common.get_col(SOURCE, SYMBOL);
	public static final String COL_ID = common.get_col(SOURCE, ID);
	public static final String COL_TYPE = common.get_col(SOURCE, TYPE);
	public static final String COL_DATA_TYPE = common.get_col(SOURCE, DATA_TYPE);
	
	public static void truncate() { accessory.db.truncate_table(SOURCE); }

	public static boolean exists(String symbol_, boolean is_quick_) { return common.exists(SOURCE, get_where_symbol(symbol_, is_quick_)); }

	public static boolean exists(int id_, boolean is_quick_) { return common.exists(SOURCE, get_where_id(id_, is_quick_)); }

	public static int get_id(String symbol_, boolean is_quick_) 
	{ 
		int output = common.get_int(SOURCE, (is_quick_ ? COL_ID : ID), get_where_symbol(symbol_, is_quick_), is_quick_); 
	
		if (output == accessory.db.WRONG_INT) output = parent_async_data.WRONG_ID;
	
		return output;
	}
	
	public static String get_symbol(int id_, boolean is_quick_) 
	{ 
		String output = common.get_string(SOURCE, (is_quick_ ? COL_SYMBOL : SYMBOL), get_where_id(id_, is_quick_), is_quick_); 
	
		if (strings.are_equal(output, accessory.db.WRONG_STRING)) output = strings.DEFAULT;
	
		return output;
	}
	
	public static String get_type(int id_, boolean is_quick_) 
	{ 
		String output = common.get_string(SOURCE, (is_quick_ ? COL_TYPE : TYPE), get_where_id(id_, is_quick_), is_quick_); 
	
		if (strings.are_equal(output, accessory.db.WRONG_STRING)) output = strings.DEFAULT;
		else output = get_type_from_key(output);
		
		return output;
	}
	
	public static int get_data_type(int id_, boolean is_quick_) 
	{ 
		int output = common.get_int(SOURCE, (is_quick_ ? COL_DATA_TYPE : DATA_TYPE), get_where_id(id_, is_quick_), is_quick_); 
	
		if (output == accessory.db.WRONG_INT) output = parent_async_data.WRONG_DATA;
		
		return output;
	}

	public static boolean insert(Object vals_, boolean is_quick_) { return common.insert(SOURCE, vals_, is_quick_); }
	
	public static boolean update(Object vals_, String symbol_, boolean is_quick_) { return common.update(SOURCE, vals_, symbol_, is_quick_); }
	
	public static void delete(int id_, boolean is_quick_) { common.delete(SOURCE, get_where_id(id_, is_quick_)); }
	
	public static Object add_to_vals(String field_, Object val_, Object vals_, boolean is_quick_) { return common.add_to_vals(SOURCE, field_, val_, vals_, is_quick_); }

	public static String get_key_from_type(String type_) { return common.get_key_from_type(type_, types.ASYNC_DATA); }

	public static String get_type_from_key(String key_) { return common.get_type_from_key(key_, types.ASYNC_DATA); }
	
	private static String get_where_symbol(String symbol_, boolean is_quick_) { return common.get_where(SOURCE, SYMBOL, symbol_, is_quick_); }

	private static String get_where_id(int id_, boolean is_quick_) { return common.get_where(SOURCE, ID, Integer.toString(id_), is_quick_); }
}