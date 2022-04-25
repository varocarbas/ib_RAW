package accessory_ib;

import java.util.HashMap;

import accessory.data;
import accessory.db_field;
import accessory.numbers;
import accessory.parent_ini_db;

class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();
	
	public _ini_db() { }
	
	public static void populate() { _instance.populate_all(); }
	
	protected boolean populate_all_dbs()
	{
		String db = types.CONFIG_DB_IB;
		HashMap<String, Object> setup_vals = null;

		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_market(db, sources);
		
		boolean is_ok = populate_db(db, sources, setup_vals);
		
		return is_ok;
	}
	
	private HashMap<String, Object[]> add_source_market(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_MARKET_SOURCE;
		boolean default_fields = true;

		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_DB_IB_MARKET_FIELD_SYMBOL, new db_field(data.STRING, 50, 0));
		info.put(types.CONFIG_DB_IB_MARKET_FIELD_TIME, new db_field(data.STRING, accessory.dates.SIZE_TIME_SHORT, 0));
		info.put(types.CONFIG_DB_IB_MARKET_FIELD_VOLUME, new db_field(data.DECIMAL, 10, numbers.DEFAULT_DECIMALS));
		info.put(types.CONFIG_DB_IB_MARKET_FIELD_HALTED, new db_field(data.STRING, 50, 0));
		info.put(types.CONFIG_DB_IB_MARKET_FIELD_HALTED_TOT, new db_field(data.INT, 2, 0));
		
		String[] ids = 
		{
			types.CONFIG_DB_IB_MARKET_FIELD_PRICE, types.CONFIG_DB_IB_MARKET_FIELD_OPEN, 
			types.CONFIG_DB_IB_MARKET_FIELD_CLOSE, types.CONFIG_DB_IB_MARKET_FIELD_LOW, 
			types.CONFIG_DB_IB_MARKET_FIELD_HIGH, types.CONFIG_DB_IB_MARKET_FIELD_ASK, 
			types.CONFIG_DB_IB_MARKET_FIELD_BID, types.CONFIG_DB_IB_MARKET_FIELD_SIZE, 
			types.CONFIG_DB_IB_MARKET_FIELD_BID_SIZE
		};
		
		for (String id: ids) { info.put(id, get_default_decimal_field()); }

		return add_source(source, db_, info, default_fields, sources_);		
	}
	
	private static db_field get_default_decimal_field() { return new db_field(data.DECIMAL, 10, numbers.DEFAULT_DECIMALS); }
}