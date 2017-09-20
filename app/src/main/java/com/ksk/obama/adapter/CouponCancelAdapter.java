package com.ksk.obama.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.CouponCancel;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：适配器
 * 创建人：Create by UUZ
 * 创建时间：2017/7/3 10:47
 * 修改人：Administrator
 * 修改时间：2017/7/3 10:47
 * 修改备注：
 */
public class CouponCancelAdapter extends RecyclerView.Adapter<CouponCancelAdapter.ViewHolder> {
    private List<CouponCancel.ResultDataBean> c_list;
    private String goodsName, vipName, vipnum, coupon_name, coupon_type;

    public void clean() {
        this.c_list.clear();
        notifyDataSetChanged();
    }

    public CouponCancelAdapter(List<CouponCancel.ResultDataBean> c_list) {
        this.c_list = c_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_goods, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        coupon_type = "优惠券类型: " + c_list.get(position).getI_Type();
        if(c_list.get(position).getI_Type().equals("代金券")){
            goodsName = "抵扣金额: " + c_list.get(position).getCoupon_money();
        }else {
            goodsName = "兑换的商品: " + c_list.get(position).getC_GoodsName();
        }
        vipName = "会员姓名: " + c_list.get(position).getC_MemberName();
        vipnum = "会员卡号: " + c_list.get(position).getC_CardNO();
        coupon_name = "优惠券名称: " + c_list.get(position).getCoupon_name();
        if (c_list.get(position).getC_MemberName() == null || c_list.get(position).getC_MemberName().equals("")) {
            vipName = "会员姓名: " + "散客";
            vipnum = "会员卡号: " + "无";
        }
        holder.goodsName.setText(goodsName);
        holder.vipName.setText(vipName);
        holder.vipnum.setText(vipnum);
        holder.coupon_name.setText(coupon_name);
        holder.coupon_type.setText(coupon_type);
    }

    @Override
    public int getItemCount() {
        return c_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView goodsName;
        //TextView state;
        TextView vipName;
        TextView vipnum;
        TextView coupon_name;
        TextView coupon_type;
        // TextView btn_goodsName;


        public ViewHolder(View itemView) {
            super(itemView);
            // btn_goodsName = (TextView) itemView.findViewById(R.id.btn_coupon);  //兑换商品名(大)
            goodsName = (TextView) itemView.findViewById(R.id.item_coupon_goods);  //兑换商品名
            //state = (TextView) itemView.findViewById(R.id.tv_item_conditional);//优惠券状态
            vipName = (TextView) itemView.findViewById(R.id.item_coupon_vipname);//会员名
            vipnum = (TextView) itemView.findViewById(R.id.item_coupon_vipnum);//会员号
            coupon_name = (TextView) itemView.findViewById(R.id.item_coupon_name);//优惠券名
            coupon_type = (TextView) itemView.findViewById(R.id.item_coupon_type);//优惠券类型
        }
    }
}
