package ib;

public abstract class common_xsync 
{
	public static final int MIN_REQ_ID_SYNC = 0;
	public static final int MAX_REQ_ID_SYNC = MIN_REQ_ID_SYNC + 10; 
	public static final int MIN_REQ_ID_ASYNC = MAX_REQ_ID_SYNC + 1;
	public static final int MAX_REQ_ID_ASYNC = MIN_REQ_ID_ASYNC + 10000;
	
	public static boolean req_id_is_ok(int id_) { return common.id_is_ok(id_, MIN_REQ_ID_SYNC, MAX_REQ_ID_ASYNC); }
	
	public static boolean req_id_is_ok_sync(int id_) { return common.id_is_ok(id_, MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC); }
	
	public static boolean req_id_is_ok_async(int id_) { return common.id_is_ok(id_, MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC); }

	static int get_req_id(boolean is_sync_) 
	{ 
		int id = 0;
				
		if (is_sync_)
		{
			id = get_req_id_internal(sync.get_req_id(), MIN_REQ_ID_SYNC, MAX_REQ_ID_SYNC);
			sync.update_req_id(id);
		}
		else
		{
			id = get_req_id_internal(async.get_last_id(), MIN_REQ_ID_ASYNC, MAX_REQ_ID_ASYNC);
			async.update_last_id(id);		
		}
		
		return id;
	}

	private static int get_req_id_internal(int last_, int min_, int max_) 
	{ 
		int id = last_ + 1;

		return (common.id_is_ok(id, min_, max_) ? id : min_);
	}
}