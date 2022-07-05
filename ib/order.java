package ib;

import java.util.HashMap;

import accessory.numbers;
import accessory.parent;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.types;
import external_ib.orders;

public class order extends parent
{
	public static final String CONFIG_TIF = types.CONFIG_ORDERS_TIF;
	public static final String CONFIG_QUANTITIES_INT = types.CONFIG_ORDERS_QUANTITIES_INT;

	public static final String STATUS = types.ORDERS_STATUS;
	public static final String STATUS_SUBMITTED = types.ORDERS_STATUS_SUBMITTED;
	public static final String STATUS_FILLED = types.ORDERS_STATUS_FILLED;
	public static final String STATUS_ACTIVE = types.ORDERS_STATUS_ACTIVE;
	public static final String STATUS_INACTIVE = types.ORDERS_STATUS_INACTIVE;
	
	public static final String TYPE_MARKET = orders.TYPE_MARKET;
	public static final String TYPE_STOP = orders.TYPE_STOP;
	public static final String TYPE_LIMIT = orders.TYPE_LIMIT;
	public static final String TYPE_STOP_LIMIT = orders.TYPE_STOP_LIMIT;
	public static final String TYPE_PLACE_MARKET = types.ORDERS_PLACE_MARKET;
	public static final String TYPE_PLACE_STOP = types.ORDERS_PLACE_STOP;
	public static final String TYPE_PLACE_LIMIT = types.ORDERS_PLACE_LIMIT;
	public static final String TYPE_PLACE_STOP_LIMIT = types.ORDERS_PLACE_STOP_LIMIT;
	
	public static final double WRONG_VALUE = 0.0;
	public static final int WRONG_ORDER_ID = -1;

	public static final String DEFAULT_TIF = orders.TIF_GTC; 
	public static final boolean DEFAULT_QUANTITIES_INT = true;
	
	private int _id_main = WRONG_ORDER_ID;
	private int _id_sec = WRONG_ORDER_ID;	
	private String _type_place = strings.DEFAULT;
	private String _type_main = strings.DEFAULT;
	private String _type_sec = strings.DEFAULT;
	private String _symbol = strings.DEFAULT;
	private double _quantity = WRONG_VALUE;
	private double _stop = WRONG_VALUE;
	private double _start = WRONG_VALUE;
	private double _start2 = WRONG_VALUE;
	
	private String _temp_type = strings.DEFAULT;
	private String _temp_symbol = strings.DEFAULT;
		
	public static boolean are_equal(order order1_, order order2_) { return are_equal_common(order1_, order2_); }

	public static int get_id_sec(int id_main_) { return (id_main_ + 1); }

	public static String get_tif() 
	{ 
		String tif = (String)config.get_order(CONFIG_TIF);
		
		return (orders.tif_is_ok(tif) ? tif : strings.DEFAULT);
	}

	public static boolean quantities_int() { return config.get_order_boolean(CONFIG_QUANTITIES_INT); }

	public static String check_type_place(String type_) { return sync_orders.check_place(type_); }

	public static String check_symbol(String symbol_) { return common.check_symbol(symbol_); }
	
	public static boolean type_is_ok(String type_) { return orders.type_is_ok(type_); }
	
	public static String type_from_place(String type_place_)
	{
		String output = null;
		
		String type_place = check_type_place(type_place_);
		if (!strings.is_ok(type_place)) return output;
		
		if (type_place.equals(TYPE_PLACE_MARKET)) output = TYPE_MARKET;
		else if (type_place.equals(TYPE_PLACE_STOP)) output = TYPE_STOP;
		else if (type_place.equals(TYPE_PLACE_LIMIT)) output = TYPE_LIMIT;
		else if (type_place.equals(TYPE_PLACE_STOP_LIMIT)) output = TYPE_STOP_LIMIT;
		
		return output;
	}
	
	public static String get_status(String status_ib_, boolean be_specific_)
	{
		String status = strings.DEFAULT;
		if (!strings.is_ok(status_ib_)) return status;

		if (status_ib_.equals(orders.STATUS_IB_SUBMITTED) || status_ib_.equals(orders.STATUS_IB_PRESUBMITTED)) status = (be_specific_ ? STATUS_SUBMITTED : STATUS_ACTIVE);
		else if (status_ib_.equals(orders.STATUS_IB_FILLED)) status = (be_specific_ ? STATUS_FILLED : STATUS_ACTIVE);
		else if (!status_ib_.equals(orders.STATUS_IB_PENDING_SUBMIT) && !status_ib_.equals(orders.STATUS_IB_PENDING_CANCEL) && !status_ib_.equals(orders.STATUS_IB_API_CANCELLED)) status = STATUS_INACTIVE;
		
		return status;
	}	

	public static String get_symbol(int id_) { return db_ib.orders.get_symbol(id_); }
	
	public order(order input_) { instantiate(input_); }
	
