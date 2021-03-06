package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class CardInfo {

    /**
     * result_code : SUCCESS
     * result_msg : 是我们的会员
     * result_data : {"id":"11","i_GroupID":"1","i_ShopID":"1","c_CardNO":"123","c_Name":"12312","c_Sex":"","c_Mobile":"","c_IDCard":"","c_Car":"","c_Add":"","c_Password":"","c_BirthdayType":"","i_BirthdayYear":"0","i_BirthdayMonth":"1","i_BirthdayDay":"1","i_ClassID":"1","c_ClassName":"金卡改过名","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_AmountAvailable":"1197.00","n_IntegralAvailable":"1000.00","n_IntegralAccumulated":"1000.00","t_CreateTime":"2016-07-08 10:41:44.000","t_StopTime":"2026-07-07 00:00:00.000","t_LastTime":"2016-07-08 10:41:44.000","c_OpenID":"","c_Remark":"","c_Photo":"","i_Memberup":null,"c_Memberpath":null,"ROW_NUMBER":"1"}
     * result_datas : [{……},{……},{……}]
     */

    private String result_stadus;
    private String result_errmsg;


    private String result_datasNum;//数值大于一时，弹出多卡提示

    private List<ResultDataBean> result_datas;


    public List<ResultDataBean> getResult_datas() {
        return result_datas;
    }

    public void setResult_datas(List<ResultDataBean> result_datas) {
        this.result_datas = result_datas;
    }


    public String getResult_datasNum() {
        return result_datasNum;
    }

    public void setResult_datasNum(String result_datasNum) {
        this.result_datasNum = result_datasNum;
    }


    /**
     * id : 11
     * i_GroupID : 1
     * i_ShopID : 1
     * c_CardNO : 123
     * c_Name : 12312
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
     * n_AmountAvailable : 1197.00
     * n_IntegralAvailable : 1000.00
     * n_IntegralAccumulated : 1000.00
     * t_CreateTime : 2016-07-08 10:41:44.000
     * t_StopTime : 2026-07-07 00:00:00.000
     * t_LastTime : 2016-07-08 10:41:44.000
     * c_OpenID :
     * c_Remark :
     * c_Photo :
     * i_Memberup : null
     * c_Memberpath : null
     * ROW_NUMBER : 1
     */

    private ResultDataBean result_data;
    private List<PesultProject> result_project;

    public List<PesultProject> getResult_project() {
        return result_project;
    }

    public void setResult_project(List<PesultProject> result_project) {
        this.result_project = result_project;
    }

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

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }


    public class PesultProject{
        private String i_GoodsID;

        public String getI_GroupID() {
            return i_GoodsID;
        }

        public void setI_GroupID(String i_GroupID) {
            this.i_GoodsID = i_GroupID;
        }

        private String c_GoodsName;
        private String n_Times;
        private String t_StopTime;
        private boolean isSelect=false;

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String getC_GoodsName() {
            return c_GoodsName;
        }

        public void setC_GoodsName(String c_GoodsName) {
            this.c_GoodsName = c_GoodsName;
        }

        public String getN_Times() {
            return n_Times;
        }

        public void setN_Times(String n_Times) {
            this.n_Times = n_Times;
        }

        public String getT_StopTime() {
            return t_StopTime;
        }

        public void setT_StopTime(String t_StopTime) {
            this.t_StopTime = t_StopTime;
        }
    }
    public class ResultDataBean {
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
        private Object i_Memberup;
        private Object c_Memberpath;
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

        public Object getI_Memberup() {
            return i_Memberup;
        }

        public void setI_Memberup(Object i_Memberup) {
            this.i_Memberup = i_Memberup;
        }

        public Object getC_Memberpath() {
            return c_Memberpath;
        }

        public void setC_Memberpath(Object c_Memberpath) {
            this.c_Memberpath = c_Memberpath;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
