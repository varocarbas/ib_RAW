package ib;

import java.util.HashMap;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import accessory._keys;
import accessory.misc;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.errors;
import accessory_ib._types;
import external_ib.calls;
import external_ib.wrapper;

public abstract class conn extends parent_static 
{		
	public static final String TYPE = _types.CONN_TYPE;
	public static final String TYPE_TWS_REAL = _types.CONN_TYPE_TWS_REAL;
	public static final String TYPE_TWS_PAPER = _types.CONN_TYPE_TWS_PAPER;
	public static final String TYPE_GATEWAY_REAL = _types.CONN_TYPE_GATEWAY_REAL;
	public static final String TYPE_GATEWAY_PAPER = _types.CONN_TYPE_GATEWAY_PAPER;

	public static final String CONFIG_CHECK_RUNNING = _types.CONFIG_CONN_CHECK_RUNNING;
	
	public static final int MIN_ID = 0;
	public static final int MAX_ID = 31;
	
	public static final int WRONG_ID = MIN_ID - 1;
	
	public static final String DEFAULT_TYPE = TYPE_GATEWAY_REAL;
	public static final int DEFAULT_ID = 18;  

	public static final int DEFAULT_PORT_TWS_REAL = 7496;
	public static final int DEFAULT_PORT_TWS_PAPER = 7497;
	public static final int DEFAULT_PORT_GATEWAY_REAL = 4001;
	public static final int DEFAULT_PORT_GATEWAY_PAPER = 4002;	

	public static final boolean DEFAULT_CHECK_RUNNING = false;	
	
	public static final String ERROR = _types.ERROR_IB;
	public static final String ERROR_NONE = _types.ERROR_IB_CONN_NONE;
	public static final String ERROR_ID = _types.ERROR_IB_CONN_ID;
	public static final String ERROR_TYPE = _types.ERROR_IB_CONN_TYPE;
	public static final String ERROR_GENERIC = _types.ERROR_IB_CONN_GENERIC;
	public static final String ERROR_IB = _types.ERROR_IB_CONN_IB;
	
	public static volatile boolean _started = false; 
	
	public static EClientSocket _client = null;

	private static final int PORT_TWS_REAL = DEFAULT_PORT_TWS_REAL;
	private static final int PORT_TWS_PAPER = DEFAULT_PORT_TWS_PAPER;
	private static final int PORT_GATEWAY_REAL = DEFAULT_PORT_GATEWAY_REAL;
	private static final int PORT_GATEWAY_PAPER = DEFAULT_PORT_GATEWAY_PAPER;

	private static final int IS_TEST_MAX_ATTEMPTS = 5;
	private static final int MANY_ATTEMPTS = 5000;
	private static final int CONN_TOO_LONG = 1000;
	
	private static final int DEFAULT_MAX_ATTEMPTS = numbers.MAX_INT;
	private static final boolean DEFAULT_FORCE_RUNNING = true;
	private static final boolean DEFAULT_IS_TEST = false;
	
	private static volatile boolean _is_connected = false;
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
	
	public static boolean check_running() { return config.get_conn_boolean(CONFIG_CHECK_RUNNING); }

	public static boolean check_running(boolean check_running_) { return config.update_conn(CONFIG_CHECK_RUNNING, check_running_); }
	
	public static boolean is_connected() { return _is_connected; }
	
	public static int check_connection() { return check_connection(_id); }
	
	public static int check_connection(int conn_id_)
	{
		if (!id_is_ok(conn_id_) || conn.is_connected()) return conn_id_;
		
		connect();
		
		return _id;
	}
	
	public static boolean ib_is_ok(int id_, String type_) { return start(id_, type_, DEFAULT_FORCE_RUNNING, true); }
	
	public static boolean start() { return start(get_conn_id(), get_conn_type(), DEFAULT_FORCE_RUNNING, DEFAULT_IS_TEST); }

	public static boolean start(int id_, String type_) { return start(id_, type_, DEFAULT_FORCE_RUNNING, DEFAULT_IS_TEST); }
	
	public static boolean start(int id_, String type_, boolean force_running_, boolean is_test_) { return start(id_, type_, force_running_, is_test_, DEFAULT_MAX_ATTEMPTS); }
	
	public static boolean start(int id_, String type_, boolean force_running_, boolean is_test_, int max_attempts_)
	{
		boolean output = false;
		
		if (is_test_) wrapper_errors.log_errors(false);
		else update_is_connected(false);
		
		String error = null;

		if (!id_is_ok(id_)) error = ERROR_ID;
		else if (!type_is_ok(type_)) error = ERROR_TYPE;

		if (error != null)
		{
			errors.manage(error);

			return output;
		}
		
		_id = id_;
		_type = type_;
		
		update_port(type_);
		
		_wrapper = new wrapper();
		_client = _wrapper.get_client();

		output = connect(force_running_, is_test_, max_attempts_);
		
		if (is_test_) wrapper_errors.log_errors(wrapper_errors.DEFAULT_LOG_ERRORS);
		
		return output;
	}
	
	public static boolean connect() { return connect(DEFAULT_FORCE_RUNNING, DEFAULT_IS_TEST); }
	
