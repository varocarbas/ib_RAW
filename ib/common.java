package ib;

import com.ib.client.Contract;

import accessory.strings;
import accessory.dates;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib.config;
import accessory_ib.types;

public class common extends parent_static 
{
	private static int MIN_ID = 1;
	private static int MAX_ID = 2500;
	
	public static int WRONG_ID = MIN_ID - 1;
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_COMMON); }
	
	public static boolean req_id_is_ok(int id_) { return numbers.is_ok(id_, MIN_ID, MAX_ID); }
	
	public static int get_req_id() { return get_req_id(WRONG_ID); }
	
	public static int get_req_id(int last_) 
	{ 
		int id = last_ + 1;

		return (req_id_is_ok(id) ? id : MIN_ID); 
	}

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