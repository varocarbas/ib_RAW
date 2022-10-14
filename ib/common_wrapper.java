package ib;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

import accessory_ib.errors;
import external_ib.calls;

public abstract class common_wrapper
{
	public static void account_summary(int id_, String account_, String tag_, String value_, String currency_) 
	{ 
		if (!basic.update_funds_is_ok(account_, tag_, value_, currency_)) return;
		
		if (sync_basic.is_ok(id_)) sync_basic.update_funds(tag_, value_); 
		else if (async_basic.is_ok(id_)) async_basic.update_funds(tag_, value_);
	}

	public static void account_summary_end(int id_) 
	{
		if (sync_basic.is_ok(id_)) sync_basic.account_summary_end(id_); 
		else if (async_basic.is_ok(id_)) async_basic.account_summary_end(id_);
		
		calls.cancelAccountSummary(id_);
	}
	
	public static void next_valid_id(int id_) 
	{
		if (!sync.next_valid_id(id_)) conn._started = true;
	}

	public static void _tick_price(int id_, int field_ib_, double price_) 
	{ 
		if (async_data_quicker.enabled()) async_data_quicker.__tick_price(id_, field_ib_, price_); 
		else 
		{
			if (async_data_market.is_ok()) async_data_market.tick_price(id_, field_ib_, price_);

			if (async_data_trades.is_ok()) async_data_trades.tick_price(id_, field_ib_, price_);
			
			if (async_data_watchlist.is_ok()) async_data_watchlist.tick_price(id_, field_ib_, price_);
		}
	}
	
	public static void _tick_size(int id_, int field_ib_, int size_) 
	{ 
		if (async_data_quicker.enabled()) async_data_quicker.__tick_size(id_, field_ib_, size_); 
		else 
		{
			if (async_data_market.is_ok()) async_data_market.tick_size(id_, field_ib_, size_);

			if (async_data_trades.is_ok()) async_data_trades.tick_size(id_, field_ib_, size_);

			if (async_data_watchlist.is_ok()) async_data_watchlist.tick_size(id_, field_ib_, size_);
		}
	}
	
	public static void _tick_generic(int id_, int field_ib_, double value_) 
	{
		if (async_data_quicker.enabled()) async_data_quicker.__tick_generic(id_, field_ib_, value_); 
		else 
		{
			if (async_data_market.is_ok()) async_data_market.tick_generic(id_, field_ib_, value_);

			if (async_data_trades.is_ok()) async_data_trades.tick_generic(id_, field_ib_, value_);

			if (async_data_watchlist.is_ok()) async_data_watchlist.tick_generic(id_, field_ib_, value_);
		} 
	}
	
	public static void _tick_snapshot_end(int id_)
	{
		//In some cases, reaching this point might take too long and relying on tickSize could
		//appreciably speed everything up. That is, all the relevant information is assumed to 
		//have already been received right after getting certain size value.
		
		if (async_data_quicker.enabled()) async_data_quicker.__tick_snapshot_end(id_); 
		else 
		{
			if (async_data_market.is_ok()) async_data_market.tick_snapshot_end(id_);
			
			if (async_data_trades.is_ok()) async_data_trades.tick_snapshot_end(id_);
			
			if (async_data_watchlist.is_ok()) async_data_watchlist.tick_snapshot_end(id_);
		} 		
	}	
	
	public static void error(int id_, int code_, String message_) 
	{
		if (!external_ib.errors.is_warning(code_)) sync.update_error_triggered(true);
		
		errors.wrapper_error(id_, code_, message_); 
	}
	
	public static void order_status(int order_id_, String status_ib_) { orders.order_status(order_id_, status_ib_); }

	public static void open_order_end() { sync_orders.open_order_end(); }
	
	public static void __exec_details(int id_, Contract contract_, Execution execution_) { async_execs.__exec_details(id_, contract_, execution_); }

	public static void __commission_report(CommissionReport report_) { async_execs.__commission_report(report_); }
}