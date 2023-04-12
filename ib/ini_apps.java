package ib;

import java.util.HashMap;

import accessory.db;
import accessory.db_common;
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
	
	public static void start(String app_name_, String conn_type_, int conn_id_, boolean ignore_ib_info_)
	{
		db.create_table(SOURCE, false);

		start_all(app_name_, conn_type_, conn_id_, ignore_ib_info_);

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_ib.apps.APP, _app_name);
		vals.put(db_ib.apps.CONN_TYPE, conn.get_type_key(conn_type_));
		vals.put(db_ib.apps.CONN_ID, _conn_id);
		
		db_ib.apps.update(vals);
	}

	private static void start_all(String app_name_, String conn_type_, int conn_id_, boolean ignore_ib_info_)
	{
		start_app_name(app_name_); 
		
		start_conn_type(conn_type_, ignore_ib_info_); 

		start_conn_id(conn_id_, ignore_ib_info_);
	}
	
	private static void start_app_name(String app_name_) 
	{ 
		String app_name = get_app_name(app_name_);
				
		_app_name = app_name;
	}
	
	private static String get_app_name(String app_name_) { return db_common.adapt_string((strings.is_ok(app_name_) ? app_name_ : apps.DEFAULT_APP_NAME), db_ib.common.MAX_SIZE_APP_NAME); }
	
	private static void start_conn_id(int conn_id_, boolean ignore_ib_info_) 
	{ 
		int conn_id = conn.WRONG_ID;
		
		if (!ignore_ib_info_)
		{
			conn_id = conn_id_;
			
			if (!conn.id_is_ok(conn_id)) conn_id = db_ib.apps.get_conn_id();
			if (!conn.id_is_ok(conn_id)) conn_id = conn.DEFAULT_ID;	
		}

		_conn_id = conn_id;
	}

	private static void start_conn_type(String conn_type_, boolean ignore_ib_info_) 
	{ 
		String conn_type = strings.DEFAULT;
		
		if (!ignore_ib_info_)
		{
			conn_type = conn_type_;
			
			if (!conn.type_is_ok(conn_type)) conn_type = db_ib.apps.get_conn_type();
			if (!conn.type_is_ok(conn_type)) conn_type = conn.DEFAULT_TYPE;
		}
		
		_conn_type = conn_type;
	}
}