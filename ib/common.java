package ib;

import com.ib.client.Contract;

import accessory.strings;
import accessory.dates;
import accessory_ib.config;
import accessory_ib.ini;
import accessory_ib._types;

public class common 
{
	static { ini.load(); }
	
	public static Contract get_contract(String symbol_)
	{
		if (!strings.is_ok(symbol_)) return null;

		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType("STK");
		contract.currency(config.get_root(_types.CONFIG_IB_CURRENCY));
		contract.exchange("SMART");	
		contract.primaryExch("ISLAND");

		return contract;
	}

	public static String get_market_time()
	{
		return dates.get_current_time(dates.FORMAT_TIME_SHORT, 0);
	}

	public static String normalise_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}
}