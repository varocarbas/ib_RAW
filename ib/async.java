package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

public class async extends parent_static 
{	
	public static final int WRONG_ID = common.MIN_REQ_ID_ASYNC - 1;
	
	static volatile int _last_id = common.MIN_REQ_ID_SYNC;
	static volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC); }

	public static boolean is_ok(int id_) { return arrays.key_exists_async(_ids, id_); }

	public static boolean is_ok(int id_, String type_) { return strings.are_equal(get_type(id_), type_); }
	
	static int add_id(String type_) 
	{ 
		int id = get_req_id();

		lock();
		
		_ids.put(id, type_);
		
		unlock();
		
		return id;
	}
		
	@SuppressWarnings("unchecked")
	static void remove_id(int id_) { _ids = (HashMap<Integer, String>)arrays.remove_key_async(_ids, id_); }

	static String get_type(int id_) { return (String)arrays.get_value_async(_ids, id_); }
	
	static boolean key_exists(Object array_, Object key_) { return arrays.key_exists_async(array_, key_); }
	
	static boolean value_exists(Object array_, Object value_) { return arrays.value_exists_async(array_, value_); }
	
	static Object get_key(Object array_, Object value_) { return arrays.get_key_async(array_, value_); }
	
	static Object get_value(Object array_, Object key_) { return arrays.get_value_async(array_, key_); }
	
	static Object remove_key(Object array_, Object key_) { return arrays.remove_key_async(array_, key_); }
	
	static Object remove_value(Object array_, Object value_) { return arrays.remove_value_async(array_, value_); }
	
	private static int get_req_id() { return common.get_req_id(false); }
}