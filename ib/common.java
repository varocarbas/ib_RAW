package ib;

import java.util.HashMap;

import com.ib.client.Contract;

import accessory.strings;
import accessory.dates;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib.config;
import accessory_ib.types;

public class common extends parent_static 
{
	public static final String ERROR_CONTRACT_INFO = types.ERROR_IB_CONTRACT_INFO;
	
	private static int MIN_ID = 1;
	private static int MAX_ID = 2500;
	
	public static int WRONG_ID = MIN_ID - 1;
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_COMMON); }
	
	public static boolean id_is_ok(int id_) { return numbers.is_ok(id_, MIN_ID, MAX_ID); }
	
	public static int get_next_id() { return get_next_id(WRONG_ID); }
	
	public static int get_next_id(int last_) 
	{ 
		int id = last_ + 1;

		return (id_is_ok(id) ? id : MIN_ID); 
	}

	public static Contract get_contract(String symbol_)
	{
		String security = config.get_contract(types.CONFIG_CONTRACT_SECURITY_TYPE);
		String currency = config.get_contract(types.CONFIG_CONTRACT_CURRENCY);
		String exchange = config.get_contract(types.CONFIG_CONTRACT_EXCHANGE);
		String primary_exchange = config.get_contract(types.CONFIG_CONTRACT_PRIMARY_EXCHANGE);
		
		if (!strings.is_ok(symbol_) || !external_ib.contracts.security_is_ok(security) || !strings.are_ok(new String[] { currency, exchange, primary_exchange })) 
		{
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("symbol", symbol_);
			info.put("security", security);
			info.put("currency", currency);
			info.put("exchange", exchange);
			info.put("primary_exchange", primary_exchange);
			
			accessory_ib.errors.manage(ERROR_CONTRACT_INFO, info);
			
			return null;
		}
		
		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType(security);
		contract.currency(currency);
		contract.exchange(exchange);	
		contract.primaryExch(primary_exchange);

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