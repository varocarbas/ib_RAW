package ib;

import java.util.HashMap;

import accessory.arrays;
import db_ib.watchlist;

public abstract class async_watchlist 
{
	public static final String _ID = "async_watchlist";
	
	private static final int PRICE = async_data.PRICE;
	private static final int HALTED = async_data.HALTED;
	private static final int VOLUME = async_data.VOLUME;

	private static volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	private static boolean _enabled = false; 
	
	public static void enable() { _enabled = true; }

	public static void disable() { _enabled = false; }
	
	public static boolean __is_ok(int id_) { return (_enabled && arrays.__key_exists_async(_ids, id_)); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, watchlist.PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME, watchlist.VOLUME);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, watchlist.HALTED);
		
		return all;
	}
}