package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;

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

	public static boolean is_ok() { return enabled(); }

	public static void enable() { enable(true); }
	
	public static void enable(boolean trades_too_) { enabled(true, trades_too_); }
	
	public static void disable() { disable(true); }
	
	public static void disable(boolean trades_too_) { enabled(false, trades_too_); }
	
	public static boolean enabled() { return async_execs._enabled; }

	public static void enabled(boolean enabled_) { enabled(enabled_, true); }
	
	public static void enabled(boolean enabled_, boolean trades_too_) 
	{ 
		async_execs._enabled = enabled_; 
	
		if (trades_too_)
		{
			if (trades.synced_with_execs()) trades.enabled(enabled_);
		}
	}
	
	public static double get_realised(int order_id_main_, int order_id_sec_) { return get_realised_unrealised(order_id_main_, order_id_sec_); }

	public static double get_unrealised(int order_id_main_) { return get_realised_unrealised(order_id_main_, ib.common.WRONG_ORDER_ID); }
	
	public static double get_investment(double price_, double quantity_) { return get_investment(price_, quantity_, common.WRONG_MONEY); }

	public static double get_investment(int order_id_main_, double fees_)
	{
		double price = db_ib.trades.get_price(order_id_main_);
		
		return (price > ib.common.WRONG_PRICE ? get_investment(price, db_ib.orders.get_quantity(order_id_main_), fees_) : common.WRONG_MONEY);		
	}

	public static double get_investment(double price_, int order_id_main_) { return (price_ > ib.common.WRONG_PRICE ? get_investment(price_, db_ib.orders.get_quantity(order_id_main_), db_ib.execs.get_fees(order_id_main_)) : common.WRONG_MONEY); }
	
	public static double get_investment(double price_, double quantity_, double fees_)
	{
		double output = common.WRONG_MONEY;
		if (price_ <= ib.common.WRONG_PRICE) return output;
		
		output = quantity_ * price_;
		if (common.money_is_ok(fees_)) output -= fees_;
		
		return output;
	}
	
	private static double get_realised_unrealised(int order_id_main_, int order_id_sec_)
	{
		double output = common.WRONG_MONEY;
		if (order_id_main_ <= common.WRONG_ORDER_ID) return output;
		
		ArrayList<HashMap<String, String>> start_info = db_ib.execs.get_money_info(order_id_main_);
		if (!arrays.is_ok(start_info)) return output;

		double start = get_total(start_info);		
	
		ArrayList<HashMap<String, String>> end_info = (order_id_sec_ <= common.WRONG_ORDER_ID ? null : db_ib.execs.get_money_info(order_id_sec_));
		
		double end = (arrays.is_ok(end_info) ? get_total(end_info) : get_investment(order_id_main_, get_fees(start_info)));
		
		return (common.money_is_ok(end) ? (end - start) : common.WRONG_MONEY);
	}

	private static double get_total(ArrayList<HashMap<String, String>> info_) { return get_total_fees(info_, false); }

	private static double get_fees(ArrayList<HashMap<String, String>> info_) { return get_total_fees(info_, true); }
	
	private static double get_total_fees(ArrayList<HashMap<String, String>> info_, boolean just_fees_)
	{
		double output = 0.0;
		
		for (HashMap<String, String> item: info_) 
		{ 
			double fee = Double.parseDouble(item.get(FEES));
			output += (just_fees_ ? fee : get_investment(Double.parseDouble(item.get(PRICE)), Double.parseDouble(item.get(QUANTITY)), fee)); 
		}
		
		return output;
	}
}