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
		String status = _order.get_status(status_ib_, true);
		if (!strings.is_ok(status) || status.equals(orders.STATUS_INACTIVE)) return;
		
		orders.update_status(order_id_, status);	
	}
	
	public static void __check_all() { __check_db(); }
	
	public static void __check_db()
	{
		__lock();
		
		HashMap<Integer, String> orders = arrays.get_new_hashmap_xy(sync._get_orders(false));
		
		ArrayList<HashMap<String, String>> db = ib.orders.get_all_active(new String[] { db_ib.orders.ORDER_ID_MAIN, db_ib.orders.STATUS });
		if (!arrays.is_ok(db)) 
		{
			__unlock();
			
			return;
		}
		
		for (HashMap<String, String> item: db)
		{
			int order_id = Integer.parseInt(item.get(ib.orders.get_field_col(db_ib.orders.ORDER_ID_MAIN)));
			String status = db_ib.orders.get_status_from_key(item.get(ib.orders.get_field_col(db_ib.orders.STATUS)));
			
			boolean is_filled = (sync._order_is_filled(order_id, orders, false, false) || execs.is_filled(order_id));
			
			if (strings.are_equal(status, ib.orders.STATUS_FILLED))
			{
				if (!is_filled) ib.orders.deactivate(order_id);
			}
			else if (is_filled) ib.orders.update_status(order_id, ib.orders.STATUS_FILLED);
			
			if (is_filled) continue;
			
			if (orders.containsKey(order_id))
			{
				String status_ib = orders.get(order_id);
				
				if (!_order.is_status(status_ib, status)) ib.orders.update_status(order_id, _order.get_status(status_ib, true));
			}
			else if (!strings.are_equal(status, ib.orders.STATUS_INACTIVE)) ib.orders.delete(order_id);
		}
		
		__unlock();
	}
}