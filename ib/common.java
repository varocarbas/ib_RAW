package ib;

import accessory.strings;

import accessory.dates;
import accessory.numbers;

public abstract class common
{
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
	
	static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
}