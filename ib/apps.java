package ib;

import accessory.logs;
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
	public static final String DEFAULT_STATUS = STATUS_STOPPED;
	
	public static boolean start()
	{
		String app = get_app_name(true);
				
		get_conn_type();

		get_conn_id();

		boolean is_running = is_running();
		
		if (is_running) logs.update_screen(app + " already running");
		else 
		{
			db_ib.apps.update_status(ib.apps.STATUS_RUNNING);
			
			logs.update_screen(app + " started");
		}
		
		return !is_running;
	}

	public static void stop() 
	{ 
		if (db_ib.apps.exists()) db_ib.apps.update_status(STATUS_STOPPED); 
		
		logs.update_screen(ini_apps.get_app_name() + " stopped");
	}
	
	public static boolean is_running() { return db_ib.apps.is_running(null); }
	
	public static boolean is_stopped() { return !is_running(); }
	
	public static void update_time() { db_ib.apps.update_time(); }

	public static String get_app_name() { return get_app_name(false); }
	
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

	private static String get_app_name(boolean is_ini_) 
	{
		if (is_ini_) db_ib.apps.update_app_name(); 
		
		return ini_apps.get_app_name();
	} 
}