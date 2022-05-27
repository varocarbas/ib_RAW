package ib;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.errors;
import accessory_ib.types;
import external_ib.wrapper;

public class conn extends parent_static 
{
	public static final String TYPE = types.CONFIG_CONN_TYPE; 
	public static final String ID = types.CONFIG_CONN_ID; 
	
	public static final String TWS_REAL = types.CONN_TWS_REAL;
	public static final String TWS_PAPER = types.CONN_TWS_PAPER;
	public static final String GATEWAY_REAL = types.CONN_GATEWAY_REAL;
	public static final String GATEWAY_PAPER = types.CONN_GATEWAY_PAPER;
	
	public static final String ERROR_NONE = types.ERROR_IB_CONN_NONE;
	public static final String ERROR_ID = types.ERROR_IB_CONN_ID;
	public static final String ERROR_TYPE = types.ERROR_IB_CONN_TYPE;
	public static final String ERROR_GENERIC = types.ERROR_IB_CONN_GENERIC;
	
	public static volatile boolean _id_is_ok = false; 

	static EClientSocket _client;

	private static volatile boolean _connected = false;
	private static volatile boolean _first_conn = false;
	private static wrapper _wrapper;
	private static int _id; 
	private static int _port;

	private static final int MIN_ID = 0;
	private static final int MAX_ID = 31;

	private static final int PORT_TWS_REAL = 7496;
	private static final int PORT_TWS_PAPER = 7497;
	private static final int PORT_GATEWAY_REAL = 4001;
	private static final int PORT_GATEWAY_PAPER = 4002;

	public static String get_id() { return accessory.types.get_id(types.ID_CONN); }
	
	public static boolean is_ok()
	{		
		if (!_connected) connect();

		if (!_connected) errors.manage(ERROR_NONE);

		return _connected;
	}

	public static boolean start() { return start(strings.to_number_int(config.get_conn(ID)), config.get_conn(TYPE)); }
	
	public static boolean start(int id_, String type_)
	{
		String error = null;

		if (!numbers.is_ok(id_, MIN_ID, MAX_ID)) error = ERROR_ID;
		else 
		{
			String type = accessory.types.check_type(type_, types.CONN);	
			if (!strings.is_ok(type)) error = ERROR_TYPE;
			else
			{
				_id = id_;
				_port = get_conn_port(type);
			}
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

	public static boolean connection_is_ok()
	{
		if (!_connected) connect();

		return _connected;
	}
	
	public static String check_error(String type_) { return accessory.types.check_type(type_, types.ERROR_IB_CONN); }

	public static String get_error_message(String type_)
	{
		String message = strings.DEFAULT;
		
		String type = check_error(type_);
		if (!strings.is_ok(type)) return message;
		
		if (type_.equals(ERROR_NONE)) message = "Impossible to connect";
		else if (type_.equals(ERROR_ID)) message = "Wrong connection ID";
		else if (type_.equals(ERROR_TYPE)) message = "Wrong connection type";

		return message;	
	}

	private static void connect()
	{	
		_connected = false;

		while (!_connected)
		{	
			connect_internal();	

			if (_connected) break;
			if (!_first_conn) misc.pause_secs(1);

			errors.manage(ERROR_NONE);
		}
	}

	private static void connect_internal()
	{
		_id_is_ok = false;

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
						
						try { reader.processMsgs(); } 
						catch (Exception e) 
						{
							String message = e.getMessage();
							if (!strings.are_equal(message, "empty String")) accessory_ib.errors.manage(ERROR_GENERIC, message);
						}
					}

					_connected = false;
				}			
			}
		)
		.start();

		int count = 0;
		int max = 3;
		while (!_id_is_ok)
		{
			misc.pause_loop();

			count++;
			if (count >= max) break;
		}	
	}

	private static int get_conn_port(String conn_type_)
	{
		int port = -1;

		if (conn_type_.equals(types.CONN_TWS_REAL)) port = PORT_TWS_REAL;
		else if (conn_type_.equals(types.CONN_TWS_PAPER)) port = PORT_TWS_PAPER;
		else if (conn_type_.equals(types.CONN_GATEWAY_REAL)) port = PORT_GATEWAY_REAL;
		else if (conn_type_.equals(types.CONN_GATEWAY_PAPER)) port = PORT_GATEWAY_PAPER;

		return port;
	}
}
