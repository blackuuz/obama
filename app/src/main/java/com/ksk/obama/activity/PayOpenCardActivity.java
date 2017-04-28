package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.OpenCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayOpenCardActivity extends BasePrintActivity implements View.OnClickListener, IPayCallBack,
        IPrintSuccessCallback, IPrintErrorCallback {

    private TextView tv_money;
    private TextView tv_pay0;
    private TextView tv_pay1;
    private TextView tv_pay3;
    private int n = -1;
    private OpenCardInfo cardInfo;
    private String orderno = "";
    private TextView tv_print;
    private String orderTime = "";
    private boolean flag = false;
    private boolean isPay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        this.setOnPayCallBack(this);
        this.setOnPrintSuccess(this);
        this.setOnPrintError(this);
        getOrderNum("KK");
        initTitale();
        initView();
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("info");
            tv_money.setText("实际收款:" + cardInfo.getPaymoney() + "元");
        }
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPay) {
                    changeActivity();
                } else {
                    finish();
                }

            }
        });
        tv_print.setText("重打印");
        title_name.setText("结账");
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo(true);
            }
        });
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_pay_open_money);
        switch (robotType) {
            case 1:
                findViewById(R.id.ll_sm).setVisibility(View.GONE);
                tv_pay0 = (TextView) findViewById(R.id.tv_pay_0);
                tv_pay1 = (TextView) findViewById(R.id.tv_pay_1);
                tv_pay3 = (TextView) findViewById(R.id.tv_pay_3);
                break;

            case 3:
            case 4:
            case 8:
                findViewById(R.id.ll_lkl).setVisibility(View.GONE);
                tv_pay0 = (TextView) findViewById(R.id.tv_pay_0_0);
                tv_pay1 = (TextView) findViewById(R.id.tv_pay_1_1);
                tv_pay3 = (TextView) findViewById(R.id.tv_pay_2);
                break;
        }


        tv_pay0.setOnClickListener(this);
        tv_pay1.setOnClickListener(this);
        tv_pay3.setOnClickListener(this);
        this.setOnPayCallBack(this);
    }

    private void sendData() {
        isPay = true;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderTime = simpleFormat.format(date);
        Map<String, String> map = new HashMap<>();
        map.put("cardNO", cardInfo.getCardnum());
        map.put("memberName", cardInfo.getName());
        map.put("sex", cardInfo.getSex());
        map.put("mobile", cardInfo.getPhone());
        String birthday = cardInfo.getBirthday();
        if (TextUtils.isEmpty(birthday)) {
            birthday = orderTime.substring(0, 10);
        }
        String str[] = birthday.split("-");
        if (birthday != null && birthday.length() != 0) {
            map.put("birthdayYear", str[0] + "");
            map.put("birthdayMonth", str[1] + "");
            map.put("birthdayDay", str[2] + "");
        }
        map.put("Supplement", "0");
        map.put("classID", cardInfo.getGoodsId() + "");
        map.put("userID", SharedUtil.getSharedData(PayOpenCardActivity.this, "userInfoId"));
        map.put("payActual", cardInfo.getPaymoney());
        map.put("orderNo", orderNumber);
        map.put("CardCode", cardInfo.getUid());
        map.put("c_Billfrom", robotType + "");
        switch (n) {
            case 0:
                map.put("payCash", cardInfo.getPaymoney());
                break;
            case 1:
                map.put("payWeChat", cardInfo.getPaymoney());
                map.put("refernumber", orderno);
                break;
            case 2:
                map.put("payAli", cardInfo.getPaymoney());
                map.put("refernumber", orderno);
                break;
            case 3:
                map.put("payBank", cardInfo.getPaymoney());
                map.put("refernumber", orderno);
                break;
        }
        map.put("EquipmentNum", terminalSn);
        map.put("C_ServiceEmployee", cardInfo.getAddPerson());
        map.put("memid", cardInfo.getAddId());
        map.put("password", cardInfo.getPassword());
        map.put("dbName", SharedUtil.getSharedData(PayOpenCardActivity.this, "dbname"));

        postToHttp(NetworkUrl.ADDMEMBER, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                showData(jsonText);
            }

            @Override
            public void OnFail(String message) {
                showAlert();
            }
        });
    }

    private void showData(String json) {
        try {
            JSONObject object = new JSONObject(json);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                flag = true;
                tv_print.setVisibility(View.VISIBLE);
                tv_print.setEnabled(true);
                tv_pay0.setVisibility(View.GONE);
                tv_pay1.setVisibility(View.GONE);
                tv_pay3.setVisibility(View.GONE);
                String msg = object.getString("result_msg");
                Utils.showToast(PayOpenCardActivity.this, msg);
                payHint(true);
                printInfo(true);
                switch (robotType) {
                    case 3:
                    case 4:
                        changeActivity();
                        break;
                }
            } else if (tag.equals("ERR")) {
                isclick_pay = true;
                String msg = object.getString("result_errmsg");
                Utils.showToast(PayOpenCardActivity.this, msg);
            } else {
                payHint(false);
                showAlert();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void payHint(boolean flag) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_pay_hint);
        TextView tv = (TextView) findViewById(R.id.tv_pay_hint);
        ImageView iv = (ImageView) findViewById(R.id.iv_pay_hint);
        ll.setVisibility(View.VISIBLE);
        tv.setText(flag ? "支付成功" : "支付失败,请重试");
        iv.setSelected(flag);
    }

    @Override
    public void onClick(View v) {
        if (isclick_pay) {
            isclick_pay = false;
            switch (v.getId()) {
                case R.id.tv_pay_0:
                case R.id.tv_pay_0_0:
                    if (isXJ) {
                        n = 0;
                        sendData();
                    } else {
                        isclick_pay = true;
                        Utils.showToast(PayOpenCardActivity.this, "没有开通此功能");
                    }

                    break;
                case R.id.tv_pay_1:
                case R.id.tv_pay_1_1:
                    n = 1;
                    payMoney(1, cardInfo.getPaymoney(), orderNumber, "开卡");
                    break;
                case R.id.tv_pay_2:
                    n = 2;
                    payMoney(2, cardInfo.getPaymoney(), orderNumber, "开卡");
                    break;
                case R.id.tv_pay_3:
                    n = 3;
                    payMoney(3, cardInfo.getPaymoney(), orderNumber, "开卡");
                    break;
            }
        }
    }

    private void setDBData() {
        Connector.getDatabase();
        OpenCardDb openCardDb = new OpenCardDb();
        openCardDb.setN(n);
        openCardDb.setUid(cardInfo.getUid());
        openCardDb.setUrl(NetworkUrl.ADDMEMBER);
        openCardDb.setCardNO(cardInfo.getCardnum());
        openCardDb.setMemberName(cardInfo.getName());
        openCardDb.setSex(cardInfo.getSex());
        openCardDb.setMobile(cardInfo.getPhone());
        openCardDb.setOrderNo(orderNumber);
        if (cardInfo.getBirthday() != null && cardInfo.getBirthday().length() != 0) {
            openCardDb.setBirthdayYear(cardInfo.getBirthday().substring(0, 4));
            openCardDb.setBirthdayMonth(cardInfo.getBirthday().substring(5, 7));
            openCardDb.setBirthdayDay(cardInfo.getBirthday().substring(8, 10));
        } else {
            openCardDb.setBirthdayYear("");
            openCardDb.setBirthdayMonth("");
            openCardDb.setBirthdayDay("");
        }
        openCardDb.setClassID(cardInfo.getGoodsId());
        openCardDb.setUserID(SharedUtil.getSharedData(PayOpenCardActivity.this, "userInfoId"));
        openCardDb.setPayActual(cardInfo.getPaymoney());
        openCardDb.setEquipmentNum(terminalSn);
        openCardDb.setDbName(SharedUtil.getSharedData(PayOpenCardActivity.this, "dbname"));
        openCardDb.setUserName(SharedUtil.getSharedData(PayOpenCardActivity.this, "username"));
        openCardDb.setShopName(SharedUtil.getSharedData(PayOpenCardActivity.this, "shopname"));
        openCardDb.setOrderTime(orderTime);
        openCardDb.setCardType(cardInfo.getCardName());
        openCardDb.setChushimoney(cardInfo.getStr0());
        openCardDb.setChushijf(cardInfo.getStr1());
        openCardDb.setAddId(cardInfo.getAddId());
        openCardDb.setAddName(cardInfo.getAddPerson());
        if (TextUtils.isEmpty(cardInfo.getPrice())) {
            openCardDb.setCardMoney("0");
        } else {
            openCardDb.setCardMoney(cardInfo.getPrice());
        }
        openCardDb.setRefernumber(orderno);
        if (!TextUtils.isEmpty(cardInfo.getCardnum()) && !TextUtils.isEmpty(orderNumber) && !TextUtils.isEmpty(cardInfo.getCardName())) {
            Logger.d("开卡" + openCardDb.save() + "");
        }
    }

    private boolean alert_flag = true;

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(PayOpenCardActivity.this);
        dialog.setTitle(R.string.dialog_warnning_name);
        dialog.setMessage(R.string.dialog_warnning_message);
        dialog.setPositiveButton(R.string.dialog_warnning_yes, null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (alert_flag) {
                    if (!TextUtils.isEmpty(cardInfo.getCardnum())
                            && !TextUtils.isEmpty(orderNumber)
                            && !TextUtils.isEmpty(cardInfo.getCardName())) {
                        setDBData();
                        printInfo(false);
                        switch (robotType) {
                            case 3:
                                changeActivity();
                                break;
                        }
                    } else {
                        changeActivity();
                    }
                }
                alert_flag = true;
            }
        });
        dialog.setNegativeButton(R.string.dialog_warnning_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert_flag = false;
                sendData();
            }
        });
        dialog.create().show();
    }

    private synchronized void printInfo(boolean flag) {
        printCount = 0;
        List<String> listp = new ArrayList<>();
        listp.add("消费店面: " + SharedUtil.getSharedData(PayOpenCardActivity.this, "shopname"));
        listp.add("操作员 :" + SharedUtil.getSharedData(PayOpenCardActivity.this, "username"));
        listp.add("手持序列号:" + terminalSn);
        listp.add((flag ? "订单时间:" : "订单生成时间") + orderTime);
        listp.add("订单号:" + orderNumber);
        listp.add("推荐人:" + cardInfo.getAddPerson());
        listp.add("会员卡号:" + cardInfo.getCardnum());
        listp.add("会员姓名:" + cardInfo.getName());
        listp.add("卡类型:" + cardInfo.getCardName());
        listp.add("初始储值:" + Utils.getNumStr(Float.parseFloat(cardInfo.getStr0())));
        listp.add("初始积分:" + cardInfo.getStr1());
        String str1 = cardInfo.getPrice();
        if (TextUtils.isEmpty(str1)) {
            listp.add("卡价格:0");
        } else {
            listp.add("卡价格:" + Utils.getNumStr(Float.parseFloat(str1)));
        }
        String str2 = cardInfo.getPaymoney();
        if (TextUtils.isEmpty(str2)) {
            listp.add("实收金额:0");
        } else {
            listp.add("实收金额:" + Utils.getNumStr(Float.parseFloat(str2)));
        }
        switch (n) {
            case 0:
                listp.add("支付方式:现金支付");
                break;
            case 1:
                listp.add("支付方式:微信支付");
                break;
            case 2:
                listp.add("支付方式:支付宝支付");
                break;
            case 3:
                listp.add("支付方式:银联支付");
                break;
        }
        if (cardInfo.getPhone().length() == 11) {
            listp.add("会员手机号:" + cardInfo.getPhone());
        } else {
            listp.add("会员手机号:无");
        }
        if (flag) {
            printPage("会员开卡小票", listp, null, false);
        } else {
            printPage("会员开卡延时扣款小票", listp, null, false);
            printPage("会员开卡延时扣款小票(商户存根)", listp, null, false);
        }

    }

    @Override
    public void OnPaySucess(String orderNum, int payMode) {
        orderno = orderNum;
        n = payMode;
        sendData();
        loadingDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isPay) {
            changeActivity();
        } else {
            super.onBackPressed();
        }
    }

    private void changeActivity() {
        Intent intent = new Intent(PayOpenCardActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_pay);
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
                    printInfo(flag);
                }

            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }

    private int printCount = 0;

    @Override
    public void OnPrintSuccess() {
        printCount++;
        if (flag) {
            changeActivity();
        } else {
            if (printCount >= 2) {
                changeActivity();
            }
        }
    }

}
