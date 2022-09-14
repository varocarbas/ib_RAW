package ib;

import accessory.parent_static;

abstract class async_trades extends parent_static
{			
	public static void start(String symbol_, int order_id_, double start_) 
	{
		if (!start_is_ok(order_id_)) return;

		start_internal(symbol_, order_id_, start_);
	}

	public static void end(String symbol_, int order_id_, double end_) 
	{
		if (!end_is_ok(order_id_)) return;

		end_internal(symbol_, order_id_, end_);
	}
	
	private static void start_internal(String symbol_, int order_id_main_, double start_) 
	{
		db_ib.trades.start(symbol_, order_id_main_, start_);

		if (trades.synced_with_execs()) async_data_trades.start(symbol_);
	}
	
	private static void end_internal(String symbol_, int order_id_sec_, double end_) 
	{
		db_ib.trades.end(order_id_sec_, end_);
	
		if (trades.synced_with_execs()) async_data_trades.stop(symbol_);		
	}	

	private static boolean start_is_ok(int order_id_) { return !db_ib.trades.started(order_id_); }

	private static boolean end_is_ok(int order_id_) { return order_id_exists(order_id_, false); }

	private static boolean order_id_exists(int order_id_, boolean is_start_) { return db_ib.trades.order_id_exists(order_id_, is_start_); }
}