package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.SalesStatistics;

import java.util.List;

/**
 * Created by djy on 2017/3/22.
 */

public class SalesStatisticsAdapter extends BaseAdapter {
    private List<SalesStatistics.ResultDataBean> list;
    private LayoutInflater inflater;

    public SalesStatisticsAdapter(Context context, List<SalesStatistics.ResultDataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_detials_statistics, null);
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView
                    .findViewById(R.id.tv_statistics0);
            holder.tv_chongzhi = (TextView) convertView
                    .findViewById(R.id.tv_statistics1);
            holder.tv_shishou = (TextView) convertView
                    .findViewById(R.id.tv_statistics2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SalesStatistics.ResultDataBean dataBean = list.get(position);
        holder.tv_num.setText(dataBean.getC_GoodsName());
        holder.tv_chongzhi.setText(dataBean.getAllNum());
        holder.tv_shishou.setText(dataBean.getAllmoney());
        return convertView;
    }

    class ViewHolder {
        TextView tv_num, tv_chongzhi, tv_shishou;
    }
}
