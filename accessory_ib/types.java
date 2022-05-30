package accessory_ib;

public class types
{
	public static final String CONFIG_CONN = "config_conn";
	public static final String CONFIG_CONN_TYPE = "config_conn_type";
	public static final String CONFIG_CONN_ID = "config_conn_id";
	
	public static final String CONFIG_ASYNC = "config_async";
	public static final String CONFIG_ASYNC_MARKET = "config_async_market";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT = "config_async_market_snapshot";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK = "config_async_market_snapshot_quick";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP = "config_async_market_snapshot_nonstop";

	public static final String CONFIG_ORDER = "config_order";
	public static final String CONFIG_ORDER_TIF = "config_order_tif";
	public static final String CONFIG_ORDER_QUANTITY_INT = "config_order_quantity_int";

	public static final String CONFIG_CONTRACT = "config_contract";
	public static final String CONFIG_CONTRACT_SECURITY_TYPE = "config_contract_security_type";
	public static final String CONFIG_CONTRACT_CURRENCY = "config_contract_currency";
	public static final String CONFIG_CONTRACT_EXCHANGE = "config_contract_exchange";
	public static final String CONFIG_CONTRACT_PRIMARY_EXCHANGE = "config_contract_primary_exchange";

	public static final String CONFIG_DB_FIELD = "config_db_field";
	public static final String CONFIG_DB_FIELD_SIZE = "config_db_field_size";
	public static final String CONFIG_DB_FIELD_TIME = "config_db_field_time";
	public static final String CONFIG_DB_FIELD_SYMBOL = "config_db_field_symbol";
	public static final String CONFIG_DB_FIELD_PRICE = "config_db_field_price";
	public static final String CONFIG_DB_FIELD_OPEN = "config_db_field_open";
	public static final String CONFIG_DB_FIELD_CLOSE = "config_db_field_close";
	public static final String CONFIG_DB_FIELD_LOW = "config_db_field_low";
	public static final String CONFIG_DB_FIELD_HIGH = "config_db_field_high";
	public static final String CONFIG_DB_FIELD_VOLUME = "config_db_field_volume";
	public static final String CONFIG_DB_FIELD_ASK = "config_db_field_ask";
	public static final String CONFIG_DB_FIELD_ASK_SIZE = "config_db_field_ask_size";
	public static final String CONFIG_DB_FIELD_BID = "config_db_field_bid";
	public static final String CONFIG_DB_FIELD_BID_SIZE = "config_db_field_bid_size";
	public static final String CONFIG_DB_FIELD_HALTED = "config_db_field_halted";
	public static final String CONFIG_DB_FIELD_HALTED_TOT = "config_db_field_halted_tot";

	public static final String CONFIG_DB_IB = "config_db_ib";
	public static final String CONFIG_DB_IB_MARKET = "config_db_ib_market";
	public static final String CONFIG_DB_IB_MARKET_SOURCE = "config_db_ib_market_source";

	public static final String ID_ASYNC = "id_async";
	public static final String ID_ASYNC_MARKET = "id_async_market";
	public static final String ID_COMMON = "id_common";
	public static final String ID_CONN = "id_conn";
	public static final String ID_ORDERS = "id_orders";
	public static final String ID_SYNC = "id_sync";
	
	public static final String CONN = "conn";
	public static final String CONN_TWS_REAL = "conn_tws_real";
	public static final String CONN_TWS_PAPER = "conn_tws_paper";
	public static final String CONN_GATEWAY_REAL = "conn_gateway_real";
	public static final String CONN_GATEWAY_PAPER = "conn_gateway_paper";	

	public static final String SYNC = "sync";
	public static final String SYNC_GET = "sync_get";
	public static final String SYNC_GET_FUNDS = "sync_get_funds";
	public static final String SYNC_GET_ID = "sync_get_id";
	public static final String SYNC_GET_IDS = "sync_get_ids";
	public static final String SYNC_OUT = "sync_out";
	public static final String SYNC_OUT_INT = "sync_out_int";
	public static final String SYNC_OUT_DECIMAL = "sync_out_decimal";
	public static final String SYNC_OUT_INTS = "sync_out_ints";
	public static final String SYNC_OUT_MISC = "sync_out_misc";

	public static final String ASYNC = "async";
	public static final String ASYNC_MARKET = "async_market";
	public static final String ASYNC_MARKET_SNAPSHOT = "async_market_snapshot";
	public static final String ASYNC_MARKET_STREAM = "async_market_stream";

	public static final String ORDER = "order";
	public static final String ORDER_PLACE = "order_place";
	public static final String ORDER_PLACE_MARKET = "order_place_market";
	public static final String ORDER_PLACE_STOP = "order_place_stop";
	public static final String ORDER_PLACE_LIMIT = "order_place_limit";
	public static final String ORDER_UPDATE = "order_update";
	public static final String ORDER_UPDATE_START_VALUE = "order_update_start_value";
	public static final String ORDER_UPDATE_START_MARKET = "order_update_start_market";
	public static final String ORDER_UPDATE_STOP_VALUE = "order_update_stop_value";
	public static final String ORDER_UPDATE_STOP_MARKET = "order_update_stop_market";

