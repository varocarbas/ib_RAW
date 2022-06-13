package external_ib;

import accessory.numbers;

public class errors 
{
	//------ To be synced with the corresponding get_all_[...]() methods.

	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/message_codes.html.	
	public static final int ERROR_200 = 200; //The specified contract does not match any in IB's database, usually because of an incorrect or missing parameter.
                                             //Ambiguity may occur when the contract definition provided is not unique.
                                             //For some stocks that has the same Symbol, Currency and Exchange, you need to specify the IBApi.Contract.PrimaryExch attribute to avoid ambiguity. Please refer to a sample stock contract here.
                                             //For futures that has multiple multipliers for the same expiration, You need to specify the IBApi.Contract.Multiplier attribute to avoid ambiguity. Please refer to a sample futures contract here.
	public static final int ERROR_202 = 202; //An active order on the IB server was cancelled. See Order Placement Considerations for additional information/considerations for these errors.
	public static final int ERROR_300 = 300; //An attempt was made to cancel market data for a ticker ID that was not associated with a current subscription. With the DDE API this occurs by clearing the spreadsheet cell.

	private static final int MIN_WARNING = 2000;
	private static final int MAX_WARNING = 3000;
	//---

	//------

	public static boolean is_warning(int code_) { return numbers.is_ok(code_, MIN_WARNING, MAX_WARNING); }
}