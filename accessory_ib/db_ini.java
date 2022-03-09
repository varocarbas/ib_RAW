package accessory_ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.data;
import accessory.db_field;
import accessory.numbers;
import accessory.size;

class db_ini 
{
	public static void load() 
	{
		load_sources();
		load_aliases_types();
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
		accessory.types.update_aliases(keys.SYMBOL, types._CONFIG_IB_DB_COMMON_FIELD_SYMBOL);
		accessory.types.update_aliases(keys.PRICE, types._CONFIG_IB_DB_COMMON_FIELD_PRICE);

		accessory.types.update_aliases(keys.TIME, types._CONFIG_IB_DB_MARKET_FIELD_TIME);
		accessory.types.update_aliases(keys.OPEN, types._CONFIG_IB_DB_MARKET_FIELD_OPEN);
		accessory.types.update_aliases(keys.CLOSE, types._CONFIG_IB_DB_MARKET_FIELD_CLOSE);
		accessory.types.update_aliases(keys.LOW, types._CONFIG_IB_DB_MARKET_FIELD_LOW);
		accessory.types.update_aliases(keys.HIGH, types._CONFIG_IB_DB_MARKET_FIELD_HIGH);
		accessory.types.update_aliases(keys.VOLUME, types._CONFIG_IB_DB_MARKET_FIELD_VOLUME);		
		accessory.types.update_aliases(keys.ASK, types._CONFIG_IB_DB_MARKET_FIELD_ASK);
		accessory.types.update_aliases(keys.ASK_SIZE, types._CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE);
		accessory.types.update_aliases(keys.BID, types._CONFIG_IB_DB_MARKET_FIELD_BID);
		accessory.types.update_aliases(keys.BID_SIZE, types._CONFIG_IB_DB_MARKET_FIELD_BID_SIZE);
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { types._CONFIG_IB_DB  };
		
		for (String main: mains)
		{
			accessory.db_ini.load_config_sources_default_fields(main);	
			
			if (main.equals(types._CONFIG_IB_DB)) load_config_sources_db(main);
		}
	}
	
	private static void load_config_sources_db(String main_)
	{
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_COMMON_FIELD_SYMBOL, defaults.DB_COMMON_FIELD_SYMBOL);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_COMMON_FIELD_PRICE, defaults.DB_COMMON_FIELD_PRICE);

		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_SOURCE, defaults.DB_MARKET_SOURCE);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_TIME, defaults.DB_MARKET_FIELD_TIME);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_OPEN, defaults.DB_MARKET_FIELD_OPEN);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_CLOSE, defaults.DB_MARKET_FIELD_CLOSE);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_LOW, defaults.DB_MARKET_FIELD_LOW);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_HIGH, defaults.DB_MARKET_FIELD_HIGH);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_VOLUME, defaults.DB_MARKET_FIELD_VOLUME);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_ASK, defaults.DB_MARKET_FIELD_ASK);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, defaults.DB_MARKET_FIELD_ASK_SIZE);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_BID, defaults.DB_MARKET_FIELD_BID);
		accessory._config.update_ini(main_, types._CONFIG_IB_DB_MARKET_FIELD_BID_SIZE, defaults.DB_MARKET_FIELD_BID_SIZE);
	
		accessory.db_ini.load_config_sources_default_fields(main_);
	}
	
	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(types._CONFIG_IB_DB_MARKET_SOURCE, types._CONFIG_IB_DB);
		
		for (Entry<String, String> item: source_mains.entrySet())
		{
			String source = item.getKey();
			String main = item.getValue();
		
			if (source.equals(types._CONFIG_IB_DB_MARKET_SOURCE)) load_sources_market(source);
			
			accessory.db.add_source_main(source, main);
		}
	}
	
	private static void load_sources_market(String source_)
	{
		if (accessory.db.source_is_ok(source_)) return;

		HashMap<String, db_field> fields = accessory.db.get_default_fields();
	
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_SYMBOL, new db_field(new data(accessory.types.DATA_STRING, new size(0.0, 50.0, 0)), null));
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_VOLUME, new db_field(new data(accessory.types.DATA_DECIMAL, new size(0.0, numbers.MAX_DEC, accessory.defaults.SIZE_DECIMALS)), null));
		
		String[] ids = 
		{
			types._CONFIG_IB_DB_MARKET_FIELD_OPEN, types._CONFIG_IB_DB_MARKET_FIELD_CLOSE, 
			types._CONFIG_IB_DB_MARKET_FIELD_LOW, types._CONFIG_IB_DB_MARKET_FIELD_HIGH, 
			types._CONFIG_IB_DB_MARKET_FIELD_ASK, types._CONFIG_IB_DB_MARKET_FIELD_BID,
			types._CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, types._CONFIG_IB_DB_MARKET_FIELD_BID_SIZE
		};
		
		for (String id: ids)
		{
			fields.put(id, get_default_decimal_field());
		}
								
		accessory.db.add_source(source_, fields);
	}
	
	private static db_field get_default_decimal_field()
	{
		return new db_field(new data(accessory.types.DATA_DECIMAL, new size(0.0, 1000000.0, accessory.defaults.SIZE_DECIMALS)), null);
	}
}