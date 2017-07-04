package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class QuickCount {


    /**
     * num : 2  如果num小于2 返回 data 和memberdata
     * Stadus : SUCCESS
     * data : [{"id":"20","i_GroupID":"1","c_GoodsNO":"P00020","c_GoodsName":"辣椒","c_Pinyin":"LJ","c_Unit":"","c_GoodsClassify":"萝卜","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"3.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"1","r_IsMaterial":"0","goods_times":"86","StopTime":"2099-12-31 00:00:00.000","ROW_NUMBER":"1"},{"id":"20","i_GroupID":"1","c_GoodsNO":"P00020","c_GoodsName":"辣椒","c_Pinyin":"LJ","c_Unit":"","c_GoodsClassify":"萝卜","c_BarCode":"","n_PricePurchase":".00","n_PriceRetail":"3.00","n_LowerLimit":".00","n_DiscountValue":"1","n_IntegralValue":"1","r_IsMaterial":"0","goods_times":"10","StopTime":"2099-12-31 00:00:00.000","ROW_NUMBER":"2"}]
     * memberdata : {"id":"325","i_GroupID":"1","i_ShopID":"1","c_CardNO":"5607","c_Name":"经济","c_Sex":"","c_Mobile":"","c_IDCard":null,"c_Car":null,"c_Add":null,"c_Password":"","c_BirthdayType":null,"i_BirthdayYear":null,"i_BirthdayMonth":null,"i_BirthdayDay":null,"i_ClassID":"1","c_ClassName":"金卡改过名","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_AmountAvailable":"3075.00","n_IntegralAvailable":"1136.00","n_IntegralAccumulated":"1118.00","t_CreateTime":"2016-10-11 11:25:04.000","t_StopTime":"2026-10-11 00:00:00.000","t_LastTime":"2000-01-01 00:00:00.000","c_OpenID":"","c_Remark":"","c_Photo":"","c_Memberpath":"0","i_Parent":"0","ROW_NUMBER":"1"}
     *
     *
     *  如果num 大于2 返回datas 和memberdatas
     * datas :[[{……},{……}],[{……},{……}]]
     *
     */
    private String num;
    private String result_stadus;
    private MemberdataBean memberdata;
    private List<DataBean> data;

    private List<MemberdataBean> memberdatas;
    private List<List<DataBean>> datas;


    public List<List<DataBean>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<DataBean>> datas) {
        this.datas = datas;
    }

    public List<MemberdataBean> getMemberdatas() {
        return memberdatas;
    }

    public void setMemberdatas(List<MemberdataBean> memberdatas) {
        this.memberdatas = memberdatas;
    }




    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }



    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public MemberdataBean getMemberdata() {
        return memberdata;
    }

    public void setMemberdata(MemberdataBean memberdata) {
        this.memberdata = memberdata;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class MemberdataBean {
        /**
         * id : 325
         * i_GroupID : 1
         * i_ShopID : 1
         * c_CardNO : 5607
         * c_Name : 经济
         * c_Sex :
         * c_Mobile :
         * c_IDCard : null
         * c_Car : null
         * c_Add : null
         * c_Password :
         * c_BirthdayType : null
         * i_BirthdayYear : null
         * i_BirthdayMonth : null
         * i_BirthdayDay : null
         * i_ClassID : 1
         * c_ClassName : 金卡改过名
         * n_IntegralValue : 100.00
         * n_DiscountValue : 88.00
         * n_AmountAvailable : 3075.00
         * n_IntegralAvailable : 1136.00
         * n_IntegralAccumulated : 1118.00
         * t_CreateTime : 2016-10-11 11:25:04.000
         * t_StopTime : 2026-10-11 00:00:00.000
         * t_LastTime : 2000-01-01 00:00:00.000
         * c_OpenID :
         * c_Remark :
         * c_Photo :
         * c_Memberpath : 0
         * i_Parent : 0
         * ROW_NUMBER : 1
         */

        private String id;
        private String i_GroupID;
        private String i_ShopID;
        private String c_CardNO;
        private String c_Name;
        private String c_Sex;
        private String c_Mobile;
        private Object c_IDCard;
        private Object c_Car;
        private Object c_Add;
        private String c_Password;
        private Object c_BirthdayType;
        private Object i_BirthdayYear;
        private Object i_BirthdayMonth;
        private Object i_BirthdayDay;
        private String i_ClassID;
        private String c_ClassName;
        private String n_IntegralValue;
        private String n_DiscountValue;
        private String n_AmountAvailable;
        private String n_IntegralAvailable;
        private String n_IntegralAccumulated;
        private String t_CreateTime;
        private String t_StopTime;
        private String t_LastTime;
        private String c_OpenID;
        private String c_Remark;
        private String c_Photo;
        private String c_Memberpath;
        private String i_Parent;
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

        public String getI_ShopID() {
            return i_ShopID;
        }

        public void setI_ShopID(String i_ShopID) {
            this.i_ShopID = i_ShopID;
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

        public String getC_Sex() {
            return c_Sex;
        }

        public void setC_Sex(String c_Sex) {
            this.c_Sex = c_Sex;
        }

        public String getC_Mobile() {
            return c_Mobile;
        }

        public void setC_Mobile(String c_Mobile) {
            this.c_Mobile = c_Mobile;
        }

        public Object getC_IDCard() {
            return c_IDCard;
        }

        public void setC_IDCard(Object c_IDCard) {
            this.c_IDCard = c_IDCard;
        }

        public Object getC_Car() {
            return c_Car;
        }

        public void setC_Car(Object c_Car) {
            this.c_Car = c_Car;
        }

        public Object getC_Add() {
            return c_Add;
        }

        public void setC_Add(Object c_Add) {
            this.c_Add = c_Add;
        }

        public String getC_Password() {
            return c_Password;
        }

        public void setC_Password(String c_Password) {
            this.c_Password = c_Password;
        }

        public Object getC_BirthdayType() {
            return c_BirthdayType;
        }

        public void setC_BirthdayType(Object c_BirthdayType) {
            this.c_BirthdayType = c_BirthdayType;
        }

        public Object getI_BirthdayYear() {
            return i_BirthdayYear;
        }

        public void setI_BirthdayYear(Object i_BirthdayYear) {
            this.i_BirthdayYear = i_BirthdayYear;
        }

        public Object getI_BirthdayMonth() {
            return i_BirthdayMonth;
        }

        public void setI_BirthdayMonth(Object i_BirthdayMonth) {
            this.i_BirthdayMonth = i_BirthdayMonth;
        }

        public Object getI_BirthdayDay() {
            return i_BirthdayDay;
        }

        public void setI_BirthdayDay(Object i_BirthdayDay) {
            this.i_BirthdayDay = i_BirthdayDay;
        }

        public String getI_ClassID() {
            return i_ClassID;
        }

        public void setI_ClassID(String i_ClassID) {
            this.i_ClassID = i_ClassID;
        }

        public String getC_ClassName() {
            return c_ClassName;
        }

        public void setC_ClassName(String c_ClassName) {
            this.c_ClassName = c_ClassName;
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

        public String getN_IntegralAccumulated() {
            return n_IntegralAccumulated;
        }

        public void setN_IntegralAccumulated(String n_IntegralAccumulated) {
            this.n_IntegralAccumulated = n_IntegralAccumulated;
        }

        public String getT_CreateTime() {
            return t_CreateTime;
        }

        public void setT_CreateTime(String t_CreateTime) {
            this.t_CreateTime = t_CreateTime;
        }

        public String getT_StopTime() {
            return t_StopTime;
        }

        public void setT_StopTime(String t_StopTime) {
            this.t_StopTime = t_StopTime;
        }

        public String getT_LastTime() {
            return t_LastTime;
        }

        public void setT_LastTime(String t_LastTime) {
            this.t_LastTime = t_LastTime;
        }

        public String getC_OpenID() {
            return c_OpenID;
        }

        public void setC_OpenID(String c_OpenID) {
            this.c_OpenID = c_OpenID;
        }

        public String getC_Remark() {
            return c_Remark;
        }

        public void setC_Remark(String c_Remark) {
            this.c_Remark = c_Remark;
        }

        public String getC_Photo() {
            return c_Photo;
        }

        public void setC_Photo(String c_Photo) {
            this.c_Photo = c_Photo;
        }

        public String getC_Memberpath() {
            return c_Memberpath;
        }

        public void setC_Memberpath(String c_Memberpath) {
            this.c_Memberpath = c_Memberpath;
        }

        public String getI_Parent() {
            return i_Parent;
        }

        public void setI_Parent(String i_Parent) {
            this.i_Parent = i_Parent;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }

    public static class DataBean {
        /**
         * id : 20
         * i_GroupID : 1
         * c_GoodsNO : P00020
         * c_GoodsName : 辣椒
         * c_Pinyin : LJ
         * c_Unit :
         * c_GoodsClassify : 萝卜
         * c_BarCode :
         * n_PricePurchase : .00
         * n_PriceRetail : 3.00
         * n_LowerLimit : .00
         * n_DiscountValue : 1
         * n_IntegralValue : 1
         * r_IsMaterial : 0
         * goods_times : 86
         * StopTime : 2099-12-31 00:00:00.000
         * ROW_NUMBER : 1
         */

        private String id;
        private String i_GroupID;
        private String c_GoodsNO;
        private String c_GoodsName;
        private String c_Pinyin;
        private String c_Unit;
        private String c_GoodsClassify;
        private String c_BarCode;
        private String n_PricePurchase;
        private String n_PriceRetail;
        private String n_LowerLimit;
        private String n_DiscountValue;
        private String n_IntegralValue;
        private String r_IsMaterial;
        private String goods_times;
        private String StopTime;
        private String SumTimes;
        private String ROW_NUMBER;
        private boolean isSelect = false;

        public String getSumTimes() {
            return SumTimes;
        }

        public void setSumTimes(String sumTimes) {
            SumTimes = sumTimes;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
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

        public String getN_PriceRetail() {
            return n_PriceRetail;
        }

        public void setN_PriceRetail(String n_PriceRetail) {
            this.n_PriceRetail = n_PriceRetail;
        }

        public String getN_LowerLimit() {
            return n_LowerLimit;
        }

        public void setN_LowerLimit(String n_LowerLimit) {
            this.n_LowerLimit = n_LowerLimit;
        }

        public String getN_DiscountValue() {
            return n_DiscountValue;
        }

        public void setN_DiscountValue(String n_DiscountValue) {
            this.n_DiscountValue = n_DiscountValue;
        }

        public String getN_IntegralValue() {
            return n_IntegralValue;
        }

        public void setN_IntegralValue(String n_IntegralValue) {
            this.n_IntegralValue = n_IntegralValue;
        }

        public String getR_IsMaterial() {
            return r_IsMaterial;
        }

        public void setR_IsMaterial(String r_IsMaterial) {
            this.r_IsMaterial = r_IsMaterial;
        }

        public String getGoods_times() {
            return goods_times;
        }

        public void setGoods_times(String goods_times) {
            this.goods_times = goods_times;
        }

        public String getStopTime() {
            return StopTime;
        }

        public void setStopTime(String StopTime) {
            this.StopTime = StopTime;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
