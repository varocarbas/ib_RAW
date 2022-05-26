package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.types;
import external_ib.constants;

public class orders extends parent_static
{	
	private static HashMap<Integer, order> _orders = new HashMap<Integer, order>();
	private static int MIN_ID = 0;

	private static final String START = accessory.types.action_to_key(accessory.types.ACTION_START);
	private static final String STOP = accessory.types.action_to_key(accessory.types.ACTION_STOP);

	public static String get_id() { return accessory.types.get_id(types.ID_ORDERS); }
	
	public static boolean place(String type_, String symbol_, int quantity_, double stop_, double start_) 
	{
		order info = new order(type_, symbol_, quantity_, stop_, start_);

		return (order.is_ok(info) ? place_update(info, strings.DEFAULT, numbers.DEFAULT_DECIMAL) : false);
	}

	public static boolean update(String type_, String symbol_, double val_) 
	{
		order info = get_info(symbol_);
		if (!order.is_ok(info)) return false;

		String type = check_update(type_);

		return ((!order.is_ok(info) || !strings.is_ok(type)) ? place_update(info, type, val_) : false);
	}
	
	public static void cancel(int id_)
	{
		if (!order_is_open(id_)) return;

		conn._client.cancelOrder(id_);

		misc.pause_secs(1);

		sync_orders();
	}

	public static order get_info(String symbol_)
	{
		order info = null;

		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return info;

		for (Entry<Integer, order> order: _orders.entrySet())
		{
			info = new order(order.getValue());
			if (symbol.equals(info.get_symbol())) return info;
		}

		return info;
	}

	public static String check_update(String type_) { return accessory.types.check_type(type_, types.ORDER_UPDATE); }

	public static boolean id_is_ok(int id_)
	{
		return (id_ >= MIN_ID);
	}

	public static boolean order_is_open(int id_)
	{
		return arrays.value_exists(get_open_orders(), id_);
	}

	public static Integer[] get_open_orders()
	{
		Integer[] open = sync.get_open_ids();

		get_open_orders_sync(open);

		return open;
	}	

	private static boolean place_update(order info_, String update_type_, double update_val_) 
	{
		Contract contract = common.get_contract(info_.get_symbol());

		int tot = 2;

		int max_i = tot - 1;
		int id = info_.get_id();
		int parent = id;

		for (int i = 0; i <= max_i; i++)
		{
			if (i > 0) id++;

			Order order = get_order(info_, id, parent, update_type_, update_val_, (i == max_i));
			if (order == null) return false;

			conn._client.placeOrder(id, contract, order);
		}

		if (!strings.is_ok(update_type_)) _orders.put(id, new order(info_));
		else if (update_type_.equals(types.ORDER_UPDATE_START_VALUE)) update_info(id, START, update_val_); 
		else if (update_type_.equals(types.ORDER_UPDATE_STOP_VALUE)) update_info(id, STOP, update_val_); 

		return true;
	}

	private static Order get_order(order info_, int id_, int parent_, String update_type_, double update_val_, boolean is_last_)
	{
		Order order = new Order();
		order.orderId(id_);

		boolean is_main = (id_ == parent_);
		boolean is_market = info_.get_type().equals(types.ORDER_PLACE_MARKET);
		double val = (is_main ? info_.get_start() : info_.get_stop());

		String type2 = null;

		if (!is_market)
		{
			type2 = update_type_;

			boolean start_market = strings.are_equal(type2, types.ORDER_UPDATE_START_MARKET);
			boolean stop_market = strings.are_equal(type2, types.ORDER_UPDATE_STOP_MARKET);

			if (start_market || strings.are_equal(type2, types.ORDER_UPDATE_START_VALUE))
			{
				if (is_main) 
				{
					val = update_val_;
					is_market = start_market;
				}
				else type2 = null;
			}
			else if (stop_market || strings.are_equal(type2, types.ORDER_UPDATE_STOP_VALUE))
			{
				if (is_main) type2 = null;
				else 
				{
					val = update_val_;
					is_market = stop_market;
				}
			}
			else type2 = null;
		}

		String action = constants.ORDER_ACTION_BUY;
		if (is_main && is_market) action = constants.ORDER_ACTION_SELL;

		order.action(action);

		if (is_market && is_main) order.orderType(constants.ORDER_TYPE_MARKET);
		else
		{
			String order_type = constants.ORDER_TYPE_STOP;
			if (is_main && info_.get_type().equals(types.ORDER_PLACE_LIMIT)) order_type = constants.ORDER_TYPE_LIMIT;

			order.orderType(order_type);
			if (order_type.equals(constants.ORDER_TYPE_STOP)) order.auxPrice(val);	
			else if (order_type.equals(constants.ORDER_TYPE_LIMIT)) order.lmtPrice(val);	
		}
		if (!is_main) order.parentId(parent_);

		String tif = config.get_order(types.CONFIG_ORDER_TIF);
		order.tif(tif);

		double quantity = info_.get_quantity();
		if (strings.to_boolean(config.get_order(types.CONFIG_ORDER_QUANTITY_INT))) quantity = Math.floor(quantity);
		order.totalQuantity(quantity);

		order.transmit(is_last_);

		return order;
	}

	private static boolean update_info(int id_, String what_, double val_)
	{
		if (!_orders.containsKey(id_) || !strings.is_ok(what_)) return false;

		order info = new order(_orders.get(id_));

		if (what_.equals(START)) info.update_start(val_);
		else if (what_.equals(STOP)) info.update_stop(val_);
		else return false;

		_orders.put(id_, info);

		return true;
	}

	private static void sync_orders()
	{
		get_open_orders();
	}

	private static void get_open_orders_sync(Integer[] open_)
	{		
		ArrayList<Integer> delete = new ArrayList<Integer>();

		for (Entry<Integer, order> order: _orders.entrySet())
		{
			int id = order.getKey();
			if (!arrays.value_exists(open_, id)) delete.add(id);
		}

		for (int id: delete) { _orders.remove(id); }
	}
}