package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_config;

class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { populate_internal(_instance); }

	protected void populate_all()
	{
		load_config_root();
		load_config_conn();
		load_config_async();
		load_config_order();
	}
	
	private boolean load_config_root()
	{
		String type = types.CONFIG_IB;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_IB_CURRENCY, _defaults.CURRENCY);

		return populate(type, null, vals);
	}

	private boolean load_config_conn()
	{
		String type = types.CONFIG_IB_CONN;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_IB_CONN, _defaults.CONN);

		return populate(type, null, vals);
	}

	private boolean load_config_async()
	{
		String type = types.CONFIG_IB_ASYNC;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_IB_ASYNC_SNAPSHOT_QUICK, _defaults.ASYNC_SNAPSHOT_QUICK);
		vals.put(types.CONFIG_IB_ASYNC_SNAPSHOT_CONSTANT, _defaults.ASYNC_SNAPSHOT_CONSTANT);
		vals.put(types.CONFIG_IB_ASYNC_STORAGE, _defaults.ASYNC_STORAGE);
		
		return populate(type, null, vals);
	}

	private boolean load_config_order()
	{
		String type = types.CONFIG_IB_ORDER;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(types.CONFIG_IB_ORDER_TIF, _defaults.ORDER_TIF);
		vals.put(types.CONFIG_IB_ORDER_QUANTITY_INT, _defaults.ORDER_QUANTITY_INT);
		
		return populate(type, null, vals);
	}
}