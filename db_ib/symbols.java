package db_ib;

import java.util.HashMap;

import accessory.db_common;
import accessory.strings;

public abstract class symbols 
{
	public static final String SOURCE = common.SOURCE_SYMBOLS;
	public static final String SOURCE_OLD = common.SOURCE_SYMBOLS_OLD;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String NAME = common.FIELD_NAME;
	public static final String COUNTRY = common.FIELD_COUNTRY;
	public static final String EXCHANGE = common.FIELD_EXCHANGE;
	
	public static final int MAX_SIZE_SYMBOLS = ib.symbols.MAX_LENGTH_SYMBOL_US_ANY;
	
	public static boolean add(String symbol_, String name_, String country_, String exchange_)
	{
		String symbol = db_common.adapt_string(symbol_, MAX_SIZE_SYMBOLS);
		if (!strings.is_ok(symbol)) return false;
		
		Object vals = db_common.add_to_vals(SOURCE, SYMBOL, symbol, null);

		String name = db_ib.common.store_type(name_, NAME);
		if (strings.is_ok(name)) vals = db_common.add_to_vals(SOURCE, NAME, name, vals);

		String country = db_common.adapt_string(country_);
		if (strings.is_ok(country)) vals = db_common.add_to_vals(SOURCE, COUNTRY, country, vals);
		
		String exchange = db_ib.common.store_type(exchange_, EXCHANGE);
		if (strings.is_ok(exchange)) vals = db_common.add_to_vals(SOURCE, EXCHANGE, exchange, vals);
		
		return db_ib.common.insert_update(SOURCE, vals, db_ib.common.get_where_symbol(SOURCE, symbol));
	}
	
	public static HashMap<String, String> get(String symbol_) { return db_common.get_vals(SOURCE, db_common.get_fields(SOURCE), db_common.get_where(SOURCE, SYMBOL, db_common.adapt_string(symbol_, MAX_SIZE_SYMBOLS))); }
}