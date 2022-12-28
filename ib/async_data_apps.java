package ib;

import java.util.ArrayList;

import accessory.arrays;
import accessory.strings;

abstract class async_data_apps 
{	
	public static void tick_price(String app_, int id_, double price_, String symbol_)
	{
		if (price_ <= ib.common.WRONG_PRICE) return;
		
		if (app_.equals(async_data_watchlist._APP)) async_data_watchlist.tick_price(id_, price_, symbol_);
		else if (app_.equals(async_data_trades._APP)) async_data_trades.tick_price(id_, price_, symbol_);
	}

	public static void tick_volume(String app_, int id_, double volume_, String symbol_)
	{
		if (volume_ <= ib.common.WRONG_SIZE) return;

		if (app_.equals(async_data_watchlist._APP)) async_data_watchlist.tick_volume(id_, volume_, symbol_);	
	}
	
	public static void enable(String app_)
	{
		if (app_.equals(async_data_watchlist._APP)) async_data_watchlist._enabled = true;		
		else if (app_.equals(async_data_trades._APP)) async_data_trades._enabled = true;		
		else if (app_.equals(async_data_market._APP)) async_data_market._enabled = true;		
	}
	
	public static void disable(String app_)
	{
		if (app_.equals(async_data_watchlist._APP)) async_data_watchlist._enabled = false;		
		else if (app_.equals(async_data_trades._APP)) async_data_trades._enabled = false;		
		else if (app_.equals(async_data_market._APP)) async_data_market._enabled = false;		
	}

	public static boolean is_quick(String app_)
	{
		boolean output = async_data.DEFAULT_IS_QUICK;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist.is_quick();
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades.is_quick();
		else if (app_.equals(async_data_market._APP)) output = async_data_market.is_quick();

		return output;
	}

	public static String get_source(String app_)
	{
		String output = strings.DEFAULT;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist.SOURCE;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades.SOURCE;
		else if (app_.equals(async_data_market._APP)) output = async_data_market.SOURCE;

		return output;
	}	
	
	public static boolean includes_time(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._includes_time;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._includes_time;
		else if (app_.equals(async_data_market._APP)) output = async_data_market._includes_time;

		return output;
	}

	public static boolean includes_time_elapsed(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._includes_time_elapsed;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._includes_time_elapsed;
		else if (app_.equals(async_data_market._APP)) output = async_data_market._includes_time_elapsed;

		return output;
	}

	public static boolean includes_halted(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._includes_halted;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._includes_halted;
		else if (app_.equals(async_data_market._APP)) output = async_data_market._includes_halted;

		return output;
	}

	public static boolean includes_halted_tot(String app_)
	{
		boolean output = false;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._includes_halted_tot;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._includes_halted_tot;
		else if (app_.equals(async_data_market._APP)) output = async_data_market._includes_halted_tot;

		return output;
	}

	public static boolean logs_to_screen(String app_) 
	{
		boolean output = async_data.DEFAULT_LOGS_TO_SCREEN;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._logs_to_screen;
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._logs_to_screen;
		else if (app_.equals(async_data_market._APP)) output = async_data_market._logs_to_screen;

		return output;		
	}

	public static int get_max_mins_inactive(String app_)
	{
		int output = async_data.DEFAULT_MAX_MINS_INACTIVE;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist.MAX_MINS_INACTIVE;		
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades.MAX_MINS_INACTIVE;		
		else if (app_.equals(async_data_market._APP)) output = async_data_market.MAX_MINS_INACTIVE;		
	
		return output;
	}

	public static boolean disable_asap(String app_)
	{
		boolean output = async_data.DEFAULT_DISABLE_ASAP;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._disable_asap;		
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._disable_asap;		
		else if (app_.equals(async_data_market._APP)) output = async_data_market._disable_asap;		
	
		return output;
	}

	public static boolean only_essential(String app_)
	{
		boolean output = async_data.DEFAULT_ONLY_ESSENTIAL;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._only_essential;		
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._only_essential;		
		else if (app_.equals(async_data_market._APP)) output = async_data_market._only_essential;		
	
		return output;
	}

