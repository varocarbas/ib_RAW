package ib;

import accessory.strings;
import accessory.dates;
import accessory.numbers;

public abstract class common
{
	public static final double WRONG_PRICE = 0.0;
	public static final double WRONG_VOLUME_SIZE = 0.0;
	public static final int WRONG_ID = 0;
	
	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String normalise_symbol(String symbol_) { return check_symbol(symbol_); }
	
	public static String check_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}

	public static double get_max_price() { return db_ib.common.get_max_val(db_ib.common.FIELD_PRICE); }

	public static double get_max_volume_size() { return db_ib.common.get_max_val(db_ib.common.FIELD_VOLUME); }
	
	public static boolean price_is_ok(double val_) { return (val_ > WRONG_PRICE && val_ <= get_max_price()); }

	public static boolean volume_size_is_ok(double val_) { return (val_ > WRONG_VOLUME_SIZE && val_ <= get_max_volume_size()); }
	
	static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
}