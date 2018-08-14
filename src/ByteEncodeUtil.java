
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import newencrypt.StrEngC0202ooo;

//TODO 下次增加自动清空常量class已经申明的变量功能。
/**
 * 对于代码过长 无解，有时候和长度已经没有关系了，需要删除几行中文才能搞定。或者把这些中文另外分到其他的变量文件中去。
 * 
 * @author admin
 *
 */
public class ByteEncodeUtil {
	/**
	 * 更换加密方式的时候应该把原来储存的常量删除，否则导致还是原来的加密 这里也许不应该用hash，这里只需要判断是否已经加密了而已
	 */
	static HashMap<String, String> sEncodeMap = new HashMap<>();
	public static String sConstantsClass = "ConstantValue";
	/**
	 * 是否支持直接解析常量行,
	 */
	public static boolean sEnableParseConstantsLine = false;
	public static String sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\ConstantValue.java";

	static HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper();
	/**
	 * 处理一个目录所有文件名字
	 * 
	 * @param path
	 */
	private static boolean writeFile = true;
	/**
	 * 多个文件共享变量 是已经生产的 变量名和机解密的字符串
	 */
	static HashMap<String, String> sDecodeMap = new HashMap<>();
	static ArrayList<String> sIgnoreFileList = new ArrayList<>();
	public static EncryptConfig encryptConfig = new EncryptConfig();
	private static String[] sIgnoresFolder = new String[] {};
	private static String[] sIgnoresFileNames = new String[] {};

