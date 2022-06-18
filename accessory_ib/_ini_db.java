package accessory_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.data;
import accessory.dates;
import accessory.db_field;
import accessory.numbers;
import accessory.parent_ini_db;
import accessory.strings;
import external_ib.contracts;
import ib.common;

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
		
		String db = accessory_ib.db.DEFAULT_DB;
		
		String name = (String)arrays.get_value(setup_vals, accessory.types.CONFIG_DB_NAME);		
		if (!strings.is_ok(name)) name = accessory_ib.db.DEFAULT_DB_NAME;
		
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_market(db, sources);
		sources = add_source_execs(db, sources);
		sources = add_source_basic(db, sources);
		sources = add_source_remote(db, sources);
		sources = add_source_orders(db, sources);
		
		boolean is_ok = populate_db(db, name, sources, setup_vals);
		
		return is_ok;
	}
	
	private HashMap<String, Object[]> add_source_market(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_MARKET_SOURCE;
		String table = "ib_market";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_DB_FIELD_SYMBOL, get_symbol(true));
		info.put(types.CONFIG_DB_FIELD_TIME, get_time());
		info.put(types.CONFIG_DB_FIELD_HALTED, new db_field(data.TINYINT));
		info.put(types.CONFIG_DB_FIELD_HALTED_TOT, new db_field(data.TINYINT));
		info.put(types.CONFIG_DB_FIELD_ENABLED, get_boolean(true));
		info.put(types.CONFIG_DB_FIELD_PRICE, get_price());
		info.put(types.CONFIG_DB_FIELD_OPEN, get_price());
		info.put(types.CONFIG_DB_FIELD_CLOSE, get_price());
		info.put(types.CONFIG_DB_FIELD_LOW, get_price());
		info.put(types.CONFIG_DB_FIELD_HIGH, get_price());
		info.put(types.CONFIG_DB_FIELD_ASK, get_price());
		info.put(types.CONFIG_DB_FIELD_BID, get_price());
		
		String[] decimal_fields = 
		{
			types.CONFIG_DB_FIELD_SIZE, types.CONFIG_DB_FIELD_BID_SIZE, 
			types.CONFIG_DB_FIELD_ASK_SIZE, types.CONFIG_DB_FIELD_VOLUME
		};		

		return add_source_common(db_, source, table, info, decimal_fields, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_execs(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_EXECS_SOURCE;
		String table = "ib_execs";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_DB_FIELD_EXEC_ID, new db_field(data.STRING, 30, 0, null, new String[] { db_field.KEY_UNIQUE }));
		info.put(types.CONFIG_DB_FIELD_SYMBOL, get_symbol(false));
		info.put(types.CONFIG_DB_FIELD_ORDER_ID, get_order_id(false));
		info.put(types.CONFIG_DB_FIELD_SIDE, new db_field(data.STRING, 3)); //Synced with execution.side's max. length as defined in external_ib.orders.
		info.put(types.CONFIG_DB_FIELD_USER, get_user());
		info.put(types.CONFIG_DB_FIELD_PRICE, get_price());

		String[] decimal_fields = new String[] { types.CONFIG_DB_FIELD_QUANTITY, types.CONFIG_DB_FIELD_FEES };

		return add_source_common(db_, source, table, info, decimal_fields, sources_);
	}
	
	private HashMap<String, Object[]> add_source_basic(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_BASIC_SOURCE;
		String table = "ib_basic";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(types.CONFIG_DB_FIELD_CONN_TYPE, new db_field(data.STRING, ib.conn.TYPE_GATEWAY_PAPER.length()));
		info.put(types.CONFIG_DB_FIELD_USER, get_user());
		
		String[] decimal_fields = new String[] { types.CONFIG_DB_FIELD_MONEY, types.CONFIG_DB_FIELD_MONEY_INI };
		
		return add_source_common(db_, source, table, info, decimal_fields, sources_);
	}
	
	private HashMap<String, Object[]> add_source_remote(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_REMOTE_SOURCE;
		String table = "ib_remote";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
			
		info.put(types.CONFIG_DB_FIELD_ORDER_ID_MAIN, get_order_id(true));
		info.put(types.CONFIG_DB_FIELD_ORDER_ID_SEC, get_order_id(true));
		info.put(types.CONFIG_DB_FIELD_SYMBOL, get_symbol(false));
		info.put(types.CONFIG_DB_FIELD_TIME, get_time());
		info.put(types.CONFIG_DB_FIELD_STATUS, get_status_type());
		info.put(types.CONFIG_DB_FIELD_STATUS2, get_status_type());
		info.put(types.CONFIG_DB_FIELD_USER, get_user());
		info.put(types.CONFIG_DB_FIELD_START, get_price());
		info.put(types.CONFIG_DB_FIELD_START2, get_price());
		info.put(types.CONFIG_DB_FIELD_STOP, get_price());
		info.put(types.CONFIG_DB_FIELD_IS_MARKET, get_boolean(false));	
		
		String[] decimal_fields = new String[] { types.CONFIG_DB_FIELD_QUANTITY };
		
		return add_source_common(db_, source, table, info, decimal_fields, sources_);
	}
	
	private HashMap<String, Object[]> add_source_orders(String db_, HashMap<String, Object[]> sources_)
	{
		String source = types.CONFIG_DB_IB_ORDERS_SOURCE;
		String table = "ib_orders";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
			
		info.put(types.CONFIG_DB_FIELD_ORDER_ID_MAIN, get_order_id(true));
		info.put(types.CONFIG_DB_FIELD_ORDER_ID_SEC, get_order_id(true));
		info.put(types.CONFIG_DB_FIELD_SYMBOL, get_symbol(false));
		info.put(types.CONFIG_DB_FIELD_STATUS, get_status_type());
		info.put(types.CONFIG_DB_FIELD_USER, get_user());
		info.put(types.CONFIG_DB_FIELD_START, get_price());
		info.put(types.CONFIG_DB_FIELD_START2, get_price());
		info.put(types.CONFIG_DB_FIELD_STOP, get_price());
		info.put(types.CONFIG_DB_FIELD_IS_MARKET, get_boolean(false));
		info.put(types.CONFIG_DB_FIELD_TYPE_PLACE, get_status_type());
		info.put(types.CONFIG_DB_FIELD_TYPE_MAIN, get_status_type());
		info.put(types.CONFIG_DB_FIELD_TYPE_SEC, get_status_type());
		
		String[] decimal_fields = new String[] { types.CONFIG_DB_FIELD_QUANTITY };
		
		return add_source_common(db_, source, table, info, decimal_fields, sources_);
	}

	private HashMap<String, Object[]> add_source_common(String db_, String source_, String table_, HashMap<String, db_field> info_, String[] decimal_fields_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>(info_);
		
		info = add_fields_decimal(decimal_fields_, info);

		return add_source(source_, table_, db_, info, true, sources_);
	}
	
	private static HashMap<String, db_field> add_fields_decimal(String[] ids_, HashMap<String, db_field> info_)
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>(info_);
		
		for (String id: ids_) { info.put(id, get_decimal()); }
		
		return info;
	}

	private static db_field get_symbol(boolean is_unique_) { return (is_unique_ ? new db_field(data.STRING, 5, 0, null, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.STRING, contracts.MAX_LENGTH_SYMBOL_US_ANY)); }

	private static db_field get_order_id(boolean is_unique_) { return (is_unique_ ? new db_field(data.INT, 0, 0, null, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.INT)); }

	private static db_field get_decimal() { return new db_field(data.DECIMAL, 10, numbers.DEFAULT_DECIMALS); }

	private static db_field get_boolean(boolean default_) { return new db_field(data.BOOLEAN, 0, 0, default_, null); }
	
	private static db_field get_price() { return new db_field(data.DECIMAL, 4, 2); }

	private static db_field get_time() { return new db_field(data.STRING, dates.get_length(dates.FORMAT_TIME_SHORT)); }

	private static db_field get_user() { return new db_field(data.STRING, common.MAX_LENGTH_USER); }

	private static db_field get_status_type() { return new db_field(data.STRING, 15); }
}