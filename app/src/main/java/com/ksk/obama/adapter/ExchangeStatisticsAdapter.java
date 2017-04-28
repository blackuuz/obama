package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.ExchangeStatistics;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class ExchangeStatisticsAdapter extends BaseAdapter {
    private Context context;
    private List<ExchangeStatistics.ResultDataBean> list;
    private LayoutInflater inflater;

    public ExchangeStatisticsAdapter(Context context, List<ExchangeStatistics.ResultDataBean> list) {
        this.context = context;
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
        ExchangeStatistics.ResultDataBean info = list.get(position);
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
        holder.tv_num.setText(info.getC_CardNO() + "");
        holder.tv_chongzhi.setText(info.getN_GetIntegral() + "");
        holder.tv_shishou.setText(info.getLast_integral() + "");
        return convertView;
    }

    class ViewHolder {
        TextView tv_num, tv_chongzhi, tv_shishou;

    }
}
