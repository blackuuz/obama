package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.model.PrintPage;

import java.util.ArrayList;
import java.util.List;


public class IntegralExchangeHintActivity extends BasePrintActivity implements IPrintErrorCallback{

    private String time;
    private String order;
    private String cardNum;
    private String name;
    private String del;
    private String integral;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchange_hint);
        setOnPrintError(this);
        initTitale();
        initView();
        initData();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
                finish();
            }
        });
        tv_print.setText("重打印");
        title_name.setText("积分兑换");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printInfo();
            }
        });
    }

    private void initView() {
        tv0 = (TextView) findViewById(R.id.tv_integral_hint_0);
        tv1 = (TextView) findViewById(R.id.tv_integral_hint_1);
        tv2 = (TextView) findViewById(R.id.tv_integral_hint_2);
        tv3 = (TextView) findViewById(R.id.tv_integral_hint_3);
        tv4 = (TextView) findViewById(R.id.tv_integral_hint_4);
    }

    private void initData() {
        if (getIntent() != null) {
            time = getIntent().getStringExtra("time");
            order = getIntent().getStringExtra("order");
            cardNum = getIntent().getStringExtra("cardNum");
            name = getIntent().getStringExtra("name");
            del = getIntent().getStringExtra("del");
            integral = getIntent().getStringExtra("integral");

            tv0.setText("会员卡号:" + cardNum);
            tv1.setText("会员姓名:" + name);
            tv2.setText("支付方式:会员卡");
            tv3.setText("扣除积分:" + del);
            tv4.setText("剩余积分:" + integral);
        }
    }

    private void printInfo() {
        List<PrintPage> list_son = new ArrayList<>();
        for (int i = 0; i < list_integral.size(); i++) {
            PrintPage page = new PrintPage();
            page.setMoney(list_integral.get(i).getCount() + "");
            page.setNum(list_integral.get(i).getNum() + "");
            page.setPrice(list_integral.get(i).getInregral() + "");
            page.setName(list_integral.get(i).getName());
            list_son.add(page);
        }
        List<String> listp = new ArrayList<>();
        listp.add("时间:" + time);
        listp.add("订单号:" + order);
        listp.add("会员卡号:" + cardNum);
        listp.add("会员姓名:" + name);
        listp.add("son");
        listp.add("支付方式:会员卡");
        listp.add("扣除积分:" + del);
        listp.add("剩余积分:" + integral);

        printPage("积分兑换小票", listp, list_son, true);

    }

    private void changeActivity() {
        Intent intent = new Intent(IntegralExchangeHintActivity.this, ReadCardInfoActivity.class);
        intent.putExtra("type", 3);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        changeActivity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list_integral.clear();
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_buy_count_list);
    }

    private boolean isclick;
    private void printError(int id) {
        isclick = true;
        final PopupWindow window = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_print_hint, null);
        view.findViewById(R.id.btn_print_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        window.setContentView(view);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        window.setTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (w * 0.8));
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isclick) {
                    isclick = false;
                    printInfo();
                }

            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }

}
