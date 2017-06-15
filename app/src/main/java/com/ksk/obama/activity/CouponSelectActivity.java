package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.CouponSelectAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.Coupon;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponSelectActivity extends BaseActivity {


    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @BindView(R.id.nocoupon)
    TextView nocoupon;
    @BindView(R.id.refersh)
    TextView refersh;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    private CouponSelectAdapter adapter;
    private List<Coupon.ResultData> list = new ArrayList<>();
    private String memberID = "";
    private String couponCode = "";
    private String costType = "";
    private String costMoney = "";
    private Float couponMoney_ = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_select);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initView();
        getHttpData();
    }

    private void initTitle() {
        tvCommit.setVisibility(View.GONE);
        titleName.setText("可用优惠券");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getHttpData() {
        Map map = new HashMap();
        map.put("shopId", SharedUtil.getSharedData(CouponSelectActivity.this, "shopid"));
        map.put("dbName", SharedUtil.getSharedData(CouponSelectActivity.this, "dbname"));
        map.put("memberID", memberID);
        map.put("couponCode", couponCode);
        map.put("costType", costType);
        map.put("costMoney", costMoney);
        postToHttp(NetworkUrl.COUPON, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Coupon coupon = new Gson().fromJson(jsonText, Coupon.class);
                if (coupon.getResult_status().equals("SUCCESS")) {
                    if (list.size() > 0) {
                        list.clear();
                    }
                    list.addAll(coupon.getResult_data());
                } else if (coupon.getResult_status().equals("ERR")) {
                    String errmsg = coupon.getResult_errmsg();
                    Utils.showToast(CouponSelectActivity.this, errmsg);
                } else {
                    Utils.showToast(CouponSelectActivity.this, "未知错误");
                }
                Logger.json(jsonText);
                Logger.e(jsonText);
                if (list.size() == 0) {
                    nocoupon.setVisibility(View.VISIBLE);
                    rvCoupon.setVisibility(View.GONE);
                } else {
                    nocoupon.setVisibility(View.GONE);
                    rvCoupon.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnFail(String message) {
                Logger.e(message);
                if (list.size() == 0) {
                    nocoupon.setVisibility(View.VISIBLE);
                    rvCoupon.setVisibility(View.GONE);
                } else {
                    nocoupon.setVisibility(View.GONE);
                    rvCoupon.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void initData() {
        if (getIntent() != null) {
            memberID = getIntent().getStringExtra("memberID");
            couponCode = getIntent().getStringExtra("couponCode");
            costType = getIntent().getStringExtra("costType");
            costMoney = getIntent().getStringExtra("costMoney");
        }

    }


    private void initView() {
        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        rvCoupon.setLayoutManager(mgr);
        if (list != null) {
            list.clear();
        }
        adapter = new CouponSelectAdapter(list);
        rvCoupon.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.buttonSetOnclick(new CouponSelectAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                Intent intent = new Intent();

                couponMoney_ = Float.parseFloat(list.get(position).getN_Money());//优惠金额)
                intent.putExtra("couponMoney",couponMoney_);
                //  intent.putExtra("conditions",list.get(position).get)
                setResult(RESULT_OK, intent);
                finish();

                //Utils.showToast(CouponSelectActivity.this, list.get());
            }
        });

        nocoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpData();
            }
        });
    }


}
