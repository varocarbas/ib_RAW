package ib;

import accessory.strings;
import accessory_ib.types;

public abstract class orders 
{
	public static final String PLACE = types.ORDERS_PLACE;
	public static final String PLACE_MARKET = types.ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = types.ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = types.ORDERS_PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = types.ORDERS_PLACE_STOP_LIMIT;

	public static final String STATUS = types.ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.ORDERS_STATUS_INACTIVE;

	public static final String CANCEL = types.ORDERS_CANCEL;
	public static final String UPDATE = types.ORDERS_UPDATE;
	public static final String UPDATE_START = types.ORDERS_UPDATE_START;
	public static final String UPDATE_START_VALUE = types.ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = types.ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_START2 = types.ORDERS_UPDATE_START2;
	public static final String UPDATE_START2_VALUE = types.ORDERS_UPDATE_START2_VALUE;
	public static final String UPDATE_STOP = types.ORDERS_UPDATE_STOP;
	public static final String UPDATE_STOP_VALUE = types.ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = types.ORDERS_UPDATE_STOP_MARKET;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;

	public static boolean place_market(String symbol_, double quantity_, double stop_) { return sync_orders.place(PLACE_MARKET, symbol_, quantity_, stop_, order.WRONG_VALUE); }

	public static boolean place_stop(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.place(PLACE_STOP, symbol_, quantity_, stop_, start_); }

	public static boolean place_limit(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.place(PLACE_LIMIT, symbol_, quantity_, stop_, start_); }

	public static boolean place_stop_limit(String symbol_, double quantity_, double stop_, double start_limit_, double start_stop_) { return sync_orders.place_update(new order(PLACE_STOP_LIMIT, symbol_, quantity_, stop_, start_limit_, start_stop_)); }

	public static boolean update(int id_, String type_, double val_) { return sync_orders.update(sync_orders.get_order(id_), check_update(type_), val_); }

	public static boolean update_start(String symbol_, double start_) { return sync_orders.update(symbol_, UPDATE_START_VALUE, start_); }

	public static boolean update_start_market(String symbol_) { return sync_orders.update(symbol_, UPDATE_START_MARKET, order.WRONG_VALUE); }

	public static boolean update_stop(String symbol_, double stop_) { return sync_orders.update(symbol_, UPDATE_STOP_VALUE, stop_); }

	public static boolean update_stop_market(String symbol_) { return sync_orders.update(symbol_, UPDATE_STOP_MARKET, order.WRONG_VALUE); }

	public static boolean update_start2(String symbol_, double start2_) { return sync_orders.update(symbol_, UPDATE_START2_VALUE, start2_); }

	public static boolean cancel(int id_) { return sync_orders.cancel(id_); }	

	public static int get_last_id_main() { return sync_orders._last_id_main; }

	public static int get_last_id_sec() { return sync_orders._last_id_sec; }
	
	public static String get_type(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_status_from_key(input_) : db_ib.orders.get_type_place_from_key(input_)); }

	public static String get_key(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_key_from_status(input_) : db_ib.orders.get_key_from_type_place(input_)); }

	public static boolean is_status(String type_) { return strings.is_ok(order.check_status(type_)); }

	public static boolean is_place(String type_) { return strings.is_ok(check_place(type_)); }

	public static boolean is_update(String type_) { return strings.is_ok(check_update(type_)); }

	public static boolean is_update_start(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_START)); }

	public static boolean is_update_stop(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_STOP)); }

	public static boolean is_update_start2(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_START2)); }

	public static boolean is_update_start_start2(String type_) { return (is_update_start(type_) || is_update_start2(type_)); }

	public static boolean is_update_market(String type_) 
	{ 
		String type = check_update(type_);
		if (!strings.is_ok(type)) return false;

		return (type.equals(UPDATE_START_MARKET) || type.equals(UPDATE_STOP_MARKET)); 
	}

	public static boolean is_cancel(String type_) { return strings.is_ok(check_cancel(type_)); }

	public static String check_place(String type_) { return accessory.types.check_type(type_, PLACE); }

	public static String check_update(String type_) { return accessory.types.check_type(type_, UPDATE); }

	public static String check_cancel(String type_) { return accessory.types.check_type(type_, CANCEL); }

	public static boolean is_cancelling(int id_) { return sync_orders.is_cancelling(id_); }

	static void order_status(int order_id_, String status_ib_) 
	{ 
		sync_orders.order_status(order_id_, status_ib_); 
		
		async_orders.order_status(order_id_, status_ib_);
	}
}