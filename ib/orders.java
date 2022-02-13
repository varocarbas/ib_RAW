package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.numbers;
import accessory_ib._ini;

public class orders 
{	
	public static final int MIN_ID = 1;
	public static final int MAX_ID = 2500;

	public static volatile int next_id = MIN_ID - 1; //Initiliased via wrapper.nextValidId().
	
	private static HashMap<Integer, String> symbols = new HashMap<Integer, String>();
	
	static { _ini.load(); }
		
	public static boolean is_order_id(int id_)
	{
		return numbers.is_ok(id_, MIN_ID, MAX_ID);
	}
	
	public static boolean order_is_open(int id_)
	{
		return arrays.value_exists(get_open_orders(), id_);
	}
	
	public static String get_symbol(int id_)
	{
		return arrays.get_value(symbols, id_);
	}
	
	private static Integer[] get_open_orders()
	{
		return (Integer[])sync.get(accessory_ib.types.SYNC_IDS);
	}
}