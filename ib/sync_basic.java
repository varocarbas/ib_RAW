package ib;

import accessory.parent_static;
import db_ib.basic;
import external_ib.contracts;

public abstract class sync_basic extends parent_static  
{
	public static void start()
	{
		get_conn_type();
		
		get_account_id();
		
		get_currency();
		
		get_funds(true);
	}
	
	public static double get_funds() { return get_funds(false); } 
	
	public static String get_conn_type() 
	{
		String conn_type = conn.get_conn_type();
		
		basic.update_conn_type(conn_type); 
		
		return conn_type;
	} 
	
	public static boolean is_ok(int req_id_, String account_id_, String key_, String currency_) { return (sync.is_ok(req_id_, key_) && conn.account_id_is_ok(account_id_) && contracts.currency_is_ok(currency_)); }
	
	public static boolean is_ok(int req_id_) { return sync.is_ok(req_id_); }

	public static String get_account_id() 
	{
		String account_id = conn.get_account_id();
		
		basic.update_account_id(account_id); 
		
		return account_id;
	} 
	
	public static String get_currency() 
	{
		String currency = contracts.get_currency();
		
		basic.update_currency(currency); 
		
		return currency;
	} 
	
	private static double get_funds(boolean ini_too_) 
	{ 
		double money = sync.get_funds();
		
		basic.update_money(money);
		if (ini_too_) basic.update_money_ini(money);
		
		return money;
	}
}