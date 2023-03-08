package ib;

abstract class common_wrapper_quicker 
{
	public static void __tick_price(int id_, int field_ib_, double price_) { async_data_quicker.__tick_price(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { async_data_quicker.__tick_size(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int field_ib_, double value_) { async_data_quicker.__tick_generic(id_, field_ib_, value_); }
	
	public static void __tick_snapshot_end(int id_) { async_data_quicker.__tick_snapshot_end(id_); }	
}