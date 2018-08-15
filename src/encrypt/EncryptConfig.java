package encrypt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import interfaces.CustromParseProvider;

public class EncryptConfig {

	
	public List<File> operaDir=new ArrayList<>();
	public List<File> getOperaDir(){
		return operaDir;
		
	}
	
	/**
	 * 常量的所在包名
	 */
	public static String sConstantsAtPackage;
	/**
	 * 加密Utils所在包名
	 */
	public static String encryptAtPackage = null;
	/**
	 * 加密的工具class简称
	 */
	public static String sDecodSimpleClass = "EncryptUtil";
	/**
	 * 加密工具里面的解密方法
	 */
	// public static String fetchDecodeMethod = sDecodSimpleClass + ".decode";
	public static String sDecodeMethod = "decode";
	public String sConstantsClass = "Constants";
	public String[] 	sIgnoresFileNames = new String[] {};
	public UseVarQuote 	useVarQuote = UseVarQuote.no;
	
	/**
	 * 新版本打算分成多个文件作为常量。并保证每个常量最多只有50个。
	 */
	public static String sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\ConstantValue.java";

	// sIgnoreKeyWords=new String[]{"TAG"};
	// sIgnoresFolder = new String[] { "MainHandlerPackage" };
	// sDecodSimpleClass="EncryptUtil";
	// sIgnoresFileNames = new String[] { "MainHandlerPackage.java" };
	// sIgnoresFileNames=new
	// String[]{"MainHandlerPackage.java","MainHandlerPackage"};
	// fetchDecodeMethod = sDecodSimpleClass + ".decode";

	public EncryptType currentEncryptType = EncryptType.OTHERENCRYPT;
	

	public static CustromParseProvider custromParseProvider = new CustromParseProvider() {

		@Override
		public String requestEncode(String str) {
			// TODO Auto-generated method stub
			throw new RuntimeErrorException(null, "没有实现");
		}

		@Override
		public String requestDecode(String str) {
			throw new RuntimeErrorException(null, "没有实现");
			// TODO Auto-generated method stub
		}
	};
	public void addDirOrFile(String temp) {
		getOperaDir().add(new File(temp));
		
	}	

	/**
	 * 忽略包含static final的常量
	 */
	public static boolean encryptStaticConstants = true;
	
	
}
