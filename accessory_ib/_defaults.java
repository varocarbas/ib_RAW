package accessory_ib;

import external_ib.contracts;
import external_ib.market;
import external_ib.orders;

public class _defaults 
{
	public static final String CONN_TYPE = types.CONN_GATEWAY_REAL;
	public static final int CONN_ID = 18; 
	public static final int CONN_PORT_TWS_REAL = 7496;
	public static final int CONN_PORT_TWS_PAPER = 7497;
	public static final int CONN_PORT_GATEWAY_REAL = 4001;
	public static final int CONN_PORT_GATEWAY_PAPER = 4002;	
	
	public static final String CONTRACT_CURRENCY = "USD";
	public static final String CONTRACT_SECURITY_TYPE = contracts.SECURITY_STOCK_ETF;
	public static final String CONTRACT_EXCHANGE = "SMART";
	public static final String CONTRACT_PRIMARY_EXCHANGE = "ISLAND";
	
	public static final String MARKET_TYPE = types.ASYNC_MARKET_SNAPSHOT;
	public static final int MARKET_DATA = market.DATA_LIVE;
	
	public static final long SYNC_TIMEOUT = 10l; 
	public static final String SYNC_ORDERS_TIF = orders.TIF_GTC; 
	public static final boolean SYNC_ORDERS_QUANTITY_INT = true;
	
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_NONSTOP = true;
	public static final int ASYNC_DATA_TYPE = market.DATA_LIVE;
	
	public static final String DB_NAME = accessory.db.DEFAULT_DB_NAME;

	public static final String ERRORS_WARNING = "WARNING";
	
	public static void populate() { } 
}