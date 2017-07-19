package com.ksk.obama.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsWareHousingActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.ll_shop_type)
    LinearLayout llShopType;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.btn_shop_query)
    Button btnShopQuery;
    @BindView(R.id.lv_buy_list)
    ListView lvBuyList;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.activity_goods_in_list)
    LinearLayout activityGoodsInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_ware_housing);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }
    private void initTitle(){
        titleName.setText("商品入库");
        tvCommit.setText("入库详情");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow window = new PopupWindow();
                View contentView = LayoutInflater.from(GoodsWareHousingActivity.this).inflate(R.layout.goods_in_detail, null);
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
                window.showAtLocation(findViewById(R.id.activity_goods_in_list), Gravity.CENTER, 0, 0);
                Utils.showToast(GoodsWareHousingActivity.this,"跳转到详情页面");
            }
        });
    }

    private void initView(){

    }

}
