package com.ksk.obama.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.Coupon;

import java.util.List;

/**
 * Created by uuz on 2017/6/13.
 */

public class CouponSelectAdapter extends RecyclerView.Adapter<CouponSelectAdapter.ViewHolder> {
        private List<Coupon.ResultData> list;
    private ButtonInterface buttonInterface;
    public CouponSelectAdapter(List<Coupon.ResultData> list ) {
        this.list = list;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon_lv, parent, false);
         ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Coupon.ResultData cResult = list.get(position);
        holder.couponNum.setText(cResult.getN_Money());
        holder.btn_getcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface != null){
                    //接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 点击事件的接口
     */
    public interface ButtonInterface{
        public void onclick( View view,int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView couponNum;//优惠金额
        TextView conditional;//使用条件
        Button btn_getcoupon;
        View coupon;

        public ViewHolder(View itemView) {
            super(itemView);
            coupon = itemView;
            btn_getcoupon = (Button) itemView.findViewById(R.id.btn_coupon);
            couponNum = (TextView) itemView.findViewById(R.id.tv_item_coupon);
            conditional = (TextView) itemView.findViewById(R.id.tv_item_conditional);
        }
    }
}
