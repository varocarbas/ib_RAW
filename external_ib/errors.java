package external_ib;

import accessory.arrays;

public class errors 
{
	//------ To be synced with the corresponding get_all_[...]() methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/message_codes.html.
	public static final int WARNING_2100 = 2100; //New account data requested from TWS. API client has been unsubscribed from account data.
	public static final int WARNING_2101 = 2101; //Unable to subscribe to account as the following clients are subscribed to a different account.
	public static final int WARNING_2102 = 2102; //	Unable to modify this order as it is still being processed.
	public static final int WARNING_2103 = 2103; //A market data farm is disconnected.
	public static final int WARNING_2104 = 2104; //Market data farm connection is OK.
	public static final int WARNING_2105 = 2105; //A historical data farm is disconnected.
	public static final int WARNING_2106 = 2106; //A historical data farm is connected.
	public static final int WARNING_2107 = 2107; //A historical data farm connection has become inactive but should be available upon demand.
	public static final int WARNING_2108 = 2108; //A market data farm connection has become inactive but should be available upon demand.
	public static final int WARNING_2109 = 2109; //Order Event Warning: Attribute "Outside Regular Trading Hours" is ignored based on the order type and destination. PlaceOrder is now processed.
	public static final int WARNING_2110 = 2110; //Connectivity between TWS and server is broken. It will be restored automatically.
	public static final int WARNING_2137 = 2137; //Cross Side Warning.
	public static final int WARNING_2158 = 2158; //Sec-def data farm connection is OK.
	public static final int WARNING_2168 = 2168; //Etrade Only Not Supported Warning.
	public static final int WARNING_2169 = 2169; //	Firm Quote Only Not Supported Warning.
	//---

	//------
	
	public static boolean is_warning(int code_) { return arrays.value_exists(get_all_warnings(), code_); }

	private static int[] get_all_warnings()
	{
		return new int[]
		{
			WARNING_2100, WARNING_2101, WARNING_2102, WARNING_2103, WARNING_2104, WARNING_2105, WARNING_2106, WARNING_2107,
			WARNING_2108, WARNING_2109, WARNING_2110, WARNING_2137, WARNING_2158, WARNING_2168, WARNING_2169
		};
	}
}
