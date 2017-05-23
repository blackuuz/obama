package com.ksk.obama.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.RechargeAgain;
import com.ksk.obama.R;
import com.ksk.obama.adapter.RechargeAdapter;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
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
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ksk.obama.R.id.et_rech_pay_give;
import static com.ksk.obama.R.id.tv_commit;
import static com.ksk.obama.utils.SharedUtil.getSharedData;

/**
 * 会员充值的页面
 */
public class RechargeActivity extends BasePrintActivity implements View.OnClickListener, IPayCallBack,
        IPrintSuccessCallback, IPrintErrorCallback, ICreateOrderNumber {

    private TextView tv_print;
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_str0;
    private TextView tv_str1;
    private TextView tv_str2;
    private TextView tv_str3;
    private TextView tv_money;
    private EditText tv_pay;
    private TextView tv_clean;


    private EditText et_paya;
    private EditText et_integral;//积分
    private EditText et_pay_give;
    private Button btn_select;
    private Button btn_modify0;
    private Button btn_modify1;
    private Button btn_modify2;
    private int n = -1;
    private float n_integra = 0;
    private float oldMoney;
    private float oldintegra;
    private String order_again = "";
    private String newMoney;
    private String newintegra;
    private String str1;
    private String str2;
    private boolean flag = false;
    private String cardNum;
    private String cardName;
    private String cardType;
    private String orderTime = "";
    private String uid = "";
    private String temName;
    private String temporaryNum;
    private boolean isTemporary;
    private boolean isflag;
    private String paysend = "";
    private String key = "";
    private String result = "";

    private String globalIntegral = "";//全局积分倍率
    private String rechargeIntegral = "";//充值积分倍率
    private Float gi = 0f;
    private Float ri = 0f;
    private Float hi = 0f;

    private ListView lv_recharge;
    // float integra;
    private ReadCardInfo.ResultDataBean cardInfo = null;
    private Unbinder unbinder;
    private RechargeAdapter adapter;

    @BindView(R.id.tv_pay_xj)
    TextView pay_xj;
    @BindView(R.id.tv_pay_xj1)
    TextView pay_xj1;
    @BindView(R.id.tv_pay_sm)
    TextView pay_sm;
    @BindView(R.id.tv_pay_yl)
    TextView pay_yl;
    @BindView(R.id.tv_pay_wx)
    TextView pay_wx;
    @BindView(R.id.tv_pay_zfb)
    TextView pay_zfb;

    @BindView(R.id.ll_lkl)
    LinearLayout ll_lkl;
    @BindView(R.id.ll_w_a)
    LinearLayout ll_w_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setOnPrintError(this);
        setOnPrintSuccess(this);
        setOnCrateOrderNumber(this);
        unbinder = ButterKnife.bind(this);
        initTitale();
        initView();
        setListener();
        initData();
        getOrderNum("CZ");
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        tv_print = (TextView) findViewById(tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("会员充值");
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
        tv_print.setText("重打印");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo(true);
            }
        });
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_rech_cardNum);
        tv_name = (TextView) findViewById(R.id.tv_rech_name);
        tv_type = (TextView) findViewById(R.id.tv_rech_type);
        tv_time = (TextView) findViewById(R.id.tv_rech_time);
        tv_str0 = (TextView) findViewById(R.id.tv_rech_str0);
        tv_str1 = (TextView) findViewById(R.id.tv_rech_str1);
        tv_str2 = (TextView) findViewById(R.id.tv_rech_str2);
        tv_str3 = (TextView) findViewById(R.id.tv_rech_str3);
        tv_money = (TextView) findViewById(R.id.tv_rech_money);
        tv_clean = (TextView) findViewById(R.id.tv_clean);

        btn_modify0 = (Button) findViewById(R.id.btn_modify_je);
        btn_modify1 = (Button) findViewById(R.id.btn_modify_jf);
        btn_modify2 = (Button) findViewById(R.id.btn_modify_ss);


        et_paya = (EditText) findViewById(R.id.et_rech_paya);//充值金额
        tv_pay = (EditText) findViewById(R.id.tv_rech_pay);//edittext 实际金额
        et_pay_give = (EditText) findViewById(et_rech_pay_give);//赠送金额
        et_integral = (EditText) findViewById(R.id.et_rech_integral);//赠送积分

        tv_pay.setInputType(InputType.TYPE_NULL);//禁止软件盘输入
        et_pay_give.setInputType(InputType.TYPE_NULL);
        et_integral.setInputType(InputType.TYPE_NULL);



        if (SharedUtil.getSharedBData(RechargeActivity.this, "zsje")) {
            btn_modify0.setVisibility(View.GONE);
            //et_pay_give.setClickable(true);
            et_pay_give.setFocusableInTouchMode(true);
            et_pay_give.setFocusable(true);
            et_pay_give.requestFocus();
            et_pay_give.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (SharedUtil.getSharedBData(RechargeActivity.this, "zsjf")) {
            btn_modify1.setVisibility(View.GONE);
            et_integral.setFocusableInTouchMode(true);
            et_integral.setFocusable(true);
            et_integral.requestFocus();
           // et_integral.setClickable(true);
            et_integral.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        }
        if (SharedUtil.getSharedBData(RechargeActivity.this, "ssje")) {
            btn_modify2.setVisibility(View.GONE);
            tv_pay.setFocusableInTouchMode(true);
            tv_pay.setFocusable(true);
            tv_pay.requestFocus();
            //tv_pay.setClickable(true);
            tv_pay.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        }


        if (!SharedUtil.getSharedBData(RechargeActivity.this, "RX")) {
            pay_xj.setVisibility(View.GONE);
            pay_xj1.setVisibility(View.GONE);
        }
        switch (robotType) {
            case 1:
                ll_w_a.setVisibility(View.GONE);
                if (SharedUtil.getSharedBData(RechargeActivity.this, "RW") && SharedUtil.getSharedBData(RechargeActivity.this, "RA")) {
                    pay_sm.setVisibility(View.VISIBLE);
                } else {
                    pay_sm.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(RechargeActivity.this, "RY")) {
                    pay_yl.setVisibility(View.GONE);
                }
                break;
            case 3:
            case 4:
            case 8:
                ll_lkl.setVisibility(View.GONE);
                if (!SharedUtil.getSharedBData(RechargeActivity.this, "RW")) {
                    pay_wx.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(RechargeActivity.this, "RA")) {
                    pay_zfb.setVisibility(View.GONE);
                }

                break;
        }

        btn_select = (Button) findViewById(R.id.btn_rech);
        lv_recharge = (ListView) findViewById(R.id.lv_recharge);
        if(!SharedUtil.getSharedBData(RechargeActivity.this,"fast_recharge")){
            btn_select.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("infoma");
            uid = getIntent().getExtras().getString("uid");
           // n_integra = Float.parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
            oldMoney = Float.parseFloat(cardInfo.getN_AmountAvailable());
            oldintegra = Float.parseFloat(cardInfo.getN_IntegralAvailable());
            cardNum = cardInfo.getC_CardNO();
            cardName = cardInfo.getC_Name();
            cardType = cardInfo.getC_ClassName();
            tv_cardNum.setText(cardNum);
            tv_name.setText(cardName);
            tv_type.setText(cardType);
            tv_time.setText(cardInfo.getT_StopTime().substring(0, 11));
            tv_str0.setText("" + oldMoney);//当前储值
            tv_str1.setText("" + cardInfo.getN_DiscountValue() + "%");//折扣倍率
            tv_str2.setText("" + oldintegra);//当前积分
            tv_str3.setText("" + cardInfo.getN_Recharge_Integral_Value() + "%");//积分倍率
        }

                if(adapter != null){
                adapter.notifyDataSetChanged();
            }


        adapter = new RechargeAdapter(RechargeActivity.this,list_rechargefast);

        globalIntegral = SharedUtil.getSharedData(RechargeActivity.this, "shopintegral");//全局积分倍数
        rechargeIntegral = SharedUtil.getSharedData(RechargeActivity.this, "rechargepoints");//充值积分倍数
    }

    private void setListener() {
        this.setOnPayCallBack(this);
        pay_xj1.setOnClickListener(this);
        pay_sm.setOnClickListener(this);
        pay_wx.setOnClickListener(this);
        pay_xj.setOnClickListener(this);
        pay_zfb.setOnClickListener(this);
        pay_yl.setOnClickListener(this);

        tv_clean.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        btn_modify0.setOnClickListener(this);
        btn_modify1.setOnClickListener(this);
        btn_modify2.setOnClickListener(this);


        InputFilter[] filters = {new MyTextFilter()};
        et_paya.setFilters(filters);
        et_pay_give.setFilters(filters);
        et_integral.setFilters(filters);
        tv_pay.setFilters(filters);
        // et_pay_give.setInputType(InputType.TYPE_NULL);
        // et_integral.setInputType(InputType.TYPE_NULL);
        et_paya.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_paya.hasFocus()){
                    addMoney();
                    et_pay_give.setText("");
                }
            }
        });

        et_pay_give.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_pay_give.hasFocus()){
                    addMoney();
                }
               // addMoney();
            }
        });

        tv_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tv_pay.hasFocus()){
                    hi = Float.parseFloat(cardInfo.getN_Recharge_Integral_Value())/100;
                    if (globalIntegral.equals("0") || globalIntegral.equals("")) {
                        gi = 0f;
                    } else {
                        gi = Float.parseFloat(globalIntegral);
                    }
                    if (rechargeIntegral.equals("0") || rechargeIntegral.equals("")) {
                        ri = 0f;
                    } else {
                        ri = Float.parseFloat(rechargeIntegral);
                    }
                    String m_sj = tv_pay.getText().toString();
                    if(m_sj.equals("")){
                        m_sj = "0";
                    }
                    float m_s = Float.parseFloat(m_sj);
                    float m_jf  = gi*ri*m_s*hi;      //m_jfs.equals("") ? 0 : Float.parseFloat(m_jfs);
                    et_integral.setText(Utils.getNumStr(m_jf));
                }
            }
        });
    }

    private void addMoney() {

        hi = Float.parseFloat(cardInfo.getN_Recharge_Integral_Value())/100;

        if (globalIntegral.equals("0") || globalIntegral.equals("")) {
             gi = 0f;
        } else {
             gi = Float.parseFloat(globalIntegral);
        }

        if (rechargeIntegral.equals("0") || rechargeIntegral.equals("")) {
            ri = 0f;
        } else {
            ri = Float.parseFloat(rechargeIntegral);
        }
        String m_is = et_paya.getText().toString();
        String m_ps = et_pay_give.getText().toString();
        //  et_integral
        float m_i = m_is.equals("") ? 0 : Float.parseFloat(m_is);
        float m_p = m_ps.equals("") ? 0 : Float.parseFloat(m_ps);
        float money = m_i + m_p;
        tv_pay.setText(Utils.getNumStr(m_i));
        String m_sj = tv_pay.getText().toString();
       // if(m_sj.equals("")){m_sj = "0";}
        float m_s = m_is.equals("") ? 0 : Float.parseFloat(m_sj);
        float m_jf  = gi*ri*m_s*hi;      //m_jfs.equals("") ? 0 : Float.parseFloat(m_jfs);
        tv_money.setText(Utils.getNumStr(money));//合计金额
        et_integral.setText(Utils.getNumStr(m_jf));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay_xj://现金支付
            case R.id.tv_pay_xj1:
                if (isXJ) {
                    if (isclick_pay) {
                        isclick_pay = false;
                        n = 0;
                        sendData("");
                    }
                } else {
                    Utils.showToast(RechargeActivity.this, "没有开通此功能");
                }

                break;
            case R.id.tv_pay_sm://扫码支付
                if (isclick_pay) {
                    isclick_pay = false;

                    n = 1;
                    if (robotType == 1) {
                        sendData("");
                    } else {
                        payMoney(1, tv_pay.getText().toString(), orderNumber, "会员充值");
                    }
                }
                break;
            case R.id.tv_pay_wx://微信支付
                if (isclick_pay) {
                    isclick_pay = false;
                    n = 1;
                    payMoney(1, tv_pay.getText().toString(), orderNumber, "会员充值");
                }
                break;
            case R.id.tv_pay_yl://银联支付
                if (isclick_pay) {
                    isclick_pay = false;
                    n = 3;
                    if (robotType == 1) {
                        sendData("");
                    } else {
                        payMoney(3, tv_pay.getText().toString(), orderNumber, "会员充值");
                    }
                }
                break;

            case R.id.tv_pay_zfb://支付宝支付
                if (isclick_pay) {
                    isclick_pay = false;
                    n = 2;
                    payMoney(2, tv_pay.getText().toString(), orderNumber, "会员充值");
                }
                break;
            case R.id.btn_rech:
                et_paya.clearFocus();
                //et_paya.setFocusable(false);
                new PopupWindows(RechargeActivity.this, findViewById(R.id.activity_recharge));
                break;
            case R.id.btn_modify_jf:
                key = "jf";
                Arrived();
                break;
            case R.id.btn_modify_je:
                key = "je";
                Arrived();
                break;
            case R.id.btn_modify_ss:
                key = "ss";
                Arrived();
                break;
            case R.id.tv_clean:
                et_pay_give.setText("0");
                break;
        }
    }

    private void sendData(String orderidScan) {
        if (TextUtils.isEmpty(orderidScan))
            orderidScan = "";
        String m_is = et_paya.getText().toString();
        String m_sj = tv_pay.getText().toString();

        if(et_pay_give.getText().toString().equals("")){
            paysend = "0";
        }else {
            paysend = et_pay_give.getText().toString();
        }

        if (TextUtils.isEmpty(m_is)) {
            isclick_pay = true;
            Utils.showToast(RechargeActivity.this, "请填写充值金额");
        } else {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderTime = simpleFormat.format(date);
            isclick_pay = false;
            Map<String, String> map = new HashMap<>();
            map.put("dbName", SharedUtil.getSharedData(RechargeActivity.this, "dbname"));
            map.put("cardNO", tv_cardNum.getText().toString());
            map.put("userID", SharedUtil.getSharedData(RechargeActivity.this, "userInfoId"));
            map.put("payActual", m_sj);
            map.put("payShould", tv_money.getText().toString());
            map.put("EquipmentNum", terminalSn);
            map.put("orderNo", orderNumber);
            map.put("CardCode", uid);
            map.put("c_Billfrom", robotType + "");
            map.put("Supplement", "0");
            map.put("result_name", result);
            map.put("paySend",paysend);
            map.put("n_GetIntegral", et_integral.getText().toString());
            switch (n) {
                case 0:
                    map.put("payCash", m_sj);
                    break;
                case 1:
                    map.put("refernumber", orderidScan);
                    map.put("payWeChat", m_sj);
                    break;
                case 2:
                    map.put("refernumber", orderidScan);
                    map.put("payAli", m_sj);
                    break;
                case 3:
                    map.put("refernumber", orderidScan);
                    map.put("payBank", m_sj);
                    break;
            }
            postToHttp(NetworkUrl.RECHARGE, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    showHttpData(jsonText);
                }

                @Override
                public void OnFail(String message) {
                    if (robotType != 1 || (n != 1 && n != 3)) {
                        showAlert();
                    } else {
                        isclick_pay = true;
                    }
                }
            });
        }
    }

    private void showHttpData(String jsontext) {
        Logger.e(jsontext);
        try {
            JSONObject object = new JSONObject(jsontext);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                if ((robotType == 1 && n == 1) || (robotType == 1 && n == 3)) {
                    payMoney(n, tv_pay.getText().toString(), orderNumber, "会员充值");
                } else {
                    reSet();
                }

            } else if (tag.equals("ERR")) {
                isclick_pay = true;
                String msg = object.getString("result_errmsg");
                Utils.showToast(RechargeActivity.this, msg);
            } else {
                Utils.showToast(RechargeActivity.this, "支付失败,请稍后重试");
                if (robotType != 1 || (n != 1 && n != 3)) {
                    showAlert();
                } else {
                    isclick_pay = true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reSet() {
        flag = true;
        float money = Float.parseFloat(tv_money.getText().toString());
        float integra = Float.parseFloat(et_integral.getText().toString());
        newMoney = Utils.getNumStr(money + oldMoney);
        newintegra = Utils.getNumStr(integra + oldintegra);
        tv_str0.setText("当前储值 :  " + newMoney);

        str1 = tv_money.getText().toString();
        str2 = tv_pay.getText().toString();
        tv_print.setVisibility(View.VISIBLE);
        tv_print.setEnabled(true);
        printInfo(true);
        switch (robotType) {
            case 3:
            case 4:
                et_paya.setText("");
                et_pay_give.setText("");
                tv_pay.setText("");
                tv_money.setText("");
                finish();
                break;
        }
    }

    private void setDBData() {
        float money = Float.parseFloat(tv_money.getText().toString());
        float integra = Float.parseFloat(et_integral.getText().toString());
        if(et_pay_give.getText().toString().equals("")){
            paysend = "0";
        }else {
            paysend = et_pay_give.getText().toString();
        }
        newMoney = Utils.getNumStr(money + oldMoney);
        newintegra = Utils.getNumStr(integra + oldintegra);
        str1 = tv_money.getText().toString();
        str2 = tv_pay.getText().toString();
        Connector.getDatabase();
        RechargeAgain upLoading = new RechargeAgain();
        upLoading.setUrl(NetworkUrl.RECHARGE);
        upLoading.setUid(uid);
        upLoading.setResult_name(result);
        upLoading.setCardNO(tv_cardNum.getText().toString());
        upLoading.setDbName(SharedUtil.getSharedData(RechargeActivity.this, "dbname"));
        upLoading.setUserID(SharedUtil.getSharedData(RechargeActivity.this, "userInfoId"));
        upLoading.setPayActual(tv_pay.getText().toString());
        upLoading.setPayShould(tv_money.getText().toString());

        upLoading.setEquipmentNum(terminalSn);
        upLoading.setN(n);
        upLoading.setUrl(NetworkUrl.RECHARGE);
        upLoading.setVipName(tv_name.getText().toString());
        upLoading.setCardType(tv_type.getText().toString());
        upLoading.setGread(oldintegra + "");
        upLoading.setOldMoney(oldMoney + "");
        upLoading.setOrderNo(orderNumber);
        upLoading.setPaySend(paysend);
        upLoading.setGetIntegral(et_integral.getText().toString());
        upLoading.setShopName(SharedUtil.getSharedData(RechargeActivity.this, "shopname"));
        upLoading.setOrderTime(orderTime);
        upLoading.setUserName(SharedUtil.getSharedData(RechargeActivity.this, "username"));
        upLoading.setRefernumber(order_again);
        if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber)) {
            Logger.d(upLoading.save() + "");
        }
    }

    private synchronized void printInfo(boolean flag) {
        printCount = 0;
        List<String> list = new ArrayList<>();
        list.add("充值店面: " + SharedUtil.getSharedData(RechargeActivity.this, "shopname"));
        list.add("操作员 :" + SharedUtil.getSharedData(RechargeActivity.this, "username"));
        list.add("手持序列号:" + terminalSn);
        list.add((flag ? "时间:" : "订单生成时间") + orderTime);
        list.add("单据号:" + orderNumber);
        list.add("卡号 :" + cardNum);
        list.add("姓名:" + cardName);
        list.add("卡类型 :" + cardType);
        list.add("原金额:" + oldMoney);
        switch (n) {
            case 0:
                list.add("充值方式:现金支付");
                break;
            case 1:
                list.add("充值方式:微信支付");
                break;
            case 2:
                list.add("充值方式:支付宝支付");
                break;
            case 3:
                list.add("充值方式:银联支付");
                break;

        }
        list.add("充值金额 :" + str1);
        list.add("实收金额 :" + str2);
        if (flag) {
            list.add("充后余额 :" + newMoney);
         //   list.add("原有积分 :"+ oldintegra);
            list.add("可用积分 :" + newintegra);
        }
        if (flag) {
            printPage("充值小票", list, null, false);
        } else {
            printPage("充值延时扣款小票", list, null, false);
            printPage("充值延时扣款小票(商户存根)", list, null, false);
        }
    }

    @Override
    public void OnCreateOrderNumber(boolean isFail) {
        if (isFail) {
            Map<String, String> map = new HashMap<>();
            map.put("orderNo", orderNumber);
            map.put("dbName", getSharedData(RechargeActivity.this, "dbname"));
            postToHttp(NetworkUrl.DELPAYQRCODE, map, null);
        }
        getOrderNum("CZ");
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public class PopupWindows extends PopupWindow implements View.OnClickListener, PopupWindow.OnDismissListener {

//        private final ImageView iv1;
//        private final ImageView iv2;
//        private final ImageView iv3;
        private List<ImageView> list;
        private int n1 = 0, n2 = 0;
        private float n3;

        public PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.my_popuwindow, null);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            setWidth(w * 2 / 3);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setContentView(view);
            setOutsideTouchable(false);
            setFocusable(true);
            setTouchable(true);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            update();
            ImageView back = (ImageView) view.findViewById(R.id.pop_iv_click);
            lv_recharge = (ListView) view.findViewById(R.id.lv_recharge);
            lv_recharge.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            lv_recharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    et_pay_give.setText(list_rechargefast.get(position).getC_Remark()+"");
                    et_paya.setText(list_rechargefast.get(position).getC_Value()+"");
//                    hi = Float.parseFloat(cardInfo.getN_Recharge_Integral_Value())/100;
//                    if (globalIntegral.equals("0") || globalIntegral.equals("")) {
//                        gi = 0f;
//                    } else {
//                        gi = Float.parseFloat(globalIntegral);
//                    }
//                    if (rechargeIntegral.equals("0") || rechargeIntegral.equals("")) {
//                        ri = 0f;
//                    } else {
//                        ri = Float.parseFloat(rechargeIntegral);
//                    }
//                    String s = list_rechargefast.get(position).getC_Value()+"";
//                    float m_s = s.equals("") ? 0 : Float.parseFloat(s);
//                    float m_jf  = gi*ri*hi*m_s;
//                    et_integral.setText(m_jf+"");
                    addMoney();

                   // adapter.getItem(position).
                    dismiss();
                }
            });
