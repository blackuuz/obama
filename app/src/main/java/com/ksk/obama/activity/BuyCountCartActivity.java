package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyCountCartAdapter;
import com.ksk.obama.utils.Utils;

import java.util.Calendar;
import java.util.Locale;

public class BuyCountCartActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_oldMoney;
    private TextView tv_payShould;
    private TextView tv_pay_a;
    private TextView tv_payNum;
    private TextView tv_delMoney;
    private GridView gv_project;
    private float discount;
    private float inte;
    private String memid;
    private BuyCountCartAdapter adapter;
    private String net_url = "";

    private EditText[] ets = new EditText[3];
    private int curyear;
    private int curmonth;
    private int curday;
    private int ocuryear;
    private int ocurmonth;
    private int ocurday;
    private int daysCountOfMonth;
    private Calendar mycalendar;
    private String password;
    private float getInte = 0;
    private String oldIntegral;
    private String uid="";
    private int cType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initTime();
        initTitale();
        initView();
        initData();
        upListData();
    }

    private void initTime() {
        mycalendar = Calendar.getInstance(Locale.CHINA);
        ocuryear = mycalendar.get(Calendar.YEAR);
        ocurmonth = mycalendar.get(Calendar.MONTH) + 1;
        ocurday = mycalendar.get(Calendar.DAY_OF_MONTH);
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
        tv_print.setText("结算");
        title_name.setText("购物车");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAcivity();
            }
        });
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_oldMoney = (TextView) findViewById(R.id.tv_shop_money);
        gv_project = (GridView) findViewById(R.id.gv_shop_list);
        tv_payShould = (TextView) findViewById(R.id.tv_shop_pay_should);
        tv_pay_a = (TextView) findViewById(R.id.tv_shop_pay_au);
        tv_payNum = (TextView) findViewById(R.id.tv_shop_pay_num);
        tv_delMoney = (TextView) findViewById(R.id.tv_shop_pay_del);

        gv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toCalculate(position);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("uid");
            memid = intent.getStringExtra("memid");
            password = intent.getStringExtra("pwd");
            String cardNum = intent.getStringExtra("cardNum");
            String name = intent.getStringExtra("name");
            String oldm = intent.getStringExtra("oldm");
            discount = intent.getFloatExtra("discount", 1);
            inte = intent.getFloatExtra("integral", 0);
            net_url = intent.getStringExtra("url");
            oldIntegral = intent.getStringExtra("oldi");
            cType = intent.getIntExtra("ctype",0);
            tv_cardNum.setText(cardNum);
            tv_name.setText(name);
            tv_oldMoney.setText(oldm);
        }
        adapter = new BuyCountCartAdapter(BuyCountCartActivity.this, BuyCountListActivity.list_buy,cType);
        gv_project.setAdapter(adapter);
    }

    private void changeAcivity() {
        Intent intent = new Intent(BuyCountCartActivity.this, PayBuyCountActivity.class);
        intent.putExtra("name", tv_name.getText().toString());
        intent.putExtra("cardNum", tv_cardNum.getText().toString());
        intent.putExtra("memid", memid);
        intent.putExtra("uid", uid);
        intent.putExtra("num", tv_payNum.getText().toString());
        intent.putExtra("should", tv_payShould.getText().toString());
        intent.putExtra("payau", tv_pay_a.getText().toString());
        intent.putExtra("inte", getInte + "");
        intent.putExtra("del", tv_delMoney.getText().toString());
        intent.putExtra("old", tv_oldMoney.getText().toString().substring(1));
        intent.putExtra("oldi", oldIntegral);
        intent.putExtra("url", net_url);
        intent.putExtra("pwd", password);
        startActivity(intent);
    }

    public void toCalculate(final int position) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(BuyCountCartActivity.this).inflate(R.layout.number_update_count, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        setValidTimes(contentView, position);
        editText.setText(list_buy.get(position).getNum() + "");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.selectAll();
            }
        });
        ImageView iv_add = (ImageView) contentView.findViewById(R.id.iv_click_add);
        final ImageView iv_del = (ImageView) contentView.findViewById(R.id.iv_click_del);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    num = 0;
                } else {
                    num = Float.parseFloat(editText.getText().toString());
                }
                num += 1;
                editText.setText(num + "");
                if (num > 0) {
                    iv_del.setEnabled(true);
                }
            }
        });

        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    num = 0;
                } else {
                    num = Float.parseFloat(editText.getText().toString());
                }
                if (num < 1) {
                    iv_del.setEnabled(false);
                } else {
                    num -= 1;
                    editText.setText(num + "");
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.6));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);

        Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
        ImageView btn_cancel = (ImageView) contentView.findViewById(R.id.alert_back_0);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                window.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    float numormoney = Float.parseFloat(editText.getText().toString());
                    list_buy.get(position).setNum(numormoney >= 1 ? numormoney : 1);
                    list_buy.get(position).setMoney(list_buy.get(position).getPrice() * (numormoney > 1 ? numormoney : 1));
                    if (list_buy.get(position).getDazhe() != 0) {
                        int moneyin = (int) (list_buy.get(position).getPrice() * (numormoney > 1 ? numormoney : 1) * discount * 100);
                        list_buy.get(position).setMoneyin(moneyin / 100f);
                    } else {
                        list_buy.get(position).setMoneyin(list_buy.get(position).getPrice() * (numormoney > 1 ? numormoney : 1));
                    }
                    if (curday < 10) {
                        list_buy.get(position).setValidTime(curyear + "-" + curmonth + "-0" + curday);
                    } else {
                        list_buy.get(position).setValidTime(curyear + "-" + curmonth + "-" + curday);
                    }
                    upListData();
                    window.dismiss();
                } else {
                    Utils.showToast(BuyCountCartActivity.this, "请填写正确的数量");
                }
            }
        });

    }

    private void getNowTime(EditText[] et, int position) {
        String str1 = list_buy.get(position).getValidTime();
        String[] str = str1.split("-");
        curyear = Integer.parseInt(str[0]);
        curmonth = Integer.parseInt(str[1]);
        curday = Integer.parseInt(str[2]);
        mycalendar.set(Calendar.YEAR, ocuryear);//先指定年份
        mycalendar.set(Calendar.MONTH, ocurmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);
        et[0].setText(curyear + "");
        et[1].setText(curmonth + "");
        et[2].setText(curday + "");
    }

    private void setValidTimes(View contentView, int position) {
        EditText et_year = (EditText) contentView.findViewById(R.id.et_time_year);
        EditText et_month = (EditText) contentView.findViewById(R.id.et_time_mouth);
        EditText et_day = (EditText) contentView.findViewById(R.id.et_time_day);
        ets[0] = et_year;
        ets[1] = et_month;
        ets[2] = et_day;
        getNowTime(ets, position);
        ImageView iv_add_y = (ImageView) contentView.findViewById(R.id.iv_click_add_year);
        ImageView iv_add_m = (ImageView) contentView.findViewById(R.id.iv_click_add_mouth);
        ImageView iv_add_d = (ImageView) contentView.findViewById(R.id.iv_click_add_day);
        ImageView iv_del_y = (ImageView) contentView.findViewById(R.id.iv_click_del_year);
        ImageView iv_del_m = (ImageView) contentView.findViewById(R.id.iv_click_del_mouth);
        ImageView iv_del_d = (ImageView) contentView.findViewById(R.id.iv_click_del_day);
        iv_add_y.setOnClickListener(this);
        iv_add_m.setOnClickListener(this);
        iv_add_d.setOnClickListener(this);
        iv_del_y.setOnClickListener(this);
        iv_del_m.setOnClickListener(this);
        iv_del_d.setOnClickListener(this);
    }


    private void getDays() {
        mycalendar.set(Calendar.YEAR, curyear);//先指定年份
        mycalendar.set(Calendar.MONTH, curmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_click_add_year:
                curyear += 1;
                getDays();
                if (curday >= daysCountOfMonth) {
                    curday = daysCountOfMonth;
                    ets[2].setText(curday + "");
                }
                ets[0].setText(curyear + "");
                break;
            case R.id.iv_click_del_year:
                if (curyear > ocuryear) {
                    curyear -= 1;
                }
                getDays();
                if (curday >= daysCountOfMonth) {
                    curday = daysCountOfMonth;
                    ets[2].setText(curday + "");
                }
                ets[0].setText(curyear + "");
                break;
            case R.id.iv_click_add_mouth:
                //ets[2].setText(1+"");
                //curday = 1;
                if (curyear == ocuryear) {
                    if (curmonth == 12) {
                        curmonth = ocurmonth;
                    } else {
                        curmonth += 1;
                    }
                } else {
                    if (curmonth == 12) {
                        curmonth = 1;
                    } else {
                        curmonth += 1;
                    }
                }
                getDays();
                if (curday >= daysCountOfMonth) {
                    curday = daysCountOfMonth;
                    ets[2].setText(curday + "");
                }
                ets[1].setText(curmonth + "");
                break;
            case R.id.iv_click_del_mouth:
                //ets[2].setText(1+"");
                // curday = 1;
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        curmonth = 12;
                    } else {
                        curmonth -= 1;
                    }
                } else {
                    if (curmonth == 1) {
                        curmonth = 12;
                    } else {
                        curmonth -= 1;
                    }
                }
                getDays();
                if (curday >= daysCountOfMonth) {//
                    curday = daysCountOfMonth;
                    ets[2].setText(curday + "");
                }
                ets[1].setText(curmonth + "");
                break;
            case R.id.iv_click_add_day:
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        if (curday >= daysCountOfMonth) {
                            curday = ocurday;
                        } else {
                            curday += 1;
                        }
                    } else {
                        if (curday >= daysCountOfMonth) {
                            curday = 1;
                        } else {
                            curday += 1;
                        }
                    }
                } else {
                    if (curday >= daysCountOfMonth) {
                        curday = 1;
                    } else {
                        curday += 1;
                    }
                }
                ets[2].setText(curday + "");
                break;
            case R.id.iv_click_del_day:
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        if (curday == ocurday) {
                            curday = daysCountOfMonth;
                        } else {
                            curday -= 1;
                        }
                    } else {
                        if (curday == 1) {
                            curday = daysCountOfMonth;
                        } else {
                            curday -= 1;
                        }
                    }
                } else {
                    if (curday == 1) {
                        curday = daysCountOfMonth;
                    } else {
                        curday -= 1;
                    }
                }
                ets[2].setText(curday + "");
                break;
        }
        mycalendar.set(Calendar.YEAR, curyear);//先指定年份
        mycalendar.set(Calendar.MONTH, curmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);
    }

    public void delProject(int position) {
        BuyCountListActivity.list_buy.remove(position);
        upListData();
    }

    private void upListData() {
        float num = 0, count = 0, countIn = 0;
        getInte = 0;
        for (int i = 0; i < BuyCountListActivity.list_buy.size(); i++) {
            num += BuyCountListActivity.list_buy.get(i).getNum();
            count += BuyCountListActivity.list_buy.get(i).getMoney();
            countIn += BuyCountListActivity.list_buy.get(i).getMoneyin();
            if (BuyCountListActivity.list_buy.get(i).getJifen() != 0) {
                getInte += BuyCountListActivity.list_buy.get(i).getMoneyin() * inte * shopIntegral;
            }
        }

        tv_payNum.setText(num + "");
        tv_payShould.setText(Utils.getNumStr(count) + "");
        tv_pay_a.setText(Utils.getNumStr(countIn) + "");
        float ft = count - countIn;
        tv_delMoney.setText(Utils.getNumStr(ft));
        adapter.notifyDataSetChanged();
    }

}
