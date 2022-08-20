package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory.arrays;
import accessory.parent_static;
import external_ib.orders;

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

	public static final String SIDE_BOUGHT = orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = orders.EXEC_SIDE_SOLD;
	
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

		boolean stored = update(execution_.execId(), vals);
		
		__unlock();
		
		if (stored) update_others();
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
		
		boolean stored = update(report_.execId(), vals);
		
		__unlock();
		
		if (stored) update_others();
	}	
	
	private static boolean update(String exec_id_, HashMap<String, Object> vals_)
	{
		boolean stored = false;
		
		HashMap<String, Object> vals = new HashMap<String, Object>(vals_);
		vals.put(EXEC_ID, exec_id_);
		
		if (_all_vals.containsKey(exec_id_))
		{
			for (Entry<String, Object> item: _all_vals.get(exec_id_).entrySet()) { vals.put(item.getKey(), item.getValue()); }
		}

		if (vals.size() == TARGET_TOT_FIELDS) 
		{
			db_ib.execs.update(exec_id_, vals, ib.execs.is_ok());
			
			if (_all_vals.containsKey(exec_id_)) _all_vals.remove(exec_id_);
			
			stored = true;
		}
		else _all_vals.put(exec_id_, vals);
		
		return stored;
	}
	
	private static void update_others()
	{
		for (boolean is_filled: new boolean[] { true, false }) update_others(is_filled);
	}
	
	private static void update_others(boolean is_filled_)
	{
		boolean is_quick = ib.execs.is_quick();
		
		ArrayList<HashMap<String, String>> all = (is_filled_ ? db_ib.execs.get_all_filled(is_quick) : db_ib.execs.get_all_completed(is_quick));
		if (!arrays.is_ok(all)) return;

		for (HashMap<String, String> item: all)
		{
			String symbol = item.get(SYMBOL);	
			
			int order_id_main = Integer.parseInt(item.get(ORDER_ID));			
			int order_id_sec = order.get_id_sec(order_id_main);

			int order_id = (is_filled_ ? order_id_main : order_id_sec);	
			if (db_ib.execs.get_quantity(order_id) != db_ib.orders.get_quantity(order_id_main)) continue;

			if (is_filled_) update_others_filled(symbol, order_id_main);
			else update_others_completed(symbol, order_id_main, order_id_sec);
		}
	}
	
	private static void update_others_filled(String symbol_, int order_id_main_)
	{
		ib.orders.update_status(order_id_main_, ib.orders.STATUS_FILLED);

		if (trades.synced_with_execs()) trades.start(symbol_, order_id_main_, execs.get_start_price(order_id_main_));
	}
	
	private static void update_others_completed(String symbol_, int order_id_main_, int order_id_sec_)
	{
		ib.orders.deactivate(order_id_main_);
		
		if (trades.synced_with_execs()) trades.end(symbol_, order_id_sec_, execs.get_end_price(order_id_sec_));
	}
}