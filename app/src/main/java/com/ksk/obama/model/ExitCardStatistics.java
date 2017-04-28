package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class ExitCardStatistics {

    /**
     * result_code : SUCCESS
     * result_msg : 查询成功
     * result_count : 1
     * result_sumPayActual : -700
     * result_sumPayShould : -700
     * result_data : [{"id":"4044","c_BillType":"退卡退费","c_CardNO":"12306","c_Name":"468","n_PayActual":-700,"n_GetIntegral":"-0","n_PayShould":-700,"c_Remark":"退款金额：【700】，支付方式：【现金】","ROW_NUMBER":"1"}]
     */

    private String result_stadus;
    private String result_errmsg;
    private int result_count;
    private float result_sumPayActual;
    private float result_sumPayShould;
    /**
     * id : 4044
     * c_BillType : 退卡退费
     * c_CardNO : 12306
     * c_Name : 468
     * n_PayActual : -700
     * n_GetIntegral : -0
     * n_PayShould : -700
     * c_Remark : 退款金额：【700】，支付方式：【现金】
     * ROW_NUMBER : 1
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

    public int getResult_count() {
        return result_count;
    }

    public void setResult_count(int result_count) {
        this.result_count = result_count;
    }

    public float getResult_sumPayActual() {
        return result_sumPayActual;
    }

    public void setResult_sumPayActual(float result_sumPayActual) {
        this.result_sumPayActual = result_sumPayActual;
    }

    public float getResult_sumPayShould() {
        return result_sumPayShould;
    }

    public void setResult_sumPayShould(float result_sumPayShould) {
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
        private String c_Name;
        private float n_PayActual;
        private String n_GetIntegral;
        private float n_PayShould;
        private String c_Remark;
        private String ROW_NUMBER;

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

        public String getC_Name() {
            return c_Name;
        }

        public void setC_Name(String c_Name) {
            this.c_Name = c_Name;
        }

        public float getN_PayActual() {
            return n_PayActual;
        }

        public void setN_PayActual(float n_PayActual) {
            this.n_PayActual = n_PayActual;
        }

        public String getN_GetIntegral() {
            return n_GetIntegral;
        }

        public void setN_GetIntegral(String n_GetIntegral) {
            this.n_GetIntegral = n_GetIntegral;
        }

        public float getN_PayShould() {
            return n_PayShould;
        }

        public void setN_PayShould(float n_PayShould) {
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
    }
}
