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
	public static final String MONEY_FREE = common.FIELD_MONEY_FREE;
	
	public static boolean exists() { return common.exists(SOURCE, USER, get_where_user()); }

	public static String get_user() { return common.get_string(SOURCE, USER, get_where_user()); }

	public static String get_account_ib() { return common.get_string(SOURCE, ACCOUNT_IB, get_where_user()); }

	public static boolean update_user_ini(String val_) { return common.insert_update(SOURCE, USER, val_, get_where_user(val_)); }

	public static boolean update_user(String val_) { return (strings.is_ok(ini_basic.get_user()) ? update_user() : update(USER, val_)); }

	public static boolean update_user() { return update(USER, ini_basic.get_user()); }

	public static boolean update_account_ib() { return update(ACCOUNT_IB, ini_basic.get_account_ib()); }
	
	public static boolean update_account_ib(String val_) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();

		if (!exists()) vals.put(USER, common.adapt_string(ini_basic.get_user(), USER));
		
		String val0 = ini_basic.get_account_ib(); 
		String val = (strings.is_ok(val0) ? val0 : val_);
		
		vals.put(ACCOUNT_IB, val);
		
		return update(vals);
	}
	
	public static boolean update_money_ini(double val_) { return update(MONEY_INI, adapt_money(val_)); }
	
	public static boolean update_money(double val_) { return update(MONEY, adapt_money(val_)); }
	
	public static boolean update_money_free(double val_) { return update(MONEY_FREE, adapt_money(val_)); }

	public static boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static boolean update(HashMap<String, Object> vals_) { return common.insert_update(SOURCE, adapt_vals(vals_), get_where_user()); }
	
	private static HashMap<String, Object> adapt_vals(HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = arrays.get_new_hashmap_xy(vals_);
		
		String target = MONEY;
		String[] fields = new String[] { MONEY, MONEY_INI, MONEY_FREE };
		
		for (String field: fields) 
		{ 
			if (!vals.containsKey(field)) continue;
			
			double val = (double)vals.get(field);
			val = common.adapt_number(val, target);
			
			vals.put(field, val);
		}
		
		return vals;
	}

	private static double adapt_money(double val_) { return common.adapt_number(val_, common.FIELD_MONEY); }
	
	private static boolean update(String field_, Object val_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(field_, val_);

		return update(vals);
	}

	private static String get_where_user() { return get_where_user(ini_basic.get_user()); }

	private static String get_where_user(String val_) { return common.get_where(SOURCE, USER, val_, false); }
}