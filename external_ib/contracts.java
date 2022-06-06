package external_ib;

import accessory.arrays;
import accessory_ib._alls;

public class contracts 
{
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