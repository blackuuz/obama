package com.ksk.obama.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LossOfCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_cardNum;
    private TextView tv_is;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_time;
    private TextView tv_str0;
    private TextView tv_str1;
    private TextView tv_str2;
    private TextView tv_str3;
    private EditText et_num1;
    private EditText et_num2;
    private EditText et_pw;
    private Button btn_stop, btn_resume, btn_change;
    private String id = "";
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_of_card);

        initTitale();
        initView();
        setData();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("补卡挂失");
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_loss_cardNum);
        tv_is = (TextView) findViewById(R.id.tv_loss_cardis);
        tv_name = (TextView) findViewById(R.id.tv_loss_name);
        tv_type = (TextView) findViewById(R.id.tv_loss_type);
        tv_time = (TextView) findViewById(R.id.tv_loss_time);
        tv_str0 = (TextView) findViewById(R.id.tv_loss_str0);
        tv_str1 = (TextView) findViewById(R.id.tv_loss_str1);
        tv_str2 = (TextView) findViewById(R.id.tv_loss_str2);
        tv_str3 = (TextView) findViewById(R.id.tv_loss_str3);
        et_num1 = (EditText) findViewById(R.id.et_loss_new_cardnum);
        et_num2 = (EditText) findViewById(R.id.et_loss_new_cardnum2);
        et_pw = (EditText) findViewById(R.id.et_loss_new_pw);

        btn_stop = (Button) findViewById(R.id.btn_loss_stop);
        btn_resume = (Button) findViewById(R.id.btn_loss_resume);
        btn_change = (Button) findViewById(R.id.btn_loss_change);

        btn_stop.setOnClickListener(this);
        btn_resume.setOnClickListener(this);
        btn_change.setOnClickListener(this);
    }

    private void setData() {
        if (getIntent() != null) {
            int stop_flag = getIntent().getExtras().getInt("stop", 0);
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
            if (stop_flag == 1) {
                btn_stop.setEnabled(false);
                tv_is.setText("(已停用)");
                btn_stop.setBackgroundResource(R.mipmap.select_1_1);
            } else {
                btn_resume.setEnabled(false);
                tv_is.setText("(未停用)");
                btn_resume.setBackgroundResource(R.mipmap.select_1_1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (isclick_pay) {
            isclick_pay = false;
            switch (v.getId()) {
                case R.id.btn_loss_stop:
                    getHttpData(NetworkUrl.STOPCARD, 0);
                    break;
                case R.id.btn_loss_resume:
                    getHttpData(NetworkUrl.RESUMECARD, 1);
                    break;
                case R.id.btn_loss_change:
                    String num1 = et_num1.getText().toString();
                    String num2 = et_num2.getText().toString();
                    if (num1.equals(tv_cardNum.getText().toString())) {
                        isclick_pay = true;
                        Utils.showToast(LossOfCardActivity.this, "与原卡号重复,请重新输入");
                    } else if (TextUtils.isEmpty(num1)) {
                        isclick_pay = true;
                        Utils.showToast(LossOfCardActivity.this, "请输入新卡号");
                    } else if (TextUtils.isEmpty(num2)) {
                        isclick_pay = true;
                        Utils.showToast(LossOfCardActivity.this, "请输入再次输入新卡号");
                    } else if (!num1.equals(num2)) {
                        isclick_pay = true;
                        Utils.showToast(LossOfCardActivity.this, "两次输入的卡号必须一致");
                    } else {
                        getHttpData(NetworkUrl.CHANGECARD, 2);
                    }
                    break;
            }
        }
    }

    private void getHttpData(String url, final int n) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(LossOfCardActivity.this, "dbname"));
        map.put("User_Id", SharedUtil.getSharedData(LossOfCardActivity.this, "userInfoId"));
        map.put("Member_Id", id);
        map.put("c_Billfrom", robotType + "");
        map.put("CardCode", uid);
        if (n == 2) {
            map.put("New_Card", et_num1.getText().toString());
            map.put("New_Pwd", et_pw.getText().toString());
        }
        postToHttp(url, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String tag = object.getString("result_stadus");
                    if (tag.equals("SUCCESS")) {
                        switch (n) {
                            case 0:
                                btn_stop.setEnabled(false);
                                btn_resume.setEnabled(true);
                                tv_is.setText("(已停用)");
                                btn_resume.setBackgroundResource(R.drawable.select_loss);
                                btn_stop.setBackgroundResource(R.mipmap.select_1_1);
                                showAlert("挂失停用");
                                break;
                            case 1:
                                btn_resume.setBackgroundResource(R.mipmap.select_1_1);
                                btn_stop.setBackgroundResource(R.drawable.select_loss);
                                btn_resume.setEnabled(false);
                                btn_stop.setEnabled(true);
                                tv_is.setText("(已恢复)");
                                showAlert("挂失恢复");
                                break;
                            case 2:
                                showAlert("补换新卡");
                                break;
                        }

                    } else {
                        if (n == 2) {
                            String msg = object.getString("result_errmsg");
                            Utils.showToast(LossOfCardActivity.this, msg);
                        } else {
                            Utils.showToast(LossOfCardActivity.this, "操作失败,请重试");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isclick_pay = true;
            }

            @Override
            public void OnFail(String message) {
                isclick_pay = true;
            }
        });
    }

    private void showAlert(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LossOfCardActivity.this);
        builder.setTitle("提示");
        builder.setMessage(str + "成功");
        builder.setPositiveButton("确定", null);
        builder.create();
        builder.show();
    }

}
