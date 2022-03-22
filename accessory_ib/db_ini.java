package accessory_ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.data;
import accessory.db_field;
import accessory.size;

class db_ini 
{
	public static void load() 
	{
		load_types();
		load_sources();
	}

	private static void load_sources()
	{
		HashMap<String, String> source_mains = new HashMap<String, String>();
		source_mains.put(db.SOURCE_MARKET, types.CONFIG_IB_DB);
		
		load_sources_all(source_mains);
	}
	
	//Method including the types more closely related to the DB setup.
	private static void load_types()
	{
		load_types_config();
	}
	
	private static void load_types_config()
	{
		load_config_sources();
	}
	
	private static void load_config_sources()
	{
		String[] mains = new String[] { types.CONFIG_IB_DB  };
		
		for (String main: mains)
		{
			accessory.db_ini.load_config_sources_default_fields(main);	
			
			if (main.equals(types.CONFIG_IB_DB)) load_config_sources_db(main);
		}
	}
	
	private static void load_config_sources_db(String main_)
	{
		accessory.config.update_ini(main_, db.FIELD_SYMBOL, db.DEFAULT_FIELD_SYMBOL);
		accessory.config.update_ini(main_, db.FIELD_PRICE, db.DEFAULT_FIELD_PRICE);

		accessory.config.update_ini(main_, db.SOURCE_MARKET, db.DEFAULT_SOURCE_MARKET);
		accessory.config.update_ini(main_, db.FIELD_TIME, db.DEFAULT_FIELD_TIME);
		accessory.config.update_ini(main_, db.FIELD_OPEN, db.DEFAULT_FIELD_OPEN);
		accessory.config.update_ini(main_, db.FIELD_CLOSE, db.DEFAULT_FIELD_CLOSE);
		accessory.config.update_ini(main_, db.FIELD_LOW, db.DEFAULT_FIELD_LOW);
		accessory.config.update_ini(main_, db.FIELD_HIGH, db.DEFAULT_FIELD_HIGH);
		accessory.config.update_ini(main_, db.FIELD_VOLUME, db.DEFAULT_FIELD_VOLUME);
		accessory.config.update_ini(main_, db.FIELD_ASK, db.DEFAULT_FIELD_ASK);
		accessory.config.update_ini(main_, db.FIELD_ASK_SIZE, db.DEFAULT_FIELD_ASK_SIZE);
		accessory.config.update_ini(main_, db.FIELD_BID, db.DEFAULT_FIELD_BID);
		accessory.config.update_ini(main_, db.FIELD_BID_SIZE, db.DEFAULT_FIELD_BID_SIZE);
		accessory.config.update_ini(main_, db.FIELD_HALTED, db.DEFAULT_FIELD_HALTED);
		accessory.config.update_ini(main_, db.FIELD_HALTED_TOT, db.DEFAULT_FIELD_HALTED_TOT);
		
		accessory.db_ini.load_config_sources_default_fields(main_);
	}
	
	private static void load_sources_all(HashMap<String, String> source_mains_)
	{		
		for (Entry<String, String> item: source_mains_.entrySet())
		{
			String source = item.getKey();
			String main = item.getValue();
		
			load_sources_source(source);			
			accessory.db.add_source_main(source, main);
		}
	}
	
	private static void load_sources_source(String source_)
	{
		if (source_.equals(db.SOURCE_MARKET)) load_sources_source_market(source_);
	}
	
	private static void load_sources_source_market(String source_)
	{
		if (accessory.db.source_is_ok(source_)) return;

		HashMap<String, db_field> fields = accessory.db.get_default_fields();
	
		fields.put(db.FIELD_SYMBOL, new db_field(data.STRING, 50, 0));
		fields.put(db.FIELD_VOLUME, new db_field(data.DECIMAL, 10, size.DEFAULT_DECIMALS));
		fields.put(db.FIELD_HALTED, new db_field(data.STRING, 50, 0));
		fields.put(db.FIELD_HALTED_TOT, new db_field(data.INT, 2, 0));
		
		String[] ids = 
		{
			db.FIELD_OPEN, db.FIELD_CLOSE, db.FIELD_LOW, db.FIELD_HIGH, 
			db.FIELD_ASK, db.FIELD_BID, db.FIELD_ASK_SIZE, db.FIELD_BID_SIZE
		};
		
		for (String id: ids)
		{
			fields.put(id, get_default_decimal_field());
		}
								
		accessory.db.add_source(source_, fields);
	}
	
	private static db_field get_default_decimal_field()
	{
		return new db_field(data.DECIMAL, 10, size.DEFAULT_DECIMALS);
	}
}