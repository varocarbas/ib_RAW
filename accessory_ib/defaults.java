package accessory_ib;

import accessory.keys;
import ib.orders;

public class defaults 
{
	public static final String CONN_TYPE = types.CONN_GATEWAY;
	
	public static final int ORDER_ID = orders.MIN_ID - 1;
	
	public static final int SYNC_ID = 5000; //Not an order_id within limits.ORDER_ID_MIN/MAX.
	
	public static final String ASYNC_STORAGE = keys.DB;
	
	public static final String DB_TABLE_CONN = "ib_conn";
	public static final String DB_TABLE_EXECS = "ib_execs";
	
	static { _ini.load(); }
}
