package ib;

import db_ib.basic;
import external_ib.contracts;

public abstract class sync_basic 
{
	public static void start()
	{
		get_conn_type();
		
		get_account_ib();
		
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
	
	public static boolean is_ok(int req_id_, String account_id_, String key_, String currency_) { return (sync.is_ok(req_id_, key_) && ib.basic.account_ib_is_ok(account_id_) && contracts.currency_is_ok(currency_)); }
	
	public static boolean is_ok(int req_id_) { return sync.is_ok(req_id_); }

	public static String get_account_ib() 
	{
		String account_ib = ib.basic.get_account_ib_config();
		
		basic.update_account_ib(account_ib); 
		
		account_ib = ib.basic.decrypt(ib.basic.get_account_ib_id(), account_ib);

		return account_ib;
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