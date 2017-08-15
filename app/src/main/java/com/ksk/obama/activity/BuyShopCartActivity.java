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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyShopCartAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.model.ShopStockDec;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.MyTextFilter3;
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

import static java.lang.Float.parseFloat;


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
    private String cardType;
    private int ctype = 0;
    private float zdj = 0;
    private List<ReadCardInfo.ResultDataBean> c_data = new ArrayList<>();


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
                if (list_buy.size() > 0) {
                    String ids = "";
                    String num = "";
                    for (int i = 0; i < list_buy.size(); i++) {
                        if (i == list_buy.size() - 1) {
                            ids += list_buy.get(i).getId();
                            num += list_buy.get(i).getNum();
                        } else {
                            ids += list_buy.get(i).getId() + ",";
                            num += list_buy.get(i).getNum() + ",";
                        }
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("shopId",shopId);
                    map.put("dbName", SharedUtil.getSharedData(BuyShopCartActivity.this, "dbname"));
                    map.put("userId",SharedUtil.getSharedData(BuyShopCartActivity.this, "userInfoId"));
                    map.put("goodsId",ids);
                    map.put("nums",num);
                    postToHttp(NetworkUrl.CHECKSTOCK, map, new IHttpCallBack() {
                        @Override
                        public void OnSucess(String jsonText) {
                            ShopStockDec stockDec = new Gson().fromJson(jsonText,ShopStockDec.class);
                            if(stockDec.getResult_stadus().equals("SUCCESS")){
                                changeAcivity();
                            }else {
                                if(stockDec.getResult_stadus().equals("ERR")){
                                    if(stockDec.getResult_data().getIsSetNegativeStock().equals("1")){//库存不足但是可以扣负数  需要弹窗判断
                                        new AlertDialog.Builder(BuyShopCartActivity.this)
                                                .setTitle("提示")
                                                .setMessage(stockDec.getResult_errmsg()+"是否进行负库存操作？")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        changeAcivity();
                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        return;
                                                    }
                                                })
                                                .show();
                                    }else {
                                        Utils.showToast(BuyShopCartActivity.this,stockDec.getResult_errmsg());
                                    }
                                }
                            }
                        }
                        @Override
                        public void OnFail(String message) {
                            Log.d("uuz", "OnFail: ++++");
                        }
                    });

                } else {
                    Utils.showToast(BuyShopCartActivity.this, "您还没有选中任何商品");
                }

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
                uid = bundle.getString("uid");
                discount = parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
                integerValue = parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
                tv_cardNum.setText(cardInfo.getC_CardNO());
                tv_name.setText(cardInfo.getC_Name());
                tv_oldMoney.setText("￥" + cardInfo.getN_AmountAvailable());
                cardType = cardInfo.getC_PriceClass();
                checkCType(cardType);
            } else {
                isShowRead(isVip);
            }
        }
        adapter = new BuyShopCartAdapter(BuyShopCartActivity.this, list_buy, ctype);
        gv_project.setAdapter(adapter);
    }

    //选择会员卡的优惠类型
    private void checkCType(String cardType) {
        switch (cardType) {
            case "零售价":
                ctype = 0;
                break;
            case "会员价A":
                ctype = 1;
                break;
            case "会员价B":
                ctype = 2;
                break;
            case "会员价C":
                ctype = 3;
                break;
            case "会员价D":
                ctype = 4;
                break;
        }

    }

    //计算物品单价
    private void checkDisPrice(int ctype, int position) {
        switch (ctype) {//物品单价
            case 0:
                zdj = list_buy.get(position).getPrice();
                break;
            case 1:
                zdj = list_buy.get(position).getDis_price_a();
                break;
            case 2:
                zdj = list_buy.get(position).getDis_price_b();
                break;
            case 3:
                zdj = list_buy.get(position).getDis_price_c();
                break;
            case 4:
                zdj = list_buy.get(position).getDis_price_d();
                break;
            default:
                zdj = list_buy.get(position).getPrice();
                break;

        }
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
        View contentView = LayoutInflater.from(BuyShopCartActivity.this).inflate(R.layout.number_update_shop1, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        TextView editd = (TextView) contentView.findViewById(R.id.alert_num_d);
        final EditText editz = (EditText) contentView.findViewById(R.id.alert_num_z);
        final EditText edit_discount = (EditText) contentView.findViewById(R.id.alert_num_zh);
        TextView tv_discount = (TextView) contentView.findViewById(R.id.vip_money_discount);

        InputFilter[] filters3 = {new MyTextFilter3()};
        InputFilter[] filters2 = {new MyTextFilter2()};

        editText.setFilters(filters3);
        editz.setFilters(filters2);
        edit_discount.setFilters(filters2);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    editText.setText("");
                }
                return false;
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

        edit_discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edit_discount.setText("");
                }
            }
        });

        edit_discount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    edit_discount.setText("");
                }
                return false;
            }
        });
        checkDisPrice(ctype, position);
        if (!isVip) {
            tv_discount.setText("无折扣");
        } else {
            if (ctype == 0 && list_buy.get(position).getDis() == 1) {
                tv_discount.setText(Utils.getNumStr(parseFloat(cardInfo.getN_DiscountValue()) * 0.1f) + "折优惠");
            } else if (ctype == 0) {
                tv_discount.setText("商品不打折");
            } else {
                tv_discount.setText(zdj + "");
            }
        }
        final float dj = list_buy.get(position).getPrice();
        editd.setText(dj + "");
        editText.setText(list_buy.get(position).getNum() + "");
        editz.setText(Utils.getStringOutE((dj * list_buy.get(position).getNum()) + ""));

        float num_ = Float.parseFloat(editText.getText().toString().trim());
        if (isVip) {
            if (list_buy.get(position).getDis() == 1 && ctype == 0) {
                edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num_ *
                        Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01) + ""));
            } else if (ctype == 0) {
                edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num_) + ""));
            } else {
                checkDisPrice(ctype, position);
                edit_discount.setText(zdj * num_ + "");
            }
        } else {
            edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num_) + ""));
        }

        edit_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_discount.hasFocus()) {
                    float money = 0;
                    if (TextUtils.isEmpty(edit_discount.getText().toString())) {
                        money = 0;
                    } else {
                        money = parseFloat(edit_discount.getText().toString());
                    }

                    float num = 0;
                    if (isVip) {
                        if (list_buy.get(position).getDazhe() != 0 && ctype == 0) {
                            num = money / (dj * discount);
                        } else if (ctype == 0) {
                            num = money / dj;
                        } else {
                            if (zdj == 0.0f) {
                                num = 0;
                                Utils.showToast(BuyShopCartActivity.this, "商品价格为\"0\",不能计算数量");
                            } else {
                                num = money / zdj;
                            }
                        }
                    } else {
                        num = money / dj;
                    }
                    editText.setText(Utils.getNum3(num));
                    editz.setText(Utils.getNum3(num * dj));
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
                        num = parseFloat(editText.getText().toString());
                    }
                    editz.setText(dj * num + "");
                    if (isVip) {
                        if (list_buy.get(position).getDazhe() != 0 && ctype == 0) {
                            edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num *
                                    discount) + ""));
                        } else if (ctype == 0) {
                            edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num) + ""));
                        } else {
                            checkDisPrice(ctype, position);
                            edit_discount.setText(Utils.getNumStrE((zdj * num) + ""));
                        }
                    } else {
                        edit_discount.setText(Utils.getNumStrE((list_buy.get(position).getPrice() * num) + ""));
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
                    num = parseFloat(editText.getText().toString());
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
                    num = parseFloat(editText.getText().toString());
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
                    float num = parseFloat(editText.getText().toString());
                    list_buy.get(position).setNum(num);
                    list_buy.get(position).setMoney(list_buy.get(position).getPrice()
                            * num);
                    if (isVip) {
                        if (list_buy.get(position).getDazhe() != 0 && ctype == 0) {//1
                            list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                    * num * discount);
                        } else if (ctype == 0) {
                            list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                    * num);
                        } else {
                            checkDisPrice(ctype, position);
                            list_buy.get(position).setMoneyin(zdj * num);
                        }
                    } else {
                        list_buy.get(position).setMoneyin(list_buy.get(position).getPrice()
                                * num);
                    }

                    adapter.notifyDataSetChanged();
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
        tv_payShould.setText("￥" + Utils.getNumStr(parseFloat(Utils.getStringOutE(count + ""))));
        tv_pay_a.setText("￥" + Utils.getNumStr(parseFloat(Utils.getStringOutE(countIn + ""))));
        float ft = count - countIn;
        tv_delMoney.setText("￥" + Utils.getNumStr(ft));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        if (editText == null) {
            return;
        }
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
        map.put("shopID", shopId);
        postToHttp(NetworkUrl.ISCARD, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                ReadCardInfo readCard = new Gson().fromJson(jsonText, ReadCardInfo.class);
                if (readCard.getResult_stadus().equals("SUCCESS")) {
                    int card_dnum = 0;
                    try {
                        card_dnum = Integer.parseInt(readCard.getResult_datasNum());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.e("uuz", " String转int 异常 ");
                    }
                    if (card_dnum > 1) {
                        dialog_(readCard);
                    } else {
                        ToActivity(readCard.getResult_data());
                    }
                } else {
                    Utils.showToast(BuyShopCartActivity.this, readCard.getResult_errmsg());
                }
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private int yourChoice;

    /**
     * 这是一个单项选择弹窗
     */
    private void showSingleChoiceDialog() {
        final String[] items = card__;
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(BuyShopCartActivity.this);
        singleChoiceDialog.setTitle("\t\t请选择要使用的会员卡");
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
                        if (yourChoice == -1) {
                            Toast.makeText(BuyShopCartActivity.this, "你选择了" + items[0], Toast.LENGTH_SHORT).show();
                            ToActivity(c_data.get(0));
                            c_data.clear();
                        }
                        if (yourChoice != -1) {
                            Toast.makeText(BuyShopCartActivity.this, "你选择了" + items[yourChoice], Toast.LENGTH_SHORT).show();
                            ToActivity(c_data.get(yourChoice));
                            c_data.clear();
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private String card__[] = null;

    private void dialog_(ReadCardInfo readCard) {
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
            String cName = "";
            String length = "             ";
            card__ = new String[card_dnum];
            c_data = readCard.getResult_datas();
            for (int i = 0; i < card_dnum; i++) {
                cardNo[i] = readCard.getResult_datas().get(i).getC_CardNO();
                cardName[i] = readCard.getResult_datas().get(i).getC_Name();
                if (cardName[i].length() > 4) {
                    cName = cardName[i].substring(0, 4) + "… :";
                } else if (cardName[i].length() == 4) {
                    cName = cardName[i] + "  :";
                } else {
                    cName = cardName[i] + length.substring(0, (4 - cardName[i].length())) + "  :";
                }
                card__[i] = cName + "  " + cardNo[i];
            }
            showSingleChoiceDialog();
        }
    }

    private void ToActivity(ReadCardInfo.ResultDataBean resultData) {
        switch (robotType) {
            case 3:
                editText.setText(resultData.getC_CardNO());
                break;
        }
        isVip = true;
        isShowRead(isVip);
        cardInfo = resultData;
        float oldMoney = parseFloat(cardInfo.getN_AmountAvailable());
        memid = cardInfo.getId();
        discount = parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
        integerValue = parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
        String cardNum = cardInfo.getC_CardNO();
        tv_cardNum.setText(cardNum);
        tv_name.setText(cardInfo.getC_Name());
        tv_oldMoney.setText("￥" + oldMoney);
        cardType = cardInfo.getC_PriceClass();
        checkCType(cardType);
        adapter.setCtype(ctype);
        for (int i = 0; i < list_buy.size(); i++) {
            if (list_buy.get(i).getDis() == 1 && ctype == 0) {
                list_buy.get(i).setMoneyin((int) (list_buy.get(i).getPrice() * list_buy.get(i).getNum() *
                        parseFloat(cardInfo.getN_DiscountValue())) * 0.01f);
            } else if (ctype == 0) {
                list_buy.get(i).setMoneyin(list_buy.get(i).getPrice() * list_buy.get(i).getNum());
            } else {
                checkDisPrice(ctype, i);
                list_buy.get(i).setMoneyin(zdj * list_buy.get(i).getNum());
            }
            list_buy.get(i).setDazhe(list_buy.get(i).getDis() * 0.01f);
            list_buy.get(i).setJifen(list_buy.get(i).getInteg() == 1 ? list_buy.get(i).getInteg() : 0);
        }
        upListData();
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
                adapter.setCtype(0);
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
            bundle.putInt("ctype", ctype);
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
                    cardType = cardInfo.getC_PriceClass();
                    checkCType(cardType);
                    memid = cardInfo.getId();
                    discount = parseFloat(cardInfo.getN_DiscountValue()) * 0.01f;
                    integerValue = parseFloat(cardInfo.getN_IntegralValue()) * 0.01f;
                    tv_cardNum.setText(cardInfo.getC_CardNO());
                    tv_name.setText(cardInfo.getC_Name());
                    tv_oldMoney.setText("￥" + cardInfo.getN_AmountAvailable());
                } else {
                    ctype = 0;
                    isVip = false;
                    discount = 1;
                    integerValue = 0;
                    isShowRead(false);
                    cardInfo = null;
                }
                adapter.setCtype(ctype);
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
