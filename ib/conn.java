package ib;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory.misc;
import accessory.numeric;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib.types;

public class conn 
{
	private volatile static boolean connected = false;
	private volatile static boolean first_conn = false;
	
	private static EClientSocket client;
	private static wrapper wrapper;
	private static int conn_id; 
	private static int conn_port;
	
	public static boolean start(int conn_id_, String conn_type_)
	{
		String error = null;
		
		if (!numeric.is_ok(conn_id_, limits.IB_CONN_ID_MIN, limits.IB_CONN_ID_MAX)) error = types.ERROR_CONN_ID;
		else 
		{
			conn_id = conn_id_;
			String conn_type = types.check_subtype(conn_type_);
			
			if (strings.is_ok(conn_type)) conn_port = get_conn_port(conn_type);
			else error = types.ERROR_CONN_TYPE;
		}
		
		if (!strings.is_ok(error))
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
		vars.last_id = 0;
				
		final EReaderSignal signal = wrapper.getSignal();

		//! [connect]
		client.eConnect("127.0.0.1", conn_port, conn_id);
		//! [connect]
		//! [ereader]
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
	    while (vars.last_id <= 0)
	    {
	    	misc.pause_min();
	    	
	    	count++;
	    	if (count >= max) break;
	    }	
	}
	
	private static int get_conn_port(String conn_type_)
	{
		int port = -1;
		
		if (conn_type_.equals(types.IB_CONN_GATEWAY_PAPER)) port = params.IB_PORT_GATEWAY_PAPER;
		else if (conn_type_.equals(types.IB_CONN_GATEWAY)) port = params.IB_PORT_GATEWAY;
		else if (conn_type_.equals(types.IB_CONN_REAL)) port = params.IB_PORT_REAL;
		else if (conn_type_.equals(types.IB_CONN_PAPER)) port = params.IB_PORT_PAPER;
		
		return port;
	}
}
