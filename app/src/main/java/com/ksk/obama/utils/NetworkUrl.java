package com.ksk.obama.utils;

/**
 * Created by Administrator on 2016/7/27.
 */
public class NetworkUrl {
    private static final String IP = "http://123.207.146.244/";

    //测试
//    private static final String IP2 = "yideweb";//测试
//    private static final String VERSION = "";
//________________________________________________________________________________________________________________________
    //通用版本正式
    //private static final String IP2 = "yidesoft";
    //private static final String VERSION = ""
// ________________________________________________________________________________________________________________________
    //各版本正式
    private static final String IP2 = "yideApp/yidesoft";//正式
    private static final String VERSION = "236";//版本号 数字部分随版本改变



    private static final String UL = "/index.php/App/";
    private static final String URL = IP + IP2 + VERSION + UL;

    public static final String LOGIN = URL + "Login/login";
    public static final String PRINThEAD = URL + "System/print_setup";
    public static final String RECHARGE = URL + "Business/rechargeHandle";//充值
    public static final String ISCARD = URL + "Business/checkCardNO";//检测卡号是否存在
    public static final String GETCARD = URL + "Business/getCardNO";//内码转换
    public static final String QUICK_M = URL + "Business/fastcostmoney";//快速扣钱
    public static final String QUICK = URL + "Business/fastcost";//快速
    public static final String QUICK_LIST = URL + "Business/fastcosttime";//快速扣次商品列表
    public static final String QUICK_COUNT = URL + "Business/fastcosttimenum";//快速扣次
    public static final String FAST_EXCHANGE = URL + "Exchange/fast_exchange";//快速积分兑换
    public static final String QUERY_MEMBER_CLASS = URL + "Business/query_member_class";//查询可用的会员类型接口
    public static final String ADDMEMBER = URL + "Business/add_member"; //开卡注册完成
    public static final String TICALRECORD = URL + "Record/ticalrecord";//交班记录
    public static final String USERECORD = URL + "Record/userecord";//充值，消费，开户记录
    public static final String DETAILED = URL + "Record/detailed";//充值，消费，开户记录详情
    public static final String BUYCOUNT = URL + "Business/buy_times";//购买次数列表
    public static final String ORDERG = URL + "Business/saverecordmemo";//挂单
    public static final String ORDERGGET = URL + "Business/getrecordmemo";//取单
    public static final String ORDERGDEL = URL + "Business/deleterecordmemo";//删单
    public static final String SENDBUYCOUNT = URL + "Business/save_times";//购买次数列表//
    public static final String INTEGRALLIST = URL + "Business/InitIntegralgift";//积分商品列表
    public static final String BUYINTEGRAL = URL + "Business/save_InitIntegralgift";//积分商品
    public static final String BUYSHOPLIST = URL + "Business/buy_goods";//购买商品列表
    public static final String BUYSHOP = URL + "Business/save_goods";//购买商品
    public static final String ADVICE = URL + "System/advice";//意见反馈
    public static final String SMS = URL + "Index/sms_code";//短信验证码
    public static final String EXITCARD = URL + "Member/cancel_card";//退卡销户
    public static final String STOPCARD = URL + "Member/stop_card";//挂失
    public static final String RESUMECARD = URL + "Member/recovery_card";//恢复
    public static final String CHANGECARD = URL + "Member/card_new";//换卡
    public static final String CHANGEPW = URL + "Member/save_pwd";//改密码
    public static final String ISOPENCARD = URL + "Business/check_oauthcard";
    public static final String CHANGEVIP = URL + "Member/editmember";
    public static final String REMOVEWX = URL + "Member/remove_save";//解绑微信
    public static final String CARDDELAYED = URL + "Member/updatestoptime";//卡片延期
    public static final String NOVIPQUICK = URL + "Business/fastcostmoneynomember";//非会员快速消费
    public static final String YZM = URL + "System/sms";
    public static final String SHIYONG = URL + "System/put_Trial";
    public static final String CHANGEPERSON = URL + "Record/saveticalrecord";
    public static final String PAYCODE = URL + "Payment/pay";//手机微信扫码支付
    public static final String queryOrder = URL + "Payment/queryOrder";//
    public static final String deleteCode = URL + "Payment/deleteCode";
    public static final String PAYQRCODE = URL + "Business/savebillstatus";//拉卡拉微信支付确认订单号
    public static final String DELPAYQRCODE = URL + "Business/deleteSaveBill";//拉卡拉微信支付确认订单号
    public static final String HOUSE = URL + "Room/getRoomList";//台房列表
    public static final String PERSON = URL + "Room/getEmployeeList";//员工列表
    public static final String ADDSHOPPERSON = URL + "Business/employee_search";//添加推荐人
    public static final String ADDVIPPERSON = URL + "Business/recommended_search";//添加会员推荐人
    public static final String SALES = URL + "Record/drawmenu";//销售统计记录
    public static final String SALESDETIALS = URL + "Record/drawmenu_list";//销售统计记录
    public static final String TEMPORARY = URL + "Business/temporary";//临时权限
    public static final String COUPON = URL + "Business/use_coupon";//优惠券
    public static final String COUPONCANCEL = URL + "Business/detail_member_coupon";//优惠券核销查询
    public static final String COUPONCANCELSURE = URL + "Business/verify_coupon";//优惠券核销确认
    public static final String BINDCARD = URL +"Business/bingCardNO";//绑定卡号
    public static final String GOODSSTOCK = URL+"Stock/listStock";//商品入列表
    public static final String CHECKSTOCK = URL+"Business/checkStock";//判断库存是否够
    public static final String STOCKIN = URL+"Stock/stock_save";//商品入库操作
    public static final String GOODSCHECK = URL+"Stock/stocktaking";//商品盘点
    public static final String GOODSCHECKSAVE = URL+"Stock/stock_taking";//商品盘点保存

}
