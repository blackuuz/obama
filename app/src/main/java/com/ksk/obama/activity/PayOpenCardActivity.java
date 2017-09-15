package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.DB.OrderNumber;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.OpenCardInfo;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ksk.obama.utils.SharedUtil.getSharedData;
import static org.litepal.tablemanager.Connector.getDatabase;

public class PayOpenCardActivity extends BasePrintActivity implements View.OnClickListener, IPayCallBack,
        IPrintSuccessCallback, IPrintErrorCallback {


    private int n = -1;
    private OpenCardInfo cardInfo;
    private String orderno = "";
    private TextView tv_print;
    private String orderTime = "";
    private boolean flag = false;
    private boolean isPay = false;
    private String actualMoney = "";
    private boolean isTemporary = false;
    private String temName = "";
    private String temporaryNum = "";

    boolean XJ, WX, AL, TR;

    private Unbinder unbinder;

    @BindView(R.id.tv_pay_xj)
    TextView tvPayXj;
    @BindView(R.id.tv_pay_dsf)
    TextView tvPayDsf;

    @BindView(R.id.tv_pay_wx)
    TextView tvPayWx;
    @BindView(R.id.tv_pay_zfb)
    TextView tvPayZfb;

    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.ll_w_a)
    LinearLayout llWA;

    @BindView(R.id.tv_pay_open_money)
    TextView tv_money;
    @BindView(R.id.btn_sjsk)
    Button btn_sjsk;
    @BindView(R.id.et_pay)
    EditText et_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        this.setOnPayCallBack(this);
        this.setOnPrintSuccess(this);
        this.setOnPrintError(this);
        unbinder = ButterKnife.bind(this);
        getOrderNum("KK");
        initTitale();
        initView();
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("info");
            tv_money.setText("应收款:" + cardInfo.getPaymoney() + "元");
            et_pay.setText(cardInfo.getPaymoney());
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
        XJ = SharedUtil.getSharedBData(PayOpenCardActivity.this, "OX");
        WX = SharedUtil.getSharedBData(PayOpenCardActivity.this, "OW");
        AL = SharedUtil.getSharedBData(PayOpenCardActivity.this, "OA");
        TR = SharedUtil.getSharedBData(PayOpenCardActivity.this, "OT");
        InputFilter[] filters = {new MyTextFilter()};
        et_pay.setFilters(filters);
        if (SharedUtil.getSharedBData(PayOpenCardActivity.this, "opencard")) {
            btn_sjsk.setVisibility(View.GONE);
            et_pay.setFocusableInTouchMode(true);
            et_pay.setFocusable(true);
            et_pay.requestFocus();
            et_pay.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (!XJ) {
            tvPayXj.setVisibility(View.GONE);//如果没有现金权限隐藏现金支付
        }
        if (!TR) {
            tvPayDsf.setVisibility(View.GONE); //如果没有第三方权限隐藏第三方支付
        }
        if (!XJ && !TR) {
            llTop.setVisibility(View.GONE); //现金第三方都没有隐藏布局
        }
        if (robotType == 4) {
            tvPayDsf.setVisibility(View.GONE); //如果机型是手机隐藏第三方支付
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
        tvPayXj.setOnClickListener(this);
        tvPayDsf.setOnClickListener(this);
        tvPayZfb.setOnClickListener(this);
        tvPayWx.setOnClickListener(this);
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
        map.put("payActual", actualMoney);//实际付款
        map.put("payShould", cardInfo.getPayshould());//应付款
        map.put("orderNo", orderNumber);
        map.put("CardCode", cardInfo.getUid());
        map.put("IDCard", cardInfo.getIdCardNumber());
        map.put("car",cardInfo.getCarNum());
        map.put("add",cardInfo.getAddress());
        map.put("Remark",cardInfo.getRemarks());
        map.put("c_Billfrom", robotType + "");
        if (isTemporary) {
            map.put("temporary_num", temporaryNum);
            map.put("result_name", temName);
        }
        switch (n) {
            case 0:
                map.put("payCash", actualMoney);
                break;
            case 1:
                map.put("payWeChat", actualMoney);
                map.put("refernumber", orderno);
                break;
            case 2:
                map.put("payAli", actualMoney);
                map.put("refernumber", orderno);
                break;
            case 3:
                map.put("payThird", actualMoney);
                map.put("refernumber", orderno);
                break;

        }
        map.put("EquipmentNum", terminalSn);
        map.put("C_ServiceEmployee", cardInfo.getAddId());//推荐员工的名字
        map.put("memid", cardInfo.getVipAddId());//
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
                // TODO: 2017/6/27 付款成功后隐藏支付方式
                llTop.setVisibility(View.GONE);
                llWA.setVisibility(View.GONE);
                String msg = object.getString("result_msg");
                Utils.showToast(PayOpenCardActivity.this, msg);
                payHint(true);
                printInfo(true);
                setOrderDB();
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
            actualMoney = et_pay.getText().toString();
            if (actualMoney.equals("")) {
                actualMoney = "0";
            }
            switch (v.getId()) {
                case R.id.tv_pay_xj:
                    n = 0;
                    sendData();
                    break;
                case R.id.tv_pay_dsf:
                    isclick_pay = true;
                    n = 3;
                    payMoney(3, actualMoney, orderNumber, "开卡");
                    break;
                case R.id.tv_pay_wx:
                    n = 1;
                    payMoney(1, actualMoney, orderNumber, "开卡");
                    break;
                case R.id.tv_pay_zfb:
                    n = 2;
                    payMoney(2, actualMoney, orderNumber, "开卡");
                    break;
            }
        }
    }
    private void setOrderDB() {
        getDatabase();
        OrderNumber upLoading = new OrderNumber();
        upLoading.setOrderNumber(orderNumber);
        upLoading.setGroupId(getSharedData(PayOpenCardActivity.this, "groupid"));
        upLoading.setDbName(getSharedData(PayOpenCardActivity.this, "dbname"));
        upLoading.setCardNum(cardInfo.getCardnum());
        upLoading.setCardName(cardInfo.getName());
        upLoading.setSex(cardInfo.getSex());
        upLoading.setMobile(cardInfo.getPhone());
        upLoading.setBirthday(cardInfo.getBirthday());
        upLoading.setGoodsId(cardInfo.getGoodsId());
        upLoading.setMoney(actualMoney);
        upLoading.setShouldMoney(cardInfo.getPayshould());
        upLoading.setCardCode(cardInfo.getUid());
        upLoading.setIDCard(cardInfo.getIdCardNumber());
        upLoading.setCar(cardInfo.getCarNum());
        upLoading.setAddress(cardInfo.getAddress());
        upLoading.setRemarks(cardInfo.getRemarks());
        upLoading.setTemporary(isTemporary);
        upLoading.setTemporary_num(temporaryNum);
        upLoading.setResult_name(temName);
        upLoading.setPayType(n);
        upLoading.setRecommendEmpoyee(cardInfo.getAddId());
        upLoading.setMemberid(cardInfo.getVipAddId());//推荐会员
        upLoading.setPassword(cardInfo.getPassword());
        //upLoading.setGforder(gforder);
        upLoading.setUserId(getSharedData(PayOpenCardActivity.this, "userInfoId"));
        upLoading.setTime(orderTime);
        upLoading.setFormClazz("KK");
        upLoading.save();
    }

    private void setDBData() {
        getDatabase();
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
        openCardDb.setPayActual(actualMoney);
        openCardDb.setPayShould(cardInfo.getPayshould());
        openCardDb.setTem(isTemporary);
        openCardDb.setTemporaryNum(temporaryNum);
        openCardDb.setTemName(temName);

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
        listp.add("责任员工:" + cardInfo.getAddPerson());
        listp.add("推荐会员:" + cardInfo.getVipAddPerson());
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
            listp.add("金额:0");
        } else {
            listp.add("金额:" + Utils.getNumStr(Float.parseFloat(str2)));
        }
        if (actualMoney.equals("")) {
            listp.add("实收金额:0");
        } else {
            listp.add("实收金额:" + Utils.getNumStr(Float.parseFloat(actualMoney)));
        }


        if (isTemporary) {
            listp.add("授权工号:" + temporaryNum);
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
                listp.add("支付方式:第三方支付");
                break;
        }
        if (cardInfo.getPhone().length() == 11) {
            listp.add("会员手机号:" + cardInfo.getPhone());
        } else {
            listp.add("会员手机号:无");
        }
        if (cardInfo.getIdCardNumber().length() == 18) {
            listp.add("会员身份证号:" + cardInfo.getIdCardNumber());
        } else {
            listp.add("会员身份证号:无");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


    @OnClick(R.id.btn_sjsk)
    public void sjsk(View view) {
        Arrived();
    }


    private void Arrived() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(PayOpenCardActivity.this).inflate(R.layout.pop_temporary, null);
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
                    Utils.showToast(PayOpenCardActivity.this, "请输入工号");
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
        window.showAtLocation(findViewById(R.id.activity_pay), Gravity.CENTER, 0, 0);
    }

    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(PayOpenCardActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    int ocard = s.indexOf("POS:开卡实收金额修改");
                    if (ocard > 0) {
                        Utils.showToast(PayOpenCardActivity.this, "临时授权成功");
                        temName = j.getString("result_name");
                        temporaryNum = num;
                        isTemporary = true;
                        et_pay.setFocusableInTouchMode(true);
                        et_pay.setFocusable(true);
                        et_pay.requestFocus();
                        et_pay.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        et_pay.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    } else {
                        Utils.showToast(PayOpenCardActivity.this, "您没有权限");
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
