package external_ib;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ib.client.Bar;
import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDescription;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.DepthMktDataDescription;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.FamilyCode;
import com.ib.client.HistogramEntry;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.NewsProvider;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.PriceIncrement;
import com.ib.client.SoftDollarTier;
import com.ib.client.TickAttrib;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;

import accessory.strings;
import accessory_ib.logs;
import ib.common;
import ib.common_wrapper;

//Implementation of the IB's EWrapper interface (https://interactivebrokers.github.io/tws-api/interfaceIBApi_1_1EWrapper.html).

public class wrapper implements EWrapper 
{
	protected int currentOrderId = common.WRONG_ORDER_ID;
	
	private EReaderSignal _reader_signal;
	private EClientSocket _client_socket;
	
	public wrapper() 
	{
		_reader_signal = new EJavaSignal();
		_client_socket = new EClientSocket(this, _reader_signal);
	}

	public EClientSocket get_client() { return _client_socket; }

	public EReaderSignal get_signal() { return _reader_signal; }

	public int get_current_order_id() { return currentOrderId; }

	@Override
	public void accountSummary(int id_, String account_, String tag_, String value_, String currency_) { common_wrapper.account_summary(id_, account_, tag_, value_, currency_); }

	@Override
	public void accountSummaryEnd(int id_) { common_wrapper.account_summary_end(id_); }

	@Override
	public void nextValidId(int id_) 
	{
		currentOrderId = id_;

		common_wrapper.next_valid_id(id_);
	}

	@Override
	public void tickPrice(int id_, int field_ib_, double price_, TickAttrib attribs_) { common_wrapper.tick_price(id_, field_ib_, price_); }

	@Override
	public void tickSize(int id_, int field_ib_, int size_) { common_wrapper.tick_size(id_, field_ib_, size_); }

	@Override
	public void tickGeneric(int id_, int tick_, double value_) { common_wrapper.tick_generic(id_, tick_, value_); }

	@Override
	public void tickSnapshotEnd(int id_) { common_wrapper.tick_snapshot_end(id_); }

	@Override
	public void error(int id_, int code_, String message_) { common_wrapper.error(id_, code_, message_); }

	@Override
	public void orderStatus(int order_id_, String status_ib_, double filled_, double remaining_, double avg_fill_price_, int perm_id_, int parent_id_, double last_fill_price_, int client_id_, String why_held_, double mkt_cap_price_) { common_wrapper.order_status(order_id_, status_ib_); }

	@Override
	public void openOrderEnd() { common_wrapper.open_order_end(); }

	@Override
	public void execDetails(int id_, Contract contract_, Execution execution_) { common_wrapper.__exec_details(id_, contract_, execution_); }

	@Override
	public void commissionReport(CommissionReport report_) { common_wrapper.__commission_report(report_); }
	
	@Override
	public void connectAck() 
	{
		if (_client_socket.isAsyncEConnect()) 
		{
			logs.update_screen("Acknowledging connection");

			_client_socket.startAPI();
		}
	}

	@Override
	public void connectionClosed() { logs.update_screen("Connection closed"); }

	@Override
	public void error(Exception e_) { if (e_ != null) logs.update_screen(e_.getMessage()); }

	@Override
	public void error(String str_) { if (strings.is_ok(str_)) logs.update_screen(str_); }

	//-----------------------------------------------------
	//-----------------------------------------------------

	@Override
	public void position(String account_, Contract contract_, double pos_, double avg_cost_) { }

	@Override
	public void updatePortfolio(Contract contract_, double position_, double market_price_, double market_value_, double average_cost_, double unrealized_pnl_, double realized_pnl_, String account_name_) { }

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) { }

	@Override
	public void accountDownloadEnd(String account_name_) { }
	
	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) { }

	@Override
	public void tickString(int tickerId, int tickType, String value) { }

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) { }

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) { }

	@Override
	public void updateAccountTime(String timeStamp) { }

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) { }

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) { }

	@Override
	public void contractDetailsEnd(int reqId) { }

	@Override
	public void execDetailsEnd(int reqId) { }

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) { }

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size, boolean isSmartDepth) { }

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) { }

	@Override
	public void managedAccounts(String accountsList) { }

	@Override
	public void receiveFA(int faDataType, String xml) { }

	@Override
	public void historicalData(int reqId, Bar bar) { }

	@Override
	public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) { }

	@Override
	public void scannerParameters(String xml) { }

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) { }

	@Override
	public void scannerDataEnd(int reqId) { }

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) { }

	@Override
	public void currentTime(long time) { }

	@Override
	public void fundamentalData(int reqId, String data) { }

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) { }

	@Override
	public void marketDataType(int reqId, int marketDataType) { }

	@Override
	public void positionEnd() { }
	
	@Override
	public void verifyMessageAPI(String apiData) { }

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) { }

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) { }

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) { }

	@Override
	public void displayGroupList(int reqId, String groups) { }

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) { }

	@Override
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, double pos, double avgCost) { }

	@Override
	public void positionMultiEnd(int reqId) { }

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) { }

	@Override
	public void accountUpdateMultiEnd(int reqId) { }

	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) { }

	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) { }

	@Override
	public void softDollarTiers(int reqId, SoftDollarTier[] tiers) { }

	@Override
	public void familyCodes(FamilyCode[] familyCodes) { }

	@Override
	public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) { }

	@Override
	public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) { }

	@Override
	public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) { }

	@Override
	public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) { }

	@Override
	public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) { }

	@Override
	public void newsProviders(NewsProvider[] newsProviders) { }

	@Override
	public void newsArticle(int requestId, int articleType, String articleText) { }

	@Override
	public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) { }

	@Override
	public void historicalNewsEnd(int requestId, boolean hasMore) { }

	@Override
	public void headTimestamp(int reqId, String headTimestamp) { }

	@Override
	public void histogramData(int reqId, List<HistogramEntry> items) { }

	@Override
	public void historicalDataUpdate(int reqId, Bar bar) { }

	@Override
	public void rerouteMktDataReq(int reqId, int conId, String exchange) { }

	@Override
	public void rerouteMktDepthReq(int reqId, int conId, String exchange) { }

	@Override
	public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) { }

	@Override
	public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) { }

	@Override
	public void pnlSingle(int reqId, int pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) { }

	@Override
	public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) { }

	@Override
	public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) { }   

	@Override
	public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) { }

	@Override
	public void tickByTickAllLast(int reqId, int tickType, long time, double price, int size, TickAttribLast tickAttribLast,String exchange, String specialConditions) { }

	@Override
	public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize, TickAttribBidAsk tickAttribBidAsk) { }

	@Override
	public void tickByTickMidPoint(int reqId, long time, double midPoint) { }

	@Override
	public void orderBound(long orderId, int apiClientId, int apiOrderId) { }

	@Override
	public void completedOrder(Contract contract, Order order, OrderState orderState) { }

	@Override
	public void completedOrdersEnd() { }
}