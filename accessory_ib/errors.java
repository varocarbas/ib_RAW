package accessory_ib;

import java.util.HashMap;

import accessory._keys;
import accessory.misc;
import accessory.strings;

public class errors
{	
	public static void manage(String type_)
	{
		String type = check(type_);
		if (!strings.is_ok(type)) return;

		HashMap<String, String> info = new HashMap<String, String>();
		info.put(_keys.TYPE, type);
		
		String message = get_message(type);
		if (strings.is_ok(message)) info.put(_keys.MESSAGE, message);
		
		accessory.errors.manage(info);
	}

	public static String check(String type_) { return accessory.types.check_type(type_, types.ERROR_IB); }
	
	private static String get_message(String type_)
	{
		String message = strings.DEFAULT;

		if (is_conn(type_)) message = get_message_conn(type_);
		else if (is_order(type_)) message = get_message_order(type_);
		else if (is_sync(type_)) message = get_message_sync(type_);
		else if (is_async(type_)) message = get_message_async(type_);

		if (!strings.is_ok(message)) return message;

		message = "IB" + misc.SEPARATOR_CONTENT + message;

		return message;
	}

	private static boolean is_conn(String type_) { return strings.is_ok(accessory.types.check_type(type_, types.CONN)); }

	private static boolean is_order(String type_) { return strings.is_ok(accessory.types.check_type(type_, types.ORDER)); }

	private static boolean is_sync(String type_) { return strings.is_ok(accessory.types.check_type(type_, types.SYNC)); }

	private static boolean is_async(String type_) { return strings.is_ok(accessory.types.check_type(type_, types.ASYNC)); }
	
	private static String get_message_conn(String type_)
	{
		String message = strings.DEFAULT;

		if (type_.equals(types.ERROR_IB_CONN_NONE)) message = "Impossible to connect";
		else if (type_.equals(types.ERROR_IB_CONN_ID)) message = "Wrong connection ID";
		else if (type_.equals(types.ERROR_IB_CONN_TYPE)) message = "Wrong connection type";

		return message;
	}

	private static String get_message_order(String type_)
	{
		String message = strings.DEFAULT;

		return message;	
	}

	private static String get_message_sync(String type_)
	{
		String message = strings.DEFAULT;

		if (type_.equals(types.ERROR_IB_SYNC_ID)) message = "Wrong sync ID";
		else if (type_.equals(types.ERROR_IB_SYNC_ID2)) message = "Wrong sync ID2";
		else if (type_.equals(types.ERROR_IB_SYNC_TIME)) message = "Sync call timed out";

		return message;	
	}

	private static String get_message_async(String type_)
	{
		String message = strings.DEFAULT;

		if (type_.equals(types.ERROR_IB_ASYNC_TIME)) message = "Async call timed out";

		return message;	
	}
}