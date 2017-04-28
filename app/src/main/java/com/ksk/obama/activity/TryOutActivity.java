package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TryOutActivity extends BasePrintActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_phone;
    private EditText et_yam;
    private EditText et_shop;
    private EditText et_shopName;
    private EditText et_remark;
    private Button btn_yzm;
    private String yzmNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_out);
        initView();
    }

    private void initView() {
        TextView name = (TextView) findViewById(R.id.title_name);
        name.setText("申请试用");
        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setText("提交");

        et_name = (EditText) findViewById(R.id.try_name);
        et_phone = (EditText) findViewById(R.id.try_phone);
        et_yam = (EditText) findViewById(R.id.try_yzm);
        et_shop = (EditText) findViewById(R.id.try_shop);
        et_shopName = (EditText) findViewById(R.id.try_shop_name);
        et_remark = (EditText) findViewById(R.id.try_remark);
        btn_yzm = (Button) findViewById(R.id.try_btn_get);

        btn_yzm.setOnClickListener(this);
        tv_print.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_btn_get:
                yzmNum = "";
                btn_yzm.setEnabled(false);
                btn_yzm.setBackgroundResource(R.mipmap.try12);
                getNum();
                break;
            case R.id.tv_commit:
                sendData();
                break;
        }
    }

    private void getNum() {
        String phone = et_phone.getText().toString();
        if (Utils.isMobileNO(phone)) {
            Map<String, String> map = new HashMap<>();
            map.put("tel", phone);
            postToHttp(NetworkUrl.YZM, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String code = object.getString("result_stadus");
                        if (code.equals("SUCCESS")) {
                            yzmNum = object.getString("result_data");
                            Utils.showToast(TryOutActivity.this, "验证码发送成功");
                        } else {
                            btn_yzm.setEnabled(true);
                            btn_yzm.setBackgroundResource(R.mipmap.try_06);
                            Utils.showToast(TryOutActivity.this, "获取验证码失败,请重试");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void OnFail(String message) {

                }
            });
        } else {
            btn_yzm.setEnabled(true);
            btn_yzm.setBackgroundResource(R.mipmap.try_06);
            Utils.showToast(TryOutActivity.this, "请填写正确手机号");
        }
    }

    private void sendData() {
        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString();
        String yzm = et_yam.getText().toString();
        String shop = et_shop.getText().toString();
        String shopName = et_shopName.getText().toString();
        String remak = et_remark.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Utils.showToast(TryOutActivity.this, "请填写姓名");
        } else if (TextUtils.isEmpty(phone)) {
            Utils.showToast(TryOutActivity.this, "请填写手机号");
        } else if (!Utils.isMobileNO(phone)) {
            Utils.showToast(TryOutActivity.this, "请填写正确手机号");
        } else if (!yzm.equals(yzmNum)) {
            Utils.showToast(TryOutActivity.this, "请填写正确的验证码");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("Ver", "易德会员云POS" + Utils.getAppVersionName(TryOutActivity.this));
            map.put("EquipmentNum", terminalSn);
            map.put("ComName", shopName);
            map.put("Industry", shop);
            map.put("LinkMan", name);
            map.put("Contact", phone);
            map.put("Trial", remak);
            postToHttp(NetworkUrl.SHIYONG, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String code = object.getString("result_stadus");
                        if (code.equals("SUCCESS")) {
                            showAlert();
                        } else {
                            Utils.showToast(TryOutActivity.this, "申请试用失败,请重试");
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

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TryOutActivity.this);
        builder.setTitle("申请试用成功!");
        builder.setMessage("工作人员会尽快与您取得联系");
        builder.setPositiveButton("确定", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        builder.create().show();
    }
}
