package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_config;

public class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { _instance.populate_all(); }

	protected void populate_all_internal()
	{
		load_config_basic();
		load_config_conn();
		load_config_async();
		load_config_order();
	}
	
	private boolean load_config_basic()
	{
		String type = accessory.types.CONFIG_BASIC;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_BASIC_CURRENCY, _defaults.CURRENCY);

		return populate(type, null, vals);
	}

	private boolean load_config_conn()
	{
		String type = types.CONFIG_CONN;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_CONN_TYPE, _defaults.CONN_TYPE);
		vals.put(types.CONFIG_CONN_ID, _defaults.CONN_ID);

		return populate(type, null, vals);
	}

	private boolean load_config_async()
	{
		String type = types.CONFIG_ASYNC;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK, _defaults.ASYNC_SNAPSHOT_QUICK);
		vals.put(types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP, _defaults.ASYNC_SNAPSHOT_NONSTOP);
		
		return populate(type, null, vals);
	}

	private boolean load_config_order()
	{
		String type = types.CONFIG_ORDER;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_ORDER_TIF, _defaults.ORDER_TIF);
		vals.put(types.CONFIG_ORDER_QUANTITY_INT, _defaults.ORDER_QUANTITY_INT);
		
		return populate(type, null, vals);
	}
}