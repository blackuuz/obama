package com.ksk.obama.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.activity.CheckGoodsActivity;
import com.ksk.obama.model.CheckGoods;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/24 14:37
 * 修改人：Administrator
 * 修改时间：2017/7/24 14:37
 * 修改备注：
 */
public class CheckGoodsAdapter extends BaseAdapter {
    private List<CheckGoods.ResultDataBean.StockSBean> list;
    private CheckGoodsActivity activity;


    public CheckGoodsAdapter(CheckGoodsActivity activity, List<CheckGoods.ResultDataBean.StockSBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_stock_goods, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_check_nam);
            holder.num = (TextView) convertView.findViewById(R.id.tv_check_num);
            holder.changenum = (TextView) convertView.findViewById(R.id.tv_check_change_num);
            holder.ll_check = (LinearLayout) convertView.findViewById(R.id.ll_check_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getC_GoodsName());
        holder.num.setText(list.get(position).getN_Amount());
        holder.changenum.setText(list.get(position).getChangeNum());
        String is0 = holder.changenum.getText().toString().trim();
        Log.e("uuz", is0+"-----"+position);
        if(is0.equals("0.0")||is0.equals("0")){
        }else {
            holder.ll_check.setBackgroundColor(0xffabbfe0);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView name, num, changenum;
        LinearLayout ll_check;
    }
}
