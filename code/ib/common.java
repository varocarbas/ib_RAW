package ib;

import accessory.strings;
import accessory_ib._types;
import accessory_ib.config;
import accessory._keys;
import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.numbers;

public abstract class common
{
	public static final String FORMAT_TIME = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_TIME2 = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_TIME_ELAPSED = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_DATE = dates.FORMAT_DATE;
	public static final String FORMAT_TIMESTAMP = dates.FORMAT_TIMESTAMP;

	public static final String CONFIG_ALWAYS_DISABLE_SYMBOL = _types.CONFIG_COMMON_ALWAYS_DISABLE_SYMBOL;

	public static final double WRONG_VALUE = 0.0;
	public static final double WRONG_PRICE = 0.0;
	public static final double WRONG_SIZE = 0.0;
	
	public static final double WRONG_QUANTITY = 0.0;
	public static final double WRONG_MONEY = numbers.MIN_DECIMAL;
	public static final double WRONG_MONEY2 = 0.0;
	
	public static final int WRONG_ID = -1;
	public static final int WRONG_ORDER_ID = orders.MIN_ORDER_ID - 1;
	public static final int WRONG_REQUEST_ID = common_xsync.MIN_REQ_ID_SYNC - 1; //Request ID required for some interactions with the IB's interface.
	public static final int WRONG_REQUEST = 0; //Unique identifier used in the remote communication within this library.
	public static final int WRONG_I = arrays.WRONG_I;
	
	public static final String DEFAULT_FORMAT_TIME = FORMAT_TIME;
	public static final int DEFAULT_WAIT_SECS_PRICE = 2;
	public static final boolean DEFAULT_ALWAYS_DISABLE_SYMBOL = false;
	
	public static boolean always_disable_symbol() { return config.get_common_boolean(CONFIG_ALWAYS_DISABLE_SYMBOL); }

	public static boolean always_disable_symbol(boolean always_disable_symbol_) { return config.update_common(CONFIG_ALWAYS_DISABLE_SYMBOL, always_disable_symbol_); }

	public static String get_current_time() { return get_current_time(true); }

	public static String get_current_time2() { return get_current_time(false); }

	public static boolean time2_is_ok(String time2_, long delay_secs_) { return (strings.is_ok(time2_) && !dates.target_met(dates.time_from_string(time2_, false), dates.UNIT_SECONDS, delay_secs_, true)); }

	public static boolean timestamp_is_ok(String timestamp_, long delay_secs_) { return (strings.is_ok(timestamp_) && !dates.target_met(dates.from_string(timestamp_, FORMAT_TIMESTAMP, false), dates.UNIT_SECONDS, delay_secs_, false)); }

	public static String normalise_symbol(String symbol_) { return check_symbol(symbol_); }
	
	public static double adapt_price(double val_) { return db_ib.common.adapt_price(val_); }
	
	public static double adapt_money(double val_) { return db_ib.common.adapt_money(val_); }

	public static String check_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}

	public static double get_max_price() { return db_ib.common.get_max_val(db_ib.common.FIELD_PRICE); }

	public static double get_max_size() { return db_ib.common.get_max_val(db_ib.common.FIELD_VOLUME); }

	public static double get_max_money() { return db_ib.common.get_max_val(db_ib.common.FIELD_MONEY); }

	public static boolean prices_are_ok(double[] vals_) 
	{
		if (!arrays.is_ok(vals_)) return false;
		
		for (double val: vals_) 
		{
			if (!price_is_ok(val)) return false;
		}
		
		return true;
	}

	public static boolean price_is_ok(double val_) { return (val_ > WRONG_PRICE && val_ <= get_max_price()); }

	public static boolean size_is_ok(double val_) { return (val_ > WRONG_SIZE && val_ <= get_max_size()); }

	public static boolean money_is_ok(double val_) 
	{ 
		double max = get_max_money();

		return numbers.is_ok(val_, -1.0 * max, max); 
	}
	
	public static boolean percent_is_ok(double val_) { return percent_is_ok(val_, true, false); }

	public static boolean percent_is_ok(double val_, boolean negative_too_) { return percent_is_ok(val_, negative_too_, false); }
	
	public static boolean percent_is_ok(double val_, boolean negative_too_, boolean beyond_100_) 
	{
		double max_min = (beyond_100_ ? 1000 : 100);
		
		return (((negative_too_ && val_ >= -1 * max_min) || (!negative_too_ && val_ > 0.0)) && (val_ <= max_min)); 
	}

	public static double _get_price(String symbol_, boolean check_ib_) 
	{ 
		double output = WRONG_PRICE;
		
		if (check_ib_)
		{
			double temp = __get_price(symbol_);
			
			if (price_is_ok(temp)) output = temp;
		}
		
		if (!price_is_ok(output)) output = get_price(symbol_);
		
		return output; 
	}
	
	public static double get_price(String symbol_)
	{
		double price = WRONG_PRICE;
		
		String symbol = check_symbol(symbol_);
		if (!strings.is_ok(symbol)) return price;
		
		for (int i = 0; i < 2; i++)
		{
			if (i == 0) price = watchlist.get_price(symbol_);
			else if (i == 1) price = market.get_price(symbol_);
			
			if (price_is_ok(price)) break;	
		}
				
		return price;
	}

	public static double __get_price(String symbol_) { return __get_price(symbol_, DEFAULT_WAIT_SECS_PRICE); }

	public static double __get_price(String symbol_, int pause_secs_) { return watchlist_quicker.__get_price(symbol_, pause_secs_); }
	
	public static String get_symbol(int order_id_main_)
	{
		String symbol = strings.DEFAULT;
		if (order_id_main_ <= WRONG_ORDER_ID) return symbol;
		
		for (int i = 0; i < 3; i++)
		{
			if (i == 0) symbol = execs.get_symbol(order_id_main_);
			else if (i == 1) symbol = remote.get_symbol_order_id(order_id_main_);
			else if (i == 2) symbol = orders.get_symbol(order_id_main_);
			
			if (strings.is_ok(symbol)) break;	
		}
				
		return symbol;
	}	
	
	public static double get_quantity(int order_id_main_)
	{
		double quantity = common.WRONG_QUANTITY;
		if (order_id_main_ <= WRONG_ORDER_ID) return quantity;
		
		for (int i = 0; i < 2; i++)
		{
			if (i == 0) quantity = orders.get_quantity(order_id_main_);
			else if (i == 1) quantity = execs.get_quantity(order_id_main_, true);
			
			if (quantity > WRONG_QUANTITY) break;	
		}
				
		return quantity;
	}	
	
	public static String get_key(String type_, String root_) { return _keys.get_startup_key(type_, root_); }

	public static String get_type(String key_, String root_) { return _keys.get_startup_type(key_, root_); }

	public static void delete_symbol(String symbol_) { delete_symbol(db_ib.common.SOURCE_MARKET, symbol_); }
	
	public static void delete_symbol(String source_, String symbol_) 
	{
		if (always_disable_symbol() && db_ib.common.has_enabled(source_)) db_ib.common.disable_symbol(source_, symbol_);
		else db_ib.common.delete_symbol(source_, symbol_); 
	}
	
	static String get_log_file_id(String id_) 
	{
		String output = apps.get_app_name();	
		
		if (strings.is_ok(output))
		{
			if (strings.is_ok(id_)) output += misc.SEPARATOR_NAME + id_;
		}
		else output = strings.to_string(id_);
		
		return output;
	}
	
	static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }

	private static String get_current_time(boolean is_main_) { return dates.get_now_string((is_main_ ? FORMAT_TIME : FORMAT_TIME2)); }
}