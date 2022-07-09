package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_where;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.types;
import ib.basic;

public abstract class common 
{
	public static final String SOURCE_MARKET = types.CONFIG_DB_IB_MARKET_SOURCE;
	public static final String SOURCE_EXECS = types.CONFIG_DB_IB_EXECS_SOURCE;
	public static final String SOURCE_BASIC = types.CONFIG_DB_IB_BASIC_SOURCE;
	public static final String SOURCE_REMOTE = types.CONFIG_DB_IB_REMOTE_SOURCE;
	public static final String SOURCE_ORDERS = types.CONFIG_DB_IB_ORDERS_SOURCE;
	public static final String SOURCE_TRADES = types.CONFIG_DB_IB_TRADES_SOURCE;
	public static final String SOURCE_WATCHLIST = types.CONFIG_DB_IB_WATCHLIST_SOURCE;
	
	public static final String FIELD_SYMBOL = types.CONFIG_DB_IB_FIELD_SYMBOL;
	public static final String FIELD_PRICE = types.CONFIG_DB_IB_FIELD_PRICE;
	public static final String FIELD_SIZE = types.CONFIG_DB_IB_FIELD_SIZE;
	public static final String FIELD_TIME = types.CONFIG_DB_IB_FIELD_TIME;
	public static final String FIELD_OPEN = types.CONFIG_DB_IB_FIELD_OPEN;
	public static final String FIELD_CLOSE = types.CONFIG_DB_IB_FIELD_CLOSE;
	public static final String FIELD_LOW = types.CONFIG_DB_IB_FIELD_LOW;
	public static final String FIELD_HIGH = types.CONFIG_DB_IB_FIELD_HIGH;
	public static final String FIELD_VOLUME = types.CONFIG_DB_IB_FIELD_VOLUME;		
	public static final String FIELD_ASK = types.CONFIG_DB_IB_FIELD_ASK;
	public static final String FIELD_ASK_SIZE = types.CONFIG_DB_IB_FIELD_ASK_SIZE;
	public static final String FIELD_BID = types.CONFIG_DB_IB_FIELD_BID;
	public static final String FIELD_BID_SIZE = types.CONFIG_DB_IB_FIELD_BID_SIZE;
	public static final String FIELD_HALTED = types.CONFIG_DB_IB_FIELD_HALTED;
	public static final String FIELD_HALTED_TOT = types.CONFIG_DB_IB_FIELD_HALTED_TOT;
	public static final String FIELD_ENABLED = types.CONFIG_DB_IB_FIELD_ENABLED;

	public static final String FIELD_USER = types.CONFIG_DB_IB_FIELD_USER;
	public static final String FIELD_ORDER_ID = types.CONFIG_DB_IB_FIELD_ORDER_ID;
	public static final String FIELD_QUANTITY = types.CONFIG_DB_IB_FIELD_QUANTITY;
	public static final String FIELD_SIDE = types.CONFIG_DB_IB_FIELD_SIDE;
	public static final String FIELD_FEES = types.CONFIG_DB_IB_FIELD_FEES;
	public static final String FIELD_EXEC_ID = types.CONFIG_DB_IB_FIELD_EXEC_ID;
	
	public static final String FIELD_MONEY = types.CONFIG_DB_IB_FIELD_MONEY;
	public static final String FIELD_MONEY_INI = types.CONFIG_DB_IB_FIELD_MONEY_INI;
	public static final String FIELD_CONN_TYPE = types.CONFIG_DB_IB_FIELD_CONN_TYPE;
	public static final String FIELD_ACCOUNT_IB = types.CONFIG_DB_IB_FIELD_ACCOUNT_IB;
	public static final String FIELD_CURRENCY = types.CONFIG_DB_IB_FIELD_CURRENCY;
	
