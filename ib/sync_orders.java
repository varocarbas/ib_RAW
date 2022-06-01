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
	public static final String ACTION_BUY = orders.ACTION_BUY;
	public static final String ACTION_SELL = orders.ACTION_SELL;
	
	public static final String TYPE_MARKET = orders.TYPE_MARKET;
	public static final String TYPE_STOP = orders.TYPE_STOP;
	public static final String TYPE_LIMIT = orders.TYPE_LIMIT;
	
	public static final String PLACE = types.SYNC_ORDERS_PLACE;
	public static final String PLACE_MARKET = types.SYNC_ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = types.SYNC_ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = types.SYNC_ORDERS_PLACE_LIMIT;
	public static final String UPDATE = types.SYNC_ORDERS_UPDATE;
	public static final String UPDATE_START_VALUE = types.SYNC_ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = types.SYNC_ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_STOP_VALUE = types.SYNC_ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = types.SYNC_ORDERS_UPDATE_STOP_MARKET;
	
	public static final double WRONG_VALUE = 0.0;

	private static HashMap<Integer, order> _orders = new HashMap<Integer, order>();
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_SYNC_ORDERS); }
	
	public static boolean place(String type_, String symbol_, double quantity_, double stop_, double start_) { return place_update(new order(type_, symbol_, quantity_, stop_, start_)); }

	public static boolean update(String type_, String symbol_) { return update(type_, symbol_, WRONG_VALUE); }
	
	public static boolean update(String type_, String symbol_, double val_) 
	{
		String type = check_update(type_);
		order order = get_order(symbol_);

		return ((order == null || !strings.is_ok(type)) ? place_update(order, type, val_) : false);
	}
	
	public static void cancel(int id_)
	{
		if (!is_open(id_) || !sync.cancel_order(id_)) return;

		remove_global(id_);
	}

	private static boolean is_open(int id_) { return arrays.value_exists(get_open_orders(), id_); }

	public static ArrayList<Integer> get_open_orders()
	{
		ArrayList<Integer> open = sync.get_open_ids();

		sync_global(open);
		
		return open;
	}	

	private static void remove_global(int id_) { arrays.remove_key(_orders, id_); }
	
	private static void sync_global(ArrayList<Integer> open_)
	{
		if (!arrays.is_ok(open_))
		{
			_orders = new HashMap<Integer, order>();
			
			return;
		}
			
		ArrayList<Integer> delete = new ArrayList<Integer>();

		for (Entry<Integer, order> item: _orders.entrySet())
		{
			int id = item.getKey();
			if (!arrays.value_exists(open_, id)) delete.add(id);
		}

		for (int id: delete) { _orders.remove(id); }
	}

	private static order get_order(String symbol_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return null;

		for (Entry<Integer, order> item: _orders.entrySet())
		{
			if (symbol.equals(item.getValue().get_symbol())) return (new order(item.getValue()));
		}

		return null;
	}

	public static String check_place(String type_) { return accessory.types.check_type(type_, PLACE); }
	
	public static String check_update(String type_) { return accessory.types.check_type(type_, UPDATE); }
	
	private static boolean place_update(order order_) { return place_update(order_, null, WRONG_VALUE); }
	
	private static boolean place_update(order order_, String update_type_, double update_val_) 
	{
		if (!order.is_ok(order_)) return false;

		String update_type = check_update(update_type_);
		
		Contract contract = common.get_contract(order_.get_symbol());

		int tot = 2;
		int max_i = tot - 1;
		
		int id = order_.get_id();
		int parent = id;

		for (int i = 0; i <= max_i; i++)
		{
			if (i > 0) id++;

			Order order = get_order_ib(order_, id, parent, update_type, update_val_, (i == max_i));
			if (order == null) return false;

			sync.place_update_order(id, contract, order);
		}

		if (!strings.is_ok(update_type)) _orders.put(id, new order(order_));
		else if (update_val_ != WRONG_VALUE)
		{
			if (update_type.equals(UPDATE_START_VALUE)) update_order(id, update_val_, true); 
			else if (update_type.equals(UPDATE_STOP_VALUE)) update_order(id, update_val_, false); 			
		}
		
		return true;
	}

	private static Order get_order_ib(order order_, int id_, int parent_, String update_type_, double update_val_, boolean is_last_)
	{
		boolean is_main = (id_ == parent_);
		boolean is_market = order_.get_type().equals(PLACE_MARKET);
		
		String tif = config.get_sync(types.CONFIG_SYNC_ORDERS_TIF);
		if (!orders.tif_is_ok(tif)) return null;
		
		Order order = new Order();
		
		order.orderId(id_);
		order.tif(tif);		
		if (!is_main) order.parentId(parent_);

		String action = ACTION_BUY;
		if (is_main && is_market) action = ACTION_SELL;
		order.action(action);
		
		double quantity = order_.get_quantity();
		if (is_quantity_int()) quantity = (double)numbers.to_int(quantity);		
		order.totalQuantity(quantity);
		
		double val = (is_main ? order_.get_start() : order_.get_stop());

		if (!is_market)
		{
			String type = check_update(update_type_);
			if (strings.is_ok(type) || update_val_ == WRONG_VALUE) return null;
			
			boolean start_market = strings.are_equal(type, UPDATE_START_MARKET);
			boolean stop_market = strings.are_equal(type, UPDATE_STOP_MARKET);
			boolean start_value = strings.are_equal(type, UPDATE_START_VALUE);
			boolean stop_value = strings.are_equal(type, UPDATE_STOP_VALUE);
			
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

		if (is_market) order.orderType(TYPE_MARKET);
		else
		{
			if (val == WRONG_VALUE) return null;
			
			String type = TYPE_STOP;
			if (is_main && order_.get_type().equals(PLACE_LIMIT)) type = TYPE_LIMIT;
			order.orderType(type);	
			
			if (type.equals(TYPE_STOP)) order.auxPrice(val);	
			else if (type.equals(TYPE_LIMIT)) order.lmtPrice(val);	
		}
		
		order.transmit(is_last_);

		return order;
	}

	private static boolean is_quantity_int() { return strings.to_boolean(config.get_sync(types.CONFIG_SYNC_ORDERS_QUANTITY_INT)); }
	
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