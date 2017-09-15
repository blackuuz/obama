package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.CouponCancelAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.model.CouponCancel;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CouponCancelActivity extends BaseActivity implements View.OnClickListener, IQrcodeCallBack {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.et_use_coupon_number)
    EditText etUseCouponNumber;
    @BindView(R.id.btn_use_coupon_qrCode)
    Button btnUseCouponQrCode;
    @BindView(R.id.btn_use_coupon_query)
    Button btnUseCouponQuery;
    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @BindView(R.id.btn_coupon_sure)
    Button btnCouponSure;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;

    private List<CouponCancel.ResultDataBean> c_list = new ArrayList();
    private String memberID = "";
    private String couponCode = "-1";
    private String couponId = "";
    private String couponState = "";

    private CouponCancelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conpon_cancle);
        ButterKnife.bind(this);
        setOnReadQrcode(this);
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘不顶起控件
    }

    private void initView() {
        titleName.setText("优惠券核销");
        tvCommit.setText("确认使用");
        tvBack.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        btnUseCouponQrCode.setOnClickListener(this);
        btnUseCouponQuery.setOnClickListener(this);
        btnCouponSure.setOnClickListener(this);

        GridLayoutManager mgr = new GridLayoutManager(this, 1);
        rvCoupon.setLayoutManager(mgr);
        if (c_list != null) {
            c_list.clear();
        }
        adapter = new CouponCancelAdapter(c_list);
        rvCoupon.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getHttpData() {
        couponCode = etUseCouponNumber.getText().toString().trim();
        if (couponCode.equals("")){
            couponCode = "-1";
            Utils.showToast(CouponCancelActivity.this, "请输入卡券编码");
            return;
        }
        Map map = new HashMap();
        map.put("shopId", SharedUtil.getSharedData(CouponCancelActivity.this, "shopid"));
        map.put("dbName", SharedUtil.getSharedData(CouponCancelActivity.this, "dbname"));
        //map.put("memberID", memberID);
        map.put("code", couponCode);
        if (c_list.size() > 0) {
            c_list.clear();
        }
        postToHttp(NetworkUrl.COUPONCANCEL, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                Logger.json(jsonText);
                CouponCancel coupon = new Gson().fromJson(jsonText, CouponCancel.class);
                if (coupon.getResult_stadus().equals("SUCCESS")) {
                    c_list.add(coupon.getResult_data());
                    couponState = coupon.getResult_data().getC_Status();
                    if (couponState.equals("核销")) {
                        Utils.showToast(CouponCancelActivity.this, "优惠券已核销");}

                } else if (coupon.getResult_stadus().equals("ERR")) {
                    String errmsg = coupon.getResult_errmsg();
                    Utils.showToast(CouponCancelActivity.this, errmsg);
                } else {
                    Utils.showToast(CouponCancelActivity.this, "未知错误");
                }
                if(couponState.equals("核销")){
                    c_list.clear();
                }
                adapter = new CouponCancelAdapter(c_list);
                rvCoupon.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnFail(String message) {
                Logger.e(message);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_use_coupon_qrCode:
                isCouponSaoma = true;
                toQrcodeActivity();
                break;
            case R.id.btn_use_coupon_query:
                getHttpData();
                break;
            case R.id.tv_commit:
                if(couponCode.equals("-1")){
                    Utils.showToast(CouponCancelActivity.this, "请选择优惠券");
                    return;
                }
                if (couponState.equals("核销")) {
                    Utils.showToast(CouponCancelActivity.this, "该优惠券已核销，不可用！");
                    return;
                }
                useCoupon();
                break;

        }
    }

    private void useCoupon() {
        Map map = new HashMap();
        map.put("shopId", SharedUtil.getSharedData(CouponCancelActivity.this, "shopid"));
        map.put("dbName", SharedUtil.getSharedData(CouponCancelActivity.this, "dbname"));
        map.put("groupId", SharedUtil.getSharedData(CouponCancelActivity.this, "groupid"));
        //map.put("memberID", memberID);
        map.put("code", couponCode);
        postToHttp(NetworkUrl.COUPONCANCELSURE, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                Logger.json(jsonText);
                try {
                    JSONObject js = new JSONObject(jsonText);
                    String str = js.getString("result_stadus");
                    if (str.equals("SUCCESS")) {
                        //
                        showAlert();
                        adapter.clean();
                    } else {
                        Utils.showToast(CouponCancelActivity.this, str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(CouponCancelActivity.this, "未知错误");
            }
        });

    }

    @Override
    public void OnReadQrcode(String number) {
        etUseCouponNumber.setText(number);
        loadingDialog.show();
        try {
            Thread.sleep(1500);
            getHttpData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

    }

    protected void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CouponCancelActivity.this);
        dialog.setTitle("提示:");
        dialog.setMessage("操作成功");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                couponCode = "-1";
                dialog.dismiss();

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.create();
        dialog.show();
    }
}
