package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.credentials;
import accessory.misc;
import accessory.os;
import accessory.strings;
import accessory.xy;
import accessory_ib.paths;

public abstract class apps_ib 
{
	public static final String _ID = "conn_apps";
	
	private static final String ID_REAL = "real";
	private static final String ID_PAPER = "paper";
	
	private static final int GATEWAY_REAL_X = 325;
	private static final int GATEWAY_REAL_Y = 245;
	private static final int GATEWAY_PAPER_X = 500;
	private static final int GATEWAY_PAPER_Y = GATEWAY_REAL_Y;

	private static final int GATEWAY_FORM_USERNAME_X = GATEWAY_REAL_X;
	private static final int GATEWAY_FORM_USERNAME_Y = 290;
	private static final int GATEWAY_FORM_PASSWORD_X = GATEWAY_FORM_USERNAME_X;
	private static final int GATEWAY_FORM_PASSWORD_Y = 330;
	private static final int GATEWAY_FORM_BUTTON_X = GATEWAY_FORM_USERNAME_X;
	private static final int GATEWAY_FORM_BUTTON_Y = 410;
	
	private static final int TWS_REAL_X = 590;
	private static final int TWS_REAL_Y = 200;
	private static final int TWS_PAPER_X = 650;
	private static final int TWS_PAPER_Y = TWS_REAL_Y;

	private static final int TWS_FORM_USERNAME_X = TWS_REAL_X;
	private static final int TWS_FORM_USERNAME_Y = 260;
	private static final int TWS_FORM_PASSWORD_X = TWS_FORM_USERNAME_X;
	private static final int TWS_FORM_PASSWORD_Y = 290;
	private static final int TWS_FORM_BUTTON_X = TWS_FORM_USERNAME_X;
	private static final int TWS_FORM_BUTTON_Y = 380;

	public static boolean encrypt_credentials(String username_, String password_) { return encrypt_credentials(conn.type_is_real(), username_, password_); }

	public static boolean encrypt_credentials(boolean is_real_, String username_, String password_) { return encrypt_credentials(is_real_, basic.get_user(), username_, password_); }
	
	public static boolean encrypt_credentials(boolean is_real_, String user_, String username_, String password_)  
	{ 
		String id = get_encryption_id(is_real_);

		return (strings.is_ok(id) ? credentials.encrypt_username_password_file(id, user_, username_, password_) : false);  
	}
	
	public static boolean run() { return run(basic.get_user()); }
	
	public static boolean run(String user_credentials_) { return run(conn.type_is_gateway(), conn.type_is_real(), user_credentials_); }
	
	public static boolean run(boolean is_gateway_, boolean is_real_, String user_credentials_)
	{
		boolean output = false;
		
		HashMap<String, String> credentials = get_credentials(is_real_, user_credentials_);
		if (!arrays.is_ok(credentials)) return output;
	
		int window_id = start_app(is_gateway_);
		if (window_id <= os.WRONG_WINDOW_ID || !select_real_paper(window_id, is_gateway_, is_real_)) return output;

		return (arrays.is_ok(credentials) ? os.fill_running_form(window_id, new String[] { credentials.get(accessory.credentials.USERNAME), credentials.get(accessory.credentials.PASSWORD) }, get_xys_form(is_gateway_)) : false);
	}
	
	private static int start_app(boolean is_gateway_) 
	{ 
		int output = os.WRONG_WINDOW_ID;
		
		String executable = get_executable(is_gateway_);
		if (!strings.is_ok(executable)) return output;

		os.kill_app(executable);
		
		misc.pause_secs(1);
		
		if (os.execute_command(executable, false)) 
		{
			misc.pause_secs(10);
			
			output = os.get_running_window_id_app(executable);
		}
		
		return output;
	}
	
	private static boolean select_real_paper(int window_id_, boolean is_gateway_, boolean is_real_) 
	{
		boolean output = os.click_running_window(window_id_, get_xy_real_paper(is_gateway_, is_real_));
		
		if (output) misc.pause_secs(1);
		
		return output;
	}
	
	private static String get_executable(boolean is_gateway_) 
	{ 
		String output = null;
		
		if (!os.is_windows()) output = paths.get_path_ib(!is_gateway_); 
	
		return output;
	}
	
	private static HashMap<String, String> get_credentials(boolean is_real_, String user_) { return credentials.get_username_password_file(get_encryption_id(is_real_), user_, true); }
	
	private static String get_encryption_id(boolean is_real_) { return basic.get_encryption_id(new String[] { _ID, (is_real_ ? ID_REAL : ID_PAPER) }); }

	private static xy[] get_xys_form(boolean is_gateway_)
	{
		xy[] output = new xy[3];
		
		int i = 0;		
		output[i] = get_xy_username(is_gateway_);
		
		i++;
		output[i] = get_xy_password(is_gateway_);
		
		i++;
		output[i] = get_xy_button(is_gateway_);
		
		return output;	
	}

	private static xy get_xy_username(boolean is_gateway_)
	{
		int x = 0;
		int y = 0;
		
		if (is_gateway_)
		{
			x = GATEWAY_FORM_USERNAME_X;
			y = GATEWAY_FORM_USERNAME_Y;	
		}
		else
		{
			x = TWS_FORM_USERNAME_X;
			y = TWS_FORM_USERNAME_Y;
		}
	
		return new xy(x, y);	
	}

	private static xy get_xy_password(boolean is_gateway_)
	{
		int x = 0;
		int y = 0;
		
		if (is_gateway_)
		{
			x = GATEWAY_FORM_PASSWORD_X;
			y = GATEWAY_FORM_PASSWORD_Y;	
		}
		else
		{
			x = TWS_FORM_PASSWORD_X;
			y = TWS_FORM_PASSWORD_Y;
		}
	
		return new xy(x, y);	
	}

	private static xy get_xy_button(boolean is_gateway_)
	{
		int x = 0;
		int y = 0;
		
		if (is_gateway_)
		{
			x = GATEWAY_FORM_BUTTON_X;
			y = GATEWAY_FORM_BUTTON_Y;	
		}
		else
		{
			x = TWS_FORM_BUTTON_X;
			y = TWS_FORM_BUTTON_Y;
		}
	
		return new xy(x, y);	
	}
	
	private static xy get_xy_real_paper(boolean is_gateway_, boolean is_real_)
	{
		int x = 0;
		int y = 0;
		
		if (is_gateway_)
		{
			if (is_real_)
			{
				x = GATEWAY_REAL_X;
				y = GATEWAY_REAL_Y;
			}
			else
			{
				x = GATEWAY_PAPER_X;
				y = GATEWAY_PAPER_Y;			
			}
		}
		else
		{
			if (is_real_)
			{
				x = TWS_REAL_X;
				y = TWS_REAL_Y;
			}
			else
			{
				x = TWS_PAPER_X;
				y = TWS_PAPER_Y;			
			}
		}
			
		return new xy(x, y);	
	}
}