package accessory_ib;

public class config 
{	
	public static String get_basic(String key_) { return accessory.config.get(accessory.types.CONFIG_BASIC, key_); }

	public static boolean update_basic(String key_, String val_) { return accessory.config.update(accessory.types.CONFIG_BASIC, key_, val_); }

	public static String get_conn(String key_) { return accessory.config.get(types.CONFIG_CONN, key_); }

	public static boolean update_conn(String key_, String val_) { return accessory.config.update(types.CONFIG_CONN, key_, val_); }

	public static String get_async(String key_) { return accessory.config.get(types.CONFIG_ASYNC, key_); }

	public static boolean update_async(String key_, String val_) { return accessory.config.update(types.CONFIG_ASYNC, key_, val_); }

	public static String get_order(String key_) { return accessory.config.get(types.CONFIG_ORDER, key_); }

	public static boolean update_order(String key_, String val_) { return accessory.config.update(types.CONFIG_ORDER, key_, val_); }

	public static boolean matches_async(String key_, String val_) { return accessory.config.matches(types.CONFIG_ASYNC, key_, val_); }

	public static String get_db(String key_) { return accessory.config.get(types.CONFIG_DB_IB, key_); }

	public static boolean update_db(String key_, String val_) { return accessory.config.update(types.CONFIG_DB_IB, key_, val_); }

	public static boolean matches_db(String key_, String val_) { return accessory.config.matches(types.CONFIG_DB_IB, key_, val_); }
}