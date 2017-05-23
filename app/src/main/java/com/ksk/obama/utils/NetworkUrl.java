package com.ksk.obama.utils;

/**
 * Created by Administrator on 2016/7/27.
 */
public class NetworkUrl {
    private static final String IP = "http://123.207.146.244/";
    //private static final String IP2 = "yideweb";//测试
    private static final String IP2 = "yidesoft";//正式
    private static final String URL_1 = IP + IP2 + "/index.php/";
    private static final String URL = IP + IP2 + "/App/Business/";
    private static final String URL_2 = IP + IP2 + "/App/";
    public static final String LOGIN = URL_1 + "App/Login/login";
    public static final String PRINThEAD = URL_1 + "App/System/print_setup";
    public static final String RECHARGE = URL + "rechargeHandle";//充值
    public static final String ISCARD = URL + "checkCardNO";//检测卡号是否存在
    public static final String GETCARD = URL + "getCardNO";//内码转换
    public static final String QUICK_M = URL + "fastcostmoney";//快速扣钱
    public static final String QUICK = URL + "fastcost";//快速
    public static final String QUICK_LIST = URL + "fastcosttime";//快速扣次商品列表
    public static final String QUICK_COUNT = URL + "fastcosttimenum";//快速扣次
    public static final String FAST_EXCHANGE = URL_1 + "App/Exchange/fast_exchange";//快速积分兑换
    public static final String QUERY_MEMBER_CLASS = URL + "query_member_class";//查询可用的会员类型接口
    public static final String ADDMEMBER = URL + "add_member"; //开卡注册完成
    public static final String TICALRECORD = URL_2 + "Record/ticalrecord";//交班记录
    public static final String USERECORD = URL_2 + "Record/userecord";//充值，消费，开户记录
    public static final String DETAILED = URL_2 + "Record/detailed";//充值，消费，开户记录详情
    public static final String BUYCOUNT = URL_1 + "App/Business/buy_times";//购买次数列表
    public static final String ORDERG = URL_1 + "App/Business/saverecordmemo";//挂单
    public static final String ORDERGGET = URL_1 + "App/Business/getrecordmemo";//取单
    public static final String ORDERGDEL = URL_1 + "App/Business/deleterecordmemo";//删单
    public static final String SENDBUYCOUNT = URL_1 + "App/Business/save_times";//购买次数列表//
    public static final String INTEGRALLIST = URL + "InitIntegralgift";//积分商品列表
    public static final String BUYINTEGRAL = URL + "save_InitIntegralgift";//积分商品
    public static final String BUYSHOPLIST = URL_1 + "App/Business/buy_goods";//购买商品列表
    public static final String BUYSHOP = URL_1 + "App/Business/save_goods";//购买商品
    public static final String ADVICE = URL_2 + "System/advice";//意见反馈
    public static final String SMS = URL_2 + "Index/sms_code";//短信验证码
    public static final String EXITCARD = URL_1 + "App/Member/cancel_card";//退卡销户
    public static final String STOPCARD = URL_1 + "App/Member/stop_card";//挂失
    public static final String RESUMECARD = URL_1 + "App/Member/recovery_card";//恢复
    public static final String CHANGECARD = URL_1 + "App/Member/card_new";//换卡
    public static final String CHANGEPW = URL_1 + "App/Member/save_pwd";//改密码
    public static final String ISOPENCARD = URL_1 + "App/Business/check_oauthcard";
    public static final String CHANGEVIP = URL_1 + "App/Member/editmember";
    public static final String REMOVEWX = URL_2 + "Member/remove_save";//解绑微信
    public static final String CARDDELAYED = URL_2 + "Member/updatestoptime";//卡片延期
    public static final String NOVIPQUICK = URL + "fastcostmoneynomember";//非会员快速消费
    public static final String YZM = URL_1 + "App/System/sms";
    public static final String SHIYONG = URL_1 + "App/System/put_Trial";
    public static final String CHANGEPERSON = URL_1 + "App/Record/saveticalrecord";
    public static final String PAYCODE = URL_2 + "Payment/pay";//手机微信扫码支付
    public static final String queryOrder = URL_2 + "Payment/queryOrder";//
    public static final String deleteCode = URL_2 + "Payment/deleteCode";
    public static final String PAYQRCODE = URL_2 + "Business/savebillstatus";//拉卡拉微信支付确认订单号
    public static final String DELPAYQRCODE = URL_2 + "Business/deleteSaveBill";//拉卡拉微信支付确认订单号
    public static final String HOUSE = URL_2 + "Room/getRoomList";//台房列表
    public static final String PERSON = URL_2 + "Room/getEmployeeList";//员工列表
    public static final String ADDSHOPPERSON = URL_1 + "App/Business/employee_search";//添加推荐人
    public static final String ADDVIPPERSON = URL_1 + "App/Business/recommended_search";//添加会员推荐人
    public static final String SALES = URL_2 + "Record/drawmenu";//销售统计记录
    public static final String SALESDETIALS = URL_2 + "Record/drawmenu_list";//销售统计记录
    public static final String TEMPORARY = URL + "temporary";//临时权限


}
