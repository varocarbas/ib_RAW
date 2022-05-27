package accessory_ib;

import external_ib.market;
import external_ib.orders;

public class _defaults 
{
	public static final String CONN_TYPE = types.CONN_GATEWAY_REAL;
	public static final int CONN_ID = 18; 

	public static final String CURRENCY = "USD"; //Format compatible with Contract.currency.

	public static final String ORDER_TIF = orders.TIF_GTC; 
	public static final String ORDER_EXEC_SIDE = orders.EXEC_SIDE_SOLD;
	public static final boolean ORDER_QUANTITY_INT = true;
	
	public static final String MARKET_TYPE = types.ASYNC_MARKET_SNAPSHOT;
	public static final int MARKET_DATA = market.DATA_LIVE;
	
	public static final int SYNC_ID = 1; 
	public static final long SYNC_TIMEOUT = 10l; 
	
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_NONSTOP = true;

	public static final String DB_NAME = "accessory";

	public static final String ERRORS_WARNING = "WARNING";
	
	public static void populate() { } 
}