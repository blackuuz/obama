package com.ksk.obama.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.fragment.HouseNoFragment;
import com.ksk.obama.fragment.HouseYesFragment;

public class HouseActivity extends BaseActivity {

    private HouseYesFragment yesFragment;
    private HouseNoFragment noFragment;
    private Button btn_use;
    private Button btn_unuse;
    private TextView tv_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        initTitle();
        initFragment();

    }

    private void initTitle() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setVisibility(View.INVISIBLE);
        tv_print.setEnabled(false);
        title_name.setText("台房选择");

        btn_unuse = (Button) findViewById(R.id.btn_unuse);
        btn_use = (Button) findViewById(R.id.btn_use);

        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesFragment.payMoney();
            }
        });
    }

    private void initFragment() {
        yesFragment = new HouseYesFragment();
        noFragment = new HouseNoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_house, noFragment).commit();
    }

    public void use(View v) {
        tv_print.setEnabled(false);
        tv_print.setVisibility(View.INVISIBLE);
        btn_use.setBackgroundResource(R.drawable.house_select);
        btn_unuse.setBackgroundResource(R.drawable.house_normal);
        btn_use.setTextColor(ContextCompat.getColor(HouseActivity.this, R.color.white));
        btn_unuse.setTextColor(ContextCompat.getColor(HouseActivity.this, R.color.main_title_bg));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_house, yesFragment).commit();
    }

    public void unuse(View v) {
        tv_print.setEnabled(true);
        tv_print.setVisibility(View.VISIBLE);
        btn_unuse.setBackgroundResource(R.drawable.house_select);
        btn_use.setBackgroundResource(R.drawable.house_normal);
        btn_unuse.setTextColor(ContextCompat.getColor(HouseActivity.this, R.color.white));
        btn_use.setTextColor(ContextCompat.getColor(HouseActivity.this, R.color.main_title_bg));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_house, noFragment).commit();
    }
}
