package external_ib;

import com.ib.client.Order;

import accessory.arrays;
import accessory.strings;
import accessory_ib._alls;
import ib.order;
import ib.sync_orders;

public abstract class orders 
{
	//--- To be synced with the execution.side values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Execution.html).
	public static final String EXEC_SIDE_BOUGHT = "bot";
	public static final String EXEC_SIDE_SOLD = "sld";
	//---

	//--- To be synced with the order.action values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Order.html).
	public static final String ACTION_BUY = "buy";
	public static final String ACTION_SELL = "sell";
	//---
	
	//--- To be synced with the order.OrderType (samples: https://interactivebrokers.github.io/tws-api/basic_orders.html).
	public static final String TYPE_MARKET = "MKT";
	public static final String TYPE_STOP = "STP";
	public static final String TYPE_LIMIT = "LMT";
	public static final String TYPE_STOP_LIMIT = "STP LMT";
	//---
	
	//--- To be synced with the order.tif values (https://interactivebrokers.github.io/tws-api/classIBApi_1_1Order.html#a6b82712a718127487631727db08f67d4).
	public static final String TIF_DAY = "DAY"; //Valid for the day only.
	public static final String TIF_GTC = "GTC"; //Good until canceled. The order will continue to work within the system and in the marketplace until it executes or is canceled. GTC orders will be automatically be cancelled under the following conditions: [...].
	public static final String TIF_IOC = "IOC"; //Immediate or Cancel. Any portion that is not filled as soon as it becomes available in the market is canceled.
	public static final String TIF_GTD = "GTD"; //Good until Date. It will remain working within the system and in the marketplace until it executes or until the close of the market on the date specified
	public static final String TIF_OPG = "OPG"; //Use OPG to send a market-on-open (MOO) or limit-on-open (LOO) order.
	public static final String TIF_FOK = "FOK"; //If the entire Fill-or-Kill order does not execute as soon as it becomes available, the entire order is canceled.
	public static final String TIF_DTC = "DTC"; //Day until Canceled.	
	//---

	//--- To be synced with the status values in wrapper.orderStatus (https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html#a27ec36f07dff982f50968c8a8887d676).
	public static final String STATUS_IB_PENDING_SUBMIT = "PendingSubmit"; //Indicates that you have transmitted the order, but have not yet received confirmation that it has been accepted by the order destination.
	public static final String STATUS_IB_PENDING_CANCEL = "PendingCancel"; //Indicates that you have sent a request to cancel the order but have not yet received cancel confirmation from the order destination. At this point, your order is not confirmed canceled. It is not guaranteed that the cancellation will be successful.
	public static final String STATUS_IB_PRESUBMITTED = "PreSubmitted"; //Indicates that a simulated order type has been accepted by the IB system and that this order has yet to be elected. The order is held in the IB system until the election criteria are met. At that time the order is transmitted to the order destination as specified.
	public static final String STATUS_IB_SUBMITTED = "Submitted"; //Indicates that your order has been accepted by the system.
	public static final String STATUS_IB_API_CANCELLED = "ApiCancelled"; //After an order has been submitted and before it has been acknowledged, an API client client can request its cancelation, producing this state.
	public static final String STATUS_IB_CANCELLED = "Cancelled"; //Indicates that the balance of your order has been confirmed canceled by the IB system. This could occur unexpectedly when IB or the destination has rejected your order.
	public static final String STATUS_IB_FILLED = "Filled"; //Indicates that the order has been completely filled. Market orders executions will not always trigger a Filled status.
	public static final String STATUS_IB_INACTIVE = "Inactive"; //Indicates that the order was received by the system but is no longer active because it was rejected or canceled.
	//---

	public static final String DEFAULT_TIF = orders.TIF_GTC; 
	public static final boolean DEFAULT_QUANTITIES_INT = true;

	public static Order get_order_new(order order_, boolean is_main_) { return get_order(order_, is_main_, null, sync_orders.WRONG_VALUE, false); }
	
	public static Order get_order_update(order order_, String update_type_, double update_val_, boolean is_main_) { return get_order(order_, is_main_, update_type_, update_val_, true); }
	
	public static boolean exec_side_is_ok(String side_) { return arrays.value_exists(get_all_exec_sides(), side_); }

	public static boolean action_is_ok(String action_) { return arrays.value_exists(get_all_actions(), action_); }

