package ib;

import java.util.ArrayList;
import java.util.HashMap;

import accessory.arrays;
import accessory.strings;
import accessory_ib.types;

public abstract class orders 
{
	public static final String ORDERS = types.ORDERS;

	public static final String PLACE = types.ORDERS_PLACE;
	public static final String PLACE_MARKET = types.ORDERS_PLACE_MARKET;
	public static final String PLACE_STOP = types.ORDERS_PLACE_STOP;
	public static final String PLACE_LIMIT = types.ORDERS_PLACE_LIMIT;
	public static final String PLACE_STOP_LIMIT = types.ORDERS_PLACE_STOP_LIMIT;

	public static final String CANCEL = types.ORDERS_CANCEL;
	public static final String UPDATE = types.ORDERS_UPDATE;
	public static final String UPDATE_START = types.ORDERS_UPDATE_START;
	public static final String UPDATE_START_VALUE = types.ORDERS_UPDATE_START_VALUE;
	public static final String UPDATE_START_MARKET = types.ORDERS_UPDATE_START_MARKET;
	public static final String UPDATE_START2 = types.ORDERS_UPDATE_START2;
	public static final String UPDATE_START2_VALUE = types.ORDERS_UPDATE_START2_VALUE;
	public static final String UPDATE_STOP = types.ORDERS_UPDATE_STOP;
	public static final String UPDATE_STOP_VALUE = types.ORDERS_UPDATE_STOP_VALUE;
	public static final String UPDATE_STOP_MARKET = types.ORDERS_UPDATE_STOP_MARKET;

	public static final String STATUS = types.ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.ORDERS_STATUS_INACTIVE;
	public static final String STATUS_IN_PROGRESS = types.ORDERS_STATUS_IN_PROGRESS;

	public static final int MIN_ORDER_ID = 0;
	
	public static final String DEFAULT_STATUS = STATUS_SUBMITTED;
	public static final boolean DEFAULT_IS_QUICK = true;
	
	private static boolean _is_quick = DEFAULT_IS_QUICK;

	public static boolean is_quick() { return _is_quick; }

	public static boolean place_market(String symbol_, double quantity_, double stop_) { return sync_orders.place(PLACE_MARKET, symbol_, quantity_, stop_, common.WRONG_PRICE); }

	public static boolean place_stop(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.place(PLACE_STOP, symbol_, quantity_, stop_, start_); }

	public static boolean place_limit(String symbol_, double quantity_, double stop_, double start_) { return sync_orders.place(PLACE_LIMIT, symbol_, quantity_, stop_, start_); }

	public static boolean place_stop_limit(String symbol_, double quantity_, double stop_, double start_limit_, double start_stop_) { return sync_orders.place(PLACE_STOP_LIMIT, symbol_, quantity_, stop_, start_limit_, start_stop_); }

	public static boolean update(int id_, String type_, double val_) { return sync_orders.update(sync_orders.__get_order(id_), check_update(type_), val_); }

	public static boolean __update_start(String symbol_, double start_) { return sync_orders.__update(symbol_, UPDATE_START_VALUE, start_); }

	public static boolean __update_start_market(String symbol_) { return sync_orders.__update(symbol_, UPDATE_START_MARKET, common.WRONG_PRICE); }

	public static boolean __update_stop(String symbol_, double stop_) { return sync_orders.__update(symbol_, UPDATE_STOP_VALUE, stop_); }

	public static boolean __update_stop_market(String symbol_) { return sync_orders.__update(symbol_, UPDATE_STOP_MARKET, common.WRONG_PRICE); }

	public static boolean __update_start2(String symbol_, double start2_) { return sync_orders.__update(symbol_, UPDATE_START2_VALUE, start2_); }

	public static boolean __cancel(int order_id_main_)
	{ 	
		boolean output = false;
		
		async_orders.__check_all();
		
		if (is_inactive(order_id_main_)) output = true;
		else if (is_filled(order_id_main_)) output = false;
		else output = sync_orders.cancel(order_id_main_);
		
		return output;
	}	

	public static boolean is_submitted(int order_id_main_) { return order_is_common(order_id_main_, STATUS_SUBMITTED); }

	public static boolean is_submitted(String symbol_) { return order_is_common(symbol_, STATUS_SUBMITTED); }

	public static boolean __is_submitted_ib(int order_id_main_) { return sync_orders.is_submitted(order_id_main_, sync.__get_orders()); }	

