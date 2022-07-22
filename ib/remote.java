package ib;

import java.util.HashMap;

import accessory_ib.types;

public abstract class remote 
{
	public static final String FIELD_USER = db_ib.remote.USER;
	public static final String FIELD_SYMBOL = db_ib.remote.SYMBOL;
	public static final String FIELD_ORDER_ID_MAIN = db_ib.remote.ORDER_ID_MAIN;
	public static final String FIELD_ORDER_ID_SEC = db_ib.remote.ORDER_ID_SEC;
	public static final String FIELD_TIME = db_ib.remote.TIME;
	public static final String FIELD_STATUS = db_ib.remote.STATUS;
	public static final String FIELD_STATUS2 = db_ib.remote.STATUS2;
	public static final String FIELD_START = db_ib.remote.START;
	public static final String FIELD_START2 = db_ib.remote.START2;
	public static final String FIELD_STOP = db_ib.remote.STOP;
	public static final String FIELD_IS_MARKET = db_ib.remote.IS_MARKET;
	public static final String FIELD_QUANTITY = db_ib.remote.QUANTITY;
	public static final String FIELD_TYPE_PLACE = db_ib.remote.TYPE_PLACE;
	public static final String FIELD_INVEST_PERC = db_ib.remote.INVEST_PERC;
	public static final String FIELD_ERROR = db_ib.remote.ERROR;
	public static final String FIELD_IS_ACTIVE = db_ib.remote.IS_ACTIVE;
	public static final String FIELD_REQUEST = db_ib.remote.REQUEST;

	public static final String STATUS = types.REMOTE_STATUS;
	public static final String STATUS_ACTIVE = types.REMOTE_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.REMOTE_STATUS_INACTIVE;
	public static final String STATUS_ERROR = types.REMOTE_STATUS_ERROR;

	public static final String STATUS2 = types.REMOTE_STATUS2;
	public static final String STATUS2_PENDING = types.REMOTE_STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = types.REMOTE_STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = types.REMOTE_STATUS2_ERROR;

	public static final int WRONG_REQUEST = 0;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	public static final String DEFAULT_STATUS2 = STATUS2_PENDING;

	public static int __place(String type_place_, String symbol_, double quantity_, double stop_, double start_) 
	{ 
		order order = new order(type_place_, symbol_, quantity_, stop_, start_);
		if (!order.is_ok()) return WRONG_REQUEST;

		int request = db_ib.remote.__add_new(order);
		boolean is_ok = sync_orders.place_update(order);
		
		update_status2(request, is_ok);

		return request;
	}
	
	public static boolean update(int request_, String type_update_) { return update(request_, type_update_, common.WRONG_PRICE); }
	
	public static boolean update(int request_, String type_update_, double val_) 
	{ 
		boolean output = false;
		if ((val_ <= common.WRONG_PRICE && !orders.is_update_market(type_update_)) || !orders.is_update(type_update_)) return output;
		
		int order_id = get_order_id_main(request_);
		if (order_id <= common.WRONG_ORDER_ID) return output;
		
		order order = sync_orders.get_order(order_id);
		if (order == null || !order.is_ok()) return output;
		
		if (type_update_.equals(orders.UPDATE_START_VALUE)) order.update_start(val_);
		else if (type_update_.equals(orders.UPDATE_START_MARKET)) order.update_type_main(ib.order.TYPE_MARKET);
		else if (type_update_.equals(orders.UPDATE_START2_VALUE)) order.update_start2(val_);
		else if (type_update_.equals(orders.UPDATE_STOP_VALUE)) order.update_stop(val_);
		else if (type_update_.equals(orders.UPDATE_STOP_MARKET)) order.update_type_sec(ib.order.TYPE_MARKET);
				
		db_ib.remote.update(request_, order);
		
		boolean is_ok = sync_orders.place_update(order, type_update_, val_);

		update_status2(request_, is_ok);
		
		return is_ok;
	}

	public static boolean cancel(int request_) 
	{
		int order_id = get_order_id_main(request_);
		if (order_id <= common.WRONG_ORDER_ID) return false;
		
		boolean is_ok = false;
		
		if (!db_ib.orders.exists_active(order_id, true)) is_ok = true;
		else is_ok = sync_orders.cancel(order_id);

		if (is_ok) db_ib.remote.deactivate_order_id(order_id);
		
		update_status2(request_, is_ok);
		
		return is_ok;
	}
	
	public static HashMap<String, String> get(int request_) { return db_ib.remote.get(request_); }
		
	private static void update_status2(int request_, boolean is_ok_) { db_ib.remote.update_status2(request_, (is_ok_ ? STATUS2_EXECUTED : STATUS2_ERROR)); }

	private static int get_order_id_main(int request_) { return (db_ib.remote.is_active(request_) ? db_ib.remote.get_order_id(request_) : common.WRONG_ORDER_ID); }
}