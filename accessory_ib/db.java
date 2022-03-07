package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.strings;
import ib.common;

public class db 
{	
	static { _ini.load(); }

	public static HashMap<String, String> get_market_info(String symbol_)
	{
		HashMap<String, String> info = null;

		String source = types._CONFIG_IB_DB_MARKET_SOURCE;
		if (!accessory.db.source_is_ok(source)) return info;	

		ArrayList<HashMap<String, String>> temp = accessory.db.select(source, null, get_where_symbol(symbol_, null), 1, null);
		if (arrays.is_ok(temp)) info = new HashMap<String, String>(temp.get(0));

		return info;
	}

	public static boolean update_market_val(String key_, double val_, String symbol_)
	{
		if (!strings.is_ok(key_) || !strings.is_ok(symbol_)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(key_, val_);
		vals.put(keys.TIME, common.get_market_time());

		return update_market(vals, symbol_);
	}

	private static boolean update_market(HashMap<String, Object> vals_, String symbol_)
	{
		accessory.db.update(types._CONFIG_IB_DB_MARKET_SOURCE, vals_, get_where_symbol(symbol_, null));

		return accessory.db._is_ok;
	}

	public static boolean insert_market(HashMap<String, String> vals_, String symbol_)
	{
		if (!arrays.is_ok(vals_) || !strings.is_ok(symbol_)) return false;

		HashMap<String, String> vals = new HashMap<String, String>(vals_);
		vals.put(get_col(types._CONFIG_IB_DB_MARKET_SOURCE, keys.SYMBOL), symbol_);

		return insert(types._CONFIG_IB_DB_MARKET_SOURCE, vals);
	}

	public static HashMap<String, String> get_default_vals()
	{
		HashMap<String, String> vals = new HashMap<String, String>();

		String zero = strings.to_string(0.0);
		String source = types._CONFIG_IB_DB_MARKET_SOURCE;

		vals.put(get_col(source, keys.TIME), "00:00");
		vals.put(get_col(source, keys.PRICE), zero);
		vals.put(get_col(source, keys.VOLUME), zero);
		vals.put(get_col(source, keys.OPEN), zero);
		vals.put(get_col(source, keys.CLOSE), zero);
		vals.put(get_col(source, keys.HIGH), zero);
		vals.put(get_col(source, keys.LOW), zero);
		vals.put(get_col(source, keys.BID), zero);
		vals.put(get_col(source, keys.BID_SIZE), zero);
		vals.put(get_col(source, keys.ASK), zero);
		vals.put(get_col(source, keys.ASK_SIZE), zero);

		return vals;
	}

	public static db_where[] get_where_symbol(String symbol_, db_where[] wheres_)
	{	
		if (!strings.is_ok(symbol_)) return wheres_;

		ArrayList<db_where> wheres = (arrays.is_ok(wheres_) ? arrays.to_arraylist(wheres_) : new ArrayList<db_where>());

		wheres.add
		(
			new db_where
			(
				types._CONFIG_IB_DB_MARKET_SOURCE, 
				types._CONFIG_IB_DB_COMMON_FIELD_SYMBOL, symbol_
			)
		);

		return arrays.to_array(wheres);
	}

	private static boolean insert(String source_, HashMap<String, String> vals_)
	{
		String table = get_table(source_);
		if (!strings.is_ok(table)) return false;

		accessory.db.insert(table, vals_);

		return accessory.db._is_ok;
	}

	private static String get_table(String source_)
	{
		return accessory.db.get_table(source_);
	}

	private static String get_col(String source_, String field_)
	{
		return accessory.db.get_col(source_, field_);
	}
}