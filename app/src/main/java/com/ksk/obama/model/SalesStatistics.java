package com.ksk.obama.model;

import java.util.List;

/**
 * Created by djy on 2017/3/21.
 */

public class SalesStatistics {


    /**
     * result_stadus : SUCCESS
     * result_errmsg : 操作成功
     * result_data : [{"c_GoodsName":"Gtest（积分打折）","AllNum":"21.00","Allmoney":"210.0000","ROW_NUMBER":"1"},{"c_GoodsName":"G积分兑换","AllNum":"5.00","Allmoney":"50.0000","ROW_NUMBER":"2"},{"c_GoodsName":"G仅打折","AllNum":"17.00","Allmoney":"170.0000","ROW_NUMBER":"3"},{"c_GoodsName":"G仅积分","AllNum":"20.00","Allmoney":"200.0000","ROW_NUMBER":"4"},{"c_GoodsName":"采摘","AllNum":"1.00","Allmoney":"100.0000","ROW_NUMBER":"5"}]
     * result_data2 : {"typecount":"33","typemoney":"1325.69","typeshouldmoney":"1338.69","ROW_NUMBER":"1"}
     */

    private String result_stadus;
    private String result_errmsg;
    private ResultData2Bean result_data2;
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

    public ResultData2Bean getResult_data2() {
        return result_data2;
    }

    public void setResult_data2(ResultData2Bean result_data2) {
        this.result_data2 = result_data2;
    }

    public List<ResultDataBean> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ResultDataBean> result_data) {
        this.result_data = result_data;
    }

    public static class ResultData2Bean {
        /**
         * typecount : 33
         * typemoney : 1325.69
         * typeshouldmoney : 1338.69
         * ROW_NUMBER : 1
         */

        private String typecount;
        private String typemoney;
        private String typenum;
        private String ROW_NUMBER;

        public String getTypecount() {
            return typecount;
        }

        public void setTypecount(String typecount) {
            this.typecount = typecount;
        }

        public String getTypemoney() {
            return typemoney;
        }

        public void setTypemoney(String typemoney) {
            this.typemoney = typemoney;
        }

        public String getTypenum() {
            return typenum;
        }

        public void setTypenum(String typenum) {
            this.typenum = typenum;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }

    public static class ResultDataBean {
        /**
         * c_GoodsName : Gtest（积分打折）
         * AllNum : 21.00
         * Allmoney : 210.0000
         * ROW_NUMBER : 1
         */

        private String c_GoodsName;
        private String AllNum;
        private String Allmoney;
        private String ROW_NUMBER;

        public String getC_GoodsName() {
            return c_GoodsName;
        }

        public void setC_GoodsName(String c_GoodsName) {
            this.c_GoodsName = c_GoodsName;
        }

        public String getAllNum() {
            return AllNum;
        }

        public void setAllNum(String AllNum) {
            this.AllNum = AllNum;
        }

        public String getAllmoney() {
            return Allmoney;
        }

        public void setAllmoney(String Allmoney) {
            this.Allmoney = Allmoney;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
