package com.ksk.obama.activity;

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
import com.ksk.obama.model.OpenCardStatisticsDetatils;
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

public class OpenCardStatisticsDetialsActivity extends BasePrintActivity implements IPrintErrorCallback {

    private TextView tv_cardMoney;
    private TextView tv_cardType;
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_mode;
    private TextView tv_dj;
    private TextView tv_startMoney;
    private TextView tv_endMoney;
    private TextView tv_cardJf;
    private TextView tv_shopName;
    private TextView tv_user;
    private TextView tv_pay_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_card_statistics_detials);
        setOnPrintError(this);
        initView();
        getDetailInfo();
    }

    private void initView() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("打    印");
        title_name.setText("开卡详情");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });

        tv_cardMoney = (TextView) findViewById(R.id.tv_open_card_money);
        tv_cardType = (TextView) findViewById(R.id.tv_open_card_type);
        tv_cardNum = (TextView) findViewById(R.id.tv_open_card_num);
        tv_name = (TextView) findViewById(R.id.tv_open_card_name);
        tv_time = (TextView) findViewById(R.id.tv_open_card_time);
        tv_mode = (TextView) findViewById(R.id.tv_open_mode);
        tv_dj = (TextView) findViewById(R.id.tv_open_dj);
        tv_startMoney = (TextView) findViewById(R.id.tv_open_start_money);
        tv_endMoney = (TextView) findViewById(R.id.tv_open_end_money);
        tv_cardJf = (TextView) findViewById(R.id.tv_open_jf);
        tv_shopName = (TextView) findViewById(R.id.tv_open_shop_name);
        tv_user = (TextView) findViewById(R.id.tv_open_user);
        tv_pay_mode = (TextView) findViewById(R.id.tv_open_pay_mode);
    }

    private void getDetailInfo() {
        String id = getIntent().getStringExtra("MemberId");
        Map<String, String> map = new HashMap<>();
        map.put("BillType", "开户收费");
        map.put("dbName", SharedUtil.getSharedData(OpenCardStatisticsDetialsActivity.this, "dbname"));
        map.put("id", id);
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
                OpenCardStatisticsDetatils recordDel = gson.fromJson(json, OpenCardStatisticsDetatils.class);
                tv_cardMoney.setText("￥" + recordDel.getN_PayShould());
                tv_name.setText(recordDel.getC_Name() + "");
                tv_time.setText(recordDel.getT_CreateTime() + "");
                tv_mode.setText(recordDel.getC_EquipmentNum());
                tv_startMoney.setText("￥" + recordDel.getN_InitAmount());
                tv_endMoney.setText("￥" + recordDel.getN_PayActual());
                tv_cardJf.setText(recordDel.getN_InitIntegral() + "");
                tv_cardNum.setText(recordDel.getC_CardNO() + "");
                tv_cardType.setText(recordDel.getC_ClassName() + "");
                tv_user.setText(recordDel.getC_UserName() + "");
                tv_dj.setText(recordDel.getC_BillNO() + "");
                tv_shopName.setText(recordDel.getC_ShopName() + "");
                tv_pay_mode.setText(recordDel.getPay_way());
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(OpenCardStatisticsDetialsActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfo() {
        List<String> list = new ArrayList<>();
        list.add("卡价格:" + tv_cardMoney.getText().toString());
        list.add("卡类型:" + tv_cardType.getText().toString());
        list.add("卡号:" + tv_cardNum.getText().toString());
        list.add("姓名:" + tv_name.getText().toString());
        list.add("开卡时间:" + tv_time.getText().toString());
        list.add("手持序列号:" + tv_mode.getText().toString());
        list.add("支付方式:" + tv_pay_mode.getText().toString());
        list.add("收款单号:" + tv_dj.getText().toString());
        list.add("初始储值:" + tv_startMoney.getText().toString());
        list.add("实收金额:" + tv_endMoney.getText().toString());
        list.add("初始积分:" + tv_cardJf.getText().toString());
        list.add("开卡店面:" + tv_shopName.getText().toString());
        list.add("操作员:" + tv_user.getText().toString());
        printPage("开卡详情", list, null, false);
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
