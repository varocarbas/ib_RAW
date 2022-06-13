package accessory_ib;

public class types
{
	public static final String CONFIG_CONN = "config_conn";
	public static final String CONFIG_CONN_TYPE = "config_conn_type";
	public static final String CONFIG_CONN_TYPE_TWS_REAL = "config_conn_type_tws_real";
	public static final String CONFIG_CONN_TYPE_TWS_PAPER = "config_conn_type_tws_paper";
	public static final String CONFIG_CONN_TYPE_GATEWAY_REAL = "config_conn_type_gateway_real";
	public static final String CONFIG_CONN_TYPE_GATEWAY_PAPER = "config_conn_type_gateway_paper";
	public static final String CONFIG_CONN_ID = "config_conn_id";
	
	public static final String CONFIG_ORDER = "config_order";
	public static final String CONFIG_ORDER_TIF = "config_order_tif";
	public static final String CONFIG_ORDER_QUANTITIES_INT = "config_order_quantities_int";
	
	public static final String CONFIG_ASYNC = "config_async";
	public static final String CONFIG_ASYNC_MARKET = "config_async_market";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT = "config_async_market_snapshot";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK = "config_async_market_snapshot_quick";
	public static final String CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP = "config_async_market_snapshot_nonstop";

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
	public static final String CONFIG_DB_FIELD_ENABLED = "config_db_field_enabled";

	public static final String CONFIG_DB_IB = "config_db_ib";
	public static final String CONFIG_DB_IB_MARKET = "config_db_ib_market";
	public static final String CONFIG_DB_IB_MARKET_SOURCE = "config_db_ib_market_source";

	public static final String ID_COMMON = "id_common";
	public static final String ID_CONN = "id_conn";
	public static final String ID_ASYNC = "id_async";
	public static final String ID_ASYNC_MARKET = "id_async_market";
	public static final String ID_SYNC = "id_sync";
	public static final String ID_SYNC_ORDERS = "id_sync_orders";	
	public static final String ID_CALLS = "id_calls";
	
	public static final String SYNC = "sync";
	public static final String SYNC_GET = "sync_get";
	public static final String SYNC_GET_FUNDS = "sync_get_funds";
	public static final String SYNC_GET_ID = "sync_get_id";
	public static final String SYNC_GET_ORDERS = "sync_get_orders";
	public static final String SYNC_ORDERS = "sync_orders";
	public static final String SYNC_ORDERS_CANCEL = "sync_orders_cancel";
	public static final String SYNC_ORDERS_PLACE = "sync_orders_place";
	public static final String SYNC_ORDERS_PLACE_MARKET = "sync_orders_place_market";
	public static final String SYNC_ORDERS_PLACE_STOP = "sync_orders_place_stop";
	public static final String SYNC_ORDERS_PLACE_LIMIT = "sync_orders_place_limit";
	public static final String SYNC_ORDERS_PLACE_STOP_LIMIT = "sync_orders_place_stop_limit";
	public static final String SYNC_ORDERS_UPDATE = "sync_orders_update";
	public static final String SYNC_ORDERS_UPDATE_START_VALUE = "sync_orders_update_start_value";
	public static final String SYNC_ORDERS_UPDATE_START_MARKET = "sync_orders_update_start_market";
	public static final String SYNC_ORDERS_UPDATE_STOP_VALUE = "sync_orders_update_stop_value";
	public static final String SYNC_ORDERS_UPDATE_STOP_MARKET = "sync_orders_update_stop_market";
	public static final String SYNC_ORDERS_STATUS = "sync_orders_status";
	public static final String SYNC_ORDERS_STATUS_SUBMITTED = "sync_orders_status_submitted";
	public static final String SYNC_ORDERS_STATUS_FILLED = "sync_orders_status_filled";
	public static final String SYNC_ORDERS_STATUS_ACTIVE = "sync_orders_status_active";
	public static final String SYNC_ORDERS_STATUS_INACTIVE = "sync_orders_status_inactive";
	public static final String SYNC_OUT = "sync_out";
	public static final String SYNC_OUT_INT = "sync_out_int";
	public static final String SYNC_OUT_DECIMAL = "sync_out_decimal";
	public static final String SYNC_OUT_INTS = "sync_out_ints";
	public static final String SYNC_OUT_STRINGS = "sync_out_strings";
	public static final String SYNC_OUT_ORDERS = "sync_out_orders";
	
