package ib;

import java.util.HashMap;
import java.util.Map.Entry;

import accessory.arrays;
import accessory.numbers;
import accessory.parent_static;
import accessory.strings;
import accessory_ib.config;
import accessory_ib.db;
import accessory_ib.types;
import external_ib.market;

public class async_market  extends parent_static 
{
	public static final String TYPE_SNAPSHOT = types.ASYNC_MARKET_SNAPSHOT;
	public static final String TYPE_STREAM = types.ASYNC_MARKET_STREAM;
	
	public static final int PRICE = market.TICK_LAST;
	public static final int OPEN = market.TICK_OPEN;
	public static final int CLOSE = market.TICK_CLOSE;
	public static final int LOW = market.TICK_LOW;
	public static final int HIGH = market.TICK_HIGH;
	public static final int ASK = market.TICK_ASK;
	public static final int BID = market.TICK_BID;
	public static final int HALTED = market.TICK_HALTED;
	public static final int VOLUME = market.TICK_VOLUME;
	public static final int SIZE = market.TICK_LAST_SIZE;
	public static final int ASK_SIZE = market.TICK_ASK_SIZE;
	public static final int BID_SIZE = market.TICK_BID_SIZE;
		
	private static int _id = async.MIN_ID - 1;
	private static HashMap<Integer, String> _symbols = new HashMap<Integer, String>();

	public static String get_id() { return accessory.types.get_id(types.ID_ASYNC_MARKET); }

	public static void wrapper_tickPrice(int id_, int field_, double price_)
	{
		String symbol = (String)arrays.get_value(_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;
		
		if (field_ == BID) key = db.FIELD_BID;
		else if (field_ == ASK) key = db.FIELD_ASK;
		else if (field_ == PRICE) key = db.FIELD_PRICE;
		else if (field_ == HIGH) key = db.FIELD_HIGH;
		else if (field_ == LOW) key = db.FIELD_LOW;
		else if (field_ == CLOSE) key = db.FIELD_CLOSE;		
		else if (field_ == OPEN) key = db.FIELD_OPEN;	
		if (!strings.is_ok(key)) return;

		update_db(symbol, key, price_);
	}

	public static void wrapper_tickSize(int id_, int field_, int size_)
	{
		String symbol = (String)arrays.get_value(_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;
		
		if (field_ == BID_SIZE) key = db.FIELD_BID_SIZE;
		else if (field_ == ASK_SIZE) key = db.FIELD_ASK_SIZE;
		else if (field_ == SIZE) key = db.FIELD_SIZE;
		else if (field_ == VOLUME) 
		{
			key = db.FIELD_VOLUME;
			if (is_snapshot(id_) && snapshot_is_quick()) snapshot_end(id_);
		}
		if (!strings.is_ok(key)) return;

		update_db(symbol, key, size_);
	}
	
	public static void wrapper_tickGeneric(int id_, int tick_, double value_)
	{
		String symbol = (String)arrays.get_value(_symbols, id_);
		if (!strings.is_ok(symbol)) return;

		String key = strings.DEFAULT;

		if (tick_ == HALTED) key = db.FIELD_HALTED;
		if (!strings.is_ok(key)) return;

		update_db(symbol, key, value_);
	}

	public static boolean is_snapshot(int id_) { return strings.are_equal((String)arrays.get_value(async._types, id_), TYPE_SNAPSHOT); }

	public static boolean snapshot_is_quick() { return strings.to_boolean(config.get_async(types.CONFIG_ASYNC_MARKET_SNAPSHOT_QUICK)); }

	public static boolean snapshot_is_nonstop() { return strings.to_boolean(config.get_async(types.CONFIG_ASYNC_MARKET_SNAPSHOT_NONSTOP)); }

	public static boolean snapshot_start(String symbol_, int data_type_) { return start(symbol_, data_type_, true); }

	public static boolean stream_start(String symbol_, int data_type_) { return start(symbol_, data_type_, false); }

	public static void snapshot_end(int id_) { end_common(id_); }
	
	public static void stream_end(String symbol_) { stream_end(get_id(symbol_)); }

	public static void stream_end(int id_) 
	{
		end_common(id_);
		
		conn._client.cancelMktData(id_); 
	}
	
	public static String get_symbol(int id_) { return (String)arrays.get_value(_symbols, id_); }
	
	public static int get_id(String symbol_)
	{
		int id = async.MIN_ID - 1;
		if (!strings.is_ok(symbol_)) return id;

		for (Entry<Integer, String> item: _symbols.entrySet())
		{
			if (strings.are_equivalent(symbol_, item.getValue())) return item.getKey();
		}

		return id;
	}

	private static boolean start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return false;

		if (!numbers.is_ok(_id, async.MIN_ID, async.MAX_ID)) _id = async.MIN_ID - 1;
		_id++;

		_symbols.put(_id, symbol);
		async._retrieving.add(_id);
		async._types.put(_id, (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM));

		if (!db.symbol_exists(symbol)) db.insert(symbol);
		if (market.data_is_ok(data_type_)) conn._client.reqMarketDataType(data_type_);

		conn._client.reqMktData(_id, common.get_contract(symbol), "", is_snapshot_, false, null);

		return true;		
	}

	private static void end_common(int id_)
	{
		arrays.remove_value(async._retrieving, id_); 
		arrays.remove_key(async._types, id_);		
	}
	
	private static boolean update_db(String symbol_, String key_, double val_)
	{
		boolean is_ok = false;
		if (!strings.is_ok(symbol_) || !strings.is_ok(key_) || val_ <= 0.0) return is_ok;

		is_ok = db.update_number(symbol_, key_, val_);

		return is_ok;
	}
}