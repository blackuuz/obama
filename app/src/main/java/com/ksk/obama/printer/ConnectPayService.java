package com.ksk.obama.printer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.sunmi.pay.hardware.aidl.DeviceProvide;

/**
 * Created by KenMa on 2016/12/19.
 */
public class ConnectPayService {
    private Context mContext;
    private static ConnectPayService INSTANCE;

    public interface PayServiceConnected {
        void onServiceConnected(DeviceProvide mDeviceProvide);

        void onServiceDisconnected();
    }

    private PayServiceConnected mPayServiceConnected;
    private DeviceProvide mDeviceProvide;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDeviceProvide = DeviceProvide.Stub.asInterface(service);
            if (mPayServiceConnected != null) {
                mPayServiceConnected.onServiceConnected(mDeviceProvide);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDeviceProvide = null;
            if (mPayServiceConnected != null) {
                mPayServiceConnected.onServiceDisconnected();
            }
        }
    };

    private ConnectPayService(Context context) {
        this.mContext = context;
    }

    public static ConnectPayService Init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ConnectPayService(context);
        }
        return INSTANCE;
    }

    public void connectPayService(PayServiceConnected mPayServiceConnected) {
        this.mPayServiceConnected = mPayServiceConnected;
        Intent intent = new Intent("sunmi.intent.action.PAY_HARDWARE");
        intent.setPackage("com.sunmi.pay.hardware");
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBind(Context context) {
        context.unbindService(mServiceConnection);
    }

    public DeviceProvide getDeviceProvide() {
        return mDeviceProvide;
    }
}
