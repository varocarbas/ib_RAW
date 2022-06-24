package db_ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.data;
import accessory.db_field;
import accessory.db_where;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.types;

public class common 
{
	public static final String SOURCE_MARKET = types.CONFIG_DB_IB_MARKET_SOURCE;
	public static final String SOURCE_EXECS = types.CONFIG_DB_IB_EXECS_SOURCE;
	public static final String SOURCE_BASIC = types.CONFIG_DB_IB_BASIC_SOURCE;
	public static final String SOURCE_REMOTE = types.CONFIG_DB_IB_REMOTE_SOURCE;
	public static final String SOURCE_ORDERS = types.CONFIG_DB_IB_ORDERS_SOURCE;
	
	public static final String FIELD_SYMBOL = types.CONFIG_DB_FIELD_SYMBOL;
	public static final String FIELD_PRICE = types.CONFIG_DB_FIELD_PRICE;
	public static final String FIELD_SIZE = types.CONFIG_DB_FIELD_SIZE;
	public static final String FIELD_TIME = types.CONFIG_DB_FIELD_TIME;
	public static final String FIELD_OPEN = types.CONFIG_DB_FIELD_OPEN;
	public static final String FIELD_CLOSE = types.CONFIG_DB_FIELD_CLOSE;
	public static final String FIELD_LOW = types.CONFIG_DB_FIELD_LOW;
	public static final String FIELD_HIGH = types.CONFIG_DB_FIELD_HIGH;
	public static final String FIELD_VOLUME = types.CONFIG_DB_FIELD_VOLUME;		
	public static final String FIELD_ASK = types.CONFIG_DB_FIELD_ASK;
	public static final String FIELD_ASK_SIZE = types.CONFIG_DB_FIELD_ASK_SIZE;
	public static final String FIELD_BID = types.CONFIG_DB_FIELD_BID;
	public static final String FIELD_BID_SIZE = types.CONFIG_DB_FIELD_BID_SIZE;
	public static final String FIELD_HALTED = types.CONFIG_DB_FIELD_HALTED;
	public static final String FIELD_HALTED_TOT = types.CONFIG_DB_FIELD_HALTED_TOT;
	public static final String FIELD_ENABLED = types.CONFIG_DB_FIELD_ENABLED;

	public static final String FIELD_USER = types.CONFIG_DB_FIELD_USER;
	public static final String FIELD_ORDER_ID = types.CONFIG_DB_FIELD_ORDER_ID;
	public static final String FIELD_QUANTITY = types.CONFIG_DB_FIELD_QUANTITY;
	public static final String FIELD_SIDE = types.CONFIG_DB_FIELD_SIDE;
	public static final String FIELD_FEES = types.CONFIG_DB_FIELD_FEES;
	public static final String FIELD_EXEC_ID = types.CONFIG_DB_FIELD_EXEC_ID;
	
	public static final String FIELD_MONEY = types.CONFIG_DB_FIELD_MONEY;
	public static final String FIELD_MONEY_INI = types.CONFIG_DB_FIELD_MONEY_INI;
	public static final String FIELD_CONN_TYPE = types.CONFIG_DB_FIELD_CONN_TYPE;

	public static final String FIELD_START = types.CONFIG_DB_FIELD_START;
	public static final String FIELD_START2 = types.CONFIG_DB_FIELD_START2;
	public static final String FIELD_STOP = types.CONFIG_DB_FIELD_STOP;
	public static final String FIELD_ORDER_ID_MAIN = types.CONFIG_DB_FIELD_ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = types.CONFIG_DB_FIELD_ORDER_ID_SEC;
	public static final String FIELD_STATUS = types.CONFIG_DB_FIELD_STATUS;
	public static final String FIELD_STATUS2 = types.CONFIG_DB_FIELD_STATUS2;
	public static final String FIELD_IS_MARKET = types.CONFIG_DB_FIELD_IS_MARKET;
	public static final String FIELD_TYPE_PLACE = types.CONFIG_DB_FIELD_TYPE_PLACE;
	public static final String FIELD_TYPE_MAIN = types.CONFIG_DB_FIELD_TYPE_MAIN;
	public static final String FIELD_TYPE_SEC = types.CONFIG_DB_FIELD_TYPE_SEC;

	public static final int MAX_LENGTH_USER = 15;

	public static final String DEFAULT_DB = types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;
	
