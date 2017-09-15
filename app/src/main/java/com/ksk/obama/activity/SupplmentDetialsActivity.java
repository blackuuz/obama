package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.R;
import com.ksk.obama.adapter.supplementDetialsAdapter;
import com.ksk.obama.callback.IHttpCallBack;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ksk.obama.utils.SharedUtil.getSharedData;

public class SupplmentDetialsActivity extends BaseActivity {


    @BindView(R.id.tv_quick_dialog_shop)
    TextView tvQuickDialogShop;
    @BindView(R.id.tv_quick_dialog_serial)
    TextView tvQuickDialogSerial;
    @BindView(R.id.tv_quick_dialog_employee)
    TextView tvQuickDialogEmployee;
    @BindView(R.id.tv_quick_dialog_ordertime)
    TextView tvQuickDialogOrdertime;
    @BindView(R.id.tv_quick_dialog_ordernumber)
    TextView tvQuickDialogOrdernumber;
    @BindView(R.id.tv_quick_dialog_vipname)
    TextView tvQuickDialogVipname;
    @BindView(R.id.tv_quick_dialog_vipnumber)
    TextView tvQuickDialogVipnumber;
    @BindView(R.id.tv_quick_dialog_type)
    TextView tvQuickDialogType;
    @BindView(R.id.tv_quick_dialog_money)
    TextView tvQuickDialogMoney;
    @BindView(R.id.tv_quick_dialog_actual_money)
    TextView tvQuickDialogActualMoney;
    @BindView(R.id.tv_quick_dialog_del_integral)
    TextView tvQuickDialogDelIntegral;
    @BindView(R.id.tv_quick_dialog_get_integral)
    TextView tvQuickDialogGetIntegral;
    @BindView(R.id.tv_quick_dialog_have_money)
    TextView tvQuickDialogHaveMoney;
    @BindView(R.id.tv_quick_dialog_have_integral)
    TextView tvQuickDialogHaveIntegral;
    @BindView(R.id.fragment_quick_supplement)
    LinearLayout fragmentQuickSupplement;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_title0)
    TextView tvTitle0;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_title3)
    TextView tvTitle3;
    @BindView(R.id.lv_statistics_bottom)
    ListView lvStatisticsBottom;
    @BindView(R.id.item_ll_lv_detials)
    LinearLayout itemLlLvDetials;
    private List<?> list = new ArrayList<>();
    private String shop, employee, serial, orderTime, ordernum, vipNo, vipName, payType, money, actualMoney, delIntegral, getIntegral, haveMoney, haveIntegral, favroableMoney;
    private boolean flag = false;
    String clazz = "";
    private supplementDetialsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_supplment_detials);
        ButterKnife.bind(this);
        queryDb();
        initView();
    }

    private void initView() {
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = SharedUtil.getSharedBData(SupplmentDetialsActivity.this, "bdsc");//判断是否有补单删除权限
                if (flag) {
                    showDialog();
                } else {
                    Arrived();
                }

            }
        });
        titleName.setText("订单详情");
        tvQuickDialogShop.setText(shop);
        tvQuickDialogEmployee.setText(employee);
        tvQuickDialogSerial.setText(serial);
        tvQuickDialogOrdertime.setText(orderTime);
        tvQuickDialogOrdernumber.setText(ordernum);
        tvQuickDialogVipnumber.setText(vipNo);
        tvQuickDialogVipname.setText(vipName);
        tvQuickDialogType.setText(payType);
        tvQuickDialogMoney.setText(money);
        tvQuickDialogActualMoney.setText(actualMoney);
        tvQuickDialogDelIntegral.setText(delIntegral);
        tvQuickDialogGetIntegral.setText(getIntegral);
