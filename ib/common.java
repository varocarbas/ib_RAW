package ib;

import accessory.strings;

import java.time.LocalDate;
import java.time.LocalTime;

import accessory.arrays;
import accessory.dates;
import accessory.io;
import accessory.misc;
import accessory.numbers;
import accessory.paths;

public abstract class common
{
	public static final int DEFAULT_ROUND_DECIMALS = 2;
	
	public static final String FORMAT_TIME = dates.FORMAT_TIME_SHORT;
	public static final String FORMAT_TIME2 = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_TIME_ELAPSED = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_DATE = dates.FORMAT_DATE;
		
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
	public static final int WRONG_I = -1;
	
	public static final String DEFAULT_FORMAT_TIME = FORMAT_TIME;

	private static boolean _use_market_tz = true;

	public static final String MARKET_TZ = dates.TZ_NY;
	public static final int MARKET_OFFSET = (_use_market_tz ? dates.get_offset(MARKET_TZ, true) : 0);
	
	public static final LocalTime MARKET_TIME_OPEN = dates.time_from_string("09:30:00");
	public static final LocalTime MARKET_TIME_CLOSE = dates.time_from_string("16:00:00");
	public static final LocalTime MARKET_TIME_CLOSE_EARLY = dates.time_from_string("13:00:00");
	
	private static LocalTime _market_time_open = null;
	private static LocalTime _market_time_close = null;
	
	private static boolean _is_market_holiday = false;
	private static boolean _is_market_holiday_ok = false;
	
	//--- Each line of these files is expected to follow the pattern defined by dates.get_pattern(FORMAT_DATE).
	private static String _path_market_holidays = paths.build(new String[] { paths.get_dir(paths.DIR_INFO), ("market_holidays" + paths.EXTENSION_TEXT) }, true);
	private static String _path_market_early_closes = paths.build(new String[] { paths.get_dir(paths.DIR_INFO), ("market_early_closes" + paths.EXTENSION_TEXT) }, true);
	//---
	
	public static boolean use_market_tz() { return _use_market_tz; }

	public static String path_market_holidays() { return _path_market_holidays; }

	public static boolean path_market_holidays(String path_) 
	{
		boolean updated = false;
		
		if (paths.exists(path_)) 
		{
			_path_market_holidays = path_;
			
			updated = true;
		}
	
		return updated;
	}

	public static String path_market_early_closes() { return _path_market_early_closes; }

	public static boolean path_market_early_closes(String path_) 
	{
		boolean updated = false;
		
		if (paths.exists(path_)) 
		{
			_path_market_early_closes = path_;
			
			updated = true;
		}
	
		return updated;
	}
	
	public static String get_current_time() { return get_current_time(true); }

	public static String get_current_time2() { return get_current_time(false); }
	
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
	
	public static boolean percent_is_ok(double val_) { return percent_is_ok(val_, true); }

	public static boolean percent_is_ok(double val_, boolean negative_too_) { return (((negative_too_ && val_ >= -100.0) || (!negative_too_ && val_ > 0.0)) && val_ <= 100.0); }
	
	public static double get_price(String symbol_)
	{
		double price = WRONG_PRICE;
		
		String symbol = check_symbol(symbol_);
		if (!strings.is_ok(symbol)) return price;
		
		for (int i = 0; i < 3; i++)
		{
			if (i == 0) price = watchlist.get_price(symbol_);
			else if (i == 1) price = trades.get_price(symbol_);
			else if (i == 2) price = market.get_price(symbol_);
			
			if (price_is_ok(price)) break;	
		}
				
		return price;
	}
	
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

	public static LocalTime get_market_time_open() 
	{
		if (_market_time_open == null) populate_market_time_open();
				
		return _market_time_open;
	}

	public static LocalTime get_market_time_close() 
	{
		if (_market_time_close == null) populate_market_time_close();
		
		return _market_time_close;
	}

	public static boolean is_market_holiday() 
	{
		if (!_is_market_holiday_ok) populate_is_market_holiday();
		
		return _is_market_holiday;
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

	private static void populate_market_time_open() { _market_time_open = MARKET_TIME_OPEN; }

	private static void populate_market_time_close() { _market_time_close = (market_today_is_included(_path_market_early_closes) ? MARKET_TIME_CLOSE_EARLY : MARKET_TIME_CLOSE); }

	private static void populate_is_market_holiday() { _is_market_holiday = market_today_is_included(_path_market_holidays); }
	
	private static boolean market_today_is_included(String path_) 
	{
		String[] lines = io.file_to_array(path_);
		if (!arrays.is_ok(lines)) return false;
		
		LocalDate today = dates.get_now_date();
		
		for (String line: lines)
		{
			LocalDate date = dates.date_from_string(line);
			if (date != null && date.isEqual(today)) return true;
		}
		
		return false;
	}
}