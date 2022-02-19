package accessory_ib;

import accessory.arrays;
import accessory.strings;

public class types
{
	//------ To be synced with get_all_subtypes().

	//--- To be synced with the corresponding _ini and _config methods/variables.
	
	//Note for DB types: the sources/fields are the types/ids, constant, used in most of the code. 
	//The tables/cols are the values, variable, only used when performing the corresponding query.

	public static final String _CONFIG_IB = "_config_ib";
	public static final String _CONFIG_IB_CURRENCY = "_config_ib_currency";

	public static final String _CONFIG_IB_CONN = "_config_ib_conn";
	public static final String _CONFIG_IB_CONN_TYPE = "_config_ib_conn_type";

	public static final String _CONFIG_IB_ASYNC = "_config_ib_async";
	public static final String _CONFIG_IB_ASYNC_SNAPSHOT_QUICK = "_config_ib_async_snapshot_quick";
	public static final String _CONFIG_IB_ASYNC_SNAPSHOT_CONSTANT = "_config_ib_async_snapshot_constant";
	public static final String _CONFIG_IB_ASYNC_STORAGE = "_config_ib_async_storage";
	public static final String _CONFIG_IB_ASYNC_STORAGE_MEMORY = "_config_ib_async_storage_memory";
	public static final String _CONFIG_IB_ASYNC_STORAGE_DB = "_config_ib_async_storage_db";

	public static final String _CONFIG_IB_ORDER = "_config_ib_order";
	public static final String _CONFIG_IB_ORDER_TIF = "_config_ib_order_tif";
	public static final String _CONFIG_IB_ORDER_QUANTITY_INT = "_config_ib_order_quantity_int";

	public static final String _CONFIG_IB_DB = "_config_ib_db";
	public static final String _CONFIG_IB_DB_COMMON = "_config_ib_db_common";
	public static final String _CONFIG_IB_DB_COMMON_FIELD_SYMBOL = "_config_ib_db_common_field_symbol";
	public static final String _CONFIG_IB_DB_COMMON_FIELD_PRICE = "_config_ib_db_common_field_price";
	public static final String _CONFIG_IB_DB_MARKET = "_config_ib_db_market";
	public static final String _CONFIG_IB_DB_MARKET_SOURCE = "_config_ib_db_market_source";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_TIME = "_config_ib_db_market_field_time";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_SYMBOL = _CONFIG_IB_DB_COMMON_FIELD_SYMBOL;
	public static final String _CONFIG_IB_DB_MARKET_FIELD_PRICE = _CONFIG_IB_DB_COMMON_FIELD_PRICE;
	public static final String _CONFIG_IB_DB_MARKET_FIELD_OPEN = "_config_ib_db_market_field_open";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_CLOSE = "_config_ib_db_market_field_close";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_LOW = "_config_ib_db_market_field_low";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_HIGH = "_config_ib_db_market_field_high";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_VOLUME = "_config_ib_db_market_field_volume";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_ASK = "_config_ib_db_market_field_ask";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE = "_config_ib_db_market_field_ask_size";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_BID = "_config_ib_db_market_field_bid";
	public static final String _CONFIG_IB_DB_MARKET_FIELD_BID_SIZE = "_config_ib_db_market_field_bid_size";
	//------

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

	//--- Types to be synced with get_all_types_error().
	public static final String ERROR_CONN = "error_conn";
	public static final String ERROR_CONN_NONE = "error_conn_none";
	public static final String ERROR_CONN_ID = "error_conn_id";
	public static final String ERROR_CONN_TYPE = "error_conn_type";
	public static final String ERROR_SYNC = "error_sync";
	public static final String ERROR_SYNC_ID = "error_sync_id";
	public static final String ERROR_SYNC_ID2 = "error_sync_id2";
	public static final String ERROR_SYNC_TIME = "error_sync_time";
	public static final String ERROR_ASYNC = "error_async";
	public static final String ERROR_ASYNC_TIME = "error_async_time";
	//------
	//---------------------------

	static { _ini.load(); }

