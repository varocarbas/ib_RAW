package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib.types;

public class async extends parent_static 
{	
	public static volatile ArrayList<Integer> _retrieving = new ArrayList<Integer>();
	public static volatile HashMap<Integer, String> _types = new HashMap<Integer, String>();

	public static int MIN_ID = 1;
	public static int MAX_ID = 2500;

	public static String get_id() { return accessory.types.get_id(types.ID_ASYNC); }
	
	public static String get_type(int id_) { return (String)arrays.get_value(_types, id_); }

	public static boolean id_is_ok(int id_) { return numbers.is_ok(id_, MIN_ID, MAX_ID); }
}