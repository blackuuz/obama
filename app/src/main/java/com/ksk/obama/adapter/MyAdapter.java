package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.BlueTool;

import java.util.List;

/**
 * Created by djy on 2017/3/14.
 */

public class MyAdapter extends BaseAdapter {

    private List<BlueTool> list;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<BlueTool> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_blue_tool, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.mate = (TextView) convertView.findViewById(R.id.mate);
            holder.connect = (TextView) convertView.findViewById(R.id.connect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BlueTool blueTool = list.get(position);
        holder.name.setText(blueTool.getName());
        holder.address.setText(list.get(position).getAddress());

//        if (blueTool.isMate()) {
//            holder.mate.setText("已配对");
//            if (blueTool.isConnect()) {
//                holder.connect.setText("已连接");
//            } else {
//                holder.connect.setText("未连接");
//            }
//        } else {
//            holder.mate.setText("未配对");
//            holder.connect.setText("未连接");
//        }
        return convertView;
    }

    class ViewHolder {
        private TextView name, address, mate, connect;
    }
}
