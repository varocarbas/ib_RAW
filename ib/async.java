package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

public class async extends parent_static 
{	
	public static final int WRONG_ID = common.WRONG_ID;
	
	static volatile int _last_id = WRONG_ID;
	static volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC); }

	public static boolean is_ok(int id_) { return id_exists(_ids, id_); }

	public static boolean is_ok(int id_, String type_) { return strings.are_equal(get_type(id_), type_); }
	
	static int add_id(String type_) 
	{ 
		lock();
		
		int id = get_req_id(false);
		
		_ids.put(id, type_);
		
		unlock();
		
		return id;
	}
		
	static void remove_id(int id_, boolean lock_) { _ids = remove_id(_ids, id_, lock_); }

	static String get_type(int id_) { return get_value(_ids, id_, strings.DEFAULT); }
	
	static <x> boolean id_exists(HashMap<Integer, x> array_, int id_) 
	{ 
		lock();
		
		boolean output = array_.containsKey(id_);

		unlock();

		return output;
	}
	
	static int get_id(HashMap<Integer, String> array_, String value_) 
	{ 
		lock();
		
		int output = WRONG_ID;

		for (Entry<Integer, String> item: array_.entrySet())
		{
			if (item.getValue().equals(value_)) 
			{
				output = item.getKey();
				
				break;
			}
		}
		
		unlock();

		return output;
	}
	
	static <x> HashMap<Integer, x> remove_id(HashMap<Integer, x> array_, int id_, boolean lock_) 
	{ 
		if (lock_) lock();
		
		HashMap<Integer, x> output = new HashMap<Integer, x>(array_);
		output.remove(id_);
		
		if (lock_) unlock();
		
		return output; 
	}
		
	static <x, y> boolean value_exists(HashMap<x, y> array_, y value_) 
	{ 
		lock();
		
		boolean output = array_.containsValue(value_);

		unlock();

		return output;
	}	

	static <x> x get_value(HashMap<Integer, x> array_, int key_, x wrong_) 
	{ 
		lock();
		
		x output = (array_.containsKey(key_) ? array_.get(key_) : wrong_);
		
		unlock();
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	static <x, y> Object get_value(HashMap<Integer, x> array_, int key_, boolean is_xy_) 
	{ 
		lock();
		
		Object output = null;
		
		if (array_.containsKey(key_))
		{
			Object temp = array_.get(key_);
			output = (is_xy_ ? arrays.get_new_hashmap_xy((HashMap<x, y>)temp) : arrays.get_new_hashmap_xx((HashMap<x, x>)temp));			
		}
		
		unlock();

		return output;
	}
	
	private static int get_req_id(boolean lock_) { return common.get_req_id(false, lock_); }
}