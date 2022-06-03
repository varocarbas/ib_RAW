package accessory_ib;

import accessory.strings;

public class logs 
{
	public static void update_screen(int id_, String symbol_, String message_) 
	{
		if (!strings.are_ok(new String[] { symbol_, message_ })) return;
		
		String message = symbol_ + " (" + id_ + ") " + message_;

		update_screen(message); 
	}
	
	public static void update_screen(String symbol_, boolean added_) 
	{
		if (!strings.is_ok(symbol_)) return;
		
		String message = symbol_ + " " + (added_ ? "added" : "removed");
		
		update_screen(message); 
	}

	public static void update_screen(String message_) { accessory.logs.update_screen(message_, true); }
}