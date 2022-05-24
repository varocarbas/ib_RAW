package accessory_ib;

import accessory.parent_ini;

public class _ini extends parent_ini
{
	private static _ini _instance = new _ini();
	
	public _ini() { }
	
	//The start() methods below these lines emulate the structure of the accessory._ini ones.
	
	public static void start() { if (!_instance._populated) start(null, false); }
	
	public static void start(String name_, boolean includes_legacy_) 
	{
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_);
	
		_instance.populate_all(name_);
	}
	
	public static void start(String name_, String dbs_user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, dbs_user_, dbs_host_, dbs_encrypted_, includes_legacy_);
		
		_instance.populate_all(name_,dbs_user_, null, null, dbs_host_, dbs_encrypted_); 
	}
	
	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, dbs_username_, dbs_password_, dbs_host_, includes_legacy_);
		
		_instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false); 
	}
}