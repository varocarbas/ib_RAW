package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_config;
import external_ib.contracts;
import ib.basic;
import ib.conn;
import ib._order;

public class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { _instance.populate_all(); }
	
	protected void populate_all_internal()
	{
		load_config_basic();
		
		load_config_order();
		
		load_config_contract();
		
		load_config_conn();
	}

	private boolean load_config_basic()
	{
		String type = _types.CONFIG_BASIC_IB;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(basic.CONFIG_ID_MAIN, _defaults.ID_MAIN);
		vals.put(paths.CONFIG_DIR_TWS, paths.get_default_dir_app_ib(true));
		vals.put(paths.CONFIG_DIR_GATEWAY, paths.get_default_dir_app_ib(false));
		vals.put(paths.CONFIG_PATH_MARKET_HOLIDAYS, paths.get_default_path_market_holidays());
		vals.put(paths.CONFIG_PATH_MARKET_EARLY_CLOSES, paths.get_default_path_market_early_closes());
		
		return populate(type, null, vals);
	}

	private boolean load_config_order()
	{
		String type = _types.CONFIG_ORDERS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(_order.CONFIG_TIF, _order.DEFAULT_TIF);
		vals.put(_order.CONFIG_QUANTITIES_INT, _order.DEFAULT_QUANTITIES_INT);

		return populate(type, null, vals);
	}	

	private boolean load_config_contract()
	{
		String type = _types.CONFIG_CONTRACT;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(contracts.CONFIG_CURRENCY, contracts.DEFAULT_CURRENCY);
		vals.put(contracts.CONFIG_SECURITY_TYPE, contracts.DEFAULT_SECURITY_TYPE);
		vals.put(contracts.CONFIG_EXCHANGE, contracts.DEFAULT_EXCHANGE);
		vals.put(contracts.CONFIG_EXCHANGE_PRIMARY, contracts.DEFAULT_EXCHANGE_PRIMARY);
		vals.put(contracts.CONFIG_EXCHANGE_COUNTRY, contracts.DEFAULT_EXCHANGE_COUNTRY);

		return populate(type, null, vals);
	}

	private boolean load_config_conn()
	{
		String type = _types.CONFIG_CONN;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(conn.CONFIG_CHECK_RUNNING, conn.DEFAULT_CHECK_RUNNING);

		return populate(type, null, vals);
	}
}