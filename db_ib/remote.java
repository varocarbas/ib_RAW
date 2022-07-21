package db_ib;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class remote 
{
	public static final String SOURCE = common.SOURCE_REMOTE;
	
	public static final String USER = common.FIELD_USER;
	public static final String SYMBOL = common.FIELD_SYMBOL;
	public static final String ORDER_ID_MAIN = common.FIELD_ORDER_ID_MAIN;
	public static final String ORDER_ID_SEC = common.FIELD_ORDER_ID_SEC;
	public static final String TIME = common.FIELD_TIME;
	public static final String STATUS = common.FIELD_STATUS;
	public static final String STATUS2 = common.FIELD_STATUS2;
	public static final String START = common.FIELD_START;
	public static final String START2 = common.FIELD_START2;
	public static final String STOP = common.FIELD_STOP;
	public static final String IS_MARKET = common.FIELD_IS_MARKET;
	public static final String QUANTITY = common.FIELD_QUANTITY;
	public static final String TYPE_PLACE = common.FIELD_TYPE_PLACE;
	public static final String INVEST_PERC = common.FIELD_INVEST_PERC;
	public static final String ERROR = common.FIELD_ERROR;

	public static ArrayList<HashMap<String, String>> get_active() { return common.get_all_vals(SOURCE, get_main_fields(), common.get_where_user(SOURCE)); }
	
	public static String get_status_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS); }

	public static String get_key_from_status(String status_) { return common.get_key_from_type(status_, ib.remote.STATUS); }
	
	public static String get_status2_from_key(String key_) { return common.get_type_from_key(key_, ib.remote.STATUS2); }

	public static String get_key_from_status2(String status2_) { return common.get_key_from_type(status2_, ib.remote.STATUS2); }

	private static String[] get_main_fields() { return new String[] { SYMBOL, ORDER_ID_MAIN, ORDER_ID_SEC, STATUS, STATUS2, START, START2, STOP, QUANTITY, TYPE_PLACE, INVEST_PERC }; }
}