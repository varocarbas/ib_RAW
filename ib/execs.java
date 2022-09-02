package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import external_ib.orders;

public abstract class execs 
{
	public static final String PRICE = db_ib.execs.PRICE;
	public static final String QUANTITY = db_ib.execs.QUANTITY;
	public static final String FEES = db_ib.execs.FEES;
	public static final String ORDER_ID = db_ib.execs.ORDER_ID;
	public static final String SIDE = db_ib.execs.SIDE;
	
	public static final String SIDE_BOUGHT = orders.EXEC_SIDE_BOUGHT;
	public static final String SIDE_SOLD = orders.EXEC_SIDE_SOLD;
	
	public static final double WRONG_MONEY = common.WRONG_MONEY;
	
	public static final boolean DEFAULT_IS_QUICK = true;
	
	private static final String INVESTMENT = "investment";
	
	private static boolean _is_quick = DEFAULT_IS_QUICK;

	public static boolean is_quick() { return _is_quick; }

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

	public static String get_side(boolean is_main_) { return (is_main_ ? SIDE_BOUGHT : SIDE_SOLD); }
	
	public static boolean side_is_main(String side_) { return get_side(true).equals(side_); }

	public static boolean is_filled(int order_id_main_) { return db_ib.execs.is_filled(order_id_main_); }

	public static boolean is_filled(String symbol_) { return arrays.is_ok(db_ib.execs.get_order_ids_filled(symbol_, _is_quick)); }

	public static boolean is_completed(int order_id_main_) { return db_ib.execs.is_completed(order_id_main_); }

	public static boolean is_completed(String symbol_) { return arrays.is_ok(db_ib.execs.get_order_ids_completed(symbol_, _is_quick)); }

	public static ArrayList<Integer> get_order_ids_filled(String symbol_) { return db_ib.execs.get_order_ids_filled(symbol_, _is_quick); }

	public static ArrayList<Integer> get_order_ids_completed(String symbol_) { return db_ib.execs.get_order_ids_completed(symbol_, _is_quick); }

	public static ArrayList<HashMap<String, String>> get_all_filled() { return db_ib.execs.get_all_filled(_is_quick); }

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
		
		double quantity = db_ib.orders.get_quantity(order_id_main_);
		if (quantity == common.WRONG_QUANTITY) quantity = get_quantity(info);
		if (quantity == common.WRONG_QUANTITY) return output;

		double price = common.get_price(info.get(0).get(db_ib.execs.SYMBOL));
		if (!common.price_is_ok(price)) return output;

		double fees = get_fees(info);
		if (fees == WRONG_MONEY) fees = 0.0;

		return (get_investment(price, quantity, fees) - start);
	}

	public static double get_realised(int order_id_sec_) 
	{
		int order_id_main = _order.get_id_main(order_id_sec_);
		
		double start = get_investment(order_id_main, true);
		double end = get_investment(order_id_sec_, false);
		
		return ((start != WRONG_MONEY && end != WRONG_MONEY) ? (end - start) : WRONG_MONEY);
	}
	
	private static double get_start_end_price(int order_id_)
	{
		double output = common.WRONG_PRICE;

		ArrayList<HashMap<String, String>> info = db_ib.execs.get_basic_info(order_id_);
		
		int tot = arrays.get_size(info);
		if (tot < 1) return output;
		if (tot == 1) return get_val(info.get(0), PRICE);
		
		double sum = 0.0;
		double quantity_tot = 0.0;
		
		for (HashMap<String, String> item: info)
		{
			double price = get_val(item, PRICE);
			double quantity = get_val(item, QUANTITY);
			
			sum += get_investment(price, quantity);
			quantity_tot += quantity;
		}
		
		return (quantity_tot == 0.0 ? output : sum / quantity_tot);
	}

	private static double get_fees(int order_id_) { return db_ib.execs.get_fees(order_id_); }
	
	private static double get_fees(ArrayList<HashMap<String, String>> info_) { return get_common(info_, FEES); }

	private static double get_quantity(int order_id_) { return db_ib.execs.get_quantity(order_id_); }
	
	private static double get_quantity(ArrayList<HashMap<String, String>> info_) { return get_common(info_, QUANTITY); }

	private static double get_investment(int order_id_) { return get_common(order_id_, INVESTMENT); }
	
	private static double get_investment(ArrayList<HashMap<String, String>> info_) { return get_common(info_, INVESTMENT); }

	private static double get_common(int order_id_, String what_) { return get_common(db_ib.execs.get_basic_info(order_id_), what_); }
	
	private static double get_common(ArrayList<HashMap<String, String>> info_, String what_)
	{
		double output = WRONG_MONEY;

		boolean is_price = what_.equals(PRICE);
		boolean is_investment = what_.equals(INVESTMENT);
			
		if (is_price) output = common.WRONG_PRICE;
		else if (what_.equals(QUANTITY)) output = common.WRONG_QUANTITY;
		
		int tot = arrays.get_size(info_);
		if (tot < 1) return output;
		if (tot == 1) return (is_investment ? get_investment(info_.get(0)) : get_val(info_.get(0), what_));
		
		double sum = 0.0;
		double quantity_tot = 0.0;

		for (HashMap<String, String> item: info_) 
		{
			if (is_price || is_investment)
			{
				double price = get_val(item, PRICE);
				double quantity = get_val(item, QUANTITY);			
				double fees = (is_price ? 0.0 : get_val(item, FEES));
				
				sum += get_investment(price, quantity, fees);
				
				if (is_price) quantity_tot += quantity;				
			}
			else sum += get_val(item, what_); 
		}
		
		return (is_price ? (sum / quantity_tot) : sum);
	}

	private static double get_investment(HashMap<String, String> item_) { return get_investment(get_val(item_, PRICE), get_val(item_, QUANTITY), get_val(item_, FEES)); }

	private static double get_investment(double price_, double quantity_) { return get_investment(price_, quantity_, 0.0); }
	
	private static double get_investment(double price_, double quantity_, double fees_) { return ((price_ * quantity_) - fees_); }

	private static double get_val(HashMap<String, String> item_, String field_) { return Double.parseDouble(item_.get(field_)); }
}