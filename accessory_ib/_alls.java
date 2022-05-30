package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_first;
import ib.async_market;
import ib.sync;

public class _alls extends parent_ini_first
{
	private static _alls _instance = new _alls(); 
	
	public _alls() { }
	public static void populate() { _instance.populate_internal_common(); }
	
	public static HashMap<String, String> SYNC_GET_OUTS = null;
	public static HashMap<Integer, String> ASYNC_MARKET_PRICES = null;
	public static HashMap<Integer, String> ASYNC_MARKET_SIZES = null;
	public static HashMap<Integer, String> ASYNC_MARKET_GENERICS = null;
	
	public void populate_internal() 
	{ 
		if (_populated) return;
		
		accessory._alls.populate_types(types.populate_all_types());
		
		SYNC_GET_OUTS = sync.populate_all_get_outs();
		ASYNC_MARKET_PRICES = async_market.populate_all_prices();
		ASYNC_MARKET_SIZES = async_market.populate_all_sizes();
		ASYNC_MARKET_GENERICS = async_market.populate_all_generics();
		
		_populated = true;
	}
}