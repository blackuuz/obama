package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksk.obama.DB.RechargeAgain;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/18. 读卡界面
 */

public class ReadCardInfoActivity extends BaseReadCardActivity implements IReadCardId, IQrcodeCallBack {

    private EditText et_cradNum;
    private int n;
    private boolean flag = false;
    private String preID = "";
    private String uid = "";
    private List<ReadCardInfo.ResultDataBean> data = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_card_info);
        if (getIntent() != null) {
            n = getIntent().getIntExtra("type", -1);
        }
        initTitale();
        this.setOnReadCardId(this);
        this.setOnReadQrcode(this);
        et_cradNum = (EditText) findViewById(R.id.et_read_card_num);
        if (SharedUtil.getSharedData(ReadCardInfoActivity.this, "isedit").equals("0")) {
            et_cradNum.setInputType(InputType.TYPE_NULL);
        }
        Button yes = (Button) findViewById(R.id.btn_read_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    sendId("");
                }
            }
        });

        setQX();

        Button code = (Button) findViewById(R.id.btn_read_code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQrcodeActivity();
            }
        });
    }

    private void setQX() {
        String str = "";
        TextView tv = (TextView) findViewById(R.id.tv_hint);
        if (SharedUtil.getSharedBData(ReadCardInfoActivity.this, "citiao")) {
            str += "磁条卡,";
        }
        if (SharedUtil.getSharedBData(ReadCardInfoActivity.this, "nfc")) {
            str += "M1卡,";
        }
        if (SharedUtil.getSharedBData(ReadCardInfoActivity.this, "saoma")) {
            str += "二维码卡";
        }

        tv.setText("当前可刷卡类型：" + str);
    }

    private void sendId(String id) {
        if (!TextUtils.isEmpty(et_cradNum.getText().toString())) {
            id = et_cradNum.getText().toString();
        }
        if (TextUtils.isEmpty(id)) {
            isclick_pay = true;
            Utils.showToast(ReadCardInfoActivity.this, "请填写卡号");
        } else {
            if (id.equals(preID)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("重要提示：");
                builder.setMessage("此次卡号" + id + "与上次卡号" + preID + "重复,是否继续操作?");
                builder.setNegativeButton("取消", null);
                final String finalId = id;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendIdToHttp(finalId);
                    }
                });
                builder.create().show();
                isclick_pay = true;
            } else {
                sendIdToHttp(id);
            }
        }
    }

    private void sendIdToHttp(String id) {
        isclick_pay = false;
        preID = id;
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(ReadCardInfoActivity.this, "dbname"));
        map.put("cardNO", id);
        map.put("CardCode", uid);
        map.put("gid", SharedUtil.getSharedData(ReadCardInfoActivity.this, "groupid"));
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                Logger.json(jsonText);
                isclick_pay = true;
                changeActivity(jsonText);
            }

            @Override
            public void OnFail(String message) {
                isclick_pay = true;
                openRead();
            }
        });
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
        title_name.setText("刷卡");
    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        et_cradNum.setText(cardNo);
        if (flag) {
            switch (robotType) {
                case 1:
                    et_cradNum.setText(cardNo);
                    break;
            }
            sendId(cardNo);
        }
    }


    int yourChoice;

    private void showSingleChoiceDialog() {
        final String[] items = card__;
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(ReadCardInfoActivity.this);
        singleChoiceDialog.setTitle("请选择要使用的会员卡");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            Toast.makeText(ReadCardInfoActivity.this,
                                    "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private String card__[] = null;
    private void changeActivity(String json) {
        ReadCardInfo readCard = new Gson().fromJson(json, ReadCardInfo.class);
        if (readCard.getResult_stadus().equals("SUCCESS")) {
            int card_dnum = 0;
            try {
                card_dnum = Integer.parseInt(readCard.getResult_datasNum());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e("uuz", " String转int 异常 ");
            }
            if (card_dnum > 1) {
                String cardNo[] = new String[card_dnum];
                String cardName[] = new String[card_dnum];
                card__ = new String[card_dnum];
                data.addAll(readCard.getResult_datas());
                for (int i = 0;i<card_dnum;i++){
                    cardNo[i] = readCard.getResult_datas().get(i).getC_CardNO();
                    cardName[i] = readCard.getResult_datas().get(i).getC_Name();
                    card__[i] = cardName[i]+"\t\t\t"+cardNo[i];

                }
                showSingleChoiceDialog();
            } else {

            }
            switch (robotType) {
                case 3:
                    et_cradNum.setText(readCard.getResult_data().getC_CardNO());
                    break;
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putParcelable("infoma", readCard.getResult_data());
            intent.putExtras(bundle);
            if (n == 11) {//充值
                Connector.getDatabase();
                List<RechargeAgain> list = DataSupport.findAll(RechargeAgain.class);
                if (isNetworkAvailable(ReadCardInfoActivity.this)) {
                    if (list != null && list.size() > 0) {
                        startActivity(new Intent(ReadCardInfoActivity.this, RechargeSupplementActivity.class));
                    } else {
                        intent.setClass(ReadCardInfoActivity.this, RechargeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Utils.showToast(ReadCardInfoActivity.this, "当前无网络,您有" + list.size() + "单子未上传");
                }
            } else {
                switch (n) {

                    //积分兑换
                    case 3:
                        intent.setClass(ReadCardInfoActivity.this, IntegralExchangeListActivity.class);
                        break;
                    //赠扣积分
                    case 4:
                        intent.setClass(ReadCardInfoActivity.this, GiveDelIntegralActivity.class);
                        break;
                    //卡片延期
                    case 6:
                        intent.setClass(ReadCardInfoActivity.this, CardDelayedActivity.class);
                        break;
                    //购买次数
                    case 12:
                        intent.setClass(ReadCardInfoActivity.this, BuyCountListActivity.class);
                        break;
                    //会员到店
                    case 13:
                        intent.setClass(ReadCardInfoActivity.this, VipToShopActivity.class);
                        break;
                    //挂失
                    case 14:
                        intent.setClass(ReadCardInfoActivity.this, LossOfCardActivity.class);
                        break;
                    //退卡
                    case 15:
                        intent.setClass(ReadCardInfoActivity.this, ExitCardActivity.class);
                        break;
                }
                startActivity(intent);
            }
        } else {
            if (readCard.getResult_lost() != null && readCard.getResult_lost().equals("1")) {//挂失
                if (n == 14) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("infoma", readCard.getResult_data());
                    bundle.putInt("stop", 1);
                    bundle.putString("uid", uid);
                    intent.putExtras(bundle);
                    intent.setClass(ReadCardInfoActivity.this, LossOfCardActivity.class);
                    startActivity(intent);
                } else {
                    openRead();
                    Utils.showToast(ReadCardInfoActivity.this, readCard.getResult_errmsg());
                }
            } else {
                openRead();
                Utils.showToast(ReadCardInfoActivity.this, readCard.getResult_errmsg());
            }
        }
        et_cradNum.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
    }

    @Override
    public void OnReadQrcode(String number) {
        et_cradNum.setText(number);
        sendId(number);
    }
}
