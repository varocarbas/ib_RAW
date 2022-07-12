package ib;

import java.util.HashMap;

public abstract class market 
{
	public static final String SOURCE = db_ib.market.SOURCE;
	
	public static final String PRICE = db_ib.market.PRICE;
	public static final String SIZE = db_ib.market.SIZE;
	public static final String OPEN = db_ib.market.OPEN;
	public static final String CLOSE = db_ib.market.CLOSE;
	public static final String LOW = db_ib.market.LOW;
	public static final String HIGH = db_ib.market.HIGH;
	public static final String VOLUME = db_ib.market.VOLUME;		
	public static final String ASK = db_ib.market.ASK;
	public static final String ASK_SIZE = db_ib.market.ASK_SIZE;
	public static final String BID = db_ib.market.BID;
	public static final String BID_SIZE = db_ib.market.BID_SIZE;
	public static final String HALTED = db_ib.market.HALTED;
	public static final String HALTED_TOT = db_ib.market.HALTED_TOT;
	
	public static void update_nonstop_pause(int nonstop_pause_) { async_data_market._instance.update_nonstop_pause_internal(nonstop_pause_); }

	public static void update_logs_to_screen(boolean logs_to_screen_) { async_data_market._instance.update_logs_to_screen_internal(logs_to_screen_); }
	
	public static void __stop_all() { async_data_market._instance.__stop_all_internal(); }
	
	public static int __start_snapshot(String symbol_) { return async_data_market._instance.__start_snapshot_internal(symbol_, parent_async_data.DEFAULT_DATA_TYPE); }

	public static int __start_snapshot(String symbol_, int data_type_) { return async_data_market._instance.__start_snapshot_internal(symbol_, data_type_); }

	public static int __start_stream(String symbol_) { return async_data_market._instance.__start_stream_internal(symbol_, parent_async_data.DEFAULT_DATA_TYPE); }

	public static int __start_stream(String symbol_, int data_type_) { return async_data_market._instance.__start_stream_internal(symbol_, data_type_); }

	public static boolean __stop_snapshot(String symbol_) { return async_data_market._instance.__stop_snapshot_internal(symbol_); }

	public static boolean __stop_snapshot(int id_) { return async_data_market._instance.__stop_snapshot_internal(id_); }
	
	public static boolean __stop_stream(String symbol_) { return async_data_market._instance.__stop_stream_internal(symbol_); }

	public static boolean __stop_stream(int id_) { return async_data_market._instance.__stop_stream_internal(id_); }
	
	public static String __get_symbol(int id_) { return async_data_market._instance._get_symbol(id_, true); }
	
	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(parent_async_data.PRICE_IB, PRICE);
		all.put(parent_async_data.OPEN_IB, OPEN);
		all.put(parent_async_data.CLOSE_IB, CLOSE);
		all.put(parent_async_data.LOW_IB, LOW);
		all.put(parent_async_data.HIGH_IB, HIGH);
		all.put(parent_async_data.ASK_IB, ASK);
		all.put(parent_async_data.BID_IB, BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(parent_async_data.VOLUME_IB, VOLUME);
		all.put(parent_async_data.SIZE_IB, SIZE);
		all.put(parent_async_data.ASK_SIZE_IB, ASK_SIZE);
		all.put(parent_async_data.BID_SIZE_IB, BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(parent_async_data.HALTED_IB, HALTED);
		
		return all;
	}
}