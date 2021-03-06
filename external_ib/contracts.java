package external_ib;

import java.util.HashMap;

import com.ib.client.Contract;

import accessory.arrays;
import accessory.strings;
import accessory_ib._alls;
import accessory_ib.config;
import accessory_ib.types;

public abstract class contracts 
{
	public static final String CONFIG_SECURITY_TYPE = types.CONFIG_CONTRACT_SECURITY_TYPE;
	public static final String CONFIG_CURRENCY = types.CONFIG_CONTRACT_CURRENCY;
	public static final String CONFIG_EXCHANGE = types.CONFIG_CONTRACT_EXCHANGE;
	public static final String CONFIG_EXCHANGE_PRIMARY = types.CONFIG_CONTRACT_EXCHANGE_PRIMARY;
	public static final String CONFIG_EXCHANGE_COUNTRY = types.CONFIG_CONTRACT_EXCHANGE_COUNTRY;
	public static final String CONFIG_EXCHANGE_COUNTRY_US = types.CONFIG_CONTRACT_EXCHANGE_COUNTRY_US;

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

	public static final int MAX_LENGTH_SYMBOL_US_STOCKS = 4;
	public static final int MAX_LENGTH_SYMBOL_US_ANY = 5;

	public static final String DEFAULT_EXCHANGE_COUNTRY = CONFIG_EXCHANGE_COUNTRY_US;
	public static final String DEFAULT_SECURITY_TYPE = contracts.SECURITY_STOCK_ETF;
	public static final String DEFAULT_CURRENCY = "USD";
	public static final String DEFAULT_EXCHANGE = "SMART";
	public static final String DEFAULT_EXCHANGE_PRIMARY = "ISLAND";
	
	public static final String ERROR_INFO_STOCK_ETF = types.ERROR_IB_CONTRACTS_INFO_STOCK_ETF;

	public static int get_max_length_currency() { return DEFAULT_CURRENCY.length(); }

	public static String get_currency() { return (String)config.get_contract(CONFIG_CURRENCY); }
	
	public static boolean currency_is_ok(String currency_) { return strings.are_equivalent(currency_, get_currency()); }
	
	public static Contract get_contract(String symbol_)
	{	
		Contract contract = null;
		
		String security = (String)config.get_contract(CONFIG_SECURITY_TYPE);
		if (!security_is_ok(security)) return contract;
		
		String country = (String)config.get_contract(CONFIG_EXCHANGE_COUNTRY);
		boolean is_us = strings.are_equal(country, CONFIG_EXCHANGE_COUNTRY_US);
		
		int length = strings.get_length(symbol_);
		if (length < 1 || (is_us && (length > MAX_LENGTH_SYMBOL_US_ANY))) return null;
		
		if (security.equals(SECURITY_STOCK_ETF)) 
		{
			if (is_us && (length > MAX_LENGTH_SYMBOL_US_STOCKS)) return null;
			
			contract = get_contract_stock_etf(symbol_);
		}
		
		return contract;
	}

	private static Contract get_contract_stock_etf(String symbol_)
	{		
		String security = SECURITY_STOCK_ETF;
		
		String currency = (String)config.get_contract(CONFIG_CURRENCY);
		String exchange = (String)config.get_contract(CONFIG_EXCHANGE);
		String exchange_primary = (String)config.get_contract(CONFIG_EXCHANGE_PRIMARY);
		
		if (!strings.are_ok(new String[] { currency, exchange, exchange_primary })) 
		{
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("symbol", symbol_);
			info.put("security", security);
			info.put("currency", currency);
			info.put("exchange", exchange);
			info.put("exchange_primary", exchange_primary);
			
			accessory_ib.errors.manage(ERROR_INFO_STOCK_ETF, info);
			
			return null;
		}
		
		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType(security);
		contract.currency(currency);
		contract.exchange(exchange);	
		contract.primaryExch(exchange_primary);

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