	public static boolean is_filled(int order_id_main_) { return order_is_common(order_id_main_, STATUS_FILLED); }	

	public static boolean is_filled(String symbol_) { return order_is_common(symbol_, STATUS_FILLED); }	

	public static boolean __is_filled_ib(int order_id_main_) { return is_filled_ib(order_id_main_, sync.__get_orders()); }	

	public static boolean is_filled_ib(int order_id_main_, HashMap<Integer, String> orders_) { return sync_orders.is_filled(order_id_main_, orders_); }	

	public static boolean is_inactive(int order_id_main_) { return is_inactive(order_id_main_, true); }	

	public static boolean is_inactive(int order_id_main_, boolean check_exists_) { return ((!check_exists_ || (check_exists_ && !db_ib.orders.exists(order_id_main_, true))) || order_is_common(order_id_main_, STATUS_INACTIVE)); }	

	public static boolean is_inactive(String symbol_) { return is_inactive(symbol_, true); }	
	
	public static boolean is_inactive(String symbol_, boolean check_exists_) { return ((!check_exists_ || (check_exists_ && !db_ib.orders.exists(symbol_))) || order_is_common(symbol_, STATUS_INACTIVE)); }	

	public static boolean __is_inactive_ib(int order_id_main_) { return sync_orders.is_inactive(order_id_main_, sync.__get_orders()); }	
	
	public static boolean is_active(int order_id_main_) { return !is_inactive(order_id_main_); }	
	
	public static boolean is_active(String symbol_) { return !is_inactive(symbol_); }	

	public static boolean is_order_id_main(int order_id_) { return db_ib.orders.exists(order_id_, true); }	

	public static boolean is_order_id_sec(int order_id_) { return db_ib.orders.exists(order_id_, false); }
	
	public static int get_last_id_main() { return sync_orders._last_id_main; }

	public static int get_last_id_sec() { return sync_orders._last_id_sec; }
	
	public static int get_order_id_main(int order_id_sec_) { return db_ib.orders.get_order_id_main(order_id_sec_, _is_quick); }
	
