package db_ib;

import java.util.HashMap;

import accessory.db_common;

public abstract class temp_price 
{
	public static final String SOURCE = common.SOURCE_TEMP_PRICE;
	
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String PRICE = common.FIELD_PRICE;
	
	private static String COL_SYMBOL = null;
	private static String COL_PRICE = null;
	
	public static boolean add(String symbol_)
	{
		boolean output = true;
		
		start();

		if (!db_common.exists(SOURCE, common.get_where_symbol(SOURCE, symbol_))) 
		{
			HashMap<String, String> vals = new HashMap<String, String>();
			vals.put(COL_SYMBOL, symbol_);
			
			output = db_common.insert(SOURCE, vals, true);
		}
	
		return output;
	}
	
	public static boolean update(String symbol_, double price_) { return db_common.update(SOURCE, COL_PRICE, price_, common.get_where_symbol(SOURCE, symbol_), false, true); }
	
	public static double get(String symbol_) { return db_common.get_decimal(SOURCE, COL_PRICE, common.get_where_symbol(SOURCE, symbol_), 0.0, false, true); }

	public static boolean delete(String symbol_) { return db_common.delete(SOURCE, common.get_where_symbol(SOURCE, symbol_)); }

	private static void start()
	{
		if (COL_SYMBOL != null) return;
		
		COL_SYMBOL = accessory.db.get_col(SOURCE, common.FIELD_SYMBOL);
		COL_PRICE = accessory.db.get_col(SOURCE, common.FIELD_PRICE);
	}
}