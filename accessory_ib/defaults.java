package accessory_ib;

import external.constants;

public class defaults 
{
	public static final String CONN = types.CONN_GATEWAY;

	public static final String CURRENCY = "USD"; //Format compatible with Contract.currency.
	
	public static final String ORDER_TIF = constants.ORDER_TIF_GTC;
	public static final boolean ORDER_QUANTITY_INT = true; 
	
	public static final int DATA = constants.DATA_LIVE;
	public static final String EXEC_SIDE = constants.EXEC_SIDE_SLD;
	
	public static final int SYNC_ID = 1; 
	
	public static final String ASYNC_STORAGE = types._CONFIG_IB_ASYNC_STORAGE_DB;
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_CONSTANT = true;
	
	public static final String DB_COMMON_COL_SYMBOL = keys.SYMBOL; 
	public static final String DB_COMMON_COL_PRICE = keys.PRICE;
	
	public static final String DB_MARKET_TABLE = "ib_market";
	public static final String DB_MARKET_COL_TIME = keys.TIME;
	public static final String DB_MARKET_COL_QUANTITY = keys.QUANTITY;
	public static final String DB_MARKET_COL_VOLUME = keys.VOLUME;
	public static final String DB_MARKET_COL_SIZE = keys.SIZE;
	public static final String DB_MARKET_COL_OPEN = keys.OPEN;
	public static final String DB_MARKET_COL_CLOSE = keys.CLOSE;
	public static final String DB_MARKET_COL_HIGH = keys.HIGH;
	public static final String DB_MARKET_COL_LOW = keys.LOW;
	public static final String DB_MARKET_COL_BID = keys.BID;
	public static final String DB_MARKET_COL_BID_SIZE = keys.BID_SIZE;
	public static final String DB_MARKET_COL_ASK = keys.ASK;
	public static final String DB_MARKET_COL_ASK_SIZE = keys.ASK_SIZE;
	public static final String DB_MARKET_COL_HALTED = keys.HALTED;
	public static final String DB_MARKET_COL_HALTED_TOT = keys.HALTED_TOT;
	
	static { _ini.load(); }
}