//            iv1 = (ImageView) view.findViewById(R.id.pop_iv1);
//            iv2 = (ImageView) view.findViewById(R.id.pop_iv2);
//            iv3 = (ImageView) view.findViewById(R.id.pop_iv3);
//            list = new ArrayList<>();
//            list.add(iv1);
//            list.add(iv2);
//            list.add(iv3);

//            LinearLayout ll1 = (LinearLayout) view.findViewById(R.id.pop_click1);
//            LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.pop_click2);
//            LinearLayout ll3 = (LinearLayout) view.findViewById(R.id.pop_click3);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //selectColor(-1);
                    dismiss();
                }
            });



//            ll1.setOnClickListener(this);
//            ll2.setOnClickListener(this);
//            ll3.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            addMoney();
            dismiss();
//            switch (view.getId()) {
//                case R.id.pop_click1:
//                    //selectColor(0);
//                    n1 = 100;
//                    // TODO: 2017/5/9
//                    n2 = 50;
//                    if (cardInfo != null) {
//                        try {
//                          //  n3 = Float.parseFloat(cardInfo.getN_IntegralValue());
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//                case R.id.pop_click2:
//                    //selectColor(1);
//                    n1 = 200;
//                    n2 = 120;
//                    if (cardInfo != null) {
//                        try {
//                           // n3 = Float.parseFloat(cardInfo.getN_IntegralValue()) * 2;
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//                case R.id.pop_click3:
//                   // selectColor(2);
//                    n1 = 300;
//                    n2 = 200;
//                    if (cardInfo != null) {
//                        try {
//                           // n3 = Float.parseFloat(cardInfo.getN_IntegralValue()) * 3;
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//
//            }
////            et_paya.setText(n1 + "");
////            et_pay_give.setText(n2 + "");
////            et_integral.setText(n3 + "");
//


        }