	public static boolean type_is_ok(String type_) { return arrays.value_exists(get_all_types(), type_); }

	public static boolean tif_is_ok(String tif_) { return arrays.value_exists(get_all_tifs(), tif_); }

	public static boolean status_is_ok(String status_) { return arrays.value_exists(get_all_statuses(), status_); }
	
	public static String[] populate_all_exec_sides() { return new String[] { EXEC_SIDE_BOUGHT, EXEC_SIDE_SOLD }; }
	
	public static String[] populate_all_actions() { return new String[] { ACTION_BUY, ACTION_SELL }; }
	
	public static String[] populate_all_types() { return new String[] { TYPE_MARKET, TYPE_STOP, TYPE_LIMIT, TYPE_STOP_LIMIT }; }
	
	public static String[] populate_all_tifs() { return new String[] { TIF_DAY, TIF_GTC, TIF_IOC, TIF_GTD, TIF_OPG, TIF_FOK, TIF_DTC }; }	
	
	public static String[] populate_all_statuses() { return new String[] { STATUS_IB_PENDING_SUBMIT, STATUS_IB_PENDING_CANCEL, STATUS_IB_PRESUBMITTED, STATUS_IB_SUBMITTED, STATUS_IB_API_CANCELLED, STATUS_IB_CANCELLED, STATUS_IB_FILLED, STATUS_IB_INACTIVE }; }

	private static String[] get_all_exec_sides() { return _alls.EXTERNAL_ORDERS_EXEC_SIDES; }
	
	private static String[] get_all_actions() { return _alls.EXTERNAL_ORDERS_ACTIONS; }
	
	private static String[] get_all_types() { return _alls.EXTERNAL_ORDERS_TYPES; }
	
	private static String[] get_all_tifs() { return _alls.EXTERNAL_ORDERS_TIFS; }
	
	private static String[] get_all_statuses() { return _alls.EXTERNAL_ORDERS_STATUSES; }
	
	private static Order get_order(order order_, boolean is_main_, String update_type_, double update_val_, boolean is_update_)
	{
		if (!order.is_ok(order_)) return null;

		String type = get_type(order_, is_main_, update_type_, is_update_);
		if (!strings.is_ok(type)) return null;
		
		boolean is_market = type.equals(TYPE_MARKET);
		int id = order_.get_id(is_main_);
		
		String tif = order.get_tif();
		if (!strings.is_ok(tif)) return null;

		double quantity = order_.get_quantity();
		if (quantity <= 0) return null;
		
		Order output = new Order();

		output.orderType(type);
		output.orderId(id);
		output.tif(tif);	
		output.totalQuantity(quantity);
		if (!is_main_) output.parentId(order_.get_id_main());
		
		double val = order_.get_val(is_main_);
		double val2 = order_.get_start2();
		
		if (is_update_) 
		{
			if (type.equals(TYPE_STOP_LIMIT) && update_type_.equals(sync_orders.UPDATE_START2_VALUE)) val2 = update_val_;
			else val = update_val_;
		}
		if ((val <= sync_orders.WRONG_VALUE && !is_market) || (val2 <= sync_orders.WRONG_VALUE && type.equals(TYPE_STOP_LIMIT))) return null;
		
		output.action((is_main_ ? ACTION_BUY : ACTION_SELL));		

		if (type.equals(TYPE_STOP)) output.auxPrice(val);	
		else if (type.equals(TYPE_LIMIT) || type.equals(TYPE_STOP_LIMIT)) 
		{
			output.lmtPrice(val);	
			if (type.equals(TYPE_STOP_LIMIT)) output.auxPrice(val2);
		}

		boolean transmit = (!is_main_ || (is_main_ && sync_orders.is_update_start_start2(update_type_)));

		output.transmit(transmit);

		return output;
	}
	
	private static String get_type(order order_, boolean is_main_, String update_type_, boolean is_update_) 
	{ 
		String type = null;
		
		if (is_update_)
		{
			type = order_.get_type(is_main_);
			
			if (type.equals(TYPE_MARKET) || (update_type_.equals(sync_orders.UPDATE_START2_VALUE) && !type.equals(TYPE_STOP_LIMIT))) type = null;
			else if (sync_orders.is_update_market(update_type_)) type = TYPE_MARKET;
		}
		else type = order_.get_type(is_main_);  
		
		return type;
	}
}