package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.generic;
import accessory.sql;
import accessory.strings;
import accessory_ib._config;
import accessory_ib.types;

public class db 
{
	static String COL_TYPE = "type";
	static String COL_SYMBOL = "symbol";
	static String COL_PRICE = "price";
	static String COL_QUANTITY = "quantity";
	static String COL_STOP = "stop";
	static String COL_START = "start";
	static String COL_ORDER_ID = "order_id";
	static String COL_STATUS = "status";
	static String COL_STATUS2 = "status2";
	static String COL_SIDE = "side";
	static String COL_FEES = "fees";
	static String COL_EXEC_ID = "exec_id";

	public static boolean update_val(String type_, String key_, String val_, String where_)
	{
		if (!strings.is_ok(key_) || !strings.is_ok(val_)) return false;
		
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(key_, val_);
		
		return update(type_, vals, where_);
	}
	
	public static boolean update(String type_, HashMap<String, String> vals_, String where_)
	{
		boolean is_ok = false;
		
		String table = get_table(type_);
		if (!strings.is_ok(table)) return is_ok;
	
		sql.update(table, vals_, where_);
		
		return sql.is_ok;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, String>> select(String type_, HashMap<String, String> params_)
	{
		ArrayList<HashMap<String, String>> output = (ArrayList<HashMap<String, String>>)generic.DEFAULT;
		
		String table = get_table(type_);
		if (!strings.is_ok(table)) return output;
		
		String where = strings.DEFAULT;
		int limit = 0;
		
		return sql.select(table, null, where, limit);
	}
	
	public static String[] get_cols(String type_)
	{
		String[] output = (String[])arrays.DEFAULT;
		if (!strings.is_ok(type_)) return output;
		
		if (type_.equals(types._CONFIG_DB_TABLE_CONN)) return get_cols_conn();
		else if (type_.equals(types._CONFIG_DB_TABLE_EXECS)) return get_cols_execs();
		
		return output;
	}
	
	private static String get_table(String type_)
	{
		return (strings.is_ok(type_) ? _config.get_db(type_) : strings.DEFAULT);
	}
	
	private static String[] get_cols_conn()
	{
		return new String[]
		{
			COL_TYPE, COL_SYMBOL, COL_PRICE, COL_QUANTITY, COL_STOP, 
			COL_START, COL_ORDER_ID, COL_STATUS, COL_STATUS2
		};
	}

	private static String[] get_cols_execs()
	{
		return new String[]
		{
			COL_SYMBOL, COL_PRICE, COL_QUANTITY, COL_SIDE, 
			COL_FEES, COL_ORDER_ID, COL_EXEC_ID				
		};
	}
}