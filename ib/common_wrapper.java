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

	public static void __tick_price(int id_, int field_ib_, double price_) { common_wrapper_quicker.__tick_price(id_, field_ib_, price_); }
	
	public static void __tick_size(int id_, int field_ib_, int size_) { common_wrapper_quicker.__tick_size(id_, field_ib_, size_); }
	
	public static void __tick_generic(int id_, int field_ib_, double value_) { common_wrapper_quicker.__tick_generic(id_, field_ib_, value_); }
	
	public static void __tick_snapshot_end(int id_) { common_wrapper_quicker.__tick_snapshot_end(id_); }	
	
	public static void error(int id_, int code_, String message_) 
	{
		boolean is_warning = external_ib.errors.is_warning(code_);
		
		if (!is_warning) sync.update_error_triggered(true);
		
		errors.wrapper_error(id_, code_, message_, is_warning); 
	}
	
	public static void order_status(int order_id_, String status_ib_) { orders.order_status(order_id_, status_ib_); }

	public static void open_order_end() { sync_orders.open_order_end(); }
	
	public static void __exec_details(int id_, Contract contract_, Execution execution_) { async_execs.__exec_details(id_, contract_, execution_); }

	public static void __commission_report(CommissionReport report_) { async_execs.__commission_report(report_); }
}