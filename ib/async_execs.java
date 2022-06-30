package ib;

import java.util.HashMap;
import java.util.Hashtable;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.arrays;
import accessory.parent_static;
import db_ib.execs;

public abstract class async_execs extends parent_static 
{	
	private static final int COLS_TOT = 7;
	
	private static boolean _enabled = false; 
	private volatile static Hashtable<String, Hashtable<String, Object>> _all_vals = new Hashtable<String, Hashtable<String, Object>>();
	
	public static void enable() { _enabled = true; }

	public static void disable() { _enabled = false; }
	
	public static boolean is_ok() { return _enabled; }
	
	public static void __wrapper_execDetails(int id_, Contract contract_, Execution execution_)
	{
		if (!_enabled) return;
		
		__lock();
		
		Hashtable<String, Object> vals = new Hashtable<String, Object>();		
		vals.put(execs.SYMBOL, contract_.symbol());
		vals.put(execs.ORDER_ID, execution_.orderId());
		vals.put(execs.PRICE, execution_.price()); 
		vals.put(execs.QUANTITY, execution_.shares()); 
		vals.put(execs.SIDE, execution_.side()); 

		update(execution_.execId(), vals);
		
		__unlock();
	}

	public static void __wrapper_commissionReport(CommissionReport report_)
	{
		if (!_enabled) return;
		
		__lock();
		
		Hashtable<String, Object> vals = new Hashtable<String, Object>();
		vals.put(execs.FEES, report_.commission());
		
		update(report_.execId(), vals);
		
		__unlock();
	}
	
	@SuppressWarnings("unchecked")
	private static void update(String exec_id_, Hashtable<String, Object> vals_)
	{
		Hashtable<String, Object> vals = (_all_vals.containsKey(exec_id_) ? new Hashtable<String, Object>(vals_) : (Hashtable<String, Object>)arrays.add(_all_vals.get(exec_id_), vals_));
		vals.put(execs.EXEC_ID, exec_id_);
		
		_all_vals.put(exec_id_, vals);
		
		update_db(exec_id_);
	}
	
	private static void update_db(String exec_id_)
	{
		if (_all_vals.get(exec_id_).size() < COLS_TOT) return;

		if (!execs.exists(exec_id_))
		{
			HashMap<String, Object> vals = new HashMap<String, Object>(_all_vals.get(exec_id_));
			vals.put(execs.USER, basic.get_user());	
			
			execs.insert(vals);			
		}
		
		_all_vals.remove(exec_id_);
	}
}