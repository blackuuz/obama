package cn.weipass.biz.nfc;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.nfc.tech.IsoDep;

/**
 * 
 * @author houj
 *
 */
public final class CpuCardBiz {
	private CpuCardBiz() {

	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static byte[] getHistoricalBytes(IsoDep c) {
		byte[] bs = CpuCard.getHistoricalBytes(c);
		return bs;
	}

	public static byte[] getID(IsoDep c) throws CpuCardException {
		CpuCard.selectFile(c, 0x3F01);
		return CpuCard.readBinFile(c, IdFileIndex, 0);
	}

	/**
	 * 判断卡是否已经初始化
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isInited(IsoDep c) {
		try {
			getID(c);
			CpuCard.selectFile(c, 0x3F01);
			byte[] ds = new byte[8];
			ds = CpuCard.desEncrypt(c, 2, ds);
			return ds.length == 8;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 取得外部认证随机数,服务器加密这个随机数后用于外部认证
	 * 
	 * @param c
	 * @return
	 * @throws CpuCardException
	 */
	@SuppressLint("NewApi")
	public static byte[] getExternalAuthenticationRandomData(IsoDep c, int dirID) throws CpuCardException {
		CpuCard.selectFile(c, dirID);
		byte[] rs = CpuCard.getRandom(c, 4);
		return Arrays.copyOf(rs, 8);
	}

	public static byte[] getExternalAuthenticationRandomData_root(IsoDep c) throws CpuCardException {
		return getExternalAuthenticationRandomData(c, 0x3F00);
	}

	public static byte[] getExternalAuthenticationRandomData_app(IsoDep c) throws CpuCardException {
		return getExternalAuthenticationRandomData(c, 0x3F01);
	}

	public static void doExternalAuthenticationByKey(IsoDep c, int dirID, byte[] key) throws CpuCardException {
		if (key.length != 8 && key.length != 16) {
			throw new CpuCardException(8);
		}
		CpuCard.selectFile(c, 0x3F00);
		byte[] ds = getExternalAuthenticationRandomData_root(c);
		try {
			if (key.length == 8) {
				ds = DES.encrypt(key, ds);
			} else {
				ds = DES.des3encrypt(key, ds);
			}
		} catch (Exception ex) {
			throw new CpuCardException("DES加密失败");
		}
		CpuCard.mainExternalAuthentication(c, ds);
	}

	/**
	 * 清除卡上的数据
	 * 
	 * @param c
	 * @param cardId
	 * @param rootMainContorlerKey
	 *            主控密钥
	 * @throws CpuCardException
	 */
	public static void clearCardByRootMainKey(IsoDep c, byte[] rootMainContorlerKey) throws CpuCardException {
		if (rootMainContorlerKey.length != 8 && rootMainContorlerKey.length != 16) {
			throw new CpuCardException(8);
		}
		// CpuCard.selectFile(c, 0x3F01);
		doExternalAuthenticationByKey(c, 0x3F00, rootMainContorlerKey);
		CpuCard.clearDirectory(c);
	}

	/**
	 * 
	 * @param c
	 * @param cardId
	 * @param encryptData
	 * @throws CpuCardException
	 */
	public static void clearCardByEncryptData(IsoDep c, byte[] encryptData) throws CpuCardException {
		CpuCard.mainExternalAuthentication(c, encryptData);
		CpuCard.clearDirectory(c);
	}

	private final static int ExternalAuthenticationKeyIndex = 2;
	private final static int DesEncryptKeyIndex = 3;

	private final static int IdFileIndex = 4;

	/**
	 * 
	 * @param c
	 * @param cardId
	 *            ID的ID
	 * @param rootMainContorlerKey
	 *            主控密钥
	 * @param appMainContorlerKey
	 *            程序子目录主控密钥, (若为空, 使用主控密钥)
	 * @param externalAuthenticationKey
	 *            程序子目录外部认证密钥
	 * @param desEncryptKey
	 *            程序子目录内部认证密钥
	 * @throws CpuCardException
	 */
	public static void initCard(IsoDep c, byte[] cardId, byte[] rootMainContorlerKey, byte[] appMainContorlerKey, byte[] externalAuthenticationKey, byte[] desEncryptKey) throws CpuCardException {
		if (appMainContorlerKey == null) {
			appMainContorlerKey = rootMainContorlerKey;
		}
		CpuCard.selectFile(c, 0x3F00);
		// try {
		// clearCardByRootKey(c, rootMainContorlerKey);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		try {
			CpuCard.createKeyFile(c, 0);
		} catch (Exception ex) {
			System.err.println("createKeyFile");
			ex.printStackTrace();
		}
		try {
			CpuCard.addMainKey(c, rootMainContorlerKey);
		} catch (Exception ex) {
			System.err.println("addMainKey");
			ex.printStackTrace();
			try {
				CpuCard.setMainKey(c, rootMainContorlerKey);
			} catch (Exception ex2) {
				System.err.println("setMainKey");
				ex2.printStackTrace();
			}
		}
		doExternalAuthenticationByKey(c, 0x3F00, rootMainContorlerKey);
		{
			// 有些新的卡并不是空的
			CpuCard.clearDirectory(c);
			try {
				CpuCard.createKeyFile(c, 0);
			} catch (Exception ex) {
			}
			try {
				CpuCard.addMainKey(c, rootMainContorlerKey);
			} catch (Exception ex) {
			}
			doExternalAuthenticationByKey(c, 0x3F00, rootMainContorlerKey);
		}

		CpuCard.createDirectory(c, 0x3F01, 0);
		CpuCard.selectFile(c, 0x3F01);
		CpuCard.createKeyFile(c, 0);
		CpuCard.addMainKey(c, appMainContorlerKey);
		CpuCard.addExternalAuthenticationKey(c, ExternalAuthenticationKeyIndex, externalAuthenticationKey);
		CpuCard.addDesEncryptKey(c, DesEncryptKeyIndex, desEncryptKey);
		CpuCard.createBinFile(c, IdFileIndex, cardId);
		CpuCard.selectFile(c, 0x3F00);
	}

	public static byte[] desEncrypt(IsoDep c, byte[] data) throws CpuCardException {
		CpuCard.selectFile(c, 0x3F01);
		return CpuCard.desEncrypt(c, DesEncryptKeyIndex, data);
	}

	public static void externalAuthentication(IsoDep c, byte[] data) throws CpuCardException {
		CpuCard.selectFile(c, 0x3F01);
		CpuCard.externalAuthentication(c, ExternalAuthenticationKeyIndex, data);
	}
}
