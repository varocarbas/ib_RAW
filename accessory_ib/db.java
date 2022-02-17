package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;

public class db 
{	
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

	public static boolean update_market_val(String col_, String val_, String symbol_)
	{
		if (!strings.is_ok(col_) || !strings.is_ok(val_) || !strings.is_ok(symbol_)) return false;

		String where = get_where_symbol(symbol_);

		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(col_, val_);

		return update_market(vals, where);
	}

	public static boolean update_market(HashMap<String, String> vals_, String symbol_)
	{
		if (!arrays.is_ok(vals_) || !strings.is_ok(symbol_)) return false;

		String where = get_where_symbol(symbol_);

		return update(types._CONFIG_IB_DB_MARKET_TABLE, vals_, where);
	}

	public static boolean insert_market(HashMap<String, String> vals_, String symbol_)
	{
		if (!arrays.is_ok(vals_) || !strings.is_ok(symbol_)) return false;

		HashMap<String, String> vals = new HashMap<String, String>(vals_);
		vals.put(get_col(keys.SYMBOL), symbol_);

		return insert(types._CONFIG_IB_DB_MARKET_TABLE, vals);
	}

	public static HashMap<String, String> get_default_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		String zero = strings.to_string(0.0);

		vals.put(get_col(keys.TIME), "00:00");
		vals.put(get_col(keys.PRICE), zero);
		vals.put(get_col(keys.VOLUME), zero);
		vals.put(get_col(keys.OPEN), zero);
		vals.put(get_col(keys.CLOSE), zero);
		vals.put(get_col(keys.HIGH), zero);
		vals.put(get_col(keys.LOW), zero);
		vals.put(get_col(keys.BID), zero);
		vals.put(get_col(keys.BID_SIZE), zero);
		vals.put(get_col(keys.ASK), zero);
		vals.put(get_col(keys.ASK_SIZE), zero);

		return vals;
	}

	public static String get_where_symbol(String symbol_)
	{
		if (!strings.is_ok(symbol_)) return strings.DEFAULT;

		return (get_variable(get_col(accessory_ib.types._CONFIG_IB_DB_COMMON_COL_SYMBOL)) + "=" + get_value(symbol_));
	}

	public static String get_col(String key_)
	{
		return _config.get_db(key_);
	}

	private static String get_variable(String input_)
	{
		return accessory.db.get_variable(input_);
	}

	private static String get_value(String input_)
	{
		return accessory.db.get_value(input_);	
	}

	private static boolean update(String type_, HashMap<String, String> vals_, String where_)
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

	private static String get_table(String type_)
	{
		return (strings.is_ok(type_) ? _config.get_db(type_) : strings.DEFAULT);
	}
}