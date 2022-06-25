package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_tests;
import db_ib.common;
import ib.async_market;
import ib.conn;
import ib.sync;
import ib.sync_basic;
import ib.sync_orders;

public class tests extends parent_tests 
{
	private static tests _instance = new tests();
	private static boolean _use_tws_paper = true;
	private static boolean _orders_too = true;
	
	public tests() { }

	public static HashMap<String, HashMap<String, Boolean>> run_all(boolean use_tws_paper_) { return run_all(use_tws_paper_, true); }
	
	public static HashMap<String, HashMap<String, Boolean>> run_all(boolean use_tws_paper_, boolean orders_too_) 
	{ 
		_use_tws_paper = use_tws_paper_;
		_orders_too = orders_too_;
		
		return _instance.run_all_internal(); 
	}
	
	public HashMap<String, HashMap<String, Boolean>> run_all_internal()
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		String name0 = "tables";
		int level = 0;
		
		update_screen(name0, true, level);		

		String[] tables = new String[] { common.SOURCE_MARKET, common.SOURCE_EXECS, common.SOURCE_BASIC, common.SOURCE_REMOTE, common.SOURCE_ORDERS };
		
		for (String table: tables) { accessory.tests.create_table(table); }
		
		update_screen(name0, false, level);
		
		name0 = "main";
		
		update_screen(name0, true, level);		

		String cur_conn = (String)accessory_ib.config.get_conn(ib.conn.CONFIG_TYPE);
		if (_use_tws_paper) accessory_ib.config.update_conn(ib.conn.CONFIG_TYPE, conn.TYPE_TWS_PAPER);
		
		conn.start();
		
		outputs = run_sync(outputs);
		outputs = run_async(outputs);
		
		conn.end();

		if (_use_tws_paper) accessory_ib.config.update_conn(conn.CONFIG_TYPE, cur_conn);

		update_screen(name0, false, level);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		Class<?> class0 = sync.class;
		String name0 = class0.getName();

		String[] methods = new String[] { "get_order_id", "get_orders" };
		
		outputs.put(name0, run_methods(class0, methods));
		
		if (_orders_too) outputs = run_sync_orders(outputs);
		outputs = run_sync_basic(outputs);
		
		return outputs;	
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_async(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_async_market(outputs);
		
		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync_orders(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		Entry<String, Double> symbol_info = get_symbol(0);
		int pause = 5;
		
		Class<?> class0 = sync_orders.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);
		
		numbers.update_round_decimals(2);
		String symbol = symbol_info.getKey(); 
		
		double quantity = 1;
		double price = symbol_info.getValue();
		double stop = numbers.apply_perc(price, -20, true);
		double start = numbers.apply_perc(price, 10, true);
		double start2 = numbers.apply_perc(price, 5, true);
		
		ArrayList<Object> args0 = new ArrayList<Object>();
		args0.add(symbol);
		args0.add(quantity);
		args0.add(stop);
		args0.add(start);

		Object target = true;
		
		HashMap<String, String> items = new HashMap<String, String>();
		items.put(sync_orders.PLACE_MARKET, "place_market");
		items.put(sync_orders.PLACE_STOP, "place_stop");
		items.put(sync_orders.PLACE_LIMIT, "place_limit");
		items.put(sync_orders.PLACE_STOP_LIMIT, "place_stop_limit");

		double stop_new = numbers.apply_perc(stop, -2, true);
		double start_new = numbers.apply_perc(start, 3, true);
		double start2_new = numbers.apply_perc(start2, 1, true);
		
		HashMap<String, String[]> items2 = new HashMap<String, String[]>();
		items2.put(sync_orders.PLACE_MARKET, new String[] { "update_stop", "update_stop_market" });
		items2.put(sync_orders.PLACE_STOP, new String[] { "update_start", "update_start_market" });
		items2.put(sync_orders.PLACE_LIMIT, new String[] { "update_start", "update_stop_market" });
		items2.put(sync_orders.PLACE_STOP_LIMIT, new String[] { "update_start", "update_start2" });

		for (Entry<String, String> item: items.entrySet())
		{
			String type = item.getKey();
			String name = item.getValue();
			
			ArrayList<Object> args = new ArrayList<Object>(args0);
			Class<?>[] params = null;
				
			boolean is_ok = false;
						
			if (name.equals("place_stop_limit")) 
			{				
				args.add(start2);	
				params = new Class<?>[] { String.class, double.class, double.class, double.class, double.class };
			}
			else if (name.equals("place_market")) 
			{
				args.remove(3);
				params = new Class<?>[] { String.class, double.class, double.class };
			}
			else params = new Class<?>[] { String.class, double.class, double.class, double.class };

			is_ok = run_method(class0, name, params, args, target);
			
			output.put(name, is_ok);			
			if (!is_ok) continue;
			
			misc.pause_secs(pause);
			
			for (String name2: items2.get(type))
			{
				is_ok = false;
				
				if (name2.equals("update_start_market") || name2.equals("update_stop_market")) 
				{	
					args = new ArrayList<Object>();
					args.add(symbol);
					
					params = new Class<?>[] { String.class };
				}
				else 
				{	
					double val = 0.0;
					if (name2.equals("update_start2")) val = start2_new;
					else val = (name2.equals("update_start") ? start_new : stop_new);
					
					args = new ArrayList<Object>();
					args.add(symbol);
					args.add(val);
							 
					params = new Class<?>[] { String.class, double.class };
				}

				is_ok = run_method(class0, name2, params, args, target);
				
				String name22 = name2 + "_" + type;
				output.put(name22, is_ok);
				
				misc.pause_secs(pause);
			}
			
			name = "cancel";
			String name2 = name + "_" + type;
			
			int id = sync_orders.get_last_id_main();
			
			args = new ArrayList<Object>();
			args.add(id);
			
			is_ok = run_method(class0, name, new Class<?>[] { int.class }, args, null);
			output.put(name2, is_ok);			
		}		

		update_screen(name0, false, 1);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync_basic(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		Class<?> class0 = sync_basic.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "start";
		
		ArrayList<Object> args = null;

		boolean is_ok = run_method(class0, name, null, args, null);
		output.put(name, is_ok);
		
		update_screen(name0, false, 1);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_async_market(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		
		async_market.update_enabled(true);
		
		Class<?> class0 = async_market.class;
		String name0 = class0.getName();
		
		update_screen(name0, true, 1);

		int pause1 = 5;
		int pause2 = 5;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "__start_snapshot";
		
		int symbol_i = 0;
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		boolean is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);

		name = "__start_stream";
		
		symbol_i++;
		
		args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);
		
		async_market.__stop_all();
		misc.pause_secs(pause2);
		
		outputs.put(name0, output);
		
		update_screen(name0, false, 1);		
		
		return outputs;
	}
	
	private static Entry<String, Double> get_symbol(int i_) 
	{ 
		HashMap<String, Double> all = get_symbols();
		
		int i = i_;
		int max_i = all.size() - 1;
		if (i > max_i) i = max_i;
		
		int i2 = -1;
		
		for (Entry<String, Double> item: all.entrySet())
		{
			i2++;
			if (i == i2) return item;
		}
		
		return null;
	}
	
	private static HashMap<String, Double> get_symbols() 
	{ 
		HashMap<String, Double> output = new HashMap<String, Double>();
		
		output.put("GOOG", 2200.0);
		output.put("AAPL", 120.0);

		return output;
	}
}