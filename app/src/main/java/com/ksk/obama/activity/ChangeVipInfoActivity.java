package com.ksk.obama.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeVipInfoActivity extends BaseActivity implements View.OnClickListener {

    private List<ImageView> list = new ArrayList<>();
    private ReadCardInfo.ResultDataBean cardInfo;
    private TextView tv_cardnum;
    private int n = -1;
    private EditText et_name;
    private EditText et_phone;
    private TextView et_time;
    private EditText et_idCard;
    private EditText et_carNum;
    private EditText et_address;
    private TextView tv_shop;
    private String getbirthday;
    private String[] str;
    private String[] strs;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_vip_info);
        initTitale();
        initView();
        initTime();
        initListener();
        initData();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finished();
                finish();
            }
        });
        tv_print.setText("确定");
        title_name.setText("会员资料修改");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void initView() {
        tv_cardnum = (TextView) findViewById(R.id.tv_change_cardNum);
        et_name = (EditText) findViewById(R.id.et_change_name);
        et_phone = (EditText) findViewById(R.id.et_change_phone);
        et_time = (TextView) findViewById(R.id.et_change_time);
        et_idCard = (EditText) findViewById(R.id.et_change_idCard);
        et_carNum = (EditText) findViewById(R.id.et_change_carNum);
        et_address = (EditText) findViewById(R.id.et_change_address);
        tv_shop = (TextView) findViewById(R.id.et_change_shop);
    }

    private void initListener() {
        TextView tv_sex1 = (TextView) findViewById(R.id.open_info_tv1);
        TextView tv_sex2 = (TextView) findViewById(R.id.open_info_tv2);
        TextView tv_sex3 = (TextView) findViewById(R.id.open_info_tv3);
        ImageView iv_sex1 = (ImageView) findViewById(R.id.open_info_iv1);
        ImageView iv_sex2 = (ImageView) findViewById(R.id.open_info_iv2);
        ImageView iv_sex3 = (ImageView) findViewById(R.id.open_info_iv3);
        et_time.setOnClickListener(this);
        tv_sex1.setOnClickListener(this);
        tv_sex2.setOnClickListener(this);
        tv_sex3.setOnClickListener(this);
        iv_sex1.setOnClickListener(this);
        iv_sex2.setOnClickListener(this);
        iv_sex3.setOnClickListener(this);
        list.add(iv_sex1);
        list.add(iv_sex2);
        list.add(iv_sex3);
    }

    private void initData() {
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("infoma");
            uid = getIntent().getStringExtra("uid");
            tv_cardnum.setText(cardInfo.getC_CardNO());
            et_name.setText(cardInfo.getC_Name());
            String sex = cardInfo.getC_Sex();
            if (sex.equals("男")) {
                n = 0;
            } else if (sex.equals("女")) {
                n = 1;
            } else {
                n = 2;
            }
            changeImage(n);
            String mo = cardInfo.getI_BirthdayMonth();
            String da = cardInfo.getI_BirthdayDay();
            String ye = cardInfo.getI_BirthdayYear();
            et_phone.setText(cardInfo.getC_Mobile());
            et_time.setText((ye.length() < 4 ? "2000" : ye) + "-" + (mo.length() > 1 ? mo : "0" + mo) + "-" + (da.length() > 1 ? da : "0" + da));
            et_idCard.setText(cardInfo.getC_IDCard());
            et_carNum.setText(cardInfo.getC_Car());
            et_address.setText(cardInfo.getC_Add());
        }
        tv_shop.setText(cardInfo.getC_ShopName());
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

            case R.id.et_change_time:
                createBirthdayDialog();
                break;
        }
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

    private void initTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = dateFormat.format(date);
        strs = str.split("-");
    }

    private void createBirthdayDialog() {
        int a = 0, b = 0, c = 0;
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("生日选择");
        View contentView = LayoutInflater.from(this).inflate(R.layout.birthdayselect, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final DatePicker datePicker = (DatePicker) contentView.findViewById(R.id.dpPicker);
        if (TextUtils.isEmpty(cardInfo.getI_BirthdayYear())) {
            a = Integer.parseInt(strs[0]);
        } else {
            a = Integer.parseInt(cardInfo.getI_BirthdayYear());
        }
        if (TextUtils.isEmpty(cardInfo.getI_BirthdayMonth())) {
            b = Integer.parseInt(strs[1]) - 1;
        } else {
            b = Integer.parseInt(cardInfo.getI_BirthdayMonth()) - 1;
        }
        if (TextUtils.isEmpty(cardInfo.getI_BirthdayDay())) {
            c = Integer.parseInt(strs[2]);
        } else {
            c = Integer.parseInt(cardInfo.getI_BirthdayDay());

        }

        getbirthday = a + "-" + (b + 1) + "" + c;
        if (b < 10) {
            getbirthday = a + "-0" + (b + 1) + "-" + c;
        }

        if (c < 10) {
            getbirthday = a + "-0" + (b + 1) + "-0" + c;
        }
        datePicker.init(a, b, c, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                getbirthday = format.format(calendar.getTime());
            }
        });

        final Button btnsure = (Button) contentView.findViewById(R.id.btn_sure);
        btnsure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                datePicker.clearFocus();
                et_time.setText(getbirthday);
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

    private void sendData() {
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            Utils.showToast(ChangeVipInfoActivity.this,"请填写会员姓名");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("dbName", SharedUtil.getSharedData(ChangeVipInfoActivity.this, "dbname"));
            map.put("uid", SharedUtil.getSharedData(ChangeVipInfoActivity.this, "userInfoId"));
            map.put("memberid", cardInfo.getId());
            map.put("c_Billfrom", robotType + "");
            map.put("CardCode", uid);
            map.put("Name", et_name.getText().toString());
            switch (n) {
                case 0:
                    map.put("Sex", "男");
                    break;
                case 1:
                    map.put("Sex", "女");
                    break;
                case 2:
                    map.put("Sex", "保密");
                    break;
            }
            str = et_time.getText().toString().split("-");
            map.put("BirthdayYear", str[0]);
            map.put("BirthdayMonth", str[1]);
            map.put("BirthdayDay", str[2]);
            map.put("IDCard", et_idCard.getText().toString());
            map.put("Car", et_carNum.getText().toString());
            map.put("Address", et_address.getText().toString());
            map.put("Phone", et_phone.getText().toString());

            postToHttp(NetworkUrl.CHANGEVIP, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    showHttpData(jsonText);
                }

                @Override
                public void OnFail(String message) {
                    Utils.showToast(ChangeVipInfoActivity.this, message);
                }
            });
        }

    }

    private void showHttpData(String str1) {
        try {
            JSONObject object = new JSONObject(str1);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                cardInfo.setC_Name(et_name.getText().toString());
                switch (n) {
                    case 0:
                        cardInfo.setC_Sex("男");
                        break;
                    case 1:
                        cardInfo.setC_Sex("女");
                        break;
                    case 2:
                        cardInfo.setC_Sex("保密");
                        break;
                }
                cardInfo.setC_Mobile(et_phone.getText().toString());
                cardInfo.setI_BirthdayYear(str[0]);
                cardInfo.setI_BirthdayMonth(str[1]);
                cardInfo.setI_BirthdayDay(str[2]);
                cardInfo.setC_IDCard(et_idCard.getText().toString());
                cardInfo.setC_Car(et_carNum.getText().toString());
                cardInfo.setC_Add(et_address.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示:");
                builder.setMessage("资料修改成功");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finished();
                        finish();
                    }
                });
                builder.create().show();
            } else {
                String msg1 = object.getString("result_errmsg");
                Utils.showToast(ChangeVipInfoActivity.this, msg1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void finished() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("infom", cardInfo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        finished();
        super.onBackPressed();
    }
}
