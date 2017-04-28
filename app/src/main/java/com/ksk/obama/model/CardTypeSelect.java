package com.ksk.obama.model;

import java.util.List;

public class CardTypeSelect {
    /**
     * result_code : SUCCESS
     * result_msg : 查询成功
     * result_data : [{"id":"1","i_GroupID":"1","c_ClassName":"金卡改过名","n_Price":"1000.00","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_InitAmount":"1200.00","n_InitIntegral":"1000.00","ROW_NUMBER":"1"},{"id":"2","i_GroupID":"1","c_ClassName":"次数卡","n_Price":".01","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":".00","n_InitIntegral":".00","ROW_NUMBER":"2"},{"id":"3","i_GroupID":"1","c_ClassName":"带初始金额的储值卡","n_Price":"1000.00","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_InitAmount":"1000.00","n_InitIntegral":".00","ROW_NUMBER":"3"},{"id":"4","i_GroupID":"1","c_ClassName":"带初始金额和积分","n_Price":"2000.00","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"2000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"4"},{"id":"5","i_GroupID":"1","c_ClassName":"带初始金额和次数的卡","n_Price":"1000.00","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"1000.00","n_InitIntegral":".00","ROW_NUMBER":"5"},{"id":"6","i_GroupID":"1","c_ClassName":"会员卡","n_Price":"2000.00","c_Validity":"[10]年","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"2000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"6"},{"id":"7","i_GroupID":"1","c_ClassName":"G-CARD","n_Price":"100.00","c_Validity":"[1]年","n_IntegralValue":"100.00","n_DiscountValue":"50.00","n_InitAmount":"1000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"7"}]
     */

    private String result_stadus;
    private String result_errmsg;
    /**
     * id : 1
     * i_GroupID : 1
     * c_ClassName : 金卡改过名
     * n_Price : 1000.00
     * c_Validity : [10]年
     * n_IntegralValue : 100.00
     * n_DiscountValue : 88.00
     * n_InitAmount : 1200.00
     * n_InitIntegral : 1000.00
     * ROW_NUMBER : 1
     */

    private List<ResultDataBean> result_data;

   // {"result_code":"SUCCESS","result_msg":"\u67e5\u8be2\u6210\u529f","result_data":[{"id":"1","i_GroupID":"1","c_ClassName":"\u91d1\u5361\u6539\u8fc7\u540d","n_Price":"1000.00","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_InitAmount":"1200.00","n_InitIntegral":"1000.00","ROW_NUMBER":"1"},{"id":"2","i_GroupID":"1","c_ClassName":"\u6b21\u6570\u5361","n_Price":".01","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":".00","n_InitIntegral":".00","ROW_NUMBER":"2"},{"id":"3","i_GroupID":"1","c_ClassName":"\u5e26\u521d\u59cb\u91d1\u989d\u7684\u50a8\u503c\u5361","n_Price":"1000.00","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_InitAmount":"1000.00","n_InitIntegral":".00","ROW_NUMBER":"3"},{"id":"4","i_GroupID":"1","c_ClassName":"\u5e26\u521d\u59cb\u91d1\u989d\u548c\u79ef\u5206","n_Price":"2000.00","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"2000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"4"},{"id":"5","i_GroupID":"1","c_ClassName":"\u5e26\u521d\u59cb\u91d1\u989d\u548c\u6b21\u6570\u7684\u5361","n_Price":"1000.00","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"1000.00","n_InitIntegral":".00","ROW_NUMBER":"5"},{"id":"6","i_GroupID":"1","c_ClassName":"\u4f1a\u5458\u5361","n_Price":"2000.00","c_Validity":"[10]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"100.00","n_InitAmount":"2000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"6"},{"id":"7","i_GroupID":"1","c_ClassName":"G-CARD","n_Price":"100.00","c_Validity":"[1]\u5e74","n_IntegralValue":"100.00","n_DiscountValue":"50.00","n_InitAmount":"1000.00","n_InitIntegral":"1000.00","ROW_NUMBER":"7"}]}


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

    public static class ResultDataBean {
        private String id;
        private String i_GroupID;
        private String c_ClassName;
        private String n_Price;
        private String c_Validity;
        private String n_IntegralValue;
        private String n_DiscountValue;
        private String n_InitAmount;
        private String n_InitIntegral;
        private String ROW_NUMBER;

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

        public String getC_ClassName() {
            return c_ClassName;
        }

        public void setC_ClassName(String c_ClassName) {
            this.c_ClassName = c_ClassName;
        }

        public String getN_Price() {
            return n_Price;
        }

        public void setN_Price(String n_Price) {
            this.n_Price = n_Price;
        }

        public String getC_Validity() {
            return c_Validity;
        }

        public void setC_Validity(String c_Validity) {
            this.c_Validity = c_Validity;
        }

        public String getN_IntegralValue() {
            return n_IntegralValue;
        }

        public void setN_IntegralValue(String n_IntegralValue) {
            this.n_IntegralValue = n_IntegralValue;
        }

        public String getN_DiscountValue() {
            return n_DiscountValue;
        }

        public void setN_DiscountValue(String n_DiscountValue) {
            this.n_DiscountValue = n_DiscountValue;
        }

        public String getN_InitAmount() {
            return n_InitAmount;
        }

        public void setN_InitAmount(String n_InitAmount) {
            this.n_InitAmount = n_InitAmount;
        }

        public String getN_InitIntegral() {
            return n_InitIntegral;
        }

        public void setN_InitIntegral(String n_InitIntegral) {
            this.n_InitIntegral = n_InitIntegral;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
