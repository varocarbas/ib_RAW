package accessory_ib;

import java.util.HashMap;

import accessory._keys;
import accessory.logs;
import accessory.misc;
import accessory.strings;
import ib.conn;
import ib.sync;

public class errors
{	
	public static final String DEFAULT_WARNING = _defaults.ERRORS_WARNING;
	
	public static void manage(HashMap<String, String> items_) { accessory.errors.manage(get_info(null, get_message(items_))); }
	
	public static void manage(String type_)
	{
		String type = check(type_);
		if (!strings.is_ok(type)) return;

		accessory.errors.manage(get_info(type, get_message(type)));
	}

	public static void manage_warning(String message_) 
	{ 
		String message = message_;
		if (!strings.is_ok(message_)) message = DEFAULT_WARNING;
		
		logs.update_screen(message);
	}

	public static String check(String type_) { return accessory.types.check_type(type_, types.ERROR_IB); }
	
	private static HashMap<String, String> get_info(String type_, String message_) 
	{ 
		HashMap<String, String> info = new HashMap<String, String>();
		
		if (strings.is_ok(type_)) info.put(_keys.TYPE, type_);
		if (strings.is_ok(message_)) info.put(_keys.MESSAGE, message_);
		
		return info;
	}

	private static String get_message(HashMap<String, String> info_) { return get_message_common(accessory.errors.to_string(info_)); }
	
	private static String get_message(String type_)
	{
		String message = strings.DEFAULT;

		if (is_conn(type_)) message = conn.get_error_message(type_);
		else if (is_sync(type_)) message = sync.get_error_message(type_);

		return get_message_common(message);
	}

	private static String get_message_common(String message_) 
	{ 
		String message = message_;
		
		if (!strings.is_ok(message)) message = accessory.errors.DEFAULT_MESSAGE;
		message = "IB" + misc.SEPARATOR_CONTENT + message;
		
		return message; 
	}
	
	private static boolean is_conn(String type_) { return strings.is_ok(conn.check_error(type_)); }

	private static boolean is_sync(String type_) { return strings.is_ok(sync.check_error(type_)); }
}