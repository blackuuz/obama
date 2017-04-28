package com.ksk.obama.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ksk.obama.DB.OrderNumber;
import com.ksk.obama.R;
import com.ksk.obama.utils.HttpTools;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class OrderNumberActivity extends BaseActivity {

    private ListView lv_order;
    private List<OrderNumber> list;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pb.setVisibility(View.GONE);
                    Utils.showToast(OrderNumberActivity.this, "网络问题");
                    break;

                case 1:
                    loadingDialog.dismiss();
                    pb.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_number);
        initTitle();
        initData();
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
                send();
            }
        });
    }

    private void initData() {
        pb = (ProgressBar) findViewById(R.id.pgb);
        lv_order = (ListView) findViewById(R.id.lv_order);
        pb.setMax(100);
        Connector.getDatabase();
        list = DataSupport.findAll(OrderNumber.class);
        if (list != null) {
            lv_order.setAdapter(new MyAdapter());
        }
    }

    private void send() {
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fileOutputStream = openFileOutput("123.txt", Context.MODE_APPEND);
                    for (int i = 0; i < list.size(); i++) {
                        fileOutputStream.write(list.get(i).toString().getBytes("UTF-8"));
                    }
                    fileOutputStream.close();

                } catch (Exception e) {
                    Logger.e("无此文件0");
                    e.printStackTrace();
                }

                HttpTools.sendToServer(handler, "", null, new File("123.txt"), new HttpTools.ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        if (done) {
                            DataSupport.deleteAll(OrderNumber.class);
                            deleteFile("123.txt");
                            pb.setProgress(100);
                            finish();
                        } else {
                            int progress = (int) (100f * bytesRead / contentLength);
                            pb.setProgress(progress);
                        }
                    }
                });
            }
        }).start();

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
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            OrderNumber number = list.get(position);
            holder.tv0.setText(number.getOrderNumber());
            holder.tv1.setText(number.getDelMoney());
            holder.tv2.setText(number.getHaveMoney());
            return convertView;
        }

        class ViewHolder {
            TextView tv0, tv1, tv2;
        }
    }

}
