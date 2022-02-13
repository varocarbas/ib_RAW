package accessory_ib;

import java.util.HashMap;

import accessory.keys;
import accessory.misc;
import accessory.strings;

public class errors
{	
	static { _ini.load(); }
	
	public static void manage(String type_, boolean exit_)
	{
		String type = types.check_error(type_, null);
		if (!strings.is_ok(type)) return;
		
		String message = get_message(type);
		if (!strings.is_ok(message)) return;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.MESSAGE, message);
		
		String key = accessory.types._CONFIG_LOGS_OUT_FILE;
		boolean cur_val = strings.to_boolean(accessory._config.get_logs(key));
		boolean new_val = (exit_ || !type.equals(types.ERROR_CONN_NONE));
		
		boolean changed = false;
		if (new_val != cur_val)
		{
			accessory._config.update_logs(key, new_val);
			changed = true;
		}
		
		accessory.errors.manage(info, exit_);
		
		if (changed) accessory._config.update_logs(key, cur_val);
	}
		
	private static String get_message(String type_)
	{
		String message = strings.DEFAULT;
		String separator = accessory.types.SEPARATOR;
		
		if (type_.contains(types.CONN + separator)) message = get_message_conn(type_);
		else if (type_.contains(types.SYNC + separator)) message = get_message_sync(type_);
		else if (type_.contains(types.ORDER + separator)) message = get_message_order(type_);
		
		if (!strings.is_ok(message)) return message;
		
		message = "IB" + misc.SEPARATOR_CONTENT + message;
		
		return message;
	}
	
	private static String get_message_conn(String type_)
	{
		String message = strings.DEFAULT;
		
		if (type_.equals(types.ERROR_CONN_NONE)) message = "Impossible to connect";
		else if (type_.equals(types.ERROR_CONN_ID)) message = "Wrong connection ID";
		else if (type_.equals(types.ERROR_CONN_TYPE)) message = "Wrong connection type";
		
		return message;
	}

	private static String get_message_sync(String type_)
	{
		String message = strings.DEFAULT;
		
		if (type_.equals(types.ERROR_SYNC_ID)) message = "Wrong sync ID";
		else if (type_.equals(types.ERROR_SYNC_ID2)) message = "Wrong sync ID2";
		else if (type_.equals(types.ERROR_SYNC_TIME)) message = "Sync call timed out";
		
		return message;	
	}

	private static String get_message_order(String type_)
	{
		String message = strings.DEFAULT;
		
		return message;	
	}
}