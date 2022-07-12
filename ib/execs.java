package ib;

public abstract class execs 
{
	public static final String USER = db_ib.execs.USER;
	public static final String SYMBOL = db_ib.execs.SYMBOL;
	public static final String ORDER_ID = db_ib.execs.ORDER_ID;
	public static final String PRICE = db_ib.execs.PRICE;
	public static final String QUANTITY = db_ib.execs.QUANTITY;
	public static final String SIDE = db_ib.execs.SIDE;
	public static final String FEES = db_ib.execs.FEES;
	public static final String EXEC_ID = db_ib.execs.EXEC_ID;
	
	public static void enable() { async_execs._enabled = true; }

	public static void disable() { async_execs._enabled = false; }
}