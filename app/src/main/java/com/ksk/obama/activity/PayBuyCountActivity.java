package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.R;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.PrintPage;
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
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ksk.obama.R.id.btn_dxjf;
import static com.ksk.obama.R.id.btn_hdjf;
import static com.ksk.obama.R.id.btn_sjsk;
import static com.ksk.obama.R.id.et_dx_jf;
import static com.ksk.obama.R.id.ll_jfdx;
import static com.ksk.obama.R.id.tv_money_dx;
import static com.ksk.obama.utils.SharedUtil.getSharedData;
import static java.lang.Float.parseFloat;

public class PayBuyCountActivity extends BasePrintActivity implements View.OnClickListener, IPayCallBack,
        IPrintSuccessCallback, IPrintErrorCallback, ICreateOrderNumber {


    private boolean isPay = false;
    private String memid;
    private String cardNum;
    private String name;
    private String num;
    private String should;
    private String payau;
    private String del;
    private int n = -1;
    private String ids;
    private String times;
    private String moneycount;
    private String order_again = "";
    private LinearLayout ll_pay;
    private String net_url;
    private TextView tv_print;
    private String validTimes;
    private String password;
    private String pay;
    private String gread;
    private float oldMoney;
    private String oldIntegral;
    private boolean flag = false;
    private String orderTime = "";
    private String uid;

    private float inteCount = 0f;
    private boolean isCheck = false;
    private boolean isTemporary = false;
    private String delMoney = "";
    private String key = "";
    private String result = "";
    private String temName = "";
    private String temporaryNum = "";
    private String dx_integral = "";
    private String dx_money = "";
    private float dx_jf;
    private float dx_mr;
    private float dx_max;

    private Unbinder unbinder;

    @BindView(R.id.pay_shop_shuold)
    TextView payShuold;
    @BindView(R.id.pay_shop_del)
    TextView payDel;
    @BindView(R.id.tv_paymoney)
    TextView tv_paymoney;
    @BindView(R.id.pay_shop_payau)
    EditText et_money;
    @BindView(btn_sjsk)
    Button btnSjsk;
    @BindView(R.id.pay_shop_gread)
    EditText et_gread;
    @BindView(btn_hdjf)
    Button btnHdjf;
    @BindView(et_dx_jf)
    EditText etDxJf;
    @BindView(tv_money_dx)
    TextView tvMoneyDx;
    @BindView(btn_dxjf)
    Button btnDxjf;
    @BindView(ll_jfdx)
    LinearLayout llJfdx;
    @BindView(R.id.cb_ischeck_cpxf)
    AppCompatCheckBox db_isCheck;

    @BindView(R.id.tv_pay_xj)
    TextView pay_xj;
    @BindView(R.id.tv_pay_sm)
    TextView pay_sm;
    @BindView(R.id.tv_pay_hy)
    TextView pay_hy;
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
        setContentView(R.layout.activity_pay_buy_shop);
        this.setOnPayCallBack(this);
        this.setOnPrintSuccess(this);
        this.setOnPrintError(this);
        this.setOnCrateOrderNumber(this);
        unbinder = ButterKnife.bind(this);
        initTitale();
        initView();
        initData();
        getOrderNum("BT");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setText("重打印");
        title_name.setText("购物车");
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
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

        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo(true);
            }
        });

    }

    private void initView() {
        dx_jf = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_jf"));//几多积分抵现一元
        dx_mr = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
        dx_max = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_max"));//最大抵现几多
        if (Math.abs(dx_jf - 0.0) == 0) {
            db_isCheck.setVisibility(View.GONE);
        }

        if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "gcsj")) {
            btnSjsk.setVisibility(View.GONE);
            et_money.setFocusableInTouchMode(true);
            et_money.setFocusable(true);
            et_money.requestFocus();
            et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "gcjf")) {
            btnHdjf.setVisibility(View.GONE);
            et_gread.setFocusableInTouchMode(true);
            et_gread.setFocusable(true);
            et_gread.requestFocus();
            et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "gcdx")) {
            btnDxjf.setVisibility(View.GONE);
            etDxJf.setFocusableInTouchMode(true);
            etDxJf.setFocusable(true);
            etDxJf.requestFocus();
            etDxJf.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }


        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PX")) {
            pay_xj.setVisibility(View.GONE);
        }

        Log.d("10086", SharedUtil.getSharedBData(PayBuyCountActivity.this, "PW") + "------" + SharedUtil.getSharedBData(PayBuyCountActivity.this, "PA"));
        switch (robotType) {
            case 1:
                ll_w_a.setVisibility(View.GONE);
                if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "PW") && SharedUtil.getSharedBData(PayBuyCountActivity.this, "PA")) {
                    pay_sm.setVisibility(View.VISIBLE);
                } else {
                    pay_sm.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PY")) {
                    pay_yl.setVisibility(View.GONE);
                }
                break;
            case 3:
            case 4:
                ll_lkl.setVisibility(View.GONE);
                if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PW")) {
                    pay_wx.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PA")) {
                    pay_zfb.setVisibility(View.GONE);
                }
                break;
            case 8:
                if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "pay_ment")) {//如果结果为true证明使用官方支付接口
                    ll_w_a.setVisibility(View.GONE);
                    if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "PW") && SharedUtil.getSharedBData(PayBuyCountActivity.this, "PA")) {
                        pay_sm.setVisibility(View.VISIBLE);
                    } else {
                        pay_sm.setVisibility(View.GONE);
                    }
                    if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PY")) {
                        pay_yl.setVisibility(View.GONE);
                    }
                } else {
                    ll_lkl.setVisibility(View.GONE);
                    if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PW")) {
                        pay_wx.setVisibility(View.GONE);
                    }
                    if (!SharedUtil.getSharedBData(PayBuyCountActivity.this, "PA")) {
                        pay_zfb.setVisibility(View.GONE);
                    }
                }
                break;
        }

        InputFilter[] filters = {new MyTextFilter()};
        et_money.setFilters(filters);
        et_gread.setFilters(filters);
        etDxJf.setFilters(filters);

        db_isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
                if (isChecked) {
                    float del;
                    delMoney = et_money.getText().toString();
                    if (TextUtils.isEmpty(delMoney)) {
                        del = 0;
                    } else {
                        del = parseFloat(delMoney);
                    }

                    float jf = inteCount;
                    if (dx_max >= del * dx_mr) {//否超过默认金额
                        if (del * dx_mr * dx_jf <= jf) {//卡积分够
                            etDxJf.setText(Utils.getNumStr(del * dx_mr * dx_jf));
                            tvMoneyDx.setText(Utils.getNumStr(del * dx_mr));
                            tv_paymoney.setText(Utils.getNumStr(del - del * dx_mr));

                        } else {
                            etDxJf.setText(inteCount + "");
                            tvMoneyDx.setText(Utils.getNumStr(jf / dx_jf));
                            tv_paymoney.setText(Utils.getNumStr(del - jf / dx_jf));
                        }
                    } else {
                        if (dx_max * dx_mr * dx_jf <= jf) {//卡积分够
                            etDxJf.setText(Utils.getNumStr(dx_max * dx_jf));
                            tvMoneyDx.setText(Utils.getNumStr(dx_max));
                            tv_paymoney.setText(Utils.getNumStr(del - dx_max));
                        } else {
                            etDxJf.setText(inteCount + "");
                            tvMoneyDx.setText(Utils.getNumStr(jf / dx_jf));
                            tv_paymoney.setText(Utils.getNumStr(del - jf / dx_jf));
                        }
                    }
                    btnDxjf.setEnabled(true);
                    et_money.setInputType(InputType.TYPE_NULL);
                    et_gread.setInputType(InputType.TYPE_NULL);
                    llJfdx.setVisibility(View.VISIBLE);

                } else {
                    dx_jf = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_jf"));//几多积分抵现一元
                    dx_mr = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
                    dx_max = parseFloat(SharedUtil.getSharedData(PayBuyCountActivity.this, "dx_max"));//最大抵现几多
                    temporaryNum = "";
                    temName = "";
                    etDxJf.setText("0");
                    tvMoneyDx.setText("0");
                    tv_paymoney.setText("0");
                    btnDxjf.setEnabled(false);
                    isTemporary = false;
                    etDxJf.setInputType(InputType.TYPE_NULL);

                    if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "cxsj")) {
                        et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    } else {
                        et_money.setInputType(InputType.TYPE_NULL);

                    }
                    if (SharedUtil.getSharedBData(PayBuyCountActivity.this, "cxjf")) {
                        et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    } else {
                        et_gread.setInputType(InputType.TYPE_NULL);
                    }

                    llJfdx.setVisibility(View.GONE);
                }

            }
        });
        etDxJf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String e = etDxJf.getText().toString();
                if (e.equals(0) || e.equals("0") || e.equals("0.")) {
                    etDxJf.setText("");
                }
                if (isTemporary || SharedUtil.getSharedBData(PayBuyCountActivity.this, "cxdx")) {
                    String str = etDxJf.getText().toString();
                    String str2 = et_money.getText().toString();
                    etDxJf.setSelection(str.length());
                    float delm;
                    float del_jf;
                    float have_jf = inteCount;
                    if (TextUtils.isEmpty(str2)) {
                        delm = 0;
                    } else {
                        delm = parseFloat(str2);
                    }
                    if (TextUtils.isEmpty(str)) {
                        del_jf = 0;
                    } else {
                        del_jf = parseFloat(str);
                    }
                    //如果默认积分 比 会员积分多 （会员积分不足） 则使用会员积分
                    int i = 1;
                    if (del_jf > have_jf) {
                        del_jf = have_jf;
                        i++;
                    }
                    if (dx_max != 0) {
                        if (del_jf / dx_jf > dx_max) {
                            del_jf = dx_max * dx_jf;
                            i++;
                        }
                    }
                    if (del_jf / dx_jf > delm) {
                        del_jf = delm * dx_jf;
                        i++;
                    }
                    etDxJf.removeTextChangedListener(this);
                    if (i == 1) {
                        etDxJf.setText(str);
                    } else {
                        etDxJf.setText(del_jf + "");
                    }
                    etDxJf.addTextChangedListener(this);
                    etDxJf.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    etDxJf.setSelection(etDxJf.getText().length());
                    tvMoneyDx.setText(Utils.getNumStr(del_jf / dx_jf));
                    tv_paymoney.setText(Utils.getNumStr(delm - del_jf / dx_jf));

                }
            }

        });


        pay_xj.setOnClickListener(this);
        pay_hy.setOnClickListener(this);
        pay_sm.setOnClickListener(this);
        pay_yl.setOnClickListener(this);
        pay_wx.setOnClickListener(this);
        pay_zfb.setOnClickListener(this);


