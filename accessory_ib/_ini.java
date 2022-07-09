package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini;
import accessory.strings;
import ib.basic;

public class _ini extends parent_ini
{	
	private static _ini _instance = new _ini();
	
	private String _account_ib = strings.DEFAULT;
	
	public static String get_user() { return _instance._user; }
	
	public _ini() { }
	
	//The start() methods below these lines emulate the structure of the accessory._ini ones.
	
	public static String get_account_ib() { return _instance._account_ib; }
	
	public static void delete_account_ib() { _instance._account_ib = strings.DEFAULT; }
	
	public static void start() { if (!_instance._populated) start(null, false, null); }
	
	public static void start(String name_, boolean includes_legacy_, String account_ib_id_) 
	{
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_);
		
		_instance.populate_account_ib(account_ib_id_);
			
		_instance.populate_all(name_);
	}

	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_, String account_ib_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_, dbs_setup_);
		
		_instance.populate_account_ib(account_ib_id_);
			
		_instance.populate_all(name_, dbs_setup_); 
	}
	
	public static void start(String name_, String user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_, String account_ib_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, user_, dbs_host_, dbs_encrypted_, includes_legacy_);
		
		_instance.populate_account_ib(account_ib_id_);
				
		_instance.populate_all(name_, user_, null, null, dbs_host_, dbs_encrypted_); 
	}

	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_, String account_ib_id_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, dbs_username_, dbs_password_, dbs_host_, includes_legacy_);

		_instance.populate_account_ib(account_ib_id_);
		
		_instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false);
	}
		
	private void populate_account_ib(String account_ib_) { _account_ib = (strings.is_ok(account_ib_) ? account_ib_ : basic.get_from_file(basic.get_account_ib_id())); }
}