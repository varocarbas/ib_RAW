package accessory_ib;

import accessory.parent_types;

public class _types extends parent_types
{
	private static _types _instance = new _types(); 

	public _types() { }
	public static void populate() { _instance.populate_internal(); }

	public static final String CONFIG_BASIC_IB = "config_basic_ib";
	public static final String CONFIG_BASIC_IB_ID_MAIN = "config_basic_ib_id_main";
	public static final String CONFIG_BASIC_IB_DIR = "config_basic_ib_dir";
	public static final String CONFIG_BASIC_IB_DIR_TWS = "config_basic_ib_dir_tws";
	public static final String CONFIG_BASIC_IB_DIR_GATEWAY = "config_basic_ib_dir_gateway";	
	public static final String CONFIG_BASIC_IB_PATH_MARKET_HOLIDAYS = "config_basic_ib_path_market_holidays";
	public static final String CONFIG_BASIC_IB_PATH_MARKET_EARLY_CLOSES = "config_basic_ib_path_market_early_closes";
	
	public static final String CONFIG_ORDERS = "config_orders";
	public static final String CONFIG_ORDERS_TIF = "config_orders_tif";
	public static final String CONFIG_ORDERS_QUANTITIES_INT = "config_orders_quantities_int";

	public static final String CONFIG_CONTRACT = "config_contract";
	public static final String CONFIG_CONTRACT_SECURITY_TYPE = "config_contract_security_type";
	public static final String CONFIG_CONTRACT_CURRENCY = "config_contract_currency";
	public static final String CONFIG_CONTRACT_EXCHANGE = "config_contract_exchange";
	public static final String CONFIG_CONTRACT_EXCHANGE_PRIMARY = "config_contract_exchange_primary";
	public static final String CONFIG_CONTRACT_EXCHANGE_COUNTRY = "config_contract_exchange_country";
	public static final String CONFIG_CONTRACT_EXCHANGE_COUNTRY_US = "config_contract_exchange_country_us";

	public static final String CONFIG_CONN = "config_conn";
	public static final String CONFIG_CONN_CHECK = "config_conn_check";
	public static final String CONFIG_CONN_CHECK_RUNNING = "config_conn_check_running";
	
	public static final String CONFIG_DB_IB = "config_db_ib";
	public static final String CONFIG_DB_IB_MARKET = "config_db_ib_market";
	public static final String CONFIG_DB_IB_MARKET_SOURCE = "config_db_ib_market_source";
	public static final String CONFIG_DB_IB_EXECS = "config_db_ib_execs";
	public static final String CONFIG_DB_IB_EXECS_SOURCE = "config_db_ib_execs_source";
	public static final String CONFIG_DB_IB_EXECS_OLD = "config_db_ib_execs_old";
	public static final String CONFIG_DB_IB_EXECS_OLD_SOURCE = "config_db_ib_execs_old_source";
	public static final String CONFIG_DB_IB_BASIC = "config_db_ib_basic";
	public static final String CONFIG_DB_IB_BASIC_SOURCE = "config_db_ib_basic_source";
	public static final String CONFIG_DB_IB_BASIC_OLD = "config_db_ib_basic_old";
	public static final String CONFIG_DB_IB_BASIC_OLD_SOURCE = "config_db_ib_basic_old_source";
	public static final String CONFIG_DB_IB_REMOTE = "config_db_ib_remote";
	public static final String CONFIG_DB_IB_REMOTE_SOURCE = "config_db_ib_remote_source";
	public static final String CONFIG_DB_IB_REMOTE_OLD = "config_db_ib_remote_old";
	public static final String CONFIG_DB_IB_REMOTE_OLD_SOURCE = "config_db_ib_remote_old_source";
	public static final String CONFIG_DB_IB_ORDERS = "config_db_ib_orders";
	public static final String CONFIG_DB_IB_ORDERS_SOURCE = "config_db_ib_orders_source";
	public static final String CONFIG_DB_IB_ORDERS_OLD = "config_db_ib_orders_old";
	public static final String CONFIG_DB_IB_ORDERS_OLD_SOURCE = "config_db_ib_orders_old_source";
	public static final String CONFIG_DB_IB_TRADES = "config_db_ib_trades";
	public static final String CONFIG_DB_IB_TRADES_SOURCE = "config_db_ib_trades_source";
	public static final String CONFIG_DB_IB_TRADES_OLD = "config_db_ib_trades_old";
	public static final String CONFIG_DB_IB_TRADES_OLD_SOURCE = "config_db_ib_trades_old_source";
	public static final String CONFIG_DB_IB_WATCHLIST = "config_db_ib_watchlist";
	public static final String CONFIG_DB_IB_WATCHLIST_SOURCE = "config_db_ib_watchlist_source";
	public static final String CONFIG_DB_IB_APPS = "config_db_ib_apps";
	public static final String CONFIG_DB_IB_APPS_SOURCE = "config_db_ib_apps_source";
	public static final String CONFIG_DB_IB_APPS_OLD = "config_db_ib_apps_old";
	public static final String CONFIG_DB_IB_APPS_OLD_SOURCE = "config_db_ib_apps_old_source";

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
	public static final String CONFIG_DB_IB_FIELD_ACCOUNT_IB = "config_db_ib_field_account_ib";
	public static final String CONFIG_DB_IB_FIELD_CURRENCY = "config_db_ib_field_currency";
	public static final String CONFIG_DB_IB_FIELD_MONEY_FREE = "config_db_ib_field_money_free";

