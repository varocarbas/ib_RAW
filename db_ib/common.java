package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.dates;
import accessory.db;
import accessory.db_quicker_mysql;
import accessory.db_where;
import accessory.generic;
import accessory.numbers;
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
	public static final String SOURCE_APPS = types.CONFIG_DB_IB_APPS_SOURCE;
	
	public static final String SOURCE_EXECS_OLD = types.CONFIG_DB_IB_EXECS_OLD_SOURCE;
	public static final String SOURCE_BASIC_OLD = types.CONFIG_DB_IB_BASIC_OLD_SOURCE;
	public static final String SOURCE_REMOTE_OLD = types.CONFIG_DB_IB_REMOTE_OLD_SOURCE;
	public static final String SOURCE_ORDERS_OLD = types.CONFIG_DB_IB_ORDERS_OLD_SOURCE;
	public static final String SOURCE_TRADES_OLD = types.CONFIG_DB_IB_TRADES_OLD_SOURCE;
	public static final String SOURCE_APPS_OLD = types.CONFIG_DB_IB_APPS_OLD_SOURCE;
	
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
	public static final String FIELD_ACCOUNT_IB = types.CONFIG_DB_IB_FIELD_ACCOUNT_IB;
	public static final String FIELD_CURRENCY = types.CONFIG_DB_IB_FIELD_CURRENCY;
	public static final String FIELD_MONEY_FREE = types.CONFIG_DB_IB_FIELD_MONEY_FREE;
	
	public static final String FIELD_START = types.CONFIG_DB_IB_FIELD_START;
	public static final String FIELD_START2 = types.CONFIG_DB_IB_FIELD_START2;
	public static final String FIELD_STOP = types.CONFIG_DB_IB_FIELD_STOP;
	public static final String FIELD_ORDER_ID_MAIN = types.CONFIG_DB_IB_FIELD_ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = types.CONFIG_DB_IB_FIELD_ORDER_ID_SEC;
	public static final String FIELD_STATUS = types.CONFIG_DB_IB_FIELD_STATUS;
	public static final String FIELD_STATUS2 = types.CONFIG_DB_IB_FIELD_STATUS2;
	public static final String FIELD_IS_MARKET = types.CONFIG_DB_IB_FIELD_IS_MARKET;
	public static final String FIELD_PERC_MONEY = types.CONFIG_DB_IB_FIELD_PERC_MONEY;
	public static final String FIELD_REQUEST = types.CONFIG_DB_IB_FIELD_REQUEST;
	public static final String FIELD_TYPE_ORDER = types.CONFIG_DB_IB_FIELD_TYPE_ORDER;
	
	public static final String FIELD_TYPE_PLACE = types.CONFIG_DB_IB_FIELD_TYPE_PLACE;
	public static final String FIELD_TYPE_MAIN = types.CONFIG_DB_IB_FIELD_TYPE_MAIN;
	public static final String FIELD_TYPE_SEC = types.CONFIG_DB_IB_FIELD_TYPE_SEC;

	public static final String FIELD_TIME_ELAPSED = types.CONFIG_DB_IB_FIELD_TIME_ELAPSED;
	public static final String FIELD_ELAPSED_INI = types.CONFIG_DB_IB_FIELD_ELAPSED_INI;
	public static final String FIELD_UNREALISED = types.CONFIG_DB_IB_FIELD_UNREALISED;
	public static final String FIELD_IS_ACTIVE = types.CONFIG_DB_IB_FIELD_IS_ACTIVE;
	public static final String FIELD_INVESTMENT = types.CONFIG_DB_IB_FIELD_INVESTMENT;
	public static final String FIELD_END = types.CONFIG_DB_IB_FIELD_END;
	public static final String FIELD_REALISED = types.CONFIG_DB_IB_FIELD_REALISED;

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
	public static final String FIELD_FLU_PRICE = types.CONFIG_DB_IB_FIELD_FLU_PRICE;
	public static final String FIELD_VAR_TOT = types.CONFIG_DB_IB_FIELD_VAR_TOT;
	
	public static final String FIELD_APP = types.CONFIG_DB_IB_FIELD_APP;
	public static final String FIELD_CONN_ID = types.CONFIG_DB_IB_FIELD_CONN_ID;
	public static final String FIELD_CONN_TYPE = types.CONFIG_DB_IB_FIELD_CONN_TYPE;
	public static final String FIELD_ERROR = types.CONFIG_DB_IB_FIELD_ERROR;
	public static final String FIELD_ADDITIONAL = types.CONFIG_DB_IB_FIELD_ADDITIONAL;
	public static final String FIELD_TIME2 = types.CONFIG_DB_IB_FIELD_TIME2;

	public static final String FIELD_ID = types.CONFIG_DB_IB_FIELD_ID;
	public static final String FIELD_TYPE = types.CONFIG_DB_IB_FIELD_TYPE;
	public static final String FIELD_DATA_TYPE = types.CONFIG_DB_IB_FIELD_DATA_TYPE;

	public static final String FIELD_DATE = types.CONFIG_DB_IB_FIELD_DATE;

	public static final String SEPARATOR = accessory.types.SEPARATOR;

	public static final String BACKUP_ENDING = SEPARATOR + "last";

	public static final int MAX_SIZE_USER = 15;
	public static final int MAX_SIZE_MONEY = 7;
	public static final int MAX_SIZE_PRICE = 4;
	public static final int MAX_SIZE_VOLUME = 5;
	public static final int MAX_SIZE_ERROR = 150;
	public static final int MAX_SIZE_APP_NAME = 30;
	public static final int MAX_SIZE_ADDITIONAL = 50;

	public static final int MAX_HOURS_ACTIVE = 12;
	
	public static final int WRONG_MAX_SIZE = 0;
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final String DEFAULT_DB = types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;
	
	public static final int DEFAULT_SIZE_DECIMAL = MAX_SIZE_MONEY;
	public static final int DEFAULT_SIZE_STRING = 30;

	private static ArrayList<String> _sources_quicker_mysql = new ArrayList<String>();
	private static HashMap<String, String> _sources_old = new HashMap<String, String>();
	
	private static boolean _sources_old_populated = false;
	private static String _col_id = null;

	public static void add_to_sources_quicker_mysql(String[] sources_)
	{
		if (!arrays.is_ok(sources_)) return;
		
		populate_sources_quicker_mysql(sources_);
	}
	
	public static void add_to_sources_old(String source_main_, String source_old_)
	{
		if (!strings.are_ok(new String[] { source_main_, source_old_ })) return;
		
		_sources_old.put(source_main_, source_old_);
	}

	public static HashMap<String, String> get_sources_old() 
	{
		if (!_sources_old_populated) populate_sources_old();
		
		return _sources_old; 
	}
	
	public static void __truncate(String source_) 
	{
		__backup(source_);
		
		accessory.db.truncate_table(source_);
	}
	
	public static void __backup(String source_) 
	{ 
		if (is_empty(source_)) return;
		
		String backup_table = accessory.db.get_table(source_) + BACKUP_ENDING;
		
		accessory.db.__backup_table(source_, backup_table); 
	}	
	
	public static boolean contains_active(String source_) { return (!is_empty(source_) && timestamp_is_today(source_, null)); }
	
	public static boolean is_empty(String source_) { return (get_count(source_, strings.DEFAULT) == 0); }

	public static int get_count(String source_, String where_) { return accessory.db.select_count(source_, where_); }

	public static boolean exists(String source_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.exists(source_, get_col_id(), where_) : accessory.db.exists_id(source_, where_)); }
	
	public static boolean is_enabled(String source_, String where_) { return (!arrays.value_exists(get_all_sources_enabled(), source_) || accessory.db.select_one_boolean(source_, FIELD_ENABLED, where_, db.DEFAULT_ORDER)); }

	public static String get_string(String source_, String field_, String where_) { return accessory.db.select_one_string(source_, field_, where_, db.DEFAULT_ORDER); }

	public static String get_string(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_string_quick(source_, col_field_, where_) : accessory.db.select_one_string(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static String get_string_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? accessory.db_quicker_mysql.select_one_string(source_, col_, where_, db.DEFAULT_ORDER) : db.select_one_string_quick(source_, col_, where_, db.DEFAULT_ORDER)); }
	
	public static int get_int(String source_, String field_, String where_) { return get_int(source_, field_, where_, false); }

	public static int get_int(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_int_quick(source_, col_field_, where_) : accessory.db.select_one_int(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static int get_int_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_one_int(source_, col_, where_, db.DEFAULT_ORDER) : db.select_one_int_quick(source_, col_, where_, db.DEFAULT_ORDER)); }

	public static double get_decimal(String source_, String field_, String where_) { return get_decimal(source_, field_, where_, false); }

	public static double get_decimal(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_decimal_quick(source_, col_field_, where_) : db.select_one_decimal(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static double get_decimal_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_one_decimal(source_, col_, where_, db.DEFAULT_ORDER) : db.select_one_decimal_quick(source_, col_, where_, db.DEFAULT_ORDER)); }

	public static long get_long(String source_, String field_, String where_) { return get_long(source_, field_, where_, false); }

	public static long get_long(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_long_quick(source_, col_field_, where_) : db.select_one_long(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static long get_long_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_one_long(source_, col_, where_, db.DEFAULT_ORDER) : db.select_one_long_quick(source_, col_, where_, db.DEFAULT_ORDER)); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_, String where_) { return get_all_decimals(source_, field_, where_, false); }

	public static ArrayList<Double> get_all_decimals(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_all_decimals_quick(source_, col_field_, where_) : db.select_some_decimals(source_, col_field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<Double> get_all_decimals_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_some_decimals(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_decimals_quick(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<String> get_all_strings(String source_, String field_, String where_) { return get_all_strings(source_, field_, where_, false); }

	public static ArrayList<String> get_all_strings(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_all_strings_quick(source_, col_field_, where_) : db.select_some_strings(source_, col_field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<String> get_all_strings_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_some_strings(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_strings_quick(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_, String where_) { return get_all_ints(source_, field_, where_, false); }

	public static ArrayList<Integer> get_all_ints(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? get_all_ints_quick(source_, col_field_, where_) : db.select_some_ints(source_, col_field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<Integer> get_all_ints_quick(String source_, String col_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_some_ints(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_ints_quick(source_, col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_cols_, String where_, boolean is_quick_) { return (is_quick_ ? get_all_vals_quick(source_, fields_cols_, where_) : get_all_vals(source_, fields_cols_, where_)); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_, String where_) { return accessory.db.select(source_, fields_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER); }

	public static ArrayList<HashMap<String, String>> get_all_vals_quick(String source_, String[] cols_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select(source_, cols_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_quick(source_, cols_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static HashMap<String, String> get_vals(String source_, String where_) { return get_vals(source_, null, where_); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_cols_, String where_, boolean is_quick_) { return (is_quick_ ? get_vals_quick(source_, fields_cols_, where_) : get_vals(source_, fields_cols_, where_)); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_) { return accessory.db.select_one(source_, fields_, where_, db.DEFAULT_ORDER); }

	public static HashMap<String, String> get_vals_quick(String source_, String[] cols_, String where_) { return (is_quicker_mysql(source_) ? db_quicker_mysql.select_one(source_, cols_, where_, db.DEFAULT_ORDER) : db.select_one_quick(source_, cols_, where_, db.DEFAULT_ORDER)); }

	public static String get_type(String source_, String field_, String root_, String where_) 
	{
		String key = get_string(source_, field_, where_);
		
		return (strings.is_ok(key) ? get_type_from_key(key, root_) : strings.DEFAULT);
	}

	@SuppressWarnings("unchecked")
	public static boolean insert(String source_, Object vals_, boolean is_quick_) { return (is_quick_ ? insert_quick(source_, (HashMap<String, String>)vals_) : insert(source_, (HashMap<String, Object>)vals_)); }

	public static boolean insert(String source_, HashMap<String, Object> vals_) 
	{ 
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		vals = get_insert_vals(source_, vals);
		accessory.db.insert(source_, vals);

		return accessory.db.is_ok(source_);
	}

	public static boolean insert_quick(String source_, HashMap<String, String> vals_) 
	{ 
		HashMap<String, String> vals = arrays.get_new_hashmap_xx(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		vals = get_insert_vals_quick(source_, vals);

		if (is_quicker_mysql(source_)) db_quicker_mysql.insert(source_, vals);
		else db.insert_quick(source_, vals);

		return accessory.db.is_ok(source_);
	}

	public static boolean update(String source_, Object vals_, String symbol_, boolean is_quick_) { return update_where(source_, vals_, get_where_symbol(source_, symbol_), is_quick_); }

	@SuppressWarnings("unchecked")
	public static boolean update_where(String source_, Object vals_, String where_, boolean is_quick_) { return (is_quick_ ? update_quick(source_, (HashMap<String, String>)vals_, where_) : update(source_, (HashMap<String, Object>)vals_, where_)); }

	public static boolean update(String source_, String field_col_, Object val_, String where_, boolean is_quick_) { return (is_quick_ ? update_quick(source_, field_col_, (String)val_, where_) : update(source_, field_col_, val_, where_)); }

	public static boolean update(String source_, String field_, Object val_, String where_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(source_, vals, where_); 
	}
	
	public static <x> boolean update(String source_, HashMap<String, x> vals_, String where_)
	{		
		accessory.db.update(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}
	
	public static boolean update_quick(String source_, String col_, String val_, String where_) 
	{ 
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(col_, val_);
		
		return update_quick(source_, vals, where_); 
	}

	public static boolean update_quick(String source_, HashMap<String, String> vals_, String where_)
	{		
		if (!arrays.is_ok(vals_)) return false;
		
		if (is_quicker_mysql(source_)) db_quicker_mysql.update(source_, vals_, where_); 
		else db.update_quick(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static boolean update_type(String source_, String field_, String type_, String root_, String where_) 
	{ 
		String type = get_key_from_type(type_, root_);
		if (!strings.is_ok(type)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, type);

		return update(source_, vals, where_);
	}
	
	public static boolean insert_update(String source_, String field_, Object val_, String where_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);

		return insert_update(source_, vals, where_);
	}

	public static boolean insert_update(String source_, HashMap<String, Object> vals_, String where_) { return insert_update(source_, vals_, where_, false); }

	@SuppressWarnings("unchecked")
	public static boolean insert_update(String source_, Object vals_, String where_, boolean is_quick_)
	{	
		if (!arrays.is_ok(vals_)) return false;
		
		Object vals = (is_quick_ ? get_insert_vals_quick(source_, (HashMap<String, String>)vals_) : get_insert_vals(source_, (HashMap<String, Object>)vals_));

		if (is_quick_) 
		{
			if (is_quicker_mysql(source_)) db_quicker_mysql.insert_update(source_, get_col_id(), (HashMap<String, String>)vals, where_); 
			else db.insert_update_id_quick(source_, (HashMap<String, String>)vals, where_);
		}
		else accessory.db.insert_update_id(source_, (HashMap<String, Object>)vals, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static boolean update_all_old_quick()
	{
		boolean is_ok = true;
		
		if (!_sources_old_populated) populate_sources_old();
		
		for (Entry<String, String> item: _sources_old.entrySet()) 
		{
			String source = item.getKey();
			String source_old = item.getValue();
			
			String message = db.get_table(source_old);
			
			if (!exists(source_old, get_where(source_old, FIELD_DATE, dates.get_now_string(ib.common.FORMAT_DATE))))
			{
				boolean temp = update_old_quick(source, source_old);
				if (!temp) is_ok = false;		
				
				message += " updated (" + (temp ? "ok" : "error") + ")";
			}
			
			generic.to_screen(message);
		}
	
		return is_ok;
	}

	public static boolean update_old_quick(String source_) 
	{
		if (!_sources_old_populated) populate_sources_old();
		
		return update_old_quick(source_, (String)arrays.get_value(_sources_old, source_)); 
	}
	
	public static String get_field_col(String source_, String field_, boolean is_quick_) { return (is_quick_ ? get_col(source_, field_) : field_); }

	public static String get_col(String source_, String field_) { return accessory.db.get_col(source_, field_); }

	public static boolean delete(String source_, String where_)
	{
		accessory.db.delete(source_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static String join_wheres(String where1_, String where2_) { return join_wheres(where1_, where2_, db_where.LINK_AND); }

	public static String join_wheres(String where1_, String where2_, String link_) { return db_where.join(where1_, where2_, link_); }
	
	public static String get_where_user(String source_) { return get_where(source_, FIELD_USER, ib.basic.get_user()); }

	public static String get_where_symbol(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_); }
	
	public static String get_where(String source_, String field_, String val_) { return get_where(source_, field_, val_, true); }

	public static String get_where(String source_, String field_, String val_, boolean add_user_) { return get_where_internal(source_, field_, val_, add_user_); }

	public static String get_where_order_id(String source_, int order_id_main_) { return get_where_order_id(source_, order_id_main_, true); }
	
	public static String get_where_order_id(String source_, int order_id_, boolean is_main_) { return get_where(source_, (is_main_ ? FIELD_ORDER_ID_MAIN : FIELD_ORDER_ID_SEC), Integer.toString(order_id_)); }

	public static String get_where_order_id(String source_, Integer[] ids_, boolean equal_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		if (source_includes_user(source_)) wheres.add(new db_where(source_, FIELD_USER, db_where.OPERAND_EQUAL, ib.basic.get_user(), db_where.LINK_AND));
		
		for (int id: ids_) { wheres.add(new db_where(source_, FIELD_ORDER_ID_MAIN, (equal_ ? db_where.OPERAND_EQUAL : db_where.OPERAND_NOT_EQUAL), id, db_where.LINK_AND)); }
		
		return db_where.to_string(wheres); 
	}
	
	public static boolean source_includes_user(String source_) { return arrays.value_exists(get_all_sources_user(), source_); }

	public static String[] populate_all_sources_user() { return new String[] { SOURCE_BASIC, SOURCE_APPS }; }
	
	public static String[] populate_all_sources() 
	{ 
		return new String[] 
		{ 
			SOURCE_MARKET, SOURCE_EXECS, SOURCE_BASIC, SOURCE_REMOTE, SOURCE_ORDERS, SOURCE_TRADES, SOURCE_WATCHLIST, SOURCE_APPS, 
			SOURCE_EXECS_OLD, SOURCE_BASIC_OLD, SOURCE_REMOTE_OLD, SOURCE_ORDERS_OLD, SOURCE_TRADES_OLD, SOURCE_APPS_OLD 
		}; 
	}

	public static String[] populate_all_sources_enabled() { return new String[] { SOURCE_MARKET }; }

	public static String[] populate_all_sources_elapsed() { return new String[] { SOURCE_TRADES, SOURCE_WATCHLIST }; }

	public static String[] get_all_sources_elapsed() { return _alls.DB_SOURCES_ELAPSED; }

	public static HashMap<String, Integer> populate_all_max_sizes_numbers()
	{
		HashMap<String, Integer> all = new HashMap<String, Integer>();

		all.put(FIELD_MONEY, MAX_SIZE_MONEY);
		all.put(FIELD_PRICE, MAX_SIZE_PRICE);
		all.put(FIELD_VOLUME, MAX_SIZE_VOLUME);
		
		return all;
	}
	
	public static HashMap<String, Double> populate_all_max_vals()
	{
		HashMap<String, Double> vals = new HashMap<String, Double>();
		
		for (Entry<String, Integer> item: populate_all_max_sizes_numbers().entrySet()) { vals.put(item.getKey(), get_max_val(item.getValue())); }
		
		return vals;
	}
	
	public static HashMap<String, Integer> populate_all_max_sizes_strings()
	{
		HashMap<String, Integer> all = new HashMap<String, Integer>();

		all.put(FIELD_USER, MAX_SIZE_USER);
		all.put(FIELD_ERROR, MAX_SIZE_ERROR);
		all.put(FIELD_APP, MAX_SIZE_APP_NAME);
		all.put(FIELD_ADDITIONAL, MAX_SIZE_ADDITIONAL);

		return all;
	}
	
	public static String[] populate_all_negative_numbers() { return new String[] { FIELD_MONEY }; }

	public static int get_max_size_number(String field_) { return get_max_size(field_, true); }

	public static int get_max_size_string(String field_) { return get_max_size(field_, false); }
	
	public static double adapt_price(double val_) { return adapt_number(val_, FIELD_PRICE); }
	
	public static double adapt_money(double val_) { return adapt_number(val_, FIELD_MONEY); }

	public static double adapt_number(double val_, String field_)
	{
		int max = get_max_size_number(field_);
		
		return (max <= WRONG_MAX_SIZE ? val_ : adapt_number(val_, max, arrays.key_exists(get_all_negative_numbers(), field_)));
	}
	
	public static double adapt_number(double val_, int max_) { return adapt_number(val_, max_, false); }
	
	public static double get_max_val(String field_) 
	{
		HashMap<String, Double> vals = get_all_max_vals();
		
		return ((strings.is_ok(field_) && vals.containsKey(field_)) ? vals.get(field_) : WRONG_MAX_VAL);
	}
	
	public static double get_max_val(int max_) { return (max_ <= WRONG_MAX_SIZE ? WRONG_MAX_SIZE : (Math.pow(10.0, ((double)max_ + 1.0)) - 1.0)); }

	public static String adapt_string(String val_, String field_)
	{
		int max = get_max_size_string(field_);
		
		return (max <= WRONG_MAX_SIZE ? val_ : adapt_string(val_, max));
	}
	
	public static String adapt_string(String val_, int max_) { return (strings.get_length(val_) <= get_max_length(max_) ? val_ : strings.truncate(val_, max_)); }
	
	public static int get_max_length(String field_) { return get_max_size_string(field_); }

	public static int get_max_length(int max_) { return (max_ <= WRONG_MAX_SIZE ? WRONG_MAX_SIZE : max_); }

	public static String get_type_from_key(String key_, String root_) { return ((strings.is_ok(key_) && strings.is_ok(root_)) ? (root_ + SEPARATOR + key_) : strings.DEFAULT); }

	public static String get_key_from_type(String type_, String root_) { return get_key(type_, root_); }

	public static String get_type_from_key(String key_, HashMap<String, String> array_) { return ((strings.is_ok(key_) && array_.containsValue(key_)) ? (String)arrays.get_key(array_, key_) : strings.DEFAULT); }

	public static String get_key_from_type(String type_, HashMap<String, String> array_) { return ((strings.is_ok(type_) && array_.containsKey(type_)) ? array_.get(type_) : strings.DEFAULT); }

	public static String get_key(String type_, String root_) { return accessory._keys.get_key(type_, root_); }
	
	public static HashMap<String, String> populate_cols(String source_, String[] fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>();
		if (!arrays.is_ok(fields_)) return output;
		
		for (String field: fields_) { output.put(field, get_col(source_, field)); }
	
		populate_col_id();
		
		return output;
	}
	
	public static String get_col_id()
	{
		if (_col_id == null) populate_col_id();
		
		return _col_id;
	}
	
	public static void populate_col_id() { _col_id = common.get_col(SOURCE_APPS, db.FIELD_ID); }
	
	public static String adapt_user(String val_) { return adapt_string(val_, FIELD_USER); }

	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String source_, String field_, Object val_, Object vals_, boolean is_quick_) 
	{ 
		Object output = null;
				
		if (is_quick_)
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(get_col(source_, field_), accessory.db.adapt_input(val_));
		
			output = vals;
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_, val_);
			
			output = vals;
		}
		
		return output;
	}

	public static boolean timestamp_is_today(String source_, String where_) 
	{ 
		String timestamp = get_string(source_, db.FIELD_TIMESTAMP, where_);
		
		return (db.WRONG_STRING.equals(timestamp) ? false : dates.is_today(timestamp, ib.common.FORMAT_TIMESTAMP)); 
	}

	public static String get_where_timestamp_today(String source_, String where_) 
	{ 
		String where = "DATE(" + db.get_variable(source_, db.get_col(source_, db.FIELD_TIMESTAMP)) + ") = CURDATE()"; 
	
		if (strings.is_ok(where_)) where = join_wheres(where_, where);
	
		return where;
	}
	
	public static String get_where_timestamp(String source_, int before_mins_) { return (before_mins_ > 0 ? (new db_where(source_, db.FIELD_TIMESTAMP, db_where.OPERAND_GREATER_EQUAL, "CURRENT_TIMESTAMP - INTERVAL " + before_mins_ + " MINUTE", false, db_where.DEFAULT_LINK)).toString() : strings.DEFAULT); }

	public static long get_timestamp_elapsed_secs(String source_, String where_) { return get_timestamp_elapsed(source_, where_, dates.UNIT_SECONDS); }

	public static long get_timestamp_elapsed_hours(String source_, String where_) { return get_timestamp_elapsed(source_, where_, dates.UNIT_HOURS); }

	public static String get_where_user(String source_, String where_) 
	{
		String output = get_where_internal(source_, FIELD_USER, ib.basic.get_user(), false);
		
		if (strings.is_ok(where_)) output = join_wheres(output, where_);
		
		return output;
	}

	private static String[] get_all_sources() { return _alls.DB_SOURCES; }
	
	private static boolean is_quicker_mysql(String source_)
	{
		if (_sources_quicker_mysql.size() == 0) populate_sources_quicker_mysql(null);
		
		return _sources_quicker_mysql.contains(source_);
	}
	
	private static void populate_sources_quicker_mysql(String[] sources_)
	{
		String any_source = null;

		for (String source: (arrays.is_ok(sources_) ? sources_ : get_all_sources())) 
		{
			if (!strings.are_equal(db.get_valid_type(source), db_quicker_mysql.TYPE)) continue;
		
			if (any_source == null) any_source = source;
			
			_sources_quicker_mysql.add(source);
		}

		if (any_source != null) db_quicker_mysql.update_conn_info(any_source);
	}
	
	private static void populate_sources_old() 
	{ 
		add_to_sources_old(SOURCE_EXECS, SOURCE_EXECS_OLD);
		add_to_sources_old(SOURCE_BASIC, SOURCE_BASIC_OLD);
		add_to_sources_old(SOURCE_REMOTE, SOURCE_REMOTE_OLD);
		
		add_to_sources_old(SOURCE_ORDERS, SOURCE_ORDERS_OLD);
		add_to_sources_old(SOURCE_TRADES, SOURCE_TRADES_OLD);
		add_to_sources_old(SOURCE_APPS, SOURCE_APPS_OLD);
	
		_sources_old_populated = true;
	}
	
	private static boolean update_old_quick(String source_, String source_old_)
	{	
		boolean output = true;
		
		ArrayList<HashMap<String, String>> all_vals = common.get_all_vals(source_, null, null, true);
		if (!arrays.is_ok(all_vals)) return output;

		String date = dates.get_now_string(ib.common.FORMAT_DATE);

		String col_date = common.get_col(source_old_, FIELD_DATE);
		String col_id = common.get_col(source_, db.FIELD_ID);

		for (HashMap<String, String> vals: all_vals)
		{
			HashMap<String, String> vals2 = new HashMap<String, String>(vals);
			
			if (vals2.containsKey(col_id)) vals2.remove(col_id);
			vals2.put(col_date, date);
			
			boolean temp = common.insert(source_old_, vals2, true);
			if (!temp) output = false;
		}
		
		return output;	
	}

	private static long get_timestamp_elapsed(String source_, String where_, String unit_) { return dates.get_diff(get_string(source_, accessory.db.FIELD_TIMESTAMP, where_), ib.common.FORMAT_TIMESTAMP, dates.get_now_string(ib.common.FORMAT_TIMESTAMP, 0), ib.common.FORMAT_TIMESTAMP, unit_); }

	private static double adapt_number(double val_, int max_, boolean is_negative_)
	{
		double output = numbers.round(val_, ib.common.DEFAULT_ROUND_DECIMALS);
		if (max_ <= WRONG_MAX_SIZE) return output;
		
		double max = get_max_val(max_);
		double min = -1 * max;
		
		if (is_negative_)
		{
			if (output < min) output = min;
		}
		else if (output <= 0.0) output = 0.0;
			
		if (output > max) output = max;
		
		return output;
	}
	
	private static String[] get_all_negative_numbers() { return _alls.DB_NEGATIVE_NUMBERS; }

	private static HashMap<String, Object> get_insert_vals(String source_, HashMap<String, Object> vals_)
	{
		if (source_includes_user(source_) && !vals_.containsKey(FIELD_USER)) 
		{
			String user = basic.get_user();
			if (strings.is_ok(user)) vals_.put(FIELD_USER, user);
		}
		
		return vals_;
	}
	
	private static HashMap<String, String> get_insert_vals_quick(String source_, HashMap<String, String> vals_)
	{
		String col = get_col(source_, FIELD_USER);
		
		if (source_includes_user(source_) && !vals_.containsKey(col)) 
		{
			String user = basic.get_user();
			if (strings.is_ok(user)) vals_.put(col, user);
		}
		
		return vals_;
	}

	private static int get_max_size(String field_, boolean is_number_)
	{
		HashMap<String, Integer> sizes = (is_number_ ? get_all_max_sizes_numbers() : get_all_max_sizes_strings());
		
		return (sizes.containsKey(field_) ? sizes.get(field_) : WRONG_MAX_SIZE);
	}

	private static HashMap<String, Integer> get_all_max_sizes_numbers() { return _alls.DB_MAX_SIZES_NUMBERS; }
	
	private static HashMap<String, Integer> get_all_max_sizes_strings() { return _alls.DB_MAX_SIZES_STRINGS; }

	private static HashMap<String, Double> get_all_max_vals() { return _alls.DB_MAX_VALS; }
	
	private static String[] get_all_sources_user() { return _alls.DB_SOURCES_USER; }

	private static String[] get_all_sources_enabled() { return _alls.DB_SOURCES_ENABLED; }
	
	private static String get_where_internal(String source_, String field_, String val_, boolean add_user_) 
	{
		String where = accessory.db.get_variable(get_col(source_, field_)) + "=" + accessory.db.get_value(val_);
		
		if (!strings.are_equal(field_, FIELD_USER) && add_user_ && source_includes_user(source_)) where = get_where_user(source_, where);
	
		return where;		
	}
}