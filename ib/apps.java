package ib;

import accessory.strings;
import accessory_ib._defaults;
import accessory_ib.types;

public abstract class apps 
{
	public static final String STATUS = types.APPS_STATUS;
	public static final String STATUS_STOPPED = types.APPS_STATUS_STOPPED;
	public static final String STATUS_RUNNING = types.APPS_STATUS_RUNNING;
	public static final String STATUS_ERROR = types.APPS_STATUS_ERROR;

	public static final String DEFAULT_APP_NAME = _defaults.APP_NAME;
	public static final String DEFAULT_STATUS = STATUS_RUNNING;
	
	public static boolean start()
	{
		if (!can_start()) return false;
		
		start_internal();
		
		db_ib.apps.update_status(STATUS_RUNNING);
		
		return true;
	}

	public static void end() { db_ib.apps.update_status(STATUS_STOPPED); }
	
	public static boolean can_start() { return !strings.are_equal(db_ib.apps.get_status(), STATUS_RUNNING); }
	
	public static void update_time() { db_ib.apps.update_time(); }

	public static String get_app_name() 
	{
		db_ib.apps.update_app_name(); 
		
		return ini_apps.get_app_name();
	} 
	
	public static String get_conn_type() 
	{ 
		String conn_type = db_ib.apps.get_conn_type();

		return update_conn_type(conn_type);
	} 
	
	public static String update_conn_type(String conn_type_) 
	{ 
		String conn_type = conn_type_;
		
		if (!conn.type_is_ok(conn_type)) conn_type = ini_apps.get_conn_type();
		if (!conn.type_is_ok(conn_type)) conn_type = conn.DEFAULT_TYPE;

		db_ib.apps.update_conn_type(conn_type);
		
		return conn_type;
	}

	public static int get_conn_id() 
	{
		db_ib.apps.update_conn_id(); 
		
		return ini_apps.get_conn_id();
	}

	public static boolean status_is_ok(String status_) { return strings.is_ok(check_status(status_)); }

	public static String check_status(String status_) { return accessory.types.check_type(status_, STATUS); }

	private static void start_internal()
	{
		get_app_name();
		
		get_conn_type();

		get_conn_id();
	}
}