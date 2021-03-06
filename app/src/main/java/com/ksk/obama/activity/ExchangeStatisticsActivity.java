package com.ksk.obama.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.ExchangeStatisticsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.ExchangeStatistics;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ksk.obama.R.id.tv_title1;

public class ExchangeStatisticsActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title0;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_cardNum;
    private TextView time_s;
    private TextView time_e;
    private TextView tv_query;
    private TextView tv_title10;
    private TextView tv_title11;
    private TextView tv_title12;
    private TextView tv_title13;
    private ListView lv_data;
    private List<ExchangeStatistics.ResultDataBean> list = new ArrayList<>();
    private ExchangeStatisticsAdapter adapter;

    private int curyear;
    private int curmonth;
    private int curday;
    private int showMonth;
    private String stringmonth, stringday;
    private String getbirthday;
    private SimpleDateFormat format;
    private Calendar calendar;
    private String dialogTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_statistics);

        initView();
        initcalendar();
        getHttpData();
        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent=new Intent();
                intent.putExtra("MemberId", list.get(position).getId());
                intent.setClass(ExchangeStatisticsActivity.this,ExchangeStatisticsDetialsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tvin = (TextView) findViewById(R.id.tv_commit);
        TextView tv_ting = (TextView) findViewById(R.id.tv_statistics_cen);
        tv_ting.setText("礼物名 :");
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name.setText("兑换记录");
        tvin.setVisibility(View.INVISIBLE);

        tv_title0 = (TextView) findViewById(R.id.tv_statistics_title0);

        et_name = (EditText) findViewById(R.id.et_statistics_name);
        et_cardNum = (EditText) findViewById(R.id.et_statistics_cardNum);
        et_phone = (EditText) findViewById(R.id.et_statistics_phone);
        time_s = (TextView) findViewById(R.id.tv_statistics_time_start);
        time_e = (TextView) findViewById(R.id.tv_statistics_time_end);
        tv_query = (TextView) findViewById(R.id.tv_statistics_commit);

        tv_title10 = (TextView) findViewById(R.id.tv_title0);
        tv_title11 = (TextView) findViewById(tv_title1);
        tv_title12 = (TextView) findViewById(R.id.tv_title2);
        tv_title13 = (TextView) findViewById(R.id.tv_title3);

        lv_data = (ListView) findViewById(R.id.lv_statistics_bottom);

        tv_title10.setText("卡号");
        tv_title11.setText("变动积分");
        tv_title12.setText("剩余积分");
        tv_title13.setText("详情");


        time_s.setOnClickListener(this);
        time_e.setOnClickListener(this);
        tv_query.setOnClickListener(this);

    }

    private void getHttpData() {
        if (list != null) {
            list.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        Map<String, String> map = new HashMap<>();
        String sTTime = time_s.getText().toString();
        String eNDTime = time_e.getText().toString();
        String cardNo = et_cardNum.getText().toString().trim();
        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString();

        map.put("BillType", "兑换记录,快速扣积分");
        map.put("UserinfoId", SharedUtil.getSharedData(ExchangeStatisticsActivity.this, "userInfoId"));
        map.put("dbName", SharedUtil.getSharedData(ExchangeStatisticsActivity.this, "dbname"));
        map.put("STTime", sTTime + " 00:00:00");
        map.put("ENDTime", eNDTime + " 23:59:59");
        map.put("CardNO", cardNo);
        map.put("Name", name);
        map.put("Phone", phone);
        postToHttp(NetworkUrl.USERECORD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.d(jsonText);
                ExchangeStatistics statistics = new Gson().fromJson(jsonText, ExchangeStatistics.class);
                if (statistics.getResult_stadus().equals("SUCCESS")) {
                    tv_title0.setText(statistics.getResult_count()+"条记录");
                    list = statistics.getResult_data();
                    adapter = new ExchangeStatisticsAdapter(ExchangeStatisticsActivity.this, list);
                    lv_data.setAdapter(adapter);
                } else {
                    Utils.showToast(ExchangeStatisticsActivity.this,statistics.getResult_errmsg());
                }


            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_statistics_time_start:
                dialogTag = "1";
                createBirthdayDialog();
                break;
            case R.id.tv_statistics_time_end:
                dialogTag = "2";
                createBirthdayDialog();
                break;
            case R.id.tv_statistics_commit:
                getHttpData();
                break;
        }
    }

    private void initcalendar() {
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date();
        mycalendar.setTime(mydate);
        curyear = mycalendar.get(Calendar.YEAR);
        curmonth = mycalendar.get(Calendar.MONTH);
        curday = mycalendar.get(Calendar.DAY_OF_MONTH);
        showMonth = curmonth + 1;
        if (0 < showMonth && showMonth < 10) {
            stringmonth = "0" + showMonth;
        } else {
            stringmonth = showMonth + "";
        }
        if (0 < curday && curday < 10) {
            stringday = "0" + curday;
        } else {
            stringday = curday + "";
        }
        getbirthday = curyear + "-" + stringmonth + "-" + stringday;
        time_s.setText(getbirthday);
        time_e.setText(getbirthday);
    }

    private void createBirthdayDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("日期选择");
        View contentView = LayoutInflater.from(this).inflate(R.layout.birthdayselect, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        showMonth = curmonth + 1;
        if (0 < showMonth && showMonth < 10) {
            stringmonth = "0" + showMonth;
        } else {
            stringmonth = showMonth + "";
        }

        if (0 < curday && curday < 10) {
            stringday = "0" + curday;

        } else {
            stringday = curday + "";
        }
        getbirthday = curyear + "-" + stringmonth + "-" + stringday;
        final DatePicker datePicker = (DatePicker) contentView.findViewById(R.id.dpPicker);
        datePicker.init(curyear, curmonth, curday, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                curyear = year;
                curmonth = monthOfYear;
                curday = dayOfMonth;
                // 获取一个日历对象，并初始化为当前选中的时间
                calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                format = new SimpleDateFormat(
                        "yyyy-MM-dd");
                getbirthday = format.format(calendar.getTime());

            }

        });

        final Button btnsure = (Button) contentView.findViewById(R.id.btn_sure);
        btnsure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                datePicker.clearFocus();
                if ("1".equals(dialogTag)) {
                    time_s.setText(getbirthday);
                } else {
                    time_e.setText(getbirthday);
                }
                dialog.dismiss();
            }
        });
        final Button btncancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

    }
}
