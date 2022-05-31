package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

public class async extends parent_static 
{	
	static final int MIN_REQ_ID = common.MIN_REQ_ID_ASYNC;
	static final int MAX_REQ_ID = common.MAX_REQ_ID_ASYNC;
	
	static int _last_id = MIN_REQ_ID;
	
	static HashMap<Integer, String> _ids = new HashMap<Integer, String>();

	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC); }

	static int add(String type_) 
	{ 
		int id = common.get_req_id(false);

		_ids.put(id, type_);
		
		return id;
	}
	
	public static String get_type(int id_) { return (String)arrays.get_value(_ids, id_); }

	public static boolean is_ok(int id_) { return arrays.key_exists(_ids, id_); }

	public static boolean is_ok(int id_, String type_) { return strings.are_equal((String)arrays.get_value(_ids, id_), type_); }
		
	@SuppressWarnings("unchecked")
	public static void remove(int id_) 
	{ 
		_ids = (HashMap<Integer, String>)arrays.remove_key(_ids, id_);
	}
}