	public static boolean snapshot_nonstop(String app_)
	{
		boolean output = async_data.DEFAULT_SNAPSHOT_NONSTOP;
		
		if (app_.equals(async_data_watchlist._APP)) output = async_data_watchlist._snapshot_nonstop;		
		else if (app_.equals(async_data_trades._APP)) output = async_data_trades._snapshot_nonstop;		
		else if (app_.equals(async_data_market._APP)) output = async_data_market._snapshot_nonstop;		
	
		return output;
	}
	
	public static void add_to_stopping(String app_, String symbol_) 
	{
		if (is_stopping(app_, symbol_)) return;

		int i = 1;
		
		if (app_.equals(async_data_watchlist._APP)) 
		{
			i += async_data_watchlist._last_i_stopping;
			if (i > async_data_watchlist.MAX_I) i = 0;
			
			async_data_watchlist._stopping[i] = symbol_;
			
			async_data_watchlist._last_i_stopping = i;
		}
		else if (app_.equals(async_data_trades._APP)) 
		{
			i += async_data_trades._last_i_stopping;
			if (i > async_data_trades.MAX_I) i = 0;
			
			async_data_trades._stopping[i] = symbol_;
			
			async_data_trades._last_i_stopping = i;
		}
		else if (app_.equals(async_data_market._APP)) 
		{
			i += async_data_market._last_i_stopping;
			if (i > async_data_market.MAX_I) i = 0;
			
			async_data_market._stopping[i] = symbol_;
			
			async_data_market._last_i_stopping = i;
		}
	}

	public static void remove_from_stopping(String app_, String symbol_) 
	{
		int i = 0;
		
		if (app_.equals(async_data_watchlist._APP)) 
		{
			i = async_data.get_i((String[])arrays.get_new(async_data_watchlist._stopping), async_data_watchlist._last_i_stopping, async_data_watchlist.MAX_I, symbol_);
			if (i > async_data.WRONG_I) async_data_watchlist._stopping[i] = null;
		}
		else if (app_.equals(async_data_trades._APP)) 
		{
			i = async_data.get_i((String[])arrays.get_new(async_data_trades._stopping), async_data_trades._last_i_stopping, async_data_trades.MAX_I, symbol_);
			if (i > async_data.WRONG_I) async_data_trades._stopping[i] = null;
		}
		else if (app_.equals(async_data_market._APP)) 
		{
			i = async_data.get_i((String[])arrays.get_new(async_data_market._stopping), async_data_market._last_i_stopping, async_data_market.MAX_I, symbol_);
			if (i > async_data.WRONG_I) async_data_market._stopping[i] = null;
		}
	}

	public static boolean is_stopping(String app_, String symbol_) 
	{
		int i = async_data.WRONG_I;

		if (app_.equals(async_data_watchlist._APP)) i = async_data.get_i(async_data_watchlist._stopping, async_data_watchlist._last_i_stopping, async_data_watchlist.MAX_I, symbol_); 
		else if (app_.equals(async_data_trades._APP)) i = async_data.get_i(async_data_trades._stopping, async_data_trades._last_i_stopping, async_data_trades.MAX_I, symbol_);
		else if (app_.equals(async_data_market._APP)) i = async_data.get_i(async_data_market._stopping, async_data_market._last_i_stopping, async_data_market.MAX_I, symbol_);
		
		return (i != async_data.WRONG_I); 
	}

	public static ArrayList<Integer> populate_fields(String app_)
	{
		ArrayList<Integer> output = null;
		
		if (app_.equals(async_data_watchlist._APP)) 
		{
			async_data_watchlist.populate_fields();
			
			output = new ArrayList<Integer>(async_data_watchlist._fields);
		}
		else if (app_.equals(async_data_trades._APP)) 
		{
			async_data_trades.populate_fields();
			
			output = new ArrayList<Integer>(async_data_trades._fields);
		}
		else if (app_.equals(async_data_market._APP)) 
		{
			async_data_market.populate_fields();
			
			output = new ArrayList<Integer>(async_data_market._fields);
		}
		
		return output;
	}
}