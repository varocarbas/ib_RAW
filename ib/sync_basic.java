package ib;

import accessory.strings;
import db_ib.basic;
import external_ib.contracts;

abstract class sync_basic 
{
	public static void start()
	{
		get_conn_type();

		get_account_ib();
		
		get_conn_id();
		
		get_currency();
		
		get_funds(true);
	}
	
	public static String get_conn_type() 
	{ 
		String conn_type = basic.get_conn_type();

		return update_conn_type(conn_type);
	} 
	
	public static String update_conn_type(String conn_type_) 
	{ 
		String conn_type = conn_type_;
		
		if (!conn.type_is_ok(conn_type)) conn_type = ini_basic.get_conn_type();
		if (!conn.type_is_ok(conn_type)) conn_type = conn.DEFAULT_TYPE;

		basic.update_conn_type(conn_type);
		
		return conn_type;
	}

	public static String get_account_ib() 
	{
		basic.update_account_ib(); 
		
		return get_account_ib_last(ini_basic.get_account_ib(), true);
	} 

	public static int get_conn_id() 
	{
		basic.update_conn_id(); 
		
		return ini_basic.get_conn_id();
	} 

	public static double get_funds() { return get_funds(false); } 

	public static String get_currency() 
	{
		String currency = contracts.get_currency();
		
		basic.update_currency(currency); 
		
		return currency;
	} 

	public static void account_summary(int id_, String account_, String tag_, String value_, String currency_) 
	{
		if (!is_ok(id_, account_, tag_, currency_)) return;

		sync.update(value_);
	}
	
	public static void account_summary_end(int id_) 
	{
		if (!sync.is_ok(id_)) return;

		sync.end();
	}

	public static String get_account_ib_last(String account_ib_, boolean decrypt_) 
	{
		String account_ib = account_ib_;
		
		if (strings.is_ok(account_ib))
		{
			if (decrypt_) account_ib = ib.basic.decrypt_account_ib(account_ib);	
		}
		else account_ib = strings.DEFAULT;

		return account_ib;
	} 
	
	private static boolean is_ok(int req_id_, String account_id_, String key_, String currency_) { return (sync.is_ok(req_id_, key_) && ib.basic.account_ib_is_ok(account_id_) && contracts.currency_is_ok(currency_)); }

	private static double get_funds(boolean ini_too_) 
	{ 
		double money = sync.get_funds();
		
		basic.update_money(money);
		if (ini_too_) basic.update_money_ini(money);
		
		return money;
	}
}