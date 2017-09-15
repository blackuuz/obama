package com.ksk.obama.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.BuyCount;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/9/1 14:32
 * 修改人：Administrator
 * 修改时间：2017/9/1 14:32
 * 修改备注：
 */
public class supplementDetialsAdapter extends BaseAdapter  {
    private List<BuyCount> list;
    private LayoutInflater inflater;

    public supplementDetialsAdapter(Context context,List<BuyCount> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
        Log.d("uuz", "supplementDetialsAdapter: 构造方法"+list.size());
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
        ViewHolder viewHolder;
        if(convertView == null ){
            convertView = inflater.inflate(R.layout.item_supplment_detials,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_item_num);
            viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_item_money);
          
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            BuyCount buyCount = list.get(position);
            viewHolder.tv_name.setText(buyCount.getName());
            viewHolder.tv_price.setText(buyCount.getPrice()+"");
            viewHolder.tv_num.setText(buyCount.getNum()+"");
            viewHolder.tv_money.setText(buyCount.getMoney()+"");
        Log.d("uuz", "getView: "+list.size());
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_price, tv_num,tv_money;
    }
}
