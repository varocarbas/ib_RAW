package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.dates;
import accessory.misc;
import accessory.numbers;
import accessory.parent_tests;
import accessory.strings;
import db_ib.common;
import ib.apps;
import ib.basic;
import ib.conn;
import ib.market_quicker;
import ib.orders;
import ib.remote;
import ib.sync;
import ib.watchlist_quicker;

public class tests extends parent_tests 
{
	private static tests _instance = new tests();
	private static boolean _use_tws_paper = true;
	private static boolean _orders_too = true;
	private static boolean _drop_tables_old = false;
	
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
			common.SOURCE_ORDERS, common.SOURCE_TRADES, common.SOURCE_WATCHLIST, common.SOURCE_APPS,
		};

		HashMap<String, String> olds = db_ib.common.get_sources_old();
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		
		for (String source: sources) 
		{ 
			boolean is_ok = create_table(source);
			String name = name0 + misc.SEPARATOR_NAME + accessory.db.get_table(source);
			
			output.put(name, is_ok);
			
			if (!olds.containsKey(source)) continue;

			source = olds.get(source);
			
			is_ok = create_table(source, _drop_tables_old);
			name = name0 + misc.SEPARATOR_NAME + accessory.db.get_table(source);
			
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
		
		if (_orders_too) outputs = run_orders_both(outputs);
		
		outputs = run_data(outputs);
		
		conn.end();

		if (_use_tws_paper) conn.update_conn_type(cur_conn_type);

		update_screen(name0, false, level);
		
		check_wrongs(outputs);
		
		return outputs;
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_sync(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		Class<?> class0 = sync.class;
		String name0 = class0.getName();

		String[] methods = new String[] { "__get_order_id", "__get_orders", "__get_funds" };
		
		outputs.put(name0, _instance.run_methods(class0, methods));
		
		return outputs;	
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_data(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		outputs = run_data_watchlist_quicker(outputs);
		outputs = run_data_market_quicker(outputs);
		
		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_orders_both(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		numbers.update_round_decimals(2);
		
		outputs = run_orders(outputs);
		outputs = run_orders_remote(outputs);
		
		return outputs;
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_orders(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		Entry<String, Double> symbol_info = get_symbol(0);
		int pause = 5;
		
		Class<?> class0 = orders.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);
		
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
		items2.put(orders.PLACE_MARKET, new String[] { "__update_stop", "__update_stop_market" });
		items2.put(orders.PLACE_STOP, new String[] { "__update_start", "__update_start_market" });
		items2.put(orders.PLACE_LIMIT, new String[] { "__update_start", "__update_stop_market" });
		items2.put(orders.PLACE_STOP_LIMIT, new String[] { "__update_start", "__update_start2" });

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
				
				if (name2.equals("__update_start_market") || name2.equals("__update_stop_market")) 
				{	
					args = new ArrayList<Object>();
					args.add(symbol);
					
					params = new Class<?>[] { String.class };
				}
				else 
				{	
					double val = 0.0;
					if (name2.equals("__update_start2")) val = start2_new;
					else val = (name2.equals("__update_start") ? start_new : stop_new);
					
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
			
			name = "__cancel";
			String name2 = name + "_" + type;
			
			int id = orders.get_last_id_main();
			
			args = new ArrayList<Object>();
			args.add(id);
			
			is_ok = _instance.run_method(class0, name, new Class<?>[] { int.class }, args, true);
			output.put(name2, is_ok);			
		}		

		outputs.put(name0, output);
		
		update_screen(name0, false, 1);
		
		return outputs;
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_orders_remote(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		Entry<String, Double> symbol_info = get_symbol(0);
		
		Class<?> class0 = remote.class;
		String name0 = class0.getName();

		update_screen(name0, true, 1);
		
		int pause = 1;
		
		String symbol = symbol_info.getKey(); 
		
		double perc = 50;
		double price = symbol_info.getValue();
		
		double stop = numbers.apply_perc(price, -3, true);
		double start = numbers.apply_perc(price, 2, true);
		double start2 = numbers.apply_perc(price, 1, true);
				
		boolean wait_for_execution = false;
		
		String type = remote.PLACE_STOP_LIMIT;
		
		String name = "__request_place_perc";
		
		Class<?>[] params = new Class<?>[] { String.class, String.class, double.class, double.class, double.class, double.class, double.class, boolean.class };
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(type);
		args.add(symbol);
		args.add(stop);
		args.add(start);
		args.add(start2);
		args.add(perc);
		args.add(price);
		args.add(wait_for_execution);
				
		boolean is_ok = _instance.run_method(class0, name, params, args, null);
		
		int request = (int)_instance._temp_output;
		if (is_ok && request == ib.remote.WRONG_REQUEST) is_ok = false;

		output.put(name, is_ok);
		
		if (!is_ok) 
		{
			outputs.put(name0, output);
			
			return outputs;
		}
		
		outputs = run_orders_remote_execute(class0, name0, request, false, outputs);
		if (!(boolean)_instance._temp_output) return outputs;

		misc.pause_secs(pause);
		
		Object target = remote.REQUEST_OK;
		
		type = remote.UPDATE_STOP_VALUE;
		
		name = "__request_update";
		
		double stop_new = numbers.apply_perc(stop, -2, true);
		
		params = new Class<?>[] { int.class, String.class, double.class, boolean.class };
		
		args = new ArrayList<Object>();
		args.add(request);
		args.add(type);
		args.add(stop_new);
		args.add(wait_for_execution);
				
		is_ok = _instance.run_method(class0, name, params, args, target);

		output.put(name, is_ok);
		
		if (!is_ok) 
		{
			outputs.put(name0, output);
			
			return outputs;
		}
		
		outputs = run_orders_remote_execute(class0, name0, request, false, outputs);
		if (!(boolean)_instance._temp_output) return outputs;

		misc.pause_secs(pause);
		
		type = remote.CANCEL;
		
		name = "__request_cancel";
		
		params = new Class<?>[] { int.class, boolean.class };
		
		args = new ArrayList<Object>();
		args.add(request);
		args.add(wait_for_execution);
				
		is_ok = _instance.run_method(class0, name, params, args, target);

		output.put(name, is_ok);
		
		if (!is_ok) 
		{
			outputs.put(name0, output);
			
			return outputs;
		}
		
		outputs = run_orders_remote_execute(class0, name0, request, true, outputs);
		
		update_screen(name0, false, 1);
		
		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_orders_remote_execute(Class<?> class0_, String name0_, int request_, boolean is_cancel_, HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		
		String name = "__execute_all";
		boolean is_ok = _instance.run_method(class0_, name, null, null, null);
		
		output.put(name, is_ok);
		
		if (!is_ok) 
		{
			outputs.put(name0_, output);
			
			_instance._temp_output = false;
			
			return outputs;
		}
		
		name = "__wait_for_execution";
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(request_);
		args.add(is_cancel_);
		
		Class<?>[] params = new Class<?>[] { int.class, boolean.class };
		
		is_ok = _instance.run_method(class0_, name, params, args, null);
		
		output.put(name, is_ok);
		
		if (!is_ok) 
		{
			outputs.put(name0_, output);
			
			_instance._temp_output = false;
			
			return outputs;
		}

		HashMap<String, String> vals = ib.remote.get_vals(request_);
		is_ok = (strings.are_equal(ib.remote.get_status(vals), ib.remote.STATUS_ACTIVE) && strings.are_equal(ib.remote.get_status2(vals), ib.remote.STATUS2_EXECUTED));
		
		if (!is_ok)
		{
			name += "_DB"; 
			output.put(name, false);
			
			_instance._temp_output = false;
		}
		else _instance._temp_output = true;
		
		return outputs;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_starts(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);

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
	
	public static HashMap<String, HashMap<String, Boolean>> run_data_market_quicker(HashMap<String, HashMap<String, Boolean>> outputs_) { return run_data_quicker(market_quicker.class, outputs_); }	
	
	public static HashMap<String, HashMap<String, Boolean>> run_data_watchlist_quicker(HashMap<String, HashMap<String, Boolean>> outputs_) { return run_data_quicker(watchlist_quicker.class, outputs_); }	
	
	private static HashMap<String, HashMap<String, Boolean>> run_data_quicker(Class<?> class0_, HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = arrays.get_new_hashmap_xy(outputs_);
		
		String name0 = class0_.getName();
		
		update_screen(name0, true, 1);

		int secs_while = 5;
		int secs_after = 5;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = (class0_.equals(market_quicker.class) ? "__start" : "__add");
		
		ArrayList<String> symbols = arrays.get_keys_hashmap(get_symbols());
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(symbols);

		boolean is_ok = false;

		long start = dates.start_elapsed();
		
		while (dates.get_elapsed(start) < secs_while)
		{
			is_ok = _instance.run_method(class0_, name, new Class<?>[] { ArrayList.class }, args, null);
			
			if (!is_ok) break;
		}

		output.put(name, is_ok);
				
		misc.pause_secs(secs_after);
		
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
		
		output.put("GOOG", 90.0);
		output.put("AAPL", 130.0);

		for (String symbol: output.keySet())
		{
			double price = ib.common._get_price(symbol, true);
			if (ib.common.price_is_ok(price)) output.put(symbol, price);
		}

		return output;
	}
}