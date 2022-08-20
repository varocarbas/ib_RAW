package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;

abstract class async_orders extends parent_static
{
	public static volatile HashMap<Integer, Long> _cancellations = new HashMap<Integer, Long>();

	public static void order_status(int order_id_, String status_ib_) 
	{ 
		String status = order.get_status(status_ib_, true);
		if (!strings.is_ok(status) || status.equals(orders.STATUS_INACTIVE)) return;
		
		orders.update_status(order_id_, status);	
	}
	
	public static void __check_all()
	{
		__check_waits();
		
		check_db();
	}
	
	public static void check_db()
	{
		HashMap<Integer, String> ib = arrays.get_new_hashmap_xy(sync.get_orders());
		
		ArrayList<HashMap<String, String>> db = db_ib.orders.get_all_active(new String[] { db_ib.orders.ORDER_ID_MAIN, db_ib.orders.STATUS });
		if (!arrays.is_ok(db)) return;
		
		for (HashMap<String, String> item: db)
		{
			int order_id = Integer.parseInt(item.get(db_ib.orders.ORDER_ID_MAIN));
			String status = db_ib.orders.get_status_from_key(item.get(db_ib.orders.STATUS));
			
			if (strings.are_equal(status, orders.STATUS_FILLED) || orders.is_filled(order_id, ib, false)) continue;
			
			if (ib.containsKey(order_id))
			{
				String status_ib = ib.get(order_id);
				
				if (!order.is_status(status_ib, status)) orders.update_status(order_id, order.get_status(status_ib, true));
			}
			else if (!strings.are_equal(status, orders.STATUS_INACTIVE)) orders.delete(order_id, false);			
		}
	}

	private static void __check_waits()
	{
		__lock();
		
		HashMap<Integer, Long> output = new HashMap<Integer, Long>(_cancellations);

		for (Entry<Integer, Long> item: _cancellations.entrySet())
		{
			HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(item.getKey(), output);
			if (temp != null) output = new HashMap<Integer, Long>(temp);
		}

		_cancellations = new HashMap<Integer, Long>(output);
		
		__unlock();
	}
}