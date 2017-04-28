package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */

public class BuyShopOrder {

    /**
     * result_msg : success
     * result_data : [{"id":"49","i_GroupID":"1","i_ShopID":"1","c_BillStatus":"未结账","c_ShopName":"总店","c_BillNO":"SY161101154840000052","c_BillType":"消费挂单","t_Time":"2016-11-01 15:48:40.000","n_PayCash":".00","n_PayCard":".00","n_PayBank":".00","n_PayIntegral":".00","n_PayTicket":".00","n_PayThird":".00","n_PayOther":".00","n_PayShould":"20.00","n_PayActual":".00","n_GetIntegral":".00","c_CardNO":"","c_Name":"","c_Remark":"321","c_UserName":"管理员1","i_MemberID":"0","i_UserID":"1","ROW_NUMBER":"1","detail":[{"id":"68","i_GroupID":"1","i_ShopID":"1","i_BillID":"49","c_BillStatus":"未结账","c_ShopName":"总店","c_BillNO":"SY161101154840000052","i_GoodsID":"26","c_GoodsNO":"P00026","c_GoodsName":"测试商品2","n_PriceRetail":"20.00","n_Number":"1.00","n_IntegralValueMember":"100.00","n_DiscountValueMember":"100.00","n_IntegralValueGoods":".00","n_DiscountValueGoods":".00","n_PayActual":"20.00","n_GetIntegral":".00","c_CardNO":"","c_Name":"","i_MemberID":"0","ROW_NUMBER":"1"}]},{"id":"51","i_GroupID":"1","i_ShopID":"1","c_BillStatus":"未结账","c_ShopName":"总店","c_BillNO":"SY161101163911210000","c_BillType":"消费收银","t_Time":"2016-11-01 16:39:11.000","n_PayCash":".00","n_PayCard":".00","n_PayBank":".00","n_PayIntegral":".00","n_PayTicket":".00","n_PayThird":".00","n_PayOther":".00","n_PayShould":"6.00","n_PayActual":".00","n_GetIntegral":".00","c_CardNO":"","c_Name":"","c_Remark":"1","c_UserName":"管理员1","i_MemberID":"0","i_UserID":"1","ROW_NUMBER":"2","detail":[{"id":"70","i_GroupID":"1","i_ShopID":"1","i_BillID":"51","c_BillStatus":"未结账","c_ShopName":"总店","c_BillNO":"SY161101163911210000","i_GoodsID":"20","c_GoodsNO":"P00020","c_GoodsName":"辣椒","n_PriceRetail":"3.00","n_Number":"2.00","n_IntegralValueMember":"100.00","n_DiscountValueMember":"100.00","n_IntegralValueGoods":"1.00","n_DiscountValueGoods":"1.00","n_PayActual":"6.00","n_GetIntegral":".00","c_CardNO":"","c_Name":"散客","i_MemberID":"0","ROW_NUMBER":"1"}]}]
     */

    private String result_stadus;
    private String result_errmsg;
    /**
     * id : 49
     * i_GroupID : 1
     * i_ShopID : 1
     * c_BillStatus : 未结账
     * c_ShopName : 总店
     * c_BillNO : SY161101154840000052
     * c_BillType : 消费挂单
     * t_Time : 2016-11-01 15:48:40.000
     * n_PayCash : .00
     * n_PayCard : .00
     * n_PayBank : .00
     * n_PayIntegral : .00
     * n_PayTicket : .00
     * n_PayThird : .00
     * n_PayOther : .00
     * n_PayShould : 20.00
     * n_PayActual : .00
     * n_GetIntegral : .00
     * c_CardNO :
     * c_Name :
     * c_Remark : 321
     * c_UserName : 管理员1
     * i_MemberID : 0
     * i_UserID : 1
     * ROW_NUMBER : 1
     * detail : [{"id":"68","i_GroupID":"1","i_ShopID":"1","i_BillID":"49","c_BillStatus":"未结账","c_ShopName":"总店","c_BillNO":"SY161101154840000052","i_GoodsID":"26","c_GoodsNO":"P00026","c_GoodsName":"测试商品2","n_PriceRetail":"20.00","n_Number":"1.00","n_IntegralValueMember":"100.00","n_DiscountValueMember":"100.00","n_IntegralValueGoods":".00","n_DiscountValueGoods":".00","n_PayActual":"20.00","n_GetIntegral":".00","c_CardNO":"","c_Name":"","i_MemberID":"0","ROW_NUMBER":"1"}]
     */

    private List<ResultDataBean> result_data;

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }

    public List<ResultDataBean> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ResultDataBean> result_data) {
        this.result_data = result_data;
    }

}
