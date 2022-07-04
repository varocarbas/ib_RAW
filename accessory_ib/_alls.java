package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_first;
import ib.async_data_market;
import ib.async_data_trades;
import ib.async_data_watchlist;
import ib.sync;

public class _alls extends parent_ini_first
{
	private static _alls _instance = new _alls(); 
	
	public _alls() { }
	public static void populate() { _instance.populate_internal_common(); }
	
	public static HashMap<String, String> SYNC_GET_OUTS = null;
	
	public static HashMap<Integer, String> ASYNC_MARKET_PRICES = null;
	public static HashMap<Integer, String> ASYNC_MARKET_SIZES = null;
	public static HashMap<Integer, String> ASYNC_MARKET_GENERICS = null;
	
	public static HashMap<Integer, String> ASYNC_TRADES_PRICES = null;
	public static HashMap<Integer, String> ASYNC_TRADES_GENERICS = null;
	
	public static HashMap<Integer, String> ASYNC_WATCHLIST_PRICES = null;
	public static HashMap<Integer, String> ASYNC_WATCHLIST_SIZES = null;
	public static HashMap<Integer, String> ASYNC_WATCHLIST_GENERICS = null;
	
	public static String[] EXTERNAL_CONTRACTS_SECURITIES = null;
	
	public static int[] EXTERNAL_MARKET_TICKS = null;
	public static int[] EXTERNAL_MARKET_HALTED = null;
	public static int[] EXTERNAL_MARKET_DATA = null;
	
	public static String[] EXTERNAL_ORDERS_EXEC_SIDES = null;
	public static String[] EXTERNAL_ORDERS_ACTIONS = null;
	public static String[] EXTERNAL_ORDERS_TYPES = null;
	public static String[] EXTERNAL_ORDERS_TIFS = null;
	public static String[] EXTERNAL_ORDERS_STATUSES = null;

	public static String[] DB_SOURCES_USER = null;
	public static String[] DB_SOURCES_ENABLED = null;
	
	public void populate_internal() 
	{ 
		if (_populated) return;
		
		accessory._alls.populate_types(types.populate_all_types());
		
		SYNC_GET_OUTS = sync.populate_all_get_outs();
		
		ASYNC_MARKET_PRICES = async_data_market.populate_all_prices();
		ASYNC_MARKET_SIZES = async_data_market.populate_all_sizes();
		ASYNC_MARKET_GENERICS = async_data_market.populate_all_generics();
		
		ASYNC_TRADES_PRICES = async_data_trades.populate_all_prices();
		ASYNC_TRADES_GENERICS = async_data_trades.populate_all_generics();
		
		ASYNC_WATCHLIST_PRICES = async_data_watchlist.populate_all_prices();
		ASYNC_WATCHLIST_SIZES = async_data_watchlist.populate_all_sizes();
		ASYNC_WATCHLIST_GENERICS = async_data_watchlist.populate_all_generics();
		
		EXTERNAL_CONTRACTS_SECURITIES = external_ib.contracts.populate_all_securities();
		
		EXTERNAL_MARKET_TICKS = external_ib.data.populate_all_ticks();
		EXTERNAL_MARKET_HALTED = external_ib.data.populate_all_halted();
		EXTERNAL_MARKET_DATA = external_ib.data.populate_all_data();		
		
		EXTERNAL_ORDERS_EXEC_SIDES = external_ib.orders.populate_all_exec_sides();
		EXTERNAL_ORDERS_ACTIONS = external_ib.orders.populate_all_actions();
		EXTERNAL_ORDERS_TYPES = external_ib.orders.populate_all_types();
		EXTERNAL_ORDERS_TIFS = external_ib.orders.populate_all_tifs();
		EXTERNAL_ORDERS_STATUSES = external_ib.orders.populate_all_statuses();
		
		DB_SOURCES_USER = db_ib.common.populate_all_sources_user();
		DB_SOURCES_ENABLED = db_ib.common.populate_all_sources_enabled();
		
		_populated = true;
	}
}