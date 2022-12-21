package ib;

import java.util.ArrayList;
import java.util.HashMap;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory._keys;
import accessory.arrays;
import accessory.misc;
import accessory.numbers;
import accessory.os;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.errors;
import accessory_ib.types;
import external_ib.calls;
import external_ib.wrapper;

public abstract class conn extends parent_static 
{	
	public static final String TYPE = types.CONN_TYPE;
	public static final String TYPE_TWS_REAL = types.CONN_TYPE_TWS_REAL;
	public static final String TYPE_TWS_PAPER = types.CONN_TYPE_TWS_PAPER;
	public static final String TYPE_GATEWAY_REAL = types.CONN_TYPE_GATEWAY_REAL;
	public static final String TYPE_GATEWAY_PAPER = types.CONN_TYPE_GATEWAY_PAPER;

	public static final int MIN_ID = 0;
	public static final int MAX_ID = 31;
	
	public static final int WRONG_ID = MIN_ID - 1;
	
	public static final String DEFAULT_TYPE = TYPE_GATEWAY_REAL;
	public static final int DEFAULT_ID = 18;  

	public static final int DEFAULT_PORT_TWS_REAL = 7496;
	public static final int DEFAULT_PORT_TWS_PAPER = 7497;
	public static final int DEFAULT_PORT_GATEWAY_REAL = 4001;
	public static final int DEFAULT_PORT_GATEWAY_PAPER = 4002;	

	public static final String ERROR = types.ERROR_IB;
	public static final String ERROR_NONE = types.ERROR_IB_CONN_NONE;
	public static final String ERROR_ID = types.ERROR_IB_CONN_ID;
	public static final String ERROR_TYPE = types.ERROR_IB_CONN_TYPE;
	public static final String ERROR_GENERIC = types.ERROR_IB_CONN_GENERIC;
	public static final String ERROR_IB = types.ERROR_IB_CONN_IB;
	
	public static volatile boolean _started = false; 
	
	public static EClientSocket _client = null;

	private static final int PORT_TWS_REAL = DEFAULT_PORT_TWS_REAL;
	private static final int PORT_TWS_PAPER = DEFAULT_PORT_TWS_PAPER;
	private static final int PORT_GATEWAY_REAL = DEFAULT_PORT_GATEWAY_REAL;
	private static final int PORT_GATEWAY_PAPER = DEFAULT_PORT_GATEWAY_PAPER;

	private static volatile boolean _connected = false;
	private static volatile boolean _first_conn = false;

	private static HashMap<String, String> _types_keys = null;
	
	private static wrapper _wrapper = null;
	private static int _id = WRONG_ID; 
	private static int _port = PORT_TWS_REAL - 1;
	private static String _type = strings.DEFAULT; 
	
	public static String get_type_key(String type_) 
	{ 
		populate_type_keys();
		
		return (_types_keys.containsKey(type_) ? _types_keys.get(type_) : strings.DEFAULT); 
	}
	
	public static int get_max_length_type_key() { return get_type_key(TYPE_GATEWAY_PAPER).length(); }

	public static String get_account_ib() { return basic.get_account_ib(); }

	public static int get_conn_id() { return (id_is_ok(_id) ? _id : apps.get_conn_id()); }

	public static String get_conn_type() { return (type_is_ok(_type) ? _type : apps.get_conn_type()); }

	public static String update_conn_type(String conn_type_) { return apps.update_conn_type(conn_type_); }
		
	public static boolean start() { return start(get_conn_id(), get_conn_type()); }
	
	public static boolean start(int id_, String type_)
	{
		_connected = false;
		
		String error = null;

		if (!id_is_ok(id_)) error = ERROR_ID;
		else if (!type_is_ok(type_)) error = ERROR_TYPE;

		if (error != null)
		{
			errors.manage(error);

			return false;
		}
		
		_id = id_;
		_type = type_;
		
		update_port(type_);
		
		_wrapper = new wrapper();
		_client = _wrapper.get_client();

		return connect();
	}
	
	public static boolean connect()
	{	
		if (_wrapper == null || _client == null || !id_is_ok() || !type_is_ok() || !port_is_ok()) return start();
		if (_connected) return true;
		
		_connected = false;
		
		while (!_connected)
		{
			if (!ib_is_running()) errors.manage(ERROR_IB);
			else connect_internal();	

			if (_connected) break;
			if (!_first_conn) misc.pause_secs(1);
			
			update_port();
		}
		
		return _connected;
	}

	public static void end() { disconnect(); }

	public static void disconnect() 
	{			
		calls.eDisconnect(); 
		
		_connected = false;
	}

	public static boolean id_is_ok() { return id_is_ok(_id); }

	public static boolean id_is_ok(int id_) { return numbers.is_ok(id_, MIN_ID, MAX_ID); }
	
	public static boolean type_is_ok() { return type_is_ok(get_conn_type()); }
	
