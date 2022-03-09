package external_ib;

import accessory.arrays;
import accessory_ib.ini;

public class constants 
{
	//--- To be synced with the corresponding get_all methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/tick_types.html.
	public static final int TICK_BID_SIZE = 0;
	public static final int TICK_BID = 1;
	public static final int TICK_ASK = 2;
	public static final int TICK_ASK_SIZE = 3;
	public static final int TICK_LAST = 4;
	public static final int TICK_LAST_SIZE = 5;
	public static final int TICK_HIGH = 6;
	public static final int TICK_LOW = 7;
	public static final int TICK_VOLUME = 8;
	public static final int TICK_CLOSE = 9;
	public static final int TICK_OPEN = 14;
	public static final int TICK_HALTED = 49;

	public static final int HALTED_NA = -1;
	public static final int HALTED_NOT = 0;
	public static final int HALTED_GENERAL = 1;	
	public static final int HALTED_VOLATILITY = 2;	
	//------

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/market_data_type.html.
	public static final int DATA_LIVE = 1;
	public static final int DATA_FROZEN = 2;
	public static final int DATA_DELAYED = 3;
	public static final int DATA_DELAYED_FROZEN = 4;
	//------

	//--- To be synced with the execution.side values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Execution.html).
	public static final String EXEC_SIDE_BOT = "bot";
	public static final String EXEC_SIDE_SLD = "sld";
	//------

	//--- To be synced with the order.action values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Order.html).
	public static final String ORDER_ACTION_BUY = "buy";
	public static final String ORDER_ACTION_SELL = "sell";

	public static final String ORDER_TYPE_MARKET = "MKT";
	public static final String ORDER_TYPE_STOP = "STP";
	public static final String ORDER_TYPE_LIMIT = "LMT";

	public static final String ORDER_TIF_GTC = "GTC";
	//------

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
	//------
	//------------

	static { ini.load(); }
	
	public static boolean is_warning(int code_)
	{
		return arrays.value_exists(get_all_warnings(), code_);
	}

	public static boolean tick_is_ok(int tick_)
	{
		return arrays.value_exists(get_all_tick(), tick_);
	}

	public static boolean halt_is_ok(int halt_)
	{
		return arrays.value_exists(get_all_halted(), halt_);
	}

	public static boolean data_is_ok(int data_)
	{
		return arrays.value_exists(get_all_data(), data_);
	}

	private static Integer[] get_all_tick()
	{
		return new Integer[]
		{
			TICK_BID_SIZE, TICK_BID, TICK_ASK, TICK_ASK_SIZE, 
			TICK_LAST, TICK_LAST_SIZE, TICK_HIGH, TICK_LOW, 
			TICK_VOLUME, TICK_CLOSE, TICK_OPEN, TICK_HALTED	
		};
	}

	private static Integer[] get_all_halted()
	{
		return new Integer[]
		{
			HALTED_NA, HALTED_NOT, HALTED_GENERAL, HALTED_VOLATILITY	
		};
	}

	private static Integer[] get_all_data()
	{
		return new Integer[]
		{
			DATA_LIVE, DATA_FROZEN, DATA_DELAYED, DATA_DELAYED_FROZEN	
		};
	}

	private static Integer[] get_all_warnings()
	{
		return new Integer[]
		{
			WARNING_2100, WARNING_2101, WARNING_2102, WARNING_2103,
			WARNING_2104, WARNING_2105, WARNING_2106, WARNING_2107,
			WARNING_2108, WARNING_2109, WARNING_2110, WARNING_2137,
			WARNING_2158, WARNING_2168, WARNING_2169
		};
	}
}