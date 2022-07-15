package db_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import ib.ini_basic;

public abstract class basic 
{
	public static final String SOURCE = common.SOURCE_BASIC;
	
	public static final String USER = common.FIELD_USER;
	public static final String ACCOUNT_IB = common.FIELD_ACCOUNT_IB;
	public static final String MONEY = common.FIELD_MONEY;
	public static final String MONEY_INI = common.FIELD_MONEY_INI;
	public static final String CURRENCY = common.FIELD_CURRENCY;
	
	public static String get_account_ib() { return common.get_string(SOURCE, ACCOUNT_IB, common.get_where_user(SOURCE)); }

	public static boolean update_account_ib(String val_) { return (strings.is_ok(ini_basic.get_account_ib()) ? update_account_ib() : update(ACCOUNT_IB, val_)); }

	public static boolean update_account_ib() { return update(ACCOUNT_IB, ini_basic.get_account_ib()); }
	
	public static boolean update_money_ini(double val_) { return update(MONEY_INI, val_); }
	
	public static boolean update_money(double val_) { return update(MONEY, val_); }
	
	public static boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static boolean update(HashMap<String, Object> vals_) { return common.insert_update(SOURCE, adapt_vals(vals_), common.get_where_user(SOURCE)); }
	
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