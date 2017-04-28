package com.ksk.obama.model;

import java.util.List;

/**
 * Created by djy on 2017/3/17.
 */

public class AddPerson {

    /**
     * result_stadus : SUCCESS
     * result_errmsg : 查询成功
     * result_data1 : [{"id":"1","c_Name":"G","ROW_NUMBER":"1"},{"id":"2","c_Name":"A","ROW_NUMBER":"2"},{"id":"3","c_Name":"AS","ROW_NUMBER":"3"},{"id":"5","c_Name":"456","ROW_NUMBER":"4"}]
     * result_data2 : [{"id":"1","c_ShopName":"总店","ROW_NUMBER":"1"},{"id":"2","c_ShopName":"一店","ROW_NUMBER":"2"},{"id":"5","c_ShopName":"总店","ROW_NUMBER":"3"},{"id":"11","c_ShopName":"测试店","ROW_NUMBER":"4"},{"id":"12","c_ShopName":"可视卡商城","ROW_NUMBER":"5"},{"id":"14","c_ShopName":"GStore","ROW_NUMBER":"6"},{"id":"15","c_ShopName":"gytest","ROW_NUMBER":"7"},{"id":"29","c_ShopName":"GStore","ROW_NUMBER":"8"},{"id":"45","c_ShopName":"沈河分店","ROW_NUMBER":"9"},{"id":"47","c_ShopName":"TStore","ROW_NUMBER":"10"},{"id":"49","c_ShopName":"客户试用","ROW_NUMBER":"11"},{"id":"50","c_ShopName":"中记客家餐饮","ROW_NUMBER":"12"},{"id":"52","c_ShopName":"欧莱雅","ROW_NUMBER":"13"},{"id":"53","c_ShopName":"宝宝","ROW_NUMBER":"14"},{"id":"54","c_ShopName":"CVBS","ROW_NUMBER":"15"},{"id":"59","c_ShopName":"sss","ROW_NUMBER":"16"},{"id":"60","c_ShopName":"总店","ROW_NUMBER":"17"},{"id":"61","c_ShopName":"22","ROW_NUMBER":"18"},{"id":"62","c_ShopName":"哈哈哈","ROW_NUMBER":"19"},{"id":"63","c_ShopName":"RStore","ROW_NUMBER":"20"},{"id":"64","c_ShopName":"孔德林百货超市","ROW_NUMBER":"21"},{"id":"65","c_ShopName":"北京智卡","ROW_NUMBER":"22"},{"id":"67","c_ShopName":"聚美车坊","ROW_NUMBER":"23"},{"id":"68","c_ShopName":"NEW AGE美的手工坊","ROW_NUMBER":"24"},{"id":"69","c_ShopName":"展会测试","ROW_NUMBER":"25"},{"id":"70","c_ShopName":"一品禄猪蹄","ROW_NUMBER":"26"}]
     */

    private String result_stadus;
    private String result_errmsg;
    private List<ResultData1Bean> result_data1;
    private List<ResultData2Bean> result_data2;

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

    public List<ResultData1Bean> getResult_data1() {
        return result_data1;
    }

    public void setResult_data1(List<ResultData1Bean> result_data1) {
        this.result_data1 = result_data1;
    }

    public List<ResultData2Bean> getResult_data2() {
        return result_data2;
    }

    public void setResult_data2(List<ResultData2Bean> result_data2) {
        this.result_data2 = result_data2;
    }

    public static class ResultData1Bean {
        /**
         * id : 1
         * c_Name : G
         *
         * ROW_NUMBER : 1
         */

        private String id;
        private String c_Name;
        private String c_ShopName;
        private String ROW_NUMBER;

        public String getC_ShopName() {
            return c_ShopName;
        }

        public void setC_ShopName(String c_ShopName) {
            this.c_ShopName = c_ShopName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_Name() {
            return c_Name;
        }

        public void setC_Name(String c_Name) {
            this.c_Name = c_Name;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }

    public static class ResultData2Bean {
        /**
         * id : 1
         * c_ShopName : 总店
         * ROW_NUMBER : 1
         */

        private String id;
        private String c_ShopName;
        private String ROW_NUMBER;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_ShopName() {
            return c_ShopName;
        }

        public void setC_ShopName(String c_ShopName) {
            this.c_ShopName = c_ShopName;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
