package accessory_ib;

import java.util.HashMap;

import accessory.crypto;
import accessory.db;
import accessory.generic;
import accessory.numbers;
import accessory.parent_ini;
import accessory.strings;
import ib.basic;
import ib.conn;
import ib.ini_apps;
import ib.ini_basic;
import ib.ini_common;
import ib.ini_market;

//One of the start() overloads in this class has to be called before using any of the resources of this library.
public class _ini extends parent_ini
{	
	public static final String INFO_APP_NAME = accessory._ini.INFO_NAME;
	public static final String INFO_INCLUDES_LEGACY = accessory._ini.INFO_INCLUDES_LEGACY;
	public static final String INFO_USER = _types.INI_INFO_USER;
	public static final String INFO_ACCOUNT_IB = _types.INI_INFO_ACCOUNT_IB;
	public static final String INFO_ACCOUNT_TYPE_IB = _types.INI_INFO_ACCOUNT_TYPE_IB;
	public static final String INFO_CONN_ID_IB = _types.INI_INFO_CONN_ID_IB;
	public static final String INFO_CONN_TYPE_IB = _types.INI_INFO_CONN_TYPE_IB;

	public static final String DBS_SETUP = accessory._ini.DBS_SETUP;
	public static final String DBS_SETUP_NAME = accessory._ini.DBS_SETUP_NAME;
	public static final String DBS_SETUP_MAX_POOL = accessory._ini.DBS_SETUP_MAX_POOL;
	public static final String DBS_SETUP_HOST = accessory._ini.DBS_SETUP_HOST;
	public static final String DBS_SETUP_TYPE = accessory._ini.DBS_SETUP_TYPE;
	public static final String DBS_SETUP_CREDENTIALS_USERNAME = accessory._ini.DBS_SETUP_CREDENTIALS_USERNAME;
	public static final String DBS_SETUP_CREDENTIALS_PASSWORD = accessory._ini.DBS_SETUP_CREDENTIALS_PASSWORD;
	public static final String DBS_SETUP_CREDENTIALS_USER = accessory._ini.DBS_SETUP_CREDENTIALS_USER;
	public static final String DBS_SETUP_CREDENTIALS_ENCRYPTED = accessory._ini.DBS_SETUP_CREDENTIALS_ENCRYPTED;
	public static final String DBS_SETUP_CREDENTIALS_MEMORY = accessory._ini.DBS_SETUP_CREDENTIALS_MEMORY;
	
	public static final int DEFAULT_INFO_CONN_ID_IB = conn.WRONG_ID;	
	public static final boolean DEFAULT_IGNORE_IB_INFO = false;
	public static final boolean DEFAULT_INCLUDES_LEGACY = accessory._ini.DEFAULT_INCLUDES_LEGACY;
	public static final String DEFAULT_NAME = accessory._ini.DEFAULT_NAME;
	public static final String DEFAULT_USER = accessory._ini.DEFAULT_USER;
	public static final boolean DEFAULT_CREDENTIALS_ENCRYPTED = accessory._ini.DEFAULT_CREDENTIALS_ENCRYPTED;
	public static final boolean DEFAULT_CREDENTIALS_MEMORY = accessory._ini.DEFAULT_CREDENTIALS_MEMORY;
	
	private static _ini _instance = new _ini();
		
	public _ini() { }
	
	public static String get_user() { return _instance.USER; }

	public static HashMap<String, Object> get_info(String app_name_, String user_, String account_ib_, String conn_type_ib_, int conn_id_ib_) { return get_info(app_name_, user_, DEFAULT_INCLUDES_LEGACY, account_ib_, conn_type_ib_, conn_id_ib_); }
	
	public static HashMap<String, Object> get_info(String app_name_, String user_, boolean includes_legacy_, String account_ib_, String conn_type_ib_, int conn_id_ib_) { return get_info(app_name_, user_, includes_legacy_, account_ib_, null, conn_type_ib_, conn_id_ib_); }
	
	public static HashMap<String, Object> get_info(String app_name_, String user_, boolean includes_legacy_, String account_ib_, String account_type_ib_, String conn_type_ib_, int conn_id_ib_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		
		if (strings.is_ok(app_name_)) info.put(INFO_APP_NAME, app_name_);
		info.put(INFO_INCLUDES_LEGACY, includes_legacy_);
		
		if (strings.is_ok(user_)) info.put(INFO_USER, user_);
		if (strings.is_ok(account_ib_)) info.put(INFO_ACCOUNT_IB, account_ib_);
		
		if (strings.is_ok(account_type_ib_)) info.put(INFO_ACCOUNT_TYPE_IB, account_type_ib_);
		if (strings.is_ok(conn_type_ib_)) info.put(INFO_CONN_TYPE_IB, conn_type_ib_);
		
		if (conn.id_is_ok(conn_id_ib_)) info.put(INFO_CONN_ID_IB, conn_id_ib_);
		
		return info;
	}

