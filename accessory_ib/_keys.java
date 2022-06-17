package accessory_ib;

public class _keys 
{
	public static final String TWS_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_REAL, types.CONFIG_CONN);
	public static final String TWS_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_TWS_PAPER, types.CONFIG_CONN);
	public static final String GATEWAY_REAL = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_REAL, types.CONFIG_CONN);
	public static final String GATEWAY_PAPER = accessory._keys.get_key(types.CONFIG_CONN_TYPE_GATEWAY_PAPER, types.CONFIG_CONN);

	public static void populate() { } //Method forcing this class to load when required (e.g., from the ini class).
}