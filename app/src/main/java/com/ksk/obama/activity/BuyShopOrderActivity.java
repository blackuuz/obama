package com.ksk.obama.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.ShopOrderBasicAdapter;
import com.ksk.obama.adapter.ShopOrderDetailAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.model.BuyShopOrder;
import com.ksk.obama.model.DetailBean;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.model.ResultDataBean;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyShopOrderActivity extends BaseActivity {
    private ShopOrderBasicAdapter adapter0;
    private ShopOrderDetailAdapter adapter1;
    private List<ResultDataBean> list0 = new ArrayList<>();
    private List<DetailBean> list1 = new ArrayList<>();
    private ListView lv_0;
    private ListView lv_1;
    private int prePosition = 0;
    private EditText et_remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_shop_order);
        initTitale();
        initView();
        getOrder();
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
        tv_print.setText("取单");
        title_name.setText("取单");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list0.size() > 0) {
                    changeActivity(list1, list0.get(prePosition).getMember(), list0.get(prePosition).getId());
                }
            }
        });
    }

    private void initView() {
        et_remark = (EditText) findViewById(R.id.et_order_q_remark);
        Button btn_query = (Button) findViewById(R.id.btn_order_q_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryOrder();
            }
        });
        lv_0 = (ListView) findViewById(R.id.lv_order_q_0);
        lv_1 = (ListView) findViewById(R.id.lv_order_q_1);

        adapter0 = new ShopOrderBasicAdapter(BuyShopOrderActivity.this, list0);
        adapter1 = new ShopOrderDetailAdapter(BuyShopOrderActivity.this, list1);
        lv_0.setAdapter(adapter0);
        lv_1.setAdapter(adapter1);

        lv_0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (prePosition != position) {
                list0.get(prePosition).setSelect(false);
                list0.get(position).setSelect(true);
                adapter0.notifyDataSetChanged();

                list1.clear();
                list1.addAll(list0.get(position).getDetail());
//                    adapter1.notifyDataSetChanged();
                adapter1 = new ShopOrderDetailAdapter(BuyShopOrderActivity.this, list1);
                lv_1.setAdapter(adapter1);
                prePosition = position;
//                }
            }
        });

        lv_0.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BuyShopOrderActivity.this);
                builder.setTitle("删除提示");
                builder.setMessage("是否删除订单");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = new HashMap<>();
                        map.put("BillID", list0.get(position).getId());
                        map.put("dbName", SharedUtil.getSharedData(BuyShopOrderActivity.this, "dbname"));
                        postToHttp(NetworkUrl.ORDERGDEL, map, new IHttpCallBack() {
                            @Override
                            public void OnSucess(String jsonText) {
                                try {
                                    JSONObject object = new JSONObject(jsonText);
                                    String msg = object.getString("result_stadus");
                                    if (msg.equals("SUCCESS")) {
                                        list0.remove(position);
                                        list1.clear();
                                        adapter0.notifyDataSetChanged();
                                        adapter1.notifyDataSetChanged();
                                    } else {
                                        Utils.showToast(BuyShopOrderActivity.this, "订单删除失败,请重试");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnFail(String message) {
                                Utils.showToast(BuyShopOrderActivity.this, message);
                            }
                        });

                    }
                });

                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true;
            }
        });
    }

    private void queryOrder() {
        String str = et_remark.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Utils.showToast(BuyShopOrderActivity.this, "请输入信息");
        } else {
            for (int i = 0; i < list0.size(); i++) {
                String str1 = list0.get(i).getC_Remark();
                if (str.equals(str1)) {
                    changeActivity(list0.get(i).getDetail(), list0.get(i).getMember(), list0.get(i).getId());
                    break;
                } else {
                    if (i == list0.size() - 1) {
                        et_remark.setText("");
                        Utils.showToast(BuyShopOrderActivity.this, "没有此订单");
                    }
                }
            }
        }
    }

    private void getOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(BuyShopOrderActivity.this, "dbname"));
        map.put("User_Id", SharedUtil.getSharedData(BuyShopOrderActivity.this, "userInfoId"));
        map.put("GroupID", SharedUtil.getSharedData(BuyShopOrderActivity.this, "groupid"));

        postToHttp(NetworkUrl.ORDERGGET, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                showOrder(jsonText);
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void showOrder(String str) {
        BuyShopOrder shopOrder = new Gson().fromJson(str, BuyShopOrder.class);
        if (shopOrder.getResult_stadus().equals("SUCCESS")) {
            list0.clear();
            list1.clear();
            list0.addAll(shopOrder.getResult_data());
            if (list0.size() > 0) {
                list0.get(prePosition).setSelect(true);
                list1.addAll(list0.get(prePosition).getDetail());
                adapter0.notifyDataSetChanged();
                adapter1.notifyDataSetChanged();

            }
        }
    }

    private void changeActivity(List<DetailBean> list1, ReadCardInfo.ResultDataBean member, String id) {
        list_buy.clear();
        for (int i = 0; i < list1.size(); i++) {
            BuyCount buyCount = new BuyCount();
            DetailBean detailBean = list1.get(i);
            buyCount.setId(detailBean.getI_GoodsID());
            buyCount.setName(detailBean.getC_GoodsName());
            buyCount.setNum(detailBean.getN_Number());
            buyCount.setPrice(detailBean.getN_PriceRetail());
            buyCount.setMoney(detailBean.getN_PriceRetail() * detailBean.getN_Number());
            buyCount.setMoneyin(detailBean.getN_PayActual());

            buyCount.setDazhe(member != null ? detailBean.getN_DiscountValueGoods() : 0);
            buyCount.setJifen(member != null ? (detailBean.getN_IntegralValueGoods() == 1 ? detailBean.getN_IntegralValueGoods() : 0) : 0);
            buyCount.setDis(detailBean.getN_DiscountValueGoods());
            buyCount.setInteg(detailBean.getN_IntegralValueGoods());
            list_buy.add(buyCount);
        }
        final Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (member != null) {
            bundle.putString("isVip", "yes");
            bundle.putParcelable("info", member);
        } else {
            bundle.putString("isVip", "no");
        }
        intent.putExtras(bundle);

        Map<String, String> map = new HashMap<>();
        map.put("BillID", id);
        map.put("dbName", SharedUtil.getSharedData(BuyShopOrderActivity.this, "dbname"));
        postToHttp(NetworkUrl.ORDERGDEL, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String msg = object.getString("result_stadus");
                    if (msg.equals("SUCCESS")) {
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Utils.showToast(BuyShopOrderActivity.this, "操作失败,请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(BuyShopOrderActivity.this, message);
            }
        });
    }

}
