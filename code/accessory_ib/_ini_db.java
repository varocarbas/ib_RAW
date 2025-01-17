package accessory_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.db_common;
import accessory.db_field;
import accessory.misc;
import accessory.parent_ini_db;
import accessory.strings;
import external_ib.contracts;
import ib.conn;
import db_ib.apps;
import db_ib.basic;
import db_ib.common;
import db_ib.execs;
import db_ib.market;
import db_ib.orders;
import db_ib.remote;
import db_ib.symbols;
import db_ib.temp_price;
import db_ib.watchlist;

public class _ini_db extends parent_ini_db 
{
	private static _ini_db _instance = new _ini_db();
	
	public _ini_db() { }
	
	public static void populate(String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { _instance.populate_all(dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_); }

	public static void populate(HashMap<String, Object> dbs_setup_) { _instance.populate_all(dbs_setup_, null); }

	public static void populate(HashMap<String, Object> dbs_setup_, String[] types_to_ignore_) { _instance.populate_all(dbs_setup_, types_to_ignore_); }

	public static String get_table_old(String table_) { return (table_ + misc.SEPARATOR_NAME + "old"); }
	
	@SuppressWarnings("unchecked")
	protected boolean populate_all_dbs(HashMap<String, Object> dbs_setup_)
	{
		HashMap<String, Object> setup_vals = (HashMap<String, Object>)arrays.get_new(dbs_setup_);
		
		String db = common.DEFAULT_DB;
		
		String name = (String)arrays.get_value(setup_vals, accessory._types.CONFIG_DB_NAME);		
		if (!strings.is_ok(name)) name = common.DEFAULT_DB_NAME;
		
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_market(db, sources);
		sources = add_source_execs(db, sources);
		sources = add_source_basic(db, sources);
		sources = add_source_remote(db, sources);
		sources = add_source_orders(db, sources);
		sources = add_source_watchlist(db, sources);
		sources = add_source_apps(db, sources);
		sources = add_source_symbols(db, sources);
		sources = add_source_temp_price(db, sources);
		
		boolean is_ok = populate_db(db, name, sources, setup_vals);
		
		return is_ok;
	}
	
	private HashMap<String, Object[]> add_source_market(String db_, HashMap<String, Object[]> sources_)
	{
		String source = market.SOURCE;
		if (ignore_source(source)) return sources_;
		
		String table = "ib_market";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(market.SYMBOL, common.get_field_symbol(true));
		info.put(market.TIME, common.get_field_time());
		info.put(market.HALTED, common.get_field_halted());
		info.put(market.HALTED_TOT, common.get_field_halted_tot());
		info.put(market.ENABLED, db_common.get_field_boolean(true));
		info.put(market.PRICE, common.get_field_price());
		info.put(market.OPEN, common.get_field_price());
		info.put(market.CLOSE, common.get_field_price());
		info.put(market.LOW, common.get_field_price());
		info.put(market.HIGH, common.get_field_price());
		info.put(market.ASK, common.get_field_price());
		info.put(market.BID, common.get_field_price());
		info.put(market.SIZE, common.get_field_size_volume());
		info.put(market.BID_SIZE, common.get_field_size_volume());
		info.put(market.ASK_SIZE, common.get_field_size_volume());
		info.put(market.VOLUME, common.get_field_size_volume());

		return add_source_common(db_, source, table, info, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_execs(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
	
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		String source = execs.SOURCE;
	
		String table = "ib_execs";
		
		if (!ignore_source(source))
		{
			info.put(execs.EXEC_ID, db_common.get_field_string());
			info.put(execs.SYMBOL, common.get_field_symbol(false));
			info.put(execs.ORDER_ID, common.get_field_order_id(false));
			info.put(execs.SIDE, db_common.get_field_string(external_ib.orders.get_max_length_side()));
			info.put(execs.PRICE, common.get_field_price());
			info.put(execs.QUANTITY, common.get_field_quantity());
			info.put(execs.FEES, common.get_field_money());
			
			sources = add_source_common(db_, source, table, info, sources);
		}		

		source = execs.SOURCE_OLD;
		if (!ignore_source(source)) sources = add_source_common(db_, source, get_table_old(table), info, sources, true);
		
		return sources;
	}
	
	private HashMap<String, Object[]> add_source_basic(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		HashMap<String, db_field> info2 = new HashMap<String, db_field>();	
		
		String source = basic.SOURCE;
				
		String table = "ib_basic";		

		if (!ignore_source(source))
		{
			info.put(basic.ACCOUNT_IB, common.get_field_status_type());
			info.put(basic.MONEY, common.get_field_money());
			info.put(basic.MONEY_INI, common.get_field_money());
			info.put(basic.CURRENCY, db_common.get_field_string(contracts.get_max_length_currency()));
			info.put(basic.MONEY_FREE, common.get_field_money());	
			
			info2 = new HashMap<String, db_field>(info);	
			info2.put(basic.USER, common.get_field_user(true));
			
			sources = add_source_common(db_, source, table, info2, sources);
		}

		source = basic.SOURCE_OLD;

		if (!ignore_source(source))
		{
			info2 = new HashMap<String, db_field>(info);	
			info2.put(basic.USER, common.get_field_user(false));

			sources = add_source_common(db_, source, get_table_old(table), info2, sources, true);
		}
		
		return sources;
	}
	
	private HashMap<String, Object[]> add_source_remote(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		HashMap<String, db_field> info2 = new HashMap<String, db_field>();	
		
		String source = remote.SOURCE;

		String table = "ib_remote";		

		if (!ignore_source(source))
		{			
			info.put(remote.ORDER_ID_MAIN, common.get_field_order_id(false));
			info.put(remote.ORDER_ID_SEC, common.get_field_order_id(false));
			info.put(remote.SYMBOL, common.get_field_symbol(false));
			info.put(remote.STATUS, common.get_field_status_type(_keys.get_key(ib.remote.DEFAULT_STATUS, ib.remote.STATUS)));
			info.put(remote.STATUS2, common.get_field_status_type(_keys.get_key(ib.remote.DEFAULT_STATUS2, ib.remote.STATUS2)));
			info.put(remote.START, common.get_field_price());
			info.put(remote.START2, common.get_field_price());
			info.put(remote.STOP, common.get_field_price());
			info.put(remote.STOP2, common.get_field_price());
			info.put(remote.IS_MARKET, db_common.get_field_boolean(false));	
			info.put(remote.QUANTITY, common.get_field_quantity());	
			info.put(remote.PERC_MONEY, db_common.get_field_decimal_tiny());
			info.put(remote.PRICE, common.get_field_price());
			info.put(remote.ERROR, db_common.get_field_error());
			info.put(remote.TYPE_ORDER, common.get_field_status_type());	
			info.put(remote.TIME2, common.get_field_time2());
			
			info2 = new HashMap<String, db_field>(info);	
			info2.put(remote.REQUEST, db_common.get_field_int(true));

			sources = add_source_common(db_, source, table, info2, sources);
		}

		source = remote.SOURCE_OLD;

		if (!ignore_source(source))
		{
			info2 = new HashMap<String, db_field>(info);	
			info2.put(remote.REQUEST, db_common.get_field_int(false));

			sources = add_source_common(db_, source, get_table_old(table), info2, sources, true);			
		}
				
		return sources;
	}
	
	private HashMap<String, Object[]> add_source_orders(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		HashMap<String, db_field> info2 = new HashMap<String, db_field>();	

		String source = orders.SOURCE;

		String table = "ib_orders";		
		
		if (!ignore_source(source))
		{
			info.put(orders.SYMBOL, common.get_field_symbol(false));
			info.put(orders.STATUS, common.get_field_status_type(_keys.get_key(ib.orders.DEFAULT_STATUS, ib.orders.STATUS)));
			info.put(orders.START, common.get_field_price());
			info.put(orders.START2, common.get_field_price());
			info.put(orders.STOP, common.get_field_price());
			info.put(orders.STOP2, common.get_field_price());
			info.put(orders.IS_MARKET, db_common.get_field_boolean(false));
			info.put(orders.TYPE_PLACE, common.get_field_status_type());
			info.put(orders.TYPE_MAIN, common.get_field_status_type());
			info.put(orders.TYPE_SEC, common.get_field_status_type());
			info.put(orders.QUANTITY, common.get_field_quantity());
			
			info2 = new HashMap<String, db_field>(info);	
			info2.put(orders.ORDER_ID_MAIN, common.get_field_order_id(true));
			info2.put(orders.ORDER_ID_SEC, common.get_field_order_id(true));
			
			sources = add_source_common(db_, source, table, info2, sources);
		}		

		source = orders.SOURCE_OLD;
		
		if (!ignore_source(source))
		{
			info2 = new HashMap<String, db_field>(info);	
			info2.put(orders.ORDER_ID_MAIN, common.get_field_order_id(false));
			info2.put(orders.ORDER_ID_SEC, common.get_field_order_id(false));

			sources = add_source_common(db_, source, get_table_old(table), info2, sources, true);			
		}
				
		return sources;
	}
	
	private HashMap<String, Object[]> add_source_watchlist(String db_, HashMap<String, Object[]> sources_)
	{
		String source = watchlist.SOURCE;
		if (ignore_source(source)) return sources_;
		
		String table = "ib_watchlist";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(watchlist.SYMBOL, common.get_field_symbol(true));
		info.put(watchlist.PRICE, common.get_field_price());
		info.put(watchlist.PRICE_INI, common.get_field_price());
		info.put(watchlist.PRICE_MIN, common.get_field_price());
		info.put(watchlist.PRICE_MAX, common.get_field_price());
		info.put(watchlist.ASK, common.get_field_price());
		info.put(watchlist.BID, common.get_field_price());
		info.put(watchlist.VOLUME, common.get_field_size_volume());
		info.put(watchlist.VOLUME_INI, common.get_field_size_volume());
		info.put(watchlist.VOLUME_MIN, common.get_field_size_volume());
		info.put(watchlist.VOLUME_MAX, common.get_field_size_volume());
		info.put(watchlist.TIME_ELAPSED, common.get_field_time_elapsed());
		info.put(watchlist.FLU, db_common.get_field_decimal_tiny());
		info.put(watchlist.FLU2, db_common.get_field_decimal_tiny());
		info.put(watchlist.FLU2_MIN, db_common.get_field_decimal_tiny());
		info.put(watchlist.FLU2_MAX, db_common.get_field_decimal_tiny());
		info.put(watchlist.FLU3, db_common.get_field_tiny());
		info.put(watchlist.FLUS_PRICE, common.get_field_price());
		info.put(watchlist.ELAPSED_INI, common.get_field_elapsed_ini());
		info.put(watchlist.VAR_TOT, db_common.get_field_decimal_tiny());
		
		return add_source_common(db_, source, table, info, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_apps(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		HashMap<String, db_field> info2 = new HashMap<String, db_field>();	

		String source = apps.SOURCE;

		String table = "ib_apps";

		if (!ignore_source(source))
		{
			info.put(apps.USER, common.get_field_user());
			info.put(apps.CONN_ID, db_common.get_field_tiny(false));
			info.put(apps.CONN_TYPE, db_common.get_field_string(conn.get_max_length_type_key()));
			info.put(apps.CONN_IS_ON, db_common.get_field_boolean(false));
			info.put(apps.STATUS, common.get_field_status_type(_keys.get_key(ib.apps.DEFAULT_STATUS, ib.apps.STATUS)));
			info.put(apps.ERROR, db_common.get_field_error());
			info.put(apps.ADDITIONAL, db_common.get_field_string(db_ib.common.MAX_SIZE_ADDITIONAL));
			info.put(apps.TIME2, common.get_field_time2());
			
			info2 = new HashMap<String, db_field>(info);	
			info2.put(apps.APP, common.get_field_app(true));
			
			sources = add_source_common(db_, source, table, info2, sources);					
		}

		source = apps.SOURCE_OLD;

		if (!ignore_source(source))
		{
			info2 = new HashMap<String, db_field>(info);	
			info2.put(apps.APP, common.get_field_app(false));

			sources = add_source_common(db_, source, get_table_old(table), info2, sources, true);
		}
		
		return sources;
	}	
	
	private HashMap<String, Object[]> add_source_symbols(String db_, HashMap<String, Object[]> sources_)
	{
		HashMap<String, Object[]> sources = (sources_ == null ? new HashMap<String, Object[]>() : new HashMap<String, Object[]>(sources_));
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		HashMap<String, db_field> info2 = new HashMap<String, db_field>();	

		String source = symbols.SOURCE;

		String table = "ib_symbols";

		if (!ignore_source(source))
		{	
			info.put(symbols.NAME, db_common.get_field_string(false));
			info.put(symbols.EXCHANGE, db_common.get_field_string(false));
			info.put(symbols.COUNTRY, db_common.get_field_string(false));
			
			info2 = new HashMap<String, db_field>(info);		
			info2.put(symbols.SYMBOL, common.get_field_symbol(true));
			
			sources = add_source_common(db_, source, table, info2, sources);					
		}

		source = symbols.SOURCE_OLD;

		if (!ignore_source(source))
		{
			info2 = new HashMap<String, db_field>(info);	
			info2.put(symbols.SYMBOL, common.get_field_symbol(false));

			sources = add_source_common(db_, source, get_table_old(table), info2, sources, true);
		}
		
		return sources;
	}
	
	private HashMap<String, Object[]> add_source_temp_price(String db_, HashMap<String, Object[]> sources_)
	{
		String source = temp_price.SOURCE;
		if (ignore_source(source)) return sources_;
		
		String table = "ib_temp_price";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(temp_price.SYMBOL, common.get_field_symbol(false));
		info.put(temp_price.PRICE, common.get_field_price());
		info.put(temp_price.ID, db_common.get_field_int());
		
		return add_source_common(db_, source, table, info, sources_);
	}
	
	private HashMap<String, Object[]> add_source_common(String db_, String source_, String table_, HashMap<String, db_field> info_, HashMap<String, Object[]> sources_) { return add_source_common(db_, source_, table_, info_, sources_, false); }
	
	private HashMap<String, Object[]> add_source_common(String db_, String source_, String table_, HashMap<String, db_field> info_, HashMap<String, Object[]> sources_, boolean is_old_) 
	{ 
		HashMap<String, db_field> info = new HashMap<String, db_field>(info_);
	
		if (is_old_) info.put(common.FIELD_DATE, common.get_field_date());
		
		return add_source(source_, table_, db_, info, true, sources_); 
	}
}