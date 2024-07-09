package db_ib;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.data;
import accessory.dates;
import accessory.db;
import accessory.db_common;
import accessory.db_field;
import accessory.db_quick;
import accessory.db_where;
import accessory.generic;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib._types;
import accessory_ib.numbers;

public abstract class common 
{
	public static final String SOURCE_MARKET = _types.CONFIG_DB_IB_MARKET_SOURCE;
	public static final String SOURCE_EXECS = _types.CONFIG_DB_IB_EXECS_SOURCE;
	public static final String SOURCE_BASIC = _types.CONFIG_DB_IB_BASIC_SOURCE;
	public static final String SOURCE_REMOTE = _types.CONFIG_DB_IB_REMOTE_SOURCE;
	public static final String SOURCE_ORDERS = _types.CONFIG_DB_IB_ORDERS_SOURCE;
	public static final String SOURCE_WATCHLIST = _types.CONFIG_DB_IB_WATCHLIST_SOURCE;
	public static final String SOURCE_APPS = _types.CONFIG_DB_IB_APPS_SOURCE;
	public static final String SOURCE_SYMBOLS = _types.CONFIG_DB_IB_SYMBOLS_SOURCE;
	public static final String SOURCE_TEMP_PRICE = _types.CONFIG_DB_IB_TEMP_PRICE_SOURCE;

	public static final String SOURCE_EXECS_OLD = _types.CONFIG_DB_IB_EXECS_OLD_SOURCE;
	public static final String SOURCE_BASIC_OLD = _types.CONFIG_DB_IB_BASIC_OLD_SOURCE;
	public static final String SOURCE_REMOTE_OLD = _types.CONFIG_DB_IB_REMOTE_OLD_SOURCE;
	public static final String SOURCE_ORDERS_OLD = _types.CONFIG_DB_IB_ORDERS_OLD_SOURCE;
	public static final String SOURCE_APPS_OLD = _types.CONFIG_DB_IB_APPS_OLD_SOURCE;
	public static final String SOURCE_SYMBOLS_OLD = _types.CONFIG_DB_IB_SYMBOLS_OLD_SOURCE;
	
	public static final String FIELD_SYMBOL = _types.CONFIG_DB_IB_FIELD_SYMBOL;
	public static final String FIELD_PRICE = _types.CONFIG_DB_IB_FIELD_PRICE;
	public static final String FIELD_SIZE = _types.CONFIG_DB_IB_FIELD_SIZE;
	public static final String FIELD_TIME = _types.CONFIG_DB_IB_FIELD_TIME;
	public static final String FIELD_OPEN = _types.CONFIG_DB_IB_FIELD_OPEN;
	public static final String FIELD_CLOSE = _types.CONFIG_DB_IB_FIELD_CLOSE;
	public static final String FIELD_LOW = _types.CONFIG_DB_IB_FIELD_LOW;
	public static final String FIELD_HIGH = _types.CONFIG_DB_IB_FIELD_HIGH;
	public static final String FIELD_VOLUME = _types.CONFIG_DB_IB_FIELD_VOLUME;		
	public static final String FIELD_ASK = _types.CONFIG_DB_IB_FIELD_ASK;
	public static final String FIELD_ASK_SIZE = _types.CONFIG_DB_IB_FIELD_ASK_SIZE;
	public static final String FIELD_BID = _types.CONFIG_DB_IB_FIELD_BID;
	public static final String FIELD_BID_SIZE = _types.CONFIG_DB_IB_FIELD_BID_SIZE;
	public static final String FIELD_HALTED = _types.CONFIG_DB_IB_FIELD_HALTED;
	public static final String FIELD_HALTED_TOT = _types.CONFIG_DB_IB_FIELD_HALTED_TOT;
	public static final String FIELD_ENABLED = _types.CONFIG_DB_IB_FIELD_ENABLED;

