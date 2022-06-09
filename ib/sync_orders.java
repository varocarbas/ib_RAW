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

public class sync_orders extends parent_static  
{
	public static final String CANCEL = types.SYNC_ORDERS_CANCEL;
	public static final String PLACE = types.SYNC_ORDERS_PLACE;
	public static final String PLACE_MARKET = types.SYNC_ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = types.SYNC_ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = types.SYNC_ORDERS_PLACE_LIMIT;
	public static final String UPDATE = types.SYNC_ORDERS_UPDATE;
	public static final String UPDATE_START_VALUE = types.SYNC_ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = types.SYNC_ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_STOP_VALUE = types.SYNC_ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = types.SYNC_ORDERS_UPDATE_STOP_MARKET;

	public static final String STATUS = types.SYNC_ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.SYNC_ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.SYNC_ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.SYNC_ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.SYNC_ORDERS_STATUS_INACTIVE;

	public static final double WRONG_VALUE = 0.0;

	public static HashMap<Integer, Long> _cancellations = new HashMap<Integer, Long>();
	public static int _last_id_main = 0;
	public static int _last_id_sec = 0;
	
	private static HashMap<Integer, order> _orders = new HashMap<Integer, order>();
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_SYNC_ORDERS); }
	
	public static boolean place(String type_place_, String symbol_, double quantity_, double stop_, double start_) { return place_update(new order(type_place_, symbol_, quantity_, stop_, start_)); }

	public static boolean update(String type_update_, String symbol_) { return update(type_update_, symbol_, WRONG_VALUE); }
	
	public static boolean update(String type_update_, String symbol_, double val_) 
	{
		String type = check_update(type_update_);
		order order = get_order(symbol_);

		return ((order == null || !strings.is_ok(type)) ? place_update(order, type, val_) : false);
	}
	
	public static void cancel(int id_)
	{
		if (!arrays.value_exists(get_ids(STATUS_SUBMITTED), id_)) return;

		_cancellations = new HashMap<Integer, Long>(common.start_wait(order.get_id_sec(id_), common.start_wait(id_, _cancellations)));
		
		if (sync.cancel_order(id_)) remove_global(id_);			
	}
	
	public static boolean is_cancelling(int id_)
	{
		HashMap<Integer, Long> temp = common.wait_is_over_sync(id_, _cancellations);
		
		boolean output = (temp == null);
		if (!output) _cancellations = new HashMap<Integer, Long>(temp);
		
		return output;
	}

	public static ArrayList<Integer> get_ids(String status_) { return get_ids(status_, null, true); }

	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_) { return get_ids(status_, orders_, false); }
	
	public static ArrayList<Integer> get_ids(String status_, HashMap<Integer, String> orders_, boolean retrieve_)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();

		String status = check_status(status_);
		if (!strings.is_ok(status)) return ids;

		HashMap<Integer, String> orders = orders_;
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
		
		boolean equals = true;
		String[] targets = null; 

		if (status.equals(STATUS_SUBMITTED)) targets = new String[] { orders.STATUS_SUBMITTED, orders.STATUS_PRESUBMITTED };
		else if (status.equals(STATUS_FILLED)) targets = new String[] { orders.STATUS_FILLED };
		else if (status.equals(STATUS_ACTIVE) || status.equals(STATUS_INACTIVE)) 
		{
			equals = status.equals(STATUS_ACTIVE);
			targets = new String[] { orders.STATUS_SUBMITTED, orders.STATUS_PRESUBMITTED, orders.STATUS_FILLED };
		}
		else return false;

		for (String target: targets)
		{
			if (strings.are_equal(status_ib_, target)) return equals;
		}
		
		return false;
	}
	
	static void sync_global(HashMap<Integer, String> orders_) { sync_global(get_ids(STATUS_ACTIVE, orders_, false)); }
	
	private static void sync_global() { sync_global(get_ids(STATUS_ACTIVE)); }

	private static void sync_global(ArrayList<Integer> active_)
	{
		sync_global_orders(active_);
		
		sync_waits();
	}
	
	private static void sync_global_orders(ArrayList<Integer> active_)
	{
		if (!arrays.is_ok(active_))
		{
			_orders = new HashMap<Integer, order>();
			
			return;
		}
			
		ArrayList<Integer> delete = new ArrayList<Integer>();

		for (Entry<Integer, order> item: _orders.entrySet())
		{
			int id = item.getKey();
			
			if (!arrays.value_exists(active_, id)) delete.add(id);
		}

		for (int id: delete) { _orders.remove(id); }
	}

	private static void sync_waits()
	{
		HashMap<Integer, Long> output = new HashMap<Integer, Long>(_cancellations);
		
		for (Entry<Integer, Long> item: _cancellations.entrySet())
		{
			HashMap<Integer, Long> temp = common.wait_is_over_sync(item.getKey(), output);
			if (temp != null) output = new HashMap<Integer, Long>(temp);
		}
		
		_cancellations = new HashMap<Integer, Long>(output);
	}
	
	private static void remove_global(int id_) { arrays.remove_key(_orders, id_); }

	private static order get_order(String symbol_)
	{
		sync_global();
		
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return null;

		for (Entry<Integer, order> item: _orders.entrySet())
		{
			if (symbol.equals(item.getValue().get_symbol())) return (new order(item.getValue()));
		}

		return null;
	}

	public static boolean is_status(String type_) { return strings.is_ok(check_status(type_)); }

	public static boolean is_place(String type_) { return strings.is_ok(check_place(type_)); }
	
	public static boolean is_update(String type_) { return strings.is_ok(check_update(type_)); }
	
	public static boolean is_cancel(String type_) { return strings.is_ok(check_cancel(type_)); }

	public static String check_status(String type_) { return accessory.types.check_type(type_, STATUS); }

	public static String check_place(String type_) { return accessory.types.check_type(type_, PLACE); }
	
	public static String check_update(String type_) { return accessory.types.check_type(type_, UPDATE); }
	
	public static String check_cancel(String type_) { return accessory.types.check_type(type_, CANCEL); }
	
	private static boolean place_update(order order_) { return place_update(order_, null, WRONG_VALUE); }
	
	private static boolean place_update(order order_, String update_type_, double update_val_) 
	{
		if (!order.is_ok(order_)) return false;

		String update_type = check_update(update_type_);
		boolean is_update = strings.is_ok(update_type);
		
		Contract contract = contracts.get_contract(order_.get_symbol());
		if (contract == null) return false;

		_last_id_main = order_.get_id_main();
		_last_id_sec = order_.get_id_sec();
				
		for (int i = 0; i < 2; i++)
		{
			boolean is_main = (i == 0);			
			int id = order_.get_id(is_main);
			
			Order order = (is_update ? orders.get_order_update(order_, update_type_, update_val_, is_main) : orders.get_order_new(order_, is_main));
			if (order == null) return false;

			boolean is_ok = true;
			
			if (is_update) is_ok = sync.update_order(id, contract, order);
			else 
			{
				is_ok = sync.place_order(id, contract, order);
				
				if (!is_ok) remove_global(_last_id_main);
			}

			if (!is_ok)
			{
				_last_id_main = sync.WRONG_ID;
				_last_id_sec = sync.WRONG_ID;
				
				return false;				
			}
		}

		if (!strings.is_ok(update_type)) add_order(_last_id_main, order_);
		else if (update_val_ != WRONG_VALUE)
		{
			if (update_type.equals(UPDATE_START_VALUE)) update_order(_last_id_main, update_val_, true); 
			else if (update_type.equals(UPDATE_STOP_VALUE)) update_order(_last_id_main, update_val_, false); 			
		}
		
		return true;
	}

	private static void add_order(int id_, order order_)
	{
		_orders.put(id_, new order(order_));
		
		sync_global();
	}
	
	private static boolean update_order(int id_, double val_, boolean is_start_)
	{
		if (!_orders.containsKey(id_)) return false;

		order order = new order(_orders.get(id_));

		if (is_start_) order.update_start(val_);
		else order.update_stop(val_);

		_orders.put(id_, order);

		return true;
	}
}