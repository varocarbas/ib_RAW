package db_ib;

import java.util.HashMap;

public abstract class trades 
{
	public static final String SOURCE = common.SOURCE_TRADES;
	
	public static final String USER = common.FIELD_USER;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	public static final String TIME_ELAPSED = common.FIELD_TIME_ELAPSED;
	public static final String START = common.FIELD_START;
	public static final String STOP = common.FIELD_STOP;
	public static final String HALTED = common.FIELD_HALTED;
	public static final String UNREALISED = common.FIELD_UNREALISED;	

	public static boolean insert(int order_id_, String symbol) 
	{ 
		HashMap<String, Object> vals = new HashMap<String, Object>();
		vals.put(ORDER_ID_MAIN, order_id_);
		vals.put(SYMBOL, symbol);
		
		return common.insert(SOURCE, vals);
	}
}