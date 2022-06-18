package accessory_ib;

public class _keys 
{
	public static final String TWS_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_REAL, types.CONFIG_CONN);
	public static final String TWS_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_PAPER, types.CONFIG_CONN);
	public static final String GATEWAY_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_REAL, types.CONFIG_CONN);
	public static final String GATEWAY_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_PAPER, types.CONFIG_CONN);

	public static final String PLACE_MARKET = accessory._keys.get_key(types.SYNC_ORDERS_PLACE_MARKET, types.SYNC_ORDERS_PLACE);
	public static final String PLACE_STOP = accessory._keys.get_key(types.SYNC_ORDERS_PLACE_STOP, types.SYNC_ORDERS_PLACE);
	public static final String PLACE_LIMIT = accessory._keys.get_key(types.SYNC_ORDERS_PLACE_LIMIT, types.SYNC_ORDERS_PLACE);
	public static final String PLACE_STOP_LIMIT = accessory._keys.get_key(types.SYNC_ORDERS_PLACE_STOP_LIMIT, types.SYNC_ORDERS_PLACE);

	public static final String STATUS_SUBMITTED = accessory._keys.get_key(types.SYNC_ORDERS_STATUS_SUBMITTED, types.SYNC_ORDERS_STATUS);
	public static final String STATUS_FILLED = accessory._keys.get_key(types.SYNC_ORDERS_STATUS_FILLED, types.SYNC_ORDERS_STATUS);
	public static final String STATUS_ACTIVE = accessory._keys.get_key(types.SYNC_ORDERS_STATUS_ACTIVE, types.SYNC_ORDERS_STATUS);
	public static final String STATUS_INACTIVE = accessory._keys.get_key(types.SYNC_ORDERS_STATUS_INACTIVE, types.SYNC_ORDERS_STATUS);
	
	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).
}