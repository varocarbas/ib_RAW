package accessory_ib;

import java.util.HashMap;

import accessory.db;
import accessory.generic;
import accessory.numbers;
import accessory.parent_ini;
import accessory.parent_ini_db;
import accessory.strings;
import ib.conn;
import ib.ini_apps;
import ib.ini_basic;
import ib.ini_market;

public class _ini extends parent_ini
{	
	//--- Keys for the information which can be included in the info_ args of the start() methods below. Any of these key-value pairs can be omitted.
	
	//-- The values associated with these variables can't be modified at runtime.

	public static final String INFO_INCLUDES_LEGACY = "includes_legacy";
	public static final String INFO_APP_NAME = "app_name";
	
	//INFO_USER isn't just used for overall identification, but also as the default user value (e.g., replacement for a non-existent DBS_USER which is expected because username/password weren't provided).
	public static final String INFO_USER = "user";
	
	public static final String INFO_ACCOUNT_IB = "account_ib";
	public static final String INFO_CONN_ID_IB = "conn_id_ib";
	
	//--

	public static final String INFO_CONN_TYPE_IB = "conn_type_ib";
	
	//---

	//--- Keys for the dbs_setup_ args of the start() methods below. They define the default setup for all the DBs and are used in the _ini_db class.
	
	public static final String DBS_SETUP = accessory.types.CONFIG_DB_SETUP;
	public static final String DBS_SETUP_NAME = accessory.types.CONFIG_DB_NAME;
	public static final String DBS_SETUP_MAX_POOL = accessory.types.CONFIG_DB_SETUP_MAX_POOL;
	public static final String DBS_SETUP_HOST = accessory.types.CONFIG_DB_SETUP_HOST;
	public static final String DBS_SETUP_TYPE = accessory.types.CONFIG_DB_SETUP_TYPE;
	public static final String DBS_SETUP_CREDENTIALS_USERNAME = accessory.types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME;
	public static final String DBS_SETUP_CREDENTIALS_PASSWORD = accessory.types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD;
	public static final String DBS_SETUP_CREDENTIALS_USER = accessory.types.CONFIG_DB_SETUP_CREDENTIALS_USER;
	public static final String DBS_SETUP_CREDENTIALS_ENCRYPTED = accessory.types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED;
	public static final String DBS_SETUP_CREDENTIALS_MEMORY = accessory.types.CONFIG_DB_SETUP_CREDENTIALS_MEMORY;
	
	//---
	
	public static final boolean DEFAULT_INFO_INCLUDES_LEGACY = false;
	public static final int DEFAULT_INFO_CONN_ID_IB = conn.WRONG_ID;	
	
	private static _ini _instance = new _ini();
		
	public _ini() { }
	
	public static String get_user() { return _instance._user; }

	public static HashMap<String, Object> get_info(String app_name_, String user_, String account_ib_, String conn_type_ib_, int conn_id_ib_) { return get_info(app_name_, user_, DEFAULT_INFO_INCLUDES_LEGACY, account_ib_, conn_type_ib_, conn_id_ib_); }
	
	public static HashMap<String, Object> get_info(String app_name_, String user_, boolean includes_legacy_, String account_ib_, String conn_type_ib_, int conn_id_ib_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		info.put(INFO_APP_NAME, app_name_);
		info.put(INFO_USER, user_);
		info.put(INFO_INCLUDES_LEGACY, includes_legacy_);
		info.put(INFO_ACCOUNT_IB, account_ib_);
		info.put(INFO_CONN_TYPE_IB, conn_type_ib_);
		info.put(INFO_CONN_ID_IB, conn_id_ib_);
		
		return info;
	}
	
	public static HashMap<String, Object> get_dbs_setup(String db_name_, String setup_, String user_, String host_, boolean encrypted_) { return parent_ini_db.get_setup_vals(db_name_, setup_, user_, host_, encrypted_); }
	
	public static HashMap<String, Object> get_dbs_setup(String setup_, String user_, String host_, boolean encrypted_) { return parent_ini_db.get_setup_vals(setup_, user_, host_, encrypted_); }

	public static HashMap<String, Object> get_dbs_setup(String setup_, String username_, String password_, String host_) { return parent_ini_db.get_setup_vals(setup_, username_, password_, host_); }

	//One of the start() overloads below these lines has to be called before using any of the resources of this library.
	
	public static void start() { if (!_instance._populated) start(null); }
	
	public static void start(HashMap<String, Object> info_) 
	{
		if (_instance._populated) return;

		String name = (String)_ini.get_info_val(info_, INFO_APP_NAME);
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name, (boolean)_ini.get_info_val(info_, INFO_INCLUDES_LEGACY));
			
		_instance.populate_all(name);

		populate_inis(info_);
	}

	public static void start(HashMap<String, Object> info_, HashMap<String, Object> dbs_setup_) 
	{ 
		if (_instance._populated) return;

		HashMap<String, Object> info = (info_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(info_));
		HashMap<String, Object> dbs_setup = (dbs_setup_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(dbs_setup_));

		String user = (String)_ini.get_info_val(info_, INFO_USER);
		String name = (String)_ini.get_info_val(info_, INFO_APP_NAME);
		
		boolean update_dbs_user = false;
		
		if (!dbs_setup.containsKey(DBS_SETUP_CREDENTIALS_USER) && !(dbs_setup.containsKey(DBS_SETUP_CREDENTIALS_USERNAME) && dbs_setup.containsKey(DBS_SETUP_CREDENTIALS_PASSWORD))) 
		{
			update_dbs_user = true;
			
			dbs_setup.put(DBS_SETUP_CREDENTIALS_USER, user);
		}
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name, (boolean)_ini.get_info_val(info, INFO_INCLUDES_LEGACY), dbs_setup);		
		
		_instance.populate_all(name, dbs_setup); 

		info.put(INFO_USER, user);
		
		populate_inis(info);
		
		if (update_dbs_user) accessory.config.update(db.get_current_setup(), DBS_SETUP_CREDENTIALS_USER, ini_basic.get_user());
	}
	
	private static void populate_inis(HashMap<String, Object> info_) 
	{
		ini_market.start();
		
		ini_basic.start((String)get_info_val(info_, INFO_USER), (String)get_info_val(info_, INFO_ACCOUNT_IB));

		ini_apps.start((String)get_info_val(info_, INFO_APP_NAME), (String)get_info_val(info_, INFO_CONN_TYPE_IB), (int)get_info_val(info_, INFO_CONN_ID_IB));
	}

	private static Object get_info_val(HashMap<String, Object> info_, String key_)
	{
		Object output = (info_.containsKey(key_) ? info_.get(key_) : null);
		
		if (key_.equals(INFO_APP_NAME) || key_.equals(INFO_USER) || key_.equals(INFO_ACCOUNT_IB) || key_.equals(INFO_CONN_TYPE_IB))
		{
			if (!generic.is_string(output)) output = strings.DEFAULT;
		}
		else if (key_.equals(INFO_INCLUDES_LEGACY)) 
		{
			if (!generic.is_boolean(output)) output = DEFAULT_INFO_INCLUDES_LEGACY;
		}
		else if (key_.equals(INFO_CONN_ID_IB)) output = (generic.is_number(output) ? numbers.to_int(numbers.to_number(output)) : DEFAULT_INFO_CONN_ID_IB);

		return output;
	}
}