	public static String get_type(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_status_from_key(input_) : db_ib.orders.get_type_place_from_key(input_)); }

	public static String get_key(String input_, boolean is_status_) { return (is_status_ ? db_ib.orders.get_key_from_status(input_) : db_ib.orders.get_key_from_type_place(input_)); }

	public static boolean is_status(String type_) { return strings.is_ok(check_status(type_)); }
	
	public static boolean update_is_ok(String type_, double stop_, double start_) { return update_is_ok(type_, stop_, start_, common.WRONG_PRICE); }

	public static boolean update_is_ok(String type_, double stop_, double start_, double start2_) { return (is_update(type_) && (is_update_market(type_) || ((orders.is_update_stop(type_) && common.price_is_ok(stop_)) || (orders.is_update_start(type_) && common.price_is_ok(start_)) || (orders.is_update_start2(type_) && common.price_is_ok(start2_))))); }
	
	public static double get_update_val(String type_, double stop_, double start_, double start2_) 
	{
		double val = common.WRONG_PRICE;
		if (!update_is_ok(type_, stop_, start_, start2_)) return val;
	
		if (orders.is_update_stop(type_)) val = stop_;
		else if (orders.is_update_start(type_)) val = start_;
		else if (orders.is_update_start2(type_)) val = start2_;

		return val; 
	}

	public static ArrayList<HashMap<String, String>> get_all_active(String[] fields_) { return get_all_common(fields_, STATUS_ACTIVE); }

	public static ArrayList<HashMap<String, String>> get_all_filled(String[] fields_) { return get_all_common(fields_, STATUS_FILLED); }

	public static ArrayList<HashMap<String, String>> get_all_submitted(String[] fields_) { return get_all_common(fields_, STATUS_SUBMITTED); }

	public static String get_field_col(String field_) { return db_ib.orders.get_field_col(field_, _is_quick); }
	
	public static boolean is_place(String type_) { return strings.is_ok(check_place(type_)); }

	public static boolean is_update(String type_) { return strings.is_ok(check_update(type_)); }

	public static boolean is_update_start(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_START)); }

	public static boolean is_update_stop(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_STOP)); }

	public static boolean is_update_start2(String type_) { return strings.is_ok(accessory.types.check_type(type_, UPDATE_START2)); }

	public static boolean is_update_start_start2(String type_) { return (is_update_start(type_) || is_update_start2(type_)); }

	public static boolean is_update_market(String type_) 
	{ 
		String type = check_update(type_);
		if (!strings.is_ok(type)) return false;

		return (type.equals(UPDATE_START_MARKET) || type.equals(UPDATE_STOP_MARKET)); 
	}

	public static boolean is_cancel(String type_) { return strings.is_ok(check_cancel(type_)); }

	public static String check_place(String type_) { return accessory.types.check_type(type_, PLACE); }

	public static String check_update(String type_) { return accessory.types.check_type(type_, UPDATE); }

	public static String check_cancel(String type_) { return accessory.types.check_type(type_, CANCEL); }

	public static String check_status(String type_) { return accessory.types.check_type(type_, STATUS); }
	
	public static String get_status(String status_ib_, boolean be_specific_)
	{
		String status = strings.DEFAULT;
		if (!external_ib.orders.status_is_ok(status_ib_)) return status;

		if (external_ib.orders.status_in_progress(status_ib_)) status = STATUS_IN_PROGRESS;
		else if (status_ib_.equals(external_ib.orders.STATUS_IB_SUBMITTED) || status_ib_.equals(external_ib.orders.STATUS_IB_PRESUBMITTED)) status = (be_specific_ ? STATUS_SUBMITTED : STATUS_ACTIVE);
		else if (status_ib_.equals(external_ib.orders.STATUS_IB_FILLED)) status = (be_specific_ ? STATUS_FILLED : STATUS_ACTIVE);
		else status = STATUS_INACTIVE;
		
		return status;
	}	

	public static boolean is_status(String status_ib_, String status_)
	{
		if (!external_ib.orders.status_is_ok(status_ib_)) return false;

		String status = check_status(status_);
		if (!strings.is_ok(status)) return false;
		
		return strings.are_equal(status, get_status(status_ib_, !status_is_generic(status)));
	}
	
	public static boolean status_is_generic(String status_) 
	{ 
		String status = check_status(status_);
		if (!strings.is_ok(status)) return false;

		return (status.equals(STATUS_ACTIVE) || status.equals(STATUS_INACTIVE)); 
	}
	
	public static double adapt_quantity(double quantity_) { return _order.adapt_quantity(quantity_); }
	
	public static void __sync_db() { async_orders.__check_db(); }

	public static boolean deactivate(int order_id_main_) 
	{ 
		boolean output = db_ib.orders.deactivate(order_id_main_, _is_quick); 
		
		if (output) db_ib.remote.deactivate_order_id(order_id_main_);
		
		return output;
	}
	
	public static boolean update_status(int order_id_main_, String status_) { return db_ib.orders.update_status(order_id_main_, status_, _is_quick); }

	public static boolean order_was_updated(int order_id_main_, String type_, double stop_, double start_, double start2_) { return db_ib.orders.order_was_updated(order_id_main_, type_, stop_, start_, start2_, _is_quick); }

	public static String get_symbol(int order_id_main_) { return db_ib.orders.get_symbol(order_id_main_, _is_quick); }

	public static double get_quantity(int order_id_main_) { return db_ib.orders.get_quantity(order_id_main_, _is_quick); }

	public static int get_highest_order_id() { return db_ib.orders.get_highest_order_id(_is_quick); }

	public static boolean exists(int order_id_main_) { return db_ib.orders.exists(order_id_main_, true); }

	public static boolean exists_status(String status_) { return db_ib.orders.exists_status(status_); }

	public static HashMap<Integer, String> __get_orders_ib() { return sync.__get_orders(); }
	
	static void order_status(int order_id_, String status_ib_) 
	{ 
		sync_orders.order_status(order_id_, status_ib_); 
		
		async_orders.order_status(order_id_, status_ib_);
	}

	private static ArrayList<HashMap<String, String>> get_all_common(String[] fields_, String status_) 
	{		
		int tot = arrays.get_size(fields_);

		String[] field_cols = null;
		
		if (tot > 0)
		{
			field_cols = new String[tot];
			
			for (int i = 0; i < tot; i++) field_cols[i] = get_field_col(fields_[i]);
		}

		return db_ib.orders.get_all_common(field_cols, status_, _is_quick);
	}

	private static boolean order_is_common(int order_id_main_, String target_) { return strings.are_equal(db_ib.orders.get_status(order_id_main_), target_); }	
	
	private static boolean order_is_common(String symbol_, String target_) { return strings.are_equal(db_ib.orders.get_status(symbol_), target_); }	
}