package accessory_ib;

import accessory.arrays;
import accessory.strings;

public class types
{
	public static final String CONFIG_BASIC_CURRENCY = "config_basic_currency";

	public static final String CONFIG_CONN = "config_conn";
	public static final String CONFIG_CONN_TYPE = "config_conn_type";

	public static final String CONFIG_ASYNC = "config_async";
	public static final String CONFIG_ASYNC_SNAPSHOT_QUICK = "config_async_snapshot_quick";
	public static final String CONFIG_ASYNC_SNAPSHOT_CONSTANT = "config_async_snapshot_constant";
	public static final String CONFIG_ASYNC_STORAGE = "config_async_storage";
	public static final String CONFIG_ASYNC_STORAGE_MEMORY = "config_async_storage_memory";
	public static final String CONFIG_ASYNC_STORAGE_DB = "config_async_storage_db";

	public static final String CONFIG_ORDER = "config_order";
	public static final String CONFIG_ORDER_TIF = "config_order_tif";
	public static final String CONFIG_ORDER_QUANTITY_INT = "config_order_quantity_int";

	public static final String CONFIG_DB_IB = "config_db_ib";
	public static final String CONFIG_DB_IB_MARKET = "config_db_ib_market";
	public static final String CONFIG_DB_IB_MARKET_SOURCE = "config_db_ib_market_source";
	public static final String CONFIG_DB_IB_MARKET_FIELD = "config_db_ib_market_field";
	public static final String CONFIG_DB_IB_MARKET_FIELD_SIZE = "config_db_ib_market_field_size";
	public static final String CONFIG_DB_IB_MARKET_FIELD_TIME = "config_db_ib_market_field_time";
	public static final String CONFIG_DB_IB_MARKET_FIELD_SYMBOL = "config_db_ib_market_field_symbol";
	public static final String CONFIG_DB_IB_MARKET_FIELD_PRICE = "config_db_ib_market_field_price";
	public static final String CONFIG_DB_IB_MARKET_FIELD_OPEN = "config_db_ib_market_field_open";
	public static final String CONFIG_DB_IB_MARKET_FIELD_CLOSE = "config_db_ib_market_field_close";
	public static final String CONFIG_DB_IB_MARKET_FIELD_LOW = "config_db_ib_market_field_low";
	public static final String CONFIG_DB_IB_MARKET_FIELD_HIGH = "config_db_ib_market_field_high";
	public static final String CONFIG_DB_IB_MARKET_FIELD_VOLUME = "config_db_ib_market_field_volume";
	public static final String CONFIG_DB_IB_MARKET_FIELD_ASK = "config_db_ib_market_field_ask";
	public static final String CONFIG_DB_IB_MARKET_FIELD_ASK_SIZE = "config_db_ib_market_field_ask_size";
	public static final String CONFIG_DB_IB_MARKET_FIELD_BID = "config_db_ib_market_field_bid";
	public static final String CONFIG_DB_IB_MARKET_FIELD_BID_SIZE = "config_db_ib_market_field_bid_size";
	public static final String CONFIG_DB_IB_MARKET_FIELD_HALTED = "config_db_ib_market_field_halted";
	public static final String CONFIG_DB_IB_MARKET_FIELD_HALTED_TOT = "config_db_ib_market_field_halted_tot";
	
	public static final String CONN = "conn";
	public static final String CONN_PAPER = "conn_paper";
	public static final String CONN_REAL = "conn_real";
	public static final String CONN_GATEWAY = "conn_gateway";
	public static final String CONN_GATEWAY_PAPER = "conn_gateway_paper";	

	public static final String SYNC = "sync";
	public static final String SYNC_GET = "sync_get";
	public static final String SYNC_GET_FUNDS = "sync_get_funds";
	public static final String SYNC_GET_ID = "sync_get_id";
	public static final String SYNC_GET_IDS = "sync_get_ids";
	public static final String SYNC_DATA = "sync_data";
	public static final String SYNC_DATA_INT = "sync_data_int";
	public static final String SYNC_DATA_DECIMAL = "sync_data_decimal";
	public static final String SYNC_DATA_INTS = "sync_data_ints";
	public static final String SYNC_DATA_MISC = "sync_data_misc";

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
	public static final String ERROR_IB_INI = "error_ib_ini";
	public static final String ERROR_IB_INI_DB = "error_ib_ini_db";
	public static final String ERROR_IB_INI_DB_DBS = "error_ib_ini_db_dbs";
	public static final String ERROR_IB_CONN = "error_ib_conn";
	public static final String ERROR_IB_CONN_NONE = "error_ib_conn_none";
	public static final String ERROR_IB_CONN_ID = "error_ib_conn_id";
	public static final String ERROR_IB_CONN_TYPE = "error_ib_conn_type";
	public static final String ERROR_IB_SYNC = "error_ib_sync";
	public static final String ERROR_IB_SYNC_ID = "error_ib_sync_id";
	public static final String ERROR_IB_SYNC_ID2 = "error_ib_sync_id2";
	public static final String ERROR_IB_SYNC_TIME = "error_ib_sync_time";
	public static final String ERROR_IB_ASYNC = "error_ib_async";
	public static final String ERROR_IB_ASYNC_TIME = "error_ib_async_time";

