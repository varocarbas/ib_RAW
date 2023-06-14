package ib;

import accessory.dates;

public abstract class ini_market 
{
	private static boolean _update_tz = true;
	private static int _offset = dates.DEFAULT_OFFSET;
	
	public static boolean update_tz() { return _update_tz; }
	
	public static void start() { populate_offset(); }
	
	public static int get_offset() { return _offset; }
	
	private static void populate_offset() { _offset = dates.get_offset(market.TZ, _update_tz); }
}