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

	public static final String STATUS = types.ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.ORDERS_STATUS_INACTIVE;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;

	public static boolean __place_market(String symbol_, double quantity_, double stop_) { return sync_orders.__place(PLACE_MARKET, symbol_, quantity_, stop_, common.WRONG_PRICE); }

	public static boolean __place_stop(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.__place(PLACE_STOP, symbol_, quantity_, stop_, start_); }

	public static boolean __place_limit(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.__place(PLACE_LIMIT, symbol_, quantity_, stop_, start_); }

	public static boolean __place_stop_limit(String symbol_, double quantity_, double stop_, double start_limit_, double start_stop_) { return sync_orders.__place(PLACE_STOP_LIMIT, symbol_, quantity_, stop_, start_limit_, start_stop_); }

	public static boolean __update(int id_, String type_, double val_) { return sync_orders.__update(sync_orders.get_order(id_), check_update(type_), val_); }

	public static boolean __update_start(String symbol_, double start_) { return sync_orders.__update(symbol_, UPDATE_START_VALUE, start_); }

	public static boolean __update_start_market(String symbol_) { return sync_orders.__update(symbol_, UPDATE_START_MARKET, common.WRONG_PRICE); }

	public static boolean __update_stop(String symbol_, double stop_) { return sync_orders.__update(symbol_, UPDATE_STOP_VALUE, stop_); }

	public static boolean __update_stop_market(String symbol_) { return sync_orders.__update(symbol_, UPDATE_STOP_MARKET, common.WRONG_PRICE); }

	public static boolean __update_start2(String symbol_, double start2_) { return sync_orders.__update(symbol_, UPDATE_START2_VALUE, start2_); }

	public static boolean __cancel(int order_id_main_)
	{ 
		if (order_is_inactive(order_id_main_)) return true;
		
		return (order_is_filled(order_id_main_) ? false : sync_orders.__cancel(order_id_main_)); 
	}	

	public static boolean cancel_is_ok(int order_id_main_) { return (order_is_active(order_id_main_) && !order_is_filled(order_id_main_)); }

	public static boolean order_is_submitted(int order_id_main_) { return order_is_common(order_id_main_, STATUS_SUBMITTED); }

	public static boolean order_is_submitted(String symbol_) { return order_is_common(symbol_, STATUS_SUBMITTED); }

	public static boolean order_is_filled(int order_id_main_) { return order_is_common(order_id_main_, STATUS_FILLED); }	

	public static boolean order_is_filled(String symbol_) { return order_is_common(symbol_, orders.STATUS_FILLED); }	
	
	public static boolean order_is_inactive(int order_id_main_) { return order_is_inactive(order_id_main_, true); }	

	public static boolean order_is_inactive(int order_id_main_, boolean check_exists_) { return ((!check_exists_ || (check_exists_ && !db_ib.orders.exists(order_id_main_, true))) || order_is_common(order_id_main_, STATUS_INACTIVE)); }	
	
	public static boolean order_is_active(int order_id_main_) { return !order_is_inactive(order_id_main_); }	
	
	public static boolean order_is_active(String symbol_) { return !order_is_inactive(symbol_); }	

	public static boolean order_is_inactive(String symbol_) { return order_is_inactive(symbol_, true); }	
	
	public static boolean order_is_inactive(String symbol_, boolean check_exists_) { return ((!check_exists_ || (check_exists_ && !db_ib.orders.exists(symbol_))) || order_is_common(symbol_, STATUS_INACTIVE)); }	

	public static int get_last_id_main() { return sync_orders._last_id_main; }

	public static int get_last_id_sec() { return sync_orders._last_id_sec; }
	
	public static String get_type(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_status_from_key(input_) : db_ib.orders.get_type_place_from_key(input_)); }

	public static String get_key(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_key_from_status(input_) : db_ib.orders.get_key_from_type_place(input_)); }

	public static boolean is_status(String type_) { return strings.is_ok(_order.check_status(type_)); }
	
	public static boolean update_is_ok(String type_, double stop_, double start_) { return update_is_ok(type_, stop_, start_, common.WRONG_PRICE); }

	public static boolean update_is_ok(String type_, double stop_, double start_, double start2_) { return (is_update(type_) && (is_update_market(type_) || (common.price_is_ok(stop_) && (common.price_is_ok(start_) || common.price_is_ok(start2_))))); }
	
	public static double get_update_val(String type_, double stop_, double start_, double start2_) 
	{
		double val = common.WRONG_PRICE;
		if (!update_is_ok(type_, stop_, start_, start2_)) return val;
	
		if (orders.is_update_stop(type_)) val = stop_;
		else if (orders.is_update_start(type_)) val = start_;
		else if (orders.is_update_start2(type_)) val = start2_;

		return val; 
	}

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

	public static double adapt_quantity(double quantity_) { return _order.adapt_quantity(quantity_); }
	
	static void order_status(int order_id_, String status_ib_) 
	{ 
		sync_orders.order_status(order_id_, status_ib_); 
		
		async_orders.order_status(order_id_, status_ib_);
	}
	
	private static boolean order_is_common(int order_id_main_, String target_) { return strings.are_equal(db_ib.orders.get_status(order_id_main_), target_); }	
	
	private static boolean order_is_common(String symbol_, String target_) { return strings.are_equal(db_ib.orders.get_status(symbol_), target_); }	
}