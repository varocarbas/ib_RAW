package accessory_ib;

public class ini 
{
	public static void load() 
	{
		load_first();
		
		config_ini.load();
		db_ini.populate();
	}
	
	private static void load_first()
	{
		_defaults.load();
	}
}