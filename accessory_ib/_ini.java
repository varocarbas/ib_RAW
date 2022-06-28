package accessory_ib;

import java.util.HashMap;

import accessory.credentials;
import accessory.parent_ini;
import accessory.strings;
import ib.basic;
import ib.conn;

public class _ini extends parent_ini
{	
	private static _ini _instance = new _ini();
	
	public static String get_user() { return _instance._user; }
	
	public _ini() { }
	
	//The start() methods below these lines emulate the structure of the accessory._ini ones.
	
	public static void start() { if (!_instance._populated) start(null, false, null); }
	
	public static void start(String name_, boolean includes_legacy_, String ib_account_id_) 
	{
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_);
	
		_instance.populate_all(name_);
		
		update_ib_account(ib_account_id_);
	}

	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_, String ib_account_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_, dbs_setup_);
		
		_instance.populate_all(name_, dbs_setup_); 
		
		update_ib_account(ib_account_id_);
	}
	
	public static void start(String name_, String user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_, String ib_account_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, user_, dbs_host_, dbs_encrypted_, includes_legacy_);
		
		_instance.populate_all(name_, user_, null, null, dbs_host_, dbs_encrypted_); 

		update_ib_account(ib_account_id_);
	}

	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_, String ib_account_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, dbs_username_, dbs_password_, dbs_host_, includes_legacy_);
		
		_instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false);
		
		update_ib_account(ib_account_id_);
	}
	
	public static String get_ib_account_path() { return get_ib_account_path(basic.get_id(), basic.get_user()); }
	
	public static String get_ib_account_path(String id_, String user_) { return credentials.get_path(id_, user_, true); }

	public static boolean encrypt_ib_account_to_file(String ib_account_) { return encrypt_ib_account_to_file(basic.get_id(), basic.get_user(), ib_account_); }
	
	public static boolean encrypt_ib_account_to_file(String id_, String user_, String ib_account_) { return credentials.encrypt_string_to_file(id_, user_, ib_account_); }

	public static String get_ib_account_from_file() { return get_ib_account_from_file(basic.get_id(), basic.get_user()); }

	public static String get_ib_account_from_file(String id_, String user_) { return credentials.get_string_from_file(id_, user_, true); }
	
	private static void update_ib_account(String ib_account_) { config.update_conn(conn.CONFIG_ACCOUNT_ID, (strings.is_ok(ib_account_) ? ib_account_ : get_ib_account_from_file())); }
}