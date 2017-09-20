package com.ksk.obama.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ksk.obama.utils.SharedUtil.getSharedData;

public class VipToShopActivity extends BuyShopReadActivity implements View.OnClickListener, IReadCardId {
    private Dialog mdialog;
    private ReadCardInfo.ResultDataBean cardInfo;
    private String oldPassword = "";
    private boolean flag = true;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_to_shop);
        this.setOnReadCardId(this);
        initTitale();
        initData();
        initView();
        close();
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
        tv_print.setVisibility(View.INVISIBLE);
        title_name.setText("会员到店");
    }

    private void initView() {

        LinearLayout ll_pw = (LinearLayout) findViewById(R.id.ll_vip_pw);
        LinearLayout ll_zl = (LinearLayout) findViewById(R.id.ll_vip_zl);
        LinearLayout ll_set = (LinearLayout) findViewById(R.id.ll_vip_set);
        LinearLayout ll_wx0 = (LinearLayout) findViewById(R.id.ll_vip_wx0);
        LinearLayout ll_wx1 = (LinearLayout) findViewById(R.id.ll_vip_wx1);
        LinearLayout ll_wx2 = (LinearLayout) findViewById(R.id.ll_vip_wx2);
        ll_pw.setOnClickListener(this);
        ll_zl.setOnClickListener(this);
        ll_set.setOnClickListener(this);
        ll_wx0.setOnClickListener(this);
        ll_wx1.setOnClickListener(this);
        ll_wx2.setOnClickListener(this);
        TextView textView = (TextView)findViewById(R.id.tv_vip_set);
        String s = robotType == 8 ? "会员卡绑定":"消息设置";
        textView.setText(s);
    }

    private void initData() {
        if (getIntent() != null) {
            cardInfo = getIntent().getExtras().getParcelable("infoma");
            uid = getIntent().getExtras().getString("uid");
        }
        TextView tv_cardNum = (TextView) findViewById(R.id.tv_vip_card_no);
        TextView tv_name = (TextView) findViewById(R.id.tv_vip_name);
        TextView tv_cardtype = (TextView) findViewById(R.id.tv_vip_type);
        TextView tv_cardtime = (TextView) findViewById(R.id.tv_vip_time);
        TextView tv_money = (TextView) findViewById(R.id.tv_vip_money);
        TextView tv_moneyzk = (TextView) findViewById(R.id.tv_vip_zhekou);
        TextView tv_gread = (TextView) findViewById(R.id.tv_vip_gread);
        TextView tv_greadzk = (TextView) findViewById(R.id.tv_vip_jifen);

        oldPassword = cardInfo.getC_Password();
        tv_cardNum.setText(cardInfo.getC_CardNO());
        tv_name.setText(cardInfo.getC_Name());
        tv_cardtype.setText(cardInfo.getC_ClassName());
        tv_cardtime.setText(cardInfo.getT_StopTime().substring(0, 11));
        tv_money.setText("￥" + cardInfo.getN_AmountAvailable());
        tv_moneyzk.setText(cardInfo.getN_DiscountValue() + "%");
        tv_gread.setText(cardInfo.getN_IntegralAvailable());
        tv_greadzk.setText(cardInfo.getN_IntegralValue() + "%");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_vip_pw:
                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip0")) {
                    if (TextUtils.isEmpty(oldPassword)) {
                        changePwActivity(110, ChangePasswordActivity.class);
                    } else {
                        showAlert();
                    }
                } else {
                    Utils.showToast(VipToShopActivity.this, "您没有权限");
                }
                break;
            case R.id.ll_vip_zl:
                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip1")) {
                    changePwActivity(1110, ChangeVipInfoActivity.class);
                } else {
                    Utils.showToast(VipToShopActivity.this, "您没有权限");
                }
                break;
            case R.id.ll_vip_set:
                switch (robotType){
                    case 1:
                    case 3:
                        case 8:
                        openRead();
                        mdialog = new Dialog(VipToShopActivity.this, R.style.BottomDialogStyle);
                        //填充对话框的布局
                        View view = LayoutInflater.from(VipToShopActivity.this).inflate(R.layout.dialog_viptoshop_bindcard, null);
                        //将布局设置给Dialog
                        mdialog.setContentView(view);
                        //获取当前Activity所在的窗体
                        Window dialogWindow = mdialog.getWindow();
                        //设置Dialog从窗体底部弹出
                        dialogWindow.setGravity(Gravity.CENTER);
                        //获得窗体的属性
                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        lp.width = (int) (dm.widthPixels * 0.95);
                        dialogWindow.setAttributes(lp); //将属性设置给窗体
                        mdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                close();
                            }
                        });
                        mdialog.show();//显示对话框
                        //mdialog.setOutsideTouchable(false);
                        mdialog.setCancelable(true);

                        break;
                    default:

                        break;
                }
