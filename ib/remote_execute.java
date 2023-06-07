package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.db_common;
import accessory.db_quick;
import accessory.misc;
import accessory.strings;
import accessory_ib._types;

abstract class remote_execute
{
	public static final String ERROR = _types.ERROR_IB_REMOTE_EXECUTE;
	public static final String ERROR_TYPE = _types.ERROR_IB_REMOTE_EXECUTE_TYPE;
	public static final String ERROR_QUANTITY = _types.ERROR_IB_REMOTE_EXECUTE_QUANTITY;
	public static final String ERROR_ORDER_ID = _types.ERROR_IB_REMOTE_EXECUTE_ORDER_ID;
	public static final String ERROR_PLACE = _types.ERROR_IB_REMOTE_EXECUTE_PLACE;
	public static final String ERROR_CANCEL = _types.ERROR_IB_REMOTE_EXECUTE_CANCEL;
	public static final String ERROR_UPDATE = _types.ERROR_IB_REMOTE_EXECUTE_UPDATE;

	public static void __execute_all()
	{
		ArrayList<HashMap<String, String>> all = db_ib.remote.get_all_pending();
		if (!arrays.is_ok(all)) return;
		
		for (HashMap<String, String> vals: all) { __execute(vals); }	
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
	
	private static boolean __execute(HashMap<String, String> vals_)
	{
		boolean executed = false;

		int request = remote.get_request(vals_);

		String type = remote.get_type(vals_);
		if (!is_ok(request, type)) 
		{
			remote.update_error(request, ERROR_TYPE, type, type);
			
			return executed;
		}

		String symbol = remote.get_symbol(vals_);
		int order_id = common.WRONG_ORDER_ID;
		
		if (remote.is_place(type))
		{
			double quantity = remote.get_quantity(vals_);
			double perc = remote.get_perc_money(vals_);
			double price = remote.get_price(vals_);
			
			HashMap<String, Object> temp = remote.__get_quantity(symbol, quantity, perc, price);			
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

			executed = __place(request, type, symbol, quantity, stop, start, start2, price);
			
			if (!executed) update_error_place(request, symbol, type, quantity, stop, start, start2);
		}
		else
		{
			order_id = remote.get_order_id_main(vals_);
			
			if (order_id <= common.WRONG_ORDER_ID) 
			{
				update_error_order_id(request, order_id, type);
				
				return executed;
			}

			if (remote.is_cancel(type)) 
			{
				executed = __cancel(request, order_id);

				if (!executed) remote.update_error(request, ERROR_CANCEL, order_id, remote.CANCEL);

			}
			else if (remote.is_update(type)) 
			{				
				String field_col = db_ib.orders.get_field_update(type);
				if (remote.is_quick()) field_col = db_quick.get_col(db_ib.remote.SOURCE, field_col);

				double val = Double.parseDouble(vals_.get(field_col));	
				executed = __update(request, order_id, type, val);
				
				if (!executed) update_error_update(request, symbol, type, order_id, val);
			}			
		}

		if (executed) remote.log(remote.get_ok_message_default(type, request, symbol, order_id, false));
		
		return executed;
	}

	private static boolean __place(int request_, String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, double price_) 
	{ 
		_order order = new _order(type_place_, symbol_, quantity_, stop_, start_, start2_);
		if (!order.is_ok()) return false;

		boolean is_ok = sync_orders._place_update(order);

		Object vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.ORDER_ID_MAIN, order.get_id_main(), null);
		vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.ORDER_ID_SEC, order.get_id_sec(), vals);
		vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.QUANTITY, quantity_, vals);
		vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.STATUS, db_ib.remote.store_status_type(ib.remote.DEFAULT_STATUS), vals);		
		vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.STATUS2, db_ib.remote.get_status2_key_execute(is_ok), vals);		

		if (ib.common.price_is_ok(price_)) vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.PRICE, price_, vals);

		db_ib.remote.update(request_, vals);
		
		return is_ok;
	}
	
	private static boolean __update(int request_, int order_id_, String type_update_, double val_) 
	{ 
		boolean is_ok = false;
		if (order_id_ <= common.WRONG_ORDER_ID) return is_ok;
		
		String type_update = ib.orders.check_update(type_update_);
		if (!strings.is_ok(type_update) || (val_ <= common.WRONG_PRICE && !remote.is_update_market(type_update))) return is_ok;
		
		_order order = sync_orders.__get_order(order_id_, false);
		if (order == null || !order.is_ok()) return is_ok;
		
		double stop = common.WRONG_PRICE;
		double start = common.WRONG_PRICE;
		double start2 = common.WRONG_PRICE;
		boolean is_market = false;
		
		if (type_update.equals(remote.UPDATE_START_MARKET) || type_update.equals(remote.UPDATE_STOP_MARKET)) is_market = true;
		else if (type_update.equals(remote.UPDATE_START_VALUE)) start = val_;
		else if (type_update.equals(remote.UPDATE_START2_VALUE)) start2 = val_;
		else if (type_update.equals(remote.UPDATE_STOP_VALUE)) stop = val_;

		Object vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.IS_MARKET, is_market, null);

		if (stop != common.WRONG_PRICE) vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.STOP, stop, vals);
		if (start != common.WRONG_PRICE) vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.START, start, vals);
		if (start2 != common.WRONG_PRICE) vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.START2, start2, vals);
		
		vals = db_common.add_to_vals(db_ib.remote.SOURCE, db_ib.remote.ERROR, "", vals);

		is_ok = sync_orders._place_update(order, type_update, val_);
		vals = db_ib.remote.get_vals_common(db_ib.remote.get_status2_key_execute(is_ok), vals);
			
		db_ib.remote.update(request_, vals);
		
		return is_ok;
	}

	private static boolean __cancel(int request_, int order_id_) 
	{
		boolean is_ok = orders.__cancel(order_id_);

		if (!is_ok) 
		{
			misc.pause_secs(5);
			
			if (orders.__is_inactive_ib(order_id_)) is_ok = true;
		}
		
		if (is_ok) db_ib.remote.deactivate_order_id(order_id_);
		
		db_ib.remote.update_status2_execute(request_, is_ok);
		
		return is_ok;
	}

	private static boolean is_ok(int request_, String type_) { return (type_is_ok(type_) && db_ib.remote.is_pending(request_)); }

	private static boolean type_is_ok(String type_) { return (remote.is_place(type_) || remote.is_cancel(type_) || remote.is_update(type_)); }

	private static void update_error_quantity(int request_, double quantity_, double perc_, double price_)
	{
		HashMap<String, Double> vals = new HashMap<String, Double>();
		
		vals.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.QUANTITY), quantity_);
		vals.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.PERC_MONEY), perc_);
		vals.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.PRICE), price_);
		
		remote.update_error(request_, ERROR_QUANTITY, vals, orders.UPDATE);		
	}

	private static void update_error_order_id(int request_, int order_id_, String type_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.ORDER_ID_MAIN), order_id_);
		vals.put(db_quick.get_col(db_ib.remote.SOURCE, db_ib.remote.TYPE_ORDER), db_ib.remote.store_order_type(type_));
		
		remote.update_error(request_, ERROR_ORDER_ID, vals, type_);		
	}

	private static void update_error_place(int request_, String symbol_, String type_, double quantity_, double stop_, double start_, double start2_) { remote.update_error_place(request_, symbol_, type_, quantity_, stop_, start_, start2_, false); }

	private static void update_error_update(int request_, String symbol_, String type_, int order_id_, double val_) { remote.update_error_update(request_, symbol_, type_, order_id_, val_, false); }
}