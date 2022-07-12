package ib;

import java.util.HashMap;

import accessory_ib._alls;

class async_data_market extends parent_async_data 
{	
	public static String _ID = "market";
	
	public static final String SOURCE = market.SOURCE;
	
	public static final String PRICE = market.PRICE;
	public static final String SIZE = market.SIZE;
	public static final String OPEN = market.OPEN;
	public static final String CLOSE = market.CLOSE;
	public static final String LOW = market.LOW;
	public static final String HIGH = market.HIGH;
	public static final String VOLUME = market.VOLUME;		
	public static final String ASK = market.ASK;
	public static final String ASK_SIZE = market.ASK_SIZE;
	public static final String BID = market.BID;
	public static final String BID_SIZE = market.BID_SIZE;
	public static final String HALTED = market.HALTED;
	public static final String HALTED_TOT = market.HALTED_TOT;
	
	public static async_data_market _instance = instantiate();
	
	private async_data_market() { }
 	
	private static async_data_market instantiate()
	{
		async_data_market instance = new async_data_market();
		
		instance._source = SOURCE;
		instance._id = _ID;
		instance._includes_halted = true;
		instance._includes_halted_tot = true;
		
		return instance;
	}

	static void __tick_price(int id_, int field_ib_, double price_) { _instance.__tick_price_internal(id_, field_ib_, price_); }
	
	static void __tick_size(int id_, int field_ib_, int size_) { _instance.__tick_size_internal(id_, field_ib_, size_); }
	
	static void __tick_generic(int id_, int tick_, double value_) { _instance.__tick_generic_internal(id_, tick_, value_); }
		
	protected HashMap<Integer, String> get_all_prices() { return _alls.MARKET_PRICES; }
	
	protected HashMap<Integer, String> get_all_sizes() { return _alls.MARKET_SIZES; }
	
	protected HashMap<Integer, String> get_all_generics() { return _alls.MARKET_GENERICS; }
	
	protected String[] get_fields() { return db_ib.market.get_fields(); }
}