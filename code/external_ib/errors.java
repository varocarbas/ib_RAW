package external_ib;

import accessory.numbers;
import accessory.strings;

public abstract class errors 
{
	//------ To be synced with the corresponding get_all_[...]() methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/message_codes.html.	
	public static final int ERROR_200 = 200; //The specified contract does not match any in IB's database, usually because of an incorrect or missing parameter.
                                             //Ambiguity may occur when the contract definition provided is not unique.
                                             //For some stocks that has the same Symbol, Currency and Exchange, you need to specify the IBApi.Contract.PrimaryExch attribute to avoid ambiguity. Please refer to a sample stock contract here.
                                             //For futures that has multiple multipliers for the same expiration, You need to specify the IBApi.Contract.Multiplier attribute to avoid ambiguity. Please refer to a sample futures contract here.
	public static final int ERROR_202 = 202; //An active order on the IB server was cancelled. See Order Placement Considerations for additional information/considerations for these errors.
	public static final int ERROR_300 = 300; //An attempt was made to cancel market data for a ticker ID that was not associated with a current subscription. With the DDE API this occurs by clearing the spreadsheet cell.
	public static final int ERROR_322 = 322; //Server error when processing an API client request.
	public static final int ERROR_354 = 354; //You do not have live market data available in your account for the specified instruments. For further details please refer to Streaming Market Data.
	public static final int ERROR_1100 = 1100; //Your TWS/IB Gateway has been disconnected from IB servers. This can occur because of an internet connectivity issue, a nightly reset of the IB servers, or a competing session.
	public static final int ERROR_1102 = 1102; //The TWS/IB Gateway has successfully reconnected to IB's servers. Your market data requests have been recovered and there is no need for you to re-submit them.
	public static final int ERROR_10090 = 10090; //Indicates that some tick types requested require additional market data subscriptions not held in the account. 
												 //This commonly occurs for instance if a user has options subscriptions but not the underlying stock so the system cannot calculate the real time greek values (other default ticks will be returned). 
												 //Or alternatively, if generic tick types are specified in a market data request without the associated subscriptions.
	public static final int ERROR_10147 = 10147; //OrderId <OrderId> that needs to be cancelled is not found.
	public static final int ERROR_10148 = 10148; //An attempt was made to cancel an order that had already been filled by the system.
	public static final int ERROR_10186 = 10186; //Requested market data is not subscribed. Delayed market data is not enabled. See Market Data Types on how to enable delayed data.
	public static final int ERROR_10197 = 10197; //Indicates that the user is logged into the paper account and live account simultaneously trying to request live market data using both the accounts. In such a scenario preference would be given to the live account, for more details please refer: https://ibkr.info/node/1719.

	private static final int MIN = 100;
	private static final int MAX = 10284;
	
	private static final int MIN_WARNING = 2000;
	private static final int MAX_WARNING = 3000;
	//---

	//------

	public static boolean is_ok(int code_) { return (code_ >= MIN && code_ <= MAX); }
	
	public static boolean is_warning(int code_, String message_) 
	{ 
		boolean is_warning = numbers.is_ok(code_, MIN_WARNING, MAX_WARNING); 
		if (is_warning) return is_warning;

		is_warning = 
		(
			code_ == ERROR_202 || code_ == ERROR_300 || code_ == ERROR_322 || code_ == ERROR_354 || 
			code_ == ERROR_1100 || code_ == ERROR_1102 || code_ == ERROR_10090 || code_ == ERROR_10147 || 
			code_ == ERROR_10148 || code_ == ERROR_10186 || code_ == ERROR_10197
		);
		if (is_warning) return is_warning;
		
		String[] targets = new String[] { "warning", "requests limitation exceeded" };
		
		is_warning = strings.matches_any(targets, message_, true);
		
		return is_warning;
	}
	
	public static boolean delete_symbol(int code_) { return ((code_ == ERROR_200) || (code_ == ERROR_354) || (code_ == ERROR_10090) || (code_ == ERROR_10186)); }
}