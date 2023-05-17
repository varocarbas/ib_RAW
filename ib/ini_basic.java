package ib;

import accessory.db;
import accessory.db_common;
import accessory.strings;

public abstract class ini_basic 
{
	public static final String SOURCE = db_ib.basic.SOURCE;

	private static String _user = strings.DEFAULT;
	private static String _account_ib = strings.DEFAULT;
	private static String _account_type_ib = strings.DEFAULT;
	
	public static String get_user() { return _user; }	

	public static String get_account_ib() { return _account_ib; }

	public static String get_account_type_ib() { return (strings.is_ok(_account_type_ib) ? _account_type_ib : basic.get_type_from_conn(conn.get_conn_type())); }
	
	public static void start(String user_, String account_ib_, String account_type_ib_, boolean ignore_ib_info_)
	{
		db.create_table(SOURCE, false);

		basic.ignore_ib_info(ignore_ib_info_);
		
		populate_user(user_);

		populate_account_ib(account_ib_, account_type_ib_, user_, ignore_ib_info_);
	}
	
	private static String populate_user(String user_) 
	{ 
		String user = get_user_prelimary(user_);

		if (!strings.is_ok(user)) user = db_common.adapt_string(db_ib.basic.get_user(), db_ib.common.MAX_SIZE_USER);

		db_ib.basic.update_user_ini(user);

		_user = user;
		
		return user;
	}
	
	private static String get_user_prelimary(String user_) { return db_common.adapt_string((strings.is_ok(user_) ? user_ : basic.DEFAULT_USER), db_ib.common.MAX_SIZE_USER); }
	
	public static void update_account_ib(String account_type_ib_, String user_) { get_update_account_ib(account_type_ib_, user_); }
	
	private static void populate_account_ib(String account_ib_, String account_type_ib_, String user_, boolean ignore_ib_info_) 
	{ 
		_account_ib = (ignore_ib_info_ ? strings.DEFAULT : get_update_account_ib(account_type_ib_, user_));
		
		_account_type_ib = account_type_ib_;
	}

	private static String encrypt_account_ib(String plain_, String type_) { return basic.encrypt(basic.get_account_ib_id(type_), plain_); }

	private static String get_update_account_ib(String type_, String user_) { return get_update_account_ib(null, type_, user_); }
	
	private static String get_update_account_ib(String account_ib_, String type_, String user_) 
	{ 
		String output = (strings.is_ok(account_ib_) ? account_ib_ : get_account_ib_from_file(type_, user_));

		if (strings.is_ok(output) && !strings.are_equal(output, get_account_ib(type_, true)))
		{
			output = encrypt_account_ib(output, type_);

			db_ib.basic.update_account_ib(output); 
		}

		return get_account_ib(type_, false);
	} 

	private static String get_account_ib(String type_, boolean decrypt_) { return basic.get_account_ib_last(db_ib.basic.get_account_ib(), type_, decrypt_); } 

	private static String get_account_ib_from_file(String account_type_, String user_) { return basic.get_from_file(basic.get_account_ib_id(account_type_), user_); }
}