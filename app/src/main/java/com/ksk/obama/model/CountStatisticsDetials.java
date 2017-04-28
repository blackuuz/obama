package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CountStatisticsDetials {

    /**
     * pay_way :
     * result_msg : success
     * c_CardNO : 1014
     * c_Name : part
     * t_Time : 2016-10-14 08:42:51.000
     * c_BillNO : KT16101408425121922
     * c_UserName : 管理员
     * c_ShopName : 总店
     * times_Detail : [{"goods":null,"times":null}]
     * c_EquipmentNum : YP610000021922
     */

    private String pay_way;
    private String result_stadus;
    private String result_errmsg;
    private String c_CardNO;
    private String c_Name;
    private String t_Time;
    private String c_BillNO;
    private String c_UserName;
    private String c_ShopName;
    private String c_EquipmentNum;
    /**
     * goods : null
     * times : null
     */

    private List<TimesDetailBean> times_Detail;

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
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

    public List<TimesDetailBean> getTimes_Detail() {
        return times_Detail;
    }

    public void setTimes_Detail(List<TimesDetailBean> times_Detail) {
        this.times_Detail = times_Detail;
    }

    public static class TimesDetailBean {
        private String goods;
        private String times;

        public String getGoods() {
            return goods;
        }

        public void setGoods(String goods) {
            this.goods = goods;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }
    }
}
