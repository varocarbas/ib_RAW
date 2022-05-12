package accessory_ib;

import external_ib.constants;

public class _defaults 
{
	public static final String CONN_TYPE = types.CONN_GATEWAY;
	public static final int CONN_ID = 18; 

	public static final String CURRENCY = "USD"; //Format compatible with Contract.currency.

	public static final String ORDER_TIF = constants.ORDER_TIF_GTC;
	public static final boolean ORDER_QUANTITY_INT = true; 

	public static final int DATA = constants.DATA_LIVE;
	public static final String EXEC_SIDE = constants.EXEC_SIDE_SLD;
	
	public static final int SYNC_ID = 1; 

	public static final String ASYNC_STORAGE = types.CONFIG_ASYNC_STORAGE_DB;
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_CONSTANT = true;

	public static final String DB_NAME = "accessory";
	
	public static void populate() { } 
}