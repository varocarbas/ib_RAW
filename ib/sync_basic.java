package ib;

import java.util.HashMap;

import external_ib.contracts;

abstract class sync_basic 
{
	public static final double WRONG_MONEY = common.WRONG_MONEY;
	
	public static void account_summary(int id_, String account_, String tag_, String value_, String currency_) 
	{
		if (!is_ok(id_, account_, tag_, currency_)) return;

		sync.update_funds(tag_, value_);
	}
	
	public static void account_summary_end(int id_) 
	{
		if (!sync.is_ok(id_)) return;

		sync.end_get();
	}

	static HashMap<String, Double> get_money() { return sync.get_funds(); }
	
	private static boolean is_ok(int req_id_, String account_id_, String key_, String currency_) { return (sync.is_ok(req_id_, key_) && ib.basic.account_ib_is_ok(account_id_) && contracts.currency_is_ok(currency_)); }
}