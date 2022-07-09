package accessory_ib;

import java.util.HashMap;

import accessory.parent_ini_config;
import external_ib.contracts;
import ib.basic;
import ib.conn;
import ib.order;
import ib.parent_async_data;

public class _ini_config extends parent_ini_config 
{
	private static _ini_config _instance = new _ini_config();
	
	public _ini_config() { }
	
	public static void populate() { _instance.populate_all(); }
	
	protected void populate_all_internal()
	{
		load_config_basic();
		load_config_conn();
		load_config_order();
		load_config_async();
		load_config_contract();
	}

	private boolean load_config_basic()
	{
		String type = accessory.types.CONFIG_BASIC;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(basic.CONFIG_MAIN_ID, basic.DEFAULT_MAIN_ID);
		
		vals.put(basic.CONFIG_ACCOUNT_IB, basic.encrypt_account_ib_ini(_ini.get_account_ib()));		
		_ini.delete_account_ib();		

		return populate(type, null, vals);
	}
	
	private boolean load_config_conn()
	{
		String type = types.CONFIG_CONN;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(conn.CONFIG_TYPE, conn.DEFAULT_TYPE);
		vals.put(conn.CONFIG_CONN_ID, conn.DEFAULT_ID);
		
		return populate(type, null, vals);
	}

	private boolean load_config_order()
	{
		String type = types.CONFIG_ORDERS;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(order.CONFIG_TIF, order.DEFAULT_TIF);
		vals.put(order.CONFIG_QUANTITIES_INT, order.DEFAULT_QUANTITIES_INT);

		return populate(type, null, vals);
	}
	
	private boolean load_config_async()
	{
		String type = types.CONFIG_ASYNC;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(parent_async_data.CONFIG_SNAPSHOT_QUICK, parent_async_data.DEFAULT_SNAPSHOT_QUICK);
		vals.put(parent_async_data.CONFIG_SNAPSHOT_NONSTOP, parent_async_data.DEFAULT_SNAPSHOT_NONSTOP);
		
		return populate(type, null, vals);
	}

	private boolean load_config_contract()
	{
		String type = types.CONFIG_CONTRACT;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(contracts.CONFIG_CURRENCY, contracts.DEFAULT_CURRENCY);
		vals.put(contracts.CONFIG_SECURITY_TYPE, contracts.DEFAULT_SECURITY_TYPE);
		vals.put(contracts.CONFIG_EXCHANGE, contracts.DEFAULT_EXCHANGE);
		vals.put(contracts.CONFIG_EXCHANGE_PRIMARY, contracts.DEFAULT_EXCHANGE_PRIMARY);
		vals.put(contracts.CONFIG_EXCHANGE_COUNTRY, contracts.DEFAULT_EXCHANGE_COUNTRY);

		return populate(type, null, vals);
	}
}