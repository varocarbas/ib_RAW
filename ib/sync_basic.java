package ib;

import java.util.HashMap;

abstract class sync_basic 
{
	public static final double WRONG_MONEY = common.WRONG_MONEY;

	public static boolean is_ok(int id_) { return sync.is_ok(id_); }
	
	public static void update_funds(String tag_, String value_) { sync.update_funds(tag_, value_); }
	
	public static void account_summary_end() { sync.end_get(); }

	public static HashMap<String, Double> __get_money() { return sync.__get_funds(); }	
}