package com.ksk.obama.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.printer.CardInfo;
import com.ksk.obama.printer.ConnectPayService;
import com.ksk.obama.printer.TransactionCallback;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.magcard.AidlMagCard;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;
import com.lkl.cloudpos.aidl.rfcard.AidlRFCard;
import com.lkl.cloudpos.util.HexUtil;
import com.orhanobut.logger.Logger;
import com.sunmi.pay.hardware.aidl.DeviceProvide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.weipass.biz.nfc.BankCard;
import cn.weipass.biz.nfc.NFCManager;

import static com.ksk.obama.utils.SharedUtil.getSharedData;

/**
 * Created by Administrator on 2016/11/1.
 */

public class BuyShopReadActivity extends BaseTypeActivity {
    private SoundPool soundPool;
    private String str;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.shuaka, 1);
        str = SharedUtil.getSharedData(BuyShopReadActivity.this, "robotType");
        if (!str.equals("30")) {
            switch (robotType) {
                case 3:
                    mInit = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sumiInit();
                        }
                    });
                    mInit.start();
                    break;
                case 8:
                    initWangPOS();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openRead();
    }

    @Override
    protected void onPause() {
        super.onPause();
        close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        switch (robotType) {
            case 8:
                try {
                    mNFCManager.procNFCIntent(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onNewIntent(intent);
    }


    private void playSound() {
        soundPool.play(1, 1, 1, 0, 0, 2f);
    }

    /*
   打开设备
     */
    protected void openRead() {
        if (!str.equals("30")) {
            switch (robotType) {
                case 1:
                    LKLread();
                    break;

                case 3:
                    SUNMIread();
                    break;

                case 8:
                    mNFCManager.setNFCListener(mNFCListener);
                    mNFCManager.onResume(this);
                    break;
            }
        }
    }

    // 关闭设备
    protected void close() {
        if (!str.equals("30")) {
            switch (robotType) {
                case 1:
                    LKLclose();
                    break;

                case 3:
                    SUNMIclose();
                    break;
                case 8:
                    mNFCManager.onPause(this);
                    break;
            }
        }
    }

    /**
     * 拉卡拉
     */

    private AidlRFCard rfcard = null;
    private AidlMagCard magCardDev = null; // 磁条卡设备
    private IReadCardId mIReadCardId;
    private boolean flag = false;

    public void setOnReadCardId(IReadCardId mIReadCardId) {
        this.mIReadCardId = mIReadCardId;
    }

    /*磁条卡设备
        */
    private void swipeCardNum() {
        try {
            if (null != magCardDev) {
                magCardDev.searchCard(6000000, new MagCardListener.Stub() {

                    @Override
                    public void onTimeout() throws RemoteException {
                        Logger.d("刷卡超时");
                        closeSwipe();
                        swipeCardNum();
                        Utils.showToast(BuyShopReadActivity.this, "刷卡超时");
                    }

                    @Override
                    public void onSuccess(final TrackData trackData)
                            throws RemoteException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mIReadCardId != null) {
                                    playSound();
                                    mIReadCardId.readCardNo(trackData.getSecondTrackData().trim(), "");
                                } else {
                                    Log.e("djy", "请实现mIReadCardId接口");
                                }
                            }
                        });

                        closeSwipe();
                        swipeCardNum();
                    }

                    @Override
                    public void onGetTrackFail() throws RemoteException {
                        Logger.d("刷卡失败");
                        closeSwipe();
                        swipeCardNum();
                        Utils.showToast(BuyShopReadActivity.this, "刷卡失败");
                    }

                    @Override
                    public void onError(int arg0) throws RemoteException {
                        Logger.d("刷卡错误，错误码为" + arg0);
                        closeSwipe();
                        swipeCardNum();
                        Utils.showToast(BuyShopReadActivity.this, "刷卡错误，错误码为" + arg0);
                    }

                    @Override
                    public void onCanceled() throws RemoteException {
                        Logger.d("刷卡被取消");
                        Utils.showToast(BuyShopReadActivity.this, "刷卡被取消");
                    }
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    非接卡设备测试
     */
    protected void LKLread() {
        flag = true;
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            swipeCardNum();
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            try {
                if (rfcard != null) {
                    boolean flag = rfcard.open();
                    if (flag) {
                        Logger.d("打开非接卡设备成功");
                        isExists();
                    } else {
                        Logger.e("打开非接卡设备失败");
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isExists();
        }
    };

    // 检测是否为在位
    protected void isExists() {
        if (!flag) {
            close();
        } else {
            try {
                if (rfcard.isExist()) {
                    Logger.d("检测到卡片");
                    auth();
                } else {
                    if (flag) {
                        handler.postDelayed(runnable, 2000);
                    }
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 认证
    private void auth() {
        try {
            @SuppressWarnings("unused")
            int cardType = rfcard.getCardType();
            byte[] resetData = rfcard.reset(cardType);
            int retCode = rfcard.auth((byte) 0x00, (byte) 0x08, new byte[]{
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, resetData);
            Logger.d("认证返回结果为" + retCode);
            if (0x00 == retCode) {
                Logger.d("认证成功");
                readBlockData();
            } else {
                Logger.e("认证失败");
                isExists();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 读取块数据
    private void readBlockData() {
        try {
            byte[] data = new byte[2048];
            rfcard.readBlock((byte) 0x08, data);
            final String str = HexUtil.bcd2str(data);
            String str2 = str.substring(0, 32);
            int i = 0;
            int j = 2;
            List<String> list = new ArrayList<String>();
            while (str2.length() >= j) {
                list.add(str2.substring(i, j));
                i = j;
                j += 2;
            }
            String cardNum = "";
            for (int n = 0; n < list.size(); n++) {
                String str3 = list.get(n);
                cardNum += (char) Integer.parseInt(str3, 16);
            }
            String uid = getUID();
            if (uid == null) {
                uid = "";
            }
            close();
            final String Num = cardNum.trim();
            final String finalUid = uid;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mIReadCardId != null) {
                        playSound();
                        mIReadCardId.readCardNo(Num, finalUid);
                    } else {
                        Log.e("djy", "请实现mIReadCardId接口");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUID() {
        String uid;
        try {
            byte[] data = rfcard.reset(0x00);
            if (null != data) {
                if ("3C".equals(HexUtil.bcd2str(data).substring(0, 2))) {
                    uid = HexUtil.bcd2str(data).substring(6, 14);
                } else {
                    uid = HexUtil.bcd2str(data).substring(4, 12);
                }
                return uid;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDeviceConnected(AidlDeviceService serviceManager) {
        try {
            rfcard = AidlRFCard.Stub
                    .asInterface(serviceManager.getRFIDReader());
            magCardDev = AidlMagCard.Stub.asInterface(serviceManager
                    .getMagCardReader());
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
            soundPool.load(this, R.raw.shuaka, 1);
            openRead();
        } catch (RemoteException e) {
            Utils.showToast(BuyShopReadActivity.this, "读卡器打开失败,请重试");
            e.printStackTrace();
        }
    }


    // 关闭设备
    protected void LKLclose() {
        flag = false;
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            closeSwipe();
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            handler.removeCallbacks(runnable);
            try {
                if (rfcard != null) {
                    int ret = rfcard.halt();
                    if (ret == 0x00) {
                        Log.d("djy", "非接卡下电操作成功");
                    }
                    boolean flag = rfcard.close();
                    if (flag) {
                        Log.d("djy", "rf关闭");
                    } else {
                        LKLclose();
                    }

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSwipe() {
        if (null != magCardDev) {
            try {
                Log.e("djy", "刷卡关闭");
                magCardDev.stopSearch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**************************************拉卡拉*******************************************/

    /**************************************************************************************
     * *************************************SUNMI*****************************************
     **************************************************************************************/
    public static final int CARDTYPE_MAG = 1;
    public static final int CARDTYPE_IC = 2;
    public static final int CARDTYPE_NFC = 4;
    public static final int GENERAL_READER_DEVICE = 269484034;
    private ConnectPayService connectPayService;
    public static DeviceProvide deviceProvide;
    private Thread mInit;

    private Handler mHandlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (4 == msg.what) {
                SUNMIclose();
                if (hasWindowFocus())
                    SUNMIread();
            } else {
                SUNMIclose();
                CardInfo cardInfo = (CardInfo) msg.obj;
                if (cardInfo != null && cardInfo.cardType != 7) {
                    String cardnum = "";
                    if (cardInfo.cardType == 1 && !TextUtils.isEmpty(cardInfo.track2)) {
                        cardnum = cardInfo.track2;
                    } else if (cardInfo.cardType == 4 && null != cardInfo.hashMap && cardInfo.hashMap.get("UUID") != null) {
                        String str = (String) cardInfo.hashMap.get("UUID") + "";
                        cardnum = str + "-Is";
                    }
                    Logger.e(cardnum);
                    if (!TextUtils.isEmpty(cardnum) && mIReadCardId != null) {
                        playSound();
                        mIReadCardId.readCardNo(cardnum, "");
                    } else {
                        Log.e("djy", "请实现mIReadCardId接口");
                    }
                } else {
                    if (hasWindowFocus())
                        SUNMIread();
                }
            }
        }
    };

    private TransactionCallback mCallback = new TransactionCallback(mHandlers);

    private ConnectPayService.PayServiceConnected mPayServiceConnected = new ConnectPayService.PayServiceConnected() {

        @Override
        public void onServiceConnected(DeviceProvide mDeviceProvide) {
            mInit.interrupt();
            deviceProvide = mDeviceProvide;
        }

        @Override
        public void onServiceDisconnected() {
            deviceProvide = null;
        }
    };

    private void sumiInit() {
        if (deviceProvide == null) {
            connectPayService = ConnectPayService.Init(BuyShopReadActivity.this);
            connectPayService.connectPayService(mPayServiceConnected);
            Log.e("sunmi", "初始化失败");
            sumiInit();
        } else {
            mInit.interrupt();
            try {
                deviceProvide.registerTransactionCallback(mCallback);
                Log.e("sunmi", "初始化成功");
                SUNMIread();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private Thread mThread;

    protected void SUNMIread() {
        int cardType = 0;
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            cardType += CARDTYPE_MAG;
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            cardType += CARDTYPE_NFC;
        }
        final int finalCardType = cardType;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] b = new byte[4];
                    if (deviceProvide != null) {
                        deviceProvide.getReadCardProvider().checkCard(finalCardType, GENERAL_READER_DEVICE, b, b.length, 360);
                        Log.e("sunmi", "等待检卡......");
                    } else {
                        sumiInit();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    protected void SUNMIclose() {
        try {
            mInit.interrupt();
            mThread.interrupt();
            if (deviceProvide != null) {
                deviceProvide.getReadCardProvider().abortCheckCard();
                Log.e("sunmi", "读卡关闭");
            }
        } catch (Exception e) {
            Log.e("sunmi", "读卡关闭异常");
        }
    }

    /*************************************SUNMI*********************************************/

    /*************************************
     * wangpos
     *********************************************/

    private NFCManager.NFCListener mNFCListener = new NFCManager.NFCListener() {

        @Override
        public void onReciveDataOffline(final byte[] data) {

        }

        @Override
        public void onError(String error) {
            getCardUID(error);
        }

        @Override
        public void onReciveBankDataOffline(BankCard.BankCardInfo bankCard) {

        }
    };
    private NFCManager mNFCManager;

    private void initWangPOS() {
        mNFCManager = NFCManager.getInstance();
        mNFCManager.init(this);
    }

    private void getCardUID(final String cardNo) {
        loadingDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(BuyShopReadActivity.this, "dbname"));
        map.put("gid", SharedUtil.getSharedData(BuyShopReadActivity.this, "groupid"));
        map.put("cardNO", "");
        map.put("CardCode", cardNo);
        postToHttp(NetworkUrl.GETCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                loadingDialog.dismiss();
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String code = object.getString("result_stadus");
                    if (code.equals("SUCCESS")) {
                        String cardNumber = object.getString("result_data").trim();
                        if (mIReadCardId != null) {
                            mIReadCardId.readCardNo(cardNumber, cardNo);
                            playSound();
                        } else {
                            Log.e("djy", "请实现mIReadCardId接口");
                        }
                    } else {
                        String msg = object.getString("result_errmsg");
                        Utils.showToast(BuyShopReadActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                loadingDialog.dismiss();
            }
        });
    }
    /*************************************wangpos*********************************************/
}
