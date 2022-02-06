package ib;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib.types;

public class conn 
{
	static EClientSocket client;
	
	private static final int MIN_ID = 0;
	private static final int MAX_ID = 31;
	
	private static final int PORT_REAL = 7496;
	private static final int PORT_PAPER = 7497;
	private static final int PORT_GATEWAY = 4001;
	private static final int PORT_GATEWAY_PAPER = 4002;
	
	private volatile static boolean connected = false;
	private volatile static boolean first_conn = false;
	
	private static wrapper wrapper;
	private static int conn_id; 
	private static int conn_port;

	public static String check(String type_)
	{
		return accessory_ib.types.check_conn(type_, accessory.keys.ADD);
	}

	public static boolean is_ok()
	{		
		if (!connected) connect();

		if (!connected) errors.manage(types.ERROR_CONN_NONE, false);
		
		return connected;
	}
	
	public static boolean start(int conn_id_, String conn_type_)
	{
		String error = null;
		
		if (!numbers.is_ok(conn_id_, MIN_ID, MAX_ID)) error = types.ERROR_CONN_ID;
		else 
		{
			conn_id = conn_id_;
			String conn_type = check(conn_type_);	
			
			if (strings.is_ok(conn_type)) conn_port = get_conn_port(conn_type);
			else error = types.ERROR_CONN_TYPE;
		}

		if (strings.is_ok(error))
		{
			errors.manage(error, true);
			
			return false;
		}
		
		wrapper = new wrapper();
		client = wrapper.getClient();

		connect();
		
		return connected;
	}

	public static void end()
	{
		if (client != null) client.eDisconnect();
	}
	
	public static boolean check_connection()
	{
		if (!connected) connect();
		
		return connected;
	}
	
	private static boolean stop_conn()
	{
		boolean stop = false;
		
		return stop;
	}
	
	private static void connect()
	{	
		connected = false;
		
		while (!connected)
		{	
			connect_internal();	
			
			if (connected) break;
			if (!first_conn) misc.pause_secs(1);
			if (stop_conn()) break;
			
			errors.manage(types.ERROR_CONN_NONE, false);
		}
	}
	
	private static void connect_internal()
	{
		global.last_id = 0;
				
		final EReaderSignal signal = wrapper.getSignal();
		client.eConnect("127.0.0.1", conn_port, conn_id);
		final EReader reader = new EReader(client, signal);   

		reader.start();

	    new Thread
	    (
	    	new Runnable()
	    	{
	    		@Override
	    		public void run() 
	    		{
	    			while (client.isConnected()) 
	    			{
	    				connected = true;
	    				first_conn = true;
	    				signal.waitForSignal();
	    				try 
	    				{
	    					reader.processMsgs();
	    				} 
	    				catch (Exception e) 
	    				{
	    					String message = e.getMessage();
	    					if 
	    					(
	    						message != null && !message.equals
	    						(
	    							"empty String"
	    						)
	    					)
	    					{
	    						System.out.println
	    						(
	    							"Exception: " + message
	    						);
	    					}
	    				}
	    			}
	    			
	    			connected = false;
	    	    }			
	    	}
	    )
	    .start();

	    int count = 0;
	    int max = 3;
	    while (global.last_id <= 0)
	    {
	    	misc.pause_min();
	    	
	    	count++;
	    	if (count >= max) break;
	    }	
	}
	
	private static int get_conn_port(String conn_type_)
	{
		int port = -1;
		
		if (conn_type_.equals(types.CONN_GATEWAY_PAPER)) port = PORT_GATEWAY_PAPER;
		else if (conn_type_.equals(types.CONN_GATEWAY)) port = PORT_GATEWAY;
		else if (conn_type_.equals(types.CONN_REAL)) port = PORT_REAL;
		else if (conn_type_.equals(types.CONN_PAPER)) port = PORT_PAPER;
		
		return port;
	}
}
