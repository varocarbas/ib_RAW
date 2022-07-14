package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.db;
import accessory.strings;
import ib.conn;
import ib.ini_basic;

public abstract class basic 
{
	public static final String SOURCE = common.SOURCE_BASIC;
	
	public static final String USER = common.FIELD_USER;
	public static final String CONN_TYPE = common.FIELD_CONN_TYPE;
	public static final String ACCOUNT_IB = common.FIELD_ACCOUNT_IB;
	public static final String MONEY = common.FIELD_MONEY;
	public static final String MONEY_INI = common.FIELD_MONEY_INI;
	public static final String CURRENCY = common.FIELD_CURRENCY;
	public static final String CONN_ID = common.FIELD_CONN_ID;
	
	public static String get_account_ib() { return common.get_string(SOURCE, ACCOUNT_IB, common.get_where_user(SOURCE)); }

	public static int get_conn_id() { return common.get_int(SOURCE, CONN_ID, common.get_where_user(SOURCE), false); }

	public static String get_conn_type() { return common.get_string(SOURCE, CONN_TYPE, common.get_where_user(SOURCE)); }

	public static boolean update_account_ib(String val_) { return (strings.is_ok(ini_basic.get_account_ib()) ? update_account_ib() : update(ACCOUNT_IB, val_)); }

	public static boolean update_account_ib() { return update(ACCOUNT_IB, ini_basic.get_account_ib()); }

	public static boolean update_conn_id(int val_) { return (conn.id_is_ok(ini_basic.get_conn_id()) ? update_conn_id() : update(CONN_ID, val_)); }

	public static boolean update_conn_id() { return update(CONN_ID, ini_basic.get_conn_id()); }

	public static boolean update_conn_type() { return update(CONN_TYPE, ini_basic.get_conn_type()); }
	
	public static boolean update_conn_type(String val_) { return (conn.type_is_ok(ini_basic.get_conn_type()) ? update_conn_type() : update(CONN_TYPE, val_)); }
	
	public static boolean update_money_ini(double val_) { return update(MONEY_INI, val_); }
	
	public static boolean update_money(double val_) { return update(MONEY, val_); }
	
	public static boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static boolean update(HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = adapt_vals(vals_);
		
		common.insert_update(SOURCE, vals, common.get_where_user(SOURCE));
	
		return db.is_ok();
	}
	
	private static HashMap<String, Object> adapt_vals(HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		
		String target = MONEY;
		String[] fields = new String[] { MONEY, MONEY_INI };
		
		for (String field: fields) 
		{ 
			if (!vals.containsKey(field)) continue;
			
			double val = (double)vals.get(field);
			val = ib.common.adapt_val(val, target);
			
			vals.put(field, val);
		}
		
		return vals;
	}
	
	private static boolean update(String field_, Object val_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(vals);
	}
}