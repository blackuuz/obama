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
import com.ksk.obama.activity.BuyCountstatisticsActivity;
import com.ksk.obama.activity.CountStatisticsActivity;
import com.ksk.obama.activity.ExchangeStatisticsActivity;
import com.ksk.obama.activity.ExitCardStatisticsActivity;
import com.ksk.obama.activity.ExpenditureStatisticsActivity;
import com.ksk.obama.activity.MainActivity;
import com.ksk.obama.activity.OpenCardStatisticsActivity;
import com.ksk.obama.activity.RechargeStatisticsActivity;
import com.ksk.obama.activity.SalesStatisticsActivity;
import com.ksk.obama.utils.Utils;

/**
 * Created by Administrator on 2016/10/8.
 */

public class MainFrag2 extends Fragment implements View.OnClickListener {

    private RelativeLayout rl_main_0;
    private RelativeLayout rl_main_1;
    private RelativeLayout rl_main_2;
    private RelativeLayout rl_main_3;
    private RelativeLayout rl_main_5;
    private RelativeLayout rl_main_6;
    private RelativeLayout rl_main_7;
    private RelativeLayout rl_main_8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_f_2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        rl_main_0 = (RelativeLayout) getView().findViewById(R.id.rl_main20);
        rl_main_1 = (RelativeLayout) getView().findViewById(R.id.rl_main21);
        rl_main_2 = (RelativeLayout) getView().findViewById(R.id.rl_main22);
        rl_main_3 = (RelativeLayout) getView().findViewById(R.id.rl_main23);
        rl_main_5 = (RelativeLayout) getView().findViewById(R.id.rl_main25);
        rl_main_6 = (RelativeLayout) getView().findViewById(R.id.rl_main26);
        rl_main_7 = (RelativeLayout) getView().findViewById(R.id.rl_main27);
        rl_main_8 = (RelativeLayout) getView().findViewById(R.id.rl_main28);

        rl_main_0.setVisibility(View.GONE);
        rl_main_1.setVisibility(View.GONE);
        rl_main_2.setVisibility(View.GONE);
        rl_main_3.setVisibility(View.GONE);
        rl_main_5.setVisibility(View.GONE);
        rl_main_6.setVisibility(View.GONE);
        rl_main_7.setVisibility(View.GONE);
        rl_main_8.setVisibility(View.GONE);
        for (int i = 0; i < MainActivity.list_pow.size(); i++) {
            String str = MainActivity.list_pow.get(i).getC_PopedomName();
            if (str.equals("POS:开卡统计")) {
                rl_main_0.setVisibility(View.VISIBLE);
                rl_main_0.setOnClickListener(this);
            } else if (str.equals("POS:消费统计")) {
                rl_main_1.setVisibility(View.VISIBLE);
                rl_main_1.setOnClickListener(this);
            } else if (str.equals("POS:充值统计")) {
                rl_main_2.setVisibility(View.VISIBLE);
                rl_main_2.setOnClickListener(this);
            } else if (str.equals("POS:兑换统计")) {
                rl_main_3.setVisibility(View.VISIBLE);
                rl_main_3.setOnClickListener(this);
            } else if (str.equals("POS:购次统计")) {
                rl_main_5.setVisibility(View.VISIBLE);
                rl_main_5.setOnClickListener(this);
            } else if (str.equals("POS:计次统计")) {
                rl_main_6.setVisibility(View.VISIBLE);
                rl_main_6.setOnClickListener(this);
            } else if (str.equals("POS:退卡统计")) {
                rl_main_7.setVisibility(View.VISIBLE);
                rl_main_7.setOnClickListener(this);
            }else if (str.equals("POS:销售统计")) {
                rl_main_8.setVisibility(View.VISIBLE);
                rl_main_8.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (isNetworkAvailable(getActivity())) {
            Intent intent = new Intent();
            switch (v.getId()) {
                //开卡统计
                case R.id.rl_main20:
                    intent.setClass(getActivity(), OpenCardStatisticsActivity.class);
                    break;
                //消费统计
                case R.id.rl_main21:
                    intent.setClass(getActivity(), ExpenditureStatisticsActivity.class);
                    break;
                //充值统计
                case R.id.rl_main22:
                    intent.setClass(getActivity(), RechargeStatisticsActivity.class);
                    break;
                //兑换统计
                case R.id.rl_main23:
                    intent.setClass(getActivity(), ExchangeStatisticsActivity.class);
                    break;
                //购次统计
                case R.id.rl_main25:
                    intent.setClass(getActivity(), BuyCountstatisticsActivity.class);
                    break;
                //计次统计
                case R.id.rl_main26:
                    intent.setClass(getActivity(), CountStatisticsActivity.class);
                    break;
                //退卡统计
                case R.id.rl_main27:
                    intent.setClass(getActivity(), ExitCardStatisticsActivity.class);
                    break;
                //销售统计
                case R.id.rl_main28:
                    intent.setClass(getActivity(), SalesStatisticsActivity.class);
                    break;

            }
            startActivity(intent);
        }else{
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
