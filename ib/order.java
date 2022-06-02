package ib;

import java.util.HashMap;

import accessory.parent;
import accessory.strings;

public class order extends parent
{
	private int _id_main = sync.WRONG_ID;
	private int _id_sec = sync.WRONG_ID;	
	private String _type = strings.DEFAULT;
	private String _symbol = strings.DEFAULT;
	private double _quantity = sync_orders.WRONG_VALUE;
	private double _stop = sync_orders.WRONG_VALUE;
	private double _start = sync_orders.WRONG_VALUE;

	private String _temp_type = strings.DEFAULT;
	private String _temp_symbol = strings.DEFAULT;
		
	public static boolean are_equal(order order1_, order order2_) { return are_equal_common(order1_, order2_); }

	public static String check_type(String type_) { return sync_orders.check_place(type_); }

	public static String check_symbol(String symbol_) { return (strings.is_ok(symbol_) ? symbol_.trim().toUpperCase() : strings.DEFAULT); }

	public order(order input_) { instantiate(input_); }
	
	public order(String type_, String symbol_, double quantity_, double stop_, double start_) { instantiate(type_, symbol_, quantity_, stop_, start_, sync.get_next_id()); }

	public String get_type() { return _type; }

	public String get_symbol() { return _symbol; }
	
	public int get_id_main() { return _id_main; }
	
	public int get_id_sec() { return _id_sec; }
	
	public double get_quantity() { return _quantity; }

	public double get_stop() { return _stop; }

	public double get_start() { return _start; }

	public void update_stop(double stop_) { _stop = stop_; }

	public void update_start(double start_) { _start = start_; }
	
	public String toString()
	{
		if (!is_ok(_type, _symbol, _quantity, _stop, _start, _id_main)) return strings.DEFAULT;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put("id_main", _id_main);
		vals.put("id_sec", _id_sec);
		vals.put("type", _type);
		vals.put("symbol", _symbol);
		vals.put("quantity", _quantity);
		vals.put("stop", _stop);
		vals.put("start", _start);

		return strings.to_string(vals);
	}

	public boolean equals(order order2_)
	{
		if (!is_ok(order2_)) return false;

		return 
		(
			_temp_type.equals(order2_._type) && _temp_symbol.equals(order2_._symbol) && (_quantity == order2_._quantity) && 
			(_id_main == order2_._id_main) && (_id_sec == order2_._id_sec) && (_stop == order2_._stop) && (_start == order2_._start)
		);		
	}

	private void instantiate(order input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(_temp_type, _temp_symbol, input_._quantity, input_._stop, input_._start, input_._id_main);
	}

	private void instantiate(String type_, String symbol_, double quantity_, double stop_, double start_, int id_main_)
	{
		instantiate_common();
		if (!is_ok(type_, symbol_, quantity_, stop_, start_, id_main_)) return;

		populate(_temp_type, _temp_symbol, quantity_, stop_, start_, id_main_);
	}

	public boolean is_ok() { return is_ok(_type, _symbol, _quantity, _stop, _start, _id_main); }
			
	private boolean is_ok(String type_, String symbol_, double quantity_, double stop_, double start_, int id_main_)
	{
		_temp_type = check_type(type_);
		_temp_symbol = check_symbol(symbol_);
		
		return (strings.are_ok(new String[] { _temp_type, _temp_symbol }) && quantity_ > 0 && stop_ > 0 && start_ > 0 && common.req_id_is_ok_sync(id_main_));
	}

	private void populate(String type_, String symbol_, double quantity_, double stop_, double start_, int id_main_)
	{
		_type = type_;
		_symbol = symbol_;
		_quantity = quantity_;
		_stop = stop_;
		_start = start_;
		_id_main = id_main_;
		_id_sec = sync_orders.get_id_sec(id_main_);
	}
}