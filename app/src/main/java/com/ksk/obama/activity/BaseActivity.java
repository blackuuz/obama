package com.ksk.obama.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.ksk.obama.R;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.model.IntegralShopCount;
import com.ksk.obama.model.LoginData;
import com.ksk.obama.utils.HttpTools;
import com.ksk.obama.utils.MyDialog;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.ksk.obama.zxing.MipcaActivityCapture;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.weipass.pos.sdk.impl.WeiposImpl;

import static com.ksk.obama.utils.SharedUtil.getSharedBData;


/**
 * AutoLayoutActivity : 屏幕适配类
 *
 * Created by Administrator on 2016/7/27.
 */
public class BaseActivity extends AutoLayoutActivity {
    public static List<BuyCount> list_buy = new ArrayList<>();
    public static List<LoginData.RechargefastBean> list_rechargefast = new ArrayList<>();
    public static List<IntegralShopCount> list_integral = new ArrayList<>();
    private InputMethodManager manager;
    protected MyDialog loadingDialog;
    private IHttpCallBack callBack;
    protected boolean isclick_pay = true;
    protected String terminalSn;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    if (callBack != null) {
                        callBack.OnFail(str);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    Utils.showToast(BaseActivity.this, str);
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;

                case 110:
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (callBack != null) {
                        callBack.OnSucess(str);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    break;

                case -12306:
                    Utils.showToast(BaseActivity.this, str);
                    break;
            }
        }
    };

    private static Timer mTimer; // 计时器，每1秒执行一次任务
    private static MyTimerTask mTimerTask; // 计时任务，判断是否未操作时间到达5s
    private static long mLastActionTime; // 上一次操作时间
    public static long curTime = 0;
    protected boolean isBluetooth = false;

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // 5s未操作
            if (System.currentTimeMillis() - mLastActionTime > curTime) {
                // 退出登录
                exit();
                // 停止计时任务
                stopTimer();
            }
        }
    }

    // 退出登录
    protected void exit() {
        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
    }

    // 登录成功，开始计时
    protected void startTimer() {
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        // 初始化上次操作时间为登录成功的时间
        mLastActionTime = System.currentTimeMillis();
        // 每过1s检查一次
        mTimer.schedule(mTimerTask, 0, 1000);
        Log.e("timer", "start timer");
    }

    // 停止计时任务
    protected static void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            Log.e("timer", "cancel timer");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLastActionTime = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }


    protected boolean isXJ = false;
    protected float shopIntegral = 0;
    protected int robotType = 4;

    private String money = "";
    private int type = 0;
    private String orderNo = "";
    private String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String str = SharedUtil.getSharedData(BaseActivity.this, "robotType");
        if (str.equals("1")) {
            robotType = 1;
        } else if (str.equals("3")) {
            robotType = 3;
        } else if (str.equals("4")) {
            robotType = 4;
        } else if (str.equals("30")) {
            robotType = 3;
        } else if (str.equals("8")) {
            robotType = 8;
        }
        switch (robotType) {

            case 1:
            case 8:
                terminalSn = SharedUtil.getSharedData(BaseActivity.this, "xlh");
                break;

            case 3:
                terminalSn = SystemProperties.get("ro.serialno");
                SharedUtil.setSharedData(BaseActivity.this, "xlh", terminalSn);
                break;

            case 4:
                terminalSn = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                        .getDeviceId();
                SharedUtil.setSharedData(BaseActivity.this, "xlh", terminalSn);
                break;
        }
        isBluetooth = SharedUtil.getSharedBData(BaseActivity.this, "bluetooth");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingDialog = new MyDialog(BaseActivity.this, R.style.loading_dialog);
        isXJ = getSharedBData(BaseActivity.this, "payxj");
        String jf = SharedUtil.getSharedData(BaseActivity.this, "shopintegral");
        if (TextUtils.isEmpty(jf)) {
            shopIntegral = 0;
        } else {
            shopIntegral = Float.parseFloat(jf);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBluetooth) {
            BluetoothAdapter bluetoothAdapter = ((MyApp) getApplication()).getBluetoothAdapter();
            if (bluetoothAdapter == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle("警告！");
                builder.setMessage("蓝牙未打开或者本机不支持蓝牙功能，无法使用打印功能");
                builder.setPositiveButton("设置连接打印机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(BaseActivity.this, BluetoothActivity.class));
                    }
                });
                builder.setNegativeButton("继续使用", null);
                builder.show();
            } else {
                OutputStream outputStream = ((MyApp) getApplication()).getOutputStream();
                if (outputStream == null) {
                    alertBluetooht();
                }
            }
        }
    }

    private void alertBluetooht() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("警告！");
        builder.setMessage("蓝牙打印机未连接，是否现在设置连接");
        builder.setPositiveButton("现在设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(BaseActivity.this, BluetoothActivity.class));
            }
        });
        builder.show();
    }

    private IPayCallBack iPayCallBack;

    public void setOnPayCallBack(IPayCallBack iPayCallBack) {
        this.iPayCallBack = iPayCallBack;
    }

    protected void payMoney(int n, String money, String orderDesc, String str2) {
        switch (robotType) {
            case 1:
                if (n == 1 && getSharedBData(BaseActivity.this, "paycode")) {
                    newPay(n, money, orderDesc);
                } else if (n == 3 && getSharedBData(BaseActivity.this, "paybank")) {
                    newPay(n, money, orderDesc);
                } else {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "没有开通此功能");
                }
                break;

            case 3:
            case 4:
            case 8:
                if (n == 1 && getSharedBData(BaseActivity.this, "paywx")) {
                    newPay(n, money, orderDesc, str2);
                } else if (n == 2 && getSharedBData(BaseActivity.this, "payal")) {
                    newPay(n, money, orderDesc, str2);
                } else {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "没有开通此功能");
                }
                break;
        }
    }

    //sunmi
    private void newPay(int n, String str, String order, String str2) {
        if (!TextUtils.isEmpty(str) && Float.parseFloat(str) > 0) {
            money = str;
            type = n;
            orderNo = order;
            title = str2;

            Intent intent = new Intent(BaseActivity.this, MipcaActivityCapture.class);
            intent.putExtra("isPay", true);
            intent.putExtra("type1", type + "");
            intent.putExtra("groupId", SharedUtil.getSharedData(BaseActivity.this, "groupid"));
            intent.putExtra("goods", title);
            intent.putExtra("money", money);
            intent.putExtra("order", orderNo);
            startActivityForResult(intent, 10087);
        } else {
            isclick_pay = true;
            Utils.showToast(BaseActivity.this, "请填写充值金额");
        }
    }

    //lkl
    private void newPay(int n, String money, String orderDesc) {
        if (!TextUtils.isEmpty(money) && Float.parseFloat(money) > 0) {
            Intent intent = new Intent();
            ComponentName componet = new ComponentName("com.lkl.cloudpos.payment",
                    "com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Bundle b = new Bundle();
            b.putString("proc_tp", "00");
            b.putString("pay_tp", n == 3 ? "0" : "1");
            b.putString("proc_cd", n == 3 ? "000000" : "660000");
            b.putString("amt", money);
            b.putString("msg_tp", "0200");
            b.putString("order_no", System.currentTimeMillis() + "");
            b.putString("appid", "com.ksk.obama");
            b.putString("time_stamp", System.currentTimeMillis() + "");
            b.putString("order_info", orderDesc + money);
            //备注信息
            b.putString("print_info", orderDesc + money);
            intent.setComponent(componet);
            intent.putExtras(b);
            startActivityForResult(intent, n);
        } else {
            isclick_pay = true;
            Utils.showToast(BaseActivity.this, "请填写充值金额");
        }
    }

    private IQrcodeCallBack iQrcodeCallBack;

    public void setOnReadQrcode(IQrcodeCallBack iQrcodeCallBack) {
        this.iQrcodeCallBack = iQrcodeCallBack;
    }

    protected void toQrcodeActivity() {
        if (getSharedBData(BaseActivity.this, "saoma")) {
            Intent intent = new Intent(BaseActivity.this, MipcaActivityCapture.class);
            intent.putExtra("isPay", false);
            startActivityForResult(intent, 10086);
        } else {
            isclick_pay = true;
            Utils.showToast(BaseActivity.this, "没有开通此功能");
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    isclick_pay = true;
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;

                case 110:
                    isclick_pay = true;
                    try {
                        JSONObject object = new JSONObject(str);
                        String code = object.getString("result");
                        if (code.equals("SUCCESS")) {
                            String order = object.getString("memoBillNum");
                            if (iPayCallBack != null) {
                                iPayCallBack.OnPaySucess(order, type);
                            } else {
                                Log.e("djy", "请实现IPayCallBack接口");
                            }
                        } else {
                            String msg1 = object.getString("result_msg");
                            Utils.showToast(BaseActivity.this, msg1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };

    private ICreateOrderNumber iCreateOrderNumber;

    public void setOnCrateOrderNumber(ICreateOrderNumber iCreateOrderNumber) {
        this.iCreateOrderNumber = iCreateOrderNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isclick_pay = true;
        if (resultCode == Activity.RESULT_CANCELED) {//交易取消
            switch (requestCode) {
                case 1:
                case 3:
                case 10087:
                    if (iCreateOrderNumber != null) {
                        iCreateOrderNumber.OnCreateOrderNumber(true);
                    }
                    Utils.showToast(BaseActivity.this, "交易取消");
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            Logger.e(resultCode + " *** " + data.toString());
            String orderidScan = "";
            switch (requestCode) {
                case 1:
                    requestCode = Integer.parseInt(data.getExtras().getString("pay_tp"));
                    String txndetail = data.getExtras().getString("txndetail");
                    if (txndetail != null && txndetail.length() != 0) {
                        JSONObject optJSONObject;
                        try {
                            optJSONObject = new JSONObject(txndetail);
                            //订单号
                            orderidScan = optJSONObject.optString("orderid_scan");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (iPayCallBack != null) {
                        iPayCallBack.OnPaySucess(orderidScan, requestCode);
                    } else {
                        Utils.showToast(BaseActivity.this, "接口错误");
                    }
                    Utils.showToast(BaseActivity.this, "支付成功");
                    break;
                case 3:
                    orderidScan = data.getExtras().getString("refernumber");//参考号
                    if (iPayCallBack != null) {
                        iPayCallBack.OnPaySucess(orderidScan, requestCode);
                    } else {
                        Utils.showToast(BaseActivity.this, "接口错误");
                    }
                    Utils.showToast(BaseActivity.this, "支付成功");
                    break;
                case 10086://扫码结果
                    String number = data.getExtras().getString("result");
                    if (number != null) {
                        if (iQrcodeCallBack != null) {
                            iQrcodeCallBack.OnReadQrcode(number);
                        } else {
                            Log.e("djy", "请实现IQrcodeCallBack接口");
                        }
                    }
                    break;
                case 10087://扫码结果
                    int type0 = data.getExtras().getInt("type");
                    switch (type0) {
                        case 0://扫码主动
                            String id = data.getExtras().getString("result");
                            Log.e("uuz", "扫码结果:" + id);
                            if (id != null) {
                                loadingDialog.show();
                                Map<String, String> map = new HashMap<>();
                                map.put("authCode", id);
                                map.put("type", type + "");
                                map.put("payStyle", "bar");
                                map.put("groupId", SharedUtil.getSharedData(BaseActivity.this, "groupid"));
                                map.put("goods", title);
                                map.put("money", money);
                                map.put("order", orderNo);
                                Log.e("HTTP1",orderNo+"");
//                                HttpTools.postMethod(handler, NetworkUrl.PAYCODE, map);
                               postToHttp(NetworkUrl.PAYCODE, map, new IHttpCallBack() {
                                    @Override
                                    public void OnSucess(String jsonText) {
                                        Log.e("djy",jsonText);
                                        try {
                                            JSONObject object1 = new JSONObject(jsonText);
                                            String tag = object1.getString("result");
                                            if (tag.equals("SUCCESS")) {
                                                Log.e("djy","成功"+jsonText);
                                                Utils.showToast(BaseActivity.this, "支付成功");
                                                if (iPayCallBack != null) {
                                                    iPayCallBack.OnPaySucess(orderNo,type);
                                                } else {
                                                    Log.e("djy", "请实现IPayCallBack接口");
                                                }
                                            }else if(tag.equals("FAIL")){
                                                Log.e("djy","失败"+jsonText);
                                                JSONObject object2 = new JSONObject(jsonText);
                                                String tag2 = object1.getString("result_msg");
                                                Utils.showToast(BaseActivity.this, ""+tag2);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("djy","异常"+jsonText);
                                        }
                                       // Log.e("djy","成功"+jsonText);
                                    }
                                    @Override
                                    public void OnFail(String message) {
                                        Log.e("djy","失败*"+message);
                                    }
                                });
                                HttpTools.postMethod(handler, NetworkUrl.PAYCODE, map);
                                Log.e("djy",map.toString());
                            } else {
                                Utils.showToast(BaseActivity.this, "获取二维码信息失败,请重试");
                            }
                            break;
                        case 1://被扫码
                            String memoBillNum = data.getExtras().getString("memoBillNum");
                            if (iPayCallBack != null) {
                                iPayCallBack.OnPaySucess(memoBillNum,type);

                            } else {
                                Log.e("djy", "请实现IPayCallBack接口");
                            }
                            break;
                    }
                    break;
            }

        }
        if (resultCode == -2) {//交易失败
            switch (requestCode) {
                case 1:
                case 3:
                    if (iCreateOrderNumber != null) {
                        iCreateOrderNumber.OnCreateOrderNumber(false);
                    }
                    Utils.showToast(BaseActivity.this, "交易失败");
                    break;
            }
        }
    }

    public static final String KEY = "82BA9000F8EF8ABAC93C2569B62AB3C5";

    protected void postToHttp(String url, Map<String, String> map, IHttpCallBack IHttpCallBack) {
        if (!loadingDialog.isShowing() && hasWindowFocus()) {
            loadingDialog.show();
        }

        int n = (int) (Math.random() * (Integer.MAX_VALUE));
        long time = System.currentTimeMillis() / 1000;
        String sign = Utils.getMD5Code(n + KEY + time).toUpperCase();
        map.put("nonceStr", n + "");
        map.put("sign", sign);
        map.put("timeStamp", time + "");
        map.put("groupId", SharedUtil.getSharedData(BaseActivity.this, "groupid"));
        map.put("equipmentType", robotType + "");
        map.put("equipmentNum", terminalSn);
        callBack = IHttpCallBack;
        HttpTools.postMethod(mHandler, url, map);
    }

    protected void postToHttp(String url, Map<String, String> map, IHttpCallBack IHttpCallBack, int nn) {
        int n = (int) (Math.random() * (Integer.MAX_VALUE));
        long time = System.currentTimeMillis() / 1000;
        String sign = Utils.getMD5Code(n + KEY + time).toUpperCase();
        map.put("nonceStr", n + "");
        map.put("sign", sign);
        map.put("timeStamp", time + "");
        map.put("groupId", SharedUtil.getSharedData(BaseActivity.this, "groupid"));
        map.put("equipmentType", robotType + "");
        map.put("equipmentNum", terminalSn);
        callBack = IHttpCallBack;
        HttpTools.postMethod(mHandler, url, map);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switch (robotType) {
            case 8:
                WeiposImpl.as().destroy();
                break;
        }
        stopTimer();
        HttpTools.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //service是否活着
    protected boolean isServiceRunning(String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    protected String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */
    protected boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState() + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
