package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory_ib.types;

public abstract class remote 
{
	public static final String STATUS = types.REMOTE_STATUS;
	public static final String STATUS_ACTIVE = types.REMOTE_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.REMOTE_STATUS_INACTIVE;
	public static final String STATUS_ERROR = types.REMOTE_STATUS_ERROR;

	public static final String STATUS2 = types.REMOTE_STATUS2;
	public static final String STATUS2_PENDING = types.REMOTE_STATUS2_PENDING;
	public static final String STATUS2_EXECUTED = types.REMOTE_STATUS2_EXECUTED;
	public static final String STATUS2_ERROR = types.REMOTE_STATUS2_ERROR;

	public static final String DEFAULT_STATUS = STATUS_ACTIVE;
	public static final String DEFAULT_STATUS2 = STATUS2_EXECUTED;
	
	public static void check_db()
	{
		ArrayList<HashMap<String, String>> all = db_ib.remote.get_active();
		if (!arrays.is_ok(all)) return;
		
		for (HashMap<String, String> item: all) { check_item(item); }
	}
	
	private static void check_item(HashMap<String, String> item_)
	{
		
	}
}