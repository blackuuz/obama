package com.ksk.obama.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyCountListAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.model.BuyCountList;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class BuyCountListActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_oldMoney;
    private TextView tv_type;
    private EditText et_shopname;
    private ListView lv_project;
    private List<BuyCountList.GoodsListBean> list = new ArrayList<>();
    private List<BuyCountList.GoodsClassBean> list_type = new ArrayList<>();
    private BuyCountListAdapter adapter;
    private BuyCountList countList;
    private String memid;
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
    private String oldIntegral;
    private String uid = "";
    private String cardType;
    private int ctype = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_count_list);
        initTime();
        initTitale();
        initView();
        initData();
        getHttpData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(BuyCountListActivity.this)) {
            queryDB(true);
        } else {
            int n = queryDB(false);
            Utils.showToast(BuyCountListActivity.this, "当前无网络,您有" + n + "单子未上传");
        }
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
        tv_print.setText("购物车");
        title_name.setText("会员购次");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_buy.size() > 0) {
                    Intent intent = new Intent(BuyCountListActivity.this, BuyCountCartActivity.class);
                    intent.putExtra("memid", memid);
                    intent.putExtra("cardNum", tv_cardNum.getText().toString());
                    intent.putExtra("name", tv_name.getText().toString());
                    intent.putExtra("oldm", tv_oldMoney.getText().toString());
                    intent.putExtra("discount", countList.getDiscount());
                    intent.putExtra("integral", countList.getIntegral());
                    intent.putExtra("url", NetworkUrl.SENDBUYCOUNT);
                    intent.putExtra("pwd", password);
                    intent.putExtra("uid", uid);
                    intent.putExtra("oldi", oldIntegral);
                    intent.putExtra("ctype", ctype);
                    startActivity(intent);
                } else {
                    Utils.showToast(BuyCountListActivity.this, "您还没选择购买项目");
                }
            }
        });
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_oldMoney = (TextView) findViewById(R.id.tv_shop_money);
        tv_type = (TextView) findViewById(R.id.tv_shop_type);
        et_shopname = (EditText) findViewById(R.id.et_shop_name);
        lv_project = (ListView) findViewById(R.id.lv_buy_list);
        findViewById(R.id.btn_shop_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpData();
            }
        });

        findViewById(R.id.ll_shop_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_type.size() > 1) {
                    AddPopWindow addPopWindow = new AddPopWindow(BuyCountListActivity.this);
                    addPopWindow.showPopupWindow(findViewById(R.id.ll_shop_type));
                }
            }
        });

        lv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toCalculate(i);
            }
        });

    }

    private void initData() {
        if (getIntent() != null) {
            ReadCardInfo.ResultDataBean cardInfo = getIntent().getExtras().getParcelable("infoma");
            uid = getIntent().getExtras().getString("uid");
            float oldMoney = Float.parseFloat(cardInfo.getN_AmountAvailable());
            oldIntegral = cardInfo.getN_IntegralAvailable();
            memid = cardInfo.getId();
            password = cardInfo.getC_Password();
            String cardNum = cardInfo.getC_CardNO();
            tv_cardNum.setText(cardNum);
            tv_name.setText(cardInfo.getC_Name());
            tv_oldMoney.setText("￥" + oldMoney);
            cardType = cardInfo.getC_PriceClass();
            switch (cardType) {
                case "零售价":
                    ctype = 0;
                    break;
                case "会员价A":
                    ctype = 1;
                    break;
                case "会员价B":
                    ctype = 2;
                    break;
                case "会员价C":
                    ctype = 3;
                    break;
                case "会员价D":
                    ctype = 4;
                    break;
            }
        }
    }

    private int queryDB(boolean flag) {
        Connector.getDatabase();
        List<BuyCountDb> list = DataSupport.findAll(BuyCountDb.class);
        if (list != null && list.size() > 0 && flag) {
            startActivity(new Intent(BuyCountListActivity.this, BuyCountSupplementActivity.class));
        }

        return list.size();
    }

    private void getHttpData() {

        if (list != null) {
            list.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        String shopname = et_shopname.getText().toString();
        final String type = tv_type.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("c_CardNO", tv_cardNum.getText().toString());
        map.put("dbName", SharedUtil.getSharedData(BuyCountListActivity.this, "dbname"));
        map.put("goodsfind", shopname);
        map.put("goodsclass", type);
        map.put("UserInfoId", SharedUtil.getSharedData(BuyCountListActivity.this, "userInfoId"));
        postToHttp(NetworkUrl.BUYCOUNT, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                countList = new Gson().fromJson(jsonText, BuyCountList.class);
                if (countList.getResult_stadus().equals("SUCCESS")) {
                    list = countList.getGoods_list();
                    resetList();
                    if (list_type.size() == 0) {
                        list_type = countList.getGoods_class();
                        if (list_type != null && list_type.size() > 0) {
                            tv_type.setText(list_type.get(0).getClassX());
                        }
                    }
                    adapter = new BuyCountListAdapter(BuyCountListActivity.this, list, ctype);
                    lv_project.setAdapter(adapter);
                } else {
                    Utils.showToast(BuyCountListActivity.this, countList.getResult_errmsg());
                }
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void resetList() {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setNum(0);
                for (int j = 0; j < list_buy.size(); j++) {
                    if (list.get(i).getId().equals(list_buy.get(j).getId())) {
                        list.get(i).setNum(list_buy.get(j).getNum());
                    }
                }
            }
        }
    }

    private void getNowTime(EditText[] et, int position) {
        String str1 = list.get(position).getValidTime();

        for (int i = 0; i < list_buy.size(); i++) {
            if (list.get(position).getId().equals(list_buy.get(i).getId())) {
                str1 = list_buy.get(i).getValidTime();
                break;
            }
        }
        String[] str = str1.split("-");
        curyear = parseInt(str[0]);
        curmonth = Integer.parseInt(str[1]);
        curday = Integer.parseInt(str[2]);
        mycalendar.set(Calendar.YEAR, ocuryear);//先指定年份
        mycalendar.set(Calendar.MONTH, ocurmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);
        et[0].setText(curyear + "");
        et[1].setText(curmonth + "");
        et[2].setText(curday + "");
    }

    public void toCalculate(final int i) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(BuyCountListActivity.this).inflate(R.layout.number_update_count, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        setValidTimes(contentView, i);
        editText.setText(list.get(i).getNum() + "");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.selectAll();
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
        window.showAtLocation(findViewById(R.id.activity_buy_count_list), Gravity.CENTER, 0, 0);
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
        final Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                window.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    boolean flag = true;
                    float num = Float.parseFloat(editText.getText().toString());
                    if (num > 0) {
                        BuyCount buyCount = new BuyCount();
                        for (int j = 0; j < list_buy.size(); j++) {
                            if (list_buy.get(j).getId().equals(list.get(i).getId())) {
                                buyCount = list_buy.get(j);
                                flag = false;
                                break;
                            }
                        }
                        float price = 0f;
                        switch (ctype) {//物品单价
                            case 0:
                                price = list.get(i).getN_PriceRetail();
                                break;
                            case 1:
                                price = list.get(i).getN_PriceMemberA();
                                break;
                            case 2:
                                price = list.get(i).getN_PriceMemberB();
                                break;
                            case 3:
                                price = list.get(i).getN_PriceMemberC();
                                break;
                            case 4:
                                price = list.get(i).getN_PriceMemberD();
                                break;
                            default:
                                price = list.get(i).getN_PriceRetail();
                                break;
                        }
                        buyCount.setC_type(cardType);
                        buyCount.setId(list.get(i).getId());
                        buyCount.setName(list.get(i).getC_GoodsName());
                        buyCount.setPrice(price);
                        buyCount.setMoney(list.get(i).getN_PriceRetail() * num);
                        buyCount.setNum(num);
                        buyCount.setDazhe(list.get(i).getN_DiscountValue());
                        buyCount.setJifen(list.get(i).getN_IntegralValue());
                        if (ctype != 0) {//判断卡的消费类型
                            buyCount.setMoneyin(price * num);
                        } else {
                            if (list.get(i).getN_DiscountValue() != 0) {
                                int n = (int) (list.get(i).getN_PriceRetail() * num * countList.getDiscount() * 100);
                                buyCount.setMoneyin(n / 100f);
                            } else {
                                buyCount.setMoneyin(list.get(i).getN_PriceRetail() * num);
                            }
                        }
                        if (curday < 10) {
                            buyCount.setValidTime(curyear + "-" + curmonth + "-0" + curday);
                        } else {
                            buyCount.setValidTime(curyear + "-" + curmonth + "-" + curday);
                        }
                        if (flag) {
                            list_buy.add(buyCount);
                        }
                        list.get(i).setNum(num);
                        adapter.notifyDataSetChanged();
                    }

                    window.dismiss();
                } else {
                    Utils.showToast(BuyCountListActivity.this, "请填写正确的数量");
                }
            }
        });

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

    private class AddPopWindow extends PopupWindow {
        private View conentView;

        public AddPopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popuwindow_card_type, null);
            ListView lv_type = (ListView) conentView.findViewById(R.id.lv_card_type);
            lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tv_type.setText(list_type.get(position).getClassX());
                    dismiss();
                    getHttpData();
                }
            });

            String str[] = new String[list_type.size()];
            for (int i = 0; i < list_type.size(); i++) {
                str[i] = list_type.get(i).getClassX();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BuyCountListActivity.this,
                    android.R.layout.simple_list_item_1, str);
            lv_type.setAdapter(adapter);
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            this.setContentView(conentView);
            this.setWidth(w / 4 - 23);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            this.update();
            ColorDrawable dw = new ColorDrawable();
            this.setBackgroundDrawable(dw);
        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent);
            } else {
                this.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list_buy.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetList();
        adapter.notifyDataSetChanged();
    }
}
