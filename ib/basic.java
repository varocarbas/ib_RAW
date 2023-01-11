package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.config;
import accessory.credentials;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._defaults;
import accessory_ib._types;
import external_ib.calls;
import external_ib.contracts;

public abstract class basic extends parent_static 
{
	public static final String DB_SOURCE = db_ib.basic.SOURCE;

	public static final String DB_USER = db_ib.basic.USER;
	public static final String DB_ACCOUNT_IB = db_ib.basic.ACCOUNT_IB;
	public static final String DB_MONEY = db_ib.basic.MONEY;
	public static final String DB_MONEY_INI = db_ib.basic.MONEY_INI;
	public static final String DB_CURRENCY = db_ib.basic.CURRENCY;
	public static final String DB_MONEY_FREE = db_ib.basic.MONEY_FREE;

	public static final String CONFIG_ID_MAIN = _types.CONFIG_BASIC_ID_MAIN;

	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	public static final double WRONG_MONEY2 = common.WRONG_MONEY2;
	
	public static final String DEFAULT_USER = _defaults.USER;
	
	private static final String ID_ACCOUNT_IB = "account_ib";
	
	public static boolean is_quick() { return db_ib.common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_ib.common.is_quick(DB_SOURCE, is_quick_); }

	public static void __start() { __start(trades.DEFAULT_SYNCED_WITH_EXECS); }
	
	public static void __start(boolean trades_synced_with_execs_) 
	{
		trades.synced_with_execs(trades_synced_with_execs_);
		
		__start_internal(); 
		
		db_ib.basic.__start();
	}
	
	public static String get_id_main() { return (String)config.get_basic(CONFIG_ID_MAIN); }

	public static boolean update_id_main(String id_) { return config.update_basic(CONFIG_ID_MAIN, id_); }
	
	public static String get_user() { return ini_basic.get_user(); }
	
	public static String get_account_ib() 
	{
		db_ib.basic.update_account_ib(); 
		
		return get_account_ib_last(ini_basic.get_account_ib(), true);
	} 

	public static String get_account_ib_id() { return ID_ACCOUNT_IB; }
	
	public static boolean account_ib_is_ok(String account_ib_) 
	{ 
		if (!conn.type_is_real()) return true;
		
		String account_ib = get_account_ib();
		
		return (!strings.is_ok(account_ib) || strings.are_equal(account_ib_, account_ib));
	}

	public static String get_encryption_id(String id_) 
	{
		String output = strings.DEFAULT;
		
		String temp = get_id_main();
		if (strings.is_ok(temp)) output = temp;
		
		if (strings.is_ok(id_)) 
		{
			if (!output.equals(strings.DEFAULT)) output += SEPARATOR;
			
			output += id_;
		}
		
		return output;
	}
	
	public static String encrypt(String id_, String plain_) { return encrypt_internal(get_encryption_id(id_), get_user(), plain_); }

	public static String decrypt_account_ib(String encrypted_) { return decrypt(get_account_ib_id(), encrypted_); }

	public static String decrypt(String id_, String encrypted_) { return credentials.decrypt_string(get_encryption_id(id_), get_user(), encrypted_); }

	public static boolean encrypt_account_ib_to_file(String plain_) { return encrypt_to_file(get_account_ib_id(), plain_); }

	public static boolean encrypt_to_file(String id_, String plain_) { return credentials.encrypt_string_to_file(get_encryption_id(id_), get_user(), plain_); }

	public static String get_from_file(String id_) { return credentials.get_string_from_file(get_encryption_id(id_), get_user(), true); }

	public static String get_encrypted_file_path(String id_) { return credentials.get_path(get_encryption_id(id_), get_user(), true); }

	public static double __get_money() 
	{
		double money = db_ib.basic.get_money();
		
		if (money == db_ib.basic.WRONG_MONEY)
		{
			__update_money();
			
			money = db_ib.basic.get_money();
		}

		if (money == db_ib.basic.WRONG_MONEY) money = WRONG_MONEY2;
		
		return money; 
	} 
	
	public static HashMap<String, Double> __get_money_ib()
	{
		HashMap<String, Double> money = sync_basic.__get_money();	
		if (!arrays.is_ok(money) || !money.containsKey(DB_MONEY) || !money.containsKey(DB_MONEY_FREE)) return null;
		
		update_money(money, false);
		
		return money;
	}

	public static double get_money() 
	{ 
		double output = db_ib.basic.get_money(); 
	
		return (output == db_ib.basic.WRONG_MONEY ? WRONG_MONEY2 : output);
	}	

	public static double get_money_free() 
	{ 
		double output = db_ib.basic.get_money_free(); 
	
		return (output == db_ib.basic.WRONG_MONEY ? WRONG_MONEY2 : output);
	}	

	public static double get_money_ini() 
	{ 
		double output = db_ib.basic.get_money_ini(); 
	
		return (output == db_ib.basic.WRONG_MONEY ? WRONG_MONEY2 : output);
	}	

	public static HashMap<String, Double> get_money_and_free() 
	{ 
		HashMap<String, Double> temp = db_ib.basic.get_money_and_free(); 
		if (!arrays.is_ok(temp)) return temp;
		
		HashMap<String, Double> output = new HashMap<String, Double>(); 
		
		for (Entry<String, Double> item: temp.entrySet())
		{
			double val = item.getValue();
			if (val < WRONG_MONEY2) val = WRONG_MONEY2;
			
			output.put(item.getKey(), val);
		}
		
		return output;
	}
	
	public static void update_money() { async_basic.get_funds(); }
	
	public static void __update_money() { __update_money(false); }

	public static String get_currency() 
	{
		String currency = contracts.get_currency();

		db_ib.basic.update_currency(currency); 
		
		return currency;
	}
	
	static boolean update_funds_is_ok(String account_id_, String key_, String value_, String currency_) { return (arrays.is_ok(calls.get_funds_fields(key_)) && strings.is_ok(value_) && ib.basic.account_ib_is_ok(account_id_) && contracts.currency_is_ok(currency_)); }

	static String get_account_ib_last(String account_ib_, boolean decrypt_) 
	{
		String account_ib = account_ib_;
		
		if (strings.is_ok(account_ib))
		{
			if (decrypt_) account_ib = decrypt_account_ib(account_ib);	
		}
		else account_ib = strings.DEFAULT;

		return account_ib;
	} 
		
	private static void __start_internal()
	{
		get_account_ib();
		
		get_currency();
		
		__update_money(true);
	}

	private static boolean __update_money(boolean ini_too_) { return update_money(sync_basic.__get_money(), ini_too_); }
	
	private static boolean update_money(HashMap<String, Double> money_, boolean ini_too_)
	{
		HashMap<String, Double> money = arrays.get_new_hashmap_xy(money_);
		if (!money.containsKey(DB_MONEY) || !money.containsKey(DB_MONEY_FREE)) return false;
		
		HashMap<String, Double> vals = new HashMap<String, Double>(money);
		if (ini_too_) vals.put(DB_MONEY_INI, money.get(DB_MONEY));

		return db_ib.basic.update_money_common(vals);
	}

	private static String encrypt_internal(String id_, String user_, String plain_) { return credentials.encrypt_string(id_, user_, plain_); }
}