	public static String check_conn(String subtype_, String add_remove_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(CONN), null, null);
	}

	public static String check_async(String subtype_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(ASYNC), null, null);
	}

	public static String check_sync(String subtype_, boolean is_data_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes_sync(is_data_), null, null);
	}

	public static String[] get_subtypes_sync(boolean is_data_)
	{		
		return get_subtypes((is_data_ ? SYNC_DATA : SYNC));
	}

	public static boolean is_order(String subtype_)
	{
		return strings.is_ok(check_order(subtype_));
	}

	public static String check_order(String subtype_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(types.ORDER), null, null);
	}

	public static String check_order_place(String subtype_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(types.ORDER_PLACE), null, null);
	}

	public static String check_order_update(String subtype_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(types.ORDER_UPDATE), null, null);
	}

	public static String check_error(String subtype_, String[] types_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes_errors(types_), null, null);
	}

	public static String[] get_subtypes_errors(String[] types_)
	{
		return accessory.types.get_subtypes
		(
			(arrays.is_ok(types_) ? types_ : get_all_types_error()), get_all_subtypes()
		);
	}

	private static String[] get_all_types_error()
	{
		return new String[]
		{
			ERROR_CONN, ERROR_SYNC, ERROR_ASYNC
		};
	}

	private static String[] get_subtypes(String type_)
	{
		return accessory.types.get_subtypes(type_, get_all_subtypes());
	}

	private static String[] get_all_subtypes()
	{
		return new String[]
		{
			//_CONFIG_IB
			_CONFIG_IB_CURRENCY,
			//_CONFIG_IB_CONN
			_CONFIG_IB_CONN_TYPE,
			//_CONFIG_IB_ASYNC
			_CONFIG_IB_ASYNC_SNAPSHOT_QUICK, 
			//_CONFIG_IB_ASYNC_STORAGE
			_CONFIG_IB_ASYNC_STORAGE_MEMORY, _CONFIG_IB_ASYNC_STORAGE_DB,
			//_CONFIG_IB_ORDER
			_CONFIG_IB_ORDER_TIF, _CONFIG_IB_ORDER_QUANTITY_INT,
			//_CONFIG_IB_DB
			//_CONFIG_IB_DB_COMMON
			_CONFIG_IB_DB_COMMON_FIELD_SYMBOL,  _CONFIG_IB_DB_COMMON_FIELD_PRICE,
			//_CONFIG_IB_DB_MARKET
			_CONFIG_IB_DB_MARKET_SOURCE,
			_CONFIG_IB_DB_MARKET_FIELD_TIME, _CONFIG_IB_DB_MARKET_FIELD_SYMBOL, _CONFIG_IB_DB_MARKET_FIELD_PRICE,
			_CONFIG_IB_DB_MARKET_FIELD_OPEN, _CONFIG_IB_DB_MARKET_FIELD_CLOSE, _CONFIG_IB_DB_MARKET_FIELD_LOW,
			_CONFIG_IB_DB_MARKET_FIELD_HIGH, _CONFIG_IB_DB_MARKET_FIELD_VOLUME, _CONFIG_IB_DB_MARKET_FIELD_ASK,
			_CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, _CONFIG_IB_DB_MARKET_FIELD_BID, _CONFIG_IB_DB_MARKET_FIELD_BID_SIZE, 

			//CONN
			CONN_PAPER, CONN_REAL, CONN_GATEWAY, CONN_GATEWAY_PAPER,

			//SYNC
			//SYNC_GET
			SYNC_GET_ID, SYNC_GET_FUNDS, SYNC_GET_IDS,
			//SYNC_DATA
			SYNC_DATA_INT, SYNC_DATA_DECIMAL, SYNC_DATA_INTS, SYNC_DATA_MISC,

			//ASYNC
			//ASYNC_MARKET
			ASYNC_MARKET_SNAPSHOT, ASYNC_MARKET_STREAM,

			//ORDER
			//ORDER_PLACE
			ORDER_PLACE_MARKET, ORDER_PLACE_STOP, ORDER_PLACE_LIMIT,
			//ORDER_UPDATE
			ORDER_UPDATE_START_VALUE, ORDER_UPDATE_START_MARKET,
			ORDER_UPDATE_STOP_VALUE, ORDER_UPDATE_STOP_MARKET,

			//ERROR_CONN
			ERROR_CONN_NONE, ERROR_CONN_ID, ERROR_CONN_TYPE,
			//ERROR_SYNC
			ERROR_SYNC_ID, ERROR_SYNC_ID2, ERROR_SYNC_TIME
			//ERROR_ASYNC
		};		
	}
}