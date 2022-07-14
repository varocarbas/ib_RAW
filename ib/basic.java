package ib;

import accessory._defaults;
import accessory.config;
import accessory.credentials;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._ini;
import accessory_ib.types;

public abstract class basic extends parent_static 
{
	public static final String CONFIG_MAIN_ID = types.CONFIG_BASIC_MAIN_ID;

	public static final String SEPARATOR = misc.SEPARATOR_NAME;
	
	public static final String DEFAULT_USER = _defaults.USER;
	public static final String DEFAULT_MAIN_ID = "ib";
	
	private static final String USER = get_user_internal();
	private static final String ID_ACCOUNT_IB = "account_ib";

	public static void start() { sync_basic.start(); }

	public static String get_main_id() { return (String)config.get_basic(CONFIG_MAIN_ID); }

	public static boolean update_main_id(String id_) { return config.update_basic(CONFIG_MAIN_ID, id_); }
	
	public static String get_user() { return USER; }

	public static String get_account_ib() { return sync_basic.get_account_ib(); }

	public static int get_conn_id() { return sync_basic.get_conn_id(); }

	public static String get_conn_type() { return sync_basic.get_conn_type(); }

	public static String update_conn_type(String conn_type_) { return sync_basic.update_conn_type(conn_type_); }
	
	public static String get_account_ib_id() { return ID_ACCOUNT_IB; }
	
	public static boolean account_ib_is_ok(String account_ib_) 
	{ 
		if (!conn.type_is_real()) return true;
		
		String account_ib = get_account_ib();
		
		return (!strings.is_ok(account_ib) || strings.are_equal(account_ib_, account_ib));
	}

	public static String get_encryption_id(String id_) 
	{
		String output = strings.DEFAULT;
		
		String temp = get_main_id();
		if (strings.is_ok(temp)) output = temp;
		
		if (strings.is_ok(id_)) 
		{
			if (!output.equals(strings.DEFAULT)) output += SEPARATOR;
			
			output += id_;
		}
		
		return output;
	}
	
	public static String encrypt(String id_, String plain_) { return encrypt_internal(get_encryption_id(id_), get_user(), plain_); }

	public static String decrypt_account_ib(String encrypted_) { return decrypt(get_account_ib_id(), encrypted_); }

	public static String decrypt(String id_, String encrypted_) { return credentials.decrypt_string(get_encryption_id(id_), get_user(), encrypted_); }

	public static boolean encrypt_account_ib_to_file(String plain_) { return encrypt_to_file(get_account_ib_id(), plain_); }

	public static boolean encrypt_to_file(String id_, String plain_) { return credentials.encrypt_string_to_file(get_encryption_id(id_), basic.get_user(), plain_); }

	public static String get_from_file(String id_) { return credentials.get_string_from_file(get_encryption_id(id_), basic.get_user(), true); }

	public static String get_encrypted_file_path(String id_) { return credentials.get_path(get_encryption_id(id_), basic.get_user(), true); }
	
	private static String encrypt_internal(String id_, String user_, String plain_) { return credentials.encrypt_string(id_, user_, plain_); }

	private static String get_user_internal()
	{
		String user = _ini.get_user();
		if (!strings.is_ok(user)) user = DEFAULT_USER;
		
		return strings.truncate(user, db_ib.common.MAX_SIZE_USER);
	}
}