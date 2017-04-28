package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.SalesDetials;

import java.util.List;

/**
 * Created by djy on 2017/3/22.
 */

public class SalesStatisticsDetialsAdapter extends BaseAdapter {
    private List<SalesDetials.ResultDataBean> list;
    private LayoutInflater inflater;

    public SalesStatisticsDetialsAdapter(Context context, List<SalesDetials.ResultDataBean> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_sales_detials, null);
            holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
            holder.tv_money = (TextView) convertView.findViewById(R.id.item_money);
            holder.tv_time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SalesDetials.ResultDataBean dataBean = list.get(position);
        holder.tv_num.setText(dataBean.getN_Number());
        holder.tv_money.setText(dataBean.getAllmoney());
        holder.tv_time.setText(dataBean.getT_Time().substring(0,10));
        return convertView;
    }

    class ViewHolder {
        private TextView tv_num,tv_money,tv_time;
    }
}
