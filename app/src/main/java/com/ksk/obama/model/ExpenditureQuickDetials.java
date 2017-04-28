package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/10/13.
 */

public class ExpenditureQuickDetials {
    /**
     * result_msg : success
     * c_CardNO : 123
     * c_Name : 123
     * t_Time : 123
     * c_BillNO : 160829163055903
     * n_PayActual : 1.00
     * c_UserName : 管理员
     * c_ShopName : 总店
     * c_EquipmentNum : 无
     */

    private String result_msg;
    private String c_CardNO;
    private String c_Name;
    private String t_Time;
    private String c_BillNO;
    private String pay_way;
    private String n_PayActual;
    private String c_UserName;
    private String c_ShopName;
    private String n_GetIntegral;
    private String c_EquipmentNum;

    public String getN_GetIntegral() {
        return n_GetIntegral;
    }

    public void setN_GetIntegral(String n_GetIntegral) {
        this.n_GetIntegral = n_GetIntegral;
    }

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

    public String getC_EquipmentNum() {
        return c_EquipmentNum;
    }

    public void setC_EquipmentNum(String c_EquipmentNum) {
        this.c_EquipmentNum = c_EquipmentNum;
    }
}
