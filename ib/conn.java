package ib;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory.misc;
import accessory.numbers;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib.types;
import external_ib.wrapper;

public class conn 
{
	public static final String TYPE = types.CONFIG_CONN_TYPE; 
	public static final String ID = types.CONFIG_CONN_ID; 
	
	public static volatile boolean _valid_id = false; 

	static EClientSocket _client;

	private static volatile boolean _connected = false;
	private static volatile boolean _first_conn = false;
	private static wrapper _wrapper;
	private static int _id; 
	private static int _port;

	private static final int MIN_ID = 0;
	private static final int MAX_ID = 31;

	private static final int PORT_REAL = 7496;
	private static final int PORT_PAPER = 7497;
	private static final int PORT_GATEWAY = 4001;
	private static final int PORT_GATEWAY_PAPER = 4002;

	public static String check(String type_)
	{
		return accessory_ib.types.check_conn(type_, accessory.types.ACTION_ADD);
	}

	public static boolean is_ok()
	{		
		if (!_connected) connect();

		if (!_connected) errors.manage(types.ERROR_IB_CONN_NONE);

		return _connected;
	}

	public static boolean start() { return start(strings.to_number_int(accessory_ib.config.get_conn(ID)), accessory_ib.config.get_conn(TYPE)); }
	
	public static boolean start(int id_, String type_)
	{
		String error = null;

		if (!numbers.is_ok(id_, MIN_ID, MAX_ID)) error = types.ERROR_IB_CONN_ID;
		else 
		{
			_id = id_;
			String type = check(type_);	

			if (strings.is_ok(type)) _port = get_conn_port(type);
			else error = types.ERROR_IB_CONN_TYPE;
		}

		if (strings.is_ok(error))
		{
			errors.manage(error);

			return false;
		}

		_wrapper = new wrapper();
		_client = _wrapper.getClient();

		connect();

		return _connected;
	}

	public static void end() { if (_client != null) _client.eDisconnect(); }

	public static boolean check_connection()
	{
		if (!_connected) connect();

		return _connected;
	}

	private static void connect()
	{	
		_connected = false;

		while (!_connected)
		{	
			connect_internal();	

			if (_connected) break;
			if (!_first_conn) misc.pause_secs(1);

			errors.manage(types.ERROR_IB_CONN_NONE);
		}
	}

	private static void connect_internal()
	{
		_valid_id = false;

		final EReaderSignal signal = _wrapper.getSignal();
		_client.eConnect("127.0.0.1", _port, _id);
		final EReader reader = new EReader(_client, signal);   

		reader.start();

		new Thread
		(
			new Runnable()
			{
				@Override
				public void run() 
				{
					while (_client.isConnected()) 
					{
						_connected = true;
						_first_conn = true;
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
								System.out.println("Exception: " + message);
							}
						}
					}

					_connected = false;
				}			
			}
		)
		.start();

		int count = 0;
		int max = 3;
		while (!_valid_id)
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
