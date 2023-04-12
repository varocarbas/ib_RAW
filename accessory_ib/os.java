package accessory_ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.credentials;
import accessory.misc;
import accessory.strings;
import accessory.xy;

public abstract class os 
{
	public static int execute_ib(boolean is_gateway_) 
	{ 
		int output = accessory.os.WRONG_WINDOW_ID;
		
		String executable = get_executable_ib(is_gateway_);
		if (!strings.is_ok(executable)) return output;

		accessory.os.kill_app(executable);
		
		misc.pause_secs(1);
		
		if (accessory.os.execute_command(executable, false)) 
		{
			misc.pause_secs(60);
			
			output = accessory.os.get_running_window_id_app(executable);
		}
		
		return output;
	}
	
	public static boolean click_ib_real_paper(int window_id_, boolean is_gateway_, boolean is_real_) 
	{
		if (!accessory.os.can_move_mouse()) return true;
		
		boolean output = false;
		
		xy xy = get_xy_ib_real_paper(is_gateway_, is_real_);
		if (xy == null) return output;
		
		output = accessory.os.click_running_window(window_id_, xy);
		
		if (output) misc.pause_secs(1);
		
		return output;
	}
	
	public static boolean fill_form_ib(int window_id_, HashMap<String, String> credentials_, boolean is_gateway_) 
	{ 
		xy[] xys = (accessory.os.can_move_mouse() ? get_xys_ib_form(is_gateway_) : null);
		
		String username = (String)arrays.get_value(credentials_, credentials.USERNAME);
		String password = (String)arrays.get_value(credentials_, credentials.PASSWORD);
		
		return (strings.are_ok(new String[] { username, password }) ? accessory.os.fill_running_form(window_id_, new String[] { username, password }, xys) : false); 
	}
	
	private static String get_executable_ib(boolean is_gateway_) 
	{ 
		String output = null;
		
		if (!accessory.os.is_windows()) output = paths.get_path_app_ib(!is_gateway_); 
	
		return output;
	}
	
	private static xy get_xy_ib_real_paper(boolean is_gateway_, boolean is_real_)
	{
		xy output = null;

		if (accessory.os.is_linux()) output = os_linux.get_xy_ib_real_paper(is_gateway_, is_real_);

		return output;
	}

	private static xy[] get_xys_ib_form(boolean is_gateway_)
	{
		xy[] output = null;
		
		if (accessory.os.is_linux())
		{
			ArrayList<xy> temp0 = new ArrayList<xy>(); 
			
			xy temp = os_linux.get_xy_ib_username(is_gateway_);
			if (temp != null) temp0.add(temp);
			
			temp = os_linux.get_xy_ib_password(is_gateway_);
			if (temp != null) temp0.add(temp);
			
			temp = os_linux.get_xy_ib_button(is_gateway_);
			if (temp != null) temp0.add(temp);
			
			if (temp0.size() > 0) output = arrays.to_array(temp0);
		}
		
		return output;	
	}
}