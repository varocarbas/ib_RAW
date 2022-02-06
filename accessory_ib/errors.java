package accessory_ib;

import java.util.HashMap;

import accessory.keys;
import accessory.strings;

public class errors
{	
	public static void manage(String type_, boolean exit_)
	{
		String type = types.check_error(type_, null);
		if (!strings.is_ok(type)) return;
		
		String message = get_message(type);
		if (!strings.is_ok(message)) return;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(keys.MESSAGE, message);
		
		boolean to_file = true;
		if (!exit_ && type.equals(types.ERROR_CONN_NONE)) to_file = false;
			
		accessory.errors.manage(info, to_file, exit_);
	}
		
	private static String get_message(String type_)
	{
		String message = strings.DEFAULT;
		
		if (type_.equals(types.ERROR_CONN_NONE)) message = "Impossible to connect to IB";
		else if (type_.equals(types.ERROR_CONN_ID)) message = "Wrong connection ID for IB";
		else if (type_.equals(types.ERROR_CONN_TYPE)) message = "Wrong connection type for IB";
		
		return message;
	}
}
