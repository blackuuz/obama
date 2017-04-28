package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.utils.Utils;

public class BuyShopUseIntegralActivity extends BaseActivity {

    private TextView tv_money;
    private float money = 0;
    private float values = 0.01f;
    private float oldIntegral = 0;
    private float delIntegral = 0;
    private float oldMoney = 0;
    private String name;
    private String cardNum;
    private EditText et_integral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_shop_use_integral);
        initData();
        initTitale();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            cardNum = getIntent().getStringExtra("cardNum");
            name = getIntent().getStringExtra("name");
            oldIntegral = getIntent().getFloatExtra("integral", 0);
            delIntegral = getIntent().getFloatExtra("delIntegral", 0);
            money = getIntent().getFloatExtra("delmoney", 0);
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
        title_name.setText("积分抵用");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("delM", money);
                intent.putExtra("delInte", delIntegral);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView() {
        TextView tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        TextView tv_name = (TextView) findViewById(R.id.tv_shop_name);
        TextView tv_oldIntegral = (TextView) findViewById(R.id.tv_shop_money);
        TextView tv_hint = (TextView) findViewById(R.id.tv_use_integral_hint);
        tv_money = (TextView) findViewById(R.id.tv_use_integral_money);
        et_integral = (EditText) findViewById(R.id.et_use_integral_num);

        et_integral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeMoney();
            }
        });

        tv_cardNum.setText(cardNum);
        tv_name.setText(name);
        tv_oldIntegral.setText(oldIntegral + "分");
        et_integral.setText((delIntegral > 0 ? delIntegral : "") + "");
        tv_money.setText((money > 0 ? money : "0.00") + "");
    }

    private void changeMoney() {
        String str = et_integral.getText().toString();
        delIntegral = Float.parseFloat(TextUtils.isEmpty(str) ? "0" : str);
        if (oldIntegral < delIntegral) {
            delIntegral = (int) oldIntegral / 1;
            et_integral.setText(delIntegral + "");
        }

        money = delIntegral * values;
        if (oldMoney < money) {
            money = oldMoney;
            et_integral.setText(money * 100 + "");
        }
        tv_money.setText(Utils.getNumStr(money));
    }


}
