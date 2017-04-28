package com.ksk.obama.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.QuickCount;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */

public class DelCountAdapter extends BaseAdapter {
    private Context context;
    private List<QuickCount.DataBean> list;

    public DelCountAdapter(Context context, List<QuickCount.DataBean> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_del_count, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.item_del_count_name);
            holder.tv_num = (TextView) convertView.findViewById(R.id.item_del_count_num);
            holder.tv_time = (TextView) convertView.findViewById(R.id.item_del_count_time);
            holder.ll_bg = (LinearLayout) convertView.findViewById(R.id.item_del_count_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).isSelect()) {
            holder.ll_bg.setBackgroundResource(R.color.text_blue);
            holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.tv_num.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.tv_time.setTextColor(ContextCompat.getColor(context,R.color.white));
        } else {
            holder.ll_bg.setBackgroundResource(R.color.item_order_bg);
            holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.text_black4));
            holder.tv_num.setTextColor(ContextCompat.getColor(context,R.color.text_black4));
            holder.tv_time.setTextColor(ContextCompat.getColor(context,R.color.text_black4));
        }
        holder.tv_name.setText(list.get(position).getC_GoodsName());
        holder.tv_num.setText(list.get(position).getGoods_times());
        String str = list.get(position).getStopTime().substring(0, 10);
        holder.tv_time.setText(str);
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name, tv_num, tv_time;
        private LinearLayout ll_bg;
    }
}