	public static final String ERROR_IB = "error_ib";
	public static final String ERROR_IB_GENERIC = "error_ib_generic";
	public static final String ERROR_IB_INI = "error_ib_ini";
	public static final String ERROR_IB_INI_DB = "error_ib_ini_db";
	public static final String ERROR_IB_INI_DB_DBS = "error_ib_ini_db_dbs";
	public static final String ERROR_IB_CONN = "error_ib_conn";
	public static final String ERROR_IB_CONN_NONE = "error_ib_conn_none";
	public static final String ERROR_IB_CONN_ID = "error_ib_conn_id";
	public static final String ERROR_IB_CONN_TYPE = "error_ib_conn_type";
	public static final String ERROR_IB_CONN_GENERIC = "error_ib_conn_generic";
	public static final String ERROR_IB_SYNC = "error_ib_sync";
	public static final String ERROR_IB_SYNC_GET = "error_ib_sync_get";
	public static final String ERROR_IB_SYNC_ID = "error_ib_sync_id";
	public static final String ERROR_IB_SYNC_TIME = "error_ib_sync_time";
	public static final String ERROR_IB_CONTRACT = "error_ib_contract";
	public static final String ERROR_IB_CONTRACT_INFO = "error_ib_contract_info";
	
	static String[] populate_all_types()
	{		
		return new String[]
		{
			CONFIG_CONN,
			CONFIG_CONN_TYPE, CONFIG_CONN_ID,
	
			CONFIG_ASYNC,
			CONFIG_ASYNC_MARKET,
			CONFIG_ASYNC_MARKET_SNAPSHOT,
			CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK, CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP, 
		
			CONFIG_ORDER,
			CONFIG_ORDER_TIF, CONFIG_ORDER_QUANTITY_INT,
			
			CONFIG_CONTRACT,
			CONFIG_CONTRACT_SECURITY_TYPE, CONFIG_CONTRACT_CURRENCY, 
			CONFIG_CONTRACT_EXCHANGE, CONFIG_CONTRACT_PRIMARY_EXCHANGE,
			
			CONFIG_DB_FIELD, 
			CONFIG_DB_FIELD_SIZE, CONFIG_DB_FIELD_TIME, CONFIG_DB_FIELD_SYMBOL, 
			CONFIG_DB_FIELD_PRICE, CONFIG_DB_FIELD_OPEN, CONFIG_DB_FIELD_CLOSE, 
			CONFIG_DB_FIELD_LOW, CONFIG_DB_FIELD_HIGH, CONFIG_DB_FIELD_VOLUME, 
			CONFIG_DB_FIELD_ASK, CONFIG_DB_FIELD_ASK_SIZE, CONFIG_DB_FIELD_BID, 
			CONFIG_DB_FIELD_BID_SIZE, CONFIG_DB_FIELD_HALTED, CONFIG_DB_FIELD_HALTED_TOT,

			CONFIG_DB_IB,		
			CONFIG_DB_IB_MARKET,
			CONFIG_DB_IB_MARKET_SOURCE,
			
			ID_ASYNC,
			ID_ASYNC_MARKET,
			ID_COMMON, ID_CONN, ID_ORDERS, ID_SYNC,
			
			CONN,
			CONN_TWS_REAL, CONN_TWS_PAPER, CONN_GATEWAY_REAL, CONN_GATEWAY_PAPER,

			SYNC,
			SYNC_GET,
			SYNC_GET_FUNDS, SYNC_GET_ID, SYNC_GET_IDS,
			SYNC_OUT,
			SYNC_OUT_INT, SYNC_OUT_DECIMAL, SYNC_OUT_INTS, SYNC_OUT_MISC,

			ASYNC,
			ASYNC_MARKET,
			ASYNC_MARKET_SNAPSHOT, ASYNC_MARKET_STREAM,
			
			ORDER,
			ORDER_PLACE,
			ORDER_PLACE_MARKET, ORDER_PLACE_STOP, ORDER_PLACE_LIMIT,
			ORDER_UPDATE,
			ORDER_UPDATE_START_VALUE, ORDER_UPDATE_START_MARKET, ORDER_UPDATE_STOP_VALUE, ORDER_UPDATE_STOP_MARKET,
			
			ERROR_IB,
			ERROR_IB_GENERIC,
			ERROR_IB_INI,
			ERROR_IB_INI_DB, 
			ERROR_IB_INI_DB_DBS,
			ERROR_IB_CONN,
			ERROR_IB_CONN_NONE, ERROR_IB_CONN_ID, ERROR_IB_CONN_TYPE, ERROR_IB_CONN_GENERIC,
			ERROR_IB_SYNC,
			ERROR_IB_SYNC_GET, ERROR_IB_SYNC_ID, ERROR_IB_SYNC_TIME,
			ERROR_IB_CONTRACT_INFO
		};		
	}
}