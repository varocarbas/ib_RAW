package accessory_ib;

import java.util.HashMap;

import accessory.parent_keys;

public class _keys extends parent_keys
{
	private static _keys _instance = new _keys(); 

	public _keys() { }
	public static void populate() { _instance.populate_internal(); }
	
	protected HashMap<String, String> get_startup_roots() 
	{ 
		HashMap<String, String> output = new HashMap<String, String>();
		
		output.put(_types.ORDERS_PLACE, _types.ORDERS_PLACE);
		output.put(_types.ORDERS_UPDATE, _types.ORDERS_UPDATE);
		output.put(_types.ORDERS_CANCEL, _types.ORDERS);
		output.put(_types.ORDERS_STATUS, _types.ORDERS_STATUS);
		output.put(_types.REMOTE_STATUS, _types.REMOTE_STATUS);
		output.put(_types.REMOTE_STATUS2, _types.REMOTE_STATUS2);
		output.put(_types.APPS_STATUS, _types.APPS_STATUS);
	
		return output;
	}

	protected HashMap<String, HashMap<String, String>> get_startup_merged_roots() 
	{ 
		HashMap<String, HashMap<String, String>> output = new HashMap<String, HashMap<String, String>>();
		
		HashMap<String, String> item = new HashMap<String, String>();
		
		item.put(_types.ORDERS_PLACE, _types.ORDERS_PLACE);
		item.put(_types.ORDERS_UPDATE, _types.ORDERS_UPDATE);
		item.put(_types.ORDERS_CANCEL, _types.ORDERS);
		
		output.put(_types.ORDERS, item);
	
		item = new HashMap<String, String>();
		
		item.put(_types.REMOTE_ORDERS_PLACE, _types.REMOTE_ORDERS_ROOT);
		item.put(_types.REMOTE_ORDERS_UPDATE, _types.REMOTE_ORDERS_ROOT);
		item.put(_types.REMOTE_ORDERS_CANCEL, _types.REMOTE_ORDERS_ROOT);
		
		output.put(_types.REMOTE_ORDERS, item);
		
		return output;
	}
	
	protected HashMap<String, HashMap<String, String>> get_startup_merged_types() { return null; }
}