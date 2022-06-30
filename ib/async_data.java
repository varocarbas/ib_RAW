package ib;

import accessory_ib.config;
import accessory_ib.types;

public class async_data 
{
	public static final String CONFIG_SNAPSHOT_QUICK = types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK;
	public static final String CONFIG_SNAPSHOT_NONSTOP = types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP;

	public static final String TYPE_SNAPSHOT = types.ASYNC_MARKET_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_MARKET_STREAM;
	
	public static final int PRICE = external_ib.market.TICK_LAST;
	public static final int OPEN = external_ib.market.TICK_OPEN;
	public static final int CLOSE = external_ib.market.TICK_CLOSE;
	public static final int LOW = external_ib.market.TICK_LOW;
	public static final int HIGH = external_ib.market.TICK_HIGH;
	public static final int ASK = external_ib.market.TICK_ASK;
	public static final int BID = external_ib.market.TICK_BID;
	public static final int HALTED = external_ib.market.TICK_HALTED;
	public static final int VOLUME = external_ib.market.TICK_VOLUME;
	public static final int SIZE = external_ib.market.TICK_LAST_SIZE;
	public static final int ASK_SIZE = external_ib.market.TICK_ASK_SIZE;
	public static final int BID_SIZE = external_ib.market.TICK_BID_SIZE;

	public static final boolean DEFAULT_SNAPSHOT_QUICK = true;
	public static final boolean DEFAULT_SNAPSHOT_NONSTOP = true;
	public static final int DEFAULT_DATA_TYPE = external_ib.market.DATA_LIVE;
	public static final boolean DEFAULT_LOGS_TO_SCREEN = true;
	public static final boolean DEFAULT_IS_DB_QUICK = true;
	public static final int DEFAULT_NONSTOP_PAUSE = 0;
	
	public static boolean snapshot_is_quick() { return config.get_async_boolean(CONFIG_SNAPSHOT_QUICK); }

	public static boolean snapshot_is_nonstop() { return config.get_async_boolean(CONFIG_SNAPSHOT_NONSTOP); }

	static String get_type(boolean is_snapshot_) { return (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM); }
}