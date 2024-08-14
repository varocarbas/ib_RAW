package external_ib;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ib.client.*;

import accessory.strings;
import accessory_ib.logs;
import ib.common;
import ib.wrapper_errors;
import ib.wrapper_main;

//Implementation of the EWrapper interface (v. 10.19.04) from the 
//Interactive Brokers's TWS API (https://ibkrcampus.com/campus/ibkr-api-page/twsapi-doc/).

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
	public void accountSummary(int id_, String account_, String tag_, String value_, String currency_) { wrapper_main.account_summary(id_, account_, tag_, value_, currency_); }

	@Override
	public void accountSummaryEnd(int id_) { wrapper_main.account_summary_end(id_); }

	@Override
	public void nextValidId(int id_) 
	{
		currentOrderId = id_;

		wrapper_main.next_valid_id(id_);
	}

	@Override
	public void tickPrice(int id_, int field_ib_, double price_, TickAttrib attribs_) { wrapper_main.__tick_price(id_, field_ib_, price_); }

	@Override
	public void tickSize(int id_, int field_ib_, Decimal size_) 
	{
		int size = 0;
		if ((size_ != null) && (size_.value().doubleValue() <= Integer.MAX_VALUE)) size = size_.value().intValue();
		
		wrapper_main.__tick_size(id_, field_ib_, size); 
	}

	@Override
	public void tickGeneric(int id_, int tick_, double value_) { wrapper_main.__tick_generic(id_, tick_, value_); }

	@Override
	public void tickSnapshotEnd(int id_) { wrapper_main.__tick_snapshot_end(id_); }

	@Override
	public void error(int id_, int code_, String message_, String advanced_order_rejectJson_) { wrapper_errors.__manage(id_, code_, message_, advanced_order_rejectJson_); }

	@Override
	public void orderStatus(int order_id_, String status_ib_, Decimal filled_, Decimal remaining_, double avg_fill_price_, int perm_id_, int parent_id_, double last_fill_price_, int client_id_, String why_held_, double mkt_cap_price_) { wrapper_main.order_status(order_id_, status_ib_); }

	@Override
	public void openOrderEnd() { wrapper_main.open_order_end(); }

	@Override
	public void execDetails(int id_, Contract contract_, Execution execution_) { wrapper_main.__exec_details(id_, contract_, execution_); }

	@Override
	public void commissionReport(CommissionReport report_) { wrapper_main.__commission_report(report_); }
	
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
	public void tickOptionComputation
	(
		int tickerId, int field, int tickAttrib, double impliedVol, double delta, 
		double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice
	) 
	{ }

	@Override
	public void tickString(int tickerId, int tickType, String value) { }

	@Override
	public void tickEFP
	(
		int tickerId, int tickType, double basisPoints, String formattedBasisPoints, 
		double impliedFuture, int holdDays, String futureLastTradeDate, 
		double dividendImpact, double dividendsToLastTradeDate
	) 
	{ }

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) { }

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) { }

	@Override
	public void updatePortfolio
	(
		Contract contract, Decimal position, double marketPrice, double marketValue, 
		double averageCost, double unrealizedPNL, double realizedPNL, String accountName
	) 
	{ }

	@Override
	public void updateAccountTime(String timeStamp) { }

	@Override
	public void accountDownloadEnd(String accountName) { }

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) { }

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) { }

	@Override
	public void contractDetailsEnd(int reqId) { }

	@Override
	public void execDetailsEnd(int reqId) { }

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) { }

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) { }

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
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) { }

	@Override
	public void currentTime(long time) { }

	@Override
	public void fundamentalData(int reqId, String data) { }

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) { }

	@Override
	public void marketDataType(int reqId, int marketDataType) { }

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) { }

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
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) { }

	@Override
	public void positionMultiEnd(int reqId) { }

	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) { }

	@Override
	public void accountUpdateMultiEnd(int reqId) { }

	@Override
	public void securityDefinitionOptionalParameter
	(
		int reqId, String exchange, int underlyingConId, String tradingClass, 
		String multiplier, Set<String> expirations, Set<Double> strikes
	) 
	{ }

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
	public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) { }

	@Override
	public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) { }

	@Override
	public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) { }

	@Override
	public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) { }

	@Override
	public void tickByTickAllLast
	(
		int reqId, int tickType, long time, double price, Decimal size, 
		TickAttribLast tickAttribLast, String exchange, String specialConditions
	) 
	{ }

	@Override
	public void tickByTickBidAsk
	(
		int reqId, long time, double bidPrice, double askPrice, 
		Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk
	) 
	{ }

	@Override
	public void tickByTickMidPoint(int reqId, long time, double midPoint) { }

	@Override
	public void orderBound(long orderId, int apiClientId, int apiOrderId) { }

	@Override
	public void completedOrder(Contract contract, Order order, OrderState orderState) { }

	@Override
	public void completedOrdersEnd() { }

	@Override
	public void replaceFAEnd(int reqId, String text) { }

	@Override
	public void wshMetaData(int reqId, String dataJson) { }

	@Override
	public void wshEventData(int reqId, String dataJson) { }

	@Override
	public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) { }

	@Override
	public void userInfo(int reqId, String whiteBrandingId) { }
}