//        private void selectColor(int n) {
//            for (int i = 0; i < list.size(); i++) {
//                list.get(i).setImageResource(n == i ? R.mipmap.pop_up_04 : R.mipmap.pop_up_02);
//            }
//        }

        @Override
        public void onDismiss() {
            isclick_pay = true;
        }
    }

    private boolean alert_flag = true;

    private void showAlert() {
        flag = false;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(RechargeActivity.this);
        dialog.setTitle(R.string.dialog_warnning_name);
        dialog.setMessage(R.string.dialog_warnning_message);
        dialog.setPositiveButton(R.string.dialog_warnning_yes, null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (alert_flag) {
                    if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber)) {
                        setDBData();
                        printInfo(false);
                        switch (robotType) {
                            case 3:
                                startActivity(new Intent(RechargeActivity.this, MainActivity.class));
                                break;
                        }
                    } else {
                        startActivity(new Intent(RechargeActivity.this, MainActivity.class));
                    }
                }

                alert_flag = true;
            }
        });
        dialog.setNegativeButton(R.string.dialog_warnning_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert_flag = false;
                if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
                    LKLPay(order_again);
                } else {
                    sendData(order_again);
                }

            }
        });
        dialog.create().show();
    }


    @Override
    public void OnPaySucess(String orderNum, int payMode) {
        n = payMode;
        order_again = orderNum;
        if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
            LKLPay(orderNum);
        } else {
            sendData(orderNum);
            loadingDialog.show();
        }

    }

    private void LKLPay(String order) {
        Map<String, String> map = new HashMap<>();
        map.put("refernumber", order);
        map.put("orderNo", orderNumber);
        map.put("dbName", getSharedData(RechargeActivity.this, "dbname"));
        switch (n) {
            case 1:
                map.put("payWeChat", et_paya.getText().toString());
                map.put("n_GetIntegral", et_integral.getText().toString());
                break;
            case 2:
                map.put("payAli", et_paya.getText().toString());
                map.put("n_GetIntegral", et_integral.getText().toString());
                break;
            case 3:
                map.put("payBank", et_paya.getText().toString());
                map.put("n_GetIntegral", et_integral.getText().toString());
                break;
        }
        postToHttp(NetworkUrl.PAYQRCODE, map, new IHttpCallBack() {
                    @Override
                    public void OnSucess(String jsonText) {
                        try {
                            JSONObject object = new JSONObject(jsonText);
                            String tag = object.getString("result_stadus");
                            if (tag.equals("SUCCESS")) {
                                reSet();
                            } else {
                                showAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void OnFail(String message) {
                        showAlert();
                    }
                }

        );
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_recharge);
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
        et_paya.setText("");
        et_pay_give.setText("");
        tv_pay.setText("");
        tv_money.setText("");
        if (flag) {
            finish();
        } else {
            if (printCount >= 2) {
                finish();
            }
        }

    }


    private void Arrived() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(RechargeActivity.this).inflate(R.layout.pop_temporary, null);
        final EditText et_num = (EditText) contentView.findViewById(R.id.et_num);
        final EditText et_pwd = (EditText) contentView.findViewById(R.id.et_pwd);
        ImageView back = (ImageView) contentView.findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        contentView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = et_num.getText().toString();
                String str2 = et_pwd.getText().toString();
                if (TextUtils.isEmpty(str1)) {
                    Utils.showToast(RechargeActivity.this, "请输入工号");
                } //else if (TextUtils.isEmpty(str2)) {
//                    Utils.showToast(RechargeActivity.this, "请输入密码");
                else {
                    getTemporary(str1, str2);
                    window.dismiss();
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.4));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_recharge), Gravity.CENTER, 0, 0);
    }

    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(RechargeActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);

                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    result = j.getString("result_name");

                    int ssje = s.indexOf("POS:充值实收金额修改");
                    int zsjf = s.indexOf("POS:充值赠送积分修改");
                    int zsje = s.indexOf("POS:充值赠送金额修改");
                    Logger.e("" + ssje + "---" + zsjf + "---" + zsje);
                    if (key.equals("jf")) {
                        if (zsjf > 0) {
                            Utils.showToast(RechargeActivity.this, "授权成功");
                            et_integral.setClickable(true);
                            et_integral.setFocusableInTouchMode(true);
                            et_integral.setFocusable(true);
                            et_integral.requestFocus();
                            et_integral.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(RechargeActivity.this, "您没有权限");
                        }
                    }

                    if (key.equals("je")) {
                        if (zsje > 0) {
                            Utils.showToast(RechargeActivity.this, "授权成功");
                            et_pay_give.setClickable(true);
                            et_pay_give.setFocusableInTouchMode(true);
                            et_pay_give.setFocusable(true);
                            et_pay_give.requestFocus();
                            et_pay_give.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        } else {
                            Utils.showToast(RechargeActivity.this, "您没有权限");
                        }
                    }

                    if (key.equals("ss")) {
                        if (ssje > 0) {
                            Utils.showToast(RechargeActivity.this, "授权成功");
                            //tv_pay.setClickable(true);
                            tv_pay.setFocusableInTouchMode(true);
                            tv_pay.setFocusable(true);
                            tv_pay.requestFocus();
                            tv_pay.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(RechargeActivity.this, "您没有权限");
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(RechargeActivity.this, "服务器异常");
                }

            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(RechargeActivity.this, "临时授权失败");
            }

        });
    }


}
