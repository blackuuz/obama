package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/10/11.
 */

public class OpenCardStatisticsDetatils {

    /**
     * result_msg : success
     * t_CreateTime : 2016-10-11 14:05:43
     * c_ClassName : 带初始金额和次数的卡
     * n_IntegralAvailable : .00
     * c_BillNO : KK16101114054303764
     * c_CardNO : 3695
     * c_Name : 369
     * n_PayShould : 1000.00
     * c_UserName : 管理员
     * c_ShopName : 总店
     * c_EquipmentNum : YP620000003764
     */

    private String result_msg;
    private String t_CreateTime;
    private String c_ClassName;
    private String n_IntegralAvailable;
    private String c_BillNO;
    private String c_CardNO;
    private String pay_way;
    private String c_Name;
    private String n_PayShould;
    private String c_UserName;
    private String c_ShopName;
    private String c_EquipmentNum;
    private String n_PayActual;
    private String n_InitAmount;
    private String n_InitIntegral;

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getN_PayActual() {
        return n_PayActual;
    }

    public void setN_PayActual(String n_PayActual) {
        this.n_PayActual = n_PayActual;
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

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getT_CreateTime() {
        return t_CreateTime;
    }

    public void setT_CreateTime(String t_CreateTime) {
        this.t_CreateTime = t_CreateTime;
    }

    public String getC_ClassName() {
        return c_ClassName;
    }

    public void setC_ClassName(String c_ClassName) {
        this.c_ClassName = c_ClassName;
    }

    public String getN_IntegralAvailable() {
        return n_IntegralAvailable;
    }

    public void setN_IntegralAvailable(String n_IntegralAvailable) {
        this.n_IntegralAvailable = n_IntegralAvailable;
    }

    public String getC_BillNO() {
        return c_BillNO;
    }

    public void setC_BillNO(String c_BillNO) {
        this.c_BillNO = c_BillNO;
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

    public String getN_PayShould() {
        return n_PayShould;
    }

    public void setN_PayShould(String n_PayShould) {
        this.n_PayShould = n_PayShould;
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

    public String getC_EquipmentNum() {
        return c_EquipmentNum;
    }

    public void setC_EquipmentNum(String c_EquipmentNum) {
        this.c_EquipmentNum = c_EquipmentNum;
    }
}
