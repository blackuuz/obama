package com.ksk.obama.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyShopCartAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.MyTextFilter3;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BuyShopCartActivity extends BuyShopReadActivity implements IReadCardId, IQrcodeCallBack {
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_oldMoney;
    private TextView tv_payShould;
    private TextView tv_pay_a;
    private TextView tv_payNum;
    private TextView tv_delMoney;
    private GridView gv_project;
    private String memid;
    private BuyShopCartAdapter adapter;
    private float discount = 1;
    private float integerValue = 0;
    private boolean isVip = false;
    private LinearLayout ll_title;
    private LinearLayout ll_readCard;
    private Button btn_read;
    private PopupWindow window0;
    private EditText editText;
    private ReadCardInfo.ResultDataBean cardInfo;
    private String preID = "";
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_shop_cart);
        initTitale();
        initView();
        initData();
        upListData();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
                finish();
            }
        });
        tv_print.setText("结算");
        title_name.setText("购物车");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAcivity();
            }
        });
    }

    private void initView() {
        this.setOnReadCardId(this);
        this.setOnReadQrcode(this);
        ll_title = (LinearLayout) findViewById(R.id.ll_buy_shop_info);
        ll_readCard = (LinearLayout) findViewById(R.id.ll_buy_read_card);
        btn_read = (Button) findViewById(R.id.btn_read_card);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalert();
            }
        });
        ll_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalert();
            }
        });

        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_oldMoney = (TextView) findViewById(R.id.tv_shop_money);
        gv_project = (GridView) findViewById(R.id.gv_shop_list);
        tv_payShould = (TextView) findViewById(R.id.tv_shop_pay_should);
        tv_pay_a = (TextView) findViewById(R.id.tv_shop_pay_au);
        tv_payNum = (TextView) findViewById(R.id.tv_shop_pay_num);
        tv_delMoney = (TextView) findViewById(R.id.tv_shop_pay_del);

        findViewById(R.id.btn_shop_cart_g).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_buy.size() > 0) {
                    showOrderAlert();
                } else {
                    Utils.showToast(BuyShopCartActivity.this, "无可挂单信息");
                }

            }
        });

        findViewById(R.id.btn_shop_cart_q).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_buy.size() > 0) {
                    showGetOrder();
                } else {
                    Intent intent = new Intent(BuyShopCartActivity.this, BuyShopOrderActivity.class);
                    startActivityForResult(intent, 12306);
                }

            }
        });


        gv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toCalculate(position);
            }
        });
    }

    private void showGetOrder() {
        final PopupWindow dialog = new PopupWindow(BuyShopCartActivity.this);
        View vv = LayoutInflater.from(BuyShopCartActivity.this).inflate(R.layout.order_alert_dialog, null);
        vv.findViewById(R.id.btn_order_alert_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyShopCartActivity.this, BuyShopOrderActivity.class);
                startActivityForResult(intent, 12306);
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
        dialog.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);

    }

    private void isShowRead(boolean flag) {
        ll_title.setVisibility(flag ? View.VISIBLE : View.GONE);
        ll_readCard.setVisibility(flag ? View.GONE : View.VISIBLE);
        btn_read.setEnabled(!flag);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String str = bundle.getString("isVip");
            if (str.equals("yes")) {
                isVip = true;
                isShowRead(isVip);
                cardInfo = bundle.getParcelable("info");
                memid = cardInfo.getId();
                uid=bundle.getString("uid");
                discount = Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
                integerValue = Float.parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
                tv_cardNum.setText(cardInfo.getC_CardNO());
                tv_name.setText(cardInfo.getC_Name());
                tv_oldMoney.setText("￥" + cardInfo.getN_AmountAvailable());
            } else {
                isShowRead(isVip);
            }
        }
        adapter = new BuyShopCartAdapter(BuyShopCartActivity.this, list_buy);
        gv_project.setAdapter(adapter);
    }

    private void changeAcivity() {
        Intent intent = new Intent(BuyShopCartActivity.this, PayBuyShopActivity.class);
        if (isVip) {
            intent.putExtra("name", tv_name.getText().toString());
            intent.putExtra("cardNum", tv_cardNum.getText().toString());
            intent.putExtra("memid", memid);
            intent.putExtra("uid", uid);
            intent.putExtra("integ", integerValue + "");
            intent.putExtra("payau", tv_pay_a.getText().toString().substring(1));
            intent.putExtra("del", tv_delMoney.getText().toString().substring(1));
            intent.putExtra("intecount", cardInfo.getN_IntegralAvailable());
            intent.putExtra("pwd", cardInfo.getC_Password());
            intent.putExtra("old", cardInfo.getN_AmountAvailable());
        }
        intent.putExtra("isVip", isVip ? "yes" : "no");
        intent.putExtra("should", tv_payShould.getText().toString().substring(1));
        startActivity(intent);
    }

    public void toCalculate(final int position) {
        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(BuyShopCartActivity.this).inflate(R.layout.number_update_shop, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        TextView editd = (TextView) contentView.findViewById(R.id.alert_num_d);
        final EditText editz = (EditText) contentView.findViewById(R.id.alert_num_z);
        InputFilter[] filters3 = {new MyTextFilter3()};
        InputFilter[] filters2 = {new MyTextFilter2()};
        editText.setFilters(filters3);
        editz.setFilters(filters2);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setText("");
                }
            }
        });

        editz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editz.setText("");
                }
            }
        });
        final float dj = list_buy.get(position).getPrice();
        editd.setText(dj + "");
        editText.setText(list_buy.get(position).getNum() + "");
        editz.setText(Utils.getStringOutE((dj * list_buy.get(position).getNum()) + ""));
        editz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editz.hasFocus()) {
                    float money = 0;
                    if (TextUtils.isEmpty(editz.getText().toString())) {
                        money = 0;
                    } else {
                        money = Float.parseFloat(editz.getText().toString());
                    }

                    float num = 0;
                    if (isVip) {
                        if (list_buy.get(position).getDazhe() != 0) {
                            num = money / (dj * discount);
                        } else {
                            num = money / dj;
                        }
                    } else {
                        num = money / dj;
                    }
                    editText.setText(Utils.getNum3(num));
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.hasFocus()) {
                    float num = 0;
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        num = 0;
                    } else {
                        num = Float.parseFloat(editText.getText().toString());
                    }

                    if (isVip) {
                        if (list_buy.get(position).getDazhe() != 0) {
                            editz.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num *
                                    discount) + ""));
                        } else {
                            editz.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num) + ""));
                        }
                    } else {
                        editz.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num) + ""));
                    }
                }
            }
        });
        ImageView iv_add = (ImageView) contentView.findViewById(R.id.iv_click_add);
        final ImageView iv_del = (ImageView) contentView.findViewById(R.id.iv_click_del);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    num = 0;
                } else {
                    num = Float.parseFloat(editText.getText().toString());
                }
                num += 1;
                editText.requestFocus();
                editText.setText(num + "");
                if (num > 0) {
                    iv_del.setEnabled(true);
                }
            }
        });

        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float num;
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    num = 0;
                } else {
                    num = Float.parseFloat(editText.getText().toString());
                }
                editText.requestFocus();
                if (num < 1) {
                    iv_del.setEnabled(false);
                } else {
                    num -= 1;
                    editText.setText(num + "");
                }
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(contentView);
        window.setWidth(w * 3 / 4);
        window.setHeight((int) (h * 0.6));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);

        Button btn_sure = (Button) contentView.findViewById(R.id.btn_sure);
        ImageView btn_cancel = (ImageView) contentView.findViewById(R.id.alert_back_0);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

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
                    if (num < 1) {
                        list_buy.remove(position);
                    } else {
                        list_buy.get(position).setNum(num);
                        list_buy.get(position).setMoney(list_buy.get(position).getPrice()
                                * num);
                        if (isVip) {
                            if (list_buy.get(position).getDazhe() != 0) {//1
                                list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                        * num * discount);
                            } else {
                                list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                        * num);
                            }
                        } else {
                            list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                    * num);
                        }
                    }
                    upListData();
                    window.dismiss();
                } else {
                    Utils.showToast(BuyShopCartActivity.this, "请填写正确的数量");
                }
            }
        });

    }

    public void delProject(int position) {
        list_buy.remove(position);
        upListData();
    }

    private void upListData() {
        float num = 0, count = 0, countIn = 0, integralCount = 0;
        for (int i = 0; i < list_buy.size(); i++) {
            num += list_buy.get(i).getNum();
            count += list_buy.get(i).getMoney();
            countIn += list_buy.get(i).getMoneyin();
            integralCount += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * integerValue * shopIntegral;
        }

        tv_payNum.setText(Utils.getNumStr(integralCount));
        tv_payShould.setText("￥" + Utils.getNumStr(Float.parseFloat(Utils.getStringOutE(count + ""))));
        tv_pay_a.setText("￥" + Utils.getNumStr(Float.parseFloat(Utils.getStringOutE(countIn + ""))));
        float ft = count - countIn;
        tv_delMoney.setText("￥" + Utils.getNumStr(ft));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        editText.setText(cardNo);
        sendCardNum(cardNo);
        window0.dismiss();
    }

    private void sendCardNum(String cardNum) {
        if (TextUtils.isEmpty(cardNum)) {
            Utils.showToast(BuyShopCartActivity.this, "请填写卡号");
        } else {
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                cardNum = editText.getText().toString();
            }
            if (cardNum.equals(preID)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("重要提示：");
                builder.setMessage("此次卡号" + cardNum + "与上次卡号" + preID + "重复,是否继续操作?");
                builder.setNegativeButton("取消", null);
                final String finalCardNum = cardNum;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendID(finalCardNum);
                    }
                });
                builder.create().show();
            } else {
                sendID(cardNum);
            }
        }
    }

    private void sendID(String cardNum) {
        preID = cardNum;
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(BuyShopCartActivity.this, "dbname"));
        map.put("cardNO", cardNum);
        map.put("CardCode", uid);
        map.put("gid", SharedUtil.getSharedData(BuyShopCartActivity.this, "groupid"));
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                ReadCardInfo readCard = new Gson().fromJson(jsonText, ReadCardInfo.class);
                if (readCard.getResult_stadus().equals("SUCCESS")) {
                    switch (robotType) {
                        case 3:
                            editText.setText(readCard.getResult_data().getC_CardNO());
                            break;
                    }
                    isVip = true;
                    isShowRead(isVip);
                    cardInfo = readCard.getResult_data();
                    float oldMoney = Float.parseFloat(cardInfo.getN_AmountAvailable());
                    memid = cardInfo.getId();
                    discount = Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
                    integerValue = Float.parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
                    String cardNum = cardInfo.getC_CardNO();
                    tv_cardNum.setText(cardNum);
                    tv_name.setText(cardInfo.getC_Name());
                    tv_oldMoney.setText("￥" + oldMoney);
                    for (int i = 0; i < list_buy.size(); i++) {
                        if (list_buy.get(i).getDis() == 1) {
                            list_buy.get(i).setMoneyin((int) (list_buy.get(i).getPrice() * list_buy.get(i).getNum() *
                                    Float.parseFloat(cardInfo.getN_DiscountValue())) * 0.01f);
                        } else {
                            list_buy.get(i).setMoneyin(list_buy.get(i).getPrice() * list_buy.get(i).getNum());
                        }
                        list_buy.get(i).setDazhe(list_buy.get(i).getDis() * 0.01f);
                        list_buy.get(i).setJifen(list_buy.get(i).getInteg() == 1 ? list_buy.get(i).getInteg() : 0);
                    }
                    upListData();
                } else {
                    Utils.showToast(BuyShopCartActivity.this, readCard.getResult_errmsg());
                }
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void showalert() {
        window0 = new PopupWindow(BuyShopCartActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.buy_shop_read_card_0, null);
        editText = (EditText) conentView.findViewById(R.id.et_buy_shop_read_card);
        if (SharedUtil.getSharedData(BuyShopCartActivity.this, "isedit").equals("0")) {
            editText.setInputType(InputType.TYPE_NULL);
        }
        Button button = (Button) conentView.findViewById(R.id.btn_buy_shop_read_card);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCardNum(editText.getText().toString());
                window0.dismiss();
            }
        });
        conentView.findViewById(R.id.btn_read_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQrcodeActivity();
                window0.dismiss();
            }
        });
        conentView.findViewById(R.id.btn_read_novip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVip = false;
                discount = 1;
                integerValue = 0;
                uid = "";
                isShowRead(false);
                cardInfo = null;
                for (int i = 0; i < list_buy.size(); i++) {
                    list_buy.get(i).setMoneyin(list_buy.get(i).getMoney());
                }
                upListData();
                window0.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        window0.setContentView(conentView);
        window0.setWidth(w * 3 / 4);
        window0.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window0.setFocusable(true);
        window0.setOutsideTouchable(true);
        window0.update();
        ColorDrawable dw = new ColorDrawable();
        window0.setBackgroundDrawable(dw);
        window0.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editText.setText("");
                close();
            }
        });
        window0.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);
        openRead();
    }

    private void showOrderAlert() {
        window0 = new PopupWindow(BuyShopCartActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.buy_shop_order_g, null);
        final EditText editText = (EditText) conentView.findViewById(R.id.et_shop_order_g);
        Button button = (Button) conentView.findViewById(R.id.btn_shop_order_g);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                sendOrderInfo(str);
                window0.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        window0.setContentView(conentView);
        window0.setWidth(w * 3 / 4);
        window0.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window0.setFocusable(true);
        window0.setOutsideTouchable(true);
        window0.update();
        ColorDrawable dw = new ColorDrawable();
        window0.setBackgroundDrawable(dw);
        window0.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editText.setText("");
            }
        });
        window0.showAtLocation(findViewById(R.id.activity_shopping_cart), Gravity.CENTER, 0, 0);
    }

    private void sendOrderInfo(String str) {
        String ids = "";
        String num = "";
        String moneycount = "";
        String integral = "";
        float integralCount = 0f;
        for (int i = 0; i < list_buy.size(); i++) {
            integralCount += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * integerValue * shopIntegral;
            if (i == list_buy.size() - 1) {
                ids += list_buy.get(i).getId();
                num += list_buy.get(i).getNum();
                moneycount += list_buy.get(i).getMoneyin();
                integral += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * (isVip ? integerValue : 0) * shopIntegral;
            } else {
                ids += list_buy.get(i).getId() + ",";
                num += list_buy.get(i).getNum() + ",";
                moneycount += list_buy.get(i).getMoneyin() + ",";
                integral += list_buy.get(i).getMoneyin() * list_buy.get(i).getJifen() * (isVip ? integerValue : 0) * shopIntegral + ",";
            }

        }
        Map<String, String> map = new HashMap<>();
        if (isVip) {
            map.put("get_integral", integralCount + "");
            map.put("Member_Id", memid);
        }
        map.put("integral", integral);
        map.put("dbName", SharedUtil.getSharedData(BuyShopCartActivity.this, "dbname"));
        map.put("goods_id", ids);
        map.put("num", num);
        map.put("money", moneycount);
        map.put("User_Id", SharedUtil.getSharedData(BuyShopCartActivity.this, "userInfoId"));
        map.put("PayShould", tv_payShould.getText().toString().substring(1));
        map.put("PayActual", tv_pay_a.getText().toString().substring(1));
        map.put("orderbymemo", str);
        postToHttp(NetworkUrl.ORDERG, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                showOrderG(jsonText);
            }

            @Override
            public void OnFail(String message) {
                Utils.showToast(BuyShopCartActivity.this, message);
            }
        });
    }

    private void showOrderG(String str) {
        try {
            JSONObject object = new JSONObject(str);
            String msg = object.getString("result_stadus");
            if (msg.equals("FAIL")) {
                String errormsg = object.getString("");
                Utils.showToast(BuyShopCartActivity.this, errormsg);
            } else {
                isVip = false;
                list_buy.clear();
                adapter.notifyDataSetChanged();
                upListData();
                isShowRead(false);
                cardInfo = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clickBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("isVip", isVip ? "yes" : "no");
        if (isVip) {
            bundle.putParcelable("info", cardInfo);
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12306 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString("isVip");
                if (str.equals("yes")) {
                    isVip = true;
                    isShowRead(isVip);
                    cardInfo = bundle.getParcelable("info");
                    memid = cardInfo.getId();
                    discount = Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
                    integerValue = Float.parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
                    tv_cardNum.setText(cardInfo.getC_CardNO());
                    tv_name.setText(cardInfo.getC_Name());
                    tv_oldMoney.setText("￥" + cardInfo.getN_AmountAvailable());
                } else {
                    isVip = false;
                    discount = 1;
                    integerValue = 0;
                    isShowRead(false);
                    cardInfo = null;
                }
                upListData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        clickBack();
        super.onBackPressed();
    }

    @Override
    public void OnReadQrcode(String number) {
        editText.setText(number);
        sendCardNum(number);
    }
}
