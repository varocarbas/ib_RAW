package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory.dates;
import accessory_ib._defaults;
import accessory_ib.errors;
import accessory_ib.types;

public class sync 
{
	public static final String GET_ID = types.SYNC_GET_ID;
	public static final String GET_IDS = types.SYNC_GET_IDS;
	public static final String GET_FUNDS = types.SYNC_GET_FUNDS;

	public static final String DATA_DECIMAL = types.SYNC_DATA_DECIMAL;
	public static final String DATA_INT = types.SYNC_DATA_INT;
	public static final String DATA_INTS = types.SYNC_DATA_INTS;
	public static final String DATA_MISC = types.SYNC_DATA_MISC;

	public static final String ERROR_ID = types.ERROR_IB_SYNC_ID;
	public static final String ERROR_ID2 = types.ERROR_IB_SYNC_ID2;
	public static final String ERROR_TIME = types.ERROR_IB_SYNC_TIME;
	
	public static volatile boolean _retrieving = false;
	public static volatile boolean _retrieved = false;

	public static String _type = strings.DEFAULT;
	public static String _type2 = strings.DEFAULT;
	public static int _id = _defaults.SYNC_ID;

	public static boolean _ignore_max_time = false;

	private static volatile double _decimal_out = numbers.DEFAULT_DECIMAL;
	private static volatile int _int_out = numbers.DEFAULT_INT;
	private static volatile ArrayList<Integer> _ints_out = new ArrayList<Integer>();
	private static volatile HashMap<String, String> _misc_out = new HashMap<String, String>();

	private static final int MAX_SECS_RETRIEVE = 10;

	public static double get_funds() { return (double)get(GET_FUNDS); }

	public static Integer[] get_open_ids() { return (Integer[])get(GET_IDS); }

	public static boolean update(String val_)
	{
		return update(strings.to_number_decimal(val_));
	}

	public static boolean update(double val_)
	{
		if (!_type2.equals(DATA_DECIMAL)) return false;

		_decimal_out = val_;

		return true;
	}

	public static boolean update(int val_)
	{
		boolean is_ok = true;

		if (_type2.equals(DATA_INTS)) _ints_out.add(val_);
		else if (_type2.equals(DATA_INT)) _int_out = val_;
		else is_ok = false;

		return is_ok;
	}

	public static boolean update(String key_, String val_)
	{
		if (!_type2.equals(DATA_MISC) || !strings.is_ok(key_)) return false;

		_misc_out.put(key_, val_);

		return true;
	}

	public static String check(String type_, boolean is_data_) { return accessory.types.check_type(type_, (is_data_ ? types.SYNC_DATA : types.SYNC)); }
	
	public static String check_error(String type_) { return accessory.types.check_type(type_, types.ERROR_IB_SYNC); }

	public static String get_error_message(String type_)
	{
		String message = strings.DEFAULT;
		
		String type = check_error(type_);
		if (!strings.is_ok(type)) return message;
		
		if (type.equals(ERROR_ID)) message = "Wrong sync ID";
		else if (type.equals(ERROR_ID2)) message = "Wrong sync ID2";
		else if (type.equals(ERROR_TIME)) message = "Sync call timed out";

		return message;	
	}
	
	//Only called when creating a new order, via the corresponding order_info constructor.
	static int get_next_id() { return (int)get(GET_ID); }

	private static Object get(String type_) { return get(type_, _defaults.SYNC_ID); }

	private static Object get(String type_, int order_id_)
	{
		get_ini();
		retrieve(type_, order_id_); 

		return get_out();
	}

	private static void get_ini() { get_ini_out(true); }

	private static Object get_out() { return get_ini_out(false); }

	private static Object get_ini_out(boolean ini_)
	{
		Object output = null;

		if (_type2.equals(DATA_INT)) 
		{
			if (ini_) _int_out = numbers.DEFAULT_INT;
			else output = _int_out;
		}
		else if (_type2.equals(DATA_DECIMAL)) 
		{
			if (ini_) _decimal_out = numbers.DEFAULT_DECIMAL;
			else output = _decimal_out;
		}
		else if (_type2.equals(DATA_INTS)) 
		{
			if (ini_) _ints_out = new ArrayList<Integer>();
			else output = arrays.to_array(_ints_out);
		}
		else if (_type2.equals(DATA_MISC)) 
		{
			if (ini_) _misc_out = new HashMap<String, String>();
			else output = new HashMap<String, String>(_misc_out);
		}

		return output;
	}

	private static String get_data_type(String input_)
	{
		String output = strings.DEFAULT;
		if (!strings.is_ok(input_)) return output;

		if (input_.equals(GET_ID)) output = DATA_INT;
		else if (input_.equals(GET_FUNDS)) output = DATA_DECIMAL;
		else if (input_.equals(GET_IDS)) output = DATA_INTS;

		return output;
	}

	private static boolean retrieve(String type_, int id_)
	{
		if (!retrieve_is_ok(type_, id_)) return false;

		_retrieving = true;
		_retrieved = false;

		if (_type.equals(GET_FUNDS))
		{	
			//accountSummary, accountSummaryEnd
			conn._client.reqAccountSummary(_id, "All", "AvailableFunds"); 
		}
		else if (_type.equals(GET_IDS))
		{	
			//openOrder, openOrderEnd, orderStatus
			conn._client.reqAllOpenOrders(); 
		}
		else if (_type.equals(GET_ID))
		{	
			//nextValidId
			conn._client.reqIds(-1); 
		}
		else return false;

		return retrieving();
	}

	private static boolean retrieve_is_ok(String type_, int id_)
	{
		String temp = check(type_, false);
		if (!strings.is_ok(temp)) 
		{
			accessory_ib.errors.manage(ERROR_ID);

			return false;
		}

		String temp2 = get_data_type(temp);
		if (!strings.is_ok(check(temp2, true))) 
		{
			accessory_ib.errors.manage(ERROR_ID2);

			return false;
		}

		_id = id_;
		_type = temp;
		_type2 = temp2;
		_retrieved = false;

		return true;
	}

	private static boolean retrieving()
	{
		boolean is_ok = true;

		long start = dates.get_elapsed(0);

		while (true)
		{
			if (_retrieved) break;

			if (!_ignore_max_time && dates.get_elapsed(start) >= MAX_SECS_RETRIEVE) 
			{
				errors.manage(ERROR_TIME);
				is_ok = false;

				break;
			}

			misc.pause_loop();
		}

		_ignore_max_time = false;
		_retrieving = false;

		return is_ok;
	}
}