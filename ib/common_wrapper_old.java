package ib;

abstract class common_wrapper_old 
{
	public static void tick_price(int id_, int field_ib_, double price_) 
	{ 
		if (async_data_market.is_ok()) async_data_market.tick_price(id_, field_ib_, price_);

		if (async_data_trades.is_ok()) async_data_trades.tick_price(id_, field_ib_, price_);
			
		if (async_data_watchlist.is_ok()) async_data_watchlist.tick_price(id_, field_ib_, price_);
	}
	
	public static void tick_size(int id_, int field_ib_, int size_) 
	{ 
		if (async_data_market.is_ok()) async_data_market.tick_size(id_, field_ib_, size_);

		if (async_data_trades.is_ok()) async_data_trades.tick_size(id_, field_ib_, size_);

		if (async_data_watchlist.is_ok()) async_data_watchlist.tick_size(id_, field_ib_, size_);
	}
	
	public static void _tick_generic(int id_, int field_ib_, double value_) 
	{
		if (async_data_market.is_ok()) async_data_market.tick_generic(id_, field_ib_, value_);

		if (async_data_trades.is_ok()) async_data_trades.tick_generic(id_, field_ib_, value_);

		if (async_data_watchlist.is_ok()) async_data_watchlist.tick_generic(id_, field_ib_, value_);
	}
	
	public static void _tick_snapshot_end(int id_)
	{
		if (async_data_market.is_ok()) async_data_market.tick_snapshot_end(id_);
			
		if (async_data_trades.is_ok()) async_data_trades.tick_snapshot_end(id_);
			
		if (async_data_watchlist.is_ok()) async_data_watchlist.tick_snapshot_end(id_);
	}
}