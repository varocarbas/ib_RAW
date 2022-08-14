package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import accessory_ib.types;

abstract class remote_execute
{
	public static final String ERROR = types.ERROR_IB_REMOTE_EXECUTE;
	public static final String ERROR_TYPE = types.ERROR_IB_REMOTE_EXECUTE_TYPE;
	public static final String ERROR_QUANTITY = types.ERROR_IB_REMOTE_EXECUTE_QUANTITY;
	public static final String ERROR_ORDER_ID = types.ERROR_IB_REMOTE_EXECUTE_ORDER_ID;
	public static final String ERROR_PLACE = types.ERROR_IB_REMOTE_EXECUTE_PLACE;
	public static final String ERROR_CANCEL = types.ERROR_IB_REMOTE_EXECUTE_CANCEL;
	public static final String ERROR_UPDATE = types.ERROR_IB_REMOTE_EXECUTE_UPDATE;

	public static void execute_all()
	{
		ArrayList<HashMap<String, String>> all = db_ib.remote.get_all_pending(remote.is_quick());
		if (!arrays.is_ok(all)) return;
		
		for (HashMap<String, String> vals: all) { execute(vals); }	
	}

	static String get_error_message(String type_)
	{	
		String message = strings.DEFAULT;
		
		if (type_.equals(ERROR_PLACE)) message = "place execution failed";
		else if (type_.equals(ERROR_CANCEL)) message = "cancel execution failed";
		else if (type_.equals(ERROR_UPDATE)) message = "update execution failed";
		else message = remote.get_error_message_default(type_, false);
		
		return message;
	}
	
	private static boolean execute(HashMap<String, String> vals_)
	{
		boolean executed = false;

		int request = remote.get_request(vals_);

		String type = remote.get_type(vals_);
		if (!is_ok(request, type)) 
		{
			remote.update_error(request, ERROR_TYPE, type);
			
			return executed;
		}

		if (orders.is_place(type))
		{
			String symbol = remote.get_symbol(vals_);
			
			double quantity = remote.get_quantity(vals_);
			double perc = remote.get_perc_money(vals_);
			double price = remote.get_price(vals_);
			
			HashMap<String, Object> temp = remote.get_quantity(symbol, quantity, perc, price);			
			if (!arrays.is_ok(temp)) 
			{
				update_error_quantity(request, quantity, perc, price);
				
				return executed;
			}
							
			quantity = (double)temp.get(db_ib.remote.QUANTITY);
			if (quantity <= common.WRONG_QUANTITY) 
			{
				update_error_quantity(request, quantity, perc, price);
				
				return executed;
			}
				
			price = (double)temp.get(db_ib.remote.PRICE);
			
			double stop = remote.get_stop(vals_);
			double start = remote.get_start(vals_);
			double start2 = remote.get_start2(vals_);

			executed = place(request, type, symbol, quantity, stop, start, start2, price);
			
			if (!executed) update_error_place(request, type, quantity, stop, start, start2);
		}
		else
		{
			int order_id = remote.get_order_id_main(vals_);
			
			if (order_id <= common.WRONG_ORDER_ID) 
			{
				update_error_order_id(request, order_id, type);
				
				return executed;
			}

			if (orders.is_cancel(type)) 
			{
				if (remote.order_id_is_ok(order_id, true)) 
				{
					executed = cancel(request);

					if (!executed) remote.update_error(request, ERROR_CANCEL, order_id);
				}
				else update_error_order_id(request, order_id, type);
			}
			else if (orders.is_update(type)) 
			{				
				if (remote.order_id_is_ok(order_id, false)) 
				{
					String field_col = db_ib.orders.get_field_update(type);
					if (remote.is_quick()) field_col = db_ib.remote.get_col(field_col);

					double val = Double.parseDouble(vals_.get(field_col));	
					executed = update(request, order_id, type, val);
				
					if (!executed) update_error_update(request, type, order_id, val);
				}
				else update_error_order_id(request, order_id, type);
			}			
		}
		
		return executed;
	}

	private static boolean place(int request_, String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, double price_) 
	{ 
		order order = new order(type_place_, symbol_, quantity_, stop_, start_, start2_);
		if (!order.is_ok()) return false;

		boolean is_ok = sync_orders.place_update(order);
		boolean is_quick = remote.is_quick();		

		Object vals = db_ib.remote.add_to_vals(db_ib.remote.ORDER_ID_MAIN, order.get_id_main(), null, is_quick);
		vals = db_ib.remote.add_to_vals(db_ib.remote.ORDER_ID_SEC, order.get_id_sec(), vals, is_quick);
		vals = db_ib.remote.add_to_vals(db_ib.remote.QUANTITY, quantity_, vals, is_quick);
		vals = db_ib.remote.add_to_vals(db_ib.remote.STATUS2, db_ib.remote.get_status2_key_execute(is_ok), vals, is_quick);		

		if (ib.common.price_is_ok(price_)) vals = db_ib.remote.add_to_vals(db_ib.remote.PRICE, price_, vals, is_quick);;

		db_ib.remote.update(request_, vals, is_quick);
		
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

		boolean is_quick = remote.is_quick();
		
		Object vals = db_ib.remote.add_to_vals(db_ib.remote.IS_MARKET, is_market, null, is_quick);

		if (stop != common.WRONG_PRICE) vals = db_ib.remote.add_to_vals(db_ib.remote.STOP, stop, vals, is_quick);
		if (start != common.WRONG_PRICE) vals = db_ib.remote.add_to_vals(db_ib.remote.START, start, vals, is_quick);
		if (start2 != common.WRONG_PRICE) vals = db_ib.remote.add_to_vals(db_ib.remote.START2, start2, vals, is_quick);

		is_ok = sync_orders.place_update(order, type_update, val_);
		vals = db_ib.remote.get_vals_common(db_ib.remote.get_status2_key_execute(is_ok), vals, is_quick);
		
		db_ib.remote.update(request_, vals, is_quick);
		
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

	private static void update_error_quantity(int request_, double quantity_, double perc_, double price_)
	{
		HashMap<String, Double> vals = new HashMap<String, Double>();
		
		vals.put(db_ib.remote.get_col(db_ib.remote.QUANTITY), quantity_);
		vals.put(db_ib.remote.get_col(db_ib.remote.PERC_MONEY), perc_);
		vals.put(db_ib.remote.get_col(db_ib.remote.PRICE), price_);
		
		remote.update_error(request_, ERROR_QUANTITY, vals);		
	}

	private static void update_error_order_id(int request_, int order_id_, String type_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_ib.remote.get_col(db_ib.remote.ORDER_ID_MAIN), order_id_);
		vals.put(db_ib.remote.get_col(db_ib.remote.TYPE_ORDER), strings.to_string(type_));
		
		remote.update_error(request_, ERROR_ORDER_ID, vals);		
	}

	private static void update_error_place(int request_, String type_, double quantity_, double stop_, double start_, double start2_) { remote.update_error_place(request_, type_, quantity_, stop_, start_, start2_, false); }

	private static void update_error_update(int request_, String type_, int order_id_, double val_) { remote.update_error_update(request_, type_, order_id_, val_, false); }
}