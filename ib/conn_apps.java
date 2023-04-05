package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.credentials;
import accessory.strings;
import accessory_ib.os;

abstract class conn_apps 
{
	public static final String _ID = "conn_apps";
	
	private static final String ID_REAL = "real";
	private static final String ID_PAPER = "paper";
	
	public static boolean encrypt_credentials(String username_, String password_) { return encrypt_credentials(conn.type_is_real(), username_, password_); }

	public static boolean encrypt_credentials(boolean is_real_, String username_, String password_) { return encrypt_credentials(is_real_, basic.get_user(), username_, password_); }
	
	public static boolean encrypt_credentials(boolean is_real_, String user_, String username_, String password_)  
	{ 
		String id = get_encryption_id(is_real_);

		return (strings.is_ok(id) ? credentials.encrypt_username_password_file(id, user_, username_, password_) : false);  
	}
	
	public static boolean run() { return run(basic.get_user()); }
	
	public static boolean run(String user_credentials_) { return run(conn.type_is_gateway(), conn.type_is_real(), user_credentials_); }
	
	public static boolean run(boolean is_gateway_, boolean is_real_, String user_credentials_)
	{
		boolean output = false;
		
		HashMap<String, String> credentials = get_credentials(is_real_, user_credentials_);
		if (!arrays.is_ok(credentials)) return output;
	
		int window_id = os.execute_ib(is_gateway_);
		if (window_id <= accessory.os.WRONG_WINDOW_ID || !os.click_ib_real_paper(window_id, is_gateway_, is_real_)) return output;

		return (arrays.is_ok(credentials) ? os.fill_form_ib(window_id, credentials, is_gateway_) : false);
	}
	
	private static HashMap<String, String> get_credentials(boolean is_real_, String user_) { return credentials.get_username_password_file(get_encryption_id(is_real_), user_, true); }
	
	private static String get_encryption_id(boolean is_real_) { return basic.get_encryption_id(new String[] { _ID, (is_real_ ? ID_REAL : ID_PAPER) }); }
}