package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;

abstract class remote_execute
{
	public static void execute_all(boolean any_user_)
	{
		ArrayList<HashMap<String, String>> all = db_ib.remote.get_all_pending(any_user_);
		if (!arrays.is_ok(all)) return;
		
		for (HashMap<String, String> vals: all) { execute(vals); }	
	}
	
	private static boolean execute(HashMap<String, String> vals_)
	{
		boolean executed = false;
	
		int request = remote.get_request(vals_);

		String type = remote.get_type(vals_);
		if (!is_ok(request, type)) return executed;

		if (orders.is_place(type))
		{
			String symbol = remote.get_symbol(vals_);
			
			HashMap<String, Object> temp = get_quantity(symbol, remote.get_quantity(vals_), remote.get_perc_money(vals_), remote.get_price(vals_));			
			
			double quantity = (double)temp.get(db_ib.remote.QUANTITY);
			if (quantity <= common.WRONG_QUANTITY) return executed;
				
			double price = (double)temp.get(db_ib.remote.PRICE);
			
			double stop = remote.get_stop(vals_);
			double start = remote.get_start(vals_);
			double start2 = remote.get_start2(vals_);

			executed = place(request, type, symbol, quantity, stop, start, start2, price);
		}
		else
		{
			int order_id = remote.get_order_id_main(vals_);
			if (order_id <= common.WRONG_ORDER_ID) return executed;

			if (orders.is_cancel(type)) 
			{
				if (remote.order_id_is_ok(order_id, true)) executed = cancel(request);
			}
			else if (orders.is_update(type)) 
			{
				if (remote.order_id_is_ok(order_id, false)) executed = update(request, order_id, type, Double.parseDouble(vals_.get(db_ib.orders.get_field_update(type))));
			}			
		}
		
		return executed;
	}

	private static HashMap<String, Object> get_quantity(String symbol_, double quantity_, double perc_money_, double price_)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		double quantity = quantity_;
		double price = price_;
				
		if (common.percent_is_ok(perc_money_, false))
		{
			if (!common.price_is_ok(price)) price = common.get_price(symbol_);
			
			if (common.price_is_ok(price))
			{
				double money = perc_money_ * basic.get_money() / 100.0;
				
				double money_free = basic.get_money_free();
				if (money > money_free) money = money_free;
				
				quantity = money / price;
				quantity = orders.adapt_quantity(quantity);				
			}
		}
		
		output.put(db_ib.remote.QUANTITY, quantity);
		output.put(db_ib.remote.PRICE, price);
		
		return output;
	}

	private static boolean place(int request_, String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, double price_) 
	{ 
		order order = new order(type_place_, symbol_, quantity_, stop_, start_, start2_);
		if (!order.is_ok()) return false;

		boolean is_ok = sync_orders.place_update(order);
		
		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(db_ib.remote.ORDER_ID_MAIN, order.get_id_main());
		vals.put(db_ib.remote.ORDER_ID_SEC, order.get_id_sec());
		vals.put(db_ib.remote.QUANTITY, quantity_);
		vals.put(db_ib.remote.STATUS2, db_ib.remote.get_status2_key_execute(is_ok));
	
		if (ib.common.price_is_ok(price_)) vals.put(db_ib.remote.PRICE, price_);

		db_ib.remote.update(request_, vals);
		
		return is_ok;
	}
	
	private static boolean update(int request_, int order_id_, String type_update_, double val_) 
	{ 
		boolean is_ok = false;
		if (order_id_ <= common.WRONG_ORDER_ID) return is_ok;
		
		String type_update = ib.orders.check_update(type_update_);
		if (!strings.is_ok(type_update) || (val_ <= common.WRONG_PRICE && !orders.is_update_market(type_update))) return is_ok;
		
		order order = sync_orders.get_order(order_id_);
		if (order == null || !order.is_ok()) return is_ok;
		
		double stop = common.WRONG_PRICE;
		double start = common.WRONG_PRICE;
		double start2 = common.WRONG_PRICE;
		boolean is_market = false;
		
		if (type_update.equals(remote.UPDATE_START_MARKET) || type_update.equals(remote.UPDATE_STOP_MARKET)) is_market = true;
		else if (type_update.equals(remote.UPDATE_START_VALUE)) start = val_;
		else if (type_update.equals(remote.UPDATE_START2_VALUE)) start2 = val_;
		else if (type_update.equals(remote.UPDATE_STOP_VALUE)) stop = val_;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_ib.remote.IS_MARKET, is_market);
		if (stop != common.WRONG_PRICE) vals.put(db_ib.remote.STOP, stop);
		if (start != common.WRONG_PRICE) vals.put(db_ib.remote.START, start);
		if (start2 != common.WRONG_PRICE) vals.put(db_ib.remote.START2, start2);

		is_ok = sync_orders.place_update(order, type_update, val_);
		vals = db_ib.remote.get_vals_common(db_ib.remote.get_status2_key_execute(is_ok), vals);
		
		db_ib.remote.update(request_, vals);
		
		return is_ok;
	}

	private static boolean cancel(int request_) 
	{
		boolean is_ok = false;
		
		int order_id = remote.get_order_id_main(request_);
		if (order_id <= common.WRONG_ORDER_ID) return is_ok;
		
		if (!db_ib.orders.is_active(order_id, true)) is_ok = true;
		else is_ok = sync_orders.cancel(order_id);

		if (is_ok) db_ib.remote.deactivate_order_id(order_id);
		
		db_ib.remote.update_status2_execute(request_, is_ok);
		
		return is_ok;
	}

	private static boolean is_ok(int request_, String type_) { return (type_is_ok(type_) && db_ib.remote.is_pending(request_)); }

	private static boolean type_is_ok(String type_) { return (ib.orders.is_place(type_) || ib.orders.is_cancel(type_) || ib.orders.is_update(type_)); }
}