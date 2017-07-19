package com.ksk.obama.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.R;
import com.ksk.obama.adapter.CardTypeAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.CardTypeSelect;
import com.ksk.obama.model.OpenCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ksk.obama.utils.SharedUtil.getSharedData;

public class OpenCardActivity extends BaseReadCardActivity implements IReadCardId, IQrcodeCallBack {
    private EditText et_cardNum, et_name, et_pay;
    private TextView tv_cardType, tv_should, tv0, tv1, tv2, tv3, tv4;
    private TextView tv_recharge_rate;//充值倍率
    private ImageView iv_cardType;
    //private Button btnchange;
    private List<CardTypeSelect.ResultDataBean> list = new ArrayList<>();
    private ListView lv_card_type;
    private boolean isShow = false;
    private boolean isM1 = true;//是否弹窗
    private String goodsId = "";
    private String cardName;
    private String cardPrice;
    private String uid = "";
    private String cardNum = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_card);
        this.setOnReadCardId(this);
        this.setOnReadQrcode(this);
        initTitale();
        initView();
        setQX();
        getCardType();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(OpenCardActivity.this)) {
            queryDB(true);
        } else {
            int n = queryDB(false);
            Utils.showToast(OpenCardActivity.this, "当前无网络,您有" + n + "单子未上传");
        }
    }

    private void setQX() {
        String str = "";
        TextView tv = (TextView) findViewById(R.id.tv_hint);
        if (SharedUtil.getSharedBData(OpenCardActivity.this, "citiao")) {
            str += "磁条卡,";
        }
        if (SharedUtil.getSharedBData(OpenCardActivity.this, "nfc")) {
            str += "M1卡,";
        }
        if (SharedUtil.getSharedBData(OpenCardActivity.this, "saoma")) {
            str += "二维码卡";
        }

        tv.setText("当前可刷卡类型：" + str);
    }

    private int queryDB(boolean flag) {
        Connector.getDatabase();
        List<OpenCardDb> list = DataSupport.findAll(OpenCardDb.class);
        if (list != null && list.size() > 0 && flag) {
            startActivity(new Intent(OpenCardActivity.this, OpenCardSupplementActivity.class));
        }

        return list.size();
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
                isOpenCars();
            }
        });
    }

    private void initView() {
        et_cardNum = (EditText) findViewById(R.id.et_open_card_num);
        et_name = (EditText) findViewById(R.id.et_open_name);
        tv_cardType = (TextView) findViewById(R.id.tv_open_type);
        iv_cardType = (ImageView) findViewById(R.id.iv_open_type);
        et_pay = (EditText) findViewById(R.id.et_open_ss);
        tv_should = (TextView) findViewById(R.id.tv_open_ys);
        tv0 = (TextView) findViewById(R.id.tv_open0);
        tv1 = (TextView) findViewById(R.id.tv_open1);
        tv2 = (TextView) findViewById(R.id.tv_open2);
        tv3 = (TextView) findViewById(R.id.tv_open3);
        tv4 = (TextView) findViewById(R.id.tv_open4);
        //btnchange = (Button) findViewById(R.id.btn_change);
        tv_recharge_rate = (TextView) findViewById(R.id.tv_integral_rate);

        tv4.setText(SharedUtil.getSharedData(OpenCardActivity.this, "shopname"));


        findViewById(R.id.btn_read_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQrcodeActivity();
            }
        });

        iv_cardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null && list.size() > 0) {
                    AddPopWindow addPopWindow = new AddPopWindow(OpenCardActivity.this);
                    addPopWindow.showPopupWindow(tv_cardType);
                } else {
                    isShow = true;
                    getCardType();
                }
            }
        });

    }

    private void getCardType() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(OpenCardActivity.this, "dbname"));
        postToHttp(NetworkUrl.QUERY_MEMBER_CLASS, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                showCardType(jsonText);
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void showCardType(String json) {
        Logger.e(json);
        Gson gson = new Gson();
        CardTypeSelect cardtype = gson.fromJson(json, CardTypeSelect.class);
        String tag = cardtype.getResult_stadus();
        if (tag.equals("SUCCESS")) {
            list = cardtype.getResult_data();
            if (isShow) {
                AddPopWindow addPopWindow = new AddPopWindow(OpenCardActivity.this);
                addPopWindow.showPopupWindow(iv_cardType);
            }
        } else {
            String msg = cardtype.getResult_errmsg();
            Utils.showToast(OpenCardActivity.this, msg);

        }
    }

    private void isOpenCars() {
        String cardnum = et_cardNum.getText().toString();
        String name = et_name.getText().toString();
        String payshould = tv_should.getText().toString();
        String paymoney = et_pay.getText().toString();
        String str0 = tv0.getText().toString();
        String str1 = tv1.getText().toString();
        String str2 = tv2.getText().toString();
        String str3 = tv3.getText().toString();
        String str4 = tv4.getText().toString();
        if (TextUtils.isEmpty(cardnum)) {
            Utils.showToast(OpenCardActivity.this, "请填写卡号");
        } else if (TextUtils.isEmpty(name)) {
            Utils.showToast(OpenCardActivity.this, "请填写姓名");
        } else if (TextUtils.isEmpty(goodsId)) {
            Utils.showToast(OpenCardActivity.this, "请选择卡类型");
        } else if (TextUtils.isEmpty(paymoney)) {
            Utils.showToast(OpenCardActivity.this, "请填写实付金额");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("cardNO", et_cardNum.getText().toString());
            map.put("dbName", SharedUtil.getSharedData(OpenCardActivity.this, "dbname"));
            postToHttp(NetworkUrl.ISOPENCARD, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    changeActivity(jsonText);
                }

                @Override
                public void OnFail(String message) {
                    Utils.showToast(OpenCardActivity.this, message);
                }
            });
        }
    }

    private void changeActivity(String str) {
        try {
            JSONObject object = new JSONObject(str);
            String tag = object.getString("result_stadus");
            if (tag.equals("SUCCESS")) {
                String cardnum = et_cardNum.getText().toString();
                String name = et_name.getText().toString();
                String payshould = tv_should.getText().toString();
                String paymoney = et_pay.getText().toString();
                String str0 = tv0.getText().toString();
                String str1 = tv1.getText().toString();
                String str2 = tv2.getText().toString();
                String str3 = tv3.getText().toString();
                String str4 = tv4.getText().toString();
                String str5 = tv_recharge_rate.getText().toString();
                Intent intent = new Intent(OpenCardActivity.this, OpenCardInfoActivity.class);
                OpenCardInfo cardInfo = new OpenCardInfo();
                cardInfo.setCardnum(cardnum);
                cardInfo.setName(name);
                cardInfo.setCardName(cardName);
                cardInfo.setPayshould(payshould);
                cardInfo.setPaymoney(paymoney);
                cardInfo.setGoodsId(goodsId);
                cardInfo.setUid(uid);
                cardInfo.setCardName(tv_cardType.getText().toString());
                cardInfo.setPrice(cardPrice);
                cardInfo.setStr0(str0);
                cardInfo.setStr1(str1);
                cardInfo.setStr2(str2);
                cardInfo.setStr3(str3);
                cardInfo.setStr4(str4);

                Bundle bundle = new Bundle();
                bundle.putParcelable("info", cardInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                String msg = object.getString("result_errmsg");
                Utils.showToast(OpenCardActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler();

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        Log.e("uuz", "uid ：" + uid + "----- 卡号 :" + cardNo);
        if (cardNo.equals("") && (!UID.equals(""))) {
            if(isM1){
                isM1 = false;
                setCardnum();
            }

        }

        et_cardNum.setText(cardNo);

        switch (robotType) {
            case 1:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hasWindowFocus()) {
                            openRead();
                        }
                    }
                }, 2000);
                break;


        }
    }

    private void getSUNMICard(String cardNo) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(OpenCardActivity.this, "dbname"));
        map.put("gid", SharedUtil.getSharedData(OpenCardActivity.this, "groupid"));
        map.put("cardNO", cardNo);
        postToHttp(NetworkUrl.GETCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String code = object.getString("result_stadus");
                    if (code.equals("SUCCESS")) {
                        String cardNumber = object.getString("cardNO");
                        if (cardNumber != null)
                            et_cardNum.setText(cardNumber);
                    } else {
                        String msg = object.getString("result_errmsg");
                        Utils.showToast(OpenCardActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {

            }
        });
    }

    @Override
    public void OnReadQrcode(String number) {
        et_cardNum.setText(number);
    }

    private class AddPopWindow extends PopupWindow {
        private View conentView;

        public AddPopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popuwindow_card_type, null);
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            int h = dm.heightPixels;
            lv_card_type = (ListView) conentView.findViewById(R.id.lv_card_type);
            lv_card_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CardTypeSelect.ResultDataBean dataBean = list.get(position);
                    cardPrice = dataBean.getN_Price();
                    goodsId = dataBean.getId();
                    cardName = dataBean.getC_ClassName();
                    tv_cardType.setText(dataBean.getC_ClassName());
                    tv_should.setText(dataBean.getN_InitAmount());
                    et_pay.setText(dataBean.getN_Price());
                    tv0.setText(dataBean.getN_InitAmount());
                    tv1.setText(dataBean.getN_InitIntegral());
                    tv2.setText(dataBean.getN_DiscountValue());
                    tv3.setText(dataBean.getN_IntegralValue());
                    tv_recharge_rate.setText(dataBean.getN_Recharge_Integral_Value());
                    dismiss();
                }
            });

            lv_card_type.setAdapter(new CardTypeAdapter(OpenCardActivity.this, list));
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(w / 2 + 36);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(h / 3);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable();
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
//            // 设置SelectPicPopupWindow弹出窗体动画效果
//            this.setAnimationStyle(R.style.AnimationPreview);
//            LinearLayout addTaskLayout = (LinearLayout) conentView
//                    .findViewById(R.id.add_task_layout);
//            LinearLayout teamMemberLayout = (LinearLayout) conentView
//                    .findViewById(R.id.team_member_layout);
        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                // 以下拉方式显示popupwindow
                this.showAsDropDown(parent);
            } else {
                this.dismiss();
            }
        }
    }

    private void setCardnum() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(OpenCardActivity.this).inflate(R.layout.num_opencard_setcardnum, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        Button sure = (Button) contentView.findViewById(R.id.btn_sure);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                isM1 = true;
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isM1 = true;
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNum = editText.getText().toString().trim();
                et_cardNum.setText(cardNum);
                //isM1 = true;
//                btnchange.setVisibility(View.VISIBLE);
//                et_cardNum.setInputType(InputType.TYPE_NULL);
                if(cardNum.equals("")){
                    Utils.showToast(OpenCardActivity.this,"请输入卡号");
                    return;
                }
                bindCard(cardNum,uid);
                isM1 = true;
                window.dismiss();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.4));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_open_card), Gravity.CENTER, 0, 0);
    }

    private void bindCard(String cardNo, String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(OpenCardActivity.this, "dbname"));
        map.put("groupId", SharedUtil.getSharedData(OpenCardActivity.this, "groupid"));
        map.put("shopId",shopId);
        map.put("cardNO", cardNo);
        map.put("CardCode",uid);
        postToHttp(NetworkUrl.BINDCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String str = object.getString("result_stadus");
                    if(str.equals("SUCCESS")){
                        Utils.showToast(OpenCardActivity.this,"卡号绑定成功");
                    }else{
                        Utils.showToast(OpenCardActivity.this,"绑定失败请重试");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                Logger.e(message);
                Utils.showToast(OpenCardActivity.this,"绑定失败请重试");
                return;
            }
        });

    }
}
