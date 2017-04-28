package com.ksk.obama.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.custonview.OnRecyclClickListener;
import com.ksk.obama.model.HouseYes;

import java.util.List;

/**
 * Created by djy on 2017/2/22.
 */

public class HouseYesAdapter extends RecyclerView.Adapter<HouseYesAdapter.MyViewHolder> {
    private List<HouseYes.ResultDataBean.RoomBean> list;
    private LayoutInflater inflater;
    private OnRecyclClickListener clickListener;
    private Context context;

    public void setOnClickRecycl(OnRecyclClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public HouseYesAdapter(Context context, List<HouseYes.ResultDataBean.RoomBean> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_house_yes, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HouseYes.ResultDataBean.RoomBean bean = list.get(position);
        if (bean.isSelect()) {
            holder.root.setBackgroundResource(R.color.house_2);
        } else {
            holder.root.setBackgroundResource(R.color.white);
        }
        holder.tv_0.setText(bean.getC_RoomName());
        holder.tv_1.setText(bean.getC_RoomName());
        holder.tv_2.setText(bean.getC_RoomState());
        holder.tv_3.setText(bean.getC_RoomRemark());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnClickRecyclListener(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_0;
        private TextView tv_1;
        private TextView tv_2;
        private TextView tv_3;
        private LinearLayout root;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.item_house_no_root);
            tv_0 = (TextView) itemView.findViewById(R.id.item_text_0);
            tv_1 = (TextView) itemView.findViewById(R.id.item_text_1);
            tv_2 = (TextView) itemView.findViewById(R.id.item_text_2);
            tv_3 = (TextView) itemView.findViewById(R.id.item_text_3);
        }
    }
}
