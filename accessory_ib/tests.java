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
		
		parent_tests.update_console(name0, true, level);		

		conn.start();
		
		outputs = run_sync(outputs);

		conn.end();
		
		parent_tests.update_console(name0, false, level);
		
		return outputs;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Boolean>> run_sync(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, Boolean> outputs = new HashMap<String, Boolean>();

		Class<?> class0 = sync.class;
		String name0 = class0.getName();
		parent_tests.update_console(name0, true, 1);
		
		String[] names = new String[] { "get_funds", "get_open_ids" };
		
		for (String name: names) { outputs.put(name, parent_tests.run_method(class0, name, null, null, null)); }

		HashMap<String, HashMap<String, Boolean>> outputs2 = (HashMap<String, HashMap<String, Boolean>>)arrays.get_new(outputs_);
		outputs2.put(name0, outputs);
		
		return outputs2;
	}

	public static HashMap<String, HashMap<String, Boolean>> run_async(HashMap<String, HashMap<String, Boolean>> outputs_)
	{
		HashMap<String, HashMap<String, Boolean>> outputs = new HashMap<String, HashMap<String, Boolean>>();

		return outputs;
	}
}