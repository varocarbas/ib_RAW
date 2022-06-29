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
	public static final String CANCEL = types.SYNC_ORDERS_CANCEL;
	public static final String PLACE = types.SYNC_ORDERS_PLACE;
	public static final String PLACE_MARKET = types.SYNC_ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = types.SYNC_ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = types.SYNC_ORDERS_PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = types.SYNC_ORDERS_PLACE_STOP_LIMIT;
	public static final String UPDATE = types.SYNC_ORDERS_UPDATE;
	public static final String UPDATE_START = types.SYNC_ORDERS_UPDATE_START;
	public static final String UPDATE_START_VALUE = types.SYNC_ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = types.SYNC_ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_START2 = types.SYNC_ORDERS_UPDATE_START2;
	public static final String UPDATE_START2_VALUE = types.SYNC_ORDERS_UPDATE_START2_VALUE;
	public static final String UPDATE_STOP = types.SYNC_ORDERS_UPDATE_STOP;
	public static final String UPDATE_STOP_VALUE = types.SYNC_ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = types.SYNC_ORDERS_UPDATE_STOP_MARKET;

	public static final String STATUS = types.SYNC_ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.SYNC_ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.SYNC_ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.SYNC_ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.SYNC_ORDERS_STATUS_INACTIVE;

	public static final double WRONG_VALUE = order.WRONG_VALUE;
	public static final int WRONG_ORDER_ID = order.WRONG_ORDER_ID;
	
	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	
	private static int _last_id_main = 0;
	private static int _last_id_sec = 0;
	
	private static HashMap<Integer, Long> _cancellations = new HashMap<Integer, Long>();
	
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

		_cancellations = new HashMap<Integer, Long>(common_xsync.start_wait(order.get_id_sec(id_), common_xsync.start_wait(id_, _cancellations)));
		
		is_ok = sync.cancel_order(id_);
		if (is_ok) db_ib.orders.delete(id_);	
		
		return is_ok;
	}
	
	public static boolean is_cancelling(int id_)
	{
		HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(id_, _cancellations);
		
		boolean output = (temp == null);
		if (!output) _cancellations = new HashMap<Integer, Long>(temp);
		
		return output;
	}

	public static int get_last_id_main() { return _last_id_main; }
	
	public static int get_last_id_sec() { return _last_id_sec; }
	
	public static ArrayList<Integer> get_ids(String status_) { return get_ids(status_, null, true); }

	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_) { return get_ids(status_, orders_, false); }
	
	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_, boolean retrieve_)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();

		String status = check_status(status_);
		if (!strings.is_ok(status)) return ids;

		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(orders_);
		if (!arrays.is_ok(orders) && retrieve_) orders = sync.get_orders();
		if (!arrays.is_ok(orders)) return ids;
	
		for (Entry<Integer, String> order: orders.entrySet())
		{
			int id = order.getKey();
			String status_ib = order.getValue();

			if (is_status(status_ib, status_)) ids.add(id);
		}
		
		return ids;
	}	

	public static boolean is_status(String status_ib_, String status_)
	{
		if (!orders.status_is_ok(status_ib_)) return false;
	
		String status = check_status(status_);
		if (!strings.is_ok(status)) return false;

		return strings.are_equal(status, get_status(status_ib_, !status_is_generic(status)));
	}
	
	public static void update_status(int id_, String status_ib_) 
	{ 
		String status = get_status(status_ib_, true);
		if (!strings.is_ok(status) || status.equals(STATUS_INACTIVE)) return;
		
		db_ib.orders.update_status(id_, status);	
	}
	
	static String get_status(String status_ib_, boolean be_specific_)
	{
		String status = strings.DEFAULT;
		if (!strings.is_ok(status_ib_)) return status;

		if (status_ib_.equals(orders.STATUS_SUBMITTED) || status_ib_.equals(orders.STATUS_PRESUBMITTED)) status = (be_specific_ ? STATUS_SUBMITTED : STATUS_ACTIVE);
		else if (status_ib_.equals(orders.STATUS_FILLED)) status = (be_specific_ ? STATUS_FILLED : STATUS_ACTIVE);
		else if (!status_ib_.equals(orders.STATUS_PENDING_SUBMIT) && !status_ib_.equals(orders.STATUS_PENDING_CANCEL) && !status_ib_.equals(orders.STATUS_API_CANCELLED)) status = STATUS_INACTIVE;
		
		return status;
	}	
	
	public static String get_type(String input_, boolean is_status_) { return db_ib.orders.status_type_db_to_order(input_, is_status_); }

	public static String get_key(String input_, boolean is_status_) { return db_ib.orders.status_type_order_to_db(input_, is_status_); }
	
	static void sync_all(HashMap<Integer, String> orders_) { sync_all(get_ids(STATUS_ACTIVE, orders_, false)); }
	
	private static boolean status_is_generic(String status_) 
	{ 
		String status = check_status(status_);
		if (!strings.is_ok(status)) return false;
		
		return (status.equals(STATUS_ACTIVE) || status.equals(STATUS_INACTIVE)); 
	}
	
	private static boolean update(String symbol_, String type_, double val_) { return update(get_order(symbol_), check_update(type_), val_); }
	
	private static boolean update(order order_, String type_, double val_) { return ((order_ != null && strings.is_ok(type_)) ? place_update(order_, type_, val_) : false); }
	
	private static boolean place(String type_place_, String symbol_, double quantity_, double stop_, double start_) { return place_update(new order(type_place_, symbol_, quantity_, stop_, start_)); }
	
	private static void sync_all() { sync_all(get_ids(STATUS_ACTIVE)); }

	private static void sync_all(ArrayList<Integer> active_)
	{
		sync_all_orders(active_);
		
		sync_all_waits();
	}
	
	private static void sync_all_orders(ArrayList<Integer> active_) { db_ib.orders.delete_except(arrays.to_array(active_)); }

	private static void sync_all_waits()
	{
		HashMap<Integer, Long> output = new HashMap<Integer, Long>(_cancellations);
		
		for (Entry<Integer, Long> item: _cancellations.entrySet())
		{
			HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(item.getKey(), output);
			if (temp != null) output = new HashMap<Integer, Long>(temp);
		}
		
		_cancellations = new HashMap<Integer, Long>(output);
	}
		
	private static order get_order(int id_)
	{
		sync_all();
		
		return db_ib.orders.get_to_order(id_);
	}
	
	private static order get_order(String symbol_)
	{
		sync_all();
		
		return db_ib.orders.get_to_order(symbol_);
	}

	public static boolean is_status(String type_) { return strings.is_ok(check_status(type_)); }

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

	public static String check_status(String type_) { return accessory.types.check_type(type_, STATUS); }

	public static String check_place(String type_) { return accessory.types.check_type(type_, PLACE); }
	
	public static String check_update(String type_) { return accessory.types.check_type(type_, UPDATE); }
	
	public static String check_cancel(String type_) { return accessory.types.check_type(type_, CANCEL); }
	
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
		
		sync_all();
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