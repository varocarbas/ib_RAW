package ib;

import accessory.numbers;
import accessory.strings;
import accessory_ib.types;

public class order_info 
{
	public String type = strings.DEFAULT;
	public String symbol = strings.DEFAULT;
	public double quantity = numbers.DEFAULT_DEC;
	public int id = numbers.DEFAULT_INT;
	public double stop = numbers.DEFAULT_DEC;
	public double start = numbers.DEFAULT_DEC;

	public static boolean is_ok(order_info input_)
	{
		if (input_ == null) return false;

		return is_ok(input_.type, input_.symbol, input_.quantity, input_.stop, input_.start, input_.id);
	}

	private static boolean is_ok(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		return strings.is_ok(check(type_, symbol_, quantity_, stop_, start_, id_));
	}

	private static String check(String type_, String symbol_, double quantity_, double stop_, double start_, int id_)
	{
		String output = types.check_order_place(type_);

		return
		(
			(
				!strings.is_ok(output) || !strings.is_ok(symbol_) || 
				quantity_ <= 0 || stop_ <= 0 || start_ <= 0 || !orders.id_is_ok(id_)
			)
			? strings.DEFAULT : output
		);	
	}

	public order_info (order_info input_)
	{
		if (!is_ok(input_)) return;

		type = input_.type;
		symbol = input_.symbol;
		quantity = input_.quantity;
		id = input_.id;
		stop = input_.stop;
		start = input_.start;
	}

	public order_info (String type_, String symbol_, double quantity_, double stop_, double start_)
	{
		int id0 = sync.get_next_id();

		String type0 = check(type_, symbol_, quantity_, stop_, start_, id0);
		if (!strings.is_ok(type0)) return;

		type = type0;
		symbol = symbol_;
		quantity = quantity_;
		id = id0;
		stop = stop_;
		start = start_;
	}
}