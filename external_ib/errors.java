package external_ib;

import accessory.arrays;
import accessory_ib._alls;

public class errors 
{
	//------ To be synced with the corresponding get_all_[...]() methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/message_codes.html.
	public static final int ERROR_200 = 200; //The specified contract does not match any in IB's database, usually because of an incorrect or missing parameter.
                                             //Ambiguity may occur when the contract definition provided is not unique.
                                             //For some stocks that has the same Symbol, Currency and Exchange, you need to specify the IBApi.Contract.PrimaryExch attribute to avoid ambiguity. Please refer to a sample stock contract here.
                                             //For futures that has multiple multipliers for the same expiration, You need to specify the IBApi.Contract.Multiplier attribute to avoid ambiguity. Please refer to a sample futures contract here.
	public static final int ERROR_202 = 202; //An active order on the IB server was cancelled. See Order Placement Considerations for additional information/considerations for these errors.
	public static final int ERROR_300 = 300; //An attempt was made to cancel market data for a ticker ID that was not associated with a current subscription. With the DDE API this occurs by clearing the spreadsheet cell.
	//---

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/message_codes.html.
	public static final int WARNING_2100 = 2100; //The TWS only allows one IBApi.EClient.reqAccountUpdates request at a time. If the client application attempts to subscribe to a second account without canceling the previous subscription, the new request will override the old one and the TWS will send this message notifying so.
	public static final int WARNING_2101 = 2101; //If a client application invokes IBApi.EClient.reqAccountUpdates when there is an active subscription started by a different client, the TWS will reject the new subscription request with this message.
	public static final int WARNING_2102 = 2102; //If you attempt to modify an order before it gets processed by the system, the modification will be rejected. Wait until the order has been fully processed before modifying it. See Placing Orders for further details.
	public static final int WARNING_2103 = 2103; //Indicates a connectivity problem to an IB server. Outside of the nightly IB server reset, this typically indicates an underlying ISP connectivity issue.
	public static final int WARNING_2104 = 2104; //A notification that connection to the market data server is ok. This is a notification and not a true error condition, and is expected on first establishing connection.
	public static final int WARNING_2105 = 2105; //Indicates a connectivity problem to an IB server. Outside of the nightly IB server reset, this typically indicates an underlying ISP connectivity issue.
	public static final int WARNING_2106 = 2106; //A notification that connection to the market data server is ok. This is a notification and not a true error condition, and is expected on first establishing connection.
	public static final int WARNING_2107 = 2107; //Whenever a connection to the historical data farm is not being used because there is not an active historical data request, the connection will go inactive in IB Gateway. This does not indicate any connectivity issue or problem with IB Gateway. As soon as a historical data request is made the status will change back to active.
	public static final int WARNING_2108 = 2108; //Whenever a connection to our data farms is not needed, it will become dormant. There is nothing abnormal nor wrong with your client application nor with the TWS. You can safely ignore this message.
	public static final int WARNING_2109 = 2109; //Indicates the outsideRth flag was set for an order for which there is not a regular vs outside regular trading hour distinction.
	public static final int WARNING_2110 = 2110; //Indicates a connectivity problem between TWS or IBG and the IB server. This will usually only occur during the IB nightly server reset; cases at other times indicate a problem in the local ISP connectivity.
	public static final int WARNING_2137 = 2137; //This warning message occurs in TWS version 955 and higher. It occurs when an order will change the position in an account from long to short or from short to long. To bypass the warning, a new feature has been added to IB Gateway 956 (or higher) and TWS 957 (or higher) so that once can go to Global Configuration > Messages and disable the "Cross Side Warning".
	public static final int WARNING_2158 = 2158; //A notification that connection to the Security definition data server is ok. This is a notification and not a true error condition, and is expected on first establishing connection.
	public static final int WARNING_2168 = 2168; //The EtradeOnly IBApi.Order attribute is no longer supported. Error received with TWS versions 983+. Remove attribute to place order.
	public static final int WARNING_2169 = 2169; //The firmQuoteOnly IBApi.Order attribute is no longer supported. Error received with TWS versions 983+. Remove attribute to place order.
	//---

	//------

	public static boolean is_warning(int code_) { return arrays.value_exists(get_all_warnings(), code_); }

	public static int[] populate_all_errors() { return new int[] { ERROR_200, ERROR_202, ERROR_300 }; }

	public static int[] populate_all_warnings()
	{
		return new int[]
		{
			WARNING_2100, WARNING_2101, WARNING_2102, WARNING_2103, WARNING_2104, WARNING_2105, WARNING_2106, WARNING_2107,
			WARNING_2108, WARNING_2109, WARNING_2110, WARNING_2137, WARNING_2158, WARNING_2168, WARNING_2169
		};
	}

	private static int[] get_all_warnings() { return _alls.EXTERNAL_ERRORS_WARNINGS; }
}