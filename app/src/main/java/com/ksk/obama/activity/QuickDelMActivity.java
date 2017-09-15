package com.ksk.obama.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksk.obama.DB.OrderNumber;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.R;
import com.ksk.obama.adapter.QuickDelMAdapter;
import com.ksk.obama.callback.ICreateOrderNumber;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPayCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.CardInfo;
import com.ksk.obama.model.QuickDelM;
import com.ksk.obama.utils.MyTextFilter;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
import static java.lang.Float.parseFloat;
import static org.litepal.tablemanager.Connector.getDatabase;

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
    private ListView lv_quick;

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
   // private String gforder = "";//官方订单号
    private boolean isvipcard = true;
    //private boolean isQrSure = false;//二维码支付确认
    private List<CardInfo.ResultDataBean> c_data = new ArrayList<>();
    private List<QuickDelM.FastListBean> q_data = new ArrayList<>();//快速消费快速选择集合

    private Unbinder unbinder;
    private boolean XJ, WX, AL, TR;
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

    @BindView(R.id.brn_quickm_select)
    Button quickm_select;

    private QuickDelMAdapter adapter;
    private int dFlag = -1;
    private String quotaMoney = "";//定额
    private float min_money = 0f;

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
        if (getSharedBData(QuickDelMActivity.this, "citiao")) {
            str += "磁条卡,";
        }
        if (getSharedBData(QuickDelMActivity.this, "nfc")) {
            str += "M1卡,";
        }
        if (getSharedBData(QuickDelMActivity.this, "saoma")) {
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
        if (SharedUtil.getSharedData(QuickDelMActivity.this, "min_money").equals("")) {
            min_money = 0f;
        } else {
            min_money = Float.parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "min_money"));
        }
    }

    private int queryDb(boolean flag) {
        getDatabase();
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

    /**
     * @param v
     */
    @OnClick({R.id.btn_read_code, R.id.btn_change, R.id.tv_pay_xj, R.id.tv_pay_hy, R.id.tv_pay_wx, R.id.tv_pay_zfb, R.id.tv_pay_dsf})
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
            case R.id.tv_pay_dsf://第三方
                getOrderNum("KM");//后加
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
                                n = 3;
                                sendDelMoney();
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        n = 3;
                        sendDelMoney();
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
                getOrderNum("KM");//后加
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
                                    sendDelMoney();
                                }
                            }
                        });
                        builder.create().show();
                        isclick_pay = true;
                    } else {
                        if (isclick_pay) {
                            n = 1;
                            sendDelMoney();
                        }
                    }
                }
                break;

            case R.id.tv_pay_zfb://支付宝
                getOrderNum("KM");//
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

