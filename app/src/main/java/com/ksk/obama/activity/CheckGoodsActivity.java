package com.ksk.obama.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.CheckGoodsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.CheckGoods;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CheckGoodsActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.ll_goods_type)
    LinearLayout llGoodsType;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.btn_shop_query)
    Button btnShopQuery;
    @BindView(R.id.lv_buy_list)
    ListView lvBuyList;
    @BindView(R.id.shop_check_goods)
    RelativeLayout shopCheckGoods;
    @BindView(R.id.tv_check_num)
    TextView tvCheckNum;
    @BindView(R.id.tv_goods_total_money)
    TextView tvGoodsTotalMoney;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.activity_check_goods)
    LinearLayout activityCheckGoods;
    @BindView(R.id.ll_check_goods)
    LinearLayout llCheckGoods;
    @BindView(R.id.ll_check_text)
    LinearLayout llCheckText;
    private List<CheckGoods.ResultDataBean.ShopSBean> list_shop = new ArrayList<>();
    private List<CheckGoods.ResultDataBean.CountSBean> list_c = new ArrayList<>();
    private List<CheckGoods.ResultDataBean.StockSBean> list_goods = new ArrayList<>();
    private static List<String> list_goodsNum = new ArrayList<>();
    private List<CheckGoods.ResultDataBean.TypeSBean> list_type = new ArrayList<>();

    private CheckGoodsAdapter adapter;

    private int x = 0;
    private String str[];
    private float goods_nums = 0f;
    private float change_nums = 0f;
    private boolean flag_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_goods);
        ButterKnife.bind(this);
        initTitle();
        initView();
        getHttpData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    private void initTitle() {
        titleName.setText("商品盘点");
        tvCommit.setText("保存");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void initView() {
        llGoodsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_type.size() > 1) {
                    x = 1;
                    AddPopWindow addPopWindow = new AddPopWindow(CheckGoodsActivity.this, v);

                    addPopWindow.showPopupWindow(findViewById(R.id.ll_goods_type));
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
                changeNums(position);
            }
        });

    }

    private void getHttpData() {
        if (list_goods != null) {
            list_goods.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        String find = etShopName.getText().toString();
        // String shopType = tvShopName.getText().toString();
        String goodsType = tvGoodsType.getText().toString();
        if (goodsType.equals("全部"))
            goodsType = "";
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(CheckGoodsActivity.this, "dbname"));
        // map.put("UserId", SharedUtil.getSharedData(GoodsWareHousingActivity.this, "userInfoId"));
        map.put("i_ShopID", shopId);
        map.put("c_GoodsName", find);//商品名/编号/拼音
        map.put("c_GoodsClassify", goodsType);//分类名
        postToHttp(NetworkUrl.GOODSCHECK, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                Logger.json(jsonText);
                CheckGoods checkGoods = new Gson().fromJson(jsonText, CheckGoods.class);
                if (checkGoods.getResult_stadus().equals("SUCCESS")) {
                    list_shop = checkGoods.getResult_data().getShop_s();
                    list_goods = checkGoods.getResult_data().getStock_s();
                    if (list_goodsNum != null) {
                        list_goodsNum.clear();
                    }
                    for (int i = 0; i < list_goods.size(); i++) {
                        list_goodsNum.add(list_goods.get(i).getN_Amount());


                    }
                    // list_type = checkGoods.getResult_data().getType_s();
                    if (list_type.size() == 0) {
                        list_type = checkGoods.getResult_data().getType_s();
                        Log.d("uuz", "list_type size : " + list_type.size());
                        if (list_type != null && list_type.size() > 0) {
                            tvGoodsType.setText(list_type.get(0).getC_Value());
                            Log.d("uuz", "0号元素名 : " + list_type.get(0).getC_Value());
                        }
                    }
                    if (list_shop.size() == 0)
                        list_shop = checkGoods.getResult_data().getShop_s();
                    Log.d("uuz", "list_type size : " + list_shop.size());
                    if (list_shop != null && list_shop.size() > 0) {
                        //  tvShopName.setText(list_shop.get(0).getC_ShopName());
                        Log.d("uuz", "0号元素名 : " + list_shop.get(0).getC_ShopName());
                    }
                    adapter = new CheckGoodsAdapter(CheckGoodsActivity.this, list_goods);
                    lvBuyList.setAdapter(adapter);
                    tvCheckNum.setText(checkGoods.getResult_data().getCount_s().get(0).getNum_s());
                    tvGoodsTotalMoney.setText(checkGoods.getResult_data().getCount_s().get(0).getMoney_s());
                } else {
                    Utils.showToast(CheckGoodsActivity.this,checkGoods.getResult_errmsg());
                }
            }

            @Override
            public void OnFail(String message) {
                Log.d("uuz", "OnFail: ***");
            }
        });

    }


    private class AddPopWindow extends PopupWindow {
        private View conentView;

        public AddPopWindow(final Activity context, View view) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popuwindow_card_type, null);
            ListView lv_type = (ListView) conentView.findViewById(R.id.lv_card_type);
            lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if (x == 1) {
                        tvGoodsType.setText(list_type.get(position).getC_Value());
                    }

                    dismiss();
                    getHttpData();
                }
            });
            if (x == 1) {
                str = new String[list_type.size()];
                for (int i = 0; i < list_type.size(); i++) {
                    str[i] = list_type.get(i).getC_Value();
                }
            } else if (x == 2) {
                str = new String[list_shop.size()];
                for (int i = 0; i < list_shop.size(); i++) {
                    str[i] = list_shop.get(i).getC_ShopName();
                }
            }

