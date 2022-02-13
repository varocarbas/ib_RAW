package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.generic;
import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory.time;
import accessory_ib._ini;
import accessory_ib.defaults;
import accessory_ib.errors;
import accessory_ib.types;

public class sync 
{
	public static volatile boolean retrieving = false;
	public static volatile boolean retrieved = false;
	
	public static String type = strings.DEFAULT;
	public static String type2 = strings.DEFAULT;
	public static int id = defaults.SYNC_ID;
	
	private static final int MAX_SECS_RETRIEVE = 10;
	private static HashMap<String, String> data_map = new HashMap<String, String>();
	
	private static volatile double decimal_out = numbers.DEFAULT_DEC;
	private static volatile int int_out = numbers.DEFAULT_INT;
	private static volatile ArrayList<Integer> ints_out = new ArrayList<Integer>();
	private static volatile HashMap<String, String> misc_out = new HashMap<String, String>();
	
	static 
	{ 
		_ini.load(); 
		_ini();
	}
	
	public static Object get(String type_)
	{
		return get(type_, defaults.SYNC_ID);
	}
	
	public static Object get(String type_, int order_id_)
	{
		get_ini();
		retrieve(type_, order_id_); 
		
		return get_out();
	}
	
	private static void get_ini()
	{
		get_ini_out(true);
	}
	
	private static Object get_out()
	{
		return get_ini_out(false);
	}
	
	private static Object get_ini_out(boolean ini_)
	{
		Object output = generic.DEFAULT;
		
		if (type2.equals(types.SYNC_DATA_INT)) 
		{
			if (ini_) int_out = (types.is_order(type) ? defaults.ORDER_ID : numbers.DEFAULT_INT);
			else output = int_out;
		}
		else if (type2.equals(types.SYNC_DATA_DECIMAL)) 
		{
			if (ini_) decimal_out = numbers.DEFAULT_DEC;
			else output = decimal_out;
		}
		else if (type2.equals(types.SYNC_DATA_INTS)) 
		{
			if (ini_) ints_out = new ArrayList<Integer>();
			else output = arrays.to_array(ints_out);
		}
		else if (type2.equals(types.SYNC_DATA_MISC)) 
		{
			if (ini_) misc_out = new HashMap<String, String>();
			else output = new HashMap<String, String>(misc_out);
		}

		return output;
	}
	
	public static boolean update(String val_)
	{
		return update(strings.to_number_decimal(val_));
	}
	
	public static boolean update(double val_)
	{
		if (!type2.equals(types.SYNC_DATA_DECIMAL)) return false;
		
		decimal_out = val_;
		
		return true;
	}

	public static boolean update(int val_)
	{
		boolean is_ok = true;
		
		if (type2.equals(types.SYNC_DATA_INTS)) ints_out.add(val_);
		else if (type2.equals(types.SYNC_DATA_INT)) int_out = val_;
		else is_ok = false;
		
		return is_ok;
	}
	
	public static boolean update(String key_, String val_)
	{
		if (!type2.equals(types.SYNC_DATA_MISC) || !strings.is_ok(key_)) return false;
		
		misc_out.put(key_, val_);
		
		return true;
	}

	private static void _ini()
	{
		if (arrays.is_ok(data_map)) return;
		
		data_map.put(types.SYNC_ID, types.SYNC_DATA_INT);
		data_map.put(types.SYNC_FUNDS, types.SYNC_DATA_DECIMAL);
		data_map.put(types.SYNC_IDS, types.SYNC_DATA_INTS);
	}
	
	private static boolean retrieve(String type_, int id_)
	{
		if (!retrieve_is_ok(type_, id_)) return false;

		retrieving = true;
		retrieved = false;
		
		if (type.equals(types.SYNC_FUNDS))
		{	
			//accountSummary, accountSummaryEnd
			conn.client.reqAccountSummary(id, "All", "AvailableFunds"); 
		}
		else if (type.equals(types.SYNC_IDS))
		{	
			//openOrder, openOrderEnd, orderStatus
			conn.client.reqAllOpenOrders(); 
		}
		else if (type.equals(types.SYNC_ID))
		{	
			//nextValidId
			conn.client.reqIds(-1); 
		}
		else return false;
		
		return retrieving();
	}
	
	private static boolean retrieve_is_ok(String type_, int id_)
	{
		String temp = types.check_sync(type_, false);
		if (!strings.is_ok(temp)) 
		{
			accessory_ib.errors.manage(types.ERROR_SYNC_ID, false);
			
			return false;
		}

		String temp2 = arrays.get_value(data_map, temp);

		if (!strings.is_ok(types.check_sync(temp2, true))) 
		{
			accessory_ib.errors.manage(types.ERROR_SYNC_ID2, false);
			
			return false;
		}

		id = id_;
		type = temp;
		type2 = temp2;
		retrieved = false;

		return true;
	}
	
	private static boolean retrieving()
	{
		boolean is_ok = true;
		
		long start = time.get_elapsed(0);
		
		while (true)
		{
			if (retrieved) break;
			
			long elapsed = time.get_elapsed(start);
			if (elapsed >= MAX_SECS_RETRIEVE) 
			{
				errors.manage(types.ERROR_SYNC_TIME, false);
				is_ok = false;
				
				break;
			}
			 
			misc.pause_min();
		}
		
		retrieving = false;
		
		return is_ok;
	}
}