	public static boolean connect(boolean force_running_, boolean is_test_) { return connect(force_running_, is_test_, DEFAULT_MAX_ATTEMPTS); }
	
	public static boolean connect(boolean force_running_, boolean is_test_, int max_attempts_)
	{	
		if (_wrapper == null || _client == null || !id_is_ok() || !type_is_ok() || !port_is_ok()) return start(get_conn_id(), get_conn_type(), force_running_, is_test_);
		if (_is_connected) return true;
		
		boolean check_running = false;
		
		if (!is_test_)
		{
			update_is_connected(false);
			
			check_running = check_running();
			
			if (!_first_conn && check_running && force_running_) check_running = false;
		}
		
		int max = max_attempts_;
		
		if (is_test_) max = IS_TEST_MAX_ATTEMPTS;
		else if (max <= 0) max = DEFAULT_MAX_ATTEMPTS;
		
		boolean many_attempts = (max >= MANY_ATTEMPTS);
		boolean stopped = false;
		
		int count = 0;
		
		while (!_is_connected && (is_test_ || !check_running || apps.is_running()))
		{
			count++;
			
			connect_internal();
			
			if (_is_connected || count > max) break;
			
			if (!_first_conn || is_test_) misc.pause_secs(1);
			else if (_first_conn)
			{
				misc.pause_loop();
				
				if (!stopped && many_attempts && (count >= CONN_TOO_LONG))
				{
					check_running = false;
					stopped = true;
					
					apps.stop();				
				}
			}
			
			update_port();
		}
		
		if (_is_connected)
		{
			if (stopped) apps.start();
			
			if (is_test_) end();
			else apps.update_is_connected(true);
		}
				
		return _is_connected;
	}

	public static void end() { disconnect(); }

	public static void disconnect() { calls.eDisconnect(); }

	public static boolean id_is_ok() { return id_is_ok(_id); }

	public static boolean id_is_ok(int id_) { return numbers.is_ok(id_, MIN_ID, MAX_ID); }
	
	public static boolean type_is_ok() { return type_is_ok(get_conn_type()); }
	
	public static boolean type_is_ok(String type_) { return strings.is_ok(check_type(type_)); }
	
	public static String check_type(String type_) { return accessory._types.check_type(type_, TYPE); }
	
	public static String get_account_type(String conn_type_) 
	{
		if (!type_is_ok(conn_type_)) return null;
		
		return (type_is_real(conn_type_) ? basic.TYPE_REAL : basic.TYPE_PAPER); 
	}

	public static boolean port_is_ok() { return port_is_ok(_port); }
	
	public static boolean port_is_ok(int port_) { return (port_ == PORT_TWS_REAL || port_ == PORT_TWS_PAPER || port_ == PORT_GATEWAY_REAL || port_ == PORT_GATEWAY_PAPER); }
	
	public static String check_error(String type_) { return accessory._types.check_type(type_, ERROR); }

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

	public static boolean encrypt_credentials_app(String username_, String password_) { return encrypt_credentials_app(type_is_real(), username_, password_); }

	public static boolean encrypt_credentials_app(boolean is_real_, String username_, String password_) { return encrypt_credentials_app(is_real_, basic.get_user(), username_, password_); }
	
	public static boolean encrypt_credentials_app(boolean is_real_, String user_, String username_, String password_) { return conn_apps.encrypt_credentials(is_real_, user_, username_, password_); }
	
	public static boolean run_app() { return run_app(basic.get_user()); }
	
	public static boolean run_app(String user_credentials_) { return run_app(type_is_gateway(), type_is_real(), user_credentials_); }
	
	public static boolean run_app(String type_, String user_credentials_) { return run_app(type_is_gateway(type_), type_is_real(type_), user_credentials_); }
	
	public static boolean run_app(boolean is_gateway_, boolean is_real_, String user_credentials_) { return conn_apps.run(is_gateway_, is_real_, user_credentials_); }
	
	public static HashMap<String, String> get_credentials(boolean is_real_, String user_) { return conn_apps.get_credentials(is_real_, user_); }
	
	public static boolean type_is_real(String type_) { return (strings.matches_any(new String[] { TYPE_TWS_REAL, TYPE_GATEWAY_REAL }, type_, false)); }

	static boolean type_is_real() { return type_is_real(get_conn_type()); }

	static boolean type_is_gateway() { return type_is_gateway(get_conn_type()); }
	
	private static boolean type_is_gateway(String type_) { return (strings.matches_any(new String[] { TYPE_GATEWAY_PAPER, TYPE_GATEWAY_REAL }, type_, false)); }
	
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
			update_is_connected(true);
			
			_first_conn = true;
			
			signal_.waitForSignal();
			
			try { reader_.processMsgs(); } 
			catch (Exception e) 
			{
				String message = e.getMessage();							
				if (strings.is_ok(message)) accessory_ib.errors.manage(ERROR_GENERIC, message);
			}
		}

		update_is_connected(false);
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
	
	private static void update_is_connected(boolean is_connected_)
	{
		_is_connected = is_connected_;
		
		apps.update_is_connected(is_connected_);
	}
}