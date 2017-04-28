package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class BuyCountStatisticsDetials {


    /**
     * pay_way : 现金支付
     * result_msg : success
     * c_CardNO : 5607
     * c_Name : 5607
     * t_Time : 2016-10-14 16:42:09.000
     * c_BillNO : BT16101416420921026
     * n_PayActual : 1880.00
     * c_UserName : 管理员
     * c_ShopName : 总店
     * n_AmountAvailable : 951446.23
     * n_IntegralAvailable : 210062.42
     * n_GetIntegral : .00
     * c_EquipmentNum : YP610000021026
     * times_Detail : [{"id":"758","i_GroupID":"1","i_ShopID":"1","i_BillID":"0","c_BillStatus":"已结账","c_ShopName":"总店","c_BillNO":"BT16101416420921026","i_GoodsID":"22","c_GoodsNO":null,"c_GoodsName":null,"n_PriceRetail":null,"n_Number":"10.00","n_IntegralValueMember":"100.00","n_DiscountValueMember":"88.00","n_IntegralValueGoods":"100.00","n_DiscountValueGoods":"100.00","n_PayActual":"1880.00","n_GetIntegral":".00","c_CardNO":"5607","c_Name":"5607","i_MemberID":"70","ROW_NUMBER":"1"}]
     */

    private String pay_way;
    private String result_msg;
    private String c_CardNO;
    private String c_Name;
    private String t_Time;
    private String c_BillNO;
    private String n_PayActual;
    private String c_UserName;
    private String c_ShopName;
    private String n_AmountAvailable;
    private String n_IntegralAvailable;
    private String n_GetIntegral;
    private String c_EquipmentNum;
    /**
     * id : 758
     * i_GroupID : 1
     * i_ShopID : 1
     * i_BillID : 0
     * c_BillStatus : 已结账
     * c_ShopName : 总店
     * c_BillNO : BT16101416420921026
     * i_GoodsID : 22
     * c_GoodsNO : null
     * c_GoodsName : null
     * n_PriceRetail : null
     * n_Number : 10.00
     * n_IntegralValueMember : 100.00
     * n_DiscountValueMember : 88.00
     * n_IntegralValueGoods : 100.00
     * n_DiscountValueGoods : 100.00
     * n_PayActual : 1880.00
     * n_GetIntegral : .00
     * c_CardNO : 5607
     * c_Name : 5607
     * i_MemberID : 70
     * ROW_NUMBER : 1
     */

    private List<TimesDetailBean> times_Detail;

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getC_CardNO() {
        return c_CardNO;
    }

    public void setC_CardNO(String c_CardNO) {
        this.c_CardNO = c_CardNO;
    }

    public String getC_Name() {
        return c_Name;
    }

    public void setC_Name(String c_Name) {
        this.c_Name = c_Name;
    }

    public String getT_Time() {
        return t_Time;
    }

    public void setT_Time(String t_Time) {
        this.t_Time = t_Time;
    }

    public String getC_BillNO() {
        return c_BillNO;
    }

    public void setC_BillNO(String c_BillNO) {
        this.c_BillNO = c_BillNO;
    }

    public String getN_PayActual() {
        return n_PayActual;
    }

    public void setN_PayActual(String n_PayActual) {
        this.n_PayActual = n_PayActual;
    }

    public String getC_UserName() {
        return c_UserName;
    }

    public void setC_UserName(String c_UserName) {
        this.c_UserName = c_UserName;
    }

    public String getC_ShopName() {
        return c_ShopName;
    }

    public void setC_ShopName(String c_ShopName) {
        this.c_ShopName = c_ShopName;
    }

    public String getN_AmountAvailable() {
        return n_AmountAvailable;
    }

    public void setN_AmountAvailable(String n_AmountAvailable) {
        this.n_AmountAvailable = n_AmountAvailable;
    }

    public String getN_IntegralAvailable() {
        return n_IntegralAvailable;
    }

    public void setN_IntegralAvailable(String n_IntegralAvailable) {
        this.n_IntegralAvailable = n_IntegralAvailable;
    }

    public String getN_GetIntegral() {
        return n_GetIntegral;
    }

    public void setN_GetIntegral(String n_GetIntegral) {
        this.n_GetIntegral = n_GetIntegral;
    }

    public String getC_EquipmentNum() {
        return c_EquipmentNum;
    }

    public void setC_EquipmentNum(String c_EquipmentNum) {
        this.c_EquipmentNum = c_EquipmentNum;
    }

    public List<TimesDetailBean> getTimes_Detail() {
        return times_Detail;
    }

    public void setTimes_Detail(List<TimesDetailBean> times_Detail) {
        this.times_Detail = times_Detail;
    }

    public static class TimesDetailBean {
        private String id;
        private String i_GroupID;
        private String i_ShopID;
        private String i_BillID;
        private String c_BillStatus;
        private String c_ShopName;
        private String c_BillNO;
        private String i_GoodsID;
        private String c_GoodsNO;
        private String c_GoodsName;
        private String n_PriceRetail;
        private String n_PayShould;
        private String n_Number;
        private String n_IntegralValueMember;
        private String n_DiscountValueMember;
        private String n_IntegralValueGoods;
        private String n_DiscountValueGoods;
        private String n_PayActual;
        private String n_GetIntegral;
        private String c_CardNO;
        private String c_Name;
        private String i_MemberID;
        private String ROW_NUMBER;

        public String getN_PayShould() {
            return n_PayShould;
        }

        public void setN_PayShould(String n_PayShould) {
            this.n_PayShould = n_PayShould;
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

        public String getI_ShopID() {
            return i_ShopID;
        }

        public void setI_ShopID(String i_ShopID) {
            this.i_ShopID = i_ShopID;
        }

        public String getI_BillID() {
            return i_BillID;
        }

        public void setI_BillID(String i_BillID) {
            this.i_BillID = i_BillID;
        }

        public String getC_BillStatus() {
            return c_BillStatus;
        }

        public void setC_BillStatus(String c_BillStatus) {
            this.c_BillStatus = c_BillStatus;
        }

        public String getC_ShopName() {
            return c_ShopName;
        }

        public void setC_ShopName(String c_ShopName) {
            this.c_ShopName = c_ShopName;
        }

        public String getC_BillNO() {
            return c_BillNO;
        }

        public void setC_BillNO(String c_BillNO) {
            this.c_BillNO = c_BillNO;
        }

        public String getI_GoodsID() {
            return i_GoodsID;
        }

        public void setI_GoodsID(String i_GoodsID) {
            this.i_GoodsID = i_GoodsID;
        }

        public String getC_GoodsNO() {
            return c_GoodsNO;
        }

        public void setC_GoodsNO(String c_GoodsNO) {
            this.c_GoodsNO = c_GoodsNO;
        }

        public String getC_GoodsName() {
            return c_GoodsName;
        }

        public void setC_GoodsName(String c_GoodsName) {
            this.c_GoodsName = c_GoodsName;
        }

        public String getN_PriceRetail() {
            return n_PriceRetail;
        }

        public void setN_PriceRetail(String n_PriceRetail) {
            this.n_PriceRetail = n_PriceRetail;
        }

        public String getN_Number() {
            return n_Number;
        }

        public void setN_Number(String n_Number) {
            this.n_Number = n_Number;
        }

        public String getN_IntegralValueMember() {
            return n_IntegralValueMember;
        }

        public void setN_IntegralValueMember(String n_IntegralValueMember) {
            this.n_IntegralValueMember = n_IntegralValueMember;
        }

        public String getN_DiscountValueMember() {
            return n_DiscountValueMember;
        }

        public void setN_DiscountValueMember(String n_DiscountValueMember) {
            this.n_DiscountValueMember = n_DiscountValueMember;
        }

        public String getN_IntegralValueGoods() {
            return n_IntegralValueGoods;
        }

        public void setN_IntegralValueGoods(String n_IntegralValueGoods) {
            this.n_IntegralValueGoods = n_IntegralValueGoods;
        }

        public String getN_DiscountValueGoods() {
            return n_DiscountValueGoods;
        }

        public void setN_DiscountValueGoods(String n_DiscountValueGoods) {
            this.n_DiscountValueGoods = n_DiscountValueGoods;
        }

        public String getN_PayActual() {
            return n_PayActual;
        }

        public void setN_PayActual(String n_PayActual) {
            this.n_PayActual = n_PayActual;
        }

        public String getN_GetIntegral() {
            return n_GetIntegral;
        }

        public void setN_GetIntegral(String n_GetIntegral) {
            this.n_GetIntegral = n_GetIntegral;
        }

        public String getC_CardNO() {
            return c_CardNO;
        }

        public void setC_CardNO(String c_CardNO) {
            this.c_CardNO = c_CardNO;
        }

        public String getC_Name() {
            return c_Name;
        }

        public void setC_Name(String c_Name) {
            this.c_Name = c_Name;
        }

        public String getI_MemberID() {
            return i_MemberID;
        }

        public void setI_MemberID(String i_MemberID) {
            this.i_MemberID = i_MemberID;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