	public static final String CONFIG_DB_IB_FIELD_START = "config_db_ib_field_start";
	public static final String CONFIG_DB_IB_FIELD_START2 = "config_db_ib_field_start2";
	public static final String CONFIG_DB_IB_FIELD_STOP = "config_db_ib_field_stop";
	public static final String CONFIG_DB_IB_FIELD_ORDER_ID_MAIN = "config_db_ib_field_order_id_main";
	public static final String CONFIG_DB_IB_FIELD_ORDER_ID_SEC = "config_db_ib_field_order_id_sec";
	public static final String CONFIG_DB_IB_FIELD_STATUS = "config_db_ib_field_status";
	public static final String CONFIG_DB_IB_FIELD_STATUS2 = "config_db_ib_field_status2";
	public static final String CONFIG_DB_IB_FIELD_IS_MARKET = "config_db_ib_field_is_market";
	public static final String CONFIG_DB_IB_FIELD_PERC_MONEY = "config_db_ib_field_perc_money";
	public static final String CONFIG_DB_IB_FIELD_REQUEST = "config_db_ib_field_request";
	public static final String CONFIG_DB_IB_FIELD_TYPE_ORDER = "config_db_ib_field_type_order";

	public static final String CONFIG_DB_IB_FIELD_TYPE_PLACE = "config_db_ib_field_type_place";
	public static final String CONFIG_DB_IB_FIELD_TYPE_MAIN = "config_db_ib_field_type_main";
	public static final String CONFIG_DB_IB_FIELD_TYPE_SEC = "config_db_ib_field_type_sec";
	
	public static final String CONFIG_DB_IB_FIELD_TIME_ELAPSED = "config_db_ib_field_time_elapsed";
	public static final String CONFIG_DB_IB_FIELD_ELAPSED_INI = "config_db_ib_field_elapsed_ini";
	public static final String CONFIG_DB_IB_FIELD_UNREALISED = "config_db_ib_field_unrealised";
	public static final String CONFIG_DB_IB_FIELD_IS_ACTIVE = "config_db_ib_field_is_active";
	public static final String CONFIG_DB_IB_FIELD_INVESTMENT = "config_db_ib_field_investment";
	public static final String CONFIG_DB_IB_FIELD_END = "config_db_ib_field_end";
	public static final String CONFIG_DB_IB_FIELD_REALISED = "config_db_ib_field_realised";

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
	public static final String CONFIG_DB_IB_FIELD_FLUS_PRICE = "config_db_ib_field_flus_price";
	public static final String CONFIG_DB_IB_FIELD_VAR_TOT = "config_db_ib_field_var_tot";	

	public static final String CONFIG_DB_IB_FIELD_APP = "config_db_ib_field_app";	
	public static final String CONFIG_DB_IB_FIELD_CONN_ID = "config_db_ib_field_conn_id";
	public static final String CONFIG_DB_IB_FIELD_CONN_TYPE = "config_db_ib_field_conn_type";
	public static final String CONFIG_DB_IB_FIELD_CONN_IS_ON = "config_db_ib_field_conn_is_on";	
	public static final String CONFIG_DB_IB_FIELD_COUNT = "config_db_ib_field_count";	
	public static final String CONFIG_DB_IB_FIELD_ERROR = "config_db_ib_field_error";	
	public static final String CONFIG_DB_IB_FIELD_ADDITIONAL = "config_db_ib_field_additional";	
	public static final String CONFIG_DB_IB_FIELD_TIME2 = "config_db_ib_field_time2";	

