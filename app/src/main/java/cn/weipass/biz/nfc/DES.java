package cn.weipass.biz.nfc;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.annotation.SuppressLint;


/**
 * DES加解密, 服务端也可以用这个类.
 * @author houj
 *
 */
public final class DES {
	private DES() {
	}

	@SuppressLint("NewApi")
	public static byte[] encrypt(byte key[], byte[] str) throws Exception {
		if (key.length != 8) {
			throw new RuntimeException("key length err:" + key.length);
		}
		int needLen = (str.length + 7) & (-8);
		if (needLen != str.length) {
			str = Arrays.copyOf(str, needLen);
		}
		byte[] rs = new byte[str.length];
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey skey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		cipher.doFinal(str, 0, str.length, rs, 0);
		return rs;
	}

	/**
	 * 解密方法
	 * 
	 * @param key
	 * @param encryptedData
	 */
	@SuppressLint("NewApi")
	public static byte[] decrypt(byte key[], byte[] str) throws Exception {
		if (key.length != 8) {
			throw new RuntimeException("key length err:" + key.length);
		}
		int needLen = (str.length + 7) & (-8);
		if (needLen != str.length) {
			str = Arrays.copyOf(str, needLen);
		}
		byte[] rs = new byte[str.length];
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey skey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
		cipher.init(Cipher.DECRYPT_MODE, skey);
		cipher.doFinal(str, 0, str.length, rs, 0);
		return rs;
	}

	public static byte[] changeKey(byte[] bs) {
		byte[] rs = new byte[bs.length];
		for (int i = 0; i < bs.length; i++) {
			int n = bs[i] & 0xFF;
			int r = 0;
			for (int m = 0; m < 8; m++) {
				r |= (((n >> m) & 1) << (7 - m));
			}
			rs[i] = (byte) r;
			// System.out.println(n+">"+r);
		}
		return rs;
	}

	/**
	 * des3加密
	 * 
	 * @param key
	 *            ；加密秘钥
	 * @param data
	 *            ；加密数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] des3encrypt(byte[] key, byte[] data) throws Exception {
		if (key.length != 16 && key.length != 24) {
			throw new RuntimeException("key length err:" + key.length);
		}
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];
		byte[] k3;
		System.arraycopy(key, 0, k1, 0, 8);
		System.arraycopy(key, 8, k2, 0, 8);
		if (key.length == 16) {
			k3 = k1;
		} else {
			k3 = new byte[8];
			System.arraycopy(key, 16, k3, 0, 8);
		}
		data = encrypt(k1, data);
		data = decrypt(k2, data);
		data = encrypt(k3, data);
		return data;
	}

	/**
	 * des3解密
	 * 
	 * @param key
	 *            ；解密秘钥
	 * @param data
	 *            ；解密数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] des3decrypt(byte[] key, byte[] data) throws Exception {
		if (key.length != 16 && key.length != 24) {
			throw new RuntimeException("key length err:" + key.length);
		}
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];
		byte[] k3;
		System.arraycopy(key, 0, k1, 0, 8);
		System.arraycopy(key, 8, k2, 0, 8);
		if (key.length == 16) {
			k3 = k1;
		} else {
			k3 = new byte[8];
			System.arraycopy(key, 16, k3, 0, 8);
		}
		data = decrypt(k1, data);
		data = encrypt(k2, data);
		data = decrypt(k3, data);
		return data;
	}

	final static char[] CS = "0123456789ABCDEF".toCharArray();

	public static String toHex(byte[] bs) {
		char[] rs = new char[bs.length * 2];
		for (int i = 0; i < bs.length; i++) {
			rs[i + i] = CS[(bs[i] >> 4) & 0xF];
			rs[i + i + 1] = CS[(bs[i]) & 0xF];
		}
		return new String(rs);
	}

	// public static void main(String[] args) throws Exception {
	// byte[] bs = "12345678ABCDEFGH12".getBytes();
	// byte[] ks = "1234567887654321".getBytes();
	// System.out.println("Data:" + toHex(bs));
	// System.out.println("KEY:" + toHex(ks));
	// byte[] rs = des3encrypt(ks, bs);
	// System.out.println("des3encrypt:" + toHex(rs));
	// byte[] rs2 = des3decrypt(ks, rs);
	// System.out.println("des3decrypt:" + toHex(rs2));
	// System.out.println(new String(rs2));
	// }
}
