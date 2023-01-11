package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db;
import accessory.db_common;
import accessory.db_where;
import accessory.strings;
import ib.conn;
import ib.ini_apps;

public abstract class apps 
{
	public static final String SOURCE = common.SOURCE_APPS;
	public static final String SOURCE_OLD = common.SOURCE_APPS_OLD;
	
	public static final String USER = common.FIELD_USER;
	public static final String APP = common.FIELD_APP;
	public static final String CONN_ID = common.FIELD_CONN_ID;
	public static final String CONN_TYPE = common.FIELD_CONN_TYPE;
	public static final String CONN_IS_ON = common.FIELD_CONN_IS_ON;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String ERROR = common.FIELD_ERROR;
	public static final String ADDITIONAL = common.FIELD_ADDITIONAL;
	public static final String TIME2 = common.FIELD_TIME2;
	
	public static final int MIN_COUNT = 1;
	
	static String[] _fields = null;
	static String[] _cols = null;
	static HashMap<String, String> _fields_cols = null;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists() { return common.exists(SOURCE, get_where_app()); }

	public static ArrayList<HashMap<String, String>> get_all_active() { return get_all_active(true, true); }

	public static ArrayList<HashMap<String, String>> get_all_active(boolean errors_too_, boolean any_user_) { return common.get_all_vals(SOURCE, null, get_where_active(errors_too_, any_user_)); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || common.exists(SOURCE, get_where_active(true, true))); }
	
	public static String get_app_name_ini() { return common.get_string(SOURCE, APP, common.get_where_user(SOURCE)); }

	public static int get_conn_id() { return common.get_int(SOURCE, CONN_ID, get_where_app()); }

	public static String get_conn_type() { return common.get_string(SOURCE, CONN_TYPE, get_where_app()); }

	public static String get_status() { return common.get_type(SOURCE, STATUS, ib.apps.STATUS, get_where_app()); }

	public static String get_error() { return common.get_string(SOURCE, ERROR, get_where_app()); }

	public static String get_additional() { return common.get_string(SOURCE, ADDITIONAL, get_where_app()); }
	
	public static boolean is_running(String app_) { return is_common(ib.apps.STATUS_RUNNING, app_); }
	
	public static boolean is_stopped(String app_) { return is_common(ib.apps.STATUS_STOPPED, app_); }
	
	public static boolean is_connected(String app_) { return common.exists(SOURCE, common.join_wheres(get_where_app(app_), get_where_connected(true))); }
	
	public static boolean update_app_name() { return update(APP, ini_apps.get_app_name(), ini_apps.get_app_name()); }

	public static boolean update_app_name(String val_) 
	{ 
		String app = val_;
		if (strings.is_ok(ini_apps.get_app_name())) app = ini_apps.get_app_name();
		
		return (strings.is_ok(app) ? update(APP, app, app) : false); 
	}

	public static boolean update_conn_id() { return update(CONN_ID, ini_apps.get_conn_id()); }

	public static boolean update_conn_id(int val_) 
	{ 
		Object vals = null;
		
		if (!exists()) vals = common.add_to_vals(SOURCE, APP, ini_apps.get_app_name(), null);
		
		int val0 = ini_apps.get_conn_id(); 
		int val = (conn.id_is_ok(val0) ? val0 : val_);
		
		vals = common.add_to_vals(SOURCE, CONN_ID, val, vals);
		
		return update(vals);
	}
	
	public static boolean update_conn_type() { return update(CONN_TYPE, conn.get_type_key(ini_apps.get_conn_type())); }
	
	public static boolean update_conn_type(String val_) 
	{ 
		Object vals = null;
				
		if (!exists()) vals = common.add_to_vals(SOURCE, APP, ini_apps.get_app_name(), vals);
		
		String val0 = ini_apps.get_conn_type(); 
		String val = conn.get_type_key(conn.type_is_ok(val0) ? val0 : val_);
		
		vals = common.add_to_vals(SOURCE, CONN_TYPE, val, vals);
		
		return update(vals);
	}

	public static boolean update_error(String val_) 
	{
		update_status(ib.apps.STATUS_ERROR);
		
		String val = common.adapt_string(val_, ERROR);
		
		return (strings.is_ok(val) ? update(ERROR, val) : false); 
	}

	public static boolean update_additional(String val_) 
	{
		String val = common.adapt_string(val_, ADDITIONAL);
		
		return (strings.is_ok(val) ? update(ADDITIONAL, val) : false); 
	}

	public static boolean update_status(String status_) { return update_status(ib.ini_apps.get_app_name(), status_); }
	
	public static boolean update_status(String app_, String status_) 
	{
		String key = store_status_type(status_);

		return (strings.is_ok(key) ? update(STATUS, key, app_) : false);
	}
	
	public static boolean update_is_connected(String app_, boolean is_connected_) { return update(CONN_IS_ON, is_connected_, app_); }

	public static boolean update(Object vals_) 
	{ 
		if (!common.vals_are_ok(SOURCE, vals_)) return false;
		
		Object vals = get_vals(vals_, (String)common.get_from_vals(SOURCE, APP, vals_));

		return (vals != null ? insert_update(vals) : false); 
	}
	
	public static boolean update_time() 
	{
		String where = get_where_app();

		return (strings.is_ok(where) ? common.update(SOURCE, TIME2, ib.common.get_current_time2(), where) : false); 
	}

	public static String get_time(String app_, String status_) 
	{
		String where = get_where_app(app_);
		if (strings.is_ok(status_)) where = common.join_wheres(where, get_where_status(status_));
		
		String output = common.get_string(SOURCE, TIME2, where);
		
		return (strings.are_equal(output, db.WRONG_STRING) ? strings.DEFAULT : output);
	}
	
	public static String store_status_type(String status_type_) { return ib.apps.get_status_key(status_type_); }

	public static String get_status_type(String status_key_) { return ib.apps.get_status_type(status_key_); }
	
	static void populate_fields() { _fields = db_common.add_default_fields(SOURCE, new String[] { USER, APP, CONN_ID, CONN_TYPE, CONN_IS_ON, STATUS, ERROR, ADDITIONAL, TIME2 }); }
	
	private static boolean is_common(String status_, String app_) { return common.exists(SOURCE, common.join_wheres(get_where_app(app_), get_where_status(status_))); }

	private static boolean update(String field_, Object val_) { return update(field_, val_, ini_apps.get_app_name()); }

	private static boolean update(String field_, Object val_, String app_) 
	{ 
		Object vals = get_vals(field_, val_, app_);

		return (vals != null ? insert_update(vals) : false);
	}

	private static boolean insert_update(Object vals_) 
	{
		String where = get_where_app((String)common.get_from_vals(SOURCE, APP, vals_));

		return (common.exists(SOURCE, where) ? common.update(SOURCE, vals_, where) : common.insert(SOURCE, vals_)); 		
	}
	
	private static Object get_vals(Object vals_, String app_) { return get_vals_common(vals_, app_); }

	private static Object get_vals(String field_, Object val_, String app_) 
	{ 
		Object vals = null;
		
		if (strings.is_ok(field_) && val_ != null) vals = common.add_to_vals(SOURCE, field_, db.adapt_input(val_), vals);

		return get_vals_common(vals, app_);
	}
	
	@SuppressWarnings("unchecked")
	private static Object get_vals_common(Object vals_, String app_) 
	{ 
		Object vals = (common.is_quick(SOURCE) ? arrays.get_new_hashmap_xx((HashMap<String, String>)vals_) : arrays.get_new_hashmap_xy((HashMap<String, Object>)vals_));
		
		if (strings.is_ok(app_) || !common.field_exists(SOURCE, APP, vals_))
		{
			String app = app_;

			app = ini_apps.get_app_name();
			if (!strings.is_ok(app)) return null;
			
			vals = common.add_to_vals(SOURCE, APP, app, vals);
		}
		
		vals = common.add_to_vals(SOURCE, TIME2, ib.common.get_current_time2(), vals);
		
		return vals;
	}

	private static String get_where_active(boolean errors_too_, boolean any_user_) 
	{ 
		String output = get_where_status(ib.apps.STATUS_RUNNING);
		
		if (errors_too_) output = common.join_wheres(output, get_where_status(ib.apps.STATUS_ERROR), db_where.LINK_OR);
		
		return output; 
	}

	private static String get_where_status(String status_) { return common.get_where(SOURCE, STATUS, store_status_type(status_), false); }
	
	private static String get_where_app() { return get_where_app(ini_apps.get_app_name()); }

	private static String get_where_app(String app_) 
	{ 
		String app = (strings.is_ok(app_) ? app_ : ini_apps.get_app_name());
				
		return (strings.is_ok(app) ? common.get_where(SOURCE, APP, app, false) : strings.DEFAULT); 
	}

	private static String get_where_connected(boolean is_connected_) { return common.get_where(SOURCE, CONN_IS_ON, db.adapt_input(is_connected_), false); }
}