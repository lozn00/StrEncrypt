package newencrypt;

import java.io.UnsupportedEncodingException;

public final class o0MyName {
    public static String o(String str, String str2) {
        try {
        	byte[] aao=AAAo.oFromTwo(str, 2);
            return new String(oFromMyName(aao, str2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(oFromMyName(AAAo.oFromTwo(str, 2), str2));
        }
    }

    private static byte[] oFromMyName(byte[] bArr, String str) {
        int length = bArr.length;
        int length2 = str.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            if (i >= length2) {
                i = 0;
            }
            bArr[i2] = (byte) (bArr[i2] ^ str.charAt(i));
            i2++;
            i++;
        }
        return bArr;
    }
}