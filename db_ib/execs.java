package db_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.db;
import accessory.db_where;
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

	private static String[] _fields = null;
	private static String[] _fields_basic_info = null;
	
	private static HashMap<String, String> _cols = new HashMap<String, String>();

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
		if (strings.is_ok(symbol_)) where = common.join_wheres(where, get_where_symbol(symbol_, is_quick_));
		
		return common.get_all_ints(SOURCE, get_field_col(ORDER_ID, is_quick_), where, is_quick_); 
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
	
	public static boolean update(String exec_id_, HashMap<String, Object> vals_, boolean is_quick_) 
	{
		if (!arrays.is_ok(vals_)) return false;
		
		Object vals = null;
		
		if (is_quick_)
		{
			HashMap<String, String> temp = new HashMap<String, String>();	
			for (Entry<String, Object> item: vals_.entrySet()) { temp.put(get_col(item.getKey()), db.adapt_input(item.getValue())); }
		
			vals = temp;
		}
		else vals = new HashMap<String, Object>(vals_);

		return common.insert_update(SOURCE, vals, get_where(exec_id_, is_quick_), is_quick_); 
	}
	
	public static double get_fees(int order_id_) 
	{ 
		double output = common.get_decimal(SOURCE, FEES, get_where_order_id_any(order_id_)); 
	
		return (output == db.WRONG_DECIMAL ? 0.0 : output);
	}

	public static double get_quantity(int order_id_) 
	{ 
		double output = common.get_decimal(SOURCE, QUANTITY, get_where_order_id_any(order_id_)); 
	
		return (output == db.WRONG_DECIMAL ? 0.0 : output);
	}

	public static double get_start_price(int order_id_buy_) 
	{ 
		double output = common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id_any(order_id_buy_), get_where_side(true), db_where.LINK_AND)); 
	
		return (output == db.WRONG_DECIMAL ? ib.common.WRONG_PRICE : output);
	}
	
	public static double get_end_price(int order_id_sell_) 
	{ 
		double output = common.get_decimal(SOURCE, PRICE, db_where.join(get_where_order_id_any(order_id_sell_), get_where_side(false), db_where.LINK_AND)); 
	
		return (output == db.WRONG_DECIMAL ? ib.common.WRONG_PRICE : output);
	}
	
	public static String get_symbol(int order_id_) 
	{ 
		String output = common.get_string(SOURCE, SYMBOL, get_where_order_id_any(order_id_)); 
	
		return (strings.are_equal(output, db.WRONG_STRING) ? strings.DEFAULT : output);
	}
	
	public static ArrayList<HashMap<String, String>> get_basic_info(int order_id_) { return common.get_all_vals(SOURCE, get_fields_basic_info(), get_where_order_id_any(order_id_)); }

	public static ArrayList<HashMap<String, String>> get_order_ids(String symbol_) { return common.get_all_vals(SOURCE, new String[] { db.FIELD_TIMESTAMP, ORDER_ID, SIDE }, get_where_symbol(symbol_)); }
	
	private static ArrayList<Integer> get_all_order_ids_main() { return get_all_order_ids_main(false); }

	private static ArrayList<HashMap<String, String>> get_all_common(String symbol_, boolean is_filled_, boolean is_quick_)
	{
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		
		ArrayList<Integer> ids_main = get_all_order_ids_main(symbol_, is_quick_);
		if (!arrays.is_ok(ids_main)) return output;
		
		String[] fields = new String[] { SYMBOL, ORDER_ID };
		String[] cols = new String[] { get_col(SYMBOL), get_col(ORDER_ID)};

		int tot = fields.length;
		
		for (int id_main: ids_main)
		{
			boolean filled = is_filled(id_main);			
			if ((is_filled_ && !filled) || (!is_filled_ && filled)) continue;

			HashMap<String, String> temp = common.get_vals(SOURCE, (is_quick_ ? cols : fields), get_where_order_id_any(id_main, is_quick_), is_quick_);
			if (!arrays.is_ok(temp)) continue;
			
			HashMap<String, String> vals = null;
			if (is_quick_) 
			{
				vals = new HashMap<String, String>();
				
				for (int i = 0; i < tot; i++) { vals.put(fields[i], temp.get(cols[i])); }
			}
			else vals = new HashMap<String, String>(temp);
			
			output.add(vals);
		}	
		
		return output; 		
	}

	private static String get_col(String field_) 
	{
		if (_cols.size() == 0) populate_cols();
		
		return _cols.get(field_);
	}

	private static String get_field_col(String field_, boolean is_quick_) { return (is_quick_ ? get_col(field_) : field_); }
	
	private static void populate_cols()
	{
		for (String field: get_fields()) { _cols.put(field, common.get_col(SOURCE, field)); }
	}	
	
	private static String[] get_fields() 
	{ 
		if (_fields == null) populate_fields();
		
		return _fields;  
	}

	private static void populate_fields() { _fields = new String[] { SYMBOL, EXEC_ID, ORDER_ID, SIDE, PRICE, QUANTITY, FEES }; }
	
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

	private static String get_where(String exec_id_) { return get_where(exec_id_, false); }

	private static String get_where(String exec_id_, boolean is_quick_) { return common.get_where(SOURCE, EXEC_ID, exec_id_); }

	private static String get_where_order_id(int order_id_, boolean is_main_) { return get_where_order_id(order_id_, is_main_, false); }

	private static String get_where_order_id(int order_id_, boolean is_main_, boolean is_quick_) { return common.join_wheres(get_where_order_id_any(order_id_, is_quick_), get_where_side(is_main_, is_quick_)); }
	
	private static String get_where_order_id_any(int order_id_) { return get_where_order_id_any(order_id_, false); }
	
	private static String get_where_order_id_any(int order_id_, boolean is_quick_) { return common.get_where(SOURCE, ORDER_ID, Integer.toString(order_id_)); }
	
	private static String get_where_side(boolean is_main_) { return get_where_side(is_main_, false); }
	
	private static String get_where_side(boolean is_main_, boolean is_quick_) { return get_where_side(ib.execs.get_side(is_main_), is_quick_); }
	
	private static String get_where_side(String side_, boolean is_quick_) { return common.get_where(SOURCE, SIDE, side_); }

	private static String get_where_symbol(String symbol_) { return get_where_symbol(symbol_, false); }

	private static String get_where_symbol(String symbol_, boolean is_quick_) { return common.get_where_symbol(SOURCE, symbol_); }
}