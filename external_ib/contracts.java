package external_ib;

import java.util.HashMap;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.config;
import accessory_ib.types;

public class contracts 
{
	public static final String SECURITY_TYPE = types.CONFIG_CONTRACT_SECURITY_TYPE;
	public static final String CURRENCY = types.CONFIG_CONTRACT_CURRENCY;
	public static final String EXCHANGE = types.CONFIG_CONTRACT_EXCHANGE;
	public static final String PRIMARY_EXCHANGE = types.CONFIG_CONTRACT_PRIMARY_EXCHANGE;

	//--- To be synced with the contract.SecType values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Contract.html).
	public static final String SECURITY_STOCK_ETF = "STK";
	public static final String SECURITY_OPTION = "OPT";
	public static final String SECURITY_FUTURE = "FUT";
	public static final String SECURITY_INDEX = "IND";
	public static final String SECURITY_FUTURE_OPTION = "FOP";
	public static final String SECURITY_FOREX = "CASH";
	public static final String SECURITY_COMBO = "BAG";
	public static final String SECURITY_WARRANT = "WAR";
	public static final String SECURITY_BOND = "BOND";
	public static final String SECURITY_COMMODITY = "CMDTY";
	public static final String SECURITY_NEWS = "NEWS";
	public static final String SECURITY_MUTUAL_FUND = "FUND";
	//---
	
	public static final String ERROR_INFO = types.ERROR_IB_CONTRACTS_INFO;
	
	public static Contract get_contract(String symbol_)
	{
		String security = (String)config.get_contract(SECURITY_TYPE);
		String currency = (String)config.get_contract(CURRENCY);
		String exchange = (String)config.get_contract(EXCHANGE);
		String primary_exchange = (String)config.get_contract(PRIMARY_EXCHANGE);
		
		if (!strings.is_ok(symbol_) || !external_ib.contracts.security_is_ok(security) || !strings.are_ok(new String[] { currency, exchange, primary_exchange })) 
		{
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("symbol", symbol_);
			info.put("security", security);
			info.put("currency", currency);
			info.put("exchange", exchange);
			info.put("primary_exchange", primary_exchange);
			
			accessory_ib.errors.manage(ERROR_INFO, info);
			
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

	public static boolean security_is_ok(String security_) { return arrays.value_exists(get_all_securities(), security_); }

	public static String[] populate_all_securities() 
	{ 
		return new String[] 
		{
			SECURITY_STOCK_ETF, SECURITY_OPTION, SECURITY_FUTURE, SECURITY_INDEX, SECURITY_FUTURE_OPTION, SECURITY_FOREX, 
			SECURITY_COMBO, SECURITY_WARRANT, SECURITY_BOND, SECURITY_COMMODITY, SECURITY_NEWS, SECURITY_MUTUAL_FUND
		}; 
	}
	
	public static String[] get_all_securities() { return _alls.EXTERNAL_CONTRACTS_SECURITIES; }
}