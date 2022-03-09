package accessory_ib;

public class ini 
{
	//Method expected to be called every time a non-ini static class is loaded.
	//It has to include all the load() methods of all the ini classes.
	public static void load() 
	{
		_config_ini.load();
		db_ini.load();
	}
}