package ib;

import accessory.strings;

import java.util.HashMap;

import accessory.dates;
import accessory.numbers;

public abstract class common
{
	public static final int WRONG_ID = 0;
			
	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String check_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}
	
	public static double adapt_val(double val_, String field_)
	{
		HashMap<String, Integer> sizes = db_ib.common.get_all_val_max_sizes();
		if (!sizes.containsKey(field_)) return val_;
		
		int size = sizes.get(field_);
		double max = Math.pow(10, (size + 1)) - 1.0;
		
		return (val_ < max ? val_ : max);
	}
	
	static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
}