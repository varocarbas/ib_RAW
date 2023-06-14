package ib;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import accessory.arrays;
import accessory.dates;
import accessory.db_common;
import accessory.io;
import accessory.strings;
import accessory_ib.paths;

public abstract class market 
{	
	public static final String DB_SOURCE = db_ib.market.SOURCE;
	
	public static final String DB_SYMBOL = db_ib.market.SYMBOL;
	public static final String DB_PRICE = db_ib.market.PRICE;
	public static final String DB_SIZE = db_ib.market.SIZE;
	public static final String DB_TIME = db_ib.market.TIME;
	public static final String DB_OPEN = db_ib.market.OPEN;
	public static final String DB_CLOSE = db_ib.market.CLOSE;
	public static final String DB_LOW = db_ib.market.LOW;
	public static final String DB_HIGH = db_ib.market.HIGH;
	public static final String DB_VOLUME = db_ib.market.VOLUME;		
	public static final String DB_ASK = db_ib.market.ASK;
	public static final String DB_ASK_SIZE = db_ib.market.ASK_SIZE;
	public static final String DB_BID = db_ib.market.BID;
	public static final String DB_BID_SIZE = db_ib.market.BID_SIZE;
	public static final String DB_HALTED = db_ib.market.HALTED;
	public static final String DB_HALTED_TOT = db_ib.market.HALTED_TOT;
	public static final String DB_ENABLED = db_ib.market.ENABLED;

	public static final LocalTime TIME_OPEN = dates.time_from_string("09:30:00");
	public static final LocalTime TIME_CLOSE = dates.time_from_string("16:00:00");
	public static final LocalTime TIME_CLOSE_EARLY = dates.time_from_string("13:00:00");
	
	public static final String TZ = dates.TZ_NY;
	
	public static final double MAX_VAR = 50.0;
	
	private static LocalTime _time_open = null;
	private static LocalTime _time_close = null;
	
	private static boolean _is_holiday = false;
	private static boolean _is_holiday_populated = false;
	
	public static boolean is_quick() { return db_common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(DB_SOURCE, is_quick_); }

	public static boolean logs_to_screen() { return async_data_market.logs_to_screen(); }

	public static void logs_to_screen(boolean logs_to_screen_) { async_data_market.logs_to_screen(logs_to_screen_); }

	public static boolean snapshot_nonstop() { return async_data_market.snapshot_nonstop(); }

	public static void snapshot_nonstop(boolean snapshot_nonstop_) { async_data_market.snapshot_nonstop(snapshot_nonstop_); }

	public static int max_simultaneous_symbols() { return async_data_market.MAX_SIMULTANEOUS_SYMBOLS; }
	
	public static boolean start(String symbol_) { return async_data_market.start(symbol_); }

	public static boolean start_snapshot(String symbol_) { return async_data_market.start_snapshot(symbol_); }

	public static boolean start_stream(String symbol_) { return async_data_market.start_stream(symbol_); }

	public static void stop(String symbol_) { async_data_market.stop(symbol_); }

	public static void stop_all() { async_data_market.stop_all(); }
	
	public static double get_price(String symbol_) 
	{ 
		String symbol = ib.common.normalise_symbol(symbol_);
		
		return (strings.is_ok(symbol) ? db_ib.async_data.get_price(DB_SOURCE, symbol) : common.WRONG_PRICE); 
	}
	
	public static ArrayList<String> get_active_symbols() { return async_data_market.get_active_symbols(); }
	
	public static ArrayList<String> get_all_symbols() { return async_data_market.get_all_symbols(); }

	public static int get_offset() { return ini_market.get_offset(); }
	
	public static boolean update_tz() { return ini_market.update_tz(); }
	
	public static String path_market_holidays() { return paths.get_path_market_holidays(); }

	public static boolean path_market_holidays(String path_) { return paths.update_path_market_holidays(path_); }

	public static String path_market_early_closes() { return paths.get_path_market_early_closes(); }

	public static boolean path_market_early_closes(String path_) { return paths.update_path_market_early_closes(path_); }

	public static LocalTime get_time_open() 
	{
		if (_time_open == null) populate_time_open();
				
		return _time_open;
	}

	public static LocalTime get_time_close() 
	{
		if (_time_close == null) populate_time_close();
		
		return _time_close;
	}

	public static boolean day_is_ok() { return day_is_ok(dates.get_now_date()); }
	
	public static String get_last_day_ok()
	{
		LocalDate date = dates.get_now_date();
	
		while (true)
		{
			date = date.minusDays(1);
			
			if (day_is_ok(date)) return dates.to_string(date, common.FORMAT_DATE);
		}
	}
	
	public static boolean is_holiday() 
	{
		if (!_is_holiday_populated) populate_is_holiday();
		
		return _is_holiday;
	}

	private static void populate_time_open() { _time_open = TIME_OPEN; }

	private static void populate_time_close() { _time_close = (is_today(path_market_early_closes(), dates.get_now_date()) ? TIME_CLOSE_EARLY : TIME_CLOSE); }

	private static void populate_is_holiday() { _is_holiday = is_holiday(dates.get_now_date()); }

	private static boolean day_is_ok(LocalDate date_) { return (!is_holiday(date_) && !dates.is_weekend(date_)); }

	private static boolean is_holiday(LocalDate date_) { return is_today(path_market_holidays(), date_); }
	
	private static boolean is_today(String path_, LocalDate date_) 
	{
		String[] lines = io.file_to_array(path_);
		if (!arrays.is_ok(lines)) return false;

		for (String line: lines)
		{
			if (dates.are_equal(dates.date_from_string(line), date_)) return true;
		}
		
		return false;
	}
}