	public static final String FIELD_USER = _types.CONFIG_DB_IB_FIELD_USER;
	public static final String FIELD_ORDER_ID = _types.CONFIG_DB_IB_FIELD_ORDER_ID;
	public static final String FIELD_QUANTITY = _types.CONFIG_DB_IB_FIELD_QUANTITY;
	public static final String FIELD_SIDE = _types.CONFIG_DB_IB_FIELD_SIDE;
	public static final String FIELD_FEES = _types.CONFIG_DB_IB_FIELD_FEES;
	public static final String FIELD_EXEC_ID = _types.CONFIG_DB_IB_FIELD_EXEC_ID;
	
	public static final String FIELD_MONEY = _types.CONFIG_DB_IB_FIELD_MONEY;
	public static final String FIELD_MONEY_INI = _types.CONFIG_DB_IB_FIELD_MONEY_INI;
	public static final String FIELD_ACCOUNT_IB = _types.CONFIG_DB_IB_FIELD_ACCOUNT_IB;
	public static final String FIELD_CURRENCY = _types.CONFIG_DB_IB_FIELD_CURRENCY;
	public static final String FIELD_MONEY_FREE = _types.CONFIG_DB_IB_FIELD_MONEY_FREE;
	
	public static final String FIELD_START = _types.CONFIG_DB_IB_FIELD_START;
	public static final String FIELD_START2 = _types.CONFIG_DB_IB_FIELD_START2;
	public static final String FIELD_STOP = _types.CONFIG_DB_IB_FIELD_STOP;
	public static final String FIELD_STOP2 = _types.CONFIG_DB_IB_FIELD_STOP2;
	public static final String FIELD_ORDER_ID_MAIN = _types.CONFIG_DB_IB_FIELD_ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = _types.CONFIG_DB_IB_FIELD_ORDER_ID_SEC;
	public static final String FIELD_STATUS = _types.CONFIG_DB_IB_FIELD_STATUS;
	public static final String FIELD_STATUS2 = _types.CONFIG_DB_IB_FIELD_STATUS2;
	public static final String FIELD_IS_MARKET = _types.CONFIG_DB_IB_FIELD_IS_MARKET;
	public static final String FIELD_PERC_MONEY = _types.CONFIG_DB_IB_FIELD_PERC_MONEY;
	public static final String FIELD_REQUEST = _types.CONFIG_DB_IB_FIELD_REQUEST;
	public static final String FIELD_TYPE_ORDER = _types.CONFIG_DB_IB_FIELD_TYPE_ORDER;
	
	public static final String FIELD_TYPE_PLACE = _types.CONFIG_DB_IB_FIELD_TYPE_PLACE;
	public static final String FIELD_TYPE_MAIN = _types.CONFIG_DB_IB_FIELD_TYPE_MAIN;
	public static final String FIELD_TYPE_SEC = _types.CONFIG_DB_IB_FIELD_TYPE_SEC;

	public static final String FIELD_TIME_ELAPSED = _types.CONFIG_DB_IB_FIELD_TIME_ELAPSED;
	public static final String FIELD_ELAPSED_INI = _types.CONFIG_DB_IB_FIELD_ELAPSED_INI;
	public static final String FIELD_UNREALISED = _types.CONFIG_DB_IB_FIELD_UNREALISED;
	public static final String FIELD_IS_ACTIVE = _types.CONFIG_DB_IB_FIELD_IS_ACTIVE;
	public static final String FIELD_INVESTMENT = _types.CONFIG_DB_IB_FIELD_INVESTMENT;
	public static final String FIELD_END = _types.CONFIG_DB_IB_FIELD_END;
	public static final String FIELD_REALISED = _types.CONFIG_DB_IB_FIELD_REALISED;

