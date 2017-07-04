package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.QuickDelM;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/4 14:41
 * 修改人：Administrator
 * 修改时间：2017/7/4 14:41
 * 修改备注：
 */
public class QuickDelMAdapter  extends BaseAdapter{
        private Context context;
        private List<QuickDelM.FastListBean> list;

        public QuickDelMAdapter(Context context, List<QuickDelM.FastListBean> list) {
            this.context = context;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (holder == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_quickdelmfast, null);
                holder.rechargeMoney = (TextView) convertView.findViewById(R.id.tv_recharge_name);
                holder.giveMoney = (TextView) convertView.findViewById(R.id.tv_give_name);
                //  holder.im_recharge = (ImageView) convertView.findViewById(R.id.im_recharge);
                holder.im_popclick = (LinearLayout) convertView.findViewById(R.id.im_popclick);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.rechargeMoney.setText(list.get(position).getC_Value() + "");
            if(list.get(position).getC_Value().equals("0")){
                holder.im_popclick.setVisibility(View.GONE);
            }
            //holder.btn_recharge.setBackgroundResource(R.drawable.select_pop);
            return convertView;
        }
        private class ViewHolder {
            private LinearLayout im_popclick;
            private TextView rechargeMoney, giveMoney;
            // private ImageView im_recharge;
        }
}
