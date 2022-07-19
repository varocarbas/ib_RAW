package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.strings;
import db_ib.orders;

abstract class async_orders 
{
	public static final String ACTIVE = order.STATUS_ACTIVE;
	public static final String INACTIVE = order.STATUS_INACTIVE;

	public static volatile HashMap<Integer, Long> _cancellations = new HashMap<Integer, Long>();

	public static void order_status(int order_id_, String status_ib_) 
	{ 
		String status = order.get_status(status_ib_, true);
		if (!strings.is_ok(status) || status.equals(INACTIVE)) return;
		
		db_ib.orders.update_status(order_id_, status);	
	}
	
	public static void perform_regular_checks(HashMap<Integer, String> orders_) { perform_regular_checks(sync_orders.get_ids(ACTIVE, orders_, false)); }

	public static void perform_regular_checks() { perform_regular_checks(sync_orders.get_ids(ACTIVE)); }

	private static void perform_regular_checks(ArrayList<Integer> active_)
	{
		perform_regular_checks_orders(active_);

		perform_regular_checks_waits();
	}

	private static void perform_regular_checks_orders(ArrayList<Integer> active_ids_) 
	{ 
		if (!arrays.is_ok(active_ids_)) return;
		
		ArrayList<Integer> remove = new ArrayList<Integer>();
		
		for (int id: active_ids_) 
		{
			if (trades.exists(id)) continue;
			
			remove.add(id);
		}
		
		orders.delete_except(arrays.to_array(remove)); 
	}

	private static void perform_regular_checks_waits()
	{
		HashMap<Integer, Long> output = new HashMap<Integer, Long>(_cancellations);

		for (Entry<Integer, Long> item: _cancellations.entrySet())
		{
			HashMap<Integer, Long> temp = common_xsync.wait_is_over_sync(item.getKey(), output);
			if (temp != null) output = new HashMap<Integer, Long>(temp);
		}

		_cancellations = new HashMap<Integer, Long>(output);
	}
}