	public static final String FIELD_PRICE_INI = _types.CONFIG_DB_IB_FIELD_PRICE_INI;
	public static final String FIELD_PRICE_MIN = _types.CONFIG_DB_IB_FIELD_PRICE_MIN;
	public static final String FIELD_PRICE_MAX = _types.CONFIG_DB_IB_FIELD_PRICE_MAX;
	public static final String FIELD_VOLUME_INI = _types.CONFIG_DB_IB_FIELD_VOLUME_INI;
	public static final String FIELD_VOLUME_MIN = _types.CONFIG_DB_IB_FIELD_VOLUME_MIN;
	public static final String FIELD_VOLUME_MAX = _types.CONFIG_DB_IB_FIELD_VOLUME_MAX;

	public static final String FIELD_FLU = _types.CONFIG_DB_IB_FIELD_FLU;
	public static final String FIELD_FLU2 = _types.CONFIG_DB_IB_FIELD_FLU2;
	public static final String FIELD_FLU2_MIN = _types.CONFIG_DB_IB_FIELD_FLU2_MIN;
	public static final String FIELD_FLU2_MAX = _types.CONFIG_DB_IB_FIELD_FLU2_MAX;
	public static final String FIELD_FLU3 = _types.CONFIG_DB_IB_FIELD_FLU3;
	public static final String FIELD_FLUS_PRICE = _types.CONFIG_DB_IB_FIELD_FLUS_PRICE;
	public static final String FIELD_VAR_TOT = _types.CONFIG_DB_IB_FIELD_VAR_TOT;

	public static final String FIELD_APP = _types.CONFIG_DB_IB_FIELD_APP;
	public static final String FIELD_CONN_ID = _types.CONFIG_DB_IB_FIELD_CONN_ID;
	public static final String FIELD_CONN_TYPE = _types.CONFIG_DB_IB_FIELD_CONN_TYPE;
	public static final String FIELD_CONN_IS_ON = _types.CONFIG_DB_IB_FIELD_CONN_IS_ON;
	public static final String FIELD_ERROR = _types.CONFIG_DB_IB_FIELD_ERROR;
	public static final String FIELD_ADDITIONAL = _types.CONFIG_DB_IB_FIELD_ADDITIONAL;
	public static final String FIELD_TIME2 = _types.CONFIG_DB_IB_FIELD_TIME2;

	public static final String FIELD_ID = _types.CONFIG_DB_IB_FIELD_ID;
	public static final String FIELD_TYPE = _types.CONFIG_DB_IB_FIELD_TYPE;
	public static final String FIELD_DATA_TYPE = _types.CONFIG_DB_IB_FIELD_DATA_TYPE;

	public static final String FIELD_DATE = _types.CONFIG_DB_IB_FIELD_DATE;
	
	public static final String FIELD_KEY = _types.CONFIG_DB_IB_FIELD_KEY;
	public static final String FIELD_VALUE = _types.CONFIG_DB_IB_FIELD_VALUE;

	public static final String FIELD_NAME = _types.CONFIG_DB_IB_FIELD_NAME;
	public static final String FIELD_COUNTRY = _types.CONFIG_DB_IB_FIELD_COUNTRY;
	public static final String FIELD_EXCHANGE = _types.CONFIG_DB_IB_FIELD_EXCHANGE;
	
	public static final String SEPARATOR = accessory._types.SEPARATOR;

	public static final String BACKUP_ENDING = SEPARATOR + "last";

	public static final int MAX_SIZE_USER = 15;
	public static final int MAX_SIZE_MONEY = db_common.DEFAULT_SIZE_DECIMAL;
	public static final int MAX_SIZE_PRICE = 6;
	public static final int MAX_SIZE_VOLUME = 5;
	public static final int MAX_SIZE_APP_NAME = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_ADDITIONAL = 50;
	
	public static final int MAX_HOURS_ACTIVE = 12;
	
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final String DEFAULT_DB = _types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;

	public static final String DEFAULT_WRONG_STRING = strings.DEFAULT;
	public static final int DEFAULT_WRONG_INT = 0;
	public static final long DEFAULT_WRONG_LONG = 0l;
	public static final double DEFAULT_WRONG_DECIMAL = 0.0;
	
