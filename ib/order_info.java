package ib;

import accessory.numbers;
import accessory.strings;
import accessory_ib.types;

public class order_info 
{
	public String type = strings.DEFAULT;
	public String symbol = strings.DEFAULT;
	public int quantity = numbers.DEFAULT_INT;
	public int id = numbers.DEFAULT_INT;
	public double stop = numbers.DEFAULT_DEC;
	public double start = numbers.DEFAULT_DEC;

	public order_info (order_info input_)
	{
		if (input_ == null) return;
		
		type = input_.type;
		symbol = input_.symbol;
		quantity = input_.quantity;
		id = input_.id;
		stop = input_.stop;
		start = input_.start;
	}
	
	public order_info (String type_, String symbol_, int quantity_, int id_, double stop_, double start_)
	{
		String type_val = types.check_order(type_);
		if 
		(
			!strings.is_ok(type_val) || !strings.is_ok(symbol_) || 
			!numbers.is_ok(id_, orders.MIN_ID, orders.MAX_ID) ||
			quantity_ <= 0 || stop_ <= 0 || start_ <= 0
		) 
		{ return; }
		
		type = type_val;
		symbol = symbol_;
		quantity = quantity_;
		id = id_;
		stop = stop_;
		start = start_;
	}
}