package accessory_ib;

public class _ini 
{
	//This method is expected to be called every time a class is loaded.
	public static void load() 
	{
		load_config();
	}
	
	private static void load_config()
	{
		load_config_async();
		load_config_db();		
	}
	
	private static void load_config_async()
	{
		String type = types._CONFIG_ASYNC;
		
		accessory._config.update_ini(type, types._CONFIG_ASYNC_STORAGE, defaults.ASYNC_STORAGE);
	}
	
	private static void load_config_db()
	{
		String type = types._CONFIG_DB;
		
		accessory._config.update_ini(type, types._CONFIG_DB_TABLE_CONN, defaults.DB_TABLE_CONN);
		accessory._config.update_ini(type, types._CONFIG_DB_TABLE_EXECS, defaults.DB_TABLE_EXECS);	
	}
}