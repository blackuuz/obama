package com.ksk.obama.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initTitale();
        initView();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("系统设置");
    }

    private void initView() {
        TextView tv0 = (TextView) findViewById(R.id.tv_set_0);
        TextView tv1 = (TextView) findViewById(R.id.tv_set_1);
        TextView tv2 = (TextView) findViewById(R.id.tv_set_2);
        TextView tv3 = (TextView) findViewById(R.id.tv_set_3);
        TextView tv4 = (TextView) findViewById(R.id.tv_set_4);
        TextView tv5 = (TextView) findViewById(R.id.tv_set_5);
        TextView tv6 = (TextView) findViewById(R.id.tv_set_6);
        TextView tv7 = (TextView) findViewById(R.id.tv_version);
        tv7.setText("版本:v" + Utils.getAppVersionName(SettingActivity.this));
        if (SharedUtil.getSharedBData(SettingActivity.this, "payxj")) {
            tv0.setText("可用");
        } else {
            tv0.setText("不可用");
            tv0.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
        if (SharedUtil.getSharedBData(SettingActivity.this, "paywx")) {
            tv1.setText("可用");
        } else {
            tv1.setText("不可用");
            tv1.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
        if (SharedUtil.getSharedBData(SettingActivity.this, "payal")) {
            tv2.setText("可用");
        } else {
            tv2.setText("不可用");
            tv2.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
//        if (SharedUtil.getSharedBData(SettingActivity.this, "paybank")) {
//            tv3.setText("可用");
//        } else {
//            tv3.setText("不可用");
//            tv3.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
//        }
        if (SharedUtil.getSharedBData(SettingActivity.this, "nfc")) {
            tv4.setText("可用");
        } else {
            tv4.setText("不可用");
            tv4.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
        if (SharedUtil.getSharedBData(SettingActivity.this, "citiao")) {
            tv5.setText("可用");
        } else {
            tv5.setText("不可用");
            tv5.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
        if (SharedUtil.getSharedBData(SettingActivity.this, "saoma")) {
            tv6.setText("可用");
        } else {
            tv6.setText("不可用");
            tv6.setTextColor(ContextCompat.getColor(SettingActivity.this, R.color.text_red2));
        }
    }
}
