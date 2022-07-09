package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.db;

public abstract class basic 
{
	public static final String SOURCE = common.SOURCE_BASIC;
	
	public static final String USER = common.FIELD_USER;
	public static final String CONN_TYPE = common.FIELD_CONN_TYPE;
	public static final String ACCOUNT_IB = common.FIELD_ACCOUNT_IB;
	public static final String MONEY = common.FIELD_MONEY;
	public static final String MONEY_INI = common.FIELD_MONEY_INI;
	public static final String CURRENCY = common.FIELD_CURRENCY;
	
	public static boolean update_conn_type(String val_) { return update(CONN_TYPE, val_); }

	public static boolean update_account_ib(String val_) { return update(ACCOUNT_IB, val_); }

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