	public order(String type_place_, String symbol_, double quantity_, double stop_, double start_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, WRONG_VALUE, sync.get_order_id()); }
	
	public order(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, start2_, sync.get_order_id()); }
	
	public order(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, start2_, id_main_); }

	public String get_type_place() { return _type_place; }

	public String get_type_main() { return _type_main; }

	public String get_type_sec() { return _type_sec; }
	
	public String get_type(boolean is_main_) { return (is_main_ ? _type_main : _type_sec); }
	
	public String get_symbol() { return _symbol; }

	public boolean is_market(boolean is_main_) { return get_type(is_main_).equals(TYPE_MARKET); }

	public boolean is_stop(boolean is_main_) { return get_type(is_main_).equals(TYPE_STOP); }

	public boolean is_limit(boolean is_main_) { return get_type(is_main_).equals(TYPE_LIMIT); }

	public boolean is_stop_limit(boolean is_main_) { return get_type(is_main_).equals(TYPE_STOP_LIMIT); }
	
	public double get_val(boolean is_main_) { return (is_main_ ? _start : _stop); }

	public int get_id(boolean is_main_) { return (is_main_ ? _id_main : _id_sec); }

	public int get_id_main() { return _id_main; }
	
	public int get_id_sec() { return _id_sec; }
	
	public double get_quantity() 
	{ 
		double quantity = _quantity;
		if (quantities_int()) quantity = (double)numbers.to_int(quantity);		

		return quantity;
	}

	public double get_stop() { return _stop; }

	public double get_start() { return _start; }

	public double get_start2() { return _start2; }

	public boolean update_start(double start_) { return update_val(start_, true); }
	
	public boolean update_stop(double stop_) { return update_val(stop_, false); }
	
	public boolean update_val(double val_, boolean is_main_) 
	{ 
		if (val_ < WRONG_VALUE) return false;
		
		if (is_main_) _start = val_;
		else _stop = val_;
		
		return true;
	}
	
	public boolean update_type_main(String type_) { return update_type(type_, true); }

	public boolean update_type_sec(String type_) { return update_type(type_, false); }
	
	public boolean update_type(String type_, boolean is_main_) 
	{ 
		if (!type_is_ok(type_)) return false;
		
		if (is_main_) _type_main = type_;
		else _type_sec = type_;
		
		return true;
	}
	
	public String toString()
	{
		if (!is_ok(_type_place, _symbol, _quantity, _stop, _start, _start2, _id_main)) return strings.DEFAULT;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put("id_main", _id_main);
		vals.put("id_sec", _id_sec);
		vals.put("type_place", _type_place);
		vals.put("type_main", _type_main);
		vals.put("type_sec", _type_sec);
		vals.put("symbol", _symbol);
		vals.put("quantity", _quantity);
		vals.put("stop", _stop);
		vals.put("start", _start);
		vals.put("start2", _start2);
		
		return strings.to_string(vals);
	}

	public boolean equals(order order2_)
	{
		if (!is_ok(order2_)) return false;

		return 
		(
			_temp_type.equals(order2_._type_place) && _temp_symbol.equals(order2_._symbol) && (_quantity == order2_._quantity) &&  
			(_id_main == order2_._id_main) && (_id_sec == order2_._id_sec) && (_stop == order2_._stop) && (_start == order2_._start) && 
			(_start2 == order2_._start2) && _type_main.equals(order2_._type_main) && _type_sec.equals(order2_._type_sec)
		);		
	}

	private void instantiate(order input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_type, input_._temp_symbol, input_._quantity, input_._stop, input_._start, input_._start2, input_._id_main);
	}

	private void instantiate(String type_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_)
	{
		instantiate_common();
		if (!is_ok(type_, symbol_, quantity_, stop_, start_, start2_, id_main_)) return;

		populate(_temp_type, _temp_symbol, quantity_, stop_, start_, start2_, id_main_);
	}

	public boolean is_ok() { return is_ok(_type_place, _symbol, _quantity, _stop, _start, _start2, _id_main); }
			
	private boolean is_ok(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_)
	{
		_temp_type = check_type_place(type_place_);
		_temp_symbol = check_symbol(symbol_);

		return (strings.are_ok(new String[] { _temp_type, _temp_symbol }) && quantity_ > 0 && (stop_ > WRONG_VALUE) && (start_ > WRONG_VALUE || _temp_type.equals(TYPE_PLACE_MARKET)) && (start2_ > WRONG_VALUE || !_temp_type.equals(TYPE_PLACE_STOP_LIMIT)));
	}

	private void populate(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_)
	{
		_type_place = type_place_;
		_type_main = get_type_from_place(true);
		_type_sec = get_type_from_place(false);
		_symbol = symbol_;
		_quantity = quantity_;
		_stop = stop_;
		_start = start_;
		_start2 = start2_;
		_id_main = id_main_;
		_id_sec = get_id_sec(id_main_);
	}
	
	private String get_type_from_place(boolean is_main_) { return (is_main_ ? type_from_place(_type_place) : TYPE_STOP); }
}