//    private void showCodeSure() {
//        final PopupWindow window = new PopupWindow();
//        View contentView = LayoutInflater.from(QuickDelM.this).inflate(R.layout.qr_pay_dialog, null);
//        final TextView textView = (TextView) contentView.findViewById(R.id.qr_pay_money);
//        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_qr);
//        final Button sure = (Button) contentView.findViewById(R.id.btn_sure_qr);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isclick_pay = true;
//                window.dismiss();
//
//            }
//        });
//        textView.setText(payau);
//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isclick_pay = false;
//                n = 1;
//                sendDelMoney();
//                // payMoney(1, payau, orderNumber, "商品消费");
//                window.dismiss();
//            }
//        });
//
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                isclick_pay = true;
//
//            }
//        });
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int w = dm.widthPixels;
//        int h = dm.heightPixels;
//        window.setContentView(contentView);
//        window.setWidth((int) (w * 0.8));
//        window.setHeight((int) (h * 0.4));
//        window.setFocusable(true);
//        window.setOutsideTouchable(false);
//        window.update();
//        ColorDrawable dw = new ColorDrawable();
//        window.setBackgroundDrawable(dw);
//        window.showAtLocation(findViewById(R.id.ll_quick_money_root), Gravity.CENTER, 0, 0);
//
//    }
//

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
                Logger.e(jsonText);
                QuickDelM quick = new Gson().fromJson(jsonText, QuickDelM.class);
                String money = "";
                if (quick.getResult_stadus().equals("SUCCESS")) {
                    if (quick.getSetDefaultCost() != null) {
                        // TODO: 2017/8/1 要进行非空判断
                        if (quick.getSetDefaultCost().equals("定额")) {
                            dFlag = 1;
                            money = quick.getDefaultcost() + "";
                            quotaMoney = quick.getDefaultcost() + "";
                        } else if (quick.getSetDefaultCost().equals("上次")) {
                            dFlag = 2;

                        } else if (quick.getSetDefaultCost().equals("0")) {
                            dFlag = 0;
                        }
                    } else {
                        money = quick.getDefaultcost() + "";
                    }
                    if (quick.getFast_state() == null || !quick.getFast_state().equals("1")) { //快速消费状态 如果不等于“1” 隐藏按钮
                        Log.d("uuz", "消费状态 ：" + quick.getFast_state());
                        quickm_select.setVisibility(View.GONE);
                    } else {
                        quickm_select.setVisibility(View.VISIBLE);
                        q_data = quick.getFast_list();
                        adapter = new QuickDelMAdapter(QuickDelMActivity.this, q_data);
                    }
                } else {
                    money = "";
                    quickm_select.setVisibility(View.GONE);
                    adapter = new QuickDelMAdapter(QuickDelMActivity.this, q_data);
                }
                et_money.setText(money);
                openRead();
            }

            @Override
            public void OnFail(String message) {
                quickm_select.setVisibility(View.GONE);
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
        map.put("shopID", shopId);
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                openRead();
                CardInfo cardInfo = new Gson().fromJson(jsonText, CardInfo.class);
                if (cardInfo != null) {
                    if (cardInfo.getResult_stadus() != null && cardInfo.getResult_stadus().equals("SUCCESS")) {
                        int card_dnum = 0;
                        try {
                            card_dnum = Integer.parseInt(cardInfo.getResult_datasNum());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.e("uuz", " String转int 异常 ");
                        }
                        if (card_dnum > 1) {
                            dialog_(cardInfo);
                        } else {
                            ToActivity(cardInfo.getResult_data());
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

    private int yourChoice;

    /**
     * 这是一个单项选择弹窗
     */
    private void showSingleChoiceDialog() {
        final String[] items = card__;
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(QuickDelMActivity.this);
        singleChoiceDialog.setTitle("\t\t请选择要使用的会员卡");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;

                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice == -1) {
                            Toast.makeText(QuickDelMActivity.this, "你选择了" + items[0], Toast.LENGTH_SHORT).show();
                            ToActivity(c_data.get(0));
                            c_data.clear();
                        }

                        if (yourChoice != -1) {
                            Toast.makeText(QuickDelMActivity.this, "你选择了" + items[yourChoice], Toast.LENGTH_SHORT).show();
                            ToActivity(c_data.get(yourChoice));
                            c_data.clear();
                        }
                    }
                });
        singleChoiceDialog.show();
    }


    private String card__[] = null;

    private void dialog_(CardInfo readCard) {
        int card_dnum = 0;
        try {
            card_dnum = Integer.parseInt(readCard.getResult_datasNum());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e("uuz", " String转int 异常 ");
        }
        if (card_dnum > 1) {
            String cardNo[] = new String[card_dnum];
            String cardName[] = new String[card_dnum];
            String cName = "";
            String length = "             ";
            card__ = new String[card_dnum];
            c_data = readCard.getResult_datas();
            for (int i = 0; i < card_dnum; i++) {
                cardNo[i] = readCard.getResult_datas().get(i).getC_CardNO();
                cardName[i] = readCard.getResult_datas().get(i).getC_Name();
                if (cardName[i].length() > 4) {
                    cName = cardName[i].substring(0, 4) + "… :";
                } else if (cardName[i].length() == 4) {
                    cName = cardName[i] + "  :";
                } else {
                    cName = cardName[i] + length.substring(0, (4 - cardName[i].length())) + "  :";
                }
                card__[i] = cName + "  " + cardNo[i];
            }
            showSingleChoiceDialog();
        }
    }


    private void ToActivity(CardInfo.ResultDataBean resultData) {
        if (resultData != null) {
//            switch (robotType) {
//                case 3:
//                    et_cardNum.setText(resultData.getC_CardNO());
//                    break;
//            }
            isInfo = true;
            cardNum = resultData.getC_CardNO();
            cardName = resultData.getC_Name();
            money1 = resultData.getN_AmountAvailable();
            vipIntegral = parseFloat(resultData.getN_IntegralValue()) * 0.01f;
            String jifen = resultData.getN_IntegralAvailable();
            password = resultData.getC_Password();
            tv_name.setText("会员姓名:" + cardName);
            haveMoney = money1;
            haveIntegral = jifen;
            tv_m.setText("当前储值:￥" + money1);
            tv_i.setText("当前积分:" + jifen);
        }


    }

    @Override
    public void OnPaySucess(String orderNum, int payMode) {
        n = payMode;
        orderNo = orderNum;
        // if ((robotType == 1 && n == 1) || (robotType == 1 && n == 2) || (robotType == 1 && n == 3)) {
        LKLPay(orderNum);
        //} else {
        // sendDelMoney();
        //  LKLPay(orderNum);
        loadingDialog.show();
        //}
    }

    private void LKLPay(String order) {
        Map<String, String> map = new HashMap<>();
        map.put("refernumber", order);
        map.put("orderNo", ordernb);
        map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));
       // gforder = order;
        switch (n) {
            case 1:
                map.put("payWeChat", money + "");
                break;
            case 2:
                map.put("payAli", money + "");
                break;
            case 3:
                map.put("payThird", money + "");
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
        dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//多少积分抵现一元
        dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率  默认抵现当前金额的多少
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


        if (getSharedBData(QuickDelMActivity.this, "quicki")) {
            shopi = 1;
        } else {
            shopi = 0;
        }
    }

    private void initViewM() {
        XJ = SharedUtil.getSharedBData(QuickDelMActivity.this, "FX");
        WX = SharedUtil.getSharedBData(QuickDelMActivity.this, "FW");
        AL = SharedUtil.getSharedBData(QuickDelMActivity.this, "FA");
        TR = SharedUtil.getSharedBData(QuickDelMActivity.this, "FT");
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
                            if (dx_max >= del * dx_mr) {//抵现的积分没超过默认金额
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
                                if (dx_max * dx_jf <= jf) {//卡积分够
                                    et_gread_dx.setText(Utils.getNumStr(dx_max * dx_jf));
                                    tv_money_dx.setText(Utils.getNumStr(dx_max));
                                    tv_pay.setText(Utils.getNumStr(del - dx_max));
                                } else {
                                    et_gread_dx.setText(haveIntegral);
                                    tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                    tv_pay.setText(Utils.getNumStr(del - jf / dx_jf));
                                }
                                if (dx_max == 0.0f) {//不限积分抵现额度
                                    if (del * dx_mr * dx_jf <= jf) {//卡积分够
                                        et_gread_dx.setText(Utils.getNumStr(del * dx_mr * dx_jf));
                                        tv_money_dx.setText(Utils.getNumStr(del * dx_mr));
                                        tv_pay.setText(Utils.getNumStr(del - del * dx_mr));
                                    } else {
                                        et_gread_dx.setText(haveIntegral);
                                        tv_money_dx.setText(Utils.getNumStr(jf / dx_jf));
                                        tv_pay.setText(Utils.getNumStr(del - jf / dx_jf));
                                    }
                                }
                            }
                            btn_change.setEnabled(true);
                            et_money.setFocusable(false);
                            et_money.setFocusableInTouchMode(false);
                            et_money.setInputType(InputType.TYPE_NULL);
                            ll_dx.setVisibility(View.VISIBLE);
                        } else {
                            dx_jf = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_jf"));//多少积分抵现一元
                            dx_mr = parseFloat(SharedUtil.getSharedData(QuickDelMActivity.this, "dx_mr")) * 0.01f;//默认抵现倍率  默认抵现当前金额的多少
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

        et_gread_dx.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et_gread_dx.setText("");
                }
                return false;
            }
        });

        et_money.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et_money.setText("");
                }
                return false;
            }
        });
