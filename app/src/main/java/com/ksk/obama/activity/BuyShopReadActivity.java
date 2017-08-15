package com.ksk.obama.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ksk.obama.R;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IReadCardId;
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
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.weipass.biz.nfc.BankCard;
import cn.weipass.biz.nfc.NFCManager;
import cn.weipass.pos.sdk.MagneticReader;
import cn.weipass.pos.sdk.impl.WeiposImpl;

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
                    mReadCardOpt = MyApp.mReadCardOpt;
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
       // openRead();
    }

    @Override
    protected void onPause() {
        super.onPause();
        close();
        switch (robotType) {
            case 8:
                if (mNFCManager != null) {
                    mNFCManager.onPause(this);
                }
                break;
        }

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
                    WangPosread();
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
                    WangPosclose();
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
                        if("".equals(Num)){
                            getCardUID(finalUid);
                        }else {
                            mIReadCardId.readCardNo(Num, finalUid);
                            Log.e("uuz", "拉卡拉读取m1卡- - - -卡号：" + Num + "   uid: " + finalUid);
                            playSound();
                        }
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
    private ReadCardOpt mReadCardOpt;

    /**
     * sunMiCardType : 1磁条卡   2 ic   4 nfc卡   7全部
     */
    private void SUNMIread() {
        int sunMiCardType = 0;
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            sunMiCardType = 1;
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            sunMiCardType = 4;
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")
                && SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            sunMiCardType = 7;
        }
        try {
            //   playSound();
            mReadCardOpt.readCard(sunMiCardType, readCardCallback, 600);
        } catch (RemoteException e) {
            e.printStackTrace();

            Toast.makeText(this, "检卡失败" + e, Toast.LENGTH_LONG).show();
            // cardInfoTxt.setText(getString(R.string.error_time) + e);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    CardInfo cardInfo = (CardInfo) msg.obj;
                    mIReadCardId.readCardNo(cardInfo.cardNo, "");
                    Log.d("uuz", cardInfo.cardNo);

                    playSound();
                    sumiCloseMA();
                    SUNMIread();
                    break;
                case 0x02:
                    Utils.showToast(BuyShopReadActivity.this, "刷卡超时");
                    SUNMIread();

                    break;
                case 0x03:
                    int i = (int) msg.obj;
                    sumiCloseMA();
                    SUNMIread();
                    break;

                case 0x16:
                    Log.d("uuz", "M1授权");
                    m1auth();
                    break;


            }
        }
    };

    /**
     * 检卡回调
     */
    private ReadCardCallback readCardCallback = new ReadCardCallback.Stub() {

        @Override
        public void onStartReadCard() throws RemoteException {
        }

        @Override
        public void onFindMAGCard(CardInfo cardInfo) throws RemoteException {
            Logger.d("lj", cardInfo.toString());
            Message message = new Message();
            message.what = 0x01;
            message.obj = cardInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFindNFCCard(CardInfo cardInfo) throws RemoteException {
            Message message = new Message();
            message.what = 0x16;
            mHandler.sendEmptyMessage(0x16);

        }

        @Override
        public void onFindICCard(CardInfo cardInfo) throws RemoteException {
            Message message = new Message();
            message.what = 0x01;
            message.obj = cardInfo;
            mHandler.sendMessage(message);

        }

        @Override
        public void onError(int i) throws RemoteException {
            Message message = new Message();
            message.what = 0x03;
            message.obj = i;
            mHandler.sendMessage(message);

        }

        @Override
        public void onTimeOut() throws RemoteException {
            mHandler.sendEmptyMessage(0x02);

        }
    };


    /**
     * M1 回调
     */
    ReadCardCallback mReadCardCallback = new ReadCardCallback.Stub() {
        @Override
        public void onStartReadCard() throws RemoteException {

        }

        @Override
        public void onFindMAGCard(CardInfo cardInfo) throws RemoteException {
        }

        @Override
        public void onFindNFCCard(CardInfo cardInfo) throws RemoteException {
            Log.e("onFindMAGCard", "onFindMAGCard");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m1auth();
                }
            });
        }

        @Override
        public void onFindICCard(CardInfo cardInfo) throws RemoteException {
        }

        @Override
        public void onError(int code) throws RemoteException {
        }

        @Override
        public void onTimeOut() throws RemoteException {
        }

    };


    /**
     * 授权
     */
    private void m1auth() {
        try {
            byte[] key = new byte[6];
            for (int i = 0; i < key.length; i++) {
                key[i] = (byte) 0xFF;
            }
            int result = mReadCardOpt.m1Auth(0, 0, key);
            if (result == 0) {
                m1readblock();
            } else {
                sumiCloseMA();
                SUNMIread();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取块数据
     */
    private void m1readblock() {
        try {
            byte[] blockData = new byte[16];
            int blockResult = mReadCardOpt.m1ReadBlock(0, blockData);
            String Num = "";
            if (blockResult == 0) {
                if (mIReadCardId != null) {
                    String str2 = Utils.bytesToHexString(blockData);
                    String cardNum = "";
                    cardNum = str2.substring(0, 8);
                    close();
                    Num = cardNum.trim();
                    getCardUID(Num);
                } else {
                    Log.e("djy", "请实现mIReadCardId接口");
                }
            } else {
                Log.e("uuz", "读卡失败");
                sumiCloseMA();
                SUNMIread();
                Utils.showToast(BuyShopReadActivity.this, "读卡失败");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e("uuz", "读卡异常");
            Utils.showToast(BuyShopReadActivity.this, "读卡异常");
        }
    }
    protected void SUNMIclose() {
        sumiCloseMA();
    }


    private void sumiCloseMA() {
        try {
            if (mReadCardOpt != null) {
                mReadCardOpt.cancelCheckCard();
                Log.e("sunmi", "读卡关闭");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e("sunmi", "读卡关闭异常");
        }

    }



    /*************************************SUNMI*********************************************/

    /************************************** wangpos*********************************************/

    private NFCManager mNFCManager;
    private MagneticReader mMagneticReader;// 磁条卡管理

    private void initWangPOS() {
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            //实现磁条卡的方法
            mMagneticReader = WeiposImpl.as().openMagneticReader();
            if (mMagneticReader == null) {
                Utils.showToast(BuyShopReadActivity.this, "磁条卡读取服务不可用");
            }
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            //实现nfc卡的方法
            mNFCManager = NFCManager.getInstance();
            mNFCManager.init(this);
        }
    }

    protected void WangPosread() {
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "citiao")) {
            startTask();
        }
        if (SharedUtil.getSharedBData(BuyShopReadActivity.this, "nfc")) {
            //实现nfc卡的方法
            mNFCManager.setNFCListener(mNFCListener);
            mNFCManager.onResume(this);
        }
    }

    protected void WangPosclose() {
        stopTask();

    }

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
                    String r_code = object.getString("result_code");
                    if (code.equals("SUCCESS")) {
                        String cardNumber = object.getString("result_data").trim();
                        if (mIReadCardId != null) {
                            mIReadCardId.readCardNo(cardNumber, cardNo);
                            playSound();
                        } else {
                            Log.e("uuz", "请实现mIReadCardId接口");
                        }
                    } else {
                        if(r_code.equals("001")){
                            if (mIReadCardId != null) {
                                mIReadCardId.readCardNo("", cardNo);
                                playSound();
                            } else {
                                Log.e("uuz", "请实现mIReadCardId接口");
                            }
                        }
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

    public String getMagneticReaderInfo() {
        if (mMagneticReader == null) {
            Utils.showToast(BuyShopReadActivity.this, "初始化磁条卡sdk失败");
            return "";
        }
        // 刷卡后，主动获取磁卡的byte[]数据
        // byte[] cardByte = mMagneticReader.readCard();

        // String decodeData = mMagneticReader.getCardDecodeData();

        // 磁卡刷卡后，主动获取解码后的字符串数据信息
        String[] decodeData = mMagneticReader.getCardDecodeThreeTrackData();//
        if (decodeData != null && decodeData.length > 0) {
            /**
             * 1：刷会员卡返回会员卡号后面变动的卡号，前面为固定卡号（没有写入到磁卡中）
             * 如会员卡号：9999100100030318，读卡返回数据为00030318，前面99991001在磁卡中没有写入
             * 2：刷银行卡返回数据格式为：卡号=有效期。
             */
            String retStr = "";
            for (int i = 0; i < decodeData.length; i++) {
                if (decodeData[i] == null)
                    continue;
                String txt = decodeData[i].trim();
                if (retStr.length() > 0) {
                    retStr = retStr + "=";
                } else {
                    if (txt.indexOf("=") >= 0) {
                        String[] arr = txt.split("=");
                        if (arr[0].length() == 16 || arr[0].length() == 19) {
                            return arr[0];
                        }
                    }
                }
                retStr = retStr + txt;
            }
            return retStr;
        } else {
            // Toast.makeText(MainNewActivity.this, "获取磁条卡数据失败，请确保已经刷卡",
            // Toast.LENGTH_LONG).show();
            return "";
        }
    }

    private ReadMagTask mReadMagTask = null;

    private void startTask() {
        if (mReadMagTask == null) {
            mReadMagTask = new ReadMagTask();
            mReadMagTask.start();
        }
    }

    private void stopTask() {
        if (mReadMagTask != null) {
            mReadMagTask.interrupt();
            mReadMagTask = null;
        }
    }

    class ReadMagTask extends Thread implements Handler.Callback {
        private Handler H;
        private boolean isRun = false;

        public ReadMagTask() {
            H = new Handler(this);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            isRun = true;
            // 磁卡刷卡后，主动获取解码后的字符串数据信息
            try {
                while (isRun) {
                    String decodeData = getMagneticReaderInfo();
                    if (decodeData != null && decodeData.length() != 0) {
                        System.out.println("final============>>>" + decodeData);
                        Message m = H.obtainMessage(0);
                        m.obj = decodeData;
                        H.sendMessage(m);
                    }
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isRun = false;
            }
        }

        @Override
        public boolean handleMessage(Message msg) {
            /**
             * 1：刷会员卡返回会员卡号后面变动的卡号，前面为固定卡号（没有写入到磁卡中）
             * 如会员卡号：9999100100030318，读卡返回数据为00030318，前面99991001在磁卡中没有写入
             * 2：刷银行卡返回数据格式为：卡号=有效期。
             */
            // updateLogInfo("磁条卡内容：：" + msg.obj);
            String str = (String) msg.obj;
            if (str != null) {
                mIReadCardId.readCardNo(str, "");
                playSound();
            }

            // getCardUID(str);
            return false;
        }

    }

    /*************************************wangpos*********************************************/
}
