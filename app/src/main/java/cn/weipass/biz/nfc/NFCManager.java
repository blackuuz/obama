package cn.weipass.biz.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

import cn.weipass.biz.nfc.BankCard.BankCardInfo;

public class NFCManager {
	private static NFCManager instance;
	private NfcAdapter nfcAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	// 是否是NFC请求
	private boolean isNfcRequest = false;
	private NFCListener mNFCListener;
	private Activity mActivity;
    private IsoDep na;

	private NFCManager() {
	}

	public static NFCManager getInstance() {
		if (instance == null)
			instance = new NFCManager();
		return instance;
	}

	@SuppressLint("NewApi")
	public void init(Context context) {
		if (Build.VERSION.SDK_INT < 10) {
			return;
		}
		try {
			nfcAdapter = NfcAdapter.getDefaultAdapter(context);
		} catch (Exception e1) {
			nfcAdapter = null;
			return;
		}
		// 判断2
		if (nfcAdapter == null) {
			// 如果手机不支持NFC，或者NFC没有打开就直接返回
			Log.d(this.getClass().getName(), "手机不支持NFC功能！");
			return;
		}

		// 三种Activity NDEF_DISCOVERED ,TECH_DISCOVERED,TAG_DISCOVERED
		// 指明的先后顺序非常重要， 当Android设备检测到有NFC Tag靠近时，会根据Action申明的顺序给对应的Activity
		// 发送含NFC消息的 Intent.
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, tech, tag };
		mTechLists = new String[][] { new String[] { Ndef.class.getName(), MifareClassic.class.getName(),
				NfcA.class.getName() } };

		if (!nfcAdapter.isEnabled()) {
			Log.d(this.getClass().getName(), "手机NFC功能没有打开！");
			enableDialog(context);
			return;
		}
	}

	private void enableDialog(final Context context) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("提醒");
		ab.setMessage("手机NFC开关未打开，是否现在去打开？");
		ab.setNeutralButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ab.setNegativeButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				dialog.dismiss();
			}
		});
		ab.create().show();
	}

	@SuppressLint("NewApi")
	public void onPause(Activity activity) {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(activity);
		}
		mActivity = null;
	}

	/**
	 * 试图用同步锁解决非法状态异常
	 * @param activity
	 */
	private synchronized void nfcManager(Activity activity){
		if (nfcAdapter != null) {
			if (mActivity == null) {
				mActivity = activity;
				mPendingIntent = PendingIntent.getActivity(mActivity, 0,
						new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
				nfcAdapter.enableForegroundDispatch(activity, mPendingIntent, mFilters, mTechLists);
			} else {
				if (!mActivity.getClass().equals(activity.getClass())) {
					mActivity = activity;
					mPendingIntent = PendingIntent.getActivity(mActivity, 0,
							new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
					nfcAdapter.enableForegroundDispatch(activity, mPendingIntent, mFilters, mTechLists);
				}
			}
		}
	}




	@SuppressLint("NewApi")
	public void onResume(Activity activity) {
		nfcManager(activity);
	}

	public boolean isEnabled() {
		return nfcAdapter != null && nfcAdapter.isEnabled();
	}

	/**
	 * 处理cup NFC卡
	 * @param intent
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	public void procNFCIntent(Intent intent) throws IOException {
		if (isEnabled()) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
            	na = IsoDep.get(tag);
                na.setTimeout(2000);
            	na.connect();
                if (mNFCListener != null) {
                	byte[] id =CpuCardBiz.getID(na);
                    Log.e("iD:",numCast(id));
                    mNFCListener.onReciveDataOffline(id);
                }
            }catch (Exception e) {
                e.printStackTrace();
				if (tag != null) {
					mNFCListener.onError(HEX.bytesToHex(tag.getId()));
				}

            }

        }else{
        	 if (mNFCListener != null) {
                 mNFCListener.onError("NFC不可用");
             }
        }
	}
	private String numCast(byte[] list) {
		String cardNum = "";
		for (int n = 0; n < list.length; n++) {
			Log.e("djy", list[n] + "");
			cardNum += (char) list[n];
		}
		return cardNum;
	}


	public void setNFCListener(NFCListener ls) {
		mNFCListener = ls;
	}

	public interface NFCListener {
		public void onReciveDataOffline(byte[] id);
		public void onReciveBankDataOffline(BankCardInfo bankCard);
		public void onError(String error);
	}
}
