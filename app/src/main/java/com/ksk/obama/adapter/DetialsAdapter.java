package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.PrintPage;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class DetialsAdapter extends BaseAdapter {
    private Context context;
    private List<PrintPage> data;

    public DetialsAdapter(Context context, List<PrintPage> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        PrintPage info = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detials, null);
            holder = new ViewHolder();
            holder.tv_total = (TextView) convertView
                    .findViewById(R.id.tv_statistics3);
            holder.tv_danjia = (TextView) convertView
                    .findViewById(R.id.tv_statistics1);
            holder.tv_pinming = (TextView) convertView
                    .findViewById(R.id.tv_statistics0);
            holder.tv_shuliang = (TextView) convertView
                    .findViewById(R.id.tv_statistics2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_danjia.setText(info.getPrice());
        holder.tv_pinming.setText(info.getName());
        holder.tv_shuliang.setText(info.getNum());
        holder.tv_total.setText(info.getMoney()) ;
        return convertView;
    }
    class ViewHolder {
        TextView tv_total,tv_danjia,tv_pinming,tv_shuliang;

    }
}
