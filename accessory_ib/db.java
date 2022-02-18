package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import ib.common;

public class db 
{	
	static { _ini.load(); }
	
	public static HashMap<String, String> get_market_info(String symbol_)
	{
		HashMap<String, String> info = null;

		String type = types._CONFIG_IB_DB_MARKET_TABLE;
		String table = get_table(type);
		if (!strings.is_ok(table) || !strings.is_ok(symbol_)) return info;	

		String where = get_where_symbol(symbol_);

		ArrayList<HashMap<String, String>> temp = accessory.db.select(table, null, where, 1, null);
		if (arrays.is_ok(temp)) info = new HashMap<String, String>(temp.get(0));

		return info;
	}

	public static boolean update_market_val(String key_, double val_, String symbol_)
	{
		if (!strings.is_ok(key_) || !strings.is_ok(symbol_)) return false;

		HashMap<String, String> vals = get_table_vals_market(key_, val_);
		if (!arrays.is_ok(vals)) return false;
		
		vals.put(get_col(types._CONFIG_IB_DB_MARKET_TABLE, keys.TIME), common.get_market_time());

		return update_market(vals, symbol_);
	}

	private static boolean update_market(HashMap<String, String> vals_, String symbol_)
	{
		if (!arrays.is_ok(vals_) || !strings.is_ok(symbol_)) return false;

		String where = get_where_symbol(symbol_);

		return update(types._CONFIG_IB_DB_MARKET_TABLE, vals_, where);
	}

	public static boolean insert_market(HashMap<String, String> vals_, String symbol_)
	{
		if (!arrays.is_ok(vals_) || !strings.is_ok(symbol_)) return false;

		HashMap<String, String> vals = new HashMap<String, String>(vals_);
		vals.put(get_col(types._CONFIG_IB_DB_MARKET_TABLE, keys.SYMBOL), symbol_);

		return insert(types._CONFIG_IB_DB_MARKET_TABLE, vals);
	}
	
	private static <x> HashMap<String, String> get_table_vals_market(String key_, double val_)
	{
		return accessory.db.get_table_vals(types._CONFIG_IB_DB_MARKET_TABLE, null, key_, val_);
	}
	
	public static HashMap<String, String> get_default_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		String zero = strings.to_string(0.0);
		String table = types._CONFIG_IB_DB_MARKET_TABLE;
		
		vals.put(get_col(table, keys.TIME), "00:00");
		vals.put(get_col(table, keys.PRICE), zero);
		vals.put(get_col(table, keys.VOLUME), zero);
		vals.put(get_col(table, keys.OPEN), zero);
		vals.put(get_col(table, keys.CLOSE), zero);
		vals.put(get_col(table, keys.HIGH), zero);
		vals.put(get_col(table, keys.LOW), zero);
		vals.put(get_col(table, keys.BID), zero);
		vals.put(get_col(table, keys.BID_SIZE), zero);
		vals.put(get_col(table, keys.ASK), zero);
		vals.put(get_col(table, keys.ASK_SIZE), zero);

		return vals;
	}

	public static String get_where_symbol(String symbol_)
	{
		if (!strings.is_ok(symbol_)) return strings.DEFAULT;

		String table = types._CONFIG_IB_DB_MARKET_TABLE;
		String col = types._CONFIG_IB_DB_COMMON_COL_SYMBOL;
		
		return (get_variable(get_col(table, col)) + "=" + get_value(symbol_));
	}

	public static boolean update(String type_, HashMap<String, String> vals_, String where_)
	{
		String table = get_table(type_);
		if (!strings.is_ok(table)) return false;

		accessory.db.update(table, vals_, where_);

		return accessory.db._is_ok;
	}

	private static boolean insert(String type_, HashMap<String, String> vals_)
	{
		String table = get_table(type_);
		if (!strings.is_ok(table)) return false;

		accessory.db.insert(table, vals_);

		return accessory.db._is_ok;
	}
	
	private static String get_table(String table_)
	{
		return accessory.db.get_table_name(table_);
	}
	
	private static String get_col(String table_, String col_)
	{
		return accessory.db.get_col_name(table_, col_);
	}
	
	private static String get_variable(String input_)
	{
		return accessory.db.get_variable(input_);
	}

	private static String get_value(String input_)
	{
		return accessory.db.get_value(input_);	
	}
}