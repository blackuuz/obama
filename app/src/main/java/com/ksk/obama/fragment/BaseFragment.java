package com.ksk.obama.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;

import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.utils.HttpTools;
import com.ksk.obama.utils.MyDialog;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by djy on 2017/2/22.
 */

public class BaseFragment extends Fragment {
    public static final String KEY = "82BA9000F8EF8ABAC93C2569B62AB3C5";
    protected Activity activity;
    protected MyDialog loadingDialog;
    protected int robotType = 4;
    protected String terminalSn;
    private IHttpCallBack callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String str = SharedUtil.getSharedData(activity, "robotType");
        if (str.equals("1")) {
            robotType = 1;
        } else if (str.equals("3")) {
            robotType = 3;
        } else if (str.equals("4")) {
            robotType = 4;
        }
        switch (robotType) {

            case 1:
                terminalSn = SharedUtil.getSharedData(activity, "xlh");
                break;

            case 3:
                terminalSn = SystemProperties.get("ro.serialno");
                SharedUtil.setSharedData(activity, "xlh", terminalSn);
                break;

            case 4:
                terminalSn = ((TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE))
                        .getDeviceId();
                SharedUtil.setSharedData(activity, "xlh", terminalSn);
                break;
        }
        loadingDialog = new MyDialog(activity, R.style.loading_dialog);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    if (callBack != null) {
                        callBack.OnFail(str);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    Utils.showToast(activity, str);
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;

                case 110:
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (callBack != null) {
                        callBack.OnSucess(str);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    break;
            }
        }
    };

    protected void postToHttp(String url, Map<String, String> map, IHttpCallBack IHttpCallBack) {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        int n = (int) (Math.random() * (Integer.MAX_VALUE));
        long time = System.currentTimeMillis() / 1000;
        String sign = Utils.getMD5Code(n + KEY + time).toUpperCase();
        map.put("nonceStr", n + "");
        map.put("sign", sign);
        map.put("timeStamp", time + "");
        map.put("groupId", SharedUtil.getSharedData(activity, "groupid"));
        map.put("equipmentType", robotType + "");
        map.put("equipmentNum", terminalSn);
        callBack = IHttpCallBack;
        HttpTools.postMethod(mHandler, url, map);
    }
}
