package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.BuyShopList;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class BuyShopListAdapter extends BaseAdapter {
    private Context context;
    private List<BuyShopList.GoodsSBean> list;

    public BuyShopListAdapter(Context context, List<BuyShopList.GoodsSBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            holder.tv_num = (TextView) view.findViewById(R.id.item_num);
            holder.tv_name = (TextView) view.findViewById(R.id.item_name);
            holder.tv_price = (TextView) view.findViewById(R.id.item_price);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_num.setText(list.get(i).getNum()+"");
        holder.tv_name.setText(list.get(i).getC_GoodsName());
        holder.tv_price.setText(list.get(i).getN_PriceRetail()+"");

        return view;
    }

    private class ViewHolder{
        private TextView tv_num,tv_name, tv_price;
    }
}
