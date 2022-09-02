package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.parent_static;

abstract class async_execs extends parent_static 
{
	public static final String SYMBOL = db_ib.execs.SYMBOL;
	public static final String ORDER_ID = db_ib.execs.ORDER_ID;
	public static final String PRICE = db_ib.execs.PRICE;
	public static final String QUANTITY = db_ib.execs.QUANTITY;
	public static final String SIDE = db_ib.execs.SIDE;
	public static final String FEES = db_ib.execs.FEES;
	public static final String EXEC_ID = db_ib.execs.EXEC_ID;
	
	public static final int TARGET_TOT_FIELDS = 7;

	public static final String SIDE_BOUGHT = external_ib.orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = external_ib.orders.EXEC_SIDE_SOLD;
	
	public static volatile boolean _enabled = false; 
	
	private static volatile HashMap<String, HashMap<String, Object>> _all_vals = new HashMap<String, HashMap<String, Object>>();
	
	public static boolean is_ok() { return _enabled; }
	
	public static void __exec_details(int id_, Contract contract_, Execution execution_) 
	{
		__lock();
		
		if (!is_ok()) 
		{
			__unlock();
			
			return;
		}
		
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
		__lock();
		
		if (!is_ok()) 
		{
			__unlock();
			
			return;
		}
		
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(FEES, db_ib.common.adapt_money(report_.commission()));
				
		update(report_.execId(), vals);
	
		__unlock();
	}	
	
	private static void update(String exec_id_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(vals_);
		vals.put(EXEC_ID, exec_id_);
		
		HashMap<String, HashMap<String, Object>> all_vals = new HashMap<String, HashMap<String, Object>>(_all_vals);
		
		if (all_vals.containsKey(exec_id_))
		{
			for (Entry<String, Object> item: all_vals.get(exec_id_).entrySet()) { vals.put(item.getKey(), item.getValue()); }
		}

		if (vals.size() == TARGET_TOT_FIELDS) 
		{
			db_ib.execs.update(exec_id_, vals, ib.execs.is_ok());
			
			update_others(vals);
		
			if (_all_vals.containsKey(exec_id_)) _all_vals.remove(exec_id_);
		} 
		else _all_vals.put(exec_id_, vals);
	}
	
	private static void update_others(HashMap<String, Object> vals_)
	{		
		String symbol = (String)vals_.get(SYMBOL);	
		int order_id = (int)vals_.get(ORDER_ID);
		double price = (double)vals_.get(PRICE);
		
		if (execs.side_is_main((String)vals_.get(SIDE))) 
		{
			ib.orders.update_status(order_id, ib.orders.STATUS_FILLED);

			double temp = execs.get_start_price(order_id);
			if (common.price_is_ok(temp)) price = temp;
			
			async_trades.start(symbol, order_id, price);		
		}
		else 
		{
			ib.orders.deactivate(_order.get_id_main(order_id));
			
			double temp = execs.get_end_price(order_id);
			if (common.price_is_ok(temp)) price = temp;
			
			async_trades.end(symbol, order_id, price);
		}
		
		basic.update_money();
	}
}