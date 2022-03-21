package accessory_ib;

import accessory._defaults;
import accessory._keys;
import accessory._types;

public class ini 
{
	//Method expected to be called every time a non-ini, non-first abstract class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		_load(); //First classes.
		
		config_ini.load();
		db_ini.load();
	}
	
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void _load()
	{
		_defaults.load();
		_types.load();
		_keys.load();
	}
}