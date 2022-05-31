package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.misc;
import accessory.parent_tests;
import ib.async_market;
import ib.conn;
import ib.sync;

public class tests extends parent_tests 
{
	private static tests _instance = new tests();
	
	public tests() { }
	
	public static HashMap<String, HashMap<String, Boolean>> run_all() { return _instance.run_all_internal(); }
	
	public HashMap<String, HashMap<String, Boolean>> run_all_internal()
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		String name0 = "main";
		int level = 0;
		
		update_screen(name0, true, level);		

		conn.start();
		
		outputs = run_sync(outputs);
		outputs = run_async(outputs);
		
		conn.end();
		
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
		
		return outputs;	
	}

	public static HashMap<String, HashMap<String, Boolean>> run_async(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		accessory.tests.create_table(accessory_ib.db.SOURCE_MARKET);
		
		Class<?> class0 = async_market.class;
		String name0 = class0.getName();
		update_screen(name0, true, 1);

		String[] symbols = new String[] { "GOOG", "AAPL" };
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
}