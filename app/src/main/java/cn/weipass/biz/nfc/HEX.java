package cn.weipass.biz.nfc;

/**
 * 
 * @author houj
 *
 */
public final class HEX {

	/**
	 * HEX
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexToBytes(String s) {
		s = s.toUpperCase();
		int len = s.length() / 2;
		int ii = 0;
		byte[] bs = new byte[len];
		char c;
		int h;
		for (int i = 0; i < len; i++) {
			c = s.charAt(ii++);
			if (c <= '9') {
				h = c - '0';
			} else {
				h = c - 'A' + 10;
			}
			h <<= 4;
			c = s.charAt(ii++);
			if (c <= '9') {
				h |= c - '0';
			} else {
				h |= c - 'A' + 10;
			}
			bs[i] = (byte) h;
		}
		return bs;
	}

	private final static char[] CS = "0123456789ABCDEF".toCharArray();

	/**
	 * @param bs
	 * @return
	 */
	public static String bytesToHex(byte[] bs) {
		char[] cs = new char[bs.length * 2];
		int io = 0;
		for (int n : bs) {
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
		}
		return new String(cs);
	}

	public static String bytesToHex(byte[] bs, int len) {
		char[] cs = new char[len * 2];
		int io = 0;
		for (int i = 0; i < len; i++) {
			int n = bs[i];
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
		}
		return new String(cs);
	}

	public static String bytesToHex(byte[] bs, char gap) {
		char[] cs = new char[bs.length * 3];
		int io = 0;
		for (int n : bs) {
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
			cs[io++] = gap;
		}
		return new String(cs);
	}

	public static String bytesToHex(byte[] bs, char gap, int len) {
		char[] cs = new char[len * 3];
		int io = 0;
		for (int i = 0; i < len; i++) {
			int n = bs[i];
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
			cs[io++] = gap;
		}
		return new String(cs);
	}

	/**
	 * 
	 * 
	 * @param bs
	 * @param bytePerLine
	 * @return
	 */
	public static String bytesToCppHex(byte[] bs, int bytePerLine) {

		if (bytePerLine <= 0 || bytePerLine >= 65536) {
			bytePerLine = 65536;
		}
		int lines = 0;
		if (bytePerLine < 65536) {
			lines = (bs.length + bytePerLine - 1) / bytePerLine;
		}

		char[] cs = new char[bs.length * 5 + lines * 3];
		int io = 0;
		int ic = 0;
		for (int n : bs) {
			cs[io++] = '0';
			cs[io++] = 'x';
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
			cs[io++] = ',';
			if (bytePerLine < 65536) {
				if (++ic >= bytePerLine) {
					ic = 0;
					cs[io++] = '/';
					cs[io++] = '/';
					cs[io++] = '\n';
				}
			}
		}
		if (bytePerLine < 65536) {
			if (io < cs.length) {
				cs[io++] = '/';
				cs[io++] = '/';
				cs[io++] = '\n';
			}
		}
		return new String(cs);
	}

	public static String toLeHex(int n, int byteCount) {
		char[] rs = new char[byteCount * 2];
		int io = 0;
		for (int i = 0; i < byteCount; i++) {
			rs[io++] = CS[(n >> 4) & 0xF];
			rs[io++] = CS[(n >> 0) & 0xF];
			n >>>= 8;
		}
		return new String(rs);
	}

	public static String toBeHex(int n, int byteCount) {
		char[] rs = new char[byteCount * 2];
		int io = 0;
		n <<= (32 - byteCount * 8);
		for (int i = 0; i < byteCount; i++) {
			rs[io++] = CS[(n >> 28) & 0xF];
			rs[io++] = CS[(n >> 24) & 0xF];
			n <<= 8;
		}
		return new String(rs);
	}

	// public static void main(String[] args) {
	// byte[] bs =
	// HEX.hexToBytes("00C6B0F084C0B05BBEC211A678114CA6C751C08FE99EE22D25F7DCC21440D4ECA193C3444D8A8C53DDE6FBD40AEB917B551119D61A6A347CEF64CAB7A9437D2D9B34478DCB256CDCDDBB8E2A4E2D5F631136C7AA91037898D65B83526D5BE1978C818B9DD60CD19D5007269B966407D7D05A9BAFFB0964BE4DDEC331D697D07C1B");
	// System.out.println(bs.length);
	//
	// String s =
	// "80 94 db 61 fa 66 16 d0 c9 d4 3d 3b 81 b4 72 a0 98 35 40 9b 71 4a ef fc 16 15 32 bd 7f a7 ba 7f 12 e2 e6 21  03 87 b1 9f c5 bc 12 dd 63 5c 27 1b 11 73 e9 08 26 85 29 96 c6 57 e4 e5 e1 db f2 e5 40 ff c0 fa 6c 27 90 f2 23 c0  20 02 3f c0 ae de 30 e7 89 71 33 10 a7 eb 7b c6 41 1b 13 b4 0f 22 25 cc d4 a1 ba 1b 0c a0 8b 7b 5c cd be ed 36 d4  5e 64 63 66 62 18 26 89 d4 c1 8c 09 56 c3 33 f8 03";
	// s = s.replace(" ", "");
	// bs = HEX.hexToBytes(s);
	// System.out.println(bs.length);
	// }

	public static void main(String[] args) {
		System.out.println(toBeHex(0x3f01, 2));
	}
}
