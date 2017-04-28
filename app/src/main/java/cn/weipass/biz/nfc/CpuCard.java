package cn.weipass.biz.nfc;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.nfc.tech.IsoDep;

/**
 * CPU卡.
 * 
 * @author houj
 *
 */
public final class CpuCard {

	public static class CmdResult {
		public final int code;
		public final byte[] rdata;

		CmdResult(int code, byte[] data) {
			this.code = code;
			this.rdata = data;
		}

		public void check(int code) throws CpuCardException {
			if (this.code != code) {
				throw new CpuCardException(this.code);
			}
		}
	}

	public static CmdResult doCmd(IsoDep c, String cmd) throws CpuCardException {
		System.out.println("send cmd:" + cmd);
		return doCmd(c, HEX.hexToBytes(cmd));
	}

	@SuppressLint("NewApi")
	public static CmdResult doCmd(IsoDep c, byte[] cmd) throws CpuCardException {
		try {
			byte[] rs = c.transceive(cmd);
			if (rs == null || rs.length < 2) {
				throw new CpuCardException(1);
			}
			System.out.println("read:" + HEX.bytesToHex(rs));
			int code = (rs[rs.length - 2] & 0xFF) << 8;
			code |= (rs[rs.length - 1] & 0xFF);
			rs = Arrays.copyOf(rs, rs.length - 2);
			return new CmdResult(code, rs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CpuCardException(0);
		}
	}

	private CpuCard() {
	}

	@SuppressLint("NewApi")
	public static byte[] getHistoricalBytes(IsoDep c) {
		return c.getHistoricalBytes();
	}

	public static byte[] getRandom(IsoDep c, int len) throws CpuCardException {
		String cmd = "00840000" + HEX.toBeHex(len, 1);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
		return r.rdata;
	}

	public static void externalAuthentication(IsoDep c, int id, byte[] data) throws CpuCardException {
		String cmd = "008200" + HEX.toBeHex(id, 1) + HEX.toBeHex(data.length,1) + HEX.bytesToHex(data);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void mainExternalAuthentication(IsoDep c, byte[] data) throws CpuCardException {
		externalAuthentication(c, 1, data);
	}

	public static void selectFile(IsoDep c, int fileID) throws CpuCardException {
		String cmd = "00A4000002" + HEX.toBeHex(fileID, 2);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}
	
	public static void selectFile(IsoDep c, String name) throws CpuCardException {
		byte[] bs=null;
		try {
			bs = name.getBytes("UTF8");
		} catch (Exception e) {
		}
		selectFile(c,bs);
	}
	
	public static void selectFile(IsoDep c, byte[] name) throws CpuCardException {
		byte[] bs=name;
		String cmd = "00A40400" + HEX.toBeHex(bs.length, 1)+HEX.bytesToHex(bs);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void createDirectory(IsoDep c, int dirID, int size) throws CpuCardException {
		if (size <= 0) {
			size = 0x100;
		}
		String cmd = "80E0" + HEX.toBeHex(dirID, 2) + "0838" + HEX.toBeHex(size, 2) + "F0F0FFFFFF";
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void clearDirectory(IsoDep c) throws CpuCardException {
		String cmd = "800E000000";
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void addMainKey(IsoDep c, byte[] key) throws CpuCardException {
		addExternalAuthenticationKey(c, 1, key);
	}
	
	public static void setMainKey(IsoDep c, byte[] key) throws CpuCardException {
		setExternalAuthenticationKey(c, 1, key);
	}

	public static void createKeyFile(IsoDep c, int size) throws CpuCardException {
		if (size <= 0) {
			size = 0x0050;
		}
		String cmd = "80E00000073F" + HEX.toBeHex(size, 2) + "FFF0FFFF";
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void addExternalAuthenticationKey(IsoDep c, int id, byte[] key) throws CpuCardException {
		if (key.length != 8 && key.length != 16) {
			throw new CpuCardException(8);
		}
		String cmd = "80D401" + HEX.toBeHex(id, 1) + HEX.toBeHex(key.length + 5, 1) + "39F0F0FFFF" + HEX.bytesToHex(key);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void setExternalAuthenticationKey(IsoDep c, int id, byte[] key) throws CpuCardException {
		if (key.length != 8 && key.length != 16) {
			throw new CpuCardException(8);
		}
		String cmd = "80D439" + HEX.toBeHex(id, 1) + HEX.toBeHex(key.length, 1) + HEX.bytesToHex(key);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void addDesEncryptKey(IsoDep c, int id, byte[] key) throws CpuCardException {
		if (key.length != 8 && key.length != 16) {
			throw new CpuCardException(8);
		}
		String cmd = "80D401" + HEX.toBeHex(id, 1) + HEX.toBeHex(key.length + 5, 1) + "30F0F00101" + HEX.bytesToHex(key);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	public static void addDesDecryptKey(IsoDep c, int id, byte[] key) throws CpuCardException {
		if (key.length != 8 && key.length != 16) {
			throw new CpuCardException(8);
		}
		String cmd = "80D401" + HEX.toBeHex(id, 1) + HEX.toBeHex(key.length + 5, 1) + "31F0F00101" + HEX.bytesToHex(key);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
	}

	/**
	 * 
	 * @param c
	 * @param id
	 * @param data
	 * @return
	 * @throws CpuCardException
	 */
	public static byte[] desEncrypt(IsoDep c, int id, byte[] data) throws CpuCardException {
		if (data.length != 8) {
			throw new CpuCardException(8);
		}
		String cmd = "008800" + HEX.toBeHex(id, 1) + "08" + HEX.bytesToHex(data);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
		return r.rdata;
	}

	/**
	 * 
	 * @param c
	 * @param id
	 * @param data
	 * @return
	 * @throws CpuCardException
	 */
	public static byte[] desDecrypt(IsoDep c, int id, byte[] data) throws CpuCardException {
		if (data.length != 8) {
			throw new CpuCardException(8);
		}
		String cmd = "008801" + HEX.toBeHex(id, 1) + "08" + HEX.bytesToHex(data);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
		return r.rdata;
	}

	public static void createBinFile(IsoDep c, int id, byte[] bs) throws CpuCardException {
		{
			//创建文件
			String cmd = "80E000" + HEX.toBeHex(id, 1) + "072800" + HEX.toBeHex(bs.length, 1) + "00F0FFFF";
			CmdResult r = doCmd(c, cmd);
			r.check(0x9000);
		}
		{
			//写入数据
			String cmd = "00D6" + HEX.toBeHex(0x80 + id, 1) + "00" + HEX.toBeHex(bs.length, 1) + HEX.bytesToHex(bs);
			CmdResult r = doCmd(c, cmd);
			r.check(0x9000);
		}
	}

	public static byte[] readBinFile(IsoDep c, int id, int len) throws CpuCardException {
		String cmd = "00B0" + HEX.toBeHex(0x80 + id, 1) + "00" + HEX.toBeHex(len, 1);
		CmdResult r = doCmd(c, cmd);
		r.check(0x9000);
		return r.rdata;
	}

}
