package com.ksk.obama.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.BuyCountList;

import java.util.List;


/**
 * Created by Administrator on 2016/8/31.
 */
public class BuyCountListAdapter extends BaseAdapter {// implements VipTypeCallBack
    private Context context;
    private int cardtype =  -1 ;
    private List<BuyCountList.GoodsListBean> list;
//    private  VipTypeCallBack vipTypeCallBack;
//    public void setVipTypeCallBack(VipTypeCallBack vipTypeCallBack) {
//        this.vipTypeCallBack = vipTypeCallBack;
//    }


    public BuyCountListAdapter(Context context, List<BuyCountList.GoodsListBean> list,int type) {
        this.context = context;
        this.list = list;
        this.cardtype = type;
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
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_num.setText(list.get(i).getNum() + "");
        holder.tv_name.setText(list.get(i).getC_GoodsName());
        Log.e("uuz", ":"+cardtype);
//        switch (cardtype){
//            case 0 :
                holder.tv_price.setText(list.get(i).getN_PriceRetail() + "");
//                break;
//            case 1:
//                holder.tv_price.setText(list.get(i).getN_PriceMemberA() + "");
//                break;
//            case 2:
//                holder.tv_price.setText(list.get(i).getN_PriceMemberB() + "");
//                break;
//            case 3:
//                holder.tv_price.setText(list.get(i).getN_PriceMemberC()+ "");
//                break;
//            case 4:
//                holder.tv_price.setText(list.get(i).getN_PriceMemberD() + "");
//                break;
//            default:
//                holder.tv_price.setText(list.get(i).getN_PriceRetail() + "");
//                break;
//        }
        return view;
    }

//    @Override
//    public void cardType(int type) {
//        this.cardtype = type;
//    }

    private class ViewHolder {
        private TextView tv_num, tv_name, tv_price;
    }
}
