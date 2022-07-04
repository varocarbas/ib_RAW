package accessory_ib;

public abstract class types
{
	public static final String CONFIG_BASIC_ID = "config_basic_id";
	
	public static final String CONFIG_CONN = "config_conn";
	public static final String CONFIG_CONN_TYPE = "config_conn_type";
	public static final String CONFIG_CONN_TYPE_TWS_REAL = "config_conn_type_tws_real";
	public static final String CONFIG_CONN_TYPE_TWS_PAPER = "config_conn_type_tws_paper";
	public static final String CONFIG_CONN_TYPE_GATEWAY_REAL = "config_conn_type_gateway_real";
	public static final String CONFIG_CONN_TYPE_GATEWAY_PAPER = "config_conn_type_gateway_paper";
	public static final String CONFIG_CONN_ID = "config_conn_id";
	public static final String CONFIG_CONN_ACCOUNT_ID = "config_conn_account_id";
	
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
	public static final String CONFIG_CONTRACT_EXCHANGE_PRIMARY = "config_contract_exchange_primary";
	public static final String CONFIG_CONTRACT_EXCHANGE_COUNTRY = "config_contract_exchange_country";
	public static final String CONFIG_CONTRACT_EXCHANGE_COUNTRY_US = "config_contract_exchange_country_us";

	public static final String CONFIG_DB_IB = "config_db_ib";
	public static final String CONFIG_DB_IB_MARKET = "config_db_ib_market";
	public static final String CONFIG_DB_IB_MARKET_SOURCE = "config_db_ib_market_source";
	public static final String CONFIG_DB_IB_EXECS = "config_db_ib_execs";
	public static final String CONFIG_DB_IB_EXECS_SOURCE = "config_db_ib_execs_source";
	public static final String CONFIG_DB_IB_BASIC = "config_db_ib_basic";
	public static final String CONFIG_DB_IB_BASIC_SOURCE = "config_db_ib_basic_source";
	public static final String CONFIG_DB_IB_REMOTE = "config_db_ib_remote";
	public static final String CONFIG_DB_IB_REMOTE_SOURCE = "config_db_ib_remote_source";
	public static final String CONFIG_DB_IB_ORDERS = "config_db_ib_orders";
	public static final String CONFIG_DB_IB_ORDERS_SOURCE = "config_db_ib_orders_source";
	public static final String CONFIG_DB_IB_TRADES = "config_db_ib_trades";
	public static final String CONFIG_DB_IB_TRADES_SOURCE = "config_db_ib_trades_source";
	public static final String CONFIG_DB_IB_WATCHLIST = "config_db_ib_watchlist";
	public static final String CONFIG_DB_IB_WATCHLIST_SOURCE = "config_db_ib_watchlist_source";

	public static final String CONFIG_DB_IB_FIELD = "config_db_ib_field";
	public static final String CONFIG_DB_IB_FIELD_USER = "config_db_ib_field_user";
	public static final String CONFIG_DB_IB_FIELD_SIZE = "config_db_ib_field_size";
	public static final String CONFIG_DB_IB_FIELD_TIME = "config_db_ib_field_time";
	public static final String CONFIG_DB_IB_FIELD_SYMBOL = "config_db_ib_field_symbol";
	public static final String CONFIG_DB_IB_FIELD_PRICE = "config_db_ib_field_price";
	public static final String CONFIG_DB_IB_FIELD_OPEN = "config_db_ib_field_open";
	public static final String CONFIG_DB_IB_FIELD_CLOSE = "config_db_ib_field_close";
	public static final String CONFIG_DB_IB_FIELD_LOW = "config_db_ib_field_low";
	public static final String CONFIG_DB_IB_FIELD_HIGH = "config_db_ib_field_high";
	public static final String CONFIG_DB_IB_FIELD_VOLUME = "config_db_ib_field_volume";
	public static final String CONFIG_DB_IB_FIELD_ASK = "config_db_ib_field_ask";
	public static final String CONFIG_DB_IB_FIELD_ASK_SIZE = "config_db_ib_field_ask_size";
	public static final String CONFIG_DB_IB_FIELD_BID = "config_db_ib_field_bid";
	public static final String CONFIG_DB_IB_FIELD_BID_SIZE = "config_db_ib_field_bid_size";
	public static final String CONFIG_DB_IB_FIELD_HALTED = "config_db_ib_field_halted";
	public static final String CONFIG_DB_IB_FIELD_HALTED_TOT = "config_db_ib_field_halted_tot";
	public static final String CONFIG_DB_IB_FIELD_ENABLED = "config_db_ib_field_enabled";	

