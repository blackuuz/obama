package com.ksk.obama.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class OPenCardStatistics {
    /**
     * result_code : SUCCESS
     * result_msg : 查询成功
     * result_count : 10
     * result_sumPayActual : 12000.00
     * result_sumPayShould : 2000.00
     * result_data : [{"id":"617","c_CardNO":"1090","n_PayActual":"2000.00","n_PayShould":null,"ROW_NUMBER":"1"},{"id":"618","c_CardNO":"888","n_PayActual":"2000.00","n_PayShould":null,"ROW_NUMBER":"2"},{"id":"623","c_CardNO":"1091","n_PayActual":"2000.00","n_PayShould":null,"ROW_NUMBER":"3"},{"id":"643","c_CardNO":"5607","n_PayActual":".00","n_PayShould":".00","ROW_NUMBER":"4"},{"id":"659","c_CardNO":"26589","n_PayActual":"1000.00","n_PayShould":null,"ROW_NUMBER":"5"},{"id":"660","c_CardNO":"2345","n_PayActual":"1000.00","n_PayShould":null,"ROW_NUMBER":"6"},{"id":"661","c_CardNO":"36996","n_PayActual":"1000.00","n_PayShould":null,"ROW_NUMBER":"7"},{"id":"672","c_CardNO":"123","n_PayActual":"1000.00","n_PayShould":"1000.00","ROW_NUMBER":"8"},{"id":"673","c_CardNO":"45678","n_PayActual":"1000.00","n_PayShould":null,"ROW_NUMBER":"9"},{"id":"675","c_CardNO":"7890","n_PayActual":"1000.00","n_PayShould":"1000.00","ROW_NUMBER":"10"}]
     */

    private String result_stadus;
    private String result_errmsg;
    private int result_count;
    private String result_sumPayActual;
    private String result_sumPayShould;
    /**
     * id : 617
     * c_CardNO : 1090
     * n_PayActual : 2000.00
     * n_PayShould : null
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
        private String c_CardNO;
        private String n_PayActual;
        private String n_PayShould;
        private String c_BillType;
        private String ROW_NUMBER;

        public String getC_BillType() {
            return c_BillType;
        }

        public void setC_BillType(String c_BillType) {
            this.c_BillType = c_BillType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getN_PayShould() {
            return n_PayShould;
        }

        public void setN_PayShould(String n_PayShould) {
            this.n_PayShould = n_PayShould;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }

}
