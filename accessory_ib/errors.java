package accessory_ib;

import java.util.HashMap;

import accessory._keys;
import accessory.arrays;
import accessory.logs;
import accessory.misc;
import accessory.strings;
import ib.async_data;
import ib.common_xsync;
import ib.conn;
import ib.orders;
import ib.sync;

public abstract class errors
{
	public static final String MESSAGE = _keys.MESSAGE;
	
	public static final String ERROR_GENERIC = types.ERROR_IB_GENERIC;
	
	public static final String DEFAULT_WARNING = "WARNING";

	public static void __wrapper_error(int id_, int code_, String message_)
	{
		String message = (strings.is_ok(message_) ? message_ : strings.DEFAULT);
		
		if (common_xsync.req_id_is_ok(id_))
		{
			String id = Integer.toString(id_);
			if (!message.contains(id)) message += misc.SEPARATOR_CONTENT + "id: " + id;			
		}
		
		if (is_warning(code_) || __treat_as_warning(id_, code_)) manage_warning(message);
		else manage_internal(ERROR_GENERIC, __wrapper_error_info(id_, code_, message_));
	}	

	public static boolean is_warning(int code_) { return external_ib.errors.is_warning(code_); }
	
	public static void manage(String type_, String message_) { manage_internal(type_, get_message_common(message_), null); }
	
	public static void manage(HashMap<String, Object> info_) { manage_internal(null, null, info_); }

	public static void manage(String type_, HashMap<String, Object> info_) { manage_internal(type_, null, info_); }

	public static void manage(String type_) { manage_internal(type_, null, null); }

	public static void manage_warning(String message_) 
	{ 
		String message = message_;
		if (!strings.is_ok(message_)) message = DEFAULT_WARNING;
		
		logs.update_screen(message);
	}

	public static String check(String type_) { return accessory.types.check_type(type_, types.ERROR_IB); }

	private static HashMap<String, Object> __wrapper_error_info(int id_, int code_, String message_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();

		info.put(_keys.ID, id_);
		info.put("code", code_);
		info.put(MESSAGE, message_);
		
		if (code_ == external_ib.errors.ERROR_200)
		{
			String symbol = async_data.get_symbol(id_);
			if (strings.is_ok(symbol)) info.put("symbol", symbol);
		}

		return info;
	}
		
	private static boolean __treat_as_warning(int id_, int code_)
	{
		return 
		(
			(code_ == external_ib.errors.ERROR_202 && orders.__is_cancelling(id_)) ||
			code_ == external_ib.errors.ERROR_300
		);
	}
	
	private static void manage_internal(String type_, HashMap<String, Object> info_) { manage_internal(type_, get_message_common((String)arrays.get_value(info_, MESSAGE)), info_); }
	
	private static void manage_internal(String type_, String message_, HashMap<String, Object> info_)
	{	
		String message = message_;
		if (!strings.is_ok(message)) message = get_message(type_);
	
		HashMap<String, Object> info = arrays.get_new_hashmap_xy(info_);
		if (strings.is_ok(message)) info.put(_keys.MESSAGE, message);
		
		accessory.errors.manage(type_, null, info);
	}
	
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