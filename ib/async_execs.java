package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.parent_static;
import accessory.strings;
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
		vals.put(PRICE, db_ib.common.adapt_price(execution_.price())); 
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
		vals.put(FEES, db_ib.common.adapt_money(report_.commission()));
		
		update(report_.execId(), vals);
		
		__unlock();
	}

	private static void update(String exec_id_, HashMap<String, Object> vals_)
	{		
		HashMap<String, Object> vals = new HashMap<String, Object>(vals_);
		vals.put(EXEC_ID, exec_id_);
		
		if (_all_vals.containsKey(exec_id_))
		{
			for (Entry<String, Object> item: _all_vals.get(exec_id_).entrySet()) { vals.put(item.getKey(), item.getValue()); }
		}

		if (vals.size() == TARGET_TOT_FIELDS) 
		{
			__update_last(exec_id_, vals);
			
			if (_all_vals.containsKey(exec_id_)) _all_vals.remove(exec_id_);
		}
		else _all_vals.put(exec_id_, vals);
	}
	
	private static void __update_last(String exec_id_, HashMap<String, Object> vals_)
	{				
		if (trades.triggered_automatically())
		{
			int order_id = (int)vals_.get(ORDER_ID);
			String side = (String)vals_.get(SIDE);
			double price = (double)vals_.get(PRICE);
			
			if (strings.are_equivalent(side, SIDE_SOLD)) trades.__end(order_id, price);
			else trades.start(order_id, price);	
		}

		db_ib.execs.update(exec_id_, vals_);
	}
}