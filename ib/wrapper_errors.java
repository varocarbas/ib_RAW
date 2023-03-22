package ib;

import java.util.HashMap;

import accessory._keys;
import accessory.arrays;
import accessory.misc;
import accessory.strings;
import accessory_ib.errors;

public abstract class wrapper_errors 
{
	private static volatile boolean _triggered = false;
	
	public static boolean triggered() 
	{ 
		boolean triggered = _triggered;
		
		if (triggered) reset();
		
		return triggered; 
	}
	
	public static void reset() { _triggered = false; }
	
	public static void manage(int id_, int code_, String message_) 
	{
		String message = (strings.is_ok(message_) ? message_ : strings.DEFAULT);
		
		if (external_ib.errors.is_warning(code_)) 
		{
			if (common_xsync.req_id_is_ok(id_))
			{
				String id = Integer.toString(id_);
				if (!message.contains(id)) message = errors.get_message_warning(message) + misc.SEPARATOR_CONTENT + "id: " + id;			
			}
			
			errors.manage_warning(message);
		}
		else 
		{
			if (external_ib.errors.is_ok(code_)) _triggered = true;
			
			HashMap<String, Object> info = get_error_info(id_, code_, message);
			
			errors.manage(errors.ERROR_GENERIC, errors.get_message_common((String)arrays.get_value(info, errors.MESSAGE)), info);			
		}	
	}
	
	private static HashMap<String, Object> get_error_info(int id_, int code_, String message_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();

		if (common_xsync.req_id_is_ok(id_)) info.put(_keys.ID, id_);
		info.put(errors.MESSAGE, message_);
		
		if (!external_ib.errors.is_ok(code_)) return info;
		
		info.put("code", code_);
		
		if (code_ == external_ib.errors.ERROR_200)
		{
			String symbol = async_data.get_symbol(id_);
			
			if (strings.is_ok(symbol)) 
			{
				common.delete_symbol(symbol);
				
				info.put("symbol", symbol);
			}
		}

		return info;
	}
}