	private static HashMap<String, String> _sources_old = new HashMap<String, String>();
	
	private static boolean _sources_old_populated = false;
	
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

	public static String[] get_all_sources() { return _alls.DB_SOURCES; }
	
	public static String[] get_all_sources_new() { return _alls.DB_SOURCES_NEW; }
	
	public static void __truncate(String source_) 
	{
		__backup(source_);
		
		db.truncate_table(source_);
	}
	
	public static void __backup(String source_) 
	{ 
		if (db_common.is_empty(source_)) return;
		
		String backup_table = db.get_table(source_) + BACKUP_ENDING;
		
		db.__backup_table(source_, backup_table); 
	}	
	
	public static boolean contains_active(String source_) { return (!db_common.is_empty(source_) && timestamp_is_today(source_, null)); }
	
	public static boolean is_enabled(String source_, String where_) { return (!arrays.value_exists(get_all_sources_enabled(), source_) || get_boolean(source_, FIELD_ENABLED, where_)); }

	public static boolean has_enabled(String source_) { return arrays.value_exists(get_all_sources_enabled(), source_); }
	
	public static void delete_symbol(String source_, String symbol_) { db_common.delete(source_, get_where_symbol(source_, symbol_)); }
	
	public static String get_string(String source_, String field_, String where_) { return db_common.get_string(source_, field_, where_, DEFAULT_WRONG_STRING, db_common.DEFAULT_IS_FIELD, db_common.is_quick(source_)); }

	public static int get_int(String source_, String field_, String where_) { return db_common.get_int(source_, field_, where_, DEFAULT_WRONG_INT, db_common.DEFAULT_IS_FIELD, db_common.is_quick(source_)); }

	public static double get_decimal(String source_, String field_, String where_) { return db_common.get_decimal(source_, field_, where_, DEFAULT_WRONG_DECIMAL, db_common.DEFAULT_IS_FIELD, db_common.is_quick(source_)); }

	public static long get_long(String source_, String field_, String where_) { return db_common.get_long(source_, field_, where_, DEFAULT_WRONG_LONG, db_common.DEFAULT_IS_FIELD, db_common.is_quick(source_)); }
	
