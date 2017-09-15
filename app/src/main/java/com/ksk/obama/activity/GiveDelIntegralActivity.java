package com.ksk.obama.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.OrderNumber;
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

import static com.ksk.obama.utils.SharedUtil.getSharedData;
import static org.litepal.tablemanager.Connector.getDatabase;

public class GiveDelIntegralActivity extends BasePrintActivity implements IPrintSuccessCallback, IPrintErrorCallback {

    private TextView tv4;
    private float integral;
    private EditText et_integral;
    private TextView tv_print;
    private ReadCardInfo.ResultDataBean cardInfo;
    private int n = -1;
    private String del_integral;
    private String ordernb = "";
    private String orderte = "";
    private String uid;
    private boolean isJurisdiction = false;//是否拥有权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_del_integral);
        this.setOnPrintSuccess(this);
        this.setOnPrintError(this);
        initTitale();
        initData();
        initView();
        getOrderNum("KJ");
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
        tv_print.setText("重打印");
        title_name.setText("赠扣积分");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });
    }

    private void initData() {
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("infoma");
            integral = Float.parseFloat(cardInfo.getN_IntegralAvailable());
            uid = getIntent().getExtras().getString("uid");
        }
    }

    private void payHint(boolean flag) {
        LinearLayout ll_hint = (LinearLayout) findViewById(R.id.ll_pay_hint);
        ImageView iv_hint = (ImageView) findViewById(R.id.iv_pay_hint);
        TextView tv_hint = (TextView) findViewById(R.id.tv_pay_hint);
        ll_hint.setVisibility(View.VISIBLE);
        iv_hint.setSelected(flag);
        tv_hint.setText((flag ? "成功" : "失败"));
    }

    private void initView() {
        if (SharedUtil.getSharedBData(GiveDelIntegralActivity.this, "giveInte")) {
            isJurisdiction = true;
        } else {
            findViewById(R.id.btn_give).setVisibility(View.INVISIBLE);
        }
        et_integral = (EditText) findViewById(R.id.et_g_d_integral);
        TextView tv0 = (TextView) findViewById(R.id.tv_exit_str0);
        TextView tv1 = (TextView) findViewById(R.id.tv_exit_str1);
        TextView tv2 = (TextView) findViewById(R.id.tv_exit_str2);
        TextView tv3 = (TextView) findViewById(R.id.tv_exit_str3);
        tv4 = (TextView) findViewById(R.id.tv_exit_str4);
        TextView tv5 = (TextView) findViewById(R.id.tv_exit_str5);
        InputFilter[] filter = {new MyTextFilter()};
        et_integral.setFilters(filter);

        findViewById(R.id.btn_give).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    n = 0;
                    sendIntegral();

                }
            }
        });
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    n = 1;
                    sendIntegral();
                }
            }
        });

        tv0.setText("会员卡号:" + cardInfo.getC_CardNO());
        tv1.setText("会员姓名:" + cardInfo.getC_Name());
        tv2.setText("当前储值:￥" + cardInfo.getN_AmountAvailable());
        tv3.setText("折扣倍率:" + cardInfo.getN_DiscountValue() + "%");
        tv4.setText("当前积分:" + integral);
        tv5.setText("积分倍率:" + cardInfo.getN_IntegralValue() + "%");

    }

    private void sendIntegral() {
        del_integral = et_integral.getText().toString();
        if (TextUtils.isEmpty(del_integral)) {
            isclick_pay = true;
            Utils.showToast(GiveDelIntegralActivity.this, "请输入积分");
        } else {
            ordernb = orderNumber;
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderte = simpleFormat.format(date);
            Map<String, String> map = new HashMap<>();
            map.put("integral_val", del_integral);
            map.put("CardNO", cardInfo.getC_CardNO());
            map.put("YdUserinfoId", getSharedData(GiveDelIntegralActivity.this, "userInfoId"));
            map.put("dbName", getSharedData(GiveDelIntegralActivity.this, "dbname"));
            map.put("Equipment", terminalSn);
            map.put("orderNo", orderNumber);
            map.put("CardCode", uid);
            map.put("c_Billfrom", robotType + "");
            if (n == 0) {
                map.put("type", "add");
            }

            postToHttp(NetworkUrl.FAST_EXCHANGE, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    showResult(jsonText, del_integral);
                }

                @Override
                public void OnFail(String message) {
                    isclick_pay = true;
                    Utils.showToast(GiveDelIntegralActivity.this, message);
                }
            });
        }
    }

    private void showResult(String text, String str) {
        try {
            JSONObject object = new JSONObject(text);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                if (n == 0) {
                    integral += Float.parseFloat(str);
                } else if (n == 1) {
                    integral -= Float.parseFloat(str);
                }
                tv4.setText("当前积分:" + Utils.getNumStr(integral));
                payHint(true);
                setOrderDB();
                tv_print.setVisibility(View.VISIBLE);
                tv_print.setEnabled(true);
                printInfo();
                switch (robotType) {
                    case 3:
                    case 4:
                        getOrderNum("KJ");
                        et_integral.setText("");
                        isclick_pay = true;
                        break;
                }
            } else {
                isclick_pay = true;
                payHint(false);
                Utils.showToast(GiveDelIntegralActivity.this, (n == 0 ? "赠送" : "扣除") + "积分失败,请重试");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOrderDB() {
        getDatabase();
        OrderNumber upLoading = new OrderNumber();
        upLoading.setOrderNumber(orderNumber);
        upLoading.setGroupId(getSharedData(GiveDelIntegralActivity.this, "groupid"));
        upLoading.setDbName(getSharedData(GiveDelIntegralActivity.this, "dbname"));
        upLoading.setCardNum(cardInfo.getC_CardNO());
        upLoading.setCardCode(uid);
        upLoading.setGetIntegral(del_integral);
        upLoading.setPayType(n);
        upLoading.setUserId(getSharedData(GiveDelIntegralActivity.this, "userInfoId"));
        upLoading.setTime(orderte);
        upLoading.setFormClazz("KJ");
        upLoading.save();
    }

    private void printInfo() {
        List<String> list = new ArrayList<>();
        String str = "";
        list.add("时间:" + orderte);
        list.add("单据号:" + ordernb);
        list.add("会员卡号 :" + cardInfo.getC_CardNO());
        list.add("会员姓名:" + cardInfo.getC_Name());
        switch (n) {
            case 0:
                str = "赠送积分小票";
                list.add("赠送积分 :" + del_integral);
                break;
            case 1:
                str = "扣除积分小票";
                list.add("扣除积分 :" + del_integral);
                break;
        }
        list.add("可用积分:" + Utils.getNumStr(integral));
        list.add("剩余储值:￥" + cardInfo.getN_AmountAvailable());

        printPage(str, list, null, true);
    }


    @Override
    public void OnPrintError() {
        printError(R.id.activity_give_del_integral);
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
        getOrderNum("KJ");
        et_integral.setText("");
        isclick_pay = true;
    }


}
