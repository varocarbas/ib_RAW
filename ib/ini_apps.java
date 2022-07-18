package ib;

import accessory.db;
import accessory.strings;

public abstract class ini_apps 
{
	public static final String SOURCE = db_ib.apps.SOURCE;

	private static String _app_name = strings.DEFAULT;
	private static int _conn_id = conn.WRONG_ID;
	private static String _conn_type = strings.DEFAULT;

	public static String get_app_name() { return _app_name; }
	
	public static int get_conn_id() { return _conn_id; }	
	
	public static String get_conn_type() { return _conn_type; }
	
	public static void update_conn_type(String conn_type_)
	{
		if (conn.type_is_ok(conn_type_)) _conn_type = conn_type_;
	}
	
	public static void start(String app_name_, String conn_type_, int conn_id_)
	{
		db.create_table(SOURCE, false);
		
		populate_app_name(app_name_); 
		
		populate_conn_type(conn_type_); 

		populate_conn_id(conn_id_);
	}

	private static String populate_app_name(String app_name_) 
	{ 
		String app_name = get_app_name_prelimary(app_name_);
		
		if (!strings.is_ok(app_name)) app_name = db_ib.common.adapt_string(db_ib.apps.get_app_name_ini(), db_ib.common.MAX_SIZE_APP_NAME);
		
		db_ib.apps.update_app_name(app_name);

		_app_name = app_name;
		
		return app_name;
	}
	
	private static String get_app_name_prelimary(String app_name_) { return db_ib.common.adapt_string((strings.is_ok(app_name_) ? app_name_ : apps.DEFAULT_APP_NAME), db_ib.common.MAX_SIZE_APP_NAME); }
	
	private static void populate_conn_id(int conn_id_) 
	{ 
		int conn_id = conn_id_;
		
		if (!conn.id_is_ok(conn_id)) conn_id = db_ib.apps.get_conn_id();
		if (!conn.id_is_ok(conn_id)) conn_id = conn.DEFAULT_ID;
		
		db_ib.apps.update_conn_id(conn_id);

		_conn_id = conn_id;
	}

	private static void populate_conn_type(String conn_type_) 
	{ 
		String conn_type = conn_type_;
		
		if (!conn.type_is_ok(conn_type)) conn_type = db_ib.apps.get_conn_type();
		if (!conn.type_is_ok(conn_type)) conn_type = conn.DEFAULT_TYPE;
		
		db_ib.apps.update_conn_type(conn_type);

		_conn_type = conn_type;
	}
}