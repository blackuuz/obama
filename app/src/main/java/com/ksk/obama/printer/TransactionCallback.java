//package com.ksk.obama.printer;
//
//import android.os.Handler;
//import android.os.Message;
//import android.os.RemoteException;
//
//import com.sunmi.pay.hardware.aidl.TransactionListener;
//
//import java.util.Map;
//
///**
// * Created by xinle on 1/14/17.
// */
//
//
//public class TransactionCallback extends TransactionListener.Stub {
//
//    private Handler mHandler;
//
//
//    public TransactionCallback(Handler handler) {
//        this.mHandler = handler;
//    }
//
//
//    @Override
//    public void onWaitingForCardSwipe() throws RemoteException {
//
//    }
//
//    @Override
//    public int onCheckCardCompleted(int i, int i1, String s, String s1, Map hashMap) throws RemoteException {
//        Message message = Message.obtain();
//        CardInfo cardInfo = new CardInfo();
//        cardInfo.cardType = i;
//        cardInfo.cardStatus = i1;
//        cardInfo.PAN = s;
//        cardInfo.track2 = s1;
//        cardInfo.hashMap = hashMap;
//        message.obj = cardInfo;
//        mHandler.sendMessage(message);
//        return 0;
//    }
//
//    @Override
//    public void onTimeout() throws RemoteException {
//        mHandler.obtainMessage(4).sendToTarget();
//    }
//
//    @Override
//    public void onUpdateProcess(int i) throws RemoteException {
//        if (i < 0) {
//            mHandler.obtainMessage(9, 0, 0, i).sendToTarget();
//        } else if (i >= 0 && i <= 100) {
//            mHandler.obtainMessage(8, 0, 0, i).sendToTarget();
//        } else if (i == 10000) {
//            mHandler.obtainMessage(10).sendToTarget();
//        }
//    }
//
//    @Override
//    public int onInputPIN(int i, byte[] data) throws RemoteException {
//        return 0;
//    }
//
//    @Override
//    public int onGetECC(byte[] bytes, int i, int i1, byte[] bytes1, byte[] bytes2) throws RemoteException {
//        return 0;
//    }
//
//    @Override
//    public int onGetTerminalTag(byte[] tag) throws RemoteException {
//        return 0;
//    }
//
//    @Override
//    public int onKernelMessage(int type, int messageID, byte[] messageData) throws RemoteException {
//        return 0;
//    }
//
//    @Override
//    public int onAppSelect(int i, String[] strings) throws RemoteException {
//        return 0;
//    }
//
//    @Override
//    public void onDebug(String info) throws RemoteException {
//
//    }
//
//    @Override
//    public int onCheckCRL(String RID, String Index, String SN) throws RemoteException {
//        return 0;
//    }
//}
