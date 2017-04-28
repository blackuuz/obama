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
import com.ksk.obama.activity.MainActivity;
import com.ksk.obama.activity.OpenCardActivity;
import com.ksk.obama.activity.ReadCardInfoActivity;
import com.ksk.obama.utils.Utils;

/**
 * Created by Administrator on 2016/10/8.
 */

public class MainFrag1 extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_main_0;
    private RelativeLayout rl_main_1;
    private RelativeLayout rl_main_2;
    private RelativeLayout rl_main_3;
    private RelativeLayout rl_main_4;
    private RelativeLayout rl_main_04;
    private RelativeLayout rl_main_06;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_f_1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        rl_main_0 = (RelativeLayout) getView().findViewById(R.id.rl_main10);
        rl_main_1 = (RelativeLayout) getView().findViewById(R.id.rl_main11);
        rl_main_2 = (RelativeLayout) getView().findViewById(R.id.rl_main12);
        rl_main_3 = (RelativeLayout) getView().findViewById(R.id.rl_main13);
        rl_main_4 = (RelativeLayout) getView().findViewById(R.id.rl_main14);

        rl_main_04 = (RelativeLayout) getView().findViewById(R.id.rl_main04);
        rl_main_06 = (RelativeLayout) getView().findViewById(R.id.rl_main06);
        rl_main_04.setVisibility(View.GONE);
        rl_main_06.setVisibility(View.GONE);

        rl_main_0.setVisibility(View.GONE);
        rl_main_1.setVisibility(View.GONE);
        rl_main_2.setVisibility(View.GONE);
        rl_main_3.setVisibility(View.GONE);
        rl_main_4.setVisibility(View.GONE);
        for (int i = 0; i < MainActivity.list_pow.size(); i++) {
            String str = MainActivity.list_pow.get(i).getC_PopedomName();
            if (str.equals("POS:会员办卡")) {
                rl_main_0.setVisibility(View.VISIBLE);
                rl_main_0.setOnClickListener(this);
            } else if (str.equals("POS:会员充值")) {
                rl_main_1.setVisibility(View.VISIBLE);
                rl_main_1.setOnClickListener(this);
            } else if (str.equals("POS:购买次数")) {
                rl_main_2.setVisibility(View.VISIBLE);
                rl_main_2.setOnClickListener(this);
            } else if (str.equals("POS:会员到店")) {
                rl_main_3.setVisibility(View.VISIBLE);
                rl_main_3.setOnClickListener(this);
            } else if (str.equals("POS:挂失补卡")) {
                rl_main_4.setVisibility(View.VISIBLE);
                rl_main_4.setOnClickListener(this);
            } else if (str.equals("POS:赠送/扣除积分")) {
                rl_main_04.setVisibility(View.VISIBLE);
                rl_main_04.setOnClickListener(this);
            } else if (str.equals("POS:卡片延期")) {
                rl_main_06.setVisibility(View.VISIBLE);
                rl_main_06.setOnClickListener(this);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (isNetworkAvailable(getActivity())) {
            Intent intent = new Intent();

            switch (v.getId()) {
                case R.id.rl_main10:
                    intent.setClass(getActivity(), OpenCardActivity.class);
                    break;
                case R.id.rl_main11:
                    intent.putExtra("type", 11);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;
                case R.id.rl_main12:
                    intent.putExtra("type", 12);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;
                case R.id.rl_main13:
                    intent.putExtra("type", 13);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;
                case R.id.rl_main14:
                    intent.putExtra("type", 14);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;
                //赠送积分*
                case R.id.rl_main04:
                    intent.putExtra("type", 4);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
                    break;
                //卡片延期*
                case R.id.rl_main06:
                    intent.putExtra("type", 6);
                    intent.setClass(getActivity(), ReadCardInfoActivity.class);
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
