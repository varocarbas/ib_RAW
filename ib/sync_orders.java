package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import external_ib.contracts;

abstract class sync_orders extends parent_static  
{
	public static final String PLACE = orders.PLACE;
	public static final String PLACE_MARKET = orders.PLACE_MARKET;
	public static final String PLACE_STOP = orders.PLACE_STOP;
	public static final String PLACE_LIMIT = orders.PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = orders.PLACE_STOP_LIMIT;

	public static final String STATUS = orders.STATUS;
	public static final String STATUS_SUBMITTED = orders.STATUS_SUBMITTED;
	public static final String STATUS_FILLED = orders.STATUS_FILLED;
	public static final String STATUS_ACTIVE = orders.STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = orders.STATUS_INACTIVE;

	public static final String CANCEL = orders.CANCEL;
	public static final String UPDATE = orders.UPDATE;
	public static final String UPDATE_START = orders.UPDATE_START;
	public static final String UPDATE_START_VALUE = orders.UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = orders.UPDATE_START_MARKET;
	public static final String UPDATE_START2 = orders.UPDATE_START2;
	public static final String UPDATE_START2_VALUE = orders.UPDATE_START2_VALUE;
	public static final String UPDATE_STOP = orders.UPDATE_STOP;
	public static final String UPDATE_STOP_VALUE = orders.UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = orders.UPDATE_STOP_MARKET;

	public static final String DEFAULT_STATUS = orders.DEFAULT_STATUS;

	public static int _last_id_main = 0;
	public static int _last_id_sec = 0;

	public static boolean __cancel(int id_)
	{
		boolean is_ok = true; 
	
		__lock();
		
		async_orders._cancellations = arrays.get_new_hashmap_xy(common_xsync.start_wait(order.get_id_sec(id_), common_xsync.start_wait(id_, async_orders._cancellations)));

		__unlock();
		
		is_ok = sync.cancel_order(id_);

		is_ok = sync.wait_orders(CANCEL);
		if (is_ok) db_ib.orders.delete(id_);	
		
		return is_ok;
	}

	public static boolean __is_cancelling(int id_)
	{
		__lock();
		
		HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(id_, async_orders._cancellations);

		boolean output = !arrays.is_ok(temp);
		if (!output) async_orders._cancellations = new HashMap<Integer, Long>(temp);

		__unlock();
		
		return output;
	}

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

	public static void order_status(int order_id_, String status_ib_) { sync.update_orders(order_id_, status_ib_); }
	
	public static void open_order_end() { sync.end_get(); }
	
	public static boolean __update(String symbol_, String type_, double val_) { return __update(__get_order(symbol_), orders.check_update(type_), val_); }

	public static boolean __update(order order_, String type_, double val_) { return ((order_ != null && strings.is_ok(type_)) ? __place_update(order_, type_, val_) : false); }

	public static boolean __place(String type_place_, String symbol_, double quantity_, double stop_, double start_) { return __place(type_place_, symbol_, quantity_, stop_, start_, common.WRONG_PRICE); }

	public static boolean __place(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_) { return (orders.is_inactive(symbol_) ? __place_update(new order(type_place_, symbol_, quantity_, stop_, start_, start2_)) : false); }

	public static order __get_order(int id_main_)
	{
		async_orders.__check_all();

		return db_ib.orders.get_to_order(id_main_, orders.is_quick());
	}

	public static boolean __place_update(order order_) { return __place_update(order_, null, common.WRONG_VALUE); }

	static boolean __place_update(order order_, String update_type_, double update_val_) 
	{
		if (!order.is_ok(order_)) return false;
		
		boolean is_update = orders.is_update(update_type_);

		Contract contract = contracts.get_contract(order_.get_symbol());
		if (contract == null) return false; 

		int main = order_.get_id_main();
		int sec = order_.get_id_sec();

		_last_id_main = main;
		_last_id_sec = sec;

		for (int i = 0; i < 2; i++)
		{
			boolean is_main = (i == 0);	
			if (((orders.is_update_start_start2(update_type_)) && !is_main) || (orders.is_update_stop(update_type_) && is_main)) continue;

			int id = order_.get_id(is_main);

			Order order = (is_update ? external_ib.orders.get_order_update(order_, update_type_, update_val_, is_main) : external_ib.orders.get_order_new(order_, is_main));
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
				_last_id_main = common.WRONG_ORDER_ID;
				_last_id_sec = common.WRONG_ORDER_ID;
				
				return false;				
			}
		}

		if (is_update) update_order(main, update_val_, orders.is_update_market(update_type_), update_type_);
		else
		{
			if (!sync.wait_orders(PLACE)) return false;

			__add_order(order_);
		}

		return true;
	}
	
	private static order __get_order(String symbol_)
	{
		async_orders.__check_all();

		return db_ib.orders.get_to_order(symbol_, orders.is_quick());
	}

	private static void __add_order(order order_)
	{
		db_ib.orders.insert_update(order_, orders.is_quick());

		async_orders.__check_all();
	}

	private static boolean update_order(int id_, double val_, boolean is_market_, String type_)
	{
		order order = db_ib.orders.get_to_order(id_, orders.is_quick());
		if (order == null) return false;

		if (is_market_) order.update_type(ib.order.TYPE_MARKET, orders.is_update_start_start2(type_));
		else if (type_.equals(UPDATE_STOP_VALUE)) order.update_stop(val_);
		else if (type_.equals(UPDATE_START_VALUE)) order.update_start(val_);
		else if (type_.equals(UPDATE_START2_VALUE)) order.update_start2(val_);
		
		return db_ib.orders.update(order, orders.is_quick());
	}
}