	public static final String ASYNC = "async";
	public static final String ASYNC_MARKET = "async_market";
	public static final String ASYNC_MARKET_SNAPSHOT = "async_market_snapshot";
	public static final String ASYNC_MARKET_STREAM = "async_market_stream";

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
	public static final String ERROR_IB_SYNC_TIMEOUT = "error_ib_sync_timeout";
	public static final String ERROR_IB_CONTRACTS = "error_ib_contracts";
	public static final String ERROR_IB_CONTRACTS_INFO = "error_ib_contracts_info";
	
	static String[] populate_all_types()
	{		
		return new String[]
		{
			CONFIG_CONN,
			CONFIG_CONN_TYPE, 
			CONFIG_CONN_TYPE_TWS_REAL, CONFIG_CONN_TYPE_TWS_PAPER, CONFIG_CONN_TYPE_GATEWAY_REAL, 
			CONFIG_CONN_TYPE_GATEWAY_PAPER,
			
			CONFIG_CONN_ID,
			
			CONFIG_ORDER,
			CONFIG_ORDER_TIF, CONFIG_ORDER_QUANTITIES_INT,
	
			CONFIG_ASYNC,
			CONFIG_ASYNC_MARKET,
			CONFIG_ASYNC_MARKET_SNAPSHOT,
			CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK, CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP, 
			
			CONFIG_CONTRACT,
			CONFIG_CONTRACT_SECURITY_TYPE, CONFIG_CONTRACT_CURRENCY, 
			CONFIG_CONTRACT_EXCHANGE, CONFIG_CONTRACT_PRIMARY_EXCHANGE,
			
			CONFIG_DB_FIELD, 
			CONFIG_DB_FIELD_SIZE, CONFIG_DB_FIELD_TIME, CONFIG_DB_FIELD_SYMBOL, 
			CONFIG_DB_FIELD_PRICE, CONFIG_DB_FIELD_OPEN, CONFIG_DB_FIELD_CLOSE, 
			CONFIG_DB_FIELD_LOW, CONFIG_DB_FIELD_HIGH, CONFIG_DB_FIELD_VOLUME, 
			CONFIG_DB_FIELD_ASK, CONFIG_DB_FIELD_ASK_SIZE, CONFIG_DB_FIELD_BID, 
			CONFIG_DB_FIELD_BID_SIZE, CONFIG_DB_FIELD_HALTED, CONFIG_DB_FIELD_HALTED_TOT,
			CONFIG_DB_FIELD_ENABLED, 
		
			CONFIG_DB_IB,		
			CONFIG_DB_IB_MARKET,
			CONFIG_DB_IB_MARKET_SOURCE,
			
			ID_ASYNC,
			ID_ASYNC_MARKET,
			ID_SYNC,
			ID_SYNC_ORDERS,
			ID_COMMON, ID_CONN, ID_CALLS,

			SYNC,
			SYNC_GET,
			SYNC_GET_FUNDS, SYNC_GET_ID, SYNC_GET_ORDERS,
			SYNC_OUT,
			SYNC_OUT_INT, SYNC_OUT_DECIMAL, SYNC_OUT_INTS, SYNC_OUT_STRINGS, SYNC_OUT_ORDERS,
			SYNC_ORDERS,
			SYNC_ORDERS_CANCEL,
			SYNC_ORDERS_PLACE,
			SYNC_ORDERS_PLACE_MARKET, SYNC_ORDERS_PLACE_STOP, SYNC_ORDERS_PLACE_LIMIT, SYNC_ORDERS_PLACE_STOP_LIMIT,
			SYNC_ORDERS_UPDATE,
			SYNC_ORDERS_UPDATE_START_VALUE, SYNC_ORDERS_UPDATE_START_MARKET, SYNC_ORDERS_UPDATE_STOP_VALUE, 
			SYNC_ORDERS_UPDATE_STOP_MARKET,
			SYNC_ORDERS_STATUS,
			SYNC_ORDERS_STATUS_SUBMITTED, SYNC_ORDERS_STATUS_FILLED, SYNC_ORDERS_STATUS_ACTIVE, SYNC_ORDERS_STATUS_INACTIVE,
			
			ASYNC,
			ASYNC_MARKET,
			ASYNC_MARKET_SNAPSHOT, ASYNC_MARKET_STREAM,
						
			ERROR_IB,
			ERROR_IB_GENERIC,
			ERROR_IB_INI,
			ERROR_IB_INI_DB, 
			ERROR_IB_INI_DB_DBS,
			ERROR_IB_CONN,
			ERROR_IB_CONN_NONE, ERROR_IB_CONN_ID, ERROR_IB_CONN_TYPE, ERROR_IB_CONN_GENERIC,
			ERROR_IB_SYNC,
			ERROR_IB_SYNC_TIMEOUT,
			ERROR_IB_CONTRACTS_INFO
		};		
	}
}