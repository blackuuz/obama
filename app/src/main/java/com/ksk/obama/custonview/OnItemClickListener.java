package com.ksk.obama.custonview;

import android.view.View;

public interface OnItemClickListener {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);

    /**
     * 添加按钮回调
     * @param position
     */
    void OnAddclick(int position);
}