	public static final String CONFIG_DB_IB_FIELD_ORDER_ID = "config_db_ib_field_order_id";
	public static final String CONFIG_DB_IB_FIELD_QUANTITY = "config_db_ib_field_quantity";
	public static final String CONFIG_DB_IB_FIELD_SIDE = "config_db_ib_field_side";
	public static final String CONFIG_DB_IB_FIELD_FEES = "config_db_ib_field_fees";
	public static final String CONFIG_DB_IB_FIELD_EXEC_ID = "config_db_ib_field_exec_id";

	public static final String CONFIG_DB_IB_FIELD_MONEY = "config_db_ib_field_money";
	public static final String CONFIG_DB_IB_FIELD_MONEY_INI = "config_db_ib_field_money_ini";
	public static final String CONFIG_DB_IB_FIELD_CONN_TYPE = "config_db_ib_field_conn_type";
	public static final String CONFIG_DB_IB_FIELD_ACCOUNT_ID = "config_db_ib_field_account_id";
	public static final String CONFIG_DB_IB_FIELD_CURRENCY = "config_db_ib_field_currency";

	public static final String CONFIG_DB_IB_FIELD_START = "config_db_ib_field_start";
	public static final String CONFIG_DB_IB_FIELD_START2 = "config_db_ib_field_start2";
	public static final String CONFIG_DB_IB_FIELD_STOP = "config_db_ib_field_stop";
	public static final String CONFIG_DB_IB_FIELD_ORDER_ID_MAIN = "config_db_ib_field_order_id_main";
	public static final String CONFIG_DB_IB_FIELD_ORDER_ID_SEC = "config_db_ib_field_order_id_sec";
	public static final String CONFIG_DB_IB_FIELD_STATUS = "config_db_ib_field_status";
	public static final String CONFIG_DB_IB_FIELD_STATUS2 = "config_db_ib_field_status2";
	public static final String CONFIG_DB_IB_FIELD_IS_MARKET = "config_db_ib_field_is_market";

	public static final String CONFIG_DB_IB_FIELD_TYPE_PLACE = "config_db_ib_field_type_place";
	public static final String CONFIG_DB_IB_FIELD_TYPE_MAIN = "config_db_ib_field_type_main";
	public static final String CONFIG_DB_IB_FIELD_TYPE_SEC = "config_db_ib_field_type_sec";
	
	public static final String CONFIG_DB_IB_FIELD_TIME_ELAPSED = "config_db_ib_field_time_elapsed";
	public static final String CONFIG_DB_IB_FIELD_UNREALISED = "config_db_ib_field_unrealised";
	
	public static final String CONFIG_DB_IB_FIELD_PRICE_INI = "config_db_ib_field_price_ini";
	public static final String CONFIG_DB_IB_FIELD_PRICE_MIN = "config_db_ib_field_price_min";
	public static final String CONFIG_DB_IB_FIELD_PRICE_MAX = "config_db_ib_field_price_max";
	public static final String CONFIG_DB_IB_FIELD_VOLUME_INI = "config_db_ib_field_volume_ini";
	public static final String CONFIG_DB_IB_FIELD_VOLUME_MIN = "config_db_ib_field_volume_min";
	public static final String CONFIG_DB_IB_FIELD_VOLUME_MAX = "config_db_ib_field_volume_max";
	public static final String CONFIG_DB_IB_FIELD_FLU = "config_db_ib_field_flu";
	public static final String CONFIG_DB_IB_FIELD_FLU2 = "config_db_ib_field_flu2";
	public static final String CONFIG_DB_IB_FIELD_FLU2_MIN = "config_db_ib_field_flu2_min";
	public static final String CONFIG_DB_IB_FIELD_FLU2_MAX = "config_db_ib_field_flu2_max";	

