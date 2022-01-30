package accessory_ib;

public class types
{
	//--- To be synced with get_all_subtypes().
	
	public static final String IB_CONN = "ib_conn";
	public static final String IB_CONN_PAPER = "ib_conn_paper";
	public static final String IB_CONN_REAL = "ib_conn_real";
	public static final String IB_CONN_GATEWAY = "ib_conn_gateway";
	public static final String IB_CONN_GATEWAY_PAPER = "ib_conn_gateway_paper";	
	
	public static final String ERROR_CONN = "error_conn";
	public static final String ERROR_CONN_NONE = "error_conn_none";
	public static final String ERROR_CONN_ID = "error_conn_id";
	public static final String ERROR_CONN_TYPE = "error_conn_type";
	
	//---------------------------
	
	public static String check_subtype(String subtype_, String add_remove_)
	{
		return accessory.types.check_subtype
		(
			subtype_, get_all_subtypes(), add_remove_, IB_CONN
		);
	}
	
	public static String[] get_all_subtypes()
	{
		return new String[]
		{
			//IB_CONN
			IB_CONN_PAPER, IB_CONN_REAL, IB_CONN_GATEWAY, IB_CONN_GATEWAY_PAPER,
			
			//ERROR_CONN
			ERROR_CONN_NONE, ERROR_CONN_ID, ERROR_CONN_TYPE
		};		
	}
}