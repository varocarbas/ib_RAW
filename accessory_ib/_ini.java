package accessory_ib;

public class _ini 
{
	//This method is expected to be called every time a class is loaded.
	public static void load() 
	{
		load_types();
		load_config();
	}

	private static void load_types()
	{
		accessory.types.update_aliases(keys.SYMBOL, types._CONFIG_IB_DB_COMMON_COL_SYMBOL);
		accessory.types.update_aliases(keys.PRICE, types._CONFIG_IB_DB_COMMON_COL_PRICE);
		
		accessory.types.update_aliases(keys.TIME, types._CONFIG_IB_DB_MARKET_COL_TIME);
		accessory.types.update_aliases(keys.OPEN, types._CONFIG_IB_DB_MARKET_COL_OPEN);
		accessory.types.update_aliases(keys.CLOSE, types._CONFIG_IB_DB_MARKET_COL_CLOSE);
		accessory.types.update_aliases(keys.LOW, types._CONFIG_IB_DB_MARKET_COL_LOW);
		accessory.types.update_aliases(keys.HIGH, types._CONFIG_IB_DB_MARKET_COL_HIGH);
		accessory.types.update_aliases(keys.VOLUME, types._CONFIG_IB_DB_MARKET_COL_VOLUME);		
		accessory.types.update_aliases(keys.ASK, types._CONFIG_IB_DB_MARKET_COL_ASK);
		accessory.types.update_aliases(keys.ASK_SIZE, types._CONFIG_IB_DB_MARKET_COL_ASK_SIZE);
		accessory.types.update_aliases(keys.BID, types._CONFIG_IB_DB_MARKET_COL_BID);
		accessory.types.update_aliases(keys.BID_SIZE, types._CONFIG_IB_DB_MARKET_COL_BID_SIZE);
	}
	
	private static void load_config()
	{
		load_config_root();
		load_config_conn();
		load_config_async();
		load_config_order();
		load_config_db();		
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
	
	private static void load_config_db()
	{
		String type = types._CONFIG_IB_DB;

		accessory._config.update_ini(type, types._CONFIG_IB_DB_COMMON_COL_SYMBOL, defaults.DB_COMMON_COL_SYMBOL);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_COMMON_COL_PRICE, defaults.DB_COMMON_COL_PRICE);

		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_TABLE, defaults.DB_MARKET_TABLE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_TIME, defaults.DB_MARKET_COL_TIME);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_OPEN, defaults.DB_MARKET_COL_OPEN);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_CLOSE, defaults.DB_MARKET_COL_CLOSE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_LOW, defaults.DB_MARKET_COL_LOW);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_HIGH, defaults.DB_MARKET_COL_HIGH);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_VOLUME, defaults.DB_MARKET_COL_VOLUME);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_ASK, defaults.DB_MARKET_COL_ASK);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_ASK_SIZE, defaults.DB_MARKET_COL_ASK_SIZE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_BID, defaults.DB_MARKET_COL_BID);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_COL_BID_SIZE, defaults.DB_MARKET_COL_BID_SIZE);
	}
}