	static { _ini.start(); }

	public static String check_conn(String subtype_, String add_remove_) { return accessory.types.check_type(subtype_, get_subtypes(CONN)); }

	public static String check_async(String subtype_) { return accessory.types.check_type(subtype_, get_subtypes(ASYNC)); }

	public static String check_sync(String subtype_, boolean is_data_) { return accessory.types.check_type(subtype_, get_subtypes_sync(is_data_)); }

	public static String[] get_subtypes_sync(boolean is_data_) { return get_subtypes((is_data_ ? SYNC_DATA : SYNC)); }

	public static boolean is_order(String subtype_) { return strings.is_ok(check_order(subtype_)); }

	public static String check_order(String subtype_) { return accessory.types.check_type(subtype_, get_subtypes(types.ORDER)); }

	public static String check_order_place(String subtype_) { return accessory.types.check_type(subtype_, get_subtypes(types.ORDER_PLACE)); }

	public static String check_order_update(String subtype_) { return accessory.types.check_type(subtype_, get_subtypes(types.ORDER_UPDATE)); }

	public static String check_error(String subtype_, String[] types_) { return accessory.types.check_type(subtype_, get_subtypes_errors(types_)); }

	public static String[] get_subtypes_errors(String[] types_)
	{
		return accessory.types.get_subtypes
		(
			(arrays.is_ok(types_) ? types_ : get_all_types_error()), get_all_subtypes()
		);
	}

	private static String[] get_all_types_error() { return new String[] { ERROR_IB_CONN, ERROR_IB_SYNC, ERROR_IB_ASYNC }; }

	private static String[] get_subtypes(String type_) { return accessory.types.get_subtypes(type_, get_all_subtypes()); }

	private static String[] get_all_subtypes()
	{
		return new String[]
		{
			CONFIG_BASIC_CURRENCY,
			CONFIG_CONN,
			CONFIG_CONN_TYPE,
			CONFIG_ASYNC,
			CONFIG_ASYNC_SNAPSHOT_QUICK, CONFIG_ASYNC_SNAPSHOT_CONSTANT, 
			CONFIG_ASYNC_STORAGE,
			CONFIG_ASYNC_STORAGE_MEMORY, CONFIG_ASYNC_STORAGE_DB,
			CONFIG_ORDER,
			CONFIG_ORDER_TIF, CONFIG_ORDER_QUANTITY_INT,
			
			CONFIG_DB_IB,
			CONFIG_DB_IB_MARKET,
			CONFIG_DB_IB_MARKET_SOURCE,
			CONFIG_DB_IB_MARKET_FIELD, CONFIG_DB_IB_MARKET_FIELD_SIZE, CONFIG_DB_IB_MARKET_FIELD_TIME, 
			CONFIG_DB_IB_MARKET_FIELD_SYMBOL, CONFIG_DB_IB_MARKET_FIELD_PRICE, CONFIG_DB_IB_MARKET_FIELD_OPEN, 
			CONFIG_DB_IB_MARKET_FIELD_CLOSE, CONFIG_DB_IB_MARKET_FIELD_LOW, CONFIG_DB_IB_MARKET_FIELD_HIGH, 
			CONFIG_DB_IB_MARKET_FIELD_VOLUME, CONFIG_DB_IB_MARKET_FIELD_ASK, CONFIG_DB_IB_MARKET_FIELD_ASK_SIZE,
			CONFIG_DB_IB_MARKET_FIELD_BID, CONFIG_DB_IB_MARKET_FIELD_BID_SIZE, CONFIG_DB_IB_MARKET_FIELD_HALTED, 
			CONFIG_DB_IB_MARKET_FIELD_HALTED_TOT,
			
			CONN,
			CONN_PAPER, CONN_REAL, CONN_GATEWAY, CONN_GATEWAY_PAPER,

			SYNC,
			SYNC_GET,
			SYNC_GET_FUNDS, SYNC_GET_ID, SYNC_GET_IDS,
			SYNC_DATA,
			SYNC_DATA_INT, SYNC_DATA_DECIMAL, SYNC_DATA_INTS, SYNC_DATA_MISC,

			ASYNC,
			ASYNC_MARKET,
			ASYNC_MARKET_SNAPSHOT, ASYNC_MARKET_STREAM,
			
			ORDER,
			ORDER_PLACE,
			ORDER_PLACE_MARKET, ORDER_PLACE_STOP, ORDER_PLACE_LIMIT,
			ORDER_UPDATE,
			ORDER_UPDATE_START_VALUE, ORDER_UPDATE_START_MARKET, ORDER_UPDATE_STOP_VALUE, ORDER_UPDATE_STOP_MARKET,
			
			ERROR_IB,
			ERROR_IB_INI,
			ERROR_IB_INI_DB, 
			ERROR_IB_INI_DB_DBS,
			ERROR_IB_CONN,
			ERROR_IB_CONN_NONE, ERROR_IB_CONN_ID, ERROR_IB_CONN_TYPE,
			ERROR_IB_SYNC,
			ERROR_IB_SYNC_ID, ERROR_IB_SYNC_ID2, ERROR_IB_SYNC_TIME,
			ERROR_IB_ASYNC,
			ERROR_IB_ASYNC_TIME
		};		
	}
}