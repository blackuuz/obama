package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;

/**
 * Created by Administrator on 2016/11/9.
 */

public class BuyShopUseCouponAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    public BuyShopUseCouponAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_use_coupon, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_use_coupon_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_use_coupon_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText("");
        holder.tv_money.setText("");
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name, tv_money;
    }
}
