package ib;

import accessory.numbers;

public class orders 
{
	private static final int MIN_ID = 1;
	private static final int MAX_ID = 2500;
	
	public static boolean is_order_id(int id_)
	{
		return numbers.is_ok(id_, MIN_ID, MAX_ID);
	}
}
