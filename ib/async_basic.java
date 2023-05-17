package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import external_ib.calls;

abstract class async_basic 
{
	private static volatile int _id = async.WRONG_ID;
	
	private static volatile ArrayList<String> _keys = new ArrayList<String>();
	private static volatile ArrayList<Double> _vals = new ArrayList<Double>();
	
	public static boolean is_ok(int id_) { return (id_ == _id); }
	
	public static void get_funds()
	{
		if (_id != async.WRONG_ID) return;
		
		_keys = new ArrayList<String>();
		_vals = new ArrayList<Double>();
		
		_id = async.get_req_id();
		
		//Methods called in external_ib.wrapper: accountSummary, accountSummaryEnd.
		calls.reqAccountSummary(_id);
	}
	
	public static void update_funds(String key_, String value_) 
	{ 
		if (!strings.is_number(value_)) return;
		
		_keys.add(key_);
		_vals.add(Double.parseDouble(value_));
	}
	
	public static void account_summary_end(int id_) 
	{ 		
		_id = async.WRONG_ID; 
		
		HashMap<String, Double> funds = sync.get_funds(_keys, _vals);
		if (!arrays.is_ok(funds)) return;
		
		db_ib.basic.update_money_common(funds);
	}
}