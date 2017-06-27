package com.ksk.obama.application;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.ksk.obama.model.BlueTool;
import com.orhanobut.logger.Logger;

import org.litepal.LitePalApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyApp extends LitePalApplication {
    private BluetoothAdapter mBlueAdapter;
    private BluetoothSocket mBlueSocket;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private boolean mIsConnect = false;

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.getInstance().init(this);
        Logger.init("uuz");
        initBluetooth();
    }


    public void initBluetooth() {
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter != null && mBlueAdapter.isEnabled()) {
            BlueTool[] bts = new BlueTool[1];
            Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    BlueTool blueTool = new BlueTool();
                    blueTool.setName(device.getName());
                    blueTool.setAddress(device.getAddress());
                    blueTool.setMate(true);
                    bts[0] = blueTool;
                    break;
                }
                final BluetoothDevice device = mBlueAdapter.getRemoteDevice(bts[0].getAddress());
                // 蓝牙连接的uuid 注意：蓝牙设备连接必须使用该uuid 上面注释从源码中拷贝出来的
                // 可以看到 using the well-known SPP UUID
                // 00001101-0000-1000-8000-00805F9B34FB.
                // 而且别忘了给使用蓝牙的程序添加所需要的权限
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                try {
                    // 根据uuid 获取一个蓝牙socket
                    mBlueSocket = device.createRfcommSocketToServiceRecord(uuid);
                    // 进行连接
                    mBlueSocket.connect();
                    mInputStream = mBlueSocket.getInputStream();
                    mOutputStream = mBlueSocket.getOutputStream();
                    // 如果蓝牙还在搜索的话 则停止搜索 （蓝牙搜索比较耗资源）
                    if (mBlueAdapter.isDiscovering()) {
                        mBlueAdapter.cancelDiscovery();
                    }
                } catch (Exception e) {
                    mIsConnect = false;
                }
                mIsConnect = true;
            }
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBlueAdapter;
    }

    public BluetoothSocket getBlueSocket() {
        return mBlueSocket;
    }

    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public boolean isConnectBluetooth() {
        return mIsConnect;
    }

    public boolean connectBluetoothPrint(BluetoothAdapter blueAdapter, final BluetoothDevice device) {
        mBlueAdapter = blueAdapter;
        // 蓝牙连接的uuid 注意：蓝牙设备连接必须使用该uuid 上面注释从源码中拷贝出来的
        // 可以看到 using the well-known SPP UUID
        // 00001101-0000-1000-8000-00805F9B34FB.
        // 而且别忘了给使用蓝牙的程序添加所需要的权限
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            // 根据uuid 获取一个蓝牙socket
            mBlueSocket = device.createRfcommSocketToServiceRecord(uuid);
            // 进行连接
            mBlueSocket.connect();
            // 连接后获取输出流
            mOutputStream = mBlueSocket.getOutputStream();
//            mInputStream = mBlueSocket.getInputStream();
            // 如果蓝牙还在搜索的话 则停止搜索 （蓝牙搜索比较耗资源）
            if (mBlueAdapter.isDiscovering()) {
                mBlueAdapter.cancelDiscovery();
            }
            mIsConnect = true;
        } catch (Exception e) {
            mIsConnect = false;
        }
        return mIsConnect;
    }

    public void reset() {
        mBlueAdapter = null;
        mBlueSocket = null;
        mOutputStream = null;
        mIsConnect = false;
    }

    public void close() {
        mBlueAdapter = null;
        try {
            if (mBlueSocket != null) {
                mBlueSocket.close();
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
