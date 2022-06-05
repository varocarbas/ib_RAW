package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.types;

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
	
	public static final String ACTION_BUY = orders.ACTION_BUY;
	public static final String ACTION_SELL = orders.ACTION_SELL;
	
	public static final String TYPE2_MARKET = orders.TYPE_MARKET;
	public static final String TYPE2_STOP = orders.TYPE_STOP;
	public static final String TYPE2_LIMIT = orders.TYPE_LIMIT;

	public static final double WRONG_VALUE = 0.0;

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
		if (!arrays.value_exists(get_ids(STATUS_SUBMITTED), id_) || !sync.cancel_order(id_)) return;
		
		remove_global(id_);
	}

	public static int get_id_sec(int id_main_) { return (id_main_ + 1); }

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
		
		Contract contract = common.get_contract(order_.get_symbol());
		if (contract == null) return false;
				
		int id = order_.get_id_main();
		int parent = id;

		_last_id_main = order_.get_id_main();
		_last_id_sec = order_.get_id_sec();
		
		for (int i = 0; i < 2; i++)
		{
			boolean is_sec = (i == 1);			
			if (is_sec) id = order_.get_id_sec();

			Order order = get_order_ib(order_, id, parent, update_type, update_val_, is_update, is_sec);
			if (order == null) return false;

			if (is_update) sync.update_order(id, contract, order);
			else sync.place_order(id, contract, order);
		}

		if (!strings.is_ok(update_type)) add_order(id, order_);
		else if (update_val_ != WRONG_VALUE)
		{
			if (update_type.equals(UPDATE_START_VALUE)) update_order(id, update_val_, true); 
			else if (update_type.equals(UPDATE_STOP_VALUE)) update_order(id, update_val_, false); 			
		}
		
		return true;
	}

	private static void add_order(int id_, order order_)
	{
		_orders.put(id_, new order(order_));
		
		sync_global();
	}
	
	private static Order get_order_ib(order order_, int id_, int parent_, String update_type_, double update_val_, boolean is_update_, boolean is_last_)
	{
		boolean is_main = (id_ == parent_);
		boolean is_market = order_.get_type().equals(PLACE_MARKET);
		
		String tif = (String)config.get_sync(types.CONFIG_SYNC_ORDERS_TIF);
		if (!orders.tif_is_ok(tif)) return null;
		
		Order order = new Order();
		
		order.orderId(id_);
		order.tif(tif);		
		if (!is_main) order.parentId(parent_);

		String action = ACTION_BUY;
		if (!is_main || (is_update_ && is_market)) action = ACTION_SELL;
		order.action(action);
		
		double quantity = order_.get_quantity();
		if (is_quantity_int()) quantity = (double)numbers.to_int(quantity);		
		order.totalQuantity(quantity);
		
		double val = (is_main ? order_.get_start() : order_.get_stop());

		if (is_update_)
		{
			if (update_val_ == WRONG_VALUE) return null;
			
			boolean start_market = strings.are_equal(update_type_, UPDATE_START_MARKET);
			boolean stop_market = strings.are_equal(update_type_, UPDATE_STOP_MARKET);
			boolean start_value = strings.are_equal(update_type_, UPDATE_START_VALUE);
			boolean stop_value = strings.are_equal(update_type_, UPDATE_STOP_VALUE);
			
			if (start_market || start_value)
			{
				if (is_main) 
				{
					val = update_val_;
					is_market = start_market;
				}
			}
			else if (stop_market || stop_value)
			{
				if (!is_main)
				{
					val = update_val_;
					is_market = stop_market;
				}
			}
		}

		if (is_market && (is_main || is_update_)) order.orderType(TYPE2_MARKET);
		else
		{
			if (val == WRONG_VALUE) return null;
			
			String type = TYPE2_STOP;
			if (is_main && order_.get_type().equals(PLACE_LIMIT)) type = TYPE2_LIMIT;
			order.orderType(type);	
			
			if (type.equals(TYPE2_STOP)) order.auxPrice(val);	
			else if (type.equals(TYPE2_LIMIT)) order.lmtPrice(val);	
		}

		order.transmit(is_last_);

		return order;
	}

	private static boolean is_quantity_int() { return (boolean)config.get_sync(types.CONFIG_SYNC_ORDERS_QUANTITY_INT); }
	
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