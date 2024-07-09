package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_config;
import external_ib.contracts;
import ib.basic;
import ib.common;
import ib.conn;
import ib.remote;
import ib._order;
import ib.async_data_quicker;

public class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { _instance.populate_all(); }
	
	protected void populate_all_internal()
	{
		load_basic();
		
		load_common();
		
		load_order();
		
		load_contract();
		
		load_conn();
		
		load_remote();
		
		load_async_data();
	}

	private boolean load_basic()
	{
		String type = _types.CONFIG_BASIC_IB;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(basic.CONFIG_ID_MAIN, _defaults.ID_MAIN);
		vals.put(basic.CONFIG_MULTIPLE_TRADES, basic.DEFAULT_MULTIPLE_TRADES);
		vals.put(basic.CONFIG_MULTIPLE_TRADES_SYMBOL, basic.DEFAULT_MULTIPLE_TRADES_SYMBOL);
		
		vals.put(paths.CONFIG_DIR_TWS, paths.get_default_dir_app_ib(true));
		vals.put(paths.CONFIG_DIR_GATEWAY, paths.get_default_dir_app_ib(false));
		vals.put(paths.CONFIG_PATH_MARKET_HOLIDAYS, paths.get_default_path_market_holidays());
		vals.put(paths.CONFIG_PATH_MARKET_EARLY_CLOSES, paths.get_default_path_market_early_closes());

		return populate(type, null, vals);
	}
	
	private boolean load_common()
	{
		String type = _types.CONFIG_COMMON;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(common.CONFIG_ALWAYS_DISABLE_SYMBOL, common.DEFAULT_ALWAYS_DISABLE_SYMBOL);

		return populate(type, null, vals);
	}
	
	private boolean load_order()
	{
		String type = _types.CONFIG_ORDERS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(_order.CONFIG_TIF, _order.DEFAULT_TIF);
		vals.put(_order.CONFIG_QUANTITIES_INT, _order.DEFAULT_QUANTITIES_INT);
		vals.put(_order.CONFIG_OUTSIDE_RTH, _order.DEFAULT_OUTSIDE_RTH);

		return populate(type, null, vals);
	}	

	private boolean load_contract()
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

	private boolean load_conn()
	{
		String type = _types.CONFIG_CONN;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(conn.CONFIG_CHECK_RUNNING, conn.DEFAULT_CHECK_RUNNING);

		return populate(type, null, vals);
	}

	private boolean load_remote()
	{
		String type = _types.CONFIG_REMOTE;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(remote.CONFIG_UPDATE_WAIT_FOR_ERRORS, remote.DEFAULT_UPDATE_WAIT_FOR_ERRORS);

		return populate(type, null, vals);
	}

	private boolean load_async_data()
	{
		String type = _types.CONFIG_ASYNC_DATA;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(async_data_quicker.CONFIG_ASK_BID_AS_PRICE, async_data_quicker.DEFAULT_ASK_BID_AS_PRICE);

		return populate(type, null, vals);
	}
}