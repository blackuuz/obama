package com.ksk.obama.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OpinionFeedbackActivity extends BasePrintActivity {
    private EditText et_ad, et_phone, et_name;
    private TextView tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_feedback);
        initView();
    }

    private void initView() {
        TextView name = (TextView) findViewById(R.id.title_name);
        name.setText("意见反馈");
        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tv_commit= (TextView) findViewById(R.id.tv_commit);
        tv_commit.setText("提交");
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        et_ad = (EditText) findViewById(R.id.set_ad);
        et_phone = (EditText) findViewById(R.id.set_phone);
        et_name = (EditText) findViewById(R.id.set_name);
        tv_hint = (TextView) findViewById(R.id.set_hint);

        et_ad.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = et_ad.getSelectionStart();
                editEnd = et_ad.getSelectionEnd();
                if (temp.length() > 500) {
                    Utils.showToast(OpinionFeedbackActivity.this, "您输入的字数已经超过了限制！");
                    editable.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    et_ad.setText(editable);
                    et_ad.setSelection(tempSelection);
                    tv_hint.setText("500/500");
                } else {
                    tv_hint.setText(temp.length() + "/500");
                }
            }
        });
    }

    private void sendData() {
        Map<String, String> map = new HashMap<>();
        map.put("PosNum", terminalSn);
        map.put("Advice", et_ad.getText().toString());
        map.put("Linkman", et_name.getText().toString());
        map.put("Phone", et_phone.getText().toString());
        postToHttp(NetworkUrl.ADVICE, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.d(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String code = object.getString("result_stadus");
                    String msg = object.getString("result_msg");
                    Utils.showToast(OpinionFeedbackActivity.this, msg);
                    if (code.equals("SUCCESS")) {
                        finish();
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
