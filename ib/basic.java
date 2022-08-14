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

	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	public static final double WRONG_MONEY2 = common.WRONG_MONEY2;
	
	public static final String DEFAULT_USER = _defaults.USER;
	
	private static final String ID_ACCOUNT_IB = "account_ib";

	public static void __start() { __start(trades.DEFAULT_SYNCED_WITH_EXECS); }
	
	public static void __start(boolean trades_synced_with_execs_) 
	{
		trades.synced_with_execs(trades_synced_with_execs_);
		
		start_internal(); 
		
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

	public static double get_money() 
	{
		double money = db_ib.basic.get_money();
		
		if (!money_db_is_ok(money)) money = db_ib.basic.get_money();
		
		return money; 
	} 

	public static double get_money_free() { return db_ib.basic.get_money_free(); }	

	public static String get_currency() 
	{
		String currency = contracts.get_currency();
		
		db_ib.basic.update_currency(currency); 
		
		return currency;
	} 
	
	public static void increase_money(double increase_) { update_increase_money(increase_, true); }
	
	public static void update_money(double money_) { update_increase_money(money_, false); }

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
	
	private static void update_increase_money(double val_, boolean is_increase_)
	{
		double val = val_;
		if ((is_increase_ && val_ == 0) || (!is_increase_ && val <= WRONG_MONEY2) || (val <= ib.common.WRONG_MONEY)) return;
		
		HashMap<String, Double> old = db_ib.basic.get_money_and_free();
		double money_old = old.get(db_ib.basic.MONEY);
		
		double money = val; 
		double increase = val;
		
		if (is_increase_) money = money_old + val;
		else increase = money - money_old;
		
		if (!money_db_is_ok(money_old) || (money == money_old)) return;
			
		double free_old = old.get(db_ib.basic.MONEY_FREE); 
		double free = free_old + increase;
		
		update_money_common(db_ib.basic.MONEY, money, false);
		update_money_common(db_ib.basic.MONEY_FREE, free, false);
	}
	
	private static boolean money_db_is_ok(double money_)
	{
		boolean is_ok = true;
		
		if (money_ <= WRONG_MONEY2) 
		{
			is_ok = false;
			update_money_all();
		}
	
		return is_ok;
	}

	private static void start_internal()
	{
		get_account_ib();
		
		get_currency();
		
		update_money_all();
	}

	private static void update_money_all()
	{		
		double money = update_money_common(db_ib.basic.MONEY, WRONG_MONEY2, true);
		if (money == sync_basic.WRONG_MONEY) return;
		
		update_money_common(db_ib.basic.MONEY_INI, money, false);
		update_money_common(db_ib.basic.MONEY_FREE, money, false);
	}

	private static double update_money_common(String field_, double money_, boolean check_ib_)
	{
		double money = money_;
		boolean update = true;
		
		if (money <= WRONG_MONEY2)
		{
			update = false;
			
			if (check_ib_) 
			{
				money = sync_basic.get_money();
				update = (money != sync_basic.WRONG_MONEY);
			}
		}
	
		if (update) db_ib.basic.update_money_common(field_, money);
		
		return money;
	}

	private static String encrypt_internal(String id_, String user_, String plain_) { return credentials.encrypt_string(id_, user_, plain_); }
}