package com.ksk.obama.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.ChangePerson;
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

public class GetMoneyActivity extends BasePrintActivity {

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
    private TextView tv_title_jrsy;
    private TextView tv_change_time;
    private String startTime;
    private String endTime;
    private boolean flag_g = false;


    //今日收益*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_person);
        initTitale();
        initView();
        getNowTime();
        getshiftInfo();
        tv_change_time.setText(startTime+" - "+endTime);
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
        title_name.setText("今日收益");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_g){
                    printInfo();
                }
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

        tv_jf = (TextView) findViewById(R.id.tv_change_jf);
        tv_jf_dx = (TextView) findViewById(R.id.tv_change_jf_dx);
        tv_jf_give = (TextView) findViewById(R.id.tv_change_jf_give);
        tv_jf_del = (TextView) findViewById(R.id.tv_change_jf_del);
        tv_jf_hj = (TextView) findViewById(R.id.tv_change_jf_hj);
        tv_change_time = (TextView) findViewById(R.id.tv_change_time);
        tv_title_jrsy = (TextView) findViewById(R.id.tv_title_jrsy);
        tv_title_jrsy.setText("今日收益");

    }

    private void getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = dateFormat.format(date);
        String[] times = endTime.split(" ");
        startTime = times[0] + " 00:00:00";
    }

    private void getshiftInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("UserinfoId", SharedUtil.getSharedData(GetMoneyActivity.this, "userInfoId"));
        map.put("dbName", SharedUtil.getSharedData(GetMoneyActivity.this, "dbname"));
        map.put("STTime", startTime);
        map.put("ENDTime", endTime);
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
                Gson gson = new Gson();
                ChangePerson shiftInfo = gson.fromJson(json, ChangePerson.class);
                tv_shopName.setText("店面:" + SharedUtil.getSharedData(GetMoneyActivity.this, "shopname"));
                tv_userName.setText("操作员:" + SharedUtil.getSharedData(GetMoneyActivity.this, "username"));
                tv_xlh.setText("手持序列号:" + SharedUtil.getSharedData(GetMoneyActivity.this, "xlh"));
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
                flag_g = true;

            } else {
                String result_msg = object.getString("result_errmsg");
                Utils.showToast(GetMoneyActivity.this, result_msg);
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

        listp.add("积分兑换:" + tv_jf.getText().toString());
        listp.add("积分抵现:" + tv_jf_dx.getText().toString());
        listp.add("赠送积分:" + tv_jf_give.getText().toString());
        listp.add("扣除积分:" + tv_jf_del.getText().toString());

        printPage("今日收益", listp, null, false);

    }
}
