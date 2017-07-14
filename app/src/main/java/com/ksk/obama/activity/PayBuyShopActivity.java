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

import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.R;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.MyTextFilter2;
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

import static com.ksk.obama.utils.SharedUtil.getSharedBData;
import static com.ksk.obama.utils.SharedUtil.getSharedData;
import static com.ksk.obama.utils.SharedUtil.getSharedInt;
import static java.lang.Float.parseFloat;

public class PayBuyShopActivity extends BasePrintActivity implements IPayCallBack,
        IPrintSuccessCallback, IPrintErrorCallback, ICreateOrderNumber, View.OnClickListener {

    @BindView(R.id.tv_pay_hy)
    TextView tvPayHy;
    @BindView(R.id.tv_pay_xj)
    TextView tvPayXj;
    @BindView(R.id.ll_top_xj)
    LinearLayout llTopXj;
    @BindView(R.id.tv_pay_wx)
    TextView tvPayWx;
    @BindView(R.id.tv_pay_zfb)
    TextView tvPayZfb;
    @BindView(R.id.ll_w_a)
    LinearLayout llWA;
    @BindView(R.id.tv_pay_dsf)
    TextView tvPayDsf;
    @BindView(R.id.ll_dsf)
    LinearLayout llDsf;
    private TextView tv_print;
    private boolean isPay = false;
    private String memid = "";
    private String cardNum = "散客";
    private String name = "散客";
    private String should = "0.00";
    private float payau = 0;
    private String del = "0.00";
    private float del_jf = 0.00f;
    private int n = -1;
    private String order_again = "";
    private boolean isVip = false;
    private TextView tv_should;
    private TextView tv_del;
    private TextView tv_del_kq;
    //private TextView tv_del_jf;
    private EditText et_payau;
    private EditText et_gread;
    private float integerValue = 0;
    private String ids = "";
    private String num = "";
    private String moneycount = "";
    private String integral = "";
    private float integralCount = 0f;
    private float inteCount = 0f;
    private TextView tv_jf;
    private float delmoney_jf = 0;
    private float delmoney_kq = 0;
    private String couponId = "";//优惠券id
    private float payAu = 0;
    private String password = "";//
    private float oldm = 0;//
    private String gread = "";//
    private boolean flag = false;
    private String orderTime = "";
    private List<PrintPage> list_son = new ArrayList<>();
    private String uid = "";
    private Unbinder unbinder = null;
    private boolean isCheck = false;
    private String delMoney = "";

    private String key = "";
    private String result = "";

    private String PayTicket = "";//优惠券优惠的金额


    private float dx_jf;
    private float dx_mr;
    private float dx_max;
    private String dx_integral = "";
    private String dx_money = "";
    private String temName = "";
    private String temporaryNum = "";
    private boolean isTemporary = false;

    private boolean XJ, WX, AL, TR;

    @BindView(R.id.ll_jfdx)
    LinearLayout ll_jfdx;
    @BindView(R.id.btn_sjsk)
    Button btn_sjsk;
    @BindView(R.id.btn_hdjf)
    Button btn_hdjf;
    @BindView(R.id.btn_dxjf)
    Button btn_dxjf;
    @BindView(R.id.cb_ischeck_cpxf)
    AppCompatCheckBox db_isCheck;
    @BindView(R.id.tv_paymoney)
    TextView tv_paymoney;
    @BindView(R.id.tv_money_dx)
    TextView tv_money_dx;
    @BindView(R.id.et_dx_jf)
    EditText et_dx_jf;


    @BindView(R.id.btn_usable_discount_coupon)//可用优惠券
            Button btn_coupon;
    @BindView(R.id.btn_nouse_coupon)//不使用优惠券
            Button btn_nocoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_buy_shop2);
        this.setOnPayCallBack(this);
        setOnPrintSuccess(this);
        setOnPrintError(this);
        this.setOnCrateOrderNumber(this);
        initTitale();
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        initSendData();
        getOrderNum("SY");
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
        title_name.setText("结算");
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
        XJ = SharedUtil.getSharedBData(PayBuyShopActivity.this, "GX");
        WX = SharedUtil.getSharedBData(PayBuyShopActivity.this, "GW");
        AL = SharedUtil.getSharedBData(PayBuyShopActivity.this, "GA");
        TR = SharedUtil.getSharedBData(PayBuyShopActivity.this, "GT");
        if (!XJ) {
            tvPayXj.setVisibility(View.GONE);//如果没有现金权限隐藏现金支付
        }
        if (!WX) {
            tvPayWx.setVisibility(View.GONE);//如果没有微信权限隐藏微信支付
        }
        if (!AL) {
            tvPayZfb.setVisibility(View.GONE);//如果没有阿里权限隐藏支付宝支付
        }
        if (!WX && !AL) {
            llWA.setVisibility(View.GONE);//微信阿里同时没有权限 隐藏布局
        }
        if (!TR || robotType == 4) {
            llDsf.setVisibility(View.GONE);
            tvPayDsf.setVisibility(View.GONE); //如果没有第三方权限隐藏第三方支付
        }

        btn_coupon.setOnClickListener(this);
        btn_nocoupon.setOnClickListener(this);
        dx_jf = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_jf"));//几多积分抵现一元
        dx_mr = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
        dx_max = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_max"));//最大抵现几多
        if (Math.abs(dx_jf - 0.0) == 0) {
            db_isCheck.setVisibility(View.GONE);
        }
        tv_should = (TextView) findViewById(R.id.pay_shop_shuold);
        tv_del = (TextView) findViewById(R.id.pay_shop_del);
        tv_del_kq = (TextView) findViewById(R.id.pay_shop_del_kq);
        // tv_del_jf = (TextView) findViewById(R.id.pay_shop_del_jf);
        et_payau = (EditText) findViewById(R.id.pay_shop_payau);
        et_gread = (EditText) findViewById(R.id.pay_shop_gread);
        InputFilter[] filters = {new MyTextFilter2()};
        et_payau.setFilters(filters);
        et_gread.setFilters(filters);
        et_dx_jf.setFilters(filters);
        // TODO: 2017/5/11 下个版本添加
        et_payau.setInputType(InputType.TYPE_NULL);
        et_gread.setInputType(InputType.TYPE_NULL);
        et_dx_jf.setInputType(InputType.TYPE_NULL);
        ////// TextView tv_kq = (TextView) findViewById(R.id.tv_pay_kq);
        if (SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxsj")) {
            btn_sjsk.setVisibility(View.GONE);
            et_payau.setFocusableInTouchMode(true);
            et_payau.setFocusable(true);
            et_payau.requestFocus();
            et_payau.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxjf")) {
            btn_hdjf.setVisibility(View.GONE);
            et_gread.setFocusableInTouchMode(true);
            et_gread.setFocusable(true);
            et_gread.requestFocus();
            et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxdx")) {
            btn_dxjf.setVisibility(View.GONE);
            et_dx_jf.setFocusableInTouchMode(true);
            et_dx_jf.setFocusable(true);
            et_dx_jf.requestFocus();
            et_dx_jf.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        tv_jf = (TextView) findViewById(R.id.tv_pay_jf);


        db_isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isVip) {
                    isCheck = isChecked;
                    if (isChecked) {
                        float del;
                        delMoney = et_payau.getText().toString();
                        if (TextUtils.isEmpty(delMoney)) {
                            del = 0;
                        } else {
                            del = parseFloat(delMoney);
                        }

                        float jf = inteCount;
                        if (dx_max >= del * dx_mr) {//否超过默认金额
                            if (del * dx_mr * dx_jf <= jf) {//卡积分够
                                et_dx_jf.setText(Utils.getNumStr(del * dx_mr * dx_jf));
                                tv_money_dx.setText(Utils.getNumStr(del * dx_mr));
                                tv_paymoney.setText(Utils.getNumStr(del - del * dx_mr));

                            } else {
                                et_dx_jf.setText(inteCount + "");
                                tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                tv_paymoney.setText(Utils.getNumStr(del - jf / dx_jf));
                            }
                        } else {
                            if (dx_max * dx_jf <= jf) {//卡积分够
                                et_dx_jf.setText(Utils.getNumStr(dx_max * dx_jf));
                                tv_money_dx.setText(Utils.getNumStr(dx_max));
                                tv_paymoney.setText(Utils.getNumStr(del - dx_max));
                            } else {
                                et_dx_jf.setText(inteCount + "");
                                tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                tv_paymoney.setText(Utils.getNumStr(del - jf / dx_jf));
                            }
                            if (dx_max == 0.0f) {//不限积分抵现额度
                                if (del * dx_mr * dx_jf <= jf) {//卡积分够
                                    et_dx_jf.setText(Utils.getNumStr(del * dx_mr * dx_jf));
                                    tv_money_dx.setText(Utils.getNumStr(del * dx_mr));
                                    tv_paymoney.setText(Utils.getNumStr(del - del * dx_mr));
                                }else {
                                    et_dx_jf.setText(inteCount + "");
                                    tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                    tv_paymoney.setText(Utils.getNumStr(del - jf / dx_jf));
                                }
                            }
                        }
                        btn_dxjf.setEnabled(true);
                        et_payau.setInputType(InputType.TYPE_NULL);
                        et_gread.setInputType(InputType.TYPE_NULL);
                        ll_jfdx.setVisibility(View.VISIBLE);

                    } else {
                        dx_jf = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_jf"));//几多积分抵现一元
                        dx_mr = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
                        dx_max = parseFloat(SharedUtil.getSharedData(PayBuyShopActivity.this, "dx_max"));//最大抵现几多
                        temporaryNum = "";
                        temName = "";
                        et_dx_jf.setText("0");
                        tv_money_dx.setText("0");
                        tv_paymoney.setText("0");
                        btn_dxjf.setEnabled(false);
                        isTemporary = false;
                        et_dx_jf.setInputType(InputType.TYPE_NULL);

                        if (SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxsj")) {
                            et_payau.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        } else {
                            et_payau.setInputType(InputType.TYPE_NULL);

                        }
                        if (SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxjf")) {
                            et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        } else {
                            et_gread.setInputType(InputType.TYPE_NULL);
                        }

                        ll_jfdx.setVisibility(View.GONE);
                    }


                } else {
                    db_isCheck.setChecked(false);
                    Utils.showToast(PayBuyShopActivity.this, "您不是会员，无法使用积分");
                    ll_jfdx.setVisibility(View.GONE);
                }
            }
        });

