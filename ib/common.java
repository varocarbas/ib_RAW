package ib;

import java.util.HashMap;

import com.ib.client.Contract;

import accessory.strings;
import accessory.dates;
import accessory.numbers;
import accessory.parent_static;
import accessory_ib.config;
import accessory_ib.types;

public class common extends parent_static 
{
	public static final String SECURITY_TYPE = types.CONFIG_CONTRACT_SECURITY_TYPE;
	public static final String CURRENCY = types.CONFIG_CONTRACT_CURRENCY;
	public static final String EXCHANGE = types.CONFIG_CONTRACT_EXCHANGE;
	public static final String PRIMARY_EXCHANGE = types.CONFIG_CONTRACT_PRIMARY_EXCHANGE;

	public static final String ERROR_CONTRACT_INFO = types.ERROR_IB_CONTRACT_INFO;
	
	static final int MIN_REQ_ID_SYNC = 1;
	static final int MAX_REQ_ID_SYNC = 495; //There has to be a gap to account for scenarios of acceptable invalid sync IDs like the ones which sync_orders.get_id_sec() might return.
	static final int MIN_REQ_ID_ASYNC = 500;
	static final int MAX_REQ_ID_ASYNC = 3000;
	
	public static String get_class_id() { return accessory.types.get_id(types.ID_COMMON); }
	
	public static Contract get_contract(String symbol_)
	{
		String security = (String)config.get_contract(SECURITY_TYPE);
		String currency = (String)config.get_contract(CURRENCY);
		String exchange = (String)config.get_contract(EXCHANGE);
		String primary_exchange = (String)config.get_contract(PRIMARY_EXCHANGE);
		
		if (!strings.is_ok(symbol_) || !external_ib.contracts.security_is_ok(security) || !strings.are_ok(new String[] { currency, exchange, primary_exchange })) 
		{
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put("symbol", symbol_);
			info.put("security", security);
			info.put("currency", currency);
			info.put("exchange", exchange);
			info.put("primary_exchange", primary_exchange);
			
			accessory_ib.errors.manage(ERROR_CONTRACT_INFO, info);
			
			return null;
		}
		
		Contract contract = new Contract();

		contract.symbol(symbol_);
		contract.secType(security);
		contract.currency(currency);
		contract.exchange(exchange);	
		contract.primaryExch(primary_exchange);

		return contract;
	}

	public static String get_market_time() { return dates.get_now_string(dates.FORMAT_TIME_SHORT); }

	public static String normalise_symbol(String symbol_)
	{
		String symbol = symbol_;
		if (!strings.is_ok(symbol)) return strings.DEFAULT;

		symbol = symbol.trim().toUpperCase();

		return symbol;
	}
	
	public static boolean req_id_is_ok_sync(int id_) { return id_is_ok(id_, MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC); }
	
	public static boolean req_id_is_ok_async(int id_) { return id_is_ok(id_, MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC); }
		
	static int get_req_id(boolean is_sync_) 
	{ 
		int id = 0;
		
		lock();
		
		if (is_sync_)
		{
			id = get_req_id_internal(sync._id, MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC);
			sync._id = id;
		}
		else
		{
			id = get_req_id_internal(async._last_id, MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC);
			async._last_id = id;		
		}
		
		unlock();
		
		return id;
	}

	private static int get_req_id_internal(int last_, int min_, int max_) 
	{ 
		int id = last_ + 1;

		return (id_is_ok(id, min_, max_) ? id : min_);
	}
	
	private static boolean id_is_ok(int id_, int min_, int max_) { return numbers.is_ok(id_, min_, max_); }
}