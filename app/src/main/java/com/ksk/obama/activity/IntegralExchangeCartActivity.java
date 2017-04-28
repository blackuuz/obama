package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.adapter.IntegralExchangeCartAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IPrintErrorCallback;
import com.ksk.obama.callback.IPrintSuccessCallback;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegralExchangeCartActivity extends BasePrintActivity implements IPrintErrorCallback, IPrintSuccessCallback {
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_inregral;
    private TextView tv_old;
    private TextView tv_payNum;
    private GridView gv_project;
    private String memid;
    private float integralCount = 0;
    private IntegralExchangeCartAdapter adapter;

    private String gids;
    private String gname;
    private String nums;
    private String integral;
    private float count;
    private String password;
    private String orderTime = "";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchange_cart);
        setOnPrintError(this);
        setOnPrintSuccess(this);
        initTitale();
        initView();
        initData();
        getOrderNum("DH");
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
        tv_print.setText("结算");
        title_name.setText("购物车");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick_pay) {
                    isclick_pay = false;
                    if (TextUtils.isEmpty(password)) {
                        sendData();
                    } else {
                        getPassword();
                    }

                }
            }
        });
    }

    private void getPassword() {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(IntegralExchangeCartActivity.this).inflate(R.layout.number_password, null);
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
                    sendData();
                    window.dismiss();
                } else {
                    isclick_pay = true;
                    editText.setText("");
                    Utils.showToast(IntegralExchangeCartActivity.this, "密码错误,请重输");
                }
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
        window.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);

    }


    private void initView() {
        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        gv_project = (GridView) findViewById(R.id.gv_shop_list);
        tv_payNum = (TextView) findViewById(R.id.tv_integral_num);
        tv_inregral = (TextView) findViewById(R.id.tv_integral_count);
        tv_old = (TextView) findViewById(R.id.tv_shop_money);
        gv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toCalculate(position);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            memid = bundle.getString("memid");
            integralCount = bundle.getFloat("integral");
            password = bundle.getString("pwd");
            uid = bundle.getString("uid");
            tv_cardNum.setText(bundle.getString("cardNum"));
            tv_name.setText(bundle.getString("name"));
            tv_old.setText(bundle.getString("oldf"));
            adapter = new IntegralExchangeCartAdapter(IntegralExchangeCartActivity.this, list_integral);
            gv_project.setAdapter(adapter);
            upListData();
        }
    }

    private void sendData() {
        if (integralCount - count >= 0) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderTime = simpleFormat.format(date);
            Map<String, String> map = new HashMap<>();
            map.put("dbName", SharedUtil.getSharedData(IntegralExchangeCartActivity.this, "dbname"));
            map.put("goods_id", gids);
            map.put("goods_name", gname);
            map.put("num", nums);
            map.put("integral", integral);
            map.put("orderNo", orderNumber);
            map.put("EquipmentNum", terminalSn);
            map.put("allintegral", count + "");
            map.put("CardCode", uid);
            map.put("c_Billfrom", robotType + "");
            map.put("User_Id", SharedUtil.getSharedData(IntegralExchangeCartActivity.this, "userInfoId"));
            map.put("Member_Id", memid);
            postToHttp(NetworkUrl.BUYINTEGRAL, map, new IHttpCallBack() {
                @Override
                public void OnSucess(String jsonText) {
                    showHttpData(jsonText);
                }

                @Override
                public void OnFail(String message) {
                    isclick_pay = true;
                    Utils.showToast(IntegralExchangeCartActivity.this, message);
                }
            });
        } else {
            isclick_pay = true;
            Utils.showToast(IntegralExchangeCartActivity.this, "您现有积分不够");
        }
    }

    private void showHttpData(String text) {
        try {
            JSONObject object = new JSONObject(text);
            String msg = object.getString("result_stadus");
            if (msg.equals("SUCCESS")) {
                printInfo();
                switch (robotType) {
                    case 3:
                    case 4:
                        changeActivity();
                        break;
                }
            } else {
                isclick_pay = true;
                Utils.showToast(IntegralExchangeCartActivity.this, "提交失败,请重试");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void toCalculate(final int position) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(IntegralExchangeCartActivity.this).inflate(R.layout.number_update, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(list_integral.get(position).getNum() + "");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.selectAll();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth((int) (w * 0.75));
        window.setHeight((int) (h * 0.4));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);
        ImageView iv_add = (ImageView) contentView.findViewById(R.id.iv_click_add);
        final ImageView iv_del = (ImageView) contentView.findViewById(R.id.iv_click_del);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.parseFloat(editText.getText().toString());
                num += 1;
                editText.setText(num + "");
                if (num > 0) {
                    iv_del.setEnabled(true);
                }
            }
        });

        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.parseFloat(editText.getText().toString());
                if (num < 1) {
                    iv_del.setEnabled(false);
                } else {
                    num -= 1;
                    editText.setText(num + "");
                }
            }
        });
        Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.alert_back_0);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                window.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    float num = Float.parseFloat(editText.getText().toString());
                    if (num > 0) {
                        list_integral.get(position).setNum(num);
                        list_integral.get(position).setCount(list_integral.get(position).getInregral() * num);
                    } else {
                        list_integral.remove(position);
                    }
                    upListData();
                    window.dismiss();
                } else {
                    Utils.showToast(IntegralExchangeCartActivity.this, "请填写正确的数量");
                }
            }
        });

    }

    public void delProject(int position) {
        list_integral.remove(position);
        upListData();
    }

    private void upListData() {
        count = 0;
        gids = "";
        gname = "";
        nums = "";
        integral = "";
        float num = 0;
        for (int i = 0; i < list_integral.size(); i++) {
            num += list_integral.get(i).getNum();
            count += list_integral.get(i).getCount();
            gids += list_integral.get(i).getGoodsId() + (i == (list_integral.size() - 1) ? "" : ",");
            gname += list_integral.get(i).getName() + (i == (list_integral.size() - 1) ? "" : ",");
            nums += list_integral.get(i).getNum() + (i == (list_integral.size() - 1) ? "" : ",");
            integral += list_integral.get(i).getCount() + (i == (list_integral.size() - 1) ? "" : ",");
        }
        tv_payNum.setText(num + "");
        tv_inregral.setText(count + "");
        adapter.notifyDataSetChanged();
    }

    private void printInfo() {
        List<PrintPage> list_son = new ArrayList<>();
        for (int i = 0; i < list_integral.size(); i++) {
            PrintPage page = new PrintPage();
            page.setMoney(list_integral.get(i).getCount() + "");
            page.setNum(list_integral.get(i).getNum() + "");
            page.setPrice(list_integral.get(i).getInregral() + "");
            page.setName(list_integral.get(i).getName());
            list_son.add(page);
        }
        List<String> listp = new ArrayList<>();
        listp.add("时间:" + orderTime);
        listp.add("订单号:" + orderNumber);
        listp.add("会员卡号:" + tv_cardNum.getText().toString());
        listp.add("会员姓名:" + tv_name.getText().toString());
        listp.add("son");
        listp.add("支付方式:会员卡");
        listp.add("扣除积分:" + Utils.getNumStr(count));
        listp.add("剩余积分:" + Utils.getNumStr(integralCount - count));

        printPage("积分兑换小票", listp, list_son, true);
    }

    @Override
    public void OnPrintError() {
        printError(R.id.activity_shopping_cart);
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
        changeActivity();
    }

    private void changeActivity() {
        Intent intent = new Intent(IntegralExchangeCartActivity.this, IntegralExchangeHintActivity.class);
        intent.putExtra("time", orderTime);
        intent.putExtra("order", orderNumber);
        intent.putExtra("cardNum", tv_cardNum.getText().toString());
        intent.putExtra("name", tv_name.getText().toString());
        intent.putExtra("del", Utils.getNumStr(count));
        intent.putExtra("integral", Utils.getNumStr(integralCount - count));
        startActivity(intent);
    }
}
