package com.ksk.obama.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.AddPersonAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.AddPerson;
import com.ksk.obama.model.AddVip;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPersonActivity extends BaseActivity {

    private int type;
    private ListView lv_add;
    private String name = "";
    private String aId = "";
    private Button btn_shop;
    private EditText et_name;
    private TextView tv_title;

    private List<AddPerson.ResultData2Bean> list_shop = new ArrayList<>();
    private List<AddPerson.ResultData1Bean> list_Person = new ArrayList<>();

    private List<AddVip.ResultData1Bean> list_Person_v = new ArrayList<>();
    private List<AddVip.ResultData2Bean> list_shop_v = new ArrayList<>();
    private List<String> lists = new ArrayList<>();

    private AddPersonAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", -1);
            switch (type) {
                case 1:
                    adapter = new AddPersonAdapter(AddPersonActivity.this, list_Person);
                    break;

                case 2:
                    adapter = new AddPersonAdapter(list_Person_v, AddPersonActivity.this);
                    break;
            }
        } else {
            finish();
        }
    }

    private void initView() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setVisibility(View.INVISIBLE);
        title_name.setText("推荐人");

        et_name = (EditText) findViewById(R.id.et_name);
        lv_add = (ListView) findViewById(R.id.add_lv);
        btn_shop = (Button) findViewById(R.id.btn_shop);
        tv_title = (TextView) findViewById(R.id.tv_id);
        lv_add.setAdapter(adapter);
        if (type == 2) {
            tv_title.setText("卡号");
        }
        findViewById(R.id.btn_serach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                sendToHttp();
            }
        });
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListPopupWindow mListPop = new ListPopupWindow(AddPersonActivity.this);
                mListPop.setAdapter(new ArrayAdapter<String>(AddPersonActivity.this, android.R.layout.simple_list_item_1, lists));
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int w = dm.widthPixels;
                mListPop.setWidth((int) (w * 0.4));
                mListPop.setHeight(w);
                mListPop.setAnchorView(btn_shop);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
                mListPop.setModal(true);//设置是否是模式

                mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (type) {
                            case 1:
                                aId = list_shop.get(position).getId();
                                btn_shop.setText(list_shop.get(position).getC_ShopName());
                                break;
                            case 2:
                                aId = list_shop_v.get(position).getId();
                                btn_shop.setText(list_shop_v.get(position).getC_ShopName());
                                break;
                        }
                        mListPop.dismiss();
                    }
                });
                mListPop.show();
            }
        });

        lv_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = "";
                String name = "";
                switch (type) {
                    case 1:
                        uid = list_Person.get(position).getId();
                        name = list_Person.get(position).getC_Name();
                        break;
                    case 2:
                        uid = list_Person_v.get(position).getId();
                        name = list_Person_v.get(position).getC_Name();
                        break;
                }

                Intent intent = new Intent();
                intent.putExtra("id", uid);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        sendToHttp();
    }

    private void sendToHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(AddPersonActivity.this, "dbname"));
        map.put("c_Name", name);
        map.put("i_ShopID", aId);
        String url = "";
        switch (type) {
            case 1:
                url = NetworkUrl.ADDSHOPPERSON;
                break;

            case 2:
                url = NetworkUrl.ADDVIPPERSON;
                break;
        }

        postToHttp(url, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                switch (type) {
                    case 1:

                        AddPerson addPerson = new Gson().fromJson(jsonText, AddPerson.class);
                        if (addPerson.getResult_stadus().equals("SUCCESS")) {
                            list_shop.clear();
                            list_Person.clear();
                            lists.clear();
                            list_shop.addAll(addPerson.getResult_data2());
                            list_Person.addAll(addPerson.getResult_data1());
                            for (int i = 0; i < list_shop.size(); i++) {
                                lists.add(list_shop.get(i).getC_ShopName());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(AddPersonActivity.this, addPerson.getResult_errmsg());
                        }
                        break;

                    case 2:
                        AddVip addVip = new Gson().fromJson(jsonText, AddVip.class);
                        if (addVip.getResult_stadus().equals("SUCCESS")) {
                            list_shop_v.clear();
                            list_Person_v.clear();
                            lists.clear();
                            list_shop_v.addAll(addVip.getResult_data2());
                            list_Person_v.addAll(addVip.getResult_data1());
                            for (int i = 0; i < list_shop_v.size(); i++) {
                                lists.add(list_shop_v.get(i).getC_ShopName());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Utils.showToast(AddPersonActivity.this, addVip.getResult_errmsg());
                        }
                        break;
                }
            }

            @Override
            public void OnFail(String message) {

            }
        });

    }
}
