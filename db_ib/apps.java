package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.db_where;
import accessory.strings;
import ib.conn;
import ib.ini_apps;

public abstract class apps 
{
	public static final String SOURCE = common.SOURCE_APPS;
	
	public static final String USER = common.FIELD_USER;
	public static final String APP = common.FIELD_APP;
	public static final String CONN_ID = common.FIELD_CONN_ID;
	public static final String CONN_TYPE = common.FIELD_CONN_TYPE;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String ERROR = common.FIELD_ERROR;
	public static final String ADDITIONAL = common.FIELD_ADDITIONAL;
	public static final String TIME2 = common.FIELD_TIME2;
	
	public static final int MIN_COUNT = 1;

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists() { return common.exists(SOURCE, get_where_app()); }

	public static ArrayList<HashMap<String, String>> get_all_active() { return get_all_active(true, true); }

	public static ArrayList<HashMap<String, String>> get_all_active(boolean errors_too_, boolean any_user_) { return common.get_all_vals(SOURCE, null, get_where_active(errors_too_, any_user_)); }

	public static boolean contains_active() { return (common.contains_active(SOURCE) || common.exists(SOURCE, get_where_active(true, true))); }
	
	public static String get_app_name_ini() { return common.get_string(SOURCE, APP, common.get_where_user(SOURCE)); }

	public static int get_conn_id() { return common.get_int(SOURCE, CONN_ID, get_where_app(), false); }

	public static String get_conn_type() { return common.get_string(SOURCE, CONN_TYPE, get_where_app()); }

	public static String get_status() { return common.get_type(SOURCE, STATUS, ib.apps.STATUS, get_where_app()); }

	public static String get_error() { return common.get_string(SOURCE, ERROR, get_where_app()); }

	public static String get_additional() { return common.get_string(SOURCE, ADDITIONAL, get_where_app()); }
	
	public static boolean update_app_name() { return update(APP, ini_apps.get_app_name()); }

	public static boolean update_app_name(String val_) { return (strings.is_ok(ini_apps.get_app_name()) ? update_app_name() : update(APP, val_)); }

	public static boolean update_conn_id() { return update(CONN_ID, ini_apps.get_conn_id()); }

	public static boolean update_conn_id(int val_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		if (!exists()) vals.put(APP, ini_apps.get_app_name());
		
		int val0 = ini_apps.get_conn_id(); 
		int val = (conn.id_is_ok(val0) ? val0 : val_);
		
		vals.put(CONN_ID, val);
		
		return update(vals);
	}
	
	public static boolean update_conn_type() { return update(CONN_TYPE, ini_apps.get_conn_type()); }
	
	public static boolean update_conn_type(String val_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		if (!exists()) vals.put(APP, ini_apps.get_app_name());
		
		String val0 = ini_apps.get_conn_type(); 
		String val = (conn.type_is_ok(val0) ? val0 : val_);
		
		vals.put(CONN_TYPE, val);
		
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

	public static boolean update_status(String status_) 
	{
		String status = ib.apps.check_status(status_);
		
		return (strings.is_ok(status) ? update(STATUS, get_key_from_status(status)) : false);
	}
	
	public static boolean update(HashMap<String, Object> vals_) 
	{ 
		vals_.put(TIME2, ib.common.get_current_time2());
		
		return common.insert_update(SOURCE, vals_, get_where_app()); 
	}

	public static boolean update_time() { return common.insert_update(SOURCE, TIME2, ib.common.get_current_time2(), get_where_app()); }
	
	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.apps.STATUS); }

	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.apps.STATUS); }

	private static boolean update(String field_, Object val_) { return common.insert_update(SOURCE, field_, val_, ((!strings.is_ok(ini_apps.get_app_name()) && field_.equals(APP)) ? get_where_app((String)val_) : get_where_app())); }

	private static String get_where_active(boolean errors_too_, boolean any_user_) 
	{ 
		String output = get_where_status(ib.apps.STATUS_RUNNING, any_user_);
		
		if (errors_too_) output = common.join_wheres(output, get_where_status(ib.apps.STATUS_ERROR, any_user_), db_where.LINK_OR);
		
		return output; 
	}

	private static String get_where_status(String status_, boolean any_user_) { return common.get_where(SOURCE, STATUS, get_key_from_status(status_), false, !any_user_); }
	
	private static String get_where_app() { return get_where_app(ini_apps.get_app_name()); }

	private static String get_where_app(String app_) { return common.get_where(SOURCE, APP, app_, false); }
}