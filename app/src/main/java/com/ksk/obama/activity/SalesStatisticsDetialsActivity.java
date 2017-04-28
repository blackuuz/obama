package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.SalesStatisticsDetialsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.SalesDetials;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesStatisticsDetialsActivity extends BaseActivity {
    private SalesStatisticsDetialsAdapter adapter;
    private List<SalesDetials.ResultDataBean> list = new ArrayList<>();
    private ListView lv;
    private TextView tv_name;
    private TextView tv_num;
    private TextView tv_money;
    private TextView tv_times;
    private TextView tv_timee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_statistics_detials);
        initTitle();
        initData();
    }

    private void initTitle() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tvin = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("销售统计详情");
        tvin.setVisibility(View.INVISIBLE);

        lv = (ListView) findViewById(R.id.lv);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_times = (TextView) findViewById(R.id.tv_time_start);
        tv_timee = (TextView) findViewById(R.id.tv_time_end);
        adapter = new SalesStatisticsDetialsAdapter(this, list);
        lv.setAdapter(adapter);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String num = intent.getStringExtra("num");
            String money = intent.getStringExtra("money");
            String times = intent.getStringExtra("times");
            String timee = intent.getStringExtra("timee");
            tv_name.setText(name);
            tv_num.setText(num);
            tv_money.setText(money);
            tv_times.setText(times);
            tv_timee.setText(timee);
            getHttp();
        } else {
            finish();
        }
    }

    private void getHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("UserinfoId", SharedUtil.getSharedData(SalesStatisticsDetialsActivity.this, "userInfoId"));
        map.put("dbName", SharedUtil.getSharedData(SalesStatisticsDetialsActivity.this, "dbname"));
        map.put("c_GoodsName", tv_name.getText().toString());
        map.put("STTime", tv_times.getText().toString());
        map.put("ENDTime", tv_timee.getText().toString());
        postToHttp(NetworkUrl.SALESDETIALS, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                SalesDetials salesDetials = new Gson().fromJson(jsonText, SalesDetials.class);
                if (salesDetials.getResult_stadus().equals("SUCCESS")) {
                    if (salesDetials.getResult_data() != null) {
                        list.addAll(salesDetials.getResult_data());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }
}
