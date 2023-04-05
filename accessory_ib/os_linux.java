package accessory_ib;

import accessory.xy;

abstract class os_linux 
{
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

	public static xy get_xy_ib_username(boolean is_gateway_)
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

	public static xy get_xy_ib_password(boolean is_gateway_)
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

	public static xy get_xy_ib_button(boolean is_gateway_)
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
	
	public static xy get_xy_ib_real_paper(boolean is_gateway_, boolean is_real_)
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