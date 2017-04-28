package com.ksk.obama.model;

import java.util.List;

/**
 * Created by djy on 2017/3/22.
 */

public class SalesDetials {

    /**
     * result_stadus : SUCCESS
     * result_errmsg : 操作成功
     * result_data : [{"t_Time":"2017-03-01 15:30:27.540","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"1"},{"t_Time":"2017-03-02 10:31:55.023","n_Number":"1.34","n_PriceRetail":"10.00","ROW_NUMBER":"2"},{"t_Time":"2017-03-02 11:17:48.370","n_Number":"1.34","n_PriceRetail":"10.00","ROW_NUMBER":"3"},{"t_Time":"2017-03-02 13:30:13.660","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"4"},{"t_Time":"2017-03-02 14:08:05.867","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"5"},{"t_Time":"2017-03-02 14:12:39.737","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"6"},{"t_Time":"2017-03-03 09:25:09.493","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"7"},{"t_Time":"2017-03-03 15:29:39.860","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"8"},{"t_Time":"2017-03-03 15:34:32.937","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"9"},{"t_Time":"2017-03-06 15:17:57.800","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"10"},{"t_Time":"2017-03-07 11:25:21.197","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"11"},{"t_Time":"2017-03-07 14:43:29.880","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"12"},{"t_Time":"2017-03-07 16:08:35.537","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"13"},{"t_Time":"2017-03-07 16:15:51.757","n_Number":"2.00","n_PriceRetail":"10.00","ROW_NUMBER":"14"},{"t_Time":"2017-03-08 15:53:21.817","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"15"},{"t_Time":"2017-03-08 15:56:03.320","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"16"},{"t_Time":"2017-03-09 11:07:02.290","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"17"},{"t_Time":"2017-03-10 15:10:59.997","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"18"},{"t_Time":"2017-03-10 15:20:57.520","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"19"},{"t_Time":"2017-03-10 15:28:41.567","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"20"},{"t_Time":"2017-03-13 15:51:38.970","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"21"},{"t_Time":"2017-03-16 16:40:48.147","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"22"},{"t_Time":"2017-03-16 16:41:45.427","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"23"},{"t_Time":"2017-03-16 16:43:32.800","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"24"},{"t_Time":"2017-03-17 15:51:02.323","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"25"},{"t_Time":"2017-03-17 15:54:51.770","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"26"},{"t_Time":"2017-03-17 15:58:12.240","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"27"},{"t_Time":"2017-03-17 16:07:29.627","n_Number":"2.00","n_PriceRetail":"10.00","ROW_NUMBER":"28"},{"t_Time":"2017-03-17 16:50:08.763","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"29"},{"t_Time":"2017-03-20 16:41:34.210","n_Number":"10.00","n_PriceRetail":"10.00","ROW_NUMBER":"30"},{"t_Time":"2017-03-21 14:11:26.397","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"31"},{"t_Time":"2017-03-21 14:20:29.103","n_Number":"1.00","n_PriceRetail":"10.00","ROW_NUMBER":"32"}]
     */

    private String result_stadus;
    private String result_errmsg;
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

    public List<ResultDataBean> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ResultDataBean> result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        /**
         * t_Time : 2017-03-01 15:30:27.540
         * n_Number : 1.00
         * n_PriceRetail : 10.00
         * ROW_NUMBER : 1
         */

        private String t_Time;
        private String n_Number;
        private String n_PriceRetail;
        private String Allmoney;
        private String ROW_NUMBER;

        public String getAllmoney() {
            return Allmoney;
        }

        public void setAllmoney(String allmoney) {
            Allmoney = allmoney;
        }

        public String getT_Time() {
            return t_Time;
        }

        public void setT_Time(String t_Time) {
            this.t_Time = t_Time;
        }

        public String getN_Number() {
            return n_Number;
        }

        public void setN_Number(String n_Number) {
            this.n_Number = n_Number;
        }

        public String getN_PriceRetail() {
            return n_PriceRetail;
        }

        public void setN_PriceRetail(String n_PriceRetail) {
            this.n_PriceRetail = n_PriceRetail;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
