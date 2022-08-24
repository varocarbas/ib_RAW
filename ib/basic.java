package ib;

import java.util.HashMap;

import accessory.config;
import accessory.credentials;
import accessory.misc;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._defaults;
import accessory_ib.types;
import external_ib.contracts;

public abstract class basic extends parent_static 
{
	public static final String CONFIG_ID_MAIN = types.CONFIG_BASIC_ID_MAIN;

	public static final String MONEY = db_ib.basic.MONEY;
	public static final String MONEY_FREE = db_ib.basic.MONEY_FREE;
	public static final String MONEY_INI = db_ib.basic.MONEY_INI;
	
	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	public static final double WRONG_MONEY2 = common.WRONG_MONEY2;
	
	public static final String DEFAULT_USER = _defaults.USER;
	
	private static final String ID_ACCOUNT_IB = "account_ib";

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
		
		if (money <= db_ib.basic.WRONG_MONEY2)
		{
			__update_money();
			
			money = db_ib.basic.get_money();
		}
		
		return money; 
	} 

	public static double get_money_free() { return db_ib.basic.get_money_free(); }	

	public static void __update_money() { __update_money(false); }

	public static String get_currency() 
	{
		String currency = contracts.get_currency();
		
		db_ib.basic.update_currency(currency); 
		
		return currency;
	} 

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
	
	private static boolean __update_money(boolean ini_too_)
	{
		HashMap<String, Double> ib = sync_basic._get_money(true);

		if (ib == null || !ib.containsKey(MONEY) || !ib.containsKey(MONEY_FREE)) return false;
		
		HashMap<String, Double> vals = new HashMap<String, Double>(ib);
		if (ini_too_) vals.put(MONEY_INI, ib.get(MONEY));

		return db_ib.basic.update_money_common(vals);
	}

	private static String encrypt_internal(String id_, String user_, String plain_) { return credentials.encrypt_string(id_, user_, plain_); }
}