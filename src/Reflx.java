import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class Reflx {
	private static final String TAG = "Reflx";
	public boolean i;

	public static String method(boolean a) {
		return "value" + a;
	}

	public static String method(String a) {
		return "value" + a;
	}

	public static String method(int a) {
		return "value" + a;
	}

	public static Object voidMethod(String className, String method, String signName) {
		return null;

	}

	/**
	 * 获取类似反射或者smali jni调用的方法签名
	 * 
	 * @param method
	 * @return
	 */
	public static String getMethodSign(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		for (int i = 0; i < parameterTypes.length; i++) {
			buffer.append(getTypeSign(parameterTypes[i]));
			// if (i != parameterTypes.length - 1) {

		}
		buffer.append(")");
		buffer.append("" + getTypeSign(method.getReturnType()));
		return buffer.toString();
	}

	// Ljava/lang/String;)[B
	public static String getTypeSign(Class<?> typeClass) {
		if (boolean.class == typeClass) {
			return "Z";
		}
		if (boolean[].class == typeClass) {
			return "[Z";
		}
		if (void.class == typeClass) {
			return "V";
		}

		if (byte.class == typeClass) {
			return "B";
		}
		if (byte[].class == typeClass) {
			return "[B";
		}
		if (short.class == typeClass) {
			return "S";
		}
		if (short[].class == typeClass) {
			return "[S";
		}
		if (int.class == typeClass) {
			return "I";
		}
		if (int.class == typeClass) {
			return "[I";
		}
		if (float.class == typeClass) {
			return "F";
		}
		if (float[].class == typeClass) {
			return "[F";
		}
		if (double.class == typeClass) {
			return "D";
		}
		if (double[].class == typeClass) {
			return "[D";
		}
		if (long.class == typeClass) {
			return "J";
		}
		if (long.class == typeClass) {
			return "[J";
		}
		if (char.class == typeClass) {
			return "C";
		}

		if (char.class == typeClass) {
			return "[C";
		}
		String className = typeClass.getName();

		if (className == null) {
			return "";
		}

		// if()
		String temp = className.replace(".", "/");
		if (typeClass == Object[].class) {
			temp = "[" + temp;
		}
		return "L" + temp + ";";
	}

	public static Class<?>[] getParameterTypes(Object... args) {
		Class[] clazzes = new Class[args.length];

		for (int i = 0; i < args.length; ++i) {
			if (args[i] != null) {
				Class<? extends Object> temp = args[i].getClass();
				clazzes[i] = temp;
				System.out.println("pram[" + i + "]" + temp.getSimpleName());
			} else {
				clazzes[i] = null;

			}
		}

		return clazzes;
	}

	public static void main(String[] args) {
		Field[] fields = Reflx.class.getDeclaredFields();
		try {
			Method method = Reflx.class.getDeclaredMethod("method", int.class);
			getParameterTypes(1, false, Boolean.FALSE, Integer.valueOf(1));
			Method findMethod = Reflx.class.getDeclaredMethod("method", boolean.class);
			findMethod = Reflx.class.getDeclaredMethod("voidMethod", String.class, String.class, String.class);
			Class<?> temp = findMethod.getReturnType();// findMethod.invoke(null,
														// true)+
			System.out.println("分社成功," + temp + ",temp" + (temp == void.class) + ",methodSign:" + getMethodSign(findMethod));
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < fields.length; i++) {
			Field type = fields[i];
			Log.d(TAG, "BOOLEAN:" + type.getType().equals(boolean.class) + " ," + type.getClass().equals(Boolean.class) + "," + type.getClass().equals(Boolean.TYPE));
			// Reflx BOOLEAN:true ,false,false
		}
		Method[] methods = Reflx.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			Class<?>[] types = method.getParameterTypes();
			if (types.length > 0) {
				Class<?> type = types[0];
				String name = type.getSimpleName();
				if (name.equalsIgnoreCase(("boolean"))) {// Reflx method
															// arg[0]Type:boolean===BOOLEAN:
															// ,false,false,false,false,true
					Log.d(TAG, "method arg[0]Type:" + types[0] + "===" + "BOOLEAN:" + " ," + type.getClass().equals(Boolean.class) + "," + type.getClass().equals(boolean.class) + "," + type.getClass().equals(Boolean.TYPE) + "," + type.equals(Boolean.class) + "," + type.equals(boolean.class));
				} else if (name.equals("String")) {// Reflx method
													// arg[0]Type:class
													// java.lang.String===String:
													// ,false,false,true
					Log.d(TAG, "method arg[0]Type:" + types[0] + "===" + "String:" + " ," + type.getClass().equals(String.class) + "," + type.getClass().equals(String.class) + "," + type.equals(String.class));
				} else if (name.equals("int") || name.equals(Integer.class.getSimpleName())) {//
					/*
					 * Reflx method arg[0]Type:boolean===BOOLEAN:
					 * ,false,false,false,false,true Reflx method
					 * arg[0]Type:class java.lang.String===String:
					 * ,false,false,true
					 */
					Log.d(TAG, "method arg[0]Type:" + types[0] + "===" + "BOOLEAN:" + " ," + type.getClass().equals(Integer.class) + "," + type.getClass().equals(int.class) + "," + type.getClass().equals(Integer.TYPE) + "," + type.equals(Boolean.class) + "," + type.equals(int.class));
				}
				// 总结 字节码对象 直接 判断 是否等于 。没有包装不包装的。不相同就是不相同。。

			}
		}

	}
}
