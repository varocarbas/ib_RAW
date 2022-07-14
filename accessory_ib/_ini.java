package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini;
import ib.ini_basic;

public class _ini extends parent_ini
{	
	//--- Keys for the information which can be included in the info_ib_ args below.
	
	//-- The values associated with these variables can't be modified at runtime.
	public static final String ACCOUNT_IB = "account_ib";
	public static final String CONN_ID_IB = "id_ib";
	//--
	
	public static final String CONN_TYPE_IB = "type_ib";
	
	//---
	
	private static _ini _instance = new _ini();
		
	public _ini() { }
	
	public static String get_user() { return _instance._user; }

	public static HashMap<String, Object> get_info_ib(String account_, String conn_type_, int conn_id_)
	{
		HashMap<String, Object> info_ib = new HashMap<String, Object>();
		info_ib.put(ACCOUNT_IB, account_);
		info_ib.put(CONN_TYPE_IB, conn_type_);
		info_ib.put(CONN_ID_IB, conn_id_);
		
		return info_ib;
	}
	
	public static void start() { if (!_instance._populated) start(null, false, null); }
	
	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> info_ib_) 
	{
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_);
			
		_instance.populate_all(name_);

		populate_ib_info(info_ib_);
	}

	public static void start(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_, HashMap<String, Object> info_ib_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, includes_legacy_, dbs_setup_);
			
		_instance.populate_all(name_, dbs_setup_); 

		populate_ib_info(info_ib_);
	}
	
	public static void start(String name_, String user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_, HashMap<String, Object> info_ib_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, user_, dbs_host_, dbs_encrypted_, includes_legacy_);
				
		_instance.populate_all(name_, user_, null, null, dbs_host_, dbs_encrypted_); 

		populate_ib_info(info_ib_);
	}

	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_, HashMap<String, Object> info_ib_) 
	{ 
		if (_instance._populated) return;
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name_, dbs_username_, dbs_password_, dbs_host_, includes_legacy_);
		
		_instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false);

		populate_ib_info(info_ib_);
	}
	
	private static void populate_ib_info(HashMap<String, Object> info_ib_) { ini_basic.start(info_ib_); }
}