package accessory_ib;

import ib.sync_orders;

public abstract class _keys 
{
	public static final String TWS_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_REAL, types.CONFIG_CONN);
	public static final String TWS_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_PAPER, types.CONFIG_CONN);
	public static final String GATEWAY_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_REAL, types.CONFIG_CONN);
	public static final String GATEWAY_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_PAPER, types.CONFIG_CONN);

	public static final String PLACE_MARKET = sync_orders.get_key(sync_orders.PLACE_MARKET, false);
	public static final String PLACE_STOP = sync_orders.get_key(sync_orders.PLACE_STOP, false);
	public static final String PLACE_LIMIT = sync_orders.get_key(sync_orders.PLACE_LIMIT, false);
	public static final String PLACE_STOP_LIMIT = sync_orders.get_key(sync_orders.PLACE_STOP_LIMIT, false);

	public static final String STATUS_SUBMITTED = sync_orders.get_key(sync_orders.STATUS_SUBMITTED, true);
	public static final String STATUS_FILLED = sync_orders.get_key(sync_orders.STATUS_FILLED, true);
	public static final String STATUS_ACTIVE = sync_orders.get_key(sync_orders.STATUS_ACTIVE, true);
	public static final String STATUS_INACTIVE = sync_orders.get_key(sync_orders.STATUS_INACTIVE, true);
	
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).
}