	public static final String FIELD_START = types.CONFIG_DB_IB_FIELD_START;
	public static final String FIELD_START2 = types.CONFIG_DB_IB_FIELD_START2;
	public static final String FIELD_STOP = types.CONFIG_DB_IB_FIELD_STOP;
	public static final String FIELD_ORDER_ID_MAIN = types.CONFIG_DB_IB_FIELD_ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = types.CONFIG_DB_IB_FIELD_ORDER_ID_SEC;
	public static final String FIELD_STATUS = types.CONFIG_DB_IB_FIELD_STATUS;
	public static final String FIELD_STATUS2 = types.CONFIG_DB_IB_FIELD_STATUS2;
	public static final String FIELD_IS_MARKET = types.CONFIG_DB_IB_FIELD_IS_MARKET;
	public static final String FIELD_TYPE_PLACE = types.CONFIG_DB_IB_FIELD_TYPE_PLACE;
	public static final String FIELD_TYPE_MAIN = types.CONFIG_DB_IB_FIELD_TYPE_MAIN;
	public static final String FIELD_TYPE_SEC = types.CONFIG_DB_IB_FIELD_TYPE_SEC;

	public static final String FIELD_TIME_ELAPSED = types.CONFIG_DB_IB_FIELD_TIME_ELAPSED;
	public static final String FIELD_UNREALISED = types.CONFIG_DB_IB_FIELD_UNREALISED;
	public static final String FIELD_IS_ACTIVE = types.CONFIG_DB_IB_FIELD_IS_ACTIVE;
	
	public static final String FIELD_PRICE_INI = types.CONFIG_DB_IB_FIELD_PRICE_INI;
	public static final String FIELD_PRICE_MIN = types.CONFIG_DB_IB_FIELD_PRICE_MIN;
	public static final String FIELD_PRICE_MAX = types.CONFIG_DB_IB_FIELD_PRICE_MAX;
	public static final String FIELD_VOLUME_INI = types.CONFIG_DB_IB_FIELD_VOLUME_INI;
	public static final String FIELD_VOLUME_MIN = types.CONFIG_DB_IB_FIELD_VOLUME_MIN;
	public static final String FIELD_VOLUME_MAX = types.CONFIG_DB_IB_FIELD_VOLUME_MAX;

	public static final String FIELD_FLU = types.CONFIG_DB_IB_FIELD_FLU;
	public static final String FIELD_FLU2 = types.CONFIG_DB_IB_FIELD_FLU2;
	public static final String FIELD_FLU2_MIN = types.CONFIG_DB_IB_FIELD_FLU2_MIN;
	public static final String FIELD_FLU2_MAX = types.CONFIG_DB_IB_FIELD_FLU2_MAX;
	
	public static final int MAX_SIZE_USER = 15;
	public static final int MAX_SIZE_MONEY = 7;
	public static final int MAX_SIZE_PRICE = 4;
	public static final int MAX_SIZE_VOLUME = 4;
	
	public static final String DEFAULT_DB = types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;
	public static final int DEFAULT_SIZE_DECIMAL = MAX_SIZE_MONEY;
	
	public static boolean exists(String source_, String where_) { return strings.is_ok(accessory.db.select_one_string(source_, FIELD_SYMBOL, where_, null)); }
	
	public static boolean is_enabled(String source_, String where_) { return (!arrays.value_exists(get_all_sources_enabled(), source_) || accessory.db.select_one_boolean(source_, FIELD_ENABLED, where_, null)); }

	public static HashMap<String, String> get_vals(String source_, String where_) { return get_vals(source_, null, where_); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_) { return accessory.db.select_one(source_, fields_, where_, null); }

	public static boolean insert(String source_, HashMap<String, Object> vals_) 
	{ 
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		if (source_includes_user(source_)) vals.put(FIELD_USER, basic.get_user());
			
		accessory.db.insert(source_, vals);

		return accessory.db.is_ok(source_);
	}

