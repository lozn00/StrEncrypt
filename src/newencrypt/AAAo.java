package newencrypt;

public final class AAAo {
    public static byte[] oFromTwo(String str, int i) {
        return oFromTreeParam(str.getBytes(), i);
    }

    public static byte[] oFromTreeParam(byte[] bArr, int i) {
        return oFromFourParam(bArr, 0, bArr.length, i);
    }

    private static byte[] oFromFourParam(byte[] bArr, int i, int i2, int i3) {
        C0184ooMy c0184oo = new C0184ooMy(i3, new byte[((i2 * 3) / 4)]);
        if (!c0184oo.o(bArr, i, i2, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (c0184oo.sb0 == c0184oo.o.length) {
            return c0184oo.o;
        } else {
            byte[] bArr2 = new byte[c0184oo.sb0];
            System.arraycopy(c0184oo.o, 0, bArr2, 0, c0184oo.sb0);
            return bArr2;
        }
    }
}