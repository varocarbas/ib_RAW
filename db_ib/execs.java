package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.db;
import accessory.db_common;
import accessory.strings;
import ib._order;

public abstract class execs 
{
	public static final String SOURCE = common.SOURCE_EXECS;
	public static final String SOURCE_OLD = common.SOURCE_EXECS_OLD;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String EXEC_ID = common.FIELD_EXEC_ID;
	public static final String ORDER_ID = common.FIELD_ORDER_ID;
	public static final String SIDE = common.FIELD_SIDE;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String FEES = common.FIELD_FEES;

	static String[] _fields = null;
	static String[] _cols = null;
	static HashMap<String, String> _fields_cols = null;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	private static String[] _fields_basic_info = null;

	public static void __truncate() { __truncate(false); }
	
	public static void __truncate(boolean only_if_not_active_) { if (!only_if_not_active_ || !contains_active()) common.__truncate(SOURCE); }
	
	public static void __backup() { common.__backup(SOURCE); }	

	public static boolean exists(String exec_id_) { return common.exists(SOURCE, get_where(exec_id_)); }

	public static boolean order_id_exists(int order_id_, boolean is_main_) { return common.exists(SOURCE, get_where_order_id(order_id_, is_main_)); }

	public static ArrayList<Integer> get_order_ids_filled(String symbol_, boolean is_quick_) { return get_order_ids_common(symbol_, true, is_quick_); }

	public static ArrayList<Integer> get_order_ids_completed(String symbol_, boolean is_quick_) { return get_order_ids_common(symbol_, false, is_quick_); }

	public static boolean is_filled(int order_id_main_) { return is_filled(order_id_main_, true); }

	public static boolean is_completed(int order_id_main_) { return is_completed(order_id_main_, true); }

	public static ArrayList<Integer> get_all_order_ids_main(boolean is_quick_) { return get_all_order_ids_main(null, is_quick_); }

	public static ArrayList<Integer> get_all_order_ids_main(String symbol_, boolean is_quick_) 
	{ 
		String where = get_where_side(true);
		if (strings.is_ok(symbol_)) where = common.join_wheres(where, common.get_where_symbol(SOURCE, symbol_));
		
		return common.get_all_ints(SOURCE, ORDER_ID, where); 
	}

	public static boolean contains_active() 
	{
		if (common.contains_active(SOURCE)) return true;
		
		ArrayList<Integer> ids_main = get_all_order_ids_main();
		if (!arrays.is_ok(ids_main)) return false;
	
		for (int id_main: ids_main)
		{
			if (is_filled(id_main)) return true;
		}	
		
		return false; 
	}

