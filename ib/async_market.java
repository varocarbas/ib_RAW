package ib;

import java.util.HashMap;

import accessory_ib._alls;
import db_ib.market;

public class async_market extends parent_async_data 
{	
	public static async_market _instance = new async_market();
	
	public async_market() { }
 	
	public static void enable() { _instance._enabled = true; }

	public static void disable() { _instance._enabled = false; }

	public static boolean get_logs_to_screen() { return _instance._logs_to_screen; }

	public static void update_logs_to_screen() { _instance._logs_to_screen = DEFAULT_LOGS_TO_SCREEN; }

	public static void update_logs_to_screen(boolean logs_to_screen_) { _instance._logs_to_screen = logs_to_screen_; }

	public static boolean get_is_db_quick() { return _instance._is_db_quick; }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	public static void update_is_db_quick() { update_is_db_quick(DEFAULT_IS_DB_QUICK); }

	//Use carefully! It relies on col-based/no-data-checks db methods (i.e., "_quick" ones).
	public static void update_is_db_quick(boolean is_db_quick_) { _instance._is_db_quick = is_db_quick_; }

	public static int get_nonstop_pause() { return _instance._nonstop_pause; }

	public static void update_nonstop_pause() { update_nonstop_pause(DEFAULT_NONSTOP_PAUSE); }

	public static void update_nonstop_pause(int nonstop_pause_) { _instance._nonstop_pause = (nonstop_pause_ >= 0 ? nonstop_pause_ : DEFAULT_NONSTOP_PAUSE); }
	
	public static void __stop_all() { _instance.__stop_all_internal(); }
	
	public static boolean __start_snapshot(String symbol_) { return _instance.__start_snapshot_internal(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean __start_snapshot(String symbol_, int data_type_) { return _instance.__start_snapshot_internal(symbol_, data_type_); }

	public static boolean __start_stream(String symbol_) { return __start_stream(symbol_, DEFAULT_DATA_TYPE); }

	public static boolean __start_stream(String symbol_, int data_type_) { return _instance.__start_stream_internal(symbol_, data_type_); }

	public static void __stop_snapshot(String symbol_) { _instance.__stop_snapshot_internal(symbol_); }

	public static void __stop_snapshot(int id_) { _instance.__stop_snapshot_internal(id_); }
	
	public static void __stop_stream(String symbol_) { _instance.__stop_stream_internal(symbol_); }

	public static void __stop_stream(int id_) { _instance.__stop_stream_internal(id_); }
	
	public static String __get_symbol(int id_) { return _instance._get_symbol(id_, true); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, market.PRICE);
		all.put(OPEN, market.OPEN);
		all.put(CLOSE, market.CLOSE);
		all.put(LOW, market.LOW);
		all.put(HIGH, market.HIGH);
		all.put(ASK, market.ASK);
		all.put(BID, market.BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME, market.VOLUME);
		all.put(SIZE, market.SIZE);
		all.put(ASK_SIZE, market.ASK_SIZE);
		all.put(BID_SIZE, market.BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, market.HALTED);
		
		return all;
	}

	protected boolean is_enabled(String symbol_) { return market.is_enabled(symbol_); }

	protected boolean exists(String symbol_) { return market.exists(symbol_); }

	protected void insert(String symbol_) { market.insert(symbol_); }
	
	protected void insert_quick(String symbol_) { market.insert_quick(symbol_); } 

	protected void update(String symbol_, HashMap<String, Object> vals_) { market.update(symbol_, vals_); }
	
	protected void update(String symbol_, String field_, Object val_) { market.update(symbol_, field_, val_); }
	
	protected void update_quick(String symbol_, HashMap<String, String> vals_) { market.update_quick(symbol_, vals_); } 
	
	protected void update_quick(String symbol_, String col_, String val_) { market.update_quick(symbol_, col_, val_); }
	
	protected String get_col(String field_) { return market.get_col(field_); }
	
	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
	
	protected HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_MARKET_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_MARKET_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_MARKET_GENERICS; }
}