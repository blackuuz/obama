package com.ksk.obama.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.HouseYesAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.custonview.OnRecyclClickListener;
import com.ksk.obama.custonview.SpaceItemDecoration;
import com.ksk.obama.model.HouseYes;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by djy on 2017/2/22.
 */

public class HouseYesFragment extends BaseFragment {

    private List<HouseYes.ResultDataBean.RoomBean> list = new ArrayList<>();
    private HouseYesAdapter adapter;
    private RecyclerView rlv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_house_yes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rlv = (RecyclerView) getView().findViewById(R.id.rly);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        rlv.setLayoutManager(gridLayoutManager);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(3, 30, false);
        rlv.addItemDecoration(spaceItemDecoration);
        adapter = new HouseYesAdapter(activity, list);
        rlv.setAdapter(adapter);
        getView().findViewById(R.id.image_0).setOnClickListener(null);
        getView().findViewById(R.id.text_0).setOnClickListener(null);
        getHttpData();
        adapter.setOnClickRecycl(new OnRecyclClickListener() {
            @Override
            public void OnClickRecyclListener(int position) {
                if (list.get(position).isSelect()) {
                    list.get(position).setSelect(false);
                } else {
                    list.get(position).setSelect(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void getHttpData() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(activity, "dbname"));
        map.put("ShopID", SharedUtil.getSharedData(activity, "shopid"));
        map.put("Status", "1");
        postToHttp(NetworkUrl.HOUSE, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                HouseYes houseYes = new Gson().fromJson(jsonText, HouseYes.class);
                if (houseYes.getResult_stadus().equals("SUCCESS")) {
                    list.addAll(houseYes.getResult_data().getRoom());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void OnFail(String message) {

            }
        });
    }

    public void payMoney(){

    }
}
