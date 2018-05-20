import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ColorHexPrint {
	public static void main(String[] args) {
		// java int类型刚好32位，刚好可以用来测试颜色的操作
		int color = 0xffef2f;
		int colorGray = 0xffcccccc;
		int colorFull = 0xffffef2f;
		int colorRed = (255 * 2) << 16;
		int fullWhite = 0xffffffff;
		int fullWhite1 = 0b1111_1111_1111_1111______1111_1111_1111_1111;

		System.out.println("ox" + Integer.toHexString(color) + ",color:" + Integer.toHexString(colorFull) + ",RED:"

		+ Integer.toHexString(colorRed) + ",white:" + Integer.toHexString(fullWhite) + ",fullWhite1:"
				+ Integer.toHexString(fullWhite1));

		// 从灰色里面取出红色通道 删除透明通道 ， 绿色通道 蓝色通道 ， 也就是取出 第一个cc
		int and = colorGray & 0x00ff0000;
		// 取出来之后这个值是很变态的, ox cc 00 00; 这一步 蓝色和绿色通道被清空了，但是还需要再继续，
		System.out.println("第一步:" + Integer.toHexString(and));
		int fixand = and >> (int) Math.pow(4, 2);
		// int fixand=and>>16;//移动多少个比特位， 1个字节有4个bit位 一个颜色 2个字节 这里
		// 要取出红色通道，所以蓝色和绿色 的话就又4个字节，4的4平方
		System.out.println("第二步:" + Integer.toHexString(fixand) + "," + (Math.pow(4, 2)));
		test();
	}

	private static void test() {
		double d = 0.145;
//		double d = 114.145;
		BigDecimal b = new BigDecimal(d);
		d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(d);

		d =0.9353;
		DecimalFormat df = new DecimalFormat("#.00");
		String str = df.format(d);
		System.out.println("DecimalFormat "+str);

		System.out.println(String.format("ffffffffffffffffformat:%.2f", d));
		NumberFormat nf = NumberFormat.getNumberInstance();
		// 保留两位小数
		nf.setMaximumFractionDigits(2);
		// 如果不需要四舍五入，可以使用RoundingMode.DOWN
		nf.setRoundingMode(RoundingMode.UP);
		System.out.println(nf.format(d));
	}
}
