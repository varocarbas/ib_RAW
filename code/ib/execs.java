package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_common;
import accessory.misc;
import external_ib.orders;

public abstract class execs 
{
	public static final String DB_SOURCE = db_ib.execs.SOURCE;

	public static final String DB_SYMBOL = db_ib.execs.SYMBOL;
	public static final String DB_EXEC_ID = db_ib.execs.EXEC_ID;
	public static final String DB_ORDER_ID = db_ib.execs.ORDER_ID;
	public static final String DB_SIDE = db_ib.execs.SIDE;
	public static final String DB_PRICE = db_ib.execs.PRICE;
	public static final String DB_QUANTITY = db_ib.execs.QUANTITY;
	public static final String DB_FEES = db_ib.execs.FEES;
	
	public static final String SIDE_BOUGHT = orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = orders.EXEC_SIDE_SOLD;
	
	public static final int PAUSE_SECS_RETRY = 5;
	
	public static final double WRONG_MONEY = common.WRONG_MONEY;
	public static final double WRONG_PRICE = common.WRONG_PRICE;
	public static final int WRONG_QUANTITY = (int)common.WRONG_QUANTITY;
	
	public static final boolean DEFAULT_IS_QUICK = true;
	
	private static final String INVESTMENT = "investment";
	
	public static boolean is_quick() { return db_common.is_quick(DB_SOURCE); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(DB_SOURCE, is_quick_); }

	public static boolean is_ok() { return enabled(); }

	public static void enable() { enabled(true); }
	
	public static void disable() { enabled(false); }
	
	public static boolean enabled() { return async_execs._enabled; }

	public static void enabled(boolean enabled_) { async_execs._enabled = enabled_; }
	
	public static int get_order_id_buy(int order_id_sell_) { return _order.get_id_main(order_id_sell_); }
	
	public static int get_order_id_sell(int order_id_buy_) { return _order.get_id_sec(order_id_buy_); }

	public static String get_side(boolean is_main_) { return (is_main_ ? SIDE_BOUGHT : SIDE_SOLD); }

	public static String get_symbol(HashMap<String, String> vals_) { return (String)db_ib.execs.get_val(DB_SYMBOL, vals_); }

	public static String get_exec_id(HashMap<String, String> vals_) { return (String)db_ib.execs.get_val(DB_EXEC_ID, vals_); }

	public static int get_order_id(HashMap<String, String> vals_) { return (int)db_ib.execs.get_val(DB_ORDER_ID, vals_); }

	public static String get_side(HashMap<String, String> vals_) { return (String)db_ib.execs.get_val(DB_SIDE, vals_); }

	public static double get_price(HashMap<String, String> vals_) { return (double)db_ib.execs.get_val(DB_PRICE, vals_); }

	public static double get_quantity(HashMap<String, String> vals_) { return (double)db_ib.execs.get_val(DB_QUANTITY, vals_); }

	public static double get_fees(HashMap<String, String> vals_) { return (double)db_ib.execs.get_val(DB_FEES, vals_); }

	public static boolean side_is_main(String side_) { return get_side(true).equals(side_); }

	public static boolean is_filled(int order_id_main_) { return db_ib.execs.is_filled(order_id_main_); }

	public static boolean is_filled(String symbol_) { return arrays.is_ok(db_ib.execs.get_order_ids_filled(symbol_)); }

	public static boolean is_completed(int order_id_main_) { return db_ib.execs.is_completed(order_id_main_); }

	public static boolean is_completed(String symbol_) { return arrays.is_ok(db_ib.execs.get_order_ids_completed(symbol_)); }

	public static ArrayList<Integer> get_order_ids_filled(String symbol_) { return db_ib.execs.get_order_ids_filled(symbol_); }

	public static ArrayList<Integer> get_order_ids_completed(String symbol_) { return db_ib.execs.get_order_ids_completed(symbol_); }

	public static ArrayList<HashMap<String, String>> get_all_filled() { return db_ib.execs.get_all_filled(); }

	public static double get_average_fees() { return db_ib.execs.get_average_fees(); }

	public static double get_start_price(int order_id_main_) { return (db_ib.execs.order_id_exists(order_id_main_, true) ? get_start_end_price(order_id_main_) : common.WRONG_PRICE); }

	public static double get_end_price(int order_id_sec_) { return (db_ib.execs.order_id_exists(order_id_sec_, false) ? get_start_end_price(order_id_sec_) : common.WRONG_PRICE); }

	public static double get_investment(int order_id_, boolean is_main_) { return (db_ib.execs.order_id_exists(order_id_, is_main_) ? get_investment(order_id_) : WRONG_MONEY); }

	public static double get_fees(int order_id_, boolean is_main_) { return (db_ib.execs.order_id_exists(order_id_, is_main_) ? get_fees(order_id_) : WRONG_MONEY); }

	public static double get_quantity(int order_id_, boolean is_main_) { return (db_ib.execs.order_id_exists(order_id_, is_main_) ? get_quantity(order_id_) : 0); }

	public static String get_symbol(int order_id_) { return db_ib.execs.get_symbol(order_id_); }
	
	public static double get_unrealised(int order_id_main_) 
	{	
		double output = WRONG_MONEY;

		ArrayList<HashMap<String, String>> info = db_ib.execs.get_basic_info(order_id_main_);

		double start = get_investment(info);
		if (start == WRONG_MONEY) return output;
		
		double quantity = ib.orders.get_quantity(order_id_main_);
		if (quantity == common.WRONG_QUANTITY) quantity = get_quantity(info);
		if (quantity == common.WRONG_QUANTITY) return output;

		double price = common.get_price(get_symbol(info.get(0)));
		if (!common.price_is_ok(price)) return output;

		double fees = get_fees(info);
		if (fees == WRONG_MONEY) fees = 0.0;

		return (get_investment(price, quantity, fees) - start);
	}

