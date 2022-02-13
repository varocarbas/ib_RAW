package accessory_ib;

import accessory.arrays;
import accessory.strings;

public class types
{
	//--- To be synced with get_all_subtypes().
	
	//--- To be synced with the corresponding _ini and _config methods/variables.
	public static final String _CONFIG_ASYNC = "_config_async";
	public static final String _CONFIG_ASYNC_STORAGE = "_config_async_storage";
	
	public static final String _CONFIG_DB = "_config_db";
	public static final String _CONFIG_DB_TABLE_CONN = "_config_db_table_conn";
	public static final String _CONFIG_DB_TABLE_EXECS = "_config_db_table_execs";
	//------
	
	public static final String CONN = "conn";
	public static final String CONN_PAPER = "conn_paper";
	public static final String CONN_REAL = "conn_real";
	public static final String CONN_GATEWAY = "conn_gateway";
	public static final String CONN_GATEWAY_PAPER = "conn_gateway_paper";	
	
	public static final String SYNC = "sync";
	public static final String SYNC_FUNDS = "sync_funds";
	public static final String SYNC_ID = "sync_id";
	public static final String SYNC_IDS = "sync_ids";
	public static final String SYNC_DATA = "sync_data";
	public static final String SYNC_DATA_INT = "sync_data_int";
	public static final String SYNC_DATA_DECIMAL = "sync_data_decimal";
	public static final String SYNC_DATA_INTS = "sync_data_ints";
	public static final String SYNC_DATA_MISC = "sync_data_misc";
	
	public static final String ORDER = "order";
	public static final String ORDER_BUY_STOP = "order_buy_stop";
	public static final String ORDER_SELL_MARKET = "order_sell_market";
	public static final String ORDER_CANCEL = "order_cancel";
	public static final String ORDER_UPDATE_START = "order_update_start";
	public static final String ORDER_UPDATE_STOP = "order_update_stop";
	
	//--- Types to be synced with get_all_types_error().
	public static final String ERROR_CONN = "error_conn";
	public static final String ERROR_CONN_NONE = "error_conn_none";
	public static final String ERROR_CONN_ID = "error_conn_id";
	public static final String ERROR_CONN_TYPE = "error_conn_type";
	public static final String ERROR_SYNC_ID = "error_conn_id";
	public static final String ERROR_SYNC_ID2 = "error_conn_id2";
	public static final String ERROR_SYNC_TIME = "error_conn_time";
	//------
	//---------------------------
	
	static { _ini.load(); }
	
	public static String check_conn(String subtype_, String add_remove_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(CONN), null, null);
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
			ERROR_CONN
		};
	}
	
	private static String[] get_subtypes(String type_)
	{
		return accessory.types.get_subtypes(type_, get_all_subtypes());
	}

	public static String[] get_all_subtypes()
	{
		return new String[]
		{
			//_CONFIG_ASYNC
			_CONFIG_ASYNC_STORAGE,
			//_CONFIG_DB
			_CONFIG_DB_TABLE_CONN, _CONFIG_DB_TABLE_EXECS,
			
			//CONN
			CONN_PAPER, CONN_REAL, CONN_GATEWAY, CONN_GATEWAY_PAPER,
			
			//SYNC
			SYNC_ID, SYNC_FUNDS, SYNC_IDS,
			//SYNC_DATA
			SYNC_DATA_INT, SYNC_DATA_DECIMAL, SYNC_DATA_INTS, SYNC_DATA_MISC,
			
			//ORDER
			ORDER_BUY_STOP, ORDER_SELL_MARKET, ORDER_CANCEL, 
			ORDER_UPDATE_START, ORDER_UPDATE_STOP,
			
			//ERROR_CONN
			ERROR_CONN_NONE, ERROR_CONN_ID, ERROR_CONN_TYPE,
			ERROR_SYNC_ID, ERROR_SYNC_ID2, ERROR_SYNC_TIME
		};		
	}
}