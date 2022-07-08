package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;
import external_ib.contracts;
import external_ib.orders;

public abstract class sync_orders extends parent_static  
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

	public static final double WRONG_VALUE = order.WRONG_VALUE;
	public static final int WRONG_ORDER_ID = order.WRONG_ORDER_ID;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;

	private static int _last_id_main = 0;
	private static int _last_id_sec = 0;

	public static boolean place_market(String symbol_, double quantity_, double stop_) { return place(PLACE_MARKET, symbol_, quantity_, stop_, WRONG_VALUE); }

	public static boolean place_stop(String symbol_, double quantity_, double stop_, double start_) { return place(PLACE_STOP, symbol_, quantity_, stop_, start_); }

	public static boolean place_limit(String symbol_, double quantity_, double stop_, double start_) { return place(PLACE_LIMIT, symbol_, quantity_, stop_, start_); }

	public static boolean place_stop_limit(String symbol_, double quantity_, double stop_, double start_limit_, double start_stop_) { return place_update(new order(PLACE_STOP_LIMIT, symbol_, quantity_, stop_, start_limit_, start_stop_)); }

	public static boolean update(int id_, String type_, double val_) { return update(get_order(id_), check_update(type_), val_); }

	public static boolean update_start(String symbol_, double start_) { return update(symbol_, UPDATE_START_VALUE, start_); }

	public static boolean update_start_market(String symbol_) { return update(symbol_, UPDATE_START_MARKET, WRONG_VALUE); }

	public static boolean update_stop(String symbol_, double stop_) { return update(symbol_, UPDATE_STOP_VALUE, stop_); }

	public static boolean update_stop_market(String symbol_) { return update(symbol_, UPDATE_STOP_MARKET, WRONG_VALUE); }

	public static boolean update_start2(String symbol_, double start2_) { return update(symbol_, UPDATE_START2_VALUE, start2_); }

	public static boolean cancel(int id_)
	{
		boolean is_ok = true; 
		if (!arrays.value_exists(get_ids(STATUS_SUBMITTED), id_)) return is_ok;

		async_orders._cancellations = new HashMap<Integer, Long>(common_xsync.start_wait(order.get_id_sec(id_), common_xsync.start_wait(id_, async_orders._cancellations)));

		is_ok = sync.cancel_order(id_);
		if (is_ok) db_ib.orders.delete(id_);	

		return is_ok;
	}

	public static boolean is_cancelling(int id_)
	{
		HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(id_, async_orders._cancellations);

		boolean output = (temp == null);
		if (!output) async_orders._cancellations = new HashMap<Integer, Long>(temp);

		return output;
	}

	public static int get_last_id_main() { return _last_id_main; }

	public static int get_last_id_sec() { return _last_id_sec; }

	public static ArrayList<Integer> get_ids(String status_) { return get_ids(status_, null, true); }

	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_) { return get_ids(status_, orders_, false); }

	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_, boolean retrieve_)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();

		String status = order.check_status(status_);
		if (!strings.is_ok(status)) return ids;

		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(orders_);
		if (!arrays.is_ok(orders) && retrieve_) orders = sync.get_orders();
		if (!arrays.is_ok(orders)) return ids;

		for (Entry<Integer, String> order: orders.entrySet())
		{
			int id = order.getKey();
			String status_ib = order.getValue();

			if (ib.order.is_status(status_ib, status_)) ids.add(id);
		}

		return ids;
	}	

	public static String get_type(String input_, boolean is_status_) { return db_ib.orders.status_type_db_to_order(input_, is_status_); }

	public static String get_key(String input_, boolean is_status_) { return db_ib.orders.status_type_order_to_db(input_, is_status_); }

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

	static void order_status(int order_id_, String status_ib_) 
	{	
		if (!sync.is_ok()) return;
		
		sync.update_orders(order_id_);
		
		sync.update_orders(status_ib_);
	}

	private static boolean update(String symbol_, String type_, double val_) { return update(get_order(symbol_), check_update(type_), val_); }

	private static boolean update(order order_, String type_, double val_) { return ((order_ != null && strings.is_ok(type_)) ? place_update(order_, type_, val_) : false); }

	private static boolean place(String type_place_, String symbol_, double quantity_, double stop_, double start_) { return place_update(new order(type_place_, symbol_, quantity_, stop_, start_)); }

	private static order get_order(int id_)
	{
		async_orders.perform_regular_checks();

		return db_ib.orders.get_to_order(id_);
	}

	private static order get_order(String symbol_)
	{
		async_orders.perform_regular_checks();

		return db_ib.orders.get_to_order(symbol_);
	}

	private static boolean place_update(order order_) { return place_update(order_, null, WRONG_VALUE); }

	private static boolean place_update(order order_, String update_type_, double update_val_) 
	{
		if (!order.is_ok(order_)) return false;

		boolean is_update = is_update(update_type_);

		Contract contract = contracts.get_contract(order_.get_symbol());
		if (contract == null) return false;

		int main = order_.get_id_main();
		int sec = order_.get_id_sec();

		_last_id_main = main;
		_last_id_sec = sec;

		for (int i = 0; i < 2; i++)
		{
			boolean is_main = (i == 0);	
			if (((is_update_start_start2(update_type_)) && !is_main) || (is_update_stop(update_type_) && is_main)) continue;

			int id = order_.get_id(is_main);

			Order order = (is_update ? orders.get_order_update(order_, update_type_, update_val_, is_main) : orders.get_order_new(order_, is_main));
			if (order == null) return false;

			boolean is_ok = true;

			if (is_update) is_ok = sync.update_order(id, contract, order);
			else 
			{
				is_ok = sync.place_order(id, contract, order);

				if (!is_ok) 
				{
					db_ib.orders.delete(main);

					return false;
				}
			}

			if (!is_ok)
			{
				_last_id_main = WRONG_ORDER_ID;
				_last_id_sec = WRONG_ORDER_ID;

				return false;				
			}
		}

		if (is_update) update_order(main, update_val_, is_update_market(update_type_), is_update_start_start2(update_type_));
		else
		{
			if (!sync.wait_orders(PLACE)) return false;

			add_order(order_);
		}

		return true;
	}

	private static void add_order(order order_)
	{
		db_ib.orders.insert(order_);

		async_orders.perform_regular_checks();
	}

	private static boolean update_order(int id_, double val_, boolean is_market_, boolean is_main_)
	{
		order order = db_ib.orders.get_to_order(id_);
		if (order == null) return false;

		if (is_market_) order.update_type(ib.order.TYPE_MARKET, is_main_);
		else order.update_val(val_, is_main_);

		return db_ib.orders.update(order);
	}
}