	public static final String CONFIG_DB_IB_FIELD_ID = "config_db_ib_field_id";
	public static final String CONFIG_DB_IB_FIELD_TYPE = "config_db_ib_field_type";
	public static final String CONFIG_DB_IB_FIELD_DATA_TYPE = "config_db_ib_field_data_type";

	public static final String CONFIG_DB_IB_FIELD_DATE = "config_db_ib_field_date";

	public static final String CONFIG_DB_IB_FIELD_KEY = "config_db_ib_field_key";
	public static final String CONFIG_DB_IB_FIELD_VALUE = "config_db_ib_field_value";

	public static final String CONN = "conn";
	public static final String CONN_TYPE = "conn_type";
	public static final String CONN_TYPE_TWS_REAL = "conn_type_tws_real";
	public static final String CONN_TYPE_TWS_PAPER = "conn_type_tws_paper";
	public static final String CONN_TYPE_GATEWAY_REAL = "conn_type_gateway_real";
	public static final String CONN_TYPE_GATEWAY_PAPER = "conn_type_gateway_paper";
	
	public static final String ORDERS = "orders";
	public static final String ORDERS_CANCEL = "orders_cancel";
	public static final String ORDERS_PLACE = "orders_place";
	public static final String ORDERS_PLACE_MARKET = "orders_place_market";
	public static final String ORDERS_PLACE_STOP = "orders_place_stop";
	public static final String ORDERS_PLACE_LIMIT = "orders_place_limit";
	public static final String ORDERS_PLACE_STOP_LIMIT = "orders_place_stop_limit";
	public static final String ORDERS_UPDATE = "orders_update";
	public static final String ORDERS_UPDATE_START = "orders_update_start";
	public static final String ORDERS_UPDATE_START_VALUE = "orders_update_start_value";
	public static final String ORDERS_UPDATE_START_MARKET = "orders_update_start_market";
	public static final String ORDERS_UPDATE_START2 = "orders_update_start2";
	public static final String ORDERS_UPDATE_START2_VALUE = "orders_update_start2_value";
	public static final String ORDERS_UPDATE_STOP = "orders_update_stop";
	public static final String ORDERS_UPDATE_STOP_VALUE = "orders_update_stop_value";
	public static final String ORDERS_UPDATE_STOP_MARKET = "orders_update_stop_market";
	public static final String ORDERS_STATUS = "orders_status";
	public static final String ORDERS_STATUS_SUBMITTED = "orders_status_submitted";
	public static final String ORDERS_STATUS_FILLED = "orders_status_filled";
	public static final String ORDERS_STATUS_ACTIVE = "orders_status_active";
	public static final String ORDERS_STATUS_INACTIVE = "orders_status_inactive";	
	public static final String ORDERS_STATUS_IN_PROGRESS = "orders_status_in_progress";	

	public static final String APPS = "apps";
	public static final String APPS_STATUS = "apps_status";
	public static final String APPS_STATUS_STOPPED = "apps_status_stopped";
	public static final String APPS_STATUS_RUNNING = "apps_status_running";
	public static final String APPS_STATUS_ERROR = "apps_status_error";
	
	public static final String REMOTE = "remote";
	public static final String REMOTE_STATUS = "remote_status";
	public static final String REMOTE_STATUS_ACTIVE = "remote_status_active";
	public static final String REMOTE_STATUS_INACTIVE = "remote_status_inactive";
	public static final String REMOTE_STATUS_ERROR = "remote_status_error";
	public static final String REMOTE_STATUS2 = "remote_status2";
	public static final String REMOTE_STATUS2_PENDING = "remote_status2_pending";
	public static final String REMOTE_STATUS2_EXECUTED = "remote_status2_executed";
	public static final String REMOTE_STATUS2_ERROR = "remote_status2_error";
	public static final String REMOTE_REQUEST = "remote_request";
	public static final String REMOTE_REQUEST_OK = "remote_request_ok";
	public static final String REMOTE_REQUEST_ERROR = "remote_request_error";
	public static final String REMOTE_REQUEST_IGNORED = "remote_request_ignored";
	public static final String REMOTE_ORDERS = "remote_orders";
	public static final String REMOTE_ORDERS_ROOT = ORDERS;
	public static final String REMOTE_ORDERS_CANCEL = ORDERS_CANCEL;
	public static final String REMOTE_ORDERS_PLACE = ORDERS_PLACE;
	public static final String REMOTE_ORDERS_PLACE_MARKET = ORDERS_PLACE_MARKET;
	public static final String REMOTE_ORDERS_PLACE_STOP = ORDERS_PLACE_STOP;
	public static final String REMOTE_ORDERS_PLACE_LIMIT = ORDERS_PLACE_LIMIT;
	public static final String REMOTE_ORDERS_PLACE_STOP_LIMIT = ORDERS_PLACE_STOP_LIMIT;
	public static final String REMOTE_ORDERS_UPDATE = ORDERS_UPDATE;
	public static final String REMOTE_ORDERS_UPDATE_START = ORDERS_UPDATE_START;
	public static final String REMOTE_ORDERS_UPDATE_START_VALUE = ORDERS_UPDATE_START_VALUE;
	public static final String REMOTE_ORDERS_UPDATE_START_MARKET = ORDERS_UPDATE_START_MARKET;
	public static final String REMOTE_ORDERS_UPDATE_START2 = ORDERS_UPDATE_START2;
	public static final String REMOTE_ORDERS_UPDATE_START2_VALUE = ORDERS_UPDATE_START2_VALUE;
	public static final String REMOTE_ORDERS_UPDATE_STOP = ORDERS_UPDATE_STOP;
	public static final String REMOTE_ORDERS_UPDATE_STOP_VALUE = ORDERS_UPDATE_STOP_VALUE;
	public static final String REMOTE_ORDERS_UPDATE_STOP_MARKET = ORDERS_UPDATE_STOP_MARKET;

