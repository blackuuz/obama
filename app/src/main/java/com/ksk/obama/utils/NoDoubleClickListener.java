package com.ksk.obama.utils;


import android.view.View;

import java.util.Calendar;

/**
 * Created by yoyoku on 2017/5/11. 防止点击过快 双次点击的方法
 */

public abstract class NoDoubleClickListener implements View.OnClickListener{
    public static final int MIN_CLICK_DELAY_TIME = 5000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }//{showToast();}
    }
    public abstract void onNoDoubleClick(View v);
   // public abstract void showToast();
}
