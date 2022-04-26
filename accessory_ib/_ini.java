package accessory_ib;

import accessory.parent_ini;

public class _ini extends parent_ini
{
	private static _ini _instance = new _ini();
	
	public _ini() { }
	
	//This or any other start() overload have to be called before using any of the resources of this library.
	public static void start() 
	{
		accessory._ini.start();
		
		_instance.populate_all(); 
	}
	
	public static void start(String dbs_user_, String dbs_host_, boolean dbs_encrypted_) 
	{ 
		accessory._ini.start(dbs_user_, dbs_host_, dbs_encrypted_);
		
		_instance.populate_all(dbs_user_, null, null, dbs_host_, dbs_encrypted_); 
	}
	
	public static void start(String dbs_username_, String dbs_password_, String dbs_host_) 
	{ 
		accessory._ini.start(dbs_username_, dbs_password_, dbs_host_);
		
		_instance.populate_all(null, dbs_username_, dbs_password_, dbs_host_, false); 
	}

	protected void populate_first_basic() {  }
	
	protected void populate_first_alls() {  }

	protected void populate_first_defaults() { _defaults.populate(); }
	
	protected void populate_config() { _ini_config.populate(); }
	
	protected void populate_db() { _ini_db.populate(_dbs_user, _dbs_username, _dbs_password, _dbs_host, _dbs_encrypted); }
}