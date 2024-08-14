package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory._keys;
import accessory.misc;
import accessory.strings;
import accessory_ib.errors;

public abstract class wrapper_errors 
{
	public static final boolean DEFAULT_LOG_ERRORS = true;
	
	private static volatile boolean _triggered = false;
	private static volatile boolean _log_errors = DEFAULT_LOG_ERRORS;
	
	private static ArrayList<Integer> _ignore_errors = new ArrayList<Integer>();
	
	public static void log_errors(boolean log_errors_) { _log_errors = log_errors_; }
	
	public static boolean log_errors() { return _log_errors; }
	
	public static boolean triggered() 
	{ 
		boolean triggered = _triggered;
		
		if (triggered) reset();
		
		return triggered; 
	}
	
	public static void reset() { _triggered = false; }
	
	public static void __manage(int id_, int code_, String message_, String message2_) 
	{
		if (_ignore_errors.contains(code_)) return;
		
		String message = (strings.is_ok(message_) ? message_ : strings.DEFAULT);
		
		if (strings.is_ok(message2_))
		{
			if (strings.is_ok(message)) message += misc.SEPARATOR_CONTENT;
			
			message += message2_;
		}
		
		String symbol = async_data_quicker.__get_symbol(id_, false);
		if (!strings.is_ok(symbol)) symbol = null;
		
		if (external_ib.errors.is_warning(code_, message_)) 
		{				
			if (common_xsync.req_id_is_ok(id_))
			{
				String id = Integer.toString(id_);
				
				if (!message.contains(id)) message = errors.get_message_warning(message) + misc.SEPARATOR_CONTENT + "id: " + id;			
			}
		
			if (symbol != null) message += misc.SEPARATOR_CONTENT + "symbol: " + symbol;
			
			errors.manage_warning(message);
		}
		else 
		{
			if (external_ib.errors.is_ok(code_)) _triggered = true;
	
			message = errors.get_message_common(message);
			
			HashMap<String, Object> info = get_error_info(id_, symbol, code_, message);
			
			if (_log_errors) errors.manage(errors.ERROR_GENERIC, message, info);
			else errors.manage_warning(message);
		}	
	}
	
	public static void ignore_errors(int ib_code_)
	{
		if (!_ignore_errors.contains(ib_code_)) _ignore_errors.add(ib_code_);
	}
	
	private static HashMap<String, Object> get_error_info(int id_, String symbol_, int code_, String message_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();

		if (common_xsync.req_id_is_ok(id_)) info.put(_keys.ID, id_);
		info.put(errors.MESSAGE, message_);
		
		if (!external_ib.errors.is_ok(code_)) return info;
		
		info.put("code", code_);
		
		if (symbol_ != null) info.put("symbol", symbol_);

		return info;
	}
}