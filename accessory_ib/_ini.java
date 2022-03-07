package accessory_ib;

import java.util.HashMap;

import accessory.data;
import accessory.db;
import accessory.db_field;
import accessory.numbers;
import accessory.size;
import accessory.dates;

public class _ini 
{
	//This method is expected to be called every time a static class is loaded.
	public static void load() 
	{
		load_types();
		load_sources();
	}
	
	private static void load_sources()
	{
		load_sources_market();
		
		load_sources_mains();
	}
	
	private static void load_types()
	{
		load_aliases();
		load_config();
	}
	
	private static void load_sources_market()
	{
		String source = types._CONFIG_IB_DB_MARKET_SOURCE;
		if (db.source_is_ok(source)) return;

		HashMap<String, db_field> fields = new HashMap<String, db_field>(get_common_fields());
	
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_SYMBOL, new db_field(new data(accessory.types.DATA_STRING, new size(0.0, 50.0, 0)), null));
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_VOLUME, new db_field(new data(accessory.types.DATA_DECIMAL, new size(0.0, numbers.MAX_DEC, accessory.defaults.SIZE_DECIMALS)), null));
		
		String[] prices = 
		{
			types._CONFIG_IB_DB_MARKET_FIELD_OPEN, types._CONFIG_IB_DB_MARKET_FIELD_CLOSE, 
			types._CONFIG_IB_DB_MARKET_FIELD_LOW, types._CONFIG_IB_DB_MARKET_FIELD_HIGH, 
			types._CONFIG_IB_DB_MARKET_FIELD_ASK, types._CONFIG_IB_DB_MARKET_FIELD_BID,
			types._CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, types._CONFIG_IB_DB_MARKET_FIELD_BID_SIZE
		};
		
		for (String id: prices)
		{
			fields.put(id, get_default_decimal_field());
		}
								
		accessory.db.add_source(source, fields);
	}
	
	private static void load_sources_mains()
	{
		db.add_source_main(types._CONFIG_IB_DB_MARKET_SOURCE, types._CONFIG_IB_DB);
	}
	
	private static HashMap<String, db_field> get_common_fields()
	{
		HashMap<String, db_field> fields = db.get_default_fields();
		
		size temp = new size(0.0, dates.get_time_pattern(dates.TIME_SHORT).length(), 0);
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_TIME, new db_field(new data(accessory.types.DATA_STRING, temp), null));
		fields.put(types._CONFIG_IB_DB_MARKET_FIELD_PRICE, get_default_decimal_field());

		return fields;
	}

	private static db_field get_default_decimal_field()
	{
		return new db_field(new data(accessory.types.DATA_DECIMAL, new size(0.0, 1000000.0, accessory.defaults.SIZE_DECIMALS)), null);
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

	private static void load_config()
	{
		load_config_root();
		load_config_conn();
		load_config_async();
		load_config_order();
		load_config_sources();		
	}

	private static void load_config_root()
	{
		String type = types._CONFIG_IB;

		accessory._config.update_ini(type, types._CONFIG_IB_CURRENCY, defaults.CURRENCY);
	}

	private static void load_config_conn()
	{
		String type = types._CONFIG_IB_CONN;

		accessory._config.update_ini(type, types._CONFIG_IB_CONN, defaults.CONN);
	}

	private static void load_config_async()
	{
		String type = types._CONFIG_IB_ASYNC;

		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_SNAPSHOT_QUICK, defaults.ASYNC_SNAPSHOT_QUICK);
		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_SNAPSHOT_CONSTANT, defaults.ASYNC_SNAPSHOT_CONSTANT);
		accessory._config.update_ini(type, types._CONFIG_IB_ASYNC_STORAGE, defaults.ASYNC_STORAGE);
	}

	private static void load_config_order()
	{
		String type = types._CONFIG_IB_ORDER;

		accessory._config.update_ini(type, types._CONFIG_IB_ORDER_TIF, defaults.ORDER_TIF);
		accessory._config.update_ini(type, types._CONFIG_IB_ORDER_QUANTITY_INT, defaults.ORDER_QUANTITY_INT);
	}

	private static void load_config_sources()
	{
		load_config_sources_db();
	}
	
	private static void load_config_sources_db()
	{
		String type = types._CONFIG_IB_DB;

		accessory._config.update_ini(type, types._CONFIG_IB_DB_COMMON_FIELD_SYMBOL, defaults.DB_COMMON_FIELD_SYMBOL);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_COMMON_FIELD_PRICE, defaults.DB_COMMON_FIELD_PRICE);

		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_SOURCE, defaults.DB_MARKET_SOURCE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_TIME, defaults.DB_MARKET_FIELD_TIME);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_OPEN, defaults.DB_MARKET_FIELD_OPEN);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_CLOSE, defaults.DB_MARKET_FIELD_CLOSE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_LOW, defaults.DB_MARKET_FIELD_LOW);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_HIGH, defaults.DB_MARKET_FIELD_HIGH);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_VOLUME, defaults.DB_MARKET_FIELD_VOLUME);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_ASK, defaults.DB_MARKET_FIELD_ASK);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_ASK_SIZE, defaults.DB_MARKET_FIELD_ASK_SIZE);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_BID, defaults.DB_MARKET_FIELD_BID);
		accessory._config.update_ini(type, types._CONFIG_IB_DB_MARKET_FIELD_BID_SIZE, defaults.DB_MARKET_FIELD_BID_SIZE);
	
		accessory._ini.load_config_default_fields(type);
	}
}