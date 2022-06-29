package ib;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._keys;
import accessory_ib.config;
import accessory_ib.errors;
import accessory_ib.types;
import db_ib.basic;
import external_ib.wrapper;

public abstract class conn extends parent_static 
{
	public static final String CONFIG_TYPE = types.CONFIG_CONN_TYPE;
	public static final String CONFIG_CONN_ID = types.CONFIG_CONN_ID;
	public static final String CONFIG_ACCOUNT_ID = types.CONFIG_CONN_ACCOUNT_ID;
	
	public static final String TYPE_TWS_REAL = _keys.TWS_REAL;
	public static final String TYPE_TWS_PAPER = _keys.TWS_PAPER;
	public static final String TYPE_GATEWAY_REAL = _keys.GATEWAY_REAL;
	public static final String TYPE_GATEWAY_PAPER = _keys.GATEWAY_PAPER;
	
	public static final String DEFAULT_TYPE = TYPE_GATEWAY_REAL;
	public static final int DEFAULT_ID = 18; 
	public static final int DEFAULT_PORT_TWS_REAL = 7496;
	public static final int DEFAULT_PORT_TWS_PAPER = 7497;
	public static final int DEFAULT_PORT_GATEWAY_REAL = 4001;
	public static final int DEFAULT_PORT_GATEWAY_PAPER = 4002;	
	
	public static final String ERROR_NONE = types.ERROR_IB_CONN_NONE;
	public static final String ERROR_ID = types.ERROR_IB_CONN_ID;
	public static final String ERROR_TYPE = types.ERROR_IB_CONN_TYPE;
	public static final String ERROR_GENERIC = types.ERROR_IB_CONN_GENERIC;

	public static volatile boolean _started = false; 
	
	public static EClientSocket _client = null;
	
	private static final int MIN_ID = 0;
	private static final int MAX_ID = 31;

	private static final int PORT_TWS_REAL = DEFAULT_PORT_TWS_REAL;
	private static final int PORT_TWS_PAPER = DEFAULT_PORT_TWS_PAPER;
	private static final int PORT_GATEWAY_REAL = DEFAULT_PORT_GATEWAY_REAL;
	private static final int PORT_GATEWAY_PAPER = DEFAULT_PORT_GATEWAY_PAPER;

	private static wrapper _wrapper = null;
	private static int _id = MIN_ID - 1; 
	private static int _port = PORT_TWS_REAL - 1;
	private static volatile boolean _connected = false;
	private static volatile boolean _first_conn = false;
	
	public static int get_max_length_type() { return TYPE_GATEWAY_PAPER.length(); }
	
	public static boolean is_ok()
	{		
		if (!_connected) connect();

		if (!_connected) errors.manage(ERROR_NONE);

		return _connected;
	}

	public static String get_conn_type() { return (String)config.get_conn(CONFIG_TYPE); }

	public static String get_account_id() { return (String)config.get_conn(CONFIG_ACCOUNT_ID); }
	
	public static boolean account_id_is_ok(String account_id_) 
	{ 
		if (!conn_type_is_real()) return true;
		
		String account_id = get_account_id();
		
		return (!strings.is_ok(account_id) || strings.are_equal(account_id_, account_id));
	}

	public static boolean start() { return start((int)config.get_conn(types.CONFIG_CONN_ID), get_conn_type()); }
	
	public static boolean start(int id_, String type_)
	{
		String error = null;

		if (!numbers.is_ok(id_, MIN_ID, MAX_ID)) error = ERROR_ID;
		else 
		{
			if (!type_is_ok(type_)) error = ERROR_TYPE;
			else
			{
				basic.update_conn_type(type_);
				
				_id = id_;
				_port = get_port(type_);
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

	public static void end() { disconnect(); }

	public static void disconnect() { if (_client != null) _client.eDisconnect(); }

	public static boolean connection_is_ok()
	{
		if (!_connected) connect();

		return _connected;
	}

	public static boolean type_is_ok(String type_) { return (strings.is_ok(type_) ? arrays.value_exists(new String[] { TYPE_TWS_REAL, TYPE_TWS_PAPER, TYPE_GATEWAY_REAL, TYPE_GATEWAY_PAPER }, type_) : false); }
	
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
	
	private static boolean conn_type_is_real() 
	{
		String conn_type = get_conn_type();
		
		return (strings.are_equal(conn_type, TYPE_TWS_REAL) || strings.are_equal(conn_type, TYPE_GATEWAY_REAL));
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
		_started = false;

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
							if (strings.is_ok(message)) 
							{
								accessory_ib.errors.manage(ERROR_GENERIC, message);
								
								disconnect();
								break;
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
		while (!_started)
		{
			misc.pause_loop();

			count++;
			if (count >= max) break;
		}	
	}

	private static int get_port(String type_)
	{
		int port = -1;

		if (type_.equals(TYPE_TWS_REAL)) port = PORT_TWS_REAL;
		else if (type_.equals(TYPE_TWS_PAPER)) port = PORT_TWS_PAPER;
		else if (type_.equals(TYPE_GATEWAY_REAL)) port = PORT_GATEWAY_REAL;
		else if (type_.equals(TYPE_GATEWAY_PAPER)) port = PORT_GATEWAY_PAPER;

		return port;
	}
}