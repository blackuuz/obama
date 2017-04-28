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
import com.ksk.obama.model.Technician;

import java.util.List;

/**
 * Created by djy on 2017/2/22.
 */

public class TechnicianNoAdapter extends RecyclerView.Adapter<TechnicianNoAdapter.MyViewHolder> {
    private Context context;
    private List<Technician.ResultDataBean.EmployeeBean> list;
    private LayoutInflater inflater;
    private OnRecyclClickListener clickListener;

    public TechnicianNoAdapter(Context context, List<Technician.ResultDataBean.EmployeeBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickRecyclLIstener(OnRecyclClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public TechnicianNoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_house_no, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Technician.ResultDataBean.EmployeeBean bean = list.get(position);
        holder.name.setText(bean.getC_Name());
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
