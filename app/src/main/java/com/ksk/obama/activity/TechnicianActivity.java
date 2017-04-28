package com.ksk.obama.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.fragment.TechnicianNoFragment;
import com.ksk.obama.fragment.TechnicianYesFragment;

public class TechnicianActivity extends BaseActivity {
    private TechnicianYesFragment yesFragment;
    private TechnicianNoFragment noFragment;
    private Button btn_use;
    private Button btn_unuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician);
        initTitle();
        initFragment();
    }

    private void initTitle() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setVisibility(View.INVISIBLE);
        title_name.setText("技师选择");

        btn_unuse = (Button) findViewById(R.id.btn_unuse);
        btn_use = (Button) findViewById(R.id.btn_use);
    }

    private void initFragment() {
        yesFragment = new TechnicianYesFragment();
        noFragment = new TechnicianNoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_technician, noFragment).commit();
    }

    public void use(View v) {
        btn_use.setBackgroundResource(R.drawable.house_select);
        btn_unuse.setBackgroundResource(R.drawable.house_normal);
        btn_use.setTextColor(ContextCompat.getColor(TechnicianActivity.this, R.color.white));
        btn_unuse.setTextColor(ContextCompat.getColor(TechnicianActivity.this, R.color.main_title_bg));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_technician, yesFragment).commit();
    }

    public void unuse(View v) {
        btn_unuse.setBackgroundResource(R.drawable.house_select);
        btn_use.setBackgroundResource(R.drawable.house_normal);
        btn_unuse.setTextColor(ContextCompat.getColor(TechnicianActivity.this, R.color.white));
        btn_use.setTextColor(ContextCompat.getColor(TechnicianActivity.this, R.color.main_title_bg));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_technician, noFragment).commit();
    }
}
