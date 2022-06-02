package accessory_ib;

public class config 
{	
	public static Object get_conn(String key_) { return accessory.config.get(types.CONFIG_CONN, key_); }

	public static boolean update_conn(String key_, String val_) { return accessory.config.update(types.CONFIG_CONN, key_, val_); }

	public static boolean matches_conn(String key_, String val_) { return accessory.config.matches(types.CONFIG_CONN, key_, val_); }

	public static Object get_sync(String key_) { return accessory.config.get(types.CONFIG_SYNC, key_); }

	public static boolean update_sync(String key_, String val_) { return accessory.config.update(types.CONFIG_SYNC, key_, val_); }

	public static boolean matches_sync(String key_, String val_) { return accessory.config.matches(types.CONFIG_SYNC, key_, val_); }

	public static Object get_async(String key_) { return accessory.config.get(types.CONFIG_ASYNC, key_); }

	public static boolean update_async(String key_, String val_) { return accessory.config.update(types.CONFIG_ASYNC, key_, val_); }

	public static boolean matches_async(String key_, String val_) { return accessory.config.matches(types.CONFIG_ASYNC, key_, val_); }

	public static Object get_contract(String key_) { return accessory.config.get(types.CONFIG_CONTRACT, key_); }

	public static boolean update_contract(String key_, String val_) { return accessory.config.update(types.CONFIG_CONTRACT, key_, val_); }

	public static boolean matches_contract(String key_, String val_) { return accessory.config.matches(types.CONFIG_CONTRACT, key_, val_); }

	public static Object get_db(String key_) { return accessory.config.get(types.CONFIG_DB_IB, key_); }

	public static boolean update_db(String key_, String val_) { return accessory.config.update(types.CONFIG_DB_IB, key_, val_); }

	public static boolean matches_db(String key_, String val_) { return accessory.config.matches(types.CONFIG_DB_IB, key_, val_); }
}