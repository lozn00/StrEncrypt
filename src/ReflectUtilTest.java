import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtilTest {

	public static Method getMethodFromAll(Class classs, String methodName, Class<?>... parameters)

	{

		while (classs != null && classs != Object.class)

			try {
				Method declaredMethod = classs.getDeclaredMethod(methodName, parameters);
				return declaredMethod;
			} catch (NoSuchMethodException e) {

				e.printStackTrace();
				classs = classs.getSuperclass();
			}

		return null;

	}
	
	public void print(String value){
		
	}
	public void print1(String value,String value1){
		
	}
	
	public static void main(String[] args) {
		
		
		
	Method method=	getMethodFromAll(ReflectUtilTest.class, "print", String.class);
	try {
		method.invoke(null, args);
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	/*
	 * // // java.lang.NoSuchMethodException: a [class android.content.Context,
	 * class java.lang.String] // at
	 * java.lang.Class.getConstructorOrMethod(Class.java:472) // at
	 * java.lang.Class.getMethod(Class.java:857) // at
	 * cn.qssq666.insertqqmodule.qssqproguard.a2.Tencent.MessageForQQWalletMsg1(
	 * Tencent.java:1053) // at
	 * cn.qssq666.redpacket.include.DoHookQQLogic$DoMessageRecordDoParseHook$1.
	 * afterHookedMethod(DoHookQQLogic.java:2795) // at
	 * de.robv.android.xposed.XposedBridge.handleHookedMethod(XposedBridge.java:
	 * 645) // at com.tencent.mobileqq.data.MessageForQQWalletMsg.doParse(Native
	 * Method) // at com.tencent.mobileqq.data.ChatMessage.parse(ProGuard:108)
	 * // at
	 * com.tencent.mobileqq.app.message.BaseMessageManager.a(ProGuard:1328) //
	 * at com.tencent.mobileqq.app.message.QQMessageFacade.a(ProGuard:1963) //
	 * at com.tencent.mobileqq.app.message.QQMessageFacade.a(ProGuard:1025) //
	 * at com.tencent.mobileqq.app.message.QQMessageFacade.a(ProGuard:835) // at
	 * com.tencent.mobileqq.troop.data.TroopMessageProcessor.a(ProGuard:887) //
	 * at com.tencent.mobileqq.troop.data.TroopMessageProcessor.a(ProGuard:115)
	 * // at com.tencent.mobileqq.app.MessageHandler.h(ProGuard:2799) // at
	 * com.tencent.mobileqq.app.MessageHandler.a(ProGuard:4313) // at
	 * com.tencent.mobileqq.service.MobileQQServiceBase.a(ProGuard:309) // at
	 * com.tencent.mobileqq.service.MobileQQService.a(ProGuard:675) // at
	 * com.tencent.mobileqq.app.QQAppInterface.a(ProGuard:9953) // at
	 * com.tencent.mobileqq.compatible.TempServlet.onReceive(ProGuard:52) // at
	 * mqq.app.MSFServlet.onReceive(MSFServlet.java:39) // at
	 * mqq.app.ServletContainer.notifyMSFServlet(ServletContainer.java:131) //
	 * at mqq.app.MainService.receiveMessageFromMSF(MainService.java:277) // at
	 * mqq.app.MainService.access$300(MainService.java:70) // at
	 * mqq.app.MainService$5.onRecvCmdPush(MainService.java:848) // at
	 * com.tencent.mobileqq.msf.sdk.MsfRespHandleUtil.handlePushMsg(
	 * MsfRespHandleUtil.java:187) // at
	 * mqq.app.MainService$2.run(MainService.java:476) // at
	 * java.lang.Thread.run(Thread.java:841)
	 */

}
