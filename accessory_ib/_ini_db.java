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
import ib.conn;
import db_ib.apps;
import db_ib.basic;
import db_ib.common;
import db_ib.execs;
import db_ib.market;
import db_ib.orders;
import db_ib.remote;
import db_ib.trades;
import db_ib.watchlist;

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
		
		String db = common.DEFAULT_DB;
		
		String name = (String)arrays.get_value(setup_vals, accessory.types.CONFIG_DB_NAME);		
		if (!strings.is_ok(name)) name = common.DEFAULT_DB_NAME;
		
		HashMap<String, Object[]> sources = new HashMap<String, Object[]>();
		sources = add_source_market(db, sources);
		sources = add_source_execs(db, sources);
		sources = add_source_basic(db, sources);
		sources = add_source_remote(db, sources);
		sources = add_source_orders(db, sources);
		sources = add_source_trades(db, sources);
		sources = add_source_watchlist(db, sources);
		sources = add_source_apps(db, sources);
		
		boolean is_ok = populate_db(db, name, sources, setup_vals);
		
		return is_ok;
	}
	
	private HashMap<String, Object[]> add_source_market(String db_, HashMap<String, Object[]> sources_)
	{
		String source = market.SOURCE;
		String table = "ib_market";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(market.SYMBOL, get_symbol(true));
		info.put(market.TIME, get_time());
		info.put(market.HALTED, get_halted());
		info.put(market.HALTED_TOT, get_halted_tot());
		info.put(market.ENABLED, get_boolean(true));
		info.put(market.PRICE, get_price());
		info.put(market.OPEN, get_price());
		info.put(market.CLOSE, get_price());
		info.put(market.LOW, get_price());
		info.put(market.HIGH, get_price());
		info.put(market.ASK, get_price());
		info.put(market.BID, get_price());
		info.put(market.SIZE, get_size_volume());
		info.put(market.BID_SIZE, get_size_volume());
		info.put(market.ASK_SIZE, get_size_volume());
		info.put(market.VOLUME, get_size_volume());

		return add_source_common(db_, source, table, info, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_execs(String db_, HashMap<String, Object[]> sources_)
	{
		String source = execs.SOURCE;
		String table = "ib_execs";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(execs.USER, get_user());
		info.put(execs.EXEC_ID, get_string(30));
		info.put(execs.SYMBOL, get_symbol(false));
		info.put(execs.ORDER_ID, get_order_id(false));
		info.put(execs.SIDE, get_string(external_ib.orders.get_max_length_side()));
		info.put(execs.PRICE, get_price());
		info.put(execs.QUANTITY, get_quantity());
		info.put(execs.FEES, get_money());

		return add_source_common(db_, source, table, info, sources_);
	}
	
	private HashMap<String, Object[]> add_source_basic(String db_, HashMap<String, Object[]> sources_)
	{
		String source = basic.SOURCE;
		String table = "ib_basic";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(basic.USER, get_user(true));
		info.put(basic.ACCOUNT_IB, get_status_type());
		info.put(basic.MONEY, get_money());
		info.put(basic.MONEY_INI, get_money());
		info.put(basic.CURRENCY, get_string(contracts.get_max_length_currency()));
		info.put(basic.MONEY_FREE, get_money());
		
		return add_source_common(db_, source, table, info, sources_);
	}
	
	private HashMap<String, Object[]> add_source_remote(String db_, HashMap<String, Object[]> sources_)
	{
		String source = remote.SOURCE;
		String table = "ib_remote";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(remote.USER, get_user());
		info.put(remote.ORDER_ID_MAIN, get_order_id(false));
		info.put(remote.ORDER_ID_SEC, get_order_id(false));
		info.put(remote.SYMBOL, get_symbol(false));
		info.put(remote.TIME, get_time());
		info.put(remote.STATUS, get_status_type(remote.get_key_from_status(ib.remote.DEFAULT_STATUS)));
		info.put(remote.STATUS2, get_status_type(remote.get_key_from_status2(ib.remote.DEFAULT_STATUS2)));
		info.put(remote.START, get_price());
		info.put(remote.START2, get_price());
		info.put(remote.STOP, get_price());
		info.put(remote.IS_MARKET, get_boolean(false));	
		info.put(remote.QUANTITY, get_quantity());	
		info.put(remote.TYPE_PLACE, get_status_type());
		info.put(remote.INVEST_PERC, get_decimal_tiny());
		info.put(remote.ERROR, get_error());
		info.put(remote.IS_ACTIVE, get_boolean(true));
		info.put(remote.REQUEST, get_int(true));
		
		return add_source_common(db_, source, table, info, sources_);
	}
	
	private HashMap<String, Object[]> add_source_orders(String db_, HashMap<String, Object[]> sources_)
	{
		String source = orders.SOURCE;
		String table = "ib_orders";		
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(orders.USER, get_user());
		info.put(orders.ORDER_ID_MAIN, get_order_id(false));
		info.put(orders.ORDER_ID_SEC, get_order_id(false));
		info.put(orders.SYMBOL, get_symbol(false));
		info.put(orders.STATUS, get_status_type(orders.get_key_from_status(ib.orders.DEFAULT_STATUS)));
		info.put(orders.START, get_price());
		info.put(orders.START2, get_price());
		info.put(orders.STOP, get_price());
		info.put(orders.IS_MARKET, get_boolean(false));
		info.put(orders.TYPE_PLACE, get_status_type());
		info.put(orders.TYPE_MAIN, get_status_type());
		info.put(orders.TYPE_SEC, get_status_type());
		info.put(orders.QUANTITY, get_quantity());
		
		return add_source_common(db_, source, table, info, sources_);
	}
	
	private HashMap<String, Object[]> add_source_trades(String db_, HashMap<String, Object[]> sources_)
	{
		String source = trades.SOURCE;
		String table = "ib_trades";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(trades.USER, get_user());
		info.put(trades.ORDER_ID_MAIN, get_order_id(false));
		info.put(trades.ORDER_ID_SEC, get_order_id(false));
		info.put(trades.SYMBOL, get_symbol(false));
		info.put(trades.PRICE, get_price());
		info.put(trades.TIME_ELAPSED, get_time_elapsed());
		info.put(trades.START, get_price());
		info.put(trades.STOP, get_price());
		info.put(trades.HALTED, get_halted());
		info.put(trades.UNREALISED, get_money());
		info.put(trades.IS_ACTIVE, get_boolean(true));
		info.put(trades.POSITION, get_position());
		info.put(trades.INVESTMENT, get_money());
		info.put(trades.END, get_price());
		info.put(trades.ELAPSED_INI, get_elapsed_ini());
		info.put(trades.REALISED, get_money());
		
		return add_source_common(db_, source, table, info, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_watchlist(String db_, HashMap<String, Object[]> sources_)
	{
		String source = watchlist.SOURCE;
		String table = "ib_watchlist";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(watchlist.SYMBOL, get_symbol(true));
		info.put(watchlist.PRICE, get_price());
		info.put(watchlist.PRICE_INI, get_price());
		info.put(watchlist.PRICE_MIN, get_price());
		info.put(watchlist.PRICE_MAX, get_price());
		info.put(watchlist.VOLUME, get_size_volume());
		info.put(watchlist.VOLUME_INI, get_size_volume());
		info.put(watchlist.VOLUME_MIN, get_size_volume());
		info.put(watchlist.VOLUME_MAX, get_size_volume());
		info.put(watchlist.TIME_ELAPSED, get_time_elapsed());
		info.put(watchlist.HALTED, get_halted());
		info.put(watchlist.HALTED_TOT, get_halted_tot());
		info.put(watchlist.FLU, get_decimal_tiny());
		info.put(watchlist.FLU2, get_decimal_tiny());
		info.put(watchlist.FLU2_MIN, get_decimal_tiny());
		info.put(watchlist.FLU2_MAX, get_decimal_tiny());
		info.put(watchlist.FLU_PRICE, get_price());
		info.put(watchlist.ELAPSED_INI, get_elapsed_ini());
		
		return add_source_common(db_, source, table, info, sources_);		
	}
	
	private HashMap<String, Object[]> add_source_apps(String db_, HashMap<String, Object[]> sources_)
	{
		String source = apps.SOURCE;
		String table = "ib_apps";
		
		HashMap<String, db_field> info = new HashMap<String, db_field>();
		
		info.put(apps.APP, get_name(db_ib.common.MAX_SIZE_APP_NAME, true));
		info.put(apps.USER, get_user());
		info.put(apps.CONN_ID, get_tiny(true));
		info.put(apps.CONN_TYPE, get_string(conn.get_max_length_type()));
		info.put(apps.COUNT, get_int());
		info.put(apps.STATUS, get_status_type(apps.get_key_from_status(ib.apps.DEFAULT_STATUS)));
		info.put(apps.ERROR, get_error());
		info.put(apps.ADDITIONAL, get_string(db_ib.common.MAX_SIZE_ADDITIONAL));
		
		return add_source_common(db_, source, table, info, sources_);		
	}
		
	private HashMap<String, Object[]> add_source_common(String db_, String source_, String table_, HashMap<String, db_field> info_, HashMap<String, Object[]> sources_) { return add_source(source_, table_, db_, info_, true, sources_); }

	private static db_field get_symbol(boolean is_unique_) { return get_string(contracts.MAX_LENGTH_SYMBOL_US_ANY, is_unique_); }

	private static db_field get_order_id(boolean is_unique_) { return (is_unique_ ? new db_field(data.INT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, ib.common.WRONG_ORDER_ID, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.INT)); }

	private static db_field get_money() { return get_decimal(common.MAX_SIZE_MONEY); }
	
	private static db_field get_quantity() { return get_decimal(); }

	private static db_field get_size_volume() { return get_decimal(common.MAX_SIZE_VOLUME); }

	private static db_field get_position() { return get_decimal(common.MAX_SIZE_POSITION); }
	
	private static db_field get_decimal() { return get_decimal(common.DEFAULT_SIZE_DECIMAL); }

	private static db_field get_decimal_tiny() { return get_decimal(3); }

	private static db_field get_decimal(int size_) { return new db_field(data.DECIMAL, size_, numbers.DEFAULT_DECIMALS); }
	
	private static db_field get_price() { return new db_field(data.DECIMAL, common.MAX_SIZE_PRICE, 2); }
	
	private static db_field get_halted() { return get_tiny(); }

	private static db_field get_boolean(boolean default_) { return new db_field(data.BOOLEAN, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, default_, null); }

	private static db_field get_halted_tot() { return get_tiny(); }

	private static db_field get_tiny() { return get_tiny(false); }
	
	private static db_field get_tiny(boolean is_unique_) { return (is_unique_ ? new db_field(data.TINYINT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, db_field.WRONG_DEFAULT, new String[] { db_field.KEY_UNIQUE }) : new db_field(data.TINYINT)); }
	
	private static db_field get_time() { return get_time(false); }
	
	private static db_field get_time_elapsed() { return get_time(true); }

	private static db_field get_elapsed_ini() { return new db_field(data.LONG); }
	
	private static db_field get_time(boolean is_elapsed_) 
	{
		int size = 0;
		String def_val = null;
		
		if (is_elapsed_)
		{
			size = dates.get_length(db_ib.common.FORMAT_TIME_ELAPSED);
			def_val = "00:00:00";	
		}
		else
		{
			size = dates.get_length(db_ib.common.FORMAT_TIME_MAIN);
			def_val = "00:00";
		}
		
		return new db_field(data.STRING, size, db_field.WRONG_DECIMALS, def_val, null); 
	}

	private static db_field get_user() { return get_user(false); }

	private static db_field get_user(boolean is_unique_) { return get_string(common.MAX_SIZE_USER, is_unique_); }

	private static db_field get_status_type() { return get_status_type(null); }

	private static db_field get_status_type(String default_) { return get_string(15, false, default_); }

	private static db_field get_name(int size_, boolean is_unique_) { return get_string(size_, is_unique_); }

	private static db_field get_error() { return get_string(common.MAX_SIZE_ERROR); }

	private static db_field get_int() { return get_int(false); }
	
	private static db_field get_int(boolean is_unique_) { return new db_field(data.INT, db_field.DEFAULT_SIZE, db_field.WRONG_DECIMALS, db_field.DEFAULT_DEFAULT, (is_unique_ ? new String[] { db_field.KEY_UNIQUE } : null)); }
	
	private static db_field get_string(int size_) { return get_string(size_, false); }
	
	private static db_field get_string(int size_, boolean is_unique_) { return get_string(size_, is_unique_, null); }

	private static db_field get_string(int size_, boolean is_unique_, String default_) { return new db_field(data.STRING, size_, db_field.WRONG_DECIMALS, (strings.is_ok(default_) ? default_ : db_field.WRONG_DEFAULT), (is_unique_ ? new String[] { db_field.KEY_UNIQUE } : null)); }
}