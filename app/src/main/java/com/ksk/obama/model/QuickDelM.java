package com.ksk.obama.model;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/4 15:33
 * 修改人：Administrator
 * 修改时间：2017/7/4 15:33
 * 修改备注：
 */
public class QuickDelM {
    /**
     * result_stadus : SUCCESS
     * defaultcost : 5
     * fast_state : 1
     * fast_list : [{"c_Value":"11","ROW_NUMBER":"1"},{"c_Value":"22","ROW_NUMBER":"2"},{"c_Value":"33","ROW_NUMBER":"3"},{"c_Value":"44","ROW_NUMBER":"4"},{"c_Value":"0","ROW_NUMBER":"5"}]
     */

    private String result_stadus;
    private int defaultcost;
    private String fast_state;
    private List<FastListBean> fast_list;

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public int getDefaultcost() {
        return defaultcost;
    }

    public void setDefaultcost(int defaultcost) {
        this.defaultcost = defaultcost;
    }

    public String getFast_state() {
        return fast_state;
    }

    public void setFast_state(String fast_state) {
        this.fast_state = fast_state;
    }

    public List<FastListBean> getFast_list() {
        return fast_list;
    }

    public void setFast_list(List<FastListBean> fast_list) {
        this.fast_list = fast_list;
    }

    public static class FastListBean {
        /**
         * c_Value : 11
         * ROW_NUMBER : 1
         */

        private String c_Value;
        private String ROW_NUMBER;

        public String getC_Value() {
            return c_Value;
        }

        public void setC_Value(String c_Value) {
            this.c_Value = c_Value;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }
    }
}
