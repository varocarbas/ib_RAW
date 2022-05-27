package external_ib;

import accessory.arrays;

public class orders 
{
	//--- To be synced with the execution.side values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Execution.html).
	public static final String EXEC_SIDE_BOUGHT = "bot";
	public static final String EXEC_SIDE_SOLD = "sld";
	//---

	//--- To be synced with the order.action values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Order.html).
	public static final String ACTION_BUY = "buy";
	public static final String ACTION_SELL = "sell";

	public static final String TYPE_MARKET = "MKT";
	public static final String TYPE_STOP = "STP";
	public static final String TYPE_LIMIT = "LMT";

	public static final String TIF_GTC = "GTC";
	//---

	public static boolean exec_side_is_ok(String side_) { return arrays.value_exists(get_all_exec_sides(), side_); }

	public static boolean action_is_ok(String action_) { return arrays.value_exists(get_all_actions(), action_); }

	public static boolean type_is_ok(String type_) { return arrays.value_exists(get_all_types(), type_); }

	public static boolean tif_is_ok(String tif_) { return arrays.value_exists(get_all_tifs(), tif_); }
		
	private static String[] get_all_exec_sides() { return new String[] { EXEC_SIDE_BOUGHT, EXEC_SIDE_SOLD }; }
	
	private static String[] get_all_actions() { return new String[] { ACTION_BUY, ACTION_SELL }; }
	
	private static String[] get_all_types() { return new String[] { TYPE_MARKET, TYPE_STOP, TYPE_LIMIT }; }
	
	private static String[] get_all_tifs() { return new String[] { TIF_GTC }; }
}