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
import com.ksk.obama.adapter.DetialsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.model.CountStatisticsDetials;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.DetialsListView;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountStatisticsDetialsActivity extends BasePrintActivity implements IPrintErrorCallback{
    private List<PrintPage> listson = new ArrayList<>();
    private List<CountStatisticsDetials.TimesDetailBean> list = new ArrayList<>();
    private DetialsListView lv;
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_djh;
    private TextView tv_xlh;
    private TextView tv_shop;
    private TextView tv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_statistics_detials);

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
        title_name.setText("计次详情");
        lv = (DetialsListView) findViewById(R.id.detials_lv);
        tv_cardNum = (TextView) findViewById(R.id.tv_statistics_detilas0);
        tv_name = (TextView) findViewById(R.id.tv_statistics_detilas1);
        tv_time = (TextView) findViewById(R.id.tv_statistics_detilas2);
        tv_djh = (TextView) findViewById(R.id.tv_statistics_detilas3);
        tv_xlh = (TextView) findViewById(R.id.tv_statistics_detilas4);
        tv_shop = (TextView) findViewById(R.id.tv_statistics_detilas5);
        tv_user = (TextView) findViewById(R.id.tv_statistics_detilas6);
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });
    }

    private void getDetailInfo() {
        String id = getIntent().getStringExtra("MemberId");
        Map<String, String> map = new HashMap<>();
        map.put("BillType", "快速扣次");
        map.put("dbName", SharedUtil.getSharedData(CountStatisticsDetialsActivity.this, "dbname"));
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

    private void showHttpData(String jsonText) {
        CountStatisticsDetials detials = new Gson().fromJson(jsonText, CountStatisticsDetials.class);
        String code = detials.getResult_stadus();
        if (code.equals("SUCCESS")) {
            list = detials.getTimes_Detail();
            tv_cardNum.setText(detials.getC_CardNO());
            tv_name.setText(detials.getC_Name());
            tv_time.setText(detials.getT_Time().substring(0,19));
            tv_djh.setText(detials.getC_BillNO());
            tv_xlh.setText(detials.getC_EquipmentNum());
            tv_shop.setText(detials.getC_ShopName());
            tv_user.setText(detials.getC_UserName());
            if (list.size() > 0) {
                for (int i=0;i<list.size();i++) {
                    PrintPage printPage = new PrintPage();
                    printPage.setName(list.get(i).getGoods());
                    printPage.setPrice("");
                    printPage.setNum("");
                    printPage.setMoney(list.get(i).getTimes());
                    listson.add(printPage);
                }
                DetialsAdapter adapter = new DetialsAdapter(CountStatisticsDetialsActivity.this, listson);
                lv.setAdapter(adapter);
                changeListViewHeight(adapter, lv);
            }
        } else {
            Utils.showToast(CountStatisticsDetialsActivity.this, detials.getResult_errmsg());
        }
    }

    private void printInfo() {
        List<String> listp = new ArrayList<>();

        listp.add("卡号:" + tv_cardNum.getText().toString());
        listp.add("姓名:" + tv_name.getText().toString());
        listp.add("son");
        listp.add("时间:" + tv_time.getText().toString());
        listp.add("手持序列号:" + tv_xlh.getText().toString());
        listp.add("收款单号:" + tv_djh.getText().toString());
        listp.add("计次店面:" + tv_shop.getText().toString());
        listp.add("操作员:" + tv_user.getText().toString());
        printPage("计次详情", listp, listson, false);
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
