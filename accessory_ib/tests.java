package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.misc;
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
		
		String[] methods = new String[] { "get_next_id", "get_funds", "get_open_ids" };
		
		outputs.put(name0, run_methods(class0, methods));
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();

		String[] symbols = get_symbols();
		int pause = 5;
		
		class0 = sync_orders.class;
		
		String name = "place";
		
		String type = sync_orders.TYPE_PLACE_LIMIT;
		String symbol = symbols[0]; 
		double quantity = 1;
		double stop = 2100;
		double start = 2500;
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(type);
		args.add(symbol);
		args.add(quantity);
		args.add(stop);
		args.add(start);
		
		boolean is_ok = run_method(class0, name, new Class<?>[] { String.class, String.class, double.class, double.class, double.class }, args, null);
		output.put(name, is_ok);	
		
		if (!is_ok) return outputs;
		
		misc.pause_secs(pause);
		
		name = "cancel";
		
		int id = sync_orders._last_id_main;
		
		args = new ArrayList<Object>();
		args.add(id);
		
		is_ok = run_method(class0, name, new Class<?>[] { int.class }, args, null);
		output.put(name, is_ok);		
		
		return outputs;	
	}

	public static HashMap<String, HashMap<String, Boolean>> run_async(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		accessory.tests.create_table(db.SOURCE_MARKET);
		
		Class<?> class0 = async_market.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);

		String[] symbols = get_symbols();
		int pause1 = 5;
		int pause2 = 5;
		
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		String name = "start_snapshot";
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(symbols[0]);

		boolean is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);

		name = "start_stream";
		
		args = new ArrayList<Object>();
		args.add(symbols[1]);

		is_ok = run_method(class0, name, new Class<?>[] { String.class }, args, null);
		
		misc.pause_secs(pause1);
		output.put(name, is_ok);
		
		outputs.put(name0, output);
		
		async_market.stop_all();
		misc.pause_secs(pause2);
		
		return outputs;
	}
	
	private static String[] get_symbols() { return new String[] { "GOOG", "AAPL" }; }
}