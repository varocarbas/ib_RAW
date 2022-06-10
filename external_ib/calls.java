package external_ib;

import com.ib.client.Contract;
import com.ib.client.Order;

import accessory_ib.types;
import ib.conn;

public class calls 
{	
	public static String get_class_id() { return accessory.types.get_id(types.ID_CALLS); }

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

	public static void reqAllOpenOrders() { if (conn._client != null) conn._client.reqAllOpenOrders(); }

	public static void reqIds() { if (conn._client != null) conn._client.reqIds(-1); }
}