package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyShopUseCouponAdapter;
import com.ksk.obama.callback.IQrcodeCallBack;

public class BuyShopUseCouponActivity extends BaseActivity implements View.OnClickListener, IQrcodeCallBack {

    private float delMoney = 0;
    private float oldMoney = 0;

    private BuyShopUseCouponAdapter adapter;
    private EditText et_number;
    private ListView lv_data;
    private TextView tv_num;
    private TextView tv_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_shop_use_coupon);
        setOnReadQrcode(this);
        initData();
        initTitale();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            delMoney = getIntent().getFloatExtra("delmoney", 0);
            oldMoney = Float.parseFloat(getIntent().getStringExtra("oldMoney"));
        }
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("确定");
        title_name.setText("卡券验证");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("delM", delMoney);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_use_coupon_number);
        Button btn_qrCode = (Button) findViewById(R.id.btn_use_coupon_qrCode);
        Button btn_query = (Button) findViewById(R.id.btn_use_coupon_query);
        lv_data = (ListView) findViewById(R.id.lv_use_coupon_list);
        tv_num = (TextView) findViewById(R.id.tv_use_coupon_num);
        tv_money = (TextView) findViewById(R.id.tv_use_coupon_money);

        btn_qrCode.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_use_coupon_qrCode:
                toQrcodeActivity();
                break;

            case R.id.btn_use_coupon_query:

                break;
        }
    }

    @Override
    public void OnReadQrcode(String number) {
        et_number.setText(number);
    }
}
