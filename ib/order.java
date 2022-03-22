package ib;

import accessory.numbers;
import accessory.strings;
import accessory_ib.types;

public class order 
{
	public String _type = strings.DEFAULT;
	public String _symbol = strings.DEFAULT;
	public double _quantity = numbers.DEFAULT_DECIMAL;
	public int _id = numbers.DEFAULT_INT;
	public double _stop = numbers.DEFAULT_DECIMAL;
	public double _start = numbers.DEFAULT_DECIMAL;

	public static boolean is_ok(order input_)
	{
		if (input_ == null) return false;

		return is_ok(input_._type, input_._symbol, input_._quantity, input_._stop, input_._start, input_._id);
	}

	public order(order input_)
	{
		if (!is_ok(input_)) return;

		_type = input_._type;
		_symbol = input_._symbol;
		_quantity = input_._quantity;
		_id = input_._id;
		_stop = input_._stop;
		_start = input_._start;
	}
	
	public order(String type_, String symbol_, double quantity_, double stop_, double start_)
	{
		int id = sync.get_next_id();
		String type = check_type(type_);
		if (!strings.is_ok(type) || !is_ok(symbol_, quantity_, stop_, start_, id)) return;
		
		_type = type;
		_symbol = symbol_;
		_quantity = quantity_;
		_id = id;
		_stop = stop_;
		_start = start_;
	}
		
	private static boolean is_ok(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		String type = check_type(type_);
		
		return (strings.is_ok(type) && is_ok(symbol_, quantity_, stop_, start_, id_));
	}

	private static boolean is_ok(String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		return (!strings.is_ok(symbol_) || quantity_ <= 0 || stop_ <= 0 || start_ <= 0 || !orders.id_is_ok(id_));
	}

	private static String check_type(String type_)
	{
		return types.check_order_place(type_);
	}
}