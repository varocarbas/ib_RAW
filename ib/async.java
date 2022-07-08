package ib;

import accessory.parent_static;

abstract class async extends parent_static 
{	
	public static final int WRONG_ID = common.WRONG_ID;
	
	private static volatile int _last_id = WRONG_ID;

	public static int get_last_id() { return _last_id; }

	public static void update_last_id(int id_) { _last_id = id_; }

	static int get_req_id() { return common_xsync.get_req_id(false); }
}