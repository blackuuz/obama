package com.ksk.obama.activity;

import android.content.Intent;
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
import com.ksk.obama.model.ExpenditureStatisticsDetials;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.DetialsListView;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ksk.obama.utils.SharedUtil.getSharedInt;

public class ExpenditureStatisticsDetialsActivity extends BasePrintActivity implements IPrintErrorCallback {
    private DetialsListView lv;
    private List<PrintPage> listson = new ArrayList<>();
    private List<ExpenditureStatisticsDetials.DataBean> list = new ArrayList<>();
    private LinearLayout ll;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private boolean isPrint = false;
    private boolean flag_esd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_statistics_detials);
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
                if (isBluetooth) {
                    final PopupWindow window = new PopupWindow(ExpenditureStatisticsDetialsActivity.this);
                    View view = LayoutInflater.from(ExpenditureStatisticsDetialsActivity.this).inflate(R.layout.pop_blue_tooth_hint, null);
                    view.findViewById(R.id.btn_print_again).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isPrint = true;
                            bluetoothPrint(listson,tv6.getText().toString());
                            if(flag_esd){
                                printInfo();
                            }

                            window.dismiss();
                        }
                    });

                    view.findViewById(R.id.btn_print_again_n).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isPrint = false;
                            printInfo();

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
                    window.setHeight((int) (w * 0.5));
                    window.update();
                    window.showAtLocation(findViewById(R.id.activity_open_card_statistics_detials), Gravity.CENTER, 0, 0);
                } else {
                    printInfo();
                }

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
        tv6 = (TextView) findViewById(R.id.tv_statistics_detilas6);
        tv7 = (TextView) findViewById(R.id.tv_statistics_detilas7);
        tv8 = (TextView) findViewById(R.id.tv_statistics_detilas8);
        tv9 = (TextView) findViewById(R.id.tv_statistics_detilas9);
        tv10 = (TextView) findViewById(R.id.tv_statistics_detilas10);
        tv11 = (TextView) findViewById(R.id.tv_statistics_detilas11);
        lv = (DetialsListView) findViewById(R.id.detials_lv);
        ll = (LinearLayout) findViewById(R.id.detials_title);
    }

    private void getDetailInfo() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(ExpenditureStatisticsDetialsActivity.this, "dbname"));
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
                ExpenditureStatisticsDetials recordDel = gson.fromJson(json, ExpenditureStatisticsDetials.class);
                list = recordDel.getData();
                tv0.setText("￥" + recordDel.getN_PayActual());
                tv1.setText(recordDel.getC_CardNO() + "");
                tv2.setText(recordDel.getC_Name() + "");
                tv3.setText(recordDel.getT_Time() + "");
                tv4.setText(recordDel.getPay_way());
                tv5.setText(recordDel.getC_EquipmentNum());
                tv6.setText(recordDel.getC_BillNO());
                tv7.setText(recordDel.getN_GetIntegral());
                tv8.setText(recordDel.getN_IntegralAvailable());
                tv9.setText(recordDel.getN_AmountAvailable());
                tv10.setText(recordDel.getC_ShopName());
                tv11.setText(recordDel.getC_UserName() + "");
                for (int i = 0; i < list.size(); i++) {
                    PrintPage printPage = new PrintPage();
                    String price = list.get(i).getN_PriceRetail();
                    String num = list.get(i).getN_Number();
                    printPage.setName(list.get(i).getC_GoodsName());
                    printPage.setPrice(price);
                    printPage.setNum(num);
                    printPage.setMoney(list.get(i).getN_PayActual());
                    listson.add(printPage);
                }
                if (list != null && list.size() > 0) {
                    ll.setVisibility(View.VISIBLE);
                    DetialsAdapter adapter = new DetialsAdapter(ExpenditureStatisticsDetialsActivity.this, listson);
                    lv.setAdapter(adapter);
                    changeListViewHeight(adapter, lv);
                } else {
                    ll.setVisibility(View.GONE);
                }
                flag_esd = true;
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(ExpenditureStatisticsDetialsActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfo() {
        List<String> listp = new ArrayList<>();
        if (SharedUtil.getSharedBData(ExpenditureStatisticsDetialsActivity.this, "bluetooth")&&isPrint) {
            int nn = getSharedInt(ExpenditureStatisticsDetialsActivity.this, "num");
            String str = SharedUtil.getSharedData(ExpenditureStatisticsDetialsActivity.this, "day");
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            if (!str.equals(time.substring(8, 10))) {
                nn = 0;
                SharedUtil.setSharedData(ExpenditureStatisticsDetialsActivity.this, "day", time.substring(8, 10));
            }
            String num = "";
            if (nn < 10) {
                num = terminalSn.substring(terminalSn.length() - 2) + "00" + nn;
            } else if (nn < 100) {
                num = terminalSn.substring(terminalSn.length() - 2) + "0" + nn;
            } else {
                num = terminalSn.substring(terminalSn.length() - 2) + nn;
            }
            listp.add("序号-:" + num);
        }
        listp.add("消费金额:" + tv0.getText().toString());
        listp.add("卡号:" + tv1.getText().toString());
        listp.add("姓名:" + tv2.getText().toString());
        listp.add("son");
        listp.add("时间:" + tv3.getText().toString());
        listp.add("支付方式:" + tv4.getText().toString());
        listp.add("手持序列号:" + tv5.getText().toString());
        listp.add("收款单号:" + tv6.getText().toString());
        listp.add("获得积分:" + tv7.getText().toString());
        listp.add("剩余积分:" + tv8.getText().toString());
        listp.add("剩余储值:" + tv9.getText().toString());
        listp.add("消费店面:" + tv10.getText().toString());
        listp.add("操作员:" + tv11.getText().toString());
        printPage("消费详情", listp, listson, false);
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
