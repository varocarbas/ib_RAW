package accessory_ib;

import external_ib.constants;

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

	public static final String DB_COMMON_FIELD_SYMBOL = keys.SYMBOL; 
	public static final String DB_COMMON_FIELD_PRICE = keys.PRICE;

	public static final String DB_MARKET_SOURCE = "ib_market";
	public static final String DB_MARKET_FIELD_TIME = keys.TIME;
	public static final String DB_MARKET_FIELD_QUANTITY = keys.QUANTITY;
	public static final String DB_MARKET_FIELD_VOLUME = keys.VOLUME;
	public static final String DB_MARKET_FIELD_SIZE = keys.SIZE;
	public static final String DB_MARKET_FIELD_OPEN = keys.OPEN;
	public static final String DB_MARKET_FIELD_CLOSE = keys.CLOSE;
	public static final String DB_MARKET_FIELD_HIGH = keys.HIGH;
	public static final String DB_MARKET_FIELD_LOW = keys.LOW;
	public static final String DB_MARKET_FIELD_BID = keys.BID;
	public static final String DB_MARKET_FIELD_BID_SIZE = keys.BID_SIZE;
	public static final String DB_MARKET_FIELD_ASK = keys.ASK;
	public static final String DB_MARKET_FIELD_ASK_SIZE = keys.ASK_SIZE;
	public static final String DB_MARKET_FIELD_HALTED = keys.HALTED;
	public static final String DB_MARKET_FIELD_HALTED_TOT = keys.HALTED_TOT;

	static { _ini.load(); }
}