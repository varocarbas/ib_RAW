package ib;

import accessory.parent_static;
import accessory_ib.types;
import db_ib.basic;

public class sync_basic extends parent_static  
{
	public static String get_class_id() { return accessory.types.get_id(types.ID_SYNC_BASIC); }

	public static void start()
	{
		basic.update_conn_type(conn.get_conn_type());
		
		double money = get_funds();
		
		basic.update_money(money);
		basic.update_money_ini(money);
	}
	
	public static double get_funds() { return sync.get_funds(); } 
	
	public static boolean update_conn_type(String val_) { return basic.update_conn_type(val_); } 
}