package accessory_ib;

class config_ini 
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
		String type = types.CONFIG_IB;

		accessory.config.update_ini(type, types.CONFIG_IB_CURRENCY, _defaults.CURRENCY);
	}

	private static void load_config_conn()
	{
		String type = types.CONFIG_IB_CONN;

		accessory.config.update_ini(type, types.CONFIG_IB_CONN, _defaults.CONN);
	}

	private static void load_config_async()
	{
		String type = types.CONFIG_IB_ASYNC;

		accessory.config.update_ini(type, types.CONFIG_IB_ASYNC_SNAPSHOT_QUICK, _defaults.ASYNC_SNAPSHOT_QUICK);
		accessory.config.update_ini(type, types.CONFIG_IB_ASYNC_SNAPSHOT_CONSTANT, _defaults.ASYNC_SNAPSHOT_CONSTANT);
		accessory.config.update_ini(type, types.CONFIG_IB_ASYNC_STORAGE, _defaults.ASYNC_STORAGE);
	}

	private static void load_config_order()
	{
		String type = types.CONFIG_IB_ORDER;

		accessory.config.update_ini(type, types.CONFIG_IB_ORDER_TIF, _defaults.ORDER_TIF);
		accessory.config.update_ini(type, types.CONFIG_IB_ORDER_QUANTITY_INT, _defaults.ORDER_QUANTITY_INT);
	}
}