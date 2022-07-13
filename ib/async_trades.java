package ib;

import accessory.parent_static;
import accessory.strings;
import db_ib.trades;

abstract class async_trades extends parent_static
{	
	public static final String STATUS_FILLED = order.STATUS_FILLED;
	public static final String STATUS_INACTIVE = order.STATUS_INACTIVE;

	public static boolean order_id_is_ok(int order_id_) { return trades.is_ok(order_id_); }
	
	public static boolean order_id_exists(int order_id_) { return trades.exists(order_id_); }

	public static void start(int order_id_) 
	{
		String symbol = get_symbol(order_id_);
		if (!strings.is_ok(symbol)) return;
		
		trades.insert(order_id_, symbol);
		
		async_data_trades._start(order_id_, symbol, false);
	}
	
	public static void end(int order_id_) 
	{
		trades.end(order_id_);
		
		async_data_trades._stop(order_id_, false);
	}
	
	public static boolean status_is_ok(String status_ib_, boolean is_add_) { return order.is_status(status_ib_, (is_add_ ? STATUS_FILLED : STATUS_INACTIVE)); }
	
	private static String get_symbol(int order_id_) { return trades.get_symbol(order_id_); }
}