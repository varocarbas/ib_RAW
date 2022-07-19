package ib;

import accessory.parent_static;
import accessory.strings;
import db_ib.trades;

abstract class async_trades extends parent_static
{		
	public static final String STATUS_FILLED = order.STATUS_FILLED;
	public static final String STATUS_INACTIVE = order.STATUS_INACTIVE;

	public static final double WRONG_PRICE = common.WRONG_PRICE;
	
	public static void start(int order_id_, String status_ib_) 
	{
		if (!start_is_ok(order_id_, status_ib_)) return;
		
		start_internal(order_id_, WRONG_PRICE);
	}
	
	public static void start(int order_id_, double price_) 
	{
		if (!start_is_ok(order_id_)) return;

		start_internal(order_id_, price_);
	}

	public static void end(int order_id_, String status_ib_) 
	{
		if (!end_is_ok(order_id_, status_ib_)) return;
		
		end_internal(order_id_, WRONG_PRICE);
	}

	public static void end(int order_id_, double price_) 
	{
		if (!end_is_ok(order_id_)) return;

		end_internal(order_id_, price_);
	}
	
	private static void start_internal(int order_id_main_, double price_) 
	{	
		String symbol = trades.get_symbol(order_id_main_, true);
		if (!strings.is_ok(symbol)) return;
		
		trades.start(order_id_main_, symbol, price_);
		
		async_data_trades._start(symbol, false);
	}
	
	private static void end_internal(int order_id_sec_, double price_) 
	{
		String symbol = trades.get_symbol(order_id_sec_, false);
		
		trades.end(order_id_sec_, price_);
	
		if (!strings.is_ok(symbol)) return;
	
		async_data_trades._stop(symbol, false);		
	}	

	private static boolean start_is_ok(int order_id_, String status_ib_) { return ((!strings.is_ok(status_ib_) || status_is_ok(status_ib_, true)) && start_is_ok(order_id_)); }

	private static boolean start_is_ok(int order_id_) { return (!order_id_exists(order_id_, true) && order_id_is_ok(order_id_)); }

	private static boolean end_is_ok(int order_id_, String status_ib_) { return ((!strings.is_ok(status_ib_) || status_is_ok(status_ib_, false)) && end_is_ok(order_id_)); }

	private static boolean end_is_ok(int order_id_) { return (order_id_exists(order_id_, false)); }

	private static boolean status_is_ok(String status_ib_, boolean is_start_) { return order.is_status(status_ib_, (is_start_ ? STATUS_FILLED : STATUS_INACTIVE)); }

	private static boolean order_id_is_ok(int order_id_main_) { return db_ib.trades.is_ok(order_id_main_); }

	private static boolean order_id_exists(int order_id_, boolean is_start_) { return db_ib.trades.order_id_exists(order_id_, is_start_); }
}