package com.ksk.obama.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ksk.obama.callback.IConnectCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.WangPos;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.system.AidlSystem;
import com.lkl.cloudpos.util.Debug;
import com.orhanobut.logger.Logger;

import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


/**
 * Created by Administrator on 2016/8/2.
 */
public abstract class BaseTypeActivity extends BaseActivity {

    protected IPrintErrorCallback errorCallback;
    protected IPrintSuccessCallback successCallback;

    public void setOnPrintSuccess(IPrintSuccessCallback successCallback) {
        this.successCallback = successCallback;
    }

    public void setOnPrintError(IPrintErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    private IConnectCallBack iConnectCallBack;

    public void setOnConnect(IConnectCallBack iConnectCallBack) {
        this.iConnectCallBack = iConnectCallBack;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (robotType) {
            case 1://拉卡拉
                bindService();
                break;
            case 3://商米
                initView();
                break;
            case 8:
                initWangPOS();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switch (robotType) {
            case 1://拉卡拉
                this.unbindService(conn);
                break;
            case 3://商米
                this.unbindService(connService);
                break;
        }
    }

    /**
     * 拉卡拉
     */
    public static final String LKL_SERVICE_ACTION = "lkl_cloudpos_mid_service";

    //设别服务连接桥
    private ServiceConnection conn = new ServiceConnection() {

        @SuppressLint("NewApi")
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            Debug.d("aidlService服务连接成功");
            if (serviceBinder != null) {    //绑定成功
                AidlDeviceService serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
                AidlSystem systemInf = null;
                try {
                    systemInf = AidlSystem.Stub.asInterface(serviceManager
                            .getSystemService());
                    terminalSn = systemInf.getSerialNo();
                    if (terminalSn != null) {
                        SharedUtil.setSharedData(BaseTypeActivity.this, "xlh", terminalSn);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BaseTypeActivity.this);
                        builder.setTitle("重要提示！！！");
                        builder.setMessage("获取不到设备序列号，请退出软件，重启设备后再试！！！");
                        builder.setPositiveButton("退出", null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                System.exit(0);
                            }
                        });
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                onDeviceConnected(serviceManager);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Debug.d("AidlService服务断开了");
        }
    };

    //绑定服务（拉卡拉）
    public void bindService() {
        final Intent intent = new Intent();
        intent.setAction(LKL_SERVICE_ACTION);
        final Intent eintent = new Intent(Utils.createExplicitFromImplicitIntent(this, intent));
        boolean flag = bindService(eintent, conn, Context.BIND_AUTO_CREATE);
        if (flag) {
            Debug.d("服务绑定成功");
        } else {
            Debug.d("服务绑定失败");
        }
    }

    public abstract void onDeviceConnected(AidlDeviceService serviceManager);

    /*****************************************************************************************/


    /**
     * SUNMI
     */
    private static final String TAG = "print";
    protected IWoyouService woyouService;

    protected ICallback callback = null;

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
            printerInit();
            if (iConnectCallBack != null) {
                iConnectCallBack.onConnected();
            }
        }
    };

    //商米
    private void initView() {
        terminalSn = SystemProperties.get("ro.serialno");
        callback = new ICallback.Stub() {

            @Override
            public void onRunResult(final boolean success) throws RemoteException {
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
            }

            @Override
            public void onRaiseException(int code, final String msg) throws RemoteException {
            }
        };
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    //打印机初始化（商米）
    public void printerInit() {
        if (woyouService != null) {
//            ThreadPoolManager.getInstance().executeTask(new Runnable() {
//
//                @Override
//                public void run() {
            try {
                woyouService.printerInit(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//                }
//            });
        }
    }

    /*****************************************************************************************/
    protected Printer printer = null;

    private void initWangPOS() {
        WeiposImpl.as().init(BaseTypeActivity.this, new Weipos.OnInitListener() {
            @Override
            public void onInitOk() {
                printer = WeiposImpl.as().openPrinter();
                setPrintListener();
                String deviceInfo = WeiposImpl.as().getDeviceInfo();
                Logger.json(deviceInfo);
                Logger.e(deviceInfo);
                WangPos wangPos = new Gson().fromJson(deviceInfo,WangPos.class);
               if(wangPos.getEn()!= null){
                   SharedUtil.setSharedData(BaseTypeActivity.this,"xlh",wangPos.getEn());
               }




            }

            @Override
            public void onError(String message) {
                Utils.showToast(BaseTypeActivity.this, message);
            }

            @Override
            public void onDestroy() {
                Log.e("djy", "print destory");
            }
        });
    }

    private void setPrintListener() {
        printer.setOnEventListener(new IPrint.OnEventListener() {
            @Override
            public void onEvent(final int what, String in) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "";
                        switch (what) {
                            case IPrint.EVENT_CONNECT_FAILD:
                                if (errorCallback != null) {
                                    errorCallback.OnPrintError();
                                } else {
                                    Log.e(TAG, "error");
                                }
                                message = "连接打印机失败";
                                break;
                            case IPrint.EVENT_PAPER_JAM:
                                if (errorCallback != null) {
                                    errorCallback.OnPrintError();
                                } else {
                                    Log.e(TAG, "error");
                                }
                                message = "打印机卡纸";
                                break;
                            case IPrint.EVENT_UNKNOW:
                                message = "打印机未知错误";
                                break;
                            case IPrint.EVENT_OK://
                                message = "打印完成";
                                // 回调函数中不能做UI操作，所以可以使用runOnUiThread函数来包装一下代码块
                                // 打印完成结束
                                Log.e(TAG, "run: ");
                                if (successCallback != null) {
                                    successCallback.OnPrintSuccess();
                                } else {
                                    Log.e(TAG, "实现successCallback接口");
                                }
                                break;
                            case IPrint.EVENT_NO_PAPER:
                                if (errorCallback != null) {
                                    errorCallback.OnPrintError();
                                } else {
                                    Log.e(TAG, "error");
                                }
                                message = "打印机缺纸";
                                break;
                            case IPrint.EVENT_HIGH_TEMP:
                                message = "打印机高温";
                                break;
                            case IPrint.EVENT_PRINT_FAILD:
                                if (errorCallback != null) {
                                    errorCallback.OnPrintError();
                                } else {
                                    Log.e(TAG, "error");
                                }
                                message = "打印失败";
                                break;
                        }
                        if (!TextUtils.isEmpty(message)) {
                            Utils.showToast(BaseTypeActivity.this, message);
                        }
                    }
                });
            }
        });
    }

}
