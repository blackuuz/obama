package com.ksk.obama.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBackS;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.PrintPage;
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
import java.util.Map;


/**
 * Created by Administrator on 2016/10/25.
 */
public class BuyCountSupplementActivity extends BaseSupplementActivity implements IPrintErrorCallback, IPrintSuccessCallback {
    private List<BuyCountDb> list = new ArrayList<>();
    private int orderNum = 0;
    private int orderYes = 0;
    private ProgressBar pb2;
    private ProgressBar pb1;
    private TextView tv_hint;
    private boolean isComplition = false;
    private String haveMoney;
    private String haveIntegral;
    private Button btn_details;


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
        name.setText("购次补单");
        findViewById(R.id.tv_commit).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    finish();
                }
            }
        });
        btn_details = (Button) findViewById(R.id.btn_quick_supplement_details);
        btn_details.setText("查看详情");
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("flag",true);
                intent.putExtra("clazz","Count");
                intent.setClass(BuyCountSupplementActivity.this, SupplmentDetialsActivity.class);
                startActivityForResult(intent, 86);
            }
        });

        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        pb1.setVisibility(View.VISIBLE);
    }

    private void queryDb() {
        Connector.getDatabase();
        list = DataSupport.findAll(BuyCountDb.class,true);
        pb2.setMax(list.size());
        pb2.setProgress(0);
        tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
        send();
    }

    private void send() {
        for (int i = 0; i < list.size(); i++) {
            BuyCountDb upLoading = list.get(i);
            String url = upLoading.getUrl();
            String dbname = upLoading.getDbname();
            String userID = upLoading.getUserid();
            String m_is = upLoading.getPayactual();
            String money = upLoading.getPayshould();
            String orderidScan = upLoading.getRefernumber();
            int payMode = upLoading.getN();
            Map<String, String> map = new HashMap<>();
            map.put("dbName", dbname);
            map.put("User_Id", userID);
            map.put("goods_id", upLoading.getGoods_id());
            map.put("Member_Id", upLoading.getMemid());
            map.put("times", upLoading.getTimes());
            map.put("get_money", upLoading.getGet_money());
            map.put("PayActual", m_is);
            map.put("PayShould", money + "");
            map.put("PayDiscounted", upLoading.getPayDiscounted());
            map.put("orderNo", upLoading.getOrderNo());
            map.put("validTimes", upLoading.getValidTimes());
            map.put("c_Billfrom", robotType + "");
            map.put("EquipmentNum", upLoading.getEquipmentNum());
            map.put("Supplement", "1");
            map.put("CardCode", upLoading.getUid());
            map.put("temporary_num", upLoading.getTemporaryNum());
            map.put("result_name", upLoading.getTemName());
            map.put("payIntegral", upLoading.getDx_Money());
            map.put("payDecIntegral", upLoading.getDx_Integral());
            switch (payMode) {
                case 0:
                    map.put("payCash", m_is);
                    break;
                case 1:
                    map.put("payWeChat", m_is);
                    map.put("refernumber", orderidScan);
                    break;
                case 2:
                    map.put("payAli", m_is);
                    map.put("refernumber", orderidScan);
                    break;
                case 3:
                    map.put("payThird", m_is);
                    map.put("refernumber", orderidScan);
                    break;
                case 4:
                    map.put("payCard", m_is);
                    break;
            }

            postToHttps(i, url, map, new IHttpCallBackS() {
                @Override
                public void OnSucess(String jsonText, int n) {

                    Logger.e(jsonText);
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
        try {
            orderNum += 1;
            JSONObject object = new JSONObject(text);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                btn_details.setClickable(false);
                orderYes += 1;
                pb2.setProgress(orderYes);
                tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
                haveMoney = object.getString("AmountAvailable");
                haveIntegral = object.getString("IntegralAvailable");
                DataSupport.delete(BuyCountDb.class, list.get(n).getId());
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BuyCountSupplementActivity.this);
        builder.setTitle("提示:");
        builder.setMessage("有" + (orderNum - orderYes) + "个订单上传失败,是否删除订单");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Connector.getDatabase();
                DataSupport.deleteAll(BuyCountDb.class);
            }
        });
        builder.setNegativeButton("否", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        builder.create().show();
    }

    private synchronized void printInformation(final int n) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleFormat.format(date);
        BuyCountDb upLoading = list.get(n);
        final List<PrintPage> list_son = new ArrayList<>();
        for (int i = 0; i < upLoading.getDataList().size(); i++) {
            Log.d("uuz", "printInformation: "+upLoading.getDataList().size());
            PrintPage page = new PrintPage();
            page.setName(upLoading.getDataList().get(i).getName());
            page.setPrice(upLoading.getDataList().get(i).getPrice() + "");
            page.setNum(upLoading.getDataList().get(i).getNum() + "");
            page.setMoney(upLoading.getDataList().get(i).getMoney() + "");
            list_son.add(page);
        }
        final List<String> printP = new ArrayList<>();
         printP.add("订单生成时间:" + upLoading.getOrderTime());
        printP.add("补单时间:" + nowTime);
         printP.add("单据号:" + upLoading.getOrderNo());
         printP.add("消费店面: " + upLoading.getShopName());
         printP.add("卡号 :" + upLoading.getCardNo());
         printP.add("姓名:" + upLoading.getCardName());
         printP.add("操作员 :" + upLoading.getUserName());
         printP.add("手持序列号:" + upLoading.getEquipmentNum());
        printP.add("son");
        switch (upLoading.getN()) {
            case 1:
                printP.add("支付方式:微信支付");
                break;
            case 2:
                printP.add("支付方式:支付宝支付");
                break;
            case 3:
                printP.add("支付方式:第三方支付");
                break;
            case 4:
                printP.add("支付方式:会员卡支付");
                break;
            case 0:
                printP.add("支付方式:现金支付");
                break;
        }
        printP.add("总金额:" + upLoading.getPayshould());
        printP.add("实收金额 :" + upLoading.getPayactual());
        printP.add("优惠金额 :" + upLoading.getYouhui());
        printP.add("获得积分 :" + upLoading.getGetIntegral());
        printP.add("剩余储值 :" + haveMoney);
        printP.add("剩余积分 :" + haveIntegral);
        printPage("会员购次补单小票", printP, list_son, false);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_recharge_supplement);
    }

    private void printError(int id) {
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
                printInformation(0);
            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }

    @Override
    public void OnPrintSuccess() {
        isChangeActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 86) {
                DataSupport.delete(BuyCountDb.class, list.get(0).getId());
                Log.d("uuz", "onActivityResult: ");
                finish();
            }
        }
    }
}