	public static Object get_info_val(HashMap<String, Object> info_, String key_)
	{
		Object output = null;
		if (info_ == null || key_ == null) return output;
		
		output = accessory._ini.get_info_val(info_, key_);
		if (output != null) return output;
		
		if (info_.containsKey(key_)) output = info_.get(key_);
		
		if (key_.equals(INFO_USER) || key_.equals(INFO_ACCOUNT_IB) || key_.equals(INFO_ACCOUNT_TYPE_IB) || key_.equals(INFO_CONN_TYPE_IB))
		{
			if (!generic.is_string(output)) output = strings.DEFAULT;
			else if (key_.equals(INFO_ACCOUNT_TYPE_IB)) output = basic.DEFAULT_TYPE;
			else if (key_.equals(INFO_CONN_TYPE_IB)) output = conn.DEFAULT_TYPE;
		}
		else if (key_.equals(INFO_CONN_ID_IB)) output = (generic.is_number(output) ? numbers.to_int(numbers.to_number(output)) : DEFAULT_INFO_CONN_ID_IB);

		return output;
	}

	public static HashMap<String, Object> get_dbs_setup(String db_name_, String setup_, String user_, String host_, boolean encrypted_) { return accessory._ini.get_dbs_setup(db_name_, setup_, user_, host_, encrypted_); }
	
	public static HashMap<String, Object> get_dbs_setup(String setup_, String user_, String host_, boolean encrypted_) { return accessory._ini.get_dbs_setup(setup_, user_, host_, encrypted_); }

	public static HashMap<String, Object> get_dbs_setup(String setup_, String username_, String password_, String host_) { return accessory._ini.get_dbs_setup(setup_, username_, password_, host_); }
	
	public static Object get_dbs_setup_val(HashMap<String, Object> dbs_setup_, String key_) { return accessory._ini.get_dbs_setup_val(dbs_setup_, key_); }
	
	public static void start() { if (!_instance._populated) start(null, DEFAULT_IGNORE_IB_INFO); }
	
	public static void start(HashMap<String, Object> info_) { start(info_, DEFAULT_IGNORE_IB_INFO); }
	
	public static void start(HashMap<String, Object> info_, boolean ignore_ib_info_) 
	{
		if (_instance._populated) return;

		String name = (String)get_info_val(info_, INFO_APP_NAME);
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name, (boolean)get_info_val(info_, INFO_INCLUDES_LEGACY));
			
		_instance.populate_all(name);

		populate_inis(info_, ignore_ib_info_);
	}

	public static void start(HashMap<String, Object> info_, HashMap<String, Object> dbs_setup_) { start(info_, dbs_setup_, null); }
	
	public static void start(HashMap<String, Object> info_, HashMap<String, Object> dbs_setup_, String[] types_to_ignore_) { start(info_, dbs_setup_, types_to_ignore_, DEFAULT_IGNORE_IB_INFO); }
	
	public static void start(HashMap<String, Object> info_, HashMap<String, Object> dbs_setup_, String[] types_to_ignore_, boolean ignore_ib_info_) 
	{ 
		if (_instance._populated) return;
		
		HashMap<String, Object> info = (info_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(info_));
		HashMap<String, Object> dbs_setup = (dbs_setup_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(dbs_setup_));

		String user = (String)get_info_val(info_, INFO_USER);
		String name = (String)get_info_val(info_, INFO_APP_NAME);
		
		boolean update_dbs_user = false;

		if 
		(
			!strings.is_ok((String)get_dbs_setup_val(dbs_setup_, DBS_SETUP_CREDENTIALS_USER)) ||
			!strings.is_ok((String)get_dbs_setup_val(dbs_setup_, DBS_SETUP_CREDENTIALS_USERNAME)) ||
			!strings.is_ok((String)get_dbs_setup_val(dbs_setup_, DBS_SETUP_CREDENTIALS_PASSWORD))
		)
		{
			update_dbs_user = true;
			
			dbs_setup.put(DBS_SETUP_CREDENTIALS_USER, user);			
		}
		
		if (!accessory._ini.is_populated()) accessory._ini.start(name, (boolean)get_info_val(info, INFO_INCLUDES_LEGACY), dbs_setup);		
		
		_instance.populate_all(name, dbs_setup, types_to_ignore_); 

		info.put(INFO_USER, user);
		
		populate_inis(info, ignore_ib_info_);
		
		if (update_dbs_user) accessory.config.update(db.get_current_setup(), accessory._types.CONFIG_DB_SETUP_CREDENTIALS_USER, ini_basic.get_user());
	}
	
	private static void populate_inis(HashMap<String, Object> info_, boolean ignore_ib_info_) 
	{
		crypto.store_in_db();
		
		ini_common.start();
		
		ini_market.start();

		String conn_type = (String)get_info_val(info_, INFO_CONN_TYPE_IB);
		
		String account_type = (String)get_info_val(info_, INFO_ACCOUNT_TYPE_IB);
		if (!basic.type_is_ok(account_type)) account_type = basic.get_type_from_conn(conn_type);

		if (conn.type_is_ok(conn_type) && basic.type_is_ok(account_type))
		{
			if (conn.type_is_real(conn_type))
			{
				if (!account_type.equals(basic.TYPE_REAL)) account_type = basic.TYPE_REAL;
			}
			else if (!account_type.equals(basic.TYPE_PAPER)) account_type = basic.TYPE_PAPER;
		}

		ini_basic.start((String)get_info_val(info_, INFO_USER), (String)get_info_val(info_, INFO_ACCOUNT_IB), account_type, ignore_ib_info_);

		ini_apps.start((String)get_info_val(info_, INFO_APP_NAME), conn_type, (int)get_info_val(info_, INFO_CONN_ID_IB), ignore_ib_info_);
	}
}