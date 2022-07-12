package ib;

import java.util.HashMap;

import accessory_ib._alls;
import db_ib.trades;

class async_data_trades extends parent_async_data 
{
	public static String _ID = "trades";
	
	public static final String SOURCE = trades.SOURCE;
	
	public static final String PRICE = trades.PRICE;
	public static final String HALTED = trades.HALTED;
	
	public static final String TYPE = TYPE_SNAPSHOT;
	public static final int DATA = external_ib.data.DATA_LIVE;
	
	private volatile HashMap<Integer, String> _order_ids = new HashMap<Integer, String>();
	
	public static async_data_trades _instance = instantiate();
	
	private async_data_trades() { }
 	
	private static async_data_trades instantiate()
	{
		async_data_trades instance = new async_data_trades();
		
		instance._source = SOURCE;
		instance._id = _ID;
		instance._includes_halted = true;
		instance._includes_halted_tot = false;
		
		return instance;
	}

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance.update_logs_to_screen_internal(logs_to_screen_); }

	public static boolean _start(int order_id_, String symbol_, boolean lock_) 
	{ 
		if (lock_) __lock();

		boolean output = (_instance.order_id_exists(order_id_) ? true : _instance.start(order_id_, symbol_)); 

		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean _stop(int order_id_, boolean lock_) 
	{ 
		if (lock_) __lock();
		
		int id = _instance.get_id(order_id_);

		boolean output = (id == WRONG_ID ? true : _instance.stop(order_id_, id)); 
		
		if (lock_) __unlock();
		
		return output;
	}
	
	public static boolean __stop_snapshot(int id_) 
	{ 
		__lock();

		boolean output = (_instance.id_is_ok(id_) ? _instance.stop_id(id_, true) : true); 

		__unlock();
		
		return output;
	}

	public static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }

	protected HashMap<Integer, String> get_all_prices() { return _alls.TRADES_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return null; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.TRADES_GENERICS; }
	
	protected String[] get_fields() { return trades.get_fields(); }

	private boolean order_id_exists(int order_id_) { return _instance._order_ids.containsKey(order_id_); }

	private boolean id_is_ok(int id_) { return (get_id(_get_symbol(id_, false)) == id_); }

	private int get_id(int order_id_) { return (_order_ids.containsKey(order_id_) ? get_id(_order_ids.get(order_id_)) : WRONG_ID); }

	private int get_id(String symbol_) { return _get_id(symbol_, false); }
	
	private boolean start(int order_id_, String symbol_) 
	{ 
		int id = _start_snapshot_internal(symbol_, DATA, false);
		if (id == WRONG_ID) return false;
			
		_order_ids.put(order_id_, symbol_);
		
		return true;
	}
		
	private boolean stop(int order_id_, int id_) 
	{ 		
		_order_ids.remove(order_id_);
		
		return stop_id(id_, false);
	}
	
	private boolean stop_id(int id_, boolean restart_) { return _stop_snapshot_internal(id_, restart_, false); }
}