	public static final String ORDER = "order";
	public static final String ORDER_CANCEL = "order_cancel";
	public static final String ORDER_PLACE = "order_place";
	public static final String ORDER_PLACE_MARKET = "order_place_market";
	public static final String ORDER_PLACE_STOP = "order_place_stop";
	public static final String ORDER_PLACE_LIMIT = "order_place_limit";
	public static final String ORDER_PLACE_STOP_LIMIT = "order_place_stop_limit";
	public static final String ORDER_UPDATE = "order_update";
	public static final String ORDER_UPDATE_START = "order_update_start";
	public static final String ORDER_UPDATE_START_VALUE = "order_update_start_value";
	public static final String ORDER_UPDATE_START2 = "order_update_start2";
	public static final String ORDER_UPDATE_START2_VALUE = "order_update_start2_value";
	public static final String ORDER_UPDATE_START_MARKET = "order_update_start_market";
	public static final String ORDER_UPDATE_STOP = "order_update_stop";
	public static final String ORDER_UPDATE_STOP_VALUE = "order_update_stop_value";
	public static final String ORDER_UPDATE_STOP_MARKET = "order_update_stop_market";
	public static final String ORDER_STATUS = "order_status";
	public static final String ORDER_STATUS_SUBMITTED = "order_status_submitted";
	public static final String ORDER_STATUS_FILLED = "order_status_filled";
	public static final String ORDER_STATUS_ACTIVE = "order_status_active";
	public static final String ORDER_STATUS_INACTIVE = "order_status_inactive";	

	public static final String SYNC = "sync";
	public static final String SYNC_GET = "sync_get";
	public static final String SYNC_GET_FUNDS = "sync_get_funds";
	public static final String SYNC_GET_ID = "sync_get_id";
	public static final String SYNC_GET_ORDERS = "sync_get_orders";
	public static final String SYNC_GET_ERROR = "sync_get_error";
	public static final String SYNC_ORDERS = ORDER;
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
	public static final String ERROR_IB_CONTRACTS_INFO_STOCK_ETF = "error_ib_contracts_info_stock_etf";
	
