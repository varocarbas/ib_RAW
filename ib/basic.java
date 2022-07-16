package ib;

import accessory.config;
import accessory.credentials;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._defaults;
import accessory_ib.types;

public abstract class basic extends parent_static 
{
	public static final String CONFIG_ID_MAIN = types.CONFIG_BASIC_ID_MAIN;

	public static final String SEPARATOR = misc.SEPARATOR_NAME;
	
	public static final String DEFAULT_USER = _defaults.USER;
	
	private static final String ID_ACCOUNT_IB = "account_ib";

	public static void start() { sync_basic.start(); }

	public static String get_id_main() { return (String)config.get_basic(CONFIG_ID_MAIN); }

	public static boolean update_id_main(String id_) { return config.update_basic(CONFIG_ID_MAIN, id_); }
	
	public static String get_user() { return ini_basic.get_user(); }
	
	public static String get_account_ib() { return sync_basic.get_account_ib(); }
	
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
		
		String temp = get_id_main();
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

	public static boolean encrypt_to_file(String id_, String plain_) { return credentials.encrypt_string_to_file(get_encryption_id(id_), get_user(), plain_); }

	public static String get_from_file(String id_) { return credentials.get_string_from_file(get_encryption_id(id_), get_user(), true); }

	public static String get_encrypted_file_path(String id_) { return credentials.get_path(get_encryption_id(id_), get_user(), true); }
	
	private static String encrypt_internal(String id_, String user_, String plain_) { return credentials.encrypt_string(id_, user_, plain_); }
}