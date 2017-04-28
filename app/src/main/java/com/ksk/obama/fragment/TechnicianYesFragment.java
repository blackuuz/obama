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
import com.ksk.obama.adapter.TechnicianYesAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.custonview.OnRecyclClickListener;
import com.ksk.obama.custonview.SpaceItemDecoration;
import com.ksk.obama.model.Technician;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by djy on 2017/2/22.
 */

public class TechnicianYesFragment extends BaseFragment {
    private RecyclerView rlv;
    private TechnicianYesAdapter adapter;
    private List<Technician.ResultDataBean.EmployeeBean> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_house_no, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rlv = (RecyclerView) getView().findViewById(R.id.rly);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        rlv.setLayoutManager(gridLayoutManager);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(3, 30, false);
        rlv.addItemDecoration(spaceItemDecoration);
        adapter = new TechnicianYesAdapter(activity, list);
        rlv.setAdapter(adapter);
        getHttpData();

        adapter.setOnClickRecycl(new OnRecyclClickListener() {
            @Override
            public void OnClickRecyclListener(int position) {
                Utils.showToast(activity, position + "");
            }
        });
    }

    private void getHttpData() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(activity, "dbname"));
        map.put("ShopID", SharedUtil.getSharedData(activity, "shopid"));
        map.put("Status", "1");
        postToHttp(NetworkUrl.PERSON, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                Technician houseNo = new Gson().fromJson(jsonText, Technician.class);
                if (houseNo.getResult_stadus().equals("SUCCESS")) {
                    list.clear();
                    list.addAll(houseNo.getResult_data().getEmployee());
                    if (list != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void OnFail(String message) {

            }
        });
    }
}
