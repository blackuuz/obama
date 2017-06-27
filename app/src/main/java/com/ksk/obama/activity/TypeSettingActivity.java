package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

public class TypeSettingActivity extends AppCompatActivity {

    private SwitchCompat[] sth = new SwitchCompat[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_setting);
        initTitle();
        initView();
    }

    private void initTitle() {
        TextView tv_name = (TextView) findViewById(R.id.title_name);
        tv_name.setText("机型设置");
        TextView tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_commit.setText("保存");
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = -1;
                if (sth[0].isChecked()) {
                    type = 1;
                } else if (sth[1].isChecked()) {
                    type = 3;
                } else if (sth[2].isChecked()) {
                    type = 4;
                } else if (sth[3].isChecked()) {
                    type = 30;
                } else if (sth[4].isChecked()) {
                    type = 8;
                }else if (sth[5].isChecked()){
                    type = 9;
                }
                if (type != -1) {
                    if(type == 9){
                        type = 4;
                    }
                    SharedUtil.setSharedData(TypeSettingActivity.this, "isSet", type + "");
                    SharedUtil.setSharedData(TypeSettingActivity.this, "robotType", type + "");
                    startActivity(new Intent(TypeSettingActivity.this, LoginActivity.class));
                    Log.d("uuz", "onClick: "+type);
                    finish();
                } else {
                    Utils.showToast(TypeSettingActivity.this, "请选择机型");
                }
            }
        });
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        SwitchCompat s_lkl = (SwitchCompat) findViewById(R.id.switch_lkl);
        SwitchCompat s_sm = (SwitchCompat) findViewById(R.id.switch_sm);
        SwitchCompat s_phone = (SwitchCompat) findViewById(R.id.switch_phone);
        SwitchCompat switch_sm_v1 = (SwitchCompat) findViewById(R.id.switch_sm_v1);
        SwitchCompat switch_wang_pos = (SwitchCompat) findViewById(R.id.switch_wang_pos);
        SwitchCompat switch_liandong = (SwitchCompat) findViewById(R.id.switch_liandong);
        sth[0] = s_lkl;
        sth[1] = s_sm;
        sth[2] = s_phone;
        sth[3] = switch_sm_v1;
        sth[4] = switch_wang_pos;
        sth[5] = switch_liandong;
        s_lkl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(0);
                }
            }
        });
        s_sm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(1);
                }
            }
        });
        s_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(2);
                }
            }
        });
        switch_sm_v1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(3);
                }
            }
        });
        switch_wang_pos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(4);
                }
            }
        });
        switch_liandong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setType(5);
                }
            }
        });


        String str = SharedUtil.getSharedData(TypeSettingActivity.this, "robotType");
        if (str.equals("1")) {
            s_lkl.setChecked(true);
        } else if (str.equals("3")) {
            s_sm.setChecked(true);
        } else if (str.equals("4")) {
            s_phone.setChecked(true);
        } else if (str.equals("30")) {
            switch_sm_v1.setChecked(true);
        } else if (str.equals("8")) {
            switch_wang_pos.setChecked(true);
        }else if(str.equals("9")){
            switch_liandong.setChecked(true);
        }
    }

    private void setType(int n) {
        for (int i = 0; i < sth.length; i++) {
            if (i != n && sth[i].isChecked()) {
                sth[i].setChecked(false);
            }
        }
    }

}
