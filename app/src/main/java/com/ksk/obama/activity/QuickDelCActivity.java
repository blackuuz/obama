package com.ksk.obama.activity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.DelCountAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.QuickCount;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ksk.obama.utils.SharedUtil.getSharedData;

public class QuickDelCActivity extends BasePAndRActivity implements IReadCardId, IQrcodeCallBack, IPrintErrorCallback, IPrintSuccessCallback {
    private EditText et_cardNum;
    private EditText et_count;
    private ListView lv_count;
    private TextView tv_project;
    private List<QuickCount.DataBean> list = new ArrayList<>();
    private DelCountAdapter adapter;
    private int prePosition = 0;
    private String projectId = "";
    private int oldCount = 0;
    private int sumCount = 0;
    private int count = 0;
    private String cardNum;
    private String project;
    private String name;
    private String haveMoney;
    private String haveintegral;
    private String password = "";
    private String orderTime = "";
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_del_count);
        this.setOnReadCardId(this);
        this.setOnReadQrcode(this);
        this.setOnPrintError(this);
        this.setOnPrintSuccess(this);
        getOrderNum("KT");
        initTitle();
        setQX();
        initViewC();
    }

    private void initTitle() {
        TextView tv_name = (TextView) findViewById(R.id.title_name);
        tv_name.setText("快速扣次");
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setText("结算");
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    if (TextUtils.isEmpty(password)) {
                        commitdata();
                    } else {
                        getPassword();
                    }
                }
            }
        });
    }

    private void getPassword() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(QuickDelCActivity.this).inflate(R.layout.number_password, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        ImageView back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick_pay = true;
                window.dismiss();
            }
        });
        contentView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = Utils.getMD5Code(editText.getText().toString());
                if (pwd.equals(password)) {
                    commitdata();
                    window.dismiss();
                } else {
                    isclick_pay = true;
                    editText.setText("");
                    Utils.showToast(QuickDelCActivity.this, "密码错误,请重输");
                }
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isclick_pay = true;
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
        window.showAtLocation(findViewById(R.id.ll_quick_count_root), Gravity.CENTER, 0, 0);

    }

    private void setQX() {
        String str = "";
        TextView tv = (TextView) findViewById(R.id.tv_hint);
        if (SharedUtil.getSharedBData(QuickDelCActivity.this, "citiao")) {
            str += "磁条卡,";
        }
        if (SharedUtil.getSharedBData(QuickDelCActivity.this, "nfc")) {
            str += "M1卡,";
        }
        if (SharedUtil.getSharedBData(QuickDelCActivity.this, "saoma")) {
            str += "二维码卡";
        }

        tv.setText("当前可刷卡类型：" + str);
    }

    private void initViewC() {
        et_cardNum = (EditText) findViewById(R.id.et_card_num);
        if (SharedUtil.getSharedData(QuickDelCActivity.this, "isedit").equals("0")) {
            et_cardNum.setInputType(InputType.TYPE_NULL);
        }
        et_count = (EditText) findViewById(R.id.et_count);
        lv_count = (ListView) findViewById(R.id.fm_del_count_lv);
        tv_project = (TextView) findViewById(R.id.tv_project);
        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCountList("");
            }
        });
        findViewById(R.id.btn_read_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQrcodeActivity();
            }
        });
        adapter = new DelCountAdapter(QuickDelCActivity.this, list);
        lv_count.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(prePosition).setSelect(false);
                list.get(position).setSelect(true);
                projectId = list.get(position).getId();
                adapter.notifyDataSetChanged();
                oldCount = Integer.parseInt(list.get(position).getGoods_times());
                sumCount = Integer.parseInt(list.get(position).getSumTimes());
                prePosition = position;
                tv_project.setText(list.get(position).getC_GoodsName());
            }
        });
    }

    private void getCountList(String cardNum) {
        if (!TextUtils.isEmpty(et_cardNum.getText().toString()))
            cardNum = et_cardNum.getText().toString();
        if (TextUtils.isEmpty(cardNum)) {
            isclick_pay = true;
            Utils.showToast(QuickDelCActivity.this, "请输入卡号");
        } else {
            sendIdToDelC(cardNum);
        }
    }

    private void sendIdToDelC(String cardNum) {
        Map<String, String> map = new HashMap<>();
        map.put("userID", getSharedData(QuickDelCActivity.this, "userInfoId"));
        map.put("dbName", getSharedData(QuickDelCActivity.this, "dbname"));
        map.put("cardNO", cardNum);
        map.put("CardCode", uid);
        map.put("gid", SharedUtil.getSharedData(QuickDelCActivity.this, "groupid"));
        postToHttp(NetworkUrl.QUICK_LIST, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                QuickCount readCard = new Gson().fromJson(jsonText, QuickCount.class);
                if (readCard.getResult_stadus().equals("SUCCESS")) {
                    switch (robotType) {
                        case 3:
                        case 4:
                            et_cardNum.setText(readCard.getMemberdata().getC_CardNO());
                            break;
                    }
                    password = readCard.getMemberdata().getC_Password();
                    list = readCard.getData();
                    if (list != null) {
                        if (list.size() > 0) {
                            adapter = new DelCountAdapter(QuickDelCActivity.this, list);
                            lv_count.setAdapter(adapter);
                        } else {
                            openRead();
                            et_cardNum.setText("");
                            Utils.showToast(QuickDelCActivity.this, "您没有可扣项目");
                        }
                    }
                } else {
                    openRead();
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String msg = object.getString("result_errmsg");
                        Utils.showToast(QuickDelCActivity.this, msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void OnFail(String message) {
                openRead();
            }
        });
    }

    public void commitdata() {
        cardNum = et_cardNum.getText().toString();
        project = tv_project.getText().toString();
        if (TextUtils.isEmpty(et_count.getText().toString())) {
            count = 0;
        } else {
            count = Integer.parseInt(et_count.getText().toString());
        }

        if (TextUtils.isEmpty(cardNum)) {
            isclick_pay = true;
            openRead();
            Utils.showToast(QuickDelCActivity.this, "请填写卡号");
        } else if (TextUtils.isEmpty(count + "")) {
            isclick_pay = true;
            openRead();
            Utils.showToast(QuickDelCActivity.this, "请填写扣去次数");
        } else if (TextUtils.isEmpty(projectId)) {
            isclick_pay = true;
            openRead();
            Utils.showToast(QuickDelCActivity.this, "请选择所扣项目");
        } else if (count <= 0) {
            isclick_pay = true;
            openRead();
            Utils.showToast(QuickDelCActivity.this, "所扣次数应大于或者等于1次");
        } else if (oldCount - count < 0) {
            isclick_pay = true;
            openRead();
            Utils.showToast(QuickDelCActivity.this, "所选项目剩余次数不够");
        } else {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderTime = simpleFormat.format(date);
            Map<String, String> map = new HashMap<String, String>();
            map.put("userID", getSharedData(QuickDelCActivity.this, "userInfoId"));
            map.put("dbName", getSharedData(QuickDelCActivity.this, "dbname"));
            map.put("costtime", count + "");
            map.put("CardNum", cardNum);
            map.put("CardCode", uid);
            map.put("c_Billfrom", robotType + "");
            map.put("GoodsId", projectId);
            map.put("orderNo", orderNumber);
            map.put("EquipmentNum", terminalSn);
            postToHttp(NetworkUrl.QUICK_COUNT, map, new IHttpCallBack() {

                @Override
                public void OnSucess(String jsonText) {
                    try {
                        JSONObject object = new JSONObject(jsonText);
                        String tag = object.getString("result_stadus");
                        if (tag.equals("SUCCESS")) {
                            JSONObject data = object.getJSONObject("data");
                            name = data.getString("c_Name");
                            haveMoney = data.getString("n_AmountAvailable");
                            haveintegral = data.getString("n_IntegralAvailable");
                            printInfo();

                        } else if (tag.equals("ERR")) {
                            isclick_pay = true;
                            String msg = object.getString("result_errmsg");
                            Utils.showToast(QuickDelCActivity.this, msg);
                        } else {
                            openRead();
                            String msg = object.getString("result_errmsg");
                            Utils.showToast(QuickDelCActivity.this, msg);
                            isclick_pay = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Utils.showToast(QuickDelMActivity.this,"权限异常");
                    }



                }

                @Override
                public void OnFail(String message) {
                    openRead();
                    isclick_pay = true;
                }
            });
        }
    }

    private void printInfo() {
        List<String> list = new ArrayList<>();
        list.add("订单时间:" + orderTime);
        list.add("单据号:" + orderNumber);
        list.add("会员卡号:" + cardNum);
        list.add("会员姓名:" + name);
        list.add("消费项目   本次扣次   剩余次数");
        list.add(project + "   " + count + "次   " + (sumCount - count) + "次");
        list.add("可用余额:￥" + haveMoney);
        list.add("可用积分:" + haveintegral);

        printPage("快速收银扣次小票", list, null, true);
    }

    protected void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(QuickDelCActivity.this);
        dialog.setTitle("提示:");
        dialog.setMessage("操作成功");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                openRead();
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    public void OnReadQrcode(String number) {
        et_cardNum.setText(number);
        getCountList(number);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.ll_quick_count_root);
    }

    private boolean isclick;

    private void printError(int id) {
        isclick = true;
        final PopupWindow window = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_print_hint, null);
        view.findViewById(R.id.btn_print_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        window.setContentView(view);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        window.setTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        window.setWidth((int) (w * 0.8));
        window.setHeight((int) (w * 0.8));
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isclick) {
                    isclick = false;
                    printInfo();
                }

            }
        });
        window.showAtLocation(findViewById(id), Gravity.CENTER, 0, 0);
    }

    @Override
    public void OnPrintSuccess() {
        uid = "";
        et_cardNum.setText("");
        tv_project.setText("");
        et_count.setText("");
        list.clear();
        adapter.notifyDataSetChanged();
        showAlert();
        getOrderNum("KT");
        isclick_pay = true;
    }


    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        et_cardNum.setText(cardNo);
        getCountList(cardNo);
    }
}
