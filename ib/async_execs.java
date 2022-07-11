package ib;

import java.util.HashMap;
import java.util.Hashtable;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.arrays;
import accessory.parent_static;
import db_ib.execs;
import external_ib.orders;

public abstract class async_execs extends parent_static 
{
	public static final String USER = execs.USER;
	public static final String SYMBOL = execs.SYMBOL;
	public static final String ORDER_ID = execs.ORDER_ID;
	public static final String PRICE = execs.PRICE;
	public static final String QUANTITY = execs.QUANTITY;
	public static final String SIDE = execs.SIDE;
	public static final String FEES = execs.FEES;
	public static final String EXEC_ID = execs.EXEC_ID;
	
	public static final int TARGET_TOT_FIELDS = 7;

	public static final String SIDE_BOUGHT = orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = orders.EXEC_SIDE_SOLD;
	
	private static volatile boolean _enabled = false; 
	private static volatile Hashtable<String, Hashtable<String, Object>> _all_vals = new Hashtable<String, Hashtable<String, Object>>();

	public static void enable() { _enabled = true; }

	public static void disable() { _enabled = false; }
	
	public static boolean is_ok() { return _enabled; }
	
	static void __exec_details(int id_, Contract contract_, Execution execution_) 
	{
		if (!_enabled) return;
		
		__lock();
		
		Hashtable<String, Object> vals = new Hashtable<String, Object>();		
		vals.put(SYMBOL, contract_.symbol());
		vals.put(ORDER_ID, execution_.orderId());
		vals.put(PRICE, execution_.price()); 
		vals.put(QUANTITY, execution_.shares()); 
		vals.put(SIDE, execution_.side()); 

		update(execution_.execId(), vals);
		
		__unlock();
	}

	public static void __commission_report(CommissionReport report_)
	{
		if (!_enabled) return;
		
		__lock();
		
		Hashtable<String, Object> vals = new Hashtable<String, Object>();
		vals.put(FEES, report_.commission());
		
		update(report_.execId(), vals);
		
		__unlock();
	}

	@SuppressWarnings("unchecked")
	private static void update(String exec_id_, Hashtable<String, Object> vals_)
	{
		Hashtable<String, Object> vals = (_all_vals.containsKey(exec_id_) ? (Hashtable<String, Object>)arrays.add(_all_vals.get(exec_id_), vals_) : (Hashtable<String, Object>)arrays.get_new(vals_));
		vals.put(EXEC_ID, exec_id_);
		
		_all_vals.put(exec_id_, vals);
		
		update_last(exec_id_);
	}
	
	private static void update_last(String exec_id_)
	{
		if (_all_vals.get(exec_id_).size() < TARGET_TOT_FIELDS) return;
		
		if (!execs.exists(exec_id_))
		{	
			HashMap<String, Object> vals = new HashMap<String, Object>(_all_vals.get(exec_id_));
		
			int order_id = (int)vals.get(ORDER_ID);
			String side = (String)vals.get(SIDE);
			
			if (side.equals(SIDE_BOUGHT)) async_trades._start(order_id, false);
			else if (side.equals(SIDE_SOLD)) async_trades._end(order_id, false);

			execs.insert(vals);			
		}
		
		_all_vals.remove(exec_id_);
	}
}