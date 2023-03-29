package accessory_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.logs;
import accessory.misc;
import accessory.strings;
import ib.conn;
import ib.sync;

public abstract class errors
{
	public static final String MESSAGE = accessory.errors.MESSAGE;
	
	public static final String ERROR_GENERIC = _types.ERROR_IB_GENERIC;
	
	public static final String DEFAULT_WARNING = "WARNING";
	public static final String DEFAULT_MESSAGE = accessory.errors.DEFAULT_MESSAGE;	

	public static boolean is_warning(int code_, String message_) { return (external_ib.errors.is_warning(code_) || strings.contains("warning", message_, true)); }
	
	public static void manage(String type_, String message_) { manage(type_, get_message_common(message_), null); }
	
	public static void manage(HashMap<String, Object> info_) { manage(null, null, info_); }

	public static void manage(String type_, HashMap<String, Object> info_) { manage(type_, null, info_); }

	public static void manage(String type_) { manage(type_, null, null); }

	public static void manage(String type_, String message_, HashMap<String, Object> info_)
	{	
		String message = message_;
		if (!strings.is_ok(message)) message = get_message(type_);
	
		HashMap<String, Object> info = arrays.get_new_hashmap_xy(info_);
		if (strings.is_ok(message)) info.put(MESSAGE, message);
		
		accessory.errors.manage(type_, null, info);
	}

	public static void manage_warning(String message_) { logs.update_screen(get_message_warning(message_)); }
	
	public static String check(String type_) { return accessory._types.check_type(type_, _types.ERROR_IB); }
	
	public static String get_message_common(String message_) 
	{ 
		String message = message_;
		
		if (!strings.is_ok(message)) message = DEFAULT_MESSAGE;
		message = "IB" + misc.SEPARATOR_CONTENT + message;
		
		return message; 
	}

	public static String get_message_warning(String message_)
	{
		String message = message_;
		if (!strings.is_ok(message_)) message = DEFAULT_WARNING;
		
		return message;
	}
	
	private static String get_message(String type_)
	{
		String message = strings.DEFAULT;

		if (is_conn(type_)) message = conn.get_error_message(type_);
		else if (is_sync(type_)) message = sync.get_error_message(type_);

		return get_message_common(message);
	}

	private static boolean is_conn(String type_) { return strings.is_ok(conn.check_error(type_)); }

	private static boolean is_sync(String type_) { return strings.is_ok(sync.check_error(type_)); }
}