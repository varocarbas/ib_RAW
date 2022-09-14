package ib;

import java.util.HashMap;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import external_ib.contracts;

abstract class sync_orders extends parent_static  
{
	public static int _last_id_main = 0;
	public static int _last_id_sec = 0;

	public static boolean cancel(int id_main_)
	{
		sync.cancel_order(id_main_);

		boolean is_ok = sync.wait_orders(id_main_, orders.CANCEL);
		if (is_ok) is_ok = ib.orders.deactivate(id_main_);	
		
		return is_ok;
	}

	public static void order_status(int order_id_, String status_ib_) { sync.update_orders(order_id_, status_ib_); }
	
	public static void open_order_end() { sync.end_get(); }
	
	public static boolean __update(String symbol_, String type_, double val_) { return __update(__get_order(symbol_), orders.check_update(type_), val_); }

	public static boolean __update(_order order_, String type_, double val_) { return ((order_ != null && strings.is_ok(type_)) ? __place_update(order_, type_, val_) : false); }

	public static boolean __place(String type_place_, String symbol_, double quantity_, double stop_, double start_) { return __place(type_place_, symbol_, quantity_, stop_, start_, common.WRONG_PRICE); }

	public static boolean __place(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_) { return (orders.is_inactive(symbol_) ? __place_update(new _order(type_place_, symbol_, quantity_, stop_, start_, start2_)) : false); }

	public static _order __get_order(int id_main_) { return __get_order(id_main_, true); }

	public static _order __get_order(int id_main_, boolean perform_checks_)
	{
		if (perform_checks_) async_orders.__check_all();

		return db_ib.orders.get_to_order(id_main_, orders.is_quick());
	}

	public static boolean __place_update(_order order_) { return __place_update(order_, null, common.WRONG_VALUE); }

	public static boolean __place_update(_order order_, String update_type_, double update_val_) 
	{
		if (!_order.is_ok(order_)) return false;
		
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
			if ((!is_main && orders.is_update_start_start2(update_type_)) || (is_main && orders.is_update_stop(update_type_))) continue;

			int id = order_.get_id(is_main);

			Order order = (is_update ? external_ib.orders.get_order_update(order_, update_type_, update_val_, is_main) : external_ib.orders.get_order_new(order_, is_main));
			if (order == null) return false;

			boolean is_ok = true;

			if (is_update) is_ok = sync.update_order(id, contract, order);
			else 
			{
				is_ok = sync.place_order(id, contract, order);
				if (!is_ok) return false;
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
			if (!sync.wait_orders(main, orders.PLACE)) return false;

			__add_order(order_);
		}

		return true;
	}

	public static boolean is_filled(int order_id_, HashMap<Integer, String> orders_) { return is_common(order_id_, orders.STATUS_FILLED, orders_); }	

	public static boolean is_submitted(int order_id_, HashMap<Integer, String> orders_) { return is_common(order_id_, orders.STATUS_SUBMITTED, orders_); }
	
	public static boolean is_inactive(int order_id_, HashMap<Integer, String> orders_) { return is_common(order_id_, orders.STATUS_INACTIVE, orders_); }

	private static boolean is_common(int order_id_, String target_, HashMap<Integer, String> orders_)
	{
		boolean output = false;
		if (order_id_ <= common.WRONG_ORDER_ID) return output;
	
		String target = orders.check_status(target_);
		if (!strings.is_ok(target)) return output;
		
		String status_ib = (String)arrays.get_value(orders_, order_id_);
		if (external_ib.orders.status_in_progress(status_ib)) return output;

		boolean is_filled = false;
		boolean exists = strings.is_ok(status_ib); 
		
		if (exists) is_filled = orders.is_status(status_ib, orders.STATUS_FILLED);
		else 
		{
			String status2 = ib.orders.get_status((String)arrays.get_value(orders_, _order.get_id_sec(order_id_)), true);
			
			is_filled = (strings.is_ok(status2) && !strings.matches_any(status2, new String[] { ib.orders.STATUS_FILLED, ib.orders.STATUS_INACTIVE }, false));
		} 
				
		if (is_filled) 
		{
			output = target.equals(orders.STATUS_FILLED);
			
			ib.orders.update_status(order_id_, orders.STATUS_FILLED);
		}
		else if (!exists) output = target.equals(orders.STATUS_INACTIVE);
		else output = orders.is_status(status_ib, target);
		
		return output;
	}

	private static _order __get_order(String symbol_)
	{
		async_orders.__check_all();

		return db_ib.orders.get_to_order(symbol_, orders.is_quick());
	}

	private static void __add_order(_order order_) { db_ib.orders.insert_update(order_, orders.is_quick()); }

	private static boolean update_order(int id_, double val_, boolean is_market_, String type_)
	{
		String type = orders.check_update(type_);
		if (!strings.is_ok(type)) return false;
		
		_order order = db_ib.orders.get_to_order(id_, orders.is_quick());
		if (order == null) return false;

		if (is_market_) order.update_type(_order.TYPE_MARKET, orders.is_update_start_start2(type));
		else if (type.equals(orders.UPDATE_STOP_VALUE)) order.update_stop(val_);
		else if (type.equals(orders.UPDATE_START_VALUE)) order.update_start(val_);
		else if (type.equals(orders.UPDATE_START2_VALUE)) order.update_start2(val_);
		
		return db_ib.orders.update(order, orders.is_quick());
	}
}