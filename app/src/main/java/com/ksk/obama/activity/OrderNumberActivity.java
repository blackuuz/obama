package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ksk.obama.DB.OrderNumber;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBackS;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ksk.obama.utils.Utils.getIsTime;

public class OrderNumberActivity extends BaseSupplementActivity {

    private TextView tv_numbs;
    private ListView lv_order;
    private int num = 0;
    private boolean cflag = false;
    private int orderNum = 0;
    private int orderYes = 0;
    private ProgressBar pb, pb1;
    private SearchView searchView;
    private LinearLayout ll_pb;
    private List<OrderNumber> list = new ArrayList<>();
    private MyAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pb.setVisibility(View.GONE);
                    ll_pb.setVisibility(View.GONE);
                    Utils.showToast(OrderNumberActivity.this, "网络问题");
                    break;
                case 1:
                    //   loadingDialog.dismiss();
                    pb.setVisibility(View.VISIBLE);
                    ll_pb.setVisibility(View.VISIBLE);
                    if (pb.getProgress() == pb.getMax()) {
                        ll_pb.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    tv_numbs.setText("当前选中 " + msg.arg1 + " 条订单");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_number);
        adapter = new MyAdapter();
        initTitle();
        initData();
        initView();
    }

    private void initView() {
        final CheckBox cb = (CheckBox) findViewById(R.id.cb_order);
        final CheckBox chckbox = (CheckBox) findViewById(R.id.cb_order_activiy);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    list = DataSupport.findAll(OrderNumber.class);
                    for (OrderNumber orderNumber : list) {
                        //应该忽略大小写查询
                        if (!isSimilar(newText,orderNumber.getOrderNumber())) {
                            orderNumber.setQuery(true);
                        }
                    }
                    Iterator<OrderNumber> it = list.iterator();
                    while (it.hasNext()) {
                        OrderNumber orderNumber = it.next();
                        if (orderNumber.isQuery()) {
                            it.remove();
                        }
                    }
                } else {
                    list = DataSupport.findAll(OrderNumber.class);
                }
                if(chckbox.isChecked()){
                    tv_numbs.setText("当前选中 " + list.size() + " 条订单");
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        tv_numbs = (TextView) findViewById(R.id.tv_checked_num);
        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).isChecked()) {
                    list.get(position).setChecked(false);
                    cflag = true;
                    chckbox.setChecked(false);
                } else {
                    list.get(position).setChecked(true);
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        num = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isChecked()) {
                                num++;
                            }
                        }
                        Message msg = Message.obtain(handler);
                        msg.what = 2;
                        msg.arg1 = num;
                        handler.sendMessage(msg);
                    }
                }).start();
                adapter.notifyDataSetChanged();
                cflag = false;
            }
        });

        lv_order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(OrderNumberActivity.this)
                        .setTitle("注意!确认删除")
                        .setMessage("你将要从数据库中删除此条信息,注意次操作不可恢复！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.delete(OrderNumber.class, list.get(position).getId());
                                list.remove(position);
                                adapter.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        chckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cflag) {
                    return;
                }
                if (isChecked) {
                    for (OrderNumber order : list) {
                        order.setChecked(true);
                        tv_numbs.setText("当前选中 " + list.size() + " 条订单");
                    }
                } else {
                    for (OrderNumber order : list) {
                        order.setChecked(false);
                        tv_numbs.setText("当前选中 0 条订单");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        chckbox.setChecked(true);
    }

//比较字符串开头是否相等 忽略大小写
    private   boolean isSimilar(String one, String anotherString) {

        int length = one.length();
        if(length > anotherString.length()) {
            //如果被期待为开头的字符串的长度大于anotherString的长度
            return false;
        }
        if (one.equalsIgnoreCase(anotherString.substring(0, length))) {
            return true;
        } else {
            return false;
        }
    }
    private void initTitle() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("订单信息");
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("上传");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderNum = 0;
                int j = 0;
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isChecked()) {
                            j++;
                        }
                    }
                    pb1.setVisibility(View.VISIBLE);
                    pb.setMax(j);
                    pb.setProgress(0);
                    if (j == 0) {
                        Utils.showToast(OrderNumberActivity.this, "你还没有选择样上传的订单");
                        return;
                    }
                    send1();

                } else {
                    Utils.showToast(OrderNumberActivity.this, "当前没有任何可上传订单");
                }

            }
        });
    }

    private void initData() {
        ll_pb = (LinearLayout) findViewById(R.id.ll_order_progressbar);
        pb = (ProgressBar) findViewById(R.id.pgb);
        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        lv_order = (ListView) findViewById(R.id.lv_order);
        Connector.getDatabase();
        // getDatabase();
        list = DataSupport.findAll(OrderNumber.class);
        pb.setMax(list.size());
        if (list != null) {
            lv_order.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d("uuz", "initData: no null" + list.size());
        }
    }

