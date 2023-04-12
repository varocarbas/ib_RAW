package accessory_ib;

import accessory.os;
import accessory.strings;

public abstract class paths 
{
	public static final String CONFIG_DIR_TWS = _types.CONFIG_BASIC_IB_DIR_TWS;
	public static final String CONFIG_DIR_GATEWAY = _types.CONFIG_BASIC_IB_DIR_GATEWAY;
	
	public static final String CONFIG_PATH_MARKET_HOLIDAYS = _types.CONFIG_BASIC_IB_PATH_MARKET_HOLIDAYS;
	public static final String CONFIG_PATH_MARKET_EARLY_CLOSES = _types.CONFIG_BASIC_IB_PATH_MARKET_EARLY_CLOSES;
	
	public static final String FILE_TWS_LINUX = "tws";
	public static final String FILE_GATEWAY_LINUX = "ibgateway";
	
	public static final String DEFAULT_DIR_TWS = "ib";
	public static final String DEFAULT_DIR_GATEWAY = "ib_gateway";
	public static final String DEFAULT_FILE_MARKET_HOLIDAYS = "market_holidays" + accessory.paths.EXTENSION_TEXT;
	public static final String DEFAULT_FILE_MARKET_EARLY_CLOSES = "market_early_closes" + accessory.paths.EXTENSION_TEXT;
	
	public static String get_path_app_ib(boolean is_tws_) 
	{ 
		String[] inputs = new String[] { get_dir_app_ib(is_tws_), get_file_app_ib(is_tws_) };

		return (strings.are_ok(inputs) ? accessory.paths.build(inputs, true) : strings.DEFAULT); 
	}
	
	public static String get_file_app_ib(boolean is_tws_) 
	{ 
		String output = null;
		
		if (os.is_linux()) output = (is_tws_ ? FILE_TWS_LINUX : FILE_GATEWAY_LINUX);
	
		return output;
	}
	
	public static String get_dir_app_ib(boolean is_tws_) { return (String)config.get_basic((is_tws_ ? CONFIG_DIR_TWS : CONFIG_DIR_GATEWAY)); }

	public static boolean update_dir_app_ib(String val_, boolean is_tws_) { return (accessory.paths.dir_exists(val_) ? config.update_basic((is_tws_ ? CONFIG_DIR_TWS : CONFIG_DIR_GATEWAY), val_) : false); }

	public static String get_default_dir_app_ib(boolean is_tws_) { return accessory.paths.build(new String[] { accessory.paths.get_dir_home(), (is_tws_ ? DEFAULT_DIR_TWS : DEFAULT_DIR_GATEWAY) }, false); }

	public static String get_path_market_holidays() { return (String)accessory.config.get_basic(CONFIG_PATH_MARKET_HOLIDAYS); }

	public static boolean update_path_market_holidays(String path_) { return (accessory.paths.exists(path_) ? accessory.config.update_basic(CONFIG_PATH_MARKET_HOLIDAYS, path_) : false); }

	public static String get_path_market_early_closes() { return (String)accessory.config.get_basic(CONFIG_PATH_MARKET_EARLY_CLOSES); }

	public static boolean update_path_market_early_closes(String path_) { return (accessory.paths.exists(path_) ? accessory.config.update_basic(CONFIG_PATH_MARKET_EARLY_CLOSES, path_) : false); }

	public static String get_default_path_market_holidays() { return accessory.paths.build(new String[] { accessory.paths.get_dir(accessory.paths.DIR_INFO), DEFAULT_FILE_MARKET_HOLIDAYS }, true); }

	public static String get_default_path_market_early_closes() { return accessory.paths.build(new String[] { accessory.paths.get_dir(accessory.paths.DIR_INFO), DEFAULT_FILE_MARKET_EARLY_CLOSES }, true); }
}