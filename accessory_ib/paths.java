package accessory_ib;

import accessory.os;
import accessory.strings;

public abstract class paths 
{
	public static final String CONFIG_DIR_TWS = _types.CONFIG_BASIC_DIR_IB_TWS;
	public static final String CONFIG_DIR_GATEWAY = _types.CONFIG_BASIC_DIR_IB_GATEWAY;
	
	public static final String APP_TWS_LINUX = "tws";
	public static final String APP_GATEWAY_LINUX = "ibgateway";
	
	public static final String DEFAULT_DIR_TWS = "ib";
	public static final String DEFAULT_DIR_GATEWAY = "ib_gateway";

	public static String get_path_ib(boolean is_tws_) 
	{ 
		String dir = get_dir(is_tws_);
		String app = null;
		
		if (os.is_linux()) app = (is_tws_ ? APP_TWS_LINUX : APP_GATEWAY_LINUX);
		
		String[] inputs = new String[] { dir, app };
		
		return (strings.are_ok(inputs) ? accessory.paths.build(inputs, true) : strings.DEFAULT); 
	}
	
	public static String get_dir(boolean is_tws_) { return accessory.paths.get_dir((is_tws_ ? CONFIG_DIR_TWS : CONFIG_DIR_GATEWAY)); }

	public static void update_dir(String val_, boolean is_tws_) { accessory.paths.update_dir((is_tws_ ? CONFIG_DIR_TWS : CONFIG_DIR_GATEWAY), val_); }

	public static String get_default_dir(boolean is_tws_) { return accessory.paths.build(new String[] { accessory.paths.get_dir_home(), (is_tws_ ? DEFAULT_DIR_TWS : DEFAULT_DIR_GATEWAY) }, false); }
}