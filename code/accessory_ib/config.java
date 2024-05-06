package accessory_ib;

public abstract class config 
{	
	public static Object get_order(String key_) { return accessory.config.get(_types.CONFIG_ORDERS, key_); }

	public static boolean get_order_boolean(String key_) { return accessory.config.get_boolean(_types.CONFIG_ORDERS, key_); }

	public static boolean update_order(String key_, Object val_) { return accessory.config.update(_types.CONFIG_ORDERS, key_, val_); }

	public static Object get_contract(String key_) { return accessory.config.get(_types.CONFIG_CONTRACT, key_); }

	public static boolean update_contract(String key_, Object val_) { return accessory.config.update(_types.CONFIG_CONTRACT, key_, val_); }

	public static Object get_db(String key_) { return accessory.config.get(_types.CONFIG_DB_IB, key_); }

	public static boolean update_db(String key_, Object val_) { return accessory.config.update(_types.CONFIG_DB_IB, key_, val_); }
	
	public static Object get_conn(String key_) { return accessory.config.get(_types.CONFIG_CONN, key_); }

	public static boolean get_conn_boolean(String key_) { return accessory.config.get_boolean(_types.CONFIG_CONN, key_); }

	public static boolean update_conn(String key_, Object val_) { return accessory.config.update(_types.CONFIG_CONN, key_, val_); }

	public static Object get_basic(String key_) { return accessory.config.get(_types.CONFIG_BASIC_IB, key_); }

	public static boolean get_basic_boolean(String key_) { return accessory.config.get_boolean(_types.CONFIG_BASIC_IB, key_); }

	public static boolean update_basic(String key_, Object val_) { return accessory.config.update(_types.CONFIG_BASIC_IB, key_, val_); }

	public static boolean get_common_boolean(String key_) { return accessory.config.get_boolean(_types.CONFIG_COMMON, key_); }

	public static boolean update_common(String key_, Object val_) { return accessory.config.update(_types.CONFIG_COMMON, key_, val_); }

	public static boolean get_remote_boolean(String key_) { return accessory.config.get_boolean(_types.CONFIG_REMOTE, key_); }

	public static boolean update_remote(String key_, Object val_) { return accessory.config.update(_types.CONFIG_REMOTE, key_, val_); }
}