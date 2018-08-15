package tools;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import encrypt.DebugLevel;
import encrypt.EncryptConfig;
import encrypt.GlobalConfig;
import encrypt.TempFlag;

public class MyUtil {

	public static String getExtendsNameByFile(File file) {
		String expandsName = FilenameUtils.getExtension(file.getAbsolutePath());
		return expandsName;
	}

	public static String getClassNameByFile(File file) {
		String className = FilenameUtils.getBaseName(file.getAbsolutePath());
		return className;
	}
	public	static boolean isIgNoreDecodeText(EncryptConfig encryptConfig,String[] sIgnoreKeyWords,String text) {// 对于静态的首先对于jni要处理加载顺序问题就是一个很头疼的问题，其次
		return isIgNoreDecodeText(encryptConfig,sIgnoreKeyWords,text, false);
	}

	/**
	 * 
	 * @return
	 */
	public static String getImportWordByPackageName(String packageName) {
		return "import " + packageName + ";";
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
		return "////【插入分割线[" + className + "]:" + MyUtil.getCurrentTime() + "】\n";
	}
	// 
	public static String getVarName(String name, boolean isChinese) {
		return "public final static int[] " + getVarBaseName(name, isChinese) + "= ";
	}

	/**
	 * public final static int[] " + varName + "=
	 * 
	 * @param varName
	 * @return
	 */
	public static String getVarName(String varName) {
		return "public final   static int[] " + varName + "= ";
	}

	/**
	 * 之前的直接分割会导致变量名冲突了。不能保证唯一 。比如 forui切换器控件。
	 * 
	 * @param conent
	 * @param isChinese
	 * @return
	 */
	public static String getVarBaseName(String conent, boolean isChinese) {

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
	
	static HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper();

	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-mm-dd-HH:mm:dd");
		return format.format(date);
	}
	public static boolean isIgnoreFolder(String[] sIgnoresFolder,File file) {
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

	/**
	 * 
	 * @param text
	 * @param isdecodeConstants
	 *            貌似是一个写错的东西，
	 * @return
	 */
	public	static boolean isIgNoreDecodeText(EncryptConfig encryptConfig,String[] sIgnoreKeyWords,String text, boolean isdecodeConstants) {// 对于静态的首先对于jni要处理加载顺序问题就是一个很头疼的问题，其次
		if (isAnnotationLine(text)) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.WRAIN.getValue()) {
				System.out.println("忽略注解(因为注解必须是常量,不能加密), " + text);
			}
			return true;
		}
		if (text.equals("\\n")) {

			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
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
			TempFlag.enteIgnoreBlock = true;
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("进入忽略忽略区域, " + text);
			}
			return true;
		}

		if (temp.toLowerCase().contains("ignore_end") || temp.toLowerCase().contains("regoin_end")
				|| temp.toLowerCase().contains("exclude_end")) {
			TempFlag.enteIgnoreBlock = false;
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("进入忽略忽略区域, " + text);
			}
			return true;
		}
		if (TempFlag.enteIgnoreBlock) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("属于自定义忽略航快区域, " + text);
			}
			return true;
		}

		if (temp.startsWith("/*") && temp.indexOf("*/") != -1) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行块注释 " + text);
			}
			return true;
		}

		if (temp.startsWith("//")) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释//" + text);
			}
			return true;// 注释行忽略，
		}
		if (temp.startsWith("/*")) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释,start/" + text);
			}
			TempFlag.enteBlockComment = true;
			return true;
		} else if (temp.indexOf("*/") != -1) {
			TempFlag.enteBlockComment = false;
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("忽略行注释,end " + text);
			}
			return true;
		}

		if (TempFlag.enteBlockComment) {
			if (GlobalConfig.debug.getValue() >= DebugLevel.All.getValue()) {
				System.out.println("属于行block区域, " + text);
			}
			return true;
		}
		if (!encryptConfig.encryptStaticConstants) {
			if (text.contains("final") && text.contains("static") && !text.contains("TAG")) {
				return true;
			}
		}
		if (isdecodeConstants) {

			return text.toUpperCase().contains(GlobalConfig.sIGNORE_DECODE) || text.toUpperCase().contains("HOOKLOG")
					|| text.toUpperCase().contains("DebugUtils.Log".toUpperCase())
					|| text.toUpperCase().contains("QSSQUtils.Log".toUpperCase());
		}

		// QSSQUtils.Log(
		return text.toUpperCase().contains(GlobalConfig.sIGNORE_DECODE) || text.toUpperCase().contains("HOOKLOG")
				|| text.toUpperCase().contains("QSSQUtils.Log".toUpperCase());
	}

	public static boolean checkZimu(String str) {
	
		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
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
	public static boolean isIgnoreFiles(EncryptConfig encryptConfig,File file) {
		if (encryptConfig.sIgnoresFileNames != null) {
			for (int i = 0; i < encryptConfig.sIgnoresFileNames.length; i++) {
				String current = encryptConfig.sIgnoresFileNames[i];
				if (file.getName().equals(current)) {
					return true;
				}
			}
		}
		return false;
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
	
	

	public static String escapeExprSpecialWord(String keyword) {
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}

		return keyword;
	}

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



}
