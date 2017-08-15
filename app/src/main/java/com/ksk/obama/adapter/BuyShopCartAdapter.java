package com.ksk.obama.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.activity.BuyShopCartActivity;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class BuyShopCartAdapter extends BaseAdapter {
    private BuyShopCartActivity activity;
    private List<BuyCount> list;
    private int ctype;
    private float disPrice;
    private String type_s;



    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public BuyShopCartAdapter(BuyShopCartActivity activity, List<BuyCount> list, int ctype) {
        this.activity = activity;
        this.list = list;
        this.ctype = ctype;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_shop_cart, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_shop_name);
            holder.price = (TextView) convertView.findViewById(R.id.item_shop_price);
            holder.num = (TextView) convertView.findViewById(R.id.item_shop_num);
            holder.money = (TextView) convertView.findViewById(R.id.item_shop_money);
            holder.del = (ImageView) convertView.findViewById(R.id.item_shop_del);
            holder.type = (TextView) convertView.findViewById(R.id.item_card_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (ctype > 0) {//判断是否显示优惠卡类型
            switch (ctype) {
                case 1:
                    type_s = "会员价A";
                    disPrice = list.get(position).getDis_price_a();
                    break;
                case 2:
                    type_s = "会员价B";
                    disPrice = list.get(position).getDis_price_b();
                    break;
                case 3:
                    type_s = "会员价C";
                    disPrice = list.get(position).getDis_price_c();
                    break;
                case 4:
                    type_s = "会员价D";
                    disPrice = list.get(position).getDis_price_d();
                    break;
            }
            holder.type.setText(type_s + " :");
            holder.type.setTextColor(0xFFFF0000);
            holder.price.setTextColor(0xFFFF0000);
            holder.price.setText(disPrice + "");
        } else {
            holder.type.setText("单价 :");
            holder.type.setTextColor(0xFF000000);
            holder.price.setTextColor(0xFF000000);
            holder.price.setText(list.get(position).getPrice() + "");
        }
        holder.name.setText(list.get(position).getName());
        holder.num.setText(list.get(position).getNum() + "");
        holder.money.setText(Utils.getNumStrE(list.get(position).getMoney() + ""));
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.delProject(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView name, price, num, money, type;
        ImageView del;
    }
}
