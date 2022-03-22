package accessory_ib;

import accessory._defaults;

public class ini 
{
	//Method expected to be called every time a non-ini, non-first abstract class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		load_first();
		
		config_ini.load();
		db_ini.load();
	}
	
	
	//Loading all the first classes, the ones whose names start with "_".
	private static void load_first()
	{
		_defaults.load();
	}
}