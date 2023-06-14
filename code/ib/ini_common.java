package ib;

import accessory.numbers;

public abstract class ini_common 
{
	public static final int DEFAULT_ROUND_DECIMALS = 2;

	public static void start() { numbers.update_round_decimals(DEFAULT_ROUND_DECIMALS); }
}