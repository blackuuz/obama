package com.ksk.obama.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ksk.obama.DB.OpenCardDb;
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

public class OpenCardSupplementActivity extends BaseSupplementActivity implements IPrintErrorCallback, IPrintSuccessCallback {
    private List<OpenCardDb> list = new ArrayList<>();
    private int orderNum = 0;
    private int orderYes = 0;
    private ProgressBar pb2;
    private ProgressBar pb1;
    private TextView tv_hint;
    private boolean isComplition = false;
    private boolean flag_f = true;

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
        name.setText("开卡补单");
        findViewById(R.id.tv_commit).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    finish();
                }
            }
        });
        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        pb1.setVisibility(View.VISIBLE);
    }

    private void queryDb() {
        Connector.getDatabase();
        list = DataSupport.findAll(OpenCardDb.class);
        pb2.setMax(list.size());
        pb2.setProgress(0);
        tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
        send();
    }

    private void send() {
        for (int i = 0; i < list.size(); i++) {
            OpenCardDb upLoading = list.get(i);
            String url = upLoading.getUrl();
            String m_is = upLoading.getPayActual();
            String orderidScan = upLoading.getRefernumber();
            int payMode = upLoading.getN();
            Map<String, String> map = new HashMap<>();
            map.put("cardNO", upLoading.getCardNO());
            map.put("memberName", upLoading.getMemberName());
            map.put("sex", upLoading.getSex());
            map.put("mobile", upLoading.getMobile());
            map.put("orderNo", upLoading.getOrderNo());
            map.put("birthdayYear", upLoading.getBirthdayYear());
            map.put("birthdayMonth", upLoading.getBirthdayMonth());
            map.put("birthdayDay", upLoading.getBirthdayDay());
            map.put("classID", upLoading.getClassID());
            map.put("userID", upLoading.getUserID());
            map.put("payActual", upLoading.getPayActual());
            map.put("payShould",upLoading.getPayShould());
            map.put("EquipmentNum", upLoading.getEquipmentNum());
            map.put("C_ServiceEmployee", upLoading.getAddName());
            map.put("memid", upLoading.getAddId());
            map.put("c_Billfrom", robotType + "");
            map.put("CardCode", upLoading.getUid());
//            map.put("Remark", upLoading.getRemark());
            map.put("dbName", upLoading.getDbName());
            map.put("Supplement", "1");
            if (upLoading.isTem()) {
                map.put("temporary_num", upLoading.getTemporaryNum());//临时权限
                map.put("result_name", upLoading.getTemName());
            }
            switch (payMode) {
                case 0:
                    map.put("payCash", m_is);
                    break;
                case 2:
                    map.put("payAli", m_is);
                    map.put("refernumber", orderidScan);
                    break;
                case 1:
                    map.put("payWeChat", m_is);
                    map.put("refernumber", orderidScan);
                    break;
                case 3:
                    map.put("payBank", m_is);
                    map.put("refernumber", orderidScan);
                    break;
            }

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

    public void showData(String text, final int n) {
        Logger.e(text);
        try {
            orderNum += 1;
            JSONObject object = new JSONObject(text);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                orderYes += 1;
                pb2.setProgress(orderYes);
                tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
                DataSupport.delete(OpenCardDb.class, list.get(n).getId());
                printInformation(n);

            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        OpenCardDb upLoading = list.get(n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String nowTime = sdf.format(new Date());
        List<String> printP = new ArrayList<>();
        printP.add("订单生成时间:" + upLoading.getOrderTime());
        printP.add("补单时间:" + nowTime);
        printP.add("单据号:" + upLoading.getOrderNo());
        printP.add("开卡店面: " + upLoading.getShopName());
        printP.add("推荐人: " + upLoading.getAddName());
        printP.add("卡号 :" + upLoading.getCardNO());
        printP.add("姓名:" + upLoading.getMemberName());
        printP.add("操作员 :" + upLoading.getUserName());
        printP.add("卡类型 :" + upLoading.getCardType());
        printP.add("手持序列号:" + upLoading.getEquipmentNum());
        printP.add("初始储值:" + upLoading.getChushimoney());
        switch (upLoading.getN()) {
            case 1:
                printP.add("充值方式:微信支付");
                break;
            case 2:
                printP.add("充值方式:支付宝支付");
                break;
            case 3:
                printP.add("充值方式:银联支付");
                break;
            case 0:
                printP.add("充值方式:现金支付");
                break;
        }
        printP.add("初始积分:" + upLoading.getChushijf());
        printP.add("卡价格 :" + upLoading.getCardMoney());
        if (upLoading.getMobile() == null || upLoading.getMobile().length() <= 0) {
            printP.add("会员手机号 : 无");
        } else {
            printP.add("会员手机号 :" + upLoading.getMobile());
        }

        printPage("会员办卡补单小票", printP, null, false);

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
        view.findViewById(R.id.btn_print_again).setOnClickListener(new View.OnClickListener() {
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
}
