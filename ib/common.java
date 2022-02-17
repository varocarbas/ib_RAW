package ib;

import com.ib.client.Contract;

import accessory.strings;
import accessory.time;
import accessory_ib._config;
import accessory_ib.types;

public class common 
{
	public static Contract get_contract(String symbol_)
	{
		if (!strings.is_ok(symbol_)) return null;

		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType("STK");
		contract.currency(_config.get_root(types._CONFIG_IB_CURRENCY));
		contract.exchange("SMART");	
		contract.primaryExch("ISLAND");

		return contract;
	}

	public static String get_market_time()
	{
		return time.get_current_time(time.TIME_SHORT, 0);
	}

	public static String normalise_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}
}