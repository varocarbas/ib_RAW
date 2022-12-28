package accessory_ib;

import accessory.parent_first;
import ib.ini_common;

class _starts extends parent_first 
{
	private static _starts _instance = new _starts(); 

	public _starts() { }
	public static void populate() { _instance.populate_internal_common(); }

	protected void populate_internal() 
	{
		ini_common.start();
		
		db_ib.common.start(); 
	}
}