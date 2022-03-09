package accessory_ib;

class _config_ini 
{
	//Method expected to be called from _ini.load().
	public static void load() 
	{
		load_aliases_types();
	}

	//Method including all the generic aliases and _config types, not easily included in any other ini class.
	private static void load_aliases_types()
	{
		load_types();
	}
	
	private static void load_types()
	{
		load_types_config();
	}

	private static void load_types_config()
	{
		load_config_root();
		load_config_conn();
		load_config_async();
		load_config_order();
	}
	
	private static void load_config_root()
	{
		String type = types._CONFIG_IB;

		accessory._config.update_ini(type, types._CONFIG_IB_CURRENCY, defaults.CURRENCY);
	}

	private static void load_config_conn()
	{
		String type = types._CONFIG_IB_CONN;

		accessory._config.update_ini(type, types._CONFIG_IB_CONN, defaults.CONN);
	}

	private static void load_config_async()
	{
		String type = types._CONFIG_IB_ASYNC;

		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_SNAPSHOT_QUICK, defaults.ASYNC_SNAPSHOT_QUICK);
		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_SNAPSHOT_CONSTANT, defaults.ASYNC_SNAPSHOT_CONSTANT);
		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_STORAGE, defaults.ASYNC_STORAGE);
	}

	private static void load_config_order()
	{
		String type = types._CONFIG_IB_ORDER;

		accessory._config.update_ini(type, types._CONFIG_IB_ORDER_TIF, defaults.ORDER_TIF);
		accessory._config.update_ini(type, types._CONFIG_IB_ORDER_QUANTITY_INT, defaults.ORDER_QUANTITY_INT);
	}
}