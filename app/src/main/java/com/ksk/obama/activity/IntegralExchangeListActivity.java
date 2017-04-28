package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.IntegralExchangeListAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.IntegralShop;
import com.ksk.obama.model.IntegralShopCount;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegralExchangeListActivity extends BaseActivity {
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_oldMoney;
    private ListView lv_project;
    private String memid;
    private EditText et_shopname;
    private EditText et_integral0;
    private EditText et_integral1;
    private List<IntegralShop.GiftBean> list = new ArrayList<>();
    private IntegralExchangeListAdapter adapter;
    private float integral;
    private String password;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchange_list);
        initTitale();
        initView();
        initData();
        getHttpData();
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
        title_name.setText("积分兑换");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_integral.size() > 0) {
                    Intent intent = new Intent(IntegralExchangeListActivity.this, IntegralExchangeCartActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("cardNum", tv_cardNum.getText().toString());
                    intent.putExtra("name", tv_name.getText().toString());
                    intent.putExtra("oldf", tv_oldMoney.getText().toString());
                    intent.putExtra("integral", integral);
                    bundle.putString("memid", memid);
                    bundle.putString("uid", uid);
                    bundle.putString("pwd", password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Utils.showToast(IntegralExchangeListActivity.this, "您还没选择购买项目");
                }
            }
        });
    }

    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_oldMoney = (TextView) findViewById(R.id.tv_shop_money);
        et_shopname = (EditText) findViewById(R.id.et_change_integral_name);
        et_integral0 = (EditText) findViewById(R.id.et_change_integral_0);
        et_integral1 = (EditText) findViewById(R.id.et_change_integral_1);
        lv_project = (ListView) findViewById(R.id.lv_buy_list);
        findViewById(R.id.et_change_integral_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpData();
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
            integral = Float.parseFloat(cardInfo.getN_IntegralAvailable());
            float oldMoney = Float.parseFloat(cardInfo.getN_IntegralAvailable());
            memid = cardInfo.getId();
            String cardNum = cardInfo.getC_CardNO();
            tv_cardNum.setText(cardNum);
            tv_name.setText(cardInfo.getC_Name());
            tv_oldMoney.setText(oldMoney + "分");
            password = cardInfo.getC_Password();
        }
    }

    private void getHttpData() {
        if (list != null) {
            list.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        String shop = et_shopname.getText().toString();
        String min = et_integral0.getText().toString();
        String max = et_integral1.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("Member_Id", memid);
        map.put("UserId", SharedUtil.getSharedData(IntegralExchangeListActivity.this, "userInfoId"));
        map.put("dbName", SharedUtil.getSharedData(IntegralExchangeListActivity.this, "dbname"));
        map.put("GiftName", shop);
        map.put("minprice", min);
        map.put("maxprice", max);
        postToHttp(NetworkUrl.INTEGRALLIST, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                showHttpData(jsonText);
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(IntegralExchangeListActivity.this, message);
            }
        });
    }

    private void showHttpData(String text) {
        Logger.e(text);
        IntegralShop integralShop = new Gson().fromJson(text, IntegralShop.class);
        if (integralShop.getResult_stadus().equals("SUCCESS")) {
            list = integralShop.getGift();
            if (list != null && list.size() > 0) {
                resetList();
                adapter = new IntegralExchangeListAdapter(IntegralExchangeListActivity.this, list);
                lv_project.setAdapter(adapter);
            } else {
                Utils.showToast(IntegralExchangeListActivity.this, "此条件下没有数据");
            }
        }
    }

    public void toCalculate(final int i) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(IntegralExchangeListActivity.this).inflate(R.layout.number_update, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        window.setWidth((int) (w * 0.75));
        window.setHeight((int) (h * 0.4));
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
                float num = Float.parseFloat(editText.getText().toString());
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
                float num = Float.parseFloat(editText.getText().toString());
                if (num < 1) {
                    iv_del.setEnabled(false);
                } else {
                    num -= 1;
                    editText.setText(num + "");
                }
            }
        });
        Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
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
                        IntegralShopCount shopCount = new IntegralShopCount();
                        for (int j = 0; j < list_integral.size(); j++) {
                            if (list_integral.get(j).getId().equals(list.get(i).getId())) {
                                flag = false;
                                shopCount = list_integral.get(j);
                                break;
                            }
                        }
                        shopCount.setName(list.get(i).getC_Present());
                        shopCount.setNum(num);
                        shopCount.setId(list.get(i).getId());
                        shopCount.setGoodsId(list.get(i).getId());
                        shopCount.setInregral(list.get(i).getN_Integral());
                        shopCount.setCount(list.get(i).getN_Integral() * num);
                        if (flag) {
                            list_integral.add(shopCount);
                        }

                        list.get(i).setNum(num);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    window.dismiss();
                } else {
                    Utils.showToast(IntegralExchangeListActivity.this, "请填写正确的数量");
                }
            }
        });

    }

    private void resetList() {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setNum(0);
                for (int j = 0; j < list_integral.size(); j++) {
                    if (list.get(i).getId().equals(list_integral.get(j).getId())) {
                        list.get(i).setNum(list_integral.get(j).getNum());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list_integral.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resetList();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
