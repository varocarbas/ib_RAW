package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.generic;
import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory.time;
import accessory_ib.defaults;
import accessory_ib.types;

public class sync 
{
	public static String type = strings.DEFAULT;
	public static String subtype = strings.DEFAULT;
	public static int id = numbers.DEFAULT_INT;
	
	private static final int MAX_SECS_RETRIEVE = 10;
	
	private static volatile boolean retrieved = false;
	private static volatile double decimal_out = numbers.DEFAULT_DEC;
	private static volatile ArrayList<Integer> ints_out = new ArrayList<Integer>();
	private static volatile HashMap<String, String> misc_out = new HashMap<String, String>();
	
	public static Object get(String subtype_)
	{
		return get(subtype_, defaults.SYNC_ID);
	}
	
	public static Object get(String subtype_, int order_id_)
	{
		Object output = generic.DEFAULT;
		if (!retrieve(subtype_, order_id_)) return output; 
		
		if (type.equals(types.SYNC_DECIMAL)) output = decimal_out;
		else if (type.equals(types.SYNC_INTS)) output = new ArrayList<Integer>(ints_out);
		else if (type.equals(types.SYNC_MISC)) output = new HashMap<String, String>(misc_out);
		
		return output;
	}
	
	public static void retrieved(boolean retrieved_)
	{
		retrieved = retrieved_;
	}
	
	public static boolean update(String val_)
	{
		return update(strings.to_number_decimal(val_));
	}
	
	public static boolean update(double val_)
	{
		if (!type.equals(types.SYNC_DECIMAL)) return false;
		
		decimal_out = val_;
		
		return true;
	}

	public static boolean update(int val_)
	{
		if (!type.equals(types.SYNC_INTS)) return false;
		
		ints_out.add(val_);
		
		return true;
	}
	
	public static boolean update(String key_, String val_)
	{
		if (!type.equals(types.SYNC_MISC) || !strings.is_ok(key_)) return false;
		
		misc_out.put(key_, val_);
		
		return true;
	}
	
	private static boolean retrieve(String subtype_, int id_)
	{
		if (start_retrieval(subtype_, id_)) return false;
		
		if (subtype.equals(types.SYNC_DECIMAL_FUNDS))
		{	
			//accountSummary, accountSummaryEnd
			conn.client.reqAccountSummary(id, "All", "AvailableFunds"); 
		}
		else if (subtype.equals(types.SYNC_INTS_IDS))
		{	
			//openOrder, openOrderEnd, orderStatus
			conn.client.reqAllOpenOrders(); 
		}
		
		return retrieving();
	}
	
	private static boolean start_retrieval(String subtype_, int id_)
	{
		String temp = types.check_sync(subtype_, strings.DEFAULT, false);
		if (!strings.is_ok(temp)) return false;

		String temp2 = types.check_sync(subtype, strings.DEFAULT, true);
		if (!strings.is_ok(temp2)) return false;
		
		id = id_;
		subtype = temp;
		type = temp2;
		retrieved = false;
		
		if (type.equals(types.SYNC_DECIMAL)) decimal_out = numbers.DEFAULT_DEC;
		else if (type.equals(types.SYNC_INTS)) ints_out = new ArrayList<Integer>();
		else if (type.equals(types.SYNC_MISC)) misc_out = new HashMap<String, String>();
		
		return true;
	}
	
	private static boolean retrieving()
	{
		long start = time.get_elapsed(0);
		
		while (true)
		{
			long elapsed = time.get_elapsed(start);
			if (elapsed >= MAX_SECS_RETRIEVE) return false;
			if (retrieved) break; 

			misc.pause_min();
		}
		
		return true;
	}
}