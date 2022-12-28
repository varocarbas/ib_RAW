package db_ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.db_common;
import accessory.strings;
import ib.ini_basic;

public abstract class basic 
{
	public static final String SOURCE = common.SOURCE_BASIC;
	public static final String SOURCE_OLD = common.SOURCE_BASIC_OLD;
	
	public static final String USER = common.FIELD_USER;
	public static final String ACCOUNT_IB = common.FIELD_ACCOUNT_IB;
	public static final String MONEY = common.FIELD_MONEY;
	public static final String MONEY_INI = common.FIELD_MONEY_INI;
	public static final String CURRENCY = common.FIELD_CURRENCY;
	public static final String MONEY_FREE = common.FIELD_MONEY_FREE;
	
	public static final double WRONG_MONEY = ib.common.WRONG_MONEY;
	
	static String[] _fields = null;
	static String[] _cols = null;
	static HashMap<String, String> _fields_cols = null;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static void __truncate() { common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static void __start() { __truncate_others(true); }
	
	public static void __truncate_others(boolean only_if_not_active_)
	{
		execs.__truncate(only_if_not_active_);

		orders.__truncate(only_if_not_active_);
		
		remote.__truncate(only_if_not_active_);
		
		trades.__truncate(only_if_not_active_);		
	}
	
	public static boolean exists() { return common.exists(SOURCE, get_where_user()); }

	public static String get_user() { return common.get_string(SOURCE, USER, get_where_user()); }

	public static String get_account_ib() { return common.get_string(SOURCE, ACCOUNT_IB, get_where_user()); }

	public static double get_money() { return common.get_decimal(SOURCE, MONEY, get_where_user(), WRONG_MONEY); }

	public static double get_money_ini() { return common.get_decimal(SOURCE, MONEY_INI, get_where_user(), WRONG_MONEY); }

	public static double get_money_free() { return common.get_decimal(SOURCE, MONEY_FREE, get_where_user(), WRONG_MONEY); }

	public static HashMap<String, Double> get_money_and_free()
	{
		HashMap<String, Double> output = new HashMap<String, Double>();
		
		String[] fields = new String[] { MONEY, MONEY_FREE };
		
		HashMap<String, String> temp = common.get_vals(SOURCE, fields, get_where_user());
		if (!arrays.is_ok(temp)) return output;
		
		for (String field: fields) { output.put(field, Double.parseDouble(temp.get(common.get_field_col(SOURCE, field)))); }
		
		return output;
	}
	
	public static boolean update_user_ini(String val_) { return common.insert_update(SOURCE, USER, val_, get_where_user(val_)); }

	public static boolean update_user(String val_) { return (strings.is_ok(ini_basic.get_user()) ? update_user() : update(USER, val_)); }

	public static boolean update_user() { return update(USER, ini_basic.get_user()); }

	public static boolean update_account_ib() { return update(ACCOUNT_IB, ini_basic.get_account_ib()); }
	
	public static boolean update_account_ib(String val_) 
	{ 
		Object vals = null;

		if (!exists()) vals = common.add_to_vals(SOURCE, USER, common.adapt_string(ini_basic.get_user(), USER), vals);

		String val0 = ini_basic.get_account_ib(); 
		String val = (strings.is_ok(val0) ? val0 : val_);
		
		vals = common.add_to_vals(SOURCE, ACCOUNT_IB, val, vals);
		
		return update(vals);
	}

	public static boolean update_money_common(HashMap<String, Double> vals_) 
	{ 
		if (!arrays.is_ok(vals_)) return false;
	
		Object vals = null;
		
		for (Entry<String, Double> item: vals_.entrySet()) { vals = common.add_to_vals(SOURCE, item.getKey(), adapt_money(item.getValue()), vals); }

		return update(vals);
	}
	
	public static boolean update_money_common(String field_, double val_) { return update(field_, adapt_money(val_)); }
	
	public static boolean update_currency(String val_) { return update(CURRENCY, val_); }
	
	public static boolean update(Object vals_) { return common.insert_update(SOURCE, vals_, get_where_user()); }

	static void populate_fields() { _fields = new String[] { USER, ACCOUNT_IB, MONEY, MONEY_INI, CURRENCY, MONEY_FREE }; }
	
	private static boolean update(String field_, Object val_) { return update(common.add_to_vals(SOURCE, field_, val_, null)); }
	
	private static double adapt_money(double val_)
	{
		double val = common.adapt_number(val_, common.FIELD_MONEY);
		
		return (ib.common.money_is_ok(val) ? val : ib.common.WRONG_MONEY2);
	}
	
	private static String get_where_user() { return get_where_user(ini_basic.get_user()); }

	private static String get_where_user(String val_) { return common.get_where(SOURCE, USER, val_); }
}