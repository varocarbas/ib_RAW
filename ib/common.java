package ib;

import java.util.HashMap;

import accessory.strings;
import accessory.arrays;
import accessory.dates;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib.types;

public class common extends parent_static 
{	
	static final int MIN_REQ_ID_SYNC = 1;
	static final int MAX_REQ_ID_SYNC = 495; //There has to be a gap to account for scenarios of acceptable invalid sync IDs like the ones which sync_orders.get_id_sec() might return.
	static final int MIN_REQ_ID_ASYNC = 500;
	static final int MAX_REQ_ID_ASYNC = 10000;

	public static final int WRONG_ID = MIN_REQ_ID_SYNC - 1;
	
	private static final long DEFAULT_WAIT_SECS_SYNC = 60;
	private static final long DEFAULT_WAIT_SECS_ASYNC = 60;
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_COMMON); }

	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String normalise_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}

	public static boolean req_id_is_ok(int id_) { return id_is_ok(id_, MIN_REQ_ID_SYNC, MAX_REQ_ID_ASYNC); }
	
	public static boolean req_id_is_ok_sync(int id_) { return id_is_ok(id_, MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC); }
	
	public static boolean req_id_is_ok_async(int id_) { return id_is_ok(id_, MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC); }

	static HashMap<Integer, Long> start_wait(int id_, HashMap<Integer, Long> all_)
	{
		HashMap<Integer, Long> output = arrays.get_new_hashmap_xy(all_);
		
		if (!output.containsKey(id_)) output.put(id_, dates.start_elapsed());
		else output = null;
		
		return output;
	}

	static HashMap<Integer, Long> wait_is_over_sync(int id_, HashMap<Integer, Long> all_) { return wait_is_over(id_, DEFAULT_WAIT_SECS_SYNC, all_); }

	static HashMap<Integer, Long> wait_is_over_async(int id_, HashMap<Integer, Long> all_) { return wait_is_over(id_, DEFAULT_WAIT_SECS_ASYNC, all_); }
	
	static HashMap<Integer, Long> wait_is_over(int id_, long secs_, HashMap<Integer, Long> all_)
	{	
		HashMap<Integer, Long> output = arrays.get_new_hashmap_xy(all_);
		
		if (!output.containsKey(id_)) return output;
	
		boolean is_over = (dates.get_elapsed(output.get(id_)) > secs_);	

		if (is_over) output.remove(id_);
		else output = null;
	
		return output;
	}
		
	static int get_req_id(boolean is_sync_) { return get_req_id(is_sync_, true); }
	
	static int get_req_id(boolean is_sync_, boolean lock_) 
	{ 
		if (lock_) lock();
		
		int id = 0;
				
		if (is_sync_)
		{
			id = get_req_id_internal(sync._id, MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC);
			sync._id = id;
		}
		else
		{
			id = get_req_id_internal(async._last_id, MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC);
			async._last_id = id;		
		}
		
		if (lock_) unlock();
		
		return id;
	}

	private static int get_req_id_internal(int last_, int min_, int max_) 
	{ 
		int id = last_ + 1;

		return (id_is_ok(id, min_, max_) ? id : min_);
	}
	
	private static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
}