	public static ArrayList<HashMap<String, String>> get_all_filled(boolean is_quick_) { return get_all_filled(null, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_filled(String symbol_, boolean is_quick_) { return get_all_common(symbol_, true, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_completed(boolean is_quick_) { return get_all_completed(null, is_quick_); }

	public static ArrayList<HashMap<String, String>> get_all_completed(String symbol_, boolean is_quick_) { return get_all_common(symbol_, false, is_quick_); }
	
	public static boolean update(String exec_id_, HashMap<String, Object> vals_) 
	{
		if (!arrays.is_ok(vals_)) return false;
		
		Object vals = null;
		
		if (common.is_quick(SOURCE))
		{
			HashMap<String, String> temp = new HashMap<String, String>();	
			for (Entry<String, Object> item: vals_.entrySet()) { temp.put(common.get_col(SOURCE, item.getKey()), db.adapt_input(item.getValue())); }
		
			vals = new HashMap<String, String>(temp);
		}
		else vals = new HashMap<String, Object>(vals_);

		return common.insert_update(SOURCE, vals, get_where(exec_id_)); 
	}
	
	public static double get_fees(int order_id_) { return common.get_decimal(SOURCE, FEES, get_where_order_id_any(order_id_)); }

	public static double get_quantity(int order_id_) { return common.get_decimal(SOURCE, QUANTITY, get_where_order_id_any(order_id_), ib.common.WRONG_QUANTITY); }

	public static double get_start_price(int order_id_buy_) { return common.get_decimal(SOURCE, PRICE, common.join_wheres(get_where_order_id_any(order_id_buy_), get_where_side(true)), ib.common.WRONG_PRICE); }
	
	public static double get_end_price(int order_id_sell_) { return common.get_decimal(SOURCE, PRICE, common.join_wheres(get_where_order_id_any(order_id_sell_), get_where_side(false)), ib.common.WRONG_PRICE); }
	
	public static String get_symbol(int order_id_) { return common.get_string(SOURCE, SYMBOL, get_where_order_id_any(order_id_)); }
	
	public static ArrayList<HashMap<String, String>> get_basic_info(int order_id_) { return common.get_all_vals(SOURCE, get_fields_basic_info(), get_where_order_id_any(order_id_)); }

	public static ArrayList<HashMap<String, String>> get_order_ids(String symbol_) { return common.get_all_vals(SOURCE, new String[] { db.FIELD_TIMESTAMP, ORDER_ID, SIDE }, common.get_where_symbol(SOURCE, symbol_)); }
	
	public static Object get_val(String field_, HashMap<String, String> vals_, boolean is_quick_)
	{
		Object val = null;
		
		String val0 = vals_.get(common.get_field_col(SOURCE, field_));
		
		if (field_is_int(field_)) val = Integer.parseInt(val0);
		else if (field_is_decimal(field_)) val = Double.parseDouble(val0);
		else val = val0;
		
		return val;
	}

	static void populate_fields() { _fields = db_common.add_default_fields(SOURCE, new String[] { SYMBOL, EXEC_ID, ORDER_ID, SIDE, PRICE, QUANTITY, FEES }); }
	
	private static boolean field_is_decimal(String field_) { return (field_.equals(PRICE) || field_.equals(FEES) || field_.equals(QUANTITY)); }
	
	private static boolean field_is_int(String field_) { return (field_.equals(ORDER_ID)); }
		
	private static ArrayList<Integer> get_all_order_ids_main() { return get_all_order_ids_main(false); }

	private static ArrayList<HashMap<String, String>> get_all_common(String symbol_, boolean is_filled_, boolean is_quick_)
	{
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		
		ArrayList<Integer> ids_main = get_all_order_ids_main(symbol_, is_quick_);
		if (!arrays.is_ok(ids_main)) return output;
		
		String[] fields = new String[] { SYMBOL, ORDER_ID };
		
		for (int id_main: ids_main)
		{
			if (is_filled_ != is_filled(id_main)) continue;

			HashMap<String, String> vals = common.get_vals(SOURCE, fields, get_where_order_id_any(id_main));
			if (!arrays.is_ok(vals)) continue;
			
			output.add(vals);
		}	
		
		return output; 		
	}
	
	private static String[] get_fields_basic_info() 
	{ 
		if (_fields_basic_info == null) populate_fields_basic_info();
		
		return _fields_basic_info;  
	}

	private static void populate_fields_basic_info() { _fields_basic_info = new String[] { SYMBOL, PRICE, QUANTITY, FEES }; }

	private static boolean is_filled(int order_id_main_, boolean check_exists_) { return is_common(order_id_main_, check_exists_, true); }

	private static boolean is_completed(int order_id_main_, boolean check_exists_) { return is_common(order_id_main_, check_exists_, false); }

	private static boolean is_common(int order_id_main_, boolean check_exists_, boolean is_filled_) 
	{ 
		if (check_exists_ && !order_id_exists(order_id_main_, true)) return false;
		
		boolean exists = order_id_exists(_order.get_id_sec(order_id_main_), false);
		
		return ((is_filled_ && !exists) || (!is_filled_ && exists));
	}

	private static ArrayList<Integer> get_order_ids_common(String symbol_, boolean is_filled_, boolean is_quick_) 
	{ 
		ArrayList<Integer> output = new ArrayList<Integer>();

		ArrayList<Integer> all = get_all_order_ids_main(symbol_, is_quick_);
		if (!arrays.is_ok(all)) return output;
		
		for (int id_main: all)
		{
			boolean sec_exists = order_id_exists(_order.get_id_sec(id_main), false); 
			if ((is_filled_ && !sec_exists) || (!is_filled_ && sec_exists)) output.add(id_main);
		}
		
		return output; 
	}

	private static String get_where(String exec_id_) { return common.get_where(SOURCE, EXEC_ID, exec_id_); }

	private static String get_where_order_id(int order_id_, boolean is_main_) { return common.join_wheres(get_where_order_id_any(order_id_), get_where_side(is_main_)); }
	
	private static String get_where_order_id_any(int order_id_) { return common.get_where(SOURCE, ORDER_ID, Integer.toString(order_id_)); }
	
	private static String get_where_side(boolean is_main_) { return get_where_side(ib.execs.get_side(is_main_)); }
	
	private static String get_where_side(String side_) { return common.get_where(SOURCE, SIDE, side_); }
}