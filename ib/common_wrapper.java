package ib;

public abstract class common_wrapper
{
	public static void __tickPrice(int id_, int field_ib_, double price_)
	{
		if (async_market.is_ok(id_)) async_market.__tick_price(id_, field_ib_, price_);
	}
}