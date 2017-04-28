package com.ksk.obama.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CardDelayedActivity extends BaseActivity implements View.OnClickListener {

    private String memid;
    private EditText et_year;
    private EditText et_month;
    private EditText et_day;

    private int curyear;
    private int curmonth;
    private int curday;
    private int ocuryear;
    private int ocurmonth;
    private int ocurday;
    private int daysCountOfMonth;
    private Calendar mycalendar;
    private String stop_time = "";
    private TextView tv_time;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_delayed);
        initTitale();
        initView();
        mycalendar = Calendar.getInstance(Locale.CHINA);
        initTime();
    }

    private void initTitale() {
        uid = getIntent().getExtras().getString("uid");
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("确定");
        title_name.setText("卡片延期");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardDelayed();
            }
        });
    }

    private void initView() {
        if (getIntent() != null) {
            ReadCardInfo.ResultDataBean cardInfo = getIntent().getExtras().getParcelable("infoma");
            memid = cardInfo.getId();
            stop_time = cardInfo.getT_StopTime().substring(0, 11).trim();

            TextView tv_cardNum = (TextView) findViewById(R.id.tv_card_num);
            TextView tv_name = (TextView) findViewById(R.id.tv_card_name);
            tv_time = (TextView) findViewById(R.id.tv_card_time);

            tv_cardNum.setText(cardInfo.getC_CardNO());
            tv_name.setText(cardInfo.getC_Name());
            tv_time.setText(stop_time);

            et_year = (EditText) findViewById(R.id.et_time_year);
            et_month = (EditText) findViewById(R.id.et_time_mouth);
            et_day = (EditText) findViewById(R.id.et_time_day);
            ImageView iv_add_y = (ImageView) findViewById(R.id.iv_click_add_year);
            ImageView iv_add_m = (ImageView) findViewById(R.id.iv_click_add_mouth);
            ImageView iv_add_d = (ImageView) findViewById(R.id.iv_click_add_day);
            ImageView iv_del_y = (ImageView) findViewById(R.id.iv_click_del_year);
            ImageView iv_del_m = (ImageView) findViewById(R.id.iv_click_del_mouth);
            ImageView iv_del_d = (ImageView) findViewById(R.id.iv_click_del_day);
            iv_add_y.setOnClickListener(this);
            iv_add_m.setOnClickListener(this);
            iv_add_d.setOnClickListener(this);
            iv_del_y.setOnClickListener(this);
            iv_del_m.setOnClickListener(this);
            iv_del_d.setOnClickListener(this);
        }
    }

    private void initTime() {
        String str[] = stop_time.split("-");
        curyear = Integer.parseInt(str[0]);
        curmonth = Integer.parseInt(str[1]);
        curday = Integer.parseInt(str[2]);

        mycalendar.set(Calendar.YEAR, curyear);//先指定年份
        mycalendar.set(Calendar.MONTH, curmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);

        ocuryear = curyear;
        ocurmonth = curmonth;
        ocurday = curday;
        et_year.setText(ocuryear + "");
        et_month.setText(ocurmonth + "");
        et_day.setText(ocurday + "");
    }

    private void cardDelayed() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(CardDelayedActivity.this, "dbname"));
        map.put("MemberID", memid);
        map.put("GroupID", SharedUtil.getSharedData(CardDelayedActivity.this, "groupid"));
        map.put("StopTime", curyear + "-" + curmonth + "-" + curday);
        map.put("c_Billfrom", robotType + "");
        map.put("CardCode", uid);
        map.put("UserInfoID", SharedUtil.getSharedData(CardDelayedActivity.this, "userInfoId"));
        postToHttp(NetworkUrl.CARDDELAYED, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String msg = object.getString("result_stadus");
                    if (msg.equals("SUCCESS")) {
                        stop_time = curyear + "-" + curmonth + "-" + curday;
                        tv_time.setText(stop_time);
                        initTime();
                        Utils.showToast(CardDelayedActivity.this, "卡片延期成功");
                    } else {
                        String msg1 = object.getString("result_errmsg");
                        Utils.showToast(CardDelayedActivity.this, msg1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(CardDelayedActivity.this, message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_click_add_year:
                curyear += 1;
                et_year.setText(curyear + "");
                break;
            case R.id.iv_click_del_year:
                if (curyear > ocuryear) {
                    curyear -= 1;
                }
                et_year.setText(curyear + "");
                break;
            case R.id.iv_click_add_mouth:
                if (curyear == ocuryear) {
                    if (curmonth == 12) {
                        curmonth = ocurmonth;
                    } else {
                        curmonth += 1;
                    }
                } else {
                    if (curmonth == 12) {
                        curmonth = 1;
                    } else {
                        curmonth += 1;
                    }
                }
                et_month.setText(curmonth + "");
                break;
            case R.id.iv_click_del_mouth:
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        curmonth = 12;
                    } else {
                        curmonth -= 1;
                    }
                } else {
                    if (curmonth == 1) {
                        curmonth = 12;
                    } else {
                        curmonth -= 1;
                    }
                }
                et_month.setText(curmonth + "");
                break;
            case R.id.iv_click_add_day:
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        if (curday == daysCountOfMonth) {
                            curday = ocurday;
                        } else {
                            curday += 1;
                        }
                    } else {
                        if (curday == daysCountOfMonth) {
                            curday = 1;
                        } else {
                            curday += 1;
                        }
                    }
                } else {
                    if (curday == daysCountOfMonth) {
                        curday = 1;
                    } else {
                        curday += 1;
                    }
                }
                et_day.setText(curday + "");
                break;
            case R.id.iv_click_del_day:
                if (curyear == ocuryear) {
                    if (curmonth == ocurmonth) {
                        if (curday == ocurday) {
                            curday = daysCountOfMonth;
                        } else {
                            curday -= 1;
                        }
                    } else {
                        if (curday == 1) {
                            curday = daysCountOfMonth;
                        } else {
                            curday -= 1;
                        }
                    }
                } else {
                    if (curday == 1) {
                        curday = daysCountOfMonth;
                    } else {
                        curday -= 1;
                    }
                }
                et_day.setText(curday + "");
                break;
        }
        mycalendar.set(Calendar.YEAR, curyear);//先指定年份
        mycalendar.set(Calendar.MONTH, curmonth - 1);//再指定月份 Java月份从0开始算
        daysCountOfMonth = mycalendar.getActualMaximum(Calendar.DATE);
    }

}
