package db_ib;

import java.util.HashMap;

public abstract class execs 
{
	public static final String SOURCE = common.SOURCE_EXECS;
	
	public static final String USER = common.FIELD_USER;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String EXEC_ID = common.FIELD_EXEC_ID;
	public static final String ORDER_ID = common.FIELD_ORDER_ID;
	public static final String SIDE = common.FIELD_SIDE;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String FEES = common.FIELD_FEES;
	
	public static boolean exists(String exec_id_) { return common.exists(SOURCE, get_where_exec_id(exec_id_)); }

	public static boolean insert(HashMap<String, Object> vals_) { return common.insert(SOURCE, vals_); }
	
	public static String get_where_exec_id(String exec_id_) { return common.get_where(SOURCE, EXEC_ID, exec_id_, false); }
}