	public static final String SYNC = "sync";
	public static final String SYNC_GET = "sync_get";
	public static final String SYNC_GET_FUNDS = "sync_get_funds";
	public static final String SYNC_GET_ORDER_ID = "sync_get_order_id";
	public static final String SYNC_GET_ORDERS = "sync_get_orders";
	public static final String SYNC_GET_ERROR = "sync_get_error";
	public static final String SYNC_ORDERS = ORDERS;
	public static final String SYNC_OUT = "sync_out";
	public static final String SYNC_OUT_INT = "sync_out_int";
	public static final String SYNC_OUT_ORDERS = "sync_out_orders";
	public static final String SYNC_OUT_FUNDS = "sync_out_funds";
	
	public static final String ASYNC = "async";
	public static final String ASYNC_DATA = "async_data";
	public static final String ASYNC_DATA_SNAPSHOT = "async_data_snapshot";
	public static final String ASYNC_DATA_STREAM = "async_data_stream";

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
	public static final String ERROR_IB_CONN_IB = "error_ib_conn_ib";
	
	public static final String ERROR_IB_SYNC = "error_ib_sync";
	public static final String ERROR_IB_SYNC_TIMEOUT = "error_ib_sync_timeout";
	public static final String ERROR_IB_CONTRACTS = "error_ib_contracts";
	public static final String ERROR_IB_CONTRACTS_INFO_STOCK_ETF = "error_ib_contracts_info_stock_etf";

	public static final String ERROR_IB_REMOTE = "error_ib_remote";
	public static final String ERROR_IB_REMOTE_EXECUTE = "error_ib_remote_execute";
	public static final String ERROR_IB_REMOTE_EXECUTE_TYPE = "error_ib_remote_execute_type";
	public static final String ERROR_IB_REMOTE_EXECUTE_QUANTITY = "error_ib_remote_execute_quantity";
	public static final String ERROR_IB_REMOTE_EXECUTE_ORDER_ID = "error_ib_remote_execute_order_id";
	public static final String ERROR_IB_REMOTE_EXECUTE_PLACE = "error_ib_remote_execute_place";
	public static final String ERROR_IB_REMOTE_EXECUTE_CANCEL = "error_ib_remote_execute_cancel";
	public static final String ERROR_IB_REMOTE_EXECUTE_UPDATE = "error_ib_remote_execute_update";
	public static final String ERROR_IB_REMOTE_REQUEST = "error_ib_remote_request";
	public static final String ERROR_IB_REMOTE_REQUEST_REQUEST = "error_ib_remote_request_request";
	public static final String ERROR_IB_REMOTE_REQUEST_PLACE = "error_ib_remote_request_place";
	public static final String ERROR_IB_REMOTE_REQUEST_CANCEL = "error_ib_remote_request_cancel";
	public static final String ERROR_IB_REMOTE_REQUEST_UPDATE = "error_ib_remote_request_update";
	public static final String ERROR_IB_REMOTE_REQUEST_WAIT = "error_ib_remote_request_wait";
	public static final String ERROR_IB_REMOTE_REQUEST_FAIL = "error_ib_remote_request_fail";
	
	protected String[] get_constant_names_to_ignore() { return null; }
}