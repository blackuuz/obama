package com.ksk.obama.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.GoodsWareHousingAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.GoodsStock;
import com.ksk.obama.model.GoodsWareHousing;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsWareHousingActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.ll_shop_type)
    LinearLayout llShopType;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.btn_shop_query)
    Button btnShopQuery;
    @BindView(R.id.lv_buy_list)
    ListView lvBuyList;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.activity_goods_in_list)
    LinearLayout activityGoodsInList;

    private GoodsWareHousingAdapter adapter;

    private List<GoodsWareHousing.ResultDataBean.StockSBean> list_G = new ArrayList<>();
    private List<GoodsWareHousing.ResultDataBean.TypeSBean> list_type = new ArrayList<>();
    private String mode = "默认";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_ware_housing);
        ButterKnife.bind(this);
        initTitle();
        initView();
        getHttpData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘不顶起控件
    }

    private void initTitle() {
        titleName.setText("商品入库");
        tvCommit.setText("入库详情");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(list_stock.size()>0)){
                    Utils.showToast(GoodsWareHousingActivity.this,"请选择入库的商品");
                    return;
                }
                Intent intent = new Intent(GoodsWareHousingActivity.this, GoodsWareHousingCarActivity.class);
                startActivityForResult(intent, 1808);
            }
        });
    }

    private void initView() {
        llShopType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_type.size() > 1) {
                    AddPopWindow addPopWindow = new AddPopWindow(GoodsWareHousingActivity.this);
                    addPopWindow.showPopupWindow(findViewById(R.id.ll_shop_type));
                }
            }
        });

        btnShopQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpData();
            }
        });


        lvBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopWindow(position);
            }
        });
    }

    private void getHttpData() {
        if (list_G != null) {
            list_G.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        String find = etShopName.getText().toString();
        String type = tvShopType.getText().toString();
        if (type.equals("全部"))
            type = "";
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(GoodsWareHousingActivity.this, "dbname"));
        // map.put("UserId", SharedUtil.getSharedData(GoodsWareHousingActivity.this, "userInfoId"));
        map.put("i_ShopID", shopId);
        map.put("c_GoodsName", find);
        map.put("c_GoodsClassify", type);
        postToHttp(NetworkUrl.GOODSSTOCK, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                GoodsWareHousing gData = new Gson().fromJson(jsonText, GoodsWareHousing.class);
                if (gData.getResult_stadus().equals("SUCCESS")) {
                    list_G = gData.getResult_data().getStock_s();
                    resetList();
                    if (list_type.size() == 0) {
                        list_type = gData.getResult_data().getType_s();
                        Log.d("uuz", "list_type size : " + list_type.size());
                        if (list_type != null && list_type.size() > 0) {
                            tvShopType.setText(list_type.get(0).getC_Value());
                            Log.d("uuz", "0号元素名 : " + list_type.get(0).getC_Value());
                        }
                    }
                    adapter = new GoodsWareHousingAdapter(GoodsWareHousingActivity.this, list_G);
                    lvBuyList.setAdapter(adapter);
                } else {
                    Utils.showToast(GoodsWareHousingActivity.this, gData.getResult_errmsg());
                }

                Logger.e(jsonText);
                Logger.json(jsonText);
            }

            @Override
            public void OnFail(String message) {
                Log.d("uuz", "OnFail: ");
            }
        });

    }

    private void resetList() {
        if (list_G != null) {
            for (int i = 0; i < list_G.size(); i++) {
                list_G.get(i).setNum(0);
                list_G.get(i).setAcPrice(-1);
                list_G.get(i).setTotalMoney(-1);
                for (int j = 0; j < list_stock.size(); j++) {
                    if (list_G.get(i).getGoodsid().equals(list_stock.get(j).getId())) {
                        list_G.get(i).setNum(list_stock.get(j).getNum());
                        list_G.get(i).setAcPrice(list_stock.get(j).getActualPrice());
                        list_G.get(i).setTotalMoney(list_stock.get(j).getMoney());
                    }
                }
            }
        }
    }
    private void showPopWindow(final int i) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(GoodsWareHousingActivity.this).inflate(R.layout.goods_in_detail, null);
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
        String money = 0 + "";
        String price = list_G.get(i).getN_DePrice() + "";
        if (list_G.get(i).getTotalMoney() != -1) {
            money = list_G.get(i).getTotalMoney() + "";
        }
        if (list_G.get(i).getAcPrice() != -1) {
            price = list_G.get(i).getAcPrice() + "";
        }
        goodsName.setText(list_G.get(i).getC_GoodsName());
        etNum.setText(list_G.get(i).getNum() + "");
        etPrice.setText(price);
        tvTotalMoney.setText(money);//总价格

        List<String> mItems = new ArrayList<>();
        mItems.add("默认");
        mItems.add("赠品");
        // 建立Adapter并且绑定数据源
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(GoodsWareHousingActivity.this, android.R.layout.simple_spinner_item, mItems);
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
                    num = Float.parseFloat(etNum.getText().toString());
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
                    num = Float.parseFloat(etNum.getText().toString());
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
                    float price = TextUtils.isEmpty(etPrice.getText().toString()) ? 0 : Float.parseFloat(etPrice.getText().toString());
                    float num = TextUtils.isEmpty(etNum.getText().toString()) ? 0 : Float.parseFloat(etNum.getText().toString());
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
                    float price = TextUtils.isEmpty(etPrice.getText().toString()) ? 0 : Float.parseFloat(etPrice.getText().toString());
                    float num = TextUtils.isEmpty(etNum.getText().toString()) ? 0 : Float.parseFloat(etNum.getText().toString());
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
                    boolean flag = true;
                    float num = Float.parseFloat(etNum.getText().toString());
                    float acPrice = Float.parseFloat(etPrice.getText().toString());
                    if (num >= 0) {
                        GoodsStock goodsStock = new GoodsStock();
                        for (int j = 0; j < list_stock.size(); j++) {
                            if (list_stock.get(j).getId().equals(list_G.get(i).getGoodsid())) {
                                goodsStock = list_stock.get(j);
                                flag = false;
                                break;
                            }
                        }
                        goodsStock.setId(list_G.get(i).getGoodsid());
                        goodsStock.setName(list_G.get(i).getC_GoodsName());
                        goodsStock.setPrice(list_G.get(i).getN_DePrice());
                        goodsStock.setActualPrice(acPrice);
                        goodsStock.setGoodsModel(mode);
                        goodsStock.setNum(num);
                        goodsStock.setGoodsNO(list_G.get(i).getC_GoodsNO());
                        goodsStock.setMoney(Utils.getNum2(Float.parseFloat(tvTotalMoney.getText().toString())));
                        if (flag) {
                            list_stock.add(goodsStock);

                        }
                        list_G.get(i).setNum(num);
                        list_G.get(i).setAcPrice(acPrice);
                        list_G.get(i).setTotalMoney(Float.parseFloat(tvTotalMoney.getText().toString()));
                        adapter.notifyDataSetChanged();
                        mIterator();
                    }
                    window.dismiss();
                } else {
                    Utils.showToast(GoodsWareHousingActivity.this, "请填写正确的数量");
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
        window.showAtLocation(findViewById(R.id.activity_goods_in_list), Gravity.CENTER, 0, 0);
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
                    tvShopType.setText(list_type.get(position).getC_Value());
                    dismiss();
                    getHttpData();
                }
            });

            String str[] = new String[list_type.size()];
            for (int i = 0; i < list_type.size(); i++) {
                str[i] = list_type.get(i).getC_Value();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(GoodsWareHousingActivity.this,
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
        list_stock.clear();
    }

    private void mIterator() {
        Iterator<GoodsStock> goodsStockIterator = list_stock.iterator();
        while (goodsStockIterator.hasNext()) {
            GoodsStock goodsStock = goodsStockIterator.next();
            if (goodsStock.getNum() == 0) {
                goodsStockIterator.remove();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1808 && resultCode == RESULT_OK) {
            resetList();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}



