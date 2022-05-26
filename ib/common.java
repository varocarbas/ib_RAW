package ib;

import com.ib.client.Contract;

import accessory.strings;
import accessory.dates;
import accessory.parent_static;
import accessory_ib.config;
import accessory_ib.types;

public class common extends parent_static 
{
	public static String get_id() { return accessory.types.get_id(types.ID_COMMON); }
	
	public static Contract get_contract(String symbol_)
	{
		if (!strings.is_ok(symbol_)) return null;

		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType("STK");
		contract.currency(config.get_basic(types.CONFIG_BASIC_CURRENCY));
		contract.exchange("SMART");	
		contract.primaryExch("ISLAND");

		return contract;
	}

	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String normalise_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}
}