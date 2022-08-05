package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.strings;

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

	public static void perform_regular_checks()
	{
		perform_regular_checks_waits();
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