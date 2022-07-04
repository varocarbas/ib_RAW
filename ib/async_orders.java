package ib;

import accessory.strings;

public abstract class async_orders 
{
	public static final String INACTIVE = order.STATUS_INACTIVE;
	
	public static void order_status(int order_id_, String status_ib_) 
	{ 
		String status = order.get_status(status_ib_, true);
		if (!strings.is_ok(status) || status.equals(INACTIVE)) return;
		
		db_ib.orders.update_status(order_id_, status);	
	}
}