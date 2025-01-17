package ib;

import java.util.HashMap;

import accessory.numbers;
import accessory.parent;
import accessory.strings;
import accessory_ib.config;
import accessory_ib._types;

public class _order extends parent
{
	public static final String CONFIG_TIF = _types.CONFIG_ORDERS_TIF;
	public static final String CONFIG_QUANTITIES_INT = _types.CONFIG_ORDERS_QUANTITIES_INT;
	public static final String CONFIG_OUTSIDE_RTH = _types.CONFIG_ORDERS_OUTSIDE_RTH;
	public static final String CONFIG_WAIT_UPDATE = _types.CONFIG_ORDERS_WAIT_UPDATE;
	
	public static final String TYPE_MARKET = external_ib.orders.TYPE_MARKET;
	public static final String TYPE_STOP = external_ib.orders.TYPE_STOP;
	public static final String TYPE_LIMIT = external_ib.orders.TYPE_LIMIT;
	public static final String TYPE_STOP_LIMIT = external_ib.orders.TYPE_STOP_LIMIT;
	
	public static final String TYPE_PLACE_MARKET = _types.ORDERS_PLACE_MARKET;
	public static final String TYPE_PLACE_STOP = _types.ORDERS_PLACE_STOP;
	public static final String TYPE_PLACE_LIMIT = _types.ORDERS_PLACE_LIMIT;
	public static final String TYPE_PLACE_STOP_LIMIT = _types.ORDERS_PLACE_STOP_LIMIT;

	public static final int INC_ID_SEC = 1;
	
	public static final String DEFAULT_TIF = external_ib.orders.TIF_GTC; 
	public static final boolean DEFAULT_QUANTITIES_INT = true;
	public static final boolean DEFAULT_OUTSIDE_RTH = false;
	public static final boolean DEFAULT_WAIT_UPDATE = true;
	
	private int _id_main = common.WRONG_ORDER_ID;
	private int _id_sec = common.WRONG_ORDER_ID;	
	private String _type_place = strings.DEFAULT;
	private String _type_main = strings.DEFAULT;
	private String _type_sec = strings.DEFAULT;
	private String _symbol = strings.DEFAULT;
	private double _quantity = common.WRONG_QUANTITY;
	private double _stop = common.WRONG_PRICE;
	private double _stop2 = common.WRONG_PRICE;
	private double _start = common.WRONG_PRICE;
	private double _start2 = common.WRONG_PRICE;
	
	private String _temp_type = strings.DEFAULT;
	private String _temp_symbol = strings.DEFAULT;
		
	public static boolean are_equal(_order order1_, _order order2_) { return are_equal_common(order1_, order2_); }

	public static int get_id_sec(int id_main_) { return (id_main_ + INC_ID_SEC ); }

	public static int get_id_main(int id_sec_) { return (id_sec_ - INC_ID_SEC ); }
	
	public static String get_tif() 
	{ 
		String tif = (String)config.get_order(CONFIG_TIF);
		
		return (external_ib.orders.tif_is_ok(tif) ? tif : strings.DEFAULT);
	}
	
	public static boolean update_tif(String tif_) { return (external_ib.orders.tif_is_ok(tif_) ? config.update_order(CONFIG_TIF, tif_) : false);  }

	public static boolean quantities_int() { return config.get_order_boolean(CONFIG_QUANTITIES_INT); }
	
	public static boolean update_quantities_int(boolean quantities_int_) { return config.update_order(CONFIG_QUANTITIES_INT, quantities_int_); }

	public static boolean outside_rth() { return config.get_order_boolean(CONFIG_OUTSIDE_RTH); }

	public static boolean update_outside_rth(boolean outside_rth_) { return config.update_order(CONFIG_OUTSIDE_RTH, outside_rth_); }

	public static boolean wait_update() { return config.get_order_boolean(CONFIG_WAIT_UPDATE); }

	public static boolean update_wait_update(boolean wait_update_) { return config.update_order(CONFIG_WAIT_UPDATE, wait_update_); }

	public static String check_symbol(String symbol_) { return common.check_symbol(symbol_); }
	
	public static boolean type_is_ok(String type_) { return external_ib.orders.type_is_ok(type_); }
	
	public static double adapt_quantity(double quantity_) { return (quantities_int() ? (double)numbers.to_int(quantity_) : quantity_); }
	
	public static boolean is_market(_order order_) { return (order_ != null && (order_.is_market(true) || order_.is_market(false))); } 
	
	public _order(_order input_) { instantiate(input_); }
	
