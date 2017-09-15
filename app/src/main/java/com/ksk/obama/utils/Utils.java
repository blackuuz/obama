package com.ksk.obama.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class Utils {

    public static Toast toast;
    public static final String isNum = "^[0-9]+(.[0-9]{2})?$";

    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }

        toast.setText(text);
        toast.show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][358]\\d{9}";
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证身份证
     */
    public static boolean isIdCardNumber(String idCatd) {
        String num = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
        if (TextUtils.isEmpty(idCatd)) return false;
        else return idCatd.matches(num);
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static String getNumStr(float num) {
        String str = String.valueOf(getNum2(num));
        String money = "";
        int n = str.length() - str.indexOf(".");
        if (n == 2) {
            money = str + "0";
        } else if (n == 3) {
            money = str;
        }
        return money;
    }

    public static float getNum2(float num) {
        BigDecimal bd = new BigDecimal(num + 0.0001f).setScale(2, BigDecimal.ROUND_HALF_UP);
        num = bd.floatValue();
        return num;
    }

    public static String getNum3(float num) {
        BigDecimal bg = new BigDecimal(num).setScale(3, BigDecimal.ROUND_HALF_UP);
        num = bg.floatValue();
        return num + "";
    }

    public static String getMD5Code(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(
                            Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (Exception e) {
            return "";
        }

    }

    public static String getStringOutE(String str) {
        BigDecimal bd = new BigDecimal(str);
        return bd.toPlainString();
    }

    public static String getNumStrE(String str) {
        str = getStringOutE(str);
        String money = "";
        int n = str.length() - str.indexOf(".");
        if (n > 3) {
            money = str.substring(0, str.indexOf(".") + 3);
        } else {
            if (n == 2) {
                money = str + "0";
            } else if (n == 3) {
                money = str;
            }
        }
        return money;
    }

    /**
     * 商米16进制转换
     * @param hexstr
     * @return
     */
    /**
     * Convert byte[] to hex string.将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断订单时间是否是三天前
     *
     * @param s1 传入的订单时间
     * @return 如果订单时间大于三天 返回true 否则返回false
     */
    public static boolean getIsTime(String s1) {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间

        Log.d("uuz", "getIsTime: " + s1);
        Date currentTime = new Date(System.currentTimeMillis());
        Date orderTime = null;
        boolean isTime = true;
        try {
            orderTime = sdf.parse(s1);

        } catch (ParseException e) {
            isTime = false;
            e.printStackTrace();
        }

        if (!isTime) {
            return false;
         }

        //比较
        if ((currentTime.getTime() - orderTime.getTime()) / (24 * 3600 * 1000) >= 3) {
            Log.d("uuz", "getIsTime: " + s1 + orderTime.getTime());
            return true;
        } else {
            Log.d("uuz", "getIsTime: " + s1 + orderTime.getTime());
            return false;
        }
    }


}
