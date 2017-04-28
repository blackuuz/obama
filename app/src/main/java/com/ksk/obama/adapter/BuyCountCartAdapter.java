package com.ksk.obama.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.activity.BuyCountCartActivity;
import com.ksk.obama.model.BuyCount;
import com.ksk.obama.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class BuyCountCartAdapter extends BaseAdapter {
    private BuyCountCartActivity activity;
    private List<BuyCount> list;

    public BuyCountCartAdapter(BuyCountCartActivity activity, List<BuyCount> list) {
        this.activity = activity;
        this.list = list;
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_count_cart, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_shop_name);
            holder.price = (TextView) convertView.findViewById(R.id.item_shop_price);
            holder.num = (TextView) convertView.findViewById(R.id.item_shop_num);
            holder.money = (TextView) convertView.findViewById(R.id.item_shop_money);
            holder.time = (TextView) convertView.findViewById(R.id.item_shop_time);
            holder.del = (ImageView) convertView.findViewById(R.id.item_shop_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice() + "");
        holder.num.setText(list.get(position).getNum() + "");
        holder.money.setText(Utils.getNumStr(list.get(position).getMoney()) + "");
        holder.time.setText(list.get(position).getValidTime());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.delProject(position);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView name, price, num, money, time;
        ImageView del;
    }
}
