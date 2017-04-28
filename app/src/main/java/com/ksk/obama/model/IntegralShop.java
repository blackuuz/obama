package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class IntegralShop {

    /**
     * result_msg : success
     * gift : [{"id":"3","i_GroupID":"1","i_ShopID":"1","n_Integral":"2131.23","c_Present":"草莓","i_GoodsID":"4","ROW_NUMBER":"1"},{"id":"5","i_GroupID":"1","i_ShopID":"1","n_Integral":"11.00","c_Present":"樱桃","i_GoodsID":"0","ROW_NUMBER":"2"},{"id":"6","i_GroupID":"1","i_ShopID":"1","n_Integral":"10.00","c_Present":"苹果","i_GoodsID":"0","ROW_NUMBER":"3"},{"id":"7","i_GroupID":"1","i_ShopID":"1","n_Integral":"1.00","c_Present":"555","i_GoodsID":"0","ROW_NUMBER":"4"}]
     * member : {"id":"70","i_GroupID":"1","i_ShopID":"1","c_CardNO":"5607","c_Name":"5607","c_Sex":"","c_Mobile":"","c_IDCard":"","c_Car":"","c_Add":"","c_Password":"","c_BirthdayType":"","i_BirthdayYear":"0","i_BirthdayMonth":"1","i_BirthdayDay":"1","i_ClassID":"1","c_ClassName":"金卡改过名","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_AmountAvailable":"1001481.53","n_IntegralAvailable":"89034.56","n_IntegralAccumulated":"89374.56","t_CreateTime":"2016-08-22 10:52:11.000","t_StopTime":"2026-08-21 00:00:00.000","t_LastTime":"2016-08-23 10:01:10.723","c_OpenID":"","c_Remark":"","c_Photo":"","c_Memberpath":"0","i_Parent":"0","ROW_NUMBER":"1"}
     */

    private String result_stadus;
    /**
     * id : 70
     * i_GroupID : 1
     * i_ShopID : 1
     * c_CardNO : 5607
     * c_Name : 5607
     * c_Sex :
     * c_Mobile :
     * c_IDCard :
     * c_Car :
     * c_Add :
     * c_Password :
     * c_BirthdayType :
     * i_BirthdayYear : 0
     * i_BirthdayMonth : 1
     * i_BirthdayDay : 1
     * i_ClassID : 1
     * c_ClassName : 金卡改过名
     * n_IntegralValue : 100.00
     * n_DiscountValue : 88.00
     * n_AmountAvailable : 1001481.53
     * n_IntegralAvailable : 89034.56
     * n_IntegralAccumulated : 89374.56
     * t_CreateTime : 2016-08-22 10:52:11.000
     * t_StopTime : 2026-08-21 00:00:00.000
     * t_LastTime : 2016-08-23 10:01:10.723
     * c_OpenID :
     * c_Remark :
     * c_Photo :
     * c_Memberpath : 0
     * i_Parent : 0
     * ROW_NUMBER : 1
     */

    private MemberBean member;
    /**
     * id : 3
     * i_GroupID : 1
     * i_ShopID : 1
     * n_Integral : 2131.23
     * c_Present : 草莓
     * i_GoodsID : 4
     * ROW_NUMBER : 1
     */

    private List<GiftBean> gift;

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public List<GiftBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftBean> gift) {
        this.gift = gift;
    }

    public static class MemberBean {
        private String id;
        private String i_GroupID;
        private String i_ShopID;
        private String c_CardNO;
        private String c_Name;
        private String c_Sex;
        private String c_Mobile;
        private String c_IDCard;
        private String c_Car;
        private String c_Add;
        private String c_Password;
        private String c_BirthdayType;
        private String i_BirthdayYear;
        private String i_BirthdayMonth;
        private String i_BirthdayDay;
        private String i_ClassID;
        private String c_ClassName;
        private float n_IntegralValue;
        private float n_DiscountValue;
        private float n_AmountAvailable;
        private float n_IntegralAvailable;
        private float n_IntegralAccumulated;
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

        public String getC_IDCard() {
            return c_IDCard;
        }

        public void setC_IDCard(String c_IDCard) {
            this.c_IDCard = c_IDCard;
        }

        public String getC_Car() {
            return c_Car;
        }

        public void setC_Car(String c_Car) {
            this.c_Car = c_Car;
        }

        public String getC_Add() {
            return c_Add;
        }

        public void setC_Add(String c_Add) {
            this.c_Add = c_Add;
        }

        public String getC_Password() {
            return c_Password;
        }

        public void setC_Password(String c_Password) {
            this.c_Password = c_Password;
        }

        public String getC_BirthdayType() {
            return c_BirthdayType;
        }

        public void setC_BirthdayType(String c_BirthdayType) {
            this.c_BirthdayType = c_BirthdayType;
        }

        public String getI_BirthdayYear() {
            return i_BirthdayYear;
        }

        public void setI_BirthdayYear(String i_BirthdayYear) {
            this.i_BirthdayYear = i_BirthdayYear;
        }

        public String getI_BirthdayMonth() {
            return i_BirthdayMonth;
        }

        public void setI_BirthdayMonth(String i_BirthdayMonth) {
            this.i_BirthdayMonth = i_BirthdayMonth;
        }

        public String getI_BirthdayDay() {
            return i_BirthdayDay;
        }

        public void setI_BirthdayDay(String i_BirthdayDay) {
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

        public float getN_IntegralValue() {
            return n_IntegralValue;
        }

        public void setN_IntegralValue(float n_IntegralValue) {
            this.n_IntegralValue = n_IntegralValue;
        }

        public float getN_DiscountValue() {
            return n_DiscountValue;
        }

        public void setN_DiscountValue(float n_DiscountValue) {
            this.n_DiscountValue = n_DiscountValue;
        }

        public float getN_AmountAvailable() {
            return n_AmountAvailable;
        }

        public void setN_AmountAvailable(float n_AmountAvailable) {
            this.n_AmountAvailable = n_AmountAvailable;
        }

        public float getN_IntegralAvailable() {
            return n_IntegralAvailable;
        }

        public void setN_IntegralAvailable(float n_IntegralAvailable) {
            this.n_IntegralAvailable = n_IntegralAvailable;
        }

        public float getN_IntegralAccumulated() {
            return n_IntegralAccumulated;
        }

        public void setN_IntegralAccumulated(float n_IntegralAccumulated) {
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

    public static class GiftBean {
        private String id;
        private float num;
        private String i_GroupID;
        private String i_ShopID;
        private float n_Integral;
        private String c_Present;
        private String i_GoodsID;
        private String ROW_NUMBER;

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

        public String getI_ShopID() {
            return i_ShopID;
        }

        public void setI_ShopID(String i_ShopID) {
            this.i_ShopID = i_ShopID;
        }

        public float getN_Integral() {
            return n_Integral;
        }

        public void setN_Integral(float n_Integral) {
            this.n_Integral = n_Integral;
        }

        public String getC_Present() {
            return c_Present;
        }

        public void setC_Present(String c_Present) {
            this.c_Present = c_Present;
        }

        public String getI_GoodsID() {
            return i_GoodsID;
        }

        public void setI_GoodsID(String i_GoodsID) {
            this.i_GoodsID = i_GoodsID;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
