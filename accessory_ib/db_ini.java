package accessory_ib;

import java.util.HashMap;

import accessory.data;
import accessory.db_field;
import accessory.errors;
import accessory.strings;

class db_ini 
{
	public static final String ERROR_SETUPS = types.ERROR_IB_INI_DB_SETUPS;
	public static final String ERROR_SOURCES = types.ERROR_IB_INI_DB_SOURCES;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{
		if (_populated) return;
		
		String error = strings.DEFAULT;
		if (!populate_all_setups()) error = ERROR_SETUPS;
		if (error.equals(strings.DEFAULT) && !populate_all_sources()) error = ERROR_SOURCES;

		_populated = true;
		if (error.equals(strings.DEFAULT)) return;

		errors._exit = true;
		errors.manage(error);
	}
	
	private static boolean populate_all_setups()
	{
		return true;
	}
	
	private static boolean populate_all_sources()
	{	
		boolean is_ok = true;

		String main = types.CONFIG_IB_DB; 
		String source = types.CONFIG_IB_DB_MARKET_SOURCE;
		String table = "ib_market";		
		HashMap<String, Object[]> fields = accessory.db_ini.get_fields(get_fields_market(), types.CONFIG_IB_DB_MARKET_FIELD);

		is_ok = accessory.db_ini.populate_source(main, source, table, fields);

		return is_ok;
	}
	
	private static HashMap<String, db_field> get_fields_market()
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>();
	
		info.put(types.CONFIG_IB_DB_MARKET_FIELD_SYMBOL, new db_field(data.STRING, 50, 0));
		info.put(types.CONFIG_IB_DB_MARKET_FIELD_TIME, new db_field(data.STRING, accessory.dates.SIZE_TIME_SHORT, 0));
		info.put(types.CONFIG_IB_DB_MARKET_FIELD_VOLUME, new db_field(data.DECIMAL, 10, accessory._defaults.NUMBERS_SIZE_DECIMALS));
		info.put(types.CONFIG_IB_DB_MARKET_FIELD_HALTED, new db_field(data.STRING, 50, 0));
		info.put(types.CONFIG_IB_DB_MARKET_FIELD_HALTED_TOT, new db_field(data.INT, 2, 0));
		
		String[] ids = 
		{
			types.CONFIG_IB_DB_MARKET_FIELD_PRICE, types.CONFIG_IB_DB_MARKET_FIELD_OPEN, 
			types.CONFIG_IB_DB_MARKET_FIELD_CLOSE, types.CONFIG_IB_DB_MARKET_FIELD_LOW, 
			types.CONFIG_IB_DB_MARKET_FIELD_HIGH, types.CONFIG_IB_DB_MARKET_FIELD_ASK, 
			types.CONFIG_IB_DB_MARKET_FIELD_BID, types.CONFIG_IB_DB_MARKET_FIELD_SIZE, 
			types.CONFIG_IB_DB_MARKET_FIELD_BID_SIZE
		};
		
		for (String id: ids)
		{
			info.put(id, get_default_decimal_field());
		}
		
		return info;
	}
	
	private static db_field get_default_decimal_field()
	{
		return new db_field(data.DECIMAL, 10, accessory._defaults.NUMBERS_SIZE_DECIMALS);
	}
}