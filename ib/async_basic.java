package ib;

import external_ib.calls;

abstract class async_basic 
{
	private static volatile int _id = async.WRONG_ID;
	
	public static boolean is_ok(int id_) { return (id_ == _id); }
	
	public static void get_funds()
	{
		_id = async.get_req_id();

		calls.reqAccountSummary(_id);
	}
	
	public static void update_funds(String key_, String value_) { db_ib.basic.update_money_common(calls.get_all_keys_funds().get(key_), Double.parseDouble(value_)); }
	
	public static void account_summary_end(int id_) { calls.cancelAccountSummary(id_); }
}