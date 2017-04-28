package com.ksk.obama.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ksk.obama.R;

import static com.ksk.obama.R.id.tv_commit;

public class WeChatSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private int n0 = 0;
    private int n1 = 0;
    private int n2 = 0;
    private int n3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_setting);
        initTitale();
        initView();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("通知设置");
        tv_print.setText("确定");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        Switch aSwitch0 = (Switch) findViewById(R.id.wcs_set0);
        Switch aSwitch1 = (Switch) findViewById(R.id.wcs_set1);
        Switch aSwitch2 = (Switch) findViewById(R.id.wcs_set2);
        Switch aSwitch3 = (Switch) findViewById(R.id.wcs_set3);
        aSwitch0.setOnCheckedChangeListener(this);
        aSwitch1.setOnCheckedChangeListener(this);
        aSwitch2.setOnCheckedChangeListener(this);
        aSwitch3.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.wcs_set0:
                n0 = isChecked ? 1 : 0;
                break;
            case R.id.wcs_set1:
                n1 = isChecked ? 1 : 0;
                break;
            case R.id.wcs_set2:
                n2 = isChecked ? 1 : 0;
                break;
            case R.id.wcs_set3:
                n3 = isChecked ? 1 : 0;
                break;
        }
    }
}
