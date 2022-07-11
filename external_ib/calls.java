package external_ib;

import com.ib.client.Contract;
import com.ib.client.Order;

import ib.conn;

public abstract class calls 
{	
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

	public static void reqAccountSummary(int id_) { if (conn._client != null) conn._client.reqAccountSummary(id_, "All", "AvailableFunds"); }

	public static void cancelAccountSummary(int id_) { if (conn._client != null) conn._client.cancelAccountSummary(id_); }
	
	public static void reqAllOpenOrders() { if (conn._client != null) conn._client.reqAllOpenOrders(); }

	public static void reqIds() { if (conn._client != null) conn._client.reqIds(-1); }

	public static void eConnect(String arg0_, int arg1_, int arg2_) { if (conn._client != null) conn._client.eConnect(arg0_, arg1_, arg2_); }

	public static void eDisconnect() { if (conn._client != null) conn._client.eDisconnect(); }
}