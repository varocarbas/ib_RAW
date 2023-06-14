package db_ib;

import accessory.arrays_quick;
import accessory.db;
import accessory.db_cache;
import accessory.db_cache_mysql;

abstract class apps_cache 
{	
	public static int IS_RUNNING = db_cache.WRONG_ID;
	public static int IS_STOPPED = db_cache.WRONG_ID;

	private static String[] _cols = null;
	
	public static boolean is_running(String app_) { return is_common(app_, true); }
	
	public static boolean is_stopped(String app_) { return is_common(app_, false); }

	public static void reset_ids() { populate(true); }
	
	private static boolean is_common(String app_, boolean is_running_) 
	{ 
		populate(false);

		int id = (is_running_ ? IS_RUNNING : IS_STOPPED);
		
		int col_id = get_col_id(db.get_col(apps.SOURCE, apps.APP));
		String col_val = ib.apps.get_app(app_);

		return db_cache_mysql.exists_simple(id, db_cache.add_changing_val(col_id, col_val));
	}
	
	private static void populate(boolean force_population_)
	{
		if (!force_population_ && _cols != null) return;

		_cols = db.get_cols(apps.SOURCE);
		
		add_queries();
	}
	
	private static void add_queries()
	{
		add_is_running();
		
		add_is_stopped();
	}
	
	private static void add_is_running() { add_is_common(true); }
	
	private static void add_is_stopped() { add_is_common(false); }
	
	private static void add_is_common(boolean is_running_)
	{
		String source = apps.SOURCE;
		
		String col = db.get_col(source, apps.APP);
		int col_id = get_col_id(col);
		
		String where = db_cache.get_variable(source, db.get_col(source, apps.STATUS)) + "=" + db_cache.get_value(source, apps.store_status_type((is_running_ ? ib.apps.STATUS_RUNNING : ib.apps.STATUS_STOPPED)));
		
		int id = db_cache_mysql.add_select_count(source, col, col_id, where, true);

		if (is_running_) IS_RUNNING = id;
		else IS_STOPPED = id;
	}
	
	private static int get_col_id(String col_) { return arrays_quick.get_i(_cols, col_); }
}