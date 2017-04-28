package com.ksk.obama.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.DetialsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.model.ExchangeStatisticsDetials;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.DetialsListView;
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

public class ExchangeStatisticsDetialsActivity extends BasePrintActivity implements IPrintErrorCallback{
    private TextView tv_cardMoney;
    private TextView tv_cardType;
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_mode;
    private TextView tv_dj;
    private TextView tv_startMoney;
    private TextView tv_endMoney;
    private TextView tv_shopName;
    private TextView tv_user;
    private List<ExchangeStatisticsDetials.DhDetailBean> list = new ArrayList<>();
    private DetialsListView lv;
    private List<PrintPage> listson = new ArrayList<>();
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_statistics_detials);
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
        title_name.setText("兑换详情");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });

        lv = (DetialsListView) findViewById(R.id.detials_lv);
        ll = (LinearLayout) findViewById(R.id.detials_title);
        tv_cardMoney = (TextView) findViewById(R.id.tv_open_card_money);
        tv_time = (TextView) findViewById(R.id.tv_open_card_type);
        tv_cardNum = (TextView) findViewById(R.id.tv_open_card_num);
        tv_name = (TextView) findViewById(R.id.tv_open_card_name);
        tv_cardType = (TextView) findViewById(R.id.tv_open_card_time);
        tv_mode = (TextView) findViewById(R.id.tv_open_mode);
        tv_dj = (TextView) findViewById(R.id.tv_open_dj);
        tv_startMoney = (TextView) findViewById(R.id.tv_open_start_money);
        tv_endMoney = (TextView) findViewById(R.id.tv_open_end_money);
        tv_shopName = (TextView) findViewById(R.id.tv_open_shop_name);
        tv_user = (TextView) findViewById(R.id.tv_open_user);
    }

    private void getDetailInfo() {
        String id = getIntent().getStringExtra("MemberId");
        Map<String, String> map = new HashMap<>();
        map.put("BillType", "兑换记录");
        map.put("dbName", SharedUtil.getSharedData(ExchangeStatisticsDetialsActivity.this, "dbname"));
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
                ExchangeStatisticsDetials recordDel = gson.fromJson(json, ExchangeStatisticsDetials.class);
                list = recordDel.getDh_Detail();
                tv_cardMoney.setText(recordDel.getN_GetIntegral()+"");
                tv_cardNum.setText(recordDel.getC_CardNO() + "");
                tv_name.setText(recordDel.getC_Name() + "");
                tv_time.setText(recordDel.getT_Time() + "");
                tv_mode.setText( "积分");
                tv_startMoney.setText(recordDel.getN_IntegralAvailable());
                tv_endMoney.setText(recordDel.getN_AmountAvailable());
                tv_cardType.setText(recordDel.getC_EquipmentNum());
                tv_user.setText(recordDel.getC_UserName() + "");
                tv_dj.setText(recordDel.getC_BillNO() + "");
                tv_shopName.setText(recordDel.getC_ShopName() + "");
                for (int i=0;i<list.size();i++) {
                    PrintPage printPage = new PrintPage();
                    String count = list.get(i).getN_GetIntegral();
                    String num = list.get(i).getN_Number();
                    printPage.setName(list.get(i).getC_GoodsName());
                    printPage.setPrice((Float.parseFloat(count)/Float.parseFloat(num))+"");
                    printPage.setNum(num);
                    printPage.setMoney(count);
                    listson.add(printPage);
                }
                if (list != null && list.size() > 0) {
                    ll.setVisibility(View.VISIBLE);
                    DetialsAdapter adapter = new DetialsAdapter(ExchangeStatisticsDetialsActivity.this, listson);
                    lv.setAdapter(adapter);
                    changeListViewHeight(adapter, lv);
                }else{
                    ll.setVisibility(View.GONE);
                }
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(ExchangeStatisticsDetialsActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfo() {
        List<String> listp = new ArrayList<>();

        listp.add("积分变动:" + tv_cardMoney.getText().toString());
        listp.add("卡号:" + tv_cardNum.getText().toString());
        listp.add("姓名:" + tv_name.getText().toString());
        listp.add("son");
        listp.add("兑换时间:" + tv_time.getText().toString());
        listp.add("支付方式:" + tv_mode.getText().toString());

        listp.add("手持序列号:" + tv_cardType.getText().toString());
        listp.add("收款单号:" + tv_dj.getText().toString());
        listp.add("剩余积分:" + tv_startMoney.getText().toString());
        listp.add("剩余储值:" + tv_endMoney.getText().toString());
        listp.add("兑换店面:" + tv_shopName.getText().toString());
        listp.add("操作员:" + tv_user.getText().toString());
        printPage("积分变动详情", listp, listson, false);
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
