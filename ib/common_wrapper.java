package ib;

public abstract class common_wrapper
{
	public static void __tickPrice(int id_, int field_ib_, double price_)
	{
		if (async_market.__is_ok(id_)) async_market.__tick_price(id_, field_ib_, price_);
	}
	
	public static void __tickSize(int id_, int field_ib_, int size_)
	{
		if (async_market.__is_ok(id_)) async_market.__tick_size(id_, field_ib_, size_);
	}
	
	public static void __tickGeneric(int id_, int tick_, double value_)
	{
		if (async_market.__is_ok(id_)) async_market.__tick_generic(id_, tick_, value_);
	}
	
	public static void __tickSnapshotEnd(int id_)
	{
		//In some cases, reaching this point might take too long and relying on tickSize could
		//appreciably speed everything up. That is, all the relevant information is assumed to 
		//have already been received right after getting certain size value.

		if (async_market.__is_ok(id_)) async_market.__stop_snapshot(id_);
	}	
}