	public static boolean exists(String source_, String where_) { return strings.is_ok(accessory.db.select_one_string(source_, FIELD_SYMBOL, where_, null)); }
	
	public static boolean is_enabled(String source_, String where_) { return accessory.db.select_one_boolean(source_, FIELD_ENABLED, where_, null); }

	public static HashMap<String, String> get_vals(String source_, String where_) { return get_vals(source_, null, where_); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_) { return accessory.db.select_one(source_, fields_, where_, null); }
	
	@SuppressWarnings("unchecked")
	public static boolean insert(String source_) { return insert(source_, (HashMap<String, Object>)get_default_vals(source_, false)); }

	public static boolean insert(String source_, HashMap<String, Object> vals_) 
	{ 
		accessory.db.insert(source_, vals_);

		return accessory.db.is_ok(source_);
	}

	@SuppressWarnings("unchecked")
	public static boolean insert_quick(String source_) { return insert_quick(source_, (HashMap<String, String>)get_default_vals(source_, true)); } 

	public static boolean insert_quick(String source_, HashMap<String, String> vals_) 
	{ 
		accessory.db.insert_quick(source_, vals_);

		return accessory.db.is_ok(source_);
	}
	
	public static <x> boolean update(String source_, HashMap<String, x> vals_, String where_)
	{		
		accessory.db.update(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}
	
	public static boolean update_quick(String source_, HashMap<String, String> vals_, String where_)
	{		
		accessory.db.update_quick(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}

	@SuppressWarnings("unchecked")
	public static Object get_default_vals(String source_, boolean is_quick_)
	{
		HashMap<String, db_field> fields = accessory.db.get_source_fields(source_);
		if (!arrays.is_ok(fields)) return null;
	
		Object output = (is_quick_ ? new HashMap<String, String>() : new HashMap<String, Object>());
	
		for (Entry<String, db_field> item: fields.entrySet())
		{
			String key = item.getKey();
			if (key.equals(accessory.db.FIELD_ID) || key.equals(accessory.db.FIELD_TIMESTAMP)) continue;
			
			Object val = null; 
			
			if (key.equals(FIELD_ENABLED)) val = (is_quick_ ? "1" : true);
			else val = get_default_val(source_, key, item.getValue().get_type());
			
			if (is_quick_) ((HashMap<String, String>)output).put(get_col(source_, key), strings.to_string(val));
			else ((HashMap<String, Object>)output).put(key, val);
		}

		return output;
	}
	
	public static String get_col(String source_, String field_) { return accessory.db.get_col(source_, field_); }

	public static boolean delete(String source_, String where_)
	{
		accessory.db.delete(source_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static String get_where_user(String source_) { return get_where(source_, FIELD_USER, ib.common.USER, false); }

	public static String get_where_symbol(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, false); }

	public static String get_where_symbol_quick(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, true); }
	
	public static String get_where(String source_, String field_, String val_, boolean is_quick_) { return get_where_internal(source_, field_, val_, is_quick_, true); }
	
	public static boolean source_includes_user(String source_) { return arrays.value_exists(get_all_sources_user(), source_); }

	public static String[] populate_all_sources_user() { return new String[] { SOURCE_BASIC, SOURCE_EXECS, SOURCE_ORDERS, SOURCE_REMOTE }; }
	
	private static String[] get_all_sources_user() { return _alls.DB_SOURCES_USER; }

	private static String get_where_internal(String source_, String field_, String val_, boolean is_quick_, boolean check_user_) 
	{
		String where = null;
		
		if (is_quick_) where = accessory.db.get_variable(get_col(source_, field_)) + "=" + accessory.db.get_value(val_);
		else where = (new db_where(source_, field_, val_)).toString();
		
		if (!strings.are_equal(field_, FIELD_USER) && check_user_ && source_includes_user(source_)) where = db_where.join(where, get_where_internal(source_, FIELD_USER, ib.common.USER, is_quick_, false), db_where.LINK_AND);
		
		return where;		
	}
	
	private static Object get_default_val(String source_, String field_, String type_)
	{
		Object output = null;
		
		if (strings.are_equal(source_, SOURCE_MARKET))
		{
			if (strings.are_equal(field_, FIELD_TIME)) output = "00:00";
		}
		if (output != null || !strings.is_ok(type_)) return output;
		
		if (data.is_number(type_)) output = 0;
		
		return (output == null ? strings.DEFAULT : output);
	}	
}