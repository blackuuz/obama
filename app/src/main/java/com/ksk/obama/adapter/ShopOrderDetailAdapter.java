package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.DetailBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */

public class ShopOrderDetailAdapter extends BaseAdapter {
    private Context context;
    private List<DetailBean> list;

    public ShopOrderDetailAdapter(Context context, List<DetailBean> list) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_order_detail, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.item_tv_order_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.item_tv_order_price);
            holder.tv_num = (TextView) convertView.findViewById(R.id.item_tv_order_num);
            holder.tv_money = (TextView) convertView.findViewById(R.id.item_tv_order_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailBean detailBean = list.get(position);
        holder.tv_name.setText(detailBean.getC_GoodsName()+"");
        holder.tv_price.setText(detailBean.getN_PriceRetail() + "");
        holder.tv_num.setText(detailBean.getN_Number() + "");
        holder.tv_money.setText(detailBean.getN_PayActual() + "");
        
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name, tv_price, tv_num, tv_money;
    }
}
