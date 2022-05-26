package accessory_ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.parent_tests;
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

		return outputs;
	}
}