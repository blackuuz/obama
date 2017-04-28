package com.ksk.obama.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/1.
 */
public class SharedUtil {
    private static final String TAG = "yide";

    public static void setSharedData(Context context, String key, String str) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString(key, str);
        et.commit();
    }

    public static String getSharedData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void setSharedBData(Context context, String key, boolean str) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean(key, str);
        et.commit();
    }

    public static boolean getSharedBData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setSharedInt(Context context, String key, int n) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(key, n);
        et.commit();
    }

    public static int getSharedInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

}
