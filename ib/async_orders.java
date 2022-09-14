package ib;

import java.util.ArrayList;
import java.util.HashMap;

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
		ArrayList<HashMap<String, String>> db = ib.orders.get_all_active(new String[] { db_ib.orders.ORDER_ID_MAIN, db_ib.orders.STATUS });
		if (!arrays.is_ok(db)) return;
	
		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(sync.__get_orders());

		for (HashMap<String, String> item: db)
		{
			int order_id = Integer.parseInt(item.get(ib.orders.get_field_col(db_ib.orders.ORDER_ID_MAIN)));
			if (order_id <= ib.common.WRONG_ORDER_ID) continue;
			
			String status = db_ib.orders.get_status_from_key(item.get(ib.orders.get_field_col(db_ib.orders.STATUS)));
			if (!strings.is_ok(status)) continue;
		
			String status2 = null;
			
			if (sync_orders.is_filled(order_id, orders)) status2 = ib.orders.STATUS_FILLED;
			else if (sync_orders.is_inactive(order_id, orders)) status2 = ib.orders.STATUS_INACTIVE;
			else if (orders.containsKey(order_id) && !external_ib.orders.status_in_progress(orders.get(order_id))) status2 = ib.orders.get_status(orders.get(order_id), true);
			
			if (!strings.is_ok(status2) || status.equals(status2) || status.equals(ib.orders.STATUS_FILLED)) continue;
			
			ib.orders.update_status(order_id, status2);
		}
	}
}