	public static boolean isIgnoreFolder(File file) {
		if (sIgnoresFolder != null) {
			for (int i = 0; i < sIgnoresFolder.length; i++) {
				String current = sIgnoresFolder[i];
				if (file.isDirectory() && file.getName().equals(current)) {
					System.out.println("忽略文件" + file.getAbsolutePath() + ",包含" + current);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isIgnoreFiles(File file) {
		if (sIgnoresFileNames != null) {
			for (int i = 0; i < sIgnoresFileNames.length; i++) {
				String current = sIgnoresFileNames[i];
				if (file.getName().equals(current)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final String sIGNORE_DECODE = "IGNORE_DECODE";
	/*
	 * "\"(.*?)\"" int[] QSSQ__with_uin_eq_ = new int[]{38, 117, 105, 110,
	 * 61};//content=&uin= public final static int[] QSSQ__with_uin_eq_ = new
	 * int[]{38, 117, 105, 110, 61};//content=&uin=
	 * \s*int\[\s*\]\s*(\w*)\s*\=\s*new\s*int\[\s*\]\s*\{(.*?)\}
	 * ="\\s*int\\[\\s*\\]\\s*(\\w*)\\s*\\=\\s*new\\s*int\\[\\s*\\]\\s*\\{(.*?)\\}";
	 */
	// java里面必须前面后面加上.*?
	public static final String encodeLineReg = ".*?\\s*int\\[\\s*\\]\\s*(.+)\\s*\\=\\s*new\\s*int\\[\\s*\\]\\s*\\{(.*?)\\}.*?";
	// public static final String encodeLineReg =
	// ".*?\\s*int\\[\\s*\\]\\s*(\\w*)\\s*\\=\\s*new\\s*int\\[\\s*\\]\\s*\\{(.*?)\\}.*?";
	// public static final String encodeLineReg
	// =".*?int\\[\\s*\\](\\s*).*?\\=.*?\\{(.*?)\\}.*?";
	public static boolean printSmallCode = false;
	public static boolean printChar = true;
	/**
	 * 默认加密模式是把字符串替换为 加密方法 加密方法传递的是加密的int数组，但是这种方法，有时候效率比较低,或者容易被hook控制模仿,
	 */
	public static boolean isIntArrEncryptMode = true;

	/**
	 * 针对加密 把所有加密的提取到一个变量数组。 或者直接 new一个匿名的。 true则使用变量引用。
	 */
	enum UseVarQuote {
		yes, no, random, str
	}

	public enum DebugLevel {
		All(5), MIDDLE(3), WRAIN(2), ERR(1), NONE(0);

		private int value;

		private int getValue() {
			return value;
		}

		private DebugLevel(int value) {
			this.value = value;
		}

	}

	static UseVarQuote useVarQuote = UseVarQuote.yes;
	// static boolean useVarQuote = true;
	/**
	 * 比如字符串 "你好"替换为Encrypt.decode(Constant.xxxx)如果为false 直接
	 * 变成Constant.xxxx加载加密序列.
	 */
	static boolean useEncryptUtilWrap = true;
	/**
	 * 加密的工具class简称
	 */
	public static String sDecodSimpleClass = "EncryptUtil";
	/**
	 * 加密工具里面的解密方法
	 */
	// public static String fetchDecodeMethod = sDecodSimpleClass + ".decode";
	public static String sDecodeMethod = "decode";

	public static String fetchCallFullDecodeMethod(String methodName) {
		return sDecodSimpleClass + "." + methodName;
	}

	/*
	 * "\"(.*?)\""
	 */
	public static final String getDecodeMethodNameReg() {

		return ".*?" + fetchCallFullDecodeMethod(sDecodeMethod) + "\\((.*?)\\)" + ".*?";
	}

	public static final String getDecodConstantsNameReg() {
		// .*?(Constants\.\w*).*? ""+Constants.xxxxdd+". 取出这个常量
		// 这个常量的名字只能是字母大小写加数字，其他的不识别。
		return ".*?\\(" + sConstantsClass + "\\.\\w*\\).*?";
	}

	// TODO 有待添加常量导包功能
	private final static String sBrSign = "brbr";
	private final static boolean maxArrWarning = true;

	enum MODULEETYPE {
		QQ, QQ_2, QQ_MODULE, QQ_2_MODULE, Q_PLUS_DIR, QQ_PLUS_MODULE_GROUP, QQ_MODULE_TEST, TEST, WECHAT, PLUGIN, PUBLIC_FOLDER, ROBOT, MIAO, CRACK_SIGN, OTHER, TEST_OTHER_ROBOT, TEST_OTHER_ROBOT_PLUGIN

	}

	public enum EncryptType {
		NEWENCRYPT, OLDENCRYPT, OTHERENCRYPT, STR_SHOW_ENCRYPT, CUSTROM_CALL_QUTO// 字符串加密类型是2017年8月10日
		// 22:19:39
		// 最新构思的一种加密方式，是直接显示字符串的方式，不过如果让你好123加密之后长度不会更长就是一个蛋疼的问题。
	}

	/**
	 * 通常这里不需要改 只需要 修改moduleType在里面动态赋值即可。每次运行走的那么都是那个个方法
	 */
	static EncryptType currentEncryptType = EncryptType.NEWENCRYPT;
//	public static MODULEETYPE moduleType = MODULEETYPE.TEST;
//	public static MODULEETYPE moduleType = MODULEETYPE.ROBOT;
	public static MODULEETYPE moduleType = MODULEETYPE.QQ_PLUS_MODULE_GROUP;
	/**
	 * 常量的所在包名
	 */
	private static String sConstantsAtPackage;
	private static boolean encryptNumber = false;

	/**
	 * 因为注解必须是常量，所以加密是无意义的，也会报错 但是这个正则只能判断有空号的注解 ，无内容的注解完全可以忽略.
	 * 
	 * @return
	 */
	public static boolean isAnnotationLine(String line) {// 匹配@()的正则表达式
		String annotationReg = ".*?\\@\\s*.*?.*?\\).*?";
		// String annotationReg = "^\\@\\s*.*?\\(.*?\\)\\s*$";
		Pattern pattern = Pattern.compile(annotationReg);
		Matcher m = pattern.matcher(line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static DebugLevel debug = DebugLevel.MIDDLE;

	public static void main(String[] args) {
		String lineText = "开始\n结束 如果还是换行了说明替换失败\b \b \b \t \t \r\n  多重转义\\\\n \\\\r";
		System.out.println("解析后替换变量结果" + lineText);
		System.out.println("解析后替换变量结果" + unescapeStr(lineText));
		/*
		 * if(true){ System.out.println("忽略注解:"+isAnnotationLine("@xxx()"));
		 * System.out.println("忽略注解:"+isAnnotationLine("@(\"xxxxxx\")")); -
		 * return; }
		 */
		maxArrLength = 89;
		boolean encyrpt =1% 2 == 0;// 2 represents encrypt 1 represents
		// boolean encyrpt=false;
		
		if (moduleType == MODULEETYPE.QQ_PLUS_MODULE_GROUP) {
			moduleType = MODULEETYPE.QQ_MODULE;
			if (encyrpt) {
				encodeJavaAndroid();
			} else {
				decodeJavaAndroid();
			}
			moduleType = MODULEETYPE.QQ_2_MODULE;
			if (encyrpt) {
				encodeJavaAndroid();
			} else {
				decodeJavaAndroid();
			}

			moduleType = MODULEETYPE.Q_PLUS_DIR;
			if (encyrpt) {
				encodeJavaAndroid();
			} else {
				decodeJavaAndroid();
			}

		}else{
			
			if (encyrpt) {
				encodeJavaAndroid();

			} else {
				decodeJavaAndroid();
			}
			
		}
		
		if (moduleType == MODULEETYPE.QQ) {
			moduleType = MODULEETYPE.QQ_2;
			if (encyrpt) {
				encodeJavaAndroid();
			} else {
				decodeJavaAndroid();
			}
		}   else if (moduleType == MODULEETYPE.QQ_MODULE) {
			moduleType = MODULEETYPE.QQ_2_MODULE;
			if (encyrpt) {
				encodeJavaAndroid();
			} else {
				decodeJavaAndroid();
			}
		}

		getFileArrayList();

		// encodeJavaAndroid();
		/**
		 * 对于换行符用brbr处理
		 */
		String temp = "再次申明软件是永久,免费体验的,如果你是从别人手里付费购买的,请邮箱举报，我将把此人拉入黑名单" + "brbr体验完毕请24小时内删除,不删除是你的事情哈!一切违法行为与我无关"
				+ "brbr,任何进行牟利的行为都是违法的,不管你是不是未成年人,请自重!搞大了蹲监狱!!! "
				+ "brbr想了很久还是留个联系方式吧!如果您已被骗希望下次不要被骗了，也希望你不要做这种人,看见一次鄙视一次、"
				+ "brbr关注更新请访问http://qssq666.cn/update/qq_redpackaget.html    "
				+ "brbr为了防止更多的人被骗举报收费邮箱:qssq666@foxmail.com    "
				+ "brbr出现未知无法领取的错误建议卸载有风险软件,重装本软件,向作者提交问题一定要说清楚问题,而不是作者追问你具体怎么怎么个现象,你做了什么  " + "brbr给我弹视频语音的人直接拉黑不解释!  "
				+ "brbr软件会持续更新,告诉大家的好消息是我的行动将掀起自带QQ抢红包狂潮哈 " + "brbr我呢是工资阶层,做这个东西呢只能晚上  "
				+ "brbr双休去搞,时间不是很多,还有就是这破网上传速度太慢,自带的包文件大,所以更新比较慢,出现问题不会马上进行更新,不然我早上传很多个版本了."
				+ "brbr所以希望大家谅解,理解 你这么喷我软件这么这么垃圾我还有毛线的动力啊,你说我单身狗双休都在搞这些玩意,有多狼狈你能体会吗？brbr"
				+ "brbr光知道喷,另外想加群的朋友可以点击http://qssq666.cn点击关于加入群哈!这里就不贴出来了..";
				// System.out.println("input result:" + getVarName(temp, true));
				// decodeJavaAndroid();
				// arrayList.add(printCharCode(charArrayToIntArray("查询昵称信息失败\n情给予QQ访问应用列表权限".toCharArray())));
				// arrayList.add(printCharCode(charArrayToIntArray("请先手动领取一个红包".toCharArray())));
				// arrayList.add(printCharCode(charArrayToIntArray("情迁红包出现错误%s".toCharArray())));
				// arrayList.add(printCharCode(charArrayToIntArray("请先手动领取一个红包或请先打开QQ".toCharArray())));
				// arrayList.add(printCharCode(charArrayToIntArray("不带密码抢".toCharArray())));

		// deAndroid();
		// String file = "F:\\QQ_weichat\\test\\Test.java";
		// String file = "C:\\Users\\Administrator\\Desktop\\Test.java";

	}

	/**
	 * java源码是已经被转义了的进行加密的时候必须进行反转义。
	 * 
	 * @param str
	 * @return
	 */
	public static String unescapeStr(String str) {
		str = str.replaceAll("\\\\n", String.valueOf(new char[] { 10 }));
		str = str.replaceAll("\\\\b", "\b");
		str = str.replaceAll("\\@", "@");
		str = str.replaceAll("\\\\t", "\t");
		str = str.replaceAll("\\n", String.valueOf(new char[] { 10 }));
		str = str.replaceAll("\\\\r", String.valueOf(new char[] { 13 }));
		str = str.replaceAll("\\r", String.valueOf(new char[] { 13 }));
		// str = str.replaceAll("\n", String.valueOf(new char[]{10}));
		// str = str.replaceAll("\r", String.valueOf(new char[]{13}));
		return str;
	}

	/**
	 * 把被转义后的进行替换 StringEscapeUtils不靠谱 中文都转义了。 这里也需要4个 否则 只有b却没有斜杠了。
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeStr(String str) {
		str = str.replaceAll("@", "\\@");// 我猜测字符转义的是2个斜杠而其他则不是。
		str = str.replaceAll("\t", "\\\\t");
		str = str.replaceAll("\b", "\\\\b");
		str = str.replaceAll("\n", "\\\\n");
		str = str.replaceAll("\r", "\\\\r");
		// str = str.replaceAll("\n", String.valueOf(new char[]{10}));
		// str = str.replaceAll("\r", String.valueOf(new char[]{13}));
		return str;
	}

	private static void decodeJavaAndroid() {
		ArrayList<String> arrayList = getFileArrayList();
		if (debug == DebugLevel.All) {
			System.out.println("准备加载常量文件" + sConstantClassPath + ",类" + sConstantsClass);
		}
		if (currentEncryptType == EncryptType.STR_SHOW_ENCRYPT || currentEncryptType == EncryptType.CUSTROM_CALL_QUTO) {// 字符串加密的方法暂时不提取。

		} else {

			looadDecodeFieldToHashMap(sConstantClassPath, false);// 删除不存在的注释一下//开启有风险如果一个文件崩溃了到时候变量找不到了
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("加载常数组量完毕!,总数:" + sDecodeMap.size() + ",即将进行解密");

			}
		}

		for (int i = 0; i < arrayList.size(); i++) {
			String file = arrayList.get(i);
			doDecodeAllJava(file);
		}
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\InitConfig.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\Cf.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\DS.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\UiUtils.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\UiUtils.java";
		// file =
		// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\qqplugin.java";
		/*
		 * file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\PublicConstants.java";
		 * doDecodeAllJava(file);
		 * 
		 * file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\UiUtils.java";
		 * doDecodeAllJava(file); file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
		 * doDecodeAllJava(file); file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\DS.java";
		 * doDecodeAllJava(file); file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\Cf.java";
		 * doDecodeAllJava(file); file =
		 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QQUnrecalledHook.java";
		 */

	}

	private static void encodeJavaAndroid() {
		ArrayList<String> list = getFileArrayList();
		// if (randomUseVarQuote(useVarQuote)) {
		looadDecodeFieldToHashMap(sConstantClassPath, false);
		readEncodeVarArrayList(list);
		// }
		for (int i = 0; i < list.size(); i++) {

			String file = list.get(i);
			doEncodeAllJava(file);
		}
	}

	private static String getExtendsNameByFile(File file) {
		String expandsName = FilenameUtils.getExtension(file.getAbsolutePath());
		return expandsName;
	}

	private static String getClassNameByFile(File file) {
		String className = FilenameUtils.getBaseName(file.getAbsolutePath());
		return className;
	}

	protected static void doEncodeAllJava(String path) {
		File file = new File(path);
		if (file.isDirectory() && !isIgnoreFolder(file)) {
			File[] listFile = file.listFiles();
			if (listFile != null && listFile.length > 0) {
				System.out.println("目录" + file.getAbsolutePath() + "总数：" + listFile.length);
				for (int i = 0; i < listFile.length; i++) {
					if (listFile[i].isDirectory()) {
						// File[] currenChildFile = listFile[i].listFiles();

						doEncodeAllJava(listFile[i].getAbsolutePath());
						// doEncodeAllJava(listFile[i].getAbsolutePath());
					} else {
						if (isIgnoreFiles(listFile[i])) {
							System.err.println("忽略文件:" + listFile[i]);
						} else {

							doEncodeAllJava(listFile[i].getAbsolutePath());
						}
					}
				}
			} else {
				System.err.println("无法读取目录:" + file.getAbsolutePath() + ",是否存在:" + file.exists());
			}
		} else {
			if (path.equals(sConstantClassPath)) {
				if (!writeFile) {
					System.out.println("忽略 常量文件无需加密" + sConstantClassPath);
					return;
				}
			} else {
				if (sIgnoreFileList.contains(path)) {
					System.err.println("发现指定的忽略文件列表中，无需加密：" + path);
					return;
				}
			}
		}
		String expandsName = getExtendsNameByFile(file);
		String className = getClassNameByFile(file);
		if (!"java".equals(expandsName)) {
			System.out.println("extensionName:" + expandsName + ",className:" + className);// extensionName:txt,className:fw
			System.out.println("忽略文件 非java文件:" + file.getAbsolutePath());

			return;
		}

		OperaInfo resultInfo = readTxtFileEncode(path, className);
		if (!resultInfo.isResult()) {
			System.err.println("忽略文件" + file.getAbsolutePath() + "因为此文件已经全部加密完毕," + resultInfo.getMessage());
			return;
		}
		String result = resultInfo.getDoWhileResultText();
		if (!writeFile) {
			System.out.println("忽略,不写入文件");
			return;
		}
		try {
			FileUtils.writeStringToFile(file, result, "utf-8", false);
		} catch (IOException e) {
			System.err.println("写入一个文件失败:" + file.getAbsolutePath() + "\n" + e.toString() + "\n");
			e.printStackTrace();
		}
		System.out.print("覆盖文件成功:" + path + "\n");
	}

	/**
	 * 解密方法 通常是常量类的接收类型如 Utils.decode(name;或者 直接Utils.deocde(new int[]{ff});
	 * 
	 * @param name
	 * @return
	 */
	public static final String getDecodeMethodNameCall(String name) {
		return fetchCallFullDecodeMethod(sDecodeMethod) + "(" + name + ")";
	}

	/**
	 * 获取要加密或者解密的文件集合
	 * 
	 * @return
	 */
	private static ArrayList<String> getFileArrayList() {
		String temp = "";
		ArrayList<String> list = new ArrayList<String>();
		if (moduleType == MODULEETYPE.PLUGIN) {// 加密情插件
			sConstantsClass = "Constants";
			sDecodSimpleClass = "EncryptUtilX";
			sIgnoresFileNames = new String[] {};
			sConstantsAtPackage = "cn.qssq666.redpacket";
			useVarQuote = UseVarQuote.no;
			encryptAtPackage = "cn.qssq666";
			// sIgnoreKeyWords=new String[]{"TAG"};
			// sIgnoresFolder = new String[] { "MainHandlerPackage" };
			// sDecodSimpleClass="EncryptUtil";
			// sIgnoresFileNames = new String[] { "MainHandlerPackage.java" };
			// sIgnoresFileNames=new
			// String[]{"MainHandlerPackage.java","MainHandlerPackage"};
			// fetchDecodeMethod = sDecodSimpleClass + ".decode";

			sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\app\\src\\main\\java\\cn\\qssq666\\redpacket\\Constants.java";
			currentEncryptType = EncryptType.OTHERENCRYPT;
			temp = "G:\\newproject\\qqrepacket_pro\\app\\src\\main\\java\\cn\\qssq666\\redpacket\\";
			list.add(temp);

		} else if (moduleType == MODULEETYPE.TEST_OTHER_ROBOT) {

			sConstantsClass = "noconstants";
			sDecodSimpleClass = "oO00oo";
			sDecodeMethod = "o";
			encryptAtPackage = "cn.qssq666";
			currentEncryptType = EncryptType.STR_SHOW_ENCRYPT;
			sConstantsAtPackage = "noconstants";
			// useVarQuote = false; C0202ooo.o
			useVarQuote = UseVarQuote.str;// 爆满了代码过长使用随机
			sConstantClassPath = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			temp = "F:\\QQ_weichat\\crackstr\\classes.dex_src";
			// temp =
			// "F:\\QQ_weichat\\crackstr\\classes.dex_src\\com\\luomi\\lm\\morethreads\\db";
			list.add(temp);

			// TEST_OTHER_ROBOT_PLUGIN
		} else if (moduleType == MODULEETYPE.TEST_OTHER_ROBOT_PLUGIN) {

			sConstantsClass = "noconstants";
			sDecodSimpleClass = "C0202ooo"; // C0202ooo.o C0202ooo.o
			sDecodeMethod = "o";// F:\QQ_weichat\crackstr\gruppluginclasses.dex_src
			encryptAtPackage = "cn.qssq666";
			currentEncryptType = EncryptType.CUSTROM_CALL_QUTO;
			sConstantsAtPackage = "noconstants";
			custromParseProvider = new CustromParseProvider() {

				@Override
				public String requestEncode(String str) {
					// TODO Auto-generated method stub
					throw new RuntimeException("不支持加密");
				}

				@Override
				public String requestDecode(String str) {
					// TODO Auto-generated method stub
//					
//					return c.ooo.o(str);
			
				return null;
				}
			};
			// useVarQuote = false; C0202ooo.o
			useVarQuote = UseVarQuote.str;// 爆满了代码过长使用随机
			sConstantClassPath = "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			// sConstantClassPath =
			// "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			temp = "F:\\QQ_weichat\\crackstr\\gruppluginclasses.dex_src\\";
			// temp = "F:\\QQ_weichat\\crackstr\\classes.dexplugin_src";
			// temp =
			// "F:\\QQ_weichat\\crackstr\\classes.dex_src\\com\\luomi\\lm\\morethreads\\db";
			list.add(temp);

			// TEST_OTHER_ROBOT_PLUGIN
		} else if (moduleType == MODULEETYPE.ROBOT) {// 机器人加密
			encryptAtPackage = "cn.qssq666";
			currentEncryptType = EncryptType.NEWENCRYPT;
			sDecodeMethod = "a7";
			sConstantsAtPackage = "cn.qssq666.robot.constants";
			sDecodSimpleClass = "EncryptUtilN";
			sConstantsClass = "EncryptConstants";
			// useVarQuote = false;
			useVarQuote = UseVarQuote.no;// 爆满了代码过长使用随机
			sConstantClassPath = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			// temp =
			// "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\MainActivity.java";
			temp = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot";
			list.add(temp);
			/*
			 * temp=
			 * "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot";
			 * list.add(temp);
			 */

		} else if (11 == 1133) {// 共同qita微信常量修复qssq6666根目录文件夹

			currentEncryptType = EncryptType.NEWENCRYPT;
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Cns.java";
			temp = "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666";
			list.add(temp);

		} else if (moduleType == MODULEETYPE.PUBLIC_FOLDER) {// 共同特性包名加密分享

			currentEncryptType = EncryptType.NEWENCRYPT;
			encryptAtPackage = "cn.qssq666";
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\cn\\qssq666\\TempConstant.java";
			sConstantsClass = "TempConstant";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\cn\\qssq666";
			list.add(temp);

		} else if (moduleType == MODULEETYPE.QQ) {// 加密内置Q文件夹
			currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			encryptAtPackage = "cn.qssq666";
			sConstantsClass = "Values1";
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a1\\"
					+ sConstantsClass + ".java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a1";
			list.add(temp);
		} else if (moduleType == MODULEETYPE.QQ_2) {// 加密内置Q文件夹
			currentEncryptType = EncryptType.NEWENCRYPT;
			sDecodSimpleClass = "EncryptUtilN";
			// cn.qssq666
			encryptAtPackage = "cn.qssq666";
			sConstantsClass = "Value2";
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a2\\"
					+ sConstantsClass + ".java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a2";
			list.add(temp);

		} else if (moduleType == MODULEETYPE.QQ_MODULE) {// 加密内置Q文件夹
			currentEncryptType = EncryptType.NEWENCRYPT;
			useVarQuote = UseVarQuote.no;
			sDecodSimpleClass = "EncryptUtilN";
			// cn.qssq666
			encryptAtPackage = "cn.qssq666";
			sConstantsClass = "Values1";
			sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a1\\"
					+ sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a1";
			list.add(temp);
			
			temp="G:\\newproject\\qqrepacket_pro\\testjniapp\\src\\main\\java\\cn\\qssq666";
			list.add(temp);
		} else if (moduleType == MODULEETYPE.QQ_2_MODULE) {// 加密内置Q文件夹
			currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			useVarQuote = UseVarQuote.no;
			encryptAtPackage = "cn.qssq666";
			sConstantsClass = "Value2";
			sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2";
			list.add(temp);
			
			

		} else if (moduleType == MODULEETYPE.Q_PLUS_DIR) {// 
			// cn.qssq666
			
			
/*			sDecodeMethod = "a7";
			currentEncryptType = EncryptType.NEWENCRYPT;
			sDecodSimpleClass = "EncryptUtilX";
			encryptAtPackage = "cn.qssq666";
			useVarQuote = UseVarQuote.no;
			sConstantsClass = "Value2";
			*/
			
			
			sDecodSimpleClass = "EncryptUtilX";
			sIgnoresFileNames = new String[] {};
			sConstantsAtPackage = "cn.qssq666.redpacket";
			useVarQuote = UseVarQuote.no;
			encryptAtPackage = "cn.qssq666";
				currentEncryptType=EncryptType.OTHERENCRYPT;
			
			
			sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\basehookmodule\\src\\main\\java\\cn\\qssq666";
			list.add(temp);
			temp = "G:\\newproject\\qqrepacket_pro\\qplus\\src\\main\\java\\cn\\qssq666";
			list.add(temp);
	
		} else if (moduleType == MODULEETYPE.QQ_MODULE_TEST) {// 加密内置Q文件夹
			useVarQuote = UseVarQuote.no;
			currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			encryptAtPackage = "cn.qssq666";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\tool\\TestEncrypt.java";
			sConstantsClass = "Value2";
			sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ sConstantsClass + ".java";
			list.add(temp);

		} else if (moduleType == MODULEETYPE.TEST) {
			useVarQuote = UseVarQuote.no;
			currentEncryptType = EncryptType.NEWENCRYPT;
			encryptAtPackage = "cn.qssq666";
			
			sDecodeMethod = "a7";
			sDecodSimpleClass = "EncryptUtilN";
			
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\test\\ConstantValue.java";
			sConstantsClass = "ConstantValue";
			temp = "G:\\newproject\\qqrepacket_pro\\testjniapp\\src\\main\\java\\cn\\qssq666";
			list.add(temp);
			

		} else if (moduleType == MODULEETYPE.WECHAT) {// 插入微信Sscon加密 文件夹批量
			currentEncryptType = EncryptType.NEWENCRYPT;
			encryptAtPackage = "cn.qssq666";
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\ConstantValue.java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard";
			// temp =
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat";
			list.add(temp);
			sIgnoreFileList.add(
					"F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\EncryptUtil.java");
			// temp =
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Control.java";
			// list.add(temp);
			/*
			 * temp=
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\UiUtils.java";
			 * list.add(temp);
			 * 
			 * temp =
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Cf.java";
			 * list.add(temp);
			 */
		} else if (moduleType == MODULEETYPE.CRACK_SIGN) {
			currentEncryptType = EncryptType.OTHERENCRYPT;
			//
			useVarQuote = UseVarQuote.no;

			encryptAtPackage = null;
			debug = DebugLevel.All;
			sConstantClassPath = null;
			sConstantsClass = null;

			sDecodSimpleClass = "LZUtils";
			// fetchDecodeMethod(sDecodeMethod)
			sDecodeMethod = "hello";
			// fetchDecodeMethod = "LZUtils.hello";
			// encryptConfig.setAllowConstantsEmpty(true);//本来就没解密这个东西都不应该有
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\statistics\\ufo";
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			list.add(temp);
		} else if (moduleType == MODULEETYPE.MIAO) {
			currentEncryptType = EncryptType.OTHERENCRYPT;
			encryptAtPackage = null;
			debug = DebugLevel.All;
			sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			sConstantsClass = "fw";
			sDecodSimpleClass = "fw";
			sDecodeMethod = "sss";
			// fetchDecodeMethod = "fw.sss";
			// encryptConfig.setAllowConstantsEmpty(true);//本来就没解密这个东西都不应该有
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			list.add(temp);
		} else if (11 == 111) {

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			list.add(temp);
		} else if (1 == 9) {
			sConstantClassPath = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\ConstantValue.java";
			temp = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\cn\\qssq666\\robot\\business\\RobotContentProvider.java";

			list.add(temp);

			temp = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\cn\\qssq666\\robot\\AddWordActivity.java";
			list.add(temp);

			/*
			 * temp =
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
			 * list.add(temp);
			 */
		} else if (false == true) {
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\TestVar.java";
			list.add(temp);
		} else if (11 == 111) {
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			list.add(temp);

		} else {
			if(true){
				throw new RuntimeException("TYPE ERROR " +moduleType);
			}
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\util\\robot\\RobotUtil.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\UiUtils.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QQUnrecalledHook.java";
			list.add(temp);

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			list.add(temp);

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\DS.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\Cf.java";

			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\qqplugin.java";
			list.add(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\InitConfig.java";
			list.add(temp);
		}

		/* */

		return list;
	}

	/**
	 * 解密文本 java
	 * 
	 * @param path
	 */
	protected static void doDecodeAllJava(String path) {
		File file = new File(path);
		if (file.isDirectory() && !isIgnoreFolder(file)) {
			File[] listFile = file.listFiles();
			if (listFile != null) {
				for (int i = 0; i < listFile.length; i++) {
					if (listFile[i].isDirectory()) {
						doDecodeAllJava(listFile[i].getAbsolutePath());
					} else {
						doDecodeAllJava(listFile[i].getAbsolutePath());
					}
				}
			}
		}
		String expandsName = FilenameUtils.getExtension(file.getAbsolutePath());
		String className = FilenameUtils.getBaseName(file.getAbsolutePath());
		if (!"java".equals(expandsName)) {
			System.out.println("extensionName:" + expandsName + ",className:" + className);// extensionName:txt,className:fw
			System.out.println("忽略文件 非java文件:" + file.getAbsolutePath() + ",是否是文件" + file.isFile());

			return;
		}

		OperaInfo info = readTxtFileDecode(path, className);
		if (!info.isResult() && !encryptConfig.isAllowConstantsEmpty()) {
			System.err.println("忽略" + file.getName() + "，因为已经解密,无需再次覆盖文件 " + info.getMessage() + "，如要强制执行，请配置加密属性。");
			return;
		}
		String result = info.getDoWhileResultText();
		if (!writeFile) {
			System.out.println("忽略,不写入文件");
			return;
		}
		try {
			FileUtils.writeStringToFile(file, result, "utf-8", false);
		} catch (IOException e) {
			System.out.println("写入一个文件失败:" + file.getAbsolutePath() + "\n" + e.toString() + "\n");
			e.printStackTrace();
		}
		System.out.print("覆盖文件成功:" + path + "\n");
	}

	// 每锟轿讹拷一锟斤拷锟角诧拷锟斤拷效锟斤拷锟叫碉拷锟斤拷耍锟�
	private static String getVarName(String name, boolean isChinese) {
		return "public final static int[] " + getVarBaseName(name, isChinese) + "= ";
	}

	/**
	 * public final static int[] " + varName + "=
	 * 
	 * @param varName
	 * @return
	 */
	private static String getVarName(String varName) {
		return "public final   static int[] " + varName + "= ";
	}

	/**
	 * 之前的直接分割会导致变量名冲突了。不能保证唯一 。比如 forui切换器控件。
	 * 
	 * @param conent
	 * @param isChinese
	 * @return
	 */
	private static String getVarBaseName(String conent, boolean isChinese) {

		Pattern p = Pattern.compile("\\s*|\t|\r|\n|>");
		Matcher m = p.matcher(conent);
		conent = m.replaceAll("");
		// if(true){
		// return;
		// }
		String temp = conent.replaceAll("#", "jing");
		temp = temp.replaceAll("\\%s", "");// 这个名字不生成变量 这是转义的
		// temp = temp.replaceAll("[\n]", "woshi换行");//搜索不到
		temp = temp.replaceAll("\\%d", "");// 这个名字不生成变量 这是转义的
		temp = temp.replaceAll("\\%f", "");// 这个名字不生成变量 这是转义的
		temp = temp.replaceAll("【", "zhongz");
		temp = temp.replaceAll("//>", "right_arr_");
		temp = temp.replaceAll("】", "zhongr");
		temp = temp.replaceAll("：", "");
		temp = temp.replaceAll("、", "");
		temp = temp.replaceAll("，", "");
		temp = temp.replaceAll("；", "");// 省略号
		temp = temp.replaceAll("，", "");
		temp = temp.replaceAll("‘", "_yiyuyan_");// 易语言注释一样奇葩
		temp = temp.replaceAll("。", "_pointZ_");// 易语言注释一样奇葩
		temp = temp.replaceAll("……", "_sheng_");// 省略号

		temp = temp.replaceAll("——", "_henganZ_");// 省略号
		temp = temp.replaceAll("—-", "_henganZ1_");// 没有按shirt
		temp = temp.replaceAll("\\(", "leftkuohao_");
		temp = temp.replaceAll("\\?", "_wenhao_");
		temp = temp.replaceAll("\\$", "_meiyuan_");
		temp = temp.replaceAll("￥", "_renminbi_");
		temp = temp.replaceAll("\\\\", "_fx_");// 这个比较特别的符号，另外
												// 如果|符号弄出来了||||4个进行匹配会导致的结果是出现多个被替换。都变成了split
		temp = temp.replaceAll("（", "leftkuohao_");
		temp = temp.replaceAll("\\)", "r_kuohao_");
		temp = temp.replaceAll("\\）", "r_kuohao_");
		/*
		 * temp = temp.replaceAll("\\[", "l_zkh_"); temp =
		 * temp.replaceAll("\\]", "r_zkh_");
		 */
		temp = temp.replaceAll("\\^", "_sjt_");
		temp = temp.replaceAll("	", "_tab_");
		temp = temp.replaceAll(" ", "_space_");
		temp = temp.replaceAll(">", "_jian_right_");
		temp = temp.replaceAll("《", "l_jt");
		temp = temp.replaceAll("<", "l_jt");
		temp = temp.replaceAll(">", "r_jt");
		temp = temp.replaceAll("》", "r_jt");
		temp = temp.replaceAll("\\*", "xing");

		temp = temp.replaceAll("\\.", "point");
		temp = temp.replaceAll("&", "_with_");
		temp = temp.replaceAll("，", "_douhao_");
		temp = temp.replaceAll("、", "_dunhao_");
		temp = temp.replaceAll("%", "_bai_");
		temp = temp.replaceAll("#", "_jin_");
		temp = temp.replaceAll("@", "aite_");
		temp = temp.replaceAll("!", "gantan_");
		temp = temp.replaceAll("\\+", "_add_");
		temp = temp.replaceAll("=", "_eq_");
		temp = temp.replaceAll("-", "_sub_");
		temp = temp.replaceAll("·", "_esc_next_");
		temp = temp.replaceAll("~", "_piao_");
		temp = temp.replaceAll("`", "_piaohao_");

		temp = temp.replaceAll(",", "_douhao_");
		temp = temp.replaceAll("/", "_x_");

		temp = temp.replaceAll("\\{", "_dkhl_");
		temp = temp.replaceAll("}", "_dkhr_");
		temp = temp.replaceAll(";", "_fenhao_");
		temp = temp.replaceAll("\"", "");
		temp = temp.replaceAll("\'", "");
		temp = temp.replaceAll("\\[", "zkhl_");
		temp = temp.replaceAll("\\]", "_zhhr_");
		temp = temp.replaceAll("\\|", "_spt_");
		temp = temp.replaceAll(":", "_mh_");
		temp = temp.replaceAll("！", "_GTH_");
		temp = temp.replaceAll("	", "");
		temp = temp.replaceAll(" ", "");
		String temp1 = "QSSQ_" + (isChinese ? hanyuPinyinHelper.toHanyuPinyin(temp) : temp);
		if (temp1.length() > 20) {
			String frist = temp1.substring(0, 20);
			frist = frist.replaceAll(" ", "");
			temp1 = frist + MD5Util.MD5Encode(temp1);
		}
		return temp1;
	}

	private static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-mm-dd-HH:mm:dd");
		return format.format(date);
	}

	/**
	 * 
	 * 多余已存在的变量还是建议手动删除的比较好 ，所以这里创建一个时间标记
	 * 
	 * @param filePath
	 *            d读入的文件
	 * @param className
	 *            类名
	 * @param vardeclareJava
	 *            插入的类
	 * @return
	 */
	public static String getInsertSplitLine(String className) {
		return "////【插入分割线[" + className + "]:" + getCurrentTime() + "】\n";
	}

	/**
	 * 
	 * 整个流程是先读入常量 然后写入常量然后再循环一遍再操作一次 读入加密字符串并写入常量文件
	 * 
	 * @param list
	 */
	public static void readEncodeVarArrayList(ArrayList<String> list) {

		String constantClassSrc;// 这个值不断的变化叠加。不会反悔null.
		try {
			constantClassSrc = FileUtils.readFileToString(new File(sConstantClassPath), "utf-8");
		} catch (IOException e1) {

			e1.printStackTrace();
			throw new RuntimeException(e1);
		}

		for (int i = 0; i < list.size(); i++) {
			String file = list.get(i);
			File fileManager = new File(file);
			if (fileManager.isDirectory()) {
				File[] dir = fileManager.listFiles();
				constantClassSrc = readEncodeVarAndInsert(dir, constantClassSrc);
				/*
				 * for (int j = 0; j < dir.length; j++) { File currentFile =
				 * dir[i]; String className = getClassNameByFile(currentFile);
				 * String temp= constantClassSrc.toString();
				 * constantClassSrc=readEncodeVarAndInsert(currentFile.
				 * getAbsolutePath(), className, temp);//
				 * className用于判断到底属于那个类的变量 }
				 */
			} else {

				constantClassSrc = readEncodeVarAndInsert(file, constantClassSrc);// className用于判断到底属于那个类的变量
			}

		}
		;
		/*
		 * try { FileUtils.copyFile(new File(sConstantClassPath), new
		 * File(sConstantClassPath + ".bak")); } catch (IOException e1) {
		 * 
		 * e1.printStackTrace(); }
		 */

		if (useVarQuote == UseVarQuote.str || useVarQuote == UseVarQuote.no) {
			System.out.println("当前模式忽略写入常量加密数组到文件，模式:" + useVarQuote);
			return;
		}
		try {
			FileUtils.writeStringToFile(new File(sConstantClassPath), constantClassSrc, "utf-8", false);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	public static String readEncodeVarAndInsert(String filePath, String vardeclareJava) {
		return readEncodeVarAndInsert(new File[] { new File(filePath) }, vardeclareJava);
	}

	public static String readEncodeVarAndInsert(File[] filePath, String vardeclareJava) {

		/**
		 * 如果已经存在了则不再进行添加
		 */

		// System.out.println();

		String patternString = "\"(.*?)\"";// 取出双引号
		StringBuilder sb = new StringBuilder();
		StringBuilder varNameSb = new StringBuilder(40);

		try {
			String encoding = "utf-8";
			loopEncryptEncodeConstant(filePath, patternString, varNameSb, encoding);

		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return insertFieldAtConstantClassAfter(vardeclareJava, varNameSb.toString());
	}

	private static void loopEncryptEncodeConstant(File[] filePath, String patternString, StringBuilder varNameSb,
			String encoding) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		for (int i = 0; i < filePath.length; i++) {
			if (filePath[i] != null) {
				if (filePath[i].isDirectory()) {
					File[] currentFiles = filePath[i].listFiles();
					for (int j = 0; j < currentFiles.length; j++) {
						File newFile = currentFiles[j];
						if (currentFiles[j].isDirectory()) {
							loopEncryptEncodeConstant(newFile.listFiles(), patternString, varNameSb, encoding);
						} else {
							doTempConstants(newFile, patternString, varNameSb, encoding);
						}

					}

				} else {
					doTempConstants(filePath[i], patternString, varNameSb, encoding);
				}
			}

		}
	}

	private static void doTempConstants(File filePath, String patternString, StringBuilder varNameSb, String encoding)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String className = getClassNameByFile(filePath);
		varNameSb.append(getInsertSplitLine(className));
		File file = filePath;
		if (file.isDirectory()) {
			System.out.println("忽略目录 不加载常量" + file.getAbsolutePath());
		} else if (file.exists() && !file.isDirectory()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				// 匹配类似velocity规则的字符串
				// 生成匹配模式的正则表达式
				Pattern pattern = Pattern.compile(patternString);

				Matcher matcher = pattern.matcher(lineTxt);

				// 两个方法：appendReplacement, appendTail
				if (isIgNoreDecodeText(lineTxt)) {// 一般情况下这是一个私有的字段
													// 忽略
					if (debug.getValue() >= DebugLevel.WRAIN.getValue()) {
						// 那么问题来了，\n字符串如何处理换行问题呢、
						System.err.println("忽略包含日志行 忽略:" + lineTxt);
					}
				} else {

					while (matcher.find()) {
						String matchBase = matcher.group();
						// System.err.println("match:" +);//这里匹配返回的包含引号
						String temp = matcher.group(1);// 不包含双引号
						if (temp != null && temp != "" && temp != "," && temp != "&" && temp.length() > 0) {
							boolean isChinese = checkChinese(temp);

							if (isChinese || checkZimu(temp) || checkNumber(temp)) {
								String baseVar = getVarBaseName(temp, isChinese);
								if (!sEncodeMap.containsKey(baseVar)) {
									if (sDecodeMap.containsKey(baseVar)) {
										sEncodeMap.put(baseVar, "");// 只是用来放置一个东西来判断是否已经加密了，但是实际上加密了啥东西不需要关注所以为空
										if (debug.getValue() >= DebugLevel.ERR.getValue()) {
											System.err.println("解码常量表已定义变量:" + baseVar + ",line:" + temp);
										}
										continue;
									}
									// 生成注释声明
									varNameSb.append("/**\n" + temp + "\n*/\n");
									/*
									 * 下面这句话实现变量申明字符串
									 */
									// 生成
									varNameSb.append(getVarName(baseVar) + stringToCharCodeJava(temp) + ";//content="// 真正的加密逻辑在这里
											+ temp + "\n\n");
									if (debug.getValue() >= DebugLevel.All.getValue()) {
										System.out.println("创建字段:" + baseVar + ",value:" + temp);
									}
									sEncodeMap.put(baseVar, "");
								} else {
									if (debug.getValue() >= DebugLevel.All.getValue()) {

										System.err.println("忽略已存在的字段:" + baseVar);
									}
								}
							}
						}
					}
				}
			}
			read.close();
		} else {
			System.out.println("找不到指定的文件" + file.getAbsolutePath());
			throw new RuntimeException("找不到指定的文件" + filePath);
		}
	}

	/*
	 * public static boolean checkChinese(String chinese) { String regex =
	 * "[\u4e00-\u9fa5]\"; return Pattern.matches(regex, chinese); }
	 */
	public static boolean checkChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean checkZimu(String str) {

		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean checkNumber(String str) {

		if (!encryptNumber) {
			return false;
		}
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 加载加密字段并进行解密处理 存放到集合中 如果需要替换则会返回替换,进行屏蔽处理之后的文本 这样做可以减少加密工作已经加密的合并到一起。
	 * 
	 * @param filePath
	 * @param className
	 * @param needReplace
	 *            是否需要替换 需要替换则把存在的申明变量注释 否则 无视！
	 * @return
	 */

	public static String looadDecodeFieldToHashMap(String filePath, boolean needReplace) {
		if (filePath == null) {
			if (debug.getValue() >= DebugLevel.WRAIN.getValue()) {
				System.err.println("忽略常量读取 因为没有配置");
			}
			return null;
		}
		/**
		 * 加密变量名 和 解密结果
		 */

		// System.out.println();

		StringBuilder sb = new StringBuilder(50);
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {

					// 匹配类似velocity规则的字符串

					// 生成匹配模式的正则表达式
					if (lineTxt.contains("int")) {// 对于常量行不需要判断是否是静态常量的。
						if (isIgNoreDecodeText(lineTxt, true)) {
							System.err.println("解析忽略忽略行：" + lineTxt);
							continue;
						}
						if (lineTxt == null || lineTxt.equals("")) {
							continue;
						}
						Pattern patternVarName = Pattern.compile(encodeLineReg);
						Matcher matcher = patternVarName.matcher(lineTxt);

						while (matcher.find()) {
							String matchBase = matcher.group();// 获取匹配行
							String temp = matcher.group(1).trim();// 获取匹配yuanzn
							String arrsStr = matcher.group(2);// 获取匹配yuanzn
							String[] arr = arr = arrsStr.split(",");
							String decodeResult = escapeStr(getDeCodeValue(arr));// 解密后变成java源码后就可以解决出现多行了特别是换行符简直惨不忍睹
							if (debug.getValue() >= DebugLevel.MIDDLE.getValue()) {
								System.out.println("varName[" + temp + "]解密结果:" + decodeResult);
							}
							sDecodeMap.put(temp, decodeResult);// sDecodeMap
							if (needReplace) {
								lineTxt = "////" + sIGNORE_DECODE + lineTxt;// 不进行删除而是进行注释
							}
						}
					}
					// 两个方法：appendReplacement, appendTail
					sb.append("" + lineTxt + "\n");

				}
				read.close();
			} else {
				throw new Error("找不到常量文件" + file);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			throw new Error(e);
		}
		/*
		 * if (true) { throw new RuntimeException("等待处理");
		 * 
		 * }
		 */
		return sb.toString();
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	/*
	 * private static boolean isChinese(char c) { Character.UnicodeBlock ub =
	 * Character.UnicodeBlock.of(c); if (ub ==
	 * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub ==
	 * Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub ==
	 * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub ==
	 * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub ==
	 * Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub ==
	 * Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub ==
	 * Character.UnicodeBlock.GENERAL_PUNCTUATION) { return true; } return
	 * false; }
	 */
	/*
	 * public static boolean checkZimu(String chinese) { String regex =
	 * "a-zA-Z"; return Pattern.matches(regex, chinese); }
	 */
	/**
	 * ffff
	 * 
	 */

	public static boolean isEncodeLine(String str) {
		return str.matches(encodeLineReg);
	}

	/**
	 * new\sint\[\s?\] \s匹配空格换行 ?0次或者多次 从变量字表中查询如果查询到则进行替换处理 并返回替换之后的文本 因此使用之前必须
	 * 写加载变量。
	 * 
	 * @param filePath
	 * @param className
	 * @return
	 */
	public static OperaInfo readTxtFileDecode(String filePath, String className) {
		int decodeCount = 0;
		int ignoreCount = 0;
		int errCount = 0;
		/**
		 * 加密变量名 和 解密结果
		 */

		// System.out.println();

		StringBuilder sb = new StringBuilder(50);
		if (debug.getValue() >= DebugLevel.All.getValue()) {
			System.out.println("读取" + filePath + "中");
		}
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 判断是否是加密方法
					String encryptMethodFull = fetchCallFullDecodeMethod(sDecodeMethod);
					boolean containEncryptMethod = lineTxt.contains(encryptMethodFull);
					if (debug.getValue() >= DebugLevel.All.getValue()) {
						System.out.println(
								"是否包含加密方法" + encryptMethodFull + ",结果为" + containEncryptMethod + ",行:" + lineTxt);
					}
					if (containEncryptMethod) {

						Pattern patternVarMethod = Pattern.compile(getDecodeMethodNameReg());
						// System.err.println("当前可能是变量行行:"+lineTxt);
						Matcher matcherMethod = patternVarMethod.matcher(lineTxt);
						while (matcherMethod.find()) {
							String varName = null;
							String matchBase = matcherMethod.group();// 获取匹配行不只是暴行这个方法这一个方法所有都在
							String temp = matcherMethod.group(1);// 把变量名替换即可
							if (isIntArr(temp)) {// 如果不是变量而是直接的数组
								String parseResult = anonymousmintArrToParseString(temp);
								if (parseResult == null) {
									continue;
								} else {
									if (debug.getValue() >= DebugLevel.All.getValue()) {
										System.out.println("int类型解密:" + temp + "结果:" + parseResult);
									}
									varName = MD5Util.MD5Encode(temp);
									sDecodeMap.put(varName, parseResult);
								}
							} else if (isStrEncrypt(temp) && EncryptType.NEWENCRYPT != currentEncryptType) {
								if (currentEncryptType == EncryptType.STR_SHOW_ENCRYPT) {
									String tempFix = temp.substring(1, temp.length() - 1);

									System.out.println("尝试解密:" + temp + ",并去掉两端双引号之后为:" + tempFix);
									String parseResult = StrEngC0202ooo.decodeStr(tempFix);// 把解密结果放进去，变量就当作字符串本身算了，也方便优化。
									varName = temp;
									sDecodeMap.put(varName, parseResult);
									System.out.println("解密字符串" + temp + "结果:" + parseResult);

								} else if (currentEncryptType == EncryptType.CUSTROM_CALL_QUTO) {
									String tempFix = temp.substring(1, temp.length() - 1);
									System.out.println("尝试解密(自定义回调):" + temp + ",并去掉两端双引号之后为:" + tempFix);
									String parseResult = custromParseProvider.requestDecode(tempFix);// 把解密结果放进去，变量就当作字符串本身算了，也方便优化。
									varName = temp;
									sDecodeMap.put(varName, parseResult);
									System.out.println("解密字符串" + temp + "结果:" + parseResult);

								} else {

									System.err.println("无法解密字符串类型的加密常量 ,目前只支持" + EncryptType.STR_SHOW_ENCRYPT.name()
											+ "类型,当前传递的类型是：" + currentEncryptType.name());
									continue;
								}
							} else {
								System.out.println("是正常的常量加密方法，得去掉类名,但是这里上面的isInitArr是否多余了" + lineTxt);
								varName = temp.replace(sConstantsClass + ".", "");// 除去类名.
							}

							if (sDecodeMap.containsKey(varName)) {

								String value = sDecodeMap.get(varName);// 解密后文本在另外一个方法已经处理

								decodeCount++;
								// 把结果替换为正常的字符串，并抹掉解密工具类的引用
								lineTxt = lineTxt.replace(fetchCallFullDecodeMethod(sDecodeMethod) + "(" + temp + ")",
										"\"" + value + "\"");// 对函数进行替换
								// lineTxt = lineTxt.replaceAll("\n",
								// sBrSign);// 处理里的字符串。

								if (lineTxt.indexOf("\n") != -1) {
									System.err.println("无法替换\n还是存在");
								}
								if (debug.getValue() >= DebugLevel.All.getValue()) {

									System.out.println("找到定义变量名：" + temp + "实际变量值:" + value + "\n替换后当前行:" + lineTxt);
								}

							} else {

								errCount++;
								if (debug.getValue() >= DebugLevel.ERR.getValue()) {

									System.err
											.println("无法处理变量 因为成员变量中不存在,looadEncodeFieldToHashMap确保解密之前是否调用了加载变量方法,var="
													+ varName + "\n所处行:\n" + lineTxt);
								}
							}
						}

					} else if (sEnableParseConstantsLine && lineTxt.contains(sConstantsClass)) {// 对非加密工具类方法包括的字符串常量进行解密

						Pattern patternVarMethod = Pattern.compile(getDecodConstantsNameReg());
						// System.err.println("当前可能是变量行行:"+lineTxt);
						Matcher matcherMethod = patternVarMethod.matcher(lineTxt);
						while (matcherMethod.find()) {
							String varName = null;
							String matchBase = matcherMethod.group();// 获取匹配行不只是暴行这个方法这一个方法所有都在
							String temp = matcherMethod.group(0);// 取出变量名
							if (isIntArr(temp)) {// 如果不是变量而是直接的数组
								String parseResult = anonymousmintArrToParseString(temp);
								if (parseResult == null) {
									continue;
								} else {
									varName = MD5Util.MD5Encode(temp);
									sDecodeMap.put(varName, parseResult);
								}
							} else {

								varName = temp.replace(sConstantsClass + ".", "");// 除去类名.
							}

							if (sDecodeMap.containsKey(varName)) {

								String value = sDecodeMap.get(varName);// 解密后文本在另外一个方法已经处理
								// value=escapeStr(value);//是不是多余我的我不知道

								decodeCount++;
								lineTxt = lineTxt.replace(fetchCallFullDecodeMethod(sDecodeMethod) + "(" + temp + ")",
										"\"" + value + "\"");// 对函数进行替换
								// lineTxt = lineTxt.replaceAll("\n",
								// sBrSign);// 处理里的字符串。

								if (lineTxt.indexOf("\n") != -1) {
									System.err.println("无法替换\n还是存在(直接常量模式)");
								}
								if (debug.getValue() >= DebugLevel.MIDDLE.getValue()) {

									System.out.println(
											"(直接常量模式)找到定义变量名：" + temp + "实际变量值:" + value + "\n替换后当前行:" + lineTxt);
								}

							} else {
								errCount++;
								System.err.println(
										"(直接常量模式)无法处理变量 因为成员变量中不存在,looadEncodeFieldToHashMap确保解密之前是否调用了加载变量方法,var["
												+ varName + "]\n所处行:\n" + lineTxt);
							}
						}

					}
					// 两个方法：appendReplacement, appendTail
					sb.append("" + lineTxt + "\n");

				}
				read.close();
			} else {

				System.err.println("找不到指定的文件");
				throw new RuntimeException("找不到文件" + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			throw new RuntimeException("文件文件" + filePath + "  ," + e);
		}
		/*
		 * if (true) { throw new RuntimeException("等待处理");
		 * 
		 * }
		 */
		System.out.println("处理" + filePath + "完毕");
		OperaInfo info = new OperaInfo();
		info.setResult(decodeCount > 0);
		info.setMessage("解密数据总数" + decodeCount + ",无法解密总数:" + errCount);
		info.setDoWhileResultText(sb.toString());
		return info;
	}

	/**
	 * 这种加密字符串算法首先他是字符串，那么有双引号，然后基本上都有= 不分没有等于号号,但是也包含了一些大小不一的英文长度不一的字符串加密乱码。
	 * 
	 * @param temp
	 * @return
	 */
	private static boolean isStrEncrypt(String temp) {
		// TODO Auto-generated method stub
		if (temp.contains("=")) {
			System.out.println("判断是否包含加密字符串算法:" + temp);
		}
		if (!temp.startsWith("\"") && temp.endsWith("\"")) {
			System.out.println("判断不是，因为没有开头和末尾的引号:" + temp);
			return false;
		}
		if (temp.contains("=")) {
			return true;
		}
		boolean isEncryptEng = temp.matches(".*?[a-zA-Z].*?");
		if (debug.getValue() >= DebugLevel.MIDDLE.getValue()) {
			System.out.println("是否匹配字符串打乱加密算法" + isEncryptEng + "," + temp);
		}
		return isEncryptEng;
	}

	/**
	 * 给我一个 new int []{11,111,33; 我给你解密
	 * 
	 * @param str
	 * @return
	 */
	private static String anonymousmintArrToParseString(String str) {
		Pattern patternVarName = Pattern.compile(AnonymousmatchRegInt);
		Matcher matcher = patternVarName.matcher(str);
		while (matcher.find()) {

			String matchBase = matcher.group();// 获取匹配行
			String arrsStr = matcher.group(1).trim();// 获取匹配yuanzn
			String[] arr = arr = arrsStr.split(",");// 得到数组。
			String decodeResult = escapeStr(getDeCodeValue(arr));
			return decodeResult;
		}
		if (debug.getValue() >= DebugLevel.WRAIN.getValue()) {
			System.err.println("加密的无法解析匿名数组 不匹配串" + str);
		}
		return null;
	}

	// * 0次或者1次? 0次或者1次
	public static final String AnonymousmatchRegInt = "new\\s*int\\s*\\[\\s*\\]\\s*?\\{\\s*(.*?)\\s*\\}.*?";

	private static boolean isIntArr(String str) {
		boolean result;
		if (str == null || (sConstantsClass != null && str.startsWith((sConstantsClass)))) {

			result = false;
		} else {

			Pattern patternVarName = Pattern.compile(AnonymousmatchRegInt);
			patternVarName.matcher(str);
			Matcher matcherMethod = patternVarName.matcher(str);
			result = matcherMethod.matches();
		}
		if (debug.getValue() >= DebugLevel.All.getValue()) {
			System.err.println("加密的字符串是数组=" + result + ",value:" + str);
		}
		return result;
	}

	public static OperaInfo readTxtFileEncode(String filePath, String simpleClassName) {
		OperaInfo info = new OperaInfo();
		int dowhileCount = 0;
		int ignoreCount = 0;
		int existVarCount = 0;
		/**
		 * 如果已经存在了则不再进行添加
		 */

		// System.out.println();

		String patternString = "\"(.*?)\"";
		StringBuilder sb = new StringBuilder(50);
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {

					// 匹配类似velocity规则的字符串

					// 生成匹配模式的正则表达式

					// 两个方法：appendReplacement, appendTail
					if (isIgNoreDecodeText(lineTxt)) {// 一般情况下这是一个私有的字段
														// 忽略
						if (debug.getValue() >= DebugLevel.All.getValue()) {
							// 那么问题来了，\n字符串如何处理换行问题呢、
							System.out.println("readTxtFileEncode->忽略包含日志行 忽略:" + lineTxt);
						}
						ignoreCount++;
					} else {
						Pattern pattern = Pattern.compile(patternString);

						Matcher matcher = pattern.matcher(lineTxt);
						while (matcher.find()) {
							String matchBase = matcher.group();
							System.err.println("match:" + matchBase);// 这里匹配返回的包含引号
							String temp = matcher.group(1);// 不包含双引号
							if (temp != null && temp != "" && temp != "," && temp != "&" && temp.trim().length() > 0) {
								UseVarQuote currentEncrypt = useVarQuote;
								if (UseVarQuote.random == useVarQuote) {// 如果是随机，那么就是字节数组加密直接引用，或者是常量字节抽取引用，
									currentEncrypt = randomUseVarQuote(useVarQuote) ? UseVarQuote.yes : UseVarQuote.no;
								}
								if (useVarQuote == UseVarQuote.yes) {
									boolean isChinese = checkChinese(temp);

									if (isChinese || checkZimu(temp) || checkNumber(temp)) {
										String baseVar = getVarBaseName(temp, isChinese);
										if (!sEncodeMap.containsKey(baseVar)) {
											existVarCount++;
											// 生成注释声明
											/*
											 * 下面这句话实现变量申明字符串
											 */
											// filedNameMap.put(baseVar, "");
											System.err.println("变量" + baseVar + "不存在,无法进行加密 忽略替换" + temp);
											continue;
											// throw new
											// RuntimeException("找不到对于的变量名:" +
											// baseVar + "，确保之前已经加载");
										} else {
											System.out.println("找到变量:" + baseVar);
										}

										if (debug.getValue() >= DebugLevel.MIDDLE.getValue()) {

											System.out.println("lineTextBefore:" + lineTxt + ",varName:" + baseVar);
										}

										String replaceValue = getDecodeMethodNameCall(sConstantsClass + "." + baseVar);
										lineTxt = lineTxt.replace(matchBase, replaceValue);// 把"ffd"替换为
										dowhileCount++;
										// 解密方法.sss(常量类.变量名)
										if (debug.getValue() >= DebugLevel.All.getValue()) {
											System.out.println("lineTextAfter:" + lineTxt);
										}
									} else {
										// varNameSb.append("/**忽略" + temp + "
										// **/\n\n");
									}

								} else if (useVarQuote == UseVarQuote.no) {// 直接插入直接数组到字符串所在位置

									String replaceValue = getDecodeMethodNameCall(stringToCharCodeJava(temp));
									if (debug.getValue() >= DebugLevel.MIDDLE.getValue()) {
										System.out
												.println("lineTextBefore:" + lineTxt + "不使用变量名,直接插入的值是" + replaceValue);
									}
									lineTxt = lineTxt.replace(matchBase, replaceValue);// 把"ffd"替换为
									dowhileCount++;
									// 解密方法.sss(常量类.变量名)
									if (debug.getValue() >= DebugLevel.All.getValue()) {
										System.out.println("lineTextAfter:" + lineTxt);
									}
								} else if (useVarQuote == UseVarQuote.str) {
									// TODO 比较复杂，暂时不知道他是怎么加密的，只知道解密
									String replaceValue = StrEngC0202ooo.encodeStr(stringToCharCodeJava(temp));
									if (debug.getValue() >= DebugLevel.All.getValue()) {
										System.out.println(
												"lineTextBefore:" + lineTxt + "使用字符串加密打法插入的字符串是" + replaceValue);
									}
									lineTxt = lineTxt.replace(matchBase, replaceValue);// 把"ffd"替换为
									dowhileCount++;
									// 解密方法.sss(常量类.变量名)
									if (debug.getValue() >= DebugLevel.All.getValue()) {
										System.out.println("lineTextAfter:" + lineTxt);
									}
								}

							}

						}
					}

					sb.append("" + lineTxt + "\n");

				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (

		Exception e)

		{
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		if (dowhileCount > 0)

		{
			info.setResult(true);
		} else

		{
			info.setResult(false);

		}
		info.setMessage("忽略总数" + ignoreCount + ",进行加密的总数:" + dowhileCount + ",其中有" + existVarCount + "个常量重复,被共用");
		// 处理没有EncryptUtils导入的情况。这样会影响编译速度批量处理的时候比较麻烦.

		String result = sb.toString();
		if (!isExistImportEncryptUtilPackage(result)) {
			result = insertPackAage(simpleClassName, result, getEncryptImportWordByPackageName());
		}
		/*
		 * if (!isExistImportConstantPackage(result)) { result =
		 * insertPackAage(simpleClassName, result,
		 * getConstantsImportWordByPackageName()); }
		 */

		if (result == null) {
			result = sb.toString();
		}

		info.setDoWhileResultText(result);
		return info;
	}

	public static boolean randomUseVarQuote(UseVarQuote quote) {
		if (quote == UseVarQuote.yes) {
			return true;
		} else if (quote == UseVarQuote.no) {// 直接new 字节数组j1iami
			return false;
		} else if (quote == UseVarQuote.str) {// 字符串乱码加密
			return false;
		}

		return new Random().nextInt(1) == 0 ? true : false;

	}

	/**
	 * 
	 * @return
	 */
	public static String getImportWordByPackageName(String packageName) {
		return "import " + packageName + ";";
	}

	public static String getEncryptImportWordByPackageName() {
		return getImportWordByPackageName(encryptAtPackage + "." + sDecodSimpleClass);
	}

	public static String getConstantsImportWordByPackageName() {
		return getImportWordByPackageName(sConstantsAtPackage + "." + sConstantsClass);
	}

	/**
	 * 如果没有定义也会返回 已经存在 了。
	 * 
	 * @param packageName
	 *            完整包名
	 * @param content
	 * @return
	 */
	public static boolean isExistImportPackage(String packageName, String content) {
		if (content.contains("import " + packageName)) {
			return true;
		} else {// cn.qssq666.EncryptUtil
			return false;
		}
	}

	public static boolean isExistImportEncryptUtilPackage(String content) {
		return encryptAtPackage == null || encryptAtPackage == ""
				|| isExistImportPackage(encryptAtPackage + "." + sDecodSimpleClass, content);
	}

	public static boolean isExistImportConstantPackage(String content) {
		return sConstantsAtPackage == null || sConstantsAtPackage == ""
				|| isExistImportPackage(sConstantsAtPackage + "." + sConstantsClass, content);
	}

	/**
	 * * 插入 一段 字符串到指定文本的 class之后的成员变量 可以用于
	 * 
	 * @param className
	 * @param javaSrcFileContent
	 * @param insertContent
	 * @return
	 */
	public static String insertFieldAtClassAfter(String simpleClassName, String javaSrcFileContent,
			String insertContent) {
		// TODO Auto-generated method stub

		StringBuilder s1 = new StringBuilder(javaSrcFileContent); // 原字符串

		String match = ".*?\\s*class " + simpleClassName + ".*?\\{";
		Pattern p = Pattern.compile(match); // 插入位置
		Matcher m = p.matcher(s1.toString());
		if (m.find()) {
			s1.insert((m.end() + 1), insertContent); // 插入字符串
		} else {
			String errorMsg = "无法匹配匹配无法插入到class之前。,className:" + simpleClassName + ",源文件内容:" + javaSrcFileContent;
			System.err.println(errorMsg);
			throw new RuntimeException("无法匹配:" + errorMsg);
		}

		// System.out.println(s1.toString());
		return s1.toString();
	}

	public static String insertFieldAtConstantClassAfter(String javaSrcFileContent, String insertContent) {
		return insertFieldAtClassAfter(sConstantsClass, javaSrcFileContent, insertContent);
	}

	/**
	 * 插入到之前
	 * 
	 * @param javaSrcFileContent
	 * @param insertContent
	 * @return
	 */
	public static String insertFieldAtConstantClassBefore(String javaSrcFileContent, String insertContent) {
		return insertFieldAtClassBefore(sConstantsClass, javaSrcFileContent, insertContent);
	}

	public static String insertFieldAtClassBefore(String simpleClassName, String javaSrcFileContent,
			String insertContent) {
		// TODO Auto-generated method stub

		StringBuilder s1 = new StringBuilder(javaSrcFileContent); // 原字符串

		String match = ".*?\\s*class " + simpleClassName + ".*?\\{";
		Pattern p = Pattern.compile(match); // 插入位置
		Matcher m = p.matcher(s1.toString());
		if (m.find()) {
			s1.insert((m.start() - 1), insertContent); // 插入字符串
		} else {
			String errorMsg = "无法匹配匹配无法插入到class之前 可能是接口 枚举类。,className:" + simpleClassName + ",源文件内容:"
					+ javaSrcFileContent;
			System.err.println(errorMsg);
		}

		// System.out.println(s1.toString());
		return s1.toString();
	}

	/**
	 * 第一行是导入包 个关键字 所以要忽略.
	 * 
	 * @param simpleClassName
	 *            这里没有用上
	 * @param javaSrcFileContent
	 * @param insertContent
	 * @return
	 */
	public static String insertPackAage(String simpleClassName, String javaSrcFileContent, String insertContent) {
		// TODO Auto-generated method stub

		StringBuilder s1 = new StringBuilder(javaSrcFileContent); // 原字符串

		String match = ".*?\\s*package.*?;";
		Pattern p = Pattern.compile(match); // 插入位置
		Matcher m = p.matcher(s1.toString());
		if (m.find()) {
			s1.insert((m.end() + 1), insertContent); // 插入字符串
		} else {
			String errorMsg = "无法插入包名到第二行。,className:" + simpleClassName + ",源文件内容:" + javaSrcFileContent;
			System.err.println(errorMsg);
		}

		// System.out.println(s1.toString());
		return s1.toString();
	}

	private static String readString2(String strPath)

	{

		StringBuilder sb = new StringBuilder("");

		File file = new File(strPath);

		try {

			FileReader fr = new FileReader(file);

			int ch = 0;

			while ((ch = fr.read()) != -1)

			{

				sb.append((char) ch);

			}

			fr.close();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

			System.out.println("File reader锟斤拷锟斤拷");

		}
		if (true) {
			return sb.toString();
		}
		try {

			return new String(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return null;

	}

	public static int[] charArrayToEncodeIntArray(char[] chars) {

		if (currentEncryptType == EncryptType.NEWENCRYPT) {
			int[] intArray = new int[chars.length + 1];
			int randomEncryptValue = Math.abs(new Random().nextInt(6948));
			intArray[0] = randomEncryptValue;
			for (int i = 1; i < intArray.length; i++) {
				int curentChars = chars[i - 1];// 加密的字符串但是还是要从0开始的，所以还是感觉放在后面更容易一些。但是
												// 解密还不是要麻烦一些。
				intArray[i] = getEncodeIntValue(curentChars, randomEncryptValue);
				System.out.println("加密前:" + ((int) curentChars) + ",加密后:" + intArray[i]);

			}
			return intArray;

		} else if (currentEncryptType == EncryptType.OLDENCRYPT) {
			int[] intArray = new int[chars.length];
			for (int i = 0; i < chars.length; i++) {
				intArray[i] = chars[i];
				intArray[i] = getEncodeIntValue(intArray[i], 69);
				System.out.println("加密前:" + ((int) chars[i]) + ",加密后:" + intArray[i]);
			}
			return intArray;

		} else if (currentEncryptType == EncryptType.OTHERENCRYPT) {// 只转换不加密
			int[] intArray = new int[chars.length];
			for (int i = 0; i < chars.length; i++) {
				intArray[i] = chars[i];
				System.out.println("加密前:" + ((int) chars[i]) + ",加密后:" + intArray[i]);
			}

			return intArray;

		} else {
			throw new RuntimeException("请手动实现加密解密");
		}

	}

	/**
	 * 解密
	 * 
	 * @param arr
	 * @return
	 */
	private static String getDeCodeValue(String[] arr) {
		if (currentEncryptType == EncryptType.NEWENCRYPT) {
			char[] chars = new char[arr.length - 1];
			String temp = arr[0].replace(" ", "");
			int decyprtValue;
			try {
				if (temp == null || temp.equals("")) {
					return "";
				}
				decyprtValue = Integer.valueOf(temp);
			} catch (Exception e) {
				throw new RuntimeException("错误原因:" + e.getMessage() + ",arr:" + Arrays.toString(arr));
			}

			for (int i = 1; i < arr.length; i++) {
				arr[i] = arr[i].replace(" ", "");
				int tempInt = Integer.parseInt(arr[i]);
				tempInt = getDecodeIntValue(tempInt, decyprtValue);
				chars[i - 1] = (char) tempInt;
			}
			String decodeResult = String.valueOf(chars);
			if (decodeResult != null) {
				decodeResult = decodeResult.replaceAll("\\\\n", "\n");
			}
			return decodeResult;
		} else if (currentEncryptType == EncryptType.OTHERENCRYPT) {// 不进行与运算
			char[] chars = new char[arr.length];
			for (int i = 0; i < chars.length; i++) {
				arr[i] = arr[i].replace(" ", "");
				int tempInt = Integer.parseInt(arr[i]);
				tempInt = getDecodeIntValue(tempInt, 0);
				chars[i] = (char) tempInt;
			}
			String decodeResult = String.valueOf(chars);
			return decodeResult;
		} else {

			throw new RuntimeException("暂时不支持其他类型解密 请手动实现");
		}
	}

	private static int maxArrLength = 500;
	// private static int maxArrLength = 100;

	public static int getEncodeIntValue(int value, int encryptValue) {
		if (currentEncryptType == EncryptType.NEWENCRYPT || currentEncryptType == EncryptType.OLDENCRYPT) {
			return (value << 2) + encryptValue;// 线 与运算再叠加
		}
		return value;
	}

	public static int getDecodeIntValue(int value, int decryptValue) {
		if (currentEncryptType == EncryptType.NEWENCRYPT || currentEncryptType == EncryptType.OLDENCRYPT) {
			return (value - decryptValue) >> 2;// 线 与运算再叠加
		}
		return value;// 移除代价再与运算
	}

	public static String escapeExprSpecialWord(String keyword) {
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}

		return keyword;
	}

	// private int[] a=new int[];
	/**
	 * 字符串转int[]数组 字符串java 该方法返回的是 赋值的部分 也就是=之后的 对于ints arr加密格式大概是这样 new int[] {
	 * } 对于直接字符串乱加密则是="XXDFDFDDFD"
	 * 
	 * @param str
	 *            要加密的字符串 返回一个 new int[]{}加密数组不包含逗号
	 * @return
	 */
	public static String stringToCharCodeJava(String str) {

		if (str.indexOf("brbr") != -1) {
			str = str.replaceAll(sBrSign, "\n");// 处理特殊符号转义 如果直接\n那么会读入多行不是吗？
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.err.println("进行编码加密brbr转换为换行符,转换结果:" + str);
			}
		}

		/*
		 * if (str.indexOf("\n") != -1) { str = str.replaceAll("\n",
		 * String.valueOf(new char[]{10}));// 处理特殊符号转义 如果直接\n那么会读入多行不是吗？ if
		 * (debug) { System.err.println("进行编码加密换行转,转换结果:" + str); } }
		 */
		str = unescapeStr(str);// java字符串需要反转义如 变成真正的换行符
		/*
		 * if (str.indexOf("\\n") != -1) { str = str.replaceAll("\\n",
		 * String.valueOf(new char[]{10}));// 处理特殊符号转义 如果直接\n那么会读入多行不是吗？ if
		 * (debug) { System.err.println("进行编码加密换行转,转换结果:" + str); } }
		 * 
		 * 
		 * if(str.contains("\\")){ str = str.replaceAll("\\", String.valueOf(new
		 * char[]{92}));// 处理特殊符号转义 如果直接\n那么会读入多行不是吗？ if(debug){
		 * System.out.println("包含特殊转义\\字符正在处理 当前包含特殊字符的是:"+str); } }
		 * if(str.contains("\\\\")){ str = str.replaceAll("\\\\", "\\");//
		 * 处理特殊符号转义 如果直接\n那么会读入多行不是吗？ if(debug){ System.out.println(
		 * "包含特殊转义\\\\字符正在处理 当前包含特殊字符的是:"+str); } }
		 */
		// System.err.println("正在加密中。。。处理当前行转义:" + str);
		if (isIntArrEncryptMode) {
			int[] temp = charArrayToEncodeIntArray(str.toCharArray());

			return getIntSvARValue(temp, str);
		} else {
			throw new RuntimeException("直接字符串加密模式尚未完善");
		}
	}

	/**
	 * ; 给我一个int数组返回一个字符串 int申明 但是变量接受不包含 return new int[] { ints的摆列 }
	 * 如果要触发本代码，需要先删除常量文件之前缓存的文件，我自己写的代码自己都搞不懂是啥逻辑了，毕竟思想没有变成图形。有点乱。
	 * 
	 * @param ints
	 * @param str
	 *            只是用来打印之前的值 没其他作用
	 * @return
	 */
	public static String getIntSvARValue(int[] ints, String str) {
		char[] chars = new char[ints.length];
		if (chars.length > maxArrLength) {
			if (maxArrWarning) {
				System.err.println("wraning :he array code is too long to compile ,current length:" + chars.length
						+ ",str is:" + str);
			} else {
				throw new RuntimeException("无法处理加密数组 数组过长，最多" + maxArrLength
						+ "个数组，请适当切割字符串,数组太长会导致报错【Error:(34, 29) 错误: 代码过长 】异常数组长度" + ints.length + ",值:" + str);

			}

		} else {
			/*
			 * System.out.println("chars.length:"+chars.length); if(true){ throw
			 * new RuntimeException("无法处理加密数组 数组过长，最多" + maxArrLength +
			 * "个数组，请适当切割字符串,数组太长会导致报错【Error:(34, 29) 错误: 代码过长 】异常数组长度" +
			 * ints.length + ",值:" + str); }
			 */

		}
		StringBuilder sbChars = new StringBuilder(50);
		sbChars.append("new int[]{");
		for (int i = 0; i < ints.length; i++) {
			chars[i] = (char) ints[i];
			sbChars.append("" + ints[i]);
			if (i != ints.length - 1) {
				sbChars.append(",");
			} else {
				sbChars.append("}");
			}
		}

		return sbChars.toString();
	}

	static boolean isIgNoreDecodeText(String text) {// 对于静态的首先对于jni要处理加载顺序问题就是一个很头疼的问题，其次
		return isIgNoreDecodeText(text, false);
	}

	/**
	 * 
	 * @param text
	 * @param isdecodeConstants
	 *            貌似是一个写错的东西，
	 * @return
	 */
	static boolean isIgNoreDecodeText(String text, boolean isdecodeConstants) {// 对于静态的首先对于jni要处理加载顺序问题就是一个很头疼的问题，其次
		if (isAnnotationLine(text)) {
			if (debug.getValue() >= DebugLevel.WRAIN.getValue()) {
				System.out.println("忽略注解(因为注解必须是常量,不能加密), " + text);
			}
			return true;
		}
		if (text.equals("\\n")) {

			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.err.println("忽略换行符");
				return true;
				// throw new RuntimeException("发现换行符" + text);
			}
		}
		if (text.indexOf("case ") != -1) {// case里面不能变成那个。 不过主要看模式
			return true;
		}
		if (sIgnoreKeyWords != null) {
			for (int i = 0; i < sIgnoreKeyWords.length; i++) {
				String currentKey = sIgnoreKeyWords[i];
				if (text.contains(currentKey)) {
					return true;
				}
			}
		}
		String temp = text.replaceAll(" ", "");
		temp = temp.replaceAll("	", "");

		if (temp.toLowerCase().contains("ignore_exclude")) {
			return false;// 表示必须加密的，
		}
		if (temp.toLowerCase().contains("ignore_include")) {
			return true;// 表示无需加密
		}

		if (temp.toLowerCase().contains("ignore_start") || temp.toLowerCase().contains("regoin_start")
				|| temp.toLowerCase().contains("exclude_start")) {
			enteIgnoreBlock = true;
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("进入忽略忽略区域, " + text);
			}
			return true;
		}

		if (temp.toLowerCase().contains("ignore_end") || temp.toLowerCase().contains("regoin_end")
				|| temp.toLowerCase().contains("exclude_end")) {
			enteIgnoreBlock = false;
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("进入忽略忽略区域, " + text);
			}
			return true;
		}
		if (enteIgnoreBlock) {
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("属于自定义忽略航快区域, " + text);
			}
			return true;
		}

		if (temp.startsWith("/*") && temp.indexOf("*/") != -1) {
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行块注释 " + text);
			}
			return true;
		}

		if (temp.startsWith("//")) {
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释//" + text);
			}
			return true;// 注释行忽略，
		}
		if (temp.startsWith("/*")) {
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释,start/" + text);
			}
			enteBlockComment = true;
			return true;
		} else if (temp.indexOf("*/") != -1) {
			enteBlockComment = false;
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释,end " + text);
			}
			return true;
		}

		if (enteBlockComment) {
			if (debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("属于行block区域, " + text);
			}
			return true;
		}
		if (!encryptStaticConstants) {
			if (text.contains("final") && text.contains("static") && !text.contains("TAG")) {
				return true;
			}
		}
		if (isdecodeConstants) {

			return text.toUpperCase().contains(sIGNORE_DECODE) || text.toUpperCase().contains("HOOKLOG")
					|| text.toUpperCase().contains("DebugUtils.Log".toUpperCase())
					|| text.toUpperCase().contains("QSSQUtils.Log".toUpperCase());
		}

		// QSSQUtils.Log(
		return text.toUpperCase().contains(sIGNORE_DECODE) || text.toUpperCase().contains("HOOKLOG")
				|| text.toUpperCase().contains("QSSQUtils.Log".toUpperCase());
	}

	/**
	 * 忽略包含static final的常量
	 */
	public static boolean encryptStaticConstants = true;
	public static boolean enteBlockComment = false;
	public static boolean enteIgnoreBlock = false;
	/**
	 * 加密Utils所在包名
	 */
	public static String encryptAtPackage = null;
	private static String[] sIgnoreKeyWords;

	public interface CustromParseProvider {
		String requestDecode(String str);

		String requestEncode(String str);
	}

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

	public static class OperaInfo {
		OperaInfo() {

		}

		@Override
		public String toString() {
			return "OpearaInfo [doWhileResultText=" + doWhileResultText + ", message=" + message + ", result=" + result
					+ ", code=" + code + "]";
		}

		String doWhileResultText;

		public String getDoWhileResultText() {
			return doWhileResultText;
		}

		public void setDoWhileResultText(String doWhileResultText) {
			this.doWhileResultText = doWhileResultText;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isResult() {
			return result;
		}

		public void setResult(boolean result) {
			this.result = result;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		String message;
		boolean result;
		int code;
	}

}
