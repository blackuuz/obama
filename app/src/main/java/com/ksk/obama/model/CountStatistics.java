package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CountStatistics {

    /**
     * result_code : SUCCESS
     * result_msg : 查询成功
     * result_count : 2
     * result_sumPayActual : 0
     * result_sumPayShould : 0
     * result_data : [{"id":"2459","c_BillType":"快速扣次","c_CardNO":"1014","n_PayActual":0,"n_GetIntegral":"-0","n_PayShould":0,"c_Remark":"项目【辣椒】，原来【9】次，扣除【2】次，剩余【7】次","ROW_NUMBER":"1","goods":"辣椒","times":"2"},{"id":"2456","c_BillType":"快速扣次","c_CardNO":"1014","n_PayActual":0,"n_GetIntegral":"-0","n_PayShould":0,"c_Remark":"项目【辣椒】，原来【10】次，扣除【1】次，剩余【9】次","ROW_NUMBER":"2","goods":"辣椒","times":"1"}]
     */

    private String result_stadus;
    private String result_errmsg;
    private String result_count;
    private String result_sumPayActual;
    private String result_sumPayShould;
    /**
     * id : 2459
     * c_BillType : 快速扣次
     * c_CardNO : 1014
     * n_PayActual : 0
     * n_GetIntegral : -0
     * n_PayShould : 0
     * c_Remark : 项目【辣椒】，原来【9】次，扣除【2】次，剩余【7】次
     * ROW_NUMBER : 1
     * goods : 辣椒
     * times : 2
     */

    private List<ResultDataBean> result_data;

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

    public String getResult_count() {
        return result_count;
    }

    public void setResult_count(String result_count) {
        this.result_count = result_count;
    }

    public String getResult_sumPayActual() {
        return result_sumPayActual;
    }

    public void setResult_sumPayActual(String result_sumPayActual) {
        this.result_sumPayActual = result_sumPayActual;
    }

    public String getResult_sumPayShould() {
        return result_sumPayShould;
    }

    public void setResult_sumPayShould(String result_sumPayShould) {
        this.result_sumPayShould = result_sumPayShould;
    }

    public List<ResultDataBean> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ResultDataBean> result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        private String id;
        private String c_BillType;
        private String c_CardNO;
        private String n_PayActual;
        private String n_GetIntegral;
        private String n_PayShould;
        private String c_Remark;
        private String ROW_NUMBER;
        private String goods;
        private String times;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_BillType() {
            return c_BillType;
        }

        public void setC_BillType(String c_BillType) {
            this.c_BillType = c_BillType;
        }

        public String getC_CardNO() {
            return c_CardNO;
        }

        public void setC_CardNO(String c_CardNO) {
            this.c_CardNO = c_CardNO;
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

        public String getN_PayShould() {
            return n_PayShould;
        }

        public void setN_PayShould(String n_PayShould) {
            this.n_PayShould = n_PayShould;
        }

        public String getC_Remark() {
            return c_Remark;
        }

        public void setC_Remark(String c_Remark) {
            this.c_Remark = c_Remark;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

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
