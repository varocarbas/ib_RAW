package accessory_ib;

public class config 
{	
	static { ini.load(); }

	public static String get_root(String key_)
	{
		return accessory.config.get(_types.CONFIG_IB, key_);		
	}

	public static boolean update_root(String key_, String val_)
	{
		return accessory.config.update(_types.CONFIG_IB, key_, val_);
	}

	public static String get_conn(String key_)
	{
		return accessory.config.get(_types.CONFIG_IB_CONN, key_);		
	}

	public static boolean update_conn(String key_, String val_)
	{
		return accessory.config.update(_types.CONFIG_IB_CONN, key_, val_);
	}

	public static String get_async(String key_)
	{
		return accessory.config.get(_types.CONFIG_IB_ASYNC, key_);
	}

	public static boolean update_async(String key_, String val_)
	{
		return accessory.config.update(_types.CONFIG_IB_ASYNC, key_, val_);
	}

	public static String get_order(String key_)
	{
		return accessory.config.get(_types.CONFIG_IB_ORDER, key_);
	}

	public static boolean update_order(String key_, String val_)
	{
		return accessory.config.update(_types.CONFIG_IB_ORDER, key_, val_);
	}

	public static boolean matches_async(String key_, String val_)
	{
		return accessory.config.matches(_types.CONFIG_IB_ASYNC, key_, val_);
	}

	public static String get_db(String key_)
	{
		return accessory.config.get(_types.CONFIG_IB_DB, key_);
	}

	public static boolean update_db(String key_, String val_)
	{
		return accessory.config.update(_types.CONFIG_IB_DB, key_, val_);
	}

	public static boolean matches_db(String key_, String val_)
	{
		return accessory.config.matches(_types.CONFIG_IB_DB, key_, val_);
	}
}