package external_ib;

import com.ib.client.Order;

import accessory.arrays;
import accessory.strings;
import accessory_ib._alls;
import ib.order;
import ib.sync_orders;

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
	
	public static Order get_order_new(order order_, boolean is_main_) { return get_order(order_, is_main_, null, sync_orders.WRONG_VALUE, false); }
	
	public static Order get_order_update(order order_, String update_type_, double update_val_, boolean is_main_) { return get_order(order_, is_main_, update_type_, update_val_, true); }
	
	public static boolean exec_side_is_ok(String side_) { return arrays.value_exists(get_all_exec_sides(), side_); }

	public static boolean action_is_ok(String action_) { return arrays.value_exists(get_all_actions(), action_); }

	public static boolean type_is_ok(String type_) { return arrays.value_exists(get_all_types(), type_); }

	public static boolean tif_is_ok(String tif_) { return arrays.value_exists(get_all_tifs(), tif_); }

	public static boolean status_is_ok(String status_) { return arrays.value_exists(get_all_statuses(), status_); }
	
	public static String[] populate_all_exec_sides() { return new String[] { EXEC_SIDE_BOUGHT, EXEC_SIDE_SOLD }; }
	
	public static String[] populate_all_actions() { return new String[] { ACTION_BUY, ACTION_SELL }; }
	
	public static String[] populate_all_types() { return new String[] { TYPE_MARKET, TYPE_STOP, TYPE_LIMIT }; }
	
	public static String[] populate_all_tifs() { return new String[] { TIF_GTC }; }
	
	public static String[] populate_all_statuses() { return new String[] { STATUS_PENDING_SUBMIT, STATUS_PENDING_CANCEL, STATUS_PRESUBMITTED, STATUS_SUBMITTED, STATUS_API_CANCELLED, STATUS_CANCELLED, STATUS_FILLED, STATUS_INACTIVE }; }

	private static String[] get_all_exec_sides() { return _alls.EXTERNAL_ORDERS_EXEC_SIDES; }
	
	private static String[] get_all_actions() { return _alls.EXTERNAL_ORDERS_ACTIONS; }
	
	private static String[] get_all_types() { return _alls.EXTERNAL_ORDERS_TYPES; }
	
	private static String[] get_all_tifs() { return _alls.EXTERNAL_ORDERS_TIFS; }
	
	private static String[] get_all_statuses() { return _alls.EXTERNAL_ORDERS_STATUSES; }
	
	private static Order get_order(order order_, boolean is_main_, String update_type_, double update_val_, boolean is_update_)
	{
		Order output = null;
		if (!order.is_ok(order_)) return output;
		
		int id = order_.get_id(is_main_);
		boolean is_market = order_.is_market();
		
		String tif = order.get_tif();
		if (!strings.is_ok(tif)) return output;

		double quantity = order_.get_quantity();
		if (quantity <= 0) return output;
		
		output = new Order();
		
		output.orderId(id);
		output.tif(tif);	
		output.totalQuantity(quantity);
		if (!is_main_) output.parentId(order_.get_id_main());
		
		double val = order_.get_val(is_main_);

		if (is_update_)
		{
			if (update_val_ == sync_orders.WRONG_VALUE) return null;
			
			boolean start_market = strings.are_equal(update_type_, sync_orders.UPDATE_START_MARKET);
			boolean stop_market = strings.are_equal(update_type_, sync_orders.UPDATE_STOP_MARKET);
			boolean start_value = strings.are_equal(update_type_, sync_orders.UPDATE_START_VALUE);
			boolean stop_value = strings.are_equal(update_type_, sync_orders.UPDATE_STOP_VALUE);
			
			if (start_market || start_value)
			{
				if (is_main_) 
				{
					val = update_val_;
					is_market = start_market;
				}
			}
			else if (stop_market || stop_value)
			{
				if (!is_main_)
				{
					val = update_val_;
					is_market = stop_market;
				}
			}
		}

		String action = ACTION_BUY;
		if (!is_main_ || (is_update_ && is_market)) action = ACTION_SELL;
		output.action(action);
		
		if (is_market && (is_main_ || is_update_)) output.orderType(TYPE_MARKET);
		else
		{
			if (val == sync_orders.WRONG_VALUE) return null;
			
			String type = TYPE_STOP;
			if (is_main_ && order_.is_limit()) type = TYPE_LIMIT;
			output.orderType(type);	
			
			if (type.equals(TYPE_STOP)) output.auxPrice(val);	
			else if (type.equals(TYPE_LIMIT)) output.lmtPrice(val);	
		}

		output.transmit(!is_main_);

		return output;
	}
}