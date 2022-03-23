package accessory_ib;

import external_ib.constants;

public class _defaults 
{
	public static final String CONN = types.CONN_GATEWAY;

	public static final String CURRENCY = "USD"; //Format compatible with Contract.currency.

	public static final String ORDER_TIF = constants.ORDER_TIF_GTC;
	public static final boolean ORDER_QUANTITY_INT = true; 

	public static final int DATA = constants.DATA_LIVE;
	public static final String EXEC_SIDE = constants.EXEC_SIDE_SLD;

	public static final int SYNC_ID = 1; 

	public static final String ASYNC_STORAGE = types.CONFIG_IB_ASYNC_STORAGE_DB;
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_CONSTANT = true;

	public static final String DB_COMMON_COL_SYMBOL = "symbol"; 
	public static final String DB_COMMON_COL_PRICE = "price";
	public static final String DB_COMMON_COL_SIZE = "size";
	
	public static final String DB_MARKET_TABLE = "ib_market";
	public static final String DB_MARKET_COL_TIME = "time";
	public static final String DB_MARKET_COL_QUANTITY = "quantity";
	public static final String DB_MARKET_COL_VOLUME = "volume";
	public static final String DB_MARKET_COL_SIZE = "size";
	public static final String DB_MARKET_COL_OPEN = "open";
	public static final String DB_MARKET_COL_CLOSE = "close";
	public static final String DB_MARKET_COL_HIGH = "high";
	public static final String DB_MARKET_COL_LOW = "low";
	public static final String DB_MARKET_COL_BID = "bid";
	public static final String DB_MARKET_COL_BID_SIZE = "bid_size";
	public static final String DB_MARKET_COL_ASK = "ask";
	public static final String DB_MARKET_COL_ASK_SIZE = "ask_size";
	public static final String DB_MARKET_COL_HALTED = "halted";
	public static final String DB_MARKET_COL_HALTED_TOT = "halted_tot";

	//Method meant to force this class to be loaded when required (e.g., when ini.load() is called).
	public static void load() { } 
}