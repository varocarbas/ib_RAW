package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.dates;
import accessory.db;
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
	public static final String SOURCE_APPS = types.CONFIG_DB_IB_APPS_SOURCE;
	
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
	public static final String FIELD_INVEST_PERC = types.CONFIG_DB_IB_FIELD_INVEST_PERC;
	public static final String FIELD_REQUEST = types.CONFIG_DB_IB_FIELD_REQUEST;
	
	public static final String FIELD_TYPE_PLACE = types.CONFIG_DB_IB_FIELD_TYPE_PLACE;
	public static final String FIELD_TYPE_MAIN = types.CONFIG_DB_IB_FIELD_TYPE_MAIN;
	public static final String FIELD_TYPE_SEC = types.CONFIG_DB_IB_FIELD_TYPE_SEC;

	public static final String FIELD_TIME_ELAPSED = types.CONFIG_DB_IB_FIELD_TIME_ELAPSED;
	public static final String FIELD_ELAPSED_INI = types.CONFIG_DB_IB_FIELD_ELAPSED_INI;
	public static final String FIELD_UNREALISED = types.CONFIG_DB_IB_FIELD_UNREALISED;
	public static final String FIELD_IS_ACTIVE = types.CONFIG_DB_IB_FIELD_IS_ACTIVE;
	public static final String FIELD_POSITION = types.CONFIG_DB_IB_FIELD_POSITION;
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
	
	public static final String FIELD_APP = types.CONFIG_DB_IB_FIELD_APP;
	public static final String FIELD_CONN_ID = types.CONFIG_DB_IB_FIELD_CONN_ID;
	public static final String FIELD_CONN_TYPE = types.CONFIG_DB_IB_FIELD_CONN_TYPE;
	public static final String FIELD_COUNT = types.CONFIG_DB_IB_FIELD_COUNT;
	public static final String FIELD_ERROR = types.CONFIG_DB_IB_FIELD_ERROR;
	public static final String FIELD_ADDITIONAL = types.CONFIG_DB_IB_FIELD_ADDITIONAL;
	
	public static final String SEPARATOR = accessory.types.SEPARATOR;
	
	public static final int MAX_SIZE_USER = 15;
	public static final int MAX_SIZE_MONEY = 7;
	public static final int MAX_SIZE_PRICE = 4;
	public static final int MAX_SIZE_VOLUME = 4;
	public static final int MAX_SIZE_POSITION = 3;
	public static final int MAX_SIZE_ERROR = 30;
	public static final int MAX_SIZE_APP_NAME = 30;
	public static final int MAX_SIZE_ADDITIONAL = 50;
	
	public static final String FORMAT_TIME_MAIN = dates.FORMAT_TIME_SHORT;
	public static final String FORMAT_TIME_ELAPSED = dates.FORMAT_TIME_FULL;
	
	public static final int WRONG_MAX_SIZE = 0;
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final String DEFAULT_DB = types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;
	public static final String DEFAULT_FORMAT_TIME = FORMAT_TIME_MAIN;
	
	public static final int DEFAULT_SIZE_DECIMAL = MAX_SIZE_MONEY;
	
	public static boolean exists(String source_, String where_) { return exists(source_, FIELD_SYMBOL, where_); }

	public static boolean exists(String source_, String field_, String where_) { return strings.is_ok(accessory.db.select_one_string(source_, field_, where_, db.DEFAULT_ORDER)); }
	
	public static boolean is_enabled(String source_, String where_) { return (!arrays.value_exists(get_all_sources_enabled(), source_) || accessory.db.select_one_boolean(source_, FIELD_ENABLED, where_, db.DEFAULT_ORDER)); }

	public static String get_string(String source_, String field_, String where_) { return accessory.db.select_one_string(source_, field_, where_, db.DEFAULT_ORDER); }

	public static int get_int(String source_, String field_, String where_) { return get_int(source_, field_, where_, false); }

	public static int get_int(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? accessory.db.select_one_int_quick(source_, col_field_, where_, db.DEFAULT_ORDER) : accessory.db.select_one_int(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static double get_decimal(String source_, String field_, String where_) { return get_decimal(source_, field_, where_, false); }

	public static double get_decimal(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? accessory.db.select_one_decimal_quick(source_, col_field_, where_, db.DEFAULT_ORDER) : accessory.db.select_one_decimal(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static long get_long(String source_, String field_, String where_) { return get_long(source_, field_, where_, false); }

	public static long get_long(String source_, String col_field_, String where_, boolean is_quick_) { return (is_quick_ ? accessory.db.select_one_long_quick(source_, col_field_, where_, db.DEFAULT_ORDER) : accessory.db.select_one_long(source_, col_field_, where_, db.DEFAULT_ORDER)); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_, String where_) { return accessory.db.select_some_decimals(source_, field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER); }

	public static ArrayList<String> get_all_strings(String source_, String field_, String where_) { return accessory.db.select_some_strings(source_, field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_, String where_) { return accessory.db.select_some_ints(source_, field_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_, String where_) { return accessory.db.select(source_, fields_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER); }

	public static HashMap<String, String> get_vals(String source_, String where_) { return get_vals(source_, null, where_); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_) { return accessory.db.select_one(source_, fields_, where_, db.DEFAULT_ORDER); }

	public static HashMap<String, String> get_vals_quick(String source_, String[] cols_, String where_) { return accessory.db.select_one_quick(source_, cols_, where_, db.DEFAULT_ORDER); }

	public static String get_type(String source_, String field_, String root_, String where_) 
	{
		String key = get_string(source_, field_, where_);
		
		return (strings.is_ok(key) ? common.get_type_from_key(key, root_) : strings.DEFAULT);
	}
	
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
		
		accessory.db.insert_quick(source_, vals);

		return accessory.db.is_ok(source_);
	}
	
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
		
		return update(source_, vals, where_); 
	}

	public static boolean update_quick(String source_, HashMap<String, String> vals_, String where_)
	{		
		if (!arrays.is_ok(vals_)) return false;
		
		accessory.db.update_quick(source_, vals_, where_);
		
		return accessory.db.is_ok(source_);
	}

	public static boolean update_type(String source_, String field_, String type_, String root_, String where_) 
	{ 
		String type = get_key_from_type(type_, root_);
		if (!strings.is_ok(type)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, type);

		return common.update(source_, vals, where_);
	}
	
	public static boolean insert_update(String source_, String field_, Object val_, String where_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);

		return insert_update(source_, vals, where_);
	}
	
	public static boolean insert_update(String source_, HashMap<String, Object> vals_, String where_)
	{	
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		if (!arrays.is_ok(vals)) return false;
		
		vals = get_insert_vals(source_, vals);

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

	public static String get_where_symbol(String source_, String symbol_, boolean quick_) { return (quick_ ? get_where_symbol_quick(source_, symbol_) : get_where_symbol(source_, symbol_)); }

	public static String get_where_symbol(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, false); }

	public static String get_where_symbol_quick(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_, true); }
	
	public static String get_where(String source_, String field_, String val_, boolean is_quick_) { return get_where_internal(source_, field_, val_, is_quick_, true); }

	public static String get_where_order_id(String source_, int order_id_main_) { return get_where_order_id(source_, order_id_main_, true); }
	
	public static String get_where_order_id(String source_, int order_id_, boolean is_main_) { return common.get_where(source_, (is_main_ ? FIELD_ORDER_ID_MAIN : FIELD_ORDER_ID_SEC), Integer.toString(order_id_), false); }

	public static String get_where_order_id(String source_, Integer[] ids_, boolean equal_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		if (source_includes_user(source_)) wheres.add(new db_where(source_, FIELD_USER, db_where.OPERAND_EQUAL, ib.basic.get_user(), db_where.LINK_AND));
		
		for (int id: ids_) { wheres.add(new db_where(source_, FIELD_ORDER_ID_MAIN, (equal_ ? db_where.OPERAND_EQUAL : db_where.OPERAND_NOT_EQUAL), id, db_where.LINK_AND)); }
		
		return db_where.to_string(wheres); 
	}
	
	public static boolean source_includes_user(String source_) { return arrays.value_exists(get_all_sources_user(), source_); }

	public static String[] populate_all_sources_user() { return new String[] { SOURCE_BASIC, SOURCE_EXECS, SOURCE_ORDERS, SOURCE_REMOTE, SOURCE_TRADES, SOURCE_APPS }; }

	public static String[] populate_all_sources_enabled() { return new String[] { SOURCE_MARKET }; }

	public static String[] populate_all_sources_elapsed() { return new String[] { SOURCE_TRADES, SOURCE_WATCHLIST }; }

	public static String[] get_all_sources_elapsed() { return _alls.DB_SOURCES_ELAPSED; }

	public static HashMap<String, Integer> populate_all_max_sizes_numbers()
	{
		HashMap<String, Integer> all = new HashMap<String, Integer>();

		all.put(FIELD_MONEY, MAX_SIZE_MONEY);
		all.put(FIELD_PRICE, MAX_SIZE_PRICE);
		all.put(FIELD_VOLUME, MAX_SIZE_VOLUME);
		all.put(FIELD_POSITION, MAX_SIZE_POSITION);
		
		return all;
	}
	
	public static HashMap<String, Double> populate_all_max_vals()
	{
		HashMap<String, Double> vals = new HashMap<String, Double>();
		
		for (Entry<String, Integer> item: populate_all_max_sizes_numbers().entrySet()) { vals.put(item.getKey(), common.get_max_val(item.getValue())); }
		
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

	public static double adapt_position(double position_) { return adapt_number(position_, FIELD_POSITION); }

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
	
	public static String get_key(String type_, String root_) { return accessory._keys.get_key(type_, root_); }
	
	public static HashMap<String, String> populate_cols(String source_, String[] fields_)
	{
		HashMap<String, String> output = new HashMap<String, String>();
		if (!arrays.is_ok(fields_)) return output;
		
		for (String field: fields_) { output.put(field, get_col(source_, field)); }
		
		return output;
	}
	
	public static String adapt_user(String val_) { return common.adapt_string(val_, FIELD_USER); }
		
	private static double adapt_number(double val_, int max_, boolean is_negative_)
	{
		double output = val_;
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

	private static String get_where_internal(String source_, String field_, String val_, boolean is_quick_, boolean check_user_) 
	{
		String where = null;
		
		if (is_quick_) where = accessory.db.get_variable(get_col(source_, field_)) + "=" + accessory.db.get_value(val_);
		else where = (new db_where(source_, field_, val_)).toString();
		
		if (!strings.are_equal(field_, FIELD_USER) && check_user_ && source_includes_user(source_)) where = db_where.join(where, get_where_internal(source_, FIELD_USER, ib.basic.get_user(), is_quick_, false), db_where.LINK_AND);
		
		return where;		
	}
}