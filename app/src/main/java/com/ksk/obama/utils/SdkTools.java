package com.ksk.obama.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.weipass.pos.sdk.Weipos.OnInitListener;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * 旺pos初始化
 *
 */
public class SdkTools {
	public final static String Tag = "SdkTools";
	public static String deviceType;
	/**
	 * 初始化SDK
	 * 
	 * @param context
	 */
	public static void initSdk(Context context) {
		/**
		 * WeiposImpl的初始化（init函数）和销毁（destroy函数），
		 * 最好分别放在一级页面的onCreate和onDestroy中执行。 其他子页面不用再调用，可以直接获取能力对象并使用。
		 */
		WeiposImpl.as().init(context, new OnInitListener() {

			@Override
			public void onInitOk() {
				Log.e(Tag, "onInitOk--------------");
				String deviceInfo = WeiposImpl.as().getDeviceInfo();
				try {
					JSONObject jsonObj = new JSONObject(deviceInfo);

				deviceType = jsonObj.optString("deviceType");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				final String msg = message;
				Log.e(Tag, "onError--------------" + msg);
			}

			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub
				Log.e(Tag, "onDestroy--------------");
			}
		});
	}
}
