package com.ksk.obama.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.broadcast.MyReceiver;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.LoginData;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.NoDoubleClickListener;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseTypeActivity {

    private EditText et_gid;
    private EditText et_id;
    private EditText et_pw;
    private Button btn_login;
    private String username;
    private String employeeNo;
    private LoginData loginData;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopTimer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Log.e("onCreate: ", "dp:" + Utils.px2dip(this, getWindowManager().getDefaultDisplay().getWidth()));
        setContentView(R.layout.activity_login);
        receiver = new MyReceiver((MyApp) getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, intentFilter);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        et_gid = (EditText) findViewById(R.id.login_gid);
        et_id = (EditText) findViewById(R.id.login_id);
        et_pw = (EditText) findViewById(R.id.login_pw);
        btn_login = (Button) findViewById(R.id.login_login);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("v" + Utils.getAppVersionName(LoginActivity.this));

        findViewById(R.id.tv_login_try).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TryOutActivity.class));
            }
        });

        findViewById(R.id.tv_login_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });

        btn_login.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (isNetworkAvailable(LoginActivity.this)) {
                    login();
                } else {
                    Utils.showToast(LoginActivity.this, "当前无网络，无法登录");
                }
            }
        });

//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isNetworkAvailable(LoginActivity.this)) {
//                    login();
//                } else {
//                    Utils.showToast(LoginActivity.this, "当前无网络，无法登录");
//                }
//            }
//        });

        employeeNo = SharedUtil.getSharedData(LoginActivity.this, "login_id");
        username = SharedUtil.getSharedData(LoginActivity.this, "login_gid");
        et_id.setText(employeeNo);
        et_gid.setText(username);
    }

    @Override
    public void onDeviceConnected(AidlDeviceService serviceManager) {

    }

    private void login() {
        username = et_gid.getText().toString();
        employeeNo = et_id.getText().toString();
        String pw = et_pw.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Utils.showToast(LoginActivity.this, "请填写账号");
        } else if (TextUtils.isEmpty(employeeNo)) {
            Utils.showToast(LoginActivity.this, "请填写工号");
        } else if (TextUtils.isEmpty(pw) && !employeeNo.equals("1000")) {
            Utils.showToast(LoginActivity.this, "请填写密码");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("UserName", username);
            map.put("EmployeeNo", employeeNo);
            map.put("Password", pw);
            map.put("EquipmentNum", terminalSn);
            switch (robotType) {
                case 1:
                    map.put("EquipmentType", "lkl");
                    break;
                case 3:
                    map.put("EquipmentType", "sunmi");
                    break;
                case 4:
                    map.put("EquipmentType", "android");
                    break;
            }
            map.put("Edition", Utils.getAppVersionName(LoginActivity.this));
            postToHttp(NetworkUrl.LOGIN, map, new IHttpCallBack() {

                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    loginData = new Gson().fromJson(jsonText, LoginData.class);
                    if (loginData.getResult_stadus().equals("SUCCESS")) {
                        SharedUtil.setSharedData(LoginActivity.this, "login_id", employeeNo);
                        SharedUtil.setSharedData(LoginActivity.this, "login_gid", username);
                        setting();
                        SharedUtil.setSharedData(LoginActivity.this, "dx_jf", loginData.getN_IntegralToMoney());
                        SharedUtil.setSharedData(LoginActivity.this, "dx_mr", loginData.getN_IntegralToMoneyDefault());
                        SharedUtil.setSharedData(LoginActivity.this, "dx_max", loginData.getN_IntegralToMoneyMax());
                        SharedUtil.setSharedData(LoginActivity.this, "userInfoId", loginData.getUserInfoId());
                        SharedUtil.setSharedData(LoginActivity.this, "username", loginData.getUserInfoName());
                        SharedUtil.setSharedData(LoginActivity.this, "dbname", loginData.getDbname() + ".");
                        SharedUtil.setSharedData(LoginActivity.this, "shopname", loginData.getShopName());
                        SharedUtil.setSharedData(LoginActivity.this, "shopid", loginData.getShopId());
                        SharedUtil.setSharedData(LoginActivity.this, "groupid", loginData.getYdManagerId());
                        SharedUtil.setSharedData(LoginActivity.this, "maxmoney", loginData.getMaxFastCostMoney());
                        SharedUtil.setSharedData(LoginActivity.this, "shopintegral", loginData.getShopIntegraltimes());
                        SharedUtil.setSharedData(LoginActivity.this, "rechargepoints", loginData.getRechargePoints());
                        SharedUtil.setSharedData(LoginActivity.this, "isedit", loginData.getSetHandCard());
                        SharedUtil.setSharedData(LoginActivity.this, "time", loginData.getHandoverTime());
                        SharedUtil.setSharedData(LoginActivity.this, "exittime", loginData.getQuitTime());
                        SharedUtil.setSharedBData(LoginActivity.this, "bluetooth", loginData.getDrawmenu().equals("1"));
                        getPrintInfo(loginData.getDbname() + ".");
                    } else {
                        try {
                            JSONObject object = new JSONObject(jsonText);
                            String msg = object.getString("result_errmsg");
                            Utils.showToast(LoginActivity.this, msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        et_pw.setText("");
                    }
                }

                @Override
                public void OnFail(String message) {
                }
            });
        }
    }

    private void initShared() {
        SharedUtil.setSharedBData(LoginActivity.this, "paybank", true);
        SharedUtil.setSharedBData(LoginActivity.this, "payxj", true);
        SharedUtil.setSharedBData(LoginActivity.this, "paywx", true);
        SharedUtil.setSharedBData(LoginActivity.this, "payal", true);
        SharedUtil.setSharedBData(LoginActivity.this, "paycode", true);
        SharedUtil.setSharedBData(LoginActivity.this, "nfc", false);
        SharedUtil.setSharedBData(LoginActivity.this, "citiao", false);
        SharedUtil.setSharedBData(LoginActivity.this, "saoma", false);
        SharedUtil.setSharedBData(LoginActivity.this, "quicki", true);
        SharedUtil.setSharedBData(LoginActivity.this, "fast_recharge", false);

        SharedUtil.setSharedBData(LoginActivity.this, "vip0", false);
        SharedUtil.setSharedBData(LoginActivity.this, "vip1", false);
        SharedUtil.setSharedBData(LoginActivity.this, "vip2", false);
        SharedUtil.setSharedBData(LoginActivity.this, "vip3", false);
        SharedUtil.setSharedBData(LoginActivity.this, "vip4", false);
        SharedUtil.setSharedBData(LoginActivity.this, "vip5", false);

        SharedUtil.setSharedBData(LoginActivity.this, "bluetooth", false);

        SharedUtil.setSharedBData(LoginActivity.this, "FW", false);
        SharedUtil.setSharedBData(LoginActivity.this, "FA", false);
        SharedUtil.setSharedBData(LoginActivity.this, "FX", false);
        SharedUtil.setSharedBData(LoginActivity.this, "FY", false);
        SharedUtil.setSharedBData(LoginActivity.this, "RW", false);
        SharedUtil.setSharedBData(LoginActivity.this, "RA", false);
        SharedUtil.setSharedBData(LoginActivity.this, "RX", false);
        SharedUtil.setSharedBData(LoginActivity.this, "RY", false);
        SharedUtil.setSharedBData(LoginActivity.this, "OW", false);
        SharedUtil.setSharedBData(LoginActivity.this, "OA", false);
        SharedUtil.setSharedBData(LoginActivity.this, "OX", false);
        SharedUtil.setSharedBData(LoginActivity.this, "OY", false);
        SharedUtil.setSharedBData(LoginActivity.this, "GW", false);
        SharedUtil.setSharedBData(LoginActivity.this, "GA", false);
        SharedUtil.setSharedBData(LoginActivity.this, "GX", false);
        SharedUtil.setSharedBData(LoginActivity.this, "GY", false);
        SharedUtil.setSharedBData(LoginActivity.this, "PW", false);
        SharedUtil.setSharedBData(LoginActivity.this, "PA", false);
        SharedUtil.setSharedBData(LoginActivity.this, "PX", false);
        SharedUtil.setSharedBData(LoginActivity.this, "PY", false);

        SharedUtil.setSharedBData(LoginActivity.this, "qdx", false);
        SharedUtil.setSharedBData(LoginActivity.this, "ssje", false);
        SharedUtil.setSharedBData(LoginActivity.this, "zsjf", false);
        SharedUtil.setSharedBData(LoginActivity.this, "zsje", false);
        SharedUtil.setSharedBData(LoginActivity.this, "cxsj", false);
        SharedUtil.setSharedBData(LoginActivity.this, "cxjf", false);
        SharedUtil.setSharedBData(LoginActivity.this, "cxdx", false);
        SharedUtil.setSharedBData(LoginActivity.this, "gcjf", false);
        SharedUtil.setSharedBData(LoginActivity.this, "gcdx", false);
        SharedUtil.setSharedBData(LoginActivity.this, "gcsj", false);
        SharedUtil.setSharedBData(LoginActivity.this, "opencard", false);
        SharedUtil.setSharedBData(LoginActivity.this,"giveInte",false);
    }

    private void setting() {
        loadingDialog.show();
        initShared();
        if (loginData.getRechargebutton() != null) {
            boolean b = loginData.getRechargebutton().equals("1");
            SharedUtil.setSharedBData(LoginActivity.this, "fast_recharge", b);
        }
        if (loginData.getSet() != null) {

            List<String> list_card = loginData.getSet().getCardSet();
            List<String> list_integral = loginData.getSet().getIntegralSet();
            //  List<String> list_pay = loginData.getSet().getPaySet();
//            if (list_pay != null && list_pay.size() > 0) {
//                for (int i = 0; i < list_pay.size(); i++) {
//                    if ("银联卡".equals(list_pay.get(i))) {
//                        SharedUtil.setSharedBData(LoginActivity.this, "paybank", true);
//                    } else if ("微信".equals(list_pay.get(i))) {
//                        SharedUtil.setSharedBData(LoginActivity.this, "paywx", true);
//                    } else if ("支付宝".equals(list_pay.get(i))) {
//                        SharedUtil.setSharedBData(LoginActivity.this, "payal", true);
//                    } else if ("现金".equals(list_pay.get(i))) {
//                        SharedUtil.setSharedBData(LoginActivity.this, "payxj", true);
//                    }
//                }
//                if (SharedUtil.getSharedBData(LoginActivity.this, "paywx") && SharedUtil.getSharedBData(LoginActivity.this, "payal")) {
//                    SharedUtil.setSharedBData(LoginActivity.this, "paycode", true);
//                }
//            }

            if (list_card != null && list_card.size() > 0) {
                for (int i = 0; i < list_card.size(); i++) {
                    if ("非接会员卡".equals(list_card.get(i))) {
                        SharedUtil.setSharedBData(LoginActivity.this, "nfc", true);
                    } else if ("磁条会员卡".equals(list_card.get(i))) {
                        SharedUtil.setSharedBData(LoginActivity.this, "citiao", true);
                    } else if ("条码会员卡".equals(list_card.get(i))) {
                        SharedUtil.setSharedBData(LoginActivity.this, "saoma", true);
                    }
                }
            }

            if (list_integral != null && list_integral.size() > 0) {
                for (int i = 0; i < list_integral.size(); i++) {
                    if ("快速消费积分".equals(list_integral.get(i))) {
                        SharedUtil.setSharedBData(LoginActivity.this, "quicki", true);
                    }
                }
            }
        }

        if (loginData.getAuthor() != null) {
            List<LoginData.AuthorBean> list = loginData.getAuthor();
            for (int i = 0; i < list.size(); i++) {
                LoginData.AuthorBean bean = list.get(i);
                String str = bean.getC_PopedomName();
                if (str.equals("POS:会员到店:修改密码")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip0", true);
                } else if (str.equals("POS:会员到店:修改资料")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip1", true);
                } else if (str.equals("POS:会员到店:通知设置")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip2", true);
                } else if (str.equals("POS:会员到店:微信绑定")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip3", true);
                } else if (str.equals("POS:会员到店:微信更改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip4", true);
                } else if (str.equals("POS:会员到店:微信解绑")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "vip5", true);

                } else if (str.equals("POS:快消积分抵现修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "qdx", true);

                } else if (str.equals("POS:产销实收金额修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "cxsj", true);
                } else if (str.equals("POS:产销获得积分修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "cxjf", true);
                } else if (str.equals("POS:产销积分抵现修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "cxdx", true);//

                } else if (str.equals("POS:购次实收金额修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "gcsj", true);//
                } else if (str.equals("POS:购次获得积分修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "gcjf", true);//
                } else if (str.equals("POS:购次积分抵现修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "gcdx", true);//

                } else if (str.equals("POS:充值实收金额修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "ssje", true);
                } else if (str.equals("POS:充值赠送积分修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "zsjf", true);
                } else if (str.equals("POS:充值赠送金额修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "zsje", true);

                } else if (str.equals("POS:开卡实收金额修改")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "opencard", true);//
                }else if(str.equals("POS:赠送积分")){
                    SharedUtil.setSharedBData(LoginActivity.this,"giveInte",true);
                }

            }
        }
        if (loginData.getSetPays() != null) {
            List<LoginData.SetPaysBean> list = loginData.getSetPays();
            for (int i = 0; i < list.size(); i++) {
                LoginData.SetPaysBean bean = list.get(i);
                String str = bean.getC_Value();
                if (str.equals("FastWeChat")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "FW", true);//快速消费 微信支付
                } else if (str.equals("FastAlipay")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "FA", true);//支付宝
                } else if (str.equals("FastCash")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "FX", true);//现金
                } else if (str.equals("FastCard")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "FY", true);//银行卡

                } else if (str.equals("RechargeWeChat")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "RW", true);//充值
                } else if (str.equals("RechargeAlipay")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "RA", true);
                } else if (str.equals("RechargeCash")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "RX", true);
                } else if (str.equals("RechargeCard")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "RY", true);

                } else if (str.equals("OpenWeChat")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "OW", true);//开卡
                } else if (str.equals("OpenAlipay")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "OA", true);
                } else if (str.equals("OpenCash")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "OX", true);
                } else if (str.equals("OpenCard")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "OY", true);

                } else if (str.equals("GoodWeChat")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "GW", true);//产品消费
                } else if (str.equals("GoodAlipay")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "GA", true);
                } else if (str.equals("GoodCash")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "GX", true);
                } else if (str.equals("GoodCard")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "GY", true);

                } else if (str.equals("PurchaseWeChat")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "PW", true);//购次
                } else if (str.equals("PurchaseAlipay")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "PA", true);
                } else if (str.equals("PurchaseCash")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "PX", true);
                } else if (str.equals("PurchaseCard")) {
                    SharedUtil.setSharedBData(LoginActivity.this, "PY", true);
                }

            }
        }
        list_rechargefast.clear();
        if (loginData.getRechargefast() != null) {
            list_rechargefast = loginData.getRechargefast();
        }


    }

    private void getPrintInfo(String dbname) {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        Map<String, String> map = new HashMap<>();
        map.put("dbName", dbname);
        postToHttp(NetworkUrl.PRINThEAD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String stadus = object.getString("result_stadus");
                    if (stadus.equals("SUCCESS")) {
                        String name1 = object.getString("name1");
                        String name2 = object.getString("name2");
                        String name3 = object.getString("name3");
                        String name4 = object.getString("name4");
                        SharedUtil.setSharedData(LoginActivity.this, "name1", name1);
                        SharedUtil.setSharedData(LoginActivity.this, "name2", name2);
                        SharedUtil.setSharedData(LoginActivity.this, "name3", name3);
                        SharedUtil.setSharedData(LoginActivity.this, "name4", name4);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) loginData.getAuthor());
                        bundle.putParcelableArrayList("infom", (ArrayList<? extends Parcelable>) loginData.getModule());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        String msg = object.getString("result_errmsg");
                        Utils.showToast(LoginActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                et_pw.setText("");
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void showHelp() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.pop_login_help, null);
        contentView.findViewById(R.id.alert_back_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.5));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_login), Gravity.CENTER, 0, 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((MyApp) getApplication()).close();
        finish();
    }
}