//        tvQuickDialogHaveMoney.setText(haveMoney);
//        tvQuickDialogHaveIntegral.setText(haveIntegral);
        tvCommit.setText("消除此订单");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplmentDetialsActivity.this.finish();
            }
        });

        switch (clazz) {
            case "Quick":
                tvQuickDialogHaveIntegral.setVisibility(View.VISIBLE);
                tvQuickDialogHaveMoney.setVisibility(View.VISIBLE);
                tvQuickDialogHaveMoney.setText(haveMoney);
                tvQuickDialogHaveIntegral.setText(haveIntegral);
                break;
            case "Shop":
                tvTitle0.setText("商品名");
                tvTitle1.setText("单价");
                tvTitle2.setText("数量");
                tvTitle3.setText("小计");
                lvStatisticsBottom.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case "Count":
                tvTitle0.setText("商品名");
                tvTitle1.setText("单价");
                tvTitle2.setText("数量");
                tvTitle3.setText("小计");
                lvStatisticsBottom.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;

        }


    }

    private void queryDb() {
        Connector.getDatabase();

        //flag = getIntent().getBooleanExtra("flag", false);
        clazz = getIntent().getStringExtra("clazz");
        switch (clazz) {
            case "Quick":
                list = DataSupport.findAll(QuickDelMoney.class);
                fromQuick();
                break;
            case "Shop":
                list = DataSupport.findAll(BuyShopDb.class,true);
                fromShop();
                break;
            case "Count":
                list = DataSupport.findAll(BuyCountDb.class,true);
                fromCount();
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SupplmentDetialsActivity.this);
        builder.setMessage("确认删除此条补传订单么?该操作可能会造成订单丢失.如果删除,删除后请仔细核对当前订单是否记录,如果没有,请重新记录.");
        builder.setTitle("重要提示！");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(RESULT_OK);
                Utils.showToast(SupplmentDetialsActivity.this, "执行清除动作");
                SupplmentDetialsActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.showToast(SupplmentDetialsActivity.this, "取消");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void Arrived() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(SupplmentDetialsActivity.this).inflate(R.layout.pop_temporary, null);
        final EditText et_num = (EditText) contentView.findViewById(R.id.et_num);
        final EditText et_pwd = (EditText) contentView.findViewById(R.id.et_pwd);
        TextView tvAuthority1 = (TextView) contentView.findViewById(R.id.tv_authority_title1);
        TextView tvAuthority2 = (TextView) contentView.findViewById(R.id.tv_authority_title2);
        tvAuthority1.setText("确认删除此条补传订单么?该操作可能会造成订单丢失.如果删除,删除后请仔细核对当前订单是否记录,如果没有,请重新记录.");
        tvAuthority2.setVisibility(View.GONE);

        ImageView back = (ImageView) contentView.findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        contentView.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = et_num.getText().toString();
                String str2 = et_pwd.getText().toString();
                if (TextUtils.isEmpty(str1)) {
                    Utils.showToast(SupplmentDetialsActivity.this, "请输入工号");
                } //else if (TextUtils.isEmpty(str2)) {
//                    Utils.showToast(RechargeActivity.this, "请输入密码");
                else {
                    getTemporary(str1, str2);
                    window.dismiss();
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (h * 0.5));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.fragment_quick_supplement), Gravity.CENTER, 0, 0);
    }

    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(SupplmentDetialsActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
//                    result = j.getString("result_name");
                    int bdsc = s.indexOf("POS:补单删除");

                    Logger.e("" + bdsc);

                    if (bdsc > 0) {
                        Utils.showToast(SupplmentDetialsActivity.this, "授权成功");
                        setResult(RESULT_OK);
                        Utils.showToast(SupplmentDetialsActivity.this, "执行清除动作");
                        SupplmentDetialsActivity.this.finish();
                    } else {
                        Utils.showToast(SupplmentDetialsActivity.this, "您没有权限或密码错误");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(SupplmentDetialsActivity.this, "服务器异常");
                }
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(SupplmentDetialsActivity.this, "临时授权失败");
            }

        });
    }

    private void fromQuick() {
        flag = getIntent().getBooleanExtra("flag", false);
        QuickDelMoney upLoading = (QuickDelMoney) list.get(0);
        shop = "消费店面:" + upLoading.getShopName();
        employee = "操作员:" + upLoading.getUserName();
        serial = "手持序列号:" + upLoading.getEquipmentNum();
        orderTime = "订单生成时间:" + upLoading.getOrderTime();
        ordernum = "单据号:" + upLoading.getOrderNo();
        if (flag) {
            vipNo = "会员卡号:" + upLoading.getCardNo();
            vipName = "会员姓名:" + upLoading.getCardName();
        } else {
            vipNo = "会员卡号:散客";
            vipName = "会员姓名:散客";
        }
        if (upLoading.isIntegral()) {//是否使用了积分抵现
            money = "消费金额:" + upLoading.getDelMoney();
        } else {
            money = "消费金额:" + upLoading.getMoney();
        }

        if (flag) {
            actualMoney = "实收金额:" + upLoading.getMoney();
        }
        switch (upLoading.getN()) {
            case 0:
                payType = "支付方式:现金支付";
                break;
            case 1:
                payType = "支付方式:微信支付";
                break;
            case 2:
                payType = "支付方式:支付宝支付";
                break;
            case 3:
                payType = "支付方式:第三方支付";
                break;
            case 4:
                payType = "支付方式:会员卡支付";
                break;
        }
        if (flag) {
            delIntegral = "抵现积分:" + upLoading.getDelIntegral();
            getIntegral = "获得积分:" + upLoading.getGetIntegral();
//            haveMoney = "可用储值:￥" + haveMoney;
//            haveIntegral = "可用积分:" + haveIntegral;
        }

    }

    private void fromShop() {

        BuyShopDb upLoading = (BuyShopDb) list.get(0);
        shop = "消费店面:" + upLoading.getShopName();
        employee = "操作员:" + upLoading.getUserName();
        serial = "手持序列号:" + upLoading.getEquipmentNum();
        orderTime = "订单生成时间:" + upLoading.getOrderTime();
        ordernum = "单据号:" + upLoading.getOrderNo();
        vipNo = "会员卡号:" + upLoading.getCardNO();
        vipName = "会员姓名:" + upLoading.getCardName();
        money = "总金额:"+upLoading.getPayShould();

       if (upLoading.isVip()) {//是否是会员
            actualMoney = "实收金额:" + upLoading.getPayActual();
        } else {
           actualMoney = "实收金额:" + upLoading.getPayActual();
        }
//
//        if (flag) {
//            actualMoney = "实收金额:" + upLoading.getMoney();
//        }
        switch (upLoading.getN()) {
            case 0:
                payType = "支付方式:现金支付";
                break;
            case 1:
                payType = "支付方式:微信支付";
                break;
            case 2:
                payType = "支付方式:支付宝支付";
                break;
            case 3:
                payType = "支付方式:第三方支付";
                break;
            case 4:
                payType = "支付方式:会员卡支付";
                break;
        }
        if (upLoading.isVip()) {
             delIntegral = "抵现积分:" + upLoading.getDeljf();
             getIntegral = "获得积分:" + upLoading.getGet_integral();
//            haveMoney = "可用储值:￥" + haveMoney;
//            haveIntegral = "可用积分:" + haveIntegral;
        }
        adapter = new supplementDetialsAdapter(SupplmentDetialsActivity.this,upLoading.getDataList());
//        Log.d("uuz", "initView: shop方法"+upLoading.getDataList().get(0).getName());



    }

    private void fromCount() {
        BuyCountDb upLoading = (BuyCountDb) list.get(0);
        shop = "消费店面:" + upLoading.getShopName();
        employee = "操作员:" + upLoading.getUserName();
        serial = "手持序列号:" + upLoading.getEquipmentNum();
        orderTime = "订单生成时间:" + upLoading.getOrderTime();
        ordernum = "单据号:" + upLoading.getOrderNo();
        vipNo = "会员卡号:" + upLoading.getCardNo();
        vipName = "会员姓名:" + upLoading.getCardName();

        switch (upLoading.getN()) {
            case 0:
                payType = "支付方式:现金支付";
                break;
            case 1:
                payType = "支付方式:微信支付";
                break;
            case 2:
                payType = "支付方式:支付宝支付";
                break;
            case 3:
                payType = "支付方式:第三方支付";
                break;
            case 4:
                payType = "支付方式:会员卡支付";
                break;
        }
        if (flag) {
            delIntegral = "抵现积分:" + upLoading.getDx_Integral();
            getIntegral = "获得积分:" + upLoading.getGetIntegral();
//            haveMoney = "可用储值:￥" + haveMoney;
//            haveIntegral = "可用积分:" + haveIntegral;
        }
        money = "消费金额:" + upLoading.getPayshould();
        actualMoney = "实收金额 :" + upLoading.getPayactual();
        favroableMoney = "优惠金额 :" + upLoading.getYouhui();
        getIntegral = "获得积分 :" + upLoading.getGetIntegral();
        haveMoney = "剩余储值 :" + haveMoney;
        haveIntegral = "剩余积分 :" + haveIntegral;
        adapter = new supplementDetialsAdapter(SupplmentDetialsActivity.this,upLoading.getDataList());
        Log.d("uuz", "initView: count方法"+upLoading.getDataList().get(0).getName());

    }


}