	public static double get_realised() 
	{
		double output = 0.0;
		
		ArrayList<Integer> order_ids = db_ib.execs.get_order_ids_completed(null);
		if (!arrays.is_ok(order_ids)) return output;

		for (int order_id: order_ids) { output += get_realised_main(order_id); }
		
		return output;
	}
	
	public static double get_realised(int order_id_sec_) { return get_realised_internal(_order.get_id_main(order_id_sec_), order_id_sec_, true); }
	
	public static double get_realised_main(int order_id_main_) { return get_realised_internal(order_id_main_, _order.get_id_sec(order_id_main_), true); }

	public static boolean is_ok(double val_) { return (val_ > WRONG_MONEY); }
	
	public static boolean is_ok(int val_) { return (val_ > WRONG_QUANTITY); }
	
	public static double get_max_sell_price(String symbol_)
	{
		double output = WRONG_PRICE;
		
		ArrayList<HashMap<String, String>> all = db_ib.execs.get_all_completed(symbol_);
		if (!arrays.is_ok(all)) return output;
		
		output = 0.0;
		
		for (HashMap<String, String> item: all)
		{
			double price = get_sell_price(get_order_id_sell(get_order_id(item)));

			if (price > output) output = price;
		}
		
		return output;
	}
	
	public static double get_sell_price(int order_id_sell_) { return db_ib.execs.get_end_price(order_id_sell_); }
	
	private static double get_realised_internal(int order_id_main_, int order_id_sec_, boolean retry_) 
	{
		double output = 0.0;
		
		double sum = 0.0;
		double fees = 0.0;
		
		double[] quantities = new double[] { 0.0, 0.0 };
		
		for (int i = 0; i < 2; i++)
		{
			ArrayList<HashMap<String, String>> info = db_ib.execs.get_basic_info(i == 0 ? order_id_main_ : order_id_sec_); 
			if (!arrays.is_ok(info)) return output;

			for (HashMap<String, String> item: info) 
			{
				double quantity = get_quantity(item);
						
				quantities[i] += quantity;
				
				sum += (i == 0 ? -1 : 1) * (get_price(item) * quantity); 

				fees += get_fees(item);
			}
		}
		
		if (quantities[0] != quantities[1])
		{
			if (retry_)
			{
				misc.pause_secs(PAUSE_SECS_RETRY);
				
				output = get_realised_internal(order_id_main_, order_id_sec_, false);
			}
		}
		else output = sum - fees;
		
		return output;
	}

	private static double get_start_end_price(int order_id_)
	{
		double output = common.WRONG_PRICE;

		ArrayList<HashMap<String, String>> info = db_ib.execs.get_basic_info(order_id_);
		
		int tot = arrays.get_size(info);
		if (tot < 1) return output;
		if (tot == 1) return get_price(info.get(0));
		
		double sum = 0.0;
		double quantity_tot = 0.0;
		
		for (HashMap<String, String> item: info)
		{
			double price = get_price(item);
			double quantity = get_quantity(item);
			
			sum += get_investment(price, quantity);
			quantity_tot += quantity;
		}
		
		return (quantity_tot == 0.0 ? output : sum / quantity_tot);
	}

	private static double get_fees(int order_id_) { return db_ib.execs.get_fees(order_id_); }
	
	private static double get_fees(ArrayList<HashMap<String, String>> info_) { return get_common(info_, DB_FEES); }

	private static double get_quantity(int order_id_) { return db_ib.execs.get_quantity(order_id_); }
	
	private static double get_quantity(ArrayList<HashMap<String, String>> info_) { return get_common(info_, DB_QUANTITY); }

	private static double get_investment(int order_id_) { return get_common(order_id_, INVESTMENT); }
	
	private static double get_investment(ArrayList<HashMap<String, String>> info_) { return get_common(info_, INVESTMENT); }

	private static double get_common(int order_id_, String what_) { return get_common(db_ib.execs.get_basic_info(order_id_), what_); }
	
	private static double get_common(ArrayList<HashMap<String, String>> info_, String what_)
	{
		double output = WRONG_MONEY;

		boolean is_price = what_.equals(DB_PRICE);
		boolean is_investment = what_.equals(INVESTMENT);
			
		if (is_price) output = common.WRONG_PRICE;
		else if (what_.equals(DB_QUANTITY)) output = common.WRONG_QUANTITY;
		
		int tot = arrays.get_size(info_);
		if (tot < 1) return output;
		if (tot == 1) return (is_investment ? get_investment(info_.get(0)) : db_ib.execs.get_val(info_.get(0), what_));
		
		double sum = 0.0;
		double quantity_tot = 0.0;

		double fees = 0.0;
		
		for (HashMap<String, String> item: info_) 
		{
			if (is_price || is_investment)
			{
				double price = get_price(item);
				double quantity = get_quantity(item);			
				
				fees += (is_price ? 0.0 : get_fees(item));
				
				sum += get_investment(price, quantity);
				
				if (is_price) quantity_tot += quantity;				
			}
			else sum += db_ib.execs.get_val(item, what_); 
		}
		
		if (is_price || is_investment) sum -= fees;
		
		return (is_price ? (sum / quantity_tot) : sum);
	}

	private static double get_investment(HashMap<String, String> item_) { return get_investment(get_price(item_), get_quantity(item_), get_fees(item_)); }

	private static double get_investment(double price_, double quantity_) { return (price_ * quantity_); }
	
	private static double get_investment(double price_, double quantity_, double fees_) { return ((price_ * quantity_) - fees_); }
}