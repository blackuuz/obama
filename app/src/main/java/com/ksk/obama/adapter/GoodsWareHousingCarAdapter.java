package com.ksk.obama.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.activity.GoodsWareHousingCarActivity;
import com.ksk.obama.model.GoodsStock;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/21 9:09
 * 修改人：Administrator
 * 修改时间：2017/7/21 9:09
 * 修改备注：
 */
public class GoodsWareHousingCarAdapter extends BaseAdapter {
    private GoodsWareHousingCarActivity activity;
    private List<GoodsStock> list;

    public GoodsWareHousingCarAdapter(GoodsWareHousingCarActivity activity,List<GoodsStock> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
           convertView =  LayoutInflater.from(activity).inflate(R.layout.item_goods_car,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_goods_name);
            holder.goodsNo = (TextView) convertView.findViewById(R.id.item_goods_no);
            holder.price = (TextView) convertView.findViewById(R.id.item_goods_price);
            holder.num = (TextView) convertView.findViewById(R.id.item_goods_num);
            holder.totalMoney = (TextView) convertView.findViewById(R.id.item_goods_money);
            holder.state = (TextView) convertView.findViewById(R.id.item_goods_mode);
            holder.del = (ImageView) convertView.findViewById(R.id.item_shop_del);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder.name.setText(list.get(position).getName());
            holder.goodsNo.setText(list.get(position).getGoodsNO());
            holder.price.setText(list.get(position).getActualPrice()+"");
            holder.num.setText(list.get(position).getNum()+"");
            holder.totalMoney.setText(list.get(position).getMoney()+"");
            holder.state.setText(list.get(position).getGoodsModel());
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   activity.delProject(position);
                }
            });

        return convertView;
    }


    private class  ViewHolder{
        TextView name,goodsNo,price,num,totalMoney,state;
        ImageView del;
    }
}
