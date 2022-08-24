package ib;

import java.util.HashMap;

abstract class sync_basic 
{
	public static final double WRONG_MONEY = common.WRONG_MONEY;
	
	public static void account_summary(int id_, String account_, String tag_, String value_, String currency_) { sync.update_funds(id_, account_, tag_, value_, currency_); }
	
	public static void account_summary_end(int id_) 
	{
		if (!sync.is_ok(id_)) return;

		sync.end_get();
	}

	static HashMap<String, Double> get_money() { return sync.get_funds(); }
}