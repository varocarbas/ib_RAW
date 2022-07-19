package ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.strings;

abstract class sync_trades 
{	
	public static final double WRONG_POSITION = trades.WRONG_POSITION;
	public static final double WRONG_PRICE = common.WRONG_PRICE;

	public static double get_position(String symbol_)
	{
		double output = WRONG_POSITION;
		
		HashMap<Double, String> all_positions = sync.get_positions();
		if (!arrays.is_ok(all_positions)) return output;
		
		ArrayList<Double> positions = new ArrayList<Double>();
		
		for (Entry<Double, String> item: all_positions.entrySet())
		{
			if (!strings.are_equal(symbol_, common.normalise_symbol(item.getValue()))) continue;
			
			positions.add(item.getKey());
		}
		
		int tot = positions.size();
		
		if (tot == 1) output = positions.get(0);
		else if (tot > 1) output = get_position(symbol_, positions);
		
		return output;
	}
	
	public static double get_unrealised(double pos_)
	{
		double output = WRONG_PRICE;
		
		HashMap<Double, Double> all_unrealised = sync.get_unrealised();
		if (!arrays.is_ok(all_unrealised)) return output;
	
		for (Entry<Double, Double> item: all_unrealised.entrySet())
		{
			double pos = item.getKey();
			double unrealised = adapt_money(item.getValue());
			
			if (pos == pos_) output = unrealised;
			
			db_ib.trades.update_unrealised(unrealised, pos);
		}
		
		return output;
	}
	
	private static double adapt_money(double val_) { return db_ib.common.adapt_number(val_, db_ib.common.FIELD_PRICE); }
	
	private static double get_position(String symbol_, ArrayList<Double> positions_)
	{
		ArrayList<Double> positions_db = db_ib.trades.get_all_positions(symbol_);
		if (!arrays.is_ok(positions_db)) return positions_.get(0);
		
		for (double position: positions_) 
		{ 
			if (!positions_db.contains(position)) return position;
		}

		return WRONG_POSITION;
	}
}