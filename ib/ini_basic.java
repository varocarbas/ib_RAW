package ib;

import accessory.db;
import accessory.strings;

public abstract class ini_basic 
{
	public static final String SOURCE = db_ib.basic.SOURCE;

	private static String _user = strings.DEFAULT;
	private static String _account_ib = strings.DEFAULT;
	
	public static String get_user() { return _user; }	

	public static void update_user_ini(String user_) { _user = user_; }	

	public static String get_account_ib() { return _account_ib; }
	
	public static String adapt_user_ini(String user_)
	{
		String user = user_;
		if (!strings.is_ok(user)) user = basic.DEFAULT_USER;
		
		return strings.truncate(user, db_ib.common.MAX_SIZE_USER);
	}
	
	public static void start(String account_ib_)
	{
		db.create_table(SOURCE, false);
		
		populate_account_ib(account_ib_);
	}
	
	private static void populate_account_ib(String account_ib_) 
	{ 
		String account_ib = (strings.is_ok(account_ib_) ? account_ib_ : get_account_ib_from_file());
		if (!strings.is_ok(account_ib)) account_ib = strings.DEFAULT;
		
		String current = get_account_ib(true);
	
		if (strings.is_ok(account_ib) && !account_ib.equals(current))
		{
			account_ib = encrypt_account_ib(account_ib);
			
			db_ib.basic.update_account_ib(account_ib);
		}
	
		_account_ib = get_account_ib(false);
	}

	private static String encrypt_account_ib(String plain_) { return basic.encrypt(basic.get_account_ib_id(), plain_); }

	private static String get_account_ib(boolean decrypt_) { return sync_basic.get_account_ib_last(db_ib.basic.get_account_ib(), decrypt_); } 

	private static String get_account_ib_from_file() { return basic.get_from_file(basic.get_account_ib_id()); }
}