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
	
	public static final double WRONG_MONEY2 = ib.common.WRONG_MONEY2;
	
	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static void __start() 
	{
		execs.__truncate(true);
		
		orders.__truncate(true);
		
		remote.__truncate(true);
		
		trades.__truncate(true);
	}
	
	public static boolean exists() { return common.exists(SOURCE, get_where_user()); }

	public static String get_user() { return common.get_string(SOURCE, USER, get_where_user()); }

	public static String get_account_ib() { return common.get_string(SOURCE, ACCOUNT_IB, get_where_user()); }

	public static double get_money() { return common.get_decimal(SOURCE, MONEY, get_where_user()); }

	public static double get_money_ini() { return common.get_decimal(SOURCE, MONEY_INI, get_where_user()); }

	public static double get_money_free() { return common.get_decimal(SOURCE, MONEY_FREE, get_where_user()); }

	public static HashMap<String, Double> get_money_and_free() 
	{ 
		HashMap<String, Double> output = new HashMap<String, Double>();
		
		String[] fields = new String[] { MONEY, MONEY_FREE };
		
		HashMap<String, String> temp = common.get_vals(SOURCE, fields, get_where_user()); 
		if (!arrays.is_ok(temp)) return output;
	
		for (String field: fields) { output.put(field, Double.parseDouble(temp.get(field))); }
	
		return output;
	}

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

	public static boolean update_money_common(String field_, double val_) 
	{ 
		double money = adapt_money(val_);
		if (!ib.common.money_is_ok(val_)) money = WRONG_MONEY2;
		
		return update(field_, money); 
	}

	public static boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static boolean update(HashMap<String, Object> vals_) { return common.insert_update(SOURCE, vals_, get_where_user()); }

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