	public static boolean type_is_ok(String type_) { return strings.is_ok(check_type(type_)); }
	
	public static String check_type(String type_) { return accessory.types.check_type(type_, TYPE); }
	
	public static boolean port_is_ok() { return port_is_ok(_port); }
	
	public static boolean port_is_ok(int port_) { return (port_ == PORT_TWS_REAL || port_ == PORT_TWS_PAPER || port_ == PORT_GATEWAY_REAL || port_ == PORT_GATEWAY_PAPER); }
	
	public static String check_error(String type_) { return accessory.types.check_type(type_, ERROR); }

	public static String get_error_message(String type_)
	{
		String message = strings.DEFAULT;
		
		String type = check_error(type_);
		if (!strings.is_ok(type)) return message;
		
		if (type_.equals(ERROR_NONE)) message = "Impossible to connect";
		else if (type_.equals(ERROR_ID)) message = "Wrong connection ID";
		else if (type_.equals(ERROR_TYPE)) message = "Wrong connection type";
		else if (type_.equals(ERROR_IB)) message = "The IB " + (type_is_gateway() ? "gateway" : "TWS") + " is stopped";
		
		return message;	
	}
	
	public static boolean ib_is_running() { return ib_is_running(get_conn_type()); }
	
	public static boolean ib_is_running(String type_)
	{	
		boolean output = false;
		
		String type = check_type(type_);
		if (!strings.is_ok(type)) return output;
		
		boolean is_gateway = type_is_gateway(type);
		boolean is_real = type_is_real(type);
		
		if (os.is_linux()) output = ib_is_running_linux(is_gateway, is_real);
		else output = true;
		
		return output;
	}
	
	static boolean type_is_real() { return type_is_real(get_conn_type()); }
	
	static boolean type_is_real(String type_) { return (strings.matches_any(type_, new String[] { TYPE_TWS_REAL, TYPE_GATEWAY_REAL }, false)); }

	private static boolean type_is_gateway() { return type_is_gateway(get_conn_type()); }
	
	private static boolean type_is_gateway(String type_) { return (strings.matches_any(type_, new String[] { TYPE_GATEWAY_PAPER, TYPE_GATEWAY_REAL }, false)); }
	
	private static boolean ib_is_running_linux(boolean is_gateway_, boolean is_real_)
	{
		boolean output = false;

		ArrayList<String> keywords = new ArrayList<String>();
		
		keywords.add("twslaunch");
		if (is_gateway_) keywords.add("ibgateway");
		else keywords.add("tws");
		
		output = os.app_is_running(null, arrays.to_array(keywords), null);
	
		if (output)
		{
			String[] keywords2 = null;
			String[] keywords_ignore = null;
	
			if (is_gateway_) keywords2 = new String[] { "ib gateway" };
			else
			{
				String account = basic.get_account_ib();
			
				keywords2 = new String[] { (strings.is_ok(account) ? account + " " : "") + "interactive brokers" + (is_real_ ? "" : " (simulated trading)") };
				keywords_ignore = (is_real_ ? new String[] { "login", "simulated trading" } : new String[] { "login" });				
			}
			
			output = arrays.is_ok(os.get_running_windows(keywords2, keywords_ignore, false));
		}
		
		return output;
	}
	
	private static void update_port() { update_port(get_conn_type()); }
	
	private static void update_port(String type_)
	{
		apps.update_conn_type(type_);
		
		_port = get_port(type_);
	}

	private static void connect_internal()
	{
		_started = false;

		connect_reader();

		int count = 0;
		int max = 3;
		
		while (!_started)
		{
			misc.pause_loop();
			
			count++;
			if (count >= max) return;
		}
	}
	
	private static void connect_reader()
	{
		final EReaderSignal signal = _wrapper.get_signal();
		
		calls.eConnect("127.0.0.1", _port, _id);
		
		final EReader reader = new EReader(_client, signal);   

		reader.start();
		
		new Thread
		(
			new Runnable()
			{
				@Override
				public void run() { connect_reader_loop(reader, signal); }			
			}				
		)
		.start();
	}

	private static void connect_reader_loop(EReader reader_, EReaderSignal signal_)
	{
		while (_client.isConnected()) 
		{
			_connected = true;
			_first_conn = true;
			
			signal_.waitForSignal();
			
			try { reader_.processMsgs(); } 
			catch (Exception e) 
			{
				String message = e.getMessage();							
				if (strings.is_ok(message)) accessory_ib.errors.manage(ERROR_GENERIC, message);
			}
		}

		_connected = false;
	}
	
	private static void populate_type_keys()
	{ 
		if (_types_keys != null) return;
		
		_types_keys = new HashMap<String, String>();
		
		for (String type: new String[] { TYPE_TWS_REAL, TYPE_TWS_PAPER, TYPE_GATEWAY_REAL, TYPE_GATEWAY_PAPER }) { _types_keys.put(type, _keys.get_key(type, TYPE)); }
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