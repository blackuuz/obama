package com.ksk.obama.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.model.GoodsStock;
import com.ksk.obama.model.IntegralShopCount;
import com.ksk.obama.model.LoginData;
import com.ksk.obama.model.WangPosPAY;
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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.weipass.biz.nfc.CashierSign;
import cn.weipass.pos.sdk.BizServiceInvoker;
import cn.weipass.pos.sdk.impl.WeiposImpl;
import cn.weipass.service.bizInvoke.RequestInvoke;
import cn.weipass.service.bizInvoke.RequestResult;

import static com.ksk.obama.utils.SharedUtil.getSharedBData;


/**
 * AutoLayoutActivity : 屏幕适配类
 * <p>
 * Created by Administrator on 2016/7/27.
 */
public class BaseActivity extends AutoLayoutActivity {
    public static List<BuyCount> list_buy = new ArrayList<>();
    public static List<GoodsStock> list_stock = new ArrayList<>();
    public static List<LoginData.RechargefastBean> list_rechargefast = new ArrayList<>();
    public static List<IntegralShopCount> list_integral = new ArrayList<>();
    private InputMethodManager manager;
    protected MyDialog loadingDialog;
    private Dialog mdialog;
    private IHttpCallBack callBack;
    protected boolean isclick_pay = true;

    protected String shopId = "";
    protected String terminalSn;
    protected Bitmap bmp;



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

            case 8:
                terminalSn = SharedUtil.getSharedData(BaseActivity.this, "xlh");
                if (terminalSn == null) {
                    terminalSn = "en码获取失败";
                }
                break;
        }
        isBluetooth = SharedUtil.getSharedBData(BaseActivity.this, "bluetooth");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingDialog = new MyDialog(BaseActivity.this, R.style.loading_dialog);
        isXJ = getSharedBData(BaseActivity.this, "payxj");
        shopId = SharedUtil.getSharedData(BaseActivity.this,"shopid");
        String jf = SharedUtil.getSharedData(BaseActivity.this, "shopintegral");
        if (TextUtils.isEmpty(jf)) {
            shopIntegral = 0;
        } else {
            shopIntegral = Float.parseFloat(jf);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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


    /**
     * @param n         付款方式 支付方式 现金——0   ,微信——1   ,支付宝——2  , 第三方——3,  会员卡——4
     * @param money     金额
     * @param orderDesc 订单号
     * @param str2      付款页面
     *                  <p> 点击支付选项 调用次方法  2017-6-27  <p/>
     *                  <p>所有页面的支付选项 都会先调用此方法   </p>
     */
    protected void payMoney(final int n, final String money, final String orderDesc, String str2) {
        switch (robotType) {
            case 1://拉卡拉
                switch (n) {
                    case 3://第三方支付
                        mdialog = new Dialog(BaseActivity.this, R.style.BottomDialogStyle);
                        //填充对话框的布局
                        View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.dialog_base_sm_yl, null);
                        //初始化控件
                        TextView tv_sm = (TextView) view.findViewById(R.id.dia_sm);
                        TextView tv_yl = (TextView) view.findViewById(R.id.dia_yl);
                        tv_sm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newPay(1, money, orderDesc); //1 扫码
                                isclick_pay = false;
                                mdialog.dismiss();
                            }
                        });
                        tv_yl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newPay(3, money, orderDesc);// 3 为银联
                                isclick_pay = false;
                                mdialog.dismiss();
                            }
                        });
                        //将布局设置给Dialog
                        mdialog.setContentView(view);
                        //获取当前Activity所在的窗体
                        Window dialogWindow = mdialog.getWindow();
                        //设置Dialog从窗体底部弹出
                        dialogWindow.setGravity(Gravity.BOTTOM);
                        //获得窗体的属性
                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        lp.width = (int) (dm.widthPixels * 0.95);
                        lp.y = 300; //设置Dialog距离底部的距离
                        dialogWindow.setAttributes(lp); //将属性设置给窗体
                        mdialog.show();//显示对话框
                        //mdialog.setOutsideTouchable(false);
                        mdialog.setCancelable(false);
                        break;
                    case 1://微信支付
                    case 2://支付宝支付
                        newPay(n, money, orderDesc, str2);//调用摄像头扫码支付
                        break;
                }
                break;
            case 3://商米
                if (n == 3) {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "第三方支付尚未开通");
                } else if (n == 1 || n == 2) {
                    newPay(n, money, orderDesc, str2);
                } else {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "没有开通此功能");
                }
                break;
            case 4://手机

                if (n == 3) {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "第三方支付尚未开通");
                } else if (n == 1 || n == 2) {
                    newPay(n, money, orderDesc, str2);
                } else {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "没有开通此功能");
                }
                break;
            case 8://旺POS
                if (n == 3) {
                    requestCashier(n, money, orderDesc);
                } else if (n == 1 || n == 2) {
                    newPay(n, money, orderDesc, str2);
                } else {
                    isclick_pay = true;
                    Utils.showToast(BaseActivity.this, "没有开通此功能");
                }
        }
    }


    /**
     * @param n     付款方式 支付方式 现金——0   , 微信——1   ,支付宝——2  ,  第三方——3, 会员卡——4
     * @param str   金额
     * @param order 订单号
     * @param str2  付款页面
     *              <p> 点击支付选项 调用此方法  2017-6-28 <p/>
     *              <p> 微信、支付宝 调用摄像机扫码 或生成码的方法{通用}<p/>
     */

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
            Utils.showToast(BaseActivity.this, "金额不能为空");
        }
    }


    /**
     * @param n         付款方式
     * @param money     付款金额
     * @param orderDesc 订单号
     *                  <p>拉卡拉官方支付接口  包含扫码 和银联 </p>
     *                  <p>2017-06-28</p>
     */
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
            Utils.showToast(BaseActivity.this, "金额不能为空");
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
                                Log.e("uuz", "请实现IPayCallBack接口");
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
            Logger.e(resultCode + "-" + data.toString());
            String orderidScan = "";
            switch (requestCode) {
                case 1:// pay_tp 1-微信 2-支付宝 3-银联钱包   4-百度钱包 5-京东钱包
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
                        iPayCallBack.OnPaySucess(orderidScan, 3);//3表示第三方支付
                    } else {
                        Utils.showToast(BaseActivity.this, "接口错误");
                    }
                    Utils.showToast(BaseActivity.this, "支付成功");
                    break;
                case 3://拉卡拉银行卡支付回调 返回信息
                    orderidScan = data.getExtras().getString("refernumber");//参考号
                    if (iPayCallBack != null) {
                        iPayCallBack.OnPaySucess(orderidScan, 3);
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
                            Log.e("uuz", "请实现IQrcodeCallBack接口");
                        }
                    }
                    break;
                case 10087://支付扫码结果
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
                                Log.e("HTTP1", orderNo + "");
