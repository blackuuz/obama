package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.model.ExpenditureQuickDetials;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenditureQuickStatisticsDetialsActivity extends BasePrintActivity implements IPrintErrorCallback {
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv_pay_way;
    private TextView tv_cardNum;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_quick_statistics_detials);
        setOnPrintError(this);
        initTitale();
        initView();
        getDetailInfo();
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
        tv_print.setText("打    印");
        title_name.setText("消费详情");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });
    }

    private void initView() {
        tv0 = (TextView) findViewById(R.id.tv_statistics_detilas0);
        tv1 = (TextView) findViewById(R.id.tv_statistics_detilas1);
        tv2 = (TextView) findViewById(R.id.tv_statistics_detilas2);
        tv3 = (TextView) findViewById(R.id.tv_statistics_detilas3);
        tv4 = (TextView) findViewById(R.id.tv_statistics_detilas4);
        tv5 = (TextView) findViewById(R.id.tv_statistics_detilas5);
        tv6 = (TextView) findViewById(R.id.tv_statistics_detilas7);
        tv_pay_way = (TextView) findViewById(R.id.tv_statistics_detilas_payway);
        tv_cardNum = (TextView) findViewById(R.id.tv_statistics_detilas_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_statistics_detilas_name);
    }

    private void getDetailInfo() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        Map<String, String> map  = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(ExpenditureQuickStatisticsDetialsActivity.this, "dbname"));
        map.put("id", id);
        map.put("BillType", type);
        postToHttp(NetworkUrl.DETAILED, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                showHttpData(jsonText);
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void showHttpData(String json) {
        try {
            JSONObject object = new JSONObject(json);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                Gson gson = new Gson();
                ExpenditureQuickDetials recordDel = gson.fromJson(json, ExpenditureQuickDetials.class);
                tv0.setText("￥" + recordDel.getN_PayActual());
                tv1.setText(recordDel.getT_Time() + "");
                tv2.setText(recordDel.getC_EquipmentNum());
                tv3.setText(recordDel.getC_BillNO());
                tv4.setText(recordDel.getC_ShopName());
                tv5.setText(recordDel.getC_UserName());
                tv6.setText(recordDel.getN_GetIntegral());
                tv_cardNum.setText(recordDel.getC_CardNO());
                tv_name.setText(recordDel.getC_Name());
                tv_pay_way.setText(recordDel.getPay_way());
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(ExpenditureQuickStatisticsDetialsActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfo() {
        List<String> listp = new ArrayList<>();
        listp.add("消费金额:" + tv0.getText().toString());
        listp.add("时间:" + tv1.getText().toString());
        listp.add("手持序列号:" + tv2.getText().toString());
        listp.add("收款单号:" + tv3.getText().toString());
        listp.add("会员卡号:" + tv_cardNum.getText().toString());
        listp.add("会员姓名:" + tv_name.getText().toString());
        listp.add("支付方式:" + tv_pay_way.getText().toString());
        listp.add("消费店面:" + tv4.getText().toString());
        listp.add("操作员:" + tv5.getText().toString());
        printPage("消费详情", listp, null, false);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_open_card_statistics_detials);
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
                    printInfo();
                }

            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }
}