	public static boolean get_boolean(String source_, String field_, String where_) { return db_common.get_boolean(source_, field_, where_, db_common.DEFAULT_IS_FIELD, db_common.is_quick(source_)); }
	
	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_cols_) { return get_vals_old(source_old_, fields_cols_, db_common.DEFAULT_IS_FIELD); }
	
	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_cols_, boolean are_fields_) 
	{ 
		HashMap<String, String> output = null;
		
		String[] fields_cols = get_fields_old(source_old_, fields_cols_, are_fields_);
		
		ArrayList<HashMap<String, String>> all = db_common.get_all_vals(source_old_, fields_cols, db.DEFAULT_WHERE, are_fields_, db_common.is_quick(source_old_));
		if (!arrays.is_ok(all)) return output;
		
		String field_col_date = db_common.get_field_quick_col(source_old_, FIELD_DATE, are_fields_, db_common.is_quick(source_old_));
		
		LocalDate date = null;
		
		for (HashMap<String, String> item: all)
		{
			LocalDate temp = dates.date_from_string((String)arrays.get_value(item, field_col_date), ib.common.FORMAT_DATE, false);
			if (temp == null || (date != null && temp.isBefore(date))) continue;
			
			date = temp;		
			output = new HashMap<String, String>(item);
		}
		
		return output;
	}

	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_cols_, String date_, boolean are_fields_) 
	{ 
		String[] fields_cols = get_fields_old(source_old_, fields_cols_, are_fields_);
		
		return (arrays.is_ok(fields_cols) ? db_common.get_vals(source_old_, fields_cols, (strings.is_ok(date_) ? get_where(source_old_, FIELD_DATE, date_) : null), db.DEFAULT_ORDER, are_fields_, db_common.is_quick(source_old_)) : null); 
	}

	public static String get_type(String source_, String field_, String root_, String where_) 
	{
		String key = get_string(source_, field_, where_);
		
		return (strings.is_ok(key) ? get_type(key, root_) : strings.DEFAULT);
	}

	public static boolean insert(String source_, Object vals_) 
	{ 
		Object vals = get_insert_vals(source_, vals_);
		
		return (arrays.is_ok(vals) ? db_common.insert(source_, vals) : false);
	}

	public static boolean update_type(String source_, String field_, String type_, String root_, String where_) 
	{ 
		String type = store_type(type_, root_);
		if (!strings.is_ok(type)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, type);

		return db_common.update(source_, vals, where_);
	}

	public static boolean enable(String source_, String where_) { return enable_disable(source_, where_, true); }
	
	public static boolean enable_symbol(String source_, String symbol_) { return enable(source_, get_where_symbol(source_, symbol_)); }

	public static boolean disable(String source_, String where_) { return enable_disable(source_, where_, false); }
	
	public static boolean disable_symbol(String source_, String symbol_) { return disable(source_, get_where_symbol(source_, symbol_)); }

	public static boolean insert_update(String source_, String field_, Object val_, String where_) { return insert_update(source_, db_common.add_to_vals(source_, field_, val_, null), where_); }

	public static boolean insert_update(String source_, Object vals_, String where_) { return db_common.insert_update(source_, get_insert_vals(source_, vals_), where_, db.FIELD_ID); }

	public static boolean update_all_old_quick()
	{
		boolean is_ok = true;
		
		if (!_sources_old_populated) populate_sources_old();
		
		for (Entry<String, String> item: _sources_old.entrySet()) 
		{
			String source = item.getKey();
			String source_old = item.getValue();
			
			String message = db.get_table(source_old);
			
			if (!db_common.exists(source_old, get_where(source_old, FIELD_DATE, dates.get_now_string(ib.common.FORMAT_DATE, dates.DEFAULT_OFFSET))))
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
		
	public static boolean source_includes_user(String source_) { return arrays.value_exists(get_all_sources_user(), source_); }

	public static String[] populate_all_sources_user() { return new String[] { SOURCE_BASIC, SOURCE_APPS }; }
	
	public static String[] populate_all_sources() 
	{ 
		return (String[])arrays.add
		(
			populate_all_sources_new(), new String[] { SOURCE_EXECS_OLD, SOURCE_BASIC_OLD, SOURCE_REMOTE_OLD, SOURCE_ORDERS_OLD, SOURCE_APPS_OLD, SOURCE_SYMBOLS_OLD }
		);
	}
	
	public static String[] populate_all_sources_new() { return new String[] { SOURCE_MARKET, SOURCE_EXECS, SOURCE_BASIC, SOURCE_REMOTE, SOURCE_ORDERS, SOURCE_WATCHLIST, SOURCE_APPS, SOURCE_TEMP_PRICE }; }
	
	public static String[] populate_all_sources_enabled() { return new String[] { SOURCE_MARKET }; }

	public static String[] populate_all_sources_elapsed() { return new String[] { SOURCE_WATCHLIST }; }

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
		
		for (Entry<String, Integer> item: populate_all_max_sizes_numbers().entrySet()) { vals.put(item.getKey(), db_common.get_max_val(item.getValue())); }
		
		return vals;
	}
	
	public static HashMap<String, Integer> populate_all_max_sizes_strings()
	{
		HashMap<String, Integer> all = new HashMap<String, Integer>();

		all.put(FIELD_USER, MAX_SIZE_USER);
		all.put(FIELD_ERROR, db_common.MAX_SIZE_ERROR);
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
		
		int decimals = accessory.numbers.get_round_decimals();
		
		accessory.numbers.update_round_decimals(numbers.get_price_round_decimals(val_));
		
		double output = (max <= db_common.WRONG_MAX_SIZE ? val_ : db_common.adapt_number(val_, max, arrays.key_exists(get_all_negative_numbers(), field_)));
	
		accessory.numbers.update_round_decimals(decimals);
		
		return output;
	}
	
	public static double get_max_val(String field_) 
	{
		HashMap<String, Double> vals = get_all_max_vals();
		
		return ((strings.is_ok(field_) && vals.containsKey(field_)) ? vals.get(field_) : WRONG_MAX_VAL);
	}

	public static String adapt_string(String val_, String field_)
	{
		int max = get_max_size_string(field_);
		
		return (max <= db_common.WRONG_MAX_SIZE ? val_ : db_common.adapt_string(db.sanitise_string(val_), max));
	}

	public static String store_type(String type_, String root_) { return ib.common.get_key(type_, root_); }

	public static String get_type(String key_, String root_) { return ib.common.get_type(key_, root_); }
	
	public static String adapt_user(String val_) { return adapt_string(val_, FIELD_USER); }

	public static boolean field_exists(String source_, String field_, Object vals_) { return field_exists(source_, field_, vals_, db_common.DEFAULT_IS_FIELD); }

	public static boolean field_exists(String source_, String field_col_, Object vals_, boolean is_field_) { return (db_common.get_from_vals(source_, field_col_, vals_, is_field_, db_common.is_quick(source_)) != null); }

	public static boolean timestamp_is_today(String source_, String where_) { return timestamp_is_today(source_, where_, true); }

	public static boolean timestamp_is_today(String source_, String where_, boolean is_today_) 
	{ 
		String timestamp = get_string(source_, db.FIELD_TIMESTAMP, where_);
		if (db.WRONG_STRING.equals(timestamp)) return false;
		
		boolean is_today = dates.is_today(timestamp, ib.common.FORMAT_TIMESTAMP);
		
		return (is_today_ ? is_today : !is_today); 
	}

	public static String get_where_timestamp_today(String source_, String where_) { return get_where_timestamp_today(source_, where_, true); }
	
	public static String get_where_timestamp_today(String source_, String where_, boolean is_today_) 
	{ 
		String where = "(DATE(" + db.get_variable(source_, db.get_col(source_, db.FIELD_TIMESTAMP)) + ") " + (is_today_ ? "=" : "!=") + " CURDATE())"; 
	
		if (strings.is_ok(where_)) where = db_common.join_wheres(where_, where);
	
		return where;
	}
	
	public static String get_where_timestamp(String source_, int before_mins_) { return (before_mins_ > 0 ? (new db_where(source_, db.FIELD_TIMESTAMP, db_where.OPERAND_GREATER_EQUAL, "CURRENT_TIMESTAMP - INTERVAL " + before_mins_ + " MINUTE", false, db_where.DEFAULT_LINK)).toString() : strings.DEFAULT); }

	public static long get_timestamp_elapsed_secs(String source_, String where_) { return get_timestamp_elapsed(source_, where_, dates.UNIT_SECONDS); }

	public static long get_timestamp_elapsed_hours(String source_, String where_) { return get_timestamp_elapsed(source_, where_, dates.UNIT_HOURS); }

	public static String get_where_user(String source_, String where_) 
	{
		String output = get_where_internal(source_, FIELD_USER, ib.basic.get_user(), false);
		
		if (strings.is_ok(where_)) output = db_common.join_wheres(output, where_);
		
		return output;
	}
	public static String get_where_user(String source_) { return get_where(source_, FIELD_USER, ib.basic.get_user()); }

	public static String get_where_symbol(String source_, String symbol_) { return get_where(source_, FIELD_SYMBOL, symbol_); }

	public static String get_where_order_id(String source_, int order_id_main_) { return get_where_order_id(source_, order_id_main_, true); }
	
	public static String get_where_order_id(String source_, int order_id_, boolean is_main_) { return get_where(source_, (is_main_ ? FIELD_ORDER_ID_MAIN : FIELD_ORDER_ID_SEC), Integer.toString(order_id_)); }

	public static String get_where_order_id(String source_, Integer[] ids_, boolean equal_) 
	{ 
		ArrayList<db_where> wheres = new ArrayList<db_where>();
		
		if (source_includes_user(source_)) wheres.add(new db_where(source_, FIELD_USER, db_where.OPERAND_EQUAL, ib.basic.get_user(), db_where.LINK_AND));
		
		for (int id: ids_) { wheres.add(new db_where(source_, FIELD_ORDER_ID_MAIN, (equal_ ? db_where.OPERAND_EQUAL : db_where.OPERAND_NOT_EQUAL), id, db_where.LINK_AND)); }
		
		return db_where.to_string(wheres); 
	}
	
	public static String get_where(String source_, String field_, String val_) { return get_where(source_, field_, val_, true); }

	public static String get_where(String source_, String field_, String val_, boolean add_user_) { return get_where_internal(source_, field_, val_, add_user_); }

	public static db_field get_field_symbol(boolean is_unique_) { return db_common.get_field_string(symbols.MAX_SIZE_SYMBOLS, is_unique_); }
	
	public static db_field get_field_order_id(boolean is_unique_) { return (is_unique_ ? new db_field(data.INT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, ib.common.WRONG_ORDER_ID, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.INT)); }
	
	public static db_field get_field_money() { return db_common.get_field_decimal(common.MAX_SIZE_MONEY); }
	
	public static db_field get_field_quantity() { return db_common.get_field_decimal(); }
	
	public static db_field get_field_size_volume() { return db_common.get_field_decimal(MAX_SIZE_VOLUME); }
	
	public static db_field get_field_price() { return new db_field(data.DECIMAL, MAX_SIZE_PRICE, numbers.MAX_ROUND_DECIMALS_PRICES); }
	
	public static db_field get_field_halted() { return db_common.get_field_tiny(); }
	
	public static db_field get_field_halted_tot() { return db_common.get_field_tiny(); }
	
	public static db_field get_field_time() { return db_common.get_field_time(ib.common.FORMAT_TIME); }
	
	public static db_field get_field_time2() { return db_common.get_field_time(ib.common.FORMAT_TIME2); }

	public static db_field get_field_time_elapsed() { return db_common.get_field_time(ib.common.FORMAT_TIME_ELAPSED); }
	
	public static db_field get_field_elapsed_ini() { return new db_field(data.LONG); }
	
	public static db_field get_field_user() { return get_field_user(false); }
	
	public static db_field get_field_user(boolean is_unique_) { return db_common.get_field_string(common.MAX_SIZE_USER, is_unique_); }
	
	public static db_field get_field_status_type() { return get_field_status_type(null); }
	
	public static db_field get_field_status_type(String default_) { return db_common.get_field_string(db_common.DEFAULT_SIZE_STRING, false, default_, true); }
	
	public static db_field get_field_app(boolean is_unique_) { return get_field_name(db_ib.common.MAX_SIZE_APP_NAME, is_unique_); }
	
	public static db_field get_field_name(int size_, boolean is_unique_) { return db_common.get_field_string(size_, is_unique_); }

	public static db_field get_field_request() { return db_common.get_field_int(true); }	
	
	public static db_field get_field_date() { return db_common.get_field_date(ib.common.FORMAT_DATE); }

	private static void populate_sources_old() 
	{ 
		add_to_sources_old(SOURCE_EXECS, SOURCE_EXECS_OLD);
		add_to_sources_old(SOURCE_BASIC, SOURCE_BASIC_OLD);
		add_to_sources_old(SOURCE_REMOTE, SOURCE_REMOTE_OLD);
		
		add_to_sources_old(SOURCE_ORDERS, SOURCE_ORDERS_OLD);
		add_to_sources_old(SOURCE_APPS, SOURCE_APPS_OLD);
	
		_sources_old_populated = true;
	}
	
	private static boolean update_old_quick(String source_, String source_old_)
	{	
		boolean output = true;
		
		ArrayList<HashMap<String, String>> all_vals = db_common.get_all_vals(source_, null, null, true, true);
		if (!arrays.is_ok(all_vals)) return output;

		String date = dates.get_now_string(ib.common.FORMAT_DATE, dates.DEFAULT_OFFSET);

		String col_date = db_quick.get_col(source_old_, FIELD_DATE);
		String col_id = db_quick.get_col(source_, db.FIELD_ID);

		for (HashMap<String, String> vals: all_vals)
		{
			HashMap<String, String> vals2 = new HashMap<String, String>();
			
			for (Entry<String, String> val: vals.entrySet()) { vals2.put(val.getKey(), db.adapt_input(val.getValue())); }
			
			if (vals2.containsKey(col_id)) vals2.remove(col_id);
			vals2.put(col_date, date);
			
			boolean temp = insert(source_old_, vals2);
			if (!temp) output = false;
		}
		
		return output;	
	}

	private static String[] get_fields_old(String source_, String[] fields_cols_, boolean are_fields_)
	{
		String field_col_date = FIELD_DATE;
		if (!are_fields_) field_col_date = db_common.get_field_quick_col(source_, FIELD_DATE, true, db_common.is_quick(source_));
		
		ArrayList<String> temp = arrays.to_arraylist(fields_cols_);
		if (!temp.contains(field_col_date)) temp.add(field_col_date);
		
		return arrays.to_array(temp);
	}

	private static long get_timestamp_elapsed(String source_, String where_, String unit_) { return dates.get_diff(get_string(source_, db.FIELD_TIMESTAMP, where_), ib.common.FORMAT_TIMESTAMP, dates.get_now_string(ib.common.FORMAT_TIMESTAMP, 0), ib.common.FORMAT_TIMESTAMP, unit_); }
	
	private static boolean enable_disable(String source_, String where_, boolean enable_) { return db_common.update(source_, FIELD_ENABLED, db.adapt_input(enable_), where_); }

	private static String[] get_all_negative_numbers() { return _alls.DB_NEGATIVE_NUMBERS; }

	private static Object get_insert_vals(String source_, Object vals_)
	{
		Object vals = arrays.get_new(vals_);

		if (source_includes_user(source_) && !common.field_exists(source_, FIELD_USER, vals)) 
		{
			String user = ib.basic.get_user();
			if (strings.is_ok(user)) vals = db_common.add_to_vals(source_, FIELD_USER, user, vals_);
		}
		
		return vals;
	}

	private static int get_max_size(String field_, boolean is_number_)
	{
		HashMap<String, Integer> sizes = (is_number_ ? get_all_max_sizes_numbers() : get_all_max_sizes_strings());
		
		return (sizes.containsKey(field_) ? sizes.get(field_) : db_common.WRONG_MAX_SIZE);
	}

	private static HashMap<String, Integer> get_all_max_sizes_numbers() { return _alls.DB_MAX_SIZES_NUMBERS; }
	
	private static HashMap<String, Integer> get_all_max_sizes_strings() { return _alls.DB_MAX_SIZES_STRINGS; }

	private static HashMap<String, Double> get_all_max_vals() { return _alls.DB_MAX_VALS; }
	
	private static String[] get_all_sources_user() { return _alls.DB_SOURCES_USER; }
	
	private static String[] get_all_sources_enabled() { return _alls.DB_SOURCES_ENABLED; }
	
	private static String get_where_internal(String source_, String field_, String val_, boolean add_user_) 
	{
		String where = db_common.get_where(source_, field_, val_);
		
		if (!strings.are_equal(field_, FIELD_USER) && add_user_ && source_includes_user(source_)) where = get_where_user(source_, where);
	
		return where;		
	}
}