package accessory_ib;

public class _config 
{	
	static { _ini.load(); }

	public static String get_async(String key_)
	{
		return accessory._config.get(types._CONFIG_ASYNC, key_);
	}

	public static boolean update_async(String key_, String val_)
	{
		return accessory._config.update(types._CONFIG_ASYNC, key_, val_);
	}

	public static boolean matches_async(String key_, String val_)
	{
		return accessory._config.matches(types._CONFIG_ASYNC, key_, val_);
	}
	
	public static String get_db(String key_)
	{
		return accessory._config.get(types._CONFIG_DB, key_);
	}

	public static boolean update_db(String key_, String val_)
	{
		return accessory._config.update(types._CONFIG_DB, key_, val_);
	}

	public static boolean matches_db(String key_, String val_)
	{
		return accessory._config.matches(types._CONFIG_DB, key_, val_);
	}
}