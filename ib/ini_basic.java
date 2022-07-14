package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.generic;
import accessory.numbers;
import accessory.strings;
import accessory_ib._ini;

public abstract class ini_basic 
{
	private static final String ACCOUNT_IB = _ini.ACCOUNT_IB;
	private static final String CONN_TYPE_IB = _ini.CONN_TYPE_IB;
	private static final String CONN_ID_IB = _ini.CONN_ID_IB;
	
	private static String _account_ib = strings.DEFAULT;
	private static int _conn_id = conn.WRONG_ID;
	private static String _conn_type = strings.DEFAULT;
	
	public static String get_account_ib() { return _account_ib; }	
	
	public static int get_conn_id() { return _conn_id; }	
	
	public static String get_conn_type() { return _conn_type; }
	
	public static void update_conn_type(String conn_type_)
	{
		if (conn.type_is_ok(conn_type_)) _conn_type = conn_type_;
	}
	
	public static void start(HashMap<String, Object> info_ib_)
	{
		Object account_ib = arrays.get_value(info_ib_, ACCOUNT_IB);
		populate_account_ib((generic.is_string(account_ib) ? (String)account_ib : strings.DEFAULT));

		Object conn_type = arrays.get_value(info_ib_, CONN_TYPE_IB);
		populate_conn_type((generic.is_string(conn_type) ? (String)conn_type : strings.DEFAULT)); 

		Object conn_id = arrays.get_value(info_ib_, CONN_ID_IB);
		populate_conn_id((generic.is_number(conn_id) ? numbers.to_int(numbers.to_number(conn_id)) : conn.WRONG_ID));
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
	
	private static void populate_conn_id(int conn_id_) 
	{ 
		int conn_id = conn_id_;
		
		if (!conn.id_is_ok(conn_id)) conn_id = db_ib.basic.get_conn_id();
		if (!conn.id_is_ok(conn_id)) conn_id = conn.DEFAULT_ID;
		
		db_ib.basic.update_conn_id(conn_id);

		_conn_id = conn_id;
	}

	private static void populate_conn_type(String conn_type_) 
	{ 
		String conn_type = conn_type_;
		
		if (!conn.type_is_ok(conn_type)) conn_type = db_ib.basic.get_conn_type();
		if (!conn.type_is_ok(conn_type)) conn_type = conn.DEFAULT_TYPE;
		
		db_ib.basic.update_conn_type(conn_type);

		_conn_type = conn_type;
	}

	private static String encrypt_account_ib(String plain_) { return basic.encrypt(basic.get_account_ib_id(), plain_); }

	private static String get_account_ib(boolean decrypt_) { return sync_basic.get_account_ib_last(db_ib.basic.get_account_ib(), decrypt_); } 

	private static String get_account_ib_from_file() { return basic.get_from_file(basic.get_account_ib_id()); }
}