package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.adapter.GoodsWareHousingCarAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.GoodsStock;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Float.parseFloat;

public class GoodsWareHousingCarActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.gv_shop_list)
    GridView gvShopList;
    @BindView(R.id.tv_goods_time)
    TextView tvGoodsTime;
    @BindView(R.id.tv_goods_order)
    TextView tvGoodsOrder;
    @BindView(R.id.tv_goods_total_money)
    TextView tvGoodsTotalMoney;
    @BindView(R.id.tv_goods_explain)
    TextView tvGoodsExplain;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.activity_goods_cart)
    LinearLayout activityGoodsCart;
    @BindView(R.id.shop_cart_gv)
    RelativeLayout shopCartGv;


    private GoodsWareHousingCarAdapter adapter;
    private String orderNumber = "";
    private String orderTime = "";
    private String mode = "";
    private String explain = "";
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_ware_housing_car);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initView();
        upListData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘不顶起控件
    }

    private void initTitle() {
        tvCommit.setText("入库");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
                finish();
            }
        });

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsin();
            }
        });
    }

    private void goodsin() {
        if (list_stock.size() > 0) {
            String ids = "";
            String num = "";
            String money = "";
            for (int i = 0; i < list_stock.size(); i++) {
                if (i == list_stock.size() - 1) {
                    ids += list_stock.get(i).getId();
                    num += list_stock.get(i).getNum();
                    money += list_stock.get(i).getMoney();
                } else {
                    ids += list_stock.get(i).getId() + ",";
                    num += list_stock.get(i).getNum() + ",";
                    money += list_stock.get(i).getMoney() + ",";
                }
            }

            Map<String, String> map = new HashMap();
            map.put("orderNo", orderNumber);
            map.put("userid", SharedUtil.getSharedData(GoodsWareHousingCarActivity.this, "userInfoId"));
            map.put("money_s", money);
            map.put("num_s", num);
            map.put("goods_id", ids);
            map.put("remark", explain);//备注
            postToHttp(NetworkUrl.STOCKIN, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    Logger.json(jsonText);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonText);
                        String status = jsonObject.getString("result_status");
                        if (status.equals("SUCCESS")) {
                            flag = true;
                        } else {
                            String result_msg = jsonObject.getString("result_errmsg");
                            Utils.showToast(GoodsWareHousingCarActivity.this, result_msg);
                            flag = false;

                        }
                        changeActiviy();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFail(String message) {
                    Log.d("uuz", "OnFail: ===");
                    flag = false;
                    changeActiviy();
                }
            });
        }else {
            Utils.showToast(GoodsWareHousingCarActivity.this,"你还没用选择入库商品");
        }

    }


    private void changeActiviy() {
        shopCartGv.setVisibility(View.GONE);
        LinearLayout ll_hint = (LinearLayout) findViewById(R.id.ll_pay_hint);
        ImageView iv_hint = (ImageView) findViewById(R.id.iv_pay_hint);
        TextView tv_hint = (TextView) findViewById(R.id.tv_pay_hint);
        ll_hint.setVisibility(View.VISIBLE);
        iv_hint.setSelected(flag);
        tv_hint.setText("入库" + (flag ? "成功" : "失败"));
        if (!flag) {
            new AlertDialog.Builder(GoodsWareHousingCarActivity.this)
                    .setTitle("入库操作失败！")
                    .setMessage("由于未知原因入库操作失败,是否在试一次？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goodsin();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .show();
        }
        if (flag) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCommit.setVisibility(View.INVISIBLE);
                    tvGoodsExplain.setClickable(false);
                    tvGoodsExplain.setBackgroundColor(0x00000000);
                    tvGoodsExplain.setHint("");
                    list_stock.clear();
                }
            });
            showAlert();
