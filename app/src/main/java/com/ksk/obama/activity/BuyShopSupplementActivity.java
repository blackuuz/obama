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

import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBackS;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.PrintPage;

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

public class BuyShopSupplementActivity extends BaseSupplementActivity implements IPrintErrorCallback, IPrintSuccessCallback {
    private List<BuyShopDb> list = new ArrayList<>();
    private int orderNum = 0;
    private int orderYes = 0;
    private ProgressBar pb2;
    private ProgressBar pb1;
    private TextView tv_hint;
    private boolean isComplition = false;
    private String haveMoney;
    private String haveIntegral;

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
        name.setText("商品消费补单");
        findViewById(R.id.tv_commit).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isComplition) {
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
        list = DataSupport.findAll(BuyShopDb.class);
        pb2.setMax(list.size());
        pb2.setProgress(0);
        tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
        send();
    }

    private void send() {
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            BuyShopDb upLoading = list.get(i);
            String url = upLoading.getUrl();
            String dbname = upLoading.getDbName();
            String userID = upLoading.getUser_Id();
            String m_is = upLoading.getPayActual();
            String money = upLoading.getPayShould();
            String orderidScan = upLoading.getOrder_again();
            int payMode = upLoading.getN();
            map.put("goods_id", upLoading.getGoods_id());
            map.put("num", upLoading.getNum());
            map.put("money", upLoading.getMoney());
            map.put("integral", upLoading.getIntegral());
            map.put("Member_Id", upLoading.getMember_Id());
            map.put("get_integral", upLoading.getGet_integral());
            map.put("EquipmentNum", upLoading.getEquipmentNum());
            map.put("dbName", dbname);
            map.put("c_Billfrom", robotType + "");
            map.put("CardCode", upLoading.getUid());
            map.put("PayDiscounted", upLoading.getPayDiscounted());
            map.put("User_Id", userID);
            map.put("PayActual", m_is);
            map.put("PayShould", money + "");
            map.put("orderNo", upLoading.getOrderNo());
            map.put("EquipmentNum", upLoading.getEquipmentNum());
            map.put("Supplement", "1");
            switch (payMode) {
                case 0:
                    map.put("payCash", m_is);
                    break;
                case 4:
                    map.put("payCard", m_is);
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

    public void showData(String str, final int n) {
        try {
            orderNum += 1;
            JSONObject object = new JSONObject(str);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                orderYes += 1;
                pb2.setProgress(orderYes);
                tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
                if (list.get(n).isVip()) {
                    haveMoney = object.getString("AmountAvailable");
                    haveIntegral = object.getString("IntegralAvailable");
                }
                DataSupport.delete(BuyShopDb.class, list.get(n).getId());
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
//            if (orderYes < list.size()) {
//                showAlert();
//            } else {
            isComplition = true;
            finish();
//            }
        }
    }


    private synchronized void printInformation(final int n) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleFormat.format(date);
        BuyShopDb upLoading = list.get(n);
        final List<PrintPage> list_son = new ArrayList<>();
        for (int i = 0; i < upLoading.getDataList().size(); i++) {
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
        printP.add("卡号 :" + upLoading.getCardNum());
        printP.add("姓名:" + upLoading.getName());
        printP.add("操作员 :" + upLoading.getUserName());
        printP.add("手持序列号:" + upLoading.getEquipmentNum());
        printP.add("son");
        switch (upLoading.getN()) {
            case 0:
                printP.add("支付方式:现金支付");
                break;
            case 1:
                printP.add("支付方式:微信支付");
                break;
            case 2:
                printP.add("支付方式:支付宝支付");
                break;
            case 3:
                printP.add("支付方式:银联支付");
                break;
            case 4:
                printP.add("支付方式:会员卡支付");
                break;
        }
        printP.add("总金额:" + upLoading.getPayShould());
        printP.add("实收金额 :" + upLoading.getPayActual());
        printP.add("折扣优惠 :" + upLoading.getYouhui());
        printP.add("卡券优惠 :" + upLoading.getDelkq());
        printP.add("积分优惠 :" + upLoading.getDeljf());
        if (upLoading.isVip()) {
            printP.add("获得积分 :" + upLoading.getGet_integral());
            printP.add("剩余储值 :" + haveMoney);
            printP.add("剩余积分 :" + haveIntegral);
        }
        printPage("购物补单小票", printP, list_son, false);
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
