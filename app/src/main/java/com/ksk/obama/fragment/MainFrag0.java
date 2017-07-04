package com.ksk.obama.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ksk.obama.R;
import com.ksk.obama.activity.BuyShopListActivity;
import com.ksk.obama.activity.ChangePersonActivity;
import com.ksk.obama.activity.CouponCancelActivity;
import com.ksk.obama.activity.GetMoneyActivity;
import com.ksk.obama.activity.HouseActivity;
import com.ksk.obama.activity.MainActivity;
import com.ksk.obama.activity.QuickDelCActivity;
import com.ksk.obama.activity.QuickDelMActivity;
import com.ksk.obama.activity.ReadCardInfoActivity;
import com.ksk.obama.activity.TechnicianActivity;
import com.ksk.obama.utils.Utils;

/**
 * Created by Administrator on 2016/10/8.
 */

public class MainFrag0 extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_main_0;
    private RelativeLayout rl_main_1;
    private RelativeLayout rl_main_3;
    private RelativeLayout rl_main_5;
    private RelativeLayout rl_main_7;
    private RelativeLayout rl_main_9;
    private RelativeLayout rl_main_10;
    private RelativeLayout rl_main_11;
    private RelativeLayout rl_main_coupon;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_f_0, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        rl_main_0 = (RelativeLayout) getView().findViewById(R.id.rl_main00);
        rl_main_1 = (RelativeLayout) getView().findViewById(R.id.rl_main01);
        rl_main_3 = (RelativeLayout) getView().findViewById(R.id.rl_main03);
        rl_main_5 = (RelativeLayout) getView().findViewById(R.id.rl_main05);
        rl_main_7 = (RelativeLayout) getView().findViewById(R.id.rl_main07);
        rl_main_9 = (RelativeLayout) getView().findViewById(R.id.rl_main09);
        rl_main_10 = (RelativeLayout) getView().findViewById(R.id.rl_main10);
        rl_main_11 = (RelativeLayout) getView().findViewById(R.id.rl_main11);
        rl_main_coupon = (RelativeLayout) getView().findViewById(R.id.rl_main_coupon);
        rl_main_0.setVisibility(View.GONE);
        rl_main_1.setVisibility(View.GONE);
        rl_main_3.setVisibility(View.GONE);
        rl_main_5.setVisibility(View.GONE);
        rl_main_7.setVisibility(View.GONE);
        rl_main_9.setVisibility(View.GONE);
        rl_main_10.setVisibility(View.GONE);
        rl_main_11.setVisibility(View.GONE);
        rl_main_coupon.setVisibility(View.GONE);
        for (int i = 0; i < MainActivity.list_pow.size(); i++) {
            String str = MainActivity.list_pow.get(i).getC_PopedomName();
            if (str.equals("POS:商品消费")) {
                rl_main_0.setVisibility(View.VISIBLE);
                rl_main_0.setOnClickListener(this);
            } else if (str.equals("POS:快速收银")) {
                rl_main_1.setVisibility(View.VISIBLE);
                rl_main_9.setVisibility(View.VISIBLE);
                rl_main_1.setOnClickListener(this);
                rl_main_9.setOnClickListener(this);
            } else if (str.equals("POS:积分兑换")) {
                rl_main_3.setVisibility(View.VISIBLE);
                rl_main_3.setOnClickListener(this);
            } else if (str.equals("POS:交班")) {
                rl_main_5.setVisibility(View.VISIBLE);
                rl_main_5.setOnClickListener(this);
            } else if (str.equals("POS:今日收益")) {
                rl_main_7.setVisibility(View.VISIBLE);
                rl_main_7.setOnClickListener(this);
            }else if(str.equals("POS:优惠券核销")){
                rl_main_coupon.setVisibility(View.VISIBLE);
                rl_main_coupon.setOnClickListener(this);

            }
        }

//        for (int i = 0; i < MainActivity.list_module.size(); i++) {
//            String str = MainActivity.list_module.get(i).getC_ModuleName();
//            if (str.equals("台房")) {
//                rl_main_10.setVisibility(View.VISIBLE);
//                rl_main_10.setOnClickListener(this);
//                rl_main_11.setVisibility(View.VISIBLE);
//                rl_main_11.setOnClickListener(this);
//            }
//        }

    }


    @Override
    public void onClick(View v) {
        if (isNetworkAvailable(getActivity())) {
            Intent intent = new Intent();
            switch (v.getId()) {
                //产品消费
                case R.id.rl_main00:
                    intent.setClass(getActivity(), BuyShopListActivity.class);
                    break;
                //快速收银*
                case R.id.rl_main01:
                    intent.setClass(getActivity(), QuickDelMActivity.class);
                    break;
                //积分兑换
                case R.id.rl_main03:
                    intent.putExtra("type", 3);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;

                //交班
                case R.id.rl_main05:
                    intent.setClass(getActivity(), ChangePersonActivity.class);
                    break;

                //今日收益*
                case R.id.rl_main07:
                    intent.putExtra("type", 6);
                    intent.setClass(getActivity(), GetMoneyActivity.class);
                    break;
                //快速扣次*
                case R.id.rl_main09:
                    intent.setClass(getActivity(), QuickDelCActivity.class);
                    break;
                case R.id.rl_main10:
                    intent.setClass(getActivity(), HouseActivity.class);
                    break;
                case R.id.rl_main11:
                    intent.setClass(getActivity(), TechnicianActivity.class);
                    break;
                //优惠券核销
                case R.id.rl_main_coupon:
                    intent.setClass(getActivity(), CouponCancelActivity.class);
                    break;

            }
            startActivity(intent);
        } else {
            Utils.showToast(getActivity(), "当前无网络，无法进行任何操作");
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    protected boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState() + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
