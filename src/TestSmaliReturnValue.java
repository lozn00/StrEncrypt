import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestSmaliReturnValue {

public static native Object ainimama(Object classloader, Object appInterface, Object context, String content, Object list,String frienduin,int istroop,String senderuin,byte flag, short startPosition, short textLen, long uin);


    public static native Object callyou(Object o, byte flag, short startPosition, short textLen, long uin);
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		System.out.println("test");
	/*	ArrayList list=new ArrayList<>();
		TestSmaliReturnValue returnValue=new TestSmaliReturnValue();
		returnValue.isEnablePreventRescinder(list);
		float value=getFontScale();*/
//		System.out.println("float:"+value);
		
		Field field = TestBean.class.getField("test");
		System.out.println("TestBean.field:"+field);
		try {
			Class classClass= Class.forName(Class.class.getName());
			Method getFieldMethod = classClass.getMethod("getField",String.class);
			Field findField= (Field) getFieldMethod.invoke(TestBean.class, "test");
			System.out.println("find TestBean.field:"+findField);
			
			Class[] obj=new Class[] {Class.class};
//		Method method=	classClass.getMethod("getMethod", obj);//自动被解开 永远拿不到自己java.lang.Class.getMethod(java.lang.Class)
			Method[] methods=classClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method=methods[i];
				System.out.println("拿到所有方法的"+i+","+method);
				if(method.getName().toLowerCase().contains("method")){
					
					System.err.println("拿到了Method反射类"+i+","+method);
				}
			}
//		System.out.println("拿到Method:"+method);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  public Method getMethod(String name, Class<?>... parameterTypes)
	  
	  {
		return null;
		  
	  }
    public static native void setField(Object classObj, String methdName, Object obj, Object methodValue);
    public static float getFontScale() {
        return 1.0f;
    }
	public void testAram(ArrayList list){
		
		TestSmaliReturnValue returnValue=new TestSmaliReturnValue();
		returnValue.isEnablePreventRescinder(list);
		
	}
	
    public static native void goout(Object mTroopHandler, Object mTroopManager, Object mTroopGagMgr,long group,List  qqList,boolean forver);
    public static native String shutdown(Object o,String group, String qq, long duration);

	public int getInt() {
		return 10000000;
	}

	public short getShort() {
		return 10000;
	}

	public double getDouble() {
		return 0.01;
	}
	public double getDoubleBig() {
		return 1111111111111f;
	}
	public double geFloat() {
		return 0.01;
	}


	public short getByte() {
		return 53;
	}

	public boolean getBoolean() {
		return true;
	}
	

	
	public Object getNull() {
		return null;
	}

	public String getString() {
		return "ddddddd";
	}
	
	public boolean isEnablePreventRescinder(ArrayList list){
		return false;
	}
	
}