//        et_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    et_money.setText("");
//                }
//            }
//        });
//
//        et_gread.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    et_gread.setText("");
//                }
//            }
//        });

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("uid");
            memid = intent.getStringExtra("memid");
            cardNum = intent.getStringExtra("cardNum");
            name = intent.getStringExtra("name");
            num = intent.getStringExtra("num");
            should = intent.getStringExtra("should");
            pay = intent.getStringExtra("payau");
            gread = intent.getStringExtra("inte");
            del = intent.getStringExtra("del");
            oldIntegral = intent.getStringExtra("oldi");
            net_url = intent.getStringExtra("url");
            password = intent.getStringExtra("pwd");
            oldMoney = Float.parseFloat(intent.getStringExtra("old"));
            payau = pay;
            et_money.setText(pay);
            et_gread.setText(gread);
            payShuold.setText("总价:￥" + should);
            payDel.setText("折扣优惠:￥" + del);
            inteCount = Float.parseFloat(oldIntegral);
        }
    }

    @Override
    public void onClick(View v) {
        payau = et_money.getText().toString();
        gread = et_gread.getText().toString();
        dx_integral = etDxJf.getText().toString();
        dx_money = tvMoneyDx.getText().toString();
        if (dx_integral.equals("")) {
            dx_integral = "0";
        }
        if (dx_money.equals("")) {
            dx_money = "0";
        }
        if (TextUtils.isEmpty(et_money.getText().toString())) {
            payau = "0";
        } else {
            payau = et_money.getText().toString();
        }
        if (db_isCheck.isChecked()) {
            payau = tv_paymoney.getText().toString();
        }

        del = Utils.getNumStr(Float.parseFloat(should) - Float.parseFloat(payau));
        if (isclick_pay) {
            isclick_pay = false;
            switch (v.getId()) {
                case R.id.tv_pay_xj:
                    if (isXJ) {
                        if (TextUtils.isEmpty(gread)) {
                            isclick_pay = true;
                            Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                        } else {
                            n = 0;
                            sendData("");
                        }
                    } else {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "没有开通此功能");
                    }

                    break;
                case R.id.tv_pay_sm:
                    if (TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                    } else {
                        n = 1;
                        sendData("");
                    }
                    break;
                case R.id.tv_pay_wx:
                    if (TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                    } else {
                        n = 1;
                        sendData("");//*
                        //payMoney(1, payau, orderNumber, "购买次数");
                    }
                    break;
                case R.id.tv_pay_zfb:
                    if (TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                    } else {
                        n = 2;
                        sendData("");//*
//                       payMoney(2, payau, orderNumber, "购买次数");
//                        if (TextUtils.isEmpty(password)) {
//                            payMoney(2, payau, orderNumber, "购买次数");
//                        } else {
//                            getPassword();
//                        }
                    }
                    break;
                case R.id.tv_pay_yl:
                    if (TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                    } else {
                        n = 3;
                        sendData("");
//                        payMoney(3, payau, orderNumber, "购买次数");
                    }
                    break;
                case R.id.tv_pay_hy:
                    if (TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyCountActivity.this, "请输入积分");
                    } else {
                        n = 4;
                        if (TextUtils.isEmpty(payau)) {
                            payau = "0";
                        }
                        if (oldMoney >= Float.parseFloat(payau)) {
                            if (TextUtils.isEmpty(password)) {
                                sendData("");
                            } else {
                                getPassword();
                            }
                        } else {
                            isclick_pay = true;
                            Utils.showToast(PayBuyCountActivity.this, "卡内余额不足，无法支付");
                        }
                    }
                    break;
            }
        }
    }


    private void getPassword() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(PayBuyCountActivity.this).inflate(R.layout.number_password, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick_pay = true;
                window.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = Utils.getMD5Code(editText.getText().toString());
                if (pwd.equals(password)) {
                    switch (n) {
                        case 4:
                            sendData("");
                            break;
                    }
                    window.dismiss();
                } else {
                    isclick_pay = true;
                    editText.setText("");
                    Utils.showToast(PayBuyCountActivity.this, "密码错误,请重输");
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
        window.showAtLocation(findViewById(R.id.activity_pay_buy_shop), Gravity.CENTER, 0, 0);

    }

    private void payHint(boolean flag) {
        LinearLayout ll_hint = (LinearLayout) findViewById(R.id.ll_pay_hint);
        ImageView iv_hint = (ImageView) findViewById(R.id.iv_pay_hint);
        TextView tv_hint = (TextView) findViewById(R.id.tv_pay_hint);
        ll_hint.setVisibility(View.VISIBLE);
        iv_hint.setSelected(flag);
        tv_hint.setText(payau + "元商品购买" + (flag ? "成功" : "失败"));
    }

    private void sendData(String order_again) {
        if (TextUtils.isEmpty(order_again))
            order_again = "";
        ids = "";
        times = "";
        moneycount = "";
        validTimes = "";
        for (int i = 0; i < list_buy.size(); i++) {
            if (i == list_buy.size() - 1) {
                ids += list_buy.get(i).getId();
                times += list_buy.get(i).getNum();
                moneycount += list_buy.get(i).getMoneyin();
                validTimes += list_buy.get(i).getValidTime();
            } else {
                ids += list_buy.get(i).getId() + ",";
                times += list_buy.get(i).getNum() + ",";
                moneycount += list_buy.get(i).getMoneyin() + ",";
                validTimes += list_buy.get(i).getValidTime() + ",";
            }
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderTime = simpleFormat.format(date);
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(PayBuyCountActivity.this, "dbname"));
        map.put("User_Id", SharedUtil.getSharedData(PayBuyCountActivity.this, "userInfoId"));
        map.put("goods_id", ids);
        map.put("Member_Id", memid);
        map.put("times", times);
        map.put("PayShould", should);
        map.put("PayActual", payau);
        map.put("get_integral", gread);
        map.put("PayDiscounted", pay);
        map.put("get_money", moneycount);
        map.put("EquipmentNum", terminalSn);
        map.put("orderNo", orderNumber);
        map.put("validTimes", validTimes);
        map.put("CardCode", uid);
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "0");
        map.put("payDecIntegral", dx_integral);//抵现的积分
        map.put("payIntegral", dx_money);//抵现的金额
        map.put("temporary_num", temporaryNum);
        map.put("result_name", temName);
        switch (n) {
            case 0:
                map.put("payCash", payau);
                break;

            case 1:
                map.put("payWeChat", payau);
                map.put("refernumber", order_again);
                break;

            case 2:
                map.put("payAli", payau);
                map.put("refernumber", order_again);
                break;

            case 3:
                map.put("payBank", payau);
                map.put("refernumber", order_again);
                break;

            case 4:
                map.put("payCard", payau);
                break;
        }
        postToHttp(net_url, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                isPay = true;
                showHttpData(jsonText);
//                if ((robotType == 4 && n == 2) || (robotType == 4 && n == 3)){
//                    LKLPay(orderNum);
//                }
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

    private void showHttpData(String text) {
        try {
            JSONObject object = new JSONObject(text);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                if ((robotType == 1 && n == 1) || (robotType == 1 && n == 3)) {
                    payMoney(n, payau + "", orderNumber, "商品消费");
                } else if ((robotType != 1 && n == 1) || (robotType != 1 && n == 2)) {
                    payMoney(n, payau + "", orderNumber, "购买次数");
                } else {
                    reSet();
                }
                switch (robotType) {
                    case 3:
                        //case 4:
                        changeActivity();
                        break;
                }
            } else if (tag.equals("ERR")) {
                isclick_pay = true;
                flag = true;
                String msg = object.getString("result_errmsg");
                Utils.showToast(PayBuyCountActivity.this, msg);
            } else {
                if (robotType != 1 || (n != 1 && n != 3)) {
                    String msg = object.getString("result_errmsg");
                    Utils.showToast(PayBuyCountActivity.this, msg);
                    tv_print.setVisibility(View.INVISIBLE);
                    tv_print.setEnabled(false);
                    payHint(false);
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
        if (robotType == 1) {
            ll_pay.setVisibility(View.GONE);
        }
        tv_print.setVisibility(View.VISIBLE);
        tv_print.setEnabled(true);
        payHint(true);
        printInfo(true);
    }

    private synchronized void printInfo(boolean flag) {
        printCount = 0;
        List<PrintPage> list_son = new ArrayList<>();
        for (int i = 0; i < list_buy.size(); i++) {
            PrintPage page = new PrintPage();
            page.setMoney(Utils.getNumStr(list_buy.get(i).getMoney()));
            page.setNum(list_buy.get(i).getNum() + "");
            page.setPrice(list_buy.get(i).getPrice() + "");
            page.setName(list_buy.get(i).getName());
            list_son.add(page);
        }
        List<String> listp = new ArrayList<>();
        listp.add((flag ? "时间:" : "订单生成时间") + orderTime);
        listp.add("订单号:" + orderNumber);
        listp.add("会员卡号:" + cardNum);
        listp.add("会员姓名:" + name);
        listp.add("son");
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
            case 4:
                listp.add("支付方式:会员卡");
                break;
        }
        listp.add("总金额:" + should);
        listp.add("实收金额:" + payau);
        listp.add("优惠金额:" + del);
        listp.add("获得积分:" + gread);
        if (flag) {
            if (n == 4) {
                listp.add("剩余储值:" + Utils.getNumStr(oldMoney - Float.parseFloat(payau)));
            } else {
                listp.add("剩余储值:" + Utils.getNumStr(oldMoney));
            }
            listp.add("剩余积分:" + Utils.getNumStr(Float.parseFloat(gread) + Float.parseFloat(oldIntegral)));
        }
        if (flag) {
            printPage("购次小票", listp, list_son, true);
        } else {
            printPage("购次延时扣款小票", listp, list_son, true);
            printPage("购次延时扣款小票(商户存根)", listp, list_son, true);
        }

    }

    private void changeActivity() {
        list_buy.clear();
        Intent intent = new Intent(PayBuyCountActivity.this, ReadCardInfoActivity.class);
        intent.putExtra("type", 12);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isPay) {
            changeActivity();
        }
        super.onBackPressed();
    }

    @Override
    public void OnPaySucess(String orderNum, int payMode) {
        n = payMode;
        order_again = orderNum;
        if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
            LKLPay(orderNum);
        } else {
            // sendData(orderNum);
            LKLPay(orderNum);
            loadingDialog.show();
        }
    }

    private void
    LKLPay(String order) {
        Map<String, String> map = new HashMap<>();
        map.put("refernumber", order);
        map.put("orderNo", orderNumber);
        map.put("dbName", getSharedData(PayBuyCountActivity.this, "dbname"));
        switch (n) {
            case 1:
                map.put("payWeChat", payau);
                break;
            case 2:
                map.put("payAli", payau);
                break;
            case 3:
                map.put("payBank", payau);
                break;
        }
        postToHttp(NetworkUrl.PAYQRCODE, map, new IHttpCallBack() {
                    @Override
                    public void OnSucess(String jsonText) {
                        Logger.e(jsonText);
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

    private void setDBData() {
        Connector.getDatabase();
        BuyCountDb buyCountDb = new BuyCountDb();
        buyCountDb.setUrl(NetworkUrl.SENDBUYCOUNT);
        buyCountDb.setCardNo(cardNum);
        buyCountDb.setUid(uid);
        buyCountDb.setDbname(SharedUtil.getSharedData(PayBuyCountActivity.this, "dbname"));
        buyCountDb.setGoods_id(ids);
        buyCountDb.setTimes(times);
        buyCountDb.setPayshould(should);
        buyCountDb.setPayDiscounted(pay);
        buyCountDb.setPayactual(payau);
        buyCountDb.setGet_money(moneycount);
        buyCountDb.setN(n);
        buyCountDb.setName(name);
        buyCountDb.setUserid(SharedUtil.getSharedData(PayBuyCountActivity.this, "userInfoId"));
        buyCountDb.setUserName(SharedUtil.getSharedData(PayBuyCountActivity.this, "username"));
        buyCountDb.setShopName(SharedUtil.getSharedData(PayBuyCountActivity.this, "shopname"));
        buyCountDb.setMemid(memid);
        buyCountDb.setOrderTime(orderTime);
        buyCountDb.setOrderNo(orderNumber);
        buyCountDb.setYouhui(del);
        buyCountDb.setEquipmentNum(terminalSn);
        buyCountDb.setDataList(list_buy);
        buyCountDb.setRefernumber(order_again);
        buyCountDb.setValidTimes(validTimes);
        buyCountDb.setGetIntegral(gread);
        buyCountDb.setDx_Money(dx_money);
        buyCountDb.setDx_Integral(dx_integral);
        buyCountDb.setTemporaryNum(temporaryNum);
        buyCountDb.setTemName(temName);
        if (n == 4) {
            buyCountDb.setHaveMoney(Utils.getNumStr(oldMoney - Float.parseFloat(payau)));
        } else {
            buyCountDb.setHaveMoney(Utils.getNumStr(oldMoney));
        }
        buyCountDb.setHavejf(Utils.getNumStr(Float.parseFloat(gread) + Float.parseFloat(oldIntegral)));
        if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber)) {
            Logger.d(buyCountDb.save() + "");
        }
    }

    private boolean alert_flag = true;

    private void showAlert() {
        flag = false;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(PayBuyCountActivity.this);
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
    public void OnPrintError() {
        printError(R.id.activity_pay_buy_shop);
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

    @Override
    public void OnCreateOrderNumber(boolean isFail) {
        if (isFail) {
            Map<String, String> map = new HashMap<>();
            map.put("orderNo", orderNumber);
            map.put("dbName", getSharedData(PayBuyCountActivity.this, "dbname"));
            postToHttp(NetworkUrl.DELPAYQRCODE, map, null);
        }
        getOrderNum("BT");
    }

    @OnClick({R.id.btn_sjsk, R.id.btn_hdjf, R.id.btn_dxjf})
    public void temporary(View v) {
        switch (v.getId()) {
            case R.id.btn_sjsk:
                if (!db_isCheck.isChecked()) {
                    key = "je";
                    Arrived();
                } else {
                    Utils.showToast(PayBuyCountActivity.this, "该状态下不能修改“实际收款”");
                }

                break;
            case R.id.btn_hdjf:
                if (!db_isCheck.isChecked()) {
                    key = "jf";
                    Arrived();
                } else {
                    Utils.showToast(PayBuyCountActivity.this, "该状态下不能修改“获得积分”");
                }
                break;
            case R.id.btn_dxjf:
                key = "dx";
                Arrived();
                break;
        }
    }

    private void Arrived() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(PayBuyCountActivity.this).inflate(R.layout.pop_temporary, null);
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
                    Utils.showToast(PayBuyCountActivity.this, "请输入工号");
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
        window.showAtLocation(findViewById(R.id.activity_pay_buy_shop), Gravity.CENTER, 0, 0);
    }


    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(PayBuyCountActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);

                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    result = j.getString("result_name");

                    int ssje = s.indexOf("POS:购次实收金额修改");
                    int zsjf = s.indexOf("POS:购次获得积分修改");
                    int jfdx = s.indexOf("POS:购次积分抵现修改");
                    Logger.e("" + ssje + "---" + zsjf + "---" + jfdx);
                    if (key.equals("je")) {
                        if (jfdx > 0) {
                            Utils.showToast(PayBuyCountActivity.this, "授权成功");
                            // isTemporary = true;
                            temporaryNum = num;
                            temName = j.getString("result_name") + " ";
                            et_money.setFocusableInTouchMode(true);
                            et_money.setFocusable(true);
                            et_money.requestFocus();
                            et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        } else {
                            Utils.showToast(PayBuyCountActivity.this, "您没有权限或密码错误");
                        }
                    }
                    if (key.equals("jf")) {
                        if (zsjf > 0) {
                            Utils.showToast(PayBuyCountActivity.this, "授权成功");

                            temporaryNum = num;
                            temName = j.getString("result_name") + "";
                            et_gread.setFocusableInTouchMode(true);
                            et_gread.setFocusable(true);
                            et_gread.requestFocus();
                            et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(PayBuyCountActivity.this, "您没有权限或密码错误");
                        }
                    }
                    if (key.equals("dx")) {
                        if (ssje > 0) {
                            Utils.showToast(PayBuyCountActivity.this, "授权成功");
                            isTemporary = true;
                            temporaryNum = num;
                            temName = j.getString("result_name") + "";
                            etDxJf.setFocusableInTouchMode(true);
                            etDxJf.setFocusable(true);
                            etDxJf.requestFocus();
                            etDxJf.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(PayBuyCountActivity.this, "您没有权限或密码错误");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(PayBuyCountActivity.this, "服务器异常");
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(PayBuyCountActivity.this, "临时授权失败");
            }

        });
    }
}
