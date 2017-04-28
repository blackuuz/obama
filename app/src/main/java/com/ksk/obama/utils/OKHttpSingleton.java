package com.ksk.obama.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/27.
 */
public class OKHttpSingleton {
    private static OkHttpClient singleton;

    //非常有必要，要不此类还是可以被new，但是无法避免反射，好恶心
    private OKHttpSingleton() {

    }

    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OKHttpSingleton.class) {
                if (singleton == null) {
                    singleton = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(false)
                            .build();
                }
            }
        }
        return singleton;
    }


}
