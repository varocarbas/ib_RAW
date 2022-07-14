package accessory_ib;

import ib.orders;

public abstract class _keys 
{
	public static final String PLACE_MARKET = orders.get_key(orders.PLACE_MARKET, false);
	public static final String PLACE_STOP = orders.get_key(orders.PLACE_STOP, false);
	public static final String PLACE_LIMIT = orders.get_key(orders.PLACE_LIMIT, false);
	public static final String PLACE_STOP_LIMIT = orders.get_key(orders.PLACE_STOP_LIMIT, false);

	public static final String STATUS_SUBMITTED = orders.get_key(orders.STATUS_SUBMITTED, true);
	public static final String STATUS_FILLED = orders.get_key(orders.STATUS_FILLED, true);
	public static final String STATUS_ACTIVE = orders.get_key(orders.STATUS_ACTIVE, true);
	public static final String STATUS_INACTIVE = orders.get_key(orders.STATUS_INACTIVE, true);
	
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).
}