//    private void send() {
//        loadingDialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    FileOutputStream fileOutputStream = openFileOutput("123.txt", Context.MODE_APPEND);
//                    for (int i = 0; i < list.size(); i++) {
//                        fileOutputStream.write(list.get(i).toString().getBytes("UTF-8"));
//                    }
//                    fileOutputStream.close();
//
//                } catch (Exception e) {
//                    Logger.e("无此文件0");
//                    e.printStackTrace();
//                }
//// TODO: 2017/8/25 先注释掉
//                HttpTools.sendToServer(handler, "", null, new File("123.txt"), new HttpTools.ProgressListener() {
//                    @Override
//                    public void update(long bytesRead, long contentLength, boolean done) {
//                        if (done) {
//                            DataSupport.deleteAll(OrderNumber.class);
//                            deleteFile("123.txt");
//                            pb.setProgress(100);
//                            finish();
//                        } else {
//                            int progress = (int) (100f * bytesRead / contentLength);
//                            pb.setProgress(progress);
//                        }
//                    }
//                });
//            }
//        }).start();
//
//    }


    private void send1() {
        for (int i = 0; i < list.size(); i++) {
            Log.e("uuz", "send1:" + list.size());
            if (list.get(i).isChecked()) {
                Log.e("uuz", "checked" + i);

                switch (list.get(i).getFormClazz()) {
                    case "SY"://消费收银
                        postBuyShop(list.get(i), i);
                        break;
                    case "KM"://快速消费
                        postQuick(list.get(i), i);
                        break;
                    case "KT"://快速扣次
                        postQuickC(list.get(i), i);
                        break;
                    case "DH"://积分兑换
                        postInteralExchange(list.get(i), i);
                        break;
                    case "KK"://开户
                        postOpenCard(list.get(i), i);
                        break;
                    case "CZ"://充值
                        postRecharge(list.get(i), i);
                        break;
                    case "BT"://购买次数
                        postBuyCount(list.get(i), i);
                        break;
                    case "KJ"://快速扣积分
                        postDelIntegral(list.get(i), i);
                        break;
                }

            }
        }
    }

    private void postBuyShop(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        if (orderNumber.isVip()) {
            map.put("integral", orderNumber.getshopIntegral());//每个商品的积分 "," 拼接
            map.put("get_integral", orderNumber.getGetIntegral());//获得的积分
            map.put("Member_Id", orderNumber.getMemberid());
            map.put("payDecIntegral", orderNumber.getPayDecIntegral());//抵现的积分
            map.put("payIntegral", orderNumber.getDelMoney());//积分抵现的金额
        }
        map.put("dbName", orderNumber.getDbName());
        map.put("goods_id", orderNumber.getGoodsId());
        map.put("num", orderNumber.getNum());
        map.put("money", orderNumber.getGoodsMoney());
        map.put("User_Id", orderNumber.getUserId());
        map.put("EquipmentNum", terminalSn);
        map.put("PayShould", orderNumber.getShouldMoney());
        map.put("PayActual", orderNumber.getMoney());
        map.put("PayDiscounted", orderNumber.getPayDiscounted());//抹零
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "1");
        map.put("PayTicket", orderNumber.getPayTicket());
        map.put("coupon_id", orderNumber.getCouponId());
        switch (orderNumber.getPayType()) {
            case 0:
                map.put("payCash", orderNumber.getMoney());
                break;
            case 1:
                map.put("payWeChat", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;
            case 2:
                map.put("payAli", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;
            case 3:
                map.put("payThird", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;
            case 4:
                map.put("payCard", orderNumber.getMoney());
                break;
        }
        map.put("temporary_num", orderNumber.getTemporary_num());
        map.put("result_name", orderNumber.getResult_name());
        postToHttps(i, NetworkUrl.BUYSHOP, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);

            }
        });


    }

    private void postQuick(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("costmoney", orderNumber.getMoney());
        map.put("dbName", orderNumber.getDbName());
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("Supplement", "1");
        // map.put("EquipmentNum", terminalSn);
        map.put("c_Billfrom", robotType + "");
        switch (orderNumber.getPayType()) {
            case 0:
                map.put("payCash", orderNumber.getMoney());
                break;
            case 1:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payWeChat", orderNumber.getMoney());
                break;
            case 2:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payAli", orderNumber.getMoney());
                break;
            case 3:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payThird", orderNumber.getMoney());
                break;
            case 4:
                map.put("payCard", orderNumber.getMoney());
                break;
        }
        map.put("payIntegral", orderNumber.getDelMoney());//积分抵现的金额
        map.put("payDecIntegral", orderNumber.getPayDecIntegral());//抵现的积分
        map.put("userID", orderNumber.getUserId());
        if (orderNumber.isTemporary()) {
            map.put("temporary_num", orderNumber.getTemporary_num());
            map.put("result_name", orderNumber.getResult_name());
        }
        postToHttps(i, NetworkUrl.QUICK_M, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);

            }
        });
    }

    private void postQuickC(OrderNumber orderNumber, int i) {
        Map<String, String> map = new HashMap<>();
        map.put("userID", orderNumber.getUserId());
        map.put("dbName", orderNumber.getDbName());
        map.put("costtime", orderNumber.getCosttime());
        map.put("CardNum", orderNumber.getCardNum());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        map.put("GoodsId", orderNumber.getGoodsId());
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("EquipmentNum", terminalSn);
        map.put("Supplement", "1");
        postToHttps(i, NetworkUrl.QUICK_COUNT, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {

            }
        });

    }

    private void postInteralExchange(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", orderNumber.getDbName());
        map.put("goods_id", orderNumber.getGoodsId());
        map.put("goods_name", orderNumber.getGoodsName());
        map.put("num", orderNumber.getNum());
        map.put("integral", orderNumber.getshopIntegral());
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("EquipmentNum", terminalSn);
        map.put("allintegral", orderNumber.getCosttime());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "1");
        map.put("User_Id", orderNumber.getUserId());
        map.put("Member_Id", orderNumber.getMemberid());
        postToHttps(i, NetworkUrl.BUYINTEGRAL, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);
            }
        });

    }

    private void postOpenCard(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("cardNO", orderNumber.getCardNum());
        map.put("memberName", orderNumber.getCardName());
        map.put("sex", orderNumber.getSex());
        map.put("mobile", orderNumber.getMobile());
        String birthday = orderNumber.getBirthday();
        if (TextUtils.isEmpty(birthday)) {
            birthday = orderNumber.getTime().substring(0, 10);
        }
        String str[] = birthday.split("-");
        if (birthday != null && birthday.length() != 0) {
            map.put("birthdayYear", str[0] + "");
            map.put("birthdayMonth", str[1] + "");
            map.put("birthdayDay", str[2] + "");
        }
        map.put("Supplement", "1");
        map.put("classID", orderNumber.getGoodsId());
        map.put("userID", orderNumber.getUserId());
        map.put("payActual", orderNumber.getMoney());//实际付款
        map.put("payShould", orderNumber.getShouldMoney());//应付款
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("IDCard", orderNumber.getIDCard());
        map.put("car", orderNumber.getCar());
        map.put("add", orderNumber.getAddress());
        map.put("Remark", orderNumber.getRemarks());
        map.put("c_Billfrom", robotType + "");
        if (orderNumber.isTemporary()) {
            map.put("temporary_num", orderNumber.getTemporary_num());
            map.put("result_name", orderNumber.getResult_name());
        }
        switch (orderNumber.getPayType()) {
            case 0:
                map.put("payCash", orderNumber.getMoney());
                break;
            case 1:
                map.put("payWeChat", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;
            case 2:
                map.put("payAli", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;
            case 3:
                map.put("payThird", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getRefernumber());
                break;

        }
        map.put("EquipmentNum", terminalSn);
        map.put("C_ServiceEmployee", orderNumber.getRecommendEmpoyee());//推荐员工的名字
        map.put("memid", orderNumber.getMemberid());//
        map.put("password", orderNumber.getPassword());
        map.put("dbName", orderNumber.getDbName());

        postToHttps(i, NetworkUrl.ADDMEMBER, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);
            }
        });
    }

    private void postRecharge(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", orderNumber.getDbName());
        map.put("cardNO", orderNumber.getCardNum());
        map.put("userID", orderNumber.getUserId());
        map.put("payActual", orderNumber.getMoney());
        map.put("payShould", orderNumber.getShouldMoney());
        map.put("EquipmentNum", terminalSn);
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "1");
        map.put("result_name", orderNumber.getResult_name());
        map.put("paySend", orderNumber.getPaySend());
        map.put("n_GetIntegral", orderNumber.getGetIntegral());
        map.put("coupon_code", orderNumber.getCouponId());
        map.put("PayTicket", orderNumber.getPayTicket());
        switch (orderNumber.getPayType()) {
            case 0:
                map.put("payCash", orderNumber.getMoney());
                break;
            case 1:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payWeChat", orderNumber.getMoney());
                break;
            case 2:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payAli", orderNumber.getMoney());
                break;
            case 3:
                map.put("refernumber", orderNumber.getRefernumber());
                map.put("payThird", orderNumber.getMoney());
                break;
        }

        postToHttps(i, NetworkUrl.RECHARGE, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);
            }
        });
    }

    private void postBuyCount(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", orderNumber.getDbName());
        map.put("User_Id", orderNumber.getUserId());
        map.put("goods_id", orderNumber.getGoodsId());
        map.put("Member_Id", orderNumber.getMemberid());
        map.put("times", orderNumber.getCosttime());
        map.put("PayShould", orderNumber.getShouldMoney());
        map.put("PayActual", orderNumber.getMoney());
        map.put("get_integral", orderNumber.getGetIntegral());
        map.put("PayDiscounted", orderNumber.getPayDecIntegral());
        map.put("get_money", orderNumber.getGetMoney());
        map.put("EquipmentNum", terminalSn);
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("validTimes", orderNumber.getValidTimes());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        map.put("Supplement", "1");
        map.put("payDecIntegral", orderNumber.getPayDecIntegral());//抵现的积分
        map.put("payIntegral", orderNumber.getDelMoney());//抵现的金额
        map.put("temporary_num", orderNumber.getTemporary_num());
        map.put("result_name", orderNumber.getResult_name());
        switch (orderNumber.getPayType()) {
            case 0:
                map.put("payCash", orderNumber.getMoney());
                break;

            case 1:
                map.put("payWeChat", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getResult_name());
                break;

            case 2:
                map.put("payAli", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getResult_name());
                break;

            case 3:
                map.put("payThird", orderNumber.getMoney());
                map.put("refernumber", orderNumber.getResult_name());
                break;

            case 4:
                map.put("payCard", orderNumber.getMoney());
                break;
        }
        postToHttps(i, NetworkUrl.SENDBUYCOUNT, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);

            }
        });
    }

    private void postDelIntegral(OrderNumber orderNumber, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("integral_val", orderNumber.getGetIntegral());
        map.put("CardNO", orderNumber.getCardNum());
        map.put("YdUserinfoId", orderNumber.getUserId());
        map.put("dbName", orderNumber.getDbName());
        map.put("Equipment", terminalSn);
        map.put("orderNo", orderNumber.getOrderNumber());
        map.put("CardCode", orderNumber.getCardCode());
        map.put("c_Billfrom", robotType + "");
        if (orderNumber.getPayType() == 0) {
            map.put("type", "add");
        }
        postToHttps(i, NetworkUrl.FAST_EXCHANGE, map, new IHttpCallBackS() {
            @Override
            public void OnSucess(String jsonText, int n) {
                showData(jsonText, n);
                Log.e("uuz", n + "OnSucess: " + jsonText);
            }

            @Override
            public void OnFail(String message) {
                list.get(i).setIsSucceedUploading(-1);
            }

        });
    }


    public synchronized void showData(String text, final int n) {
        Logger.e(text);
        try {
            orderNum += 1;
            JSONObject object = new JSONObject(text);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                orderYes += 1;
                pb.setProgress(orderYes);
                list.get(n).setIsSucceedUploading(1);
                //      tv_hint.setText("当前有 " + orderYes + "/" + list.size() + " 个订单正在上传,不要退出页面");
                //      DataSupport.delete(QuickDelMoney.class, list.get(n).getId());

                Message msg1 = Message.obtain(handler);
                msg1.what = 1;
                msg1.arg1 = num;
                handler.sendMessage(msg1);

                // printInformation(n);
            }
        } catch (JSONException e) {
            list.get(n).setIsSucceedUploading(-1);
            e.printStackTrace();
        }
        switch (robotType) {
            case 3:
                //  isChangeActivity();
                break;
        }

    }


    class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyAdapter() {
            inflater = LayoutInflater.from(OrderNumberActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_order_number, null);
                holder.tv0 = (TextView) convertView.findViewById(R.id.tv0);
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                // holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_order);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            switch (list.get(position).getIsSucceedUploading()) {
                case 0:
                    holder.tv0.setTextColor(0xff000000);//黑
                    holder.tv1.setTextColor(0xff000000);
                    break;
                case 1:
                    holder.tv0.setTextColor(0xff0eff20);//绿
                    holder.tv1.setTextColor(0xff0eff20);
                    break;
                case -1:
                    holder.tv0.setTextColor(0xffff2621);//红
                    holder.tv1.setTextColor(0xffff2621);
                    break;

            }
            OrderNumber number = list.get(position);
            holder.tv0.setText("订单号:" + number.getOrderNumber());
            holder.tv1.setText("订单时间:" + number.getTime());
            //holder.tv2.setText(number.toString());
            if (number.isChecked()) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            Log.d("uuz", "getView: " + number.getMoney() + "*" + number.getOrderNumber());
            return convertView;
        }

        class ViewHolder {
            TextView tv0, tv1, tv2;
            CheckBox checkBox;
        }
    }


    private void removeTimeOut() {
        for (int i = 0; i < list.size(); i++) {
            if (getIsTime(list.get(i).getTime())) {
                DataSupport.delete(OrderNumber.class, list.get(i).getId());
            }
        }
    }
}
