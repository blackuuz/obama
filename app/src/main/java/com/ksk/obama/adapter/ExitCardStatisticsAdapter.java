package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.ExitCardStatistics;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class ExitCardStatisticsAdapter extends BaseAdapter {

    private List<ExitCardStatistics.ResultDataBean> list;
    private LayoutInflater inflater;

    public ExitCardStatisticsAdapter(Context context, List<ExitCardStatistics.ResultDataBean> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
        ExitCardStatistics.ResultDataBean dataBean = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_detials_statistics, null);
            holder = new ViewHolder();
            holder.tv_cardNo = (TextView) convertView
                    .findViewById(R.id.tv_statistics0);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_statistics1);
            holder.tv_money = (TextView) convertView
                    .findViewById(R.id.tv_statistics2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_cardNo.setText(dataBean.getC_CardNO() + "");
        holder.tv_name.setText(dataBean.getC_Name() + "");
        holder.tv_money.setText((dataBean.getN_PayActual()) + "");

        return convertView;
    }

    class ViewHolder {
        TextView tv_cardNo, tv_name, tv_money;
    }
}
