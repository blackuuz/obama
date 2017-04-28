package com.ksk.obama.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.DB.RechargeAgain;
import com.ksk.obama.activity.ChangePersonActivity;
import com.ksk.obama.utils.SharedUtil;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by djy on 2017/1/5.
 */

public class OrderService extends Service {

    private Timer timer;
    private String[] times;

    public class OrderBinder extends Binder {
        public OrderService getService() {
            return OrderService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new OrderBinder();
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String str = dateFormat.format(new Date(System.currentTimeMillis()));
            String time[] = str.split(":");
            int n = Integer.parseInt(time[1]) - Integer.parseInt(times[1]);
            if (isNetworkAvailable(getApplicationContext()) && isHaveOrder() && time[0].equals(times[0]) && n >= 0) {
                Intent intent = new Intent(getApplicationContext(), ChangePersonActivity.class);
                intent.putExtra("supplement", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };

    private boolean isHaveOrder() {
        Connector.getDatabase();
        List<RechargeAgain> list1 = DataSupport.findAll(RechargeAgain.class);
        List<QuickDelMoney> list2 = DataSupport.findAll(QuickDelMoney.class);
        List<OpenCardDb> list3 = DataSupport.findAll(OpenCardDb.class);
        List<BuyCountDb> list4 = DataSupport.findAll(BuyCountDb.class);
        List<BuyShopDb> list5 = DataSupport.findAll(BuyShopDb.class);
        if (list1 != null && list1.size() > 0) {
            return true;
        } else if (list2 != null && list2.size() > 0) {
            return true;
        } else if (list3 != null && list3.size() > 0) {
            return true;
        } else if (list4 != null && list4.size() > 0) {
            return true;
        } else if (list5 != null && list5.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState() + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reSetTime();
        timer = new Timer();
        timer.schedule(timerTask, 60000, 60000);
    }

    public void reSetTime() {
        String str = SharedUtil.getSharedData(getApplicationContext(), "time");
        if (TextUtils.isEmpty(str)) {
            str = "00:00:00";
        }
        times = str.split(":");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
