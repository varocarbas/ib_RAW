package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_tests;
import ib.async_market;
import ib.conn;
import ib.sync;
import ib.sync_orders;

public class tests extends parent_tests 
{
	private static tests _instance = new tests();
	private static boolean _use_tws_paper = false;
	
	public tests() { }
	
	public static HashMap<String, HashMap<String, Boolean>> run_all(boolean use_tws_paper_) 
	{ 
		_use_tws_paper = use_tws_paper_;
		
		return _instance.run_all_internal(); 
	}
	
	public HashMap<String, HashMap<String, Boolean>> run_all_internal()
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		String name0 = "main";
		int level = 0;
		
		update_screen(name0, true, level);		

		String cur_conn = (String)accessory_ib.config.get_conn(accessory_ib.types.CONFIG_CONN_TYPE);
		if (_use_tws_paper) accessory_ib.config.update_conn(accessory_ib.types.CONFIG_CONN_TYPE, conn.TYPE_TWS_PAPER);
		
		conn.start();
		
		outputs = run_sync(outputs);
		outputs = run_async(outputs);
		
		conn.end();

		if (_use_tws_paper) accessory_ib.config.update_conn(accessory_ib.types.CONFIG_CONN_TYPE, cur_conn);

		update_screen(name0, false, level);
		
		return outputs;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);

		Class<?> class0 = sync.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);
		
		String[] methods = new String[] { "get_order_id", "get_funds", "get_orders" };
		
		outputs.put(name0, run_methods(class0, methods));
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		Entry<String, Double> symbol_info = get_symbol(0);
		int pause = 5;
		
		class0 = sync_orders.class;
		
		numbers._round_decimals = 2;
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

		ArrayList<Object> args02 = new ArrayList<Object>();
		args02.add(symbol);

		Object target = true;
		
		HashMap<String, String> items = new HashMap<String, String>();
		items.put(sync_orders.PLACE_MARKET, "place_market");
		items.put(sync_orders.PLACE_STOP, "place_stop");
		items.put(sync_orders.PLACE_LIMIT, "place_limit");
		items.put(sync_orders.PLACE_STOP_LIMIT, "place_stop_limit");
		
		for (Entry<String, String> item: items.entrySet())
		{
			String type = item.getKey();
			String name = item.getValue();
			
			ArrayList<Object> args = new ArrayList<Object>(args0);
				
			boolean is_ok = false;
			
			if (name.equals("place_stop_limit")) 
			{				
				args.add(start2);
				
				is_ok = run_method(class0, name, new Class<?>[] { String.class, double.class, double.class, double.class, double.class }, args, target);
			}
			else if (name.equals("place_market")) 
			{
				args.remove(3);

				is_ok = run_method(class0, name, new Class<?>[] { String.class, double.class, double.class }, args, target);
			}
			else is_ok = run_method(class0, name, new Class<?>[] { String.class, double.class, double.class, double.class }, args, target);
			
			output.put(name, is_ok);			
			if (!is_ok) continue;
			
			misc.pause_secs(pause);
			
			name = "cancel";
			String name2 = name + "_" + type;
			
			int id = sync_orders._last_id_main;
			
			args = new ArrayList<Object>();
			args.add(id);
			
			is_ok = run_method(class0, name, new Class<?>[] { int.class }, args, null);
			output.put(name2, is_ok);			
		}		
		
		return outputs;	
	}
	
	public static HashMap<String, HashMap<String, Boolean>> run_async(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		accessory.tests.create_table(db.SOURCE_MARKET);
		
		Class<?> class0 = async_market.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);

		int pause1 = 5;
		int pause2 = 5;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "start_snapshot";
		
		int symbol_i = 0;
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		boolean is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);

		name = "start_stream";
		
		symbol_i++;
		
		args = new ArrayList<Object>();
		args.add(get_symbol(symbol_i).getKey());

		is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);
		
		outputs.put(name0, output);
		
		async_market.stop_all();
		misc.pause_secs(pause2);
		
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