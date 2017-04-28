package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/10/12.
 */

public class ExitCardStatisticsDetials {


    /**
     * result_msg : success
     * t_CreateTime : false
     * c_ClassName : null
     * n_IntegralAvailable : null
     * c_BillNO : TK161108094536121026
     * c_CardNO : 12306
     * c_Name : 468
     * n_PayShould : -700.00
     * n_PayActual : -700.00
     * c_UserName : 管理员1
     * c_ShopName : 总店
     * MemberId : null
     * pay_way : 现金支付
     * c_EquipmentNum : 无
     */

    private String result_msg;
    private boolean t_CreateTime;
    private Object c_ClassName;
    private Object n_IntegralAvailable;
    private String c_BillNO;
    private String c_CardNO;
    private String c_Name;
    private String n_PayShould;
    private float n_PayActual;
    private String c_UserName;
    private String c_ShopName;
    private Object MemberId;
    private String pay_way;
    private String t_Time;
    private String c_EquipmentNum;

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getT_Time() {
        return t_Time;
    }

    public void setT_Time(String t_Time) {
        this.t_Time = t_Time;
    }

    public boolean isT_CreateTime() {
        return t_CreateTime;
    }

    public void setT_CreateTime(boolean t_CreateTime) {
        this.t_CreateTime = t_CreateTime;
    }

    public Object getC_ClassName() {
        return c_ClassName;
    }

    public void setC_ClassName(Object c_ClassName) {
        this.c_ClassName = c_ClassName;
    }

    public Object getN_IntegralAvailable() {
        return n_IntegralAvailable;
    }

    public void setN_IntegralAvailable(Object n_IntegralAvailable) {
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

    public float getN_PayActual() {
        return n_PayActual;
    }

    public void setN_PayActual(float n_PayActual) {
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

    public Object getMemberId() {
        return MemberId;
    }

    public void setMemberId(Object MemberId) {
        this.MemberId = MemberId;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getC_EquipmentNum() {
        return c_EquipmentNum;
    }

    public void setC_EquipmentNum(String c_EquipmentNum) {
        this.c_EquipmentNum = c_EquipmentNum;
    }
}
