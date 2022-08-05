package external_ib;

import accessory.arrays;
import accessory.numbers;
import accessory_ib._alls;

public abstract class data 
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

	public static final int MAX_TICK = TICK_HALTED;
	
	public static final int WRONG_DATA = 0;
	public static final int WRONG_HALTED = HALTED_NA - 1;
	
	public static boolean is_halted(int val_) { return (halted_is_ok(val_) && (val_ != HALTED_NA && val_ != HALTED_NOT)); }

	public static boolean tick_is_ok(int val_) { return arrays.value_exists(get_all_ticks(), val_); }

	public static boolean halted_is_ok(int val_) { return numbers.is_ok(val_, HALTED_NA, HALTED_VOLATILITY); }
	
	public static boolean data_is_ok(int val_) { return numbers.is_ok(val_, DATA_LIVE, DATA_DELAYED_FROZEN); }
	
	public static int[] populate_all_ticks()
	{
		return new int[]
		{
			TICK_BID_SIZE, TICK_BID, TICK_ASK, TICK_ASK_SIZE, TICK_LAST, TICK_LAST_SIZE, 
			TICK_HIGH, TICK_LOW, TICK_VOLUME, TICK_CLOSE, TICK_OPEN, TICK_HALTED	
		};
	}

	private static int[] get_all_ticks() { return _alls.EXTERNAL_DATA_TICKS; }
}