//            String str[] = new String[list_type.size()];
//            for (int i = 0; i < list_type.size(); i++) {
//                str[i] = list_type.get(i).getC_Value();
//            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckGoodsActivity.this,
                    android.R.layout.simple_list_item_1, str);
            lv_type.setAdapter(adapter);
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            this.setContentView(conentView);
            this.setWidth(w / 3);
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

    private void changeNums(final int i) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(CheckGoodsActivity.this).inflate(R.layout.number_nums, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        InputFilter[] filters2 = {new MyTextFilter2()};
        editText.setFilters(filters2);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL ){
                    editText.setText("");
                }
                return false;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick_pay = true;
                window.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().trim().equals("")){
                    Utils.showToast(CheckGoodsActivity.this,"请输入正确的数量");
                    return;
                }

                goods_nums = Utils.getNum2(Float.parseFloat(editText.getText().toString()));
                change_nums = Float.parseFloat(list_goodsNum.get(i));
                change_nums = goods_nums - change_nums;
                change_nums = Utils.getNum2(change_nums);
                list_goods.get(i).setN_Amount(goods_nums + "");
                list_goods.get(i).setChangeNum(change_nums + "");

                adapter.notifyDataSetChanged();
                          window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.4));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_check_goods), Gravity.CENTER, 0, 0);

    }

    private void inputPop(final int i) {
        final EditText inputServer = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入修改后的库存").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                InputFilter[] filters2 = {new MyTextFilter2()};
                                inputServer.setFilters(filters2);
                                goods_nums = Utils.getNum2(Float.parseFloat(inputServer.getText().toString()));
                                list_goods.get(i).setN_Amount(goods_nums + "");
                                adapter.notifyDataSetChanged();
                            }
                        }
                );
            }
        });
        builder.show();
    }


    private void saveData() {
        if (list_goods.size() > 0) {
            String ids = "";
            String num = "";
            String money = "";
            for (int i = 0; i < list_goods.size(); i++) {
                if (Float.parseFloat(list_goods.get(i).getChangeNum()) != 0) {
                    if (i == list_goods.size() - 1) {
                        ids += list_goods.get(i).getI_GoodsID();
                        num += list_goods.get(i).getChangeNum();
                        money += Float.parseFloat(list_goods.get(i).getN_DePrice()) * Float.parseFloat(list_goodsNum.get(i)) + "";
                    } else {
                        ids += list_goods.get(i).getI_GoodsID() + ",";
                        num += list_goods.get(i).getChangeNum() + ",";
                        money += Float.parseFloat(list_goods.get(i).getN_DePrice()) * Float.parseFloat(list_goodsNum.get(i)) + ",";
                    }
                }
            }

            if (money.endsWith(",")) {
                money = money.substring(0, money.length() - 1);
            }
            if (num.endsWith(",")) {
                num = num.substring(0, num.length() - 1);
            }
            if (ids.endsWith(",")) {
                ids = ids.substring(0, ids.length() - 1);
            }
            Map<String, String> map = new HashMap();
            map.put("dbName", SharedUtil.getSharedData(CheckGoodsActivity.this, "dbname"));
            map.put("userid", SharedUtil.getSharedData(CheckGoodsActivity.this, "userInfoId"));
            map.put("money_s", money);
            map.put("num_s", num);
            map.put("goods_id", ids);
            postToHttp(NetworkUrl.GOODSCHECKSAVE, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    Logger.json(jsonText);
                    flag_save = true;
                    changeActiviy();
                }

                @Override
                public void OnFail(String message) {
                    Log.e("uuz", "OnFail:sadfjasdfkjhasfsad ");
                    flag_save = false;
                    changeActiviy();
                }
            });
        }
    }

    private void changeActiviy() {
        shopCheckGoods.setVisibility(View.GONE);
        llCheckGoods.setVisibility(View.GONE);
        llCheckText.setVisibility(View.GONE);
        LinearLayout ll_hint = (LinearLayout) findViewById(R.id.ll_pay_hint);
        ImageView iv_hint = (ImageView) findViewById(R.id.iv_pay_hint);
        TextView tv_hint = (TextView) findViewById(R.id.tv_pay_hint);
        ll_hint.setVisibility(View.VISIBLE);
        iv_hint.setSelected(flag_save);
        tv_hint.setText("盘点操作" + (flag_save ? "成功" : "失败"));
        if (!flag_save) {
            new AlertDialog.Builder(CheckGoodsActivity.this)
                    .setTitle("操作失败！")
                    .setMessage("由于未知原因入库操作失败,是否在试一次？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // goodsin();
                            saveData();
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
        if (flag_save) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCommit.setVisibility(View.INVISIBLE);
                    list_goods.clear();
                }
            });
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    Intent intent = new Intent(CheckGoodsActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            };
            timer.schedule(task, 1200);
        }
    }
}