	public static boolean insert_quick(String source_, HashMap<String, String> vals_) 
	{ 
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		if (source_includes_user(source_)) vals.put(get_col(source_, FIELD_USER), basic.get_user());
		
		accessory.db.insert_quick(source_, vals);

		return accessory.db.is_ok(source_);
	}
	
	public static <x> boolean update(String source_, HashMap<String, x> vals_, String where_)
	{		
		accessory.db.update(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static boolean update_quick(String source_, HashMap<String, String> vals_, String where_)
	{		
		if (!arrays.is_ok(vals_)) return false;
		
		accessory.db.update_quick(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}
	
	public static <x> boolean insert_update(String source_, HashMap<String, Object> vals_, String where_)
	{	
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		if (source_includes_user(source_)) vals.put(FIELD_USER, basic.get_user());

		accessory.db.insert_update(source_, vals, where_);
		
		return accessory.db.is_ok(source_);
	}
	
	public static String get_col(String source_, String field_) { return accessory.db.get_col(source_, field_); }

	public static boolean delete(String source_, String where_)
	{
		accessory.db.delete(source_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static String get_where_user(String source_) { return get_where(source_, FIELD_USER, ib.basic.get_user(), false); }

	public static String get_where_symbol(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, false); }

	public static String get_where_symbol_quick(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, true); }
	
	public static String get_where(String source_, String field_, String val_, boolean is_quick_) { return get_where_internal(source_, field_, val_, is_quick_, true); }

	public static String get_where_order_id(String source_, int id_) { return get_where_order_id(source_, new Integer[] { id_ }, true); }

	public static String get_where_order_id(String source_, Integer[] ids_, boolean equal_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		if (source_includes_user(source_)) wheres.add(new db_where(source_, FIELD_USER, db_where.OPERAND_EQUAL, ib.basic.get_user(), db_where.LINK_AND));
		
		for (int id: ids_) { wheres.add(new db_where(source_, FIELD_ORDER_ID_MAIN, (equal_ ? db_where.OPERAND_EQUAL : db_where.OPERAND_NOT_EQUAL), id, db_where.LINK_AND)); }
		
		return db_where.to_string(wheres); 
	}
	
	public static boolean source_includes_user(String source_) { return arrays.value_exists(get_all_sources_user(), source_); }

	public static String[] populate_all_sources_user() { return new String[] { SOURCE_BASIC, SOURCE_EXECS, SOURCE_ORDERS, SOURCE_REMOTE, SOURCE_TRADES }; }

	public static String[] populate_all_sources_enabled() { return new String[] { SOURCE_MARKET }; }
	
	public static HashMap<String, Integer> populate_all_val_max_sizes()
	{
		HashMap<String, Integer> all = new HashMap<String, Integer>();
		
		all.put(FIELD_MONEY, MAX_SIZE_MONEY);
		all.put(FIELD_PRICE, MAX_SIZE_PRICE);
		all.put(FIELD_VOLUME, MAX_SIZE_VOLUME);
		
		return all;
	}
	
	public static HashMap<String, Integer> get_all_val_max_sizes() { return _alls.DB_VAL_MAX_SIZES; }
	
	private static String[] get_all_sources_user() { return _alls.DB_SOURCES_USER; }

	private static String[] get_all_sources_enabled() { return _alls.DB_SOURCES_ENABLED; }

	private static String get_where_internal(String source_, String field_, String val_, boolean is_quick_, boolean check_user_) 
	{
		String where = null;
		
		if (is_quick_) where = accessory.db.get_variable(get_col(source_, field_)) + "=" + accessory.db.get_value(val_);
		else where = (new db_where(source_, field_, val_)).toString();
		
		if (!strings.are_equal(field_, FIELD_USER) && check_user_ && source_includes_user(source_)) where = db_where.join(where, get_where_internal(source_, FIELD_USER, ib.basic.get_user(), is_quick_, false), db_where.LINK_AND);
		
		return where;		
	}
}