//                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip2")) {

//                } else {
//                    Utils.showToast(VipToShopActivity.this, "您没有权限");
//                }

                break;
            case R.id.ll_vip_wx0:
                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip3")) {
                    if (flag) {
                        flag = false;
                        if (TextUtils.isEmpty(cardInfo.getC_OpenID())) {
                            changePwActivity(1111, ChangeWechatActivity.class);
                        } else {
                            Utils.showToast(VipToShopActivity.this, "您已经绑定过微信");
                        }
                    }
                } else {
                    Utils.showToast(VipToShopActivity.this, "您没有权限");
                }
                break;
            case R.id.ll_vip_wx1:
                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip4")) {
                    if (flag) {
                        flag = false;
                        changePwActivity(1111, ChangeWechatActivity.class);
                    }
                } else {
                    Utils.showToast(VipToShopActivity.this, "您没有权限");
                }
                break;
            case R.id.ll_vip_wx2:
                if (SharedUtil.getSharedBData(VipToShopActivity.this, "vip5")) {
                    getOpenId();
                } else {
                    Utils.showToast(VipToShopActivity.this, "您没有权限");
                }
                break;
        }
    }

    private void getOpenId() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(VipToShopActivity.this, "dbname"));
        map.put("cardNO", cardInfo.getC_CardNO());
        map.put("shopID", shopId);
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
                    @Override
                    public void OnSucess(String jsonText) {
                        Logger.e(jsonText);
                        ReadCardInfo readCard = new Gson().fromJson(jsonText, ReadCardInfo.class);
                        cardInfo = readCard.getResult_data();
                        if (readCard.getResult_stadus().equals("SUCCESS")) {
                            if (TextUtils.isEmpty(cardInfo.getC_OpenID())) {
                                Utils.showToast(VipToShopActivity.this, "您还没有绑定微信");
                            } else {
                                showUnbindWeChat();
                            }
                        } else {
                            Utils.showToast(VipToShopActivity.this, "请重试");
                        }
                    }

                    @Override
                    public void OnFail(String message) {
                        Utils.showToast(VipToShopActivity.this, message);
                    }
                }
        );
    }

    private void showUnbindWeChat() {
        final PopupWindow dialog = new PopupWindow(VipToShopActivity.this);
        View vv = LayoutInflater.from(VipToShopActivity.this).inflate(R.layout.order_alert_dialog, null);
        TextView tv_hint = (TextView) vv.findViewById(R.id.tv_hint);
        TextView tv_message = (TextView) vv.findViewById(R.id.tv_message);
        tv_hint.setText("微信解绑提示");
        tv_message.setText("您当前正在解绑微信账号,是否继续操作");
        vv.findViewById(R.id.btn_order_alert_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUnbind();
                dialog.dismiss();
            }
        });
        vv.findViewById(R.id.btn_order_alert_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(vv);
        dialog.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.update();
        ColorDrawable dw = new ColorDrawable();
        dialog.setBackgroundDrawable(dw);
        dialog.showAtLocation(findViewById(R.id.activity_vip_to_shop), Gravity.CENTER, 0, 0);

    }

    private void sendUnbind() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(VipToShopActivity.this, "dbname"));
        map.put("OpenID", cardInfo.getC_OpenID());
        map.put("GroupID", getSharedData(VipToShopActivity.this, "groupid"));
        postToHttp(NetworkUrl.REMOVEWX, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String msg = object.getString("result_stadus");
                    if (msg.equals("SUCCESS")) {
                        cardInfo.setC_OpenID("");
                        Utils.showToast(VipToShopActivity.this, "解绑成功");
                    } else {
                        Utils.showToast(VipToShopActivity.this, "解绑失败,请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(VipToShopActivity.this, message);
            }
        });

    }

    private void showAlert() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(VipToShopActivity.this).inflate(R.layout.vip_to_shop_alert, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.vip_alert_pw);
        editText.requestFocus();
        contentView.findViewById(R.id.btn_vip_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_vip_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = Utils.getMD5Code(editText.getText().toString());
                Logger.e(pw + "-----" + oldPassword);
                if (pw.equals(oldPassword)) {
                    changePwActivity(110, ChangePasswordActivity.class);
                } else {
                    Utils.showToast(VipToShopActivity.this, "您输入的密码不正确");
                }
                window.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth(w * 3 / 4);
        window.setHeight((int) (h * 0.4));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_vip_to_shop), Gravity.CENTER, 0, 0);
    }

    private void changePwActivity(int n, Class clazz) {
        Intent intent = new Intent(VipToShopActivity.this, clazz);
        intent.putExtra("pw", oldPassword);
        intent.putExtra("memid", cardInfo.getId());
        intent.putExtra("uid", uid);
        Bundle bundle = new Bundle();
        bundle.putParcelable("infoma", cardInfo);
        intent.putExtras(bundle);
        startActivityForResult(intent, n);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 110:
                    oldPassword = data.getStringExtra("pw");
                    cardInfo.setC_Password(oldPassword);
                    break;

                case 1110:
                    cardInfo = data.getExtras().getParcelable("infom");
                    break;
                case 1111:
                    cardInfo.setC_OpenID("sdfsf");
                    getCardInfo();
                    break;
            }
        }
    }

    private void getCardInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(VipToShopActivity.this, "dbname"));
        map.put("cardNO", cardInfo.getC_CardNO());

        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                ReadCardInfo readCard = new Gson().fromJson(jsonText, ReadCardInfo.class);
                if (readCard.getResult_stadus().equals("SUCCESS")) {
                    cardInfo = readCard.getResult_data();
                    flag = true;
                } else {
                    finish();
                }
            }

            @Override
            public void OnFail(String message) {
                finish();
            }
        });
    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        Log.d("uuz", "_" + cardNo + "*****" + UID);
        if(mdialog == null){return;}
        if (mdialog.isShowing()) {
            if (cardNo.equals("") && (!UID.equals(""))) {
                cardNo = cardInfo.getC_CardNO();
                bindCard(cardNo, UID);
                mdialog.dismiss();
                close();
            } else {
                Utils.showToast(VipToShopActivity.this, "该会员卡已绑定过卡号");
            }
        }
    }

    private synchronized void bindCard(String cardNo, String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("dbName", getSharedData(VipToShopActivity.this, "dbname"));
        map.put("groupId", getSharedData(VipToShopActivity.this, "groupid"));
        map.put("shopId", shopId);
        map.put("cardNO", cardNo);
        map.put("CardCode", uid);
        postToHttp(NetworkUrl.BINDCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String str = object.getString("result_stadus");
                    if (str.equals("SUCCESS")) {
                        Utils.showToast(VipToShopActivity.this, "卡号绑定成功");
                    } else {
                        Utils.showToast(VipToShopActivity.this, "绑定失败请重试");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {
                Logger.e(message);
                Utils.showToast(VipToShopActivity.this, "绑定失败请重试");
                return;
            }
        });

    }


}
