package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_first;
import external_ib.calls;
import ib.sync;

public class _alls extends parent_ini_first
{
	private static _alls _instance = new _alls(); 
	
	public _alls() { }
	public static void populate() { _instance.populate_internal_common(); }
	
	public static HashMap<String, String> SYNC_GET_OUTS = null;

	public static HashMap<String, String> CALLS_KEYS_FUNDS = null;
	
	public static String[] EXTERNAL_CONTRACTS_SECURITIES = null;
	
	public static int[] EXTERNAL_DATA_TICKS = null;
	
	public static String[] EXTERNAL_ORDERS_EXEC_SIDES = null;
	public static String[] EXTERNAL_ORDERS_ACTIONS = null;
	public static String[] EXTERNAL_ORDERS_TYPES = null;
	public static String[] EXTERNAL_ORDERS_TIFS = null;
	public static String[] EXTERNAL_ORDERS_STATUSES = null;

	public static String[] DB_SOURCES_USER = null;
	public static String[] DB_SOURCES_ENABLED = null;
	public static String[] DB_SOURCES_ELAPSED = null;
	public static HashMap<String, String> DB_SOURCES_OLD = null;
	
	public static HashMap<String, Integer> DB_MAX_SIZES_NUMBERS = null;
	public static HashMap<String, Integer> DB_MAX_SIZES_STRINGS = null;
	public static HashMap<String, Double> DB_MAX_VALS = null;
	
	public static String[] DB_NEGATIVE_NUMBERS = null;
	
	public void populate_internal() 
	{ 
		if (_populated) return;
		
		accessory._alls.populate_types(types.populate_all_types());
		
		SYNC_GET_OUTS = sync.populate_all_get_outs();
		
		CALLS_KEYS_FUNDS = calls.populate_all_keys_funds();
				
		EXTERNAL_CONTRACTS_SECURITIES = external_ib.contracts.populate_all_securities();
		
		EXTERNAL_DATA_TICKS = external_ib.data.populate_all_ticks();	
		
		EXTERNAL_ORDERS_EXEC_SIDES = external_ib.orders.populate_all_exec_sides();
		EXTERNAL_ORDERS_ACTIONS = external_ib.orders.populate_all_actions();
		EXTERNAL_ORDERS_TYPES = external_ib.orders.populate_all_types();
		EXTERNAL_ORDERS_TIFS = external_ib.orders.populate_all_tifs();
		EXTERNAL_ORDERS_STATUSES = external_ib.orders.populate_all_statuses();
		
		DB_SOURCES_USER = db_ib.common.populate_all_sources_user();
		DB_SOURCES_ENABLED = db_ib.common.populate_all_sources_enabled();
		DB_SOURCES_ELAPSED = db_ib.common.populate_all_sources_elapsed();
		DB_SOURCES_OLD = db_ib.common.populate_all_sources_old();
		
		DB_MAX_SIZES_NUMBERS = db_ib.common.populate_all_max_sizes_numbers();
		DB_MAX_SIZES_STRINGS = db_ib.common.populate_all_max_sizes_strings();
		DB_MAX_VALS = db_ib.common.populate_all_max_vals();
		
		DB_NEGATIVE_NUMBERS = db_ib.common.populate_all_negative_numbers();
		
		_populated = true;
	}
}