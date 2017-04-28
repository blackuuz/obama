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
import com.ksk.obama.model.HouseNo;

import java.util.List;

/**
 * Created by djy on 2017/2/22.
 */

public class HouseNoAdapter extends RecyclerView.Adapter<HouseNoAdapter.MyViewHolder> {

    private List<HouseNo.ResultDataBean.RoomBean> list;
    private Context context;
    private LayoutInflater inflater;
    private OnRecyclClickListener clickListener;

    public HouseNoAdapter(Context context, List<HouseNo.ResultDataBean.RoomBean> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickRecyclLIstener(OnRecyclClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_house_no, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HouseNo.ResultDataBean.RoomBean bean = list.get(position);
        if (bean.isSelect()) {
            holder.root.setBackgroundResource(R.color.house_2);
        } else {
            holder.root.setBackgroundResource(R.color.white);
        }
        holder.name.setText(bean.getC_RoomName());
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout root;
        private TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.item_house_no_root);
            name = (TextView) itemView.findViewById(R.id.item_text);
        }
    }

}
