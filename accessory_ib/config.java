package accessory_ib;

public abstract class config 
{	
	public static Object get_conn(String key_) { return accessory.config.get(types.CONFIG_CONN, key_); }

	public static boolean update_conn(String key_, String val_) { return accessory.config.update(types.CONFIG_CONN, key_, val_); }

	public static boolean update_conn(String key_, int val_) { return accessory.config.update(types.CONFIG_CONN, key_, val_); }

	public static Object get_order(String key_) { return accessory.config.get(types.CONFIG_ORDERS, key_); }

	public static boolean get_order_boolean(String key_) { return accessory.config.get_boolean(types.CONFIG_ORDERS, key_); }

	public static boolean update_order(String key_, String val_) { return accessory.config.update(types.CONFIG_ORDERS, key_, val_); }

	public static Object get_async(String key_) { return accessory.config.get(types.CONFIG_ASYNC, key_); }

	public static boolean get_async_boolean(String key_) { return accessory.config.get_boolean(types.CONFIG_ASYNC, key_); }

	public static boolean update_async(String key_, String val_) { return accessory.config.update(types.CONFIG_ASYNC, key_, val_); }

	public static boolean update_async(String key_, boolean val_) { return accessory.config.update(types.CONFIG_ASYNC, key_, val_); }

	public static Object get_contract(String key_) { return accessory.config.get(types.CONFIG_CONTRACT, key_); }

	public static boolean update_contract(String key_, String val_) { return accessory.config.update(types.CONFIG_CONTRACT, key_, val_); }

	public static Object get_db(String key_) { return accessory.config.get(types.CONFIG_DB_IB, key_); }

	public static boolean update_db(String key_, String val_) { return accessory.config.update(types.CONFIG_DB_IB, key_, val_); }
}