//        et_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus && isCheck == false) {
//                    et_money.setText("");
//                }
//            }
//        });

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
                if (e.equals(0) || e.equals("0") || e.equals("0.") || e.equals(".0")) {
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
                }
            }
        });

        quickm_select.setOnClickListener(new View.OnClickListener() {//快速消费列表
            @Override
            public void onClick(View v) {
                if (!db_isCheck.isChecked()) {
                    new PopupWindows(QuickDelMActivity.this, findViewById(R.id.ll_quick_money_root));
                }


            }
        });


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
                    map.put("payCash", money);
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
                    map.put("payThird", money + "");
                    break;
                case 4:
                    map.put("payCard", money + "");
                    break;
            }
            if(tv_money_dx == null){
                dmoney = "0";
            }else {
                dmoney = tv_money_dx.getText().toString();
            }
            map.put("payIntegral", dmoney);//积分抵现的金额
            map.put("payDecIntegral", delIntegral);//抵现的积分
            map.put("userID", getSharedData(QuickDelMActivity.this, "userInfoId"));
            if (!TextUtils.isEmpty(et_cardNum.getText().toString()) && isInfo) {

                if (n == 4 && ((parseFloat(money1) + min_money) < parseFloat(money))) {
                    isvipcard = false;
                }else {
                    isvipcard  =true;
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
                                        if (n == 0 || n == 4) {
                                            VIPReset();
                                        } else {
                                            payMoney(n, payau, orderNumber, "商品消费");
                                        }
                                    } else if (tag.equals("ERR")) {
                                        isclick_pay = true;
                                        String msg = object1.getString("result_errmsg");
                                        Utils.showToast(QuickDelMActivity.this, msg);
                                    } else {
                                        String msg = object1.getString("result_errmsg");
                                        Utils.showToast(QuickDelMActivity.this, msg);
                                        openRead();
                                        if (n == 0 || n == 4) {
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
                                if (n == 0 || n == 4) {
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
                                if (n == 0 || n == 4) {
                                    noVIPReset();
                                } else {
                                    payMoney(n, payau, orderNumber, "快速消费");
                                }
                            } else {
                                Utils.showToast(QuickDelMActivity.this, "支付失败,请重试");
                                if (n == 0 || n == 4) {
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
                        if (n == 0 || n == 4) {
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
        setOrderDB();
        uid = "";
        cardNum = "散客";
        haveMoney = "0";
        haveIntegral = "0";
        getIntegral = "0";
        orderNo = "";
        flag = true;
        et_cardNum.setText("");
        switch (dFlag) {
            case 0://关闭
                et_money.setText("");
                break;
            case 1://定额
                et_money.setText(quotaMoney);
                break;
            case 2://上次
                break;
            default:
                et_money.setText("");
                break;
        }
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

    private void setOrderDB() {
        getDatabase();
        OrderNumber upLoading = new OrderNumber();
        upLoading.setOrderNumber(ordernb);
        upLoading.setGroupId(SharedUtil.getSharedData(QuickDelMActivity.this, "groupid"));
        upLoading.setDbName(getSharedData(QuickDelMActivity.this, "dbname"));
        upLoading.setCardCode(uid);
        upLoading.setPayType(n);
        upLoading.setMoney(money);
        upLoading.setRefernumber(orderNo);
        //upLoading.setGforder(gforder);
        upLoading.setDelMoney(dmoney);
        upLoading.setPayDecIntegral(delIntegral);
        upLoading.setUserId(getSharedData(QuickDelMActivity.this, "userInfoId"));
        upLoading.setVip(isVip);
        upLoading.setCardNum(et_cardNum.getText().toString());
        upLoading.setTemporary_num(temporaryNum);
        upLoading.setResult_name(temName);
        upLoading.setTemporary(isTemporary);
        upLoading.setTime(orderte);
        upLoading.setFormClazz("KM");
        upLoading.save();
    }


    private void VIPReset() {
        setOrderDB();
        uid = "";
        flag = true;
        preID = cardNum;
        password = "";
        money1 = "0";
        et_cardNum.setText("");
        switch (dFlag) {
            case 0://关闭
                et_money.setText("");
                break;
            case 1://定额
                et_money.setText(quotaMoney);
                break;
            case 2://上次
                break;
        }

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
        if (dx_jf != 0) {
            et_gread_dx.setText("0");
            et_gread_dx.setInputType(InputType.TYPE_NULL);
        }
        tv_money_dx.setText("0");
        tv_pay.setText("0");
        et_money.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        reset();
    }

    int a = 1;

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
                list.add("支付方式:第三方支付");
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
        getDatabase();
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
        upLoading.setCardNo(cardNum);
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
        upLoading.setisIntegral(db_isCheck.isChecked());
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
                if (n == 3) {
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

    public class PopupWindows extends PopupWindow implements View.OnClickListener, PopupWindow.OnDismissListener {
        public PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.popup_window_quickdelm, null);
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
            lv_quick = (ListView) view.findViewById(R.id.lv_quickdelm);
            lv_quick.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            lv_quick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    et_money.setText(q_data.get(position).getC_Value() + "");
                    dismiss();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        @Override
        public void onClick(View view) {
            dismiss();
        }

        @Override
        public void onDismiss() {
            isclick_pay = true;
        }
    }


}
