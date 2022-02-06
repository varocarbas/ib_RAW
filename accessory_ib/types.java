package accessory_ib;

import accessory.arrays;
import accessory.strings;

public class types
{
	//--- To be synced with get_all_subtypes().
	
	public static final String CONN = "conn";
	public static final String CONN_PAPER = "conn_paper";
	public static final String CONN_REAL = "conn_real";
	public static final String CONN_GATEWAY = "conn_gateway";
	public static final String CONN_GATEWAY_PAPER = "conn_gateway_paper";	
	
	public static final String SYNC = "sync";
	public static final String SYNC_DECIMAL = "sync_decimal";
	public static final String SYNC_DECIMAL_FUNDS = "sync_decimal_funds";
	public static final String SYNC_INTS = "sync_ints";
	public static final String SYNC_INTS_IDS = "sync_ints_ids";
	public static final String SYNC_MISC = "sync_misc";
	
	//--- Types to be synced with get_all_types_error().
	public static final String ERROR_CONN = "error_conn";
	public static final String ERROR_CONN_NONE = "error_conn_none";
	public static final String ERROR_CONN_ID = "error_conn_id";
	public static final String ERROR_CONN_TYPE = "error_conn_type";
	//------
	//---------------------------
		
	public static String check_conn(String subtype_, String add_remove_)
	{
		return check(subtype_, CONN, add_remove_);
	}
	
	public static String check_sync(String subtype_, String type_, boolean get_type_)
	{
		String output = accessory.types.check_subtype(subtype_, get_subtypes_sync(type_), null, null);
		if (!strings.is_ok(output)) return strings.DEFAULT;
		
		if (get_type_) output = strings.substring_after(subtype_, accessory.types.SEPARATOR, 2, false);
		
		return output;
	}

	public static String[] get_subtypes_sync(String type_)
	{		
		return get_subtypes((strings.is_ok(type_) ? SYNC : type_));
	}
	
	public static String check_error(String subtype_, String[] types_)
	{
		return check(subtype_, null, null, get_subtypes_errors(types_));
	}
	
	public static String[] get_subtypes_errors(String[] types_)
	{
		return accessory.types.get_subtypes
		(
			(arrays.is_ok(types_) ? types_ : get_all_types_error()), get_all_subtypes()
		);
	}
	
	private static String[] get_all_types_error()
	{
		return new String[]
		{
			ERROR_CONN
		};
	}
	
	private static String[] get_subtypes(String type_)
	{
		return accessory.types.get_subtypes(type_, get_all_subtypes());
	}
	
	private static String check(String subtype_, String type_, String add_remove_, String[] subtypes_)
	{
		return accessory.types.check_subtype(subtype_, subtypes_, add_remove_, type_);	
	}
	
	private static String check(String subtype_, String type_, String add_remove_)
	{
		return accessory.types.check_subtype(subtype_, get_subtypes(type_), add_remove_, type_);	
	}
	
	public static String[] get_all_subtypes()
	{
		return new String[]
		{
			//CONN
			CONN_PAPER, CONN_REAL, CONN_GATEWAY, CONN_GATEWAY_PAPER,
			
			//SYNC
			//SYNC_DECIMAL
			SYNC_DECIMAL_FUNDS,
			//SYNC_INTS
			SYNC_INTS_IDS, 
			//SYNC_MISC
			
			//ERROR_CONN
			ERROR_CONN_NONE, ERROR_CONN_ID, ERROR_CONN_TYPE
		};		
	}
}