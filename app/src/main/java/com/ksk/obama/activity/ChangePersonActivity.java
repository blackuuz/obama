package com.ksk.obama.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.DB.RechargeAgain;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.ChangePerson;
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

public class ChangePersonActivity extends BasePrintActivity implements IPrintSuccessCallback, IPrintErrorCallback {

    private TextView tv_shopName;
    private TextView tv_userName;
    private TextView tv_xlh;
    private TextView tv_count_money;
    private TextView tv_count_moneyin;
    private TextView tv_pay0;
    private TextView tv_pay1;
    private TextView tv_pay2;
    private TextView tv_pay3;
    private TextView tv_pay4;
    private TextView tv_open_money;
    private TextView tv_open_moneyin;
    private TextView tv_vip_money;
    private TextView tv_vip_moneyin;
    private TextView tv_vipin_money;
    private TextView tv_vipin_moneyin;
    private TextView tv_jf;
    private TextView tv_novip_money;
    private TextView tv_novip_moneyin;
    private TextView tv_buyc_money;
    private TextView tv_buyc_moneyin;
    private TextView tv_tk;
    private TextView tv_pay0_t;
    private TextView tv_pay1_t;
    private TextView tv_pay2_t;
    private TextView tv_pay3_t;
    private TextView tv_jf_dx;
    private TextView tv_jf_give;
    private TextView tv_jf_del;
    private TextView tv_jf_hj;
    private TextView tv_time;
    private boolean isSupplement = false;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_person);
        this.setOnPrintSuccess(this);
        this.setOnPrintError(this);
        Intent intent = getIntent();
        if (intent != null) {
            isSupplement = intent.getBooleanExtra("supplement", false);
        }
        initTitale();
        initView();
    }

    private void queryDB() {
        Connector.getDatabase();
        List<RechargeAgain> list1 = DataSupport.findAll(RechargeAgain.class);
        List<QuickDelMoney> list2 = DataSupport.findAll(QuickDelMoney.class);
        List<OpenCardDb> list3 = DataSupport.findAll(OpenCardDb.class);
        List<BuyCountDb> list4 = DataSupport.findAll(BuyCountDb.class);
        List<BuyShopDb> list5 = DataSupport.findAll(BuyShopDb.class);
        if (list1 != null && list1.size() > 0) {
            startActivity(new Intent(ChangePersonActivity.this, RechargeSupplementActivity.class));
        } else if (list2 != null && list2.size() > 0) {
            startActivity(new Intent(ChangePersonActivity.this, QuickDelMoneySupplementActivity.class));
        } else if (list3 != null && list3.size() > 0) {
            startActivity(new Intent(ChangePersonActivity.this, OpenCardSupplementActivity.class));
        } else if (list4 != null && list4.size() > 0) {
            startActivity(new Intent(ChangePersonActivity.this, BuyCountSupplementActivity.class));
        } else if (list5 != null && list5.size() > 0) {
            startActivity(new Intent(ChangePersonActivity.this, BuyShopSupplementActivity.class));
        } else {
            if (isSupplement) {
                finish();
            } else {
                getshiftInfo();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(ChangePersonActivity.this)) {
            queryDB();
        } else {
            Utils.showToast(ChangePersonActivity.this, "当前无网络");
        }

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
        tv_print.setText("打印票据");
        title_name.setText("交班");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePersonActivity.this);
                    builder.setTitle("交班提示:");
                    builder.setMessage("正进行交班操作，是否继续？");
                    builder.setPositiveButton("确定继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            send();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();

                }
            }
        });
    }

    private void send() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date(System.currentTimeMillis()));
        Log.e("djy", "send: " + time);
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(ChangePersonActivity.this, "dbname"));
        map.put("UserinfoId", SharedUtil.getSharedData(ChangePersonActivity.this, "userInfoId"));
        map.put("ticalTime", time);
        map.put("GroupID", SharedUtil.getSharedData(ChangePersonActivity.this, "groupid"));
        map.put("EquipmentNum", terminalSn);
        postToHttp(NetworkUrl.CHANGEPERSON, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String str = object.getString("result_stadus");
                    if (str.equals("SUCCESS")) {
                        flag = false;
                        printInfo();
                        switch (robotType) {
                            case 3:
                            case 4:
                                startActivity(new Intent(ChangePersonActivity.this, LoginActivity.class));
                                break;
                        }
                    } else {
                        Utils.showToast(ChangePersonActivity.this, "提交数据失败，请稍后再试");
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

    private void initView() {
        tv_shopName = (TextView) findViewById(R.id.tv_change_shop_name);
        tv_userName = (TextView) findViewById(R.id.tv_change_user_name);
        tv_xlh = (TextView) findViewById(R.id.tv_change_xlh);
        tv_count_money = (TextView) findViewById(R.id.tv_change_count_money);
        tv_count_moneyin = (TextView) findViewById(R.id.tv_change_count_moneyin);
        tv_pay0 = (TextView) findViewById(R.id.tv_change_pay0);
        tv_pay1 = (TextView) findViewById(R.id.tv_change_pay1);
        tv_pay2 = (TextView) findViewById(R.id.tv_change_pay2);
        tv_pay3 = (TextView) findViewById(R.id.tv_change_pay3);
        tv_pay4 = (TextView) findViewById(R.id.tv_change_pay4);
        tv_open_money = (TextView) findViewById(R.id.tv_change_open_money);
        tv_open_moneyin = (TextView) findViewById(R.id.tv_change_open_moneyin);
        tv_vip_money = (TextView) findViewById(R.id.tv_change_expenfiture_money);
        tv_vip_moneyin = (TextView) findViewById(R.id.tv_change_expenfiture_moneyin);
        tv_vipin_money = (TextView) findViewById(R.id.tv_change_recharge_money);
        tv_vipin_moneyin = (TextView) findViewById(R.id.tv_change_recharge_moneyin);
        tv_novip_money = (TextView) findViewById(R.id.tv_change_novip_money);
        tv_novip_moneyin = (TextView) findViewById(R.id.tv_change_novip_moneyin);
        tv_buyc_money = (TextView) findViewById(R.id.tv_change_buyc_money);
        tv_buyc_moneyin = (TextView) findViewById(R.id.tv_change_buyc_moneyin);

        tv_tk = (TextView) findViewById(R.id.tv_change_count_moneyin_t);
        tv_pay0_t = (TextView) findViewById(R.id.tv_change_pay0_t);
        tv_pay1_t = (TextView) findViewById(R.id.tv_change_pay1_t);
        tv_pay2_t = (TextView) findViewById(R.id.tv_change_pay2_t);
        tv_pay3_t = (TextView) findViewById(R.id.tv_change_pay3_t);
        tv_time = (TextView) findViewById(R.id.tv_change_time);

        tv_jf = (TextView) findViewById(R.id.tv_change_jf);
        tv_jf_dx = (TextView) findViewById(R.id.tv_change_jf_dx);
        tv_jf_give = (TextView) findViewById(R.id.tv_change_jf_give);
        tv_jf_del = (TextView) findViewById(R.id.tv_change_jf_del);
        tv_jf_hj = (TextView) findViewById(R.id.tv_change_jf_hj);
    }

    private void getshiftInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("UserinfoId", SharedUtil.getSharedData(ChangePersonActivity.this, "userInfoId"));
        map.put("dbName", SharedUtil.getSharedData(ChangePersonActivity.this, "dbname"));
        postToHttp(NetworkUrl.TICALRECORD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                showHttpData(jsonText);
            }

            @Override
            public void OnFail(String message) {
            }
        });

    }

    private void showHttpData(String json) {
        Logger.e(json);
        try {
            JSONObject object = new JSONObject(json);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                String time = object.getString("result_time");
                Gson gson = new Gson();
                ChangePerson shiftInfo = gson.fromJson(json, ChangePerson.class);
                tv_shopName.setText("店面:" + SharedUtil.getSharedData(ChangePersonActivity.this, "shopname"));
                tv_userName.setText("操作员:" + SharedUtil.getSharedData(ChangePersonActivity.this, "username"));
                tv_xlh.setText("手持序列号:" + SharedUtil.getSharedData(ChangePersonActivity.this, "xlh"));
                tv_time.setText(time);
                tv_open_money.setText(shiftInfo.getResult_data().getCreateshouldcardmoney());
                tv_open_moneyin.setText(shiftInfo.getResult_data().getCreatecardmoney());
                tv_vipin_money.setText(shiftInfo.getResult_data().getRechargeshouldcardmoney());
                tv_vipin_moneyin.setText(shiftInfo.getResult_data().getRechargemoney());
                tv_vip_money.setText(shiftInfo.getResult_data().getMembercostshouldcardmoney());
                tv_vip_moneyin.setText(shiftInfo.getResult_data().getMembercostmoney());
                tv_count_money.setText(shiftInfo.getResult_data().getAllmoney());
                tv_count_moneyin.setText(shiftInfo.getResult_data().getAllgetmoney());
                tv_buyc_money.setText(shiftInfo.getResult_data().getBuytimeshouldmoney());
                tv_buyc_moneyin.setText(shiftInfo.getResult_data().getBuytimemoney());
                tv_novip_money.setText(shiftInfo.getResult_data().getNomembershouldcostmoney());
                tv_novip_moneyin.setText(shiftInfo.getResult_data().getNomembercostmoney());
                tv_pay0.setText("现金收款:￥" + shiftInfo.getResult_data().getPayCash());
                tv_pay1.setText("微信收款:￥" + shiftInfo.getResult_data().getPaywechat());
                tv_pay4.setText("储值卡收款:￥" + shiftInfo.getResult_data().getPayCard());
                tv_pay3.setText("银行卡收款:￥" + shiftInfo.getResult_data().getPayBank());
                tv_pay2.setText("支付宝收款:￥" + shiftInfo.getResult_data().getPayali());
                tv_jf_hj.setText(shiftInfo.getResult_data().getAll_Integral());
                tv_jf.setText(shiftInfo.getResult_data().getGift_integral());
                tv_jf_dx.setText(shiftInfo.getResult_data().getPayIntegral());
                tv_jf_give.setText(shiftInfo.getResult_data().getGive_integral());
                tv_jf_del.setText(shiftInfo.getResult_data().getDeduct_integral());
                tv_tk.setText(shiftInfo.getResult_data().getCancelcardmoney());
                tv_pay0_t.setText("现金退款:￥" + shiftInfo.getResult_data().getCancelcardmoneyPayCash());
                tv_pay1_t.setText("微信退款:￥" + shiftInfo.getResult_data().getCancelcardmoneyPaywechat());
                tv_pay2_t.setText("支付宝退款:￥" + shiftInfo.getResult_data().getCancelcardmoneyPayali());
                tv_pay3_t.setText("银行卡退款:￥" + shiftInfo.getResult_data().getCancelcardmoneyPayBank());
                flag = true;
            } else {
                String result_msg = object.getString("result_errmsg");
                Utils.showToast(ChangePersonActivity.this, result_msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfo() {
        List<String> listp = new ArrayList<>();

        listp.add(tv_shopName.getText().toString());
        listp.add(tv_userName.getText().toString());
        listp.add(tv_xlh.getText().toString());
        listp.add("统计时间:" + tv_time.getText().toString());
        listp.add("收益统计:" + tv_count_money.getText().toString());
        listp.add("收款:" + tv_count_moneyin.getText().toString());
        listp.add(tv_pay0.getText().toString());
        listp.add(tv_pay1.getText().toString());
        listp.add(tv_pay2.getText().toString());
        listp.add(tv_pay3.getText().toString());
        listp.add(tv_pay4.getText().toString());
        listp.add("退款(销户):￥" + tv_tk.getText().toString());
        listp.add(tv_pay0_t.getText().toString());
        listp.add(tv_pay1_t.getText().toString());
        listp.add(tv_pay2_t.getText().toString());
        listp.add(tv_pay3_t.getText().toString());
        listp.add("会员开卡:" + tv_open_money.getText().toString());
        listp.add("收款:" + tv_open_moneyin.getText().toString());
        listp.add("会员消费:" + tv_vip_money.getText().toString());
        listp.add("收款:" + tv_vip_moneyin.getText().toString());
        listp.add("会员充值:" + tv_vipin_money.getText().toString());
        listp.add("收款:" + tv_vipin_moneyin.getText().toString());

        listp.add("非会员消费:" + tv_novip_money.getText().toString());
        listp.add("收款:" + tv_novip_moneyin.getText().toString());
        listp.add("购买次数:" + tv_buyc_money.getText().toString());
        listp.add("收款:" + tv_buyc_moneyin.getText().toString());

        listp.add("合计积分:" + tv_jf_hj.getText().toString());
        listp.add("积分兑换:" + tv_jf.getText().toString());
        listp.add("积分抵现:" + tv_jf_dx.getText().toString());
        listp.add("赠送积分:" + tv_jf_give.getText().toString());
        listp.add("扣除积分:" + tv_jf_del.getText().toString());

        printPage("交班", listp, null, false);

    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_change_person);
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
        startActivity(new Intent(ChangePersonActivity.this, LoginActivity.class));
    }
}