	static String[] populate_all_types()
	{		
		return new String[]
		{
			CONFIG_BASIC_ID,
				
			CONFIG_CONN,
			CONFIG_CONN_TYPE, 
			CONFIG_CONN_TYPE_TWS_REAL, CONFIG_CONN_TYPE_TWS_PAPER, CONFIG_CONN_TYPE_GATEWAY_REAL, 
			CONFIG_CONN_TYPE_GATEWAY_PAPER,
			CONFIG_CONN_ID,
			CONFIG_CONN_ACCOUNT_ID,
			
			CONFIG_ORDER,
			CONFIG_ORDER_TIF, CONFIG_ORDER_QUANTITIES_INT,
	
			CONFIG_ASYNC,
			CONFIG_ASYNC_MARKET,
			CONFIG_ASYNC_MARKET_SNAPSHOT,
			CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK, CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP, 
			
			CONFIG_CONTRACT,
			CONFIG_CONTRACT_SECURITY_TYPE, CONFIG_CONTRACT_CURRENCY, 
			CONFIG_CONTRACT_EXCHANGE,
			CONFIG_CONTRACT_EXCHANGE_PRIMARY, 
			CONFIG_CONTRACT_EXCHANGE_COUNTRY,
			CONFIG_CONTRACT_EXCHANGE_COUNTRY_US,
			
			CONFIG_DB_IB,		
			CONFIG_DB_IB_MARKET,
			CONFIG_DB_IB_MARKET_SOURCE,
			CONFIG_DB_IB_EXECS,
			CONFIG_DB_IB_EXECS_SOURCE,
			CONFIG_DB_IB_BASIC,
			CONFIG_DB_IB_BASIC_SOURCE,
			CONFIG_DB_IB_REMOTE,
			CONFIG_DB_IB_REMOTE_SOURCE,
			CONFIG_DB_IB_ORDERS,
			CONFIG_DB_IB_ORDERS_SOURCE,
			CONFIG_DB_IB_TRADES,
			CONFIG_DB_IB_TRADES_SOURCE,
			CONFIG_DB_IB_WATCHLIST,
			CONFIG_DB_IB_WATCHLIST_SOURCE,
			
			CONFIG_DB_IB_FIELD,
			CONFIG_DB_IB_FIELD_USER, CONFIG_DB_IB_FIELD_SIZE, CONFIG_DB_IB_FIELD_TIME, CONFIG_DB_IB_FIELD_SYMBOL, 
			CONFIG_DB_IB_FIELD_PRICE, CONFIG_DB_IB_FIELD_OPEN, CONFIG_DB_IB_FIELD_CLOSE, CONFIG_DB_IB_FIELD_LOW, 
			CONFIG_DB_IB_FIELD_HIGH, CONFIG_DB_IB_FIELD_VOLUME, CONFIG_DB_IB_FIELD_ASK, CONFIG_DB_IB_FIELD_ASK_SIZE, 
			CONFIG_DB_IB_FIELD_BID, CONFIG_DB_IB_FIELD_BID_SIZE, CONFIG_DB_IB_FIELD_HALTED, CONFIG_DB_IB_FIELD_HALTED_TOT,
			CONFIG_DB_IB_FIELD_ENABLED, CONFIG_DB_IB_FIELD_ORDER_ID, CONFIG_DB_IB_FIELD_QUANTITY, CONFIG_DB_IB_FIELD_SIDE, 
			CONFIG_DB_IB_FIELD_FEES, CONFIG_DB_IB_FIELD_EXEC_ID, CONFIG_DB_IB_FIELD_MONEY, CONFIG_DB_IB_FIELD_MONEY_INI, 
			CONFIG_DB_IB_FIELD_CONN_TYPE, CONFIG_DB_IB_FIELD_ACCOUNT_ID, CONFIG_DB_IB_FIELD_CURRENCY, CONFIG_DB_IB_FIELD_START, 
			CONFIG_DB_IB_FIELD_START2, CONFIG_DB_IB_FIELD_STOP, CONFIG_DB_IB_FIELD_ORDER_ID_MAIN, CONFIG_DB_IB_FIELD_ORDER_ID_SEC, 
			CONFIG_DB_IB_FIELD_STATUS, CONFIG_DB_IB_FIELD_STATUS2, CONFIG_DB_IB_FIELD_IS_MARKET, CONFIG_DB_IB_FIELD_TYPE_PLACE, 
			CONFIG_DB_IB_FIELD_TYPE_MAIN, CONFIG_DB_IB_FIELD_TYPE_SEC, CONFIG_DB_IB_FIELD_TIME_ELAPSED, CONFIG_DB_IB_FIELD_UNREALISED,
			CONFIG_DB_IB_FIELD_PRICE_INI, CONFIG_DB_IB_FIELD_PRICE_MIN, CONFIG_DB_IB_FIELD_PRICE_MAX, CONFIG_DB_IB_FIELD_VOLUME_INI,
			CONFIG_DB_IB_FIELD_VOLUME_MIN, CONFIG_DB_IB_FIELD_VOLUME_MAX, CONFIG_DB_IB_FIELD_FLU, CONFIG_DB_IB_FIELD_FLU2, 
			CONFIG_DB_IB_FIELD_FLU2_MIN, CONFIG_DB_IB_FIELD_FLU2_MAX,
			
			ORDER,
			ORDER_CANCEL, ORDER_PLACE, ORDER_PLACE_MARKET, ORDER_PLACE_STOP, ORDER_PLACE_LIMIT, ORDER_PLACE_STOP_LIMIT, ORDER_UPDATE,
			ORDER_UPDATE_START, ORDER_UPDATE_START_VALUE, ORDER_UPDATE_START2, ORDER_UPDATE_START2_VALUE, ORDER_UPDATE_START_MARKET,
			ORDER_UPDATE_STOP, ORDER_UPDATE_STOP_VALUE, ORDER_UPDATE_STOP_MARKET, ORDER_STATUS, ORDER_STATUS_SUBMITTED, ORDER_STATUS_FILLED,
			ORDER_STATUS_ACTIVE, ORDER_STATUS_INACTIVE,
			
			SYNC,
			SYNC_GET,
			SYNC_GET_FUNDS, SYNC_GET_ID, SYNC_GET_ORDERS, SYNC_GET_ERROR,
			SYNC_OUT,
			SYNC_OUT_INT, SYNC_OUT_DECIMAL, SYNC_OUT_INTS, SYNC_OUT_STRINGS, SYNC_OUT_ORDERS,
			SYNC_ORDERS,
			
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
			ERROR_IB_CONTRACTS_INFO_STOCK_ETF
		};		
	}
}