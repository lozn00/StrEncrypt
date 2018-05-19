
public class TextUtils {
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	public static void main(String[] args) {
		/*
		 * short reuslt=0b0011&~0b1100;
		 * System.out.println(Integer.toBinaryString(reuslt));
		 * 
		 * reuslt=0b0011&0b1100;
		 * System.out.println(Integer.toBinaryString(reuslt)); //耳机 short
		 * value=~0b1; System.out.println(Integer.toBinaryString(value));
		 * System.out.println(value);
		 */

	 int MODE_SHIFT = 30;
	 int MODE_MASK = 0x3 << MODE_SHIFT;
		// 0x3 左移动30位=01 00 0000 0000 0000 0000 000

	int UNSPECIFIED = 0 << MODE_SHIFT;

		 int EXACTLY = 1 << MODE_SHIFT;

	int AT_MOST = 2 << MODE_SHIFT;
	System.out.println("UNSPECIFIED OX:"+Integer.toBinaryString(UNSPECIFIED));
	System.out.println("M OX:"+Integer.toBinaryString(MODE_MASK));
	System.out.println("E OX:"+Integer.toBinaryString(EXACTLY));//MATCH_PARETN或者精确值
	System.out.println("A OX:"+Integer.toBinaryString(AT_MOST));//WRAP_CONTENT

		// System.out.println(String.format("与异或结果:%o",reuslt));
	}

}
