package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;

abstract class async_orders extends parent_static
{
	public static void order_status(int order_id_, String status_ib_) 
	{ 
		String status = orders.get_status(status_ib_, true);
		if (!strings.is_ok(status) || strings.matches_any(status, new String[] { orders.STATUS_IN_PROGRESS, orders.STATUS_INACTIVE }, false) || !orders.exists(order_id_)) return;
		
		orders.update_status(order_id_, status);	
	}
	
	public static void __check_all() { __check_db(); }
	
	public static void __check_db()
	{
		ArrayList<HashMap<String, String>> db = arrays.get_new(ib.orders.get_all_active(new String[] { db_ib.orders.ORDER_ID_MAIN, db_ib.orders.STATUS }));
		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(sync.__get_orders());

		check_db_orders(orders, check_db_db(db, orders));
	}
	
	private static ArrayList<Integer> check_db_db(ArrayList<HashMap<String, String>> db_, HashMap<Integer, String> orders_)
	{
		ArrayList<Integer> active = new ArrayList<Integer>();

		for (HashMap<String, String> item: db_)
		{
			int order_id = orders.get_order_id_main(item);
			if (order_id <= ib.common.WRONG_ORDER_ID) continue;
			
			active.add(order_id);
			
			String status = orders.get_status(item);
			if (!strings.is_ok(status)) continue;
		
			String status2 = null;
			
			if (sync_orders.is_filled(order_id, orders_)) status2 = orders.STATUS_FILLED;
			else if (sync_orders.is_inactive(order_id, orders_)) status2 = orders.STATUS_INACTIVE;
			else if (orders_.containsKey(order_id) && !external_ib.orders.status_in_progress(orders_.get(order_id))) status2 = orders.get_status(orders_.get(order_id), true);
			
			if (!strings.is_ok(status2) || status2.equals(ib.orders.STATUS_INACTIVE) || strings.matches_any(status, new String[] { status2, orders.STATUS_FILLED }, false)) continue;
			
			orders.update_status(order_id, status2);
		}
				
		return active;
	}
	
	private static void check_db_orders(HashMap<Integer, String> orders_, ArrayList<Integer> active_)
	{
		for (Entry<Integer, String> order: orders_.entrySet())
		{
			int order_id = order.getKey();
			String status = orders.get_status(order.getValue(), true);
			
			if (active_.contains(order_id) || !strings.is_ok(status) || strings.matches_any(status, new String[] { orders.STATUS_IN_PROGRESS, orders.STATUS_INACTIVE }, false)) continue;
					
			if (orders.is_order_id_main(order_id)) orders.update_status(order_id, status);
			else if (orders.is_order_id_sec(order_id))
			{
				int order_id_main = orders.get_order_id_main(order_id);
				if (order_id_main != common.WRONG_ORDER_ID && sync_orders.is_filled(order_id_main, orders_)) orders.update_status(order_id_main, orders.STATUS_FILLED);
			}
		}		
	}	
}