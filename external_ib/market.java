package external_ib;

import accessory.arrays;
import accessory_ib._alls;

public abstract class market 
{
	//------ To be synced with the corresponding get_all_[...]() methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/tick_types.html.
	public static final int TICK_BID_SIZE = 0;
	public static final int TICK_BID = 1;
	public static final int TICK_ASK = 2;
	public static final int TICK_ASK_SIZE = 3;
	public static final int TICK_LAST = 4;
	public static final int TICK_LAST_SIZE = 5;
	public static final int TICK_HIGH = 6;
	public static final int TICK_LOW = 7;
	public static final int TICK_VOLUME = 8;
	public static final int TICK_CLOSE = 9;
	public static final int TICK_OPEN = 14;
	public static final int TICK_HALTED = 49;

	public static final int HALTED_NA = -1;
	public static final int HALTED_NOT = 0;
	public static final int HALTED_GENERAL = 1;	
	public static final int HALTED_VOLATILITY = 2;	
	//---

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/market_data_type.html.
	public static final int DATA_LIVE = 1;
	public static final int DATA_FROZEN = 2;
	public static final int DATA_DELAYED = 3;
	public static final int DATA_DELAYED_FROZEN = 4;
	//---
	
	//------

	public static final int WRONG_DATA = 0;
	
	public static boolean tick_is_ok(int tick_) { return arrays.value_exists(get_all_ticks(), tick_); }

	public static boolean halt_is_ok(int halt_) { return arrays.value_exists(get_all_halted(), halt_); }

	public static boolean data_is_ok(int data_) { return arrays.value_exists(get_all_data(), data_); }

	public static int[] populate_all_ticks()
	{
		return new int[]
		{
			TICK_BID_SIZE, TICK_BID, TICK_ASK, TICK_ASK_SIZE, TICK_LAST, TICK_LAST_SIZE, 
			TICK_HIGH, TICK_LOW, TICK_VOLUME, TICK_CLOSE, TICK_OPEN, TICK_HALTED	
		};
	}

	public static int[] populate_all_halted() { return new int[] { HALTED_NA, HALTED_NOT, HALTED_GENERAL, HALTED_VOLATILITY }; }

	public static int[] populate_all_data() { return new int[] { DATA_LIVE, DATA_FROZEN, DATA_DELAYED, DATA_DELAYED_FROZEN }; }	

	private static int[] get_all_ticks() { return _alls.EXTERNAL_MARKET_TICKS; }

	private static int[] get_all_halted() { return _alls.EXTERNAL_MARKET_HALTED; }

	private static int[] get_all_data() { return _alls.EXTERNAL_MARKET_DATA; }	
}