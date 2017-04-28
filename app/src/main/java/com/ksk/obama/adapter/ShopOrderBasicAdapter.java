package com.ksk.obama.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.ResultDataBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */

public class ShopOrderBasicAdapter extends BaseAdapter {
    private Context context;
    private List<ResultDataBean> list;

    public ShopOrderBasicAdapter(Context context, List<ResultDataBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_order_q_basic, null);
            holder.ll_bg = (LinearLayout) convertView.findViewById(R.id.item_ll_bg);
            holder.tv_0 = (TextView) convertView.findViewById(R.id.item_tv_order_0);
            holder.tv_1 = (TextView) convertView.findViewById(R.id.item_tv_order_1);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).isSelect()) {
            holder.ll_bg.setBackgroundResource(R.color.text_blue);
            holder.tv_0.setTextColor(Color.WHITE);
            holder.tv_1.setTextColor(Color.WHITE);
        }else{
            holder.tv_0.setTextColor(Color.BLACK);
            holder.tv_1.setTextColor(Color.BLACK);
            holder.ll_bg.setBackgroundResource(R.color.item_order_bg);
        }

        holder.tv_0.setText(list.get(position).getC_BillNO());
        holder.tv_1.setText(list.get(position).getC_Remark());

        return convertView;
    }

    private class ViewHolder{
        LinearLayout ll_bg;
        TextView tv_0,tv_1;
    }
}
