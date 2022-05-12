package accessory_ib;

import accessory.parent_ini;

public class _ini extends parent_ini
{
	private static _ini _instance = new _ini();
	
	public _ini() { }
	
	//This or any other start() overload have to be called before using any of the resources of this library.
	public static void start() { start(false); }
	
	public static void start(boolean includes_legacy_) 
	{
		if (_instance._populated) return;
		
		accessory._ini.start(includes_legacy_);
	
		_instance.populate_all(includes_legacy_);
	}
	
	public static void start(String dbs_user_, String dbs_host_, boolean dbs_encrypted_) { start(dbs_user_, dbs_host_, dbs_encrypted_, false); }
	
	public static void start(String dbs_user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) 
	{ 
		if (_instance._populated) return;
		
		accessory._ini.start(dbs_user_, dbs_host_, dbs_encrypted_, includes_legacy_);
		
		_instance.populate_all(dbs_user_, null, null, dbs_host_, dbs_encrypted_, includes_legacy_); 
	}
	
	public static void start(String dbs_username_, String dbs_password_, String dbs_host_) { start(dbs_username_, dbs_password_, dbs_host_, false); }
	
	public static void start(String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_) 
	{ 
		if (_instance._populated) return;
		
		accessory._ini.start(dbs_username_, dbs_password_, dbs_host_, includes_legacy_);
		
		_instance.populate_all(null, dbs_username_, dbs_password_, dbs_host_, false, includes_legacy_); 
	}
}