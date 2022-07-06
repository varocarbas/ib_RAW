package ib;

import java.util.HashMap;

import accessory.arrays;
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
	
	static void __order_status(int order_id_, String status_ib_) 
	{ 
		__lock();
		
		if (!is_ok(order_id_, status_ib_))
		{
			__unlock();
			
			return;
		}
		
		_instance.start_order_id_internal(order_id_); 
		
		__unlock();
	}

	static void __start_order_id(int order_id_) 
	{ 
		__lock();
		
		if (!is_ok(order_id_))
		{
			__unlock();
			
			return;
		}
		
		_instance.start_order_id_internal(order_id_); 
		
		__unlock();
	}
	
	static boolean __stop_snapshot(int id_) 
	{ 
		__lock();
		
		boolean output = false;
		
		if (!id_is_ok(id_))
		{
			__unlock();
			
			return output;
		}
		
		output = _instance.stop_id_internal(id_); 

		__unlock();
		
		return output;
	}

	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }

	protected HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_TRADES_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return null; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_TRADES_GENERICS; }

	private static boolean is_ok(int order_id_, String status_ib_) { return (is_ok(order_id_) && order.get_status(status_ib_, true).equals(TARGET_STATUS)); }
	
	private static boolean is_ok(int order_id_) { return (_instance._enabled && !_instance._orders_ids.containsKey(order_id_) && trades.order_id_is_ok(order_id_)); }
	
	private static boolean id_is_ok(int id_) { return _instance._orders_ids.containsValue(id_); }

	private String get_symbol(int order_id_) { return order.get_symbol(order_id_); }

	private int get_order_id(int id_) { return (_orders_ids.containsValue(id_) ? (int)arrays.get_key(_orders_ids, id_) : WRONG_ID); }

	private void start_order_id_internal(int order_id_) 
	{
		String symbol = get_symbol(order_id_);
		if (!strings.is_ok(symbol)) return;
		
		start_trade(order_id_, symbol);
		
		add_trade(order_id_, symbol);
	}

	private boolean stop_id_internal(int id_) 
	{ 	
		boolean output = stop_trade(id_);
		
		remove_trade(get_order_id(id_));
		
		return output;
	}
	
	private void start_trade(int order_id_, String symbol_) 
	{ 
		if (order_id_ == WRONG_ID) return;
		
		int id = _start_snapshot_internal(symbol_, DATA, false);
		
		if (id != WRONG_ID) _orders_ids.put(order_id_, id);
	}
	
	private void add_trade(int order_id_, String symbol_) 
	{ 
		if (order_id_ == WRONG_ID) return;
		
		trades.insert(order_id_, symbol_); 
	}
	
	private boolean stop_trade(int id_) { return (id_ == WRONG_ID ? false : _stop_snapshot_internal(id_, false)); }
	
	private void remove_trade(int order_id_) 
	{
		if (order_id_ == WRONG_ID) return;
		
		_orders_ids.remove(order_id_);
		
		trades.delete(order_id_);
	}
}