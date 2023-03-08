package db_ib;

import accessory.db;
import accessory.db_cache;
import accessory.db_cache_mysql;

abstract class apps_cache 
{
	private static String[] COLS = null;

	private static String COL_APP = null;
	private static String COL_STATUS = null;
	
	public static int IS_RUNNING = db_cache.WRONG_ID;
	
	public static boolean is_running(String app_) 
	{ 
		start(app_);
		
		return db_cache_mysql.exists_simple(IS_RUNNING, null);

		//return db_cache_mysql.exists_simple(IS_RUNNING, cache.add_changing_val(get_col_id(COL_APP), app_));
	}
	
	private static void start(String app_)
	{
		if (COLS != null) return;
		
		populate_cols();
		
		add_queries(app_);
	}
	
	private static void populate_cols()
	{
		String[] fields = new String[] { apps.APP, apps.STATUS };
		
		int tot = fields.length;
		
		COLS = new String[tot];
		
		for (int i = 0; i < tot; i++) 
		{ 
			String field = fields[i];
			String col = db.get_col(apps.SOURCE, field); 
			
			if (field.equals(apps.APP)) COL_APP = col;
			else if (field.equals(apps.STATUS)) COL_STATUS = col;
			
			COLS[i] = col; 
		}
	}
	
	private static void add_queries(String app_)
	{
		add_is_running(app_);
	}
	
	private static void add_is_running(String app_)
	{
		String source = apps.SOURCE;
		
		String where = cache.get_variable(source, COL_APP) + "=" + cache.get_value(source, app_);
		
		where += " AND " + cache.get_variable(source, COL_STATUS) + "=" + cache.get_value(source, apps.store_status_type(ib.apps.STATUS_RUNNING));
		
		String query = cache.get_query_exists(source, where);
		
		IS_RUNNING = cache.add_query(source, query, true);		
	}
}