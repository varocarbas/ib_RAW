package ib;

import java.util.HashMap;

import accessory.strings;
import accessory._defaults;
import accessory.arrays;
import accessory.dates;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib._ini;
import accessory_ib.types;

public class common extends parent_static 
{	
	public static final String USER = get_user();
	public static final int MAX_LENGTH_USER = 15;

	public static final int WRONG_ID = 0;
	
	static final int MIN_REQ_ID_SYNC = WRONG_ID + 1;
	static final int MAX_REQ_ID_SYNC = MIN_REQ_ID_SYNC + 10; 
	static final int MIN_REQ_ID_ASYNC = MAX_REQ_ID_SYNC + 1;
	static final int MAX_REQ_ID_ASYNC = MIN_REQ_ID_ASYNC + 2500;
	
	private static final long DEFAULT_WAIT_SECS_SYNC = 60;
	private static final long DEFAULT_WAIT_SECS_ASYNC = 60;
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_COMMON); }

	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String check_symbol(String symbol_)
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

	static int get_req_id(boolean is_sync_) 
	{ 
		int id = 0;
				
		if (is_sync_)
		{
			id = get_req_id_internal(sync.get_req_id(), MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC);
			sync.update_req_id(id);
		}
		else
		{
			id = get_req_id_internal(async.get_last_id(), MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC);
			async.update_last_id(id);		
		}
		
		return id;
	}

	private static int get_req_id_internal(int last_, int min_, int max_) 
	{ 
		int id = last_ + 1;

		return (id_is_ok(id, min_, max_) ? id : min_);
	}
	
	private static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
	
	private static String get_user()
	{
		String user = _ini.get_user();
		if (!strings.is_ok(user)) user = _defaults.USER;
		
		return strings.truncate(user, MAX_LENGTH_USER);
	}
}