//                                HttpTools.postMethod(handler, NetworkUrl.PAYCODE, map);
                                postToHttp(NetworkUrl.PAYCODE, map, new IHttpCallBack() {
                                    @Override
                                    public void OnSucess(String jsonText) {
                                        Log.e("uuz", jsonText);
                                        try {
                                            JSONObject object1 = new JSONObject(jsonText);
                                            String tag = object1.getString("result");
                                            if (tag.equals("SUCCESS")) {
                                                Log.e("uuz", "成功" + jsonText);
                                                Utils.showToast(BaseActivity.this, "支付成功");
                                                if (iPayCallBack != null) {
                                                    Log.e("uuz", "OnSucess: +++++++++++" + type);
                                                    iPayCallBack.OnPaySucess(orderNo, type);
                                                } else {
                                                    Log.e("uuz", "请实现IPayCallBack接口");
                                                }
                                            } else if (tag.equals("FAIL")) {

                                                Log.e("uuz", "失败" + jsonText);
                                                JSONObject object2 = new JSONObject(jsonText);
                                                String tag2 = object1.getString("result_msg");
                                                Utils.showToast(BaseActivity.this, "" + tag2);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("uuz", "异常" + jsonText);
                                        }
                                        // Log.e("djy","成功"+jsonText);
                                    }

                                    @Override
                                    public void OnFail(String message) {
                                        Log.e("uuz", "失败*" + message);
                                    }
                                });
                                HttpTools.postMethod(handler, NetworkUrl.PAYCODE, map);
                                Log.e("uuz", map.toString());
                            } else {
                                Utils.showToast(BaseActivity.this, "获取二维码信息失败,请重试");
                            }
                            break;
                        case 1://被扫码
                            String memoBillNum = data.getExtras().getString("memoBillNum");
                            if (iPayCallBack != null) {
                                iPayCallBack.OnPaySucess(memoBillNum, type);
                                Log.e("uuz", "OnSucess: +++++++++++" + type);
                            } else {
                                Log.e("uuz", "请实现IPayCallBack接口");
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


    private ProgressDialog pd = null;
    private BizServiceInvoker mBizServiceInvoker;


    /**
     * @param n         支付方式
     * @param money     支付金额
     * @param orderDesc 订单号
     *                  <p> 旺POS 官方支付接口调用</p>
     */
    private void requestCashier(int n, String money, String orderDesc) {
        if (!TextUtils.isEmpty(money) && Float.parseFloat(money) > 0) {
            // 1003 微信
            // 1004 支付宝
            // 1006 银行卡
            switch (n) {
                case 3:
                    pay_type = "1003";
                    break;
                default:
                    pay_type = "1003";
                    break;
            }
            if (money == "") {
                Utils.showToast(BaseActivity.this, "金额不能为空");

                return;
            }
            String s[] = (Utils.getNumStr(Float.parseFloat(money) * 100)).split("\\.");
            total_fee = s[0];
            backClassPath = "com.ksk.obama.activity." + getRunningActivityName();//uuz 动态获取当前运行Activity 的名字
            Log.d("8268", "innerRequestCashier: " + backClassPath + "===" + s[0]);
            tradeNo = orderDesc;


            try {
                // 初始化服务调用
                mBizServiceInvoker = WeiposImpl.as().getService(BizServiceInvoker.class);
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (mBizServiceInvoker == null) {
                Toast.makeText(this, "初始化服务调用失败", Toast.LENGTH_SHORT).show();
                return;
            }
            // 设置请求订阅服务监听结果的回调方法
            mBizServiceInvoker.setOnResponseListener(mOnResponseListener);
            innerRequestCashier();
        } else {
            isclick_pay = true;
            Utils.showToast(BaseActivity.this, "金额不能为空");
        }
    }

    //业务demo在bp平台中的的bpid，这里填写对应应用所属bp账号的bpid和对应的key--------------需要动态改变
    //private String InvokeCashier_BPID="53b3a1ca45ceb5f96d153eec";
    // private String InvokeCashier_KEY="LIz6bPS2z8jUnwLHRYzcJ6WK2X87ziWe";

    private String InvokeCashier_BPID = "58db1762eb670024651d6535";
    private String InvokeCashier_KEY = "JHj3HKD2fAeC5L3gTM8zrCuijVuztz9J";
    // 1001 现金
    // 1003 微信
    // 1004 支付宝
    // 1005 百度钱包
    // 1006 银行卡
    // 1007 易付宝
    // 1009 京东钱包
    // 1011 QQ钱包
    private String pay_type = "1004";
    // 第三方订单流水号，非空,发起请求，tradeNo不能相同，相同在收银会提示有存在订单
    private String tradeNo = System.currentTimeMillis() + "";
    private String channel = "POS";//标明是pos调用，不需改变
    private String body = "消费";//订单body描述信息 ，不可空
    private String attach = "无";//备注信息，可空，订单信息原样返回，可空
    private String total_fee = "1";//支付金额，单位为分，1=0.01元，100=1元，不可空
    private String seqNo = "1";//服务端请求序列,本地应用调用可固定写死为1
    // 如果需要页面调回到自己的App，需要在调用中增加参数package和classpath(如com.xxx.pay.ResultActivity)，并且这个跳转的Activity需要在AndroidManifest.xml中增加android:exported=”true”属性。
    // 如果不需要回调页面，则backPkgName和backClassPath需要同时设置为空字符串 ："";
    private String backPkgName = "com.ksk.obama";//，可空
    private String backClassPath = "";//，可空
    //指定接收收银结果的url地址默认为："http://apps.weipass.cn/pay/notify"，填写自己服务器接收地址
    private String notifyUrl = "";//，可空

    // 通过context来获取 Activity：
    private String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }


    // 1.执行调用之前需要调用WeiposImpl.as().init()方法，保证sdk初始化成功。
    //
    // 2.调用收银支付成功后，收银支付结果页面完成后，BizServiceInvoker.OnResponseListener后收到响应的结果
    //
    // 3.如果需要页面调回到自己的App，需要在调用中增加参数package和classpath(如com.xxx.pay.ResultActivity)，并且这个跳转的Activity需要在AndroidManifest.xml中增加android:exported=”true”属性。
    private void innerRequestCashier() {

        try {
            RequestInvoke cashierReq = new RequestInvoke();
            cashierReq.pkgName = this.getPackageName();
            cashierReq.sdCode = CashierSign.Cashier_sdCode;// 收银服务的sdcode信息
            cashierReq.bpId = InvokeCashier_BPID;
            cashierReq.launchType = CashierSign.launchType;
            cashierReq.params = CashierSign.sign(InvokeCashier_BPID, InvokeCashier_KEY, channel,
                    pay_type, tradeNo, body, attach, total_fee, backPkgName, backClassPath, notifyUrl);
            cashierReq.seqNo = seqNo;

            RequestResult r = mBizServiceInvoker.request(cashierReq);
            Log.i("requestCashier", r.token + "," + r.seqNo + "," + r.result);
            // 发送调用请求
            if (r != null) {
                Log.d("requestCashier", "request result:" + r.result + "|launchType:" + cashierReq.launchType);
                String err = null;
                switch (r.result) {
                    case BizServiceInvoker.REQ_SUCCESS: {
                        // 调用成功
                        Toast.makeText(this, "收银服务调用成功", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BizServiceInvoker.REQ_ERR_INVAILD_PARAM: {
                        Toast.makeText(this, "请求参数错误！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BizServiceInvoker.REQ_ERR_NO_BP: {
                        Toast.makeText(this, "未知的合作伙伴！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BizServiceInvoker.REQ_ERR_NO_SERVICE: {
                        //调用结果返回，没有订阅对应bp账号中的收银服务，则去调用sdk主动订阅收银服务
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (pd == null) {
                                    pd = new ProgressDialog(BaseActivity.this);
                                }
                                pd.setMessage("正在申请订阅收银服务...");
                                pd.show();
                                // 如果没有订阅，则主动请求订阅服务
                                mBizServiceInvoker.subscribeService(CashierSign.Cashier_sdCode,
                                        InvokeCashier_BPID);
                            }
                        });
                        break;
                    }
                    case BizServiceInvoker.REQ_NONE: {
                        Toast.makeText(this, "请求未知错误！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (err != null) {
                    Log.w("requestCashier", "serviceInvoker request err:" + err);
                }
            } else {
                Toast.makeText(this, "请求结果对象为空！", Toast.LENGTH_SHORT).show();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 这个是服务调用完成后的响应监听方法
     */
    private BizServiceInvoker.OnResponseListener mOnResponseListener = new BizServiceInvoker.OnResponseListener() {

        @Override
        public void onResponse(String sdCode, String token, byte[] data) {
            // 收银服务调用完成后的返回方法
            String orderidScan = "";
            Log.d("uuz", new String(data));
            Log.e("uuz", "sdCode = " + sdCode + " , token = " + token + " , data = " + new String(data));
            String json = new String(data);
            Logger.json(json);
            WangPosPAY wData = new Gson().fromJson(json, WangPosPAY.class);
            if (wData != null) {
                Utils.showToast(BaseActivity.this, wData.getErrMsg());
                if (wData.getErrCode().equals("-1")) {//交易取消
                    if (iCreateOrderNumber != null) {
                        iCreateOrderNumber.OnCreateOrderNumber(true);
                    }
                } else {
                    orderidScan = wData.getCashier_trade_no();
                    if (iPayCallBack != null) {
                        int type = -1;
                        if (wData.getPay_type() != null) {//type 值为付款方式 但是现在只设定为“3” 表示第三方支付
                            iPayCallBack.OnPaySucess(orderidScan, 3);
                        }
                    } else {
                        Utils.showToast(BaseActivity.this, "接口错误");
                    }
                }
            }
            /* new String(data) 为支付结果json，内容样式为:
               {
			    "errCode": "-1",
			    "errMsg": "取消交易",
			    "out_trade_no": "1474442791311",
			    "trade_status": null,
			    "input_charset": "UTF-8",
			    "cashier_trade_no": null,
			    "pay_type": null,
			    "pay_info": "取消交易"
			}*/
            if (pd != null) {
                pd.hide();
            }

        }

        @Override
        public void onFinishSubscribeService(boolean result, String err) {
            // TODO Auto-generated method stub
            // 申请订阅收银服务结果返回
            if (pd != null) {
                pd.hide();
            }
            // bp订阅收银服务返回结果
            if (!result) {
                //订阅失败
                Toast.makeText(BaseActivity.this, err, Toast.LENGTH_SHORT).show();
            } else {
                //订阅成功
                Toast.makeText(BaseActivity.this, "订阅收银服务成功，请按home键回调主页刷新订阅数据后重新进入调用收银", Toast.LENGTH_SHORT).show();
            }
        }
    };


}
