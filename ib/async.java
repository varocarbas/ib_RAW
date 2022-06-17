package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

public class async extends parent_static 
{	
	public static final int WRONG_ID = common.WRONG_ID;
	
	private static volatile HashMap<Integer, String> _ids = new HashMap<Integer, String>();
	private static volatile int _last_id = WRONG_ID;

	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC); }

	public static HashMap<Integer, String> get_ids() { return (new HashMap<Integer, String>(_ids)); }

	public static int get_last_id() { return _last_id; }

	public static void update_last_id(int id_) { _last_id = id_; }
	
	public static boolean is_ok(int id_) { return arrays.key_exists(_ids, id_); }

	public static boolean is_ok(int id_, String type_) { return strings.are_equal(get_type(id_), type_); }
	
	static int add_id(String type_) 
	{ 
		int id = get_req_id();
		
		_ids.put(id, type_);
		
		return id;
	}
		
	@SuppressWarnings("unchecked")
	static void remove_id(int id_) { _ids = (HashMap<Integer, String>)arrays.remove_key(_ids, id_); }

	static String get_type(int id_) { return (String)arrays.get_value(_ids, id_); }

	private static int get_req_id() { return common.get_req_id(false); }
}