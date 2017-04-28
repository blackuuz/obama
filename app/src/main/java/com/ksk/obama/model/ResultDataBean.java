package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ResultDataBean {
    private boolean isSelect = false;
    private String id;
    private String i_GroupID;
    private String i_ShopID;
    private String c_BillStatus;
    private String c_ShopName;
    private String c_BillNO;
    private String c_BillType;
    private String t_Time;
    private String n_PayCash;
    private String n_PayCard;
    private String n_PayBank;
    private String n_PayIntegral;
    private String n_PayTicket;
    private String n_PayThird;
    private String n_PayOther;
    private String n_PayShould;
    private String n_PayActual;
    private String n_GetIntegral;
    private String c_CardNO;
    private String c_Name;
    private String c_Remark;
    private String c_UserName;
    private String i_MemberID;
    private String i_UserID;
    private String ROW_NUMBER;
    private ReadCardInfo.ResultDataBean member;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public ReadCardInfo.ResultDataBean getMember() {
        return member;
    }

    public void setMember(ReadCardInfo.ResultDataBean member) {
        this.member = member;
    }

    /**
     * id : 68
     * i_GroupID : 1
     * i_ShopID : 1
     * i_BillID : 49
     * c_BillStatus : 未结账
     * c_ShopName : 总店
     * c_BillNO : SY161101154840000052
     * i_GoodsID : 26
     * c_GoodsNO : P00026
     * c_GoodsName : 测试商品2
     * n_PriceRetail : 20.00
     * n_Number : 1.00
     * n_IntegralValueMember : 100.00
     * n_DiscountValueMember : 100.00
     * n_IntegralValueGoods : .00
     * n_DiscountValueGoods : .00
     * n_PayActual : 20.00
     * n_GetIntegral : .00
     * c_CardNO :
     * c_Name :
     * i_MemberID : 0
     * ROW_NUMBER : 1
     */



    private List<DetailBean> detail;

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

    public String getC_BillType() {
        return c_BillType;
    }

    public void setC_BillType(String c_BillType) {
        this.c_BillType = c_BillType;
    }

    public String getT_Time() {
        return t_Time;
    }

    public void setT_Time(String t_Time) {
        this.t_Time = t_Time;
    }

    public String getN_PayCash() {
        return n_PayCash;
    }

    public void setN_PayCash(String n_PayCash) {
        this.n_PayCash = n_PayCash;
    }

    public String getN_PayCard() {
        return n_PayCard;
    }

    public void setN_PayCard(String n_PayCard) {
        this.n_PayCard = n_PayCard;
    }

    public String getN_PayBank() {
        return n_PayBank;
    }

    public void setN_PayBank(String n_PayBank) {
        this.n_PayBank = n_PayBank;
    }

    public String getN_PayIntegral() {
        return n_PayIntegral;
    }

    public void setN_PayIntegral(String n_PayIntegral) {
        this.n_PayIntegral = n_PayIntegral;
    }

    public String getN_PayTicket() {
        return n_PayTicket;
    }

    public void setN_PayTicket(String n_PayTicket) {
        this.n_PayTicket = n_PayTicket;
    }

    public String getN_PayThird() {
        return n_PayThird;
    }

    public void setN_PayThird(String n_PayThird) {
        this.n_PayThird = n_PayThird;
    }

    public String getN_PayOther() {
        return n_PayOther;
    }

    public void setN_PayOther(String n_PayOther) {
        this.n_PayOther = n_PayOther;
    }

    public String getN_PayShould() {
        return n_PayShould;
    }

    public void setN_PayShould(String n_PayShould) {
        this.n_PayShould = n_PayShould;
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

    public String getC_Remark() {
        return c_Remark;
    }

    public void setC_Remark(String c_Remark) {
        this.c_Remark = c_Remark;
    }

    public String getC_UserName() {
        return c_UserName;
    }

    public void setC_UserName(String c_UserName) {
        this.c_UserName = c_UserName;
    }

    public String getI_MemberID() {
        return i_MemberID;
    }

    public void setI_MemberID(String i_MemberID) {
        this.i_MemberID = i_MemberID;
    }

    public String getI_UserID() {
        return i_UserID;
    }

    public void setI_UserID(String i_UserID) {
        this.i_UserID = i_UserID;
    }

    public String getROW_NUMBER() {
        return ROW_NUMBER;
    }

    public void setROW_NUMBER(String ROW_NUMBER) {
        this.ROW_NUMBER = ROW_NUMBER;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }
}
