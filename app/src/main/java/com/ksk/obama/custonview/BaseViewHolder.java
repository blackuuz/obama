package com.ksk.obama.custonview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;

/**
 * Created by djy on 2017/2/21.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public TextView delete;
    public TextView add;
    public LinearLayout layout;

    public BaseViewHolder(View itemView) {
        super(itemView);
        delete = (TextView) itemView.findViewById(R.id.item_delete);
        add = (TextView) itemView.findViewById(R.id.item_add);
        layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
    }
}
