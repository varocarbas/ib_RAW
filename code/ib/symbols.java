package ib;

import java.util.HashMap;

import accessory.strings;
import accessory_ib._types;

public abstract class symbols 
{
	public static final String EXCHANGE = _types.EXCHANGE;
	public static final String EXCHANGE_ANY = _types.EXCHANGE_ANY;
	
	public static final String EXCHANGE_US = _types.EXCHANGE_US;
	public static final String EXCHANGE_US_ANY = _types.EXCHANGE_US_ANY;
	public static final String EXCHANGE_NYSE = _types.EXCHANGE_US_NYSE;
	public static final String EXCHANGE_NASDAQ = _types.EXCHANGE_US_NASDAQ;

	public static final int MAX_LENGTH_SYMBOL_US_STOCKS = 4;
	public static final int MAX_LENGTH_SYMBOL_US_ANY = 5;
	
	public static final String DEFAULT_COUNTRY = strings.DEFAULT;
	public static final String DEFAULT_EXCHANGE = EXCHANGE_US_ANY;
	public static final int DEFAULT_MAX_LENGTH = MAX_LENGTH_SYMBOL_US_STOCKS;
	
	public static boolean add(String symbol_) { return add(symbol_, null); }
	
	public static boolean add(String symbol_, String exchange_) { return add(symbol_, null, null, exchange_); }
	
	public static boolean add(String symbol_, String name_, String country_, String exchange_) { return db_ib.symbols.add(symbol_, name_, country_, exchange_); }

	public static HashMap<String, String> get(String symbol_) { return db_ib.symbols.get(symbol_); }
	
	public static boolean exchange_is_us(String exchange_) { return exchange_us_is_ok(exchange_); }

	public static boolean exchange_is_ok(String exchange_) { return strings.is_ok(check_exchange(exchange_)); }

	public static String check_exchange(String exchange_) { return accessory._types.check_type(exchange_, EXCHANGE); }

	public static boolean exchange_us_is_ok(String exchange_us_) { return strings.is_ok(check_exchange_us(exchange_us_)); }

	public static String check_exchange_us(String exchange_us_) { return accessory._types.check_type(exchange_us_, EXCHANGE_US); }
}