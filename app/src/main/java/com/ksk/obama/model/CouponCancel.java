package com.ksk.obama.model;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/3 13:24
 * 修改人：Administrator
 * 修改时间：2017/7/3 13:24
 * 修改备注：
 */
public class CouponCancel {
    /**
     * result_stadus : SUCCESS
     * result_data : {"id":"171","i_GroupID":"1","i_CouponID":"44","c_Channel":"按会员手动投放","i_MemberID":"1666","i_BillID":"0","c_BillNO":"","t_UseTime":"2017-07-03 10:14:52.137","c_Code":"01666201707031014520","t_SendTime":"2017-07-03 10:14:52.000","c_Status":"可用","c_MemberName":"琪露诺","i_SendUserID":"152","c_SendUserName":"琪露诺","i_SendShopID":"1","c_SendShopName":"总店","i_UseUserID":"0","c_UseUserName":"","i_UseShopID":"0","c_UseShopName":"","i_WeChatCardID":"0","member_name":"琪露诺","c_CardNO":"9","coupon_name":"草莓兑换券","ROW_NUMBER":"1"}
     * result_errmsg : 该优惠券不存在
     *
     */

    private String result_stadus;
    private ResultDataBean result_data;
    private String result_errmsg;

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }



    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        /**
         * id : 171
         * i_GroupID : 1
         * i_CouponID : 44
         * c_Channel : 按会员手动投放
         * i_MemberID : 1666
         * i_BillID : 0
         * c_BillNO :
         * t_UseTime : 2017-07-03 10:14:52.137
         * c_Code : 01666201707031014520
         * t_SendTime : 2017-07-03 10:14:52.000
         * c_Status : 可用
         * c_MemberName : 琪露诺
         * i_SendUserID : 152
         * c_SendUserName : 琪露诺
         * i_SendShopID : 1
         * c_SendShopName : 总店
         * i_UseUserID : 0
         * c_UseUserName :
         * i_UseShopID : 0
         * c_UseShopName :
         * i_WeChatCardID : 0
         * member_name : 琪露诺
         * c_CardNO : 9
         * coupon_name : 草莓兑换券
         * ROW_NUMBER : 1
         * i_Type :兑换券
         *
         */

        private String id;
        private String i_GroupID;
        private String i_CouponID;
        private String c_Channel;
        private String i_MemberID;
        private String i_BillID;
        private String c_BillNO;
        private String t_UseTime;
        private String c_Code;
        private String t_SendTime;
        private String c_Status;
        private String c_MemberName;
        private String i_SendUserID;
        private String c_SendUserName;
        private String i_SendShopID;
        private String c_SendShopName;
        private String i_UseUserID;
        private String c_UseUserName;
        private String i_UseShopID;
        private String c_UseShopName;
        private String i_WeChatCardID;
        private String member_name;
        private String c_CardNO;
        private String coupon_name;
        private String ROW_NUMBER;
        private String i_Type;
        private String c_UseShopType;
        private String c_GoodsName;

        public String getCoupon_money() {
            return coupon_money;
        }

        public void setCoupon_money(String coupon_money) {
            this.coupon_money = coupon_money;
        }

        private String coupon_money;

        public String getI_Type() {
            return i_Type;
        }

        public void setI_Type(String i_Type) {
            this.i_Type = i_Type;
        }

        public String getC_UseShopType() {
            return c_UseShopType;
        }

        public void setC_UseShopType(String c_UseShopType) {
            this.c_UseShopType = c_UseShopType;
        }

        public String getC_GoodsName() {
            return c_GoodsName;
        }

        public void setC_GoodsName(String c_GoodsName) {
            this.c_GoodsName = c_GoodsName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getI_GroupID() {
            return i_GroupID;
        }

        public void setI_GroupID(String i_GroupID) {
            this.i_GroupID = i_GroupID;
        }

        public String getI_CouponID() {
            return i_CouponID;
        }

        public void setI_CouponID(String i_CouponID) {
            this.i_CouponID = i_CouponID;
        }

        public String getC_Channel() {
            return c_Channel;
        }

        public void setC_Channel(String c_Channel) {
            this.c_Channel = c_Channel;
        }

        public String getI_MemberID() {
            return i_MemberID;
        }

        public void setI_MemberID(String i_MemberID) {
            this.i_MemberID = i_MemberID;
        }

        public String getI_BillID() {
            return i_BillID;
        }

        public void setI_BillID(String i_BillID) {
            this.i_BillID = i_BillID;
        }

        public String getC_BillNO() {
            return c_BillNO;
        }

        public void setC_BillNO(String c_BillNO) {
            this.c_BillNO = c_BillNO;
        }

        public String getT_UseTime() {
            return t_UseTime;
        }

        public void setT_UseTime(String t_UseTime) {
            this.t_UseTime = t_UseTime;
        }

        public String getC_Code() {
            return c_Code;
        }

        public void setC_Code(String c_Code) {
            this.c_Code = c_Code;
        }

        public String getT_SendTime() {
            return t_SendTime;
        }

        public void setT_SendTime(String t_SendTime) {
            this.t_SendTime = t_SendTime;
        }

        public String getC_Status() {
            return c_Status;
        }

        public void setC_Status(String c_Status) {
            this.c_Status = c_Status;
        }

        public String getC_MemberName() {
            return c_MemberName;
        }

        public void setC_MemberName(String c_MemberName) {
            this.c_MemberName = c_MemberName;
        }

        public String getI_SendUserID() {
            return i_SendUserID;
        }

        public void setI_SendUserID(String i_SendUserID) {
            this.i_SendUserID = i_SendUserID;
        }

        public String getC_SendUserName() {
            return c_SendUserName;
        }

        public void setC_SendUserName(String c_SendUserName) {
            this.c_SendUserName = c_SendUserName;
        }

        public String getI_SendShopID() {
            return i_SendShopID;
        }

        public void setI_SendShopID(String i_SendShopID) {
            this.i_SendShopID = i_SendShopID;
        }

        public String getC_SendShopName() {
            return c_SendShopName;
        }

        public void setC_SendShopName(String c_SendShopName) {
            this.c_SendShopName = c_SendShopName;
        }

        public String getI_UseUserID() {
            return i_UseUserID;
        }

        public void setI_UseUserID(String i_UseUserID) {
            this.i_UseUserID = i_UseUserID;
        }

        public String getC_UseUserName() {
            return c_UseUserName;
        }

        public void setC_UseUserName(String c_UseUserName) {
            this.c_UseUserName = c_UseUserName;
        }

        public String getI_UseShopID() {
            return i_UseShopID;
        }

        public void setI_UseShopID(String i_UseShopID) {
            this.i_UseShopID = i_UseShopID;
        }

        public String getC_UseShopName() {
            return c_UseShopName;
        }

        public void setC_UseShopName(String c_UseShopName) {
            this.c_UseShopName = c_UseShopName;
        }

        public String getI_WeChatCardID() {
            return i_WeChatCardID;
        }

        public void setI_WeChatCardID(String i_WeChatCardID) {
            this.i_WeChatCardID = i_WeChatCardID;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getC_CardNO() {
            return c_CardNO;
        }

        public void setC_CardNO(String c_CardNO) {
            this.c_CardNO = c_CardNO;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
