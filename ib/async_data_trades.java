package ib;

import java.util.HashMap;

import accessory.strings;
import accessory_ib._alls;
import db_ib.trades;

public class async_data_trades extends parent_async_data 
{
	public static String _ID = "trades";
	
	public static final int DATA = external_ib.data.DATA_LIVE;
	public static final String TARGET_STATUS = order.STATUS_SUBMITTED;
	
	private volatile HashMap<Integer, Integer> _orders_ids = new HashMap<Integer, Integer>();
	
	public static async_data_trades _instance = instantiate();
	
	private async_data_trades() { }
 	
	private static async_data_trades instantiate()
	{
		async_data_trades instance = new async_data_trades();
		
		instance._source = trades.SOURCE;
		instance._id = _ID;
		
		return instance;
	}

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static boolean is_ok(String status_ib_) { return (_instance._enabled && order.get_status(status_ib_, true).equals(TARGET_STATUS)); }
	
	public static void order_status(int order_id_, String status_ib_) 
	{ 
		if (!is_ok(status_ib_)) return;
		
		__start(order_id_);
	}

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE_IB, trades.PRICE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED_IB, trades.HALTED);
		
		return all;
	}

	static void __start(int order_id_) { _instance.__add(order_id_); }

	static void __stop(int order_id_) { _instance.__remove(order_id_); }
	
	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }

	static boolean __stop_snapshot(int id_) { return _instance.__stop_snapshot_internal(id_); }

	protected HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_TRADES_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return null; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_TRADES_GENERICS; }

	private String get_symbol(int order_id_) { return order.get_symbol(order_id_); }
	
	private void __add(int order_id_) 
	{
		__lock();
		
		if (_orders_ids.containsKey(order_id_))
		{
			__unlock();
			
			return;
		}
		
		String symbol = get_symbol(order_id_);
		if (!strings.is_ok(symbol))
		{
			__unlock();
			
			return;
		}
		
		int id = _start_snapshot_internal(symbol, DATA, false);
		if (id == WRONG_ID)
		{
			__unlock();
			
			return;
		}
		
		_orders_ids.put(order_id_, id);
		trades.insert(order_id_, symbol);
		
		__unlock();
	}
	
	private void __remove(int order_id_) 
	{
		__lock();
		
		if (!_orders_ids.containsKey(order_id_))
		{
			__unlock();
			
			return;
		}

		__stop_snapshot(_orders_ids.get(order_id_));
		
		_orders_ids.remove(order_id_);
		
		trades.delete(order_id_);
		
		__unlock();
	}
}