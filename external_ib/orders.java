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

	//--- To be synced with the status values in wrapper.orderStatus (https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html#a27ec36f07dff982f50968c8a8887d676).
	public static final String STATUS_PENDING_SUBMIT = "PendingSubmit"; //Indicates that you have transmitted the order, but have not yet received confirmation that it has been accepted by the order destination.
	public static final String STATUS_PENDING_CANCEL = "PendingCancel"; //Indicates that you have sent a request to cancel the order but have not yet received cancel confirmation from the order destination. At this point, your order is not confirmed canceled. It is not guaranteed that the cancellation will be successful.
	public static final String STATUS_PRESUBMITTED = "PreSubmitted"; //Indicates that a simulated order type has been accepted by the IB system and that this order has yet to be elected. The order is held in the IB system until the election criteria are met. At that time the order is transmitted to the order destination as specified.
	public static final String STATUS_SUBMITTED = "Submitted"; //Indicates that your order has been accepted by the system.
	public static final String STATUS_API_CANCELLED = "ApiCancelled"; //After an order has been submitted and before it has been acknowledged, an API client client can request its cancelation, producing this state.
	public static final String STATUS_CANCELLED = "Cancelled"; //Indicates that the balance of your order has been confirmed canceled by the IB system. This could occur unexpectedly when IB or the destination has rejected your order.
	public static final String STATUS_FILLED = "Filled"; //Indicates that the order has been completely filled. Market orders executions will not always trigger a Filled status.
	public static final String STATUS_INACTIVE = "Inactive"; //Indicates that the order was received by the system but is no longer active because it was rejected or canceled.
	//---
	
	public static boolean exec_side_is_ok(String side_) { return arrays.value_exists(get_all_exec_sides(), side_); }

	public static boolean action_is_ok(String action_) { return arrays.value_exists(get_all_actions(), action_); }

	public static boolean type_is_ok(String type_) { return arrays.value_exists(get_all_types(), type_); }

	public static boolean tif_is_ok(String tif_) { return arrays.value_exists(get_all_tifs(), tif_); }

	public static boolean status_is_ok(String status_) { return arrays.value_exists(get_all_status(), status_); }
	
	private static String[] get_all_exec_sides() { return new String[] { EXEC_SIDE_BOUGHT, EXEC_SIDE_SOLD }; }
	
	private static String[] get_all_actions() { return new String[] { ACTION_BUY, ACTION_SELL }; }
	
	private static String[] get_all_types() { return new String[] { TYPE_MARKET, TYPE_STOP, TYPE_LIMIT }; }
	
	private static String[] get_all_tifs() { return new String[] { TIF_GTC }; }
	
	private static String[] get_all_status() { return new String[] { STATUS_PENDING_SUBMIT, STATUS_PENDING_CANCEL, STATUS_PRESUBMITTED, STATUS_SUBMITTED, STATUS_API_CANCELLED, STATUS_CANCELLED, STATUS_FILLED, STATUS_INACTIVE }; }
}