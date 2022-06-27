package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.db;

public class basic 
{
	public static final String SOURCE = common.SOURCE_BASIC;
	
	public static final String USER = common.FIELD_USER;
	public static final String CONN_TYPE = common.FIELD_CONN_TYPE;
	public static final String ACCOUNT_ID = common.FIELD_ACCOUNT_ID;
	public static final String MONEY = common.FIELD_MONEY;
	public static final String MONEY_INI = common.FIELD_MONEY_INI;
	public static final String CURRENCY = common.FIELD_CURRENCY;
	
	public static final boolean update_conn_type(String val_) { return update(CONN_TYPE, val_); }

	public static final boolean update_account_id(String val_) { return update(ACCOUNT_ID, val_); }

	public static final boolean update_money_ini(double val_) { return update(MONEY_INI, val_); }
	
	public static final boolean update_money(double val_) { return update(MONEY, val_); }
	
	public static final boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static final boolean update(HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		if (!vals.containsKey(USER)) vals.put(USER, ib.common.USER);
		
		db.insert_update(SOURCE, vals, common.get_where_user(SOURCE));
	
		return db.is_ok();
	}
	
	private static final boolean update(String field_, Object val_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);
		
		return update(vals);
	}
}