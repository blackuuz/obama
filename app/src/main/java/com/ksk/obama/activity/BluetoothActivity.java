package com.ksk.obama.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ksk.obama.R;
import com.ksk.obama.adapter.MyAdapter;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.model.BlueTool;
import com.ksk.obama.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    private ArrayList<BlueTool> mDeviceList = new ArrayList<BlueTool>();

    private BluetoothAdapter mBlueAdapter;

    public static final byte[][] byteCommands = {{0x1b, 0x40},// 复位打印机
            {0x1b, 0x4d, 0x00},// 标准ASCII字体
            {0x1b, 0x4d, 0x01},// 压缩ASCII字体
            {0x1d, 0x21, 0x00},// 字体不放大
            {0x1d, 0x21, 0x11},// 宽高加倍
            {0x1b, 0x45, 0x00},// 取消加粗模式
            {0x1b, 0x45, 0x01},// 选择加粗模式
            {0x1b, 0x7b, 0x00},// 取消倒置打印
            {0x1b, 0x7b, 0x01},// 选择倒置打印
            {0x1d, 0x42, 0x00},// 取消黑白反显
            {0x1d, 0x42, 0x01},// 选择黑白反显
            {0x1b, 0x56, 0x00},// 取消顺时针旋转90°
            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
            {0x1b, 0x69} // 选择顺时针旋转90°
    };
    private ListView lv;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initTitale();
        lv = (ListView) findViewById(R.id.lv);
        adapter = new MyAdapter(BluetoothActivity.this, mDeviceList);
        lv.setAdapter(adapter);

        // 获取蓝牙适配器
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        // 如果获取的蓝牙适配器为空 说明 该设备不支持蓝牙
        if (mBlueAdapter == null) {
            finish();
        } else {
            Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    BlueTool blueTool = new BlueTool();
                    blueTool.setName(device.getName());
                    blueTool.setAddress(device.getAddress());
                    blueTool.setMate(true);
                    mDeviceList.add(blueTool);
                }
            }

        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = mBlueAdapter.getRemoteDevice(mDeviceList.get(position).getAddress());
                ((MyApp) getApplication()).connectBluetoothPrint(mBlueAdapter, device);
            }
        });
        initIntentFilter();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("蓝牙设置");
        tv_print.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (hasAllPermissionsGranted(grantResults)) {
                    mBlueAdapter.startDiscovery();
                } else {
                    finish();
                }

                break;
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(receiver);
        mBlueAdapter = null;
    }

    /**
     * 蓝牙广播接收器
     */
    ProgressDialog progressDialog = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("onReceive: ", action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (isHave(device.getAddress())) {
                    BlueTool blueTool = new BlueTool();
                    blueTool.setName(device.getName());
                    blueTool.setAddress(device.getAddress());
                    mDeviceList.add(blueTool);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                progressDialog = ProgressDialog.show(context, "请稍等...",
                        "正在搜索蓝牙设备...", true);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                // 蓝牙设备搜索完毕
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                mBlueAdapter.cancelDiscovery();
                adapter.notifyDataSetChanged();
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Utils.showToast(BluetoothActivity.this, "蓝牙已关闭");
                        Log.e("123", "BluetoothAdapter.STATE_OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:

                        Log.e("123", "BluetoothAdapter.STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e("123", "BluetoothAdapter.STATE_ON");
                        Utils.showToast(BluetoothActivity.this, "蓝牙已打开");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.e("123", "BluetoothAdapter.STATE_TURNING_OFF");
                        break;
                }

            } else if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Utils.showToast(BluetoothActivity.this, "打印机连接成功");
                Log.e("123", device.getName() + " ACTION_ACL_CONNECTED");
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("123", device.getName() + " ACTION_ACL_DISCONNECTED");
            }
        }

    };

    private boolean isHave(String address) {
        for (int i = 0; i < mDeviceList.size(); i++) {
            if (mDeviceList.get(i).getAddress().indexOf(address) != -1) {
                return false;
            }
        }
        return true;
    }

    public void add(View view) {
        mDeviceList.clear();
        adapter.notifyDataSetChanged();
        if (!mBlueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                int permissionCheck = 0;
                permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionCheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                permissionCheck += this.checkSelfPermission(Manifest.permission.BLUETOOTH_PRIVILEGED);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    //注册权限
                    this.requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.BLUETOOTH_PRIVILEGED},
                            1001); //Any number
                } else {//已获得过权限
                    mBlueAdapter.startDiscovery();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            mBlueAdapter.startDiscovery();
        }
    }

    /**
     * 打印内容
     */
    public void printContent(View v) {
        OutputStream mOutputStream = ((MyApp) getApplication()).getOutputStream();
        // 如果连接成功
        if (mOutputStream != null) {
            try {
                //执行其他命令之前  先进行复位
                mOutputStream.write(byteCommands[0]);
                mOutputStream.write(byteCommands[4]);
                String title = "欢迎使用易德会员软件";
                byte[] contentData = title.getBytes("GB2312");
                mOutputStream.write(contentData, 0, contentData.length);
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Toast.makeText(this, "打印失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Utils.showToast(BluetoothActivity.this, "打印机未连接");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
