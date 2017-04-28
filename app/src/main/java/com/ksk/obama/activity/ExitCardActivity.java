package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.MyTextFilter;
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

public class ExitCardActivity extends BasePrintActivity implements View.OnClickListener, IPrintErrorCallback, IPrintSuccessCallback {

    private TextView tv_cardNum;
    private TextView tv_is;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_str0;
    private TextView tv_str1;
    private TextView tv_str2;
    private TextView tv_str3;
    private EditText et_money;
    private TextView pay0;
    private TextView pay1;
    private TextView pay2;
    private TextView pay3;
    private int n = -1;
    private String id = "";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_card);
        setOnPrintSuccess(this);
        setOnPrintError(this);
        initTitale();
        initView();
        setData();
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
        tv_print.setText("确定");
        title_name.setText("退卡销户");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    getHttpData();
                }
            }
        });
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_exit_cardNum);
        tv_is = (TextView) findViewById(R.id.tv_exit_cardis);
        tv_name = (TextView) findViewById(R.id.tv_exit_name);
        tv_type = (TextView) findViewById(R.id.tv_exit_type);
        tv_time = (TextView) findViewById(R.id.tv_exit_time);
        tv_str0 = (TextView) findViewById(R.id.tv_exit_str0);
        tv_str1 = (TextView) findViewById(R.id.tv_exit_str1);
        tv_str2 = (TextView) findViewById(R.id.tv_exit_str2);
        tv_str3 = (TextView) findViewById(R.id.tv_exit_str3);
        et_money = (EditText) findViewById(R.id.et_exit_money);
        pay0 = (TextView) findViewById(R.id.tv_pay_open_0);
        pay1 = (TextView) findViewById(R.id.tv_pay_open_1);
        pay2 = (TextView) findViewById(R.id.tv_pay_open_2);
        pay3 = (TextView) findViewById(R.id.tv_pay_open_3);

        InputFilter[] filters = {new MyTextFilter()};
        et_money.setFilters(filters);

        pay0.setOnClickListener(this);
        pay1.setOnClickListener(this);
        pay2.setOnClickListener(this);
        pay3.setOnClickListener(this);

    }

    private void setData() {
        if (getIntent() != null) {
            ReadCardInfo.ResultDataBean cardInfo = getIntent().getExtras().getParcelable("infoma");
            uid = getIntent().getExtras().getString("uid");
            id = cardInfo.getId();
            tv_cardNum.setText(cardInfo.getC_CardNO());
            tv_name.setText(cardInfo.getC_Name());
            tv_type.setText(cardInfo.getC_ClassName());
            tv_time.setText(cardInfo.getT_StopTime().substring(0, 11));
            tv_str0.setText("当前储值 :  " + cardInfo.getN_AmountAvailable());
            tv_str1.setText("折扣倍率 :  " + cardInfo.getN_DiscountValue() + "%");
            tv_str2.setText("当前积分 :  " + cardInfo.getN_IntegralAvailable());
            tv_str3.setText("积分倍率 :  " + cardInfo.getN_IntegralValue() + "%");
        }
    }

    private void getHttpData() {
        if (n == -1) {
            Utils.showToast(ExitCardActivity.this, "请选择支付方式");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("dbName", SharedUtil.getSharedData(ExitCardActivity.this, "dbname"));
            map.put("User_Id", SharedUtil.getSharedData(ExitCardActivity.this, "userInfoId"));
            map.put("Member_Id", id);
            map.put("orderNo", getOrderNum("TK"));
            map.put("EquipmentNum", terminalSn);
            map.put("c_Billfrom", robotType + "");
            map.put("CardCode", uid);
            map.put("money", et_money.getText().toString().equals("") ? 0 + "" : et_money.getText().toString());
            switch (n) {
                case 0:
                    map.put("pay_way", "现金");
                    map.put("payCash", et_money.getText().toString().equals("") ? 0 + "" : et_money.getText().toString());
                    break;
                case 1:
                    map.put("pay_way", "微信");
                    map.put("payWeChat", et_money.getText().toString().equals("") ? 0 + "" : et_money.getText().toString());
                    break;
                case 2:
                    map.put("pay_way", "支付宝");
                    map.put("payAli", et_money.getText().toString().equals("") ? 0 + "" : et_money.getText().toString());
                    break;
                case 3:
                    map.put("pay_way", "银联");
                    map.put("payBank", et_money.getText().toString().equals("") ? 0 + "" : et_money.getText().toString());
                    break;
            }

            postToHttp(NetworkUrl.EXITCARD, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String tag = object.getString("result_stadus");
                        if (tag.equals("SUCCESS")) {
                            printInfo();
                        } else {
                            Utils.showToast(ExitCardActivity.this, "操作失败,请重试");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFail(String message) {
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay_open_0:
                n = 0;
                pay0.setBackgroundResource(R.mipmap.xj1);
                pay1.setBackgroundResource(R.mipmap.wx);
                pay2.setBackgroundResource(R.mipmap.zfb);
                pay3.setBackgroundResource(R.mipmap.yl);
                break;
            case R.id.tv_pay_open_1:
                n = 1;
                pay0.setBackgroundResource(R.mipmap.xj);
                pay1.setBackgroundResource(R.mipmap.wx1);
                pay2.setBackgroundResource(R.mipmap.zfb);
                pay3.setBackgroundResource(R.mipmap.yl);
                break;
            case R.id.tv_pay_open_2:
                n = 2;
                pay0.setBackgroundResource(R.mipmap.xj);
                pay1.setBackgroundResource(R.mipmap.wx);
                pay2.setBackgroundResource(R.mipmap.zfb1);
                pay3.setBackgroundResource(R.mipmap.yl);
                break;
            case R.id.tv_pay_open_3:
                n = 3;
                pay0.setBackgroundResource(R.mipmap.xj);
                pay1.setBackgroundResource(R.mipmap.wx);
                pay2.setBackgroundResource(R.mipmap.zfb);
                pay3.setBackgroundResource(R.mipmap.yl1);
                break;
        }
    }

    private String getTime1() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    private void printInfo() {
        String money = et_money.getText().toString();
        List<String> listp = new ArrayList<>();
        listp.add("退卡时间:" + getTime1());
        listp.add("订单号:" + orderNumber);
        listp.add("卡号:" + tv_cardNum.getText().toString());
        listp.add("姓名:" + tv_name.getText().toString());
        switch (n) {
            case 0:
                listp.add("退款方式:现金");
                break;
            case 1:
                listp.add("退款方式:微信");
                break;
            case 2:
                listp.add("退款方式:支付宝");
                break;
            case 3:
                listp.add("退款方式:银联");
                break;
        }
        listp.add("退款金额:" + (money.equals("") ? 0 + "" : money));
        listp.add("手持序列号:" + terminalSn);
        listp.add("退卡店面:" + SharedUtil.getSharedData(ExitCardActivity.this, "shopname"));
        listp.add("操作员:" + SharedUtil.getSharedData(ExitCardActivity.this, "username"));
        printPage("退卡详情", listp, null,false);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_exit_card);
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

    @Override
    public void OnPrintSuccess() {
        startActivity(new Intent(ExitCardActivity.this, MainActivity.class));
    }
}
