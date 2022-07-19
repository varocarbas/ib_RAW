package ib;

import java.util.HashMap;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.arrays;
import accessory.parent_static;
import external_ib.orders;

abstract class async_execs extends parent_static 
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
	
	public static volatile boolean _enabled = false; 
	
	private static volatile HashMap<String, HashMap<String, Object>> _all_vals = new HashMap<String, HashMap<String, Object>>();
	
	public static boolean is_ok() { return _enabled; }
	
	public static void __exec_details(int id_, Contract contract_, Execution execution_) 
	{
		if (!is_ok()) return;
		
		__lock();
		
		HashMap<String, Object> vals = new HashMap<String, Object>();		
		vals.put(SYMBOL, contract_.localSymbol());
		vals.put(ORDER_ID, execution_.orderId());
		vals.put(PRICE, adapt_price(execution_.price())); 
		vals.put(QUANTITY, execution_.shares()); 
		vals.put(SIDE, execution_.side()); 

		update(execution_.execId(), vals);
		
		__unlock();
	}
	
	public static void __commission_report(CommissionReport report_)
	{
		if (!is_ok()) return;

		__lock();
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(FEES, report_.commission());
		
		update(report_.execId(), vals);
		
		__unlock();
	}
	
	private static double adapt_price(double price_) { return db_ib.common.adapt_number(price_, db_ib.common.FIELD_PRICE); }

	@SuppressWarnings("unchecked")
	private static void update(String exec_id_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = (_all_vals.containsKey(exec_id_) ? (HashMap<String, Object>)arrays.add(_all_vals.get(exec_id_), vals_) : (HashMap<String, Object>)arrays.get_new(vals_));
		vals.put(EXEC_ID, exec_id_);

		_all_vals.put(exec_id_, vals);

		update_last(exec_id_);
	}
	
	private static void update_last(String exec_id_)
	{
		if (_all_vals.get(exec_id_).size() < TARGET_TOT_FIELDS) return;

		HashMap<String, Object> vals = new HashMap<String, Object>(_all_vals.get(exec_id_));
		
		int order_id = (int)vals.get(ORDER_ID);
		String side = (String)vals.get(SIDE);
		double price = (double)vals.get(PRICE);
		
		if (side.equals(SIDE_SOLD)) trades._end(order_id, price, false);
		else trades._start(order_id, price, false);

		db_ib.execs.update(exec_id_, vals);
		
		_all_vals.remove(exec_id_);
	}
}