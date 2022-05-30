package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.types;

public class async extends parent_static 
{	
	private static volatile ArrayList<Integer> _ids = new ArrayList<Integer>();
	private static volatile HashMap<Integer, String> _types = new HashMap<Integer, String>();

	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC); }
	
	public static String get_type(int id_) { return (String)arrays.get_value(_types, id_); }

	public static boolean is_ok(int id_, String type_) { return (id_is_ok(id_) && type_is_ok(id_, type_)); }
	
	public static boolean id_is_ok(int id_) { return _ids.contains(id_); }
	
	public static boolean type_is_ok(int id_, String type_) { return strings.are_equal((String)arrays.get_value(_types, id_), type_); }
	
	public static boolean add(int id_, String type_) 
	{ 
		if (!common.id_is_ok(id_) || !strings.is_ok(type_)) return false;
		
		_ids.add(id_);
		_types.put(id_, type_);
		
		return true; 
	}

	@SuppressWarnings("unchecked")
	public static void remove(int id_) 
	{ 
		_ids = (ArrayList<Integer>)arrays.remove_value(_ids, id_); 
		_types = (HashMap<Integer, String>)arrays.remove_key(_types, id_);
	}
}