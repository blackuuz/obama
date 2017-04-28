package com.ksk.obama.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class BuyCountList {


    /**
     * result_msg : success
     * goods_list : [{"id":"18","i_GroupID":"1","c_GoodsNO":"P00001","c_GoodsName":"草莓","c_Pinyin":"CM","c_Unit":"","c_GoodsClassify":"默认分类","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"1.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"1","r_IsMaterial":"0","ROW_NUMBER":"1"},{"id":"19","i_GroupID":"1","c_GoodsNO":"P00019","c_GoodsName":"花椒","c_Pinyin":"HJ","c_Unit":"","c_GoodsClassify":"默认分类","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"2.00","n_LowerLimit":".00","n_DiscountValue":"0","n_IntegralValue":"0","r_IsMaterial":"0","ROW_NUMBER":"2"},{"id":"20","i_GroupID":"1","c_GoodsNO":"P00020","c_GoodsName":"辣椒","c_Pinyin":"LJ","c_Unit":"","c_GoodsClassify":"默认分类","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"3.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"1","r_IsMaterial":"0","ROW_NUMBER":"3"},{"id":"21","i_GroupID":"1","c_GoodsNO":"P00021","c_GoodsName":"iPhone 7 Plus","c_Pinyin":"iPhone 7 Plus","c_Unit":"","c_GoodsClassify":"默认分类","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"6688.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"1","r_IsMaterial":"0","ROW_NUMBER":"4"},{"id":"22","i_GroupID":"1","c_GoodsNO":"8000001","c_GoodsName":"足疗","c_Pinyin":"ZL","c_Unit":"","c_GoodsClassify":"默认分类","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"188.00","n_LowerLimit":".00","n_DiscountValue":"0","n_IntegralValue":"0","r_IsMaterial":"0","ROW_NUMBER":"5"},{"id":"31","i_GroupID":"1","c_GoodsNO":"P00031","c_GoodsName":"红牛（仅可打折）","c_Pinyin":"HN（JKDZ）","c_Unit":"个","c_GoodsClassify":"G","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"10.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"0","r_IsMaterial":"0","ROW_NUMBER":"6"},{"id":"32","i_GroupID":"1","c_GoodsNO":"P00032","c_GoodsName":"绿牛（仅可积分）","c_Pinyin":"LN（JKJF）","c_Unit":"个","c_GoodsClassify":"G","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"10.00","n_LowerLimit":".00","n_DiscountValue":"0","n_IntegralValue":"1","r_IsMaterial":"0","ROW_NUMBER":"7"}]
     * goods_class : [{"class":"123","ROW_NUMBER":"1"},{"class":"456","ROW_NUMBER":"2"},{"class":"萝卜1","ROW_NUMBER":"3"},{"class":"G","ROW_NUMBER":"4"}]
     * Discount : 0.88
     * Integral : 1
     */

    private String result_stadus;
    private String result_errmsg;
    private float Discount;
    private float Integral;
    /**
     * id : 18
     * i_GroupID : 1
     * c_GoodsNO : P00001
     * c_GoodsName : 草莓
     * c_Pinyin : CM
     * c_Unit :
     * c_GoodsClassify : 默认分类
     * c_BarCode :
     * n_PricePurchase : .00
     * n_PriceRetail : 1.00
     * n_LowerLimit : .00
     * n_DiscountValue : 1
     * n_IntegralValue : 1
     * r_IsMaterial : 0
     * ROW_NUMBER : 1
     */

    private List<GoodsListBean> goods_list;
    /**
     * class : 123
     * ROW_NUMBER : 1
     */

    private List<GoodsClassBean> goods_class;

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

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float Discount) {
        this.Discount = Discount;
    }

    public float getIntegral() {
        return Integral;
    }

    public void setIntegral(float Integral) {
        this.Integral = Integral;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public List<GoodsClassBean> getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(List<GoodsClassBean> goods_class) {
        this.goods_class = goods_class;
    }

    public static class GoodsListBean {
        private String id;
        private String i_GroupID;
        private String c_GoodsNO;
        private String c_GoodsName;
        private String c_Pinyin;
        private String c_Unit;
        private String c_GoodsClassify;
        private String c_BarCode;
        private String n_PricePurchase;
        private float n_PriceRetail;
        private String n_LowerLimit;
        private float n_DiscountValue;
        private float n_IntegralValue;
        private String r_IsMaterial;
        private float num = 0;
        private String validTime = getNowTime();
        private String ROW_NUMBER;

        private String getNowTime() {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }

        public float getNum() {
            return num;
        }

        public void setNum(float num) {
            this.num = num;
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

        public String getC_Pinyin() {
            return c_Pinyin;
        }

        public void setC_Pinyin(String c_Pinyin) {
            this.c_Pinyin = c_Pinyin;
        }

        public String getC_Unit() {
            return c_Unit;
        }

        public void setC_Unit(String c_Unit) {
            this.c_Unit = c_Unit;
        }

        public String getC_GoodsClassify() {
            return c_GoodsClassify;
        }

        public void setC_GoodsClassify(String c_GoodsClassify) {
            this.c_GoodsClassify = c_GoodsClassify;
        }

        public String getC_BarCode() {
            return c_BarCode;
        }

        public void setC_BarCode(String c_BarCode) {
            this.c_BarCode = c_BarCode;
        }

        public String getN_PricePurchase() {
            return n_PricePurchase;
        }

        public void setN_PricePurchase(String n_PricePurchase) {
            this.n_PricePurchase = n_PricePurchase;
        }

        public float getN_PriceRetail() {
            return n_PriceRetail;
        }

        public void setN_PriceRetail(float n_PriceRetail) {
            this.n_PriceRetail = n_PriceRetail;
        }

        public String getN_LowerLimit() {
            return n_LowerLimit;
        }

        public void setN_LowerLimit(String n_LowerLimit) {
            this.n_LowerLimit = n_LowerLimit;
        }

        public float getN_DiscountValue() {
            return n_DiscountValue;
        }

        public void setN_DiscountValue(float n_DiscountValue) {
            this.n_DiscountValue = n_DiscountValue;
        }

        public float getN_IntegralValue() {
            return n_IntegralValue;
        }

        public void setN_IntegralValue(float n_IntegralValue) {
            this.n_IntegralValue = n_IntegralValue;
        }

        public String getR_IsMaterial() {
            return r_IsMaterial;
        }

        public void setR_IsMaterial(String r_IsMaterial) {
            this.r_IsMaterial = r_IsMaterial;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }

    public static class GoodsClassBean {
        @SerializedName("class")
        private String classX;
//        private String ROW_NUMBER;

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

//        public String getROW_NUMBER() {
//            return ROW_NUMBER;
//        }
//
//        public void setROW_NUMBER(String ROW_NUMBER) {
//            this.ROW_NUMBER = ROW_NUMBER;
//        }
    }
}
