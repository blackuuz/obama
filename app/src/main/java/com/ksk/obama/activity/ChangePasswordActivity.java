package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {

    private TextView et_pw0;
    private EditText et_pw1;
    private EditText et_pw2;
    private String memid;
    private TextView tv_hint;
    private String pw = "";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initTitale();
        initView();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("pw", pw);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tv_print.setText("确定");
        title_name.setText("修改密码");

        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void initView() {
        if (getIntent() != null) {
            pw = getIntent().getStringExtra("pw");
            memid = getIntent().getStringExtra("memid");
            uid = getIntent().getStringExtra("uid");
        }
        tv_hint = (TextView) findViewById(R.id.change_hint);
        et_pw0 = (TextView) findViewById(R.id.change_pw_0);
        et_pw1 = (EditText) findViewById(R.id.change_pw_1);
        et_pw2 = (EditText) findViewById(R.id.change_pw_2);
        et_pw0.setText(pw);
    }

    private void sendData() {
        tv_hint.setText("");
        String pw1 = et_pw1.getText().toString();
        String pw2 = et_pw2.getText().toString();

        if (!pw1.equals(pw2)) {
            Utils.showToast(ChangePasswordActivity.this, "两次输入的密码不一致");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("dbName", SharedUtil.getSharedData(ChangePasswordActivity.this, "dbname"));
            map.put("memberid", memid);
            map.put("newpwd", pw1);
            map.put("c_Billfrom", robotType + "");
            map.put("CardCode", uid);
            map.put("uid", SharedUtil.getSharedData(ChangePasswordActivity.this, "userInfoId"));

            postToHttp(NetworkUrl.CHANGEPW, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    showHttpData(jsonText);
                }

                @Override
                public void OnFail(String message) {
                    Utils.showToast(ChangePasswordActivity.this, message);
                }
            });
        }

    }

    private void showHttpData(String str) {
        try {
            JSONObject object = new JSONObject(str);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                pw = Utils.getMD5Code(et_pw1.getText().toString());
                et_pw1.setText("");
                et_pw2.setText("");
                tv_hint.setText("密码修改成功");
                Utils.showToast(ChangePasswordActivity.this, "密码修改成功");
            } else {
                tv_hint.setText("密码修改失败,请重试");
                String msg1 = object.getString("result_errmsg");
                Utils.showToast(ChangePasswordActivity.this, msg1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("pw", pw);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