	public _order(String type_place_, String symbol_, double quantity_, double stop_, double start_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, common.WRONG_PRICE, __get_new_order_id()); }
	
	public _order(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, start2_, __get_new_order_id()); }
	
	public _order(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_) { instantiate(type_place_, symbol_, quantity_, stop_, start_, start2_, id_main_); }

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
	
	public double get_quantity() { return adapt_quantity(_quantity); }
		
	public double get_stop() { return _stop; }

	public double get_stop2() { return _stop2; }
	
	public double get_start() { return _start; }

	public double get_start2() { return _start2; }
	
	public boolean update_start(double start_) { return update_val(start_, true); }
	
	public boolean update_start2(double start2_) 
	{ 
		boolean output = false;
		
		double start2 = ib.common.adapt_price(start2_);
		
		if (common.price_is_ok(start2))
		{
			output = true;
			
			_start2 = start2; 			
		}
		
		return output;
	}

	public boolean update_stop(double stop_) { return update_val(stop_, false); }
	
	public boolean update_stop2(double stop2_) 
	{ 
		boolean output = false;
		
		double stop2 = ib.common.adapt_price(stop2_);
		
		if (common.price_is_ok(stop2))
		{
			output = true;
			
			_stop2 = stop2;
			_type_sec = TYPE_STOP_LIMIT;
		}
		
		return output;
	}
	
	public boolean update_stop_limit(double stop_) { return update_val(stop_, false); }

	public double get_main_stop_limit_stop() { return get_start2(); }
	
	public double get_main_stop_limit_limit() { return get_start(); }
	
	public double get_sec_stop_limit_stop() { return get_stop2(); }
	
	public double get_sec_stop_limit_limit() { return get_stop(); }
	
	public boolean update_main_stop_limit_stop(double stop_) { return update_start2(stop_); }
	
	public boolean update_main_stop_limit_limit(double limit_) { return update_start(limit_); }
	
	public boolean update_sec_stop_limit_stop(double stop_) { return update_stop2(stop_); }
	
	public boolean update_sec_stop_limit_limit(double limit_) { return update_stop(limit_); }

	public boolean update_val(double val_, boolean is_main_) 
	{ 
		boolean output = false;
		
		double val = ib.common.adapt_price(val_);
		if (!common.price_is_ok(val)) return output;
		
		output = true;
		
		if (is_main_) _start = val;
		else _stop = val;
		
		return output;
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
	
	public String serialise() { return toString(); }
	
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
		vals.put("stop2", _stop2);
		vals.put("start", _start);
		vals.put("start2", _start2);
		
		return strings.to_string(vals);
	}

	public boolean equals(_order order2_)
	{
		if (!is_ok(order2_)) return false;

		return 
		(
			_temp_type.equals(order2_._type_place) && _temp_symbol.equals(order2_._symbol) && (_quantity == order2_._quantity) &&  
			(_id_main == order2_._id_main) && (_id_sec == order2_._id_sec) && (_stop == order2_._stop) && (_start == order2_._start) && 
			(_start2 == order2_._start2) && _type_main.equals(order2_._type_main) && _type_sec.equals(order2_._type_sec)
		);		
	}

	private static int __get_new_order_id()
	{
		int order_id = sync.__get_order_id();
		
		int order_id2 = orders.get_highest_order_id();
		
		if (order_id2 > ib.orders.MIN_ORDER_ID) 
		{
			order_id2++;
			
			if (order_id <= common.WRONG_ORDER_ID || order_id < order_id2) order_id = order_id2;
		}
				
		return order_id;
	}
	
	private void instantiate(_order input_)
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
		_temp_type = ib.orders.check_place(type_place_);
		_temp_symbol = check_symbol(symbol_);

		return (strings.are_ok(new String[] { _temp_type, _temp_symbol }) && quantity_ > common.WRONG_QUANTITY && (stop_ > common.WRONG_PRICE) && (start_ > common.WRONG_PRICE || _temp_type.equals(TYPE_PLACE_MARKET)) && (start2_ > common.WRONG_PRICE || !_temp_type.equals(TYPE_PLACE_STOP_LIMIT)) && id_main_ > common.WRONG_ORDER_ID);
	}

	private void populate(String type_place_, String symbol_, double quantity_, double stop_, double start_, double start2_, int id_main_)
	{
		_type_place = type_place_;
		_type_main = get_type_from_place(true);
		_type_sec = get_type_from_place(false);
		_symbol = symbol_;
		_quantity = quantity_;
		_stop = ib.common.adapt_price(stop_);
		_start = ib.common.adapt_price(start_);
		_start2 = ib.common.adapt_price(start2_);
		_id_main = id_main_;
		_id_sec = get_id_sec(id_main_);
	}
	
	private String get_type_from_place(boolean is_main_) { return (is_main_ ? external_ib.orders.get_type(_type_place) : TYPE_STOP); }
}