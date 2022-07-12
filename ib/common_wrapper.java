package ib;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory_ib.errors;

public abstract class common_wrapper
{
	public static final String KEY_FUNDS = "AvailableFunds";
	
	public static void accountSummary(int id_, String account_, String tag_, String value_, String currency_) { sync_basic.account_summary(id_, account_, tag_, value_, currency_); }
	
	public static void accountSummaryEnd(int id_) { sync_basic.account_summary_end(id_); }
	
	public static void nextValidId(int id_) 
	{
		if (!sync.next_valid_id(id_)) conn._started = true;
	}

	public static void __tickPrice(int id_, int field_ib_, double price_)
	{
		async_data_market.__tick_price(id_, field_ib_, price_);
		async_data_trades.__tick_price(id_, field_ib_, price_);
		async_data_watchlist.__tick_price(id_, field_ib_, price_);
	}
	
	public static void __tickSize(int id_, int field_ib_, int size_)
	{
		async_data_market.__tick_size(id_, field_ib_, size_);
		async_data_trades.__tick_size(id_, field_ib_, size_);
		async_data_watchlist.__tick_size(id_, field_ib_, size_);
	}
	
	public static void __tickGeneric(int id_, int tick_, double value_)
	{
		async_data_market.__tick_generic(id_, tick_, value_);
		async_data_trades.__tick_generic(id_, tick_, value_);
		async_data_watchlist.__tick_generic(id_, tick_, value_);
	}
	
	public static void __tickSnapshotEnd(int id_)
	{
		//In some cases, reaching this point might take too long and relying on tickSize could
		//appreciably speed everything up. That is, all the relevant information is assumed to 
		//have already been received right after getting certain size value.

		market.__stop_snapshot(id_);
		async_data_trades.__stop_snapshot(id_);
		async_data_watchlist.__stop_snapshot(id_);
	}	
	
	public static void __error(int id_, int code_, String message_) 
	{
		sync.update_error_triggered(true);
		
		errors.__wrapper_error(id_, code_, message_); 
	}
	
	public static void __orderStatus(int order_id_, String status_ib_) 
	{ 
		sync_orders.order_status(order_id_, status_ib_); 
		
		async_orders.order_status(order_id_, status_ib_);
		
		async_trades.__order_status(order_id_, status_ib_);
	}

	public static void openOrderEnd() { sync_orders.open_order_end(); }
	
	public static void __execDetails(int id_, Contract contract_, Execution execution_) { async_execs.__exec_details(id_, contract_, execution_); }

	public static void __commissionReport(CommissionReport report_) { async_execs.__commission_report(report_); }
}