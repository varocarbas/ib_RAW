package external_ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory.arrays;
import accessory.strings;
import accessory_ib._alls;
import ib.conn;

public abstract class calls 
{
	//--- To be synced with the info from https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html#acd761f48771f61dd0fb9e9a7d88d4f04.
	public static final String NET_LIQUIDATION = "NetLiquidation"; //The basis for determining the price of the assets in your account. Total cash value + stock value + options value + bond value.
	public static final String TOTAL_CASH_VALUE = "TotalCashValue"; //Total cash balance recognized at the time of trade + futures PNL.
	//---
	
	public static void eConnect(String arg0_, int arg1_, int arg2_) { if (conn._client != null) conn._client.eConnect(arg0_, arg1_, arg2_); }

	public static void eDisconnect() { if (conn._client != null) conn._client.eDisconnect(); }

	public static boolean reqMktData(int id_, String symbol_, boolean is_snapshot_) 
	{ 
		Contract contract = contracts.get_contract(symbol_);
		if (contract == null || conn._client == null) return false;
		
		conn._client.reqMktData(id_, contract, "", is_snapshot_, false, null); 
		
		return true;
	}

	public static void reqMarketDataType(int type_) { if (conn._client != null) conn._client.reqMarketDataType(type_); }

	public static void cancelMktData(int id_) { if (conn._client != null) conn._client.cancelMktData(id_); }

	public static void placeOrder(int id_, Contract contract_, Order order_) { if (conn._client != null) conn._client.placeOrder(id_, contract_, order_); }

	public static void cancelOrder(int id_) { if (conn._client != null) conn._client.cancelOrder(id_); }

	public static void reqAccountSummary(int id_) { if (conn._client != null) conn._client.reqAccountSummary(id_, "All", get_account_summary_tags()); }

	public static void cancelAccountSummary(int id_) { if (conn._client != null) conn._client.cancelAccountSummary(id_); }
	
	public static void reqAllOpenOrders() { if (conn._client != null) conn._client.reqAllOpenOrders(); }

	public static void reqIds() { if (conn._client != null) conn._client.reqIds(-1); }

	public static ArrayList<String> get_funds_fields(String key_)
	{
		ArrayList<String> fields = new ArrayList<String>();
		if (!strings.is_ok(key_)) return fields;
		
		for (Entry<String, String[]> item: get_all_keys_funds().entrySet())
		{
			if (arrays.value_exists(item.getValue(), key_)) fields.add(item.getKey());
		}
		
		return fields;
	}
	
	public static HashMap<String, String[]> populate_all_keys_funds()
	{		
		HashMap<String, String[]> all = new HashMap<String, String[]>();
		
		all.put(db_ib.basic.MONEY, new String[] { NET_LIQUIDATION });
		all.put(db_ib.basic.MONEY_FREE, new String[] { TOTAL_CASH_VALUE });
		
		return all;
	}
	
	public static HashMap<String, String[]> get_all_keys_funds() { return _alls.CALLS_KEYS_FUNDS; }

	private static String get_account_summary_tags()
	{
		String output = "";
		
		ArrayList<String> included = new ArrayList<String>();
		
		for (Entry<String, String[]> item: get_all_keys_funds().entrySet()) 
		{
			for (String key: item.getValue())
			{
				if (included.contains(key)) continue;
		
				included.add(key);
				
				if (output != "") output += ",";
				output += key;
			}
		}
		
		return output;
	}
}