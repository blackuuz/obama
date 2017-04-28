package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.AddPerson;
import com.ksk.obama.model.AddVip;

import java.util.List;

/**
 * Created by djy on 2017/3/17.
 */

public class AddPersonAdapter extends BaseAdapter {

    private List<AddPerson.ResultData1Bean> list_shop;
    private List<AddVip.ResultData1Bean> list_shop_v;
    private LayoutInflater inflater;
    private boolean isVip;

    public AddPersonAdapter(Context context, List<AddPerson.ResultData1Bean> list_shop) {
        isVip = false;
        this.list_shop = list_shop;
        inflater = LayoutInflater.from(context);
    }

    public AddPersonAdapter(List<AddVip.ResultData1Bean> list_shop_v, Context context) {
        isVip = true;
        this.list_shop_v = list_shop_v;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (isVip) {
            return list_shop_v.size();
        } else {
            return list_shop.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (isVip) {
            return list_shop_v.get(position);
        } else {
            return list_shop.get(position);
        }
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
            convertView = inflater.inflate(R.layout.item_add_person, null);
            holder.tv_id = (TextView) convertView.findViewById(R.id.item_id);
            holder.tv_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.tv_shop = (TextView) convertView.findViewById(R.id.item_shop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isVip) {
            holder.tv_id.setText(list_shop_v.get(position).getC_CardNO());
            holder.tv_name.setText(list_shop_v.get(position).getC_Name());
            holder.tv_shop.setText(list_shop_v.get(position).getC_ShopName());
        } else {
            holder.tv_id.setText(list_shop.get(position).getId());
            holder.tv_name.setText(list_shop.get(position).getC_Name());
            holder.tv_shop.setText(list_shop.get(position).getC_ShopName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_id, tv_name, tv_shop;
    }


}
