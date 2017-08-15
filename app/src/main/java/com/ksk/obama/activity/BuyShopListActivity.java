package com.ksk.obama.activity;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.R;
import com.ksk.obama.adapter.BuyShopListAdapter;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.callback.IQrcodeCallBack;
import com.ksk.obama.callback.IReadCardId;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.model.BuyShopList;
import com.ksk.obama.model.ReadCardInfo;
import com.ksk.obama.utils.MyTextFilter2;
import com.ksk.obama.utils.MyTextFilter3;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Float.parseFloat;

public class BuyShopListActivity extends BuyShopReadActivity implements IReadCardId, IQrcodeCallBack {
    private TextView tv_cardNum;
    private TextView tv_name;
    private TextView tv_oldMoney;
    private TextView tv_type;
    private EditText et_shopname;
    private ListView lv_project;
    private List<BuyShopList.GoodsSBean> list = new ArrayList<>();
    private List<BuyShopList.GoodsClassBean> list_type = new ArrayList<>();
    private BuyShopListAdapter adapter;
    private String memid;
    private boolean isVip = false;
    private LinearLayout ll_title;
    private LinearLayout ll_readCard;
    private Button btn_read;
    private ReadCardInfo.ResultDataBean cardInfo;
    private EditText editText;
    private PopupWindow window0;
    private String preID = "";
    private boolean isReadCard = false;
    private String uid = "";
    private String cardType = "零售价";
    private int ctype = 0;
    private float disprice = 0f;
    private List<ReadCardInfo.ResultDataBean> c_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_shop);
        initTitale();
        initView();
        getHttpData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(BuyShopListActivity.this)) {
            queryDB(true);
        } else {
            int n = queryDB(false);
            Utils.showToast(BuyShopListActivity.this, "当前无网络,您有" + n + "单子未上传");
        }
    }

    private void initTitale() {
        this.setOnReadCardId(this);
        this.setOnReadQrcode(this);
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_print.setText("购物车");
        title_name.setText("产品消费");
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyShopListActivity.this, BuyShopCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isVip", isVip ? "yes" : "no");
                if (isVip) {
                    bundle.putString("uid", uid);
                    bundle.putParcelable("info", cardInfo);
                }
                intent.putExtras(bundle);
                startActivityForResult(intent, 12306);
            }
        });
    }

    private void initView() {
        ll_title = (LinearLayout) findViewById(R.id.ll_buy_shop_info);
        ll_readCard = (LinearLayout) findViewById(R.id.ll_buy_read_card);
        btn_read = (Button) findViewById(R.id.btn_read_card);
        isShowRead(isVip);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadCard)
                    showalert();
            }
        });
        ll_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadCard)
                    showalert();
            }
        });

        tv_cardNum = (TextView) findViewById(R.id.tv_shop_cardnum);
        tv_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_oldMoney = (TextView) findViewById(R.id.tv_shop_money);
        tv_type = (TextView) findViewById(R.id.tv_shop_type);
        et_shopname = (EditText) findViewById(R.id.et_shop_name);
        lv_project = (ListView) findViewById(R.id.lv_buy_list);
        findViewById(R.id.btn_shop_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpData();
            }
        });

        findViewById(R.id.ll_shop_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_type.size() > 1) {
                    AddPopWindow addPopWindow = new AddPopWindow(BuyShopListActivity.this);
                    addPopWindow.showPopupWindow(findViewById(R.id.ll_shop_type));
                }
            }
        });

        lv_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toCalculate(i);
            }
        });

    }

    private int queryDB(boolean flag) {
        Connector.getDatabase();
        List<BuyShopDb> list = DataSupport.findAll(BuyShopDb.class);
        if (list != null && list.size() > 0 && flag) {
            startActivity(new Intent(BuyShopListActivity.this, BuyShopSupplementActivity.class));
        }
        return list.size();
    }

    private void getHttpData() {
        if (list != null) {
            list.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        String shopname = et_shopname.getText().toString();
        String type = tv_type.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(BuyShopListActivity.this, "dbname"));
        map.put("UserId", SharedUtil.getSharedData(BuyShopListActivity.this, "userInfoId"));
        map.put("goodsfind", shopname);
        map.put("goodsclass", type);
        postToHttp(NetworkUrl.BUYSHOPLIST, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                BuyShopList shopList = new Gson().fromJson(jsonText, BuyShopList.class);
                if (shopList.getResult_stadus().equals("SUCCESS")) {
                    list = shopList.getGoods_s();
                    resetList();
                    if (list_type.size() == 0) {
                        list_type = shopList.getGoods_class();
                        if (list_type != null && list_type.size() > 0) {
                            tv_type.setText(list_type.get(0).getClassX());
                        }
                    }

                    adapter = new BuyShopListAdapter(BuyShopListActivity.this, list);
                    lv_project.setAdapter(adapter);
                    isReadCard = true;
                } else {
                    Utils.showToast(BuyShopListActivity.this, shopList.getResult_errmsg());
                }
            }

            @Override
            public void OnFail(String message) {
            }
        });
    }

    private void resetList() {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setNum(0);
                for (int j = 0; j < list_buy.size(); j++) {
                    if (list.get(i).getId().equals(list_buy.get(j).getId())) {
                        list.get(i).setNum(list_buy.get(j).getNum());
                    }
                }
            }
        }
    }

    public void toCalculate(final int i) {

        final PopupWindow window = new PopupWindow();
        View contentView = LayoutInflater.from(BuyShopListActivity.this).inflate(R.layout.number_update_shop1, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.alert_num);
        TextView editd = (TextView) contentView.findViewById(R.id.alert_num_d);
        final TextView tv_discount = (TextView) contentView.findViewById(R.id.vip_money_discount);
        final EditText editz = (EditText) contentView.findViewById(R.id.alert_num_z);
        final EditText edit_discount = (EditText) contentView.findViewById(R.id.alert_num_zh);


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


        final float dj = list.get(i).getN_PriceRetail();
        editd.setText(dj + "");

        switch (ctype) {//物品单价
            case 0:
                disprice = list.get(i).getN_PriceRetail();
                break;
            case 1:
                disprice = list.get(i).getN_PriceMemberA();
                break;
            case 2:
                disprice = list.get(i).getN_PriceMemberB();
                break;
            case 3:
                disprice = list.get(i).getN_PriceMemberC();
                break;
            case 4:
                disprice = list.get(i).getN_PriceMemberD();
                break;
            default:
                disprice = list.get(i).getN_PriceRetail();
                break;
        }

        if (!isVip) {
            tv_discount.setText("无折扣");
        } else {
            if (ctype == 0 && list.get(i).getN_DiscountValue() == 1) {
                tv_discount.setText(Utils.getNumStr(parseFloat(cardInfo.getN_DiscountValue()) * 0.1f) + "折优惠");
            } else if (ctype == 0) {
                tv_discount.setText("商品不打折");
            } else {
                tv_discount.setText(disprice + "");
            }
        }

        editText.setText(list.get(i).getNum() + "");
        editz.setText(Utils.getStringOutE((dj * list.get(i).getNum()) + ""));
        // TODO: 2017/6/5
        float num_ = Float.parseFloat(editText.getText().toString().trim());
        if (isVip) {
            if (list.get(i).getN_DiscountValue() == 1 && ctype == 0) {
                edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num_ *
                        Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01) + ""));
            } else if (ctype == 0) {
                edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num_) + ""));
            } else {
                edit_discount.setText(disprice * num_ + "");
            }
        } else {
            edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num_) + ""));
        }
        //___
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
                    float money = 0f;
                    if (TextUtils.isEmpty(edit_discount.getText().toString())) {
                        money = 0;
                    } else {
                        money = Float.parseFloat(edit_discount.getText().toString());
                    }

                    float num = 0f;
                    if (isVip) {
                        if (list.get(i).getN_DiscountValue() == 1 && ctype == 0) {
                            num = money / (dj * Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01f);
                        } else if (ctype == 0) {
                            num = money / dj;
                        } else {

                            if (disprice == 0.0f) {
                                num = 0;
                                Utils.showToast(BuyShopListActivity.this, "商品价格为\"0\",不能计算数量");
                            } else {
                                num = money / (disprice);
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
                        num = Float.parseFloat(editText.getText().toString());
                    }
                    editz.setText(dj * num + "");
                    if (isVip) {
                        if (list.get(i).getN_DiscountValue() == 1 && ctype == 0) {
                            edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num *
                                    Float.parseFloat(cardInfo.getN_DiscountValue()) * 0.01) + ""));
                        } else if (ctype == 0) {
                            edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num) + ""));
                        } else {
                            edit_discount.setText(disprice * num + "");
                        }
                    } else {
                        edit_discount.setText(Utils.getNumStrE((list.get(i).getN_PriceRetail() * num) + ""));
                    }

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
        window.setOutsideTouchable(false);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAtLocation(findViewById(R.id.activity_buy_count_list), Gravity.CENTER, 0, 0);
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
                    boolean flag = true;
                    float num = Float.parseFloat(editText.getText().toString());
                    if (num >= 0) {
                        BuyCount buyCount = new BuyCount();
                        for (int j = 0; j < list_buy.size(); j++) {
                            if (list_buy.get(j).getId().equals(list.get(i).getId())) {
                                buyCount = list_buy.get(j);
                                flag = false;
                                break;
                            }
                        }
                        //buyCount.setDis_type(tv_discount.getText().toString().trim());
                        // buyCount.setC_type(cardType);
                        buyCount.setId(list.get(i).getId());
                        buyCount.setName(list.get(i).getC_GoodsName());
                        buyCount.setPrice(list.get(i).getN_PriceRetail());
                        buyCount.setDis_price_a(list.get(i).getN_PriceMemberA());
                        buyCount.setDis_price_b(list.get(i).getN_PriceMemberB());
                        buyCount.setDis_price_c(list.get(i).getN_PriceMemberC());
                        buyCount.setDis_price_d(list.get(i).getN_PriceMemberD());
                        buyCount.setMoney(list.get(i).getN_PriceRetail() * num);
                        buyCount.setNum(num);
                        buyCount.setDis(list.get(i).getN_DiscountValue());
                        buyCount.setInteg(list.get(i).getN_IntegralValue());
                        if (isVip) {
                            if (list.get(i).getN_DiscountValue() == 1 && ctype == 0) {
                                buyCount.setMoneyin((int) (list.get(i).getN_PriceRetail() * num *
                                        Float.parseFloat(cardInfo.getN_DiscountValue())) * 0.01f);
                            } else if (ctype == 0) {
                                buyCount.setMoneyin(list.get(i).getN_PriceRetail() * num);
                            } else {
                                buyCount.setMoneyin(disprice * num);
                            }
                            buyCount.setDazhe(list.get(i).getN_DiscountValue() * 0.01f);
                            buyCount.setJifen(list.get(i).getN_IntegralValue() == 1 ? list.get(i).getN_IntegralValue() : 0);
                        } else {
                            buyCount.setMoneyin(list.get(i).getN_PriceRetail() * num);
                        }
                        if (flag) {
                            list_buy.add(buyCount);
                        }
                        list.get(i).setNum(num);
                        removeList();
                        adapter.notifyDataSetChanged();
                    }
                    window.dismiss();
                } else {
                    Utils.showToast(BuyShopListActivity.this, "请填写正确的数量");
                }

            }
        });

    }

    @Override
    public void readCardNo(String cardNo, String UID) {
        uid = UID;
        if (editText != null) {
            editText.setText(cardNo);
            sendCardNum(cardNo);
            window0.dismiss();
        }

    }

    private void isShowRead(boolean flag) {
        ll_title.setVisibility(flag ? View.VISIBLE : View.GONE);
        ll_readCard.setVisibility(flag ? View.GONE : View.VISIBLE);
        btn_read.setEnabled(!flag);
    }

    private void sendCardNum(String cardNum) {
        if (TextUtils.isEmpty(cardNum)) {
            Utils.showToast(BuyShopListActivity.this, "请填写卡号");
        } else {
            if (editText == null) {
                return;
            }
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
                        sendId(finalCardNum);
                    }
                });
                builder.create().show();
            } else {
                sendId(cardNum);
            }
        }
    }

    private void checkCtype(String cardType) {
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

    private void sendId(String cardNum) {
        preID = cardNum;
        Map<String, String> map = new HashMap<>();
        map.put("dbName", SharedUtil.getSharedData(BuyShopListActivity.this, "dbname"));
        map.put("cardNO", cardNum);
        map.put("CardCode", uid);
        map.put("gid", SharedUtil.getSharedData(BuyShopListActivity.this, "groupid"));
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
                    Utils.showToast(BuyShopListActivity.this, readCard.getResult_errmsg());
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
                new AlertDialog.Builder(BuyShopListActivity.this);
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
                            Toast.makeText(BuyShopListActivity.this, "你选择了" + items[0], Toast.LENGTH_SHORT).show();
                            ToActivity(c_data.get(0));
                            c_data.clear();
                        }
                        if (yourChoice != -1) {
                            Toast.makeText(BuyShopListActivity.this, "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();
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


    /**
     * @param resultData <p>读取会员卡数据</p>
     */
    private void ToActivity(ReadCardInfo.ResultDataBean resultData) {
        switch (robotType) {
            case 3:
                editText.setText(resultData.getC_CardNO());
                break;
        }
        cardInfo = resultData;
        isVip = true;
        resetData();
        cardType = cardInfo.getC_PriceClass();
        checkCtype(cardType);
        if (cardType == "零售价") {
            // ctype = 0;
            for (int i = 0; i < list_buy.size(); i++) {
                if (list_buy.get(i).getDis() == 1) {
                    list_buy.get(i).setMoneyin((int) (list_buy.get(i).getPrice() * list_buy.get(i).getNum() *
                            parseFloat(cardInfo.getN_DiscountValue())) * 0.01f);
                } else {
                    list_buy.get(i).setMoneyin(list_buy.get(i).getPrice() * list_buy.get(i).getNum());

                }
                list_buy.get(i).setDazhe(list_buy.get(i).getDis() * 0.01f);
                list_buy.get(i).setJifen(list_buy.get(i).getInteg() == 1 ? list_buy.get(i).getInteg() : 0);
            }
        } else {
            for (int i = 0; i < list_buy.size(); i++) {
                list_buy.get(i).setMoneyin(list_buy.get(i).getPrice() * list_buy.get(i).getNum());
                list_buy.get(i).setDazhe(list_buy.get(i).getDis() * 0.01f);
                list_buy.get(i).setJifen(list_buy.get(i).getInteg() == 1 ? list_buy.get(i).getInteg() : 0);
                switch (cardType) {
                    case "会员价A":
                        ctype = 1;
                        list_buy.get(i).setMoneyin(list_buy.get(i).getDis_price_a() * list_buy.get(i).getNum());
                        break;
                    case "会员价B":
                        ctype = 2;
                        list_buy.get(i).setMoneyin(list_buy.get(i).getDis_price_b() * list_buy.get(i).getNum());
                        break;
                    case "会员价C":
                        ctype = 3;
                        list_buy.get(i).setMoneyin(list_buy.get(i).getDis_price_c() * list_buy.get(i).getNum());
                        break;
                    case "会员价D":
                        ctype = 4;
                        list_buy.get(i).setMoneyin(list_buy.get(i).getDis_price_d() * list_buy.get(i).getNum());
                        break;
                }
            }
        }

    }


    private void resetData() {
        isShowRead(isVip);
        float oldMoney = parseFloat(cardInfo.getN_AmountAvailable());
        memid = cardInfo.getId();
        String cardNum = cardInfo.getC_CardNO();
        tv_cardNum.setText(cardNum);
        tv_name.setText(cardInfo.getC_Name());
        tv_oldMoney.setText("￥" + oldMoney);


    }

    @Override
    public void OnReadQrcode(String number) {
        editText.setText(number);
        sendCardNum(number);
    }

    private class AddPopWindow extends PopupWindow {
        private View conentView;

        public AddPopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popuwindow_card_type, null);
            ListView lv_type = (ListView) conentView.findViewById(R.id.lv_card_type);
            lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tv_type.setText(list_type.get(position).getClassX());
                    dismiss();
                    getHttpData();
                }
            });

            String str[] = new String[list_type.size()];
            for (int i = 0; i < list_type.size(); i++) {
                str[i] = list_type.get(i).getClassX();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BuyShopListActivity.this,
                    android.R.layout.simple_list_item_1, str);
            lv_type.setAdapter(adapter);
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int w = dm.widthPixels;
            this.setContentView(conentView);
            this.setWidth(w / 4 - 23);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            this.update();
            ColorDrawable dw = new ColorDrawable();
            this.setBackgroundDrawable(dw);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String isPay = intent.getStringExtra("isPay");
        if (isPay != null && isPay.equals("yes")) {
            isVip = false;
            isShowRead(false);
            cardInfo = null;
            resetList();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
        list_buy.clear();
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
                    cardInfo = bundle.getParcelable("info");
                    resetData();
                    ctype = bundle.getInt("ctype");
                } else {
                    isVip = false;
                    isShowRead(isVip);
                    cardInfo = null;
                    ctype = 0;
                }
            }
            resetList();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showalert() {
        window0 = new PopupWindow(BuyShopListActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.buy_shop_read_card_0, null);
        editText = (EditText) conentView.findViewById(R.id.et_buy_shop_read_card);
        if (SharedUtil.getSharedData(BuyShopListActivity.this, "isedit").equals("0")) {
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
                isShowRead(false);
                cardInfo = null;
                uid = "";
                ctype = 0;
                for (int i = 0; i < list_buy.size(); i++) {
                    list_buy.get(i).setMoneyin(list_buy.get(i).getMoney());
                }
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
        window0.showAtLocation(findViewById(R.id.activity_buy_count_list), Gravity.CENTER, 0, 0);
        openRead();
    }

    private void removeList() {
        Iterator<BuyCount> bc = list_buy.iterator();
        while (bc.hasNext()) {
            BuyCount buyCount = bc.next();
            if (buyCount.getNum() == 0) {
                bc.remove();
            }
        }
    }
}