//        et_payau.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    //  et_payau.setText("");
//                }
//            }
//        });
//
//        et_gread.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // et_gread.setText("");
//                }
//            }
//        });

        et_dx_jf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String e = et_dx_jf.getText().toString();
                if (e.equals(0) || e.equals("0") || e.equals("0.")) {
                    et_dx_jf.setText("");
                }
                if (isTemporary || SharedUtil.getSharedBData(PayBuyShopActivity.this, "cxdx")) {
                    String str = et_dx_jf.getText().toString();
                    String str2 = et_payau.getText().toString();
                    et_dx_jf.setSelection(str.length());
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
                    et_dx_jf.removeTextChangedListener(this);
                    if (i == 1) {
                        et_dx_jf.setText(str);
                    } else {
                        et_dx_jf.setText(del_jf + "");
                    }
                    et_dx_jf.addTextChangedListener(this);
                    et_dx_jf.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    et_dx_jf.setSelection(et_dx_jf.getText().length());
                    tv_money_dx.setText(Utils.getNumStr(del_jf / dx_jf));
                    tv_paymoney.setText(Utils.getNumStr(delm - del_jf / dx_jf));

                }
            }

        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra("isVip");
            should = intent.getStringExtra("should");
            if (str.equals("yes")) {
                isVip = true;
                tv_jf.setVisibility(View.VISIBLE);
                //  tvPayHy.setVisibility(View.VISIBLE);
                uid = intent.getStringExtra("uid");
                memid = intent.getStringExtra("memid");
                cardNum = intent.getStringExtra("cardNum");
                name = intent.getStringExtra("name");
                password = intent.getStringExtra("pwd");
                oldm = Float.parseFloat(intent.getStringExtra("old"));
                integerValue = Float.parseFloat(intent.getStringExtra("integ"));
                inteCount = Float.parseFloat(intent.getStringExtra("intecount"));
                payAu = Float.parseFloat(intent.getStringExtra("payau"));
                del = intent.getStringExtra("del");
                et_payau.setText(payAu + "");
            } else {
                isVip = false;
                tv_jf.setVisibility(View.INVISIBLE);
                payAu = Float.parseFloat(should);
                et_payau.setText(should);
                // tvPayHy.setVisibility(View.INVISIBLE);
            }
            payau = payAu - delmoney_jf - delmoney_kq;
            tv_should.setText("总价:  ￥" + should);
            tv_del.setText("折扣优惠:  ￥" + del);
            tv_del_kq.setText("卡券优惠:  ￥" + delmoney_kq);
            // tv_del_jf.setText("积分抵用:￥" + del_jf);
        }
    }

    //支付的点击监听
    @OnClick({R.id.tv_pay_xj, R.id.tv_pay_dsf, R.id.tv_pay_hy, R.id.tv_pay_wx, R.id.tv_pay_zfb})
    public void pay(TextView view) {
        dx_integral = et_dx_jf.getText().toString();
        dx_money = tv_money_dx.getText().toString();
        if (dx_integral.equals("")) {
            dx_integral = "0";
        }
        if (dx_money.equals("")) {
            dx_money = "0";
        }
        if (TextUtils.isEmpty(et_payau.getText().toString())) {
            payau = 0;
        } else {
            payau = Float.parseFloat(et_payau.getText().toString());
        }
        if (db_isCheck.isChecked()) {
            payau = Float.parseFloat(tv_paymoney.getText().toString());
        } else {

        }
        gread = et_gread.getText().toString();
        if (TextUtils.isEmpty(gread)) {
            gread = "0";
        }


        if (isclick_pay) {
            isclick_pay = false;
            switch (view.getId()) {
//                case R.id.tv_pay_kq:
//                    if (payau > 0) {
//                        Intent intent = new Intent(PayBuyShopActivity.this, BuyShopUseCouponActivity.class);
//                        intent.putExtra("delmoney", delmoney_kq);
//                        intent.putExtra("oldMoney", Utils.getNumStr(payau));
//                        startActivityForResult(intent, 119);
//                    } else {
//                        Utils.showToast(PayBuyShopActivity.this, "已经不用付钱了");
//                    }
//                    break;
//                case R.id.tv_pay_jf:
//                    if (isVip) {
//                        if (payau > 0) {
//                            Intent intent1 = new Intent(PayBuyShopActivity.this, BuyShopUseIntegralActivity.class);
//                            intent1.putExtra("cardNum", cardNum);
//                            intent1.putExtra("name", name);
//                            intent1.putExtra("integral", inteCount);
//                            intent1.putExtra("delIntegral", del_jf);
//                            intent1.putExtra("delmoney", delmoney_jf);
//                            intent1.putExtra("oldMoney", Utils.getNumStr(payau));
//                            startActivityForResult(intent1, 120);
//                        } else {
//                            Utils.showToast(PayBuyShopActivity.this, "已经不用付钱了");
//                        }
//                    }
//                    break;
                case R.id.tv_pay_xj:
                    if (isVip && TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyShopActivity.this, "请输入积分");
                    } else {
                        n = 0;
                        sendData("");
                    }
                    break;
                case R.id.tv_pay_dsf:
                    getOrderNum("SY");//后加
                    if (isVip && TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyShopActivity.this, "请输入积分");
                    } else {
                        isclick_pay = true;
                        n = 3;
                        sendData("");
//                        payMoney(1, payau + "", orderNumber, "商品消费");
                    }
                    break;

                case R.id.tv_pay_wx:
                    getOrderNum("SY");//后加
                    if (isVip && TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyShopActivity.this, "请输入积分");
                    } else {
                        n = 1;
                        sendData("");
                        // payMoney(1, payau + "", orderNumber, "商品消费");
                    }
                    break;
                case R.id.tv_pay_zfb:
                    getOrderNum("SY");//后加
                    if (isVip && TextUtils.isEmpty(gread)) {
                        isclick_pay = true;
                        Utils.showToast(PayBuyShopActivity.this, "请输入积分");
                    } else {
                        n = 2;
                        sendData("");
                        // payMoney(2, payau + "", orderNumber, "商品消费");
                    }
                    break;
                case R.id.tv_pay_hy:
                    if (isVip) {
                        if (TextUtils.isEmpty(gread)) {
                            isclick_pay = true;
                            Utils.showToast(PayBuyShopActivity.this, "请输入积分");
                        } else {
                            n = 4;
                            if (oldm >= payau) {
                                if (TextUtils.isEmpty(password)) {
                                    sendData("");
                                } else {
                                    getPassword();
                                }
                            } else {
                                isclick_pay = true;
                                Utils.showToast(PayBuyShopActivity.this, "余额不足,请充值");
                            }
                        }
                    } else {
                        isclick_pay = true;
                        Utils.showToast(PayBuyShopActivity.this, "您不是会员，无法使用会员卡支付");
                    }
                    break;
            }
        }
    }


    private void getPassword() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(PayBuyShopActivity.this).inflate(R.layout.number_password, null);
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
                        case 0:
                        case 4:
                            sendData("");
                            break;
                        case 1:
                        case 3:
                            payMoney(n, payau + "", orderNumber, "商品消费");
                            break;
                    }
                    window.dismiss();
                } else {
                    isclick_pay = true;
                    editText.setText("");
                    Utils.showToast(PayBuyShopActivity.this, "密码错误,请重输");
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


    private void sendData(String orderidScan) {

        if (TextUtils.isEmpty(orderidScan))
            orderidScan = "";
        Map<String, String> map = new HashMap<>();
        if (isVip) {
            map.put("integral", integral);
            map.put("get_integral", gread);
            map.put("Member_Id", memid);
            map.put("payDecIntegral", dx_integral);//抵现的积分
            map.put("payIntegral", dx_money);//抵现的金额
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderTime = simpleFormat.format(date);
        map.put("dbName", SharedUtil.getSharedData(PayBuyShopActivity.this, "dbname"));
        map.put("goods_id", ids);
        map.put("num", num);
        map.put("money", moneycount);
        map.put("User_Id", SharedUtil.getSharedData(PayBuyShopActivity.this, "userInfoId"));
        map.put("EquipmentNum", terminalSn);
        map.put("PayShould", should);
        map.put("PayActual", payau + "");
        map.put("PayDiscounted", payAu + "");
        map.put("orderNo", orderNumber);
        map.put("CardCode", uid);
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "0");
        map.put("PayTicket", PayTicket);
        map.put("coupon_id", couponId);

        switch (n) {
            case 0:
                map.put("payCash", payau + "");
                break;
            case 1:
                map.put("payWeChat", payau + "");
                map.put("refernumber", orderidScan);
                break;
            case 2:
                map.put("payAli", payau + "");
                map.put("refernumber", orderidScan);
                break;
            case 3:
                map.put("payThird", payau + "");
                map.put("refernumber", orderidScan);
                break;
            case 4:
                map.put("payCard", payau + "");
                break;
        }
        map.put("temporary_num", temporaryNum);
        map.put("result_name", temName);
        postToHttp(NetworkUrl.BUYSHOP, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e("" + jsonText);
                isPay = true;
                showHttpData(jsonText);
            }

            @Override
            public void OnFail(String message) {
                if (n != 3) {
                    flag = false;
                    payHint(false);
                    showAlert();
                } else {
                    isclick_pay = true;
                }
            }
        });

    }

    private void initSendData() {
        ids = "";
        num = "";
        moneycount = "";
        integral = "";
        PayTicket = "";
        couponId = "";
        integralCount = 0;
        for (int i = 0; i < list_buy.size(); i++) {
            integralCount += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * integerValue * shopIntegral;
            if (i == list_buy.size() - 1) {
                ids += list_buy.get(i).getId();
                num += list_buy.get(i).getNum();
                moneycount += list_buy.get(i).getMoneyin();
                integral += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * (isVip ? integerValue : 0) * shopIntegral;
            } else {
                ids += list_buy.get(i).getId() + ",";
                num += list_buy.get(i).getNum() + ",";
                moneycount += list_buy.get(i).getMoneyin() + ",";
                integral += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * (isVip ? integerValue : 0) * shopIntegral + ",";
            }
        }
        gread = integralCount + "";
        et_gread.setText(Utils.getNumStr(integralCount));
    }

    private void showHttpData(String text) {
        try {
            JSONObject object = new JSONObject(text);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                if (n == 0 || n == 4) {
                    reSet();
                } else {
                    payMoney(n, payau + "", orderNumber, "商品消费");

                }
            } else if (tag.equals("ERR")) {
                flag = true;
                isclick_pay = true;
                String msg = object.getString("result_errmsg");
                Utils.showToast(PayBuyShopActivity.this, msg);
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(PayBuyShopActivity.this, msg);
                if (n != 3) {
                    flag = false;
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
        payHint(true);
        tv_print.setVisibility(View.VISIBLE);
        tv_print.setEnabled(true);
        printInfo(true);
        switch (robotType) {
            case 3:
            case 4:
                changeActivity();
                break;
        }
    }

    private void payHint(boolean flag) {
        LinearLayout ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        LinearLayout ll_pay_shop = (LinearLayout) findViewById(R.id.ll_pay_shop);
        ll_pay_shop.setVisibility(View.GONE);
        ll_pay.setVisibility(View.GONE);
        LinearLayout ll_hint = (LinearLayout) findViewById(R.id.ll_pay_hint);
        ImageView iv_hint = (ImageView) findViewById(R.id.iv_pay_hint);
        TextView tv_hint = (TextView) findViewById(R.id.tv_pay_hint);
        ll_hint.setVisibility(View.VISIBLE);
        iv_hint.setSelected(flag);
        tv_hint.setText(payau + "元商品购买" + (flag ? "成功" : "失败"));
    }

    private synchronized void printInfo(boolean flag) {
        printCount = 0;
        list_son.clear();
        for (int i = 0; i < list_buy.size(); i++) {
            PrintPage page = new PrintPage();
            page.setMoney(Utils.getNumStr(list_buy.get(i).getMoney()) + "");
            page.setNum(list_buy.get(i).getNum() + "");
            page.setPrice(list_buy.get(i).getPrice() + "");
            page.setName(list_buy.get(i).getName());
            list_son.add(page);
        }
        List<String> listp = new ArrayList<>();
        if (getSharedBData(PayBuyShopActivity.this, "bluetooth")) {
            int nn = getSharedInt(PayBuyShopActivity.this, "num");
            String str = SharedUtil.getSharedData(PayBuyShopActivity.this, "day");
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            if (!str.equals(time.substring(8, 10))) {
                nn = 0;
                SharedUtil.setSharedData(PayBuyShopActivity.this, "day", time.substring(8, 10));
            }
            nn += 1;
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
                listp.add("支付方式:第三方支付");
                break;
            case 4:
                listp.add("支付方式:会员卡");
                break;
        }
        listp.add("总金额:" + should);
        if (isVip) {
            listp.add("实收金额:" + payau);
        } else {
            listp.add("实收金额:" + payau);
        }
        listp.add("折扣优惠:" + del);
        listp.add("卡券优惠:" + delmoney_kq);
        listp.add("积分优惠:" + del_jf);
        listp.add("抹零:" + Utils.getNumStr(payAu - delmoney_jf - delmoney_kq - payau));
        if (isVip) {
            listp.add("获得积分:" + gread);
            if (flag) {
                if (n == 4) {
                    listp.add("剩余储值:" + Utils.getNumStr(oldm - payau));
                } else {
                    listp.add("剩余储值:" + Utils.getNumStr(oldm));
                }
                listp.add("剩余积分:" + Utils.getNumStr(inteCount + Float.parseFloat(gread)));
            }
        }
        if (flag) {
            printPage("商品消费小票", listp, list_son, true);
        } else {
            printPage("商品消费延时扣款小票", listp, list_son, true);
            printPage("商品消费延时扣款小票(商户存根)", listp, list_son, true);
        }


    }

    private void changeActivity() {
        list_buy.clear();
        Intent intent = new Intent(PayBuyShopActivity.this, MainActivity.class);
        intent.putExtra("isPay", "yes");
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
//        if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
//            LKLPay(orderNum);
//        } else {
//            //     sendData(orderNum);
        LKLPay(orderNum);
        loadingDialog.show();
//        }
    }

    private void LKLPay(String order) {
        Map<String, String> map = new HashMap<>();
        map.put("refernumber", order);
        map.put("orderNo", orderNumber);
        map.put("dbName", getSharedData(PayBuyShopActivity.this, "dbname"));
        switch (n) {
            case 1:
                map.put("payWeChat", payau + "");
                break;
            case 2:
                map.put("payAli", payau + "");
                break;
            case 3:
                map.put("payThird", payau + "");
            case 4:
                map.put("payCard", payau + "");
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
        BuyShopDb buyShopDb = new BuyShopDb();
        buyShopDb.setN(n);
        buyShopDb.setVip(isVip);
        buyShopDb.setUrl(NetworkUrl.BUYSHOP);
        buyShopDb.setGoods_id(ids);
        buyShopDb.setNum(num);
        buyShopDb.setUid(uid);
        buyShopDb.setMoney(moneycount);
        buyShopDb.setIntegral(integral);
        buyShopDb.setDx_Integral(dx_integral);
        buyShopDb.setDx_Money(dx_money);
        buyShopDb.setGet_integral(gread);
        buyShopDb.setMember_Id(memid);
        buyShopDb.setUser_Id(SharedUtil.getSharedData(PayBuyShopActivity.this, "userInfoId"));
        buyShopDb.setEquipmentNum(terminalSn);
        buyShopDb.setPayShould(should);
        buyShopDb.setPayActual(payau + "");
        buyShopDb.setPayDiscounted(payAu + "");
        buyShopDb.setOrderNo(orderNumber);
        buyShopDb.setDbName(SharedUtil.getSharedData(PayBuyShopActivity.this, "dbname"));
        buyShopDb.setOrderTime(orderTime);
        buyShopDb.setShopName(SharedUtil.getSharedData(PayBuyShopActivity.this, "shopname"));
        buyShopDb.setCardNum(cardNum);
        buyShopDb.setOrder_again(order_again);
        buyShopDb.setName(name);
        buyShopDb.setYouhui(del);
        buyShopDb.setTemName(temName);
        buyShopDb.setTemporaryNum(temporaryNum);
        buyShopDb.setPayTicket(PayTicket);
        buyShopDb.setCoupon_id(couponId);

        if (n == 4) {
            buyShopDb.setOldMoney(Utils.getNumStr(oldm - payAu));
        } else {
            buyShopDb.setOldMoney(Utils.getNumStr(oldm));
        }
        buyShopDb.setOldIntegral(Utils.getNumStr(inteCount + Float.parseFloat(gread)));
        buyShopDb.setDelkq(delmoney_kq + "");
        buyShopDb.setDeljf(del_jf + "");
        buyShopDb.setUserName(SharedUtil.getSharedData(PayBuyShopActivity.this, "username"));
        buyShopDb.setDataList(list_buy);
        if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber)) {
            Logger.d(buyShopDb.save() + "");
        }

    }

    private boolean alert_flag = true;

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(PayBuyShopActivity.this);
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
                if (n == 3) {
                    LKLPay(order_again);
                } else {
                    sendData(order_again);
                }

            }
        });
        dialog.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 119:
                    delmoney_kq = data.getFloatExtra("delM", 0);
                    resetMoney();
                    break;

                case 120:
                    del_jf = data.getFloatExtra("delInte", 0);
                    delmoney_jf = data.getFloatExtra("delM", 0);
                    resetMoney();
                    break;

                case 121:
                    delmoney_kq = data.getFloatExtra("couponMoney", 0);
                    couponId = data.getStringExtra("couponId");
                    PayTicket = delmoney_kq + "";
                    resetMoney();
                    break;

            }
        }
    }

    private void resetMoney() {
        payau = payAu - delmoney_jf - delmoney_kq;
        if (payau < 0) {
            payau = 0f;
        }
        et_payau.setText(Utils.getNumStr(payau));
        tv_del.setText("折扣优惠:  ￥" + del);
        tv_del_kq.setText("卡券优惠:  ￥" + delmoney_kq);
        // tv_del_jf.setText("积分抵用:￥" + delmoney_jf);
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
        if (printCount == 0) {
            bluetoothPrint(list_son, orderNumber);
        }
        printCount++;
        if (flag) {
            changeActivity();//打印成功跳转到主页面
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
            map.put("dbName", getSharedData(PayBuyShopActivity.this, "dbname"));
            postToHttp(NetworkUrl.DELPAYQRCODE, map, null);
        }
        getOrderNum("SY");
    }


    @OnClick({R.id.btn_sjsk, R.id.btn_hdjf, R.id.btn_dxjf})
    public void temporary(View v) {
        switch (v.getId()) {
            case R.id.btn_sjsk:
                if (!db_isCheck.isChecked()) {
                    key = "je";
                    Arrived();
                } else {
                    Utils.showToast(PayBuyShopActivity.this, "该状态下不能修改“实际收款”");
                }

                break;
            case R.id.btn_hdjf:
                if (!db_isCheck.isChecked()) {
                    key = "jf";
                    Arrived();
                } else {
                    Utils.showToast(PayBuyShopActivity.this, "该状态下不能修改“获得积分”");
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
        View contentView = LayoutInflater.from(PayBuyShopActivity.this).inflate(R.layout.pop_temporary, null);
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
                    Utils.showToast(PayBuyShopActivity.this, "请输入工号");
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
        map.put("dbName", getSharedData(PayBuyShopActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);

                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    result = j.getString("result_name");

                    int ssje = s.indexOf("POS:产销实收金额修改");
                    int zsjf = s.indexOf("POS:产销获得积分修改");
                    int jfdx = s.indexOf("POS:产销积分抵现修改");
                    Logger.e("" + ssje + "---" + zsjf + "---" + jfdx);
                    if (key.equals("je")) {
                        if (jfdx > 0) {
                            Utils.showToast(PayBuyShopActivity.this, "授权成功");
                            // isTemporary = true;
                            temporaryNum = num;
                            temName = j.getString("result_name") + " ";
                            et_payau.setFocusableInTouchMode(true);
                            et_payau.setFocusable(true);
                            et_payau.requestFocus();
                            et_payau.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        } else {
                            Utils.showToast(PayBuyShopActivity.this, "您没有权限或密码错误");
                        }
                    }
                    if (key.equals("jf")) {
                        if (zsjf > 0) {
                            Utils.showToast(PayBuyShopActivity.this, "授权成功");

                            temporaryNum = num;
                            temName = j.getString("result_name") + "";
                            et_gread.setFocusableInTouchMode(true);
                            et_gread.setFocusable(true);
                            et_gread.requestFocus();
                            et_gread.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(PayBuyShopActivity.this, "您没有权限或密码错误");
                        }
                    }
                    if (key.equals("dx")) {
                        if (ssje > 0) {
                            Utils.showToast(PayBuyShopActivity.this, "授权成功");
                            isTemporary = true;
                            temporaryNum = num;
                            temName = j.getString("result_name") + "";
                            et_dx_jf.setFocusableInTouchMode(true);
                            et_dx_jf.setFocusable(true);
                            et_dx_jf.requestFocus();
                            et_dx_jf.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        } else {
                            Utils.showToast(PayBuyShopActivity.this, "您没有权限或密码错误");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(PayBuyShopActivity.this, "服务器异常");
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(PayBuyShopActivity.this, "临时授权失败");
            }

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_usable_discount_coupon:
                Intent intent = new Intent();
                intent.setClass(PayBuyShopActivity.this, CouponSelectActivity.class);
                intent.putExtra("memberID", memid);
                intent.putExtra("couponCode", "");
                intent.putExtra("costType", "产品消费");
                intent.putExtra("costMoney", payAu);
                startActivityForResult(intent, 121);
                db_isCheck.setChecked(false);
                //db_isCheck.setChecked(true);
                break;
            case R.id.btn_nouse_coupon:
                delmoney_kq = 0;
                couponId = "";
                PayTicket = "";
                resetMoney();
                break;


        }
    }


}
