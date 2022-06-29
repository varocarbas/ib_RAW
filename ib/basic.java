package ib;

import accessory._defaults;
import accessory.config;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._ini;
import accessory_ib.types;

public abstract class basic extends parent_static 
{
	public static final String CONFIG_ID = types.CONFIG_BASIC_ID;
	
	public static final String DEFAULT_USER = _defaults.USER;
	public static final String DEFAULT_ID = "ib";

	private static final String USER = get_user_internal();
	
	public static String get_id() { return (String)config.get_basic(CONFIG_ID); }

	public static boolean update_id(String id_) { return config.update_basic(CONFIG_ID, id_); }
	
	public static String get_user() { return USER; }
	
	private static String get_user_internal()
	{
		String user = _ini.get_user();
		if (!strings.is_ok(user)) user = DEFAULT_USER;
		
		return strings.truncate(user, db_ib.common.MAX_SIZE_USER);
	}
}