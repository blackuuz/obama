package com.ksk.obama.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class ReadCardInfo {

    /**
     * result_code : SUCCESS
     * result_msg : 是我们的会员
     * result_data : {"id":"11","i_GroupID":"1","i_ShopID":"1","c_CardNO":"123","c_Name":"12312","c_Sex":"","c_Mobile":"","c_IDCard":"","c_Car":"","c_Add":"","c_Password":"","c_BirthdayType":"","i_BirthdayYear":"0","i_BirthdayMonth":"1","i_BirthdayDay":"1","i_ClassID":"1","c_ClassName":"金卡改过名","n_IntegralValue":"100.00","n_DiscountValue":"88.00","n_AmountAvailable":"1197.00","n_IntegralAvailable":"1000.00","n_IntegralAccumulated":"1000.00","t_CreateTime":"2016-07-08 10:41:44.000","t_StopTime":"2026-07-07 00:00:00.000","t_LastTime":"2016-07-08 10:41:44.000","c_OpenID":"","c_Remark":"","c_Photo":"","i_Memberup":null,"c_Memberpath":null,"ROW_NUMBER":"1"}
     * result_datas: [{……},{……},{……}]
     *
     */

    private String result_stadus;
    private String result_errmsg;
    private String result_lost;
    private String result_datasNum;  //是否开启


    private List<ResultDataBean> result_datas;

    public List<ResultDataBean> getResult_datas() {
        return result_datas;
    }

    public void setResult_datas(List<ResultDataBean> result_datas) {
        this.result_datas = result_datas;
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
     * N_Recharge_Integral_Value: .00
     * C_ServiceEmployee :""
     * C_PriceClass :""
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
    public String getResult_datasNum() {
        return result_datasNum;
    }

    public void setResult_datasNum(String result_datasNum) {
        this.result_datasNum = result_datasNum;
    }
    public String getResult_lost() {
        return result_lost;
    }

    public void setResult_lost(String result_lost) {
        this.result_lost = result_lost;
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


    public class PesultProject {
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
        private boolean isSelect = false;

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

    public static class ResultDataBean implements Parcelable {

        private String id;
        private String i_GroupID;
        private String i_ShopID;
        private String c_ShopName;
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
        private String i_Memberup;
        private String c_Memberpath;
        private String ROW_NUMBER;
        private String N_Recharge_Integral_Value;
        private String C_ServiceEmployee;//服务员工
        private String C_PriceClass;//会员商品打折类型





        public String getC_ServiceEmployee() {
            return C_ServiceEmployee;
        }

        public void setC_ServiceEmployee(String c_ServiceEmployee) {
            C_ServiceEmployee = c_ServiceEmployee;
        }

        public String getC_PriceClass() {
            return C_PriceClass;
        }

        public void setC_PriceClass(String c_PriceClass) {
            C_PriceClass = c_PriceClass;
        }

        public String getN_Recharge_Integral_Value() {
            return N_Recharge_Integral_Value;
        }

        public void setN_Recharge_Integral_Value(String n_Recharge_Integral_Value) {
            N_Recharge_Integral_Value = n_Recharge_Integral_Value;
        }
        public static final Creator<ResultDataBean> CREATOR = new Creator<ResultDataBean>() {
            @Override
            public ResultDataBean createFromParcel(Parcel in) {
                return new ResultDataBean(in);
            }

            @Override
            public ResultDataBean[] newArray(int size) {
                return new ResultDataBean[size];
            }
        };

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

        public String getC_ShopName() {
            return c_ShopName;
        }

        public void setC_ShopName(String c_ShopName) {
            this.c_ShopName = c_ShopName;
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

        public String getI_Memberup() {
            return i_Memberup;
        }

        public void setI_Memberup(String i_Memberup) {
            this.i_Memberup = i_Memberup;
        }

        public String getC_Memberpath() {
            return c_Memberpath;
        }

        public void setC_Memberpath(String c_Memberpath) {
            this.c_Memberpath = c_Memberpath;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        protected ResultDataBean(Parcel in) {
            id = in.readString();
            i_GroupID = in.readString();
            i_ShopID = in.readString();
            c_ShopName = in.readString();
            c_CardNO = in.readString();
            c_Name = in.readString();
            c_Sex = in.readString();
            c_Mobile = in.readString();
            c_IDCard = in.readString();
            c_Car = in.readString();
            c_Add = in.readString();
            c_Password = in.readString();
            c_BirthdayType = in.readString();
            i_BirthdayYear = in.readString();
            i_BirthdayMonth = in.readString();
            i_BirthdayDay = in.readString();
            i_ClassID = in.readString();
            c_ClassName = in.readString();
            n_IntegralValue = in.readString();
            n_DiscountValue = in.readString();
            n_AmountAvailable = in.readString();
            n_IntegralAvailable = in.readString();
            n_IntegralAccumulated = in.readString();
            t_CreateTime = in.readString();
            t_StopTime = in.readString();
            t_LastTime = in.readString();
            c_OpenID = in.readString();
            c_Remark = in.readString();
            c_Photo = in.readString();
            N_Recharge_Integral_Value = in.readString();
            ROW_NUMBER = in.readString();
            C_ServiceEmployee = in.readString();
            C_PriceClass = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(i_GroupID);
            dest.writeString(i_ShopID);
            dest.writeString(c_ShopName);
            dest.writeString(c_CardNO);
            dest.writeString(c_Name);
            dest.writeString(c_Sex);
            dest.writeString(c_Mobile);
            dest.writeString(c_IDCard);
            dest.writeString(c_Car);
            dest.writeString(c_Add);
            dest.writeString(c_Password);
            dest.writeString(c_BirthdayType);
            dest.writeString(i_BirthdayYear);
            dest.writeString(i_BirthdayMonth);
            dest.writeString(i_BirthdayDay);
            dest.writeString(i_ClassID);
            dest.writeString(c_ClassName);
            dest.writeString(n_IntegralValue);
            dest.writeString(n_DiscountValue);
            dest.writeString(n_AmountAvailable);
            dest.writeString(n_IntegralAvailable);
            dest.writeString(n_IntegralAccumulated);
            dest.writeString(t_CreateTime);
            dest.writeString(t_StopTime);
            dest.writeString(t_LastTime);
            dest.writeString(c_OpenID);
            dest.writeString(c_Remark);
            dest.writeString(c_Photo);
            dest.writeString(N_Recharge_Integral_Value);
            dest.writeString(ROW_NUMBER);
            dest.writeString(C_ServiceEmployee);
            dest.writeString(C_PriceClass);
        }
    }
}
