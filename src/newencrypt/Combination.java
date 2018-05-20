package newencrypt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class Combination {
	public static ArrayList<String> list = new ArrayList<>();
	public static int maxInsertLength = 10;
	public static int minInsertLength = 9;
	
	/**
	 * 是否忽略大小写
	 */
	private static boolean mEnableIgnoreUpperCase;

	public static void comb(char[] chs) {
		int len = chs.length;
		int nbits = 1 << len;
		for (int i = 0; i < nbits; ++i) {
			int t;
			for (int j = 0; j < len; j++) {
				t = 1 << j;
				if ((t & i) != 0) { // 与运算，同为1时才会是1
					System.out.print(chs[j]);
				} else {

				}
			}
			System.out.println();
		}

	}

	public static void arrange(char[] chs, int start, int len) {
		if (start < -0) {
			return;
		}
		if (start == len - 1) {
			for (int i = 0; i < chs.length; ++i)
				System.out.print(chs[i]);
			System.out.println();
			return;
		}
		for (int i = start; i < len; i++) {
			char temp = chs[start];
			chs[start] = chs[i];
			chs[i] = temp;
			arrange(chs, start + 1, len);
			temp = chs[start];
			chs[start] = chs[i];
			chs[i] = temp;
		}
	}

	public static void traversalSubstrings(String s) {
		int len = s.length();
		int start = (1 << len) - 1;
		int end = (1 << len + 1) - 1;
		for (int i = start; i < end; i++) {
			System.out.println(treeIndex2Substring(s, i));
		}
		return;
	}

	public static String treeIndex2Substring(String s, int index) {
		StringBuilder sb = new StringBuilder();
		int srcIndex = 0;
		while (index > 0) {
			if (index % 2 != 0) {
				// 奇数，表示存在当前字符
				sb.append(s.charAt(srcIndex++));
			} else {
				// 偶数，表示不存在当前字符
				srcIndex++;
			}
			index = (index - 1) / 2;
		}
		return sb.toString();
	}

	public static void doLoopAppend(String[] chars, String hasAppend, int maxLength) {
		if (hasAppend.length() >= maxLength) {
			return;
		}
		// 1 2 3 11 12 13 2 22 23 3 31 32 33 111 112 113
		for (int i = 0; i < chars.length; i++) {
			String current = hasAppend + chars[i];
			addItem(current);
//			 System.out.println(""+current);
			 if(list.size()>0&&list.size()%5000==0){
				 System.out.println("已插入"+list.size()+"个");
			 }
			doLoopAppend(chars, current, maxLength);
		}
	}

	private static void doMyLoop() {

		String chars[] = new String[] { "1", "2", "3" };
		doLoopAppend(chars, "", 3);
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException {

		String chars[] = new String[] { "o", "O", "0" };
		doLoopAppend(chars, "", maxInsertLength);
		StringBuffer sb=new StringBuffer();
		
		System.out.println("共"+list.size()+"个");
		for (int i = 0; i < list.size(); i++) {
			String current = list.get(i);
			sb.append(current+"\n");
			System.out.println(current);

			// System.out.println((1 + i) + ":" + current);

		}
		File file=new File("hunxiao.txt");
		System.out.println("即将写入到"+file.getAbsolutePath());
		FileUtils.write(file, sb.toString(),"utf-8");
		System.out.println("写入完毕");
		/*
		 * if(true){ traversalSubstrings("abc"); return; }
		 */
		// char[] combination = new char[] { 'a', 'b', 'c' };

		/*
		 * combination = new String[] {
		 * "a","A","x","X","b","B","X","B","X","B","X","B","X"};
		 * loopPrintCombination(combination); combination = new String[]
		 * {"D","d","D","d"}; loopPrintCombination(combination);
		 */

		/*
		 * combination = new String[] {
		 * "j","J","L","l","I","j","J","J","L","L","J","L","L","l"};
		 * loopPrintCombination(combination); combination = new String[]
		 * {"j","J","L","l","I","L","J","L","L","J","j","i","I","L"};
		 * loopPrintCombination(combination);
		 */

		/*
		 * combination = new String[] { "0", "O", "o", "0", "0", "0", "O", "o",
		 * "0", "O", "o", "0", "O", "o", "0", "O", "o", "0", "O", "o", "0", "O",
		 * "o", "o", "0", "O", "o", "0", "O", "o", "0", "O", "o", "0", "O", "o",
		 * "0", "O", "o", "o", "o", "o", "o", "o", "o", "o", "0", "o", "0", "o",
		 * "0", "o", "o", "o", "O", "o", "o", "0", "0" };
		 * loopPrintCombination(combination); combination = new String[] { "0",
		 * "o", "o", "o", "o", "o", "o", "o", "o", "0", "O", "o", "0", "O", "o",
		 * "0", "O", "o", "0", "O", "o", "0", "O", "o", "0", "O", "o", "0", "O",
		 * "o", "0", "O", "o", "0", "O", "o", "o", "o", "o", "o", "o", "o", "0",
		 * "O", "o", "o", "o", "0", "0", "0", "o", "o", "O", "o", "o", "0" };
		 * 
		 * loopPrintCombination(combination);
		 * 
		 * 
		 * combination = new String[] { "1", "1", "i", "I", "l", "i", "I", "l",
		 * "i", "I", "i", "I", "i", "I", "i", "I", "i", "I", "i", "I", "i", "I",
		 * "i", "I", "i", "I", "i", "I", "i", "I", "i", "I", "l", "i", "I", "l",
		 * "i", };
		 * 
		 * 
		 * loopPrintCombination(combination);
		 * 
		 * 
		 * System.out.println("结果:"); for (int i = 0; i < list.size(); i++) {
		 * String current = list.get(i); System.out.println(current);
		 * 
		 * // System.out.println((1 + i) + ":" + current);
		 * 
		 * }
		 */
	}

	public static boolean addItemRandom(String str) {
		if (str.length() > 5) {
		}
		return addItem(str);

	}

	public static boolean addItem(String str) {
		if (str.length() > maxInsertLength) {
			return false;
		}
		if (str.length() < minInsertLength) {
			return false;
		}
		if (mEnableIgnoreUpperCase) {
			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				if (temp.toUpperCase().equals(str.toUpperCase())) {
					return false;
				}
			}
		}else{
			if (list.contains(str)) {
				return false;
			}
			
		}
	
		list.add(str);
		return true;
		/*
		 * if (!list.contains(str)) { // System.out.println("add:" + str);
		 * list.add(str); return true; } else { // System.err.println(
		 * "find repeat data:" + str); return false; }
		 */

	}

	/*
	 * private static void loopPrintCombination( char[] combination) { for (int
	 * i = 0; i < combination.length; i++) { String item =
	 * String.valueOf(combination[i]); addItem(item); int loopStartIndex=0;
	 * 
	 * while (loopStartIndex < combination.length) {
	 * 
	 * for (; loopStartIndex< combination.length; loopStartIndex++) {
	 * 
	 * 
	 * String temp=item+combination[loopStartIndex]; addItem(temp); } }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */
	/*
	 * private static void loopPrintCombination(String temp, String[]
	 * combination) { for (int i = 0; i < combination.length; i++) { String item
	 * = combination[i]; list.add(item); temp = item;
	 * 
	 * while (temp.length() <= combination.length) { for (int j = 0; j <
	 * combination.length; j++) { temp = temp + combination[j];// 组合后 还需要循环
	 * 
	 * // loopPrintCombination(temp, combination); if (temp.length() >
	 * combination.length) { continue; } addItem(temp); char[] chars =
	 * temp.toCharArray(); for (int k = 0; k < chars.length - 1; k++) {
	 * 
	 * StringBuffer sb = new StringBuffer(); int startIndex = chars.length - 1;
	 * 
	 * while (startIndex > 0) {
	 * 
	 * String tempChar = String.valueOf(chars[startIndex]); startIndex--; //
	 * String fixTemp = // tempChar+String.valueOf(chars[k]);
	 * 
	 * String willAppend = tempChar + String.valueOf(chars[k]);
	 * sb.append(willAppend); if (sb.length() > combination.length) { continue;
	 * } addItem(sb.toString());
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * } }
	 */

	public static String strReverseWithArray2(String string) {
		if (string == null || string.length() == 0)
			return string;
		int length = string.length();
		char[] array = string.toCharArray();
		for (int i = 0; i < length / 2; i++) {
			array[i] = string.charAt(length - 1 - i);
			array[length - 1 - i] = string.charAt(i);
		}
		return new String(array);
	}

	private static void loopPrintCombination(String[] combination) {
		for (int i = 0; i < combination.length; i++) {
			String item = String.valueOf(combination[i]);
			addItem(item);// 一位数的穷举
			String temp = item;
			while (temp.length() <= combination.length) {
				String childtemp = temp;
				for (int j = 0; j < combination.length; j++) {
					// childtemp = temp + combination[j];
					childtemp = childtemp + combination[j];

					addItem(item + combination[j]); // 2位数的穷举 1 2 1 3 14
					addItem(strReverseWithArray2(childtemp) + combination[j]); // 2位数的穷举
																				// 1
																				// 2
																				// 1
																				// 3
																				// 14
					addItem(strReverseWithArray2(childtemp) + strReverseWithArray2(combination[j])); // 2位数的穷举
																										// 1
																										// 2
																										// 1
																										// 3
																										// 14
					addItem(item + childtemp + combination[j]);
					addItem(item + childtemp + combination[j]);

					if (j == 4) {
						addItem(combination[0] + childtemp);
						addItem(combination[0] + combination[0] + combination[0] + childtemp);
						addItem(combination[0] + combination[0] + combination[1] + childtemp);
						addItem(combination[0] + combination[1] + combination[1] + childtemp);
						addItem(combination[1] + combination[0] + combination[0] + childtemp);
						addItem(combination[1] + combination[0] + combination[1] + childtemp);
						addItem(combination[1] + combination[1] + combination[1] + childtemp);
					} else if (j == 4) {

						addItem(combination[0] + combination[0] + combination[0] + combination[0] + childtemp);
						addItem(combination[0] + combination[0] + combination[0] + combination[1] + childtemp);
						addItem(combination[0] + combination[0] + combination[1] + combination[1] + childtemp);
						addItem(combination[0] + combination[1] + combination[0] + combination[0] + childtemp);
						addItem(combination[0] + combination[1] + combination[0] + combination[1] + childtemp);
						addItem(combination[0] + combination[1] + combination[1] + combination[1] + childtemp);

						addItem(combination[1] + combination[0] + combination[0] + combination[0] + childtemp);
						addItem(combination[1] + combination[0] + combination[0] + combination[1] + childtemp);
						addItem(combination[1] + combination[0] + combination[1] + combination[1] + childtemp);
						addItem(combination[1] + combination[1] + combination[1] + combination[1] + childtemp);

					} else if (combination.length >= 5 || j >= 5) {
						addItem(combination[0] + combination[0] + combination[0] + combination[0] + combination[0]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[0] + combination[0] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[0] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[1] + combination[0] + combination[0]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[1] + combination[1] + combination[0]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[1] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[0] + combination[1] + combination[1] + combination[1]
								+ childtemp);

						addItem(combination[0] + combination[1] + combination[0] + combination[0] + combination[0]
								+ childtemp);
						addItem(combination[0] + combination[1] + combination[0] + combination[0] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[1] + combination[0] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[1] + combination[0] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[0] + combination[1] + combination[1] + combination[1] + combination[1]
								+ childtemp);

						addItem(combination[1] + combination[1] + combination[0] + combination[0] + combination[0]
								+ childtemp);
						addItem(combination[1] + combination[1] + combination[0] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[1] + combination[1] + combination[1] + combination[1] + combination[1]
								+ childtemp);
						addItem(combination[1] + combination[1] + combination[1] + combination[1] + combination[1]
								+ childtemp);

					}

					addItem(temp + combination[j]);
					// addItem(combination[j]+temp);
				}
				temp = temp + childtemp;

			}

		}
	}

}
