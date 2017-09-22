[33mcommit 1267f9233dbe03f852d561ead7fd83f5659c363d[m
Author: yoyoku <826806125@qq.com>
Date:   Wed Sep 20 17:35:52 2017 +0800

    正式版2.3.8  2017-9-20

[1mdiff --git a/app/build.gradle b/app/build.gradle[m
[1mindex 190e65d..ec4a25a 100644[m
[1m--- a/app/build.gradle[m
[1m+++ b/app/build.gradle[m
[36m@@ -8,21 +8,21 @@[m [mandroid {[m
     compileSdkVersion 24[m
     buildToolsVersion "24.0.1"[m
 [m
[31m-//    defaultConfig {//正式版[m
[31m-//        applicationId "com.ksk.obama"[m
[31m-//        minSdkVersion 15[m
[31m-//        targetSdkVersion 25[m
[31m-//        versionCode 363      //商米刷卡 更新sdk   会员卡绑定[m
[31m-//        versionName "2.3.7"//2017-8-15 版本号为 x.y.z  其中y为单数[m
[31m-//    }[m
[31m-    defaultConfig {//测试版[m
[32m+[m[32m    defaultConfig {//正式版[m
         applicationId "com.ksk.obama"[m
         minSdkVersion 15[m
         targetSdkVersion 25[m
[31m-        versionCode 347[m
[31m-        versionName "3.1.7"//time : 2017-9-18 8:59 本地数据库修复[m
[31m-        //versionName "_____"//旺pos[m
[32m+[m[32m        versionCode 365      //商米刷卡 更新sdk   会员卡绑定[m
[32m+[m[32m        versionName "2.3.8"//2017-9-20 版本号为 x.y.z  其中y为单数[m
     }[m
[32m+[m[32m//    defaultConfig {//测试版[m
[32m+[m[32m//        applicationId "com.ksk.obama"[m
[32m+[m[32m//        minSdkVersion 15[m
[32m+[m[32m//        targetSdkVersion 25[m
[32m+[m[32m//        versionCode 347[m
[32m+[m[32m//        versionName "3.1.7"//time : 2017-9-18 8:59 本地数据库修复[m
[32m+[m[32m//        //versionName "_____"//旺pos[m
[32m+[m[32m//    }[m
     buildTypes {[m
         release {[m
             minifyEnabled false[m
[1mdiff --git a/app/src/main/assets/litepal.xml b/app/src/main/assets/litepal.xml[m
[1mindex 190df83..b9c37b6 100644[m
[1m--- a/app/src/main/assets/litepal.xml[m
[1m+++ b/app/src/main/assets/litepal.xml[m
[36m@@ -1,7 +1,7 @@[m
 <?xml version="1.0" encoding="utf-8"?>[m
 <litepal>[m
     <dbname value="yide"></dbname>[m
[31m-    <version value="30"></version>[m
[32m+[m[32m    <version value="31"></version>[m
 [m
     <list>[m
         <mapping class="com.ksk.obama.DB.RechargeAgain"></mapping>[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/DB/OrderNumber.java b/app/src/main/java/com/ksk/obama/DB/OrderNumber.java[m
[1mindex 2ff3284..1aded1d 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/DB/OrderNumber.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/DB/OrderNumber.java[m
[36m@@ -57,6 +57,15 @@[m [mpublic class OrderNumber extends DataSupport {[m
     private boolean isChecked = true;//是否被选中[m
     private  String getMoney;//购买次数里面的属性[m
     private boolean isQuery;//是否被查询[m
[32m+[m[32m    private boolean flag_;//临时变量[m
[32m+[m
[32m+[m[32m    public boolean isFlag_() {[m
[32m+[m[32m        return flag_;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setFlag_(boolean flag_) {[m
[32m+[m[32m        this.flag_ = flag_;[m
[32m+[m[32m    }[m
 [m
     public boolean isQuery() {[m
         return isQuery;[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/activity/BaseActivity.java b/app/src/main/java/com/ksk/obama/activity/BaseActivity.java[m
[1mindex 0f29529..cc9e34f 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/activity/BaseActivity.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/activity/BaseActivity.java[m
[36m@@ -75,6 +75,7 @@[m [mimport cn.weipass.service.bizInvoke.RequestInvoke;[m
 import cn.weipass.service.bizInvoke.RequestResult;[m
 [m
 import static com.ksk.obama.utils.SharedUtil.getSharedBData;[m
[32m+[m[32mimport static java.lang.Float.parseFloat;[m
 [m
 [m
 /**[m
[36m@@ -243,7 +244,7 @@[m [mpublic class BaseActivity extends AutoLayoutActivity {[m
         if (TextUtils.isEmpty(jf)) {[m
             shopIntegral = 0;[m
         } else {[m
[31m-            shopIntegral = Float.parseFloat(jf);[m
[32m+[m[32m            shopIntegral = parseFloat(jf);[m
         }[m
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);[m
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);[m
[36m@@ -403,7 +404,7 @@[m [mpublic class BaseActivity extends AutoLayoutActivity {[m
      */[m
 [m
     private void newPay(int n, String str, String order, String str2) {[m
[31m-        if (!TextUtils.isEmpty(str) && Float.parseFloat(str) > 0) {[m
[32m+[m[32m        if (!TextUtils.isEmpty(str) && parseFloat(str) > 0) {[m
             money = str;[m
             type = n;[m
             orderNo = order;[m
[36m@@ -432,7 +433,12 @@[m [mpublic class BaseActivity extends AutoLayoutActivity {[m
      *                  <p>2017-06-28</p>[m
      */[m
     private void newPay(int n, String money, String orderDesc) {[m
[31m-        if (!TextUtils.isEmpty(money) && Float.parseFloat(money) > 0) {[m
[32m+[m[32m        if (!TextUtils.isEmpty(money) && parseFloat(money) > 0) {[m
[32m+[m[32m            float fmoney = Float.parseFloat(money);[m
[32m+[m[32m            money = fmoney+"";[m
[32m+[m[32m//            if(!(".".indexOf(money)>0)){[m
[32m+[m[32m//                money = money+".0";[m
[32m+[m[32m//            }[m
             Intent intent = new Intent();[m
             ComponentName componet = new ComponentName("com.lkl.cloudpos.payment",[m
                     "com.lkl.cloudpos.payment.activity.MainMenuActivity");[m
[36m@@ -792,7 +798,7 @@[m [mpublic class BaseActivity extends AutoLayoutActivity {[m
      *                  <p> 旺POS 官方支付接口调用</p>[m
      */[m
     private void requestCashier(int n, String money, String orderDesc) {[m
[31m-        if (!TextUtils.isEmpty(money) && Float.parseFloat(money) > 0) {[m
[32m+[m[32m        if (!TextUtils.isEmpty(money) && parseFloat(money) > 0) {[m
             // 1003 微信[m
             // 1004 支付宝[m
             // 1006 银行卡[m
[36m@@ -809,7 +815,7 @@[m [mpublic class BaseActivity extends AutoLayoutActivity {[m
 [m
                 return;[m
             }[m
[31m-            String s[] = (Utils.getNumStr(Float.parseFloat(money) * 100)).split("\\.");[m
[32m+[m[32m            String s[] = (Utils.getNumStr(parseFloat(money) * 100)).split("\\.");[m
             total_fee = s[0];[m
             backClassPath = "com.ksk.obama.activity." + getRunningActivityName();//uuz 动态获取当前运行Activity 的名字[m
             Log.d("8268", "innerRequestCashier: " + backClassPath + "===" + s[0]);[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/activity/OrderNumberActivity.java b/app/src/main/java/com/ksk/obama/activity/OrderNumberActivity.java[m
[1mindex 98c7ac5..e2d9abb 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/activity/OrderNumberActivity.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/activity/OrderNumberActivity.java[m
[36m@@ -423,7 +423,7 @@[m [mpublic class OrderNumberActivity extends BaseSupplementActivity {[m
         map.put("orderNo", orderNumber.getOrderNumber());[m
         map.put("CardCode", orderNumber.getCardCode());[m
         map.put("Supplement", "1");[m
[31m-        // map.put("EquipmentNum", terminalSn);[m
[32m+[m[32m        map.put("EquipmentNum", terminalSn);[m
         map.put("c_Billfrom", robotType + "");[m
         switch (orderNumber.getPayType()) {[m
             case 0:[m
[36m@@ -448,23 +448,37 @@[m [mpublic class OrderNumberActivity extends BaseSupplementActivity {[m
         map.put("payIntegral", orderNumber.getDelMoney());//积分抵现的金额[m
         map.put("payDecIntegral", orderNumber.getPayDecIntegral());//抵现的积分[m
         map.put("userID", orderNumber.getUserId());[m
[31m-        if (orderNumber.isTemporary()) {[m
[31m-            map.put("temporary_num", orderNumber.getTemporary_num());[m
[31m-            map.put("result_name", orderNumber.getResult_name());[m
[31m-        }[m
[31m-        postToHttps(i, NetworkUrl.QUICK_M, map, new IHttpCallBackS() {[m
[31m-            @Override[m
[31m-            public void OnSucess(String jsonText, int n) {[m
[31m-                showData(jsonText, n);[m
[31m-                Log.e("uuz", n + "OnSucess: " + jsonText);[m
[32m+[m[32m        if(orderNumber.isFlag_()){[m
[32m+[m[32m            map.put("CardNum", orderNumber.getCardNum());[m
[32m+[m[32m            if (orderNumber.isTemporary()) {[m
[32m+[m[32m                map.put("temporary_num", orderNumber.getTemporary_num());[m
[32m+[m[32m                map.put("result_name", orderNumber.getResult_name());[m
             }[m
[32m+[m[32m            postToHttps(i, NetworkUrl.QUICK_M, map, new IHttpCallBackS() {[m
[32m+[m[32m                @Override[m
[32m+[m[32m                public void OnSucess(String jsonText, int n) {[m
[32m+[m[32m                    showData(jsonText, n);[m
[32m+[m[32m                    Log.e("uuz", n + "OnSucess: " + jsonText);[m
[32m+[m[32m                }[m
[32m+[m[32m                @Override[m
[32m+[m[32m                public void OnFail(String message) {[m
[32m+[m[32m                    list.get(i).setIsSucceedUploading(-1);[m
[32m+[m[32m                }[m
[32m+[m[32m            });[m
[32m+[m[32m        }else {[m
[32m+[m[32m            postToHttps(i, NetworkUrl.NOVIPQUICK, map, new IHttpCallBackS() {[m
[32m+[m[32m                @Override[m
[32m+[m[32m                public void OnSucess(String jsonText, int n) {[m
[32m+[m[32m                    showData(jsonText, n);[m
[32m+[m[32m                    Log.e("uuz", n + "OnSucess: " + jsonText);[m
[32m+[m[32m                }[m
 [m
[31m-            @Override[m
[31m-            public void OnFail(String message) {[m
[31m-                list.get(i).setIsSucceedUploading(-1);[m
[31m-[m
[31m-            }[m
[31m-        });[m
[32m+[m[32m                @Override[m
[32m+[m[32m                public void OnFail(String message) {[m
[32m+[m[32m                    list.get(i).setIsSucceedUploading(-1);[m
[32m+[m[32m                }[m
[32m+[m[32m            });[m
[32m+[m[32m        }[m
     }[m
 [m
     private void postQuickC(OrderNumber orderNumber, int i) {[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/activity/QuickDelMActivity.java b/app/src/main/java/com/ksk/obama/activity/QuickDelMActivity.java[m
[1mindex f0fc696..3cf7726 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/activity/QuickDelMActivity.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/activity/QuickDelMActivity.java[m
[36m@@ -128,12 +128,12 @@[m [mpublic class QuickDelMActivity extends BasePAndRActivity implements View.OnClick[m
     private String temporaryNum = "";[m
     private boolean isTemporary = false;[m
     private String temName = "";[m
[31m-   // private String gforder = "";//官方订单号[m
[32m+[m[32m    // private String gforder = "";//官方订单号[m
     private boolean isvipcard = true;[m
     //private boolean isQrSure = false;//二维码支付确认[m
     private List<CardInfo.ResultDataBean> c_data = new ArrayList<>();[m
     private List<QuickDelM.FastListBean> q_data = new ArrayList<>();//快速消费快速选择集合[m
[31m-[m
[32m+[m[32m    private boolean flag_;[m
     private Unbinder unbinder;[m
     private boolean XJ, WX, AL, TR;[m
     @BindView(R.id.tv_pay_hy)[m
[36m@@ -866,7 +866,7 @@[m [mpublic class QuickDelMActivity extends BasePAndRActivity implements View.OnClick[m
         map.put("refernumber", order);[m
         map.put("orderNo", ordernb);[m
         map.put("dbName", getSharedData(QuickDelMActivity.this, "dbname"));[m
[31m-       // gforder = order;[m
[32m+[m[32m        // gforder = order;[m
         switch (n) {[m
             case 1:[m
                 map.put("payWeChat", money + "");[m
[36m@@ -1294,20 +1294,20 @@[m [mpublic class QuickDelMActivity extends BasePAndRActivity implements View.OnClick[m
                     map.put("payCard", money + "");[m
                     break;[m
             }[m
[31m-            if(tv_money_dx == null){[m
[32m+[m[32m            if (tv_money_dx == null) {[m
                 dmoney = "0";[m
[31m-            }else {[m
[32m+[m[32m            } else {[m
                 dmoney = tv_money_dx.getText().toString();[m
             }[m
             map.put("payIntegral", dmoney);//积分抵现的金额[m
             map.put("payDecIntegral", delIntegral);//抵现的积分[m
             map.put("userID", getSharedData(QuickDelMActivity.this, "userInfoId"));[m
             if (!TextUtils.isEmpty(et_cardNum.getText().toString()) && isInfo) {[m
[31m-[m
[32m+[m[32m                flag_ = !TextUtils.isEmpty(et_cardNum.getText().toString()) && isInfo;[m
                 if (n == 4 && ((parseFloat(money1) + min_money) < parseFloat(money))) {[m
                     isvipcard = false;[m
[31m-                }else {[m
[31m-                    isvipcard  =true;[m
[32m+[m[32m                } else {[m
[32m+[m[32m                    isvipcard = true;[m
                 }[m
                 if (isvipcard) {[m
                     isVip = true;[m
[36m@@ -1459,12 +1459,13 @@[m [mpublic class QuickDelMActivity extends BasePAndRActivity implements View.OnClick[m
         upLoading.setCardCode(uid);[m
         upLoading.setPayType(n);[m
         upLoading.setMoney(money);[m
[32m+[m[32m        upLoading.setFlag_(flag_);[m
         upLoading.setRefernumber(orderNo);[m
         //upLoading.setGforder(gforder);[m
         upLoading.setDelMoney(dmoney);[m
         upLoading.setPayDecIntegral(delIntegral);[m
         upLoading.setUserId(getSharedData(QuickDelMActivity.this, "userInfoId"));[m
[31m-        upLoading.setVip(isVip);[m
[32m+[m[32m        upLoading.setVip(isInfo);[m
         upLoading.setCardNum(et_cardNum.getText().toString());[m
         upLoading.setTemporary_num(temporaryNum);[m
         upLoading.setResult_name(temName);[m
[36m@@ -1472,6 +1473,7 @@[m [mpublic class QuickDelMActivity extends BasePAndRActivity implements View.OnClick[m
         upLoading.setTime(orderte);[m
         upLoading.setFormClazz("KM");[m
         upLoading.save();[m
[32m+[m[32m        flag_ = false;[m
     }[m
 [m
 [m
[1mdiff --git a/app/src/main/java/com/ksk/obama/activity/VipToShopActivity.java b/app/src/main/java/com/ksk/obama/activity/VipToShopActivity.java[m
[1mindex 26925a1..0cd3386 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/activity/VipToShopActivity.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/activity/VipToShopActivity.java[m
[36m@@ -148,13 +148,12 @@[m [mpublic class VipToShopActivity extends BuyShopReadActivity implements View.OnCli[m
                         //获取当前Activity所在的窗体[m
                         Window dialogWindow = mdialog.getWindow();[m
                         //设置Dialog从窗体底部弹出[m
[31m-                        dialogWindow.setGravity(Gravity.BOTTOM);[m
[32m+[m[32m                        dialogWindow.setGravity(Gravity.CENTER);[m
                         //获得窗体的属性[m
                         WindowManager.LayoutParams lp = dialogWindow.getAttributes();[m
                         DisplayMetrics dm = new DisplayMetrics();[m
                         getWindowManager().getDefaultDisplay().getMetrics(dm);[m
                         lp.width = (int) (dm.widthPixels * 0.95);[m
[31m-                        lp.y = 370; //设置Dialog距离底部的距离[m
                         dialogWindow.setAttributes(lp); //将属性设置给窗体[m
                         mdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {[m
                             @Override[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/adapter/CouponCancelAdapter.java b/app/src/main/java/com/ksk/obama/adapter/CouponCancelAdapter.java[m
[1mindex f590a42..6125a5a 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/adapter/CouponCancelAdapter.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/adapter/CouponCancelAdapter.java[m
[36m@@ -43,11 +43,15 @@[m [mpublic class CouponCancelAdapter extends RecyclerView.Adapter<CouponCancelAdapte[m
     @Override[m
     public void onBindViewHolder(ViewHolder holder, int position) {[m
 [m
[31m-        goodsName = "兑换的商品: " + c_list.get(position).getC_GoodsName();[m
[32m+[m[32m        coupon_type = "优惠券类型: " + c_list.get(position).getI_Type();[m
[32m+[m[32m        if(c_list.get(position).getI_Type().equals("代金券")){[m
[32m+[m[32m            goodsName = "抵扣金额: " + c_list.get(position).getCoupon_money();[m
[32m+[m[32m        }else {[m
[32m+[m[32m            goodsName = "兑换的商品: " + c_list.get(position).getC_GoodsName();[m
[32m+[m[32m        }[m
         vipName = "会员姓名: " + c_list.get(position).getC_MemberName();[m
         vipnum = "会员卡号: " + c_list.get(position).getC_CardNO();[m
         coupon_name = "优惠券名称: " + c_list.get(position).getCoupon_name();[m
[31m-        coupon_type = "优惠券类型: " + c_list.get(position).getI_Type();[m
         if (c_list.get(position).getC_MemberName() == null || c_list.get(position).getC_MemberName().equals("")) {[m
             vipName = "会员姓名: " + "散客";[m
             vipnum = "会员卡号: " + "无";[m
[1mdiff --git a/app/src/main/java/com/ksk/obama/model/CouponCancel.java b/app/src/main/java/com/ksk/obama/model/CouponCancel.java[m
[1mindex c40b710..784c7be 100644[m
[1m--- a/app/src/main/java/com/ksk/obama/model/CouponCancel.java[m
[1m+++ b/app/src/main/java/com/ksk/obama/model/CouponCancel.java[m
[36m@@ -107,6 +107,16 @@[m [mpublic class CouponCancel {[m
         private String c_UseShopType;[m
         private String c_GoodsName;[m
 [m
[32m+[m[32m        public String getCoupon_money() {[m
[32m+[m[32m            return coupon_money;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        public void setCoupon_money(String coupon_money) {[m
[32m+[m[32m            this.coupon_money = coupon_money;[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        private String coupon_money;[m
[32m+[m
         public String getI_Type() {[m
             return i_Type;[m
         }[m
