package ib;

import java.util.HashMap;

import accessory.arrays;
import accessory.parent_static;
import accessory.strings;
import accessory_ib._alls;
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
		
	private static int _id = common.WRONG_ID;
	private static HashMap<Integer, String> _symbols = new HashMap<Integer, String>();

	public static String get_class_id() { return accessory.types.get_id(types.ID_ASYNC_MARKET); }

	public static void wrapper_tickPrice(int id_, int field_, double price_)
	{
		String field = (String)arrays.get_value(get_all_prices(), field_);
		if (!strings.is_ok(field)) return;

		update_db(id_, field, price_);
	}

	public static void wrapper_tickSize(int id_, int field_, int size_)
	{
		String field = (String)arrays.get_value(get_all_sizes(), field_);
		if (!strings.is_ok(field)) return;
		
		if (field_ == VOLUME)
		{
			if (is_snapshot(id_) && snapshot_is_quick()) snapshot_end(id_);
		}

		update_db(id_, field, size_);
	}
	
	public static void wrapper_tickGeneric(int id_, int tick_, double value_)
	{
		String field = (String)arrays.get_value(get_all_generics(), tick_);
		if (!strings.is_ok(field)) return;

		update_db(id_, field, value_);
	}

	public static boolean is_snapshot(int id_) { return async.type_is_ok(id_, TYPE_SNAPSHOT); }

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
	
	public static int get_id(String symbol_) { return (int)arrays.get_key(_symbols, symbol_); }

	public static HashMap<Integer, String> populate_all_prices()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
			
		all.put(PRICE, db.FIELD_PRICE);
		all.put(OPEN, db.FIELD_OPEN);
		all.put(CLOSE, db.FIELD_CLOSE);
		all.put(LOW, db.FIELD_LOW);
		all.put(HIGH, db.FIELD_HIGH);
		all.put(ASK, db.FIELD_ASK);
		all.put(BID, db.FIELD_BID);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_sizes()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();
		
		all.put(VOLUME, db.FIELD_VOLUME);
		all.put(SIZE, db.FIELD_SIZE);
		all.put(ASK_SIZE, db.FIELD_ASK_SIZE);
		all.put(BID_SIZE, db.FIELD_BID_SIZE);
		
		return all;
	}

	public static HashMap<Integer, String> populate_all_generics()
	{		
		HashMap<Integer, String> all = new HashMap<Integer, String>();			

		all.put(HALTED, db.FIELD_HALTED);
		
		return all;
	}
	
	private static HashMap<Integer, String> get_all_prices() { return _alls.ASYNC_MARKET_PRICES; }
	
	private static HashMap<Integer, String> get_all_sizes() { return _alls.ASYNC_MARKET_SIZES; }
	
	private static HashMap<Integer, String> get_all_generics() { return _alls.ASYNC_MARKET_GENERICS; }
	
	private static boolean start(String symbol_, int data_type_, boolean is_snapshot_)
	{
		String symbol = common.normalise_symbol(symbol_);
		if (!strings.is_ok(symbol)) return false;

		_id = common.get_next_id(_id);

		_symbols.put(_id, symbol);
		async.add(_id, (is_snapshot_ ? TYPE_SNAPSHOT : TYPE_STREAM));

		if (!db.symbol_exists(symbol)) db.insert(symbol);
		if (market.data_is_ok(data_type_)) conn._client.reqMarketDataType(data_type_);

		conn._client.reqMktData(_id, common.get_contract(symbol), "", is_snapshot_, false, null);

		return true;		
	}

	private static void end_common(int id_) { async.remove(id_); }
	
	private static boolean update_db(int id_, String field_, double val_)
	{
		String symbol = (String)arrays.get_value(_symbols, id_);
		if (!strings.is_ok(symbol) || !strings.is_ok(field_) || val_ <= 0.0) return false;

		return db.update_number(symbol, field_, val_);
	}
}