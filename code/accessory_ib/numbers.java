package accessory_ib;

public abstract class numbers 
{
	public static final int MAX_ROUND_DECIMALS_PRICES = 4;
	public static final double MAX_PRICE_MAX_ROUND_DECIMALS = 0.9999;
	
	public static final int DEFAULT_ROUND_DECIMALS_PRICES = 2;
	public static final int DEFAULT_ROUND_DECIMALS = 2;
	
	public static double round_price(double price_) { return accessory.numbers.round(price_, get_price_round_decimals(price_)); }

	public static int get_price_round_decimals(double price_) { return (price_ > MAX_PRICE_MAX_ROUND_DECIMALS ? DEFAULT_ROUND_DECIMALS_PRICES : MAX_ROUND_DECIMALS_PRICES); }
}