package com.ksk.obama.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.activity.IntegralExchangeCartActivity;
import com.ksk.obama.model.IntegralShopCount;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */

public class IntegralExchangeCartAdapter extends BaseAdapter {
    private IntegralExchangeCartActivity activity;
    private List<IntegralShopCount> list;

    public IntegralExchangeCartAdapter(IntegralExchangeCartActivity context, List<IntegralShopCount> list) {
        this.activity = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getInregral()+"");
        holder.num.setText(list.get(position).getNum()+"");
        holder.money.setText(list.get(position).getCount()+"");
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.delProject(position);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        TextView name,price,num,money;
        ImageView del;
    }
}
