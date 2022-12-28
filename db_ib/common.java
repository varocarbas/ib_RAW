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
import accessory_ib.types;
import external_ib.contracts;

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
	public static final String FIELD_FLUS_PRICE = types.CONFIG_DB_IB_FIELD_FLUS_PRICE;
	public static final String FIELD_VAR_TOT = types.CONFIG_DB_IB_FIELD_VAR_TOT;
	
	public static final String FIELD_APP = types.CONFIG_DB_IB_FIELD_APP;
	public static final String FIELD_CONN_ID = types.CONFIG_DB_IB_FIELD_CONN_ID;
	public static final String FIELD_CONN_TYPE = types.CONFIG_DB_IB_FIELD_CONN_TYPE;
	public static final String FIELD_CONN_IS_ON = types.CONFIG_DB_IB_FIELD_CONN_IS_ON;
	public static final String FIELD_ERROR = types.CONFIG_DB_IB_FIELD_ERROR;
	public static final String FIELD_ADDITIONAL = types.CONFIG_DB_IB_FIELD_ADDITIONAL;
	public static final String FIELD_TIME2 = types.CONFIG_DB_IB_FIELD_TIME2;

	public static final String FIELD_ID = types.CONFIG_DB_IB_FIELD_ID;
	public static final String FIELD_TYPE = types.CONFIG_DB_IB_FIELD_TYPE;
	public static final String FIELD_DATA_TYPE = types.CONFIG_DB_IB_FIELD_DATA_TYPE;

	public static final String FIELD_DATE = types.CONFIG_DB_IB_FIELD_DATE;
	
	public static final String FIELD_KEY = types.CONFIG_DB_IB_FIELD_KEY;
	public static final String FIELD_VALUE = types.CONFIG_DB_IB_FIELD_VALUE;

	public static final String SEPARATOR = accessory.types.SEPARATOR;

	public static final String BACKUP_ENDING = SEPARATOR + "last";

	public static final int MAX_SIZE_USER = 15;
	public static final int MAX_SIZE_MONEY = db_common.DEFAULT_SIZE_DECIMAL;
	public static final int MAX_SIZE_PRICE = 4;
	public static final int MAX_SIZE_VOLUME = 5;
	public static final int MAX_SIZE_APP_NAME = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_ADDITIONAL = 50;
	
	public static final int MAX_HOURS_ACTIVE = 12;
	
	public static final double WRONG_MAX_VAL = 0.0;
	
	public static final String DEFAULT_DB = types.CONFIG_DB_IB;
	public static final String DEFAULT_DB_NAME = accessory.db.DEFAULT_DB_NAME;
	public static final boolean DEFAULT_IS_FIELD = true;
	public static final String DEFAULT_WRONG_STRING = strings.DEFAULT;
	public static final int DEFAULT_WRONG_INT = 0;
	public static final long DEFAULT_WRONG_LONG = 0l;
	public static final double DEFAULT_WRONG_DECIMAL = 0.0;
	
	private static HashMap<String, String> _sources_old = new HashMap<String, String>();
	
	private static boolean _sources_old_populated = false;
	private static boolean _sources_quicker_populated = false;
	
	public static db_field get_field_symbol(boolean is_unique_) { return db_common.get_field_string(contracts.MAX_LENGTH_SYMBOL_US_ANY, is_unique_); }
	
	public static db_field get_field_order_id(boolean is_unique_) { return (is_unique_ ? new db_field(data.INT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, ib.common.WRONG_ORDER_ID, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.INT)); }
	
	public static db_field get_field_money() { return db_common.get_field_decimal(common.MAX_SIZE_MONEY); }
	
	public static db_field get_field_quantity() { return db_common.get_field_decimal(); }
	
	public static db_field get_field_size_volume() { return db_common.get_field_decimal(common.MAX_SIZE_VOLUME); }
	
	public static db_field get_field_price() { return new db_field(data.DECIMAL, common.MAX_SIZE_PRICE, 2); }
	
	public static db_field get_field_halted() { return db_common.get_field_tiny(); }
	
	public static db_field get_field_halted_tot() { return db_common.get_field_tiny(); }
	
	public static db_field get_field_time() { return db_common.get_field_time(ib.common.FORMAT_TIME); }
	
	public static db_field get_field_time2() { return db_common.get_field_time(ib.common.FORMAT_TIME2); }

	public static db_field get_field_time_elapsed() { return db_common.get_field_time(ib.common.FORMAT_TIME_ELAPSED); }
	
	public static db_field get_field_elapsed_ini() { return new db_field(data.LONG); }
	
	public static db_field get_field_user() { return get_field_user(false); }
	
	public static db_field get_field_user(boolean is_unique_) { return db_common.get_field_string(common.MAX_SIZE_USER, is_unique_); }
	
	public static db_field get_field_status_type() { return get_field_status_type(null); }
	
	public static db_field get_field_status_type(String default_) { return db_common.get_field_string(db_common.DEFAULT_SIZE_STRING, false, default_); }
	
	public static db_field get_field_app(boolean is_unique_) { return get_field_name(db_ib.common.MAX_SIZE_APP_NAME, is_unique_); }
	
	public static db_field get_field_name(int size_, boolean is_unique_) { return db_common.get_field_string(size_, is_unique_); }

	public static db_field get_field_request() { return db_common.get_field_int(true); }	
	
	public static db_field get_field_date() { return db_common.get_field_date(ib.common.FORMAT_DATE); }
	
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

	public static Object get_input(String source_, Object input_) { return (is_quick(source_) ? db.adapt_input(input_) : input_); }

	public static boolean is_quick(String source_) 
	{
		boolean output = db_common.DEFAULT_IS_QUICK;
		
		if (source_.equals(SOURCE_MARKET)) output = market._is_quick;
		else if (source_.equals(SOURCE_EXECS)) output = execs._is_quick;
		else if (source_.equals(SOURCE_BASIC)) output = basic._is_quick;
		else if (source_.equals(SOURCE_REMOTE)) output = remote._is_quick;
		else if (source_.equals(SOURCE_ORDERS)) output = orders._is_quick;
		else if (source_.equals(SOURCE_TRADES)) output = trades._is_quick;
		else if (source_.equals(SOURCE_WATCHLIST)) output = watchlist._is_quick;
		else if (source_.equals(SOURCE_APPS)) output = apps._is_quick;
		
		if (!_sources_quicker_populated)
		{
			db_quick.add_sources_quicker(get_all_sources()); 

			_sources_quicker_populated = true;
		}
		
		return output; 
	}
	
	public static void is_quick(String source_, boolean is_quick_) 
	{ 
		if (source_.equals(SOURCE_MARKET)) market._is_quick = is_quick_;
		else if (source_.equals(SOURCE_EXECS)) execs._is_quick = is_quick_;
		else if (source_.equals(SOURCE_BASIC)) basic._is_quick = is_quick_;
		else if (source_.equals(SOURCE_REMOTE)) remote._is_quick = is_quick_;
		else if (source_.equals(SOURCE_ORDERS)) orders._is_quick = is_quick_;
		else if (source_.equals(SOURCE_TRADES)) trades._is_quick = is_quick_;
		else if (source_.equals(SOURCE_WATCHLIST)) watchlist._is_quick = is_quick_;
		else if (source_.equals(SOURCE_APPS)) apps._is_quick = is_quick_;
	}
	
	public static String get_field_col(String source_, String field_) { return get_field_col(source_, field_, DEFAULT_IS_FIELD); }
	
	public static String get_field_col(String source_, String field_col_, boolean is_field_) { return ((is_field_ && is_quick(source_)) ? get_col(source_, field_col_) : field_col_); }

	public static String get_col(String source_, String field_) 
	{ 
		HashMap<String, String> fields_cols = get_fields_cols(source_);
		
		return ((fields_cols != null && fields_cols.containsKey(field_)) ? fields_cols.get(field_) : db_common.get_col(source_, field_)); 
	}

	public static String[] get_fields(String source_) 
	{
		if (source_.equals(SOURCE_MARKET))
		{
			if (market._fields == null) market.populate_fields();
			
			return market._fields;
		}
		else if (source_.equals(SOURCE_EXECS)) 
		{
			if (execs._fields == null) execs.populate_fields();
			
			return execs._fields;
		}
		else if (source_.equals(SOURCE_BASIC))
		{
			if (basic._fields == null) basic.populate_fields();
			
			return basic._fields;
		}
		else if (source_.equals(SOURCE_REMOTE))
		{
			if (remote._fields == null) remote.populate_fields();
			
			return remote._fields;
		}
		else if (source_.equals(SOURCE_ORDERS))
		{
			if (orders._fields == null) orders.populate_fields();
			
			return orders._fields;
		}
		else if (source_.equals(SOURCE_TRADES))
		{
			if (trades._fields == null) trades.populate_fields();
			
			return trades._fields;
		}
		else if (source_.equals(SOURCE_WATCHLIST))
		{
			if (watchlist._fields == null) watchlist.populate_fields();
			
			return watchlist._fields;
		}
		else if (source_.equals(SOURCE_APPS))
		{
			if (apps._fields == null) apps.populate_fields();
			
			return apps._fields;
		}
		
		return null;
	}

	public static String[] get_cols(String source_) 
	{ 
		if (source_.equals(SOURCE_MARKET))
		{
			if (market._cols == null) market._cols = get_cols_internal(source_);
			
			return market._cols;
		}
		else if (source_.equals(SOURCE_EXECS)) 
		{
			if (execs._cols == null) execs._cols = get_cols_internal(source_);
			
			return execs._cols;
		}
		else if (source_.equals(SOURCE_BASIC))
		{
			if (basic._cols == null) basic._cols = get_cols_internal(source_);
			
			return basic._cols;
		}
		else if (source_.equals(SOURCE_REMOTE))
		{
			if (remote._cols == null) remote._cols = get_cols_internal(source_);
			
			return remote._cols;
		}
		else if (source_.equals(SOURCE_ORDERS))
		{
			if (orders._cols == null) orders._cols = get_cols_internal(source_);
			
			return orders._cols;
		}
		else if (source_.equals(SOURCE_TRADES))
		{
			if (trades._cols == null) trades._cols = get_cols_internal(source_);
			
			return trades._cols;
		}
		else if (source_.equals(SOURCE_WATCHLIST))
		{
			if (watchlist._cols == null) watchlist._cols = get_cols_internal(source_);
			
			return watchlist._cols;
		}
		else if (source_.equals(SOURCE_APPS))
		{
			if (apps._cols == null) apps._cols = get_cols_internal(source_);
			
			return apps._cols;
		}
		
		return null;
	}

	public static String[] get_cols(String source_, String[] fields_) 
	{ 
		if (!arrays.is_ok(fields_)) return null;
		
		HashMap<String, String> fields_cols = arrays.get_new_hashmap_xx(get_fields_cols(source_));
		if (!arrays.is_ok(fields_cols)) return null;
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for (String field: fields_) 
		{ 
			if (fields_cols.containsKey(field)) temp.add(fields_cols.get(field));
		}
		
		return arrays.to_array(temp);
	}

	public static HashMap<String, String> get_fields_cols(String source_) 
	{ 
		if (source_.equals(SOURCE_MARKET))
		{
			if (market._fields_cols == null) market._fields_cols = get_fields_cols_internal(source_);
			
			return market._fields_cols;
		}
		else if (source_.equals(SOURCE_EXECS)) 
		{
			if (execs._fields_cols == null) execs._fields_cols = get_fields_cols_internal(source_);
			
			return execs._fields_cols;
		}
		else if (source_.equals(SOURCE_BASIC))
		{
			if (basic._fields_cols == null) basic._fields_cols = get_fields_cols_internal(source_);
			
			return basic._fields_cols;
		}
		else if (source_.equals(SOURCE_REMOTE))
		{
			if (remote._fields_cols == null) remote._fields_cols = get_fields_cols_internal(source_);
			
			return remote._fields_cols;
		}
		else if (source_.equals(SOURCE_ORDERS))
		{
			if (orders._fields_cols == null) orders._fields_cols = get_fields_cols_internal(source_);
			
			return orders._fields_cols;
		}
		else if (source_.equals(SOURCE_TRADES))
		{
			if (trades._fields_cols == null) trades._fields_cols = get_fields_cols_internal(source_);
			
			return trades._fields_cols;
		}
		else if (source_.equals(SOURCE_WATCHLIST))
		{
			if (watchlist._fields_cols == null) watchlist._fields_cols = get_fields_cols_internal(source_);
			
			return watchlist._fields_cols;
		}
		else if (source_.equals(SOURCE_APPS))
		{
			if (apps._fields_cols == null) apps._fields_cols = get_fields_cols_internal(source_);
			
			return apps._fields_cols;
		}
		
		return null;
	} 

	public static boolean vals_are_ok(String source_, Object vals_) { return (arrays.is_ok(vals_) && (is_quick(source_) ? db_quick.input_is_ok(vals_) : db.input_is_ok(vals_))); }
	
	public static void __truncate(String source_) 
	{
		__backup(source_);
		
		db.truncate_table(source_);
	}
	
	public static void __backup(String source_) 
	{ 
		if (is_empty(source_)) return;
		
		String backup_table = db.get_table(source_) + BACKUP_ENDING;
		
		db.__backup_table(source_, backup_table); 
	}	
	
	public static boolean contains_active(String source_) { return (!is_empty(source_) && timestamp_is_today(source_, null)); }
	
	public static boolean is_empty(String source_) { return (get_count(source_, db.DEFAULT_WHERE) == 0); }

	public static int get_count(String source_, String where_) { return db_quick.select_count(source_, where_); }

	public static boolean exists(String source_, String where_) { return db_quick.exists(source_, where_); }
	
	public static boolean is_enabled(String source_, String where_) { return (!arrays.value_exists(get_all_sources_enabled(), source_) || get_boolean(source_, FIELD_ENABLED, where_)); }

	public static String get_string(String source_, String field_, String where_) { return get_string(source_, field_, where_, DEFAULT_WRONG_STRING); }

	public static String get_string(String source_, String field_, String where_, String wrong_) { return get_string(source_, field_, where_, wrong_, DEFAULT_IS_FIELD); }

	public static String get_string(String source_, String field_col_, String where_, String wrong_, boolean is_field_) 
	{ 
		String output = (is_quick(source_) ? db_quick.select_one_string(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_ORDER) : db.select_one_string(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (db.WRONG_STRING.equals(output) ? wrong_ : output);
	}

	public static int get_int(String source_, String field_, String where_) { return get_int(source_, field_, where_, DEFAULT_WRONG_INT); }
	
	public static int get_int(String source_, String field_, String where_, int wrong_) { return get_int(source_, field_, where_, wrong_, DEFAULT_IS_FIELD); }	
	
	public static int get_int(String source_, String field_col_, String where_, int wrong_, boolean is_field_) 
	{ 
		int output = (is_quick(source_) ? db_quick.select_one_int(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_ORDER) : db.select_one_int(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output == db.WRONG_INT ? output : wrong_);
	}

	public static double get_decimal(String source_, String field_, String where_) { return get_decimal(source_, field_, where_, DEFAULT_WRONG_DECIMAL); }

	public static double get_decimal(String source_, String field_, String where_, double wrong_) { return get_decimal(source_, field_, where_, wrong_, DEFAULT_IS_FIELD); }
	
	public static double get_decimal(String source_, String field_col_, String where_, double wrong_, boolean is_field_) 
	{ 
		double output = (is_quick(source_) ? db_quick.select_one_decimal(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_ORDER) : db.select_one_decimal(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output == db.WRONG_DECIMAL ? wrong_ : output);
	}

	public static long get_long(String source_, String field_, String where_) { return get_long(source_, field_, where_, DEFAULT_WRONG_LONG); }

	public static long get_long(String source_, String field_, String where_, long wrong_) { return get_long(source_, field_, where_, wrong_, DEFAULT_IS_FIELD); }
	
	public static long get_long(String source_, String field_col_, String where_, long wrong_, boolean is_field_) 
	{ 
		long output = (is_quick(source_) ? db_quick.select_one_long(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_ORDER) : db.select_one_long(source_, field_col_, where_, db.DEFAULT_ORDER)); 
	
		return (output == db.WRONG_LONG ? wrong_ : output);
	}
	
	public static boolean get_boolean(String source_, String field_, String where_) { return get_boolean(source_, field_, where_, DEFAULT_IS_FIELD); }
	
	public static boolean get_boolean(String source_, String field_col_, String where_, boolean is_field_) { return (is_quick(source_) ? db_quick.select_one_boolean(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_ORDER) : db.select_one_boolean(source_, field_col_, where_, db.DEFAULT_ORDER)); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_, String where_) { return get_all_decimals(source_, field_, where_, DEFAULT_IS_FIELD); }

	public static ArrayList<Double> get_all_decimals(String source_, String field_col_, String where_, boolean is_field_) { return (is_quick(source_) ? db_quick.select_some_decimals(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_decimals(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }
	
	public static ArrayList<String> get_all_strings(String source_, String field_, String where_) { return get_all_strings(source_, field_, where_, DEFAULT_IS_FIELD); }

	public static ArrayList<String> get_all_strings(String source_, String field_col_, String where_, boolean is_field_) { return (is_quick(source_) ? db_quick.select_some_strings(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_strings(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_, String where_) { return get_all_ints(source_, field_, where_, DEFAULT_IS_FIELD); }

	public static ArrayList<Integer> get_all_ints(String source_, String field_col_, String where_, boolean is_field_) { return (is_quick(source_) ? db_quick.select_some_ints(source_, (is_field_ ? get_col(source_, field_col_) : field_col_), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select_some_ints(source_, field_col_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_, String where_) { return get_all_vals(source_, fields_, where_, DEFAULT_IS_FIELD); }

	public static ArrayList<HashMap<String, String>> get_all_vals(String source_, String[] fields_cols_, String where_, boolean are_fields_) { return (is_quick(source_) ? db_quick.select(source_, (are_fields_ ? get_cols(source_, fields_cols_) : fields_cols_), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : accessory.db.select(source_, fields_cols_, where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER)); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_) { return get_vals(source_, fields_, where_, db.DEFAULT_ORDER); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_, String where_, String order_) { return get_vals(source_, fields_, where_, order_, DEFAULT_IS_FIELD); }

	public static HashMap<String, String> get_vals(String source_, String[] fields_cols_, String where_, String order_, boolean are_fields_) { return (is_quick(source_) ? db_quick.select_one(source_, (are_fields_ ? get_cols(source_, fields_cols_) : fields_cols_), where_, order_) : db.select_one(source_, fields_cols_, where_, order_)); }
	
	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_) { return get_vals_old(source_old_, fields_, DEFAULT_IS_FIELD); }
	
	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_cols_, boolean are_fields_) 
	{ 
		HashMap<String, String> output = null;
		
		String[] fields_cols = get_fields_old(source_old_, fields_cols_, are_fields_);
		
		ArrayList<HashMap<String, String>> all = get_all_vals(source_old_, fields_cols, db.DEFAULT_WHERE, are_fields_);
		if (!arrays.is_ok(all)) return output;
		
		String field_col_date = get_field_col(source_old_, FIELD_DATE, are_fields_);
		
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

	public static HashMap<String, String> get_vals_old(String source_old_, String[] fields_cols_, String date_, boolean is_quick_, boolean are_fields_) 
	{ 
		String[] fields_cols = get_fields_old(source_old_, fields_cols_, are_fields_);
		
		return (arrays.is_ok(fields_cols) ? get_vals(source_old_, fields_cols, (strings.is_ok(date_) ? get_where(source_old_, FIELD_DATE, date_) : null), db.DEFAULT_ORDER, are_fields_) : null); 
	}

	public static String get_type(String source_, String field_, String root_, String where_) 
	{
		String key = get_string(source_, field_, where_);
		
		return (strings.is_ok(key) ? get_type_from_key(key, root_) : strings.DEFAULT);
	}

	@SuppressWarnings("unchecked")
	public static boolean insert(String source_, Object vals_) 
	{ 
		Object vals = get_insert_vals(source_, vals_);
		if (!arrays.is_ok(vals)) return false;
		
		if (is_quick(source_)) db_quick.insert(source_, (HashMap<String, String>)vals_);
		else db.insert(source_, (HashMap<String, Object>)vals);

		return db.is_ok(source_);
	}

	public static boolean update(String source_, String field_, Object val_, String where_) { return update(source_, field_, val_, where_, DEFAULT_IS_FIELD); }

	public static boolean update(String source_, String field_col_, Object val_, String where_, boolean is_field_) { return update(source_, add_to_vals(source_, field_col_, val_, null, is_field_), where_); }
	
	@SuppressWarnings("unchecked")
	public static boolean update(String source_, Object vals_, String where_)
	{	
		if (!vals_are_ok(source_, vals_)) return false;
		
		if (is_quick(source_)) db_quick.update(source_, (HashMap<String, String>)vals_, where_);
		else db.update(source_, (HashMap<String, Object>)vals_, where_);
		
		return db.is_ok(source_);
	}

	public static boolean update_type(String source_, String field_, String type_, String root_, String where_) 
	{ 
		String type = get_key_from_type(type_, root_);
		if (!strings.is_ok(type)) return false;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, type);

		return update(source_, vals, where_);
	}
	
	public static boolean insert_update(String source_, String field_, Object val_, String where_) { return insert_update(source_, add_to_vals(source_, field_, val_, null), where_); }

	@SuppressWarnings("unchecked")
	public static boolean insert_update(String source_, Object vals_, String where_)
	{	
		boolean output = false;
		if (!vals_are_ok(source_, vals_)) return output;

		Object vals = get_insert_vals(source_, vals_);

		if (is_quick(source_)) db_quick.insert_update(source_, get_col(source_, db.FIELD_ID), (HashMap<String, String>)vals, where_);
		else db.insert_update_id(source_, (HashMap<String, Object>)vals, where_);
		
		return db.is_ok(source_);
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
			
			if (!exists(source_old, get_where(source_old, FIELD_DATE, dates.get_now_string(ib.common.FORMAT_DATE, dates.DEFAULT_OFFSET))))
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
		return (String[])arrays.add
		(
			populate_all_sources_new(), new String[] { SOURCE_EXECS_OLD, SOURCE_BASIC_OLD, SOURCE_REMOTE_OLD, SOURCE_ORDERS_OLD, SOURCE_TRADES_OLD, SOURCE_APPS_OLD }
		);
	}
	
	public static String[] populate_all_sources_new() { return new String[] { SOURCE_MARKET, SOURCE_EXECS, SOURCE_BASIC, SOURCE_REMOTE, SOURCE_ORDERS, SOURCE_TRADES, SOURCE_WATCHLIST, SOURCE_APPS }; }
	
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
		
		return (max <= db_common.WRONG_MAX_SIZE ? val_ : db_common.adapt_number(val_, max, arrays.key_exists(get_all_negative_numbers(), field_)));
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

	public static String get_type_from_key(String key_, String root_) { return ((strings.is_ok(key_) && strings.is_ok(root_)) ? (root_ + SEPARATOR + key_) : strings.DEFAULT); }

	public static String get_key_from_type(String type_, String root_) { return get_key(type_, root_); }

	public static String get_type_from_key(String key_, HashMap<String, String> array_) { return ((strings.is_ok(key_) && array_.containsValue(key_)) ? (String)arrays.get_key(array_, key_) : strings.DEFAULT); }

	public static String get_key_from_type(String type_, HashMap<String, String> array_) { return ((strings.is_ok(type_) && array_.containsKey(type_)) ? array_.get(type_) : strings.DEFAULT); }

	public static String get_key(String type_, String root_) { return accessory._keys.get_key(type_, root_); }
	
	public static String adapt_user(String val_) { return adapt_string(val_, FIELD_USER); }

	public static Object add_to_vals(String source_, String field_, Object val_, Object vals_) { return add_to_vals(source_, field_, val_, vals_, DEFAULT_IS_FIELD); }
	
	@SuppressWarnings("unchecked")
	public static Object add_to_vals(String source_, String field_col_, Object val_, Object vals_, boolean is_field_) 
	{ 
		Object output = null;
		
		String field_col = get_field_col(source_, field_col_, is_field_);

		if (is_quick(source_))
		{
			HashMap<String, String> vals = (HashMap<String, String>)arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			vals.put(field_col, accessory.db.adapt_input(val_));

			output = new HashMap<String, String>(vals);
		}
		else
		{
			HashMap<String, Object> vals = (HashMap<String, Object>)arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
			vals.put(field_col, val_);
			
			output = new HashMap<String, Object>(vals);
		}
		
		return output;
	}

	public static boolean field_exists(String source_, String field_, Object vals_) { return field_exists(source_, field_, vals_, DEFAULT_IS_FIELD); }

	public static boolean field_exists(String source_, String field_col_, Object vals_, boolean is_field_) { return (get_from_vals(source_, field_col_, vals_, is_field_) != null); }

	public static Object get_from_vals(String source_, String field_, Object vals_) { return get_from_vals(source_, field_, vals_, DEFAULT_IS_FIELD); }
	
	@SuppressWarnings("unchecked")
	public static Object get_from_vals(String source_, String field_col_, Object vals_, boolean is_field_) 
	{ 
		Object output = null;
		if (!vals_are_ok(source_, vals_)) return output;
		
		String field_col = common.get_field_col(source_, field_col_, is_field_);
		
		if (is_quick(source_))
		{
			HashMap<String, String> vals = arrays.get_new_hashmap_xx((HashMap<String, String>)vals_);	
			
			if (vals.containsKey(field_col)) output = vals.get(field_col);		

		}
		else
		{
			HashMap<String, Object> vals = arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_);
		
			if (vals.containsKey(field_col)) output = vals.get(field_col);				
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
		String where = "(DATE(" + db.get_variable(source_, db.get_col(source_, db.FIELD_TIMESTAMP)) + ") = CURDATE())"; 
	
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
	
	public static void start() 
	{
		for (String source: get_all_sources_new()) { get_fields_cols(source); }
	
		get_fields_cols(SOURCE_APPS_OLD);
	};
	
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
		
		ArrayList<HashMap<String, String>> all_vals = common.get_all_vals(source_, null, null);
		if (!arrays.is_ok(all_vals)) return output;

		String date = dates.get_now_string(ib.common.FORMAT_DATE, dates.DEFAULT_OFFSET);

		String col_date = db_common.get_col(source_old_, FIELD_DATE);
		String col_id = db_common.get_col(source_, db.FIELD_ID);

		for (HashMap<String, String> vals: all_vals)
		{
			HashMap<String, String> vals2 = new HashMap<String, String>(vals);
			
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
		if (!are_fields_) field_col_date = get_field_col(source_, FIELD_DATE, true);
		
		ArrayList<String> temp = arrays.to_arraylist(fields_cols_);
		if (!temp.contains(field_col_date)) temp.add(field_col_date);
		
		return arrays.to_array(temp);
	}

	private static String[] get_cols_internal(String source_) { return db.get_cols(get_fields_cols(source_)); }

	private static HashMap<String, String> get_fields_cols_internal(String source_) { return db.get_fields_cols(source_, get_fields(source_)); }

	private static long get_timestamp_elapsed(String source_, String where_, String unit_) { return dates.get_diff(get_string(source_, db.FIELD_TIMESTAMP, where_), ib.common.FORMAT_TIMESTAMP, dates.get_now_string(ib.common.FORMAT_TIMESTAMP, 0), ib.common.FORMAT_TIMESTAMP, unit_); }
	
	private static String[] get_all_negative_numbers() { return _alls.DB_NEGATIVE_NUMBERS; }

	private static Object get_insert_vals(String source_, Object vals_)
	{
		Object vals = arrays.get_new(vals_);

		if (source_includes_user(source_) && !common.field_exists(source_, FIELD_USER, vals)) 
		{
			String user = ib.basic.get_user();
			if (strings.is_ok(user)) vals = common.add_to_vals(source_, FIELD_USER, user, vals_);
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
		String where = accessory.db.get_variable(db_common.get_col(source_, field_)) + "=" + accessory.db.get_value(val_);
		
		if (!strings.are_equal(field_, FIELD_USER) && add_user_ && source_includes_user(source_)) where = get_where_user(source_, where);
	
		return where;		
	}
}