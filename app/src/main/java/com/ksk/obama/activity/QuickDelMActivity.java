package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.gson.Gson;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.R;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.CardInfo;
import com.ksk.obama.utils.MyTextFilter;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ksk.obama.utils.SharedUtil.getSharedData;
import static java.lang.Float.parseFloat;

/**
 * 快速收银界面*2017
 */
public class QuickDelMActivity extends BasePAndRActivity implements View.OnClickListener,
        IReadCardId, IPayCallBack, IQrcodeCallBack, IPrintErrorCallback, IPrintSuccessCallback
        , ICreateOrderNumber {
    private String password = "";
    private String payau;
    private String money1;
    private float vipIntegral = 0;

    private String preID = "";
    private String getIntegral = "0";
    private float maxMoney;
    private boolean flag = false;
    private int shopi;

    private EditText et_cardNum;
    private EditText et_money;
    private TextView tv_name;
    private TextView tv_m;
    private TextView tv_i;
    private String cardNum = "散客";
    private String cardName = "散客";
    private String haveMoney = "";
    private String haveIntegral = "";
    private int n = -1;
    private String money;
    private String dmoney = "";
    private String orderNo = "";
    private boolean isVip = false;
    private boolean isInfo = false;//是否检测到会员卡的信息
    private String gread = "0";
    private String ordernb = "";
    private String orderte;
    private String uid = "";
    private Button btn_change;
    private TextView tv_pay;
    private TextView tv_money_dx;
    private EditText et_gread_dx;
    private AppCompatCheckBox db_isCheck;
    private LinearLayout ll_dx;
    private boolean isCheck = false;
    private String delMoney;
    private float dx_jf;
    private float dx_mr;
    private float dx_max;
    private String delIntegral = "";
    private String temporaryNum = "";
    private boolean isTemporary = false;
    private String temName = "";
    //private boolean isQrSure = false;//二维码支付确认

    private Unbinder unbinder;


    @BindView(R.id.tv_pay_xj)
    TextView pay_xj;
    @BindView(R.id.tv_pay_hy)
    TextView pay_hy;
    @BindView(R.id.tv_pay_sm)
    TextView pay_sm;
    @BindView(R.id.tv_pay_yl)
    TextView pay_yl;
    @BindView(R.id.tv_pay_wx)
    TextView pay_wx;
    @BindView(R.id.tv_pay_zfb)
    TextView pay_zfb;
    @Nullable
    @BindView(R.id.ll_lkl)
    LinearLayout ll_lkl;
    @Nullable
    @BindView(R.id.ll_w_a)
    LinearLayout ll_w_a;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_del_money);
        this.setOnReadCardId(this);
        this.setOnPayCallBack(this);
        this.setOnReadQrcode(this);
        this.setOnPrintError(this);
        this.setOnPrintSuccess(this);
        this.setOnCrateOrderNumber(this);
        unbinder = ButterKnife.bind(this);
        initTitle();
        setQX();
        initViewM();
        getOrderNum("KM");
        if (isNetworkAvailable(QuickDelMActivity.this)) {
            queryDb(true);
        } else {
            int n = queryDb(false);
            Utils.showToast(QuickDelMActivity.this, "当前无网络,您有" + n + "单子未上传");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        close();
    }

    private void setQX() {
        String str = "";
        TextView tv = (TextView) findViewById(R.id.tv_hint);
        if (SharedUtil.getSharedBData(QuickDelMActivity.this, "citiao")) {
            str += "磁条卡,";
        }
        if (SharedUtil.getSharedBData(QuickDelMActivity.this, "nfc")) {
            str += "M1卡,";
        }
        if (SharedUtil.getSharedBData(QuickDelMActivity.this, "saoma")) {
            str += "二维码卡";
        }
        tv.setText("当前可刷卡类型：" + str);
    }

    private void initTitle() {
        TextView tv_name = (TextView) findViewById(R.id.title_name);
        tv_name.setText("快速消费");
        findViewById(R.id.tv_commit).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int queryDb(boolean flag) {
        Connector.getDatabase();
        List<QuickDelMoney> list = DataSupport.findAll(QuickDelMoney.class);
        if (list != null && list.size() > 0 && flag) {
            startActivity(new Intent(QuickDelMActivity.this, QuickDelMoneySupplementActivity.class));
        }
        return list.size();
    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        et_cardNum.setText(cardNo);
        getCardInfo(cardNo);

    }

    protected void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(QuickDelMActivity.this);
        dialog.setTitle("提示:");
        dialog.setMessage("操作成功");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                openRead();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void Arrived() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(QuickDelMActivity.this).inflate(R.layout.pop_temporary, null);
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
                    Utils.showToast(QuickDelMActivity.this, "请输入工号");
                } else {
                    window.dismiss();
                    getTemporary(str1, str2);
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
        window.showAtLocation(findViewById(R.id.ll_quick_money_root), Gravity.CENTER, 0, 0);
    }

    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    int jfdx = s.indexOf("POS:快消积分抵现修改");
                    if (jfdx > 0) {
                        Utils.showToast(QuickDelMActivity.this, "临时授权成功");
                        temName = j.getString("result_name");
                        temporaryNum = num;
                        isTemporary = true;
                        et_gread_dx.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    } else {
                        Utils.showToast(QuickDelMActivity.this, "您没有权限");
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

    @OnClick({R.id.btn_read_code, R.id.btn_change, R.id.tv_pay_xj, R.id.tv_pay_sm, R.id.tv_pay_yl, R.id.tv_pay_hy, R.id.tv_pay_wx, R.id.tv_pay_zfb})
    public void onClick(View v) {
        payau = et_money.getText().toString();
        String mon = et_money.getText().toString();
        if (TextUtils.isEmpty(mon)) {
            mon = "0";
        }
        switch (v.getId()) {
            case R.id.btn_read_code:
                toQrcodeActivity();
                break;
            case R.id.btn_change:
                Arrived();
                break;

            case R.id.tv_pay_xj://现金支付
                if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("重要提示：");
                    builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isclick_pay) {
                                isclick_pay = false;
                                n = 0;
                                sendDelMoney();
                            }
                        }
                    });
                    builder.create().show();
                    isclick_pay = true;
                } else {
                    if (isclick_pay) {
                        isclick_pay = false;
                        n = 0;
                        sendDelMoney();
                    }
                }

                break;
            case R.id.tv_pay_sm://lkl扫码
                if (parseFloat(mon) > maxMoney && maxMoney != 0) {
                    isclick_pay = true;
                    Utils.showToast(QuickDelMActivity.this, "超出扣除最大范围，请修改扣除金额");
                } else {
                    if (db_isCheck.isChecked()) {
                        payau = tv_pay.getText().toString();
                    } else {
                        payau = et_money.getText().toString();
                    }
                    if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("重要提示：");
                        builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isclick_pay) {
                                    isclick_pay = false;
                                    n = 1;
                                    if (robotType == 1 || robotType == 8) {
                                        sendDelMoney();
                                    } else {
                                        payMoney(1, payau, orderNumber, "商品消费");
                                    }
                                }
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        if (isclick_pay) {
                            isclick_pay = false;
                            n = 1;
                            if (robotType == 1 || robotType == 8) {
                                sendDelMoney();
                            } else {
                                payMoney(1, payau, orderNumber, "商品消费");
                            }
                        }
                    }
                }
                break;
            case R.id.tv_pay_yl://银联支付
                if (parseFloat(mon) > maxMoney && maxMoney != 0) {
                    isclick_pay = true;
                    Utils.showToast(QuickDelMActivity.this, "超出扣除最大范围，请修改扣除金额");
                } else {
                    if (db_isCheck.isChecked()) {
                        payau = tv_pay.getText().toString();
                    } else {
                        payau = et_money.getText().toString();
                    }
                    if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("重要提示：");
                        builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isclick_pay) {
                                    isclick_pay = false;
                                    n = 3;
                                    if (robotType == 1 || robotType == 8) {
                                        sendDelMoney();
                                    } else {
                                        payMoney(3, payau, orderNumber, "商品消费");
                                    }
                                }
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        if (isclick_pay) {
                            isclick_pay = false;
                            n = 3;
                            if (robotType == 1 || robotType == 8) {
                                sendDelMoney();
                            } else {
                                payMoney(3, payau, orderNumber, "商品消费");
                            }
                        }
                    }
                }
                break;
            case R.id.tv_pay_hy://会员支付
                if (isInfo) {
                    if (isclick_pay) {
                        isclick_pay = false;
                        if (TextUtils.isEmpty(et_cardNum.getText().toString())) {
                            Utils.showToast(QuickDelMActivity.this, "请填写卡号");
                            openRead();
                        } else {
                            if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("重要提示：");
                                builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                                builder.setNegativeButton("取消", null);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        n = 4;
                                        if (TextUtils.isEmpty(password)) {
                                            sendDelMoney();
                                        } else {
                                            getPassword();
                                        }
                                    }
                                });
                                builder.create().show();
                                isclick_pay = true;
                            } else {
                                n = 4;
                                if (TextUtils.isEmpty(password)) {
                                    sendDelMoney();
                                } else {
                                    getPassword();
                                }
                            }

                        }
                    }
                } else {
                    Utils.showToast(QuickDelMActivity.this, "没有读取会员信息，无法会员卡支付");
                }
                break;

            case R.id.tv_pay_wx://微信支付
                if (parseFloat(mon) > maxMoney && maxMoney != 0) {
                    isclick_pay = true;
                    Utils.showToast(QuickDelMActivity.this, "超出扣除最大范围，请修改扣除金额");
                } else {
                    if (db_isCheck.isChecked()) {
                        payau = tv_pay.getText().toString();
                    } else {
                        payau = et_money.getText().toString();
                    }
                    if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("重要提示：");
                        builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isclick_pay) {
                                    if (payau.equals("") || payau.equals("0") || payau.equals("0.")) {
                                        Utils.showToast(QuickDelMActivity.this, "该方式实际消费金额不能为0");
                                    } else {
                                        showCodeSure();
                                    }
//                                    if (isQrSure) {
//                                        isclick_pay = false;
//                                        n = 1;
//                                        payMoney(1, payau, orderNumber, "商品消费");
//                                    }

                                }
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        if (isclick_pay) {
                            if (payau.equals("") || payau.equals("0") || payau.equals("0.")) {
                                Utils.showToast(QuickDelMActivity.this, "该方式实际消费金额不能为0");
                            } else {
                                showCodeSure();
                            }
//                            if (isQrSure) {
//                                isclick_pay = false;
//                                n = 1;
//                                payMoney(1, payau, orderNumber, "商品消费");
//                            }
                        }
                    }
                }
                break;

            case R.id.tv_pay_zfb://支付宝
                if (maxMoney != 0 && parseFloat(mon) > maxMoney) {
                    isclick_pay = true;
                    Utils.showToast(QuickDelMActivity.this, "超出扣除最大范围，请修改扣除金额");
                } else {
                    if (db_isCheck.isChecked()) {
                        payau = tv_pay.getText().toString();
                    } else {
                        payau = et_money.getText().toString();
                    }
                    if (!TextUtils.isEmpty(cardNum) && cardNum.equals(preID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("重要提示：");
                        builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isclick_pay) {
                                    isclick_pay = false;
                                    n = 2;
                                    sendDelMoney();
                                    //payMoney(2, payau, orderNumber, "商品消费");
                                }
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        if (isclick_pay) {
                            isclick_pay = false;
                            n = 2;
                            sendDelMoney();
                            //payMoney(2, payau, orderNumber, "商品消费");
                        }
                    }
                }
                break;
        }
    }

    private void showCodeSure() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(QuickDelMActivity.this).inflate(R.layout.qr_pay_dialog, null);
        final TextView textView = (TextView) contentView.findViewById(R.id.qr_pay_money);
        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_qr);
        final Button sure = (Button) contentView.findViewById(R.id.btn_sure_qr);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick_pay = true;
                window.dismiss();

            }
        });
        textView.setText(payau);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick_pay = false;
                n = 1;
                sendDelMoney();
                // payMoney(1, payau, orderNumber, "商品消费");
                window.dismiss();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isclick_pay = true;

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
        window.showAtLocation(findViewById(R.id.ll_quick_money_root), Gravity.CENTER, 0, 0);

    }


    private void getPassword() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(QuickDelMActivity.this).inflate(R.layout.number_password, null);
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
                            sendDelMoney();
                            break;
                        // case 2:
                        case 1:
                        case 3:
                            payMoney(n, payau, orderNumber, "商品消费");
                            break;
                    }
                    window.dismiss();
                } else {
                    isclick_pay = true;
                    editText.setText("");
                    Utils.showToast(QuickDelMActivity.this, "密码错误,请重输");
                }
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isclick_pay = true;
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
        window.showAtLocation(findViewById(R.id.ll_quick_money_root), Gravity.CENTER, 0, 0);
    }

    private void getDelMoney() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
        postToHttp(NetworkUrl.QUICK, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    Logger.e(jsonText);
                    JSONObject object = new JSONObject(jsonText);
                    String money = object.getString("defaultcost");
                    et_money.setText(money);
                    openRead();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {

            }
        });
    }

    private void getCardInfo(String cardNum) {
        if (!TextUtils.isEmpty(et_cardNum.getText().toString())) {
            cardNum = et_cardNum.getText().toString();
        }
        if (TextUtils.isEmpty(cardNum)) {
            isclick_pay = true;
            Utils.showToast(QuickDelMActivity.this, "请输入卡号");
        } else {
            sendIdToDel(cardNum);
        }
    }

    private void sendIdToDel(final String cardNum1) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
        map.put("cardNO", cardNum1);
        map.put("CardCode", uid);
        map.put("gid", SharedUtil.getSharedData(QuickDelMActivity.this, "groupid"));
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                openRead();
                CardInfo cardInfo = new Gson().fromJson(jsonText, CardInfo.class);
                if (cardInfo != null) {
                    if (cardInfo.getResult_stadus() != null && cardInfo.getResult_stadus().equals("SUCCESS")) {
                        if (cardInfo.getResult_data() != null) {
                            switch (robotType) {
                                case 3:
                                    et_cardNum.setText(cardInfo.getResult_data().getC_CardNO());
                                    break;
                            }
                            isInfo = true;
                            cardNum = cardInfo.getResult_data().getC_CardNO();
                            cardName = cardInfo.getResult_data().getC_Name();
                            money1 = cardInfo.getResult_data().getN_AmountAvailable();
                            vipIntegral = parseFloat(cardInfo.getResult_data().getN_IntegralValue()) * 0.01f;
                            String jifen = cardInfo.getResult_data().getN_IntegralAvailable();
                            password = cardInfo.getResult_data().getC_Password();
                            tv_name.setText("会员姓名:" + cardName);
                            haveMoney = money1;
                            haveIntegral = jifen;
                            tv_m.setText("当前储值:￥" + money1);
                            tv_i.setText("当前积分:" + jifen);
                        }
                    } else {
                        et_cardNum.setText("");
                        Utils.showToast(QuickDelMActivity.this, cardInfo.getResult_errmsg());
                    }
                }
            }

            @Override
            public void OnFail(String message) {
                openRead();
            }
        });
    }

    @Override
    public void OnPaySucess(String orderNum, int payMode) {
        n = payMode;
        orderNo = orderNum;
        if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
            LKLPay(orderNum);
        } else {
            // sendDelMoney();
            LKLPay(orderNum);
            loadingDialog.show();
        }
    }

    private void LKLPay(String order) {
        Map<String, String> map = new HashMap<>();
        map.put("refernumber", order);
        map.put("orderNo", ordernb);
        map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
        switch (n) {
            case 1:
                map.put("payWeChat", money + "");
                break;
            case 2:
                map.put("payAli", money + "");
                break;
            case 3:
                map.put("payBank", money + "");
                break;
            case 10:

                break;

        }
        postToHttp(NetworkUrl.PAYQRCODE, map, new IHttpCallBack() {
                    @Override
                    public void OnSucess(String jsonText) {
                        try {
                            JSONObject object = new JSONObject(jsonText);
                            String tag = object.getString("result_stadus");
                            if (tag.equals("SUCCESS")) {
                                if (isVip) {
                                    VIPReset();
                                } else {
                                    noVIPReset();
                                }
                            } else {
                                showDBAlert();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String message) {
                        showDBAlert();
                    }
                }

        );
    }

    private boolean isNull() {
        if (TextUtils.isEmpty(et_money.getText().toString())) {
            isclick_pay = true;
            Utils.showToast(QuickDelMActivity.this, "请填写扣除金额");
            return false;
        } else if (parseFloat(et_money.getText().toString()) <= 0) {
            isclick_pay = true;
            Utils.showToast(QuickDelMActivity.this, "所扣金额应大于0元");
            return false;
        } else if (parseFloat(et_money.getText().toString()) > maxMoney && maxMoney != 0) {
            isclick_pay = true;
            Utils.showToast(QuickDelMActivity.this, "超出扣除最大范围，请修改扣除金额");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 计算
     */
    private void calculate() {
        dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//几多积分抵现一元

        dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
        dx_max = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_max"));//最大抵现几多
        Log.d("65535", "" + dx_jf + "^^^" + SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));
        String str = SharedUtil.getSharedData(QuickDelMActivity.this, "maxmoney");

        if (TextUtils.isEmpty(str)) {
            maxMoney = 0;
        } else if (str.equals("0")) {
            maxMoney = 999999;
        } else {
            maxMoney = parseFloat(str);
        }


        if (SharedUtil.getSharedBData(QuickDelMActivity.this, "quicki")) {
            shopi = 1;
        } else {
            shopi = 0;
        }
    }

    private void initViewM() {
        calculate();
        et_cardNum = (EditText) findViewById(R.id.et_card_num);
        if (SharedUtil.getSharedData(QuickDelMActivity.this, "isedit").equals("0")) {
            et_cardNum.setInputType(InputType.TYPE_NULL);
        }
        et_money = (EditText) findViewById(R.id.et_money);//消费金额
        InputFilter[] filters = {new MyTextFilter()};
        et_money.setFilters(filters);
        et_money.setFocusableInTouchMode(true);
        et_money.setFocusable(true);
        et_money.requestFocus();
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_money.getText().toString();
                float delm;
                if (TextUtils.isEmpty(str)) {
                    delm = 0;
                } else {
                    delm = parseFloat(str);
                }
                if (delm > maxMoney) {
                    et_money.setText(Utils.getNumStr(maxMoney));
                    et_money.setSelection(Utils.getNumStr(maxMoney).length());
                }
            }
        });
        Button btn_query = (Button) findViewById(R.id.btn_query1);
        tv_name = (TextView) findViewById(R.id.tv_exit_str0);
        tv_m = (TextView) findViewById(R.id.tv_exit_str2);
        tv_i = (TextView) findViewById(R.id.tv_exit_str4);
        //    TextView pay0 = (TextView) findViewById(R.id.tv_pay_0);
        //    TextView pay4 = (TextView) findViewById(R.id.tv_pay_4);
        db_isCheck = (AppCompatCheckBox) findViewById(R.id.cb_ischeck);

        if (Math.abs(dx_jf - 0.0) == 0) {//精度比较两个float是否相等
            db_isCheck.setVisibility(View.GONE);
        }
        ll_dx = (LinearLayout) findViewById(R.id.ll_dx);
        et_gread_dx = (EditText) findViewById(R.id.et_gread_dx);
        tv_money_dx = (TextView) findViewById(R.id.tv_money_dx);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        btn_change = (Button) findViewById(R.id.btn_change);
        et_gread_dx.setFilters(filters);
        if (SharedUtil.getSharedBData(QuickDelMActivity.this, "qdx")) {
            btn_change.setVisibility(View.GONE);
        } else {
            et_gread_dx.setInputType(InputType.TYPE_NULL);
            btn_change.setVisibility(View.VISIBLE);
        }
        db_isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isInfo) {
                    if (TextUtils.isEmpty(et_money.getText().toString())) {
                        db_isCheck.setChecked(false);
                        Utils.showToast(QuickDelMActivity.this, "请输入金额");
                    } else {
                        isCheck = isChecked;
                        if (isChecked) {
                            float del;
                            delMoney = et_money.getText().toString();
                            if (TextUtils.isEmpty(delMoney)) {
                                del = 0;
                            } else {
                                del = parseFloat(delMoney);
                            }

                            float jf = parseFloat(haveIntegral);
                            if (dx_max >= del * dx_mr) {//否超过默认金额
                                if (del * dx_mr * dx_jf <= jf) {//卡积分够
                                    et_gread_dx.setText(Utils.getNumStr(del * dx_mr * dx_jf));
                                    tv_money_dx.setText(Utils.getNumStr(del * dx_mr));
                                    tv_pay.setText(Utils.getNumStr(del - del * dx_mr));

                                } else {
                                    et_gread_dx.setText(haveIntegral);
                                    tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                    tv_pay.setText(Utils.getNumStr(del - jf / dx_jf));
                                }
                            } else {
                                if (dx_max * dx_mr * dx_jf <= jf) {//卡积分够
                                    et_gread_dx.setText(Utils.getNumStr(dx_max * dx_jf));
                                    tv_money_dx.setText(Utils.getNumStr(dx_max));
                                    tv_pay.setText(Utils.getNumStr(del - dx_max));
                                } else {
                                    et_gread_dx.setText(haveIntegral);
                                    tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                    tv_pay.setText(Utils.getNumStr(del - jf / dx_jf));
                                }
                            }
                            btn_change.setEnabled(true);
                            et_money.setFocusable(false);
                            et_money.setFocusableInTouchMode(false);
                            et_money.setInputType(InputType.TYPE_NULL);
                            ll_dx.setVisibility(View.VISIBLE);
                        } else {
                            dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//几多积分抵现一元
                            dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
                            dx_max = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_max"));//最大抵现几多
                            temporaryNum = "";
                            temName = "";
                            et_gread_dx.setText("0");
                            tv_money_dx.setText("0");
                            tv_pay.setText("0");
                            btn_change.setEnabled(false);
                            isTemporary = false;
                            et_gread_dx.setInputType(InputType.TYPE_NULL);
                            et_money.setFocusableInTouchMode(true);
                            et_money.setFocusable(true);
                            et_money.requestFocus();
                            et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            ll_dx.setVisibility(View.GONE);
                        }
                    }
                } else {
                    temporaryNum = "";
                    temName = "";
                    db_isCheck.setChecked(false);
                    isTemporary = false;
                    Utils.showToast(QuickDelMActivity.this, "请刷卡");
                }

            }
        });


        if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FX")) {
            pay_xj.setVisibility(View.GONE);
        }

        Log.d("10086", SharedUtil.getSharedBData(QuickDelMActivity.this, "FW") + "------" + SharedUtil.getSharedBData(QuickDelMActivity.this, "FA"));
        switch (robotType) {
            case 1:
                ll_w_a.setVisibility(View.GONE);
                if (SharedUtil.getSharedBData(QuickDelMActivity.this, "FW") && SharedUtil.getSharedBData(QuickDelMActivity.this, "FA")) {
                    pay_sm.setVisibility(View.VISIBLE);
                } else {
                    pay_sm.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FY")) {
                    pay_yl.setVisibility(View.GONE);
                }
                break;
            case 3:
            case 4:
                ll_lkl.setVisibility(View.GONE);
                if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FW")) {
                    pay_wx.setVisibility(View.GONE);
                }
                if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FA")) {
                    pay_zfb.setVisibility(View.GONE);
                }
                break;
            case 8:
                if (SharedUtil.getSharedBData(QuickDelMActivity.this, "pay_ment")) {//如果结果为true证明使用官方支付接口
                    ll_w_a.setVisibility(View.GONE);
                    if (SharedUtil.getSharedBData(QuickDelMActivity.this, "FW") && SharedUtil.getSharedBData(QuickDelMActivity.this, "FA")) {
                        pay_sm.setVisibility(View.VISIBLE);
                    } else {
                        pay_sm.setVisibility(View.GONE);
                    }
                    if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FY")) {
                        pay_yl.setVisibility(View.GONE);
                    }
                } else {
                    ll_lkl.setVisibility(View.GONE);
                    if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FW")) {
                        pay_wx.setVisibility(View.GONE);
                    }
                    if (!SharedUtil.getSharedBData(QuickDelMActivity.this, "FA")) {
                        pay_zfb.setVisibility(View.GONE);
                    }
                }
                break;
        }

        et_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isCheck == false) {
                    et_money.setText("");
                }
            }
        });

        et_gread_dx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String e = et_gread_dx.getText().toString();
                if (e.equals(0) || e.equals("0") || e.equals("0.")) {
                    et_gread_dx.setText("");
                }
                if (isTemporary || SharedUtil.getSharedBData(QuickDelMActivity.this, "qdx")) {
                    String str = et_gread_dx.getText().toString();
                    String str2 = et_money.getText().toString();
                    et_gread_dx.setSelection(str.length());
                    float delm;
                    float del_jf;
                    float have_jf = parseFloat(haveIntegral);
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
                    et_gread_dx.removeTextChangedListener(this);
                    if (i == 1) {
                        et_gread_dx.setText(str);
                    } else {
                        et_gread_dx.setText(del_jf + "");
                    }
                    et_gread_dx.addTextChangedListener(this);
                    et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    et_gread_dx.setSelection(et_gread_dx.getText().length());
                    tv_money_dx.setText(Utils.getNumStr(del_jf / dx_jf));
                    tv_pay.setText(Utils.getNumStr(delm - del_jf / dx_jf));

//                    if(del_jf/dx_jf>dx_max){
//                        if(del_jf>have_jf){
//                            et_gread_dx.removeTextChangedListener(this);
//                            et_gread_dx.setText(Utils.getNumStr(have_jf));
//                            et_gread_dx.addTextChangedListener(this);
//                            et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                            et_gread_dx.setSelection(et_gread_dx.getText().length());
//                            tv_money_dx.setText(Utils.getNumStr(have_jf * dx_jf));
//                            tv_pay.setText(Utils.getNumStr(delm - have_jf * dx_jf));
//                        }
//                    }

                    ///
//                    if (del_jf / dx_jf > delm) {//抵现金额大于消费金额
//                        if (del_jf > have_jf) {//抵现积分大于现有积分
//                            et_gread_dx.removeTextChangedListener(this);
//                            et_gread_dx.setText(Utils.getNumStr(have_jf));
//                            et_gread_dx.addTextChangedListener(this);
//                            et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                            et_gread_dx.setSelection(et_gread_dx.getText().length());
//                            tv_money_dx.setText(Utils.getNumStr(have_jf * dx_jf));
//                            tv_pay.setText(Utils.getNumStr(delm - have_jf * dx_jf));
//                        } else {
//                            et_gread_dx.removeTextChangedListener(this);
//                            et_gread_dx.setText(Utils.getNumStr(delm * dx_jf));
//                            et_gread_dx.addTextChangedListener(this);
//                            et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                            et_gread_dx.setSelection(et_gread_dx.getText().length());
//                            tv_money_dx.setText(str2);
//                            //  tv_pay.setText("0");
//                        }
//                    } else {
//                        if (del_jf > have_jf) {//抵现积分大于现有积分
//                            et_gread_dx.removeTextChangedListener(this);
//                            et_gread_dx.setText(Utils.getNumStr(have_jf));
//                            et_gread_dx.addTextChangedListener(this);
//                            et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                            et_gread_dx.setSelection(et_gread_dx.getText().length());
//                            tv_money_dx.setText(Utils.getNumStr(have_jf / dx_jf));
//                            tv_pay.setText(Utils.getNumStr(delm - have_jf / dx_jf));
//                        } else {
//                            et_gread_dx.removeTextChangedListener(this);
//                            et_gread_dx.setText(str);
//                            et_gread_dx.addTextChangedListener(this);
//                            et_gread_dx.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//                            et_gread_dx.setSelection(et_gread_dx.getText().length());
//                            tv_money_dx.setText(Utils.getNumStr(del_jf / dx_jf));
//                            tv_pay.setText(Utils.getNumStr(delm - del_jf / dx_jf));
//                        }
//                    }
                }
            }
        });
        //findViewById(R.id.btn_read_code).setOnClickListener(this);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardInfo("");
            }
        });
        getDelMoney();
    }

    private void sendDelMoney() {
        isclick_pay = false;
        delMoney = et_money.getText().toString();
        if (TextUtils.isEmpty(delMoney)) {
            delMoney = "0";
        }
        if (isCheck) {
            money = tv_pay.getText().toString();
        } else {
            money = delMoney;
        }
        if (isNull()) {
            haveMoney = money1;
            ordernb = orderNumber;
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderte = simpleFormat.format(date);
            float jf = parseFloat(money) * vipIntegral * shopIntegral * shopi;
            if (isInfo) {
                getIntegral = Utils.getNumStr(jf);
                if (n == 4) {
                    haveMoney = Utils.getNumStr(parseFloat(money1) - parseFloat(money));
                }
                delIntegral = et_gread_dx.getText().toString();//抵现的积分
                if (delIntegral.equals("")) {
                    delIntegral = "0";
                }
                gread = Utils.getNumStr(parseFloat(haveIntegral) + jf - parseFloat(delIntegral));
            }
            Map<String, String> map = new HashMap<>();
            map.put("costmoney", money);
            map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
            map.put("orderNo", orderNumber);
            map.put("EquipmentNum", terminalSn);
            map.put("CardCode", uid);
            map.put("c_Billfrom", robotType + "");
            map.put("Supplement", "0");
            switch (n) {
                case 0:
                    map.put("payCash", money + "");
                    break;
                case 1:
                    map.put("refernumber", orderNo);
                    map.put("payWeChat", money + "");
                    break;
                case 2:
                    map.put("refernumber", orderNo);
                    map.put("payAli", money + "");
                    break;
                case 3:
                    map.put("refernumber", orderNo);
                    map.put("payBank", money + "");
                    break;
                case 4:
                    map.put("payCard", money + "");
                    break;
            }
            dmoney = tv_money_dx.getText().toString();
            map.put("payIntegral", dmoney);//积分抵现的金额
            map.put("payDecIntegral", delIntegral);//抵现的积分
            map.put("userID", getSharedData(QuickDelMActivity.this, "userInfoId"));
            if (!TextUtils.isEmpty(et_cardNum.getText().toString()) && isInfo) {
                boolean isvipcard = true;
                if (n == 4 && parseFloat(money1) < parseFloat(money)) {
                    isvipcard = false;
                }
                if (isvipcard) {
                    isVip = true;
                    if (isInfo) {
                        map.put("CardNum", et_cardNum.getText().toString());
                        if (isTemporary) {
                            map.put("temporary_num", temporaryNum);
                            map.put("result_name", temName);
                        }
                        postToHttp(NetworkUrl.QUICK_M, map, new IHttpCallBack() {

                            @Override
                            public void OnSucess(String jsonText) {
                                Logger.e(jsonText);
                                try {
                                    JSONObject object1 = new JSONObject(jsonText);
                                    String tag = object1.getString("result_stadus");
                                    if (tag.equals("SUCCESS")) {
                                        if (robotType_pay(n)) {
                                            payMoney(n, payau, orderNumber, "商品消费");
                                        } else if ((robotType != 1 && n == 1) || (robotType != 1 && n == 2)) {
                                            payMoney(n, payau + "", orderNumber, "商品消费");
                                        } else {
                                            VIPReset();
                                        }
                                    } else if (tag.equals("ERR")) {
                                        isclick_pay = true;
                                        String msg = object1.getString("result_errmsg");
                                        Utils.showToast(QuickDelMActivity.this, msg);
                                    } else {
                                        String msg = object1.getString("result_errmsg");
                                        Utils.showToast(QuickDelMActivity.this, msg);
                                        openRead();
                                        if (robotType != 1 || (n != 1 && n != 3)) {
                                            showDBAlert();
                                        } else {
                                            isclick_pay = true;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Logger.e(jsonText + "!!!!!!" + "异常");
                                }

                            }

                            @Override
                            public void OnFail(String message) {
                                Logger.e(message + "!!!!!!失败");
                                openRead();
                                if (robotType != 1 || (n != 1 && n != 3)) {
                                    showDBAlert();
                                } else {
                                    isclick_pay = true;
                                }
                            }
                        });
                    } else {
                        isclick_pay = true;
                        Utils.showToast(QuickDelMActivity.this, "请先获取卡内信息");
                    }
                } else {
                    isclick_pay = true;
                    Utils.showToast(QuickDelMActivity.this, "卡内余额不足，无法支付");
                }
            } else {
                isVip = false;
                getIntegral = "0";
                postToHttp(NetworkUrl.NOVIPQUICK, map, new IHttpCallBack() {
                    @Override
                    public void OnSucess(String jsonText) {
                        isclick_pay = false;
                        try {
                            JSONObject object = new JSONObject(jsonText);
                            String tag = object.getString("result_stadus");
                            if (tag.equals("SUCCESS")) {
                                if (((robotType == 1 || robotType == 8) && n == 1) || ((robotType == 1 || robotType == 8) && n == 3)) {
                                    payMoney(n, payau, orderNumber, "商品消费");
                                } else if ((robotType != 1 && n == 1) || (robotType != 1 && n == 2)) {
                                    payMoney(n, payau + "", orderNumber, "商品消费");
                                } else {
                                    noVIPReset();
                                }
                            } else {
                                Utils.showToast(QuickDelMActivity.this, "支付失败,请重试");
                                if (robotType != 1 || (n != 1 && n != 3)) {
                                    showDBAlert();
                                } else {
                                    isclick_pay = true;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void OnFail(String message) {
                        if (robotType != 1 || (n != 1 && n != 3)) {
                            showDBAlert();
                        } else {
                            isclick_pay = true;
                        }
                    }
                });
            }
        }
    }

    private void noVIPReset() {
        uid = "";
        cardNum = "散客";
        haveMoney = "0";
        haveIntegral = "0";
        getIntegral = "0";
        orderNo = "";
        flag = true;
        et_cardNum.setText("");
        et_money.setText("");
        et_money.setFocusableInTouchMode(true);
        et_money.setFocusable(true);
        et_money.requestFocus();


        tv_name.setText("会员姓名:");
        tv_m.setText("当前储值:");
        tv_i.setText("当前积分:");
        password = "";
        isInfo = false;
        printInfo(true);
        reset();
    }

    private void VIPReset() {
        uid = "";
        flag = true;
        preID = cardNum;
        password = "";
        money1 = "0";
        et_cardNum.setText("");
        et_money.setText("");
        et_money.setFocusableInTouchMode(true);
        et_money.setFocusable(true);
        et_money.requestFocus();
        tv_name.setText("会员姓名:");
        tv_m.setText("当前储值:");
        tv_i.setText("当前积分:");
        isInfo = false;
        printInfo(true);
        db_isCheck.setChecked(false);
        isCheck = false;
        ll_dx.setVisibility(View.GONE);
        btn_change.setEnabled(false);
        et_gread_dx.setText("0");
        tv_money_dx.setText("0");
        tv_pay.setText("0");
        et_gread_dx.setInputType(InputType.TYPE_NULL);
        et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        reset();
    }

    private void reset() {
        dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//几多积分抵现一元
        dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
        dx_max = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_max"));//最大抵现几多
        switch (robotType) {
            case 3:
            case 4:
                gread = "0";
                cardNum = "";
                cardName = "";
                money = "";
                haveMoney = "0";
                delMoney = "0";
                haveIntegral = "0";
                getIntegral = "0";
                orderNo = "";
                isInfo = false;
                if (flag) {
                    showAlert();
                    getOrderNum("KM");
                } else {
                    finish();
                }
                openRead();
                isclick_pay = true;
                break;
        }
    }

    private synchronized void printInfo(boolean flag) {
        printCount = 0;
        List<String> list = new ArrayList<>();
        list.add((flag ? "订单时间" : "订单生成时间") + ":" + orderte);
        list.add("单据号:" + ordernb);
        if (isVip) {
            list.add("会员卡号:" + cardNum);
            list.add("会员姓名:" + cardName);
        } else {
            list.add("会员卡号:散客");
            list.add("会员姓名:散客");
        }
        switch (n) {
            case 0:
                list.add("支付方式:现金支付");
                break;
            case 1:
                list.add("支付方式:微信支付");
                break;
            case 2:
                list.add("支付方式:支付宝支付");
                break;
            case 3:
                list.add("支付方式:银联支付");
                break;
            case 4:
                list.add("支付方式:会员卡支付");
                break;
        }
        list.add("消费金额:" + delMoney);
        if (isVip) {
            list.add("实收金额:" + money);
            if (isTemporary) {
                list.add("授权工号:" + temporaryNum);
            }
            list.add("抵现积分:" + delIntegral);
            list.add("获得积分:" + getIntegral);
            if (flag) {
                list.add("可用储值:￥" + haveMoney);
                list.add("可用积分:" + gread);
            }
        }

        if (flag) {
            printPage("快速收银小票", list, null, true);
        } else {
            printPage("快速收银延时扣款小票", list, null, true);
            printPage("快速收银延时扣款小票(商户存根)", list, null, true);
        }
    }

    private void setDBData() {
        Connector.getDatabase();
        QuickDelMoney upLoading = new QuickDelMoney();
        upLoading.setUrl(NetworkUrl.QUICK_M);
        upLoading.setUrl1(NetworkUrl.NOVIPQUICK);
        upLoading.setVip(isVip);
        upLoading.setUid(uid);
        upLoading.setIsTem(isTemporary);
        upLoading.setTem(temporaryNum);
        upLoading.setTemName(temName);
        upLoading.setDelIntegral(delIntegral);
        upLoading.setDelMoney(dmoney);
        upLoading.setCardNum(cardNum);

        upLoading.setCardName(cardName);
        upLoading.setHaveMoney(haveMoney);
        upLoading.setHaveIntegral(gread);
        upLoading.setGetIntegral(getIntegral);
        upLoading.setDbName(getSharedData(QuickDelMActivity.this, "dbname"));
        upLoading.setUser_Id(getSharedData(QuickDelMActivity.this, "userInfoId"));
        upLoading.setMoney(money);
        upLoading.setRefernumber(orderNo);
        upLoading.setEquipmentNum(terminalSn);
        upLoading.setN(n);
        upLoading.setOrderNo(ordernb);
        upLoading.setOrderTime(orderte);
        upLoading.setUserName(getSharedData(QuickDelMActivity.this, "username"));
        upLoading.setShopName(getSharedData(QuickDelMActivity.this, "shopname"));
        if (isVip) {
            if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber) && !TextUtils.isEmpty(money)) {
                Logger.d(upLoading.save() + "");
            }
        } else {
            if (!TextUtils.isEmpty(orderNumber) && !TextUtils.isEmpty(money)) {
                Logger.d(upLoading.save() + "非会员");
            }
        }

    }

    private boolean alert_flag = true;

    private void showDBAlert() {
        isclick_pay = false;
        flag = false;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(QuickDelMActivity.this);
        dialog.setTitle(R.string.dialog_warnning_name);
        dialog.setMessage(R.string.dialog_warnning_message);
        dialog.setPositiveButton(R.string.dialog_warnning_yes, null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (alert_flag) {
                    if (isVip) {
                        if (!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(orderNumber) && !TextUtils.isEmpty(money)) {
                            setDBData();
                            printInfo(false);
                            switch (robotType) {
                                case 3:
                                    reset();
                                    break;
                            }
                        } else {
                            reset();
                        }
                    } else {
                        if (!TextUtils.isEmpty(orderNumber) && !TextUtils.isEmpty(money)) {
                            setDBData();
                            printInfo(false);
                            switch (robotType) {
                                case 3:
                                    reset();
                                    break;
                            }
                        } else {
                            reset();
                        }
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
                    LKLPay(orderNo);
                } else {
                    sendDelMoney();
                }
            }
        });
        dialog.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }

    @Override
    public void OnReadQrcode(String number) {
        et_cardNum.setText(number);
        getCardInfo(number);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.ll_quick_money_root);
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
        gread = "0";
        cardNum = "";
        cardName = "";
        money = "";
        delMoney = "";
        haveMoney = "0";
        haveIntegral = "0";
        getIntegral = "0";
        isInfo = false;
        temporaryNum = "";
        temName = "";
        isTemporary = false;
        if (flag) {
            showAlert();
            getOrderNum("KM");
        } else {
            if (printCount >= 2) {
                finish();
            }
        }
        openRead();
        dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//几多积分抵现一元
        dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率
        dx_max = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_max"));//最大抵现几多
        isclick_pay = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void OnCreateOrderNumber(boolean isFail) {
        getOrderNum("KM");
        if (isFail) {
            Map<String, String> map = new HashMap<>();
            map.put("orderNo", ordernb);
            map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
            postToHttp(NetworkUrl.DELPAYQRCODE, map, null);
        }
    }
}
