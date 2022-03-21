package accessory_ib;

import external_ib.constants;

public class _defaults 
{
	public static final String CONN = _types.CONN_GATEWAY;

	public static final String CURRENCY = "USD"; //Format compatible with Contract.currency.

	public static final String ORDER_TIF = constants.ORDER_TIF_GTC;
	public static final boolean ORDER_QUANTITY_INT = true; 

	public static final int DATA = constants.DATA_LIVE;
	public static final String EXEC_SIDE = constants.EXEC_SIDE_SLD;

	public static final int SYNC_ID = 1; 

	public static final String ASYNC_STORAGE = _types.CONFIG_IB_ASYNC_STORAGE_DB;
	public static final boolean ASYNC_SNAPSHOT_QUICK = true;
	public static final boolean ASYNC_SNAPSHOT_CONSTANT = true;

	public static final String DB_COMMON_FIELD_SYMBOL = _keys.SYMBOL; 
	public static final String DB_COMMON_FIELD_PRICE = _keys.PRICE;

	public static final String DB_MARKET_SOURCE = "ib_market";
	public static final String DB_MARKET_FIELD_TIME = _keys.TIME;
	public static final String DB_MARKET_FIELD_QUANTITY = _keys.QUANTITY;
	public static final String DB_MARKET_FIELD_VOLUME = _keys.VOLUME;
	public static final String DB_MARKET_FIELD_SIZE = _keys.SIZE;
	public static final String DB_MARKET_FIELD_OPEN = _keys.OPEN;
	public static final String DB_MARKET_FIELD_CLOSE = _keys.CLOSE;
	public static final String DB_MARKET_FIELD_HIGH = _keys.HIGH;
	public static final String DB_MARKET_FIELD_LOW = _keys.LOW;
	public static final String DB_MARKET_FIELD_BID = _keys.BID;
	public static final String DB_MARKET_FIELD_BID_SIZE = _keys.BID_SIZE;
	public static final String DB_MARKET_FIELD_ASK = _keys.ASK;
	public static final String DB_MARKET_FIELD_ASK_SIZE = _keys.ASK_SIZE;
	public static final String DB_MARKET_FIELD_HALTED = _keys.HALTED;
	public static final String DB_MARKET_FIELD_HALTED_TOT = _keys.HALTED_TOT;

	//Method meant to force this class to be loaded when required (e.g., when ini.load() is called).
	public static void load() { } 
}