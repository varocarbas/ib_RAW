package accessory_ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.db;
import accessory.db_field;

class db_ini 
{
	public static void load() 
	{
		load_aliases_types();
		load_sources();
	}

	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(_types.CONFIG_IB_DB_MARKET_SOURCE, _types.CONFIG_IB_DB);
		
		load_sources_all(source_mains);
	}
	
	//Method including the aliases and types more closely related to the DB setup.
	private static void load_aliases_types()
	{
		load_aliases();
		load_types();
	}
	
	private static void load_types()
	{
		load_types_config();
	}
	
	private static void load_types_config()
	{
		load_config_sources();
	}

	private static void load_aliases()
	{
		accessory._types.update_aliases(_keys.SYMBOL, _types.CONFIG_IB_DB_COMMON_FIELD_SYMBOL);
		accessory._types.update_aliases(_keys.PRICE, _types.CONFIG_IB_DB_COMMON_FIELD_PRICE);

		accessory._types.update_aliases(_keys.TIME, _types.CONFIG_IB_DB_MARKET_FIELD_TIME);
		accessory._types.update_aliases(_keys.OPEN, _types.CONFIG_IB_DB_MARKET_FIELD_OPEN);
		accessory._types.update_aliases(_keys.CLOSE, _types.CONFIG_IB_DB_MARKET_FIELD_CLOSE);
		accessory._types.update_aliases(_keys.LOW, _types.CONFIG_IB_DB_MARKET_FIELD_LOW);
		accessory._types.update_aliases(_keys.HIGH, _types.CONFIG_IB_DB_MARKET_FIELD_HIGH);
		accessory._types.update_aliases(_keys.VOLUME, _types.CONFIG_IB_DB_MARKET_FIELD_VOLUME);		
		accessory._types.update_aliases(_keys.ASK, _types.CONFIG_IB_DB_MARKET_FIELD_ASK);
		accessory._types.update_aliases(_keys.ASK_SIZE, _types.CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE);
		accessory._types.update_aliases(_keys.BID, _types.CONFIG_IB_DB_MARKET_FIELD_BID);
		accessory._types.update_aliases(_keys.BID_SIZE, _types.CONFIG_IB_DB_MARKET_FIELD_BID_SIZE);
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { _types.CONFIG_IB_DB  };
		
		for (String main: mains)
		{
			accessory.db_ini.load_config_sources_default_fields(main);	
			
			if (main.equals(_types.CONFIG_IB_DB)) load_config_sources_db(main);
		}
	}
	
	private static void load_config_sources_db(String main_)
	{
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_COMMON_FIELD_SYMBOL, _defaults.DB_COMMON_FIELD_SYMBOL);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_COMMON_FIELD_PRICE, _defaults.DB_COMMON_FIELD_PRICE);

		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_SOURCE, _defaults.DB_MARKET_SOURCE);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_TIME, _defaults.DB_MARKET_FIELD_TIME);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_OPEN, _defaults.DB_MARKET_FIELD_OPEN);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_CLOSE, _defaults.DB_MARKET_FIELD_CLOSE);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_LOW, _defaults.DB_MARKET_FIELD_LOW);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_HIGH, _defaults.DB_MARKET_FIELD_HIGH);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_VOLUME, _defaults.DB_MARKET_FIELD_VOLUME);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_ASK, _defaults.DB_MARKET_FIELD_ASK);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, _defaults.DB_MARKET_FIELD_ASK_SIZE);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_BID, _defaults.DB_MARKET_FIELD_BID);
		accessory.config.update_ini(main_, _types.CONFIG_IB_DB_MARKET_FIELD_BID_SIZE, _defaults.DB_MARKET_FIELD_BID_SIZE);
	
		accessory.db_ini.load_config_sources_default_fields(main_);
	}
	
	private static void load_sources_all(HashMap<String, String> source_mains_)
	{		
		for (Entry<String, String> item: source_mains_.entrySet())
		{
			String source = item.getKey();
			String main = item.getValue();
		
			load_sources_source(source);			
			db.add_source_main(source, main);
		}
	}
	
	private static void load_sources_source(String source_)
	{
		if (source_.equals(_types.CONFIG_IB_DB_MARKET_SOURCE)) load_sources_source_market(source_);
	}
	
	private static void load_sources_source_market(String source_)
	{
		if (accessory.db.source_is_ok(source_)) return;

		HashMap<String, db_field> fields = accessory.db.get_default_fields();
	
		fields.put(_types.CONFIG_IB_DB_MARKET_FIELD_SYMBOL, new db_field(accessory._types.DATA_STRING, 50, 0));
		fields.put(_types.CONFIG_IB_DB_MARKET_FIELD_VOLUME, new db_field(accessory._types.DATA_DECIMAL, 10, accessory._defaults.SIZE_DECIMALS));
		
		String[] ids = 
		{
			_types.CONFIG_IB_DB_MARKET_FIELD_OPEN, _types.CONFIG_IB_DB_MARKET_FIELD_CLOSE, 
			_types.CONFIG_IB_DB_MARKET_FIELD_LOW, _types.CONFIG_IB_DB_MARKET_FIELD_HIGH, 
			_types.CONFIG_IB_DB_MARKET_FIELD_ASK, _types.CONFIG_IB_DB_MARKET_FIELD_BID,
			_types.CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, _types.CONFIG_IB_DB_MARKET_FIELD_BID_SIZE
		};
		
		for (String id: ids)
		{
			fields.put(id, get_default_decimal_field());
		}
								
		accessory.db.add_source(source_, fields);
	}
	
	private static db_field get_default_decimal_field()
	{
		return new db_field(accessory._types.DATA_DECIMAL, 10, accessory._defaults.SIZE_DECIMALS);
	}
}