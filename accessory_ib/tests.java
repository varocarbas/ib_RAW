package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_tests;
import db_ib.common;
import ib.apps;
import ib.basic;
import ib.conn;
import ib.market;
import ib.orders;
import ib.sync;
import ib.watchlist;

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

		String[] sources = new String[] 
		{ 
			common.SOURCE_MARKET, common.SOURCE_EXECS, common.SOURCE_BASIC, common.SOURCE_REMOTE, 
			common.SOURCE_ORDERS, common.SOURCE_TRADES, common.SOURCE_WATCHLIST, common.SOURCE_APPS
		};

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		for (String source: sources) 
		{ 
			boolean is_ok = create_table(source);
			String name = name0 + misc.SEPARATOR_NAME + accessory.db.get_table(source);
			
			output.put(name, is_ok);
		}
		
		outputs.put(name0, output);
		
		update_screen(name0, false, level);
		
		name0 = "main";
		
		update_screen(name0, true, level);		

		String cur_conn_type = conn.get_conn_type();
		if (_use_tws_paper) conn.update_conn_type(conn.TYPE_TWS_PAPER);
		
		conn.start();
		
		outputs = run_starts(outputs);

		outputs = run_sync(outputs);
		
		if (_orders_too) outputs = run_orders(outputs);
		
		outputs = run_data(outputs);
		
		conn.end();

		if (_use_tws_paper) conn.update_conn_type(cur_conn_type);

		update_screen(name0, false, level);
		
		check_wrongs(outputs);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		Class<?> class0 = sync.class;
		String name0 = class0.getName();

		String[] methods = new String[] { "get_order_id", "get_orders" };
		
		outputs.put(name0, _instance.run_methods(class0, methods));
		
		return outputs;	
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_data(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_data_market(outputs);
		outputs = run_data_watchlist(outputs);
		
		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_orders(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		Entry<String, Double> symbol_info = get_symbol(0);
		int pause = 5;
		
		Class<?> class0 = orders.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);
		
		numbers.update_round_decimals(2);
		String symbol = symbol_info.getKey(); 
		
		double quantity = 1;
		double price = symbol_info.getValue();
		double stop = numbers.apply_perc(price, -3, true);
		double start = numbers.apply_perc(price, 2, true);
		double start2 = numbers.apply_perc(price, 1, true);
		
		ArrayList<Object> args0 = new ArrayList<Object>();
		args0.add(symbol);
		args0.add(quantity);
		args0.add(stop);
		args0.add(start);

		Object target = true;
		
		HashMap<String, String> items = new HashMap<String, String>();
		items.put(orders.PLACE_MARKET, "place_market");
		items.put(orders.PLACE_STOP, "place_stop");
		items.put(orders.PLACE_LIMIT, "place_limit");
		items.put(orders.PLACE_STOP_LIMIT, "place_stop_limit");

		double stop_new = numbers.apply_perc(stop, -2, true);
		double start_new = numbers.apply_perc(start, 3, true);
		double start2_new = numbers.apply_perc(start2, 2, true);
		
		HashMap<String, String[]> items2 = new HashMap<String, String[]>();
		items2.put(orders.PLACE_MARKET, new String[] { "update_stop", "update_stop_market" });
		items2.put(orders.PLACE_STOP, new String[] { "update_start", "update_start_market" });
		items2.put(orders.PLACE_LIMIT, new String[] { "update_start", "update_stop_market" });
		items2.put(orders.PLACE_STOP_LIMIT, new String[] { "update_start", "update_start2" });

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

			is_ok = _instance.run_method(class0, name, params, args, target);
			
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

				is_ok = _instance.run_method(class0, name2, params, args, target);
				
				String name22 = name2 + "_" + type;
				output.put(name22, is_ok);
				
				misc.pause_secs(pause);
			}
			
			name = "cancel";
			String name2 = name + "_" + type;
			
			int id = orders.get_last_id_main();
			
			args = new ArrayList<Object>();
			args.add(id);
			
			is_ok = _instance.run_method(class0, name, new Class<?>[] { int.class }, args, null);
			output.put(name2, is_ok);			
		}		

		outputs.put(name0, output);
		
		update_screen(name0, false, 1);
		
		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_starts(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		ArrayList<Object> args = null;
		String name = "__start";

		Class<?>[] classes0 = new Class<?>[] { basic.class, apps.class };
		
		for (Class<?> class0: classes0)
		{
			if (class0.equals(apps.class)) name = "start";
			
			String name0 = class0.getName();
			
			update_screen(name0, true, 1);
			
			HashMap<String, Boolean> output = new HashMap<String, Boolean>();

			boolean is_ok = _instance.run_method(class0, name, null, args, null);
			output.put(name, is_ok);
			
			String name2 = name + misc.SEPARATOR_NAME + name0;
			outputs.put(name2, output);
			
			update_screen(name0, false, 1);
		}

		return outputs;
	}
		
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_data_market(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		
		Class<?> class0 = market.class;
		String name0 = class0.getName();
		
		update_screen(name0, true, 1);

		int pause1 = 5;
		int pause2 = 2;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "start_snapshot";
		
		int symbol_i = 0;
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		boolean is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class }, args, null);
		output.put(name, is_ok);
		
		misc.pause_secs(pause1);

		name = "start_stream";
		
		symbol_i++;
		
		args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class }, args, null);
		output.put(name, is_ok);
		
		misc.pause_secs(pause1);

		name = "stop_all";

		is_ok = _instance.run_method(class0, name, null, null, null);
		output.put(name, is_ok);
		
		misc.pause_secs(pause2);

		outputs.put(name0, output);
		
		update_screen(name0, false, 1);		
		
		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_data_watchlist(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		
		Class<?> class0 = watchlist.class;
		String name0 = class0.getName();
		
		update_screen(name0, true, 1);

		int pause1 = 5;
		int pause2 = 2;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "add";
		
		String symbol = tests.get_symbol(0).getKey();
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(symbol);

		boolean is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);

		name = "remove";
		
		args = new ArrayList<Object>();
		args.add(symbol);

		is_ok = _instance.run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);
		
		watchlist.remove_all();
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
		
		output.put("GOOG", 110.0);
		output.put("AAPL", 150.0);

		return output;
	}
}