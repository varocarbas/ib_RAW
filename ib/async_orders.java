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
		if (!strings.is_ok(status) || status.equals(orders.STATUS_IN_PROGRESS)) return;
		
		orders.update_status(order_id_, status);	
	}
	
	public static void __check_all() { __check_db(); }
	
	public static void __check_db()
	{
		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(sync.__get_orders());
		
		ArrayList<HashMap<String, String>> db = ib.orders.get_all_active(new String[] { ib.orders.get_field_col(db_ib.orders.ORDER_ID_MAIN), ib.orders.get_field_col(db_ib.orders.STATUS) });
		if (!arrays.is_ok(db)) return;
		
		for (HashMap<String, String> item: db)
		{
			int order_id = Integer.parseInt(item.get(ib.orders.get_field_col(db_ib.orders.ORDER_ID_MAIN)));
			
			String status = db_ib.orders.get_status_from_key(item.get(ib.orders.get_field_col(db_ib.orders.STATUS)));
			if (!strings.is_ok(status)) continue;
			
			boolean is_filled = sync_orders.is_filled(order_id, orders);
			
			if (status.equals(ib.orders.STATUS_FILLED))
			{
				if (!is_filled) ib.orders.deactivate(order_id);
			}
			else if (is_filled) ib.orders.update_status(order_id, ib.orders.STATUS_FILLED);
			
			if (is_filled || !orders.containsKey(order_id)) continue;
			
			String status_ib = orders.get(order_id);
			if (external_ib.orders.status_in_progress(status_ib) || ib.orders.is_status(status_ib, status)) continue;
			
			status = ib.orders.get_status(status_ib, true);
			if (strings.is_ok(status)) ib.orders.update_status(order_id, status);
		}
	}
}