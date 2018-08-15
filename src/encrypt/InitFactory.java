package encrypt;

import java.util.ArrayList;
import java.util.List;

import interfaces.CustromParseProvider;

public class InitFactory {
	/**
	 * 获取要加密或者解密的文件集合
	 * 
	 * @return
	 */
	public static EncryptConfig genereateEncryptConfigByModuleType(MODULEETYPE moduleType) {
		String temp = "";
		EncryptConfig encryptConfig = new EncryptConfig();
		if (moduleType == MODULEETYPE.PLUGIN) {// 加密情插件
			encryptConfig.sConstantsClass = "Constants";
			encryptConfig.sDecodSimpleClass = "EncryptUtilX";
			encryptConfig.sIgnoresFileNames = new String[] {};
			encryptConfig.sConstantsAtPackage = "cn.qssq666.redpacket";
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.encryptAtPackage = "cn.qssq666";

			encryptConfig.sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\app\\src\\main\\java\\cn\\qssq666\\redpacket\\Constants.java";
			encryptConfig.currentEncryptType = EncryptType.OTHERENCRYPT;
			temp = "G:\\newproject\\qqrepacket_pro\\app\\src\\main\\java\\cn\\qssq666\\redpacket\\";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.TEST_OTHER_ROBOT) {

			encryptConfig.sConstantsClass = "noconstants";
			encryptConfig.sDecodSimpleClass = "oO00oo";
			encryptConfig.sDecodeMethod = "o";
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.currentEncryptType = EncryptType.STR_SHOW_ENCRYPT;
			encryptConfig.sConstantsAtPackage = "noconstants";
			encryptConfig.useVarQuote = UseVarQuote.str;// 爆满了代码过长使用随机
			encryptConfig.sConstantClassPath = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			temp = "F:\\QQ_weichat\\crackstr\\classes.dex_src";
			// temp =
			// "F:\\QQ_weichat\\crackstr\\classes.dex_src\\com\\luomi\\lm\\morethreads\\db";
			encryptConfig.addDirOrFile(temp);

			// TEST_OTHER_ROBOT_PLUGIN
		} else if (moduleType == MODULEETYPE.TEST_OTHER_ROBOT_PLUGIN) {

			encryptConfig.sConstantsClass = "noconstants";
			encryptConfig.sDecodSimpleClass = "C0202ooo"; // C0202ooo.o
															// C0202ooo.o
			encryptConfig.sDecodeMethod = "o";// F:\QQ_weichat\crackstr\gruppluginclasses.dex_src
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.currentEncryptType = EncryptType.CUSTROM_CALL_QUTO;
			encryptConfig.sConstantsAtPackage = "noconstants";
			encryptConfig.custromParseProvider = new CustromParseProvider() {

				@Override
				public String requestEncode(String str) {
					// TODO Auto-generated method stub
					throw new RuntimeException("不支持加密");
				}

				@Override
				public String requestDecode(String str) {
					// TODO Auto-generated method stub
					//
					// return c.ooo.o(str);

					return null;
				}
			};
			// useVarQuote = false; C0202ooo.o
			encryptConfig.useVarQuote = UseVarQuote.str;// 爆满了代码过长使用随机
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			// sConstantClassPath =
			// "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			temp = "F:\\QQ_weichat\\crackstr\\gruppluginclasses.dex_src\\";
			// temp = "F:\\QQ_weichat\\crackstr\\classes.dexplugin_src";
			// temp =
			// "F:\\QQ_weichat\\crackstr\\classes.dex_src\\com\\luomi\\lm\\morethreads\\db";
			encryptConfig.addDirOrFile(temp);

			// TEST_OTHER_ROBOT_PLUGIN
		} else if (moduleType == MODULEETYPE.ROBOT) {// 机器人加密
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.sDecodeMethod = "a7";
			encryptConfig.sConstantsAtPackage = "cn.qssq666.robot.constants";
			encryptConfig.sDecodSimpleClass = "EncryptUtilN";
			encryptConfig.sConstantsClass = "EncryptConstants";
			// useVarQuote = false;
			encryptConfig.useVarQuote = UseVarQuote.no;// 爆满了代码过长使用随机
			encryptConfig.sConstantClassPath = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\constants\\EncryptConstants.java";
			// temp =
			// "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot\\MainActivity.java";
			temp = "G:\\newproject\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot";
			encryptConfig.addDirOrFile(temp);
			/*
			 * temp=
			 * "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666\\robot";
			 * encryptConfig.addDirOrFile(temp);
			 */

		} else if (11 == 1133) {// 共同qita微信常量修复qssq6666根目录文件夹

			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Cns.java";
			temp = "F:\\src\\git_project\\qq_qqrobot\\app\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.PUBLIC_FOLDER) {// 共同特性包名加密分享

			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\cn\\qssq666\\TempConstant.java";
			encryptConfig.sConstantsClass = "TempConstant";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.QQ) {// 加密内置Q文件夹
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantsClass = "Values1";
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a1\\"
					+ encryptConfig.sConstantsClass + ".java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a1";
			encryptConfig.addDirOrFile(temp);
		} else if (moduleType == MODULEETYPE.QQ_2) {// 加密内置Q文件夹
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.sDecodSimpleClass = "EncryptUtilN";
			// cn.qssq666
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantsClass = "Value2";
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a2\\"
					+ encryptConfig.sConstantsClass + ".java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\qssqproguard\\a2";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.QQ_MODULE) {// 加密内置Q文件夹
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.sDecodSimpleClass = "EncryptUtilN";
			// cn.qssq666
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantsClass = "Values1";
			encryptConfig.sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a1\\"
					+ encryptConfig.sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a1";
			encryptConfig.addDirOrFile(temp);

			temp = "G:\\newproject\\qqrepacket_pro\\testjniapp\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);
		} else if (moduleType == MODULEETYPE.QQ_2_MODULE) {// 加密内置Q文件夹
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantsClass = "Value2";
			encryptConfig.sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ encryptConfig.sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.Q_PLUS_DIR) {//
			// cn.qssq666

			/*
			 * sDecodeMethod = "a7"; currentEncryptType =
			 * EncryptType.NEWENCRYPT; sDecodSimpleClass = "EncryptUtilX";
			 * encryptAtPackage = "cn.qssq666"; useVarQuote = UseVarQuote.no;
			 * sConstantsClass = "Value2";
			 */

			encryptConfig.sDecodSimpleClass = "EncryptUtilX";
			encryptConfig.sIgnoresFileNames = new String[] {};
			encryptConfig.sConstantsAtPackage = "cn.qssq666.redpacket";
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.currentEncryptType = EncryptType.OTHERENCRYPT;

			encryptConfig.sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ encryptConfig.sConstantsClass + ".java";
			temp = "G:\\newproject\\qqrepacket_pro\\basehookmodule\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);
			temp = "G:\\newproject\\qqrepacket_pro\\qplus\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.QQ_MODULE_TEST) {// 加密内置Q文件夹
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			// cn.qssq666
			encryptConfig.encryptAtPackage = "cn.qssq666";
			temp = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\tool\\TestEncrypt.java";
			encryptConfig.sConstantsClass = "Value2";
			encryptConfig.sConstantClassPath = "G:\\newproject\\qqrepacket_pro\\insertqqmodule\\src\\main\\java\\cn\\qssq666\\insertqqmodule\\qssqproguard\\a2\\"
					+ encryptConfig.sConstantsClass + ".java";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.TEST) {
			encryptConfig.useVarQuote = UseVarQuote.no;
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.encryptAtPackage = "cn.qssq666";

			encryptConfig.sDecodeMethod = "a7";
			encryptConfig.sDecodSimpleClass = "EncryptUtilN";

			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\test\\ConstantValue.java";
			encryptConfig.sConstantsClass = "ConstantValue";
			temp = "G:\\newproject\\qqrepacket_pro\\testjniapp\\src\\main\\java\\cn\\qssq666";
			encryptConfig.addDirOrFile(temp);

		} else if (moduleType == MODULEETYPE.WECHAT) {// 插入微信Sscon加密 文件夹批量
			encryptConfig.currentEncryptType = EncryptType.NEWENCRYPT;
			encryptConfig.encryptAtPackage = "cn.qssq666";
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\ConstantValue.java";
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard";
			// temp =
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat";
			encryptConfig.addDirOrFile(temp);
			encryptConfig.addDirOrFile(
					"F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\EncryptUtil.java");
			// temp =
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Control.java";
			// encryptConfig.addDirOrFile(temp);
			/*
			 * temp=
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\UiUtils.java";
			 * encryptConfig.addDirOrFile(temp);
			 * 
			 * temp =
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\qqproguard\\wechat\\Cf.java";
			 * encryptConfig.addDirOrFile(temp);
			 */
		} else if (moduleType == MODULEETYPE.CRACK_SIGN) {
			encryptConfig.currentEncryptType = EncryptType.OTHERENCRYPT;
			encryptConfig.useVarQuote = UseVarQuote.no;

			encryptConfig.encryptAtPackage = null;
			GlobalConfig.debug = DebugLevel.All;
			encryptConfig.sConstantClassPath = null;
			encryptConfig.sConstantsClass = null;

			encryptConfig.sDecodSimpleClass = "LZUtils";
			// fetchDecodeMethod(sDecodeMethod)
			encryptConfig.sDecodeMethod = "hello";
			// fetchDecodeMethod = "LZUtils.hello";
			// encryptConfig.setAllowConstantsEmpty(true);//本来就没解密这个东西都不应该有
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\statistics\\ufo";
			// "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			encryptConfig.addDirOrFile(temp);
		} else if (moduleType == MODULEETYPE.MIAO) {
			encryptConfig.currentEncryptType = EncryptType.OTHERENCRYPT;
			encryptConfig.encryptAtPackage = null;
			GlobalConfig.debug = DebugLevel.All;
			encryptConfig.sConstantClassPath = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			encryptConfig.sConstantsClass = "fw";
			encryptConfig.sDecodSimpleClass = "fw";
			encryptConfig.sDecodeMethod = "sss";
			// fetchDecodeMethod = "fw.sss";
			// encryptConfig.setAllowConstantsEmpty(true);//本来就没解密这个东西都不应该有
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\ui\\base\\fw.java";
			encryptConfig.addDirOrFile(temp);
		} else if (11 == 111) {

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			encryptConfig.addDirOrFile(temp);
		} else if (1 == 9) {
			encryptConfig.sConstantClassPath = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\ConstantValue.java";
			temp = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\cn\\qssq666\\robot\\business\\RobotContentProvider.java";

			encryptConfig.addDirOrFile(temp);

			temp = "F:\\QQ_weichat\\smali_debug\\MyApplicationQQRobot\\qqrobot1\\app\\src\\main\\java\\cn\\qssq666\\robot\\AddWordActivity.java";
			encryptConfig.addDirOrFile(temp);

			/*
			 * temp =
			 * "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
			 * encryptConfig.addDirOrFile(temp);
			 */
		} else if (false == true) {
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\TestVar.java";
			encryptConfig.addDirOrFile(temp);
		} else if (11 == 111) {
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			encryptConfig.addDirOrFile(temp);

		} else {
			if (true) {
				throw new RuntimeException("TYPE ERROR " + moduleType);
			}
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\util\\robot\\RobotUtil.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\UiUtils.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\fw.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QQUnrecalledHook.java";
			encryptConfig.addDirOrFile(temp);

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\QTA.java";
			encryptConfig.addDirOrFile(temp);

			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\DS.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\Cf.java";

			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\qqplugin.java";
			encryptConfig.addDirOrFile(temp);
			temp = "F:\\src\\git_project\\insert_qq_or_wechat\\app\\src\\main\\java\\com\\tencent\\mobileqq\\zhengl\\InitConfig.java";
			encryptConfig.addDirOrFile(temp);
		}

		/* */

		return encryptConfig;
	}
	
	public  static List<EncryptConfig> getGroupByType(GroupType groupType){
		List<EncryptConfig> list=new ArrayList<>();
		if(groupType==GroupType.Q_PLUS_GROUP){
			list.add(InitFactory.genereateEncryptConfigByModuleType(MODULEETYPE.QQ_MODULE));
			list.add(InitFactory.genereateEncryptConfigByModuleType(MODULEETYPE.QQ_2_MODULE));
			list.add(InitFactory.genereateEncryptConfigByModuleType(MODULEETYPE.Q_PLUS_DIR));
			return list;
		}
		return null;
		
	}
}