//            Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                public void run() {
//                    Intent intent = new Intent(GoodsWareHousingCarActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                }
//            };
//            timer.schedule(task, 2500);
        }
    }
    protected void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(GoodsWareHousingCarActivity.this);
        dialog.setTitle("提示:");
        dialog.setMessage("操作成功");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(GoodsWareHousingCarActivity.this, MainActivity.class);
               startActivity(intent);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(GoodsWareHousingCarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void initData() {
        adapter = new GoodsWareHousingCarAdapter(GoodsWareHousingCarActivity.this, list_stock);
    }


    private void initView() {
        gvShopList.setAdapter(adapter);
        getOrderNum("RK");
        tvGoodsOrder.setText(orderNumber);
        tvGoodsTime.setText(orderTime);
        gvShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopWindow(position);
            }
        });
        tvGoodsExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPop();

            }
        });


    }

    /**
     * @param first
     * @return
     */
    private String getOrderNum(String first) {
        String str = "";
        switch (robotType) {
            case 1:
                str = terminalSn.substring(9);
                break;
            case 3:
                str = terminalSn.substring(8);
                break;
            case 4:
                str = terminalSn.substring(10);
                break;
            case 8:
                str = terminalSn.substring(11);
                break;

        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");

        String time = simpleDateFormat.format(date);
        orderTime = simpleFormat.format(date);
        orderNumber = first + time + "1" + str;
        return orderNumber;
    }

    private void showPopWindow(final int i) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(GoodsWareHousingCarActivity.this).inflate(R.layout.goods_in_detail, null);
        final EditText etPrice = (EditText) contentView.findViewById(R.id.et_price);//进货价 单价
        final EditText etNum = (EditText) contentView.findViewById(R.id.alert_num);
        final TextView goodsName = (TextView) contentView.findViewById(R.id.goods_in_name);//商品名
        final TextView tvTotalMoney = (TextView) contentView.findViewById(R.id.total_money);//合计金额
        final ImageView iv_add = (ImageView) contentView.findViewById(R.id.iv_click_add);
        final ImageView iv_del = (ImageView) contentView.findViewById(R.id.iv_click_del);
        final Spinner spinner = (Spinner) contentView.findViewById(R.id.goods_spinner);
        Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        InputFilter[] filters2 = {new MyTextFilter2()};
        etPrice.setFilters(filters2);
        etNum.setFilters(filters2);
        String price = list_stock.get(i).getActualPrice() + "";
        goodsName.setText(list_stock.get(i).getName());
        etNum.setText(list_stock.get(i).getNum() + "");
        etPrice.setText(price);
        tvTotalMoney.setText(list_stock.get(i).getNum() * list_stock.get(i).getActualPrice() + "");//总价格

        List<String> mItems = new ArrayList<>();
        mItems.add("默认");
        mItems.add("赠品");
        // 建立Adapter并且绑定数据源
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(GoodsWareHousingCarActivity.this, android.R.layout.simple_spinner_item, mItems);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                mode = spinner.getSelectedItem().toString();

            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(etNum.getText().toString())) {
                    num = 0;
                } else {
                    num = parseFloat(etNum.getText().toString());
                }
                num += 1;
                etNum.requestFocus();
                etNum.setText(num + "");
                if (num > 0) {
                    iv_del.setEnabled(true);
                }
            }
        });
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(etNum.getText().toString())) {
                    num = 0;
                } else {
                    num = parseFloat(etNum.getText().toString());
                }
                etNum.requestFocus();
                if (num < 1) {
                    iv_del.setEnabled(false);
                } else {
                    num -= 1;
                    etNum.setText(num + "");
                }
            }
        });
        etPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    etPrice.setText("");
                }
                return false;
            }
        });
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPrice.hasFocus()) {
                    //三元运算符
                    float price = TextUtils.isEmpty(etPrice.getText().toString()) ? 0 : parseFloat(etPrice.getText().toString());
                    float num = TextUtils.isEmpty(etNum.getText().toString()) ? 0 : parseFloat(etNum.getText().toString());
                    tvTotalMoney.setText(price * num + "");
                }
            }
        });

        etNum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    etNum.setText("");
                }
                return false;
            }
        });

        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNum.setText("");
                }
            }
        });



        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etNum.hasFocus()) {
                    float price = TextUtils.isEmpty(etPrice.getText().toString()) ? 0 : parseFloat(etPrice.getText().toString());
                    float num = TextUtils.isEmpty(etNum.getText().toString()) ? 0 : parseFloat(etNum.getText().toString());
                    tvTotalMoney.setText(price * num + "");
                }
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                window.dismiss();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(etNum.getText().toString()) && !TextUtils.isEmpty(etPrice.getText().toString())) {

                    float num = parseFloat(etNum.getText().toString());
                    float acPrice = parseFloat(etPrice.getText().toString());
                    if (num >= 0) {
                        list_stock.get(i).setActualPrice(acPrice);
                        list_stock.get(i).setGoodsModel(mode);
                        list_stock.get(i).setNum(num);
                        list_stock.get(i).setMoney(Utils.getNum2(Float.parseFloat(tvTotalMoney.getText().toString())));
                        //都没有发生改变不需要重复操作
                        //list_stock.get(i).setId(list_G.get(i).getId());
                        //list_stock.get(i).setName(list_G.get(i).getC_GoodsName());
                        //list_stock.get(i).setPrice(list_G.get(i).getN_DePrice());
                        // list_stock.get(i).setGoodsNO(list_G.get(i).getC_GoodsNO());

                        mIterator();
                        adapter.notifyDataSetChanged();
                        upListData();
                    }
                    window.dismiss();
                } else {
                    Utils.showToast(GoodsWareHousingCarActivity.this, "请填写正确的数量");
                }
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth(w * 3 / 4);
        window.setHeight((int) (h * 0.6));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_goods_cart), Gravity.CENTER, 0, 0);
    }

    private void upListData() {
        float num = 0, money = 0;
        for (int i = 0; i < list_stock.size(); i++) {
            num += list_stock.get(i).getNum();
            money += list_stock.get(i).getMoney();
        }
        tvGoodsTotalMoney.setText("￥" + Utils.getNumStr(parseFloat(Utils.getStringOutE(money + ""))));

        adapter.notifyDataSetChanged();
    }


    //集合迭代器  当集合中元素数量属性的值为0 时，移除该元素
    private void mIterator() {
        Iterator<GoodsStock> goodsStockIterator = list_stock.iterator();
        while (goodsStockIterator.hasNext()) {
            GoodsStock goodsStock = goodsStockIterator.next();
            if (goodsStock.getNum() == 0) {
                goodsStockIterator.remove();
            }
        }
    }

    private void clickBack() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        clickBack();
        super.onBackPressed();
    }

    public void delProject(int position) {
        list_stock.remove(position);
        upListData();
    }


    private void inputPop() {
        final EditText inputServer = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("备注说明").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                explain = inputServer.getText().toString();
                                tvGoodsExplain.setText(explain);
                            }
                        }
                );
            }
        });
        builder.show();
    }

}
