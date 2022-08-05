package ib;

import accessory.strings;
import accessory.arrays;
import accessory.dates;
import accessory.numbers;

public abstract class common
{
	public static final int DEFAULT_ROUND_DECIMALS = 2;
	
	public static final String FORMAT_TIME = dates.FORMAT_TIME_SHORT;
	public static final String FORMAT_TIME2 = dates.FORMAT_TIME_FULL;
	public static final String FORMAT_TIME_ELAPSED = dates.FORMAT_TIME_FULL;
	
	public static final double WRONG_VALUE = 0.0;
	public static final double WRONG_PRICE = 0.0;
	public static final double WRONG_SIZE = 0.0;
	
	public static final double WRONG_QUANTITY = 0.0;
	public static final double WRONG_MONEY = numbers.MIN_DECIMAL;
	
	public static final int WRONG_ID = -1;
	public static final int WRONG_ORDER_ID = WRONG_ID;
	public static final int WRONG_REQUEST_ID = WRONG_ID; //Request ID required for some interactions with the IB's interface.
	public static final int WRONG_REQUEST = 0; //Unique identifier used in the remote communication within this library.

	public static final String DEFAULT_FORMAT_TIME = FORMAT_TIME;
	
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
			if (i == 0) price = trades.get_price(symbol_);
			else if (i == 1) price = watchlist.get_price(symbol_);
			else if (i == 2) price = market.get_price(symbol_);
			
			if (price_is_ok(price)) break;	
		}
				
		return price;
	}
	
	static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }

	private static String get_current_time(boolean is_main_) { return dates.get_now_string((is_main_ ? FORMAT_TIME : FORMAT_TIME2)); }
}