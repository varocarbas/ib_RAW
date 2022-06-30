package ib;

import java.util.HashMap;

import accessory.arrays;
import db_ib.trades;

public abstract class async_trades 
{
	public static final String _ID = "async_trades";
	
	private static final int PRICE = async_data.PRICE;
	private static final int HALTED = async_data.HALTED;

	private static volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	private static boolean _enabled = false; 
	
	public static void enable() { _enabled = true; }

	public static void disable() { _enabled = false; }
	
	public static boolean __is_ok(int id_) { return (_enabled && arrays.__key_exists_async(_ids, id_)); }
	
	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, trades.PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, trades.HALTED);
		
		return all;
	}
}