package ib;

import java.util.HashMap;

import accessory.numbers;
import accessory.parent;
import accessory.strings;
import accessory_ib.types;

public class order extends parent
{
	private String _type = strings.DEFAULT;
	private String _symbol = strings.DEFAULT;
	private double _quantity = numbers.DEFAULT_DECIMAL;
	private int _id = numbers.DEFAULT_INT;
	private double _stop = numbers.DEFAULT_DECIMAL;
	private double _start = numbers.DEFAULT_DECIMAL;

	private String _temp_type = strings.DEFAULT;
	private String _temp_symbol = strings.DEFAULT;
		
	public static boolean are_equal(order order1_, order order2_) { return are_equal_common(order1_, order2_); }

	public static String check_type(String type_) { return accessory.types.check_type(type_, types.ORDER_PLACE); }

	public static String check_symbol(String symbol_) { return (strings.is_ok(symbol_) ? symbol_.trim().toUpperCase() : strings.DEFAULT); }

	public order(order input_) { instantiate(input_); }
	
	public order(String type_, String symbol_, double quantity_, double stop_, double start_) { instantiate(type_, symbol_, quantity_, stop_, start_, sync.get_next_id()); }

	public String get_type() { return _type; }

	public String get_symbol() { return _symbol; }
	
	public int get_id() { return _id; }

	public double get_quantity() { return _quantity; }

	public double get_stop() { return _stop; }

	public double get_start() { return _start; }

	public void update_stop(double stop_) { _stop = stop_; }

	public void update_start(double start_) { _start = start_; }
	
	public String toString()
	{
		if (!is_ok(_type, _symbol, _quantity, _stop, _start, _id)) return strings.DEFAULT;

		HashMap<String, Object> vals = new HashMap<String, Object>();
		
		vals.put("type", _type);
		vals.put("id", _id);
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
			_temp_type.equals(order2_._type) && _temp_symbol.equals(order2_._symbol) &&
			(_quantity == order2_._quantity) && (_id == order2_._id) && 
			(_stop == order2_._stop) && (_start == order2_._start)
		);		
	}

	private void instantiate(order input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(_temp_type, _temp_symbol, input_._quantity, input_._stop, input_._start, input_._id);
	}

	private void instantiate(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		instantiate_common();
		if (!is_ok(type_, symbol_, quantity_, stop_, start_, id_)) return;

		populate(_temp_type, _temp_symbol, quantity_, stop_, start_, id_);
	}

	public boolean is_ok() { return is_ok(_type, _symbol, _quantity, _stop, _start, _id); }
			
	private boolean is_ok(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		_temp_type = check_type(type_);
		_temp_symbol = check_symbol(symbol_);
		
		return (strings.are_ok(new String[] { _temp_type, _temp_symbol }) && quantity_ > 0 && stop_ > 0 && start_ > 0 && orders.id_is_ok(id_));
	}

	private void populate(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		_type = type_;
		_symbol = symbol_;
		_quantity = quantity_;
		_stop = stop_;
		_start = start_;
		_id = id_;
	}
}