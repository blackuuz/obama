package com.ksk.obama.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBackS;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Administrator on 2016/10/25.
 */

public class QuickDelMoneySupplementActivity extends BaseSupplementActivity implements IPrintErrorCallback, IPrintSuccessCallback, OnClickListener {
    private List<QuickDelMoney> list = new ArrayList<>();
    private int orderNum = 0;
    private int orderYes = 0;
    private ProgressBar pb2;
    private ProgressBar pb1;
    private TextView tv_hint;
    private boolean isComplition = false;
    private String haveMoney;
    private String haveIntegral;
    private Button btn_details;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplement);
        setOnPrintError(this);
        setOnPrintSuccess(this);
        initView();
        queryDb();
    }

    private void initView() {
        TextView name = (TextView) findViewById(R.id.title_name);
        name.setText("快速消费补单");
        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) {
                    finish();
                }
            }
        });
        btn_details = (Button) findViewById(R.id.btn_quick_supplement_details);
        btn_details.setOnClickListener(this);

        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        pb1.setVisibility(View.VISIBLE);
    }

    private void queryDb() {
        Connector.getDatabase();
        list = DataSupport.findAll(QuickDelMoney.class);
        pb2.setMax(list.size());
        pb2.setProgress(0);
        tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
        send();
    }

    private void send() {
        for (int i = 0; i < list.size(); i++) {
            QuickDelMoney upLoading = list.get(i);
            String dbname = upLoading.getDbName();
            String userID = upLoading.getUser_Id();
            String money = upLoading.getMoney();
            int payMode = upLoading.getN();
             flag = upLoading.isVip();
            Map<String, String> map = new HashMap<>();
            if (flag) {
                map.put("CardNum", upLoading.getCardNo());
                if (upLoading.getIsTem()) {
                    map.put("temporary_num", upLoading.getTem());
                    map.put("result_name", upLoading.getTemName());
                }
            }
            map.put("payIntegral", upLoading.getDelMoney());
            map.put("payDecIntegral", upLoading.getDelIntegral());
            map.put("Supplement", "1");
            map.put("userID", userID);
            map.put("EquipmentNum", upLoading.getEquipmentNum());
            map.put("orderNo", upLoading.getOrderNo());
            map.put("dbName", dbname);
            map.put("costmoney", money);
            map.put("Supplement", "1");
            map.put("CardCode", upLoading.getUid());
            map.put("c_Billfrom", robotType + "");

            switch (payMode) {
                case 0:
                    map.put("payCash", money);
                    break;
                case 1:
                    map.put("payWeChat", money);
                    map.put("refernumber", upLoading.getRefernumber());
                    break;
                case 2:
                    map.put("payAli", money);
                    map.put("refernumber", upLoading.getRefernumber());
                    break;
                case 3:
                    map.put("payThird", money);
                    map.put("refernumber", upLoading.getRefernumber());
                    break;
                case 4:
                    map.put("payCard", money);
                    break;
            }
            if (flag) {
                String url = upLoading.getUrl();
                postToHttps(i, url, map, new IHttpCallBackS() {
                    @Override
                    public void OnSucess(String jsonText, int n) {
                        showData(jsonText, n);
                    }

                    @Override
                    public void OnFail(String message) {
                        isComplition = true;
                        isChangeActivity();
                    }
                });
            } else {
                String url = upLoading.getUrl1();
                postToHttps(i, url, map, new IHttpCallBackS() {
                    @Override
                    public void OnSucess(String jsonText, int n) {
                        showData(jsonText, n);
                    }

                    @Override
                    public void OnFail(String message) {
                        isComplition = true;
                        isChangeActivity();
                    }
                });
            }

        }
    }

    public void showData(String text, final int n) {
        Logger.e(text);
        try {
            orderNum += 1;
            JSONObject object = new JSONObject(text);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                btn_details.setClickable(false);
                orderYes += 1;
                pb2.setProgress(orderYes);
                tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
                DataSupport.delete(QuickDelMoney.class, list.get(n).getId());
                if (list.get(n).isVip()) {
                    haveMoney = object.getString("AmountAvailable");
                    haveIntegral = object.getString("IntegralAvailable");
                }
                printInformation(n);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            btn_details.setClickable(true);

        }
        switch (robotType) {
            case 3:
                isChangeActivity();
                break;
        }

    }

    private void isChangeActivity() {
        if (orderNum == list.size()) {
            pb1.setVisibility(View.INVISIBLE);
            isComplition = true;
            finish();
        }
    }

    private synchronized void printInformation(final int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String nowTime = sdf.format(new Date());
        QuickDelMoney upLoading = list.get(n);
        boolean flag = upLoading.isVip();
        List<String> list = new ArrayList<>();
        list.add("消费店面:" + upLoading.getShopName());
        list.add("操作员:" + upLoading.getUserName());
        list.add("手持序列号:" + upLoading.getEquipmentNum());
        list.add("订单生成时间:" + upLoading.getOrderTime());
        list.add("补单时间:" + nowTime);
        list.add("单据号:" + upLoading.getOrderNo());
        if (flag) {
            list.add("会员卡号:" + upLoading.getCardNo());
            list.add("会员姓名:" + upLoading.getCardName());
        } else {
            list.add("会员卡号:散客");
            list.add("会员姓名:散客");
        }
        if (upLoading.isIntegral()) {//是否使用了积分抵现
            list.add("消费金额:" + upLoading.getDelMoney());
        } else {
            list.add("消费金额:" + upLoading.getMoney());
        }

        if (flag) {
            list.add("实收金额:" + upLoading.getMoney());
            if (upLoading.getIsTem()) {
                list.add("授权工号:" + upLoading.getTem());
            }
        }
        switch (upLoading.getN()) {
            case 0:
                list.add("支付方式:现金支付");
                break;
            case 1:
                list.add("支付方式:微信支付");
                break;
            case 2:
                list.add("支付方式:支付宝支付");
                break;
            case 3:
                list.add("支付方式:银联支付");
                break;
            case 4:
                list.add("支付方式:会员卡支付");
                break;
        }

        if (flag) {
            list.add("抵现积分:" + upLoading.getDelIntegral());
            list.add("获得积分:" + upLoading.getGetIntegral());
            list.add("可用储值:￥" + haveMoney);
            list.add("可用积分:" + haveIntegral);
        }

        printPage("快速收银补单小票", list, null, false);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_recharge_supplement);
    }

    private boolean isclick;

    private void printError(int id) {
        isclick = true;
        final PopupWindow window = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_print_hint, null);
        view.findViewById(R.id.btn_print_again).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        window.setContentView(view);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        window.setTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (w * 0.8));
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isclick) {
                    isclick = false;
                    printInformation(0);
                }
            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }

    @Override
    public void OnPrintSuccess() {
        isChangeActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_quick_supplement_details:
                Intent intent = new Intent();
                intent.putExtra("flag",flag);
                intent.putExtra("clazz","Quick");
                intent.setClass(QuickDelMoneySupplementActivity.this, SupplmentDetialsActivity.class);
                startActivityForResult(intent, 324);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 324){
                DataSupport.delete(QuickDelMoney.class, list.get(0).getId());
                Log.d("uuz", "onActivityResult: ");
             finish();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
