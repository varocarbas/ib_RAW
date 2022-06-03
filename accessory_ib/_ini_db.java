package accessory_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.data;
import accessory.dates;
import accessory.db_field;
import accessory.numbers;
import accessory.parent_ini_db;
import accessory.strings;

public class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();
	
	public _ini_db() { }
	
	public static void populate(String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { _instance.populate_all(dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_); }

	public static void populate(HashMap<String, Object> dbs_setup_) { _instance.populate_all(dbs_setup_); }

	@SuppressWarnings("unchecked")
	protected boolean populate_all_dbs(HashMap<String, Object> dbs_setup_)
	{
		HashMap<String, Object> setup_vals = (HashMap<String, Object>)arrays.get_new(dbs_setup_);
		
		String db = _defaults.DB;
		
		String name = (String)arrays.get_value(setup_vals, accessory.types.CONFIG_DB_NAME);		
		if (!strings.is_ok(name)) name = _defaults.DB_NAME;
		
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_market(db, sources);
		
		boolean is_ok = populate_db(db, name, sources, setup_vals);
		
		return is_ok;
	}
	
	private HashMap<String, Object[]> add_source_market(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_MARKET_SOURCE;

		boolean default_fields = true;
		String table = "ib_market";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_DB_FIELD_SYMBOL, new db_field(data.STRING_SMALL, 5, 0, null, new String[] { db_field.KEY_UNIQUE }));
		info.put(types.CONFIG_DB_FIELD_TIME, new db_field(data.STRING_SMALL, accessory.dates.get_length(dates.FORMAT_TIME_SHORT)));
		info.put(types.CONFIG_DB_FIELD_HALTED, new db_field(data.TINYINT));
		info.put(types.CONFIG_DB_FIELD_HALTED_TOT, new db_field(data.TINYINT));
		info.put(types.CONFIG_DB_FIELD_ENABLED, new db_field(data.BOOLEAN, 0, 0, true, null));

		String[] ids = 
		{
			types.CONFIG_DB_FIELD_PRICE, types.CONFIG_DB_FIELD_OPEN, types.CONFIG_DB_FIELD_CLOSE, 
			types.CONFIG_DB_FIELD_LOW, types.CONFIG_DB_FIELD_HIGH, types.CONFIG_DB_FIELD_ASK, 
			types.CONFIG_DB_FIELD_BID, types.CONFIG_DB_FIELD_SIZE, types.CONFIG_DB_FIELD_BID_SIZE, 
			types.CONFIG_DB_FIELD_ASK_SIZE, types.CONFIG_DB_FIELD_VOLUME
		};
		
		for (String id: ids) { info.put(id, get_default_decimal_field()); }

		return add_source(source, table, db_, info, default_fields, sources_);		
	}
	
	private static db_field get_default_decimal_field() { return new db_field(data.DECIMAL, 10, numbers.DEFAULT_DECIMALS); }
}