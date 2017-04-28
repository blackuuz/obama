package com.ksk.obama.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ksk.obama.application.MyApp;

public class MyReceiver extends BroadcastReceiver {
    private MyApp app;

    public MyReceiver(MyApp app) {
        this.app = app;
    }

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e("djy", "BluetoothAdapter.STATE_OFF");
                    app.reset();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:

                    Log.e("djy", "BluetoothAdapter.STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e("djy", "BluetoothAdapter.STATE_ON");
                    app.initBluetooth();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e("djy", "BluetoothAdapter.STATE_TURNING_OFF");
                    break;
            }

        }
    }
}
