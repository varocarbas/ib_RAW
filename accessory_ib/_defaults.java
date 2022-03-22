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

	public static final String DB_COMMON_FIELD_SYMBOL = "symbol"; 
	public static final String DB_COMMON_FIELD_PRICE = "price";
	public static final String DB_COMMON_FIELD_SIZE = "size";
	
	public static final String DB_MARKET_SOURCE = "ib_market";
	public static final String DB_MARKET_FIELD_TIME = "time";
	public static final String DB_MARKET_FIELD_QUANTITY = "quantity";
	public static final String DB_MARKET_FIELD_VOLUME = "volume";
	public static final String DB_MARKET_FIELD_SIZE = "size";
	public static final String DB_MARKET_FIELD_OPEN = "open";
	public static final String DB_MARKET_FIELD_CLOSE = "close";
	public static final String DB_MARKET_FIELD_HIGH = "high";
	public static final String DB_MARKET_FIELD_LOW = "low";
	public static final String DB_MARKET_FIELD_BID = "bid";
	public static final String DB_MARKET_FIELD_BID_SIZE = "bid_size";
	public static final String DB_MARKET_FIELD_ASK = "ask";
	public static final String DB_MARKET_FIELD_ASK_SIZE = "ask_size";
	public static final String DB_MARKET_FIELD_HALTED = "halted";
	public static final String DB_MARKET_FIELD_HALTED_TOT = "halted_tot";

	//Method meant to force this class to be loaded when required (e.g., when ini.load() is called).
	public static void load() { } 
}