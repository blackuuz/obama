package com.ksk.obama.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.model.OpenCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class


OpenCardInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv_birthday;
    private EditText et_phone;
    private EditText et_yzm;
    private EditText et_pw1;
    private EditText et_pw2;
    private Button et_btn;
    private Button btn_add_shop;
    private Button btn_add_vip;
    private int curyear;
    private int curmonth;
    private int curday;
    private int showMonth;
    private String stringmonth, stringday;
    private String getbirthday;
    private SimpleDateFormat format;
    private Calendar calendar;
    private List<ImageView> list = new ArrayList<>();
    private int i = 61;
    private int n = 2;
    private String addId = "";
    private String addName = "";
    private String vipAddId = "";
    private String vipAddName = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (i > 0) {
                        et_btn.setText(i + "后重新获取验证码");
                    } else {
                        et_btn.setBackgroundResource(R.mipmap.query);
                        et_btn.setText("点击重新获取");
                        et_btn.setEnabled(true);
                    }
                    break;
            }
        }
    };
    private String code = "";
    private OpenCardInfo cardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_card_info);
        cardInfo = getIntent().getExtras().getParcelable("info");
        initTime();
        initTitale();
        initView();
        changeImage(2);

    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("下一步");
        title_name.setText("会员开卡");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }

    private void initView() {
        iv1 = (ImageView) findViewById(R.id.open_info_iv1);
        iv2 = (ImageView) findViewById(R.id.open_info_iv2);
        iv3 = (ImageView) findViewById(R.id.open_info_iv3);
        tv1 = (TextView) findViewById(R.id.open_info_tv1);
        tv2 = (TextView) findViewById(R.id.open_info_tv2);
        tv3 = (TextView) findViewById(R.id.open_info_tv3);
        tv_birthday = (TextView) findViewById(R.id.tv_open_info_birthday);
        et_phone = (EditText) findViewById(R.id.et_open_info_phone);
        et_yzm = (EditText) findViewById(R.id.et_open_info_yzm);
        et_pw1 = (EditText) findViewById(R.id.et_open_info_pw1);
        et_pw2 = (EditText) findViewById(R.id.et_open_info_pw2);
        et_btn = (Button) findViewById(R.id.btn_yzm);
        btn_add_shop = (Button) findViewById(R.id.add_shop);
        btn_add_vip = (Button) findViewById(R.id.add_vip);

        list.add(iv1);
        list.add(iv2);
        list.add(iv3);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        et_btn.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        btn_add_shop.setOnClickListener(this);
        btn_add_vip.setOnClickListener(this);
    }


    private void changeImage(int n) {
        for (int i = 0; i < list.size(); i++) {
            if (i == n) {
                list.get(i).setImageResource(R.mipmap.sel_box_right);
            } else {
                list.get(i).setImageResource(R.mipmap.sel_box);
            }
        }
    }

    private void changeActivity() {
        if (isChange()) {
            if (getIntent() != null) {
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    change();
                } else {
                    if (Utils.isMobileNO(et_phone.getText().toString())) {
                        change();
                    } else {
                        Utils.showToast(OpenCardInfoActivity.this, "请填写正确的手机号");
                    }
                }

            }
        } else {
            Utils.showToast(OpenCardInfoActivity.this, "消费密码与确认密码必须一致");
        }
    }

    private void change() {
        String pw1 = et_pw1.getText().toString();
        String pw2 = et_pw2.getText().toString();
        if (pw1.equals(pw2)) {
            switch (n) {
                case 0:
                    cardInfo.setSex("男");
                    break;
                case 1:
                    cardInfo.setSex("女");
                    break;

                case 2:
                    cardInfo.setSex("保密");
                    break;
            }
            cardInfo.setPhone(et_phone.getText().toString());
            cardInfo.setBirthday(tv_birthday.getText().toString());
            cardInfo.setPassword(et_pw1.getText().toString());
            cardInfo.setAddId(addId);
            cardInfo.setAddPerson(addName);//推荐员工
            cardInfo.setVipAddId(vipAddId);
            cardInfo.setVipAddPerson(vipAddName);//推荐会员
            Bundle bundle = new Bundle();
            bundle.putParcelable("info", cardInfo);
            Intent intent = new Intent(OpenCardInfoActivity.this, PayOpenCardActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Utils.showToast(OpenCardInfoActivity.this, "两次密码必须一致");
        }

    }


    private boolean isChange() {
        String pw1 = et_pw1.getText().toString();
        String pw2 = et_pw2.getText().toString();
        if (pw1.equals(pw2)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_info_iv1:
            case R.id.open_info_tv1:
                n = 0;
                changeImage(0);
                break;
            case R.id.open_info_iv2:
            case R.id.open_info_tv2:
                n = 1;
                changeImage(1);
                break;
            case R.id.open_info_iv3:
            case R.id.open_info_tv3:
                n = 2;
                changeImage(2);
                break;

//            case R.id.btn_yzm:
//                getMessage();
//                break;

            case R.id.tv_open_info_birthday:
                createBirthdayDialog();
                break;

            case R.id.add_shop:
                startActivityForResult(new Intent(OpenCardInfoActivity.this, AddPersonActivity.class).putExtra("type", 1), 100);
                break;
            case R.id.add_vip:
                startActivityForResult(new Intent(OpenCardInfoActivity.this, AddPersonActivity.class).putExtra("type", 2), 101);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name= "";
        if (resultCode == RESULT_OK) {
            if (data != null) {
//                addId = data.getStringExtra("id");
                 name = data.getStringExtra("name");
            }
            switch (requestCode) {
                case 100:
                    btn_add_shop.setText("推荐员工:" + name);
                    if (data != null) {
                        addId = data.getStringExtra("id");
                        addName = data.getStringExtra("name");
                    }
                   //btn_add_vip.setText("点击选择推荐会员");
                    break;
                case 101:
                    btn_add_vip.setText("推荐会员:" + name);
                    //btn_add_shop.setText("点击选择推荐员工");

                    if (data != null) {
                        vipAddId = data.getStringExtra("id");
                        vipAddName = data.getStringExtra("name");
                    }
                    break;
            }
        }
    }

    private void getMessage() {
        String phone = et_phone.getText().toString();
        if (Utils.isMobileNO(phone)) {
            Map<String, String> map = new HashMap<>();
            map.put("Phone", phone);
            postToHttp(NetworkUrl.SMS, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    Logger.e(jsonText);
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String tag = object.getString("result_stadus");
                        if (tag.equals("SUCCESS")) {
                            et_btn.setBackgroundResource(R.mipmap.query_0);
                            et_btn.setEnabled(false);
                            changeTime();
                            Utils.showToast(OpenCardInfoActivity.this, "验证码发送成功");
                        } else {
                            Utils.showToast(OpenCardInfoActivity.this, "验证码发送失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFail(String message) {
                    Utils.showToast(OpenCardInfoActivity.this, "验证码发送失败");
                }
            });
        } else {
            Utils.showToast(OpenCardInfoActivity.this, "请输入正确的手机号");
        }
    }

    private void changeTime() {
        if (i == 0)
            i = 61;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
                if (i == 0)
                    return;
                i--;
            }
        };

        Timer mTimer = new Timer();
        mTimer.schedule(timerTask, 1000, 1000);
    }

    private void initTime() {
        calendar = Calendar.getInstance();
        curyear = calendar.get(Calendar.YEAR);
        curmonth = calendar.get(Calendar.MONTH);
        curday = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void createBirthdayDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("生日选择");
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
                tv_birthday.setText(getbirthday);
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
