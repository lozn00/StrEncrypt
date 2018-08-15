package encrypt;


public class GlobalConfig {

	public static final String sIGNORE_DECODE = "IGNORE_DECODE";
	
	
	public static DebugLevel debug = DebugLevel.MIDDLE;
	
	/**
	 * 是否支持直接解析常量行,
	 */
	public static boolean sEnableParseConstantsLine = false;
	/**
	 * 处理一个目录所有文件名字
	 * 
	 * @param path
	 */
	private static boolean writeFile = true;
	
	/**
	 * 默认加密模式是把字符串替换为 加密方法 加密方法传递的是加密的int数组，但是这种方法，有时候效率比较低,或者容易被hook控制模仿,
	 */
	public static boolean isIntArrEncryptMode = true;
}
